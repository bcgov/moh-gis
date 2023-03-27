/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        DataComponent.java                             *
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
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adebiyi.kuseju
 */
public class DataComponent implements IBatchComponent {

    private static final Logger logger = Logger.getLogger(DataComponent.class.getName());
    private IBatchContext context;
    private ILogHelper logHelper;

    public void init(IBatchContext context) {
        this.context = context;
        logHelper = (ILogHelper) context.getProperty(BatchConstants.LOG_HELPER);
    }

    public boolean execute() throws BatchException {

        logger.log(Level.FINEST, "Executing: %1$s", DataComponent.class.getName());

        String downloadFile = null;
        boolean success = false;
        try {
            Properties props = new Properties();
            IData ds = (IData) context.getProperty(BatchConstants.FED_FILE_DATASOURCE);
            List<String> files = ((SFTPDatasource) ds).filesInDir((String) context.getProperty(BatchConstants.REMOTE_FED_FILE_DIR));

            if (files == null || files.isEmpty()) {
                logger.log(Level.SEVERE, "No .txt in remote location");
                logHelper.log("ERROR", "No .txt in remote location");
                return false;
            } else if (files.size() > 1) {
                logger.log(Level.SEVERE, "Multiple .txt files in remote location");
                logHelper.log("ERROR", "Multiple .txt files in remote location");
                return false;
            }

            // add remote file path to context object so that we can remove the file later down the chain
            context.addProperty(BatchConstants.REMOTE_FILE_PATH, context.getProperty(BatchConstants.REMOTE_FED_FILE_DIR) + files.get(0));

            props.put(BatchConstants.REMOTE_FILE_PATH, (String) context.getProperty(BatchConstants.REMOTE_FED_FILE_DIR) + files.get(0));

            logHelper.log("INFO", "Downloading " + props.getProperty(BatchConstants.REMOTE_FILE_PATH) + " ...");
            downloadFile = ds.getData(props).getProperty(BatchConstants.FILEPATH);
            logHelper.log("INFO", "Download completed Successfully");
            
            success = (downloadFile != null);
        } catch (Exception e) {
            throw new BatchException(e);
        }

        context.addProperty(BatchConstants.FILEPATH, downloadFile);
        return success;
    }

    public void destroy() {
    }

    
}
