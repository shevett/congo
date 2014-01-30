package com.stonekeep.congo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import org.springframework.transaction.annotation.Transactional;

import com.stonekeep.congo.data.Link;

public class LinkDAO extends GenericDAO<Link,Integer> {
	static Link dataType = new Link();
	static Integer fieldType = new Integer(0);
	static String[] arrayNames = {"one","two","three"};
	private int cid = 0;		// injected
	
	public void setCid(int cid) { this.cid = cid; }

    @Override
    protected Class<Link> getDomainClass() {
        return Link.class;
    }
    
	public LinkDAO() {
		super(dataType,fieldType);
		setTableName("reg_links");
		setPrimaryKey("link_rid1");
		setJoinSQL("left outer join reg_master " +
				"on link_rid1=master_rid " +
				"left outer join reg_state " +
				"on link_rid1=state_rid and state_cid=" + cid);
	}
	
	// Note this should use :named params
	@Transactional 
	public List<Link> listPending(Integer id) {
		Query q = sessionFactory.getCurrentSession().createQuery("from Link " +
				"where (linkRid1=:id OR linkRid2=:id2) " +
				"and linkStatus='Pending' order by linkRid1");
		q.setInteger("id", id);
		q.setInteger("id2",id);
		return q.list();
	}
	
	@Transactional
	public List<Link> listOk(Integer id) {
		Query q = sessionFactory.getCurrentSession().createQuery("from Link " +
				"where (linkRid1=:id or linkRid2=:id2) " +
				"and linkStatus='Ok' order by linkRid1");
		q.setInteger("id", id);
		q.setInteger("id2",id);
		return q.list();
	}
	
	@Transactional
	public List<Link> listBrowse(Integer id) {
		Query q = sessionFactory.getCurrentSession().createQuery("from Link " +
				"where (linkRid1=:id OR linkRid2=:id2) " +
				"and linkStatus='Ok' order by linkRid1");
		q.setInteger("id", id);
		q.setInteger("id2", id);
		return q.list();
	}
	
	@Transactional
	public void remove(Integer id) throws SQLException {
		Link f = new Link();
		f.setId(id);
		remove(f);
	}
	
	@Override
	public Link loadData(ResultSet r) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	

}