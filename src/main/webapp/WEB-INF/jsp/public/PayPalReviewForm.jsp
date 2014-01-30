<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<LINK REL="StyleSheet" href="web.css" type="text/css">
	<LINK REL="StyleSheet" href="getConfiguredStylesheet.action" type="text/css">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Pay Invoice : ${currentConvention.conName}</title>
</head>
<body>
	
	<%@include file="PageHeader.jsp" %>

	<form method="post" action="webPayPalComplete.action">
	
	<div class="inputform">

		<div class="title">
			Review Invoice Before Paying
		</div>
		
		<br>
	
		<div class="sectionbody">
			<table>
				<tr><td>Invoice #</td><td>${currentinvoice.id}</td></tr>
				<tr><td>Amount</td><td><fmt:formatNumber value="${currentinvoice.amount}" type="currency"/></td></tr>
			</table>
		</div>
		
		<br>
		<div class="title">
			Paypal Payer Information
		</div>
		<div class="sectionbody">
			<table>
			<tr><td>Email</td><td><s:property value="%{responseArguments['EMAIL']}"/></td></tr>
			<tr><td>Payer Status</td><td><s:property value="%{responseArguments['PAYERSTATUS']}"/></td></tr>
			<tr><td>First name</td><td><s:property value="%{responseArguments['FIRSTNAME']}"/></td></tr>
			<tr><td>Last Name</td><td><s:property value="%{responseArguments['LASTNAME']}"/></td></tr>
			<tr><td>Country Code</td><td><s:property value="%{responseArguments['COUNTRYCODE']}"/></td></tr>
			<tr><td>Business name</td><td><s:property value="%{responseArguments['BUSINESS']}"/></td></tr>
			<tr><td>Ship to</td><td><s:property value="%{responseArguments['SHIPTONAME']}"/></td></tr>
			<tr><td>Street</td><td><s:property value="%{responseArguments['SHIPTOSTREET']}"/></td></tr>
			<tr><td>City</td><td><s:property value="%{responseArguments['SHIPTOCITY']}"/></td></tr>
			<tr><td>State</td><td><s:property value="%{responseArguments['SHIPTOSTATE']}"/></td></tr>
			<tr><td>Country</td><td><s:property value="%{responseArguments['SHIPTOCOUNTRYCODE']}"/></td></tr>
			<tr><td>Country Name</td><td><s:property value="%{responseArguments['SHIPTOCOUNTRYNAME']}"/></td></tr>
			<tr><td>Zipcode</td><td><s:property value="%{responseArguments['SHIPTOZIP']}"/></td></tr>
			<tr><td>Address Status</td><td><s:property value="%{responseArguments['ADDRESSSTATUS=Confirmed']}"/></td></tr>
			</table>
		</div>
		
		<input name="token" type="hidden" value="${token}">
		<input name="payerID" type="hidden" value="${payerID}">
		
		<div class="message">
			${message}
		</div>
	</div>
	
	<div class="buttonbar">
		<input type="submit" name="cancelbutton" class="webbutton" value="Cancel">
		<input type="submit" name="finishbutton" class="webbutton" value="Pay and Complete Registration">
	</div>
	<jsp:include page="PageFooter.jsp" />
	</form>
</body>
</html>
