package com.stonekeep.congo.interceptors;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * This interceptor has two states: "enabled", which passes invocations through
 * unaltered, and "disabled", which returns a constant result without passing
 * through to the rest of the call chain.
 */
public class ToggleInterceptor extends AbstractInterceptor {

	private String result = Action.ERROR;
	private boolean enabled;

	/**
	 * Depending on the last setting applied to {@link #setEnabled(boolean)},
	 * either continues the invocation normally or skips the invocation and
	 * returns the last configured {@link #setDisabledResult(String) bypass
	 * result}.
	 * 
	 * @see com.opensymphony.xwork2.interceptor.Interceptor#intercept(ActionInvocation)
	 */
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		if (!enabled)
			return result;
		return invocation.invoke();
	}

	/**
	 * Configures the name of the result to use when the interceptor is
	 * disabled. By default, this is set to {@link Action#ERROR}, but any result
	 * can be named.
	 * 
	 * @param result
	 *            the struts result name to use when the filtered action is
	 *            disabled.
	 */
	public void setDisabledResult(String result) {
		this.result = result;
	}

	/**
	 * Enables or disables the filtered actions. If disabled, all calls to
	 * {@link #intercept(ActionInvocation)} will return the last configured
	 * {@link #setDisabledResult(String) disabled result} without continuing the
	 * invocation.
	 * 
	 * @param enabled
	 *            <code>true</code> to enable the filtered actions,
	 *            <code>false</code> to disable the filtered actions and return
	 *            a fixed result.
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
