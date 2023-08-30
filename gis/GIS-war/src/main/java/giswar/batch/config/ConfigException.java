/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        ConfigException.java                           *
 * Date of Last Commit: $Date::                                              $ *
 * Revision Number:      $Rev::                                              $ *
 * Last Commit by:    $Author::                                              $ *
 *                                                                             *
 *******************************************************************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package giswar.batch.config;

/**
 *
 * @author adebiyi.kuseju
 */
public class ConfigException extends Exception {

    public ConfigException() {
    }

    public ConfigException(String msg) {
        super(msg);
    }

    public ConfigException(Throwable t) {
        super(t);
    }
}
