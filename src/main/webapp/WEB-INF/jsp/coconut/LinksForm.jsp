<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Links</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp" />
	
	<jsp:include page="RegistrantSubNav.jsp" />

	<div id="content_wrapper">
	
		<jsp:include page="RegistrantHeader.jsp" />

		<div id="primary_content">
			<s:form name="links" action="coconut/showLinks">
			Find a registrant to link...<br/>
	  		<s:textfield name="linksearch" size="80"/>
	  		<select name="resultlimit">
	  			<option value="20">Limit to 20 results</option>
	  			<option value="100">Limit to 100 results</option>
	  			<option value="500">Limit to 500 results</option>
	  		</select>
	  		
  			<input type="submit" name="linkbutton" value="Search" class="button">
  			<table>
				<c:forEach items="${searchResults}" var="rows">
					<tr>
						<td>
							<s:url id="url" action="coconut/gotoUser" >
								<s:param name="rid">${rows.rid}</s:param>
							</s:url>
							<a href="${url}">${rows.rid}</a>
						</td>
						<td>${rows.lastName},${rows.firstName} (${rows.badgeName})</td>
						<td>
							<s:url id="url" action="coconut/addLink">
								<s:param name="id">${rows.rid}</s:param>
								<s:param name="action">add</s:param>
							</s:url>
			  				<a href="${url}">Add link</a>
			  			</td>
					</tr>
				</c:forEach>
			</table>
			
			<hr>
			<table>
				<tr>
					<th>Link RID</th>
					<th>Name (Badgename)</th>
					<th>Link Status</th>
					<th>Registration Status</th>
					<th>Functions</th>
				</tr>
				<c:forEach items="${linkList}" var="rows">
					<tr>
						<c:choose>
							<c:when test="${rows.linkRid1 == workingregistrant.rid}">
								<c:set var="target" value="${rows.linkRid2}" />
							</c:when>
							<c:otherwise>
								<c:set var="target" value="${rows.linkRid1}" />
							</c:otherwise>
						</c:choose>
						<td>
							<s:url id="url" action="coconut/gotoUser" >
								<s:param name="rid">${target}</s:param>
							</s:url>
							<a href="${url}">${target}</a>
						</td>
						<td>${rows.linkName}</td>
						<td>${rows.linkStatus}</td>
						<c:choose>
			  				<c:when test="${rows.linkRegistered}">
			  					<td>(Registered)</td>
			  				</c:when>
			  				<c:otherwise>
			  					<td>(Not registered)</td>
			  				</c:otherwise>
			  			</c:choose>
						<s:url id="unlinkurl" action="coconut/unLink">
							<s:param name="id">${rows.id}</s:param>
						</s:url>
						<td>[<a href="${unlinkurl}">X</a>]</td>
					</tr>
				</c:forEach>
			</table>
			</s:form>
		</div>
		<jsp:include page="RegistrantSidebar.jsp" />
	
		<jsp:include page="PageFooter.jsp" />
		
	</div>
</div>
<script language="JavaScript">
<!--
document.getElementById("registrantMenuPick").setAttribute("class","active");
document.getElementById("registrantMenuText").setAttribute("class","active");	
document.getElementById("linksSide").setAttribute("class","active");

document.links.linksearch.focus();


//-->
</script>
</body>
</html>