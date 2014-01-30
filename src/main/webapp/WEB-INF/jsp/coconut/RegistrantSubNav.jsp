<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="subnav">
	<ul>
		<li id="previewSubPick"><s:a id="previewSubText" href="previewEmail" accesskey="L">Mai<u>l</u>...</s:a></li>
		<li id="deleteSubPick"><s:a id="deleteSubText" href="DeleteRegistrantForm.action" accesskey="D"><u>D</u>elete</s:a></li>
		<li id="mergeSubPick"><s:a id="mergeSubText" href="MergeRegistrantForm">Merge...</s:a></li>
		<li id="repairSubPick"><s:a id="repairSubText" href="repairRegistrant">Repair</s:a></li>
		<li id="editSubPick"><s:a id="editSubText" href="EditRegistrantForm.action" accesskey="E"><u>E</u>dit</s:a></li>
		<s:url id="addPhoneUrl" action="coconut/addPhone">
			<s:param name="rid">${workingregistrant.rid}</s:param>
		</s:url>
		<li id="addEmailSubPick"><s:a id="addEmailSubText" href="%{addPhoneUrl}">Add Phone</s:a>
		<s:url id="addEmailUrl" action="coconut/addEmail">
			<s:param name="rid">${workingregistrant.rid}</s:param>
		</s:url>
		<li id="addEmailSubPick"><s:a id="addEmailSubText" href="%{addEmailUrl}">Add Email</s:a>
		<s:url id="addAddressUrl" action="coconut/addAddress">
			<s:param name="rid">${workingregistrant.rid}</s:param>
		</s:url>
		<li id="addAddressSubPick"><s:a id="addAddressSubText" href="%{addAddressUrl}">Add Address</s:a>
		
	</ul>
</div>