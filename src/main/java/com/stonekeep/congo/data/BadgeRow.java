package com.stonekeep.congo.data;


public class BadgeRow {
    public void setBadgeIdentifier(String badgeid) {
        this.badgeIdentifier = badgeid;
    }
    
    public void setCid(int newcid) {
    	this.cid = newcid;
    }

    public void setSequence(int seq) {
    	this.seq = seq;
    }
    
    public void setPosx(int posx) {
    	this.posx=posx;
    }
    
    public void setPosy(int posy) {
    	this.posy=posy;
    }
    
    public void setFieldname(String fn) {
    	this.fieldname = fn;
    }
    
    public void setBackgroundImage(String fn) {
    	this.backgroundImage = fn;
    }
    
    public void setFontSize(int fs) {
    	this.fontsize = fs;
    }
    
    public void setWidth(int w) {
    	this.width=w;
    }
    
    public void setHeight(int h) {
    	this.height=h;
    }
    
    public void setAlignment(String a) {
    	this.alignment=a;
    }
    
    public void setStyle(String s){
    	this.style=s;
    }
    public void setFont(String f){
    	this.font=f;
    }
    public void setFontEncoding(String f){
    	this.fontEncoding=f;
    }
    public void setMaxWidth(float w){
    	this.maxWidth = w;
    }

    private int cid;
    private String badgeIdentifier;
    private int seq;
    private int posx;
    private int posy;
    private int width;
    private int height;
    private String fieldname;
    private String justification;
    private String backgroundImage;
    private String alignment;
    private int fontsize;
    private String fontEncoding;
    private String font;
    private String style;
    private float maxWidth;

	public int getCid() { 
		return this.cid;
	}
	
	public String getBadgeIdentifier() {
		return this.badgeIdentifier;
	}
	
	public int getSeq() {
		return this.seq;
	}
	
    public int getPosx() {
    	return this.posx;
    }
    
    public int getPosy() {
    	return this.posy;
    }
    
    public String getFieldname() {
    	return this.fieldname;
    }
    
    public String getBackgroundImage() {
    	return this.backgroundImage;
    }
    
    public int getFontSize() {
    	return this.fontsize;
    }
    
    public String getStyle(){
    	return this.style;
    }

	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public String getAlignment() {
		return this.alignment;
	}
	public String getFont(){
		return this.font;
	}
	public String getFontEncoding(){
		return this.fontEncoding;
	}
	public float getMaxWidth(){
		return this.maxWidth;
	}

}
