<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Edit Venue</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>
	<jsp:include page="SchedulingSubNav.jsp" />
	
	<s:form name="edit" action="coconut/updateVenue">
	<div id="content_wrapper">
		<div id="floating_content">
			Edit Venue...
		</div>
	
		<s:hidden name="id" />
	
		<div class="message">
			${message}
		</div>
	
		<div id="primary_content">
			<table>
				<tr><td>Name</td>
					<td><s:textfield name="workingvenue.venueName" size="50" /></td></tr>
				<tr><td>Website</td>
					<td><s:textfield name="workingvenue.venueWebsite" size="50"  /></td></tr>
				<tr><td>Location</td>
					<td><s:textfield name="workingvenue.venueLocation" size="50" /></td></tr>
				<tr><td>Notes</td>
					<td><s:textfield name="workingvenue.venueNotes" /></td></tr>
				<tr><td>Phone</td>
					<td><s:textfield name="workingvenue.venuePhone" size="20" /></td></tr>
				<tr><td>Contact</td>
					<td><s:textfield name="workingvenue.venueContact" size="50" /></td></tr>
				<tr><td>Rooms</td>
					<td><s:textfield name="workingvenue.venueRooms" /></td></tr>
				<tr><td>Footage</td>
					<td><s:textfield name="workingvenue.venueFootage" /></td></tr>
			</table>
			<s:fielderror/>
			<input type="submit" name="typebutton" accesskey="S" value="Save" class="button">
			<input type="submit" name="exitbutton" accesskey="X" value="Exit without saving" class="button">
		</div>
		<jsp:include page="PageFooter.jsp"></jsp:include>
	</div>
</div>
</s:form>
<script language="JavaScript">
<!--
document.edit.whichVenue.focus();

/* Find the current menu pick, and make sure its class is right... */
document.getElementById("maintenanceMenuPick").setAttribute("class","active");
document.getElementById("maintenanceMenuText").setAttribute("class","active");

//-->
</script>
	
</body>