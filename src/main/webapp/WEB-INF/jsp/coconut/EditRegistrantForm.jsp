<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Show Registrant</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"/>
	<jsp:include page="RegistrantSubNav.jsp"/>

	<s:form name="edit" action="coconut/EditRegistrant">
	<div id="content_wrapper">
		<jsp:include page="RegistrantHeader.jsp" />
	
		<div id="primary_content">
		<table>
			<tr><td colspan=2">${message}</td></tr>
			<tr><td>First Name</td>
				<td><s:textfield name="firstName" value="%{#session.workingregistrant.firstName}" /></td></tr>
			<tr><td>Last Name</td>
				<td><s:textfield name="lastName" value="%{#session.workingregistrant.lastName}" /></td></tr>
			<tr><td>Company</td>
				<td><s:textfield name="company" value="%{#session.workingregistrant.company}" /></td></tr>
			<tr><td>Badgename</td>
				<td><s:textfield name="badgeName" value="%{#session.workingregistrant.badgeName}" /></td></tr>
			<tr><td>Password</td>
				<td><s:textfield name="password" value="%{#session.workingregistrant.password}" /></td></tr>
			<tr><td colspan=2><hr></td></tr>
			<tr><td>Enabled?</td>
				<td><s:checkbox name="enabled" value="%{#session.workingregistrant.enabled}" /></td></tr>
			<tr><td>Comment</td>
				<td><s:textfield name="comment" value="%{#session.workingregistrant.comment}" /></td></tr>
			<tr><td>Merged to</td>
				<td>${workingregistrant.mergedTo}</td></tr>
		</table>
		<button name="savebutton" type="submit" accesskey="S" value="Save" class="button"><u>S</u>ave</button>
		<button name="exitbutton" type="submit" accesskey="X" value="exit" class="button">E<u>x</u>it without Saving</button>
		
		</div> <!-- end primary_content -->
	</div> <!-- end content_wrapper -->
</div> <!-- end wrapper -->
</s:form>
<script language="JavaScript">
<!--
/* Find the current menu pick, and make sure its class is right... */
document.getElementById("registrantMenuPick").setAttribute("class","active");
document.getElementById("registrantMenuText").setAttribute("class","active");	
document.getElementById("editSubText").setAttribute("class","active");	
-->
</script>
</body>
</html>