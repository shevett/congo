<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Review Email...</title>
</head>
<body>

<div class="wrapper">
	<jsp:include page="PageHeader.jsp" />
	<jsp:include page="RegistrantSubNav.jsp" />
	<s:form name="preview" action="coconut/postReview">
	<div id="content_wrapper">
		<jsp:include page="RegistrantHeader.jsp" />
		
		<c:if test='${primaryEmail != null}'> 
			<div id="floating_content">
				<div style="font-size: small;">Select template to use: 
					<s:select 
						list="templateList"
						listKey="value.name"
						listValue="value.name"
						value="selectedTemplate" 
						name="selectedTemplate" />
					<input type="submit" name="previewButton" value="Preview" class="button">
				</div>
			</div>
		</c:if>
		
		<div id="primary_content">

		<div class="messages">
			${message}
		</div>
		<pre>${templateBody}</pre>
		<button name=sentButton" value="Send mail" class="button" accesskey="S"><u>S</u>end mail</button>
		</div> <!-- end primary content -->
		
		<jsp:include page="RegistrantSidebar.jsp" />
	
		<jsp:include page="PageFooter.jsp" />
		
	</div><!-- end content wrapper -->
	
</div><!-- end wrapper -->
</s:form>
<script language="JavaScript">
<!--
/* Find the current menu pick, and make sure its class is right... */
document.getElementById("registrantMenuPick").setAttribute("class","active");
document.getElementById("registrantMenuText").setAttribute("class","active");	
-->
</script>
</body>
</html>