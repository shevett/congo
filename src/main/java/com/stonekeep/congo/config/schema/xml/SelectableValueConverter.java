package com.stonekeep.congo.config.schema.xml;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class SelectableValueConverter implements Converter {

	@Override
	@SuppressWarnings("unchecked")
	public boolean canConvert(Class type) {
		return SelectableValueDescription.class.equals(type);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		SelectableValueDescription value = (SelectableValueDescription) source;
		writer.startNode("value");
		writer.addAttribute("label", value.getLabel());
		writer.setValue(value.getContent());
		writer.endNode();
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		SelectableValueDescription value = new SelectableValueDescription();
		value.setLabel(reader.getAttribute("label"));
		value.setContent(reader.getValue());

		return value;
	}

}
