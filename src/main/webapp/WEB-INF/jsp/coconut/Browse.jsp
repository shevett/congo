<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="<s:url value="css/styles.css"/>" rel="stylesheet"
	type="text/css" />
	<title>Browse Registrants</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>

	<div id="content_wrapper">
		<div id="primary_content">

			<s:form name="browse" action="coconut/processBrowse">
			<c:set var="maxrow" value="${skip + 200}"/>
			<c:if test='${totalrows < 200 }'>
				<c:set var="maxrow" value="${totalrows}"/>
			</c:if>
			<p>Browse returned ${totalrows} results. (showing ${skip} - ${maxrow})</p>
			<c:if test="${totalrows > 200}">
				<button name="slip" type="submit" value="${skip - 200}" accesskey="P">
					<u>P</u>revious
				</button>
				<button name="skip" type="submit" value="${skip + 200}" accesskey="N">
					<u>N</u>ext
				</button>
			</c:if>
			<table width="100%">
				<tr>
					<th>RegID</th>
					<th>Last Name</th>
					<th>First Name</th>
					<th>Badge Name</th>
					<th>Company</th>
					<th>Regtype</th>
					<th>Subd</th>
					<th>Regd</th>
					<th>Bdgd</th>
					<th>Chkd</th>
				</tr>
				<c:forEach var="rows" items="${registrantList}">
					<c:set var="cellColor">
 						<c:choose>
	 						<c:when test="${cellColor == '#f0f0f0'}">#ffffcc</c:when>
 							<c:otherwise>#f0f0f0</c:otherwise>
	 					</c:choose>
					</c:set>
					<s:url id="url" action="coconut/gotoUser" >
						<s:param name="rid">${rows.rid}</s:param>
					</s:url>
		
					<tr bgcolor="${cellColor}">
						<td><s:a href="%{url}">${rows.rid}</s:a></td>
						<td>${rows.lastName}</td>
						<td>${rows.firstName}</td>
						<td>${rows.badgeName}</td>
						<td>${rows.company}</td>
						<td>${rows.currentstate.regtype}</td>
						<td>${rows.currentstate.subscribedText }</td>
						<td>${rows.currentstate.registeredText }</td>
						<td>${rows.currentstate.badgedText }</td>
						<td>${rows.currentstate.checkedinText }</td>
					</tr>
				</c:forEach>
			</table>
		</div>
		
		<div id="sidebar">
			<ul>
				<li class="active">
					<p>Registration type<br />
					<s:select 
			    		headerKey="Any"
    					headerValue="Any"
						list="regtypeList"
						listKey="value.regName"
						listValue="value.regName"
						value="regtype" 
						name="regtype" />
					</p>
					
					<p>Registration Status<br />
					<s:select label="Registration Status"
						headerKey="-1"
						list="#{'-1':'Any','1':'Registered','0':'Not registered'}"
						name="status"/>
					</p>
					
					<p>Badged<br />
					<s:select label="Badged"
						list="#{'-1':'Any','1':'Badged','0':'Not badged'}"
						name="badged"/>
					</p>
					
					<p>Checkedin<br />
					<s:select label="Checkedin"
						list="#{'-1':'Any','1':'Checkedin','0':'Not checkedin'}"
						name="checkedin"/>
					</p>
					
					
					<button type="submit" name="browsebutton" value="Go" accesskey="R">
						<u>R</u>eload
					</button>
					
			</ul>
		</div>
		
		<jsp:include page="PageFooter.jsp"></jsp:include>
			
	</s:form>
</div>

<script language="JavaScript">
<!--
/* Find the current menu pick, and make sure its class is right... */
document.getElementById("browseMenuPick").setAttribute("class","active");
document.getElementById("browseMenuText").setAttribute("class","active");
//-->
</script>

</body>
</html>