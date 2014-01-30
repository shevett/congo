<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Create Property Configuration</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>
		
	<div id="content_wrapper">
		<div id="floating_content">
			Creating new Property Configuration
		</div>
		<div id="primary_content">
		<div class="message">
			${message}
		</div>
		<s:form action="coconut/maintCreatePropertyConfiguration">
		<table>
			<tr><td>Name:</td><td width=80%><s:textfield name="name"/></td></tr>
			<tr><td>Default Value</td><td width=80%><s:textfield name="defaultvalue"/></td></tr>
			<tr>
				<td>Scope</td>
				<td width=80%>
					<s:select 
						label="Scope" 
						name="scope" 
						value="scope"
						list="#{'Event':'Event','Global':'Global'}"
					/>
				</td>
			</tr>
			<tr>
				<td>Data type</td>
				<td width=80%>
					<s:select label="Data type"
		        		name="type"
		        		headerKey="-1" headerValue="Select Type"
		        		list="#{'boolean':'boolean','string':'string','float':'float','date':'date','numeric':'numeric','picker':'picker'}"
		        		value="type"
		        		required="true"
		 			/>
				</td>
			</tr>
				<tr>
				<td>Format</td>
				<td>
					<s:textfield name="format"/>
				</td>
			</tr>
			<tr><td>Prompt during Registration?</td><td width=80%><s:checkbox name="regprompt"/></td></tr>
			<tr><td>Cost</td><td width=80%><s:textfield name="cost"/></td></tr>
			<tr><td>Description</td><td width=80%><s:textfield name="description"/></td></tr>
			<tr><td>Sequence</td><td width=80%><s:textfield name="sequence"/></td></tr>
		</table>
		<input type="submit" accesskey="C" name="createbutton" class="button" value="Create">
		</s:form>
	</div>
</div>
</div>

</body>
</html>