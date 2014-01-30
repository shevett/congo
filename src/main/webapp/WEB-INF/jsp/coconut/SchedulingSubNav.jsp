<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="subnav">
	<ul>
		<li id="sessionSubPick"><s:a id="sessionSubPickText" href="schedulingBrowse">Sessions</s:a></li>
		<li id="venueSubPick"><s:a id="venueSubPickText" href="listVenues.action">Venues</s:a></li>
		<li id="roomSubPick"><s:a id="roomSubPickText" href="listRooms.action">Rooms</s:a></li>
		<li id="categorySubPick"><s:a id="categorySubPickText" href="listCategories.action">Categories</s:a></li>
		<li id="departmentSubPick"><s:a id="departmentSubPickText" href="listDepartments.action">Departments</s:a></li>
		<li id="layoutSubPick"><s:a id="layoutSubPickText" href="listLayouts.action">Layouts</s:a></li>
	</ul>
</div>