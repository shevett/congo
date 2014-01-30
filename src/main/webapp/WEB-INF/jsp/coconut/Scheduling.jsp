<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Edit / Create Sessions</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>
	
	<jsp:include page="SchedulingSubNav.jsp" />
	
	<s:form name="create" action="coconut/createSession">
		
	<div id="content_wrapper">
	
		<div id="floating_content">
			Sessions
		</div>
	
		<div id="primary_content_wide">
			<table>
				<tr>
					<th>Func</th>
					<th>Title/Description</th>
					<th>Details</th>
					<th>Status</th>
					<th>Schedule</th>
				</tr>
				<c:forEach var="rows" items="${sessions}">
					<c:set var="cellColor">
			 			<c:choose>
							<c:when test="${cellColor == '#f0f0f0'}">#ffffcc</c:when>
			 				<c:otherwise>#f0f0f0</c:otherwise>
				 		</c:choose>
					</c:set>
					<tr bgcolor="${cellColor}">			
						<td width="5%">
							<s:url id="editurl" action="coconut/editSession" >
								<s:param name="function">edit</s:param>
								<s:param name="id">${rows.id}</s:param>
							</s:url>
							<s:url id="deleteurl" action="coconut/deleteSession" >
								<s:param name="id">${rows.id}</s:param>
							</s:url>
							<s:a href="%{editurl}"><img src="graphics/edit.png" border=0></s:a>
							<s:a href="%{deleteurl}"><img src="graphics/eventdelete.png" border=0></s:a>
						</td>
						
						<td width="35%">
							<b>${rows.sessionTitle}</b><br />
							${rows.sessionDescription}
						</td>
						<td width="20%">
							Owner: ${rows.sessionOwner}<br/>
							MaxAttendees: ${rows.sessionMaxAttendees}<br/>
						</td>
						<td width="20%">
							Status: ${rows.sessionStatus}<br/>
							Public: ${rows.sessionPublic}<br />
							Last Edit: ${rows.sessionLastUpdated} by ${rows.sessionLastEditedBy }
						</td>
						<td width="20%">
							Start: ${rows.sessionStart}<br/>
							End: ${rows.sessionEnd}<br />
							Duration: ${rows.sessionDuration}<br />
							Room: ${rows.sessionRoom}
							Layout: [undefined]
						<td>
					</tr>
				</c:forEach>
			</table>
			<input type="submit" accesskey="C" value="Create" name="typebutton" class="button">
		</div>
		<jsp:include page="PageFooter.jsp"></jsp:include>
	</div>
</div>
</s:form>
<script language="JavaScript">
<!--

/* Find the current menu pick, and make sure its class is right... */
document.getElementById("schedulingMenuPick").setAttribute("class","active");
document.getElementById("schedulingMenuText").setAttribute("class","active");
document.getElementById("sessionSubPickText").setAttribute("class","active");	

//-->
</script>

</body>
</html>