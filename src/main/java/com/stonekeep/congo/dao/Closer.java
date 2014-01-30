package com.stonekeep.congo.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

/**
 * This class is usually called from a DAO in the 'final' clause of a database interaction.  It is
 * designed to make sure all resultsets, preparedstatements, and database connections are closed
 * properly after an operation.  The database pooler (C3P0 at the moment) will re-use the connection
 * for other purposes.
 * 
 * @author dshevett
 */
public class Closer {
	private static Logger logger = Logger.getLogger(Closer.class);

	public static void close(ResultSet rs, Statement ps, Connection conn) {

		if (rs!=null)   {
			try {
				rs.close();
			}
			catch(SQLException e) {
				logger.error("The result set cannot be closed.", e);
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				logger.error("The statement cannot be closed.", e);
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("The data source connection cannot be closed.", e);
			}
		}

	}
}