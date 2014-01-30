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
	<title>Select : ${currentConvention.conName}</title>
</head>
<body>
	<form method="post" action="webRegisterSelected.action">
	<%@include file="PageHeader.jsp" %>
	<div class="inputform">

		<div class="title">
			Select Person to Register<hr>
		</div>
		
		<div style="float: right; width: 200px;">
			<div class='box-orange'>
				<div class='boxtop-orange'><div></div></div>
  				<div class='boxcontent-orange'>
	  				Select<hr>
	  				You can register yourself or anyone who hasn't registered from your Links list.
 				</div>
 				<div class='boxbottom-orange'><div></div></div>
 			</div>
 		</div>

	
		<div class="sectionbody">


  						<table>
  							<tr>
  								<c:choose>
			  						<c:when test="${webuser.currentstate.registered}">
			  							<c:set var="regtext" value="(Registered)" />
			  							<td></td>
			  						</c:when>
			  						<c:otherwise>
			  							<c:set var="regtext" value="(Not registered)" />
										<td><input type="radio" name="selectedRegistrant" value="${webuser.rid}"></td>
									</c:otherwise>
								</c:choose>
  							
  								<td>${webuser.rid}</td>
  								<td>${webuser.lastName}, ${webuser.firstName} (${webuser.badgeName})</td>
  								<td><c:out value="${regtext}"/></td>

			  				</tr>
  						
			  				<c:forEach items="${linksList}" var="rows">
			  					<tr>
			  						<td><input type="radio" name="selectedRegistrant" value="${rows.linkOtherRid}"></td>
			  						<td>${rows.linkOtherRid}</td>
			  						<td>${rows.linkName}</td>
			  						<c:choose>
			  							<c:when test="${rows.linkRegistered}">
			  								<td>(Registered)</td>
			  							</c:when>
			  							<c:otherwise>
			  								<td>(Not registered)</td>
			  							</c:otherwise>
			  						</c:choose>
			  					</tr>
			  				</c:forEach>
			  				
			  				  <tr>
  								<td><input type="radio" name="selectedRegistrant" value="-1"></td>
  								<td>Write-in</td>
  								<td>
  									<table><tr>
  										<td>Last name<br><input type="text" name="writeinlastname"></td>
  										<td>First name<br><input type="text" name="writeinfirstname"></td>
  										<td>Badge name<br><input type="text" name="writeinbadgename"></td>
  										<td>Email<br><input type="text" name="writeinemail"></td>
  									</table>
  								</td>
			  				</tr>
			  				
			  			</table>
			  			
			  				
  				<c:choose>
  					<c:when test="${empty linksList}"><br />
	  					(You currently have no Links.  CONGO's <a href="Links">Links system</a> allows you to 
  						see your friends and family's status, and buy registrations for them.)
  					</c:when>
  				</c:choose>
  				
		</div>
		
		<div class="message">${message}</div>
		
 	</div>
	
	<div class="buttonbar">
		<input  type="submit" name="homeregisterbutton" class="webbutton" value="Register selected">
		<input type="submit" name="registerexitbutton" value="Cancel" class="webbutton">
	</div>
	<jsp:include page="PageFooter.jsp" />
	</form>
</body>
</html>