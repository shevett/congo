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
	<title>${currentConvention.conName} - Register Friend...</title>
</head>
<body>
	<form method="post" action="webLinksForm.action" name="registerform">
	
	<%@include file="PageHeader.jsp" %>
	
	<div class="inputform">
	
		<div class="title">
			Register your Links<hr>
		</div>

		<div class="sectionbody">
			<c:choose>
  				<c:when test="${empty linksList}">
	  				You currently have no links.
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
			  							<td>&nbsp;</td>
			  						</c:when>
			  						<c:otherwise>
			  							<td>(Not registered)</td>
			  							<s:url id="url" action="public/webLinksRegister.action">
											<s:param name="id">${rows.id}</s:param>
										</s:url>
			  							<td><a href="${url}">Register...</a></td>
		  							</c:otherwise>
		  						</c:choose>
			  				</tr>
			  			</c:forEach>
			  		</table>
			  	</c:otherwise>
			</c:choose>		
		</div>
				
		<div class="message">${message}</div>
		
	</div>
	
	<div class="buttonbar">
		<input type="submit" name="donebutton" class="webbutton" value="Done Adding Friends">
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
