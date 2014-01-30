<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="css/styles.css"/>" rel="stylesheet" type="text/css"/>
	<title>Show Registrant</title>
</head>
<body>
<div class="wrapper">
	<jsp:include page="PageHeader.jsp" />
	
	<jsp:include page="RegistrantSubNav.jsp" />

	<div id="content_wrapper">
	
		<jsp:include page="RegistrantHeader.jsp" />


		<div id="primary_content">

			<c:if test='${workingregistrant.mergedTo > 0}'>
				<div class="proplist">
					This registrant has been merged into registrant <a href="gotoUser?rid=${workingregistrant.mergedTo}">${workingregistrant.mergedTo}</a>.<br/>&nbsp;
				</div>
			</c:if>

			<div class="proplist">
				<c:forEach var="rows" items="${propertyList}"> 
				<c:if test="${! rows.isdefault}">
				<c:choose>
					<c:when test="${rows.type == 'boolean'}">
						<c:if test='${rows.value == "1"}'>
							${rows.name}
						</c:if>
					</c:when>
					<c:when test="${rows.type == 'string'}">
						${rows.name}="${rows.value}"
					</c:when>
					<c:when test="${rows.type == 'numeric'}">
						${rows.name}=${rows.value}
					</c:when>
					<c:otherwise>
						${rows.name}="${rows.value}"
					</c:otherwise>
				</c:choose>
				</c:if>
				</c:forEach>
			</div>


			<table width="100%">
				<tr>
					<th>Category</th>
					<th>Type</th>
					<th>Data</th>
					<th>Primary?</th>
				</tr>
				<c:forEach var="rows" items="${workingregistrant.phoneList}">
					<tr>
						<td>
							Phone [
							<s:url id="phoneediturl" action="coconut/editPhone">
								<s:param name="rid">${rows.value.rid}</s:param>
								<s:param name="location">${rows.value.location}</s:param>
							</s:url>
							<s:a href="%{phoneediturl}">Edit</s:a> ]</td>
						<td>${rows.value.location}</td>
						<td>${rows.value.phone}</td>
						<td>${rows.value.primary}</td>
					</tr>
				</c:forEach>
				<c:forEach var="rows" items="${workingregistrant.addressList}">
					<tr>
						<td>Address [
						<s:url id="addressediturl" action="coconut/editAddress">
							<s:param name="rid">${rows.value.rid}</s:param>
							<s:param name="location">${rows.value.location}</s:param>
						</s:url>
						<s:a href="%{addressediturl}">Edit</s:a> ]</td>
						<td>
							${rows.value.location}
						</td>
						<td>
							${rows.value.line1} 
							${rows.value.line2},
							${rows.value.city},
							${rows.value.state}
							${rows.value.zipcode}
							${rows.value.country}
						</td>
						<td>${rows.value.primary}</td>
					</tr>
				</c:forEach>
				<c:forEach var="rows" items="${workingregistrant.emailList}">
					<tr>
						<td>Email [
							<s:url id="emailEditUrl" action="coconut/editEmail">
								<s:param name="rid">${rows.value.rid}</s:param>
								<s:param name="location">${rows.value.location}</s:param>
							</s:url>
						
						<s:a href="%{emailEditUrl}">Edit</s:a> ]</td>
						<td>${rows.value.location}</td>
						<td>${rows.value.address}</td>
						<td>${rows.value.primary}</td>
					</tr>
				</c:forEach>
				<tr>
			</table>




				<c:if test="${fn:length(workingregistrant.noteList) != 0}">
				<br />
				<table width="100%">
					<tr>
						<th>Function</th>
						<th>Operator</th>
						<th>Date</th>
						<th>Message</th>
						<th>Acked by</th>
						<th>on</th>
					</tr>
					<c:forEach var="rows" items="${workingregistrant.noteList}">
						<tr>
							<c:choose>
								<c:when test="${empty rows.value.acknowledgeRid || rows.value.acknowledgeRid == 0}">
									<td bgcolor="#ff8080">
										<s:form name="note" action="coconut/ackNote">
											<input type="hidden" name="noteid" value="${rows.value.id}">
											<input type="submit" name="Ok" value="Ok">
										</s:form>
									</td>
								</c:when>
								<c:otherwise>
									<td bgcolor="#80ff80">Done</td>
								</c:otherwise>
							</c:choose>
							<td>${rows.value.postRid }</td>
							<td>${rows.value.postDate}</td>
							<td>${rows.value.message}</td>
							<td>${rows.value.acknowledgeRid}</td>
							<td>${rows.value.acknowledgeDate}</td>
						</tr>
					</c:forEach>
				</table>
				</c:if>

			<br />
			<table width="80%">
				<tbody>
				<tr>
					<th>CID</th>
					<th>Name</th>
					<th>Subbed</th>
					<th>Regstd</th>
					<th>Badged</th>
					<th>Checked</th>
					<th>Regtype</th>
				</tr>
				</tbody>
				<tbody>
				<c:set var="rowCounter" value="0"/>
				<c:forEach var="rows" items="${workingregistrant.conList}">
					<c:if test='${rowCounter == 5}'>
						</tbody>
						<tbody id="extendedtable" style="display: none; width: 80%">
					</c:if>
					<c:set var="rowCounter" value='${rowCounter + 1}'/>
					<tr>
						<c:choose>
							<c:when test="${rows.value.cid == conference.conCID}">
								<td>${rows.value.cid}</td>
								<td>${rows.value.eventname}</td>
								<td>${subscribedText}</td>
								<td>${registeredText}</td>
								<td>${badgedText}</td>
								<td>${checkedinText}</td>
								<td>${rows.value.regtype}</td>	
							</c:when>
							<c:otherwise>
								<td>${rows.value.cid}</td>
								<td>${rows.value.eventname}</td>
								<td>${rows.value.subscribedText}</td>
								<td>${rows.value.registeredText}</td>
								<td>${rows.value.badgedText}</td>
								<td>${rows.value.checkedinText}</td>
								<td>${rows.value.regtype}</td>	
							</c:otherwise>
						</c:choose>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			<div style="float: right">
				[<a href="javascript:expand()"> + </a>/<a href="javascript:collapse()"> - </a>]
			</div>
		
			<div class="message">
				${message}
			</div>
		
	
		</div> <!-- end primary content -->
		
		<jsp:include page="RegistrantSidebar.jsp" />
	
		<jsp:include page="PageFooter.jsp" />
		
	</div><!-- end content wrapper -->
	
</div><!-- end wrapper -->

<script language="JavaScript">
<!--
/* Find the current menu pick, and make sure its class is right... */
document.getElementById("registrantMenuPick").setAttribute("class","active");
document.getElementById("registrantMenuText").setAttribute("class","active");
document.getElementById("detailSide").setAttribute("class","active");

	function expand() {
		document.getElementById('extendedtable').style.display = '';
	}
	
	function collapse() { 
		document.getElementById('extendedtable').style.display = 'none';
	}	
	
	
-->
</script>
</body>