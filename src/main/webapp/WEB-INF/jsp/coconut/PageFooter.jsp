<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="mvn" uri="http://alchemy.grimoire.ca/taglibs/mvn" %>

<div id="footer">
	CONGO v<mvn:version groupId="com.stonekeep.congo" artifactId="congo" />
	&copy;2015 Stonekeep Consulting, Inc.  All rights reserved<br/>
	<c:choose>
		<c:when test="${registered}">
			Enterprise Edition - Registered to ${keyname}
		</c:when>
		<c:otherwise>
			Unregistered Community Edition
		</c:otherwise>
	</c:choose>
</div>
