<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Delete Template</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>
	<s:form name="delete" action="coconut/deleteTemplate">
	<input type="hidden" name="workingTemplate" value="<s:property value="workingTemplate.name"/>"/>

	<div id="content_wrapper">
		<div id="primary_content">
			Delete template '<s:property value="workingTemplate.name" />'?
			<br>
			<button type="submit" accesskey="D" name="typebutton" class="button"><u>D</u>elete</button>
		</div>
		<jsp:include page="PageFooter.jsp"></jsp:include>
	</div>
</div>
</s:form>

</body>
</html>