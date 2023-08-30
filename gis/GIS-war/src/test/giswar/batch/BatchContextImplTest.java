/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        BatchContextImplTest.java                      *
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

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author adebiyi.kuseju
 */
public class BatchContextImplTest {

    public BatchContextImplTest() {
    }

    /**
     * Test of init method, of class BatchContextImpl.
     */
    @Test
    public void testInit() throws Exception {

        Map<String, Object> config = mock(Map.class);
        BatchContextImpl instance = new BatchContextImpl(config);

        instance.init();
        assertTrue(instance.isInitialized());
    }

    /**
     * Test of getProperty method, of class BatchContextImpl.
     */
    @Test
    public void testGetProperty() throws Exception {

        String key = "key";
        String value = "value";

        Map<String, Object> config = new HashMap<String, Object>();
        IBatchContext instance = new BatchContextImpl(config);
        instance.addProperty(key, value);

        Object result = instance.getProperty(key);
        assertEquals(value, result);
    }

    /**
     * Test of addProperty method, of class BatchContextImpl.
     */
    @Test
    public void testAddProperty() throws Exception {

        String key = "key";
        String value = "value";

        Map<String, Object> config = new HashMap<String, Object>();
        IBatchContext instance = new BatchContextImpl(config);
        instance.addProperty(key, value);

        Object result = instance.getProperty(key);
        assertEquals(value, result);
    }


     /**
     * Test of addProperty method, of class BatchContextImpl.
     */
    @Test(expected=IllegalStateException.class)
    public void testShutdown() throws Exception {

        Map<String, Object> config = mock(Map.class);
        BatchContextImpl instance = new BatchContextImpl(config);
        instance.destroy();
        instance.execute();
    }

    /**
     * Test of addProperty method, of class BatchContextImpl.
     */
    @Test(expected=IllegalStateException.class)
    public void testRunAfterShutdown() throws Exception {

        Map<String, Object> config = mock(Map.class);
        BatchContextImpl instance = new BatchContextImpl(config);
        instance.destroy();
        instance.execute();
    }

    /**
     * Test of addProperty method, of class BatchContextImpl.
     */
    @Test(expected=IllegalStateException.class)
    public void testInitAfterShutdown() throws Exception {

        Map<String, Object> config = mock(Map.class);
        BatchContextImpl instance = new BatchContextImpl(config);
        instance.destroy();
        instance.init();
    }

}