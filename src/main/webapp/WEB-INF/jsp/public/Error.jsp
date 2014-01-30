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
	<form method="post" action="MyHome.action">
	
	<%@include file="PageHeader.jsp" %>

	<div class="formbody">
		${message}
	</div>


	<div class="buttonbar">
		<input type="submit" name="homebutton" class="webbutton" value="Home">
	</div>
	
	<%@include file="PageFooter.jsp" %>
	</form>
</body>
</html>