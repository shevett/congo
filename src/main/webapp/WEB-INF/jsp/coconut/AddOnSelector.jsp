<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="coconut.css"/>" rel="stylesheet" type="text/css"/>
	<title>Edit / Create AddOns</title>
</head>
<body>
<jsp:include page="PageHeader.jsp"></jsp:include>
<div class="bodybox">
<s:form name="lookup" action="coconut/editAddOn">
	<div class="menuboxes menuheader">
		Maintain AddOns
	</div>

<div class="menuboxes">
<table>
	<tr>
		<th>Func</th>
		<th>Name</th>
		<th>Desc</th>
		<th>Last Modified</th>
	</tr>
	<c:forEach items="${addons}" var="addons">
		<c:set var="cellColor">
 			<c:choose>
				<c:when test="${cellColor == '#f0f0f0'}">#ffffcc</c:when>
 				<c:otherwise>#f0f0f0</c:otherwise>
	 		</c:choose>
		</c:set>
		<tr bgcolor="${cellColor}">			
			<td>
				<s:url id="editurl" action="coconut/editAddOn" >
					<s:param name="function">edit</s:param>
					<s:param name="whichAddOn">${addons.value.name}</s:param>
				</s:url>
				<s:url id="deleteurl" action="coconut/AddOnDeletePrompt" >
					<s:param name="whichAddOn">${addons.value.name}</s:param>
				</s:url>
				<s:a href="%{editurl}"><img src="graphics/edit.png" border=0></s:a>
				<s:a href="%{deleteurl}"><img src="graphics/eventdelete.png" border=0></s:a>
			</td>
			
			<td>${addons.value.name}</td>
			<td>${addons.value.desc}</td>
			<td>${addons.value.lastmodified}</td>
		</tr>
	</c:forEach>
</table>
</div>

<div class="menuboxes">
	<button type="submit" accesskey="C" name="typebutton">
		<img align="left" src="graphics/ok.png" height=15 width=15>[C]reate
	</button>
	<button type="submit" accesskey="X" name="exitbutton">
		<img align="left" src="graphics/start.png" height=15 width=15>E[x]it to Maintenance Menu
	</button>
</div>
</s:form>
</div>
</body>
</html>