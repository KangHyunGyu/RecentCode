<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	//팝업 리사이즈
	popupResize();
});
</script>

<!-- 부품 -->
<jsp:include page="${e3ps:getIncludeURLString('/part/include_relatedPart')}" flush="true">
	<jsp:param name="oid" value="${oid}"/>
	<jsp:param name="rowCheck" value="true"/>
	<jsp:param name="gridHeight" value="300"/>
</jsp:include>
<!-- 도면 현황	 -->
<jsp:include page="${e3ps:getIncludeURLString('/distribute/include_relatedTempEpmList')}" flush="true">
	<jsp:param name="oid" value="${oid}"/>
	<jsp:param name="gridHeight" value="500"/>
</jsp:include>