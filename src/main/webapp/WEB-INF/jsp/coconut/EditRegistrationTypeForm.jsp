<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
<title>Edit Registration Type</title>
</head>
<body>
<div class="wrapper">

	<jsp:include page="PageHeader.jsp"></jsp:include>
	<s:form name="edit" action="coconut/updateEditRegistrationTypes">
	<div id="content_wrapper">
		<div id="floating_content">
			<c:if test="${ empty workingRegistrationType.regName }">
				Creating new Registration Type
			</c:if>
			<c:if test="${! empty workingRegistrationType.regName }">
				Editing Registration Type '${workingRegistrationType.regName}'
			</c:if>
		</div>
	
		<div id="primary_content">
			<s:hidden name="id" />
	
			<div class="message">
				${message}
			</div>

			<table>
				<tr><td>Name</td>
					<td>
						<c:if test="${ empty workingRegistrationType.regName }">
							<s:textfield name="whichRegtype" />
						</c:if>
						<c:if test="${! empty workingRegistrationType.regName }">
							<s:hidden name="whichRegtype" />
							${whichRegtype}
						</c:if>
					</td></tr>
				<tr><td>Description</td>
					<td><s:textfield name="workingRegistrationType.regDesc"  /></td></tr>
				<tr><td>Cost</td>
					<td><s:textfield name="workingRegistrationType.regCost"  /></td></tr>
				<tr><td>Print As</td>
					<td><s:textfield name="workingRegistrationType.regPrint" /></td></tr>
				<tr><td>Sequence number</td>
					<td><s:textfield name="workingRegistrationType.regSequence" /></td></tr>
				<tr><td>Active?</td>
					<td><s:checkbox name="workingRegistrationType.regActive" /></td></tr>
				<tr><td>Required Discount Code</td>
					<td><s:textfield name="workingRegistrationType.regDiscountCode" /></td></tr>
				<tr><td>Banner text</td>
					<td><s:textfield name="workingRegistrationType.regBanner"/></td></tr>
				<tr><td>Considered a comp?</td>
					<td><s:checkbox name="workingRegistrationType.regComp" /></td></tr>
<%-- 				<tr><td>What days is this valid for? (Used for headcounts)</td>
					<td><s:label value="Sun"/>
						<s:checkbox name="workingRegistrationType.regDayArray[0]"/>
						<s:label value="Mon"/>
						<s:checkbox name="workingRegistrationType.regDayArray[1]"/>
						<s:label value="Tue"/>
						<s:checkbox name="workingRegistrationType.regDayArray[2]"/>
						<s:label value="Wed"/>
						<s:checkbox name="workingRegistrationType.regDayArray[3]"/>
						<s:label value="Thu"/>
						<s:checkbox name="workingRegistrationType.regDayArray[4]"/>
						<s:label value="Fri"/>
						<s:checkbox name="workingRegistrationType.regDayArray[5]"/>
						<s:label value="Sat"/>
						<s:checkbox name="workingRegistrationType.regDayArray[6]"/>
					</td>
				</tr> --%>
			</table>
			<s:fielderror/>
			<input type="submit" name="typebutton" value="Save" accesskey="S" class="button">
			<input type="submit" name="exitbutton" value="Exit without saving" accesskey="X" class="button">
		</div> <!-- end primary_content -->
		<jsp:include page="PageFooter.jsp" />
	</div> <!-- end content_wrapper -->
</div> <!-- end wrapper -->
</s:form>

<script language="JavaScript">
<!--
document.edit.whichRegtype.focus();

/* Find the current menu pick, and make sure its class is right... */
document.getElementById("maintenanceMenuPick").setAttribute("class","active");
document.getElementById("maintenanceMenuText").setAttribute("class","active");

//-->
</script>
	
</body>