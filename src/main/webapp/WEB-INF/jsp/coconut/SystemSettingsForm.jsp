	<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Coconut: Maintenance: Configure System Settings</title>
</head>

<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>

	<s:form name="edit" action="coconut/editSettings">
	<div id="content_wrapper">
		<div class="message">
			${message}
			<s:fielderror/>
		</div>
		<div id="primary_content">
			<h5>System Settings</h5>
			<table>
				<tr>
					<td>Default event?</td>
					<td>What event ID should the system default to?</td>
					<td>
						<s:textfield name="event_default" value="%{#session.settings.event_default.settingValue}" size="30" />
					</td>
				</tr>

				<tr>
					<td>Default Payment Type</td>
					<td>What payment type should Invoices default to?</td>
					<td>
						<s:select label="event_paymenttype"
							list="#{'cash':'Cash','check':'Check','paypal':'PayPal','amex':'American Express','discover':'Discover','visa':'Visa','mastercard':'MasterCard'}"
							value="%{#session.settings.event_paymenttype.settingValue}"
							name="event_paymenttype"/>
					</td>
				</tr>
				
				<tr>
					<td>Print command?</td>
					<td>Command to use to print badges (ie 'lpr -Ppool')</td>
					<td>
						<s:textfield name="event_printcommand" value="%{#session.settings.event_printcommand.settingValue}" size="30" />
					</td>
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