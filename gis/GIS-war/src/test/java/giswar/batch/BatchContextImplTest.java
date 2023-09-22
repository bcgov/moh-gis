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

import java.util.Properties;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Mockito.mock;

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

        Properties config = mock(Properties.class);
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

        Properties config = new Properties();
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

        Properties config = new Properties();
        IBatchContext instance = new BatchContextImpl(config);
        instance.addProperty(key, value);

        Object result = instance.getProperty(key);
        assertEquals(value, result);
    }

    /**
     * Test of addProperty method, of class BatchContextImpl.
     */
    @Test(expected = IllegalStateException.class)
    public void testShutdown() throws Exception {

        Properties config = mock(Properties.class);
        BatchContextImpl instance = new BatchContextImpl(config);
        instance.destroy();
        instance.execute();
    }

    /**
     * Test of addProperty method, of class BatchContextImpl.
     */
    @Test(expected = IllegalStateException.class)
    public void testRunAfterShutdown() throws Exception {

        Properties config = mock(Properties.class);
        BatchContextImpl instance = new BatchContextImpl(config);
        instance.destroy();
        instance.execute();
    }

    /**
     * Test of addProperty method, of class BatchContextImpl.
     */
    @Test(expected = IllegalStateException.class)
    public void testInitAfterShutdown() throws Exception {

        Properties config = mock(Properties.class);
        BatchContextImpl instance = new BatchContextImpl(config);
        instance.destroy();
        instance.init();
    }

}
