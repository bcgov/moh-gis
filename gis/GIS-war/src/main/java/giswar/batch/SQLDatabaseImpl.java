/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        SQLDatabaseImpl.java                           *
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

import giswar.batch.util.MiscellaneousHelper;
import java.sql.BatchUpdateException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class SQLDatabaseImpl implements IDatabase {

    private static final Logger logger = Logger.getLogger(SQLDatabaseImpl.class.getName());
    private int batchSize;

    public SQLDatabaseImpl(int batchSize) {
        this.batchSize = batchSize;
    }

    public Properties execute(Properties props) throws DatabaseException {

        List<String> errors = new ArrayList<String>();
        List<String[]> params = (List<String[]>) props.get(BatchConstants.PARAMETERS);
        String query = props.getProperty(BatchConstants.QUERY);
        IDatabase.STATEMENT_TYPE stmtType = (IDatabase.STATEMENT_TYPE) props.get(BatchConstants.STATEMENT_TYPE);

        Properties response = null;
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ((DataSource) props.get(BatchConstants.DB_DATASOURCE)).getConnection();
            response = new Properties();
            response.put(BatchConstants.ERROR, errors);
            String[] posParams = null;

            if (stmtType.equals(IDatabase.STATEMENT_TYPE.UPDATE)
                    || stmtType.equals(IDatabase.STATEMENT_TYPE.INSERT)) {

                stmt = conn.prepareStatement(query);

                int total = 0;
                if (params != null && !params.isEmpty()) {  // if there are parametes available for query
                    conn.setAutoCommit(false);

                    process:
                    while (true) {
                        int i;
                        for (i = 0; i < batchSize && total < params.size(); i++, total++) {
                            posParams = params.get(total);
                            for (int j = 1; j <= posParams.length; j++) {
                                stmt.setString(j, posParams[j - 1]);
                            }

                            stmt.addBatch();
                        }

                        try {
                            stmt.executeBatch();
                        } catch (BatchUpdateException e) {
                            int[] successful = e.getUpdateCounts();

                            if (successful.length != params.size()) {
                                logger.log(Level.WARNING, String.format("%1$s statements successful. Last value returned is: %2$s", successful.length, successful[successful.length - 1]));
                                errors.add(String.format("Error: %1$s \nRecord: %2$s", e.getMessage(), MiscellaneousHelper.toString(params.size() == total ? params.get(total - 1) : params.get(total))));

                                if ((successful.length - 1) != i) {
                                    logger.log(Level.WARNING, String.format("Warning: Skipping erroneous record: [%1$s]",
                                            MiscellaneousHelper.toString(params.size() == total ? params.get(total - 1) : params.get(total))));
                                    stmt.clearBatch();
                                    continue process;
                                }
                            }
                        }

                        if (total >= params.size()) {
                            break;
                        }
                    }
                    conn.commit();
                } else {
                    stmt.executeUpdate();
                }

            } else if (stmtType.equals(IDatabase.STATEMENT_TYPE.SELECT)) {

                stmt = conn.prepareStatement(query);

                if (params != null && !params.isEmpty()) {  // if there are parametes available for query
                    posParams = params.get(0);
                    for (int j = 1; j <= posParams.length; j++) {
                        stmt.setString(j, posParams[j - 1]);
                    }
                }

                stmt.executeQuery();
                ResultSet rs = stmt.getResultSet();
                List<String[]> result = new ArrayList<String[]>();
                String[] resItem = null;

                while (rs.next()) {
                    resItem = new String[rs.getMetaData().getColumnCount()];

                    for (int x = 0; x < resItem.length; x++) {
                        resItem[x] = rs.getString(x + 1);
                    }

                    result.add(resItem);
                }

                response.put(BatchConstants.RESULTSET, result);

            } else if (stmtType.equals(IDatabase.STATEMENT_TYPE.CALLABLE)) {

                CallableStatement cstmt = null;
                cstmt = conn.prepareCall(query);

                if (params != null && !params.isEmpty()) {  // if there are parametes available for query
                    posParams = params.get(0);
                    for (int j = 1; j <= posParams.length; j++) {
                        cstmt.setString(j, posParams[j - 1]);
                    }
                }
                cstmt.execute();
            }
            response.put(BatchConstants.SUCCESS, true);

        } catch (Exception ne) {
            throw new DatabaseException(ne);
        } finally {

            try {

                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }

                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException sqe) {
                throw new DatabaseException(sqe);
            }
        }

        return response;
    }
}
