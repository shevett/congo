<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Search for a Registrant</title>
</head>
<body>

<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>

	<s:form name="lookup" action="coconut/LookupRegistrant">
	<div id="content_wrapper">
		<div id="primary_content">
			<h5>Look up a Registrant</h5>
			
				<s:textfield name="search"/>
				<label>Include disabled?</label>
				<s:checkbox name="includedisabled"/>
				
				&nbsp;
				<label>Registered only?</label>
				<s:checkbox name="registeredonly"/>
				
				&nbsp;
				
  				<button type="submit" value="Search" name="searchbutton" class="button">
					Search
				</button>
				
				&nbsp;
			 
			<hr>
  			
			<label>
				<c:if test="! ${totalrows < 1}">
					Total rows : ${totalrows} 
					<c:if test="${totalrows > 200}">
						(showing only first 200 results)
					</c:if>
				</c:if>
			</label>
		<table>
			<tr>
				<th>Key</th>
				<th>RegID</th>
				<th>Lastname</th>
				<th>Firstname</th>
				<th>Badgename</th>
				<th>Company</th>
				<th>Regtype</th>
				<th>Badged?</th>
				<th>Info</th>
			</tr>
			<c:set var="counter" value="1" />
			<c:forEach items="${searchResults}" var="rows">
				<tr>
					<td>${counter}</td>
					<td>
						<s:url id="url" action="coconut/gotoUser" >
							<s:param name="rid">${rows.rid}</s:param>
						</s:url>
						<a href="${url}" accesskey="${counter}">${rows.rid}</a>
					</td>
					<td>${rows.lastName}</td>
					<td>${rows.firstName}</td>
					<td>${rows.badgeName}</td>
					<td>${rows.company}</td>
					<td>${rows.currentstate.regtype}</td>
					<td>
						<c:if test='${rows.currentstate.badged == true}'>
							Yes
						</c:if>
					</td>
					<c:remove var="disabled"/>
					<c:if test='${! rows.enabled}'>
						<c:set var="disabled" value="Registrant Disabled (${rows.comment})" />
					</c:if>
					<td>${disabled}</td>
					<c:set var="counter" value="${counter + 1}" />
				</tr>
			</c:forEach>
		</table>
		</div> <!-- end primary_content-->
		<jsp:include page="PageFooter.jsp"/>	
	</div><!-- end cnotent_wrapper -->
	</s:form>
</div>
<script language="JavaScript">
<!--

document.lookup.search.focus();

/* Find the current menu pick, and make sure its class is right... */
document.getElementById("lookupMenuPick").setAttribute("class","active");
document.getElementById("lookupMenuText").setAttribute("class","active");

//-->
</script>


</body>
</html>