package com.stonekeep.congo.data;

import java.sql.Timestamp;

/**
 * A class representing a history object.  This includes legacy data for now - we'll likely need
 * a conversion routine when we load in 'old' data into this.  For now, this matches the olds tructure.
 * @author dbs
 */
public class History {
	public int id;
	public int rid;
	public int cid;
	public int tid;
	public Timestamp activity;
	public int operator;
	public String actcode;
	public String comment;
	public String arg1;
	public String arg2;
	public String lastname;
	public String firstname;

	public int getId() { return this.id; }
	public int getRid() { return this.rid; }
	public int getCid() { return this.cid; }
	public int getTid() { return this.tid; }
	public Timestamp getActivity() { return this.activity; }
	public int getOperator() { return this.operator; }
	public String getActcode() { return this.actcode; }
	public String getComment() { return this.comment; }
	public String getArg1() { return this.arg1; }
	public String getArg2() { return this.arg2; }
	public String getLastname() { return this.lastname; }
	public String getFirstname() { return this.firstname; }

}
