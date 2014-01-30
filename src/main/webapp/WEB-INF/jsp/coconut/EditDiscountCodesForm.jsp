<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Edit DiscountCode</title>
</head>
<body>
<div class="wrapper">

	<jsp:include page="PageHeader.jsp"></jsp:include>
	<s:form name="edit" action="coconut/updateDiscountCode">
	
	<div id="content_wrapper">
		<div id="primary_content_wide">
		<c:if test="${ empty workingDiscountCode.name }">
			Creating new DiscountCode
		</c:if>
		<c:if test="${! empty workingDiscountCode.name }">
			Editing '${workingDiscountCode.name}'
		</c:if>
	
	<s:hidden name="id" />
	
	<div class="message">
		${message}
	</div>

		<table>
			<tr><td>Name</td>
				<td><s:textfield name="workingDiscountCode.name"  /></td>
				<td rowspan="5" valign="top">
					<b>Discount Codes</b><br><p>
					Discount codes allow event managers to distribute codes to attendees that allows
					the attendee to get a price discount at the point of sale.<br>
					<p>
					The 'Factor' number is used to calculate the discount, depending on the type selected:
					<ul>
					<li>Discount : the 'Factor' is deducted from the final price
					<li>Percent : The 'Factor' is multiplied against the invoice price, and the result is deducted. 
					<li>Absolute: The 'Factor' price is the absolute final price, no matter what the calculated invoice price is.
					</td>
				</tr>
			<tr><td>Description</td>
				<td><s:textfield name="workingDiscountCode.desc"  /></td></tr>
			<tr><td>Note</td>
				<td><s:textarea label="DiscountCode Note" name="workingDiscountCode.note" cols="60" rows="10"  /></td></tr>
			<tr><td>Type</td>
				<td>
					<s:radio list="#{'percent':'Percent', 'discount':'Discount','absolute':'Absolute'}" 
						name="workingDiscountCode.type"/>
			<tr><td>Factor</td>
				<td><s:textfield label="Factor" name="workingDiscountCode.factor"/></td></tr>
			<tr><td>Active</td>
				<td><s:checkbox label="Active" name="workingDiscountCode.active"/></td></tr>
		</table>
		<s:fielderror/>
		<input type="submit" name="typebutton" accesskey="S" value="Save" class="button">
		<input type="submit" name="exutbutton" accesskey="X" value="Exit without saving" class="button">
	</div>
	</div>
</div>
</s:form>
<script language="JavaScript">
<!--
document.edit.whichDiscountCode.focus();
//-->
</script>
	
</body>