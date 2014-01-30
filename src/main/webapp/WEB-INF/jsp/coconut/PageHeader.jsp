<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="mvn" uri="http://alchemy.grimoire.ca/taglibs/mvn" %>

<div id="header">
	<div style="float:right; color:#ffffff; font-size: 12px; text-align: right; margin-right: 10px; margin-top: 5px; margin-bottom: 5px;">
		Event #${conference.conCID} : ${conference.conName}<br>
		${conference.conLocation}<br>&nbsp;<br>
		S:${conference.numSubscribed} /
		R:${conference.numRegistered} /
		B:${conference.numBadged} /
		C:${conference.numCheckedin}
	</div>
	<img src="./graphics/PalmTreeIcon.gif" align="left" valign="top">
	<h1>CONGO&#0153; Coconut</h1>
</div>
	
<div id="nav">
	<div style="float:right; text-align: right; color:#ffffff; margin-top: 8px; margin-right: 10px; ">
		<c:if test="${ not empty sessionScope.workinginvoice }">
			<a href="invoiceSelect.action?id=${workinginvoice.id}">Active invoice: # ${workinginvoice.id} : $ ${workinginvoice.amount}
			</a>
			<a href="invoiceDiscard.action" accesskey="Z">[X]</a>
		</c:if>
	</div>
	<ul>
		<li id="homeMenuPick"><a id="homeMenuText" href="Home" accesskey="H"><u>H</u>ome</a></li>
		<li id="lookupMenuPick"><a id="lookupMenuText" href="LookupRegistrantForm" accesskey="S"><u>S</u>earch</a></li>
		<li id="registrantMenuPick"><a id="registrantMenuText" href="gotoUser" accesskey="R"><u>R</u>egistrant</a></li>
		<li id="browseMenuPick"><a id="browseMenuText" href="processBrowse" accesskey="B"><u>B</u>rowse</a></li>
		<li id="createMenuPick"><a id="createMenuText" href="CreateRegistrantForm" accesskey="A">Cre<u>a</u>te</a></li>
		<li id="reportMenuPick"><a id="reportMenuText" href="ReportsMenu">Reports</a></li>
		<li id="invoiceMenuPick"><a id="invoiceMenuText" href="invoiceSelect" accesskey="I"><u>I</u>nvoices</a></li>
		<li id="schedulingMenuPick"><a id="schedulingMenuText" href="schedulingBrowse" accesskey="I">Scheduling</a></li>
		<c:if test="${session.userType == 'Administrator' }">
			<li id="maintenanceMenuPick"><a id="maintenanceMenuText" href="gotoMaintenance" acceskey="M"><u>M</u>aintenance</a></li>
		</c:if>
		<li id="logoutMenuPick"><a id="logoutMenuText" href="LogoutForm" accesskey="O">L<u>o</u>g out</a></li>
	</ul>
</div>

	
