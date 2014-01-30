<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Coconut: Message Reporting</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp" />
	
	<jsp:include page="RegistrantSubNav.jsp" />

	<div id="content_wrapper">
	
		<div id="primary_content">
			<p>
			An system message or error was generated during the last action.  This indicates the last action
			did not complete successfully:<br />
			<c:if test="${empty message}">
				<b>Permission Denied.</b>
			</c:if>
			<b>${message}</b>
			</p>
		</div>
	</div>
</div>
</body>
</html>