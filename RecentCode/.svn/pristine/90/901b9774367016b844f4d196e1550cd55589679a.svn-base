<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>

$(document).ready(function(){
	
	
});


</script>

<br>
<!-- 관련 품목 -->
<jsp:include page="${e3ps:getIncludeURLString('/change/include_relatedECOPart')}" flush="true">
	<jsp:param name="oid" value="${eca.orderOid}"/>
	<jsp:param name="ecaOid" value="${eca.oid}"/>
	<jsp:param name="gridHeight" value="200"/>
</jsp:include>
<br>
<!-- 관련 도면 -->
<jsp:include page="${e3ps:getIncludeURLString('/change/include_relatedECOEPM')}" flush="true">
	<jsp:param name="oid" value="${eca.orderOid}"/>
	<jsp:param name="gridHeight" value="200"/>
</jsp:include>
<!-- //pro_table -->
