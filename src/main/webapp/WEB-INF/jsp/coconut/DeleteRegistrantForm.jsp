<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Delete Registrant</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp" />
	<jsp:include page="RegistrantSubNav.jsp" />
	<s:form name="delete" action="coconut/DeleteRegistrant.action">
	
	<div id="content_wrapper">
		<jsp:include page="RegistrantHeader.jsp" />

		<div id="primary_content">
			Deleting a registrant may have the following repercussions:
			<ul>
			<li>Attendance counts for events may change
			<li>Transactional history such as Invoices may be removed
			<li>History information associated with this registrant will be removed
			<li>Any pending notes, postings, or other communications will be removed
			<li>The registrant will disappear from <b>ALL</b> events, not just the current one.
			</ul>
			In other words, be absolutely sure this is what you want to do. <br />
	
			<input type="hidden" name="whatRID" value="${workingregistrant.rid}">
			<input type="submit" name="deletebutton" value="Delete this registrant" accesskey="D" class="button">
		</div>	
		<jsp:include page="PageFooter.jsp" />
	</div>
</div>
</s:form>
<script language="JavaScript">
<!--
/* Find the current menu pick, and make sure its class is right... */
document.getElementById("registrantMenuPick").setAttribute("class","active");
document.getElementById("registrantMenuText").setAttribute("class","active");	
document.getElementById("deleteSubText").setAttribute("class","active");	
-->
</script>
</body>
</html>