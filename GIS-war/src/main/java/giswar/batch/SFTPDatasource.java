/** *****************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        SFTPDatasource.java                            *
 * Date of Last Commit: $Date::                                              $ *
 * Revision Number:      $Rev::                                              $ *
 * Last Commit by:    $Author::                                              $ *
 *                                                                             *
 ****************************************************************************** */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package giswar.batch;

import com.ibm.network.sftp.Sftp;
import com.jcraft.jsch.ChannelSftp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adebiyi.kuseju
 */
public class SFTPDatasource implements IData {

  private static final Logger logger = Logger.getLogger(SFTPDatasource.class.getName());
  private final int BUFFER_SIZE = 30000;
  private int clientBufSize;
  private String user;
  private String host;
  private String privateKeyPath;
  private String knownHostsFile;

  public SFTPDatasource(String host, String user, String privateKeyPath, String clientBufSize, String knownHostsFile) {
    this.user = user;
    this.privateKeyPath = privateKeyPath;
    this.host = host;
    this.knownHostsFile = knownHostsFile;

    try {
      this.clientBufSize = Integer.parseInt(clientBufSize);
    } catch (NumberFormatException e) {
      logger.log(Level.WARNING, "byteBuffer has invalid value, using internal setting of " + BUFFER_SIZE, e);
    }
  }

  public Properties getData(Properties props) throws Exception {

    File tempFile = null;
    ChannelSftp client = null;
    InputStream handle = null;
    FileOutputStream fos = null;
    Sftp conn = null;
    Properties response = null;

    try {
      conn = getSFTPClientConnection();
      File remoteFile = new File(props.getProperty(BatchConstants.REMOTE_FILE_PATH));
      tempFile = Paths.get(System.getProperty("java.io.tmpdir"), remoteFile.getName()).toFile();
      remoteFile = null;

      fos = new FileOutputStream(tempFile);
      client = conn.getSftpChannel();
      
      try {
        handle = client.get(props.getProperty(BatchConstants.REMOTE_FILE_PATH));
        logger.log(Level.INFO, "New file found...");
      } catch (Exception sfe) {

        fos.close();
        fos = null;

        tempFile.delete();
        releaseResources(client, conn);

        logger.log(Level.INFO, sfe.getMessage());
        return null;  // Return null if file cannot be found
      }

      byte[] buffer = new byte[getBufferSize()];
      int read = 0;
      long totalRead = 0;

      logger.log(Level.INFO, String.format("Downloading file %1$s from %2$s to %3$s", props.get(BatchConstants.REMOTE_FILE_PATH), host, tempFile.getAbsolutePath()));

      while ((read = handle.read(buffer, 0, buffer.length)) != -1) {
        fos.write(buffer, 0, read);
        totalRead += read;
      }

      logger.log(Level.INFO, "File download complete.");

    } catch (Exception sfe) {

      logger.log(Level.SEVERE, "Connection problem with remote SFTP host", sfe);
      return null;  // Return null if file cannot be found
    } finally {

      if (fos != null) {
        fos.close();
        fos = null;
      }

      if (handle != null) {
        handle.close();
      }

      releaseResources(client, conn);
    }

    response = new Properties();
    response.put(BatchConstants.FILEPATH, tempFile.getAbsolutePath());

    return response;
  }

  public void uploadData(Properties props) throws Exception {

    File tempFile = new File(props.getProperty(BatchConstants.FILEPATH));
    String uploadLocation = props.getProperty(BatchConstants.UPLOAD_LOCATION);

    FileInputStream fis = null;
    ChannelSftp client = null;
    OutputStream handle = null;
    Sftp conn = null;

    try {

      byte[] buffer = new byte[getBufferSize()];
      int read = 0;

      fis = new FileInputStream(tempFile);
      conn = getSFTPClientConnection();
      client = conn.getSftpChannel();

      logger.log(Level.INFO, String.format("Uploading file %1$s to %2$s ...", tempFile.getAbsolutePath(), uploadLocation));
      handle = client.put(uploadLocation + tempFile.getName());
      int totalRead = 0;

      while ((read = fis.read(buffer, 0, buffer.length)) != -1) {
        handle.write(buffer, totalRead, 0);
        //client.write(handle, totalRead, buffer, 0, read);
        totalRead += read;
      }
      handle.close();
      handle = null;
      client.disconnect();
      client = null;

    } finally {

      if (fis != null) {
        fis.close();
        fis = null;
      }

      if (handle != null) {
        handle.close();
      }

      releaseResources(client, conn);

    }

    logger.log(Level.INFO, String.format("File %1$s upload to %2$s completed.", tempFile.getAbsolutePath(), uploadLocation));
  }

  public List<String> filesInDir(String dirPath) throws Exception {
    List<String> files = new ArrayList<String>();

    Sftp conn = null;
    ChannelSftp client = null;
    try {
      conn = getSFTPClientConnection();
      client = conn.getSftpChannel();

      List<String> v = client.ls(dirPath);
      Iterator it = v.iterator();
      ChannelSftp.LsEntry dirEntry = null;
      while (it.hasNext()) {
        dirEntry = (ChannelSftp.LsEntry) it.next();

        if (dirEntry.getFilename().endsWith(".txt")) {
          files.add(dirEntry.getFilename());
        }
      }
      logger.log(Level.INFO, String.format("Found %1$s file(s) in %2$s", files.size(), dirPath));
    } finally {
      releaseResources(client, conn);
    }
    return files;
  }

  public void deleteFiles(String... files) throws Exception {

    ChannelSftp client = null;
    Sftp conn = null;
    try {
      conn = getSFTPClientConnection();
      client = conn.getSftpChannel();

      for (String file : files) {
        logger.log(Level.INFO, String.format("Deleting %1$s file ...", file));
        client.rm(file);
      }
    } finally {
      releaseResources(client, conn);
    }
  }

  private Sftp getSFTPClientConnection() throws Exception {

    Sftp sftp = new Sftp();
    try {
      
    sftp.connectByKey(host, user, privateKeyPath, knownHostsFile);
    } catch (Exception sfe) {

      logger.log(Level.SEVERE, "Connection problem with remote SFTP host", sfe);
      return null;  // Return null if file cannot be found
    }
    boolean isAuthenticated = sftp.isConnected();
    if (!isAuthenticated) {
      throw new IOException("Authentication failed.");
    }

    return sftp;
  }

  private void releaseResources(ChannelSftp client, Sftp conn) {

    if (client != null) {
      client.disconnect();
    }

    if (conn != null) {
      conn.disconnect();
    }
  }

  private int getBufferSize() {
    return clientBufSize > 0 ? clientBufSize : BUFFER_SIZE;
  }
}
