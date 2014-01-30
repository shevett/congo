<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Merge Registrant - Results</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>
	<jsp:include page="RegistrantSubNav.jsp" />
	<s:form action="coconut/mergeRegistrantExit">
	<div id="content_wrapper">
		<jsp:include page="RegistrantHeader.jsp" />
		<div id="primary_content">
			<div class="floating_content">
				<b>Merging FROM (This registrant will be DELETED)</b> <br>
				<ul>
				# ${workingregistrant.rid} - ${workingregistrant.lastName}, ${workingregistrant.firstName} (${workingregistrant.badgeName})
				</ul>
			</div>

			<div class="floating_content">
				<b>Merging TO (This registrant will have information merged INTO it)</b><br>
				<ul>
					# ${session.mergetarget.rid} - ${session.mergetarget.lastName}, ${session.mergetarget.firstName} (${session.mergetarget.badgeName})
				</ul>
			</div>
			<div class="floating_content">
				<b>Results of merge process:</b>
				<pre>${mergeLog}</pre>
			</div>
			<button type="submit" accesskey="X" value="exit" class="button">E<u>x</u>it back to Registrant</button>
		</div> <!-- end primary_content -->
		<jsp:include page="PageFooter.jsp" />
	</div> <!-- end content_wrapper -->
</div> <!-- end wrapper -->
</s:form>
<script language="JavaScript">
<!--

/* Find the current menu pick, and make sure its class is right... */
document.getElementById("registrantMenuPick").setAttribute("class","active");
document.getElementById("registrantMenuText").setAttribute("class","active");	
document.getElementById("mergeSubText").setAttribute("class","active");
//-->
</script>
</body>
</html>