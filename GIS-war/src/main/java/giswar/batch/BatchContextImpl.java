/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        BatchContextImpl.java                          *
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adebiyi.kuseju
 */
public class BatchContextImpl implements IBatchContext {

    private static final Logger logger = Logger.getLogger(BatchContextImpl.class.getName());
    private List<IBatchComponent> chain;
    private int index = 0;
    private Properties config;
    private boolean initialized;
    private AtomicBoolean destroyed = new AtomicBoolean();

    public BatchContextImpl(Properties config) throws Exception {
        this.config = config;

        chain = new ArrayList<IBatchComponent>();
        String chainItems = (String) config.get(BatchConstants.CHAIN_ITEMS);

        if (chainItems != null && chainItems.trim().length() > 0) {
            String[] tokens = chainItems.split(",");
            logger.log(Level.INFO, "Creating batch chain components....");

            for (String item : tokens) {
                Class t = Class.forName(item.trim());
                chain.add((IBatchComponent) t.newInstance());
                logger.log(Level.INFO, String.format("\"%1$s\" created successfully ", item));
            }
        }

        this.config.put(BatchConstants.INTERNAL_PROPS, new HashMap<String, Object>()); // map of internal settings
    }

    public void init() {
        checkState();
        for (IBatchComponent chainItem : chain) {
            chainItem.init(this);
        }
        initialized = true;
    }

    public void execute() throws BatchException {
        doNext();
    }

    private void doNext() throws BatchException {
        boolean success = true;

        while (true) {

            checkState();
            // Continue processing only if the last item execute
            // successfully and there are still components waiting to run
            if (success && index < chain.size()) {  // Continue proce
                success = chain.get(index++).execute();
            } else {
                index = 0;
                break;
            }
        }
    }

    private void checkState() {
        if (destroyed.get()) {
            throw new IllegalStateException("No method calls allowed on an instance already marked for destruction");
        }
    }

    public Object getProperty(String key) {
        checkState();
        return config.containsKey(key) ? config.get(key) : ((Map<String, Object>) config.get(BatchConstants.INTERNAL_PROPS)).get(key);
    }

    public void addProperty(String key, Object value) {
        checkState();
        ((Map<String, Object>) config.get(BatchConstants.INTERNAL_PROPS)).put(key, value);
    }

    public void destroy() {
        for (IBatchComponent cahinItem : chain) {
            try {
                cahinItem.destroy();
            } catch (Exception e) {
                logger.log(Level.WARNING, e.getMessage());
            }
        }

        chain.clear();
        destroyed.set(true);

    }

    public boolean isInitialized() {
        return initialized;
    }
}
