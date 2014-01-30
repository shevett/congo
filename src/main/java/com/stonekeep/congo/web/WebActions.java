package com.stonekeep.congo.web;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;
import com.stonekeep.congo.dao.AddressDAO;
import com.stonekeep.congo.dao.ConventionDAO;
import com.stonekeep.congo.dao.EmailDAO;
import com.stonekeep.congo.dao.LinkDAO;
import com.stonekeep.congo.dao.HistoryDAO;
import com.stonekeep.congo.dao.InvoiceDAO;
import com.stonekeep.congo.dao.PhoneDAO;
import com.stonekeep.congo.dao.PropertyDAO;
import com.stonekeep.congo.dao.RegistrantDAO;
import com.stonekeep.congo.dao.RegistrationTypeDAO;
import com.stonekeep.congo.data.Address;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Email;
import com.stonekeep.congo.data.Link;
import com.stonekeep.congo.data.History;
import com.stonekeep.congo.data.Invoice;
import com.stonekeep.congo.data.InvoiceDetail;
import com.stonekeep.congo.data.Phone;
import com.stonekeep.congo.data.Property;
import com.stonekeep.congo.data.PropertyConfiguration;
import com.stonekeep.congo.data.Registrant;
import com.stonekeep.congo.data.RegistrationType;
import com.stonekeep.congo.data.Setting;
import com.stonekeep.congo.payments.InvoiceUtilities;

public class WebActions extends ActionSupport implements Preparable,SessionAware,ServletRequestAware {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(WebActions.class);
	private Map<String,Object> sessionData;
	public String message;
	public Convention currentConvention = null;
	public LinkedHashMap<String, RegistrationType> regTypes;
	public List<PropertyConfiguration> properties; 
	public List<Property> plist;
	private HttpServletRequest request;
	private int cid = 0;
	public boolean paypalenabled ; 			// injected!
	public boolean authorizenetenabled ; 	// injected!'
		
	// Login form...
	public String loginbutton = null;
	public String forgotbutton = null;
	public String login = null;
	public String password = null;
	
	// Create form...
	public String createbutton = null;
	public String cancelbutton = null;
	public String lastname ;
	public String firstname;
	public String companyname;
	public String badgename;
	public String address1;
	public String address2;
	public String city;
	public String state;
	public String zipcode;
	public String country;
	public String phonenumber;
	public String email;
	public String comment;

	// MyHome form...
	public String homeregisterbutton = null;
	public String homeeventsbutton = null;
	public String homelogoutbutton = null;
	public String homechangebutton = null;
	public String homechangepass = null;
	public String homechangesettings = null;
	public String homeinvoicesbutton = null;
	public String homelinks = null;
	
	// Select form...
	public int selectedRegistrant = 0;
	public String writeinlastname = null;
	public String writeinfirstname = null;
	public String writeinbadgename = null;
	public String writeinemail = null;
	
	// Register form
	public String registerexitbutton = null;
	public String registerproceedbutton = null;
	public String regselected = null;
	public String discountcode = null;
	
	// Payment form
	public String paymentpaybutton = null;
	public String paymentbailbutton = null;
	public String paymentfixbutton = null;
	public String addwriteinbutton = null;
	public String addlinksbutton = null;
	public String processnopaybutton = null;
	
	// forgot password form
	public String emailbutton = null;
	
	// change form
	public String savebutton = null;
	
	// Change password form
	public String password1 = null;
	public String password2 = null;
	
	// Internal DAO's - injected on the constructor by spring.
	private final ConventionDAO conventionDAO;
	private final RegistrantDAO registrantDAO;
	private final RegistrationTypeDAO registrationTypeDAO ;
	private final PropertyDAO propertyDAO;
	private final InvoiceDAO invoiceDAO;
	private final HistoryDAO historyDAO;
	private final InvoiceUtilities invoiceUtilities;
	private final PhoneDAO phoneDAO;
	private final EmailDAO emailDAO;
	private final AddressDAO addressDAO;
	private final LinkDAO linkDAO;

	public void setCid(int cid) { this.cid = cid; }
	public void setPaypalenabled(boolean newvalue) { this.paypalenabled = newvalue; }
	public void setAuthorizenetenabled(boolean newvalue) { this.authorizenetenabled = newvalue; }
	
	public WebActions(ConventionDAO conventionDAO, 
			RegistrantDAO registrantDAO, 
			RegistrationTypeDAO registrationTypeDAO, 
			PropertyDAO propertyDAO,
			InvoiceDAO invoiceDAO,
			HistoryDAO historyDAO,
			InvoiceUtilities invoiceUtilities,
			PhoneDAO phoneDAO,
			EmailDAO emailDAO,
			AddressDAO addressDAO,
			LinkDAO linkDAO) {
		this.conventionDAO = conventionDAO;
		this.registrantDAO = registrantDAO;
		this.registrationTypeDAO = registrationTypeDAO ;
		this.propertyDAO = propertyDAO;
		this.historyDAO = historyDAO;
		this.invoiceDAO = invoiceDAO;
		this.invoiceUtilities = invoiceUtilities;
		this.phoneDAO = phoneDAO;
		this.emailDAO = emailDAO;
		this.addressDAO = addressDAO;
		this.linkDAO = linkDAO;
		
		// Set the cid from the current session data.
//		logger.debug("sessionData has " + sessionData + " elements.");
//		logger.debug("sessionData looks like " + sessionData);
//		cid = ((Convention) sessionData.get("currentConvention")).conCID;

	}
		
	// Method for managing responses from the Welcome.jsp page.
	@SkipValidation			/* don't do validation on this page */
	public String login() {
		logger.debug("Login...");
		String returnString = SUCCESS;
		cid = ((Convention) sessionData.get("currentConvention")).conCID;
		try {
			if (loginbutton != null) { 
				logger.debug("Attempted login using '" + login + "'");
				int possibleRID;
				Registrant r;
				try {
					possibleRID = Integer.parseInt(login);
					r = registrantDAO.getByID(possibleRID);
				} catch (NumberFormatException e) {
					// They entered text - so it's either an email address or a badge name.  Email
					// addresses have an @ in them, so search accordingly...
					if (login.indexOf("@") > 1) {
						r = registrantDAO.searchbyEmail(login);
					} else {
						r = registrantDAO.searchByBadgeName(login);
					}
				}
				
				if ((r != null) && registrantDAO.checkPassword(r, password)) {
					// One last check.  Don't allow a login if the account is disabled.
					if (!r.enabled) {
						message="This account is currently disabled.";
					} else {
						// Successful login!
						// Store away the web user, and proceed to home.
						registrantDAO.updateCurrentState(r, cid) ;
						sessionData.put("webuser", r);
						History loginRecord = historyDAO.create(r.rid,cid,"LOGIN",0);
						loginRecord.comment = "Public login";
						historyDAO.save(loginRecord);
						logger.debug("Successful login!");
						
						// This is a bit of a hack.  The login checker isn't being 
						// executed on startup, and therefore isn't loading the 
						// friendslist.  So force it in.  Just copied from the publicloginchecker
						List<Link> fl = linkDAO.findAllById(r.rid);
						logger.info("Loading friends list with " + fl.size() + " members.");
						sessionData.put("friendsList",fl);

						return SUCCESS;
					}
				} else {
					message="Could not log in using those credentials.";
				}
				logger.debug("Returning INPUT with message " + message);
				returnString=INPUT;
			}
			if (forgotbutton != null) {
				logger.debug("forgotbutton pushed.");
				returnString="forgot";
			}
			if (createbutton != null) {
				logger.debug("createbutton pushed.");
				returnString="create";
			}
		}
		catch (Exception e) { 
			message = "Error thrown processing form : " + e.toString();
			logger.error(e.getMessage());
			e.printStackTrace();
			returnString = INPUT;
		}
		return returnString;
	}
	
	// Method for managing responses from the Create.jsp page
    @Validations(
    		requiredFields = {
    				@RequiredFieldValidator(type = ValidatorType.SIMPLE, fieldName = "firstname", message = "s2 validatorYou must enter a value for field.")
            }
    )
          
	public String create() throws SQLException {
		cid = ((Convention) sessionData.get("currentConvention")).conCID;

		logger.debug("Create...");
		if (cancelbutton != null) {
			logger.debug("create cancelled.");
			return "exit";
		}
		// Only way we got here was Create was pressed.
		
		// Do some checking
		if (firstname.isEmpty() || lastname.isEmpty() || badgename.isEmpty() ) { 
			message = "First name, Last name, and Badge name are required.";
			return INPUT;
		} else {
			logger.debug("firstname okay");
		}
		
		if (email.isEmpty()) {
			message = "Please supply an email address";
			return INPUT;
		}else {
			logger.debug("email okay");
		}
		
		if (password1.isEmpty() || password2.isEmpty()) {
			message = "Please supply a password";
			return INPUT;
		} else {
			logger.debug("password okay");
		}
		
		if (! password1.equals(password2)) {
			message = "Passwords do not match";
			return INPUT;
		} else {
			logger.debug("passwords match");
		}
		
		// Do some data validation on the input fields to make sure we dont' have XSS problems:
		if (!validate(firstname) ||
				!validate(lastname) ||
				!validate(companyname)||
				!validate(badgename)) {
			message = "Invalid characters in firstname, lastname, company, or badgename.<br>Use browse BACK button to retrieve previous values.";
			return INPUT;
		}
		
		if (!validate(city) ||
				!validate(state) ||
				!validate(zipcode) ||
				!validate(address1) ||
				!validate(address2)) {
			message = "Invalid characters in address information.<br>Use browse BACK button to retrieve previous values.";
			return INPUT;
		}
		
		if (!validate(email)) {
			message = "Invalid characters in email address.<br>Use browse BACK button to retrieve previous values.";
			return INPUT;
		}
		
		if (!validate(phonenumber)) {
			message = "Invalid characters in phone number.<br>Use browse BACK button to retrieve previous values.";
			return INPUT;
		}
		
		// To avoid duplicates, check to see if we have someone with this email address.
		
		Registrant checker = registrantDAO.searchbyEmail(email);
		if (checker != null) {
			message = "Account with email address '" + email + "' already exists.";
			return INPUT;
		}
		
		// This should be smart enough to check for user@host first, but we're doing this fast right
		// now.  Go ahead and create the new Registrant.
		Registrant r = registrantDAO.create();
		logger.debug("New registrantID: " + r.rid);
		logger.debug("First name is --: " + firstname);
		logger.debug("Last name is ---: " + lastname);
		logger.debug("Company is -----: " + companyname);
		logger.debug("Badgename is ---: " + badgename);
		r.firstName = firstname.trim();
		r.lastName = lastname.trim();
		r.company = companyname.trim();
		r.badgeName = badgename.trim();
		r.enabled = true;
		registrantDAO.save(r);
		
		registrantDAO.setPassword(r, password1);
		
		// Save off the address information
		Address a = new Address();
		a.rid = r.rid;
		a.city = city;
		a.state = state;
		a.zipcode = zipcode;
		a.country = country;
		a.line1 = address1;
		a.line2 = address2;
		a.location="HOME";
		a.primary=true;
		registrantDAO.addressDAO.create(a);
		registrantDAO.addressDAO.save(a,r.rid);
		
		// Aaaand email
		Email e = new Email();
		e.rid = r.rid;
		e.address = email;
		e.location = "HOME";
		e.primary=true;
		registrantDAO.emailDAO.create(e);
		
		// Don't forget phone.
		Phone p = new Phone();
		p.rid = r.rid;
		p.location = "HOME";
		p.phone = phonenumber;
		p.primary=true;
		registrantDAO.phoneDAO.create(p);
		
		// Because of new sublists, it's a good idea to reload the registrant
		registrantDAO.updateCurrentState(r, cid) ;
		r = registrantDAO.getByID(r.rid) ;
		
		sessionData.put("webuser", r);
		
		// Make a CREATE record for this entry
		History loginRecord = historyDAO.create(r.rid,cid,"CREATE",0);
		loginRecord.comment = "Created via public page";
		historyDAO.save(loginRecord);
		
		logger.debug("Create completed - returning Success");
		return SUCCESS;
	}
	
	// MyHome handler...
    @SkipValidation
	public String myhome() throws SQLException {
		if (homelogoutbutton != null) {
			sessionData.clear();
			return "exit";
		}
		if (homeregisterbutton != null) {
//			regTypes = registrationTypeDAO.list(cid);
//			
//			logger.debug("Loading available properties for this event...");
//			List<PropertyConfiguration>propertiesTmp = propertyDAO.listPropertyConfigurations(cid);
//			
//			// Only use those propertyconfigurations that are marked as regprompts.
//			properties = new ArrayList<PropertyConfiguration>();
//			for (PropertyConfiguration pc : propertiesTmp) {
//				if (pc.regprompt) {
//					properties.add(pc);
//				}
//			}
//
//			logger.debug("Loading available Events...");
//			events = eventDAO.getByConventionID(cid);
			
			return "select";
		}
		if (homechangesettings != null) {
			// They've asked to change their settings.  Load up properties and current values, then redirect...
			logger.debug("Loading properties and their set values for this user / event...");
			Registrant r = (Registrant)sessionData.get("webuser");
			
			plist = propertyDAO.listAllProperties(cid, r.getRid());
			
			// Jettison the rows that are not prompty. (need a copy because of concurrent exceptions)
			List<Property> ptmp = new ArrayList(plist);
			for (Property pitem : ptmp) {
				if (! pitem.regprompt) { 
					logger.debug("Removing " + pitem.description + " cuz it's not regprompty.");
					plist.remove(pitem);
				}
			}

			return "settings";
		}
		if (homechangebutton != null) {
			return "change";
		}
		if (homechangepass != null) {
			return "password";
		}
		if (homeinvoicesbutton != null) {
			return "invoices";
		}
		if (homelinks != null) { 
			return "links";
		}
		if (addwriteinbutton != null) {
			logger.debug("addwritein button pushed.  loading regtypes...");
			Invoice i = null;
			Registrant r = (Registrant)sessionData.get("webuser");
			i = invoiceDAO.createInvoice();
			i.creator= r.rid;
			i.comment = "Online registration";
			i.status = "READY";
			i.operator = r.rid;
			logger.debug("Created new invoice " + i.id);
			invoiceDAO.saveInvoice(i);
			sessionData.put("currentinvoice",i);
			regTypes = new LinkedHashMap(registrationTypeDAO.list(cid));

			logger.debug("Loading available properties for this event...");
			List<PropertyConfiguration>propertiesTmp = propertyDAO.listPropertyConfigurations(cid);
			
			// Only use those propertyconfigurations that are marked as regprompts.
			properties = new ArrayList<PropertyConfiguration>();
			for (PropertyConfiguration pc : propertiesTmp) {
				if (pc.regprompt) {
					properties.add(pc);
				}
			}
			return "addwritein";
		}
		return SUCCESS;
	}
    
    // First step selecting registrant from the 'select' page and
    // passing it to the Register page.  Make sure something is
    // selected, load it into the session, and return...  selectedRegistrant
    // should contain the RID of the registrant...
    // Note that this might be a writein - if that's the case, return 'writein' to go
    // to the write JSP.
    @SkipValidation
    public String registerSelected() throws SQLException {
    	logger.debug("registerSelected...");
    	
    	cid = ((Convention) sessionData.get("currentConvention")).conCID;
    	
    	if (registerexitbutton != null) {
    		return "exit";
    	}
    	if (selectedRegistrant == 0) {
    		message="Please select a registrant before proceeding";
    		logger.warn("In Select.jsp, register button pushed without selecting someone.");
    		return INPUT;
    	}
    	logger.debug("Selected registrant " + selectedRegistrant);
		logger.debug("Loading regtypes...");
		regTypes = new LinkedHashMap(registrationTypeDAO.list(cid));

		logger.debug("Loading available properties for this event...");
		List<PropertyConfiguration>propertiesTmp = propertyDAO.listPropertyConfigurations(cid);
		
		// Only use those propertyconfigurations that are marked as regprompts.
		properties = new ArrayList<PropertyConfiguration>();
		for (PropertyConfiguration pc : propertiesTmp) {
			if (pc.regprompt) {
				properties.add(pc);
			}
		}
		
		// Loading selected registrant into session...
		Registrant w = registrantDAO.getByID(selectedRegistrant);
		if (w == null) {
			logger.warn("Select page returned an invalid registrant (" + selectedRegistrant + ") - gotta be a writein");
			return "writein";
		}
		sessionData.put("workingregistrant", w);

    	return SUCCESS;
    }
	
	// Register handler...
    @SkipValidation
	public String register() throws SQLException { 
		logger.debug("register...");
		Invoice i = null;
		Registrant r = null;
		Convention c = ((Convention) sessionData.get("currentConvention"));
		cid = c.getConCID() ;
		if (registerexitbutton != null) {
			return "exit";
		}
		// If we're here, they proceeded.  Do some validation.
		if (regselected == null) {
			message="You need to select a registration type to proceed.";
			return INPUT;
		}
		
		// There's a chance they used the Back button to try and add someone to an invoice
		// after it's already been paid.  Check to make sure the invoice we're working on isn't
		// already paid.
		if (sessionData.containsKey("currentinvoice")) {
			Invoice invoiceQuickie = (Invoice)sessionData.get("currentinvoice");
			if (invoiceQuickie.status.equals("PAID")) {
				message="Current invoice is already marked as PAID.  Please restart.";
				return INPUT;
			}
		}
		
		// NEed to make sure some environment settings are live.  Load them up.
		Map<String,Setting> settings = (Map<String, Setting>) sessionData.get("settings");
		logger.debug("Setting value for 'paypal_enabled' is " + settings.get("paypal_enabled").getSettingValue());
		paypalenabled = (settings.get("paypal_enabled").getSettingValue()).equals("1") ? true : false;
		
		logger.debug("Setting up payment screen.  Paypal is " + paypalenabled);
		
		// There are circumstances where they'd get here after a REGISTRATION record is already
		// on the invoice.  Make sure there aren't any...
		if (sessionData.containsKey("currentinvoice")) {
			Invoice invoiceQuickie = (Invoice)sessionData.get("currentinvoice");
			Registrant wr = (Registrant)sessionData.get("workingregistrant");
			logger.debug("Using existing invoice " + invoiceQuickie.id);
			for (InvoiceDetail id : invoiceQuickie.detailList) {
				logger.info("Checking detail line item : " + id.type);
				logger.info("Checking against working registrant : " + wr);
				// Occasionally we hit this when we're doing a writein, which has no
				// working registrant, so don't check this condition if wr is null...
				if ((wr != null) && (id.type.equals("REGISTRATION") && (id.rid == wr.rid)) ) {
					// Sloppy, but exit with success which will have it ignore this item.
					message="Already have a registration line item for this person (#" + wr.rid + ")  Skipped.";
					return SUCCESS;
				}
			}
		}
		
		// Check to see if they pickd a selected regtype that requires a discount code, and validate
		// the code...
		RegistrationType rt = registrationTypeDAO.getRegistrantType(cid,regselected);
		if (rt.getRegDiscountCode() != null) {
			if (! rt.getRegDiscountCode().equalsIgnoreCase(discountcode)) {
				message="Invalid Discount Code for selected registration type.";
				logger.warn("Invalid discount code '" + discountcode + "' tried for reg type " + regselected);
				return "error";
			}
		} 
		
			// Create a new invoice into the session if there isn't one already.
			r = (Registrant)sessionData.get("webuser");
			if (sessionData.containsKey("currentinvoice")) {
				i = (Invoice)sessionData.get("currentinvoice");
				logger.debug("Using existing invoice " + i.id);
			} else {
				i = invoiceDAO.createInvoice();
				i.creator= r.rid;
				i.comment = "Online registration";
				i.status = "READY";
				i.operator = r.rid;
				logger.debug("Created new invoice " + i.id);
				invoiceDAO.saveInvoice(i);
			}

			// Populate line items into the invoice as specified by the form.
			
			Registrant wr = (Registrant)sessionData.get("workingregistrant");
				
			InvoiceDetail id = invoiceDAO.createInvoiceDetail();
			id.rid = wr.rid;
			id.cid = cid;
			id.invoice = i.id ; 
			id.operator = 0;
			id.type = "REGISTRATION";
			id.type2 = regselected;
			id.description = "#" + cid + " : " + c.getConName() + " : Online Registration";
			
			// We have a regtype, but not an amount.  Look it up.
			// RegistrationType rt = registrationTypeDAO.getRegistrantType(cid,regselected);
			
			id.amount = rt.getRegCost();	// Need to look up amount - hidden value?
			id.discount = new BigDecimal(0);
			id.finalamount = id.amount;
			invoiceDAO.saveInvoiceDetail(id);
			
			// They may have checked properties.  Run through the available property list and
			// check form values for those properties.
			
			logger.debug("Loading available properties for this event...");
			List<PropertyConfiguration>propertiesTmp = propertyDAO.listPropertyConfigurations(cid);
			
			// Only use those propertyconfigurations that are marked as regprompts.
			// properties = new ArrayList<PropertyConfiguration>();
			for (PropertyConfiguration pc : propertiesTmp) {
				if (pc.regprompt) {
					// Okay, this property would have been prompted.  Did someone check something?
					logger.debug(pc.name + " in the request as " + request.getParameter(pc.name) + ", config default is " + pc.defaultvalue);
					
					// Checkboxes are 'null' or 'on'
					// input fields are empty or have a typed in value.
					
					id = invoiceDAO.createInvoiceDetail();
					id.rid = r.rid;
					id.cid = cid;
					id.invoice = i.id ; 
					id.operator = 0;
					
					id.type="PROPERTY";
					id.type2=pc.name;
					id.description=request.getParameter(pc.name);
					
					id.amount = new BigDecimal(pc.cost);
					id.finalamount = new BigDecimal(pc.cost);	
					id.discount = new BigDecimal(0);

					// Here we figure out what goes into the detail...
					if (pc.type.equals("boolean") && (request.getParameter(pc.name) != null)) { 
						invoiceDAO.saveInvoiceDetail(id);
					}
					if (pc.type.equals("numeric") && (Integer.parseInt(request.getParameter(pc.name)) > 0)) {
						invoiceDAO.saveInvoiceDetail(id);
					}
					if (pc.type.equals("string") && (request.getParameter(pc.name).length() > 0 )) {
						invoiceDAO.saveInvoiceDetail(id);
					}
					if (pc.type.equals("picker")) {
						if (! request.getParameter(pc.name).equals(pc.defaultvalue)) {
							invoiceDAO.saveInvoiceDetail(id);
						}
					}
				}
			}		
			
			// Was there a discount code?  If so add it
			if ((discountcode) != null && (discountcode.length()) > 0) {
				id = invoiceDAO.createInvoiceDetail();
				id.rid = r.rid;
				id.cid = cid;
				id.invoice = i.id ; 
				id.operator = 0;
				
				id.type="DISCOUNT";
				id.type2=discountcode;
				id.amount=new BigDecimal(0);
				invoiceDAO.saveInvoiceDetail(id);
			}
			
			invoiceDAO.recalculate(i.id);
			
			// Reload the invoice...
			i = invoiceDAO.getInvoice(i.id);
			i.detailList = invoiceDAO.listInvoiceDetails(i.id);
			sessionData.put("currentinvoice",i);

			return SUCCESS;
	}

	// Process the payment screen in the web interface. 
    @SkipValidation	
	public String pay() throws SQLException,Exception { 
		logger.debug("pay...");
		if (paymentfixbutton != null) {
			return INPUT;
		}
		if (addlinksbutton != null) {
			logger.debug("Adding a friend to an invoice...");
			return "addlinks";
		}
		if (paymentbailbutton != null) {
			Invoice i = (Invoice)sessionData.get("currentinvoice");
			i.status="VOID";
			invoiceDAO.saveInvoice(i);
			sessionData.remove("currentinvoice");
			return "exit";
		}
		if (addwriteinbutton != null) {
			logger.debug("addwritein button pushed.  loading regtypes...");
			regTypes = new LinkedHashMap(registrationTypeDAO.list(cid));

			logger.debug("Loading available properties for this event...");
			List<PropertyConfiguration>propertiesTmp = propertyDAO.listPropertyConfigurations(cid);
			
			// Only use those propertyconfigurations that are marked as regprompts.
			properties = new ArrayList<PropertyConfiguration>();
			for (PropertyConfiguration pc : propertiesTmp) {
				if (pc.regprompt) {
					properties.add(pc);
				}
			}
			return "addwritein";
		}
		if (processnopaybutton != null) {
			Invoice i = (Invoice)sessionData.get("currentinvoice");
			logger.debug("Processing invoice " + i.id + " which has " + i.getDetailList().size() + " detail items.");
			String paymenttype = "paypal";	// obviously needs to change.
			invoiceUtilities.setSession(sessionData);	// because we're not invoking this via action, need to inject this.
			invoiceUtilities.processInvoice(i, paymenttype);
		}
		return SUCCESS;
	}
	
	/*
	 * Write-in management
	 */
    @SkipValidation
	public String writeinadd() throws SQLException {
		logger.debug("writeinadd...");
		
		// This is probably silly as it's copy/pasted from a bunch of spots.  but it's needed.
		Map<String,Setting> settings = (Map<String, Setting>) sessionData.get("settings");
		logger.debug("Setting value for 'paypal_enabled' is " + settings.get("paypal_enabled").getSettingValue());
		paypalenabled = (settings.get("paypal_enabled").getSettingValue()).equals("1") ? true : false;
		
		logger.debug("Setting up payment screen.  Paypal is " + paypalenabled);
		
		if (registerproceedbutton == null) {
			return SUCCESS;
		}
		
		// If we're here, they proceeded.  Do some validation.
		if (regselected == null) {
			message="You need to select a registration type to proceed.";
			return INPUT;
		}
		
		// There's a chance they used the Back button to try and add someone to an invoice
		// after it's already been paid.  Check to make sure the invoice we're working on isn't
		// already paid.
		if (sessionData.containsKey("currentinvoice")) {
			Invoice invoiceQuickie = (Invoice)sessionData.get("currentinvoice");
			if (invoiceQuickie.status.equals("PAID")) {
				message="Current invoice is already marked as PAID.  Please restart.";
				return INPUT;
			}
		}
		
		// Check to see if they pickd a selected regtype that requires a discount code, and validate
		// the code...
		RegistrationType rt = registrationTypeDAO.getRegistrantType(cid,regselected);
		if (rt.getRegDiscountCode() != null) {
			if (! rt.getRegDiscountCode().equalsIgnoreCase(discountcode)) {
				message="Invalid Discount Code for selected registration type.";
				logger.warn("Invalid discount code '" + discountcode + "' tried for reg type " + regselected);
				return "error";
			}
		} 
		
		Registrant r = (Registrant)sessionData.get("webuser");
		Invoice i = (Invoice)sessionData.get("currentinvoice");
		if (i == null) {
			// Eegads, no current invoice!  Create one, stat!
			i = null;
			i = invoiceDAO.createInvoice();
			i.creator= r.rid;
			i.comment = "Online registration";
			i.status = "READY";
			i.operator = r.rid;
			logger.debug("Created new invoice " + i.id);
			invoiceDAO.saveInvoice(i);
			sessionData.put("currentinvoice",i);
		} else {
			// There are conditions where someone could be trying to add a writein to
			// an invoice that's already been processed.  This is very bad, and can result
			// in weird situations.  Generally this only happens if someone has hit the back
			// button a bunch of times AFTER they've paid for a reg.  If this is the case, error out.
			if (! i.status.equalsIgnoreCase("READY")) {
				message = "Attempted to add new invoice items to an already processed invoice.";
				return ERROR;
			}
		}
		InvoiceDetail id = invoiceDAO.createInvoiceDetail();
		logger.debug("adding new writein:");
		logger.debug("firstname = " + writeinfirstname);
		logger.debug("lastname = " + writeinlastname);
		logger.debug("badgename = " + writeinbadgename);
		logger.debug("email = " + writeinemail);
		id = invoiceDAO.createInvoiceDetail();
		id.rid = r.rid;
		id.cid = cid;
		id.invoice = i.id ; 
		id.operator = 0;
		
		id.type="WRITEIN";
		id.type2=regselected;
		id.description=writeinlastname + ", " + writeinfirstname + "(" + writeinbadgename + ")";
		id.comment=writeinlastname + "|" + writeinfirstname + "|" + writeinbadgename + "|" + writeinemail + "|" + phonenumber;
		id.postprocess = true;
			
		id.amount = rt.getRegCost();	// Need to look up amount - hidden value?
		id.discount = new BigDecimal(0);
		id.finalamount = id.amount;
		
		invoiceDAO.saveInvoiceDetail(id);
		
		// Was there a discount code?  If so add it
		if ((discountcode) != null && (discountcode.length()) > 0) {
			id = invoiceDAO.createInvoiceDetail();
			id.rid = r.rid;
			id.cid = cid;
			id.invoice = i.id ; 
			id.operator = 0;
			
			id.type="DISCOUNT";
			id.type2=discountcode;
			id.amount=new BigDecimal(0);
			invoiceDAO.saveInvoiceDetail(id);
		}

		invoiceDAO.recalculate(i.id);

		// Reload the invoice...
		i = invoiceDAO.getInvoice(i.id);
		i.detailList = invoiceDAO.listInvoiceDetails(i.id);
		sessionData.put("currentinvoice",i);		
		return SUCCESS;
	}
	
	/**
	 * Validate whether the input string used has only text characters in it.
	 * @param s The string to check
	 * @return True if the string passes muster
	 */
    @SkipValidation
	private boolean validate(String s) {
		// String re = "^[a-zA-Z0-9\\- ,]+$";
		String re = "^[()-@\\.\\p{L}\\p{Digit} ,_']*$";
        Pattern pattern = Pattern.compile(re);
        Matcher matcher = pattern.matcher(s);
        return matcher.find();
	}
	
	// Stub...
    @SkipValidation
	public String change() throws Exception{
		if (savebutton != null) {
			logger.debug("Saving registrant after change...");
			if (!validate(firstname)) {
				message = "First name contains illegal characters.";
				return INPUT;
			}
			if (!validate(lastname)) {
				message = "Last name contains illegal characters.";
				return INPUT;
			}
			if (!validate(companyname)) {
				message = "Company name contains illegal characters.";
				return INPUT;
			}
			if (!validate(badgename)) {
				message = "Badge name contains illegal characters.";
				return INPUT;
			}
			
			Registrant r = (Registrant)sessionData.get("webuser");

			// Check to make sure that the email address they're changing to isn't someone
			// elses address...
			
			Registrant testRegistrant = registrantDAO.searchbyEmail(email);
			if (testRegistrant != null) {
				if (testRegistrant.rid != r.rid) {
					message = "The email address you've selected is in use by another registrant.";
					return INPUT;
				}
			}
			
			logger.debug("Old firstname was " + r.firstName);
			logger.debug("Changing it to " + firstname);
			r.firstName = firstname;
			r.lastName = lastname;
			r.company = companyname;
			r.badgeName = badgename;
			//r.comment = comment;
			registrantDAO.logChanges(r, r.rid, cid);
			registrantDAO.save(r);
			
			// Update the email entries
			try {
				Email oldEmail = emailDAO.getEmail(r.rid);
				logger.debug("oldEmail email is " + oldEmail);
				
				if (oldEmail !=null && ! oldEmail.address.equals(email)) {
					if (oldEmail != null) emailDAO.delete(oldEmail);
					logger.debug("Updating email to " + email);
					Email newEmail = new Email();
					newEmail.rid = r.rid;
					newEmail.location = "Home";
					newEmail.primary = true;
					newEmail.address = email;
					emailDAO.create(newEmail);
					History emailRecord = historyDAO.create(r.rid,cid,"CHANGE",r.rid);
					emailRecord.comment = "Changed email address";
					emailRecord.arg1 = email;
					historyDAO.save(emailRecord);
				
					// Since we've changed, we need to reload the registrant so that i'll be there for further actions.
					r = registrantDAO.getByID(r.rid);
				}
			} catch (Exception e) {
				logger.error("email change failed for #" + r.rid + " : " + e.getMessage());
				e.printStackTrace();
				message = "An error occurred updating your email: " + e.getMessage();
				return INPUT;
			}

			// Update the email entries
			try {
				Phone phone = phoneDAO.getPhone(r.rid);
				logger.debug("Old phone is " + phone);
				

				// This logic is a little wonky.  
				// If there is an existing phone record, and it's different than what was typed, delete it.
				if (phone !=null && ! phone.phone.equals(phonenumber)) {
					phoneDAO.delete(phone);
				}
				
				// Then, after deleting, recreate it if there was no phone, or they did not equal.
				if (phone == null || ! phone.phone.equals(phonenumber)) {
					logger.debug("Updating phone to " + phonenumber);
					Phone newphone = new Phone();
					newphone.rid = r.rid;
					newphone.location = "Home";
					newphone.primary = true;
					newphone.phone = phonenumber;
					phoneDAO.create(newphone);
					History phoneRecord = historyDAO.create(r.rid,cid,"CHANGE",r.rid);
					phoneRecord.comment = "Changed phone number";
					phoneRecord.arg1 = phonenumber;
					historyDAO.save(phoneRecord);
				
					// Since we've changed, we need to reload the registrant so that i'll be there for further actions.
					r = registrantDAO.getByID(r.rid);
				}
			} catch (Exception e) {
				logger.error("phone change failed for #" + r.rid + " : " + e.getMessage());
				e.printStackTrace();
				message = "An error occurred updating your phone number: " + e.getMessage();
				return INPUT;
			}
			
			// Update the email entries
			try {
				Address address = addressDAO.getAddress(r.rid);
				logger.debug("Old address is " + address);
				
				if (address !=null && (
						areDifferent(address.line1,address1) ||
						areDifferent(address.line2,address2) ||
						areDifferent(address.city,city) ||
						areDifferent(address.state,state) ||
						areDifferent(address.zipcode,zipcode) ||
						areDifferent(address.country,country))) {
					if (address != null) addressDAO.delete(address);
					logger.debug("Updating address to " + address1);
					Address newaddress = new Address();
					newaddress.rid = r.rid;
					newaddress.location = "Home";
					newaddress.primary = true;
					newaddress.line1 = address1;
					newaddress.line2 = address2;
					newaddress.city = city;
					newaddress.state = state;
					newaddress.zipcode = zipcode;
					newaddress.country = country;
					addressDAO.create(newaddress);
					
					History addressRecord = historyDAO.create(r.rid,cid,"CHANGE",r.rid);
					addressRecord.comment = "Changed mailing address";
					historyDAO.save(addressRecord);
				
					// Since we've changed, we need to reload the registrant so that i'll be there for further actions.
					r = registrantDAO.getByID(r.rid);
				}
			} catch (Exception e) {
				logger.error("address change failed for #" + r.rid + " : " + e.getMessage());
				e.printStackTrace();
				message = "An error occurred updating your address information: " + e.getMessage();
				return INPUT;
			}
			
			// Force the update of the current Registrant object in the session to contain
			// proper state.  This was bug #176? something like that.  -dbs
			registrantDAO.updateCurrentState(r, cid);
			sessionData.put("webuser",r);
		}
		return SUCCESS;
	}
	
    
    @SkipValidation
	private boolean areDifferent(String v1, String v2) {
		if ((v1 == null) && (v2 != null)) return true;
		if ((v2 == null) && (v1 != null)) return true;
		if (v1 == v2) return false;
		// If we're here, neither v1 nor v2 are null.  Compare away.
		return (! v1.equals(v2.toString()));
	}
	
    @SkipValidation
	public String changepassword() throws Exception {
		if (savebutton != null) {
			logger.debug("This would change the password!");
			logger.trace("password 1 is " + password1);
			logger.trace("password 2 is " + password2);	
			if (password1.equals(password2)) {
				Registrant r = (Registrant)sessionData.get("webuser");
				registrantDAO.setPassword(r, password1);
			} else {
				message = "Passwords do not match!";
				return INPUT;
			}
		}
		return SUCCESS ;
	}
	
    @SkipValidation
	public String settings() throws Exception {
		if (savebutton != null) {
			logger.debug("Saving settings from Settings screen...");
			// Get a list of properties again, work through them...
			Registrant r = (Registrant)sessionData.get("webuser");
			List<PropertyConfiguration>propertiesTmp = propertyDAO.listPropertyConfigurations(cid);

			// Only use those propertyconfigurations that are marked as regprompts.
			// properties = new ArrayList<PropertyConfiguration>();
			for (PropertyConfiguration pc : propertiesTmp) {
				if (pc.regprompt) {
					// Okay, this property would have been prompted.  Did someone check something?
					logger.debug(pc.name + " is represented in the request as " + request.getParameter(pc.name));
					
					// Check to see if the property is global or local, and set the cid appropriately. Bug #206
					int cidvalue = (pc.scope.equals("Event")) ? cid : 0;
					
					// Boolean values / checkboxes are wonky.  Special handling...
					if (pc.type.equals("boolean")) {
						if (request.getParameter(pc.name) != null) {
							propertyDAO.setProperty(r.rid,cidvalue,pc.name,"1");
						} else {
							propertyDAO.deleteProperty(r.rid, cidvalue, pc.name);
						}
					} else {
						if (request.getParameter(pc.name) != null) {
							propertyDAO.setProperty(r.rid, cidvalue, pc.name,(String)request.getParameter(pc.name));
						}
					}
				}
			}
		}
		return SUCCESS;
	}
	
	
	@Override
	public void prepare() throws Exception {
		logger.debug("Injected value for preferredcid is " + cid);
		currentConvention = (Convention)sessionData.get("currentConvention");
		if (currentConvention == null) {
			logger.debug("Loading convention " + cid + " into session...");
			currentConvention = conventionDAO.getByID(cid);
			sessionData.put("currentConvention",currentConvention);
		}
		cid = currentConvention.conCID;	// This replaces the injected value.
		logger.debug("Prepare: Current event is " + "#" + currentConvention.conCID + " : " + currentConvention.conName);
	}
	
	@Override
	public void setSession(Map<String, Object> arg0) {
		this.sessionData = arg0;
	}
	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}	
	
	
	
  
}
