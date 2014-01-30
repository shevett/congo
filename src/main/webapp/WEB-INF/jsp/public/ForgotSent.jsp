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
		Mail has been sent to ${email} with your new password.
	</div>

	<div class="buttonbar">
		<input type="submit" name="cancelbutton" class="webbutton" value="Ok">
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