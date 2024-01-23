<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
});

<%----------------------------------------------------------
*                      임시 저장 및 등록 버튼
----------------------------------------------------------%>
function save(btn){
	if(!checkValidate()) {
		return;
	}
	
	$("#multiApprovalForm").attr("action",getURLString("/approval/createMultiApprovalAction"));
	
	var param = new Object();

	//결재선 지정 리스트
// 	var approvalList = $$("app_line_grid_wrap").data.serialize();
	var approvalList = AUIGrid.getGridData(app_line_myGridID);
	
	//일괄 결재 목록
// 	var multiObjectList = $$("add_${pageName}_grid_wrap").data.serialize();
	var multiObjectList = AUIGrid.getGridData(add_${pageName}_myGridID);
	//var multiAppPartList = AUIGrid.getGridData(add_multiAppPart_myGridID);
	//var multiAppEpmList = AUIGrid.getGridData(add_multiAppEpm_myGridID);
	param["approvalList"] = approvalList;
	param["multiObjectList"] = multiObjectList;
	//param["multiAppPartList"] = multiAppPartList;
	//param["multiAppEpmList"] = multiAppEpmList;
	
	var appState = $(btn).val();
	param["appState"] = appState;
	param["objectType"] = "doc";
	
	if(appState == "APPROVING") {
		if(checkApproveLine()) {
			msg = "${e3ps:getMessage('등록하시겠습니까?')}";	
		} else {
			return;
		}
	} else if(appState == "TEMP_STORAGE") {
		msg = "${e3ps:getMessage('임시저장하시겠습니까?')}";
	}
	
	formSubmit("multiApprovalForm", param, msg, null, true);
}

function checkValidate() {
	
	if($("#name").val() == null || $("#name").val() == "") {
		$("#name").focus();
		openNotice("${e3ps:getMessage('일괄 결재 제목을 입력하세요.')}");
		return false;
	}
	
// 	var multiObjectList = $$("add_${pageName}_grid_wrap").data.serialize();
	var multiObjectValid = AUIGrid.validateGridData(add_${pageName}_myGridID, ["number"], "${e3ps:getMessage('필수 필드는 반드시 값을 입력해야 합니다.')}");
	
// 	if(multiObjectList.length == 0) {
// 		openNotice("${e3ps:getMessage('일괄 결재 문서를 추가하세요.')}");
// 		return false;
// 	}
	if(!multiObjectValid) {
		$(add_${pageName}_myGridID)[0].scrollIntoView();
		openNotice("${e3ps:getMessage('추가된 일괄 결재 문서에 값을 입력하세요.')}");
		return false;
	}
	
// 	var multiObjectValid = true;
// 	$$("add_${pageName}_grid_wrap").data.each(function(obj){
// 		if(!obj.number || obj.number.length === 0){
// 			multiObjectValid = false;
// 		}
// 	});
	
// 	if(!multiObjectValid) {
// 		$("#add_${pageName}_grid_wrap")[0].scrollIntoView();
// 		openNotice("${e3ps:getMessage('추가된 일괄 결재 문서에 값을 입력하세요.')}");
// 		return false;
// 	}
	
// 	var appLineValid = true;
// 	 $$("app_line_grid_wrap").data.each(function(obj){
// 		if(!obj.roleType || obj.roleType.length === 0){
// 			appLineValid = false;
// 		} else if(!obj.name || obj.name.length === 0) {
// 			appLineValid = false;
// 		}
// 	}); 
	
// 	if(!appLineValid) {
// 		$("#app_line_grid_wrap")[0].scrollIntoView();
// 		openNotice("${e3ps:getMessage('추가된 결재선에 값을 입력하세요.')}");
// 		return false;
// 	}
	
	var appLineValid = AUIGrid.validateGridData(app_line_myGridID, 
			["roleType", "name"], 
			"${e3ps:getMessage('필수 필드는 반드시 값을 입력해야 합니다.')}");
	
	if(!appLineValid) {
		$(app_line_myGridID)[0].scrollIntoView();
		openNotice("${e3ps:getMessage('추가된 결재선에 값을 입력하세요.')}");
		return false;
	}
	
	return true;
}
</script>
<div class="product">
<form name="multiApprovalForm" id="multiApprovalForm" method="post">
	<!-- button -->
	<div class="seach_arm pt20 pb5">
		<div class="leftbt">
			<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('기본 정보')}</span>
		</div>
		<div class="rightbt">
			<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0"/>
			<%-- <button type="button" value="TEMP_STORAGE" class="s_bt03" onclick="save(this)">${e3ps:getMessage('임시 저장')}</button> --%>
			<button type="button" value="APPROVING" class="s_bt03" onclick="save(this)">${e3ps:getMessage('등록')}</button>
		</div>
	</div>
	<!-- //button -->
	<div class="pro_table mr30 ml30">
		<table class="mainTable">
			<colgroup>
				<col style="width:20%">
				<col style="width:80%">
			</colgroup>	
			<tbody>
				<tr>
					<th>${e3ps:getMessage('일괄 결재 제목')}<span class="required">*</span></th>
					<td>
						<input type="text" class="w50" id="name" name="name">
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('일괄 결재 설명')}</th>
					<td class="pd15">
						<div class="textarea_autoSize">
							<textarea name="description" id="description" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
						</div>
					</td>
				</tr>
	            <tr> 
	               <th>${e3ps:getMessage('첨부파일')}</th>
	               <td>
	                  <jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
	                     <jsp:param name="formId" value="multiApprovalForm"/>
	                     <jsp:param name="command" value="insert"/>
	                     <jsp:param name="btnId" value="createBtn" />
	                  </jsp:include>
	               </td>
	            </tr>
			</tbody>
		</table>
	</div>

	<!-- 문서 지정 include 화면 -->
	<div class="ml30 mr30">
		<jsp:include page="${e3ps:getIncludeURLString('/common/include_addObject')}" flush="true">
			<jsp:param name="objType" value="${objType}"/>
			<jsp:param name="pageName" value="${pageName}"/>
			<jsp:param name="title" value="${title}<span class='required'>*</span>"/>
			<jsp:param name="gridHeight" value="200"/>
			<jsp:param name="moduleType" value="multiApproval"/>
		</jsp:include>
	</div>
	
	<!-- 결재선 지정 include 화면 -->
	<div class="ml30 mr30">
		<jsp:include page="${e3ps:getIncludeURLString('/approval/include_addApprovalLine')}" flush="true">
			<jsp:param name="gridHeight" value="200"/>
		</jsp:include>
	</div>
</form>
</div>