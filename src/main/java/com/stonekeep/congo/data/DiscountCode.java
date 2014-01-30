package com.stonekeep.congo.data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * A class representing a single Discount Code. 
 * @author dbs
 */
public class DiscountCode extends Data {
	private Integer CID ;
	private String name;
	private String desc;
	private String note;
	private String type;	// percent, discount, absolute
	private BigDecimal factor;	// how much percent to take off, or how much to discount, or what the absolute price is
	private Boolean active;
	public java.sql.Timestamp lastmodified;
	
	public Integer getCID() { return this.CID; }
	public String getName() { return this.name; }
	public String getDesc() { return this.desc; }
	public String getNote() { return this.note; }
	public String getType() { return this.type; }
	public BigDecimal getFactor() { return this.factor; }
	public Boolean getActive() { return this.active; }
	public Timestamp getLastmodified() { return this.lastmodified; }

	public void setCID(Integer newcid) { this.CID = newcid; }
	public void setName(String newname) { this.name = newname; }
	public void setDesc(String newdesc) { this.desc = newdesc; }
	public void setNote(String newnote) { this.note = newnote; }
	public void setType(String newtype) { this.type = newtype; }
	public void setFactor(BigDecimal newfactor) { this.factor = newfactor; }
	public void setActive(Boolean newactive) { this.active = newactive; }
	public void setLastmodified(Timestamp newdata) { this.lastmodified = newdata; }

}
