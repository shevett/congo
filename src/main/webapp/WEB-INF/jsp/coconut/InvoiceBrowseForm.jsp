<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Browse Invoices</title>
</head>

<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp"></jsp:include>
	
	<div id="content_wrapper">
		<c:choose>
			<c:when test="${rid > 0}">
				<jsp:include page="RegistrantHeader.jsp" />
				<div id="primary_content">
			</c:when>
			<c:otherwise>
				<div id="primary_content_wide">
			</c:otherwise>
		</c:choose>
			<form method="post" action="invoiceSelect">
			
			<c:if test="${skip > 0}">
				<button name="skip" type="submit" value="${skip - 50}" accesskey="P">
					<u>P</u>revious
				</button>
			</c:if>
			(showing ${skip} - ${skip + 50} results)
			<button name="skip" type="submit" value="${skip + 50}" accesskey="N">
				<u>N</u>ext
			</button>
			
			&nbsp;
			<label>Void?</label>
			<s:checkbox name="includevoid"/>
				
			&nbsp;
			<label>Paid?</label>
			<s:checkbox name="includepaid"/>
			
			&nbsp;
			<label>Ready?</label>
			<s:checkbox name="includeready"/>
			
			&nbsp;
			<label>Search:</label>
			<s:textfield name="id"/>
			
			<button name="refresh" type="submit">
				Go
			</button>
			
			<strong>${message}</strong>
		
			<table>
				<tr>
					<th>Id</th>
					<th>Stat</th>
					<th>Activity</th>
					<th>Creator</th>
					<th>Op</th>
					<th>Amt</th>
					<th>PayType</th>
					<th>Paydate</th>
					<!-- <th>Authcode</th> -->
					<th>Comment</th>
					<th>Post</th>
				</tr>
				<c:forEach items="${invoiceList}" var="rows">
					<c:set var="cellColor">
						<c:choose>
							<c:when test="${cellColor == '#f0f0f0'}">#ffffcc</c:when>
							<c:otherwise>#f0f0f0</c:otherwise>
			 			</c:choose>
					</c:set>
					<tr bgcolor="${cellColor}">
						<td><a href="invoiceSelect.action?id=${rows.id}">${rows.id}</a></td>
						<td>${rows.status}</td>
						<td>${rows.activity}</td>
						<td>${rows.creator} ${rows.creatorName}</td>
						<td>${rows.operator}</td>
						<td><fmt:formatNumber value="${rows.amount}" type="currency"/></td>
						<td>${rows.paytype}</td>
						<td>${rows.paydate}</td>
						<!-- <td>&nbsp;</td> -->
						<td>${rows.comment}</td>
						<td align="center">
							<c:if test="${rows.postcount > 0}">
								${rows.postcount}
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div> <!-- end primary_content -->
		
		<c:if test="${rid > 0}">
			<jsp:include page="RegistrantSidebar.jsp" />
		</c:if>
		<jsp:include page="PageFooter.jsp"></jsp:include>

	</div> <!-- end content-wrapper -->
</div> <!-- end wrapper -->

</form>

<script language="JavaScript">
<!--

/* Find the current menu pick, and make sure its class is right... */
document.getElementById("invoiceMenuPick").setAttribute("class","active");
document.getElementById("invoiceMenuText").setAttribute("class","active");
document.getElementById("invoiceSide").setAttribute("class","active");


//-->
</script>
</body>
</html>