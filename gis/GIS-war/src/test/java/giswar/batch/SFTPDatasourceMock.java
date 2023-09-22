/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        SFTPDatasourceMock.java                        *
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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author adebiyi.kuseju
 */
public class SFTPDatasourceMock extends SFTPDatasource {

    private File fedFile;

    public SFTPDatasourceMock() throws Exception {
        super("localhost", "test", BatchConstants.KEYFILEPATH, "1", BatchConstants.KNOWNHOSTSFILE);
        fedFile = File.createTempFile("TEST-FED-FILE", ".txt");
    }

    @Override
    public Properties getData(Properties props) throws Exception {

        BufferedWriter bw = new BufferedWriter(new FileWriter(fedFile));

        bw.append(SampleFedFileData.RECORD_1_TYPE_1).append("\n\n");
        bw.append(SampleFedFileData.RECORD_1_TYPE_2).append("\n\n");
        bw.append(SampleFedFileData.RECORD_2_TYPE_1).append("\n\n");
        bw.append(SampleFedFileData.RECORD_2_TYPE_2).append("\n\n");
        bw.append(SampleFedFileData.LAST_LINE).append("\n\n");

        bw.close();
        bw = null;

        Properties response = new Properties();
        response.put(BatchConstants.FILEPATH, fedFile.getAbsolutePath());
        return response;
    }

    @Override
    public void uploadData(Properties props) throws Exception {
    }

    @Override
    public List<String> filesInDir(String dirPath) throws Exception {
        List<String> files = new ArrayList<>();
        files.add(fedFile.getName());

        return files;
    }

    @Override
    public void deleteFiles(String... files) throws Exception {
        // do nothing
    }
}
