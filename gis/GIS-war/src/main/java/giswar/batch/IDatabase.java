/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        IDatabase.java                                 *
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
public interface IDatabase {

    public enum STATEMENT_TYPE {
        UPDATE, INSERT, SELECT, CALLABLE
    }

    public Properties execute(Properties props) throws DatabaseException;
}
