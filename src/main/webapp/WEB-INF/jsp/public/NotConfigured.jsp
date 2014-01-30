<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome to CONGO</title>
<LINK REL="StyleSheet" href="getConfiguredStylesheet.action" type="text/css">
</head>
<body>

<div class='box-green'>
<div class='boxtop-green'>
<div></div>
</div>
<div class='boxcontent-green'></div>
<div class='boxbottom-green'>
<div></div>
</div>
</div>

<div class='box-orange'>
<div class='boxtop-orange'>
<div></div>
</div>
<div class='boxcontent-orange'><s:url id="configureUrl"
	action="coconut/configure" />
<p><strong>Welcome to CONGO!</strong> </p>

<p>This CONGO installation is not
yet set up, or has been upgraded and needs to be reconfigured.</p>
</div>
<div class='boxbottom-orange'>
<div></div>
</div>
</div>

<jsp:include page="PageFooter.jsp" />


</body>
</html>