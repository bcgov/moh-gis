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

            // Check if the property contains a reference to an environment
            // variable of the form ${ENV=VARIABLE_NAME}
            value = value.replace(" ", "");
            if (value.startsWith(ENV_PREFIX) && value.endsWith(ENV_SUFFIX)) {
                value = System.getenv(value.substring(ENV_PREFIX.length(), value.length() - ENV_SUFFIX.length()));
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
