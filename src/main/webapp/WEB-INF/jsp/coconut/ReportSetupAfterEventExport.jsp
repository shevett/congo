<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Coconut: After Event Export</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>
	<form method="post" action="ReportAfterEventExport.action" name="createform">

	<div id="content_wrapper">
		<div id="primary_content">
			<h5>Reports - After Event Export</h5>
			<table>
				<tr><td>Event ID</td><td><s:textfield name="eventnumber"/></td></tr>
				<tr><td>Only registered attendees?</td><td><s:checkbox name="onlyregistered"/></td></tr>
			</table>
			<input type="submit" name="runbutton" class="button" accesskey="R" value="[R]un report">
			<input type="submit" name="cancelbutton" class="button" accesskey="X" value="E[x]it to Reports menu">
		</div>
		
		<jsp:include page="PageFooter.jsp" />
		
	</div>
</div>

<script language="JavaScript">
<!--

document.searchform.search.focus();

//-->
</script>
</body>
</html>