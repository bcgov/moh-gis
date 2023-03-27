/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        IBatchContext.java                             *
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

import java.util.Map;

/**
 *
 * @author adebiyi.kuseju
 */
public interface IBatchContext {

    public void init();
    public Object getProperty(String key);
    public void execute() throws BatchException;
    public void addProperty(String key, Object value);
    public void destroy();

}
