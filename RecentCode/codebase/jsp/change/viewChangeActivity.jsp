<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<c:if test="${activeType eq 'REVISE_BOM'}">
<jsp:include page="${e3ps:getIncludeURLString('/change/viewECA')}" flush="true">
	<jsp:param name="oid" value="${oid}"/>
	<jsp:param name="isEdit" value="false"/>
</jsp:include>
</c:if>
<c:if test="${activeType eq 'DRAWING'}">
<jsp:include page="${e3ps:getIncludeURLString('/change/include_relatedEpm')}" flush="true">
	<jsp:param name="oid" value="${oid}"/>
	<jsp:param name="isEdit" value="false"/>
</jsp:include>
</c:if>
<c:if test="${activeType eq 'DOCUMENT'}">
<jsp:include page="${e3ps:getIncludeURLString('/change/include_relatedDoc')}" flush="true">
	<jsp:param name="oid" value="${oid}"/>
	<jsp:param name="isEdit" value="false"/>
</jsp:include>
</c:if>
<c:if test="${activeType eq 'COMMON'}">
<jsp:include page="${e3ps:getIncludeURLString('/change/include_relatedCommon')}" flush="true">
	<jsp:param name="oid" value="${oid}"/>
	<jsp:param name="isEdit" value="false"/>
</jsp:include>
</c:if>
<%-- <jsp:include page="/jsp/change/include/ViewActivityState_include.jsp" flush="true"/> --%>
<%-- <jsp:include page="/jsp/change/include/ViewActivityOutput_include.jsp" flush="true"> --%>
<%-- <jsp:param name="oid"  value="${oid}"/> --%>
<%-- <jsp:param name="viewType"  value="NoEdit"/> --%>
<%-- </jsp:include> --%>

<!-- <br> -->

<%-- <center><input type="button" class="button01" value="닫기" onclick="self.close()"></center> --%>