package com.stonekeep.congo.data;

import java.sql.Timestamp;

/**
 * A class representing a single addon - ala an addon purchased and attached to a registrant
 * @author dbs
 */
public class AddOn extends Data {
	private int cid ;
	private int rid;
	private String name;
	private int quantity;
	private int invoiceid;
	public java.sql.Timestamp lastmodified;
	
	public int getCid() { return this.cid; }
	public int getRid() { return this.rid; }
	public String getName() { return this.name; }
	public int getQuantity() { return this.quantity; }
	public int getInvoiceid() { return this.invoiceid; }
	public Timestamp getLastmodified() { return this.lastmodified; }

	public void setCid(int newcid) { this.cid = newcid; }
	public void setRid(int newrid) { this.rid = newrid; }
	public void setName(String newname) { this.name = newname; }
	public void setQuantity(int newquantity) { this.quantity = newquantity; }
	public void setInvoiceid(int newinvoiceid) { this.invoiceid = newinvoiceid; }
	public void setLastmodified(Timestamp newdata) { this.lastmodified = newdata; }

}
