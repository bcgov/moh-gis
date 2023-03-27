/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        GisAccessLogFacade.java                        *
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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Dan.Goulet
 */
@Stateless
public class GisAccessLogFacade implements GisAccessLogFacadeLocal {
    @PersistenceContext
    private EntityManager em;

    public void create(GisAccessLog gisAccessLog) {
        em.persist(gisAccessLog);
    }
}
