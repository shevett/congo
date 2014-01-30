<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
<title>Login</title>
</head>
<body>
	<div id="admin_wrapper">
		<h1>CONGO&#0153; Coconut Login</h1>
		
		<!-- Pddreferred cid is <s:property value="#attr.properties['preferredcid']"/><br> -->
		<s:form action="performlogin" name="loginform">

		<c:if test='${fn:length(message) > 1}'>
			<div class="attention">${message}</div>
		</c:if>	

		<p>
			<label>Username</label><br/>
			<s:textfield name="username" cssClass="input large"/>
		</p>
		<p>
			<label>Password</label><br/>
			<s:password name="password" cssClass="input large"/>
		</p>
		<br />
		<p>
			<label>Select event</label><br/>
			<br>
			<s:select 
				list="conList"
				listKey="value.conCID"
				listValue="key + ' - ' + value.conName"
				value="settings.event_default.settingValue" 
				name="cid" 
				cssClass="input large"/>
		</p>
		<br />
		<p>			
			<button accesskey="L" name="Submit" value="Login" class="button"><u>L</u>ogin</button> 
			<button type="reset" name="reset" id="reset" value="Reset" class="button" />
		</p>
		
		<jsp:include page="PageFooter.jsp"></jsp:include>
		
</div>
</s:form>
</body>
<script language="JavaScript">
<!--
document.loginform.username.focus();
//-->
</script>
</html>