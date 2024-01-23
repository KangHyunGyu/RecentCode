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

<%----------------------------------------------------------
*                      임시 저장 및 등록 버튼
----------------------------------------------------------%>
function save(btn){
	if(!checkValidate()) {
		return;
	}
	
	$("#docForm").attr("action",getURLString("/doc/createDocPopupAction2"));
	var param = new Object();

	//결재선 지정 리스트
	var approvalList = $$("app_line_grid_wrap").data.serialize();
	//관련 부품
	var relatedPartList = $$("add_relatedPart_grid_wrap").data.serialize();
	
	var appState = $(btn).val();
	var activeOid = "${activeOid}";
	var activeLinkOid = "${activeLinkOid}";
	var activeLinkType = "${activeLinkType}";
	param["approvalList"] = approvalList;
	param["relatedPartList"] = relatedPartList;
	param["appState"] = appState;
	param["activeOid"] = activeOid;
	param["activeLinkOid"] = activeLinkOid;
	param["activeLinkType"] = activeLinkType;
	
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
	
	formSubmit("docForm", param, msg, function(data) {
		var docOid = data.docOid;
		
		if(opener.window.addECARelatedDoc_createDocLink) {
			opener.window.addECARelatedDoc_createDocLink(docOid);
		}
		opener.location.reload();
		window.close();
	}, true);
}

function checkValidate() {
	
	if($("#locationDisplay").val().length == 0){
		openNotice("${e3ps:getMessage('문서 분류를 로딩중입니다.')}");
		return false;
	}
	
	if($("#locationDisplay").val() == "/Document"){
		openNotice("${e3ps:getMessage('문서 분류를 선택하세요.')}");
		return false;
	}
	if($("#docName").val() == null || $("#docName").val() == "") {
		$("#docName").focus();
		openNotice("${e3ps:getMessage('문서 명을 입력하세요.')}");
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
<!-- pop -->
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('문서 등록')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->

	<form name="docForm" id="docForm" method="post">
		<div class="pl25 pr25">
			<div class="seach_arm2 pt10 pb10">
				<div class="leftbt">
					<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('기본 정보')}</span>
				</div>
				<div class="rightbt">
					<button type="button" value="TEMP_STORAGE" class="s_bt03" onclick="save(this)">${e3ps:getMessage('임시 저장')}</button>
					<button type="button" value="APPROVING" class="s_bt03" onclick="save(this)">${e3ps:getMessage('등록')}</button>
					<button type="button" class="s_bt04" onclick="javascript:window.close()">${e3ps:getMessage('닫기')}</button>
				</div>
			</div>
			<input type="hidden" id="location" name="location" value="">
			<input type="hidden" id="mode" name="mode" value="input">
			<input type="hidden" id="docCode" name="docCode" value="">
			<div class="pro_table">
				<table class="mainTable">
					<colgroup>
						<col style="width:20%">
						<col style="width:80%">
					</colgroup>	
					<tbody>
						<tr>
							<th>${e3ps:getMessage('문서 분류')}<span class="required">*</span></th>
							<td>
								<div style="position:relative">
									<input type="text" id="locationDisplay" name="locationDisplay" value="${locationDisplay}" class="w60" disabled> 
									<button type="button" class="s_bt03" id="toggleBtn" onclick="toggleFolderList(this)">목록</button>
									<div id="folderList" style="display:none;position:absolute;z-index:999">
										<jsp:include page="${e3ps:getIncludeURLString('/common/include_folderTree')}" flush="true">
											<jsp:param name="container" value="worldex"/>
											<jsp:param name="renderTo" value="docFolder"/>
											<jsp:param name="formId" value="docForm"/>
											<jsp:param name="rootLocation" value="/Default/Document"/>
											<jsp:param name="autoGridHeight" value="true"/>
										</jsp:include>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('문서 명')}<span class="required">*</span></th>
							<td>
								<input type="text" class="w50" id="docName" name="docName">
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('설명')}</th>
							<td class="pd15">
								<div class="textarea_autoSize">
									<textarea name="description" id="description" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
								</div>
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('보존연한')}</th>
							<td class="pd15">
								<input type="radio" id="keepYear" name="keepYear" value="infinite" checked>
								<label>${e3ps:getMessage('영구보존')}</label>
								<input type="radio" id="keepYear" name="keepYear" value="1year">
								<label>${e3ps:getMessage('1년')}</label>
								<input type="radio" id="keepYear" name="keepYear" value="3year">
								<label>${e3ps:getMessage('3년')}</label>
								<input type="radio" id="keepYear" name="keepYear" value="5year">
								<label>${e3ps:getMessage('5년')}</label>
								<input type="radio" id="keepYear" name="keepYear" value="10year">
								<label>${e3ps:getMessage('10년')}</label>
								<input type="radio" id="keepYear" name="keepYear" value="15year">
								<label>${e3ps:getMessage('15년')}</label>
							</td>
						</tr>
						<tr>
			               <th>${e3ps:getMessage('주첨부파일')}<span class="required">*</span></th>
			               <td class="primary">
			                  <jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
			                     <jsp:param name="formId" value="docForm"/>
			                     <jsp:param name="command" value="insert"/>
			                     <jsp:param name="type" value="PRIMARY"/>
			                     <jsp:param name="btnId" value="createBtn" />
			                  </jsp:include>
			               </td>
			            </tr>
			            <tr> 
			               <th>${e3ps:getMessage('첨부파일')}</th>
			               <td>
			                  <jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
			                     <jsp:param name="formId" value="docForm"/>
			                     <jsp:param name="command" value="insert"/>
			                     <jsp:param name="btnId" value="createBtn" />
			                  </jsp:include>
			               </td>
			            </tr>
					</tbody>
				</table>
			</div>
		</div>
		<!-- 속성 include 화면 -->
<!-- 		<div id="attributeList"> -->
<%-- 			<jsp:include page="${e3ps:getIncludeURLString('/doc/include_docAttribute')}" flush="true"> --%>
<%-- 			    <jsp:param name="foid" value=""/> --%>
<%-- 			    <jsp:param name="mode" value="input"/> --%>
<%-- 		    </jsp:include> --%>
<!-- 		</div> -->
	</form>
	<!-- 관련 부품 지정 include 화면 -->
	<div class="ml30 mr30">
		<jsp:include page="${e3ps:getIncludeURLString('/common/include_addObject')}" flush="true">
			<jsp:param name="objType" value="part"/>
			<jsp:param name="pageName" value="relatedPart"/>
			<jsp:param name="title" value="${e3ps:getMessage('관련 부품')}"/>
			<jsp:param name="gridHeight" value="200"/>
		</jsp:include>
	</div>
	<!-- 결재선 지정 include 화면 -->
	<div class="ml30 mr30">
		<jsp:include page="${e3ps:getIncludeURLString('/approval/include_addApprovalLine')}" flush="true">
			<jsp:param name="gridHeight" value="200"/>
		</jsp:include>
	</div>
</div>		
<!-- //pop-->
