<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<s:url value="/coconut/coconut.css"/>" rel="stylesheet" type="text/css"/>
<title>Not configured</title>
</head>
<body>
	<div class="bodybox">
		<div class="menuboxes menuheader">
			Not configured
		</div>

		<div class="menuboxes">
			<p>
			<b>Welcome to your brand new installation of CONGO</b><br>

			<p>Congratulations!  You have successfully installed CONGO.  The next step is giving CONGO enough
			information to allow it to find your database.</p>

			<p>To proceed, you must create a $HOME/.congo/congo.properties file.</p>			

			<p>Here is a sample congo.properties file:</p>
			<blockquote><pre>
	preferredcid=2008
	database.type=mysql
	database.user=root
	database.host=localhost
	database.name=congo
			</pre></blockquote>

			<p>Once this file is in place, click <a href="showloginpage.action">here</a> to continue.  (note that
			on some installations, Tomcat may need to be restarted in order for CONGO to be able to see the file).</p>
		</div>
	</div>
</body> 
</html>