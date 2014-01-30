<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="coconut.css"/>" rel="stylesheet" type="text/css"/>
	<title>Edit Template</title>
</head>
<body>
<jsp:include page="PageHeader.jsp"></jsp:include>
<s:form name="edit" action="coconut/updateEditTemplate">
<div class="bodybox">
	<div class="menuboxes menuheader">
		Edit Template
	</div>
	
	<div class="menuboxes topbox">
		<c:if test="${ empty workingTemplate.name }">
			Creating new Template
		</c:if>
		<c:if test="${! empty workingTemplate.name }">
			Editing '${workingTemplate.name}'
		</c:if>
	</div>
	
	<s:hidden name="id" />
	
	<div class="message">
		${message}
	</div>

	<div class="menuboxes">
		<table>
			<tr><td>Name</td>
				<td><s:textfield name="workingTemplate.name"  /></td></tr>
			<tr><td>Description</td>
				<td><s:textfield name="workingTemplate.desc"  /></td></tr>
			<tr><td>Body</td>
				<td><s:textarea label="Template Body" name="workingTemplate.body" cols="60" rows="10"  /></td></tr>
		</table>
		<s:fielderror/>
	</div>
	
	<div class="menuboxes">
		<button name="typebutton" accesskey="S" value="Save">
			<img src="graphics/ok.png" align="top">[S]ave
		</button>
		<button name="exitbutton" accesskey="X" value="Exit">
			<img src="graphics/start.png" align="top">E[x]it to Template Editor
		</button>
	</div>
</div>
</s:form>
<script language="JavaScript">
<!--
document.edit.whichTemplate.focus();
//-->
</script>
	
</body>