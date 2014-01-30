package com.stonekeep.congo.data;

public class Email implements Contact {
    public void setRid(int rid) {
        this.rid = rid;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public int rid;
    public String location;
    public String address;
    public boolean primary;

    public int getRid() {
        return this.rid;
    }

    public String getLocation() {
        return this.location;
    }

    public String getAddress() {
        return this.address;
    }

    public boolean getPrimary() {
        return this.primary;
    }

}
