<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Logout</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>
		
	<div id="content_wrapper">
		<div id="primary_content">
			<s:form action="coconut/Logout.action">
			Do you want to log out of Coconut?
			<button accesskey="L" name="logoutbutton" value="logout" class="button">
				<U>L</u>ogout
			</button>
			</s:form>
		</div> <!-- end primary_content -->
		
		<jsp:include page="PageFooter.jsp"></jsp:include>

	</div> <!-- end content-wrapper -->
</div> <!-- end wrapper -->

<script language="JavaScript">
<!--

document.searchform.search.focus();

/* Find the current menu pick, and make sure its class is right... */
document.getElementById("homeMenuPick").setAttribute("class","active");
document.getElementById("homeMenuText").setAttribute("class","active");

//-->
</script>
</body>
</html>