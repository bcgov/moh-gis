package giswar.listener;

import giswar.batch.config.ConfigException;
import giswar.batch.util.BatchJobAutoStarter;
import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
import java.util.Properties;


/**
 * Web application lifecycle listener.
 *
 * @author adebiyi.kuseju
 */
public class StartupListener implements ServletContextListener {

    @Resource(lookup = "java:app/jdbc/gis")
    private DataSource ds;

    @Resource(lookup = "java:app/gis/batch_properties")
    private Properties batchProperties;
    
    private BatchJobAutoStarter batchJobAutoStarter;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        for (String property : batchProperties.stringPropertyNames()) {
            String value = batchProperties.getProperty(property);

            // Check if the property contains a reference to an environment
            // variable of the form ${ENV=VARIABLE_NAME}
            value = value.replace(" ", "");
            if (value.startsWith("${ENV=")) {
                value = System.getenv(value.substring(6, value.length() - 1));
                batchProperties.setProperty(property, value);
            }
        }

        try {
            batchJobAutoStarter = new BatchJobAutoStarter(ds, batchProperties);
            
        } catch (ConfigException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        batchJobAutoStarter.destroy();
    }
}
