<%@ tag body-content="empty"%>
<%@ attribute name="account" required="true" type="com.cit.venulum.domain.entity.Account" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="v" tagdir="/WEB-INF/tags" %>

<h3><s:text name="account.referredAccounts"/></h3>
<c:choose>
	<c:when test="${empty account.referredAccounts}">
		<div class="message">This account has not referred any accounts</div>
	</c:when>
	<c:otherwise>
		<div class="message">This account has referred the following account(s)</div>
		<table class="sortable">
			<thead>
				<th>Account Id</th>
				<th>First Name</th>
				<th>Last Name</th>
				<th>Create Date</th>						
			</thead>
			<tbody>
				<c:forEach items="${account.referredAccounts}" var="referree">
					<tr>
						<td>${referree.id}</td>
						<td>${referree.firstName}</td>
						<td>${referree.lastName}</td>
						<td><v:date value="${referree.createDateTime}" type="date"/></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:otherwise>
</c:choose>