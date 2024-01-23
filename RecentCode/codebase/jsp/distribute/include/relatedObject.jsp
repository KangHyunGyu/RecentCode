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
<!--
	관련 구매요청 리스트 화면 shjeong 2022.12.08
	파라미터 type 설명 : 각각의 화면에 따라 create, modify, view 의 value 를 가지며 value에 따라 호출하는 function 및 그리드 화면이 달라짐
	type 의 값이 create, modify 의 경우 수주번호를 매개변수로 받는 purchase_byOrderNumber_getGridData 메소드를 호출해야한다.
-->
<%-- <jsp:include page="${e3ps:getIncludeURLString('/purchase/include_purchaseListByOrderNumber')}" flush="true">
	<jsp:param name="oid" value="${oid}"/>
	<jsp:param name="type" value="view"/>
	<jsp:param name="gridHeight" value="150"/>
	<jsp:param name="autoGridHeight" value="false"/>
</jsp:include> --%>

<%-- <jsp:include page="${e3ps:getIncludeURLString('/purchase/include_refProductPart')}" flush="true"> --%>
<%-- 	<jsp:param name="oid" value="${oid}"/> --%>
<%-- 	<jsp:param name="gridHeight" value="50"/> --%>
<%-- </jsp:include> --%>

<%-- <jsp:include page="${e3ps:getIncludeURLString('/purchase/include_refPurchasePartList')}" flush="true"> --%>
<%-- 	<jsp:param name="oid" value="${oid}"/> --%>
<%-- 	<jsp:param name="gridHeight" value="500"/> --%>
<%-- </jsp:include> --%>

<jsp:include page="${e3ps:getIncludeURLString('/part/include_relatedPart')}" flush="true">
	<jsp:param name="oid" value="${oid}"/>
</jsp:include>
<jsp:include page="${e3ps:getIncludeURLString('/project/include_relatedProject')}" flush="true">
	<jsp:param name="oid" value="${oid}"/>
</jsp:include>
