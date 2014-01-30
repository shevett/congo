<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Print Badge</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp" />
	
	
	<jsp:include page="RegistrantSubNav.jsp" />

	<div id="content_wrapper">
	
		<jsp:include page="RegistrantHeader.jsp" />

		<div id="primary_content">
			
			<s:form action="coconut/printBadge" method="get">
				<table>				
					<c:forEach items="${promptProperties}" var="p">
						<tr>
							<td>${p.name}</td>
							<td>
								<c:if test="${p.type == 'string'}">
									<input type="text" name="${p.name}" value="${p.value}">
								</c:if>
								
								<c:set var="checked" value=""/>
								<c:if test="${p.type == 'boolean'}">
									<c:if test="${p.value == '1'}">
										<c:set var="checked" value="checked"/>
									</c:if>
									<input type="checkbox" name="${p.name}" value="${p.value}" ${checked}/>
								</c:if>

							</td>
						</tr>
					</c:forEach>
					
				<tr><td colspan="2"><hr></td></tr>
						
				<tr>
					<td>Print bad<u>g</u>e</td>
					<td><s:checkbox name="printbadge" accesskey="G"></s:checkbox>
				<tr>
					<td>Mark as <u>C</u>hecked In</td>
					<td><s:checkbox name="checkin" accesskey="C"></s:checkbox></td>
				</tr>
				</table>
				<button class="button" type="submit" name="printbutton" value="print" accesskey="P"><u>P</u>rint</button>
				<button class="button" type="submit" name="printbutton" value="preview" accesskey="W">Previe<u>w</u></button>
				<button class="button" type="submit" name="printbutton" value="exit" accesskey="X">E<u>x</u>it</button>
			</s:form>
		</div> <!-- end primary_content -->
		<jsp:include page="PageFooter.jsp" />
	</div> <!-- end content_wrapper -->
</div> <!-- end wrapper -->
<script language="JavaScript">
<!--
/* Find the current menu pick, and make sure its class is right... */
document.getElementById("registrantMenuPick").setAttribute("class","active");
document.getElementById("registrantMenuText").setAttribute("class","active");	
-->
</script>

</body>
</html>
