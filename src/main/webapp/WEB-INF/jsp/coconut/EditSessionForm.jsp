<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Edit Session</title>
	<script type="text/javascript" src="js/jquery-1.7.1.js"></script>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>
	<jsp:include page="SchedulingSubNav.jsp"/>
	<s:form name="edit" action="coconut/updateSession">
	<div id="content_wrapper">
		<div id="floating_content">
			Edit Session...
		</div>
	
		<s:hidden name="id" />
	
		<div id="primary_content">
		<div class="message">
			${message}
			<s:fielderror/>
		</div>
			<table>
				<tr><td>Title</td>
					<td><s:textfield name="workingsession.sessionTitle" size="60"  maxlength="100"/></td></tr>
				<tr><td>Description</td>
					<td><s:textarea rows="5" cols="60" name="workingsession.sessionDescription" /></td></tr>
				<tr><td>Detail</td>
					<td><s:textarea rows="5" cols="60" name="workingsession.sessionDetail" /></td></tr>
				<tr><td>Notes</td>
					<td><s:textfield name="workingsession.sessionNotes" size="50" maxlength="200" /></td></tr>
				<tr><td>Tags</td>
					<td><s:textfield name="workingsession.sessionTags" size="50" maxlength="200" /></td></tr>
				<tr><td>Department</td>
					<td><s:textfield name="workingsession.sessionDepartment" size="5" /></td></tr>
				<tr><td>Category</td>
					<td><s:textfield name="workingsession.sessionCategory" size="5" /></td></tr>
				<tr><td>Start</td>
					<td><s:textfield name="workingsession.sessionStart" size="30"/></td></tr>			
				<tr><td>End</td>
					<td><s:textfield name="workingsession.sessionEnd" size="30"/></td></tr>			
				<tr><td>Duration</td>
					<td><s:textfield name="workingsession.sessionDuration" size="5" /></td></tr>			
				<tr><td>MaxAttendees</td>
					<td><s:textfield name="workingsession.sessionMaxAttendees" size="5" /></td></tr>			
				<tr><td>Cost</td>
					<td><s:textfield name="workingsession.sessionCost" size="5" /></td></tr>			
				<tr><td>Owner</td>
					<td><s:textfield name="workingsession.sessionOwner" size="5" /></td></tr>			
				<tr><td>Status</td>
					<td>
						<s:select key="workingsession.sessionStatus" list="statuses"/>
					</td>
				<tr><td>Public</td>
					<td><s:checkbox name="workingsession.sessionPublic"/></td></tr>			
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
document.edit.whichSession.focus();

/* Find the current menu pick, and make sure its class is right... */
document.getElementById("schedulingMenuPick").setAttribute("class","active");
document.getElementById("schedulingMenuText").setAttribute("class","active");
document.getElementById("sessionSubPickText").setAttribute("class","active");	

$.getJSON('coconut/getRegistrant', 555 { });

//-->
</script>
	
</body>