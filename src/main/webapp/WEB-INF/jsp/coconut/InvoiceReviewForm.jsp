<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Invoice Review...</title>
</head>
<body onload="switchblock('${session.settings.event_paymenttype.settingValue}')">
<div class="wrapper">
	<jsp:include page="PageHeader.jsp" />
	<form method="post" name="edit" action="invoiceProcess">
	
	<jsp:include page="RegistrantSubNav.jsp" />

	<div id="content_wrapper">
		<div id="primary_content">
		<c:if test="${ empty sessionScope.workinginvoice }">
			No invoice currently in progress.
		</c:if>
		<c:if test="${ not empty sessionScope.workinginvoice }">
			<c:if test="${ sessionScope.workinginvoice.status == 'READY' }">
			
			<div style="float: right; width: 350px;">
				<table cellspacing=3>
    				<tr><td><b>Notifications</b></td><td><b>Processing</b></td></tr>
    				<tr valign="top"><td>Send email to:<br>
   			 			<input type="radio" name="sendto" value="none" ${session.sendto == 'none' ? 'checked' : ''}>None<br>
   			 			<input type="radio" name="sendto" value="registrant" ${session.sendto == 'registrant' ? 'checked' : ''}>This registrant<br>
    					<input type="radio" name="sendto" value="everyone" ${session.sendto == 'everyone' ? 'checked' : ''}>Everyone on this invoice
    					</td>
    					<td>
							<input type="checkbox" name="checkin" value="true" ${session.checkin==true ? 'checked' : '' } accesskey="C">[C]heck in<br>
							<input type="checkbox" name="printbadge" value="true" ${session.printbadge==true ? 'checked' : '' } accesskey="G">Print bad[g]e<br>
						</td>
					</tr>
				</table>
       		</div>
       		</c:if>
       		
    		
       		<c:if test="${ sessionScope.workinginvoice.status == 'PAID' }">
	       		<div style="float: right; width: 350px;">
    	   			<table cellspacing=3>
       					<tr><td colspan=2><b>Payment Detail</b></td></tr>
       					<tr><td width=35%>Paid via</td><td>${workinginvoice.paytype}</td></tr>
       					<tr><td>Paid on</td><td>${workinginvoice.paydate}</td></tr>
       					<tr><td>Auth code</td><td>${workinginvoice.authcode}</td></tr>
       				</table>
    			</div>
       		</c:if>

    		<div style="width: 300px;">
			<table cellspacing=3>
				<tr><td colspan=3><b>Invoice Summary</b></td></tr>
				<tr><td width=80px>Invoice</td><td><b># ${workinginvoice.id}</td></tr>
				<tr><td>Amount</td><td><fmt:formatNumber value="${workinginvoice.amount}" type="currency"/></td></tr>
				<tr><td>Status</td><td>${workinginvoice.status }</td></tr>
				<tr><td>Created by</td><td>${workinginvoice.creator }</td><td>${workinginvoice.creatorName}</td></tr>
				<tr><td>Operator</td><td>${workinginvoice.operator }</td></tr>
			</table>
			</div>

       	</c:if>
				<table width="80%">
					<tr>
						<th></th>
						<th>#</th>
						<th>By</th>
						<th>For</th>
						<th>Date</th>
						<th>Transaction</th>
						<th>Type</th>
						<th>Comment</th>
						<th>Processed?</th>
						<th>Amount</th>
					</tr>
					<c:forEach var="rows" items="${ sessionScope.workinginvoice.detailList }">
						<tr>
							<td>
								<c:if test="${ sessionScope.workinginvoice.status == 'READY' }">
									<a href="invoiceDeleteItem?id=${rows.sequence}">Void</a>
								</c:if>
							</td>
							<td>${rows.sequence}</td>
							<td>${rows.operator}</td>
							<td>${rows.rid } ${rows.fullname}</td>
							<td>${rows.activity}</td>
							<td>${rows.type}</td>
							<td>${rows.type2}</td>
							<td>${rows.comment}${rows.description}</td>
							<td>
								<c:choose>
									<c:when test="${rows.postprocess}">
										<s:url var="url" action="coconut/invoiceMarkWritein">
											<s:param name="id">${rows.id}</s:param>
										</s:url>
										<s:a href="%{url}">Incomplete.  Click to mark as done</s:a>
									</c:when>
									<c:otherwise>
										Done
									</c:otherwise>
								</c:choose>
							</td>
							<td><fmt:formatNumber value="${rows.finalamount}" type="currency"/></td>
						</tr>
					</c:forEach>
					<tr>
						<td colspan=5>&nbsp;</td>
					</tr>
					<tr>
						<td colspan=5>&nbsp;</td>
						<td colspan=3>Total:</td>
						<td><fmt:formatNumber value="${workinginvoice.amount}" type="currency"/></td>
					</tr>	
				</table>

	<c:if test="${ sessionScope.workinginvoice.status == 'READY' }">
	<h3>Payment Method</h3>

		Select a payment method: 
 
		<input type="radio" name="paymenttype" value="cash" onclick="switchblock('cash')" ${session.settings.event_paymenttype.settingValue=='cash' ? 'checked' : '' }>Cash |
		<input type="radio" name="paymenttype" value="paypal" onclick="switchblock('paypal')" ${session.settings.event_paymenttype.settingValue=='paypal' ? 'checked' : '' }>Paypal |
		<input type="radio" name="paymenttype" value="check" onclick="switchblock('check')" ${session.settings.event_paymenttype.settingValue=='check' ? 'checked' : '' }>Check | 
		<input type="radio" name="paymenttype" value="amex" onclick="switchblock('amex')" ${session.settings.event_paymenttype.settingValue=='amex' ? 'checked' : '' }>Amex | 
		<input type="radio" name="paymenttype" value="visa" onclick="switchblock('visa')" ${session.settings.event_paymenttype.settingValue=='visa' ? 'checked' : '' }>Visa | 
		<input type="radio" name="paymenttype" value="discover" onclick="switchblock('discover')" ${session.settings.event_paymenttype.settingValue=='discover' ? 'checked' : '' }>Discover | 
		<input type="radio" name="paymenttype" value="mastercard" onclick="switchblock('mastercard')" ${session.settings.event_paymenttype.settingValue=='mastercard' ? 'checked' : '' }>Mastercard
		<hr>
		<div id="cash" style="display: block">
			Pay by cash
		</div>
		<div id="check" style="display: none">
			Pay by check
			<blockquote>
				<table width="60%">
					<tr>
						<td>Name on check: </td>
						<td><input name="checkname" size=40>
					</tr>
				</table>
			</blockquote>
		</div>
		<div id="amex" style="display: none">
			Pay by American Express (Don't leave home without it!)
			<blockquote>
				<table width="60%">
					<tr><td>Name on card: </td><td><input name="amexname" size=40></td></tr>
					<tr><td>Card number:</td><td><input name="amexnumber" size=40></td></tr>
					<tr><td>Expiration:</td><td><input name="amexpiration" size=40></td></tr>
				</table>
			</blockquote>
		</div>
		<div id="visa" style="display: none">
			Pay by VISA
			<blockquote>
				<table width="60%">
					<tr><td>Name on card: </td><td><input name="visaname" size=40></td></tr>
					<tr><td>Card number:</td><td><input name="visanumber" size=40></td></tr>
					<tr><td>Expiration:</td><td><input name="visaexpiration" size=40></td></tr>
				</table>
			</blockquote>
		</div>
		<div id="discover" style="display: none">
			Pay by Discover
			<blockquote>
				<table width="60%">
					<tr><td>Name on card: </td><td><input name="discovername" size=40></td></tr>
					<tr><td>Card number:</td><td><input name="discovernumber" size=40></td></tr>
					<tr><td>Expiration:</td><td><input name="discoverexpiration" size=40></td></tr>
				</table>
			</blockquote>
		</div>		
		<div id="mastercard" style="display: none">
			Pay by Mastercard
			<blockquote>
				<table width="60%">
					<tr><td>Name on card: </td><td><input name="mcname" size=40></td></tr>
					<tr><td>Card number:</td><td><input name="mcnumber" size=40></td></tr>
					<tr><td>Expiration:</td><td><input name="mcexpiration" size=40></td></tr>
				</table>
			</blockquote>
		</div>
		<div id="paypal" style="display: none">
			Pay by Paypal
			<blockquote>
				<table width="60%">
					<tr><td>Paypal account email address:</td><td><input name="paypalaccount" size="40"></td></tr>
				</table>
			</blockquote>
		</div>
		<strong>${message}</strong>
	</c:if>
	<br/>
	<c:if test="${ sessionScope.workinginvoice.status == 'READY' }">
		<button accesskey="P" name="proceedbutton" value="Process Invoice" class="button"><u>P</u>rocess Invoice</button> 
	</c:if>
	<button accesskey="V" name="voidbutton" value="void" class="button"><u>V</u>oid (cancel) this invoice</button>
	<button accesskey="R" name="registrantbutton" value="registrant" class="button">Go to <u>R</u>egistrant</button>
	<button accesskey="I" name="invoicebutton" value="invoice" class="button"><u>I</u>nvoice Browse</button>
	<button accesskey="E" name="reprocessbutton" value="reprocess" class="button">R<u>e</u>process this invoice</button>	 
	</div>
</div>
</form>
<script language="JavaScript">
<!--
	function switchblock(whatblock) {
		//  switch off the old views...
		document.getElementById('cash').style.display = whatblock == 'cash' ? '' : 'none';
		document.getElementById('check').style.display = whatblock == 'check' ? '' : 'none';
		document.getElementById('amex').style.display = whatblock == 'amex' ? '' : 'none';
		document.getElementById('visa').style.display = whatblock == 'visa' ? '' : 'none';
		document.getElementById('discover').style.display = whatblock == 'discover' ? '' : 'none';
		document.getElementById('mastercard').style.display = whatblock == 'mastercard' ? '' : 'none';
		document.getElementById('paypal').style.display = whatblock == 'paypal' ? '' : 'none';
 	}
-->
</script>
</body>
</html>