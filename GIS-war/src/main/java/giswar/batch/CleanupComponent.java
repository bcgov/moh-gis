/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        CleanupComponent.java                          *
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

import giswar.batch.util.ILogHelper;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adebiyi.kuseju
 */
public class CleanupComponent implements IBatchComponent {

    private static final Logger logger = Logger.getLogger(CleanupComponent.class.getName());
    private IBatchContext context;
    private ILogHelper logHelper;

    public void init(IBatchContext context) {
        this.context = context;
        logHelper = (ILogHelper) context.getProperty(BatchConstants.LOG_HELPER);
    }

    public boolean execute() throws BatchException {

        logger.log(Level.FINEST, "Executing: %1$s", CleanupComponent.class.getName());

        boolean success = false;
        logHelper.log("INFO", "Cleaning up .....");

        try {
            File file = new File((String) context.getProperty(BatchConstants.FILEPATH));
            logger.log(Level.INFO, String.format("Deleting: %1$s", file.getAbsolutePath()));
            deleteFile(file);

            file = new File((String) context.getProperty(BatchConstants.HARS_FILE));
            logger.log(Level.INFO, String.format("Deleting: %1$s", file.getAbsolutePath()));
            deleteFile(file);

            file = new File((String) context.getProperty(BatchConstants.ATTACHMENT));
            logger.log(Level.INFO, String.format("Deleting: %1$s", file.getAbsolutePath()));
            deleteFile(file);


            // Remove Fed file from sFTP server
            logHelper.log("INFO",  String.format("Deleting: %1$s", file.getAbsolutePath()));
            IData ds = (IData) context.getProperty(BatchConstants.FED_FILE_DATASOURCE);
            ((SFTPDatasource) ds).deleteFiles((String) context.getProperty(BatchConstants.REMOTE_FILE_PATH));


            logHelper.log("INFO",  "GIS Data load process completed succesfully");
            logger.log(Level.INFO, "GIS Data load process completed succesfully");
            success = true;
        } catch (Exception e) {
            throw new BatchException(e);
        }

        return success;
    }

    public void destroy() {
    }
    private void deleteFile(File file) {

        if (file != null) {
            file.delete();
        }
    }
}
