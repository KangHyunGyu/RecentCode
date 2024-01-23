<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<style>
	.allCon, .aCon, .conDe{
		display : none;
	}
	
</style>
<script type="text/javascript">
$(document).ready(function(){
	getDocAttributes();
	document.getElementById("docAttribute").addEventListener("change", handleSelectChange);
	
	/* $(".allCon").each(function(){
		$(".allCon").hide();
	});
	$(".aCon").each(function(){
		$(".aCon").hide();
	});
	$(".conDe").each(function(){
		$(".conDe").hide();
	}); */
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
	
	formSubmit("docForm", param, msg, null, true);
}

function checkValidate() {
	
// 	if($("#locationDisplay").val().length == 0){
// 		openNotice("${e3ps:getMessage('문서 분류를 로딩중입니다.')}");
// 		return false;
// 	}
	
// 	if($("#locationDisplay").val() == "/Document"){
// 		openNotice("${e3ps:getMessage('문서 분류를 선택하세요.')}");
// 		return false;
// 	}
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

function handleSelectChange(){
	const selectBox = document.getElementById("docAttribute");
	const selectedValue = selectBox.value;
	
	console.log("selectedValue :::::: " + selectedValue);
	if(selectedValue == ""){
		selectedValue = "D000";
	} else if(selectedValue === "D001"){
		
		$(".conDe").show();
		$(".aCon").hide();
		$(".bCon").hide();
		$(".tebon").hide();
		$(".reCon").hide();
		$(".deCon").hide();
		$(".allCon").hide();
	}else if(selectedValue === "D004"){
		$(".aCon").show();
		$(".allCon").hide();
		$(".conDe").hide();
		$(".bCon").hide();
		$(".tebon").hide();
		$(".reCon").hide();
		$(".deCon").hide();
	}else if (selectedValue === "D005"){
		$(".bCon").show();
		$(".aCon").hide();
		$(".conDe").hide();
		$(".tebon").hide();
		$(".reCon").hide();
		$(".deCon").hide();
		$(".allCon").hide();
	}else if (selectedValue === "D006"){
		$(".tebon").show();
		$(".aCon").hide();
		$(".allCon").hide();
		$(".conDe").hide();
		$(".bCon").hide();
		$(".reCon").hide();
		$(".deCon").hide();
	}else if (selectedValue === "D007"){
		$(".reCon").show();
		$(".tebon").hide();
		$(".aCon").hide();
		$(".allCon").hide();
		$(".conDe").hide();
		$(".bCon").hide();
		$(".deCon").hide();
	}else if (selectedValue === "D008"){
		$(".deCon").show();
		$(".reCon").hide();
		$(".tebon").hide();
		$(".aCon").hide();
		$(".allCon").hide();
		$(".conDe").hide();
		$(".bCon").hide();
	}else{
		$(".aCon").hide();
		$(".allCon").hide();
		$(".conDe").hide();
		$(".bCon").hide();
		$(".tebon").hide();
		$(".reCon").hide();
		$(".deCon").hide();
	}
}

//상담방법
function selectedConsultMethod(element) {
    var consultMethod = $("input[name=consultMethod]:checked").val();
  }
//접수자료
function selectedApplicationMaterial(element) {
    var applicationMaterial = $("input[name=applicationMaterial]:checked").val();
  }
//접수구분
function selectedReciptionClass(element) {
    var productClass = $("input[name=receptionClass]:checked").val();
  }
//요구사항
function selectedRequirements(element) {
    var productClass = $("input[name=requirements]:checked").val();
//     if(element.value == "companyDraw"){
//     	$( "#datepicker" ).datepicker("show");
//     }else {
//     	$( "#datepicker" ).datepicker("hide");
//     }
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
<!-- 				<tr> -->
<%-- 					<th>${e3ps:getMessage('문서 분류')}<span class="required">*</span></th> --%>
<!-- 					<td> -->
<!-- 						<input type="text" class="w50" id="locationDisplay" name="locationDisplay" disabled> -->
<!-- 					</td> -->
<!-- 				</tr> -->
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
<!-- 				<tr> -->
<%-- 					<th>${e3ps:getMessage('설명')}</th> --%>
<!-- 					<td class="pd15"> -->
<!-- 						<div class="textarea_autoSize"> -->
<!-- 							<textarea name="description" id="description" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea> -->
<!-- 						</div> -->
<!-- 					</td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<%-- 	               <th>${e3ps:getMessage('주첨부파일')}<span class="required">*</span></th> --%>
<!-- 	               <td class="primary"> -->
<%-- 	                  <jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true"> --%>
<%-- 	                     <jsp:param name="formId" value="docForm"/> --%>
<%-- 	                     <jsp:param name="command" value="insert"/> --%>
<%-- 	                     <jsp:param name="type" value="PRIMARY"/> --%>
<%-- 	                     <jsp:param name="btnId" value="createBtn" /> --%>
<%-- 	                  </jsp:include> --%>
<!-- 	               </td> -->
<!-- 	            </tr> -->
<!-- 	            <tr>  -->
<%-- 	               <th>${e3ps:getMessage('첨부파일')}</th> --%>
<!-- 	               <td> -->
<%-- 	                  <jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true"> --%>
<%-- 	                     <jsp:param name="formId" value="docForm"/> --%>
<%-- 	                     <jsp:param name="command" value="insert"/> --%>
<%-- 	                     <jsp:param name="btnId" value="createBtn" /> --%>
<%-- 	                  </jsp:include> --%>
<!-- 	               </td> -->
<!-- 	            </tr> -->
			</tbody>
		</table>
	</div>
	
	<div class="D001 conDe ml30 mr30">
		<jsp:include page="${e3ps:getIncludeURLString('/doc/createDoc')}" flush="true">
<%-- 			<jsp:param name="objType" value="part"/> --%>
<%-- 			<jsp:param name="pageName" value="relatedPart"/> --%>
			<jsp:param name="title" value="${e3ps:getMessage('상세내용')}"/>
		</jsp:include>
	</div>
	
	<!-- button -->
	<!-- 속성 include 화면 -->
<!-- 	<div id="attributeList"> -->
<%-- 		<jsp:include page="${e3ps:getIncludeURLString('/doc/include_docAttribute')}" flush="true"> --%>
<%-- 		    <jsp:param name="foid" value=""/> --%>
<%-- 		    <jsp:param name="mode" value="input"/> --%>
<%-- 	    </jsp:include> --%>
<!-- 	</div> -->
	<!-- 관련 부품 지정 include 화면 -->
<!-- 	<div class="ml30 mr30"> -->
<%-- 		<jsp:include page="${e3ps:getIncludeURLString('/common/include_addObject')}" flush="true"> --%>
<%-- 			<jsp:param name="objType" value="part"/> --%>
<%-- 			<jsp:param name="pageName" value="relatedPart"/> --%>
<%-- 			<jsp:param name="title" value="${e3ps:getMessage('관련 부품')}"/> --%>
<%-- 		</jsp:include> --%>
<!-- 	</div> -->
	<!-- 결재선 지정 include 화면 -->
	<div class="ml30 mr30">
		<jsp:include page="${e3ps:getIncludeURLString('/approval/include_addApprovalLine')}" flush="true">
			<jsp:param name="gridHeight" value="200"/>
		</jsp:include>
	</div>
</form>
</div>
