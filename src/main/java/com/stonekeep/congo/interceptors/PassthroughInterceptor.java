package com.stonekeep.congo.interceptors;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * Does nothing; passes the request as-is. Useful to dummy out existing
 * interceptors.
 */
public class PassthroughInterceptor extends AbstractInterceptor {

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		return invocation.invoke();
	}

}
