<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Import Data</title>
</head>
<body>
	<div class="wrapper">
		<jsp:include page="PageHeader.jsp"></jsp:include>
	
		<s:form action="coconut/importData">
		
		<div id="content_wrapper">
			<div id="primary_content">
				<label>Select importer to use: </label>
				<s:select label="Select import format"
						id="importPulldown"
						list="#{'' : 'Select an importer...',
							'bib':'BiB','intercon':'InterCon',
							'byregtype':'By Registrant Type', 
							'bulk':'Bulk Registration'}"
						name="importFormat"
						onchange="switchblock()"/> 
				<div id="regtypes" style="display: none">		
					<label>Registration type</label>
					<s:select 
			    		headerKey="Any"
    					headerValue="Any"
						list="regTypes"
						listKey="value.regName"
						listValue="value.regName"
						value="regtype" 
						name="regtype" />
				</div>
				<hr>
				<label>Paste in data to be imported, then select an option below... </label>
				<s:textarea name="data" cols="100" rows="10" /><br>
				<input type="submit" accesskey="V" name="importbutton" value="Validate only" class="button">
				<input type="submit" accesskey="I" name="importbutton" value="Validate and Import" class="button"><br>
				${message}<br />
			</div> <!-- end primary_content -->
			
			<jsp:include page="PageFooter.jsp"></jsp:include>

		</div> <!-- end content-wrapper -->
	
	</div> <!-- end wrapper -->

	</s:form>

<script language="JavaScript">
<!--

/* Find the current menu pick, and make sure its class is right... */
document.getElementById("maintenanceMenuPick").setAttribute("class","active");
document.getElementById("maintenanceMenuText").setAttribute("class","active");
function switchblock() {
	// Only switch on the regtype pull down if the select is byregtype...
	console.log("switchblock - changing...");
	var e = document.getElementById("importPulldown");
	var f = e.options[e.selectedIndex].value;
	document.getElementById('regtypes').style.display = f == 'byregtype' ? '' : 'none';
}

//-->
</script>
</body>
</html>