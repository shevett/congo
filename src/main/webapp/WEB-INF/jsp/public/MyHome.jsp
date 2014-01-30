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
	<title>My Home Page : ${currentConvention.conName}</title>
</head>
<body>
	<form method="post" action="webMyHomeForm.action">
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
  				<br>	
  				<div align="center">		 
  					<input type="submit" name="homechangebutton" class="webbutton" value="Contact Information..."><br/>
  					<input type="submit" name="homeinvoicesbutton" class="webbutton" value="Payment History..."><br/>
  					<input type="submit" name="homechangesettings" class="webbutton" value="Settings..."><br />
  					<input type="submit" name="homechangepass" class="webbutton" value="Change password..."><br />
					<input type="submit" name="homelinks" class="webbutton" value="Manage Links...">
  				</div> 
			</div>
 			<div class='boxbottom-grey'><div></div></div>
		</div>
	</div>
	
	<div style="float: right; width: 200px;">
		<div class='box-orange'>
			<div class='boxtop-orange'><div></div></div>
  			<div class='boxcontent-orange'>
	  				This is your Home page<hr>
	  				From here you can change your profile information, update your password, or purchase any additional
	  				services from the event, if any are available.
 			</div>
 			<div class='boxbottom-orange'><div></div></div>
 		</div>
	</div>
	
	
	<div style="padding: 1px 200px 5px 285px;">
		<div class='box-grey'>
	 		<div class='boxtop-grey'><div></div></div>
  			<div class='boxcontent-grey'>
				<c:choose>
					<c:when test="${webuser.currentstate.registered}">
						You are currently registered for "<b>${currentConvention.conName }</b>" as 
						registration type "<b>${webuser.currentstate.regtype}</b>"
					</c:when>
					<c:otherwise>
						You are currently not registered for this event.
					</c:otherwise>
				</c:choose>
			</div>
 			<div class='boxbottom-grey'><div></div></div>
 		</div>
 	</div>

	<div style="padding: 2px 200px 5px 285px;">
		<div class='box-grey'>
	 		<div class='boxtop-grey'><div></div></div>
  			<div class='boxcontent-grey'>
  				<c:choose>
  					<c:when test="${empty linksList}">
	  					You currently have no Links.  CONGO's Links system allows you to 
  						see your friends and family's status, and buy registrations for them.<br />
  					</c:when>
  					<c:otherwise>
  						<b>Your links...</b><br>
  						<table>
			  				<c:forEach items="${linksList}" var="rows">
			  					<tr>
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
			  			</table>
			  		</c:otherwise>
			  	</c:choose>
  				
				<input style="float: right;" type="submit" name="homeregisterbutton" class="webbutton" value="Register...">
  				<br />
				Click "Manage Links" to maintain your Links list.
  			</div>
 			<div class='boxbottom-grey'><div></div></div>
 		</div>
 	</div>
	
	<div class="buttonbar">
		<input type="submit" name="homelogoutbutton" value="Log out" class="webbutton">
	</div>
	<jsp:include page="PageFooter.jsp" />
	</form>
</body>
</html>