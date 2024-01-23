<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<%@page import="com.e3ps.common.util.TypeUtil"%>
<%
	// #. 기본변수
	String renderTo = "TreeDiv";
%>
<script src="/Windchill/jsp/js/jquery/jstree-3.0.9/dist/jstree.js"></script>
<script language="javascript">
var g_renderTo = "<%= renderTo %>";
/**
 * 트리 더블 클릭 핸들러
 */
function treeDoubleClickHandler() {
	var f = document.mainForm;
	
	// #. 선택된 노드 조회
	var selectedNodes = $("#" + g_renderTo).jstree(true).get_selected(true);
	var node = null;
	if (selectedNodes != null && selectedNodes.length > 0) {
		node = selectedNodes[0];
	}
	alert(f.callbackName.value);
	alert(f.callbackObj.value);
	// #. 콜백 수행
	var callbackName = f.callbackName.value;
	var callbackObj = null;
	if (f.callbackObj.value.length > 0) {
		callbackObj = JSON.parse(f.callbackObj.value);
	}
	if (callbackName != null && callbackName.length > 0) {
		execCallback(callbackName, node.original, callbackObj);
	}
	
	// #. 닫기
	window.self.close();
}

</script>

<!-- pop -->
<div class="pop3">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('폴더 검색')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->
	<div class="seach_arm2 pt10 pb5">
		<div class="leftbt"></div>
		<div class="rightbt"></div>
	</div>

	<div class="pl25 pr25">
		<form name="mainForm" method="post">
			<!-- opener callback 관련 -->
			<input type="hidden" name="callbackName" value="${param.callbackName}"/>
			<textarea style="display:none" name="callbackObj">${param.callbackObj}</textarea>

			<!-- tree -->
			<jsp:include page="${e3ps:getIncludeURLString('/common/include_folderTree')}" flush="true">
				<jsp:param name="container" value="worldex"/>
				<jsp:param name="renderTo" value="docFolder"/>
				<jsp:param name="formId" value="mainForm"/>
				<jsp:param name="rootLocation" value="/Default/Document"/>
				<jsp:param name="gridHeight" value="450"/>
				<jsp:param name="gridWidth" value="450"/>
			</jsp:include>
			<!-- //tree -->
			
		</form>
	</div>
</div>	
