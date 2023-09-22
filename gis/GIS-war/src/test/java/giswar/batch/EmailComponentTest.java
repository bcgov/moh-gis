/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        EmailComponentTest.java                        *
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Properties;
import org.junit.Test;

/**
 * if running this test on local env, remember to disable McAfee on-access protection
 *
 * @author adebiyi.kuseju
 */
public class EmailComponentTest {

    public EmailComponentTest() {
    }

    /**
     * Test of execute method, of class EmailComponent.
     */
    @Test
    public void testExecute() throws Exception {

        File attachment = getAttachment();
        Properties config = new Properties();
        config.put(BatchConstants.MAIL_SEREVR, "mail.vic.cgi.com");
        config.put(BatchConstants.MAIL_FROM, "ASSM-JAVA.VIC@cgi.com");
        config.put(BatchConstants.MAIL_TO, "adebiyi.kuseju@cgi.com");
        config.put(BatchConstants.EMAIL_SUBJECT, "GIS Email Component Test (Please ignore)");
        config.put(BatchConstants.EMAIL_BODY, "GIS Email Component body text (Please ignore)");
        config.put(BatchConstants.ATTACHMENT, attachment.getAbsolutePath());
        config.put(BatchConstants.CHAIN_ITEMS, "giswar.batch.EmailComponent");

        IBatchContext context = new BatchContextImpl(config);
        context.init();
        //  context.execute();

        attachment.delete();

    }

    private File getAttachment() throws Exception {

        File fedFile = File.createTempFile("Attachment", ".txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(fedFile));

        bw.append("This is a mock attachment file for email component testing in GIS.\n");

        bw.close();
        bw = null;

        return fedFile;
    }

}
