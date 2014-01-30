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
	<form method="post" action="webRegisterForm.action">
	
	<%@include file="PageHeader.jsp" %>
		
	<div class="inputform">
	
		<div class="title">
			Registering #${workingregistrant.rid} : ${workingregistrant.lastName}, ${workingregistrant.firstName} (${workingregistrant.badgeName})<hr>
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
		
		
		<c:if test="${! empty properties}">
	
			<div class="title">
				Options / User properties
			</div>
			
			<div class="sectionbody">
				<table>
				<tr bgcolor="#dddddd">
					<th>Option</th><th>Description</th><th>Cost</th>
				</tr>
				<c:forEach items="${properties}" var="rows">
					<tr>
					<td>${rows.description}</td>
					<td>
					<c:choose>
						<c:when test='${rows.type == "boolean"}'>
							<input type="checkbox" name="${rows.name}">
						</c:when>
						<c:when test='${rows.type == "string"}'>
							<input type="text" name="${rows.name}">
						</c:when>
						<c:when test='${rows.type == "numeric"}'>
							<input type="text" name="${rows.name}">
						</c:when>
						<c:when test='${rows.type == "picker"}'>
							<select name="${rows.name}">
							<c:forEach var="pickitem" items="${rows.format}">
								<c:set var="x" value="${fn:split(pickitem, ':')}" />
								
<%-- 								<c:set var="selected" value=""/> --%>
<%-- 								<c:set var="dbvalue" value="${rows.value}"/> --%>
<%-- 								<c:set var="indexvalue" value="${x[0]}"/> --%>
<%-- 								<c:if test='${dbvalue == indexvalue}'> --%>
<%-- 									<c:set var="selected" value="selected"/> --%>
<%-- 								</c:if> --%>
<%-- 								<option value="<c:out value="${x[0]}"/>" ${selected}><c:out value="${x[1]}"/> --%>
								
								<option value="<c:out value="${x[0]}"/>"><c:out value="${x[1]}"/>
							</c:forEach>
							</select>
						</c:when>
						<c:otherwise>
							<b>Unknown property type ${rows.type} - must be boolean or string.
						</c:otherwise>
					</c:choose>
					</td>	
					<td><fmt:formatNumber type="currency" value="${rows.cost}"/></td>
					</tr>
				</c:forEach>
				</table>
			</div>
			
		</c:if>
		
		<c:if test="${! empty events }">
	
			<div class="sectionbody"><b>Select Events you'd like to attend</b><br>
				<table>
				<tr><th>Select</th><th>Name</th><th>Description</th><th>Start time</th><th>End time</th><th>Location</th><th>Cost</th></tr>
				<tr>
				<c:forEach items="${events}" var="rows">
					<tr>
					<td><input type="checkbox" name="${rows.name}"></td>
					<td>${rows.name }</td>
					<td>${rows.description}</td>
					<td><fmt:formatDate type="both" dateStyle="long" timeStyle="short" value="${rows.start}"/></td>
					<td><fmt:formatDate type="both" dateStyle="long" timeStyle="short" value="${rows.end}"/></td>
					<td>${rows.location }</td>
					<td>${rows.price}</td>
					</tr>
				</c:forEach>
				</table>
			</div>
		</c:if>
		
		</td></tr></table>
		
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
