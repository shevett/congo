package com.stonekeep.congo.config.schema.xml;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class EntryDescription {
	@XStreamAlias("default")
	@XStreamAsAttribute
	private String defaultValue;

	@XStreamAlias("description")
	private String description;

	@XStreamAlias("key")
	@XStreamAsAttribute
	private String key;

	@XStreamAlias("name")
	@XStreamAsAttribute
	private String name;

	@XStreamAlias("optional")
	@XStreamAsAttribute
	private boolean optional;

	@XStreamAlias("type")
	@XStreamAsAttribute
	private EntryType entryType;

	@XStreamAlias("values")
	private List<SelectableValueDescription> values;

	public String getDefaultValue() {
		return defaultValue;
	}

	public String getDescription() {
		return description;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public EntryType getType() {
		return entryType;
	}

	public List<SelectableValueDescription> getValues() {
		return values;
	}

	public boolean isOptional() {
		return optional;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	public void setType(EntryType entryType) {
		this.entryType = entryType;
	}

	public void setValues(List<SelectableValueDescription> values) {
		this.values = values;
	}
}
