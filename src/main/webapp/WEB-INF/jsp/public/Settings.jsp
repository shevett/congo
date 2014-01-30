<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="StyleSheet" href="getConfiguredStylesheet.action" type="text/css">
	<title>${currentConvention.conName}</title>
</head>
<body>
	<form method="post" action="webSettingsForm.action" name="settingsform">
	
	<%@include file="PageHeader.jsp" %>
	
	<div class="inputform">
	
		<div class="title">
			Update your Settings<hr>
		</div>
		
		<div style="float: right; width: 200px;">
			<div class='box-orange'>
	 			<div class='boxtop-orange'><div></div></div>
  					<div class='boxcontent-orange'>
	  					Use this form to update your Settings.
 					</div>
	 			<div class='boxbottom-orange'><div></div></div>
			</div>
		</div>
		
		<div class="sectionbody">
			<table>
				<tr bgcolor="#dddddd">
					<th>Option</th><th>Description</th><th>Cost (if applicable)</th>
				</tr>
				<c:forEach items="${plist}" var="rows">
					<tr>
					<td>${rows.description}</td>
					<td>
					<c:choose>
						<c:when test='${rows.type == "boolean"}'>
							<%-- This is a little odd, but sets up whether a checkbox is checked... --%>
							<c:set var="checked" value=""/>
							<c:if test='${rows.value == "1"}'>
								<c:set var="checked" value="checked" />
							</c:if>
							<input type="checkbox" name="${rows.name}" value="${rows.value}" ${checked}>
						</c:when>
						<c:when test='${rows.type == "string"}'>
							<input type="text" name="${rows.name}" value="${rows.value}">
						</c:when>
						<c:when test='${rows.type == "numeric"}'>
							<input type="text" name="${rows.name}" value="${rows.value}">
						</c:when>
						<c:when test='${rows.type == "picker"}'>
							<select name="${rows.name}">
							<c:forEach var="pickitem" items="${rows.format}">
								<c:set var="x" value="${fn:split(pickitem, ':')}" />
								<c:set var="selected" value=""/>
								<c:set var="dbvalue" value="${rows.value}"/>
								<c:set var="indexvalue" value="${x[0]}"/>
								<c:if test='${dbvalue == indexvalue}'>
									<c:set var="selected" value="selected"/>
								</c:if>
								<option value="<c:out value="${x[0]}"/>" ${selected}><c:out value="${x[1]}"/>
							</c:forEach>
							</select>
						</c:when>
						<c:otherwise>
							<b>Unknown property type ${rows.type} - must be boolean or string.
						</c:otherwise>
					</c:choose>
					</td>	
					<td>
						<c:if test='${rows.cost} > 0'>
							<fmt:formatNumber type="currency" value="${rows.cost}"/>
						</c:if>
					</td>
					</tr>
				</c:forEach>
			</table>
		</div>
		
		<div class="message">${message}</div>
		
	</div>

		<div class="buttonbar">
			<input type="submit" name="savebutton" class="webbutton" value="Save changes">
			<input type="submit" name="cancelbutton" class="webbutton" value="Cancel">
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
