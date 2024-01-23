<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript" src="/Windchill/jsp/js/distribute.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	// 다운로드 기한 영구 선택 
	$("#downloadDeadlineCheck").click(function(){
		var chk = $(this).is(":checked");
        if(chk) {
        	$("#downloadDeadline").val("infinite");
        	$("#downloadDeadline").hide();
        	$("#downloadDeadlineBtn").hide();
        	$(".calendar span").hide();
        }else{
        	$("#downloadDeadline").val("");
        	$("#downloadDeadline").show();
        	$("#downloadDeadlineBtn").show();
        	$(".calendar span").show();
        }
	});
	
	// 다운로드 횟수 영구 선택
	$("#downloadCountCheck").click(function(){
		var chk = $(this).is(":checked");
        if(chk) {
        	$("#downloadCount").val("99999");
        	$("#downloadCount").hide();
        }else{
        	$("#downloadCount").val("");
        	$("#downloadCount").show();
        }
	});

	// 프로젝트 필수처리
	//$("#projectRequired").show();
	// CAD 타입 체크
	if("${data.cadType}" == "CADCOMPONENT") $("#cadType").prop('checked', true);	
	
});

<%----------------------------------------------------------
*                      임시 저장 및 등록 버튼
----------------------------------------------------------%>
function save(){
	if(!checkValidate()) {
		return;
	}
	
	$("#distributeForm").attr("action",getURLString("/distribute/createDistributePopupAction"));
	
	var param = new Object();
	
	//관련 부품
	var relatedPartList = AUIGrid.getGridData(rel_part_myGridID);
	// 관련 프로젝트
	//var relatedProjectList = AUIGrid.getGridData(add_relatedProject_myGridID);
	
	param["relatedPartList"] = relatedPartList;
	//param["relatedProjectList"] = relatedProjectList;
	
	var msg = "${e3ps:getMessage('등록하시겠습니까?')}";	
	formSubmit("distributeForm", param, msg, null, true);
}
// 필수 여부 체크
function checkValidate() {
	if($("#distributeName").val() == null || $("#distributeName").val() == "") {
		$("#distributeName").focus();
		openNotice("${e3ps:getMessage('배포 제목을 입력하여 주십시오.')}");
		return false;
	}
	if($("#classification").val() == null || $("#classification").val() == "") {
		$("#classification").focus();
		openNotice("${e3ps:getMessage('배포 분류를 입력하여 주십시오.')}");
		return false;
	}
	if($("#supplierId").val() == null || $("#supplierId").val() == "") {
		$("#supplierId").focus();
		openNotice("${e3ps:getMessage('업체를 입력하여 주십시오.')}");
		return false;
	}
	if($("#downloadDeadline").val() == null || $("#downloadDeadline").val() == "") {
		$("#downloadDeadline").focus();
		openNotice("${e3ps:getMessage('다운로드 기한을 입력하여 주십시오.')}");
		return false;
	}
	if($("#downloadCount").val() == null || $("#downloadCount").val() == "") {
		$("#downloadCount").focus();
		openNotice("${e3ps:getMessage('다운로드 횟수를 입력하여 주십시오.')}");
		return false;
	}
	// 프로젝트 필수 여부
	/*if(!gridLengthCheck(add_relatedProject_myGridID)){
		openNotice("${e3ps:getMessage('프로젝트를 선택하세요.')}");
		return false;
	}
	var projectValid = AUIGrid.validateGridData(add_relatedProject_myGridID, ["pjtNo"], "${e3ps:getMessage('필수 필드는 반드시 값을 입력해야 합니다.')}");
	
	if(!projectValid) {
		$(add_relatedProject_myGridID)[0].scrollIntoView();
		openNotice("${e3ps:getMessage('추가된 관련 프로젝트에 값을 입력하세요.')}");
		return false;
	}
	*/
	if(!gridLengthCheck(rel_part_myGridID)){
		 openNotice("${e3ps:getMessage('부품을 선택하세요.')}");
		 return false;
	}
	var partValid = AUIGrid.validateGridData(rel_part_myGridID, ["number"], "${e3ps:getMessage('필수 필드는 반드시 값을 입력해야 합니다.')}");
	
	if(!partValid) {
		$(rel_part_myGridID)[0].scrollIntoView();
		openNotice("${e3ps:getMessage('추가된 관련 부품에 값을 입력하세요.')}");
		return false;
	}
	return true;
}
function add_addPartList(list){
	var gridList = AUIGrid.getGridData(rel_part_myGridID);
	
	for(var i=0; i < gridList.length; i++) {
		list = list.filter(function(item, index, arr){
		    return item.oid != gridList[i].oid;
		});
	}

	for(var i=0; i < list.length; i++) {
		var item = list[i];
		if(item.hasOwnProperty("children")) {
			delete item.children;
		}
		AUIGrid.addRow(rel_part_myGridID, item, "last");	
	}
}
</script>
<div class="pop"> 
<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('배포 등록')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->
<form name="distributeForm" id="distributeForm" method="post">
	<input type="hidden" id="location" name="location" value="/Default/Distribute">
	<input type="hidden" id="tempOid" name="tempOid" value="${data.oid}">
	<!-- button -->
	<div class="seach_arm pt20 pb5">
		<div class="leftbt">
			<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('기본 정보')}</span>
		</div>
		<div class="rightbt">
			<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0"/>
			<%-- <button type="button" value="TEMP_STORAGE" class="s_bt03" onclick="save(this)">${e3ps:getMessage('임시 저장')}</button> --%>
			<button type="button"class="s_bt03" onclick="save()">${e3ps:getMessage('등록')}</button>
		</div>
	</div>
	<!-- //button -->
	<div class="pro_table mr30 ml30">
		<table class="mainTable">
			<colgroup>
				<col style="width:15%">
				<col style="width:35%">
				<col style="width:15%">
				<col style="width:35%">
			</colgroup>	
			<tbody>
				<tr>
					<th>${e3ps:getMessage('배포 제목')}<span class="required">*</span></th>
					<td colspan="3">
						<input type="text" class="w100" id="distributeName" name="distributeName">
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('배포 타입')}<span class="required">*</span></th>
					<td>
						<input type="radio" id="distributeTypeEpm" name="distributeType" value="epm" data-rolecode="ECF" checked>
						<label>${e3ps:getMessage('도면')}</label>
					</td>
					<th>${e3ps:getMessage('배포 분류')}<span class="required">*</span></th>
					<td>
						<select class="searchCode" id="classification" name="classification" data-rolecode="ECF" data-codetype="CLASSIFICATION" data-endlevel="2" data-width="70%"></select>
						<span class="pointer verticalMiddle" id="classificationPop" data-rolecode="ECF" onclick="javascript:openCodePopup('classification', 'CLASSIFICATION', this);"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
						<span class="pointer verticalMiddle" onclick="javascript:deleteCode('classification');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('업체')}<span class="required supplierReq">*</span></th>
					<td>
						<input type="hidden" name="supplierName" id="supplierName">
						<select class="searchSupplier" data-width="70%" name="supplierId" id="supplierId" onchange="javascript:setSupplierData(this);"></select>
						<span class="pointer verticalMiddle" onclick="javascript:openSupplierPopup();"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
						<span class="pointer verticalMiddle" onclick="javascript:deleteCode('supplierId');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
					</td>
					<th>${e3ps:getMessage('CAD 타입')}</th>
					<td>
						<input type="checkbox" id="cadType" name="cadType" value="CADCOMPONENT" readonly disabled>
						<label for="cadType">${e3ps:getMessage('3D')}</label>
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('발주 타입')}<span class="required">*</span></th>
					<td>
						<input type="radio" id="orderTypeE" name="orderType" value="E" checked>
						<label>${e3ps:getMessage('견적 발주')}</label>
						<input type="radio" id="orderTypeP" name="orderType" value="P">
						<label>${e3ps:getMessage('구매 발주')}</label>
					</td>
					<th>${e3ps:getMessage('발주 번호')}</th>
					<td>
						<input type="text" class="w100" id="requestNumber" name="requestNumber">
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('다운로드 기한')}<span class="required">*</span></th>
					<td class="calendar">
						<input type="text" class="datePicker w25" name="downloadDeadline" id="downloadDeadline" readonly/>
						<input type="checkbox" id="downloadDeadlineCheck" name="downloadDeadlineCheck" value="infinite">
						<label>${e3ps:getMessage('영구')}</label>
					</td>
					<th>${e3ps:getMessage('다운로드 횟수')}<span class="required">*</span></th>
					<td>
						<input type="number" class="w30" id="downloadCount" name="downloadCount">
						<input type="checkbox" id="downloadCountCheck" name="downloadCountCheck" value="99999">
						<label>${e3ps:getMessage('영구')}</label>
					</td>
				</tr>
				<tr>
	               <th>${e3ps:getMessage('비고')}</th>
	               <td class="pd15" colspan="3">
						<div class="textarea_autoSize">
							<textarea name="description" id="description" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
						</div>
					</td>
	            </tr>
	            <tr> 
	               <th>${e3ps:getMessage('첨부파일')}</th>
	               <td colspan="3">
	                  <jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
	                     <jsp:param name="formId" value="distributeForm"/>
	                     <jsp:param name="command" value="insert"/>
	                     <jsp:param name="btnId" value="createBtn" />
	                  </jsp:include>
	               </td>
	            </tr>
			</tbody>
		</table>
	</div>
	<!-- 관련 프로젝트 지정 include 화면 -->
	<%-- <div class="ml30 mr30">
		<jsp:include page="${e3ps:getIncludeURLString('/project/include_addProject')}" flush="true">
			<jsp:param name="type" value="single"/>
			<jsp:param name="pageName" value="relatedProject"/>
			<jsp:param name="gridHeight" value="200"/>
			<jsp:param name="oid" value="${data.oid}"/>
		</jsp:include>
	</div> --%>
	<!-- 부품 -->
	<div class="ml30 mr30">
	<jsp:include page="${e3ps:getIncludeURLString('/distribute/include_addTempPart')}" flush="true">
		<jsp:param name="oid" value="${data.oid}"/>
		<jsp:param name="gridHeight" value="300"/>
	</jsp:include>
	</div>
</form>
</div>
