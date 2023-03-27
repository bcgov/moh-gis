/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        BatchException.java                            *
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

/**
 *
 * @author adebiyi.kuseju
 */
public class BatchException extends Exception {

    public BatchException() {
    }

    public BatchException(String msg) {
        super(msg);
    }

    public BatchException(Throwable t) {
        super(t);
    }
}
