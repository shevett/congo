package com.stonekeep.congo.config;

import java.util.Properties;

public interface Configurer {

	public void updateConfiguration(Properties configuration)
			throws ConfigurationUpdateFailedException;

}
