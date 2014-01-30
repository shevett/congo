<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<LINK REL="StyleSheet" href="getConfiguredStylesheet.action" type="text/css">
	<title>${currentConvention.conName}</title>
</head>
<body>
	<form method="post" action="MyHome.action" name="changeform">
	
	<%@include file="PageHeader.jsp" %>
	
	<div class="inputform">
	
		<div class="title">
			Your payment history<hr>
		</div>
		
		<div style="float: right; width: 200px;">
			<div class='box-orange'>
	 			<div class='boxtop-orange'><div></div></div>
  					<div class='boxcontent-orange'>
	  					This page shows your most recent transactions.
 					</div>
	 			<div class='boxbottom-orange'><div></div></div>
			</div>
		</div>
	
		<div class="sectionbody">
		<c:choose>
			<c:when test="${empty invoices}">
				No invoices
			</c:when>
			
			<c:otherwise>

			<table width=80% class="sectiontable">
				<tr>
					<th>Invoice #</td>
					<th>Date</td>
					<th>Created by</td>
					<th>Comment</td>
					<th>Amount</td>
					<th>Type</td>
					<th>Status</td>
				</tr>
				
				
				<c:forEach var="rows" items="${ invoices }">
					<tr>
						<td>${rows.id}</td>
						<td>${rows.activity}</td>
						<td>${rows.creator} (${rows.creatorName})</td>
						<td>${rows.comment}</td>
						<td><fmt:formatNumber value="${rows.amount}" type="currency"/></td>
						<td>${rows.paytype}</td>
						<td>${rows.status}</td>
					</tr>
				</c:forEach>
				
			</table>
		
		<div class="message">${message}</div>
		</c:otherwise>
		</c:choose>
		
	</div>

	<div class="buttonbar">
		<input type="submit" name="exitbutton" class="webbutton" value="Return home">
	</div>

	<jsp:include page="PageFooter.jsp" />
	</form>
	
<script language="JavaScript">
<!--
document.createform.firstname.focus();
//-->
</script>
</body>
</html>
