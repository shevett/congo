package com.stonekeep.congo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.stonekeep.congo.data.Note;

/**
 * DAO for Note objects
 * 
 * @author dbs
 * @author ojacobson
 */
public class NoteDAO {
	private static Logger logger = Logger.getLogger(NoteDAO.class);
	private final DataSource dataSource;

	public NoteDAO(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Note getNote(int whatrid, int whatnote) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet rset = null;
		Note a = null;
		try {
			logger.debug("Fetching note #" + whatnote + " from rid #"
							+ whatrid);
			String sql = "SELECT * FROM reg_notes WHERE note_rid=? and note_id=?";

			p = c.prepareStatement(sql);
			p.setInt(1, whatrid);
			p.setInt(2, whatnote);
			rset = p.executeQuery();
			if (rset.next()) {
				// Got one. Load it.
				a = new Note();
				loadData(a, rset);
			}
		} finally {
			Closer.close(rset,p,c);
		}
		return a;
	}

	public void save(Note n) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		try {
			String sql = "UPDATE reg_notes SET " + "note_id=?, "
					+ "note_rid=?, " + "note_cid=?, " + "note_postrid=?, "
					+ "note_postdate=?, " + "note_ackrid=?, "
					+ "note_ackdate=?, " + "note_message=?, " + "note_type=? "
					+ "WHERE note_id=?";
			p = c.prepareStatement(sql);
			p.setInt(1, n.id);
			p.setInt(2, n.rid);
			if (n.cid == 0) {
				p.setNull(3, Types.INTEGER);
			} else {
				p.setInt(3, n.cid);
			}
			if (n.postRid == 0) {
				p.setNull(4, Types.INTEGER);
			} else {
				p.setInt(4, n.postRid);
			}
			p.setTimestamp(5, n.postDate);
			if (n.acknowledgeRid == 0) {
				p.setNull(6, Types.INTEGER);
			} else {
				p.setInt(6, n.acknowledgeRid);
			}
			p.setTimestamp(7, n.acknowledgeDate);
			p.setString(8, n.message);
			p.setString(9, n.type);
			p.setInt(10, n.id);

			logger.debug(p.executeUpdate() + " rows updated.");
		} finally {
			Closer.close(null,p,c);
		}
	}

	public void create(Note n) throws SQLException {
		logger.debug("Creating new Note for registrant " + n.rid);
		Connection c = dataSource.getConnection();
		ResultSet r = null;
		try {
			// String sql = "INSERT INTO reg_notes (note_rid,note_cid) VALUES ("
			// +
			// n.rid + ", " + n.cid + ")";
			String sql = "INSERT INTO reg_notes (note_postdate) VALUES (now())";
			Statement s = c.createStatement();
			s.execute(sql, Statement.RETURN_GENERATED_KEYS);
			r = s.getGeneratedKeys();
			r.next();
			n.id = r.getInt(1);
			n.postDate = new java.sql.Timestamp(System.currentTimeMillis());
			save(n);
		} finally {
			Closer.close(r,null,c);
		}
	}

	public Map<Integer, Note> list(int whatrid) throws SQLException {
		return getList(whatrid, "SELECT * from reg_notes where note_rid=?");
	}

	public Map<Integer, Note> listNotices(int whatrid) throws SQLException {
		return getList(whatrid,
				"SELECT * from reg_notes where note_rid=? AND note_type='NOTICE' AND note_ackrid IS NULL");
	}

	private Map<Integer, Note> getList(int whatrid, String sql)
			throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet rset = null;
		HashMap<Integer, Note> allNotes = new HashMap<Integer, Note>();
		try {
			p = c.prepareStatement(sql);
			p.setInt(1, whatrid);
			rset = p.executeQuery();
			while (rset.next()) {
				Note row = new Note();
				loadData(row, rset);
				allNotes.put(new Integer(row.id), row);
				logger.debug("Adding id " + row.id + " with message "
						+ row.message);
			}
		} finally {
			Closer.close(rset,p,c);
		}
		return allNotes;
	}

	public void delete(int whatrid, int whatid) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		try {
			String sql = "DELETE from reg_notes where note_rid=? and note_id=?";
			p = c.prepareStatement(sql);
			p.setInt(1, whatrid);
			p.setInt(2, whatid);
			p.execute();
			p.close();
		} finally {
			Closer.close(null, p, c);
		}
	}

	private static void loadData(Note where, ResultSet fromwhat)
			throws SQLException {
		where.id = fromwhat.getInt("note_id");
		where.rid = fromwhat.getInt("note_rid");
		where.cid = fromwhat.getInt("note_cid");
		where.postRid = fromwhat.getInt("note_postrid");
		where.postDate = fromwhat.getTimestamp("note_postdate");
		where.acknowledgeRid = fromwhat.getInt("note_ackrid");
		where.acknowledgeDate = fromwhat.getTimestamp("note_ackdate");
		where.message = fromwhat.getString("note_message");
		where.type = fromwhat.getString("note_type");
	}
}