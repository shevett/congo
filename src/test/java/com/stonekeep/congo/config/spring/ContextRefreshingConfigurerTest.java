package com.stonekeep.congo.config.spring;

import static org.junit.Assert.fail;

import java.util.Properties;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import com.stonekeep.congo.config.ConfigurationUpdateFailedException;
import com.stonekeep.congo.config.Configurer;

@RunWith(JMock.class)
public class ContextRefreshingConfigurerTest {
	private final Mockery context = new JUnit4Mockery();

	@Test
	public void configureAndRefresh() throws ConfigurationUpdateFailedException {
		final Properties p = new Properties();
		p.setProperty("a", "a");

		final Configurer next = context.mock(Configurer.class);
		final ConfigurableApplicationContext appContext = context
				.mock(ConfigurableApplicationContext.class);
		final Sequence configureFirstSequence = context
				.sequence("Configure, then refresh");

		ContextRefreshingConfigurer configurer = new ContextRefreshingConfigurer(
				next);
		configurer.setApplicationContext(appContext);

		context.checking(new Expectations() {
			{
				one(next).updateConfiguration(p);
				inSequence(configureFirstSequence);

				one(appContext).refresh();
				inSequence(configureFirstSequence);
			}
		});

		configurer.updateConfiguration(p);
	}

	@Test
	public void configureFailsNoRefresh()
			throws ConfigurationUpdateFailedException {
		final Properties p = new Properties();
		p.setProperty("a", "a");

		final Configurer next = context.mock(Configurer.class);
		final ConfigurableApplicationContext appContext = context
				.mock(ConfigurableApplicationContext.class);

		ContextRefreshingConfigurer configurer = new ContextRefreshingConfigurer(
				next);
		configurer.setApplicationContext(appContext);

		context.checking(new Expectations() {
			{
				one(next).updateConfiguration(p);
				will(throwException(new ConfigurationUpdateFailedException()));
			}
		});

		try {
			configurer.updateConfiguration(p);
			fail();
		} catch (ConfigurationUpdateFailedException expected) {
			// Succeed.
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void nonConfigurableAppContext() {
		final Properties p = new Properties();
		p.setProperty("a", "a");

		final Configurer next = context.mock(Configurer.class);
		final ApplicationContext appContext = context
				.mock(ApplicationContext.class);

		ContextRefreshingConfigurer configurer = new ContextRefreshingConfigurer(
				next);
		configurer.setApplicationContext(appContext);
	}
}
