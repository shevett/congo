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
	<title>Register for Convention : ${currentConvention.conName}</title>
</head>
<body>
	<form method="post" action="webRegisterWriteinForm.action">
	<input type="hidden" name="writeinlastname" value="${writeinlastname}">
	<input type="hidden" name="writeinfirstname" value="${writeinfirstname}">
	<input type="hidden" name="writeinbadgename" value="${writeinbadgename}">
	<input type="hidden" name="writeinemail" value="${writeinemail}">
	
	<%@include file="PageHeader.jsp" %>
		
	<div class="inputform">
	
		<div class="title">
			Registering Writein ${writeinlastname}, ${writeinfirstname} (${writeinbadgename})<hr>
		</div>
		
		<%-- Discount code prompt here... --%>
		<div style="float: right;">
			<div class="title">Discount Code</div>
			<div class="sectionbody">
				If you have a Discount Code, enter it here: <input type="text" name="discountcode">
			</div>
		</div>

		<%-- Registration type... --%>
		<div class="sectionbody">
			<div class="title">
				Select registration type
			</div>

			<c:if test="${empty regTypes}" >
				Sorry, there are no registration options available at this time.
			</c:if>

			<c:forEach items="${regTypes}" var="rows">
				<c:if test='${rows.value.regActive}'>
					<input type="radio" name="regselected" value="${rows.value.regName}">
					${rows.value.regDesc } - ${rows.value.regCost } 
					<c:if test="${! empty rows.value.regDiscountCode}">
						(Discount code required!)
					</c:if>
					<br>
				</c:if>
			</c:forEach>

			<div class="warning">${message}</div><br>
		</div>
	</div>
		
	<div class="buttonbar">
		<c:if test="${! empty regTypes}" >
			<input type="submit" name="registerproceedbutton" class="webbutton" value="Proceed">
		</c:if>
		<input type="submit" name="registerexitbutton" class="webbutton" value="Cancel">
	</div>
	<jsp:include page="PageFooter.jsp" />
	</form>
</body>
</html>
