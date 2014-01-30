package com.stonekeep.congo.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.stonekeep.congo.data.Link;




public abstract class GenericDAO<T, ID extends Serializable> {
	public DataSource dataSource;	// Injectimundo.
	public SessionFactory sessionFactory;	//injected

	private Logger logger = Logger.getLogger(getClass());
	
    protected Class<T> domainClass = getDomainClass();

    /**
     * Method to return the class of the domain object
     */
    protected abstract Class<T> getDomainClass();

	private T dataType;
	private ID fieldType;
	
	private String tableName;
	private String primaryKey;
	private String joinSQL = new String("");
	private String andSQL = new String("");
	
	public void setTableName(String newName) {
		logger.debug("Setting table name to " + newName);
		this.tableName = newName;
	}
	
	public void setDataSource(DataSource ds) {
		this.dataSource = ds;
	}
	
	public void setJoinSQL(String sql) { 
		this.joinSQL = sql;
	}
	
	public void setAndSQL(String sql) {
		this.andSQL = sql;
	}
	
	public DataSource getDataSource() {
		return this.dataSource;
	}
	
	public void setPrimaryKey(String columnName) {
		logger.debug("Setting primary column key to " + columnName);
		this.primaryKey = columnName;
	}


	public GenericDAO(T dataType,ID fieldType) {
		logger.debug("Instantiated...");
		this.dataType = dataType;
		this.fieldType = fieldType;
		logger.debug("Type is " + dataType.getClass().getName());
		logger.debug("Field is " + fieldType.getClass().getName());
	}


	@SuppressWarnings("unchecked")
	public List<T> findAll() throws SQLException {
		logger.debug("findAll: List all " + dataType.getClass().getName());
		List resultList = new ArrayList();
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = c.prepareStatement("select * from " + tableName);
			rs = p.executeQuery();
			while (rs.next()) {
				resultList.add(loadData(rs));
			}
			logger.debug("Found " + resultList.size() + " entries.");
		} finally {
			Closer.close(rs,p,c);
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findAllById(ID key) throws SQLException {
		logger.debug("findAllByID: List all " + dataType.getClass().getName() + " with key of " + key);
		List resultList = new ArrayList();
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = c.prepareStatement("select * from " + tableName + " " + joinSQL + " where " + primaryKey + "=?");
			p.setObject(1,key);
			logger.debug("Looking for column '" + primaryKey + "' to have value '" + key + "' in table '" + tableName + "'");
			rs = p.executeQuery();
			while (rs.next()) {
				resultList.add(loadData(rs));
			}
			logger.debug("Found " + resultList.size() + " friends for " + key);
		} finally {
			Closer.close(rs,p,c);
		}
		return resultList;
	}
	
	public abstract T loadData(ResultSet r) throws SQLException ;
	
	// Everything below here implements Hibernate functionality...
	
	public void setSessionFactory(SessionFactory s) {
		this.sessionFactory = s;
	}

	
	@Transactional
	public void remove(T f) throws SQLException {
		sessionFactory.getCurrentSession().delete(f);
	}
	
	@Transactional
    public T add(T f) {
        sessionFactory.getCurrentSession().save(f);
        return f;
    }
	
	@Transactional
	public T update(T f) {
		sessionFactory.getCurrentSession().update(f);
		return f;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public T getById(ID i) {
		return (T)sessionFactory.getCurrentSession().get(domainClass,i);
	}
	
    @SuppressWarnings("unchecked")
    @Transactional
    public T load(ID id) {
        return (T) sessionFactory.getCurrentSession().load(domainClass, id);
    }
    	
}

