<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Void Registrant</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp" />
	<s:form action="coconut/voidRegistrant">
	
	<jsp:include page="RegistrantSubNav.jsp" />

	<div id="content_wrapper">
	
		<jsp:include page="RegistrantHeader.jsp" />

		<div id="primary_content">

			<table>
				<tr>
					<td>Reason for void</td>
					<td><s:textfield name="voidreason"/>
				</tr>
			</table>
			<button accesskey="V" name="voidbutton" value="Void" class="button"><u>V</u>oid</button> 
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
-->
</script>
</body>
</html>