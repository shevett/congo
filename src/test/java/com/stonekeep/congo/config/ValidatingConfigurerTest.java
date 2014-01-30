package com.stonekeep.congo.config;

import static org.junit.Assert.fail;

import java.util.Properties;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class ValidatingConfigurerTest {
	private final Mockery context = new JUnit4Mockery();

	@Test
	public void validConfiguration() throws ConfigurationUpdateFailedException {
		final Properties p = new Properties();
		p.setProperty("a", "a");

		final Configurer next = context.mock(Configurer.class);
		final ConfigurationValidator validator = context
				.mock(ConfigurationValidator.class);
		final Sequence validateFirstSequence = context
				.sequence("Validate, then update");

		ValidatingConfigurer configurer = new ValidatingConfigurer(next,
				validator);

		context.checking(new Expectations() {
			{
				allowing(validator).validate(p);
				will(returnValue(true));
				inSequence(validateFirstSequence);

				one(next).updateConfiguration(p);
				inSequence(validateFirstSequence);
			}
		});

		configurer.updateConfiguration(p);
	}

	@Test
	public void invalidConfiguration()
			throws ConfigurationUpdateFailedException {
		final Properties p = new Properties();
		p.setProperty("a", "a");

		final Configurer next = context.mock(Configurer.class);
		final ConfigurationValidator validator = context
				.mock(ConfigurationValidator.class);

		ValidatingConfigurer configurer = new ValidatingConfigurer(next,
				validator);

		context.checking(new Expectations() {
			{
				allowing(validator).validate(p);
				will(returnValue(false));
			}
		});

		try {
			configurer.updateConfiguration(p);
			fail();
		} catch (InvalidConfigurationException expected) {
			// Test passes.
		}
	}
}
