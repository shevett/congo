<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Merge Registrant</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>
	<jsp:include page="RegistrantSubNav.jsp" />
	<s:form action="coconut/mergeRegistrantPreview" name="mergeform">
	<div id="content_wrapper">
		<jsp:include page="RegistrantHeader.jsp" />
		<div id="primary_content">
			<p>This function is used to resolve duplicate registrants within the database.  The "Source" registrant
			is # ${workingregistrant.rid} - this registrant will be deleted, and all information will be merged 
			to the 'target' registrant, specified below.</p>
	
			<p>This function cannot be reversed once complete.  Make sure you know what you're doing!</p>
			<table>
				<tr>
					<td>Merge to ('target') Registrant ID #</td>
					<td><s:textfield name="mergetarget"/>
				</tr>
				<tr><td><div class="message">${message}</div></td></tr>
			</table>
			<input type="submit" accesskey="P" name="mergebutton" value="Preview" class="button">
		</div> <!-- end primary_content -->
		<jsp:include page="PageFooter.jsp" />
	</div> <!-- end content_wrapper -->
</div> <!-- end wrapper -->

</s:form>

<script language="JavaScript">
<!--
document.mergeform.mergetarget.focus();

/* Find the current menu pick, and make sure its class is right... */
document.getElementById("registrantMenuPick").setAttribute("class","active");
document.getElementById("registrantMenuText").setAttribute("class","active");	
document.getElementById("mergeSubText").setAttribute("class","active");
//-->
</script>
</body>
</html>