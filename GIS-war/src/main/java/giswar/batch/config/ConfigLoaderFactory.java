/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        ConfigLoaderFactory.java                       *
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

import giswar.batch.BatchConstants;

/**
 *
 * @author adebiyi.kuseju
 */
public class ConfigLoaderFactory {

    private static IConfigLoader jndiConfigLoader = new JNDIConfigLoaderImpl();

    public static IConfigLoader getConfigLoader(String source) {

        IConfigLoader configLoader = null;

        if (source.equals(BatchConstants.JNDI)) {
            configLoader = jndiConfigLoader;
        } 

        return configLoader;
    }
}
