<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Edit Category</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>
	<jsp:include page="SchedulingSubNav.jsp" />
	
	<s:form name="edit" action="coconut/updateCategory">
	<div id="content_wrapper">
		<div id="floating_content">
			Edit Category...
		</div>
	
		<s:hidden name="id" />
	
		<div id="primary_content">
		<div class="message">
			${message}
			<s:fielderror/>
		</div>
			<table>
				<tr><td>CID</td>
					<td><s:textfield name="workingcategory.Cid" size="5" /></td></tr>
				<tr><td>Name</td>
					<td><s:textfield name="workingcategory.categoryName" size="20" /></td></tr>
				<tr><td>Description</td>
					<td><s:textfield name="workingcategory.categoryDescription" size="50"  /></td></tr>
				<tr><td>Location</td>
					<td><s:checkbox name="workingcategory.categoryActive" /></td></tr>
			</table>
			<input type="submit" name="typebutton" accesskey="S" value="Save" class="button">
			<input type="submit" name="exitbutton" accesskey="X" value="Exit without saving" class="button">
		</div>
		<jsp:include page="PageFooter.jsp"></jsp:include>
	</div>
</div>
</s:form>
<script language="JavaScript">
<!--
document.edit.whichCategory.focus();

/* Find the current menu pick, and make sure its class is right... */
document.getElementById("schedulingMenuPick").setAttribute("class","active");
document.getElementById("schedulingMenuText").setAttribute("class","active");

//-->
</script>
	
</body>