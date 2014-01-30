package com.stonekeep.congo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.stonekeep.congo.data.Category;

public class CategoryDAO extends GenericDAO<Category,Integer> {
	static Category dataType = new Category();
	static Integer fieldType = new Integer(0);
	private int cid = 0;		// injected
	
	public void setCid(int cid) { this.cid = cid; }

    @Override
    protected Class<Category> getDomainClass() {
        return Category.class;
    }
    
	public CategoryDAO() {
		super(dataType,fieldType);
		setTableName("config_Categorys");
		setPrimaryKey("id");
		setJoinSQL("left outer join reg_master " +
				"on Category_rid1=master_rid " +
				"left outer join reg_state " +
				"on Category_rid1=state_rid and state_cid=" + cid);
	}
	
	// Note this should use :named params

	@Transactional
	public List<Category> listAll() {
		Query q = sessionFactory.getCurrentSession().createQuery("from Category");
		return q.list();
	}
	
	@Transactional
	public void remove(Integer id) throws SQLException {
		Category f = new Category();
		f.setId(id);
		remove(f);
	}

	@Override
	public Category loadData(ResultSet r) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}