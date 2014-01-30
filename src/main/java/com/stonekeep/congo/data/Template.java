package com.stonekeep.congo.data;

import java.sql.Timestamp;

/**
 * A class representing a single template. 
 * @author dbs
 */
public class Template extends Data {
	private Integer CID ;
	private String name;
	private String desc;
	private String body;
	public java.sql.Timestamp lastmodified;
	
	public Integer getCID() { return this.CID; }
	public String getName() { return this.name; }
	public String getDesc() { return this.desc; }
	public String getBody() { return this.body; }
	public Timestamp getLastmodified() { return this.lastmodified; }

	public void setCID(Integer newcid) { this.CID = newcid; }
	public void setName(String newname) { this.name = newname; }
	public void setDesc(String newdesc) { this.desc = newdesc; }
	public void setBody(String newbody) { this.body = newbody; }
	public void setLastmodified(Timestamp newdata) { this.lastmodified = newdata; }

}
