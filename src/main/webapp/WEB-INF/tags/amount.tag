<%@ tag body-content="empty" %>
<%@ attribute name="value" required="true" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:formatNumber value="${empty value ? 0.0 : value}" type="currency"/>