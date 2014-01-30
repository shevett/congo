<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="<s:url value="/coconut/coconut.css"/>" rel="stylesheet"
	type="text/css" />
<title>Not configured</title>
</head>
<body>
<div class="menuboxes menuheader">Configure CONGO</div>

<s:form action="coconut/saveProperties">

<div class="menuboxes">
	<p>${config.schema.description}</p>

	<table>

	<c:forEach items="${config.schema.categories}" var="category">

		<tr><td colspan="3"><div class="menuboxes menuheader">${category.name}</div> </td></tr>
			<%-- This might want to be a taglib or otherwise factored down a bit. --%>
			<c:forEach items="${category.entries}" var="entry">
				<c:choose>
					<c:when test="${! empty config.current[entry.key]}">
						<c:set scope="page" var="currentValue" value="${config.current[entry.key]}" />
					</c:when>
					<c:otherwise>
						<c:set scope="page" var="currentValue" value="${entry.defaultValue}" />
					</c:otherwise>
				</c:choose>
				<tr>
					<td width="20%">${entry.name}</td>
					<td width="60%">${entry.description}</td>
					<td width="20%"><c:choose>
						<c:when test="${entry.type == 'text'}">
							<input type="text" name="${entry.key}"
								value="${currentValue}" />
						</c:when>
						<c:when test="${entry.type == 'password'}">
							<input type="password" name="${entry.key}"
								value="${currentValue}" />
						</c:when>
						<c:when test="${entry.type == 'select'}">
							<select name="${entry.key}">
								<c:forEach items="${entry.values}" var="value">
									<c:choose>
										<c:when test="${value.content == currentValue}">
											<option value="${value.content}" selected="selected">${value.label}</option>
										</c:when>
										<c:otherwise>
											<option value="${value.content}">${value.label}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
						</c:when>
					</c:choose></td>
				</tr>
			</c:forEach>
		</c:forEach>
	</table>
</div>

<div class="menuboxes">
	<s:submit name="savebutton" value="[S]ave Settings" accesskey="S"/>
	<s:submit name="cancelbutton" value="E[x]it without Saving" accesskey="X"/>
</div>
</s:form>
</body>
</html>