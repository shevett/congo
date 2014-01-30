package com.stonekeep.congo.spring;

import java.io.InputStream;

import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;

/**
 * Constructs objects for Spring based on the result of unmarshalling an XML
 * resource.
 */
public class UnmarshallerFactory extends AbstractFactoryBean {

	public UnmarshallerFactory(Resource resource, Unmarshaller unmarshaller) {
		this.resource = resource;
		this.unmarshaller = unmarshaller;
	}

	private final Unmarshaller unmarshaller;
	private final Resource resource;
	private Class<?> objectType = null;

	/**
	 * Configures the unmarshaller to only construct instances of a given type.
	 * If this is set to something other than <code>null</code>, instance
	 * creation will enforce the passed type; otherwise, instance creation will
	 * accept whatever type is unmarshalled.
	 * 
	 * @param objectType
	 *            the "expected" result type.
	 */
	public void setObjectType(Class<?> objectType) {
		this.objectType = objectType;
	}

	@Override
	protected Object createInstance() throws Exception {
		InputStream stream = resource.getInputStream();
		try {
			Object unmarshalledObject = unmarshaller
					.unmarshal(new StreamSource(stream));

			if (objectType != null)
				return objectType.cast(unmarshalledObject);

			return unmarshalledObject;
		} finally {
			stream.close();
		}
	}

	/**
	 * Unless explicitly set using {@link #setObjectType(Class)}, this will
	 * return <code>null</code>, as it's frequently impossible to tell in
	 * advance what type will be unmarshalled.
	 * 
	 * @see FactoryBean#getObjectType()
	 */
	@Override
	public Class<?> getObjectType() {
		return objectType;
	}

}
