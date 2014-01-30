package com.stonekeep.congo.data;

/**
 * A data object representing a property associated with a registrant.
 * 
 * @author dbs
 */

public class Property extends Data {
	public int rid;
	public int cid;
	public String name;
	public String value;
	
	// These values are used when doing edits - they're not actually stored in the
	// reg_properties table, but are populated via a join.
	
	public boolean isglobal;
	public boolean isdefault;
	public String type;
	public String description;
	public Float cost ;
	public String format;
	public Boolean regprompt;
	public Boolean editable;
	
	public int getRid() { return this.rid; }
	public int getCid() { return this.cid; }
	public String getName() { return this.name; }
	public String getValue() { return this.value; }
	public boolean getIsglobal() { return this.isglobal; }
	public boolean getIsdefault() { return this.isdefault; }
	public String getDescription() { return this.description; }
	public String getType() { return this.type; }
	public Float getCost() { return this. cost; }
	public String getFormat() { return this.format; }
	public Boolean getRegprompt() { return this.regprompt; }
	public Boolean getEditable() { return this.editable; }
}
