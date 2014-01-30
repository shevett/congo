<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<div id="sidebar">
	<ul>
		<li id="detailSide"><a href="gotoUser">Details</a></li>
		<li id="historySide"><s:a href="ShowHistory" accesskey="Y">Histor<u>y</u></s:a></li>
		<li id="propertySide"><s:a href="EditPropertiesForm.action" accesskey="t">Proper<u>t</u>ies</s:a></li>
		<li id="invoiceSide">
			<s:url id="invoiceURL" action="coconut/invoiceBrowse">
				<s:param name="rid">${workingregistrant.rid}</s:param>
			</s:url>
			<s:a href="%{invoiceURL}" accesskey="I" ><u>I</u>nvoices</s:a>
		</li> 
		<li id="notesSide"><s:a href="showNotes">Notes
		<c:if test="${workingregistrant.totalNotes > 0 }">
			( ${workingregistrant.totalNotes} )
		</c:if>
		</s:a>
		</li>
		<li id="linksSide"><s:a href="showLinks" accesskey="K" >Lin<u>k</u>s</s:a></li>
	</td>
		
	</ul>
</div>
