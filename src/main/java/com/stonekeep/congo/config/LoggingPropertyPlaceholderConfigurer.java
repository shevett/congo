package com.stonekeep.congo.config;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class LoggingPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	
	private Logger logger = Logger.getLogger(LoggingPropertyPlaceholderConfigurer.class);

    @Autowired
    private ServletContext servletContext;
	
	@Override
	protected void loadProperties(Properties props) throws IOException {
		super.loadProperties(props);
//		logger.debug("Servlet context path is : " + servletContext.getRealPath("index.html"));
		for (Entry entry : props.entrySet()) {
			logger.debug("Configuration loaded: " + entry.getKey() + ": " + entry.getValue());
		}
	}
}