<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Coconut: Properties Report</title>
</head>
<body>
	<jsp:include page="PageHeader.jsp"></jsp:include>
	<form method="post" action="runReportProperties.action" name="createform">
	
	<div class="message">
		${message}
	</div>
		
	<div id="content_wrapper">
		<div id="primary_content">
		(For boolean fields, use '1' for true)<br>For wildcards, use '%' (alone, it means 'all values')
		<table>
			<tr>
				<td>Property to query</td>
				<td>
					<s:select 	
						name="selectedProperty" 
						list="configList.{name}"
						value="configList.{name}"/>
				</td>
			</tr>
			<tr>
				<td>Containing value</td>
				<td>
					<s:textfield name="checkValue"/>
				<td>
			</tr>
			<tr>
				<td>Format</td>
				<td>
					<input type="radio" name="format" value="HTML">HTML  
					<input type="radio" name="format" value="CSV">CSV
				</td>
			</tr>
		</table>
		<input type="submit" name="runbutton" class="button" value="Run report">
		</div>
		<jsp:include page="PageFooter.jsp"/>
	</div>
</div>
</form>

<script language="JavaScript">
<!--

document.propertiesform.selectedProperty.focus();

//-->
</script>
</body>
</html>