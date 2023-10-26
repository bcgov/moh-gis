/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        CleanupComponentTest.java                      *
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
import java.io.File;
import java.util.Properties;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 *
 * @author adebiyi.kuseju
 */
public class CleanupComponentTest {

    public CleanupComponentTest() {
    }

    /**
     * Test of init method, of class CleanupComponent.
     */
    /**
     * Test of execute method, of class CleanupComponent.
     */
    @Test
    public void testExecute() throws Exception {

        Properties config = new Properties();
        config.put(BatchConstants.CHAIN_ITEMS, "giswar.batch.CleanupComponent");
        File file = File.createTempFile("GIS-FED-FILE", ".gis");
        File file2 = File.createTempFile("HARS-FILE", ".txt");
        File file3 = File.createTempFile("ATTACHMENT", ".txt");

        SFTPDatasource ds = mock(SFTPDatasource.class);
        ILogHelper logHelper = mock(ILogHelper.class);

        IBatchContext context = new BatchContextImpl(config);
        context.addProperty(BatchConstants.FILEPATH, file.getAbsolutePath());
        context.addProperty(BatchConstants.HARS_FILE, file2.getAbsolutePath());
        context.addProperty(BatchConstants.ATTACHMENT, file3.getAbsolutePath());
        context.addProperty(BatchConstants.REMOTE_FILE_PATH, "Whatever");
        context.addProperty(BatchConstants.FED_FILE_DATASOURCE, ds);
        context.addProperty(BatchConstants.LOG_HELPER, logHelper);
        context.init();
        context.execute();

        assertFalse(file.getName() + "should have been deleted", file.exists());
        assertFalse(file2.getName() + "should have been deleted", file2.exists());
        assertFalse(file3.getName() + "should have been deleted", file3.exists());

        verify(ds).deleteFiles((String) context.getProperty(BatchConstants.REMOTE_FILE_PATH));
    }
}
