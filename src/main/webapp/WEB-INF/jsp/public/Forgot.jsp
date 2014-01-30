<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>${currentConvention.conName}</title>
	<LINK REL="StyleSheet" href="getConfiguredStylesheet.action" type="text/css">
</head>
<body>
	<form method="post" action="webForgotForm.action" name="forgotform">
	
	<%@include file="PageHeader.jsp" %>

	<div class="formbody">
		For security reasons, it is not possible to retrieve your password and show you it, but 
		it can be reset it to something new.  Your new password will be emailed to you.  Once you
		log in, you can change it to whatever you like.<br>
		
		<br>
		
		Enter your Email address and a new password will be emailed to you.<br>
		<input name="email">
		
		<br>
		
		<div class="message">
			${message}
		</div>

	</div>


	<div class="buttonbar">
		<input type="submit" name="emailbutton" class="webbutton" value="Reset password">
		<input type="submit" name="cancelbutton" class="webbutton" value="Cancel">
	</div>
	
	<%@include file="PageFooter.jsp" %>
	</form>
<script language="JavaScript">
<!--
document.forgotform.email.focus();
//-->
</script>
	
</body>
</html>