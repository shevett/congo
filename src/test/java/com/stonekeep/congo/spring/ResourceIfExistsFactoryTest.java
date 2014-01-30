package com.stonekeep.congo.spring;

import static org.junit.Assert.assertSame;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.Resource;

@RunWith(JMock.class)
public class ResourceIfExistsFactoryTest {
	private final Mockery context = new JUnit4Mockery();

	@Test
	public void resourceExists() throws Exception {
		final Resource primary = context.mock(Resource.class, "primary");
		final Resource fallback = context.mock(Resource.class, "fallback");

		context.checking(new Expectations() {
			{
				allowing(primary).exists();
				will(returnValue(true));
			}
		});

		ResourceIfExistsFactory factory = new ResourceIfExistsFactory(primary,
				fallback);
		factory.afterPropertiesSet();

		assertSame(primary, factory.getObject());
	}

	@Test
	public void fallback() throws Exception {
		final Resource primary = context.mock(Resource.class, "primary");
		final Resource fallback = context.mock(Resource.class, "fallback");

		context.checking(new Expectations() {
			{
				allowing(primary).exists();
				will(returnValue(false));
			}
		});

		ResourceIfExistsFactory factory = new ResourceIfExistsFactory(primary,
				fallback);
		factory.afterPropertiesSet();

		assertSame(fallback, factory.getObject());
	}
}
