package com.stonekeep.congo.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.stonekeep.congo.dao.SettingDAO;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Invoice;
import com.stonekeep.congo.data.InvoiceDetail;
import com.stonekeep.congo.data.Setting;
import com.stonekeep.congo.payments.InvoiceUtilities;

	// These are the sandbox / production targets.... 
	// <value label="Sandbox">https://api-3t.sandbox.paypal.com/nvp</value>
	// <value label="Production">https://api-3t.paypal.com/nvp</value>

public class Paypal extends ActionSupport implements SessionAware {
	private Logger logger = Logger.getLogger(Paypal.class);
	private Map sessionData;

	public String email;
	public String emailbutton;
	public String finishbutton;
	public String cancelbutton;
	public String message;
	public String redirectURL;
	public String payerID;	// this is passed via GET parameter in from Paypal.
	public String token;	// ditto
	public boolean paypalenabled = true;

	private BigDecimal amount = new BigDecimal(25.0);
	private HashMap<String,String> responseArguments = new HashMap<String,String>();

	private String paypalmerchant;
	private String paypalpassword;
	private String paypalsignature;
	private String paypaltarget;
	private String paypalmode;
	private String returnurl;
	
	private final InvoiceUtilities invoiceUtilities;
	private final SettingDAO settingDAO;

	public boolean getPaypalenabled() { return this.paypalenabled; }
	public String getRedirectURL() { return this.redirectURL; }
	public HashMap<String,String> getResponseArguments() { return this.responseArguments; }
	public void setToken(String newvalue) { this.token = newvalue; }
	public void setPayerID(String newvalue) { this.payerID = newvalue; }
	
	Map<String,Setting> settings;

	
	public Paypal(InvoiceUtilities invoiceUtilities, SettingDAO settingDAO) {
		this.invoiceUtilities = invoiceUtilities;
		this.settingDAO = settingDAO;
	}

	public HashMap<String,String> makeHTTPCall(PostMethod method) throws Exception {
		HashMap<String,String> responseArguments = new HashMap<String,String>();
		HttpClient client = new HttpClient();
		// Provide custom retry handler is necessary
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
				new DefaultHttpMethodRetryHandler(3, false));

		try {
			// Execute the method.

			logger.debug("Calling...");
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Method failed: " + method.getStatusLine());
			} else {
				logger.info("Call to server " + paypaltarget + " complete.  Status code: " + statusCode);
			}

			// Read the response body.
			byte[] b = method.getResponseBody();
			String responseBody = new String(b);
			logger.debug("Server response: " + responseBody);

			// Split it up the response string and pile it into the responseArguments...

			String[] args = responseBody.split("&");

			for (String item : args) {
				String[] nvpair = item.split("=");
				responseArguments.put(nvpair[0],(String)URLDecoder.decode(nvpair[1],"UTF-8"));
				logger.debug(nvpair[0] + " --> " + nvpair[1]);
			}
		} catch (HttpException e) {
			logger.error("Fatal protocol violation: " + e.getMessage());
			throw e;
		} catch (IOException e) {
			logger.error("Fatal transport error: " + e.getMessage());
			throw e;
		} finally {
			// Release the connection.
			method.releaseConnection();
		}  
		return responseArguments;
	}

	public String pay() throws Exception {
		loadSettings();

		


		// Retrieve the appropriate invoice information...

		Invoice currentInvoice = (Invoice)sessionData.get("currentinvoice");

		PostMethod method = new PostMethod(paypaltarget);
		method.addParameter("USER",paypalmerchant);
		method.addParameter("PWD",paypalpassword);
		method.addParameter("SIGNATURE",paypalsignature);
		method.addParameter("VERSION","60.0");
		method.addParameter("PAYMENTACTION","Sale");

		// The AMT value here has to be formatted right. :-/
		// NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);
		DecimalFormat n = new DecimalFormat("####0.00");

//		Convention currentConvention = (Convention)sessionData.get("currentConvention");
		int counter=0;
		for (InvoiceDetail id : currentInvoice.getDetailList()) {
			// This makes sure that the only thing that gets sent to paypal for their invoice are things
			// that cost money or the registrations themselves.
			if ((id.amount.floatValue() > 0) || 
					(id.type.equals("REGISTRATION") || id.type.equals("WRITEIN"))) {
				logger.debug("Adding invoice detail : " + id.comment + "(" + id.type2 + " " + id.description + " " + id.amount + ")");
				method.addParameter("L_NAME" + counter,id.type2);
				method.addParameter("L_NUMBER" + counter,id.rid+"");
				method.addParameter("L_DESC" + counter,id.description);
				method.addParameter("L_AMT" + counter,n.format(id.finalamount) + "");
				method.addParameter("L_QTY" + counter,"1");
				counter++;
			} else {
				logger.debug("Skipping invoice detail : " + id.comment + "(" + id.type2 + " " + id.description + " " + id.amount + ") (No amount)");
			}
		}
		method.addParameter("AMT", n.format(currentInvoice.amount)+"");
		method.addParameter("ITEMAMT",n.format(currentInvoice.amount)+""); /* see http://www.paypalobjects.com/en_US/ebook/PP_NVPAPI_DeveloperGuide/expresscheckout.html */
		method.addParameter("RETURNURL",returnurl + "/public/webPayPalConfirmSetup.action");
		method.addParameter("CANCELURL",returnurl + "/public/webMyHomeForm.action");
		method.addParameter("METHOD","SetExpressCheckout");
		method.addParameter("LANDINGPAGE","Billing");		// should enable credit card prompts
		method.addParameter("SOLUTIONTYPE","Sole");			// ditto.
		
		logger.debug("=================================================");
		logger.debug("Method pay() : Dumping method params before Paypal call: ");
		for (NameValuePair nv :  method.getParameters()) {
			logger.debug(nv.getName() + ":" + nv.getValue());
		}
		logger.debug("=================================================");

		// Make the call...
		responseArguments = makeHTTPCall(method);

		// Check to see what happened.
		if (responseArguments.get("ACK").equals("Failure")) {
			// There can be more than one reason for failures.  Loop, building up a message.
			StringBuffer m = new StringBuffer();
			for (int c=0; c<=9; c++) {
				if (responseArguments.containsKey("L_SHORTMESSAGE" + c)) {
					m = m.append(responseArguments.get("L_SHORTMESSAGE" + c) 
							+ " : "
							+ responseArguments.get("L_LONGMESSAGE" + c));
					m.append("<br>");
				}
			}
			message="Call to Paypal failed : <br> " + m.toString();
			return INPUT;
		}

		// If we're here, we didn't fail.  Redirect.
		logger.debug("Successful setup call, redirecting...");
		logger.debug("Token is " + responseArguments.get("TOKEN"));
		if (paypalmode.equalsIgnoreCase("sandbox")) {
			redirectURL = "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=" +
				responseArguments.get("TOKEN");
			logger.warn("NOTE - In Paypal SANDBOX mode.  These are not real payments!");
		} else {
			redirectURL = "https://www.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=" +
				responseArguments.get("TOKEN");
			logger.debug("Using Paypal production URL");
		}
		
		logger.debug("Redirect URL is " + redirectURL);
		return "redirect";
	}

	public String setup() throws Exception { 
		loadSettings();
		
		logger.debug("Setup - here we'll confirm the token and buyer ID, and get all the form information.");
		logger.debug("Token is " + token);
		logger.debug("PayerID is " + payerID);
		
		Convention c = (Convention)sessionData.get("currentConvention");
		
		DecimalFormat n = new DecimalFormat("####0.00");

		PostMethod method = new PostMethod(paypaltarget);
		method.addParameter("USER",paypalmerchant);
		method.addParameter("PWD",paypalpassword);
		method.addParameter("SIGNATURE",paypalsignature);
		method.addParameter("VERSION","60.0");
		method.addParameter("PAYMENTACTION","Sale");
		method.addParameter("TOKEN",token);
		method.addParameter("METHOD","GetExpressCheckoutDetails");
		method.addParameter("DESC",c.conName);
		method.addParameter("CUSTOM","Event Registration.");

		responseArguments = makeHTTPCall(method);
		
		return SUCCESS;
	}
	
	public String alldone() throws Exception {
		// Finished with this invoice.  Remove it from the session and we're outta here.
		logger.debug("Cleaning up existing invoice...");
		Invoice currentInvoice = (Invoice)sessionData.get("currentinvoice");
		sessionData.remove("currentinvoice");
		return SUCCESS;
	}
	
	public String complete() throws Exception {
		logger.debug("Complete...");
		if (cancelbutton != null) {
			logger.debug("Cancel button pressed.  Exiting.");
			return "exit";
		}

		logger.debug("Complete - handle completing the transaction.");
		
		loadSettings();

		Invoice currentInvoice = (Invoice)sessionData.get("currentinvoice");
		
		PostMethod method = new PostMethod(paypaltarget);
		method.addParameter("USER",paypalmerchant);
		method.addParameter("PWD",paypalpassword);
		method.addParameter("SIGNATURE",paypalsignature);
		method.addParameter("VERSION","52.0");
		method.addParameter("PAYMENTACTION","Sale");
		NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);
		method.addParameter("AMT", n.format(currentInvoice.amount));
		method.addParameter("ITEMAMT",n.format(currentInvoice.amount));
		method.addParameter("TOKEN",token);
		method.addParameter("PAYERID",payerID);
		method.addParameter("METHOD","DoExpressCheckoutPayment");

		int counter=0;
		for (InvoiceDetail id : currentInvoice.getDetailList()) {
			// This makes sure that the only thing that gets sent to paypal for their invoice are things
			// that cost money or the registrations themselves.
			if ((id.amount.floatValue() > 0) || 
					 (id.type.equals("REGISTRATION") || id.type.equals("WRITEIN"))) {
				logger.debug("Adding invoice detail : " + id.comment + "(" + id.type2 + " " + id.description + " " + id.amount + ")");
				method.addParameter("L_NAME" + counter,id.type2);
				method.addParameter("L_NUMBER" + counter,id.rid+"");
				method.addParameter("L_DESC" + counter,id.description);
				method.addParameter("L_AMT" + counter,n.format(id.finalamount) + "");
				method.addParameter("L_QTY" + counter,"1");
				counter++;
			} else {
				logger.debug("Skipping invoice detail : " + id.comment + "(" + id.type2 + " " + id.description + " " + id.amount + ")");

			}
		}
		
		logger.debug("=================================================");
		logger.debug("Method complete() : Dumping method params before Paypal call: ");
		for (NameValuePair nv :  method.getParameters()) {
			logger.debug(nv.getName() + ":" + nv.getValue());
		}
		logger.debug("=================================================");

		
		responseArguments = makeHTTPCall(method);
		
		// If we got a successful payment, process the invoice, log it, and return success. 
		
		if (responseArguments.get("ACK").equals("Success")) {
			invoiceUtilities.setSession(sessionData);
			invoiceUtilities.sendMail=true;
			invoiceUtilities.processInvoice(currentInvoice, "Paypal");
			return SUCCESS;
		} else {
			return ERROR;
		}
	}
	
	private void loadSettings() {
		settings = settingDAO.listSettings();
		paypalmode = settings.get("paypal_mode").getSettingValue();
		returnurl = settings.get("paypal_url").getSettingValue();
		paypalmerchant = settings.get("paypal_user").getSettingValue();
		paypalpassword = settings.get("paypal_password").getSettingValue();
		paypalsignature = settings.get("paypal_signature").getSettingValue();
		
		// Set up the target based on our mode...
		paypaltarget="https://api-3t.paypal.com/nvp";
		logger.debug("Paypalmode is " + paypalmode);
		if (paypalmode.equalsIgnoreCase("sandbox")) {
			paypaltarget = "https://api-3t.sandbox.paypal.com/nvp";
			logger.warn("Using SANDBOX Url for Paypal: " + paypaltarget);
		}
	}

	public String execute() {
		logger.debug("execute...");
		return SUCCESS;
	}

	@Override
	public void setSession(Map arg0) {
		this.sessionData = arg0;
	}


}