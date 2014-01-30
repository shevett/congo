package com.stonekeep.congo.data;

/**
 * A data object representing a property configuration object.
 * 
 * @author dbs
 */

public class PropertyConfiguration  {
	public int cid;
	public String name;
	public String scope;
	public String format;
	public String defaultvalue;
	public String type;
	public boolean regprompt;
	public float cost;
	public String description;
	public int sequence;
	
	public int getCid() { return this.cid; }
	public String getName() { return this.name; }
	public String getScope() { return this.scope; }
	public String getFormat() { return this.format; }
	public String getDefaultvalue() { return this.defaultvalue; }
	public String getType() { return this.type; }
	public boolean getRegprompt() { return this.regprompt; }
	public float getCost() { return this.cost; }
	public String getDescription() { return this.description; }
	public int getSequence() { return this.sequence; }
}