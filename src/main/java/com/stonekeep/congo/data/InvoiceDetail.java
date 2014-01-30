package com.stonekeep.congo.data;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class InvoiceDetail {
	public int id;
	public int rid;
	public int cid;
	public int invoice;
	public int sequence;
	public int operator;
	public String fullname;
	public String type;
	public String type2;
	public String description;
	public String comment;
	public BigDecimal amount;
	public BigDecimal discount;
	public BigDecimal finalamount;
	public Timestamp activity;
	public Boolean postprocess = false ;
	
	public int getId() { return this.id; }
	public int getRid() { return this.rid; }
	public int getCid() { return this.cid; }
	public int getInvoice() { return this.invoice; }
	public int getSequence() { return this.sequence; }
	public int getOperator() { return this.operator; }
	public String getFullname() { return this.fullname; } 
	public String getType() { return this.type; }
	public String getType2() { return this.type2; }
	public String getDescription() { return this.description; }
	public String getComment() { return this.comment; }
	public BigDecimal getAmount() { return this.amount; }
	public BigDecimal getDiscount() { return this.discount; }
	public BigDecimal getFinalamount() { return this.finalamount; }
	public Timestamp getActivity() { return this.activity; }
	public Boolean getPostprocess() { return this.postprocess; }

}