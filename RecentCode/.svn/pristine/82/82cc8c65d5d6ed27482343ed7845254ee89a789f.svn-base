<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!-- 변경대상 부품 -->


<!-- 변경결과  도면-->
<%-- <c:if test="${fn:contains(oid, 'EChangeOrder2')}"> --%>
<c:choose>
	<c:when test= "${type eq 'ECR' }">
<%-- 		<jsp:include page="${e3ps:getIncludeURLString('/change/include_relatedPart')}" flush="true"> --%>
<%-- 			<jsp:param name="oid" value="${oid}"/> --%>
<%-- 			<jsp:param name="title" value="${e3ps:getMessage('부품')}"/> --%>
<%-- 		</jsp:include> --%>
		<jsp:include page="${e3ps:getIncludeURLString('/change/include_relatedECO')}" flush="true">
			<jsp:param name="oid" value="${oid}"/>
			<jsp:param name="title" value="${e3ps:getMessage('관련 ECO')}"/>
			<jsp:param name="isEdit" value="false"/>
		</jsp:include>
	</c:when>
	<c:when test= "${type eq 'ECO' }">
		<!-- 관련 품목 -->
		<jsp:include page="${e3ps:getIncludeURLString('/change/include_relatedECOPart')}" flush="true">
			<jsp:param name="oid" value="${oid}"/>
			<jsp:param name="ecaOid" value="${ecaOid}"/>
			<jsp:param name="gridHeight" value="200"/>
		</jsp:include>
		<br>
		<!-- 관련 도면 -->
		<jsp:include page="${e3ps:getIncludeURLString('/change/include_relatedECOEPM')}" flush="true">
			<jsp:param name="oid" value="${oid}"/>
			<jsp:param name="gridHeight" value="200"/>
		</jsp:include>
		
		<jsp:include page="${e3ps:getIncludeURLString('/change/include_relatedECR')}" flush="true">
			<jsp:param name="oid" value="${oid}"/>
			<jsp:param name="title" value="${e3ps:getMessage('관련 ECR')}"/>
			<jsp:param name="isEdit" value="false"/>
		</jsp:include>
	</c:when>
</c:choose>
<%-- </c:if> --%>
<!-- 프로젝트-->
<%-- <jsp:include page="${e3ps:getIncludeURLString('/project/include_relatedProject')}" flush="true">
	<jsp:param name="oid" value="${oid}"/>
</jsp:include> --%>