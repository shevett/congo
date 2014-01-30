<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Edit / Create Discount Codes</title>
</head>
<body>

<div class="wrapper">

	<jsp:include page="PageHeader.jsp"></jsp:include>
	<s:form name="lookup" action="coconut/editDiscountCode">
	<div id="content_wrapper">
		<div id="primary_content_wide">
			<div class="message">
				${message}
			</div>

			<table>
				<tr>
					<th>Func</th>
					<th>Name</th>
					<th>Desc</th>
					<th>Type</th>
					<th>Factor</th>
					<th>Active?</th>
					<th>Last Modified</th>
				</tr>
				<c:forEach items="${discountcodes}" var="dc">
					<c:set var="cellColor">
			 			<c:choose>
							<c:when test="${cellColor == '#f0f0f0'}">#ffffcc</c:when>
			 				<c:otherwise>#f0f0f0</c:otherwise>
				 		</c:choose>
					</c:set>
					<tr bgcolor="${cellColor}">			
						<td>
							<s:url id="editurl" action="coconut/editDiscountCode" >
								<s:param name="function">edit</s:param>
								<s:param name="whichDiscountCode">${dc.value.name}</s:param>
							</s:url>
							<s:url id="deleteurl" action="coconut/deleteDiscountCode" >
								<s:param name="whichDiscountCode">${dc.value.name}</s:param>
							</s:url>
							<s:a href="%{editurl}"><img src="graphics/edit.png" border=0></s:a>
							<s:a href="%{deleteurl}"><img src="graphics/eventdelete.png" border=0></s:a>
						</td>
						
						<td>${dc.value.name}</td>
						<td>${dc.value.desc}</td>
						<td>${dc.value.type}</td>
						<td>${dc.value.factor}</td>
						<td>${dc.value.active}</td>
						<td>${dc.value.lastmodified}</td>
					</tr>
				</c:forEach>
			</table>
			<input type="submit" name="typebutton" accesskey="C" value="Create" class="button">
		</div> <!-- end primary_content_wide -->
		<jsp:include page="PageFooter.jsp" />
	</div> <!-- end content_wrapper -->
</div> <!-- end wrapper -->
</s:form>
</body>
</html>