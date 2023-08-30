/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        HarsFileCreatorComponentTest.java              *
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
import java.sql.DriverManager;
import giswar.batch.util.MiscellaneousHelper;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author adebiyi.kuseju
 */
public class HarsFileCreatorComponentTest {

    private final String HARS_DATA_QUERY = "SELECT RC.ACCOUNT_ID, RC.RCPT_SURNAME, RC.RCPT_GIVENNAME, RC.BIRTHDATE, RC.PAY_DATE"
            + " FROM GIS_LOAD_DETAILS_SA RC"
            + " INNER JOIN GIS_LOAD_SA LD"
            + " ON RC.PAY_DATE = LD.PAY_DATE";

    public HarsFileCreatorComponentTest() {
    }

    /**
     * Test of execute method, of class HarsFileCreatorComponent.
     */
    @Test
    public void testExecute() throws Exception {

        //Connection con = DriverManager.getConnection("jalapeno.hlth.gov.bc.ca:1522", "gisd", "gisdev");
        
        IData ds = mock(IData.class);
        IDatabase db = mock(IDatabase.class);
        DataSource dds = mock(DataSource.class);
        ILogHelper logHelper = mock(ILogHelper.class);

        Properties props = new Properties();
        props.put(BatchConstants.DB_DATASOURCE, dds);
        props.put(BatchConstants.QUERY, HARS_DATA_QUERY);
        props.put(BatchConstants.STATEMENT_TYPE, IDatabase.STATEMENT_TYPE.SELECT);     

        Properties response = new Properties();
        response.put(BatchConstants.RESULTSET, getSampleData());
        response.put(BatchConstants.SUCCESS, true);

        when(db.execute(refEq(props))).thenReturn(response);

        Map<String, Object> config = new HashMap<String, Object>();
        config.put(BatchConstants.DATASBASE, db);
        config.put(BatchConstants.HARS_FILE_DATASOURCE, ds);
        config.put(BatchConstants.HARS_FILE_UPLOAD_LOCATION, "/NRTWebDev/");
        config.put(BatchConstants.DB_BATCH_SIZE, "1");
        config.put(BatchConstants.CHAIN_ITEMS, "giswar.batch.HarsFileCreatorComponent");

        IBatchContext context = new BatchContextImpl(config);
        context.addProperty(BatchConstants.DB_DATASOURCE, dds);
        context.addProperty(BatchConstants.LOG_HELPER, logHelper);
        context.init();
        context.execute();

        StringBuilder builder = new StringBuilder();
        File harsFile = new File((String) context.getProperty(BatchConstants.HARS_FILE));
        BufferedReader br = new BufferedReader(new FileReader(harsFile));

        String line = null;
        while ((line = br.readLine()) != null) {
            builder.append(line).append("\r\n");
        }

        assertEquals("File name must be " + harsFile.getName(), harsFile.getName(), BatchConstants.HARS_FILE_NAME);
        assertEquals("File format is wrong", expectedFileFormat(), builder.toString());

        br.close();
        br = null;

        // Clean up
        //harsFile.delete();
    }

    private List<String[]> getSampleData() {

        List<String[]> sampleData = new ArrayList<String[]>();
        /*for (int i = 0; i < 12257; i++ ) {
            sampleData.add(new String[]{"102102", "Doe", "John", "201206", "201205", "Address Line 1", "Address Line 2", "Address Line 3", "Address Line 4", "Post Code"});
        }*/
        sampleData.add(new String[]{"102102", "Doe", "John", "201206", "201205", "Address Line 1", "Address Line 2", "Address Line 3", "Address Line 4", "Post Code"});
        sampleData.add(new String[]{"102178", "Perkins", "Joe", "201201", "201208", "Address Line 1", "Address Line 2", "Address Line 3", "Address Line 4", "Post Code"});

        return sampleData;
    }

    private String expectedFileFormat() throws Exception {
        List<String[]> sampleData = getSampleData();
        StringBuilder builder = new StringBuilder();

        for (String[] dt : sampleData) {
            builder.append(MiscellaneousHelper.formatHarsData(dt[0], 9));
            builder.append(MiscellaneousHelper.formatHarsData(dt[1], 20));
            builder.append(MiscellaneousHelper.formatHarsData(dt[2], 15));
            builder.append(MiscellaneousHelper.formatHarsData(dt[3], 6));
            builder.append(MiscellaneousHelper.formatHarsData(dt[4], 6));
            builder.append(MiscellaneousHelper.formatHarsData(dt[5], 30));
            builder.append(MiscellaneousHelper.formatHarsData(dt[6], 30));
            builder.append(MiscellaneousHelper.formatHarsData(dt[7], 30));
            builder.append(MiscellaneousHelper.formatHarsData(dt[8], 30));
            builder.append(MiscellaneousHelper.formatHarsData(dt[9], 6)).append("\r\n");
        }

        return builder.toString();
    }
}
