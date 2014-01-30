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
	<title>Add more Registrants to invoice...</title>
</head>
<body>
	<form method="post" action="webAddRegistrants.action">
	
	<%@include file="PageHeader.jsp" %>
	
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
	
	
	<div class="formbody">
		<table cellpadding=0 cellspacing=0 border=0>
		<tr><td>
		<div class="title">
			<b>Add Write-In Registrations</b>
		</div>

		<div class="sectionbody">
			This form allows you to add 'write-in' registrations to your invoice.  If you are registering friends
			or family members, use this form to add their names to your shopping cart.  Due to privacy concerns, CONGO
			does not allow 'looking up' of other registrants.  Your 'write-in' registrations will be processed
			by a staff member manually.
		</div>

		<div class="sectionbody"><b>Select your registration type : </b><br>

			<c:if test="${empty regTypes}" >
				Sorry, there are no registration options available at this time.
			</c:if>

			<c:forEach items="${regTypes}" var="rows">
					<c:if test='${rows.value.regActive}'>
						<input type="radio" name="regselected" value="${rows.value.regName}">
						${rows.value.regDesc } - ${rows.value.regCost } <br>
					</c:if>
			</c:forEach>
		</div>
		
		<div class="inputform">
			<table class="inputform">
				<tr><td>First name</td><td><s:textfield name="firstname"/><span class="required"> *</span></td></tr>
				<tr><td>Last name</td><td><s:textfield name="lastname"/><span class="required"> *</span></td></tr>
				<tr><td>Badge name</td><td><s:textfield name="badgename"/><span class="required"> *</span></td></tr>
				<tr><td>Email</td><td><s:textfield name="email"/><span class="required"> *</span></td></tr>
				<tr><td>Phone Number</td><td><s:textfield name="phonenumber"/></td></tr>
				<tr>
					<td>New registrant?</td>
					<td><s:checkbox name="newregistrant"/> Check this box if you're pretty sure this person is not already in the database.</td>
					</td>
				</tr>
			</table>
		</div>
		
		<c:if test="${! empty properties}">
	
			<div class="title">
				<b>Options / User properties</b>
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
		<input type="submit" name="registercancelbutton" class="webbutton" value="Cancel">
	</div>
	<jsp:include page="PageFooter.jsp" />
	</form>
</body>
</html>
