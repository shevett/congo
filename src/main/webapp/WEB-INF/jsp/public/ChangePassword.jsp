<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<LINK REL="StyleSheet" href="getConfiguredStylesheet.action" type="text/css">
	<title>${currentConvention.conName}</title>
</head>
<body>
	<form method="post" action="webChangePasswordForm.action" name="changeform">
	
	<%@include file="PageHeader.jsp" %>
	
	<div class="inputform">
	
		<div class="title">
			Change your password<hr>
		</div>
		
		<div style="float: right; width: 200px;">
			<div class='box-orange'>
	 			<div class='boxtop-orange'><div></div></div>
  					<div class='boxcontent-orange'>
	  					Use this form to change your password.
 					</div>
	 			<div class='boxbottom-orange'><div></div></div>
			</div>
		</div>
	
		<table class="inputform">
			<tr><td>New password</td>
				<td><s:password name="password1" /></td></tr>
			<tr><td>Enter it again</td>
				<td><s:password name="password2" /></td></tr>
		</table>
		
		<div class="message">${message}</div>
	
	</div>

		<div class="buttonbar">
			<input type="submit" name="savebutton" class="webbutton" value="Save changes">
			<input type="submit" name="cancelbutton" class="webbutton" value="Cancel">
		</div>

	<jsp:include page="PageFooter.jsp" />
	</form>
	
<script language="JavaScript">
<!--
document.createform.password1.focus();
//-->
</script>
</body>
</html>
