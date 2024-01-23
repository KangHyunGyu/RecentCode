<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript">
$(document).ready(function(){
	getNumberCodeChildList("customer", "CUSTOMER", "", false, false, true);
	getNumberCodeList("extensions", "EXTENSIONS", false, true);
});

<%----------------------------------------------------------
*                      임시 저장 및 등록 버튼
----------------------------------------------------------%>
function save(btn){
	if(!checkValidate()) {
		return;
	}
	$("#epmForm").attr("action",getURLString("/epm/createEpmFolderAction"));
	
	var param = new Object();
	
	//param["container"] = $("#container").val();
	//console.log(param);
	
// 	//관련 부품
// 	var relatedPartList = $$("add_relatedPart_grid_wrap").data.serialize();
	
// 	//결재선 지정 리스트
// 	var approvalList = $$("app_line_grid_wrap").data.serialize();
	
// 	var msg = "???";
	
	formSubmit("epmForm", param, null, null, true);
}

function checkValidate() {
	
	if($("#nasLocation").val() == null || $("#nasLocation").val() == "") {
		$("#nasLocation").focus();
		openNotice("${e3ps:getMessage('프로젝트 폴더 이름을 입력하세요.')}");
		return false;
	}
	
// 	var partValid = true;
// 	$$("add_relatedPart_grid_wrap").data.each(function(obj){
// 		if(!obj.number || obj.number.length === 0){
// 			partValid = false;
// 		}
// 	});
// 	if(!partValid) {
// 		$("#add_relatedPart_grid_wrap")[0].scrollIntoView();
// 		openNotice("${e3ps:getMessage('추가된 관련 부품에 값을 입력하세요.')}");
// 		return false;
// 	}

// 	var appLineValid = true;
// 	 $$("app_line_grid_wrap").data.each(function(obj){
// 		if(!obj.roleType || obj.roleType.length === 0){
// 			partValid = false;
// 		} else if(!obj.name || obj.name.length === 0) {
// 			partValid = false;
// 		}
// 	});
	
// 	if(!appLineValid) {
// 		$("#app_line_grid_wrap")[0].scrollIntoView();
// 		openNotice("${e3ps:getMessage('추가된 결재선에 값을 입력하세요.')}");
// 		return false;
// 	}
	
	return true;
}
</script>
<div class="product"> 
<form name="epmForm" id="epmForm" method="post">
	<input type="hidden" name="lifecycle"	id="lifecycle" 	value="LC_Default" />
	<input type="hidden" id="location" name="location" value="">
	<!-- button -->
	<div class="seach_arm pt20 pb5">
		<div class="leftbt">
			<h4>${e3ps:getMessage('기본 정보')}</h4>
		</div>
		<div class="rightbt">
			<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0"/>
			<%-- <button type="button" value="TEMP_STORAGE" class="s_bt03" onclick="save(this)">${e3ps:getMessage('임시 저장')}</button> --%>
			<button type="button" id="folderSave" class="s_bt03" onclick="save(this)">${e3ps:getMessage('등록')}</button><%-- ownershipPart --%>
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
					<th>${e3ps:getMessage('프로젝트 폴더 이름')}<span class="required">*</span></th>
					<td><input type="text" class="w90" id="nasLocation" name="nasLocation"></td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('Windchill 프로젝트 폴더 경로')}<span class="required">*</span></th>
					<td>
						<input type="text" class="w50" id="locationDisplay" name="locationDisplay">
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<!-- button -->
	
	<!-- 관련 부품 지정 include 화면 -->
<!-- 	<div class="ml30 mr30"> -->
<%-- 		<jsp:include page="${e3ps:getIncludeURLString('/common/include_addObject')}" flush="true"> --%>
<%-- 			<jsp:param name="type" value="multi"/> --%>
<%-- 			<jsp:param name="objType" value="part"/> --%>
<%-- 			<jsp:param name="pageName" value="relatedPart"/> --%>
<%-- 			<jsp:param name="gridHeight" value="200"/> --%>
<%-- 			<jsp:param name="title" value="${e3ps:getMessage('관련 부품')}"/> --%>
<%-- 		</jsp:include> --%>
<!-- 	</div> -->
	
<!-- 	결재선 지정 include 화면 -->
<!-- 	<div id="approvalDiv" class="ml30 mr30"> -->
<%-- 		<jsp:include page="${e3ps:getIncludeURLString('/approval/include_addApprovalLine')}" flush="true"> --%>
<%-- 			<jsp:param name="gridHeight" value="200"/> --%>
<%-- 		</jsp:include> --%>
<!-- 	</div> -->
	<!-- ###############  -->
<!-- 	<div style="margin-top: 30px"></div> -->
<!-- 	<div class="seach_arm pt5 pb5"> -->
<!-- 		<div class="leftbt"> -->
<!-- 		</div> -->
<!-- 		<div class="rightbt"> -->
<!-- 			<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0"/> -->
<%-- 				<button type="button" class="s_bt03" onclick="fileDown('searchForm', 'excelDownPart');">${e3ps:getMessage('파일 다운로드')}</button> --%>
<!-- 		</div> -->
<!-- 	</div> -->
	<!-- ###############  -->
</form>
</div>