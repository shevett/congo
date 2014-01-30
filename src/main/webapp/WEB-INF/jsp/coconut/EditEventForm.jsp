<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Edit conference Details</title>
</head>
<body>
<s:form action="coconut/postEditEvent">
<s:hidden name="id" />

<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>
		
	<div id="content_wrapper">
			<div id="floating_content">
				# ${workingEvent.conCID} - 
				<s:property value="workingEvent.conName"/>
			</div>
		<div id="primary_content_wide">
			<div class="message">
				${message}
			</div>
			<table>
				<tr>
					<td>Name:</td>
					<td width=45%><s:textfield name="workingEvent.conName" size="60" /></td>
				</tr>
				<tr>
					<td>Location:</td>
					<td width=45%><s:textfield name="workingEvent.conLocation" size="60" /></td>
				</tr>
				<tr>
					<td>Start Date:<br>yyyy-mm-dd</td>
					<td width=45%>
					     <s:textfield name="constart"/>
					</td>
				</tr>
				<tr>
					<td>End Date:<br>yyyy-mm-dd</td>
					<td width=45%>
						<s:textfield name="conend"/>
					</td>
				</tr>
				<tr>
					<td>Expected attendance / Cap</td>
					<td width=45%>
						<s:textfield name="workingEvent.conCap"/>
					</td>
				<tr>
					<td>Description / Tagline / Comment:</td>
					<td width=45%><s:textfield name="workingEvent.conComment" size="60" /></td>
				</tr>
				<tr>
					<td>Website URL:</td>
					<td width=45%><s:textfield name="workingEvent.conWebsite" size="60" /></td>
				</tr>
				<tr>
					<td>Contact Email:</td>
					<td width=45%><s:textfield name="workingEvent.conEmail" size="60" /></td>
				</tr>
				<tr valign="top">
					<td>Description Block:<br>May be as detailed and HTML-full as needed</td>
					<td width=45%>
						<s:textarea name="workingEvent.conDescription" rows="5" cols="60"/>
					</td>
				</tr>
				<tr valign="top">
					<td>Stylesheet:</td>
					<td width=45%>
						<s:textarea name="workingEvent.conStylesheet" rows="5" cols="60"/>
					</td>
					<td width="40%">
						Stylesheet styles:<br>
						body, footer, buttonbar, title, comment, topbox, description, dates, div.inputform, table.inputform 
					</td>
				</tr>
				<tr valign="top">
					<td>Badge Layout:<br>Define how a badge will be printed.  See docs!</td>
					<td width=45%>
						<s:textarea id="badge" name="workingEvent.conBadgelayout" rows="10" cols="60"/>
					</td>
					<td width="40%">
						<a name="#badge"/>
						Badge layout is defined using XML.  Example:<br>
						&lt;badge&gt;<br>
							&lt;image filename="/tmp/foo.tif" posx="0" posy="10" height="50" width="150"/&gt;<br>
						
							&lt;field name="lname" posx="120" posy="100"/&gt;<br>
							&lt;field name="fname" posx="122" posy="100"/&gt;<br>
							
							&lt;field name="badgename" posx="120" posy="75" fontsize="24" align="block" width="40" height="30"/&gt;<br>
							&lt;field name="company" posx="120" posy="50" fontsize="12"/&gt;<br>
							&lt;field name="id" posx="5" posy="5" fontsize="8"/&gt;<br>
							&lt;field name="lnamefname" posx="35" posy="5" align="right"/&gt;<br>
							&lt;field name="regtype" posx="210" posy="135"/&gt;<br>
						&lt;/badge&gt;<br>
						Click <a href="#badge" onclick="badgeReset()">here</a> to reset to a default layout.<br>
						(You can also use 'fnbnln' as a field name for the badgename to be in quotes between fn and ln)
					</td>
				</tr>
			</table>
			<button type="submit" name="saveButton" value="Save" class="button"><u>S</u>ave</button>
			<button type="submit" name="cancelButton" value="Cancel" class="button">Cancel</button>
		</div>
		
		<jsp:include page="PageFooter.jsp" />
		
	</div>
</div>
</s:form>
<script language="JavaScript">
<!--

	/* Find the current menu pick, and make sure its class is right... */
	document.getElementById("maintenanceMenuPick").setAttribute("class","active");
	document.getElementById("maintenanceMenuText").setAttribute("class","active");
	
	/* Reset the contents of the badge layout to some basic default */
	function badgeReset() {
		s = "<badge>\n";
 		s += '\t<field name="lname posx="120" posy="100"/>\n';
 		s += '\t<field name="fname" posx="122" posy="100"/>\n';
 		s += '\t<field name="badgename" posx="120" posy="75" fontsize="18" align="block" width="40" height="30"/>\n';
 		s += '\t<field name="company" posx="120" posy="50" fontsize="12"/>\n';
 		s += '\t<field name="id" posx="5" posy="5" fontsize="8"/>\n';
 		s += '\t<field name="lnamefname" posx="35" posy="5" align="right"/>\n';
 		s += '\t<field name="regtype" posx="210" posy="135"/>\n';
 		s += '</badge>\n';
		document.getElementById('badge').value=s;
	}
	
//-->
</script>
</body>
</html>