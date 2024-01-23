<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	getMaterialTypeList();
	
	getUnitList();
	
	getNumberCodeList("teamType", "JELPROCESSTYPE", false, false);
	
	getMaterialList();
	
	getNumberCodeList("pmCode", "JELPMTYPE", true, true);
	
	getNumberCodeList("PartType", "PARTTYPE", false, true);
	
	getCadTypeListForCreate();
	
	changeType();
	
	setPartNumber();
});

<%----------------------------------------------------------
*                      임시 저장 및 등록 버튼
----------------------------------------------------------%>
function save(btn){
	if(!checkValidate()) {
		return;
	}
	$("#partForm").attr("action",getURLString("/part/createPartAction2"));
	
	var param = new Object();
	
	param["epmContainer"] = $("#container").val();
	
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

function changeType() {
	
	$("#partTable > tbody > tr").each(function() {
		var trId = $(this).attr("id");
		if(trId != "materialTypeTR") {
			$(this).css("display","none");
		}
	});
	
	var materialType = $("#materialType").val();
	
	$("#unit").val("");
	$('#unit').attr("style", "pointer-events: ;");
	
	if(materialType == "P") {
		$("#approvalDiv").css("display", "");
		$("#approvingBtn").css("display", "");
		$("#tempStorageBtn").html("${e3ps:getMessage('임시 저장')}");
		
		$("#heatingJacketTR").css("display","");
		
		var heatingJacketFlag = $("#heatingJacketFlag").val();
		if(heatingJacketFlag == "Y") {
			$("#controlTextTR").css("display","");
		} else {
			$("#stdPartNoTR").css("display","");
			$("#partNoTR").css("display","");
			$("#serialTR").css("display","");
			$("#repairTR").css("display","");	
		}

	} else if(materialType == "A") {
		$("#approvalDiv").css("display", "");
		$("#approvingBtn").css("display", "");
		$("#tempStorageBtn").html("${e3ps:getMessage('임시 저장')}");
		
		$("#controlTR").css("display","");
		
		var controlFlag = $("#controlFlag").val();
		if(controlFlag == "Y") {
			
			$("#controlTextTR").css("display","");
			
		}else{
			$("#stdPartNoTR").css("display","");
			$("#partNoTR").css("display","");
			$("#serialTR").css("display","");
			$("#repairTR").css("display","");
		}		
	
	} else if((materialType == "M") || (materialType == "U")) {

		$("#approvalDiv").css("display", "none");
		$("#approvingBtn").css("display", "none");
		$("#tempStorageBtn").html("${e3ps:getMessage('저장')}");
		
		$("#unit").val("ea");
		$('#unit').attr("style", "pointer-events: none;");
		
		
		$("#teamTypeTR").css("display","");
		$("#processTypeTR").css("display","");
		$("#unitTypeTR").css("display","");
		$("#projectTR").css("display","");
		$("#teamTypeTR").css("display","");
		$("#pmCodeTR").css("display","");
		$("#subCodeTR").css("display","");
		
	}
	
	if(materialType == "M") {
		$("#wtPartType").val("module");
	} else if(materialType == "U") {
		$("#wtPartType").val("unit");
	} else {
		$("#wtPartType").val("separable");
	}
}

function setPartNumber() {
	
	var materialType = $("#materialType").val();
	
	var partNumber = materialType;

	if(materialType == "P") {

		var heatingJacketFlag = $("#heatingJacketFlag").val();
		
		if(heatingJacketFlag == "Y") {
			partNumber += $("#controlTextNo").val();
		} else {
			if($("#stdPartNo").val() != null) {
				partNumber += $("#stdPartNo").val();	
			}
			partNumber += $("#partNo").val();
			partNumber += $("#serial").val();
			if($("#repairCode").val().length > 0) {
				partNumber += "00" + $("#repairCode").val();
			}
		}
		
	} else if(materialType == "A") {
		
		var controlFlag = $("#controlFlag").val();

		if(controlFlag == "Y") {
			
			partNumber += $("#controlTextNo").val();
			
		}else{
			if($("#stdPartNo").val() != null) {
				partNumber += $("#stdPartNo").val();
			}
			partNumber += $("#partNo").val();
			partNumber += $("#serial").val();
			if($("#repairCode").val().length > 0) {
				partNumber += "00" + $("#repairCode").val();
			}
		}		
	
	} else if((materialType == "M") || (materialType == "U")) {

		//partNumber += $("#teamType").val();
		if($("#processTypeNo").val() != null) {
			var processTypeNo = $("#processTypeNo").val();
			partNumber += processTypeNo.substr(processTypeNo.length - 3);
		}
		if($("#unitTypeNo").val() != null) {
			partNumber += $("#unitTypeNo").val();
		}
		partNumber += $("#projectNo").val();
		partNumber += $("#pmCode").val();
		partNumber += $("#subCode").val();
		
	}
	
	$("#partNumber").text(partNumber);
	$("#number").val(partNumber);
}

function checkValidate() {
	
	var materialType = $("#materialType").val();
	
	if(materialType == "P") {
		
		var heatingJacketFlag = $("#heatingJacketFlag").val();
		
		if(heatingJacketFlag == "Y") {
			
			if(!($("#controlTextNo").val().length >= 10 && $("#controlTextNo").val().length <= 12)) {
				$("#controlTextNo").focus();
				openNotice("${e3ps:getMessage('PART 번호를 10~12자리 값으로 입력하여 주십시오.')}");
				return false;
			}
			
		}else{
			if($("#stdPartNo").val() == null || $("#stdPartNo").val() == "") {
				$("#stdPartNo").focus();
				openNotice("${e3ps:getMessage('PART 구분 번호를 선택하여 주십시오.')}");
				return false;
			}
			
			if($("#partNo").val().length != 2) {
				$("#partNo").focus();
				openNotice("${e3ps:getMessage('PART 번호를 2자리 값으로 입력하여 주십시오.')}");
				return false;
			}
			
			if($("#serial").val().length != 3) {
				$("#serial").focus();
				openNotice("${e3ps:getMessage('일련번호를 3자리 값으로 입력하여 주십시오.')}");
				return false;
			}
		}
	} else if(materialType == "A") {
		
		var controlFlag = $("#controlFlag").val();

		if(controlFlag == "Y") {
			
			if(!($("#controlTextNo").val().length >= 10 && $("#controlTextNo").val().length <= 12)) {
				$("#controlTextNo").focus();
				openNotice("${e3ps:getMessage('PART 번호를 10~12자리 값으로 입력하여 주십시오.')}");
				return false;
			}
			
		}else{

			if($("#stdPartNo").val() == null || $("#stdPartNo").val() == "") {
				$("#stdPartNo").focus();
				openNotice("${e3ps:getMessage('PART 구분 번호를 입력하여 주십시오.')}");
				return false;
			}
			
			if($("#partNo").val().length != 2) {
				$("#partNo").focus();
				openNotice("${e3ps:getMessage('PART 번호를 2자리 값으로 입력하여 주십시오.')}");
				return false;
			}
			
			if($("#serial").val().length != 3) {
				$("#paserialrtNo").focus();
				openNotice("${e3ps:getMessage('일련번호를 3자리 값으로 입력하여 주십시오.')}");
				return false;
			}
		}		
	
	} else if((materialType == "M") || (materialType == "U")) {

		if($("#processTypeNo").val() == null || $("#processTypeNo").val() == "") {
			$("#processTypeNo").focus();
			openNotice("${e3ps:getMessage('공정 구분 번호를 선택하여 주십시오.')}");
			return false;
		}
		
		if($("#unitTypeNo").val() == null || $("#unitTypeNo").val() == "") {
			$("#unitTypeNo").focus();
			openNotice("${e3ps:getMessage('P/M UNIT 구분 번호를 선택하여 주십시오.')}");
			return false;
		}
		
		if($("#projectNo").val().length != 8) {
			$("#projectNo").focus();
			openNotice("${e3ps:getMessage('PROJECT 번호를 입력하여 주십시오.')}");
			return false;
		}
		
		if($("#pmCode").val() == null || $("#pmCode").val() == "") {
			$("#pmCode").focus();
			openNotice("${e3ps:getMessage('P/M 번호를 입력하여 주십시오.')}");
			return false;
		}
		
		if($("#subCode").val() != null && $("#subCode").val() != "") {
			if($("#subCode").val().length != 2) {
				$("#subCode").focus();
				openNotice("${e3ps:getMessage('Sub Code를 2자리 값으로 입력하여 주십시오.')}");
				return false;
			}
		}
	}
	
	if($("#name").val() == null || $("#name").val() == "") {
		$("#name").focus();
		openNotice("${e3ps:getMessage('부품명을 입력하여 주십시오.')}");
		return false;
	}
	
	if($("#unit").val() == null || $("#unit").val() == "") {
		$("#unit").focus();
		openNotice("${e3ps:getMessage('단위를 선택하여 주십시오.')}");
		return false;
	}
	
	if($("input:radio[name=epmType]:checked").val() == "design") {
		if($("#cadType").val() == "") {
			$("#cadType").focus();
			openNotice("${e3ps:getMessage('CAD타입을 선택하여 주십시오.')}");
			return false;
		}
	} else if($("input:radio[name=epmType]:checked").val() == "control") {
		if($("[id=PRIMARY]").length == 0){
			openNotice("${e3ps:getMessage('파일을 첨부하여 주십시오.')}");
			return false;
		}
	}
	
	var appLineValid = AUIGrid.validateGridData(app_line_myGridID, ["roleType", "name"], "${e3ps:getMessage('필수 필드는 반드시 값을 입력해야 합니다.')}");
	
	if(!appLineValid) {
		$(app_line_myGridID)[0].scrollIntoView();
		openNotice("${e3ps:getMessage('추가된 결재선에 값을 입력하세요.')}");
		return false;
	}
	
	return true;
}

function setPartCode(id, item){
	$("#" + id).trigger("change");
	$("#" + id).append("<option value='" + item.code + "' selected>" + item.code + " : "+ item.name + "</option>");
	$("#" + id).val(item.code);
	
	setPartNumber();
}

function deletePartCode(id){
	$("#" + id).val("");
	$("#" + id).trigger("change");
	
	setPartNumber();
}

function checkEpm(){
	var value = $("input:radio[name=epmType]:checked").val();
	
	if(value == "none") {
		$("#cadTypeTR").css("display", "none");
		$("#epmLocationTR").css("display", "none");
		$("#epmFileTR").css("display", "none");
	} else if(value == "design") {
		$("#cadTypeTR").css("display", "");
		$("#epmLocationTR").css("display", "");
		$("#epmFileTR").css("display", "none");
		
		$("#container").val("COMMON");
		epmFolder_changeContainer();
	} else if(value == "control") {
		$("#cadTypeTR").css("display", "none");
		$("#epmLocationTR").css("display", "");
		$("#epmFileTR").css("display", "");
		
		$("#container").val("ELECTRIC");
		epmFolder_changeContainer();
	} 
}
</script>
<!-- pop -->
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('부품 등록')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->

	<form name="partForm" id="partForm" method="post">
	<input type="hidden" name="wtPartType"		id="wtPartType"		 	value="separable"     />
	<input type="hidden" name="source"			id="source"	      		value="make"            />
	<input type="hidden" name="lifecycle"   	id="lifecycle"			value="LC_Default"  />
	<input type="hidden" name="view"			id="view"        		value="Design" />
	<!-- button -->
	<div class="seach_arm pt20 pb5">
		<div class="leftbt">
			<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('기본 정보')}</span>
		</div>
		<div class="rightbt">
			<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0"/>
			<button type="button" id="tempStorageBtn" value="TEMP_STORAGE" class="s_bt03" onclick="save(this)">${e3ps:getMessage('임시 저장')}</button>
			<button type="button" id="approvingBtn" value="APPROVING" class="s_bt03" onclick="save(this)">${e3ps:getMessage('등록')}</button>
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
						<table class="mainTable" id="partTable" style="border-top:none">
							<colgroup>
								<col style="width:40%">
								<col style="width:60%">
							</colgroup>	
							<tbody>
								<tr id="materialTypeTR">
									<th>${e3ps:getMessage('부품 분류')}<span class="required">*</span></th>
									<td>
										<select class="w100" id="materialType" name="materialType" onchange="javascript:changeType();setPartNumber();"></select>
									</td>
								</tr>
								<tr id="controlTR" style="display:none;">
									<th>${e3ps:getMessage('제어 여부')}<span class="required">*</span></th>
									<td>
										<select class="w100" id="controlFlag" name="controlFlag" onchange="javascript:changeType();setPartNumber();">
											<option value="Y">Y</option>
											<option value="N" selected>N</option>
										</select>
									</td>
								</tr>
								<tr id="heatingJacketTR" style="display:none;">
									<th>${e3ps:getMessage('Heating Jacket 여부(제어)')}<span class="required">*</span></th>
									<td>
										<select class="w100" id="heatingJacketFlag" name="heatingJacketFlag" onchange="javascript:changeType();setPartNumber();">
											<option value="Y">Y</option>
											<option value="N" selected>N</option>
										</select>
									</td>
								</tr>
								<tr id="controlTextTR" style="display:none;">
									<th>${e3ps:getMessage('PART 번호')}<span class="required">*</span></th>
									<td>
										<input type="text" class="w100" id="controlTextNo" name="controlTextNo" size="12" maxlength="12" onkeyup="javascript:strToUpperCase(this);setPartNumber();"/>
									</td>
								</tr>
								<tr id="stdPartNoTR" style="display:none;">
									<th>${e3ps:getMessage('PART 구분 번호')}<span class="required">*</span></th>
									<td>
										<select class="searchCode" id="stdPartNo" name="stdPartNo" data-codetype="JELSTDPARTCODE" data-endlevel="2" data-width="70%" onchange="javascript:setPartNumber();"></select>
										<span class="pointer verticalMiddle" onclick="javascript:openCodePopup('stdPartNo', 'JELSTDPARTCODE');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
										<span class="pointer verticalMiddle" onclick="javascript:deletePartCode('stdPartNo');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
									</td>
								</tr>
								<tr id="partNoTR" style="display:none;">
									<th>${e3ps:getMessage('PART 번호')}<span class="required">*</span></th>
									<td>
										<input type="text" class="w100" id="partNo" name="partNo" maxlength="2" onkeyup="javascript:strToUpperCase(this);setPartNumber();">
									</td>
								</tr>
								<tr id="serialTR" style="display:none;">
									<th>${e3ps:getMessage('일련 번호')}<span class="required">*</span></th>
									<td>
										<input type="text" class="w100" id="serial" name="serial" maxlength="3" onkeyup="javascript:strToUpperCase(this);setPartNumber();">
									</td>
								</tr>
								<tr id="repairTR" style="display:none;">
									<th>${e3ps:getMessage('Repair 번호')}</th>
									<td>
										<input type="text" class="w100" id="repairCode" name="repairCode" maxlength="2" onkeyup="javascript:strToUpperCase(this);setPartNumber();">
									</td>
								</tr>
								
								<%-- <tr id="teamTypeTR" style="display:none;">
									<th>${e3ps:getMessage('팀 구분')}<span class="required">*</span></th>
									<td>
										<select class="w100" id="teamType" name="teamType" onchange="javascript:setPartNumber();"></select>
									</td>
								</tr> --%>
								<tr id="processTypeTR" style="display:none;">
									<th>${e3ps:getMessage('공정 구분 번호')}<span class="required">*</span></th>
									<td>
										<!-- <input type="text" class="w100" id="processTypeNo" name="processTypeNo"> -->
										<select class="searchCode" id="processTypeNo" name="processTypeNo" data-codetype="PROCESSDIVISIONOLD" data-endlevel="3" data-width="70%" onchange="javascript:setPartNumber();"></select>
										<span class="pointer verticalMiddle" onclick="javascript:openCodePopup('processTypeNo', 'PROCESSDIVISIONOLD');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
										<span class="pointer verticalMiddle" onclick="javascript:deletePartCode('processTypeNo');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
									</td>
								</tr>
								<tr id="unitTypeTR" style="display:none;">
									<th>${e3ps:getMessage('P/M UNIT 구분 번호')}<span class="required">*</span></th>
									<td>
										<!-- <input type="text" class="w100" id="unitTypeNo" name="unitTypeNo"> -->
										<select class="searchCode" id="unitTypeNo" name="unitTypeNo" data-codetype="UNITDIVISIONCODE" data-endlevel="2" data-width="70%" onchange="javascript:setPartNumber();"></select>
										<span class="pointer verticalMiddle" onclick="javascript:openCodePopup('unitTypeNo', 'UNITDIVISIONCODE');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
										<span class="pointer verticalMiddle" onclick="javascript:deletePartCode('unitTypeNo');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
									</td>
								</tr>
								<tr id="projectTR" style="display:none;">
									<th>${e3ps:getMessage('PROJECT 번호')}<span class="required">*</span></th>
									<td>
										<input type="text" class="w100" id="projectNo" name="projectNo" maxlength="8" onkeyup="javascript:strToUpperCase(this);setPartNumber();">
									</td>
								</tr>
								<tr id="pmCodeTR" style="display:none;">
									<th>${e3ps:getMessage('P/M 번호')}<span class="required">*</span></th>
									<td>
										<select class="w100" id="pmCode" name="pmCode" onchange="javascript:setPartNumber();"></select>
									</td>
								</tr>
								<tr id="subCodeTR" style="display:none;">
									<th>Sub Code</th>
									<td>
										<input type="text" class="w100" id="subCode" name="subCode" size="2" maxlength="2" onkeyup="javascript:strToUpperCase(this);setPartNumber();">
									</td>
								</tr>
							</tbody>
						</table>
					</td>
					<td style="padding:0;text-align:center;background-color:#F0F8FF;font: 2em arial, helvetica, sans-serif;color:#191970;">
						<span id="partNumber"></span>
						<input type="hidden" id="number" name="number">
					</td>
				</tr>
			</tbody>
		</table>
		<table class="mainTable mt10">
			<colgroup>
				<col style="width:20%">
				<col style="width:30%">
				<col style="width:20%">
				<col style="width:30%">
			</colgroup>	
			<tbody>
				<tr>
					<th>${e3ps:getMessage('부품 명')}<span class="required">*</span></th>
					<td>
						<input type="text" class="w100" id="name" name="name">
					</td>
					<th>${e3ps:getMessage('단위')}<span class="required">*</span></th>
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
			<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('부품 속성')}</span>
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
					<th>MATERIAL</th>
					<td>
						<input type="text" class="w100" id="MATERIAL" name="MATERIAL">
					</td>
					<th>SPEC</th>
					<td>
						<input type="text" class="w100" id="SPEC" name="SPEC">
					</td>
				</tr>
				<tr>
					<th>WEIGHT</th>
					<td>
						<input type="text" class="w100" id="WEIGHT" name="WEIGHT">
					</td>
					<th></th>
					<td>
					</td>
				</tr>
				<tr>
					<th>DESCRIPTION</th>
					<td colspan="3" class="pd15">
						<div class="textarea_autoSize">
							<textarea name="DESCRIPTION" id="DESCRIPTION" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<!-- 부품 검색 include 화면 -->
	<div class="ml30 mr30">
		<jsp:include page="${e3ps:getIncludeURLString('/part/include_searchPartList')}" flush="true">
			<jsp:param name="gridHeight" value="200"/>
		</jsp:include>
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
						<input type="radio" name="epmType" value="none" onclick="checkEpm();" checked><label>${e3ps:getMessage('도면 없음')}</label>
						<input type="radio" name="epmType" value="design" onclick="checkEpm();"><label>${e3ps:getMessage('설계 도면')}</label>
						<input type="radio" name="epmType" value="control" onclick="checkEpm();"><label>${e3ps:getMessage('제어 도면')}</label>
					</td>
				</tr>
				<tr id="cadTypeTR" style="display:none;">
					<th>${e3ps:getMessage('CAD 타입')}<span class="required">*</span></th>
					<td>
						<select id="cadType" name="cadType" style="width:100px"></select>
					</td>
				</tr>
				<tr id="epmLocationTR" style="display:none;">
					<th>${e3ps:getMessage('위치')}<span class="required">*</span></th>
					<td>
						<div style="position:relative">
							<input type="text" id="epmLocationDisplay" name="epmLocationDisplay" class="w60" disabled> <button type="button" class="s_bt03" id="toggleBtn" data-folderListId="epmFolderList" data-folderposition="up" onclick="toggleFolderList(this)">목록</button>
							<input type="hidden" id="epmLocation" name="epmLocation" value="">
							<div id="epmFolderList" style="display:none;position:absolute;z-index:999">
								<!-- tree -->
								<jsp:include page="${e3ps:getIncludeURLString('/common/include_folderTree')}" flush="true">
									<jsp:param name="container" value="worldex"/>
									<jsp:param name="renderTo" value="epmFolder"/>
									<jsp:param name="formId" value="partForm"/>
									<jsp:param name="locationId" value="epmLocation"/>
									<jsp:param name="rootLocation" value="/Default"/>
									<jsp:param name="gridHeight" value="300"/>
									<jsp:param name="gridWidth" value="250"/>
								</jsp:include>
								<!-- //tree -->
							</div>
						</div>
					</td>
				</tr>
				<tr id="epmFileTR" style="display:none;">
					<th>${e3ps:getMessage('주첨부파일')}<span class="required">*</span></th>
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
	<div id="approvalDiv" class="ml30 mr30" style="display:none;">
		<jsp:include page="${e3ps:getIncludeURLString('/approval/include_addApprovalLine')}" flush="true">
			<jsp:param name="gridHeight" value="200"/>
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
