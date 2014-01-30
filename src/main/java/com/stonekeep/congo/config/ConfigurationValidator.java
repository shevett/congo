package com.stonekeep.congo.config;

import java.util.Properties;

public interface ConfigurationValidator {

	/**
	 * Verifies that the passed {@link Properties} instance is a valid
	 * configuration.
	 * 
	 * @param p
	 *            the Properties instance to check.
	 * @return <code>true</code> if and only if the passed Properties is a valid
	 *         configuration.
	 */
	public boolean validate(Properties p);

}
