package com.stonekeep.congo.log4j;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.FactoryConfigurationError;

import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;

/**
 * Configures Log4J using a {@link DOMConfigurator} at context startup. We use
 * this instead of the default mechanism because we want per-deployment logging
 * configuration without needing to edit the WAR for each deployment.
 * <p>
 * The log4j XML file is located using Spring's {@link Resource} mechanism, and
 * can be anywhere resources can be (local filesystem, on the classpath, network
 * URL, or what have you). The resource path should be provided through the
 * {@value #LOG4J_CONFIG_PARAMETER} context init parameter.
 * <p>
 * This listener should be configured before any classes which may use log4j.
 * This is easy to do by putting the listener first in web.xml.
 */
public class Log4jBootstrapListener implements ServletContextListener {

    /*
     * Yes, I do know about Spring's Log4jConfigListener class. It's
     * inappropriate here, because it relies(!) on containers unpacking the
     * webapp, which is neither required, guaranteed, nor a good idea as default
     * practice.
     */

    public static final String LOG4J_CONFIG_PARAMETER = "log4j.xml.url";

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // We care about this event.
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String log4jConfigResource = getLog4jLocation(sce.getServletContext());
        Resource r = findResource(log4jConfigResource);
        configureLog4j(r);
    }

    private void configureLog4j(Resource r) {
        try {
            DOMConfigurator.configure(r.getURL());

        } catch (FactoryConfigurationError e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Resource findResource(String resource) {
        ResourceEditor propertyEditor = new ResourceEditor();
        propertyEditor.setAsText(resource);
        Resource r = (Resource) propertyEditor.getValue();
        return r;
    }

    private String getLog4jLocation(ServletContext servletContext) {
        String log4jConfigResource = servletContext
                .getInitParameter(LOG4J_CONFIG_PARAMETER);

        if (log4jConfigResource == null)
            throw new IllegalStateException(LOG4J_CONFIG_PARAMETER
                    + " context parameter not set.");
        return log4jConfigResource;
    }
}
