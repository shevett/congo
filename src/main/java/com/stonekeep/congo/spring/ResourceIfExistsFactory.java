package com.stonekeep.congo.spring;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.core.io.Resource;

/**
 * Checks if the primary resource it's given exists and, if so, returns it;
 * otherwise, returns a fallback resource.
 */
public class ResourceIfExistsFactory extends AbstractFactoryBean {

    private final Logger log = Logger.getLogger(ResourceIfExistsFactory.class);
	private final Resource primary;
	private final Resource fallback;

	public ResourceIfExistsFactory(Resource primary, Resource fallback) {
		this.primary = primary;
		this.fallback = fallback;
	}

	@Override
	protected Resource createInstance() {
	    log.debug(String.format("Primary resource is %s (exists = %s), fallback is %s", primary, primary.exists(), fallback));
	    
		if (primary.exists())
			return primary;

		return fallback;
	}

	@Override
	public Class<? extends Resource> getObjectType() {
		return Resource.class;
	}
}
