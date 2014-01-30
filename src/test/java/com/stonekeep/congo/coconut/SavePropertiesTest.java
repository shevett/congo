package com.stonekeep.congo.coconut;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.opensymphony.xwork2.Action;
import com.stonekeep.congo.config.ConfigurationUpdateFailedException;
import com.stonekeep.congo.config.Configurer;
import com.stonekeep.congo.config.InvalidConfigurationException;

@RunWith(JMock.class)
public class SavePropertiesTest {
	private final Mockery context = new JUnit4Mockery();

	@Test
	public void saveValidConfig() throws ConfigurationUpdateFailedException {
		final Configurer configurer = context.mock(Configurer.class);
		final HttpServletRequest request = context
				.mock(HttpServletRequest.class);
		final Enumeration<?> e = context.mock(Enumeration.class);

		SaveProperties action = new SaveProperties(configurer);
		action.setServletRequest(request);

		context.checking(new Expectations() {
			{
				one(request).getParameterNames();
				will(returnValue(e));

				one(e).hasMoreElements();
				will(returnValue(true));
				one(e).nextElement();
				will(returnValue("a"));

				one(e).hasMoreElements();
				will(returnValue(false));

				allowing(request).getParameter("a");
				will(returnValue("b"));
				Properties expected = new Properties();
				expected.setProperty("a", "b");

				one(configurer).updateConfiguration(expected);
			}
		});

        action.savebutton = "save";
		assertEquals(Action.SUCCESS, action.execute());
	}

	@Test
	public void saveInvalidConfig() throws ConfigurationUpdateFailedException {
		final Configurer configurer = context.mock(Configurer.class);
		final HttpServletRequest request = context
				.mock(HttpServletRequest.class);
		final Enumeration<?> e = context.mock(Enumeration.class);

		SaveProperties action = new SaveProperties(configurer);
		action.setServletRequest(request);

		context.checking(new Expectations() {
			{
				one(request).getParameterNames();
				will(returnValue(e));

				one(e).hasMoreElements();
				will(returnValue(true));
				one(e).nextElement();
				will(returnValue("a"));

				one(e).hasMoreElements();
				will(returnValue(false));

				allowing(request).getParameter("a");
				will(returnValue("b"));

				Properties expected = new Properties();
				expected.setProperty("a", "b");

				one(configurer).updateConfiguration(expected);
				will(throwException(new InvalidConfigurationException()));
			}
		});

		action.savebutton = "save";
		assertEquals(Action.INPUT, action.execute());
	}

	@Test
	public void saveFailed() throws ConfigurationUpdateFailedException {
		final Configurer configurer = context.mock(Configurer.class);
		final HttpServletRequest request = context
				.mock(HttpServletRequest.class);
		final Enumeration<?> e = context.mock(Enumeration.class);

		SaveProperties action = new SaveProperties(configurer);
		action.setServletRequest(request);

		context.checking(new Expectations() {
			{
				one(request).getParameterNames();
				will(returnValue(e));

				// TODO whip me, beat me, make me refactor this.
				one(e).hasMoreElements();
				will(returnValue(true));
				one(e).nextElement();
				will(returnValue("a"));

				one(e).hasMoreElements();
				will(returnValue(false));

				allowing(request).getParameter("a");
				will(returnValue("b"));

				Properties expected = new Properties();
				expected.setProperty("a", "b");

				one(configurer).updateConfiguration(expected);
				will(throwException(new ConfigurationUpdateFailedException()));
			}
		});

		try {
	        action.savebutton = "save";
			action.execute();
			fail();
		} catch (ConfigurationUpdateFailedException cufe) {
			// Success case.
		}
	}

	/**
	 * A quick sanity test to validate that two Properties with identical keys
	 * compare equal.
	 */
	@Test
	public void propertiesEquality() {
		Properties a = new Properties();
		Properties b = new Properties();

		a.setProperty("a", "b");
		b.setProperty("a", "b");

		assertTrue(a.equals(b));
		assertTrue(b.equals(a));
	}
}
