package com.stonekeep.congo.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ResourceConfigurer implements Configurer {

	private final File configFile;

	public ResourceConfigurer(File configFile) {
		this.configFile = configFile;
	}

	@Override
	public void updateConfiguration(Properties configuration)
			throws ConfigurationUpdateFailedException {
		try {
			configFile.getParentFile().mkdirs();
			saveToFile(configuration);
		} catch (IOException ioe) {
			throw new ConfigurationUpdateFailedException(ioe);
		}
	}

	private void saveToFile(Properties configuration)
			throws FileNotFoundException, IOException {
		FileOutputStream target = new FileOutputStream(configFile);
		try {
			configuration.store(target, null);
		} finally {
			target.close();
		}
	}
}
