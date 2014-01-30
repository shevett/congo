<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Edit / Create Events</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>
		
	<div id="content_wrapper">
		<div id="floating_content">
			Events
		</div>
		<div id="primary_content">
			<s:form name="lookup" action="coconut/createEvent">
		
			<table>
				<tr>
					<th>Func</th>
					<th>ID</th>
					<th>Name</th>
					<th>Location</th>
					<th>Start</th>
					<th>End</th>
					<th>Reg</th>
					<th>Badged</th>
					<th>Checked</th>
					<th>Active</th>
					<th>Select</th>
				</tr>
				<c:forEach items="${events}" var="event" varStatus="status">
					<c:set var="cellColor">
						<c:choose>
							<c:when test="${status.count % 2 == 0}">#ffffcc</c:when>
							<c:otherwise>#f0f0f0</c:otherwise>
						</c:choose>
					</c:set>
					<tr bgcolor="${cellColor}">
						<s:url id="editurl" action="coconut/editEvent">
							<s:param name="id">${event.value.conCID}</s:param>
						</s:url>
						<s:url id="deleteurl" action="coconut/deleteEvent">
							<s:param name="id">${event.value.conCID}</s:param>
						</s:url>
			
						<td><s:a href="%{editurl}">
							<img src="graphics/edit.png" border="0">
						</s:a><s:a href="%{deleteurl}">
							<img src="graphics/eventdelete.png" border="0">
						</s:a></td>
						<td>${event.value.conCID }</td>
						<td>${event.value.conName}</td>
						<td>${event.value.conLocation}</td>
						<td><fmt:formatDate type="date" dateStyle="short"
							 value="${event.value.conStart}" /></td>
						<td><fmt:formatDate type="date" dateStyle="short"
							timeStyle="short" value="${event.value.conEnd}" /></td>
						<td>${event.value.numRegistered}</td>
						<td>${event.value.numBadged}</td>
						<td>${event.value.numCheckedin}</td>
						<td>
							<c:choose>
								<c:when test="${event.value.conCID == eventDefault}">
									<img src="graphics/ok.png" border="0">
								</c:when>
								<c:otherwise>
									<s:url id="activate" action="coconut/activateEvent">
										<s:param name="id">${event.value.conCID}</s:param>
									</s:url>
									<s:a href="%{activate}">
										Activate
									</s:a>
								</c:otherwise>
							</c:choose>
						</td>
						<td>
							<c:choose>
								<c:when test="${event.value.conCID == conference.conCID}">
									<img src="graphics/ok.png" border="0">
								</c:when>
								<c:otherwise>
									<s:url id="activate" action="coconut/selectEvent">
										<s:param name="id">${event.value.conCID}</s:param>
									</s:url>
									<s:a href="%{activate}">
										Select
									</s:a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:forEach>
			</table>
			<input type="submit" accesskey="C" value="Create" name="typebutton" class="button">
		</div>
		<div id="sidebar">
			<ul>
				<li class="active"><p><strong>Activate</strong><br />
				This event is active on the public interface</p></li>
				<li class="active"><p><strong>Select</strong><br />
				This is the event you are currently working with.</p></li>
			</ul>
		</div>
		
		<jsp:include page="PageFooter.jsp"></jsp:include>
</div>
</s:form>
<script language="JavaScript">
<!--

/* Find the current menu pick, and make sure its class is right... */
document.getElementById("maintenanceMenuPick").setAttribute("class","active");
document.getElementById("maintenanceMenuText").setAttribute("class","active");

//-->
</script>
</body>
</html>