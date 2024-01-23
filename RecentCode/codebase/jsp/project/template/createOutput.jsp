<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- pop -->
<div class="pop5">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('프로젝트 산출물 등록')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->

	<div class="pl25 pr25">
		<div class="seach_arm2 pt10 pb5">
			<div class="leftbt"></div>
			<div class="rightbt">
				<button type="button" class="s_bt03" onclick="javascript:addOutput()">${e3ps:getMessage('등록')}</button>
			</div>
		</div>
		<form name="addOutputForm" id="addOutputForm" method="post">
			<input type="hidden" id="location" name="location" value=""/>
			<input type="hidden" name="oid"  id="oid" value="${oid}" />
			<div class="pro_table">
				<table class="mainTable">
					<colgroup>
						<col style="width:20%">
						<col style="width:80%">
					</colgroup>	
					<tbody>
						<tr>
							<th>${e3ps:getMessage('이름')}</th>
							<td>
								<input class="w100" type="text" id="name" name="name">
							</td>
						</tr>
<!-- 						<tr> -->
<%-- 							<th>${e3ps:getMessage('산출물 인증타입')}</th> --%>
<!-- 							<td> -->
<!-- 								<select class="w10" id="outputType" name="outputType" onchange="selectOutputType(this);"> -->
<!-- 								</select> -->
<!-- 								<select class="mw10 hide" id="outputStep" name="outputStep" onchange="selectOutputStep(this)"> -->
<!-- 							 		<option value="">선택</option> -->
<!-- 								</select> -->
<!-- 								<select class="mw10 hide" id="outputChildStep" name="outputChildStep"> -->
<!-- 							 		<option value="">선택</option> -->
<!-- 								</select> -->
<!-- 								<br> -->
<%-- 								<font color=red>(${e3ps:getMessage('인증타입 미 선택시 일반 산출물로 등록 됩니다.')})</font> --%>
<!-- 							</td> -->
<!-- 						</tr> -->
						<tr> 
							<th>${e3ps:getMessage('분류')}</th>
							<td>
								<div style="position:relative">
									<input type="text" class="w50" id="locationDisplay" name="locationDisplay" value="/Document" disabled>
									<%-- <button class="s_bt03" onclick="javascript:openFolderTreePopup('E3PS','docFolder','addOutputForm','/Default/Document')">${e3ps:getMessage('분류 지정')}</button> --%>
									<button type="button" class="s_bt03" id="toggleBtn" onclick="toggleFolderList(this)">${e3ps:getMessage('분류 지정')}</button>
									<div id="folderList" style="display:none;position:absolute;z-index:999">
										<!-- tree -->
										<jsp:include page="${e3ps:getIncludeURLString('/common/include_folderTree')}" flush="true">
											<jsp:param name="container" value="worldex"/>
											<jsp:param name="renderTo" value="docFolder"/>
											<jsp:param name="formId" value="addOutputForm"/>
											<jsp:param name="rootLocation" value="/Default/Document"/>
											<jsp:param name="gridHeight" value="180"/>
										</jsp:include>
										<!-- //tree -->
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('설명')}</th>
							<td class="pt5">
								<div class="textarea_autoSize">
									<textarea name="description" id="description"></textarea>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</form>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	//lifecycle list
// 	getOutputTypeList();
});

function selectOutputType(select) {
	var key = $(select).val();

	if(key.length > 0 && key == "PSO") {
		$("#outputStep").show();
		getOutputStepList("outputStep", key);
	} else {
		$("#outputStep").hide();
		$("#outputStep").find("option").remove();
		$("#outputStep").append("<option value=''>선택</option>");
		
		$("#outputChildStep").hide();
		$("#outputChildStep").find("option").remove();
		$("#outputChildStep").append("<option value=''>선택</option>");
	}
}

function selectOutputStep(select) {
	var key = $("#outputType").val();
	var parentOid = $(select).val();
	
	if(parentOid.length > 0) {
		$("#outputChildStep").show();
		getOutputStepList("outputChildStep", key, parentOid);
	} else {
		$("#outputChildStep").hide();
		$("#outputChildStep").find("option").remove();
		$("#outputChildStep").append("<option value=''>선택</option>");
	}
}

function addOutput() {
	if(!checkValidate()) {
		return;
	}
	
	$("#addOutputForm").attr("action",getURLString("/project/template/createOutputAction"));
	var param = new Object();
	formSubmit("addOutputForm", param, "${e3ps:getMessage('산출물을 등록하시겠습니까?')}", function(){
		if(opener.window.viewTask) {
			opener.window.viewTask("${oid}");
		}
	}, true);
}

function checkValidate() {
	
	if($("#name").val().length == 0){
		$("#name").focus();
		openNotice("${e3ps:getMessage('이름을 입력하세요.')}");
		return false;
	}
	
// 	if($("#outputType").val() == "PSO") {
// 		if($("#outputStep").val() != null && $("#outputStep").val().length == 0){
// 			$("#outputStep").focus();
// 			openNotice("${e3ps:getMessage('산출물 인증 타입을 입력하세요.')}");
// 			return false;
// 		}
		
// 		if($("#outputChildStep").val() != null && $("#outputChildStep").val().length == 0){
// 			$("#outputChildStep").focus();
// 			openNotice("${e3ps:getMessage('산출물 인증 타입을 입력하세요.')}");
// 			return false;
// 		}
// 	}
	
	if($("#locationDisplay").val().length == 0){
		openNotice("${e3ps:getMessage('문서 분류를 로딩중입니다.')}");
		return false;
	}
	
	if($("#locationDisplay").val() == "/Document"){
		openNotice("${e3ps:getMessage('문서 분류를 선택하세요.')}");
		return false;
	}
	
	return true;
}
</script>