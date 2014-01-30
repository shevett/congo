<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Edit a phone number</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp" />
	<s:form name="editphone" action="coconut/savePhone">
	
	<jsp:include page="RegistrantSubNav.jsp" />

	<div id="content_wrapper">
	
		<jsp:include page="RegistrantHeader.jsp" />

		<div id="primary_content">
			<%@ include file="PhoneFormFields.jspf" %>
			<button type="submit" accesskey="S" value="Save" class="button"><u>S</u>ave</button>
			<button type="submit" accesskey="D" value="Delete" name="deleteButton" class="button"><u>D</u>elete</button>
		</div>
		<jsp:include page="PageFooter.jsp" />
	</div>
</div>
</s:form>
<script language="JavaScript">
<!--
/* Find the current menu pick, and make sure its class is right... */
document.getElementById("registrantMenuPick").setAttribute("class","active");
document.getElementById("registrantMenuText").setAttribute("class","active");	
-->
</script>
			
</body>
</html>