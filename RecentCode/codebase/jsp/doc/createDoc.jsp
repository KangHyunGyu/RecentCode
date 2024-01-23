<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<style>
	.allCon, .aCon, .bCon, .cCon, .dCon, .eCon, .fCon, .gCon{
		display : none;
	}
	
</style>
<script type="text/javascript">
$(document).ready(function(){
	getDocAttributes();
	document.getElementById("docAttribute").addEventListener("change", handleSelectChange);
});
<%----------------------------------------------------------
*                      임시 저장 및 등록 버튼
----------------------------------------------------------%>

function save(btn){
	if(!checkValidate()) {
		return;
	}
	
	$("#docForm").attr("action",getURLString("/doc/createDocAction"));
	var param = new Object();

	//결재선 지정 리스트
	var approvalList = AUIGrid.getGridData(app_line_myGridID);
	
	//관련 부품
	var relatedPartList = AUIGrid.getGridData(add_relatedPart_myGridID);
	
	//공정 개발 검토서 contentsList
	const pdr_contents_list = AUIGrid.getGridData(pdr_content_gridID);
	
	//기타 contentsList
	const etc_contents_list = AUIGrid.getGridData(etc_content_gridID);
	
	var appState = $(btn).val();
	
	param["approvalList"] = approvalList;
	param["relatedPartList"] = relatedPartList;
	param["appState"] = appState;
	param.pdrContentsList = pdr_contents_list;
	param.etcContentsList = etc_contents_list;
	
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
	
	formSubmit("docForm", param, msg, function(){}, true);
}

function checkValidate() {
	
	const selectBox = document.getElementById("docAttribute");
	const docAttribute = $("#docAttribute").val();
	const selectedOption = selectBox.options[selectBox.selectedIndex];
	const selectedOptionValue = selectedOption.getAttribute('dataLocation');
	

	if($("#docAttribute").val() == "D000") {
		openNotice("${e3ps:getMessage('문서 유형을 입력하세요.')}");
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
	
	var partValid = AUIGrid.validateGridData(add_relatedPart_myGridID, ["number"], "${e3ps:getMessage('필수 필드는 반드시 값을 입력해야 합니다.')}");
	
	if(!partValid) {
		$(add_relatedPart_myGridID)[0].scrollIntoView();
		openNotice("${e3ps:getMessage('추가된 관련 부품에 값을 입력하세요.')}");
		return false;
	}

	return true;
}

function handleSelectChange(){
	const selectBox = document.getElementById("docAttribute");
	const selectedValue = selectBox.value;
	const selectedOption = selectBox.options[selectBox.selectedIndex];
	const selectedOptionValue = selectedOption.getAttribute('dataLocation');
	
	document.getElementById("locationDisplay").value = selectedOptionValue;
	document.getElementById('location').value = '/Default'+selectedOptionValue;
	if(selectedValue == ""){
		selectedValue = "D000";
	} else if(selectedValue === "D001"){
		
		$(".aCon").show();
		$(".bCon").hide();
		$(".cCon").hide();
		$(".dCon").hide();
		$(".eCon").hide();
		$(".fCon").hide();
		$(".gCon").hide();
	}else if(selectedValue === "D002"){
		
		$(".aCon").hide();
		$(".bCon").show();
		$(".cCon").hide();
		$(".dCon").hide();
		$(".eCon").hide();
		$(".fCon").hide();
		$(".gCon").hide();
	}else if (selectedValue === "D003"){
		
		$(".aCon").hide();
		$(".bCon").hide();
		$(".cCon").show();
		$(".dCon").hide();
		$(".eCon").hide();
		$(".fCon").hide();
		$(".gCon").hide();
	}else if (selectedValue === "D004"){
		
		$(".aCon").hide();
		$(".bCon").hide();
		$(".cCon").hide();
		$(".dCon").show();
		$(".eCon").hide();
		$(".fCon").hide();
		$(".gCon").hide();
	}else if (selectedValue === "D005"){
		
		$(".aCon").hide();
		$(".bCon").hide();
		$(".cCon").hide();
		$(".dCon").hide();
		$(".eCon").show();
		$(".fCon").hide();
		$(".gCon").hide();
	}else if (selectedValue === "D006"){
		
		$(".aCon").hide();
		$(".bCon").hide();
		$(".cCon").hide();
		$(".dCon").hide();
		$(".eCon").hide();
		$(".fCon").show();
		$(".gCon").hide();
// 	}else if (selectedValue === "D009"){
		
// 		$(".aCon").hide();
// 		$(".bCon").hide();
// 		$(".cCon").hide();
// 		$(".dCon").hide();
// 		$(".eCon").hide();
// 		$(".fCon").hide();
// 		$(".gCon").show();
	}else{
		
		$(".aCon").hide();
		$(".bCon").hide();
		$(".cCon").hide();
		$(".dCon").hide();
		$(".eCon").hide();
		$(".fCon").hide();
		$(".gCon").show();
	}
	
	resizeGrid();
}

  
</script>
<div class="product"> 
<form name="docForm" id="docForm" method="post">
	<input type="hidden" id="location" name="location" value="">
	<input type="hidden" id="mode" name="mode" value="input">
	<input type="hidden" id="docCode" name="docCode" value="">
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
				<col style="width:20%">
				<col style="width:80%">
			</colgroup>	
			<tbody>
				<tr style="display: none">
					<th>${e3ps:getMessage('문서 분류')}<span class="required">*</span></th>
					<td colspan="3">
						<input type="text" class="w50" id="locationDisplay" name="locationDisplay" disabled>
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('문서 유형')}<span class="required">*</span></th>
					<td colspan="3">
						<select class="w50" id="docAttribute" name="docAttribute" style="height:30px;overflow-y: hidden;">
						</select>
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('문서 명')}<span class="required">*</span></th>
					<td>
						<input type="text" class="w50" id="docName" name="docName">
					</td>
				</tr>
				<tr>
				<th>${e3ps:getMessage('주첨부파일')}<span class="required">*</span></th>
				<td class="primary" colspan="3">
					<jsp:include
						page="${e3ps:getIncludeURLString('/content/include_fileAttach')}"
						flush="true">
						<jsp:param name="formId" value="docForm" />
						<jsp:param name="command" value="insert" />
						<jsp:param name="type" value="PRIMARY" />
						<jsp:param name="btnId" value="createBtn" />
					</jsp:include></td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('첨부파일')}</th>
				<td colspan="3">
					<jsp:include
						page="${e3ps:getIncludeURLString('/content/include_fileAttach')}"
						flush="true">
						<jsp:param name="formId" value="docForm" />
						<jsp:param name="command" value="insert" />
						<jsp:param name="type" value="SECONDARY" />
						<jsp:param name="btnId" value="createBtn" />
					</jsp:include></td>
			</tr>
			</tbody>
		</table>
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
<!-- 	<div class="D009 gCon ml30 mr30"> -->
<%-- 		<jsp:include page="${e3ps:getIncludeURLString('/doc/include_docCreateEtc')}"  flush="true"> --%>
<%-- 			<jsp:param name="docNumber" value="D009"/> --%>
<%-- 			<jsp:param name="title" value="${e3ps:getMessage('상세내용')}" /> --%>
<%-- 		</jsp:include> --%>
<!-- 	</div> -->
	<div class="gCon ml30 mr30">
		<jsp:include page="${e3ps:getIncludeURLString('/doc/include_docCreateEtc')}"  flush="true">
			<jsp:param name="title" value="${e3ps:getMessage('상세내용')}" />
		</jsp:include>
	</div>
	
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
</form>
</div>
