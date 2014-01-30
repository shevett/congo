<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="sidebar">
	<div id="primary_content">
		<p># ${coconutuser.rid} - ${coconutuser.firstName}	${coconutuser.lastName}	<br>
		(${session.userType})
		</p>
	</div>

	<p>
		<mvn:version groupId="com.stonekeep.congo" artifactId="congo" />
	</p>
</div>
