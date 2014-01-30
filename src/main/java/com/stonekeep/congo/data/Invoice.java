package com.stonekeep.congo.data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Invoice {
	public int id;
	public int creator;
	public String creatorName;
	public int payer;
	public String payerName;
	public int operator;
	public Timestamp activity;
	public BigDecimal amount;
	public String paytype;
	public Timestamp paydate;
	public String comment;
	public String authcode;
	public String status;
	public String message;
	public int postcount;	// Calculated.
	
	public ArrayList<InvoiceDetail> detailList = new ArrayList<InvoiceDetail>();
	// public StringBuffer summary = new StringBuffer();	// This might be nice later.
	
	
	public int getId() { return this.id; }
	public int getCreator() { return this.creator; }
	public String getCreatorName() { return this.creatorName; }
	public int getPayer() { return this.payer; }
	public String getPayerName() { return this.payerName; }
	public int getOperator() { return this.operator; } 
	public Timestamp getActivity() { return this.activity; }
	public BigDecimal getAmount() { return this.amount; }
	public String getPaytype() { return this.paytype; }
	public Timestamp getPaydate() { return this.paydate; }
	public String getComment() { return this.comment; }
	public ArrayList<InvoiceDetail> getDetailList() { return this.detailList; } 
	public String getAuthcode() { return this.authcode; }
	public String getStatus() { return this.status; }
	public int getPostcount() { return this.postcount; }
}
