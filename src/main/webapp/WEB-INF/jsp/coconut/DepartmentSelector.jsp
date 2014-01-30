<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Edit / Create Departments</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>
	<jsp:include page="SchedulingSubNav.jsp" />
	
	<s:form name="create" action="coconut/createDepartment">
		
	<div id="content_wrapper">
	
		<div id="floating_content">
			Departments
		</div>
	
		<div id="primary_content_wide">
			<table>
				<tr>
					<th>Func</th>
					<th>CID</th>
					<th>Name</th>
					<th>Description</th>
					<th>Owner</th>
				</tr>
				<c:forEach var="rows" items="${departments}">
					<c:set var="cellColor">
			 			<c:choose>
							<c:when test="${cellColor == '#f0f0f0'}">#ffffcc</c:when>
			 				<c:otherwise>#f0f0f0</c:otherwise>
				 		</c:choose>
					</c:set>
					<tr bgcolor="${cellColor}">			
						<td>
							<s:url id="editurl" action="coconut/editDepartment" >
								<s:param name="function">edit</s:param>
								<s:param name="id">${rows.id}</s:param>
							</s:url>
							<s:url id="deleteurl" action="coconut/deleteDepartment" >
								<s:param name="id">${rows.id}</s:param>
							</s:url>
							<s:a href="%{editurl}"><img src="graphics/edit.png" border=0></s:a>
							<s:a href="%{deleteurl}"><img src="graphics/eventdelete.png" border=0></s:a>
						</td>
						
						<td>${rows.cid}</td>
						<td>${rows.departmentName}</td>
						<td>${rows.departmentDescription}</td>
						<td>${rows.departmentOwner}</td>
					</tr>
				</c:forEach>
			</table>
			<input type="submit" accesskey="C" value="Create" name="typebutton" class="button">
		</div>
		<jsp:include page="PageFooter.jsp"></jsp:include>
	</div>
</div>
</s:form>
<script language="JavaScript">
<!--

/* Find the current menu pick, and make sure its class is right... */
document.getElementById("schedulingMenuPick").setAttribute("class","active");
document.getElementById("schedulingMenuText").setAttribute("class","active");
document.getElementById("departmentSubPickText").setAttribute("class","active");	

//-->
</script>

</body>
</html>