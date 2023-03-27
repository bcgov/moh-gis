/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        ArchiverComponent.java                         *
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

/**
 *
 * @author adebiyi.kuseju
 */
public class ArchiverComponent implements IBatchComponent {

    private static final Logger logger = Logger.getLogger(ArchiverComponent.class.getName());
    private IBatchContext context;

    public void init(IBatchContext context) {
        this.context = context;
    }

    public boolean execute() throws BatchException {

        logger.log(Level.FINEST, "Executing: %1$s", ArchiverComponent.class.getName());

        String archiveFile = null;
        boolean success = false;
        try {

            Properties props = new Properties();
            IData ds = (IData) context.getProperty(BatchConstants.FED_FILE_DATASOURCE);

            archiveFile = makeGz((String) context.getProperty(BatchConstants.FILEPATH));

            props.put(BatchConstants.FILEPATH, archiveFile);
            props.put(BatchConstants.UPLOAD_LOCATION, (String) context.getProperty(BatchConstants.ARCHIVE_FILE_UPLOAD_LOCATION));
            ((SFTPDatasource) ds).uploadData(props);

            File file = new File(archiveFile);
            file.delete();

            logger.log(Level.INFO, String.format("Deleting file %1$s ...", archiveFile));

            success = true;
        } catch (Exception e) {
            throw new BatchException(e);
        }

        return success;
    }

    public void destroy() {
    }

    private String makeGz(String file) throws Exception {

        File fileObj = new File(file);

        StringBuilder archiveFN = new StringBuilder(fileObj.getParent());
        int ndx = fileObj.getName().lastIndexOf(".");

        ndx = ndx > 0 ? ndx : fileObj.getName().length();
        archiveFN.append(File.separator).append(fileObj.getName().substring(0, ndx)).append(".gz");

        FileInputStream fis = null;
        GZIPOutputStream gos = null;
        File archiveFile = null;


        archiveFile = new File(archiveFN.toString());

        fis = new FileInputStream(file);
        gos = new GZIPOutputStream(new FileOutputStream(archiveFile));

        byte[] buffer = new byte[Integer.parseInt((String) context.getProperty(BatchConstants.BUFFER_SIZE))];
        int read = 0;

        logger.log(Level.INFO, String.format("Archiving downloaded FED file %1$s to %2$s", file, archiveFile.getAbsolutePath()));

        while ((read = fis.read(buffer, 0, buffer.length)) != -1) {
            gos.write(buffer, 0, read);

        }

        gos.close();
        fis.close();

        return archiveFile.getAbsolutePath();
    }
}
