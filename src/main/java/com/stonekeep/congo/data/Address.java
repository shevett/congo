package com.stonekeep.congo.data;

public class Address implements Contact {
    public void setRid(int rid) {
        this.rid = rid;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
    
    public void setCountry(String country) {
    	this.country = country;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public int rid;
    public String location;
    public String line1;
    public String line2;
    public String city;
    public String state;
    public String zipcode;
    public String country;
    public boolean primary;

    public int getRid() {
        return this.rid;
    }

    public String getLocation() {
        return this.location;
    }

    public String getLine1() {
        return this.line1;
    }

    public String getLine2() {
        return this.line2;
    }

    public String getCity() {
        return this.city;
    }

    public String getState() {
        return this.state;
    }

    public String getZipcode() {
        return this.zipcode;
    }
    
    public String getCountry() {
    	return this.country;
    }

    public boolean getPrimary() {
        return this.primary;
    }

}
