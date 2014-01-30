<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Delete Registrant</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>
	<s:form name="edit" action="coconut/deleteEventProcess">
	<s:hidden name="id" />
	
	<div id="content_wrapper">
		<div id="floating_content">
			Delete Event
		</div>
	
		<div id="primary_content">
			<strong>WARNING</strong><br />
			You are requesting to delete Event # ${workingEvent.conCID} - '${workingEvent.conName}'.  This is a big deal!<br />
			<br />
			If you proceed, the following things will happen:
			<ul>
			<li>Any history on registrants associated with this event will be deleted.
			<li>Any invoice history for this event will be deleted.
			<li>All state information for registrants for this event will be deleted.
			<li>All configuration information for this event will be deleted.
			</ul>
			Are you really sure you want to do this?
	
			<input type="submit" name="deleteButton" value="Yes, delete this event" class="button">
			<input type="submit" name="cancelButton" value="No, don't do this." class="button">
		</div>	
		<jsp:include page="PageFooter.jsp" />
	</div>
</div>
</s:form>
<script language="JavaScript">
<!--
document.edit.whichTemplate.focus();

/* Find the current menu pick, and make sure its class is right... */
document.getElementById("maintenanceMenuPick").setAttribute("class","active");
document.getElementById("maintenanceMenuText").setAttribute("class","active");

//-->
</script>
</body>
</html>