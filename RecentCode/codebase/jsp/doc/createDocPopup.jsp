<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	//팝업 리사이즈
	popupResize();
	//getDocAttributes();
	handleSelectChange();
});

function handleSelectChange(){
	const selectBox = document.getElementById("locationDisplay");
	const selectedValue = selectBox.value;
	const D001 = "/Document/1.계약 개발 검토서";
	const D002 = "/Document/2.서비스 요청서";
	const D003 = "/Document/3.측정 의뢰서";
	const D004 = "/Document/4.분석 의뢰서";
	const D005 = "/Document/5.공정 변경 요청서";
	const D006 = "/Document/6.공정 개발 검토서";
	const D009 = "/Document/기타";
	
	if(selectedValue === D001){
		
		$(".aCon").show();
		$(".bCon").hide();
		$(".cCon").hide();
		$(".dCon").hide();
		$(".eCon").hide();
		$(".fCon").hide();
		$(".gCon").hide();
// 		$(".allCon").hide();
	}else if(selectedValue === D002){
		
		$(".aCon").hide();
		$(".bCon").show();
		$(".cCon").hide();
		$(".dCon").hide();
		$(".eCon").hide();
		$(".fCon").hide();
		$(".gCon").hide();
// 		$(".allCon").hide();
	}else if (selectedValue === D003){
		
		$(".aCon").hide();
		$(".bCon").hide();
		$(".cCon").show();
		$(".dCon").hide();
		$(".eCon").hide();
		$(".fCon").hide();
		$(".gCon").hide();
// 		$(".allCon").hide();
	}else if (selectedValue === D004){
		
		$(".aCon").hide();
		$(".bCon").hide();
		$(".cCon").hide();
		$(".dCon").show();
		$(".eCon").hide();
		$(".fCon").hide();
		$(".gCon").hide();
// 		$(".allCon").hide();
	}else if (selectedValue === D005){
		
		$(".aCon").hide();
		$(".bCon").hide();
		$(".cCon").hide();
		$(".dCon").hide();
		$(".eCon").show();
		$(".fCon").hide();
		$(".gCon").hide();
// 		$(".allCon").hide();
	}else if (selectedValue === D006){
		
		$(".aCon").hide();
		$(".bCon").hide();
		$(".cCon").hide();
		$(".dCon").hide();
		$(".eCon").hide();
		$(".fCon").show();
		$(".gCon").hide();
// 		$(".allCon").hide();
	}else if (selectedValue === D009 || selectedValue === ""){
		
		$(".aCon").hide();
		$(".bCon").hide();
		$(".cCon").hide();
		$(".dCon").hide();
		$(".eCon").hide();
		$(".fCon").hide();
		$(".gCon").show();
// 		$(".allCon").hide();
	}else{
		
		$(".aCon").hide();
		$(".bCon").hide();
		$(".cCon").hide();
		$(".dCon").hide();
		$(".eCon").hide();
		$(".fCon").hide();
		$(".gCon").hide();
// 		$(".allCon").hide();
	}
}

<%----------------------------------------------------------
*                      임시 저장 및 등록 버튼
----------------------------------------------------------%>
function save(btn){
	if(!checkValidate()) {
		return;
	}
	
	$("#docForm").attr("action",getURLString("/doc/createDocPopupAction"));
	var param = new Object();

	//결재선 지정 리스트
	//var approvalList = $$("app_line_grid_wrap").data.serialize();
	var approvalList = AUIGrid.getGridData(app_line_myGridID);
	
	//관련 부품
	//var relatedPartList = $$("add_relatedPart_grid_wrap").data.serialize();
	var relatedPartList = AUIGrid.getGridData(add_relatedPart_myGridID);

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
	
	formSubmit("docForm", param, msg, function(data) {
		var docOid = data.docOid;
		
		if(opener.window.set_Document) {
			opener.window.set_Document("${outputOid}",docOid);
		}
		
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

	/*
	var partValid = true;
 	$("#add_relatedPart_grid_wrap").data.each(function(obj){
		if(!obj.number || obj.number.length === 0){
			partValid = false;
		}
	});
	
	if(!partValid) {
		$("#add_relatedPart_grid_wrap")[0].scrollIntoView();
		openNotice("${e3ps:getMessage('추가된 관련 부품에 값을 입력하세요.')}");
		return false;
	}
	*/
	//var partValid = AUIGrid.validateGridData(add_relatedPart_grid_wrap, ["roleType", "number"], "${e3ps:getMessage('추가된 관련 부품에 값을 입력하세요.')}" );
	/*
	if(!partValid){
		$(add_relatedPart_grid_wrap)[0].scrollIntoView();
		openNotice("${e3ps:getMessage('값을 입력하세요.')}");
		return false;
	}
	*/
	
	/* 
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
	 */
	var appLineValid = AUIGrid.validateGridData(app_line_myGridID, ["roleType", "name"], "${e3ps:getMessage('필수 필드는 반드시 값을 입력해야 합니다.')}");
	
	if(!appLineValid) {
		$(app_line_myGridID)[0].scrollIntoView();
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
			<input type="hidden" id="location" name="location" value="${location}">
			<input type="hidden" id="mode" name="mode" value="input">
			<input type="hidden" id="docCode" name="docCode" value="${foid}">
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
								</div>
							</td>
						</tr>
<!-- 						<tr> -->
<%-- 						<th>${e3ps:getMessage('문서 유형')}<span class="required">*</span></th> --%>
<!-- 							<td colspan="3"> -->
<!-- 								<select class="w50" id="docAttribute" name="docAttribute" style="height:30px;overflow-y: hidden;"> -->
<!-- 								</select> -->
<!-- 							</td> -->
<!-- 						</tr> -->
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
		
		<!-- 계약 개발 검토서 -->
		<div class="D001 aCon ml30 mr30">
			<jsp:include page="${e3ps:getIncludeURLString('/doc/include_contractDevelopmentReview')}"  flush="true">
				<jsp:param name="docNumber" value="D001"/>
				<jsp:param name="title" value="${e3ps:getMessage('상세내용')}" />
			</jsp:include>
		</div>
		<!-- 서비스 요청서 -->
		<div class="D002 bCon ml30 mr30">
			<jsp:include page="${e3ps:getIncludeURLString('/doc/include_serviceRequest')}"  flush="true">
				<jsp:param name="docNumber" value="D002"/>
				<jsp:param name="title" value="${e3ps:getMessage('상세내용')}" />
			</jsp:include>
		</div>
		<!-- 측정 의뢰서 -->
		<div class="D003 cCon ml30 mr30">
			<jsp:include page="${e3ps:getIncludeURLString('/doc/include_measurementRequest')}"  flush="true">
				<jsp:param name="docNumber" value="D003"/>
				<jsp:param name="title" value="${e3ps:getMessage('상세내용')}" />
			</jsp:include>
		</div>
		<!-- 분석 의뢰서 -->
		<div class="D004 dCon ml30 mr30">
			<jsp:include page="${e3ps:getIncludeURLString('/doc/include_analysisRequest')}"  flush="true">
				<jsp:param name="docNumber" value="D004"/>
				<jsp:param name="title" value="${e3ps:getMessage('상세내용')}" />
			</jsp:include>
		</div>
		<!-- 공정 변경 요청서  여기서부터-->
		<div class="D005 eCon ml30 mr30">
			<jsp:include page="${e3ps:getIncludeURLString('/doc/include_processChangeRequest')}"  flush="true">
				<jsp:param name="docNumber" value="D005"/>
				<jsp:param name="title" value="${e3ps:getMessage('상세내용')}" />
			</jsp:include>
		</div>
		<!-- 공정 개발 검토서 -->
		<div class="D006 fCon ml30 mr30">
			<jsp:include page="${e3ps:getIncludeURLString('/doc/include_processDevelopmentReview')}"  flush="true">
				<jsp:param name="docNumber" value="D006"/>
				<jsp:param name="title" value="${e3ps:getMessage('상세내용')}" />
			</jsp:include>
		</div>
		<!-- 기타 -->
		<div class="D009 gCon ml30 mr30">
			<jsp:include page="${e3ps:getIncludeURLString('/doc/include_docCreateEtc')}"  flush="true">
				<jsp:param name="docNumber" value="D009"/>
				<jsp:param name="title" value="${e3ps:getMessage('상세내용')}" />
			</jsp:include>
		</div>
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
