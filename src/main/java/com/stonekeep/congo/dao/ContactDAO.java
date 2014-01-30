package com.stonekeep.congo.dao;

import java.sql.SQLException;

import com.stonekeep.congo.data.Contact;

public interface ContactDAO<T extends Contact> {

    public void create(T contact) throws SQLException;

    public void save(T contact,int rid) throws SQLException;

    public void delete(T contact) throws SQLException;

    public T lookup(int rid, String location) throws SQLException;

    public T createBlankContact();

}
