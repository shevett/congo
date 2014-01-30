package com.stonekeep.congo.coconut;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.config.ConfigurationUpdateFailedException;
import com.stonekeep.congo.config.Configurer;
import com.stonekeep.congo.config.InvalidConfigurationException;

public class SaveProperties implements Action, ServletRequestAware {
	private final Configurer configurer;
	private Logger logger = Logger.getLogger(SaveProperties.class);
	private HttpServletRequest request;
	public String savebutton = null;
	public String cancelbutton = null;

	public SaveProperties(Configurer configurer) {
		this.configurer = configurer;
	}

	@Override
	public String execute() throws ConfigurationUpdateFailedException {
		try {
			if (savebutton != null) {
				logger.debug("Updating configuration");
				configurer.updateConfiguration(parseProperties(request));
				return SUCCESS;
			} else {
				logger.debug("Cancelled.");
				return "exit";
			}
		} catch (InvalidConfigurationException ice) {
			return INPUT;
		}
	}

	private Properties parseProperties(HttpServletRequest request) {
		Properties p = new Properties();

		Enumeration<?> parameters = request.getParameterNames();
		while (parameters.hasMoreElements()) {
			String parameterName = parameters.nextElement().toString();
			String parameterValue = request.getParameter(parameterName);

			p.setProperty(parameterName, parameterValue);
		}

		return p;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

}
