<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript">
$(document).ready(function(){
	
	getUnitList();
	
	getMaterialList();
	
	getNumberCodeList("PartType", "PARTTYPE", false, true);
	
	getCadTypeListForCreate();
});

<%----------------------------------------------------------
*                      임시 저장 및 등록 버튼
----------------------------------------------------------%>
function save(btn){
	if(!checkValidate()) {
		return;
	}
	$("#partForm").attr("action",getURLString("/part/createPartAction"));
	
	var param = new Object();

	//유효성 검사
	var isValid = AUIGrid.validateGridData(app_line_myGridID, ["roleType", "name"], "${e3ps:getMessage('필수 필드는 반드시 값을 입력해야 합니다.')}");
	
	if(isValid) {
		
		//결재선 지정 리스트
		var approvalList = AUIGrid.getGridData(app_line_myGridID);
		
		var appState = $(btn).val();
		param["appState"] = appState;
		param["approvalList"] = approvalList;
		
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
		
		formSubmit("partForm", param, msg, null, true);
	}
}

function setPartNumber(partNumber) {
	
	partNumber += "00";
	
	$("#tempNumber").val(partNumber);
	
	$("#partNumber").text(partNumber);
	$("#number").val(partNumber);
}

function setRepairPartNumber() {
	
	var tempNumber = $("#tempNumber").val();
	
	tempNumber += $("#repairCode").val();
	
	$("#partNumber").text(tempNumber);
	$("#number").val(tempNumber);
}

function checkValidate() {
	
	if($("#repairCode").val().length != 2) {
		openNotice("${e3ps:getMessage('Repair 번호를 2자리 값으로 입력하여 주십시오.')}");
		return false;
	}
	
	if($("#name").val() == null || $("#name").val() == "") {
		openNotice("${e3ps:getMessage('부품명을 입력하여 주십시오.')}");
		return false;
	}
	
	if($("#unit").val() == null || $("#unit").val() == "") {
		openNotice("${e3ps:getMessage('단위를 선택하여 주십시오.')}");
		return false;
	}
	
	if($("input:radio[name=epmType]:checked").val() == "design") {
		if($("#epmCategory").val() == "") {
			openNotice("${e3ps:getMessage('범주를 선택하여 주십시오.')}");
			return false;
		}
	} else if($("input:radio[name=epmType]:checked").val() == "design") {
		/* if($("#PRIMARY").val() == "") {
			openNotice("${e3ps:getMessage('파일을 첨부하여 주십시오.')}");
			return false;
		} */
	}
	
	return true;
}

function checkEpm(){
	var value = $("input:radio[name=epmType]:checked").val();
	
	if(value == "none") {
		$("#epmCategoryTR").css("display", "none");
		$("#epmLocationTR").css("display", "none");
		$("#epmFileTR").css("display", "none");
	} else if(value == "design") {
		$("#epmCategoryTR").css("display", "");
		$("#epmLocationTR").css("display", "");
		$("#epmFileTR").css("display", "none");
	} else if(value == "control") {
		$("#epmLocationTR").css("display", "");
		$("#epmFileTR").css("display", "");
	} 
}
</script>
<div class="product"> 
<form name="partForm" id="partForm" method="post">
	<input type="hidden" name="wtPartType"		id="wtPartType"		 	value="separable"     />
	<input type="hidden" name="source"			id="source"	      		value="make"            />
	<input type="hidden" name="lifecycle"   	id="lifecycle"			value="LC_Default"  />
	<input type="hidden" name="view"			id="view"        		value="Design" />
	
	<!-- 부품 검색 include 화면 -->
	<div class="ml30 mr30">
		<jsp:include page="${e3ps:getIncludeURLString('/part/include_searchPartListForRepair')}" flush="true">
			<jsp:param name="gridHeight" value="200"/>
		</jsp:include>
	</div>
	
	<!-- button -->
	<div class="seach_arm pt20 pb5">
		<div class="leftbt">
			<h4>${e3ps:getMessage('기본 정보')}</h4>
		</div>
		<div class="rightbt">
			<button type="button" value="TEMP_STORAGE" class="s_bt03" onclick="save(this)">${e3ps:getMessage('임시 저장')}</button>
			<button type="button" value="APPROVING" class="s_bt03" onclick="save(this)">${e3ps:getMessage('등록')}</button>
		</div>
	</div>
	<!-- //button -->
	<div class="pro_table mr30 ml30">
		<table class="mainTable">
			<colgroup>
				<col style="width:55%">
				<col style="width:45%">
			</colgroup>	
			<tbody>
				<tr>
					<td style="padding:0;">
						<table id="partTable" style="border-top:none">
							<colgroup>
								<col style="width:40%">
								<col style="width:60%">
							</colgroup>	
							<tbody>
								<tr id="repairTR">
									<th>${e3ps:getMessage('Repair 번호')}</th>
									<td>
										<input type="text" class="w100" id="repairCode" name="repairCode" maxlength="2" onkeyup="javascript:strToUpperCase(this);setRepairPartNumber();">
									</td>
								</tr>
							</tbody>
						</table>
					</td>
					<td style="padding:0;text-align:center;background-color:#F0F8FF;font: 2em arial, helvetica, sans-serif;color:#191970;">
						<span id="partNumber"></span>
						<input type="hidden" id="tempNumber" name="tempNumber">
						<input type="hidden" id="number" name="number">
					</td>
				</tr>
			</tbody>
		</table>
		<table class="mt10">
			<colgroup>
				<col style="width:20%">
				<col style="width:30%">
				<col style="width:20%">
				<col style="width:30%">
			</colgroup>	
			<tbody>
				<tr>
					<th>${e3ps:getMessage('부품 명')}</th>
					<td>
						<input type="text" class="w100" id="name" name="name">
					</td>
					<th>${e3ps:getMessage('단위')}</th>
					<td>
						<select class="w100" id="unit" name="unit"></select>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<!-- button -->
	<div class="seach_arm pt20 pb5">
		<div class="leftbt">
			<h4>${e3ps:getMessage('부품 속성')}</h4>
		</div>
		<div class="rightbt">
		</div>
	</div>
	<!-- //button -->
	<div class="pro_table mr30 ml30">
		<table class="mainTable">
			<colgroup>
				<col style="width:20%">
				<col style="width:30%">
				<col style="width:20%">
				<col style="width:30%">
			</colgroup>	
			<tbody>
				<tr>
					<th>${e3ps:getMessage('자재 타입')}</th>
					<td>
						<select class="w100" id="PartType" name="PartType"></select>
					</td>
					<th>MATERIAL</th>
					<td>
						<select class="w100" id="MATERIAL" name="MATERIAL"></select>
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('표면 처리')}</th>
					<td>
						<input type="text" class="w100" id="SurfaceTreatment" name="SurfaceTreatment">
					</td>
					<th>Maker</th>
					<td>
						<input type="text" class="w100" id="Maker" name="Maker">
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('SN 관리 여부')}</th>
					<td>
						<input type="checkbox" id="SNOCK" name="SNOCK" value="Y">Y
					</td>
					<th>${e3ps:getMessage('참조 부품')}</th>
					<td>
						<input type="text" class="w100" id="CPARTNUM" name="CPARTNUM">
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('개요')}</th>
					<td colspan="3" class="pd15">
						<div class="textarea_autoSize">
							<textarea name="Summary" id="Summary" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<!-- 도면 추가 기능  -->
	<!-- button -->
	<div class="seach_arm pt20 pb5">
		<div class="leftbt">
			<h4>${e3ps:getMessage('도면 생성')}</h4>
		</div>
		<div class="rightbt">
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
					<th>${e3ps:getMessage('도면 분류')}</th>
					<td>
						<div class="radio-group2">
							<input type="radio" id="epmType" name="epmType" value="none" onclick="checkEpm()" checked>
							<label>${e3ps:getMessage('도면 없음')}</label>
							<input type="radio" id="epmType" name="epmType" value="design" onclick="checkEpm()">
							<label>${e3ps:getMessage('설계 도면')}</label>
							<input type="radio" id="epmType" name="epmType" value="control" onclick="checkEpm()">
							<label>${e3ps:getMessage('제어 도면')}</label>
						</div>
					</td>
				</tr>
				<tr id="epmCategoryTR" style="display:none;">
					<th>${e3ps:getMessage('범주')}</th>
					<td>
						<select id="cadType" name="cadType" style="width:100px"></select>
					</td>
				</tr>
				<tr id="epmLocationTR" style="display:none;">
					<th>${e3ps:getMessage('위치')}</th>
					<td>
						<div style="position:relative">
							<input type="text" id="epmLocationDisplay" name="epmLocationDisplay" class="w60" disabled> <button type="button" class="s_bt03" id="toggleBtn" data-folderListId="epmFolderList" onclick="toggleFolderList(this)">목록</button>
							<input type="hidden" id="epmLocation" name="epmLocation" value="">
							<div id="epmFolderList" style="display:none;position:absolute;z-index:999">
								<!-- tree -->
								<jsp:include page="${e3ps:getIncludeURLString('/common/include_folderTree')}" flush="true">
									<jsp:param name="container" value="worldex"/>
									<jsp:param name="renderTo" value="epmFolder"/>
									<jsp:param name="formId" value="partForm"/>
									<jsp:param name="locationId" value="epmLocation"/>
									<jsp:param name="rootLocation" value="/Default"/>
									<jsp:param name="autoGridHeight" value="true"/>
								</jsp:include>
								<!-- //tree -->
							</div>
						</div>
					</td>
				</tr>
				<tr id="epmFileTR" style="display:none;">
					<th>${e3ps:getMessage('주첨부파일')}</th>
					<td>
						<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
		                    <jsp:param name="formId" value="partForm"/>
		                    <jsp:param name="command" value="insert"/>
	    	                <jsp:param name="type" value="PRIMARY"/>
	        	            <jsp:param name="btnId" value="createBtn" />
	                	</jsp:include>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<!-- 결재선 지정 include 화면 -->
	<div class="ml30 mr30">
		<jsp:include page="${e3ps:getIncludeURLString('/approval/include_addApprovalLine')}" flush="true">
			<jsp:param name="gridHeight" value="200"/>
		</jsp:include>
	</div>
</form>
</div>
