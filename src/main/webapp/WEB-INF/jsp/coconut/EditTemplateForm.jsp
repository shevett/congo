<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Edit Template</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>
	<s:form name="edit" action="coconut/updateEditTemplate">
	<div id="content_wrapper">
		<div id="floating_content">
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
	
		<div id="primary_content">
			<table>
				<tr><td>Name</td>
					<td><s:textfield name="workingTemplate.name"  /></td>
					<td rowspan=3>
						<b>Variables:</b><Br>
						<div style="margin-left:20px;">
							EventName<br>
							EventEmail<br>
							EventWebsite<br>
							EventStart<br>
							RegistrantID<br>
							RegistrantLastName<br>
							RegistrantFirstName<br>
							RegistrantBadgeName<br>
							RegistrantEmail<br>
							RegistrantType<br>
						</div>
					</td>
				</tr>
				<tr><td>Description</td>
					<td><s:textfield name="workingTemplate.desc"  /></td></tr>
				<tr><td>Body</td>
					<td><s:textarea label="Template Body" name="workingTemplate.body" cols="60" rows="10"  /></td></tr>
			</table>
			<s:fielderror/>
			<input type="submit" name="typebutton" accesskey="S" value="Save" class="button">
			<input type="submit" name="exitbutton" accesskey="X" value="Exit without saving" class="button">
		</div>
		<jsp:include page="PageFooter.jsp"></jsp:include>
	</div>
</div>
</s:form>
<script language="JavaScript">
<!--
document.edit.whichTemplate.focus();

/* Find the current menu pick, and make sure its class is right... */
document.getElementById("maintenanceMenuPick").setAttribute("class","active");
document.getElementById("maintenanceMenuText").setAttribute("class","active");

//-->
</script>
	
</body>