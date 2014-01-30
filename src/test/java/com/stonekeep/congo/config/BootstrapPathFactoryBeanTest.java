package com.stonekeep.congo.config;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class BootstrapPathFactoryBeanTest {
	@Test
	public void defaultResource() throws Exception {
		Resource defaultResource = new FileSystemResource("/tmp");

		System.clearProperty("com.stonekeep.testing.a.very.unlikely.property");
		BootstrapPathFactoryBean factory = new BootstrapPathFactoryBean(
				defaultResource,
				"com.stonekeep.testing.a.very.unlikely.property");

		factory.afterPropertiesSet();

		assertEquals(defaultResource, factory.getObject());
	}

	@Test
	public void overrideResourceFromProperty() throws Exception {
		Resource defaultResource = new FileSystemResource("/tmp");

		System
				.setProperty(
						"com.stonekeep.testing.abstract.factory.factory.factory.factory",
						"/etc");
		BootstrapPathFactoryBean factory = new BootstrapPathFactoryBean(
				defaultResource,
				"com.stonekeep.testing.abstract.factory.factory.factory.factory");

		factory.afterPropertiesSet();

		assertEquals(new FileSystemResource("/etc"), factory.getObject());
	}
}
