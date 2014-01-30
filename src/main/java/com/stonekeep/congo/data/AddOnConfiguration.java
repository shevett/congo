package com.stonekeep.congo.data;

import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * A class representing a single addonconfiguration . 
 * @author dbs
 */
public class AddOnConfiguration {
	private Integer cid ;
	private String name;
	private String displayName;
	private String description;
	private BigDecimal price;
	private Boolean active;
	private Boolean closed;
	private int cap;
	private String detail;
	public java.sql.Timestamp lastmodified;

	
	public Integer getCid() { return this.cid; }
	public String getName() { return this.name; }
	public String getDisplayName() { return this.displayName; }
	public String getDescription() { return this.description; }
	public BigDecimal getPrice() { return this.price; }
	public Boolean getActive() { return this.active; }
	public Boolean getClosed() { return this.closed; }
	public int getCap() { return this.cap; }
	public String getDetail() { return this.detail; }
	public Timestamp getLastmodified() { return this.lastmodified; }

	public void setCid(Integer newcid) { this.cid = newcid; }
	public void setName(String newname) { this.name = newname; }
	public void setDisplayName(String newname) { this.displayName = newname; }
	public void setDescription(String newdesc) { this.description = newdesc; }
	public void setPrice(BigDecimal newPrice) { this.price = newPrice; }
	public void setActive(Boolean newactive) { this.active = newactive; }
	public void setClosed(Boolean newclosed) { this.closed = newclosed; }
	public void setCap(int newcap) { this.cap = newcap; }
	public void setDetail(String newdetail) { this.detail = newdetail; }
	public void setLastmodified(Timestamp newdata) { this.lastmodified = newdata; }

}
