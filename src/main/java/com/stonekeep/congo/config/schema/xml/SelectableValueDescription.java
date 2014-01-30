package com.stonekeep.congo.config.schema.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("value")
@XStreamConverter(SelectableValueConverter.class)
public class SelectableValueDescription {

	private String content;

	@XStreamAlias("label")
	@XStreamAsAttribute
	private String label;

	@Override
	public String toString() {
		return getLabel() + " (" + getContent() + ")";
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
