/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        GisAccessLogFacadeLocal.java                   *
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

import ca.bc.hnet.moh.ccims.gis.entity.GisAccessLog;
import jakarta.ejb.Local;

/**
 *
 * @author Dan.Goulet
 */
@Local
public interface GisAccessLogFacadeLocal {

    void create(GisAccessLog gisAccessLog);
}
