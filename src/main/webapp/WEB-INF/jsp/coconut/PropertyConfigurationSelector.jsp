<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Maintain Property Configuration</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>
	<s:form action="coconut/maintPropertiesFormButton">
		
	<div id="content_wrapper">
		<div id="primary_content_wide">
			<table>
				<tr>
					<th>Func</th>
					<th>Name</th>
					<th>Scope</th>
					<th>Default Value</th>
					<th>Type</th>
					<th>Prompt?</th>
					<th>Cost</th>
					<th>Description</th>
					<th>Sequence</th>
				</tr>
				<c:forEach items="${propertyConfigurationList}" var="rows">
						<c:set var="cellColor">
			 				<c:choose>
				 				<c:when test="${cellColor == '#f0f0f0'}">#ffffcc</c:when>
			 					<c:otherwise>#f0f0f0</c:otherwise>
				 			</c:choose>
						</c:set>
					<tr bgcolor="${cellColor}">
						<td>
							<c:if test="${(rows.name != 'Administrator') && (rows.name != 'Operator')}">
								<s:url id="editurl" action="coconut/editPropertyConfiguration" >
									<s:param name="function">edit</s:param>
									<s:param name="name">${rows.name}</s:param>
								</s:url>
								<s:url id="deleteurl" action="coconut/maintPropertiesFormButton" >
									<s:param name="function">delete</s:param>
									<s:param name="name">${rows.name}</s:param>
								</s:url>
								<s:a href="%{editurl}"><img src="graphics/edit.png" border=0></s:a>
								<s:a href="%{deleteurl}"><img src="graphics/eventdelete.png" border=0></s:a>
							</c:if>
						</td>
						<td>${rows.name}</td>
						<td>${rows.scope}</td>
						<td>${rows.defaultvalue}</td>
						<td>${rows.type}</td>
						<td>${rows.regprompt}</td>
						<td>${rows.cost}</td>
						<td>${rows.description}</td>
						<td>${rows.sequence}</td>
					</tr>
				</c:forEach>
			</table>
			<input type="submit" accesskey="C" value="Create" name="createbutton" class="button">
		</div>
		<jsp:include page="PageFooter.jsp"></jsp:include>
		
	</div>
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