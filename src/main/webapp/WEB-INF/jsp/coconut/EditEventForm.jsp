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
			<table id="eventTable">
				<tr>
					<td>Name:</td>
					<td width=45%><s:textfield name="workingEvent.conName" size="80" /></td>
				</tr>
				<tr>
					<td>Location:</td>
					<td width=45%><s:textfield name="workingEvent.conLocation" size="80" /></td>
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
					<td width=45%><s:textfield name="workingEvent.conComment" size="80" /></td>
				</tr>
				<tr>
					<td>Website URL:</td>
					<td width=45%><s:textfield name="workingEvent.conWebsite" size="80" /></td>
				</tr>
				<tr>
					<td>Contact Email:</td>
					<td width=45%><s:textfield name="workingEvent.conEmail" size="80" /></td>
				</tr>
				<tr valign="top">
					<td>Description Block:<br>May be as detailed and HTML-full as needed</td>
					<td width=45%>
						<s:textarea name="workingEvent.conDescription" rows="10" cols="80"/>
					</td>
				</tr>
				<tr valign="top">
					<td>Stylesheet:</td>
					<td width=45%>
						<s:textarea name="workingEvent.conStylesheet" rows="10" cols="80"/>
					</td>
					<td width="40%">
						Stylesheet styles:<br>
						body, footer, buttonbar, title, comment, topbox, description, dates, div.inputform, table.inputform 
					</td>
				</tr>
				<tr valign="top">
					<td>Badge Layout:<br>Define how a badge will be printed.  See docs!</td>
					<td width=45%>
						<s:textarea id="badge" name="workingEvent.conBadgelayout" rows="20" cols="80"/>
					</td>
					<td width="40%">
						<a name="#badge"></a>
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
						<p>Field Attributes:
							<ul>
								<li>"name" - sets the field type
									<ul>
										<li>"lname" - Last name</li>
										<li>"fname" - First name</li>
										<li>"fnamelname" - First Last</li>
										<li>"lnamefname" - Last, First</li>
										<li>"badgename" - Badgename field</li>
										<li>"company" - Company field</li>
										<li>"id" - User ID</li>
										<li>"regtype" - Registration Type</li>
										<li>"fnbnln" - First Badgename Last</li>
										<li>"badgeorfnln" - Badgename if it exists, else First Last</li>
										<li>"badgeorlnfn" - Badgename if it exists, else Last, First</li>
										<li>"fnlnifbadge" - First Last only if Badgename exists</li>
										<li>"fnlnifbadge" - Last, First only if Badgename exists</li>
									</ul>
								</li> 

								<li>"posx" - sets the field's horizontal position on the badge</li>
								<li>"posy" - sets the field's vertical position on the badge</li>
								<li>"maxwidth" - sets the field's maximum width (default / maximum 233)</li>
								<li>"fontsize" - sets default fontsize for the field.  If the field would be larger than maxwidth, the font is lowered in 0.25 pt increments until it fits</li>
								<li>"align" - sets alignment of the field: "left", "center", "right", "block"</li>
								<li>"width" - sets width for block alignment</li>
								<li>"height" - sets height for block alignment</li>
								<li>"style" - sets style for field: "bold", "italic", "bolditalic", "strikethrough", "underline"</li>
								<li>"font" - sets font for field:
									<ul>
										<li>"helvetica" - Use Helvetica</li>
										<li>"times" - Use Times New Roman</li>
										<li>"courier" - Use Courier New</li>
										<li>"/path/to/font/file" - use user-specified font, fall back to Helvetica if the path is invalid</li>
									</ul>
								</li>
							</ul>
						</p>
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
 		s += '\t<field name="lname" posx="120" posy="100"/>\n';
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
