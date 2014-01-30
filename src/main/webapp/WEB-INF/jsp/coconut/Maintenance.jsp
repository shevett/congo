	<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Coconut: Maintenance</title>
</head>

<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>

	<div id="content_wrapper">
		<div id="primary_content">
			<div style="float: right; width: 300px; height: 200px; color=black; border-style: solid; border-color: black;">
				<h5>System Notices</h5>
				<ul>
					<s:iterator value="systemNotices">
  						<li><s:property/></li>
					</s:iterator>
				</ul>
			</div>
			<h5>General Administration</h5>
			<ul>
				<li><a href="<s:url action="coconut/listEvents.action"/>">Work with Events</a></li>
			</ul>

			<h5>This Event</h5>
			<ul>
				<li><a href="<s:url action="coconut/listRegistrationTypes.action"/>">Edit Registration Types</a></li>
				<li><a href="<s:url action="coconut/listPropertyConfigurations.action"/>">Edit Property Configurations</a></li>
				<li><a href="<s:url action="coconut/listTemplates.action"/>">Edit Templates</a></li>
<!--				<li><a href="<s:url action="coconut/listAddOnConfigurations.action"/>">Edit Add-Ons</a></li> -->
				<li><a href="<s:url action="coconut/listDiscountCodes.action"/>">Edit Discount Codes</a></li>
				<li><a href="<s:url action="coconut/importData"/>">Import registration data</a></li>
<!-- 				<li><a href="<s:url action="coconut/ImportDataForm"/>">Import bulk registration data</a></li> -->
			</ul>

			<h5>Configure Server</h5>
			<ul>
				<li><a href="<s:url action="coconut/SystemSettingsForm"/>">System Settings...</a></li>
				<li><a href="<s:url action="coconut/PaymentsSettingsForm"/>" accesskey="P">Payment processing...</a></li>
				<li><a href="<s:url action="coconut/SMTPSettingsForm"/>">SMTP (Email) settings...</a></li>
			</ul>
			
		</div>
		<jsp:include page="PageFooter.jsp"></jsp:include>
	</div>
</div>

<script language="JavaScript">
<!--

/* Find the current menu pick, and make sure its class is right... */
document.getElementById("maintenanceMenuPick").setAttribute("class","active");
document.getElementById("maintenanceMenuText").setAttribute("class","active");

//-->
</script>

</body>
</html>