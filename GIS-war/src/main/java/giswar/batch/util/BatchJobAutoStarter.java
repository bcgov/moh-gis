/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        BatchJobAutoStarter.java                       *
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
import giswar.batch.BatchTaskImpl;
import giswar.batch.IBatchTask;
import giswar.batch.config.ConfigException;
import giswar.batch.config.ConfigLoaderFactory;
import giswar.batch.config.IConfigLoader;
import it.sauronsoftware.cron4j.Scheduler;
import it.sauronsoftware.cron4j.Task;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author adebiyi.kuseju
 */
public class BatchJobAutoStarter implements Runnable {

    private static final Logger logger = Logger.getLogger(BatchJobAutoStarter.class.getName());
    private final Scheduler scheduler;
    private final IConfigLoader configLoader;
    private Properties config;
    private String scheduleId;
    private final String myScheduleId;
    private final DataSource datasource;
    private String mySchedule;
    private String batchJobScedule;
    private boolean createImmediateScheduler;

    public BatchJobAutoStarter(final DataSource datasource, Properties config) throws ConfigException {

        this.configLoader = ConfigLoaderFactory.getConfigLoader(BatchConstants.JNDI);
        this.datasource = datasource;
        this.config = config;
        
        this.scheduler = new Scheduler();
        
        //Schedule this autoscheduler
        mySchedule = (String) config.get(BatchConstants.BATCH_JOB_AUTO_START_SCHEDULE);
        myScheduleId = scheduler.schedule(mySchedule, this);
        
        scheduler.start();
        
        runImmediateBatchJobIfNeeded();

    }

    @Override
    public void run() {

        //try {
            logger.log(Level.INFO, "Reloading configuration values");

            //config = configLoader.getConfig(false);

            runImmediateBatchJobIfNeeded();
            
            boolean runJob = Boolean.parseBoolean((String) config.get(BatchConstants.RUN_BATCH_JOB));

            if (runJob) {
                startBatchJob();
            } else {
                stopBatchJob();
            }
/*
        } catch (ConfigException ce) {
            logger.log(Level.SEVERE, ce.getMessage(), ce);
        } finally { */
            String temp = (String) config.get(BatchConstants.BATCH_JOB_AUTO_START_SCHEDULE);
            
            if (!mySchedule.equals(temp)) {
                mySchedule = temp;
                scheduler.reschedule(myScheduleId, mySchedule);
                logger.log(Level.INFO, String.format("Batch Job Auto Starter Schedule Changed to %s", mySchedule));

            }
        }

    //}

    public void destroy() {

        if (scheduler != null && scheduler.isStarted()) {
            scheduler.stop();
            logger.log(Level.INFO, "Batch Cronjob Destroyed");
            logger.log(Level.INFO, "Batch Job Auto Starter Destroyed");
        }
    }
    
    private void runImmediateBatchJobIfNeeded() {

         boolean temp = Boolean.parseBoolean((String) config.get(BatchConstants.CREATE_IMMEDIATE_SCHEDULE));

        if ((temp != createImmediateScheduler) && temp) {
            logger.log(Level.INFO, "Starting immediate batch job");
            scheduler.launch((Task) setUpBatchJob());         
        }
        
        createImmediateScheduler = temp;
    }

    private void stopBatchJob() {
        if (scheduleId != null) {
            logger.log(Level.INFO, "Descheduling batch job");
            scheduler.deschedule(scheduleId);
            scheduleId = null;
        }
    }

    private void startBatchJob() {
        if (scheduleId == null) {
            batchJobScedule = (String) config.get(BatchConstants.SCHEDULE);
            logger.log(Level.INFO, String.format("Scheduling batch job to run at %s", batchJobScedule));
            scheduleId = scheduler.schedule(batchJobScedule, setUpBatchJob());
        } else {
            
            String temp = (String) config.get(BatchConstants.SCHEDULE);
            
            // Reschedule batch job if a schedule string has been updated
            if (!batchJobScedule.equals(temp)) {
                batchJobScedule = temp;
                scheduler.reschedule(scheduleId, batchJobScedule);
                logger.log(Level.INFO, String.format("Rescheduling batch job to run at %s", batchJobScedule));

            }
        }
    }

    private IBatchTask setUpBatchJob() {
        IBatchTask batchTask = new BatchTaskImpl();
        ((BatchTaskImpl) batchTask).setDataSource(datasource);
        ((BatchTaskImpl) batchTask).setProperties(config);

        return batchTask;
    }
}
