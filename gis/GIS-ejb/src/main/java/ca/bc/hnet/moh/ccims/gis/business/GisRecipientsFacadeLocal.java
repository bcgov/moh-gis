/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        GisRecipientsFacadeLocal.java                  *
 * Date of Last Commit: $Date::                                              $ *
 * Revision Number:      $Rev::                                              $ *
 * Last Commit by:    $Author::                                              $ *
 *                                                                             *
 *******************************************************************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ca.bc.hnet.moh.ccims.gis.business;

import ca.bc.hnet.moh.ccims.gis.entity.GisRecipients;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Dan.Goulet
 *
 * History
 *  September 7, 2010 - John Au - Added the findCount method to determine the
 *                                number of results returned from the db
 */
@Local
public interface GisRecipientsFacadeLocal {

    List<GisRecipients> findAll();
    
    List<GisRecipients> find(String sin, 
            String surName, String middleName, String givenName, String birthDate, 
            Calendar lastUpdate);
    
    String getLastLoadDate();

    int findCount(String sin,
            String surName, String middleName, String givenName, String birthDate,
            Calendar lastUpdate);
}
