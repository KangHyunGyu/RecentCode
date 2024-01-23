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

function fileUpload(){
	var param = new Object();
	$("#filePopup").attr("action", getURLString("/gate/fileUpload"));
	formSubmit("filePopup", param, "등록하시겠습니까?", null, true);
}
</script>
<body>
<div class="pop5">
<form method="POST" id="filePopup" name="filePopup">
	<input type="hidden" name="oid" value="${oid}">
	<!-- top -->
	<div class="top">
		<h2>
			${e3ps:getMessage('파일 목록')}
		</h2>

		<span class="close">
			<a class="closeBtn" onclick="self.close();">
				<img src="/Windchill/jsp/portal/images/colse_bt.png">
			</a>
		</span>
	</div>
	
	<div class="seach_arm pt20 pb5">
		<div class="leftbt">
		</div>
		<div class="rightbt">
			<input type="button" class="sm_bt03" value="${e3ps:getMessage('저장')}" onclick="javascript:fileUpload()"/>
		</div>
	</div>
	<!-- //top -->
	<div class="pb5 pr25 pl25">
		<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
           <jsp:param name="formId" value="filePopup"/>
           <jsp:param name="command" value="insert"/>
           <jsp:param name="btnId" value="createBtn" />
        </jsp:include>
          
    </div>
  
    
</form>
</div>
</body>

