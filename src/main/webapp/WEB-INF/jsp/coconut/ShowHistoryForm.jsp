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
	<title>Show History</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>
	<jsp:include page="RegistrantSubNav.jsp" />
		
	<div id="content_wrapper">
		<jsp:include page="RegistrantHeader.jsp" />
	
		<div id="primary_content">
			<table>
				<tr>
					<th width="5%">Id</th>
					<th width="5%">CID</th>
					<th width="20%">Date</th>
					<th width="10%">Operator</th>
					<th width="10%">Actcode</th>
					<th width="40%">Comment</th>
					<th width="10%">Arg1</th>
					<th width="10%">Arg2</th>
				</tr>
				<c:forEach items="${history}" var="rows">
					<c:set var="cellColor">
						<c:choose>
							<c:when test="${cellColor == '#f0f0f0'}">#ffffcc</c:when>
							<c:otherwise>#f0f0f0</c:otherwise>
						</c:choose>
					</c:set>
			
					<tr bgcolor="${cellColor}">
						<td width="10%">${rows.id}</td>
						<td width="5%">${rows.cid}</td>
						<td width="15%">${rows.activity}</td>
						<td width="10%">${rows.operator }</td>
						<td width="10%">${rows.actcode }</td>
						<td width="40%">${rows.comment }</td>
						<td width="10%">${rows.arg1 }</td>
						<td width="10%">${rows.arg2 }</td>
					</tr>
				</c:forEach>
			</table>
		</div>
		<jsp:include page="RegistrantSidebar.jsp" />
		<jsp:include page="PageFooter.jsp" />
	</div>
</div>

<script language="JavaScript">
<!--

/* Find the current menu pick, and make sure its class is right... */
document.getElementById("registrantMenuPick").setAttribute("class","active");
document.getElementById("registrantMenuText").setAttribute("class","active");	
document.getElementById("historySide").setAttribute("class","active");

//-->
</script>

</body>