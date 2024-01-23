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
	//if(!checkValidate()) {
	//	return;
	//}
	$("#epmForm").attr("action",getURLString("/epm/createEpmAction"));
	
	var param = new Object();

	param["container"] = $("#container").val();
	
	var isOwnershipPart = $("input:radio[name=isOwnershipPart]:checked").val();
	
	console.log($$("add_ownershipPart_grid_wrap"));
	//주 부품
// 	var ownershipPartList = $$("add_ownershipPart_grid_wrap").data.serialize();
	var ownershipPartList = AUIGrid.getGridData(add_ownershipPart_myGridID);
	
	//관련 부품
	//var relatedPartList = $$("add_relatedPart_grid_wrap").data.serialize();
	
	//결재선 지정 리스트
	//var approvalList = $$("app_line_grid_wrap").data.serialize();
	
	var msg = "";
	
	if(isOwnershipPart == "true") {
		param["ownershipPartList"] = ownershipPartList;
		//param["relatedPartList"] = relatedPartList;
		
		
		if(ownershipPartList.length > 0 ) {
			msg = "${e3ps:getMessage('등록하시겠습니까?')}";
		} else {
			openNotice("${e3ps:getMessage('주부품을 선택하세요')}");
		}
	} else if(isOwnershipPart == "false") {
		var appState = $(btn).val();
		param["appState"] = appState;
		//param["relatedPartList"] = relatedPartList;
		//param["approvalList"] = approvalList;
		
		if(appState == "APPROVING") {
			if(checkApproveLine()) {
				msg = "${e3ps:getMessage('주부품 연결 없이 도면을 등록하시겠습니까?')}";
			} else {
				return;
			}
		} else if(appState == "TEMP_STORAGE") {
			msg = "${e3ps:getMessage('임시저장하시겠습니까?')}";
		}
	}
	
	formSubmit("epmForm", param, msg, null, true);
}

function checkOwnershipPart(){
	var value = $("input:radio[name=isOwnershipPart]:checked").val();
	
	if(value == "true") {
		$("#ownershipPart").css("display", "");
		$("#noOwnershipPart1").css("display", "none");
		$("#noOwnershipPart2").css("display", "none");
		$(".ownershipPartTR").css("display", "");
		$(".noOwnershipPartTR").css("display", "none");
		$("#approvalDiv").css("display", "none");
	} else if(value == "false") {
		$("#ownershipPart").css("display", "none");
		$("#noOwnershipPart1").css("display", "");
		$("#noOwnershipPart2").css("display", "");
		$(".ownershipPartTR").css("display", "none");
		$(".noOwnershipPartTR").css("display", "");
		$("#approvalDiv").css("display", "");
	}
	
	resizeGrid();
}

function checkValidate() {
	var isOwnershipPart = $("input:radio[name=isOwnershipPart]:checked").val();
	
	if(isOwnershipPart == "true") {
// 		var ownershipPartList = $$("add_ownershipPart_grid_wrap").data.serialize();
		var ownershipPartList = AUIGrid.getGridData(add_ownershipPart_myGridID);
		
		if(ownershipPartList.length == 0) {
			openNotice("${e3ps:getMessage('주 부품을 추가하세요.')}");
			return false;
		}
		
		ownershipPartValid = AUIGrid.validateGridData(add_ownershipPart_myGridID, ["number"], "${e3ps:getMessage('필수 필드는 반드시 값을 입력해야 합니다.')}");
		
		var ownershipPartValid = true;
		$$("add_ownershipPart_grid_wrap").data.each(function(obj){
			if(!obj.number || obj.number.length === 0){
				ownershipPartValid = false;
			}
		});
		
		if(!ownershipPartValid) {
			//$("#add_relatedPart_grid_wrap")[0].scrollIntoView();
			$(add_ownershipPart_myGridID)[0].scrollIntoView();
			openNotice("${e3ps:getMessage('추가된 주부품에 값을 입력하세요.')}");
			return false;
		}
	} else if(isOwnershipPart == "false") {
		if($("#name").val() == null || $("#name").val() == "") {
			$("#name").focus();
			openNotice("${e3ps:getMessage('도면 명을 입력하세요.')}");
			return false;
		}
	}
	
	if($("[id=PRIMARY]").length == 0){
		openNotice("${e3ps:getMessage('파일을 첨부하여 주십시오.')}");
		return false;
	}
	
	var partValid = true;
	$$("add_relatedPart_grid_wrap").data.each(function(obj){
		if(!obj.number || obj.number.length === 0){
			partValid = false;
		}
	});
	
	if(!partValid) {
		$("#add_relatedPart_grid_wrap")[0].scrollIntoView();
		openNotice("${e3ps:getMessage('추가된 관련 부품에 값을 입력하세요.')}");
		return false;
	}

	var appLineValid = true;
	 $$("app_line_grid_wrap").data.each(function(obj){
		if(!obj.roleType || obj.roleType.length === 0){
			partValid = false;
		} else if(!obj.name || obj.name.length === 0) {
			partValid = false;
		}
	});
	
	if(!appLineValid) {
		$("#app_line_grid_wrap")[0].scrollIntoView();
		openNotice("${e3ps:getMessage('추가된 결재선에 값을 입력하세요.')}");
		return false;
	}
	
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
			<button type="button" id="ownershipPart" class="s_bt03" onclick="save(this)">${e3ps:getMessage('등록')}</button>
			<button type="button" id="noOwnershipPart1" value="TEMP_STORAGE" class="s_bt03" onclick="save(this)" style="display:none;">${e3ps:getMessage('임시 저장')}</button>
			<button type="button" id="noOwnershipPart2" value="APPROVING" class="s_bt03" onclick="save(this)" style="display:none;">${e3ps:getMessage('등록')}</button>
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
					<th>${e3ps:getMessage('도면 분류')}<span class="required">*</span></th>
					<td>
						<input type="text" class="w50" id="locationDisplay" name="locationDisplay" disabled>
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('주 부품 연결 여부')}<span class="required">*</span></th>
					<td>
						<input type="radio" id="isOwnershipPart" name="isOwnershipPart" value="true" onclick="javascript:checkOwnershipPart()" checked>
						<label>${e3ps:getMessage('연결')}</label>
						<input type="radio" id="isOwnershipPart" name="isOwnershipPart" value="false" onclick="javascript:checkOwnershipPart()">
						<label>${e3ps:getMessage('연결 안함')}</label>
					</td>
				</tr>
				<tr class="ownershipPartTR">
					<th>${e3ps:getMessage('주 부품')}<span class="required">*</span></th>
					<td class="pb5">
						<jsp:include page="${e3ps:getIncludeURLString('/common/include_addObject')}" flush="true">
							<jsp:param name="type" value="single"/>
							<jsp:param name="objType" value="part"/>
							<jsp:param name="pageName" value="ownershipPart"/>
							<jsp:param name="toggle" value="false"/>
						</jsp:include>
					</td>
				</tr>
				<%-- <tr class="noOwnershipPartTR" style="display:none;">
					<th>${e3ps:getMessage('도면 번호')}<span class="required">*</span></th>
					<td>
						<input type="text" class="w50" id="number" name="number">
					</td>
				</tr> --%>
				<tr class="noOwnershipPartTR" style="display:none;">
					<th>${e3ps:getMessage('도면 명')}<span class="required">*</span></th>
					<td>
						<input type="text" class="w50" id="name" name="name">
					</td>
				</tr>
				<tr>
	               <th>${e3ps:getMessage('주첨부파일')}<span class="required">*</span></th>
	               <td>
	                  <jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
	                     <jsp:param name="formId" value="epmForm"/>
	                     <jsp:param name="command" value="insert"/>
	                     <jsp:param name="type" value="PRIMARY"/>
	                     <jsp:param name="btnId" value="createBtn" />
	                  </jsp:include>
	               </td>
	            </tr>
	            <tr>
	               <th>${e3ps:getMessage('부첨부파일')}</th>
	               <td>
	                  <jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
	                     <jsp:param name="formId" value="epmForm"/>
	                     <jsp:param name="command" value="insert"/>
	                     <jsp:param name="type" value="SECONDARY"/>
	                     <jsp:param name="btnId" value="createBtn" />
	                  </jsp:include>
	               </td>
	            </tr>
			</tbody>
		</table>
	</div>
	
	<!-- button -->
<%-- 	<div class="seach_arm pt20 pb5">
		<div class="leftbt">
			<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('도면 속성')}</span>
		</div>
		<div class="rightbt">
		</div>
	</div>
	<!-- //button -->
	<div class="pro_table mr30 ml30">
		<table class="mainTable">
			<colgroup>
				<col style="width:13%">
				<col style="width:20%">
				<col style="width:13%">
				<col style="width:20%">
				<col style="width:13%">
				<col style="width:21%">
			</colgroup>	
			<tbody>
				<tr>
					<th>${e3ps:getMessage('고객사')}</th>
					<td>
						<select class="w100" id="customer" name="customer"></select>
					</td>
					<th>UPG</th>
					<td>
						<input type="text" class="w95" id="upg" name="upg">
					</td>
					<th>${e3ps:getMessage('확장자')}</th>
					<td>
						<select class="w100" id="extensions" name="extensions"></select>
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('설명')}</th>
					<td class="pd15" colspan="5">
						<div class="textarea_autoSize">
							<textarea name="description" id="description" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div> --%>
	
	<!-- 관련 부품 지정 include 화면 -->
	<div class="ml30 mr30">
		<jsp:include page="${e3ps:getIncludeURLString('/common/include_addObject')}" flush="true">
			<jsp:param name="type" value="multi"/>
			<jsp:param name="objType" value="part"/>
			<jsp:param name="pageName" value="relatedPart"/>
			<jsp:param name="gridHeight" value="200"/>
			<jsp:param name="title" value="${e3ps:getMessage('관련 부품')}"/>
		</jsp:include>
	</div>
	
	<!-- 결재선 지정 include 화면 -->
	<div id="approvalDiv" class="ml30 mr30" style="display:none;">
		<jsp:include page="${e3ps:getIncludeURLString('/approval/include_addApprovalLine')}" flush="true">
			<jsp:param name="gridHeight" value="200"/>
		</jsp:include>
	</div>
</form>
</div>