/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        EmailComponent.java                            *
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author adebiyi.kuseju
 */
public class EmailComponent implements IBatchComponent {

    private static final Logger logger = Logger.getLogger(EmailComponent.class.getName());
    private IBatchContext context;
    private ILogHelper logHelper;

    public void init(IBatchContext context) {
        this.context = context;
        logHelper = (ILogHelper) context.getProperty(BatchConstants.LOG_HELPER);
    }

    public boolean execute() throws BatchException {

        logger.log(Level.FINEST, "Executing: %1$s", EmailComponent.class.getName());

        boolean isCloudDeployed = Boolean.valueOf((String) context.getProperty(BatchConstants.IS_CLOUD_DEPLOYED));

        String apiURL = (String) context.getProperty(BatchConstants.AWS_API_URL);

        boolean success = false;
        String from = (String) context.getProperty(BatchConstants.MAIL_FROM);
        String to = (String) context.getProperty(BatchConstants.MAIL_TO);
        String subject = (String) context.getProperty(BatchConstants.EMAIL_SUBJECT);
        String bodyText = (String) context.getProperty(BatchConstants.EMAIL_BODY);
        String attachmentText = (String) context.getProperty(BatchConstants.ATTACHMENT_TEXT);
        String filename = (String) context.getProperty(BatchConstants.ATTACHMENT);
        List<String> recipients = null;

        if (to != null) {
            recipients = new ArrayList<String>();
            String[] tokens = to.split(",");

            for (String s: tokens) {
                s = s.trim();
                if (s.length() > 0) {
                    recipients.add(s);
                }
            }
        }

        if (recipients == null || recipients.isEmpty()) {
            logHelper.log("WARNING", "No recipients provided for email");
            return true;
        }

        if (isCloudDeployed) {
            // Make a call to the API via the url given in the environment variables
            try {
                URL url = new URL(apiURL);

                HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setRequestMethod("POST");
                httpConnection.setRequestProperty("Content-Type", "application/json");

                httpConnection.setDoOutput(true);

                // Format the attachment text properly for JSON
                attachmentText = attachmentText.replace("\n", "\\n").replace("\"", "\\\"");

                // Include the summary as text since AWS SNS doesn't support email attachments
                // Add both the subject and the body to the request in JSON format
                String jsonRequest =
                    "{" +
                        "\"body\": \"" + bodyText + "\\n\\n" + attachmentText + "\"," +
                        "\"subject\": \""+ subject + "\"" +
                    "}";

                DataOutputStream requestStream = new DataOutputStream(httpConnection.getOutputStream());
                requestStream.writeBytes(jsonRequest);
                requestStream.flush();
                requestStream.close();

                InputStream content = (InputStream) httpConnection.getContent();
                StringBuilder response = new StringBuilder();
                int nextChar = content.read();

                while (nextChar >= 0) {
                    response.append((char) nextChar);
                    nextChar = content.read();
                }

                logger.log(Level.INFO, response.toString());
                logHelper.log("INFO", response.toString());
            }
            catch (IOException e) {
                logger.log(Level.WARNING, String.format("Connection error: %1$s",  e.getMessage()));
                logHelper.log("ERROR", "Connection error: " + e.getMessage());
            }
            finally {
                success = true;
            }
        } else {
            InternetAddress[] addressTo = new InternetAddress[recipients.size()];
            Properties props = setupMailProps();
            Session session = Session.getDefaultInstance(props, null);
            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));

                for (int i = 0; i < recipients.size(); i++) {
                    addressTo[i] = new InternetAddress(recipients.get(i));
                }

                message.setRecipients(Message.RecipientType.TO, addressTo);
                message.setSubject(subject);
                message.setSentDate(new Date());

                //
                // Set the email message text.
                //
                MimeBodyPart messagePart = new MimeBodyPart();
                messagePart.setText(bodyText);

                //
                // Set the email attachment file
                //
                MimeBodyPart attachmentPart = new MimeBodyPart();
                FileDataSource fileDataSource = new FileDataSource(filename) {

                    @Override
                    public String getContentType() {
                        return "application/octet-stream";
                    }
                };
                attachmentPart.setDataHandler(new DataHandler(fileDataSource));
                attachmentPart.setFileName(fileDataSource.getName());

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messagePart);
                multipart.addBodyPart(attachmentPart);

                message.setContent(multipart);

                logHelper.log("INFO", "Sending summary email .....");
                Transport.send(message);

                logger.log(Level.INFO, "Summary e-mail sent.");
                
            } catch (MessagingException e) {
                logger.log(Level.WARNING, String.format("Email error: %1$s",  e.getMessage()));
                logHelper.log("ERROR", "Email error: " + e.getMessage());
            } finally {
                success = true;
            }
        }

        return success;
    }

    public void destroy() {
    }

    private Properties setupMailProps() {
        Properties props = new Properties();

        Object temp = context.getProperty(BatchConstants.MAIL_SEREVR);

        if (temp != null) {
            props.put("mail.smtp.host", temp);

            temp = context.getProperty(BatchConstants.MAIL_SEREVR_PORT);

            if (temp != null) {
                props.put("mail.smtp.port", temp);
            }
        }

        return props;
    }
}
