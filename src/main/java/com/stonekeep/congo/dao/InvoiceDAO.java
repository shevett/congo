package com.stonekeep.congo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.stonekeep.congo.data.DiscountCode;
import com.stonekeep.congo.data.Invoice;
import com.stonekeep.congo.data.InvoiceDetail;

public class InvoiceDAO {
	private final DataSource dataSource;
	private static Logger logger = Logger.getLogger(InvoiceDAO.class);
	private final DiscountCodeDAO discountCodeDAO;
	
	public enum IncludeTypes { 	PAID,READY,VOID; }

	// Invoices queries...
//	private final String SQL_LIST_INVOICE = "select *,count(detail_invoice) as post,reg_master.master_lastname,reg_master.master_firstname "
//		+ " from invoices "
//		+ "LEFT OUTER JOIN reg_master ON invoice_creator = master_rid "
//		+ "LEFT OUTER JOIN invoice_detail ON (invoice_id=detail_invoice AND detail_postprocess) "
//		+ "ORDER BY invoice_id DESC";
	
	private final String SQL_LIST_INVOICE = "SELECT SQL_CALC_FOUND_ROWS *,post.postcount " 
		+ "from invoices "
		+ "LEFT OUTER JOIN reg_master ON invoice_creator = master_rid "
		+ "left outer join (select detail_invoice,count(*) as postcount from invoice_detail where detail_postprocess GROUP BY detail_invoice) post "
		+ "ON (post.detail_invoice = invoice_id) "
		+ "WHERE (invoice_status LIKE ? "
		+ "  OR invoice_status LIKE ? "
		+ "  OR invoice_status LIKE ?) "
		+ "AND (master_firstname LIKE ? OR master_lastname LIKE ?)  "
		+ "ORDER BY invoice_id DESC "
		+ "LIMIT ?,?";

	private final String SQL_LIST_INVOICES_FOR_REGISTRANT = "select *,post.postcount " 
		+ "from invoices "
		+ "LEFT OUTER JOIN reg_master ON invoice_creator = master_rid "
		+ "left outer join (select detail_invoice,count(*) as postcount from invoice_detail where detail_postprocess group by detail_invoice) post "
		+ "ON (post.detail_invoice = invoice_id) "
		+ "WHERE invoice_creator=? OR invoice_payer=? "
		+ "ORDER BY invoice_id DESC";

	private final String SQL_DELETE_INVOICE = "DELETE FROM invoices "
		+ "WHERE invoice_id=?";
	private final String SQL_UPDATE_INVOICE = "UPDATE invoices SET "
		+ "invoice_creator=?,invoice_amount=?,invoice_payer=?,invoice_paytype=?,"
		+ "invoice_paydate=?,invoice_operator=?,invoice_comment=?,invoice_authcode=?,invoice_status=? "
		+ "WHERE invoice_id=?";
	private final String SQL_GET_INVOICE = "SELECT * FROM invoices,reg_master " +
	"where invoice_creator = master_rid AND invoice_id=?";

	// Invoice detail queries...
	private final String SQL_LIST_INVOICEDETAIL = "SELECT * FROM invoice_detail " +
		"left outer join reg_master on (detail_rid = master_rid) " +
		"left outer join config_discountcodes on ((detail_type2 = dc_name) AND (detail_cid = dc_cid)) " +
	"WHERE detail_invoice=? ";
	private final String SQL_UPDATE_INVOICEDETAIL = "UPDATE invoice_detail SET "
		+ "detail_id=? , detail_rid=?, detail_cid=?, detail_invoice=?, detail_sequence=?, detail_operator=?, "
		+ "detail_type=?, detail_type2=?, detail_description=?, detail_amount=?, detail_comment=?, "
		+ "detail_discount=?, detail_final=?, detail_activity=?, detail_postprocess=? "
		+ "WHERE detail_id=?";
	private final String SQL_DELETE_INVOICEDETAIL = "DELETE FROM invoice_detail "
		+ "WHERE detail_invoice=? AND detail_sequence=?";
	private final String SQL_GET_INVOICE_DETAIL = "SELECT * FROM invoice_detail " +
			"left outer join reg_master on (detail_rid=master_rid) " +
			"where detail_id=?";
	private final String SQL_GET_REVENUE = "SELECT sum(detail_final) as s from invoice_detail " +
			"LEFT OUTER JOIN invoices ON detail_invoice = invoice_id " +
			"where detail_cid=? and detail_type='REGISTRATION' and detail_type2=? " +
			"AND invoice_status='PAID'";

	// Maintenance calls and update stuff.
	private final String SQL_RECALCULATE_DETAIL = "UPDATE invoice_detail "
		+ "SET detail_final=detail_amount - detail_discount "
		+ "WHERE detail_invoice=?";
	private final String SQL_RECALCULATE_INVOICE = "UPDATE invoices "
		+ "SET invoice_amount = (SELECT sum(detail_final) FROM invoice_detail "
		+ "WHERE detail_invoice=?) where invoice_id=?";
	private final String SQL_NEXT_SEQUENCE = "SELECT MAX(detail_sequence)+1 AS nextsequence FROM "
		+ "invoice_detail WHERE detail_invoice=?";

	/* ----------------------------------------------------------- */

	public InvoiceDAO(DataSource dataSource, DiscountCodeDAO discountCodeDAO) {
		this.dataSource = dataSource;
		this.discountCodeDAO = discountCodeDAO;
	}

	public Invoice getInvoice(int whatInvoice) throws SQLException {
		logger.debug("Retrieving invoice # " + whatInvoice);
		Invoice i = null;
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet rset = null;
		try {
			p = c.prepareStatement(SQL_GET_INVOICE);
			p.setInt(1, whatInvoice);
			rset = p.executeQuery();
			while (rset.next()) {
				i = new Invoice();
				loadInvoice(i, rset);
			}
		} finally {
			Closer.close(rset,p,c);
		}
		logger.debug("Returning " + i);
		return i;

	}
	
	public InvoiceDetail getInvoiceDetail(int whatDetail) throws SQLException {
		logger.debug("Retrieving invoice detail id #" + whatDetail);
		InvoiceDetail id = null;
		Connection c = dataSource.getConnection();
		ResultSet rset = null;
		PreparedStatement p = null;
		try {
			p = c.prepareStatement(SQL_GET_INVOICE_DETAIL);
			p.setInt(1,whatDetail);
			rset = p.executeQuery();
			while (rset.next()) {
				id = new InvoiceDetail();
				loadInvoiceDetail(id,rset);
			}
		} finally {
			Closer.close(rset,p,c);
		}
		return id;
	}
	
	public List<Invoice> listInvoices(int cid,int skip, int limit, EnumSet<InvoiceDAO.IncludeTypes> toSelect,String s) throws SQLException {
		logger.debug("Retrieving invoices for event " + cid + " skipping " + skip + " limited to " + limit + " rows.");
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet rset = null;
		ArrayList<Invoice> invoices = new ArrayList<Invoice>();
		try {
			p = c.prepareStatement(SQL_LIST_INVOICE);
			if (toSelect.contains(IncludeTypes.PAID)) {	p.setString(1,"PAID"); } else {p.setString(1,""); }
			if (toSelect.contains(IncludeTypes.VOID)) {	p.setString(2,"VOID"); } else {p.setString(2,""); }
			if (toSelect.contains(IncludeTypes.READY)) {	p.setString(3,"READY"); } else {p.setString(3,""); }
			p.setString(4,"%"+s+"%");
			p.setString(5,"%"+s+"%");
			p.setInt(6,skip);
			p.setInt(7,limit);
			rset = p.executeQuery();
			while (rset.next()) {
				Invoice row = new Invoice();
				loadInvoice(row, rset);
				row.postcount=rset.getInt("post.postcount");
				row.creatorName = rset.getString("master_lastname") + " "
				+ rset.getString("master_firstname")
				+ " (" + rset.getString("master_badgename") + ")";
				invoices.add(row);
			}

		} finally {
			Closer.close(rset, p, c);
		}
		logger.debug("invoice list is " + invoices.size() + " elements long.");
		return invoices;	}
	
	public List<Invoice> listInvoices(int cid,int rid) throws SQLException {
		logger.debug("Retrieving invoices for event " + cid + " for registrant " + rid);
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet rset = null;
		ArrayList<Invoice> invoices = new ArrayList<Invoice>();
		try {
			p = c.prepareStatement(SQL_LIST_INVOICES_FOR_REGISTRANT);
			p.setInt(1,rid);
			p.setInt(2,rid);
			rset = p.executeQuery();
			while (rset.next()) {
				Invoice row = new Invoice();
				loadInvoice(row, rset);
				row.postcount=rset.getInt("post.postcount");
				row.creatorName = rset.getString("master_lastname") + " "
				+ rset.getString("master_firstname")
				+ " (" + rset.getString("master_badgename") + ")";
				invoices.add(row);
			}
		} finally {
			Closer.close(rset,p,c);
		}
		logger.debug("invoice list is " + invoices.size() + " elements long.");
		return invoices;
	}

	public ArrayList<InvoiceDetail> listInvoiceDetails(int invoiceId)
			throws SQLException {
		logger.debug("Retrieving invoice detail for invoice " + invoiceId);
		ArrayList<InvoiceDetail> dl = new ArrayList<InvoiceDetail>();
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = c.prepareStatement(SQL_LIST_INVOICEDETAIL);
			p.setInt(1, invoiceId);
			rs = p.executeQuery();
			while (rs.next()) {
				InvoiceDetail id = new InvoiceDetail();
				loadInvoiceDetail(id, rs);
				dl.add(id);
			}
		} finally {
			Closer.close(rs,p,c);
		}
		return dl;
	}

	public Invoice createInvoice() throws SQLException {
		Connection c = dataSource.getConnection();
		ResultSet r = null;
		Invoice i = new Invoice();
		try {
			String sql = "INSERT into invoices values ()";
			Statement s = c.createStatement();
			s.execute(sql, Statement.RETURN_GENERATED_KEYS);
			r = s.getGeneratedKeys();
			while (r.next()) {
				Integer invoiceid = r.getInt(1);
				logger.debug("Generated invoice number: " + invoiceid);
				i.id = invoiceid;
			}
		} finally {
			Closer.close(r,null,c);
			c.close();
		}
		return i;
	}

	public InvoiceDetail createInvoiceDetail() throws SQLException {
		Connection c = dataSource.getConnection();
		ResultSet r = null;
		InvoiceDetail i = new InvoiceDetail();
		try {
			String sql = "INSERT into invoice_detail values ()";
			Statement s = c.createStatement();
			s.execute(sql, Statement.RETURN_GENERATED_KEYS);
			r = s.getGeneratedKeys();
			while (r.next()) {
				Integer invoicedetailid = r.getInt(1);
				i.id = invoicedetailid;
			}
		} finally {
			Closer.close(r,null,c);
		}
		return i;
	}

	public void saveInvoice(Invoice i) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		try {
			p = c.prepareStatement(SQL_UPDATE_INVOICE);
			p.setInt(1, i.creator);
			p.setBigDecimal(2, i.amount);
			p.setInt(3, i.payer);
			p.setString(4, i.paytype);
			p.setTimestamp(5, i.paydate);
			p.setInt(6, i.operator);
			p.setString(7, i.comment);
			p.setString(8, i.authcode);
			p.setString(9, i.status);
			p.setInt(10, i.id);
			p.executeUpdate();
		} finally {
			Closer.close(null,p,c);
		}
	}

	public void saveInvoiceDetail(InvoiceDetail id) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		try {
			p = c.prepareStatement(SQL_UPDATE_INVOICEDETAIL);
			p.setInt(1, id.id);
			p.setInt(2, id.rid);
			p.setInt(3, id.cid);
			p.setInt(4, id.invoice);
			p.setInt(5, id.sequence);
			p.setInt(6, id.operator);
			p.setString(7, id.type);
			p.setString(8, id.type2);
			if (id.description != null && id.description.length() > 34) {
				id.description = id.description.substring(1,34);
			}
			p.setString(9, id.description);
			p.setBigDecimal(10, id.amount);
			p.setString(11, id.comment);
			p.setBigDecimal(12, id.discount);
			p.setBigDecimal(13, id.finalamount);
			p.setTimestamp(14, id.activity);
			p.setBoolean(15,id.postprocess);
			p.setInt(16, id.id);
			p.executeUpdate();
		} finally {
			Closer.close(null,p,c);
		}
	}

	public void deleteInvoice(int id) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		try {
			p = c.prepareStatement(SQL_DELETE_INVOICE);
			p.setInt(1, id);
			p.execute();
		} finally {
			Closer.close(null,p,c);
		}
	}
	
	public void deleteDetailItem(int invoiceid,int sequence) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement p= null;
		try {
			p = c.prepareStatement(SQL_DELETE_INVOICEDETAIL);
			p.setInt(1,invoiceid);
			p.setInt(2,sequence);
			p.execute();
		} finally {
			Closer.close(null,p,c);
		}
	}

	private void loadInvoice(Invoice i, ResultSet rset) throws SQLException {
		i.activity = rset.getTimestamp("invoice_activity");
		i.status = rset.getString("invoice_status");
		i.amount = rset.getBigDecimal("invoice_amount");
		i.authcode = rset.getString("invoice_authcode");
		i.comment = rset.getString("invoice_comment");
		i.creator = rset.getInt("invoice_creator");
		i.id = rset.getInt("invoice_id");
		i.operator = rset.getInt("invoice_operator");
		i.paydate = rset.getTimestamp("invoice_paydate");
		i.payer = rset.getInt("invoice_payer");
		i.paytype = rset.getString("invoice_paytype");
		if ((rset.getString("master_lastname") != null) && (rset.getString("master_firstname") != null)) {
			i.creatorName= rset.getString("master_lastname") 
					+ ", " + rset.getString("master_firstname")
					+ " (" + rset.getString("master_badgename") + ")";
		}
	}

	private void loadInvoiceDetail(InvoiceDetail id, ResultSet rset)
	throws SQLException {
		id.activity = rset.getTimestamp("detail_activity");
		id.amount = rset.getBigDecimal("detail_amount");
		id.cid = rset.getInt("detail_cid");
		id.comment = rset.getString("detail_comment");
		id.description = rset.getString("detail_description");
		id.discount = rset.getBigDecimal("detail_discount");
		id.finalamount = rset.getBigDecimal("detail_final");
		id.id = rset.getInt("detail_id");
		id.invoice = rset.getInt("detail_invoice");
		id.operator = rset.getInt("detail_operator");
		id.rid = rset.getInt("detail_rid");
		id.sequence = rset.getInt("detail_sequence");
		id.type = rset.getString("detail_type");
		id.type2 = rset.getString("detail_type2");
		id.postprocess = rset.getBoolean("detail_postprocess");
		if ((rset.getString("master_lastname") != null) && (rset.getString("master_firstname") != null)) {
			id.fullname = rset.getString("master_lastname") + ", " 
					+ rset.getString("master_firstname") 
					+ " (" + rset.getString("master_badgename") + ")";
			logger.debug("Added " +id.fullname);
		}
	}

	/*
	 * Recalculate an invoice's total amount, as well as all the Detail items
	 * based on discounts and the like.
	 */

	public void recalculate(int whatInvoice) throws SQLException {
		logger.info("Recalculating invoice " + whatInvoice);

		// Pull invoidedetail list and iterate to process discount codes...
		List<InvoiceDetail> l = listInvoiceDetails(whatInvoice);
		List<DiscountCode> dlist = new ArrayList<DiscountCode>();
		
		// Pull a list of discount codes from the ID in order, store them in the arraylist...
		for (InvoiceDetail workingId : l) {
			if (workingId.type.equals("DISCOUNT")) {
				String dcode = workingId.type2;
				// Look up this discount code and apply the discount
				DiscountCode dc = discountCodeDAO.get(workingId.cid, dcode);
				if (dc != null) {
					dlist.add(dc);
					logger.debug("Adding requested discount " + workingId.type + " to dlist...");
				
					// Also update the invoice row to have the description, while we're here.
					workingId.description = dc.getDesc();
					saveInvoiceDetail(workingId);
				} else {
					logger.warn("Invalid discount code " + dcode + " attempted processing invoice " + whatInvoice + ".  Deleting from invoice.");
					deleteDetailItem(workingId.id,workingId.sequence);
				}
			}
		}
		
		// Discount processing may have purged items from the invoice.  Reload it...
		l = listInvoiceDetails(whatInvoice);

		
		// Now go through the existing Registrations and apply each discount code in order
		int dlistPosition = 0;
		logger.debug("Discount list size is " + dlist.size());
		for (InvoiceDetail workingId : l) {
			if (workingId.type.equals("REGISTRATION")) {
				logger.debug("Analyzing invoice line for REGISTRATION " + workingId.description);
				if (dlistPosition < dlist.size()) {
					// We have a discount, we have a registration.  Apply it.
					DiscountCode dc = dlist.get(dlistPosition);
					logger.debug("Discount type in progress is " + dc.getType());
					if (dc.getType().equalsIgnoreCase("Discount")) {
						workingId.discount = dc.getFactor();
					}
					if (dc.getType().equalsIgnoreCase("Percent")) {
						workingId.discount = workingId.amount.multiply(dc.getFactor());
					}
					if (dc.getType().equalsIgnoreCase("Absolute")) {
						workingId.discount = workingId.amount.subtract(dc.getFactor());
					}
					logger.debug("post application, ID discount is " + workingId.discount);
					
					// Since this ID has been recalced, we need to save it...
					saveInvoiceDetail(workingId);
					
					dlistPosition++;
				}
			}
		}
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		try {
			
			p = c.prepareStatement(SQL_RECALCULATE_DETAIL);
			p.setInt(1, whatInvoice);
			p.execute();
			p.close();

			p = c.prepareStatement(SQL_RECALCULATE_INVOICE);
			p.setInt(1, whatInvoice);
			p.setInt(2, whatInvoice);
			p.execute();
		} finally {
			Closer.close(null,p,c);
		}

	}

	/*
	 * Get the next sequence number for a new detail item.
	 */
	public int getNextSequence(int invoiceNumber) throws SQLException {
		logger.debug("Calculating next sequence number for a detail item...");
		PreparedStatement p = null;
		ResultSet rset = null;
		int nextsequence = 1;
		Connection c = dataSource.getConnection();
		try {
			p = c.prepareStatement(SQL_NEXT_SEQUENCE);
			p.setInt(1, invoiceNumber);
			rset = p.executeQuery();
			if (rset.next()) {
				nextsequence = rset.getInt("nextsequence");
			}
		} finally {
			Closer.close(rset,p,c);
		}
		return nextsequence;
	}
	
	/*
	 * Give a quick revenue sales total for a given registration type and event...
	 */
	public BigDecimal getRevenue(int cid, String rtname) throws SQLException {
		PreparedStatement p = null;
		Connection c = dataSource.getConnection();
		ResultSet rset = null;
		BigDecimal returnvalue = new BigDecimal(0);
		try {
			p = c.prepareStatement(SQL_GET_REVENUE);
			p.setInt(1,cid);
			p.setString(2,rtname);
			rset = p.executeQuery();
			if (rset.next()) {
				returnvalue = rset.getBigDecimal("s");
			}
		} finally {
			Closer.close(rset,p,c);
		}
		logger.debug("Calculated income for event " + cid + " + for regtype " + rtname + " is " + returnvalue) ;
		if (returnvalue == null) {
			return new BigDecimal(0);
		} else {
			return returnvalue;
		}
	}

	/*
	 * salesByType - Generate a report showing sales broken
	 * down by sales type
	 */
	public Map<String,BigDecimal[]> salesByType(int cid) throws SQLException {
		Map salesData = new TreeMap();
		logger.debug("Generating salesByType for event " + cid);
		Connection c = dataSource.getConnection();
		PreparedStatement p = null;
		ResultSet rs = null;
		int counter = 0;

		try {

			// Create a temporary table that has all the invoices that had transactions from this year...
			p = c.prepareStatement("CREATE TEMPORARY TABLE IF NOT EXISTS sbt (sbt_invoice int(10), sbt_type varchar(20), sbt_amount decimal(10,0))");
			p.execute();
			p.close();
			
			// Make sure we have an empty table
			p = c.prepareStatement("DELETE FROM sbt");
			p.execute();
			p.close();
			
			// Now load the temp table with the invoice numbers, types, and amounts.
			p = c.prepareStatement("INSERT INTO sbt " +
					"select distinct detail_invoice, invoice_paytype, invoice_amount " +
					"from invoice_detail " +
					"left outer join invoices " +
					"on invoice_id=detail_invoice " +
					"where detail_cid=? and invoice_amount > 0");
			p.setInt(1,cid);
			p.execute();
			logger.debug("Rowcount from temp table populate : " + p.getUpdateCount());
			p.close();
			
			// Okay, now pull out the data in summarized form
			p = c.prepareStatement("select sbt_type,count(sbt_type),sum(sbt_amount) from sbt group by sbt_type");
			rs = p.executeQuery();
			
			// Now populate that able with detail data 
			while (rs.next()) {
				counter++;
				if (rs.getString("sbt_type") != null) {
					BigDecimal[] bd = new BigDecimal[10];
					bd[0] = rs.getBigDecimal(2);
					bd[1] = rs.getBigDecimal(3);
					salesData.put(rs.getString("sbt_type"),bd);
				}
			}
		} finally {
			Closer.close(rs,p,c);
		}
		logger.debug("Iterated over " + counter + " rows, Size of salesByType map : " + salesData.size());
		return salesData;
	}
}
