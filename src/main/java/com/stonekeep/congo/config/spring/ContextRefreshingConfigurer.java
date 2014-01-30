package com.stonekeep.congo.config.spring;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import com.stonekeep.congo.config.ConfigurationUpdateFailedException;
import com.stonekeep.congo.config.Configurer;

public class ContextRefreshingConfigurer implements Configurer,
        ApplicationContextAware {
    private final Logger log = Logger
            .getLogger(ContextRefreshingConfigurer.class);
    private final Configurer next;
    private ConfigurableApplicationContext applicationContext;

    public ContextRefreshingConfigurer(Configurer next) {
        this.next = next;
    }

    @Override
    public void updateConfiguration(Properties configuration)
            throws ConfigurationUpdateFailedException {
        next.updateConfiguration(configuration);

        log.info("Refreshing context.");
        applicationContext.refresh();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        try {
            this.applicationContext = (ConfigurableApplicationContext) applicationContext;
        } catch (ClassCastException cce) {
            throw new IllegalArgumentException(
                    "Cannot reconfigure non-configurable application contexts.",
                    cce);
        }
    }
}
