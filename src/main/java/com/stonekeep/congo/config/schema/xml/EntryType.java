package com.stonekeep.congo.config.schema.xml;

public enum EntryType {
	// XXX lower-case enum constants due to limitations of XStream - it's that
	// XXX write a custom converter just to whack case or use an annotation.
	password, select, text
}