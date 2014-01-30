package com.stonekeep.congo.config.schema.xml;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("schema")
public class ConfigurationSchemaDescription {

	@XStreamImplicit(itemFieldName = "category")
	private List<CategoryDescription> categoryDescriptions;

	@XStreamAlias("description")
	private String description;

	public List<CategoryDescription> getCategories() {
		return categoryDescriptions;
	}

	public String getDescription() {
		return description;
	}

	public void setCategories(List<CategoryDescription> categoryDescriptions) {
		this.categoryDescriptions = categoryDescriptions;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
