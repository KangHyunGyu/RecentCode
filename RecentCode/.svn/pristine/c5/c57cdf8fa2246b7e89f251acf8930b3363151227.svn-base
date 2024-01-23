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
<!-- BOM -->
<jsp:include page="${e3ps:getIncludeURLString('/erp/include_relatedBOM_ecn')}" flush="true">
	<jsp:param name="instance_id" value="${instance_id}"/>
	<jsp:param name="ecr_number" value="${ecr_number}"/>
	<jsp:param name="pjt_number" value="${pjt_number}"/>
	<jsp:param name="gridHeight" value="200"/>
</jsp:include>
<!-- BOM Change -->
<jsp:include page="${e3ps:getIncludeURLString('/erp/include_relatedBOMChange')}" flush="true">
	<jsp:param name="instance_id" value="${instance_id}"/>
	<jsp:param name="gridHeight" value="200"/>
</jsp:include>