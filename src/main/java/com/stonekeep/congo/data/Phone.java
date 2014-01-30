package com.stonekeep.congo.data;

public class Phone implements Contact {
    public void setRid(int rid) {
        this.rid = rid;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public int rid;
    public String location;
    public String phone;
    public boolean primary;

    public int getRid() {
        return this.rid;
    }

    public String getLocation() {
        return this.location;
    }

    public String getPhone() {
        return this.phone;
    }

    public boolean getPrimary() {
        return this.primary;
    }

}
