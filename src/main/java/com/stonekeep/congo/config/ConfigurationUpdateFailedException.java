package com.stonekeep.congo.config;

public class ConfigurationUpdateFailedException extends Exception {
	public ConfigurationUpdateFailedException() {
		super();
	}

	public ConfigurationUpdateFailedException(String message) {
		super(message);
	}

	public ConfigurationUpdateFailedException(Throwable cause) {
		super(cause);
	}

	public ConfigurationUpdateFailedException(String message, Throwable cause) {
		super(message, cause);
	}
}
