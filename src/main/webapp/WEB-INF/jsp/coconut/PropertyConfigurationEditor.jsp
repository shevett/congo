<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Edit Property Configuration</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>
	<s:form action="coconut/updatePropertyConfiguration">
		
	<div id="content_wrapper">
		<div id="floating_content">
			Edit Property Configuration '${name}'
		</div>
		
		<div id="primary_content_wide">
			<table>
				<tr><td width=20%>Name:</td><td width=35%>${name}</td></tr>
				<tr>
					<td width=20%>Default Value</td>
					<td width=35%>
						<s:textfield name="defaultvalue" value="%{#session.workingpc.defaultvalue}" width="100"/>
					</td>
				</tr>
				<tr>
					<td width=20%>Scope</td>
					<td width=35%>
						<s:select 
							label="Scope" 
							name="scope" 
							value="%{#session.workingpc.scope}"
							list="#{'Event':'Event','Global':'Global'}"
						/>
					</td>
				</tr>
				<tr>
					<td width=20%>Data type</td>
					<td width=35%>
						<s:select label="Data type"
			        		name="type"
			        		headerKey="-1" headerValue="Select Type"
			        		list="#{'boolean':'boolean','string':'string','float':'float','date':'date','numeric':'numeric','picker':'picker'}"
			        		value="%{#session.workingpc.type}"
			        		required="true"
			 			/>
					</td>
					<td width=45%>For pickers, use "value:prompt,value:prompt,value:prompt"</td>
				</tr>
				<tr>
					<td width=20%>Format</td>
					<td width=35%>
						<s:textfield name="format" value="%{#session.workingpc.format}" maxlength="255"/>
					</td>
					<td>This field currently only used for the 'picker' type.</td>
				</tr>
						
				<tr>
					<td width=20%>Prompt during Registration?</td>
					<td width=35%><s:checkbox name="regprompt" value="%{#session.workingpc.regprompt}" /></td></tr>
				<tr>
					<td width=20%>Cost</td>
					<td width="35%"><s:textfield name="cost" value="%{#session.workingpc.cost}" maxlength="8"/></td>
				</tr>
				<tr>
					<td width=20%>Description</td>
					<td width=35%><s:textfield name="description" value="%{#session.workingpc.description}" maxlength="40"/></td>
				</tr>
				<tr>
					<td width=20%>Sequence</td>
					<td width=35%><s:textfield name="sequence" value="%{#session.workingpc.sequence}" maxlength="2"/></td>
				</tr>
			</table>
			<input type="submit" accesskey="S" value="Save" name="editbutton" class="button">
			<input type="submit" accesskey="X" value="Exit without saving" name="exitbutton" class="button">
		</div>
		<jsp:include page="PageFooter.jsp"></jsp:include>
		
	</div>
</div>
</s:form>
<script language="JavaScript">
<!--

/* Find the current menu pick, and make sure its class is right... */
document.getElementById("maintenanceMenuPick").setAttribute("class","active");
document.getElementById("maintenanceMenuText").setAttribute("class","active");

//-->

</body>
</html>