<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions"	%>
<c:choose>
	
	
	<c:when test= "${activeType eq 'REVISE_BOM' }">
		<!-- ECO 도면/BOM -->
		<jsp:include page="${e3ps:getIncludeURLString('/change/include_view_ReviseBom')}" flush="true">
			<jsp:param name="oid" value="${oid}"/>
		</jsp:include>
	</c:when>
</c:choose>
