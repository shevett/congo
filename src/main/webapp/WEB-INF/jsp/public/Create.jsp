<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<LINK REL="StyleSheet" href="getConfiguredStylesheet.action" type="text/css">
	<title>${currentConvention.conName}</title>
</head>
<body>
	<form method="post" action="webCreateForm.action" name="createform">
	
	<%@include file="PageHeader.jsp" %>
	
	<div class="inputform">
	
		<div class="title">
			Create new Account (<span class="required"> * </span> denotes required field)<hr>
		</div>
		
		<div class="message">
			${message}
		</div>
		
		<div style="float: right; width: 200px;">
			<div class='box-orange'>
	 			<div class='boxtop-orange'><div></div></div>
	  				<div class='boxcontent-orange'>
  						Creating an account will allow you to come back and view your
  						registration information later.  You will also be able to make changes,
  						purchase additional services or items (if available), or check on your status.<hr>
  						Event home page:<br>
  						<a href="${currentConvention.conWebsite }">${currentConvention.conWebsite }</a>
					</div>
 				<div class='boxbottom-orange'><div></div></div>
			</div>
		</div>
	
		
		<table class="inputform">
			<tr><td>First name</td><td><s:textfield name="firstname"/><span class="required"> *</span></td></tr>
			<tr><td>Last name</td><td><s:textfield name="lastname"/><span class="required"> *</span></td></tr>
			<tr><td>Badge name</td><td><s:textfield name="badgename"/><span class="required"> *</span></td></tr>
			<tr><td>Company name</td><td><input name="companyname"/></td></tr>
			<tr><td>Address1</td><td><s:textfield name="address1"/></td></tr>
			<tr><td>Address2</td><td><s:textfield name="address2"/></td></tr>
			<tr><td>City</td><td><s:textfield name="city"/></td></tr>
			<tr><td>State</td><td><s:textfield name="state"/></td></tr>
			<tr><td>Zipcode</td><td><s:textfield name="zipcode"/></td></tr>
			<tr><td>Country</td><td><s:textfield name="country"/></td></tr>
			<tr><td>Email</td><td><s:textfield name="email"/><span class="required"> *</span></td></tr>
			<tr><td>Phone Number</td><td><s:textfield name="phonenumber"/></td></tr>
			<tr><td colspan=2>&nbsp;</td></tr>
			<tr><td>Choose a password</td><td><s:password name="password1"/><span class="required"> *</span></td></tr>
			<tr><td>Retype your passsword</td><td><s:password name="password2"/><span class="required"> *</span></td></tr>
		</table><br>
	
	</div>

		<div class="buttonbar">
			<input type="submit" name="createbutton" class="webbutton" value="Create">
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
