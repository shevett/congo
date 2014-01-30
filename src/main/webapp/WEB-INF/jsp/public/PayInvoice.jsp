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

	<form method="post" action="webPayInvoiceForm.action">
	
	<div class="inputform">
	<div style="float: left; width: 300px;">
		<div class='box-grey'>
	 		<div class='boxtop-grey'><div></div></div>
  				<div class='boxcontent-grey'>
  					Badge ID # ${webuser.rid}
  					<hr>
  					${webuser.firstName } ${webuser.lastName }<br>
  					"${webuser.badgeName}"<br>
  					${webuser.company }<br>
 				</div>
 			<div class='boxbottom-grey'><div></div></div>
		</div>
	</div>
	<div class="title">
		Pay Invoice
	</div>

	<div class="sectionbody">
		<table class="sectiontable">
			<tr>
				<td>Invoice #</td>
				<td>:</td>
				<td>${currentinvoice.id}</td>
			</tr>
		</table>
	</div>
	
	<br>

	<div class="sectionbody">
		<table cellpadding="5">
			<tr bgcolor="#dddddd">
				<th>Registrant</th><th>Item Type</th><th>Detail</th><th>Description</th><th>Cost</th><th>Discount</th><th>Final Amount</th>
			</tr>
			<c:forEach var="rows" items="${currentinvoice.detailList}">
				<tr>
					<td>${rows.rid} ${rows.fullname}</td>
					<td>${rows.type}</td>
					<td>${rows.type2}</td>
					<td>${rows.description}</td>
					<td><fmt:formatNumber type="currency" value="${rows.amount}"/></td>
					<td align="right"><fmt:formatNumber type="currency" value="${rows.discount}"/></td>
					<td align="right"><fmt:formatNumber type="currency" value="${rows.finalamount}"/></td>
				</tr>
			</c:forEach>
			<tr><td colspan=4></td><td colspan=2><hr></td></tr>
			<tr><td colspan=4></td><td>Total:</td>
			<td align="right">
				<fmt:formatNumber type="currency" value="${currentinvoice.amount}"/>
			</td>
			</tr>
			
			<tr><td colspan=6>
				<div class="message">
					${message}
				</div>
			</td></tr>
			
			<tr><td colspan="6" align="right">
				<c:set var="payLabel" value="Submit Payment" />
				<c:choose>
					<c:when test="${sessionScope.currentinvoice.amount < 0.01}">
						No payment necessary, proceed.
						<c:set var="payLabel" value="Proceed"/>
					</c:when>
					<c:otherwise>
						<c:if test="${paypalenabled}">
							<a href="webPayPalForm.action"><img src="https://www.paypal.com/en_US/i/btn/btn_xpressCheckout.gif" align="left" style="margin-right:7px;" border="0"></a>
						</c:if>
						<c:if test="${authorizenetenabled}">
							[ ] Authorize.net
						</c:if>
					</c:otherwise>
				</c:choose>
			</td></tr>
		</table>
	</div>
	
	<div class="buttonbar">
		<input type="submit" name="addlinksbutton" class="webbutton" value="Add more...">
		<c:if test="${sessionScope.currentinvoice.amount < 0.01}">
			<input type="submit" name="processnopaybutton" class="webbutton" value="Process Invoice">
		</c:if>
		<input type="submit" name="paymentbailbutton" class="webbutton" value="Cancel">
		
	</div>
	<jsp:include page="PageFooter.jsp" />
	</form>
</body>
</html>
