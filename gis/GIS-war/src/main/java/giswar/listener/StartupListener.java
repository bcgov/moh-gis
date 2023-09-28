package giswar.listener;

import giswar.batch.config.ConfigException;
import giswar.batch.util.BatchJobAutoStarter;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import javax.sql.DataSource;
import java.util.Properties;


/**
 * Web application lifecycle listener.
 *
 * @author adebiyi.kuseju
 */
public class StartupListener implements ServletContextListener {

    public static final String ENV_PREFIX = "${ENV=";
    public static final String ENV_SUFFIX = "}";

    @Resource(lookup = "java:app/jdbc/gis")
    private DataSource ds;

    @Resource(lookup = "java:app/gis/batch_properties")
    private Properties batchProperties;
    
    private BatchJobAutoStarter batchJobAutoStarter;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        for (String property : batchProperties.stringPropertyNames()) {
            String value = batchProperties.getProperty(property);

            String newValue = value.replace(" ", "");

            // Check if the property contains a reference to an environment
            // variable of the form ${ENV=VARIABLE_NAME}
            if (newValue.startsWith(ENV_PREFIX) && newValue.endsWith(ENV_SUFFIX)) {
                // Remove the prefix and suffix
                String varName = newValue.substring(ENV_PREFIX.length(), newValue.length() - ENV_SUFFIX.length());

                // Get the value of the environment variable
                newValue = System.getenv(varName);

                // Set the value of the property only if the env var is defined
                if (newValue != null) {
                    batchProperties.setProperty(property, newValue);
                }
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
