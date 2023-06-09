/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        BatchConstants.java                            *
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

/**
 *
 * @author adebiyi.kuseju
 */
public class BatchConstants {

    public static final String CONFIG = "java:app/gis/batch_properties";
    public static final String JNDI = "JNDI";
    public static final String PROPERTIES_FILE = "FILE";
    public static final String FED_FILE_HOST = "fedFilehost";
    public static final String FED_FILE_HOST_USER_ID = "fedFileHostUserId";
    public static final String HARS_FILE_HOST = "harsFilehost";
    public static final String HARS_FILE_HOST_USER_ID = "harsFileHostUserId";
    public static final String REMOTE_FED_FILE_DIR = "fedFileLocation";
    public static final String BUFFER_SIZE = "bufferSize";
    public static final String CHAIN_ITEMS = "chainItems";
    public static final String SCHEDULE = "schedule";
    public static final String CREATE_IMMEDIATE_SCHEDULE = "createImmediateScheduler";
    public static final String RUN_BATCH_JOB = "run-batch-job";
    public static final String BATCH_JOB_AUTO_START_SCHEDULE = "batch-auto-start-schedule";
    public static final String FED_FILE_DATASOURCE = "fedFileDs";
    public static final String HARS_FILE_DATASOURCE = "harsFileDs";
    public static final String FILEPATH = "filePath";
    public static final String INTERNAL_PROPS = "internalProps";
    public static final String KEYFILEPATH = "/opt/payara/.ssh/id_rsa";
    public static final String KNOWNHOSTSFILE = "/opt/payara/.ssh/known_hosts";

    
    public static final String MAIL_SEREVR = "mailServer";
    public static final String MAIL_SEREVR_PORT = "mailServerPort";
    public static final String MAIL_FROM = "mailFrom";
    public static final String MAIL_TO = "mailTo";
    public static final String EMAIL_SUBJECT = "emailSubject";
    public static final String EMAIL_BODY = "emailBody";
    public static final String ATTACHMENT = "attachment";
    public static final String ATTACHMENT_TEXT = "attachment-text";
    public static final String IS_CLOUD_DEPLOYED = "is-cloud-deployed";
    public static final String AWS_API_URL = "aws-api-url";

    //File
    public static final String ARCHIVE_FILE = "archiveFile";
    public static final String HARS_FILE = "harsFile";
    public static final String REMOTE_FILE_PATH = "remoteFilePath";
    public static final String UPLOAD_LOCATION = "uploadLocation";
    public static final String HARS_FILE_UPLOAD_LOCATION = "harsFileUploadLocation";
    public static final String ARCHIVE_FILE_UPLOAD_LOCATION = "archiveFileUploadLocation";
    public static final String HARS_FILE_NAME = "HARGIS.txt";
    public static final String MONTHLY_DATALOAD_FILE_SUMMARY_PREFIX =  "GIS-Summary-";


    //DB
    public static final String DATASBASE = "database";
    public static final String DB_DATASOURCE = "datasource";
    public static final String QUERY = "query";
    public static final String PARAMETERS = "parameters";
    public static final String STATEMENT_TYPE = "statementType";
    public static final String RESULTSET = "resultSet";
    public static final String SUCCESS = "success";
    public static final String NO_OF_RECORDS_AFFECTED = "noOfRecordsAffected";
    public static final String DB_BATCH_SIZE = "dbBatchSize";

    //Log
    public static final String LOG_HELPER = "logHelper";
    public static final String LOG_LEVEL = "logLevel";
    public static final String LOG_MESSAGE = "logMessage";


    public static final String ERROR = "error";
}
