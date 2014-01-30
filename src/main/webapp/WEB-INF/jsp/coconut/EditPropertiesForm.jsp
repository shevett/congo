<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Edit Registrant Properties</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp" />
	
	<jsp:include page="RegistrantSubNav.jsp" />

	<div id="content_wrapper">
	
		<jsp:include page="RegistrantHeader.jsp" />

		<div id="primary_content">
			<s:form name="edit" action="coconut/updateProperties">
			<table>
				<tr>
					<th>Name</th>
					<th>Description</th>
					<th>Type</th>
					<th>Scope</th>
					<th>Local Value</th>
					<th>Cost</th>
					<th>Functions</th>
					<th>Reset</th>
				</tr>
				<c:forEach var="rows" items="${propertyList}">
					<c:set var="cellColor">
		 				<c:choose>
		 					<c:when test="${cellColor == '#f0f0f0'}">#ffffcc</c:when>
							<c:otherwise>#f0f0f0</c:otherwise>
	 					</c:choose>
					</c:set>
					<tr bgcolor="${cellColor}">
					<td>${rows.name}</td>
					<td>${rows.description }</td>
					<td>${rows.type}</td>
					<td>
						<c:if test="${rows.isglobal}">
							Global
						</c:if>
						<c:if test="${! rows.isglobal}">
							This Event
						</c:if>
					</td>
					<td>
						<c:if test="${rows.isdefault}">
							<i>Use system default</i>
						</c:if>
						${rows.value}
					</td>
					<td>${rows.cost }</td>
					<c:if test="${rows.editable}">
					<td>
						<c:if test="${rows.type == 'boolean'}">
							<button type="submit" value="toggle:${rows.name}" name="propertyaction">Toggle</button>
						</c:if>
						<c:if test="${rows.type == 'string'}">
							<input type="text" name="${rows.name}" value="${rows.value}">
							<button type="submit" value="set:${rows.name}" name="propertyaction">Set</button>
						</c:if>
						<c:if test="${rows.type == 'numeric'}">
							<input type="text" name="${rows.name}" value="${rows.value}">
							<button type="submit" value="set:${rows.name}" name="propertyaction">Set</button>
						</c:if>
						<c:if test="${rows.type == 'picker'}">
							<input type="text" name="${rows.name}" value="${rows.value}">
							<button type="submit" value="set:${rows.name}" name="propertyaction">Set</button>
						</c:if>
					</td>
					<td>
						<c:if test="${! rows.isdefault}">
							<button type="submit" name="propertyaction" value="remove:${rows.name}:${rows.isglobal}">Remove local value</button>
						</c:if>
					</td>
					</c:if>
				</tr>
			</c:forEach>
			</table>
		</div>
		<jsp:include page="RegistrantSidebar.jsp" />
		<jsp:include page="PageFooter.jsp" />
		
	</div>
</div>
</s:form>

<script language="JavaScript">
<!--

/* Find the current menu pick, and make sure its class is right... */
document.getElementById("registrantMenuPick").setAttribute("class","active");
document.getElementById("registrantMenuText").setAttribute("class","active");	
document.getElementById("propertySide").setAttribute("class","active");

//-->
</script>

</body>