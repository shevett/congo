	<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Coconut: Maintenance: SMTP (Email) Settings</title>
</head>

<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>

	<s:form name="edit" action="coconut/editSettings">
	<div id="content_wrapper">
		<div id="primary_content">
			<h5>SMTP (Email) Server settings</h5>
			<table>
				<tr>
					<td>Server to connect to</td>
					<td>Name of the SMTP server to connect to to send mail</td>
					<td><s:textfield name="smtp_server" value="%{#session.settings.smtp_server.settingValue}" size="30" /></td>
				</tr>

				<tr>
					<td>Port number</td>
					<td>What port to connect to</td>
					<td><s:textfield name="smtp_port" value="%{#session.settings.smtp_port.settingValue}" size="10" /></td>
				</tr>
				<tr>
					<td>From Name</td>
					<td>The name of the user to be shown in mail</td>
					<td><s:textfield name="smtp_fromname" value="%{#session.settings.smtp_fromname.settingValue}" size="30" /></td>
				</tr>
				<tr>
					<td>From address</td>
					<td>The email address originating mail (people may reply to this)</td>
					<td><s:textfield name="smtp_fromaddress" value="%{#session.settings.smtp_fromaddress.settingValue}" size="30" /></td>
				</tr>
				<tr>
					<td>BCC</td>
					<td>Outbound mail can be BCC'ed to any address</td> 
					<td><s:textfield name="smtp_bcc" value="%{#session.settings.smtp_bcc.settingValue}" size="30" /></td>
				</tr>
				<tr>
					<td>Method</td>
					<td>Authentication Method</td>
					<td><s:select label="paypal_enabled"
							list="#{'CRAM-MD5':'CRAM-MD5','LOGIN':'LOGIN','PLAIN':'PLAIN','NONE':'NONE'}"
							value="%{#session.settings.smtp_method.settingValue}"
							name="smtp_method"/>
				<tr>
					<td>Authentication name</td>
					<td>Some mailservers require authentication.</td> 
					<td><s:textfield name="smtp_username" value="%{#session.settings.smtp_username.settingValue}" size="30" /></td>
				</tr>
				<tr>
					<td>Authentication password</td>
					<td>Authentication password</td> 
					<td><s:textfield name="smtp_password" value="%{#session.settings.smtp_password.settingValue}" size="30" /></td>
				</tr>
				
			</table>
			<input type="submit" accesskey="S" name="savebutton" class="button" value="Save">
			<input type="submit" accesskey="X" name="exitbutton" class="button" value="Exit without Saving">
		</div>
		<jsp:include page="PageFooter.jsp"></jsp:include>
	</div>
</div>
</s:form>
<script language="JavaScript">
<!--

/* Find the current menu pick, and make sure its class is right... */
document.getElementById("maintenanceMenuPick").setAttribute("class","active");
document.getElementById("maintenanceMenuText").setAttribute("class","active");

//-->
</script>

</body>
</html>