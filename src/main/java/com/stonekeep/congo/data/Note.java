package com.stonekeep.congo.data;

import java.sql.Timestamp;

public class Note {
	public int id;
	public int rid;
	public int cid;
	public int postRid;
	public int acknowledgeRid;
	public Timestamp postDate;
	public Timestamp acknowledgeDate;
	public String message;
	public String type;
	
	public int getId() { return this.id; }
	public int getRid() { return this.rid; }
	public int getCid() { return this.cid; }
	public int getPostRid() { return this.postRid; }
	public int getAcknowledgeRid() { return this.acknowledgeRid; }
	public Timestamp getPostDate() { return this.postDate; }
	public Timestamp getAcknowledgeDate() { return this.acknowledgeDate; }
	public String getMessage() { return this.message; }
	public String getType() { return this.type; }

}
