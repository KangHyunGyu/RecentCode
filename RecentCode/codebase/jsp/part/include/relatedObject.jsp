<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- 주도면 -->
<jsp:include page="${e3ps:getIncludeURLString('/epm/include_mainEpm')}" flush="true">
	<jsp:param name="oid" value="${oid}"/>
</jsp:include>

<!-- 참조 항목 2D-->
<jsp:include page="${e3ps:getIncludeURLString('/epm/include_referenceBy')}" flush="true">
	<jsp:param name="oid" value="${epmOid}"/>
</jsp:include>

<!-- 참조-->
<%-- <jsp:include page="${e3ps:getIncludeURLString('/epm/include_reference')}" flush="true">
	<jsp:param name="oid" value="${epmOid}"/>
</jsp:include> --%>

<!-- 관련 도면-->
<%-- <jsp:include page="${e3ps:getIncludeURLString('/epm/include_relatedEpm')}" flush="true">
	<jsp:param name="oid" value="${oid}"/>
</jsp:include> --%>

<!-- 관련 배포 -->
<%-- <jsp:include page="${e3ps:getIncludeURLString('/distribute/include_relatedDistribute')}" flush="true">
	<jsp:param name="oid" value="${oid}"/>
</jsp:include> --%>

<!-- 관련 ECO -->
<jsp:include page="${e3ps:getIncludeURLString('/change/include_relatedECO')}" flush="true">
	<jsp:param name="oid" value="${oid}"/>
</jsp:include>

<!-- 과련 프로젝트 -->
<jsp:include page="${e3ps:getIncludeURLString('/project/include_relatedProject')}" flush="true">
	<jsp:param name="oid" value="${oid}"/>
</jsp:include>

<!-- 관련 문서 -->
<jsp:include page="${e3ps:getIncludeURLString('/doc/include_relatedDoc')}" flush="true">
	<jsp:param name="oid" value="${oid}"/>
</jsp:include>