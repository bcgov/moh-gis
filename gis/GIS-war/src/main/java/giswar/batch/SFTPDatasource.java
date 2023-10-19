/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        SFTPDatasource.java                            *
 * Date of Last Commit: $Date::                                              $ *
 * Revision Number:      $Rev::                                              $ *
 * Last Commit by:    $Author::                                              $ *
 *                                                                             *
 *******************************************************************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package giswar.batch;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
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
import java.util.Vector;
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

    @Override
    public Properties getData(Properties props) throws Exception {

        Session session = null;
        ChannelSftp client = null;
        File tempFile = null;
        InputStream handle = null;
        FileOutputStream fos = null;
        Properties response = null;

        try {
            session = getSftpSession();
            client = getSftpChannel(session);

            File remoteFile = new File(props.getProperty(BatchConstants.REMOTE_FILE_PATH));
            tempFile = Paths.get(System.getProperty("java.io.tmpdir"), remoteFile.getName()).toFile();
            remoteFile = null;

            fos = new FileOutputStream(tempFile);

            try {
                handle = client.get(props.getProperty(BatchConstants.REMOTE_FILE_PATH));
                logger.log(Level.INFO, "New file found...");
            } catch (Exception sfe) {

                fos.close();
                fos = null;

                tempFile.delete();
                releaseResources(client, session);

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

            releaseResources(client, session);
        }

        response = new Properties();
        response.put(BatchConstants.FILEPATH, tempFile.getAbsolutePath());

        return response;
    }

    @Override
    public void uploadData(Properties props) throws Exception {

        File tempFile = new File(props.getProperty(BatchConstants.FILEPATH));
        String uploadLocation = props.getProperty(BatchConstants.UPLOAD_LOCATION);

        FileInputStream fis = null;
        Session session = null;
        ChannelSftp client = null;
        OutputStream handle = null;

        try {
            session = getSftpSession();
            client = getSftpChannel(session);

            byte[] buffer = new byte[getBufferSize()];
            int read = 0;

            fis = new FileInputStream(tempFile);

            logger.log(Level.INFO, String.format("Uploading file %1$s to %2$s ...", tempFile.getAbsolutePath(), uploadLocation));
            handle = client.put(uploadLocation + tempFile.getName());
            int totalRead = 0;

            while ((read = fis.read(buffer, 0, buffer.length)) != -1) {
                handle.write(buffer, 0, read);
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

            releaseResources(client, session);

        }

        logger.log(Level.INFO, String.format("File %1$s upload to %2$s completed.", tempFile.getAbsolutePath(), uploadLocation));
    }

    public List<String> filesInDir(String dirPath) throws Exception {
        List<String> files = new ArrayList<>();

        Session session = null;
        ChannelSftp client = null;
        try {
            session = getSftpSession();
            client = getSftpChannel(session);

            Vector<LsEntry> v = client.ls(dirPath);
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
            releaseResources(client, session);
        }
        return files;
    }

    public void deleteFiles(String... files) throws Exception {

        Session session = null;
        ChannelSftp client = null;
        try {
            session = getSftpSession();
            client = getSftpChannel(session);

            for (String file : files) {
                logger.log(Level.INFO, String.format("Deleting %1$s file ...", file));
                client.rm(file);
            }
        } finally {
            releaseResources(client, session);
        }
    }

    private Session getSftpSession() throws Exception {

        JSch jsch = new JSch();
        jsch.addIdentity(privateKeyPath);
        jsch.setKnownHosts(knownHostsFile);
        Session session;
        try {
            session = jsch.getSession(user, host);
            // MoH SFTP server still proposes using ssh-rsa
            session.setConfig("server_host_key", session.getConfig("server_host_key") + ",ssh-rsa");
            session.setConfig("PubkeyAcceptedAlgorithms", session.getConfig("PubkeyAcceptedAlgorithms") + ",ssh-rsa");
            session.connect();
        } catch (JSchException sfe) {

            logger.log(Level.SEVERE, "Connection problem with remote SFTP host", sfe);
            return null;  // Return null if file cannot be found
        }
        boolean isAuthenticated = session.isConnected();
        if (!isAuthenticated) {
            throw new IOException("Authentication failed.");
        }

        return session;
    }

    private ChannelSftp getSftpChannel(Session session) throws Exception {

        Channel channel = null;
        try {
            channel = session.openChannel("sftp");
            channel.connect();
        } catch (JSchException sfe) {
            logger.log(Level.SEVERE, "Connection problem with remote SFTP session", sfe);
        }
        return (ChannelSftp) channel;
    }

    private void releaseResources(ChannelSftp client, Session session) {

        if (client != null) {
            client.disconnect();
        }

        if (session != null) {
            session.disconnect();
        }
    }

    private int getBufferSize() {
        return clientBufSize > 0 ? clientBufSize : BUFFER_SIZE;
    }
}
