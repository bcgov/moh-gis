/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        LogException.java                              *
 * Date of Last Commit: $Date::                                              $ *
 * Revision Number:      $Rev::                                              $ *
 * Last Commit by:    $Author::                                              $ *
 *                                                                             *
 *******************************************************************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package giswar.batch.util;

import giswar.batch.config.*;

/**
 *
 * @author adebiyi.kuseju
 */
public class LogException extends Exception {

    public LogException() {
    }

    public LogException(String msg) {
        super(msg);
    }

    public LogException(Throwable t) {
        super(t);
    }
}
