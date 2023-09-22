/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        BatchComponentMock2.java                       *
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
public class BatchComponentMock2 implements IBatchComponent {

    @Override
    public void init(IBatchContext context) {
    }

    @Override
    public boolean execute() throws BatchException {
        return false;
    }

    @Override
    public void destroy() {
    }
}
