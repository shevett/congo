package com.stonekeep.congo.config.actions;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.Action;

/**
 * Exposes an object and then continues the request.
 */
public class StaticValue implements Action, ServletRequestAware {

	private final String key;
	private final String result;
	private final Object value;

	private HttpServletRequest request;

	public StaticValue(String key, Object value, String result) {
		this.key = key;
		this.result = result;
		this.value = value;
	}

	@Override
	public String execute() {
		request.setAttribute(key, value);
		return result;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

}
