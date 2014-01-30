package com.stonekeep.congo.builders;

import javax.sql.DataSource;

import com.stonekeep.congo.dao.AddressDAO;
import com.stonekeep.congo.dao.EmailDAO;
import com.stonekeep.congo.dao.HistoryDAO;
import com.stonekeep.congo.dao.NoteDAO;
import com.stonekeep.congo.dao.PhoneDAO;
import com.stonekeep.congo.dao.RegistrantDAO;
import com.stonekeep.congo.dao.StateDAO;

public class RegistrantDAOBuilder  {
	protected RegistrantDAO registrantDAO;
	private final DataSource dataSource;
	private final StateDAO stateDAO;
	public final AddressDAO addressDAO;
	public final EmailDAO emailDAO;
	private final NoteDAO noteDAO;
	public final PhoneDAO phoneDAO;
	public final HistoryDAO historyDAO;

	public RegistrantDAOBuilder(DataSource dataSource, StateDAO stateDAO, AddressDAO addressDAO,
			EmailDAO emailDAO, NoteDAO noteDAO, PhoneDAO phoneDAO, HistoryDAO historyDAO) {
		this.dataSource = dataSource;
		this.stateDAO = stateDAO;
		this.addressDAO = addressDAO;
		this.emailDAO = emailDAO;
		this.noteDAO = noteDAO;
		this.phoneDAO = phoneDAO;
		this.historyDAO = historyDAO;
	}
	 
	public RegistrantDAO getRegistrantDAO() {
		return new RegistrantDAO(dataSource, stateDAO, addressDAO, emailDAO, noteDAO, phoneDAO, historyDAO);
	}
 
}
