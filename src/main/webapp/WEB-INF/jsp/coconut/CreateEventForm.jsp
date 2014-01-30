<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="<s:url value="coconut.css"/>" rel="stylesheet"
	type="text/css" />
<title>Create Event</title>
</head>
<body>
<jsp:include page="PageHeader.jsp"></jsp:include>
<s:form action="coconut/createEvent">

	<div class="bodybox">

	<div class="menuboxes menuheader">Create a new Event</div>

	<div class="menuboxes">
	<%@include file="EventFormFields.jspf" %>
	</div>
	<div class="menuboxes">
	<button type="submit" accesskey="C" value="Create"><img
		src="graphics/ok.png" align="top">[C]reate Event</button>
	<button type="submit" accesskey="X" value="Exit"
		name="navigationButton"><img src="graphics/start.png"
		align="top">E[x]it to Event Maintenance</button>
	</div>
	</div>

</s:form>
</body>
</html>