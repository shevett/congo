package com.stonekeep.congo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.stonekeep.congo.data.Data;

public abstract class DAO <D extends Data> {
	private static Logger logger ;
	protected final DataSource dataSource;
	private String dataname;

	public DAO(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public abstract Data get(int whatcid, String name) throws SQLException;

	public abstract void save(D d) throws SQLException;

	public abstract void create(D d) throws SQLException ;

	public abstract Map<String, D> list(int whatcid) throws SQLException;

	protected abstract void loadData(D where, ResultSet fromwhat) throws SQLException;

}
