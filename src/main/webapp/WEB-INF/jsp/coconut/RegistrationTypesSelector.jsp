<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Edit / Create Registrant Types</title>
</head>
<body>
<div class="wrapper">

	<jsp:include page="PageHeader.jsp" />
	<s:form name="lookup" action="coconut/editRegistrationTypes">
	<div id="content_wrapper">
		<div id="primary_content_wide">

			<table>
				<tr>
					<th>Func</th>
					<th>
						<s:url id="sortbyname" action="coconut/editRegistrantTypesOrder" >
							<s:param name="order">reg_name</s:param>
						</s:url>
						<s:a href="%{sortbyname}">Name</s:a>
					</th>
					<th>Desc</th>
					<th>Print</th>
					<th>Cost</th>
					<th>Count</th>
					<th>
						<s:url id="sortbysequence" action="coconut/editRegistrantTypesOrder" >
							<s:param name="order">reg_sequence</s:param>
						</s:url>
						<s:a href="%{sortbysequence}">Seq</s:a>
					</th>
					<th>Discount Code</th>
					<th>Active?</th>
					<th>Comp?</th>
				</tr>
				<c:forEach items="${regTypes}" var="rows">
					<c:set var="cellColor">
			 			<c:choose>
				 			<c:when test="${cellColor == '#f0f0f0'}">#ffffcc</c:when>
			 				<c:otherwise>#f0f0f0</c:otherwise>
				 		</c:choose>
					</c:set>
					<tr bgcolor="${cellColor}">
						<td>
							<s:url id="editurl" action="coconut/editRegistrationTypes" >
								<s:param name="whichRegtype">${rows.value.regName}</s:param>
							</s:url>
							<s:url id="deleteurl" action="coconut/deleteRegistrationTypes" >
								<s:param name="whichRegtype">${rows.value.regName}</s:param>
							</s:url>
							<s:a href="%{editurl}"><img src="graphics/edit.png" border=0 height="15" width="15"></s:a>
							<s:a href="%{deleteurl}"><img src="graphics/eventdelete.png" border=0 height="15" width="15"></s:a>
						</td>
						<td>${rows.value.regName}</td>
						<td>${rows.value.regDesc}</td>
						<td>${rows.value.regPrint}</td>
						<td>${rows.value.regCost}</td>
						<td>${rows.value.regCount}</td>
						<td>${rows.value.regSequence}</td>
						<td>${rows.value.regDiscountCode}</td>
						<td>${rows.value.regActive}</td>
						<td>${rows.value.regComp}</td>
					</tr>
				</c:forEach>
			</table>
			<input type="submit" accesskey="c" name="typebutton" value="Create" class="button">
			<input type="submit" accesskey="i" name="typebutton" value="Import from Another Event" class="button">
		</div>
		<jsp:include page="PageFooter.jsp" />
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