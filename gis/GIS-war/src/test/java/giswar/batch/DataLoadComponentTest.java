/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        DataLoadComponentTest.java                     *
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
import javax.sql.DataSource;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.util.Properties;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Mockito.mock;

/**
 *
 * @author adebiyi.kuseju
 */
public class DataLoadComponentTest {

    public DataLoadComponentTest() {
    }

    /**
     * Test of execute method, of class DataLoadComponentTest.
     */
    @Test
    public void testExecute() throws Exception {

        IData ds = mock(IData.class);
        IDatabase db = mock(IDatabase.class);
        DataSource dds = mock(DataSource.class);
        ILogHelper logHelper = mock(ILogHelper.class);

        Properties config = new Properties();
        config.put(BatchConstants.DATASBASE, db);
        config.put(BatchConstants.FED_FILE_DATASOURCE, ds);
        config.put(BatchConstants.DB_BATCH_SIZE, "5000");
        config.put(BatchConstants.CHAIN_ITEMS, "giswar.batch.DataLoadComponent");

        File data = getDownloadedFile();
        IBatchContext context = new BatchContextImpl(config);
        context.addProperty(BatchConstants.DB_DATASOURCE, dds);
        context.addProperty(BatchConstants.FILEPATH, data.getAbsolutePath());
        context.addProperty(BatchConstants.LOG_HELPER, logHelper);
        context.init();
        context.execute();

        File attachment = new File((String) context.getProperty(BatchConstants.ATTACHMENT));
        assertTrue(attachment.getName() + " must exist at this point", attachment.exists());

        // Clean up
        //data.delete();
        attachment.delete();
    }

    private File getDownloadedFile() throws Exception {

        File fedFile = File.createTempFile("TEST-FED-FILE", ".txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(fedFile));

        for (int i = 0; i < 5500; i++) {
            bw.append(SampleFedFileData.RECORD_1_TYPE_1).append("\n");
        }
        bw.append(SampleFedFileData.LAST_LINE).append("\n");

        bw.close();
        bw = null;

        return fedFile;
    }
}
