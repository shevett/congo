package com.stonekeep.congo.payments;
// package com.stonekeep.CONGO.authorize;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import org.apache.log4j.Logger;


public class AuthorizeNet {

	//	public static void main(String[] Args){

	public boolean testMode = true;
	public float invoiceAmount;
	public String cardNumber = "";
	public String cardCVV = null;
	public String expirationDate = "";
	public String firstName = null;
	public String lastName = null;
	public String address = null;
	public String city = null;
	public String state = null;
	public String zip = null;

	private String login = "6PcE8Qh6MpUm";
	private String transactionKey = "6SL8rC6p6XZ86eqr";
	Logger logger;

	public AuthorizeNet() {
		logger = Logger.getLogger("AuthorizeNet");
		logger.debug("AuthorizeNet processor initialized...");
	}


	public String processPayment() {
		try {
			logger.debug("Setting up call information");
			// standard variables for basic Java AIM test
			// use your own values where appropriate

			StringBuffer sb = new StringBuffer();

			// mandatory name/value pairs for all AIM CC transactions
			// as well as some "good to have" values
			sb.append("x_login=" + login + "&");             // replace with your own
			sb.append("x_tran_key=" + transactionKey + "&");     // replace with your own
			sb.append("x_version=3.1&");
			logger.debug("test mode is " + testMode);
			if (testMode) {
				sb.append("x_test_request=TRUE&");             // for testing
			}
			sb.append("x_method=CC&");
			sb.append("x_type=AUTH_CAPTURE&");
			sb.append("x_amount=" + invoiceAmount + "&");
			sb.append("x_delim_data=TRUE&");
			sb.append("x_delim_char=|&");
			sb.append("x_relay_response=FALSE&");
	
			// billing information
			sb.append("x_first_name=" + firstName + "&");
			sb.append("x_last_name=" + lastName + "&");
			sb.append("x_address=" + address + "&");
			sb.append("x_city=" + city + "&");
			sb.append("x_state=" + state + "&");
			sb.append("x_zip=" + zip + "&");

//			sb.append("x_invoice_num=" + + "&");
			sb.append("x_description=ConferenceRegistration&");
	

			// CC information
			sb.append("x_card_num=" + cardNumber + "&");
			sb.append("x_exp_date=" + expirationDate + "&");
			sb.append("x_card_code=" + cardCVV + "&");

			// not required...but my test account is set up to require it
			sb.append("x_description=Conference Registration&");


			logger.debug("Calling authorize.net...");
			logger.debug("Callstring: " + sb.toString());
			// open secure connection
			URL url = new URL(
//					"https://test.authorize.net/gateway/transact.dll");
			//  Uncomment the line ABOVE for test accounts or BELOW for live merchant accounts
					"https://secure.authorize.net/gateway/transact.dll");

			/* NOTE: If you want to use SSL-specific features,change to:
			   HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			   */

			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			connection.setUseCaches(false);

			// not necessarily required but fixes a bug with some servers
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

			// POST the data in the string buffer
			DataOutputStream out = new DataOutputStream( connection.getOutputStream() );
			out.write(sb.toString().getBytes());
			out.flush();
			out.close();

			logger.debug("Done, reading response...");

			// process and read the gateway response
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			line = in.readLine();
			in.close();	                     // no more data
			logger.debug("Response: " + line);
			// System.err.println(line);



			// ONLY FOR THOSE WHO WANT TO CAPTURE GATEWAY RESPONSE INFORMATION
			// make the reply readable (be sure to use the x_delim_char for the split operation)
			Vector ccrep = split("|", line);

			logger.debug("Response Code ----------------: " + ccrep.elementAt(0));
			logger.debug("Human Readable Response Code -: " + ccrep.elementAt(3));
			logger.debug("Approval Code ----------------: " + ccrep.elementAt(4));
			logger.debug("Trans ID ---------------------: " + ccrep.elementAt(6));
			logger.info("AuthorizeNet Exiting successfully");
			return line;


		}catch(Exception e){
			e.printStackTrace();
			logger.info("AuthorizeNet Exiting with a failure, returning null");
			return null;
		}
	}


	// utility functions
	public static Vector split(String pattern, String in){
		int s1=0, s2=-1;
		Vector out = new Vector(30);
		while(true){
			s2 = in.indexOf(pattern, s1);
			if(s2 != -1){
				out.addElement(in.substring(s1, s2));
			}else{
				//the end part of the string (string not pattern terminated)
				String _ = in.substring(s1);
				if(_ != null && !_.equals("")){
					out.addElement(_);
				}
				break;
			}
			s1 = s2;
			s1 += pattern.length();
		}
		return out;
	}

	// by Roedy Green (c)1996-2003 Canadian Mind Products
	public static String toHexString ( byte[] b ){
		StringBuffer sb = new StringBuffer( b.length * 2 );
		for ( int i=0 ; i<b.length ; i++ )
		{
			// look up high nibble char
			sb.append( hexChar [ ( b[ i] & 0xf0 ) >>> 4 ] ) ;

			// look up low nibble char
			sb.append( hexChar [ b[ i] & 0x0f ] ) ;
		}
		return sb.toString() ;
	}

	// table to convert a nibble to a hex character
	static char[] hexChar = {
		'0' , '1' , '2' , '3' ,
		'4' , '5' , '6' , '7' ,
		'8' , '9' , 'A' , 'B' ,
		'C' , 'D' , 'E' , 'F' }
	;
}


