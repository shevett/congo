<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css" />
	<title>Create Registrant</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>
		
	<div id="content_wrapper">
		<div id="primary_content">
		
<s:form action="coconut/createRegistrant" name="createform">
<table>
	<tr><td colspan="2"><strong>${message}</strong></td></tr>
	<tr><td>First name</td><td><s:textfield name="fname"/></td></tr>
	<tr><td>Last name</td><td><s:textfield name="lname"/></td></tr>
	<tr><td>Company</td><td><s:textfield name="company"/></td></tr>
	<tr><td>Badgename</td><td><s:textfield name="badgename"/></td></tr>
	<tr><td>Address</td><td><s:textfield name="address"/></td></tr>
	<tr><td>&nbsp;</td><td><s:textfield name="address1"/></td></tr>
	<tr><td>City</td><td> <s:textfield name="city"/></td></tr>
	<tr><td>State </td><td><s:textfield name="state"/></td></tr>
	<tr><td>Zipcode </td><td><s:textfield name="zipcode" /></td></tr>
	<tr><td>Phone Number </td><td><s:textfield name="phonenumber" /></td></tr>
	<tr><td>Email address</td><td><s:textfield name="email"/></td></tr>
</table>
<button type="submit" accesskey="C" name="createbutton" class="button" value="Create"><u>C</u>reate</button>
</div>
</div>
</div>
</s:form>
<script language="JavaScript">
<!--
document.createform.fname.focus();
/* Find the current menu pick, and make sure its class is right... */
document.getElementById("createMenuPick").setAttribute("class","active");
document.getElementById("createMenuText").setAttribute("class","active");
//-->
</script>
</body>
</html>