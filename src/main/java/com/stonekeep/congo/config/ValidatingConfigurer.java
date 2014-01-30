package com.stonekeep.congo.config;

import java.util.Properties;

public class ValidatingConfigurer implements Configurer {

	private final Configurer next;
	private final ConfigurationValidator validator;

	public ValidatingConfigurer(Configurer next,
			ConfigurationValidator validator) {
		this.next = next;
		this.validator = validator;
	}

	@Override
	public void updateConfiguration(Properties configuration)
			throws ConfigurationUpdateFailedException {
		if (!validator.validate(configuration))
			throw new InvalidConfigurationException();

		next.updateConfiguration(configuration);
	}
}
