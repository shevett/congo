	<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Coconut: Maintenance: Configure Payments</title>
</head>

<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>

	<s:form name="edit" action="coconut/editSettings">
	<div id="content_wrapper">
		<div id="primary_content">
			<h5>Paypal</h5>
			<table>
				<tr>
					<td>Enable Paypal?</td>
					<td>Should this CONGO installation accept Paypal payments?</td>
					<td>
						<s:select label="paypal_enabled"
							list="#{'0':'No','1':'Yes'}"
							value="%{#session.settings.paypal_enabled.settingValue}"
							name="paypal_enabled"/>
					</td>
				</tr>

				<tr>
					<td>Mode</td>
					<td>Which Paypal mode should we be in?</td>
					<td>
						<s:select label="paypal_mode"
							list="#{'sandbox':'Sandbox','production':'Production'}"
							value="%{#session.settings.paypal_mode.settingValue}"
							name="paypal_mode"/>
					</td>
				</tr>
<%-- 				<tr>
					<td>Server target</td>
					<td>What Paypal server to use</td>
					<td>
						<s:select label="paypal_server"
							list="#{'testing':'Testing','production':'Production'}"
							value="%{#session.settings.paypal_server.settingValue}"
							name="paypal_server"/>
					</td>
				</tr> --%>
				<tr>
					<td>Home URL</td>
					<td>Top level URL of this CONGO installation? (no trailing slash)</td>
					<td><s:textfield name="paypal_url" value="%{#session.settings.paypal_url.settingValue}" size="30" /></td>
				</tr>	
				<tr>
					<td>Username</td>
					<td>Username with Paypal</td>
					<td><s:textfield name="paypal_user" value="%{#session.settings.paypal_user.settingValue}" size="30" /></td>
				</tr>
				<tr>
					<td>Password</td>
					<td>Password with paypal</td>
					<td><s:textfield name="paypal_password" value="%{#session.settings.paypal_password.settingValue}" size="30" /></td>
				<tr>
					<td>Signature</td>
					<td>Signature (see Paypal to get this)</td> 
					<td><s:textfield name="paypal_signature" value="%{#session.settings.paypal_signature.settingValue}" size="30" /></td>
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