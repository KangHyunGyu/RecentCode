<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- ENDITEM -->
<jsp:include page="${e3ps:getIncludeURLString('/bom/include_bomPartList')}" flush="true">
	<jsp:param name="oid" value="${oid}"/>
	<jsp:param name="type" value="end"/>
</jsp:include>

<!-- 상위 부품-->
<jsp:include page="${e3ps:getIncludeURLString('/bom/include_bomPartList')}" flush="true">
	<jsp:param name="oid" value="${oid}"/>
	<jsp:param name="type" value="up"/>
</jsp:include>

<!-- 하위 부품-->
<jsp:include page="${e3ps:getIncludeURLString('/bom/include_bomPartList')}" flush="true">
	<jsp:param name="oid" value="${oid}"/>
	<jsp:param name="type" value="down"/>
</jsp:include>