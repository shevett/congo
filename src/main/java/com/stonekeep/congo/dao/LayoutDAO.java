package com.stonekeep.congo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.stonekeep.congo.data.Layout;

public class LayoutDAO extends GenericDAO<Layout,Integer> {
	static Layout dataType = new Layout();
	static Integer fieldType = new Integer(0);
	private int cid = 0;		// injected
	
	public void setCid(int cid) { this.cid = cid; }

    @Override
    protected Class<Layout> getDomainClass() {
        return Layout.class;
    }
    
	public LayoutDAO() {
		super(dataType,fieldType);
		setTableName("config_layouts");
		setPrimaryKey("id");
		setJoinSQL("left outer join reg_master " +
				"on Layout_rid1=master_rid " +
				"left outer join reg_state " +
				"on Layout_rid1=state_rid and state_cid=" + cid);
	}
	
	// Note this should use :named params

	@Transactional
	public List<Layout> listAll() {
		Query q = sessionFactory.getCurrentSession().createQuery("from Layout");
		return q.list();
	}
	
	@Transactional
	public void remove(Integer id) throws SQLException {
		Layout f = new Layout();
		f.setId(id);
		remove(f);
	}

	@Override
	public Layout loadData(ResultSet r) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}