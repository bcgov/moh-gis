/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        HarsFileCreatorComponent.java                  *
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
import giswar.batch.util.MiscellaneousHelper;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author grant.shan
 */
public class HarsFileCreatorComponent implements IBatchComponent {

    private static final Logger logger = Logger.getLogger(HarsFileCreatorComponent.class.getName());
    private final String HARS_DATA_QUERY = "SELECT RC.ACCOUNT_ID, RC.RCPT_SURNAME, RC.RCPT_GIVENNAME, RC.BIRTHDATE, RC.PAY_DATE, ADDRESS1, ADDRESS2, ADDRESS3, ADDRESS4, POSTAL_CODE"
            + " FROM GIS_LOAD_DETAILS_SA RC"
            + " INNER JOIN GIS_LOAD_SA LD"
            + " ON RC.PAY_DATE = LD.PAY_DATE";
    private IBatchContext context;
    private IDatabase db;
    private DataSource ds;
    private ILogHelper logHelper;

    @Override
    public void init(IBatchContext context) {
        this.context = context;
        db = (IDatabase) context.getProperty(BatchConstants.DATASBASE);
        ds = (DataSource) context.getProperty(BatchConstants.DB_DATASOURCE);
        logHelper = (ILogHelper) context.getProperty(BatchConstants.LOG_HELPER);
    }

    //Read the fed file and load the data into database
    @Override
    public boolean execute() throws BatchException {

        logger.log(Level.FINEST, "Executing: %1$s", HarsFileCreatorComponent.class.getName());

        boolean success = true;
        BufferedWriter bw = null;
        File harsFile = null;

        try {
            harsFile = Paths.get(System.getProperty("java.io.tmpdir"), BatchConstants.HARS_FILE_NAME).toFile();
            bw = new BufferedWriter(new FileWriter(harsFile));

            Properties props = new Properties();
            props.put(BatchConstants.DB_DATASOURCE, ds);
            props.put(BatchConstants.QUERY, HARS_DATA_QUERY);
            props.put(BatchConstants.STATEMENT_TYPE, IDatabase.STATEMENT_TYPE.SELECT);

            logger.log(Level.INFO, String.format("Creating HARS file: %1$s", harsFile.getName()));
            logHelper.log("INFO", String.format("Creating HARS file: %1$s", harsFile.getName()));

            List<String[]> rs = (List<String[]>) db.execute(props).get(BatchConstants.RESULTSET);
            StringBuilder builder = new StringBuilder();

            int i;
            String[] dt;
            for (i = 0; i < rs.size(); i++) {
                dt = rs.get(i);
                builder.append(MiscellaneousHelper.formatHarsData(dt[0], 9));
                builder.append(MiscellaneousHelper.formatHarsData(dt[1], 20));
                builder.append(MiscellaneousHelper.formatHarsData(dt[2], 15));
                builder.append(MiscellaneousHelper.formatHarsData(dt[3], 6));
                builder.append(MiscellaneousHelper.formatHarsData(dt[4], 6));
                builder.append(MiscellaneousHelper.formatHarsData(dt[5], 30));
                builder.append(MiscellaneousHelper.formatHarsData(dt[6], 30));
                builder.append(MiscellaneousHelper.formatHarsData(dt[7], 30));
                builder.append(MiscellaneousHelper.formatHarsData(dt[8], 30));
                builder.append(MiscellaneousHelper.formatHarsData(dt[9], 6)).append("\r\n");

                bw.append(builder.toString());

                builder.delete(0, builder.length());

            }

            logHelper.log("INFO", String.format("%1$s records written to HARS File", i));
        } catch (Exception e) {

            logHelper.log("Error", e.toString());
            logger.log(Level.SEVERE, null, e);
        } finally {
            try {

                if (bw != null) {
                    bw.close();

                    Properties props = new Properties();
                    props.put(BatchConstants.FILEPATH, harsFile.getAbsolutePath());
                    props.put(BatchConstants.UPLOAD_LOCATION, context.getProperty(BatchConstants.HARS_FILE_UPLOAD_LOCATION));

                    logHelper.log("INFO", String.format("Uploading HARS File %1$s to %2$s ...", harsFile.getAbsolutePath(), props.getProperty(BatchConstants.UPLOAD_LOCATION)));
                    ((IData) context.getProperty(BatchConstants.HARS_FILE_DATASOURCE)).uploadData(props);

                    logHelper.log("INFO", String.format("HARS file \"%1$s\" uploaded successfully", harsFile.getAbsolutePath()));
                    // We are done with this file. There is no point putting it in the context object
                    // as no other component needs it down the chain, however, for the sake of unit testing
                    // the path to the file will be placed in the context object
                    context.addProperty(BatchConstants.HARS_FILE, harsFile.getAbsolutePath());
                }
            } catch (Exception e) {
                 logHelper.log("ERROR", String.format("HARS file: \"%1$s\"", e.getMessage()));
            }
        }

        return success;
    }

    @Override
    public void destroy() {
    }

}
