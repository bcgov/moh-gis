/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        DataComponentTest.java                         *
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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.mockito.Mockito.*;


/**
 *
 * @author adebiyi.kuseju
 */
public class DataComponentTest {

    private static final Logger logger = Logger.getLogger(DataComponentTest.class.getName());

    public DataComponentTest() {
    }

  
    /**
     * Test of execute method, of class FileDownloader.
     */
    @Test
    public void testExecute() throws Exception {
        IData ds = new SFTPDatasourceMock();
        ILogHelper logHelper = mock(ILogHelper.class);

        Map<String, Object> config = new HashMap<String, Object>();
        config.put(BatchConstants.FED_FILE_DATASOURCE, ds);
        config.put(BatchConstants.CHAIN_ITEMS, "giswar.batch.DataComponent");
        config.put(BatchConstants.REMOTE_FED_FILE_DIR, "/NRTWebDev/");
        config.put(BatchConstants.BUFFER_SIZE, "30000");


        IBatchContext context = new BatchContextImpl(config);
        context.addProperty(BatchConstants.LOG_HELPER, logHelper);
        context.init();
        context.execute();

        logger.log(Level.INFO, String.format("Downloaded file is located at: %1$s", context.getProperty(BatchConstants.FILEPATH)));
        File file = new File((String)context.getProperty(BatchConstants.FILEPATH));
        file.delete();
        logger.log(Level.INFO, String.format("Downloaded file %1$s deleted", context.getProperty(BatchConstants.FILEPATH)));

    }


}