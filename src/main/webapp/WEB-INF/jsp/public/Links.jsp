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
	<form method="post" action="webLinksForm.action" name="changeform">
	
	<%@include file="PageHeader.jsp" %>
	
	<div class="inputform">
	
		<div class="title">
			Manage your Links<hr>
		</div>
		
		<div style="float: right; width: 200px;">
			<div class='box-orange'>
	 			<div class='boxtop-orange'><div></div></div>
  					<div class='boxcontent-orange'>
	  					Use this form to manage your links.  Linking to someone allows
	  					them to see your registration status, and vice-versa.  A link also
	  					allows registering someone other than yourself.
 					</div>
	 			<div class='boxbottom-orange'><div></div></div>
			</div>
		</div>
	
		<div class="title">
			Current links
		</div>
		
		<div class="sectionbody">
			<c:choose>
  				<c:when test="${empty linksList}">
	  				You currently have no Links.  CONGO's Links system allows you to 
  					see your friends and family's status, and buy registrations for them.<br />
  				</c:when>
  				<c:otherwise>
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
			  					<s:url id="url" action="public/webLinksRemove.action">
									<s:param name="id">${rows.id}</s:param>
								</s:url>
			  					<td><a href="${url}">Remove</a></td>
			  				</tr>
			  			</c:forEach>
			  		</table>
			  	</c:otherwise>
			</c:choose>		
		</div>
		
		<div class="title">
			Invite someone to be Linked...
		</div>
		
		<div class="sectionbody">
			To add a link, enter their email address below, and click 'Send Invite'.  Once they accept, you'll be able to see their name, badgename, and registration status.
			<table class="inputform">
				<tr>
					<td>
						<s:textfield name="email" value="%{#session.webuser.getPrimaryEmail()}" size="40" />
					</td>
					<td>
						<input type="submit" name="emailbutton" class="webbutton" value="Send invite">
					</td>
				</tr>
				<tr>
					<td colspan="2"><s:checkbox name="dontsendmail" />Check here if you don't want CONGO to send this person email about this Link request</td>
				</tr>
			</table>
		</div>
		
		<div class="title">
			Pending link requests...
		</div>
		
		<div class="sectionbody">
			<c:choose>
  				<c:when test="${empty linksPending}">
  					You have no pending link requests.
  				</c:when>
  				<c:otherwise>
  					<table>
			  			<c:forEach items="${linksPending}" var="rows">
			  				<tr>
			  					<c:choose>
			  						<c:when test="${rows.linkRegistered}">
			  							<td>${rows.linkName} (Registered)</td>
			  						</c:when>
			  						<c:otherwise>
			  							<td>${rows.linkName} (Not registered)</td>
		  							</c:otherwise>
		  						</c:choose>
		  						
		  						<td>
	  								<s:url id="url" action="public/webLinksRemove.action">
										<s:param name="id">${rows.id}</s:param>
									</s:url>			  							
			  						<c:choose>
			  							<c:when test="${rows.linkRid1 == webuser.rid}">
			  								Waiting to be accepted
					  						<a href="${url}">Cancel</a>
										</c:when>
			  							<c:otherwise>
			  								Wants to be friends with you
											<s:url id="accepturl" action="public/webLinksAccept.action">
												<s:param name="id">${rows.id}</s:param>
											</s:url>
					  						<a href="${accepturl}">Accept</a>
					  						<a href="${url}">Reject</a>
			  							</c:otherwise>
			  						</c:choose>
		  						</td>
			  				</tr>
			  			</c:forEach>
			  		</table>
			  	</c:otherwise>
			</c:choose>		
		</div>
		
		<div class="message">${message}</div>
		
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
