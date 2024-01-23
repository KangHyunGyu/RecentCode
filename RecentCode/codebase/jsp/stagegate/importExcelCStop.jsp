<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>

<!-- HTML -->
<link rel="stylesheet" type="text/css" href="/Windchill/jsp/stagegate/stagegateHTML/css/custom.css">
<link rel="stylesheet" type="text/css" href="/Windchill/jsp/stagegate/stagegateHTML/css/style.css">
<!-- AXISJ Component -->
<link rel="stylesheet" type="text/css" href="/Windchill/jsp/component/axisj/ui/bulldog/AXJ.min.css" id="axu-theme-axisj" />
<script type="text/javascript" src="/Windchill/jsp/component/axisj/dist/AXJ.all.js"></script>

<script type=text/javascript>
$(document).ready(function(){
	
});
let importExcel = (e) => {
	console.log("importCStop");
	let url = getURLString("/gate/importExcelCStopAction");
	let param = getFormParams("importCStop", null);
    ajaxCallServer(url, param, function(data){
    	let onTab = opener.document.querySelector("div .tap>ul>li.on");
    	opener.loadIncludePage(onTab);
    	window.close();
	}, true);
}
</script>
<body class="xhidden">
<div class="pop5">
<form method="POST" id="importCStop" name="importCStop">
	<input type="hidden" name="cOid" value="${cOid}">
	<input type="hidden" name="cObjType" value="${cObjType}">
	<!-- top -->
	<div class="top">
		<h2>
			Import C_Stop
		</h2>

		<span class="close">
			<a class="closeBtn" onclick="self.close();">
				<img src="/Windchill/jsp/portal/images/colse_bt.png">
			</a>
		</span>
	</div>
	<!-- //top -->
	<div class="pb5 pr25 pl25">
		<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
		    <jsp:param name="formId" value="importCStop"/>
		    <jsp:param name="command" value="insert"/>
		    <jsp:param name="btnId" value="createBtn" />
		    <jsp:param name="type" value="PRIMARY" />
	    </jsp:include>
		<div class="pt5 pb5">
			<input type="button" class="sm_bt03 bigBottomBtn" value="import" onclick="javascript:importExcel()"/>
		</div>
    </div>
</form>
</div>
</body>

