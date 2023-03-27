/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        JNDIConfigLoaderImpl.java                      *
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
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;

/**
 *
 * @author adebiyi.kuseju
 */
class JNDIConfigLoaderImpl implements IConfigLoader {

    private static final Logger logger = Logger.getLogger(JNDIConfigLoaderImpl.class.getName());
    private Properties cachedConfig;

    public synchronized Properties getConfig(boolean fromCache) throws ConfigException {

        if (fromCache && cachedConfig != null) {
            return cachedConfig;
        }

        Properties config = null;
        try {
            InitialContext ic = new InitialContext();
            config = (Properties) ic.lookup(BatchConstants.CONFIG);

            cachedConfig = config;

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Configuration lookup failed", e);
            throw new ConfigException(e);
        }

        return config;
    }
}
