<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div id="floating_content">
	# ${workingregistrant.rid} : 
	${workingregistrant.lastName}, ${workingregistrant.firstName} 
	( ${workingregistrant.badgeName} ) 
	<c:if test='${fn:length(workingregistrant.company) > 1 }'>
		of ${workingregistrant.company} </b>
	</c:if>
	<c:if test='${! workingregistrant.enabled}'>
		<b>This record is disabled.</b>
	</c:if>
</div>