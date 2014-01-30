<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Register...</title>
</head>
<body onload="setprice()">
	<div class="wrapper">
		<jsp:include page="PageHeader.jsp" />
		<s:form name="edit" action="coconut/invoiceaddRegistrant">
		
		<jsp:include page="RegistrantSubNav.jsp" />		

		<div id="content_wrapper">
			<div id="primary_content">
				<c:if test="${ empty sessionScope.workinginvoice }">
					No invoice currently in progress.
				</c:if>
				<c:if test="${ not empty sessionScope.workinginvoice }">
					Current working invoice: # ${workinginvoice.id} : $ ${workinginvoice.amount} 
					<table width="80%">
						<tr>
							<th>#</th>
							<th>By</th>
							<th>For</th>
							<th>Date</th>
							<th>Type</th>
							<th></th>
							<th>Comment</th>
							<th>Amount</th>
						</tr>
						<c:forEach var="rows" items="${workinginvoice.detailList}">
							<tr>
								<td>${rows.sequence}</td>
								<td>${rows.operator}</td>
								<td>${rows.rid }</td>
								<td>${rows.activity}</td>
								<td>${rows.type}</td>
								<td>${rows.type2}</td>
								<td>${rows.comment}</td>
								<td>${rows.finalamount}</td>
							</tr>
						</c:forEach>
					</table>
				</c:if>
				<table>
					<tr>
						<td>Membership Type </td>
						<td>Price</td>
						<td>Comment</td>
					</tr>
					<tr>
						<td>		
							<s:select 
								list="rtList"
								listKey="value.regName"
								listValue="key + ' (' + value.CostText + ' ) '"
								value="key" 
								name="regtype" onchange="setprice()"/>
						</td>
						<td>
							<input name="regprice" value="0.00">
						</td>
						<td>
							<input name="regcomment" value=" ">
						</td>
						<td>
							<input type="submit" accesskey="A" name="registerbutton" value="Add this registration" class="button">
						</td>
					</tr>
				</table>
		</div>
		</div>
	</div>
</s:form>
<script language="JavaScript">
<!--
	document.edit.regtype.focus();
	
	function setprice() {
		var y=document.edit.regtype.options[document.edit.regtype.selectedIndex].text; 
		myregexp = /^.*\(\\$([^)]*)\).*$/;
		match=myregexp.exec(y);
		document.edit.regprice.value=match[1];
		return true;
 	}
-->
</script>
</body>