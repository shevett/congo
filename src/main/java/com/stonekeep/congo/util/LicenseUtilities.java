package com.stonekeep.congo.util;

import java.math.BigInteger;
import java.security.MessageDigest;

import org.apache.log4j.Logger;

public class LicenseUtilities {
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(LicenseUtilities.class);
	
	public String keyname ;
	public String keytext ;
	
	public LicenseUtilities(String keyname, String keytext) {
		this.keyname = keyname;
		this.keytext = keytext;
	}
	
	public static boolean validate(String keyname, String keytext) throws Exception {
		// This functionality is no longer needed in the GPL version of CONGO.  Removed.
		return true;
	}

}
