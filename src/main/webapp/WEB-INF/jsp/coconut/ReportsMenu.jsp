<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Coconut: Reports</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>
		
	<div id="content_wrapper">
		<div id="primary_content">
		<h5>Reports</h5>
		<ul>
			<a href="<s:url action="coconut/ReportSetupAfterEventExport"/>">Export after-convention...</a><br />
			A full dump of the registrants attending the current event.<br />
			<br/>
						
			<a href="<s:url action="coconut/ReportStatus"/>" accesskey="R"><u>R</u>egistrant current status report</a><br />
			Show a summary of the current event<br/>
			<br />
			
			<a href="<s:url action="coconut/ReportByDate"/>">Registrant report by date</a><br />
			A report over time showing estimated registration numbers<br />
			<br />
			
			<a href="<s:url action="coconut/ReportSalesbyType"/>">Sales by payment type</a><br />
			Breakdown of sales by payment type<br />
			<br />
			
			<a href="<s:url action="coconut/setupReportProperties"/>">Registrant Properties report</a><br />
			<br />
			
			<a href="<s:url action="coconut/ReportActivity"/>">Activity report</a><br />
			A report of all recent activity in CONGO.<br />
			<br />
			
			<a href="<s:url action="coconut/ReportNotices"/>">Notice Report</a><br />
			List all registrant notes for registrants<br />
			
		</ul>
		
		</div>

		<jsp:include page="PageFooter.jsp"></jsp:include>

	</div> <!-- end content-wrapper -->
</div> <!-- end wrapper -->



<script language="JavaScript">
<!--

/* Find the current menu pick, and make sure its class is right... */
document.getElementById("reportMenuPick").setAttribute("class","active");
document.getElementById("reportMenuText").setAttribute("class","active");

//-->
</script>
</body>
</html>