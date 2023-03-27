/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        LogHelperImpl.java                             *
 * Date of Last Commit: $Date::                                              $ *
 * Revision Number:      $Rev::                                              $ *
 * Last Commit by:    $Author::                                              $ *
 *                                                                             *
 *******************************************************************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package giswar.batch.util;

import giswar.batch.BatchConstants;
import giswar.batch.DatabaseException;
import giswar.batch.IDatabase;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author adebiyi.kuseju
 */
public class LogHelperImpl implements ILogHelper {
    
    private static final Logger logger = Logger.getLogger(LogHelperImpl.class.getName());
    private final String LOG_INSERT_QUERY = "insert into gis_log (log_date,  log_type,   log_message) "
            + "values (sysdate, ?, ?)";
    private IDatabase db;
    private DataSource ds;

    public LogHelperImpl(IDatabase db, DataSource ds) {
        this.db = db;
        this.ds = ds;
    }

     public void log(String level, String message) {

         try {
            String[] paramItem = new String[2];
            paramItem[0] = level;
            paramItem[1] = message;

            List<String[]> params = new ArrayList<String[]>(2);
            params.add(paramItem);

            Properties props = new Properties();
            props.put(BatchConstants.DB_DATASOURCE, ds);
            props.put(BatchConstants.QUERY, LOG_INSERT_QUERY);
            props.put(BatchConstants.PARAMETERS, params);
            props.put(BatchConstants.STATEMENT_TYPE, IDatabase.STATEMENT_TYPE.INSERT);
            db.execute(props);
        } catch (DatabaseException ex) {
            logger.log(Level.SEVERE, "Error inserting log into the database", ex);
        }

     }
}
