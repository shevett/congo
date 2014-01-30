<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Select Convention</title>
</head>

<body>
	<div class="wrapper">
		<jsp:include page="PageHeader.jsp"></jsp:include>
	
		<s:form action="coconut/selectConvention">
		
		<div id="content_wrapper">
			<div id="primary_content">
				<label>Select Event:</label>
				<s:select 
					list="eventlist"
					listKey="value.conCID"
					listValue="key + ' - ' + value.conName"
					value="key" 
					name="selectedevent" />
				<input type="submit" name="selectbutton" value="Select" class="button" accesskey="S">
			</div> <!-- end primary_content -->
			<jsp:include page="PageFooter.jsp"></jsp:include>
			
		</div> <!-- end content_wrapper -->
	</div> <!-- end wrapper -->

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