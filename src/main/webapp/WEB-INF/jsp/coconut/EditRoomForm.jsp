<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Edit Room</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>
	<jsp:include page="SchedulingSubNav.jsp" />
	
	<s:form name="edit" action="coconut/updateRoom">
	<div id="content_wrapper">
		<div id="floating_content">
			Edit Room...
		</div>
	
		<s:hidden name="id" />
	
		<div id="primary_content">
		<div class="message">
			${message}
			<s:fielderror/>
		</div>
			<table>
				<tr><td>Name</td>
					<td><s:textfield name="workingroom.roomName" size="50" /></td></tr>
				<tr><td>Venue</td>
					<td><s:textfield name="workingroom.roomVenue" size="50"  /></td></tr>
				<tr><td>Location</td>
					<td><s:textfield name="workingroom.roomLocation" size="50" /></td></tr>
				<tr><td>Phone</td>
					<td><s:textfield name="workingroom.roomPhone" size="20" /></td></tr>
				<tr><td>Width</td>
					<td><s:textfield name="workingroom.roomWidth" size="5" /></td></tr>
				<tr><td>Length</td>
					<td><s:textfield name="workingroom.roomLength" size="5" /></td></tr>
				<tr><td>Height</td>
					<td><s:textfield name="workingroom.roomHeight" size="5" /></td></tr>
				<tr><td>Capacity</td>
					<td><s:textfield name="workingroom.roomCapacity" size="5" /></td></tr>			
			</table>
			<input type="submit" name="typebutton" accesskey="S" value="Save" class="button">
			<input type="submit" name="exitbutton" accesskey="X" value="Exit without saving" class="button">
		</div>
		<jsp:include page="PageFooter.jsp"></jsp:include>
	</div>
</div>
</s:form>
<script language="JavaScript">
<!--
document.edit.whichRoom.focus();

/* Find the current menu pick, and make sure its class is right... */
document.getElementById("maintenanceMenuPick").setAttribute("class","active");
document.getElementById("maintenanceMenuText").setAttribute("class","active");

//-->
</script>
	
</body>