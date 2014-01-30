package com.stonekeep.congo.config.schema.xml;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class CategoryDescription {

	@XStreamAlias("description")
	private String description;

	@XStreamImplicit(itemFieldName = "entry")
	private List<EntryDescription> entryDescriptions;

	@XStreamAlias("name")
	@XStreamAsAttribute
	private String name;

	public String getDescription() {
		return description;
	}

	public List<EntryDescription> getEntries() {
		return entryDescriptions;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEntries(List<EntryDescription> entryDescriptions) {
		this.entryDescriptions = entryDescriptions;
	}

	public void setName(String name) {
		this.name = name;
	}
}
