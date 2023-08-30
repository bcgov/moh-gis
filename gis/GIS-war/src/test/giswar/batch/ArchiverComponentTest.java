/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        ArchiverComponentTest.java                     *
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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author adebiyi.kuseju
 */
public class ArchiverComponentTest {

    private static final Logger logger = Logger.getLogger(ArchiverComponentTest.class.getName());

    public ArchiverComponentTest() {
    }

    /**
     * Test of execute method, of class FileDownloader.
     */
    @Test
    public void testExecute() throws Exception {
        IData ds = mock(SFTPDatasource.class);
        
        String tempir = System.getProperty("java.io.tmpdir");
        File file = new File(tempir + "dataFileMock.txt");

        BufferedWriter bw = new BufferedWriter(new FileWriter(file));

        for (int i = 0; i < 500; i++) {
            bw.write("This is a mock data file for testing.\n");
        }

        bw.close();
        bw = null;

        Map<String, Object> config = new HashMap<String, Object>();
        config.put(BatchConstants.FED_FILE_DATASOURCE, ds);
        config.put(BatchConstants.CHAIN_ITEMS, "giswar.batch.ArchiverComponent");
        config.put(BatchConstants.FILEPATH, file.getAbsolutePath());
        config.put(BatchConstants.ARCHIVE_FILE_UPLOAD_LOCATION, "/Dummy/");
        config.put(BatchConstants.BUFFER_SIZE, "30000");
        

        IBatchContext context = new BatchContextImpl(config);
        context.init();
        context.execute();

        file.delete();

    }
}
