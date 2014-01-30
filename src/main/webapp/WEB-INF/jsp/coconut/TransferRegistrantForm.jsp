<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Transfer Registrant</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp" />
	<s:form action="coconut/transferRegistrant" name="transferform">
	
	<jsp:include page="RegistrantSubNav.jsp" />

	<div id="content_wrapper">
	
		<jsp:include page="RegistrantHeader.jsp" />

		<div id="primary_content">
			<table>
				<tr>
					<td>Transfer Registration to ID #:</td>
					<td><s:textfield name="transfertarget"/>
					<td>${transfertargetinfo}&nbsp;</td>
				</tr>
				<tr><td colspan="3">${message}</td></tr>
			</table>

			<button type="submit" name="transferbutton" accesskey="V" class="button" value="Validate"><u>V</u>alidate</u></button>
			<button type="submit" name="transferbutton" accesskey="F" class="button" value="Transfer">Trans<u>f</u>er</button>
		</div>
	</div>
</div>

</s:form>
<script language="JavaScript">
<!--
document.transferform.transfertarget.focus();

/* Find the current menu pick, and make sure its class is right... */
document.getElementById("registrantMenuPick").setAttribute("class","active");
document.getElementById("registrantMenuText").setAttribute("class","active");	

//-->
</script>
</body>
</html>