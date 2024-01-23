<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript">
$(document).ready(function(){
	getNumberCodeList("occurrenceModel", "CARTYPE", false, true);
	getNumberCodeList("occurrenceStep", "OCCURRENCESTEP", false, true);
	getNumberCodeList("occurrencePlace", "OCCURRENCEPLACE", false, true);
	getNumberCodeList("oldcarState", "OLDCARSTEP", false, true);
});
<%----------------------------------------------------------
*                      임시 저장 및 등록 버튼
----------------------------------------------------------%>
function save(btn){
	if(!checkValidate()) {
		return;
	}
	
	$("#oldcarForm").attr("action",getURLString("/oldcar/createOldcarAction"));
	var param = new Object();

	//결재선 지정 리스트
	var approvalList = $$("app_line_grid_wrap").data.serialize();
	
	//관련 부품
	var relatedPartList = $$("add_relatedPart_grid_wrap").data.serialize();
	
	var appState = $(btn).val();
	
	param["approvalList"] = approvalList;
	param["relatedPartList"] = relatedPartList;
	param["appState"] = appState;
	
	var msg = "";
	
	if(appState == "APPROVING") {
		if(checkApproveLine()) {
			msg = "${e3ps:getMessage('등록하시겠습니까?')}";	
		} else {
			return;
		}
	} else if(appState == "TEMP_STORAGE") {
		msg = "${e3ps:getMessage('임시저장하시겠습니까?')}";
	}
	
	formSubmit("oldcarForm", param, msg, null, true);
}

function checkValidate() {
	
	if($("#oldcarName").val() == null || $("#oldcarName").val() == "") {
		$("#oldcarName").focus();
		openNotice("${e3ps:getMessage('제목을 입력하세요.')}");
		return false;
	}
	if($("#occurrenceDate").val() == null || $("#occurrenceDate").val() == "") {
		$("#occurrenceDate").focus();
		openNotice("${e3ps:getMessage('발생일을 입력하세요.')}");
		return false;
	}
	if($("#occurrenceStep").val() == null || $("#occurrenceStep").val() == "") {
		$("#occurrenceStep").focus();
		openNotice("${e3ps:getMessage('발생시점을 입력하세요.')}");
		return false;
	}
	if($("#occurrenceModel").val() == null || $("#occurrenceModel").val() == "") {
		$("#occurrenceModel").focus();
		openNotice("${e3ps:getMessage('차종을 입력하세요.')}");
		return false;
	}
	if($("#occurrencePlace").val() == null || $("#occurrencePlace").val() == "") {
		$("#occurrencePlace").focus();
		openNotice("${e3ps:getMessage('발생장소를 입력하세요.')}");
		return false;
	}
	if($("#oldcarState").val() == null || $("#oldcarState").val() == "") {
		$("#oldcarState").focus();
		openNotice("${e3ps:getMessage('처리상태를 입력하세요.')}");
		return false;
	}
	if($("#problemContent").val() == null || $("#problemContent").val() == "") {
		$("#problemContent").focus();
		openNotice("${e3ps:getMessage('문제내용을 입력하세요.')}");
		return false;
	}
	if($("[id=PRIMARY]").length == 0){
		openNotice("${e3ps:getMessage('주 첨부파일을 추가하세요.')}");
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
			appLineValid = false;
		} else if(!obj.name || obj.name.length === 0) {
			appLineValid = false;
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
<form name="oldcarForm" id="oldcarForm" method="post">
	<!-- button -->
	<div class="seach_arm pt20 pb5">
		<div class="leftbt">
			<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('기본 정보')}</span>
		</div>
		<div class="rightbt">
			<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0"/>
			<button type="button" value="TEMP_STORAGE" class="s_bt03" onclick="save(this)">${e3ps:getMessage('임시 저장')}</button>
			<button type="button" value="APPROVING" class="s_bt03" onclick="save(this)">${e3ps:getMessage('등록')}</button>
		</div>
	</div>
	<!-- //button -->
	<div class="pro_table mr30 ml30">
		<table class="mainTable">
			<colgroup>
				<col style="width:13%">
				<col style="width:37%">
				<col style="width:13%">
				<col style="width:37%">
			</colgroup>	
			<tbody>
				<tr>
					<th>${e3ps:getMessage('제목')}<span class="required">*</span></th>
					<td colspan="3">
						<input type="text" class="w95" id="oldcarName" name="oldcarName">
					</td>
				</tr>
				<tr>
					
					<th>${e3ps:getMessage('발생일')}<span class="required">*</span></th>
					<td class="calendar">
						<input type="text" class="datePicker w25" id="occurrenceDate" name="occurrenceDate" readonly>
					</td>
					<th>${e3ps:getMessage('발생시점')}<span class="required">*</span></th>
					<td>
						<select class="w50" id="occurrenceStep" name="occurrenceStep"></select>
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('차종')}<span class="required">*</span></th>
					<td>
						<select class="w50" id="occurrenceModel" name="occurrenceModel"></select>
					</td>
					<th>${e3ps:getMessage('발생장소')}<span class="required">*</span></th>
					<td>
						<select class="w50" id="occurrencePlace" name="occurrencePlace"></select>
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('시행자')}</th>
					<td>
						<div class="pro_view">
							<select class="searchUser" id="worker" name="worker" data-width="60%">
							</select>
							<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('worker');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
							<span class="pointer verticalMiddle" onclick="javascript:deleteUser('worker');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
						</div>
					</td>
					<th>${e3ps:getMessage('처리상태')}<span class="required">*</span></th>
					<td>
						<select class="w50" id="oldcarState" name="oldcarState"></select>
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('관리검증기준')}</th>
					<td colspan="3">
						<input type="text" class="w95" id="criteria" name="criteria">
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('문제공정')}</th>
					<td class="pd15" colspan="3">
						<div class="textarea_autoSize">
							<textarea name="problemProcess" id="problemProcess" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
						</div>
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('문제내용')}<span class="required">*</span></th>
					<td class="pd15" colspan="3">
						<div class="textarea_autoSize">
							<textarea name="problemContent" id="problemContent" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
						</div>
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('문제점 및 원인')}</th>
					<td class="pd15" colspan="3">
						<div class="textarea_autoSize">
							<textarea name="cause" id="cause" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
						</div>
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('개선대책 및 처리내용')}</th>
					<td class="pd15" colspan="3">
						<div class="textarea_autoSize">
							<textarea name="improve" id="improve" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
						</div>
					</td>
				</tr>
				<tr>
	               <th>${e3ps:getMessage('주첨부파일')}<span class="required">*</span></th>
	               <td class="primary" colspan="3">
	                  <jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
	                     <jsp:param name="formId" value="oldcarForm"/>
	                     <jsp:param name="command" value="insert"/>
	                     <jsp:param name="type" value="PRIMARY"/>
	                     <jsp:param name="btnId" value="createBtn" />
	                  </jsp:include>
	               </td>
	            </tr>
	            <tr> 
	               <th>${e3ps:getMessage('첨부파일')}</th>
	               <td colspan="3">
	                  <jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
	                     <jsp:param name="formId" value="oldcarForm"/>
	                     <jsp:param name="command" value="insert"/>
	                     <jsp:param name="btnId" value="createBtn" />
	                  </jsp:include>
	               </td>
	            </tr>
			</tbody>
		</table>
	</div>
	<!-- button -->
	<!-- 관련 부품 지정 include 화면 -->
	<div class="ml30 mr30">
		<jsp:include page="${e3ps:getIncludeURLString('/common/include_addObject')}" flush="true">
			<jsp:param name="objType" value="part"/>
			<jsp:param name="pageName" value="relatedPart"/>
			<jsp:param name="title" value="${e3ps:getMessage('관련 부품')}"/>
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
