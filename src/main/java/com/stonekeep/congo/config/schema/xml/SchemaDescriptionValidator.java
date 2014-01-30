package com.stonekeep.congo.config.schema.xml;

import java.util.Properties;

import com.stonekeep.congo.config.ConfigurationValidator;

/**
 * Validates configuration properties against a
 * {@link ConfigurationSchemaDescription}. The properties instance is accepted
 * if it contains all non-optional properties defined in the description; the
 * values of the properties are not checked (even for SELECT-typed entries), and
 * extra properties are allowed.
 */
public class SchemaDescriptionValidator implements ConfigurationValidator {

	private final ConfigurationSchemaDescription description;

	public SchemaDescriptionValidator(ConfigurationSchemaDescription description) {
		this.description = description;
	}

	@Override
	public boolean validate(Properties p) {
		for (CategoryDescription c : description.getCategories())
			if (!validateCategory(c, p))
				return false;

		return true;
	}

	private boolean validateCategory(CategoryDescription c, Properties p) {
		for (EntryDescription e : c.getEntries())
			if (!validateEntry(e, p))
				return false;

		return true;
	}

	private boolean validateEntry(EntryDescription e, Properties p) {
		return e.isOptional() || p.getProperty(e.getKey()) != null;
	}

}
