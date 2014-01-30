package com.stonekeep.congo.coconut;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.stonekeep.congo.dao.ConventionDAO;
import com.stonekeep.congo.dao.RegistrantDAO;
import com.stonekeep.congo.dao.StateDAO;
import com.stonekeep.congo.data.Badge;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Registrant;
import com.stonekeep.congo.util.SystemBadge;

public class PreviewBadge extends ActionSupport implements ServletResponseAware,SessionAware {
	private static final long serialVersionUID = 1L;
	public Map<String, Object> sessionData;
	private Logger logger = Logger.getLogger(PreviewBadge.class);
	private HttpServletResponse response;
	
	public int rid;
	public int cid;
	
	public String tmpdir = "/tmp/";
	public String imagedir = "/tmp/imagedir/";
	
	public final StateDAO stateDAO;
	public final RegistrantDAO registrantDAO;
	public final ConventionDAO conventionDAO;
	
	// Passed as an argument on the HTTP URL.
	public void setRid(int rid) { this.rid = rid; }
	
	public PreviewBadge(RegistrantDAO registrantDAO, StateDAO stateDAO, ConventionDAO conventionDAO) {
		this.registrantDAO = registrantDAO;
		this.stateDAO = stateDAO;
		this.conventionDAO = conventionDAO ;
	}

	public String execute() throws Exception {
		response.setContentType("application/pdf");
		logger.debug("Running preview for registrant " + rid);
		
		Registrant r = ((Registrant) sessionData.get("workingregistrant"));
		cid  = ((Convention) sessionData.get("conference")).getConCID();
		Convention c = conventionDAO.getByID(cid);

		// registrantDAO.updateCurrentState(r, c.getConCID());
		
		logger.debug("Registrant:  " + r.firstName + " " + r.lastName + " (" + r.badgeName + ")");
		
		SystemBadge sb = new SystemBadge();
		sb.registrant = r;
		sb.convention = c;
		sb.tmpdir = tmpdir;
		sb.imagedir = imagedir;

		Badge b = sb.generateBadge();
		
		sb.badge = b;
		sb.printCommand="echo";
		sb.preview = true;
		sb.printIt();
		
		// The file has been generated, now need to open it up and stream it back.
	    // Set content size
	    File file = new File(tmpdir + "/" + r.getRid() + ".pdf");
	    response.setContentLength((int)file.length());

	    // Open the file and output streams
	    FileInputStream in = new FileInputStream(file);
	    OutputStream out = response.getOutputStream();

	    // Copy the contents of the file to the output stream
	    byte[] buf = new byte[1024];
	    int count = 0;
	    while ((count = in.read(buf)) >= 0) {
	        out.write(buf, 0, count);
	    }
	    in.close();
	    out.close();
		return null;
	}

	public void setServletResponse(HttpServletResponse response){
		this.response = response;
	}

	public HttpServletResponse getServletResponse(){
		return response;
	}

	@Override
	public void setSession(Map<String, Object> arg0) {
		this.sessionData = arg0;
	}
}