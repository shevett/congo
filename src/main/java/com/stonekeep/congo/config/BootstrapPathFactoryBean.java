package com.stonekeep.congo.config;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.ServletContextAware;

/**
 * Identifies the location for the application's configuration files without
 * resorting to configuration files itself. To identify the configuration path,
 * this looks at system properties; if the property it's set up to search is
 * set, it's used to create a {@link FileSystemResource}. Otherwise, this will
 * return a default resource set at creation, instead.
 */
public class BootstrapPathFactoryBean extends AbstractFactoryBean implements
        ServletContextAware {

    private static final Logger log = Logger
            .getLogger(BootstrapPathFactoryBean.class);

    private final Resource defaultResource;
    private final String bootstrapPathProperty;
    private ServletContext servletContext;
    private String contextParameter;

    public BootstrapPathFactoryBean(Resource defaultResource,
            String bootstrapPathProperty) {
        this.defaultResource = defaultResource;
        this.bootstrapPathProperty = bootstrapPathProperty;
    }

    @Override
    protected Resource createInstance() {
        String configuredPath = getContextPath();
        if (configuredPath == null)
            configuredPath = System.getProperty(bootstrapPathProperty);
        if (configuredPath != null)
            return new FileSystemResource(configuredPath);
        return defaultResource;
    }

    private String getContextPath() {
        if (servletContext == null) {
            log.debug("No servlet context available during bootstrap. Skipping servlet context.");
            return null;
        } if (contextParameter == null) {
            log
                    .error("Servlet context available, but no context config parameter provided. Skipping servlet context.");
            return null;
        }

        String param = servletContext.getInitParameter(contextParameter);
        log.debug(String.format("Found servlet context parameter %s, set to %s", contextParameter, param));
        return param;
    }

    @Override
    public Class<? extends Resource> getObjectType() {
        return Resource.class;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void setContextParameter(String contextParameter) {
        this.contextParameter = contextParameter;
    }
}
