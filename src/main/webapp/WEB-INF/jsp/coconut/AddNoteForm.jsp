<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Notes</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp" />
	
	<jsp:include page="RegistrantSubNav.jsp" />

	<div id="content_wrapper">
	
		<jsp:include page="RegistrantHeader.jsp" />

		<div id="primary_content">
		<s:form name="note" action="coconut/addNote">
  		<table>
  		<tr><td>Text of note:</td><td><s:textfield name="note" size="80"/></td></tr>
  		<tr><td>Login note?</td><td><s:checkbox name="loginnote"/> (Check this box to have this note show up as an 'alert' when an operator looks up this registrant)</td></tr>
	</table>
	<input type="submit" name="notebutton" value="Add note" class="button">
	</s:form>
	
	<hr>
	
	<b>Note History</b>

	<table>
		<tr>
			<th>&nbsp;</th>
			<th>CID</th>
			<th>PostDate</th>
			<th>Posted by</th>
			<th>Ackdate</th>
			<th>Acked by</th>
			<th>Message</th>
			<th>Note Type</th>
		</tr>
			<c:forEach var="rows" items="${noteList}"> 
				<tr>
					<s:url id="deleteurl" action="coconut/deleteNote" >
						<s:param name="noteid">${rows.value.id}</s:param>
					</s:url>
					<td>
						<s:a href="%{deleteurl}"><img src="graphics/eventdelete.png" border=0 height="15" width="15"></s:a>
					</td>
					<td>${rows.value.cid}
					<td>${rows.value.postDate}</td>
					<td>${rows.value.postRid}</td>
					<td>${rows.value.acknowledgeDate}</td>
					<td>${rows.value.acknowledgeRid}</td>
					<td>${rows.value.message}</td>
					<td>${rows.value.type}</td>
				</tr>
			</c:forEach>
	</table>
		</div>
		<jsp:include page="RegistrantSidebar.jsp" />
	
		<jsp:include page="PageFooter.jsp" />
		
	</div>
</div>
<script language="JavaScript">
<!--
document.getElementById("registrantMenuPick").setAttribute("class","active");
document.getElementById("registrantMenuText").setAttribute("class","active");	
document.getElementById("notesSide").setAttribute("class","active");
//-->
</script>
</body>
</html>