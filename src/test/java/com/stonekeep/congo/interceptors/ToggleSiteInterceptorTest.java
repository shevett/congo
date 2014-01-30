package com.stonekeep.congo.interceptors;

import static org.junit.Assert.assertEquals;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.opensymphony.xwork2.ActionInvocation;

@RunWith(JMock.class)
public class ToggleSiteInterceptorTest {

	private final Mockery context = new JUnit4Mockery();

	@Test
	public void disabledInterceptorSkipsInvocation() throws Exception {
		final ActionInvocation invocation = context
				.mock(ActionInvocation.class);
		context.checking(new Expectations() {
			{
				never(invocation);
			}
		});

		ToggleInterceptor interceptor = new ToggleInterceptor();

		interceptor.setDisabledResult("hello");
		interceptor.setEnabled(false);

		assertEquals("hello", interceptor.intercept(invocation));
	}

	@Test
	public void enabledInterceptorContinuesInvocation() throws Exception {
		final ActionInvocation invocation = context
				.mock(ActionInvocation.class);
		context.checking(new Expectations() {
			{
				one(invocation).invoke();
				will(returnValue("goodbye"));
			}
		});

		ToggleInterceptor interceptor = new ToggleInterceptor();

		interceptor.setDisabledResult("hello");
		interceptor.setEnabled(true);

		assertEquals("goodbye", interceptor.intercept(invocation));
	}

}
