<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<LINK REL="StyleSheet" href="getConfiguredStylesheet.action" type="text/css">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Pay Invoice : ${currentConvention.conName}</title>
</head>
<body>
	
	<%@include file="PageHeader.jsp" %>

	<form method="post" action="webPayPalComplete.action">
	
	<div class="inputform">

		<div class="title">
			Payment Error During Processing
		</div>
		
		<br>
	
		<div class="sectionbody">
			<table>
				<tr><td>Invoice # </td><td>${currentinvoice.id}</td></tr>
				<tr><td>Amount</td><td><fmt:formatNumber value="${currentinvoice.amount}" type="currency"/></td></tr>
			</table>
		</div>
		
		<br>
		<div class="title">
			Processing Failed
		</div>
		<div class="sectionbody">
			<table>
				<tr><td>Transaction ID</td><td><s:property value="%{responseArguments['TRANSACTIONID']}"/></td></tr>
				<tr><td>Date and Time</td><td><s:property value="%{responseArguments['ORDERTIME']}"/></td></tr>
				<tr><td>Amount</td><td><s:property value="%{responseArguments['AMT']}"/></td></tr>
				<tr><td>Status</td><td><s:property value="%{responseArguments['PAYMENTSTATUS']}"/></td></tr>
			</table>
		</div>
		
		<div class="message">
			${message}
		</div>
	</div>
	
	<div class="buttonbar">
		<input type="submit" name="cancelbutton" class="webbutton" value="Return to Home">
	</div>
	<jsp:include page="PageFooter.jsp" />
	</form>
</body>
</html>
