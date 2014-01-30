<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>${sessionScope.currentConvention.conName}</title>
<LINK REL="StyleSheet" href="getConfiguredStylesheet.action" type="text/css">
<!-- <LINK REL="StyleSheet" href="getStylesheet.action" type="text/css"> -->
</head>
<body>

	<form method="post" action="webLoginForm.action" name="welcomeform">
	
	<%@include file="PageHeader.jsp" %>
	
	<div style="float: left; width: 320px;">
		<div class='box-green'>
	 		<div class='boxtop-green'><div></div></div>
  				<div class='boxcontent-green'>
  				<strong>Already have an account?</strong><br>&nbsp;<br>
  				If you've attended a previous event with this organization, chances are you already
  				have an account.<br>&nbsp;<br>
				Badge, Email, or Registrant ID:<br>
				<input name="login"><br>
				Password<br>
				<input name="password" type="password"><br>&nbsp;<br>
				<input type="submit" name="loginbutton" class="webbutton" value="Login">
				<div class="message">${message}</div>
				<hr>
				You can use your Badge name, Registrant ID, or E-Mail address to log in and register, check your registration status, 
				or update your profile information.  If you've forgotten your password, use the 'Forgot password' link at the bottom. <hr>
				</div>
 			<div class='boxbottom-green'><div></div></div>
		</div>
	</div>
	
	<div class="sectionbody">
		<div class="title">
			Welcome to registration for ${sessionScope.currentConvention.conName } 
		</div>
	</div>
	
	<div class="sectionbody">
		${sessionScope.currentConvention.conDescription}
	</div>

	<div class="sectionbody">
		<hr>
		<p>If you are a new registrant, click on the 'Create new account' button.  You will be asked for your personal
		information in order to create an account with this system.  Once your account is created, you can select your
		registration options and complete the registration for this event. </p>

		<p>You can return at any time and, using your login information, log in and check on the status of your registration.</p>
	</div>

	<div class="buttonbar">
		<input type="submit" name="createbutton" class="webbutton" value="Create a new account">
		<input type="submit" name="forgotbutton" class="webbutton" value="Forgot password">
	</div>
	
	<jsp:include page="PageFooter.jsp" />
	</form>
<script language="JavaScript">
<!--
document.welcomeform.login.focus();
//-->
</script>
	
</body>
</html>