/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        DataLoadComponent.java                         *
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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author grant.shan
 */
public class DataLoadComponent implements IBatchComponent {

    private static final Logger logger = Logger.getLogger(DataLoadComponent.class.getName());
    private IBatchContext context;
    private IDatabase db;
    private ILogHelper logHelper;
    private final String RECIPIENTS_INSERT_QUERY = "insert into GIS_LOAD_DETAILS_SA (  ACCOUNT_ID, "
            + "RCPT_SURNAME, RCPT_MIDDLENAME, RCPT_GIVENNAME, BIRTHDATE, ACCOUNT_STATUS_CODE,"
            + "ACCOUNT_CODE, ENTLMNT_DATE, PAY_DATE, FINAL_PAY_DATE, "
            + "ADDRESS1, ADDRESS2, ADDRESS3, ADDRESS4,"
            + "POSTAL_CODE, MARital_STATUS_CODE, SPOUSE_ACT_ID, "
            + "SPOUSE_GIVEN_NAME, IMS_STRT_DT, LAST_UPDATE_DATE, LAST_MODIFIED_DATETIME) "
            + " values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
    private DataSource ds;

    private StringBuilder summary;

    public void init(IBatchContext context) {
        this.context = context;
        db = (IDatabase) context.getProperty(BatchConstants.DATASBASE);
        ds = (DataSource) context.getProperty(BatchConstants.DB_DATASOURCE);
        logHelper = (ILogHelper) context.getProperty(BatchConstants.LOG_HELPER);

        summary = new StringBuilder();
    }

    //Read the fed file and load the data into database
    public boolean execute() throws BatchException {

        logger.log(Level.FINEST, "Executing: %1$s", DataLoadComponent.class.getName());

        int batchSizeNum = Integer.parseInt((String) context.getProperty(BatchConstants.DB_BATCH_SIZE));

        boolean success = true;

        // Clear the attachment string and create a new file for logs
        summary.delete(0, summary.length());
        BufferedReader br = null;
        BufferedWriter bw = null;
        String fedFile = (String) context.getProperty(BatchConstants.FILEPATH);

        File outputFile = null;

        try {

            outputFile = Paths.get(
                    System.getProperty("java.io.tmpdir"),
                    "GIS-Summary-" + MiscellaneousHelper.getDateId() + ".txt")
                    .toFile();

            br = new BufferedReader(new FileReader(fedFile));
            bw = new BufferedWriter(new FileWriter(outputFile));

            writeMessage("Starting data load ...", "INFO", bw);
            writeMessage("Cleaning temp table ...", "INFO", bw);

            initProcess();
            logger.log(Level.INFO, String.format("Parsing and loading downloaded file \"%1$s\" into the database", fedFile));
            writeMessage(String.format("Parsing and loading downloaded file \"%1$s\" into the database", fedFile), "INFO",  bw);

            int type1Count = 0;
            int type2Count = 0;
                               
            String read = null;
            String prevLine = null;
            String type1Line = null;
            String type2Line = null;
            String lastLine = null;
            boolean havingType2 = false;
            List<String[]> batch = new ArrayList<String[]>(batchSizeNum + 1);
            Properties prop = new Properties();
            GisEntry gisEntry = null;

            while (true) {

                for (int i = 0; i < batchSizeNum && (read = br.readLine()) != null;) {

                    if (read.startsWith("1")) {
                        type1Line = read;
                        type1Count++;
                    } else if (read.startsWith("2")) {
                        type2Count++;
                        type2Line = read;
                    } else if (read.startsWith("9")) {
                        lastLine = read;
                        break;
                    } else {
                        continue;
                    }

                    if(prevLine == null) {
                        prevLine = read;
                        continue;
                    } else if (prevLine.startsWith("1") && read.startsWith("2")) {
                        havingType2 = true;
                    } else if (prevLine.startsWith("1") && read.startsWith("1")) {
                        havingType2 = false;
                    } else {
                        // prevLine must be a typeLine2 record so we need
                        // to read the next record before deciding what to do
                        prevLine = read;
                        continue;
                    }

                    gisEntry = new GisEntry();

                    if (havingType2) {  
                        gisEntry.setEntry(type1Line, type2Line, db, ds);
                    } else {
                        // if no type2Line then we need to write our previous read type1Line
                        gisEntry.setEntry(prevLine, null, db, ds);
                    }

                    try {
                        validate(gisEntry, bw);
                    } catch (IOException io) {
                         logger.log(Level.WARNING, "Cannot write into summary file", io);
                    }
                    System.out.println("GisEntry: " + gisEntry.toString());
                    batch.add(MiscellaneousHelper.makeStringArray(gisEntry));
                    prevLine = read;
                    i++; // increment counter only after doing a write
                }

                // If we have only type when at end of the file, then we need to make sure we write this record
                // because we always wait to see if we have type 2 before we write.
                if (read == null && prevLine != null && prevLine.startsWith("1")) {
                    gisEntry = new GisEntry();
                    gisEntry.setEntry(prevLine, null, db, ds);

                    try {
                        validate(gisEntry, bw);
                    } catch (IOException io) {
                         logger.log(Level.WARNING, "Cannot write into summary file", io);
                    }
                    System.out.println("GisEntry: " + gisEntry.toString());
                    batch.add(MiscellaneousHelper.makeStringArray(gisEntry));

                    prevLine = null;
                }

                if (!batch.isEmpty()) {                  
                    prop.put(BatchConstants.DB_DATASOURCE, ds);
                    prop.put(BatchConstants.QUERY, RECIPIENTS_INSERT_QUERY);
                    prop.put(BatchConstants.PARAMETERS, batch);
                    prop.put(BatchConstants.STATEMENT_TYPE, IDatabase.STATEMENT_TYPE.INSERT);

                    try {
                        Properties response = db.execute(prop);

                        if (response != null && response.containsKey(BatchConstants.ERROR)) {
                            List<String> errors = (List<String>) response.get(BatchConstants.ERROR);

                            for (String s : errors) {
                                writeMessage(s, "ERROR", bw);
                            }
                        }

                    } catch (DatabaseException dbe) {
                        logger.log(Level.SEVERE, String.format("Data load error: %1$s", dbe.getMessage()));
                    }                  
                    batch.clear();
                } else if (read == null) {
                    if (lastLine != null) {
                        saveType9Data(lastLine, bw);
                    }  
                    break;  // we muct be at the nd of the file so we need to break the infinite while loop
                }

            }
            writeMessage("Completed: Total Type1 rows: " + type1Count, "INFO", bw);
            writeMessage("Completed: Total Type2 rows: " + type2Count, "INFO", bw);
            writeMessage("Total Active Account Selected: " + lastLine.substring(12, 19), "INFO", bw);
            writeMessage("FED file loading completed successfully", "INFO", bw);

            writeMessage("Starting the merging process...", "INFO", bw);
            mergeProcess();
            writeMessage("Merging process completed", "INFO", bw);
            deleteOldData();
            writeMessage("Old data deleted", "INFO", bw);

        } catch (IOException e) {
            try {
                writeMessage("IOExceptione" + e.toString(), bw);
            } catch (IOException ex) {
            }
            logger.log(Level.SEVERE, null, e);

        } catch (DatabaseException e) {
            try {
                writeMessage("DatabaseException" + e.toString(), bw);
            } catch (IOException ex) {
            }
            logger.log(Level.SEVERE, null, e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (bw != null) {
                    bw.close();

                    context.addProperty(BatchConstants.ATTACHMENT, outputFile.getAbsolutePath());
                    context.addProperty(BatchConstants.ATTACHMENT_TEXT, summary.toString());
                }
            } catch (Exception e) {
                //do nothing here
            }
        }

        return success;
    }

    public void destroy() {
    }

    private void validate(GisEntry gisEntry, BufferedWriter bw) throws IOException {
        //Validate Address
        if (gisEntry.getAddress1() == null || gisEntry.getAddress1().length() == 0) {
            writeMessage("Address Line is blank. Account ID:" + gisEntry.getAccountId(), "Warning", bw);
            writeMessage("Offending Record:" + gisEntry.toString(), "INFO", bw);

        }
        //Validate account Id
        String accountIdPattern = "\\d{9,12}";  // Matches any number that is 9 to 12 digits long
        if (!gisEntry.getAccountId().matches(accountIdPattern)) {
            writeMessage("AccountID is invalid: " + gisEntry.getAccountId(), bw);
            writeMessage("Offending Record:" + gisEntry.toString(), "INFO", bw);
        }

        //Validate Name
        if (gisEntry.getSurname() == null || gisEntry.getSurname().length() == 0) {
            writeMessage("Surname is blank. Account:" + gisEntry.getAccountId(), bw);
            writeMessage("Offending Record:" + gisEntry.toString(), "INFO", bw);
        }

        //Validate Entitlement Date

        writeMessage(validateDate(gisEntry.getEntitlementDate(), "Entitlement Date"), bw);

    }

    private void saveType9Data(String type9Line, BufferedWriter bw) throws DatabaseException, IOException {
              
        String payDate = "20" + type9Line.substring(47, 51);

        int year = Integer.parseInt(payDate.substring(0, 4));
        int month = Integer.parseInt(payDate.substring(4));

        month = month > 1 ? month - 1 : 12;
        year = month == 12 ? year - 1 : year;

        int load_id = Integer.parseInt(year + "" + (String.valueOf(month).length() < 2 ? "0" + month : month));

        writeMessage(validateDate(payDate, "Pay Date"), bw);

        Properties prop = new Properties();

        //Update Pay Date
        prop.put(BatchConstants.DB_DATASOURCE, ds);
        prop.put(BatchConstants.QUERY, "update GIS_LOAD_DETAILS_SA set PAY_DATE = '" + payDate + "', IMS_STRT_DT = '" + payDate + "'");
        prop.put(BatchConstants.STATEMENT_TYPE, IDatabase.STATEMENT_TYPE.UPDATE);
        db.execute(prop);

        //Update Final Pay Date if the account is deceased
        prop.put(BatchConstants.QUERY, "update GIS_LOAD_DETAILS_SA set FINAL_PAY_DATE = '" + payDate + "' where ACCOUNT_STATUS_CODE = 'D' ");
        prop.put(BatchConstants.STATEMENT_TYPE, IDatabase.STATEMENT_TYPE.UPDATE);  // redundant, but makes code more understandable
        db.execute(prop);


        prop.put(BatchConstants.QUERY, "insert into GIS_LOAD_SA (load_id, File_name, pay_date, CREATE_DATETIME) values "
                + "(" + load_id + ", '', '" + payDate + "', CURRENT_TIMESTAMP)");
        prop.put(BatchConstants.STATEMENT_TYPE, IDatabase.STATEMENT_TYPE.INSERT);
        db.execute(prop);
    }

    private String validateDate(String valDate, String name) {
        String datePattern = "[0-9][0-9][0-9][0-9](0[1-9]|1[012])";
        if (name == null) {
            return name + " is blank.";
        } else if (!valDate.matches(datePattern)) {
            return name + " is invalid:" + valDate;
        }
        return null;
    }

    private void initProcess() throws DatabaseException {

        //Clear the table
        Properties prop = new Properties();
        prop.put(BatchConstants.DB_DATASOURCE, ds);
        prop.put(BatchConstants.QUERY, "delete from GIS_LOAD_DETAILS_SA");
        prop.put(BatchConstants.STATEMENT_TYPE, IDatabase.STATEMENT_TYPE.UPDATE);
        db.execute(prop);

        prop.put(BatchConstants.QUERY, "delete from GIS_LOAD_SA");
        db.execute(prop);

    }

    private void mergeProcess() throws DatabaseException {
        Properties prop = new Properties();
        prop.put(BatchConstants.DB_DATASOURCE, ds);
        prop.put(BatchConstants.QUERY, "call gis.merge()");
        prop.put(BatchConstants.STATEMENT_TYPE, IDatabase.STATEMENT_TYPE.CALLABLE);

        db.execute(prop);
    }
    
    private void deleteOldData() throws DatabaseException  {
        Properties prop = new Properties();
        prop.put(BatchConstants.DB_DATASOURCE, ds);
        prop.put(BatchConstants.QUERY, "call gis.delete_old_data()");
        prop.put(BatchConstants.STATEMENT_TYPE, IDatabase.STATEMENT_TYPE.CALLABLE);

        db.execute(prop);
    }

    private void writeMessage(String message, BufferedWriter bw) throws IOException {
        writeMessage(message, "ERROR", bw);
    }

    private void writeMessage(String message, String level, BufferedWriter bw) throws IOException {
        if (message != null && message.length() > 0) {
            String formattedMessage = new java.util.Date() + ": " + level + ": " + message;

            summary.append(formattedMessage);
            summary.append('\n');
            System.out.println(formattedMessage);
            bw.write(formattedMessage);
            bw.newLine();
            bw.flush();
            logHelper.log(level, message);
        }

    }
}
