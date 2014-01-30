package com.stonekeep.congo.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.util.ServletContextAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.stonekeep.congo.dao.ConventionDAO;
import com.stonekeep.congo.data.Convention;

public class CSSFetcher extends ActionSupport implements ServletResponseAware, ServletContextAware {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(CSSFetcher.class);
	private Map sessionData;
	private HttpServletResponse resp;
	private ServletContext sc;
	private final ConventionDAO conventionDAO;
	private int cid = 0;

	public CSSFetcher(ConventionDAO conventionDAO) {
		this.conventionDAO = conventionDAO;
		logger.debug("CSSFetcher instantiated.");
	}
	
	public void setCid(int cid) { 
		this.cid = cid; }
	
	/*
	 * This method puts a stylesheet, as pulled from the convention details
	 * table, out on the HTTP response stream.  This is so a CSS 'page' can
	 * be loaded dynamically from the JSP just by referencing this as an action
	 * from the JSP header.
	 */
	public String execute() throws IOException, SQLException {
		logger.debug("Fetching con-specific stylesheet information...");
		sessionData = ActionContext.getContext().getSession();
		Convention currentConvention = (Convention)sessionData.get("currentConvention");
		if (currentConvention == null) {
			logger.error("Alert!  No convention loaded into session!  Aieeee!");
		}
		StringBuffer output = new StringBuffer(currentConvention.conStylesheet);
		resp.getWriter().write(output.toString());
		return null;
	}
	
	public void getConfiguredStylesheet() throws IOException, Exception {
		logger.debug("Fetching configured stylesheet...");
		sessionData = ActionContext.getContext().getSession();
		
		resp.setContentType("text/css");
		
		InputStream is = sc.getResourceAsStream("/public/web-emphasis.css");
		try {
			logger.debug("input stream is " + is);
			OutputStream out = resp.getOutputStream();
			out.write("/* Read via CSSFetcher from web-emphasis.css */\n".getBytes("US-ASCII"));
			IOUtils.copy(is,out);
		} catch (Exception e) {
			logger.error(e);
			logger.error(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			is.close();
		}
//		
//		// Open up the .css file specified in the event config... (currently hardcoded)
//		try {
//			InputStream is = sc.getResourceAsStream("/public/web-emphasis.css");
//			logger.debug("inputstream for stylesheet should not be null.  It is: " + is);
//			BufferedReader br = new BufferedReader(new InputStreamReader(is));
//			logger.debug("Setting up sb with basic stylesheet info...");
//			StringBuffer sb = new StringBuffer("/* Read via CSSFetcher from web-emphasis.css */\n");
//			String s ;
//			int counter=0;
//			while ((s = br.readLine()) != null)   {
//				counter++;
//				sb.append(s + "\n");
//			}
//			logger.debug("Stringbuffer counted " + counter + " lines, and is " + sb.length() + " bytes long.");
//			is.close();
//			logger.debug("Writing it to the response...");
//			resp.getWriter().write(sb.toString());
//			resp.getWriter().flush();
//			resp.getWriter().close();
//		} 
//		}
	}
	
	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		resp = arg0;
	}

	@Override
	public void setServletContext(ServletContext arg0) {
		sc = arg0;
	}

}
