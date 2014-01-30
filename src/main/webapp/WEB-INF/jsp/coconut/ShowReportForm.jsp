<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Show Report</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>
		
	<div id="content_wrapper">
		<div id="floating_content" style="width: 100%">
			<b>${myReport.title}</b>
			<div style="float: right">
				<form method="post" action="${myReport.actionClass}">
				Select format: 
				<input type="radio" name="format" value="HTML">HTML  
				<input type="radio" name="format" value="CSV">CSV 
				</select>
				<input type="submit" value="Refresh">
				</form>
			</div>
		</div>
		<div id="primary_content_wide">
			<table width="100%">
			<tr>
				<c:choose>
					<c:when test="${format == 'CSV'}">
						<th>
							<c:forEach items="${myReport.titles}" var="t">
								"${t}",
							</c:forEach>
						</th>
					</c:when>
					<c:otherwise>
						<c:forEach items="${myReport.titles}" var="t">
							<th>${t}</th>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tr>
	
			<c:forEach items="${myReport.rows}" var="r">		
				<c:set var="cellColor">
					<c:choose>
						<c:when test="${cellColor == '#f0f0f0'}">#ffffcc</c:when>
						<c:otherwise>#f0f0f0</c:otherwise>
					</c:choose>
				</c:set>
			
				<tr bgcolor="${cellColor}">
					<c:choose>
					<c:when test="${format == 'CSV'}">
						<td>
							<c:forEach items="${r}" var="i">
								"${i}",
							</c:forEach>
						</td>
					</c:when>
					<c:otherwise>
						<c:forEach items="${r}" var="i">
							<td><c:out value="${i}"/></td>
						</c:forEach>
					</c:otherwise>
					</c:choose>
				</tr>
			</c:forEach>
	
			</table>
			
			<hr>
			
			<table width="50%">
			<c:forEach items="${myReport.summary}" var="s">
				<tr>
					<c:choose>
						<c:when test="${format == 'CSV'}">
							<td>
								<c:forEach items="${s}" var="t">
									"${t}",
								</c:forEach>
							</td>
						</c:when>
						<c:otherwise>
							<c:forEach items="${s}" var="t">
								<td>${t}</td>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</tr>
			</c:forEach>
			</table>
			
			<c:forEach items="${myReport.charts}" var="s">
				<hr>
				<img src="${s}" class="displayed">
			</c:forEach>
		</div> <!-- end primary_content -->
		<jsp:include page="PageFooter.jsp"></jsp:include>	
	</div> <!-- end content_wrapper -->
</div> <!-- end wrapper -->
<script language="JavaScript">
<!--

/* Find the current menu pick, and make sure its class is right... */
document.getElementById("reportMenuPick").setAttribute("class","active");
document.getElementById("reportMenuText").setAttribute("class","active");

//-->
</script>

</body>