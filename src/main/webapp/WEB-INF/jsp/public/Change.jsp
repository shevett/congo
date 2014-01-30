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
	<form method="post" action="webChangeForm.action" name="changeform">
	
	<%@include file="PageHeader.jsp" %>
	
	<div class="inputform">
	
		<div class="title">
			Update your Contact information<hr>
		</div>
		
		<div style="float: right; width: 200px;">
			<div class='box-orange'>
	 			<div class='boxtop-orange'><div></div></div>
  					<div class='boxcontent-orange'>
	  					Use this form to update your profile and Contact information
 					</div>
	 			<div class='boxbottom-orange'><div></div></div>
			</div>
		</div>
	
		<div class="title">
			Basics
		</div>
		
		<div class="sectionbody">
		
		<table class="inputform">
			<tr><td width=150px>First name</td>
				<td><s:textfield name="firstname" value="%{#session.webuser.firstName}" size="30" /></td></tr>
			<tr><td>Last name</td>
				<td><s:textfield name="lastname" value="%{#session.webuser.lastName}" size="40"/></td></tr>
			<tr><td>Company</td>
				<td><s:textfield name="companyname" value="%{#session.webuser.company}" size="30"/></td></tr>
			<tr><td>Badge name</td>
				<td><s:textfield name="badgename" value="%{#session.webuser.badgeName}" size="40"/></td></tr>
		</table>
		</div>
		
		<div class="title">
			Contact information
		</div>
		
		<div class="sectionbody">
			<table class="inputform">
				<tr><td width=150px>Email</td>
					<td><s:textfield name="email" value="%{#session.webuser.getPrimaryEmail()}" size="40" /></td></tr>
				</tr>
				<tr><td width=150px>Phone</td>
					<td><s:textfield name="phonenumber" value="%{#session.webuser.getPrimaryPhone()}" size="40"/></td></tr>
			</table>
		</div>
		
		<div class="title">
			Mailing address
		</div>
		
		<div class="sectionbody">
			<table class="inputform">
				<tr><td width=150px>Address1</td>
					<td><s:textfield name="address1" value="%{#session.webuser.getPrimaryAddress().line1}" size="40"/></td></tr>
				<tr><td>Address2</td>
					<td><s:textfield name="address2" value="%{#session.webuser.getPrimaryAddress().line2}" size="40"/></td></tr>
				<tr><td>City</td>
					<td><s:textfield name="city"  value="%{#session.webuser.getPrimaryAddress().city}" size="40"/></td></tr>
				<tr><td>State</td>
					<td><s:textfield name="state" value="%{#session.webuser.getPrimaryAddress().state}" size="20"/></td></tr>
				<tr><td>Zipcode</td>
					<td><s:textfield name="zipcode" value="%{#session.webuser.getPrimaryAddress().zipcode}" size="10"/></td></tr>
				<tr><td>Country</td>
					<td><s:textfield name="country" value="%{#session.webuser.getPrimaryAddress().country}" size="40"/></td></tr>
			</table>
		</div>
		
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
document.createform.firstname.focus();
//-->
</script>
</body>
</html>
