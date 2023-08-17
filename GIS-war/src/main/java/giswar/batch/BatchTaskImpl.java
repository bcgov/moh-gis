/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        BatchTaskImpl.java                             *
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
import giswar.batch.util.LogHelperImpl;
import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author adebiyi.kuseju
 */
public class BatchTaskImpl extends Task implements IBatchTask {

    private static final Logger logger = Logger.getLogger(BatchTaskImpl.class.getName());
    private DataSource dds;
    private Properties properties;


    public void setDataSource(DataSource ds) {
        dds = ds;
    }

    public void setProperties(Properties properties) { this.properties = properties; }

    @Override
    public void run() {
        IBatchContext ctx = null;

        try {
            ctx = createContext();
            ctx.execute();
        } catch (Exception be) {
            logger.log(Level.SEVERE, "Batch Error", be);
            throw new RuntimeException(be);
        } finally {
            ctx.destroy();
            logger.log(Level.INFO, "Batch context object destroyed");
        }


    }
    
    @Override
    public void execute(TaskExecutionContext tc) {
        run();
    }

    private IBatchContext createContext() throws Exception {

        Properties config = properties;

        String host = (String) config.get(BatchConstants.FED_FILE_HOST);
        String user = (String) config.get(BatchConstants.FED_FILE_HOST_USER_ID);
        String privateKeyPath = BatchConstants.KEYFILEPATH;
        String knownHostsFile = BatchConstants.KNOWNHOSTSFILE;
        String bufferSize = (String) config.get(BatchConstants.BUFFER_SIZE);
        int dbBatchSize = Integer.parseInt((String) config.get(BatchConstants.DB_BATCH_SIZE));

        // Check if the host address property is a reference to an environment variable
        // of the form ${ENV=VARIABLE_NAME} (after spaces are removed)
        host = host.replace(" ", "");
        if (host.startsWith("${ENV=")) {
            host = System.getenv(host.substring(6, host.length() - 1));
        }

        IData fedDs = new SFTPDatasource(host, user, privateKeyPath, bufferSize, knownHostsFile);

        host = (String) config.get(BatchConstants.HARS_FILE_HOST);
        user = (String) config.get(BatchConstants.HARS_FILE_HOST_USER_ID);

        // Check if the host address property is a reference to an environment variable
        // of the form ${ENV=VARIABLE_NAME} (after spaces are removed)
        host = host.replace(" ", "");
        if (host.startsWith("${ENV=")) {
            host = System.getenv(host.substring(6, host.length() - 1));
        }

        IData harsDS = new SFTPDatasource(host, user, privateKeyPath, bufferSize, knownHostsFile);

        IDatabase db = new SQLDatabaseImpl(dbBatchSize);
        ILogHelper logHelper = new LogHelperImpl(db, dds);

        IBatchContext context = new BatchContextImpl(config);
        context.addProperty(BatchConstants.DATASBASE, db);
        context.addProperty(BatchConstants.FED_FILE_DATASOURCE, fedDs);
        context.addProperty(BatchConstants.DB_DATASOURCE, dds);
        context.addProperty(BatchConstants.HARS_FILE_DATASOURCE, harsDS);
        context.addProperty(BatchConstants.LOG_HELPER, logHelper);
        context.init();
        
        return context;
    }

}
