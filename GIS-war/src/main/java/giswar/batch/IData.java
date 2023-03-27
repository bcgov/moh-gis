/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        IData.java                                     *
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

import java.util.Properties;

/**
 *
 * @author adebiyi.kuseju
 */
public interface IData {

    public Properties getData(Properties props) throws Exception ;
    public void uploadData(Properties props) throws Exception;
}
