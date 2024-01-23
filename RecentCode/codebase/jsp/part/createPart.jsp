<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript">
$(document).ready(function(){
	
	partCreateNumberProp = getCreateNumberPropList('PARTNUMBERPROP');
	dataInit();
	
});

var partCreateNumberProp = {};

function dataInit(){
	let keys = Object.keys(partCreateNumberProp);
	keys.forEach((item) => {
		const codeList = partCreateNumberProp[item];
		const eleID = 'p_' + item.toLowerCase();
		if(document.getElementById(eleID)){
			document.getElementById(eleID).innerHTML += "<option value=''>${e3ps:getMessage('선택')}</option>"
			codeList.forEach((x) => {
				document.getElementById(eleID).innerHTML += "<option value='" + x.code + "'>" + x.displayOptionTag + "</option>"
			})
		}
	})
}

function changeHandler(){
	
	let partProp = '';
	
	document.querySelectorAll('.partProperties').forEach((item) => {
		let selectValue = item.value;
		if(selectValue) partProp += selectValue;
	})
	
	if(partProp.length == 5){
		partProp += '-' + getSeqenceNo(partProp + '-', "0000", "WTPartMaster", "WTPartNumber") + 'A';
	}
	
	document.getElementById('partNumber').innerText = partProp;
	document.getElementById('number').value = partProp;
}

<%----------------------------------------------------------
*                      임시 저장 및 등록 버튼
----------------------------------------------------------%>
function save(btn){
	
	if(!validationCheck(true)){
		return;	
	}
		
	//let approvalList = AUIGrid.getGridData(app_line_myGridID);
	let relatedDocList = AUIGrid.getGridData(add_relatedDoc_myGridID);
	
	let appState = btn.value;
	var msg = "";
			msg = "${e3ps:getMessage('등록하시겠습니까?')}";	
	
// 	if(appState == "APPROVING") {
// 		if(checkApproveLine()) {
// 		} else {
// 			return;
// 		}
// 	} else if(appState == "TEMP_STORAGE") {
// 		msg = "${e3ps:getMessage('임시저장하시겠습니까?')}";
// 	}
	
	if(!confirm(msg)){
		return;
	}
	
	
	let param = new Object(); 
	param = getFormParams('partForm', param)
	param.appState = appState;
// 	param.approvalList = approvalList;
	param.relatedDocList = relatedDocList;
	console.log("param",param)
	
	
	var url	= getURLString("/part/createPartAction");
	ajaxCallServer(url, param, function(){}, true);
	
}

function validationCheck(isSave){
	let relatedDocList = AUIGrid.getGridData(add_relatedDoc_myGridID);
	let result = false;
	
	if(isEmpty(getElementValue('p_unit'))){
		alert("Unit ${e3ps:getMessage('을 선택해주세요')}.");
	}else if(isEmpty(getElementValue('p_material'))){
		alert("Material ${e3ps:getMessage('을 선택해주세요')}.");
	}else if(isEmpty(getElementValue('p_equipment'))){
		alert("Equipment ${e3ps:getMessage('을 선택해주세요')}.");
	}else if(isEmpty(getElementValue('p_product'))){
		alert("Product ${e3ps:getMessage('을 선택해주세요')}.");
	}else if(isEmpty(getElementValue('p_inch'))){
		alert("Inch ${e3ps:getMessage('을 선택해주세요')}.");
	}else if(isSave && isEmpty(getElementValue('name'))){
		alert("${e3ps:getMessage('부품명을 입력해주세요')}.");
	}else if(relatedDocList.length > 0 && relatedDocList.some(x=>isEmpty(x.oid))){
		alert("${e3ps:getMessage('관련 문서를 검색하여 추가해주세요')}.");
	}else{
		result = true;
	}
		
	return result;	
}

function checkApproveLine(){
	
	let approvalList = AUIGrid.getGridData(app_line_myGridID);
	
	let checkApproval = false;
	
	if(approvalList.length == 0){
		alert("${e3ps:getMessage('결재선을 지정해주세요')}");
			
	}else if(!approvalList.some((item) => item.roleType=='APPROVE')){
		alert("${e3ps:getMessage('결재 승인자를 지정해주세요')}");
			
	}else{
		checkApproval = true;
	} 
	
	return checkApproval;
}


function setPartNumber(){
	if(!validationCheck()){
		return;	
	}
	changeHandler();
}


function getSeqenceNo(partNumber, format, tableName, columnName) {
	var url	= getURLString("/common/getSequenceNo");
	
	var param = new Object();
	
	param["partNumber"] = partNumber;
	param["format"] = format;
	param["tableName"] = tableName;
	param["columnName"] = columnName;

	var data = ajaxCallServer(url, param, null);
	
	return data.sequence;
}

</script>
<div class="product"> 
<form name="partForm" id="partForm" method="post">
	<input type="hidden" name="wtPartType"		id="wtPartType"		 	value="separable"     />
	<input type="hidden" name="source"			id="source"	      		value="make"            />
	<input type="hidden" name="lifecycle"   	id="lifecycle"			value="LC_Default"  />
	<input type="hidden" name="view"			id="view"        		value="Design" />
	<input type="hidden" name="location"		id="location"			value="" />
	<!-- button -->
	<div class="seach_arm pt20 pb5">
		<div class="leftbt">
			<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png"> ${e3ps:getMessage('기본 정보')}</span>
		</div>
		<div class="rightbt">
			<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0"/>
			<%-- <button type="button" id="tempStorageBtn" value="TEMP_STORAGE" class="s_bt03" onclick="save(this)">${e3ps:getMessage('임시 저장')}</button> --%>
			<button type="button" id="approvingBtn" value="TEMP_STORAGE" class="s_bt03" onclick="save(this)">${e3ps:getMessage('등록')}</button>
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
					<td style="padding:0;" colspan="2">
						<table class="mainTable" id="partTable" style="border-top:none">
							<colgroup>
								<col style="width:40%">
								<col style="width:60%">
							</colgroup>	
							<tbody>
								<tr>
									<th>Unit<span class="required">*</span></th>
									<td>
										<select class="w50 partProperties" id="p_unit" name="p_unit" onchange="changeHandler()"></select>
									</td>
								</tr>
								<tr>
									<th>Material<span class="required">*</span></th>
									<td>
										<select class="w50 partProperties" id="p_material" name="p_material" onchange="changeHandler()"></select>
									</td>
								</tr>
								<tr>
									<th>Equipment<span class="required">*</span></th>
									<td>
										<select class="w50 partProperties" id="p_equipment" name="p_equipment" onchange="changeHandler()"></select>
									</td>
								</tr>
								<tr>
									<th>Product<span class="required">*</span></th>
									<td>
										<select class="w50 partProperties" id="p_product" name="p_product" onchange="changeHandler()"></select>
									</td>
								</tr>
								<tr>
									<th>Inch<span class="required">*</span></th>
									<td>
										<select class="w50 partProperties" id="p_inch" name="p_inch" onchange="changeHandler()"></select>
									</td>
								</tr>
								<tr>
									<th>${e3ps:getMessage('부품 명')}<span class="required">*</span></th>
									<td>
										<input type="text" class="w100" id="name" name="name" oninvalid="alert('40자제한')" maxlength="40" placeholder="${e3ps:getMessage('40자 이내로 작성해 주세요')}">
									</td>
								</tr>
							</tbody>
						</table>
					</td>
					<td colspan="2" style="padding:0;text-align:center;background-color:#F0F8FF;font: 2em arial, helvetica, sans-serif;color:#191970;">
						<span id="partNumber"></span><br>
						<button type="button" id="setNumber" value="setNumber" class="s_bt03" onclick="setPartNumber();">${e3ps:getMessage('채번')}</button>
						<input type="hidden" id="number" name="number">
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<div class="seach_arm pt20 pb5">
		<div class="leftbt">
			<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png"> ${e3ps:getMessage('속성 정보')}</span>
		</div>
		<div class="rightbt">
		</div>
	</div>
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
					<th>${e3ps:getMessage('외경')}</th>
					<td><input type="text" class="w50" id="external_diameter" name="external_diameter"></td>
					<th>${e3ps:getMessage('내경')}</th>
					<td><input type="text" class="w50" id="internal_diameter" name="internal_diameter"></td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('두께')}</th>
					<td><input type="text" class="w50" id="thickness" name="thickness"></td>
					<th>${e3ps:getMessage('홀 Dia')}</th>
					<td><input type="text" class="w50" id="hall_dia" name="hall_dia"></td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('홀 개수')}</th>
					<td><input type="text" class="w50" id="hall_count" name="hall_count"></td>
					<th>${e3ps:getMessage('Remark')}</th>
					<td><input type="text" class="w50" id="remark" name="remark"></td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('비고')}</th>
					<td colspan="3"><input type="text" class="w85" id="description" name="description"></td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<!-- button -->
	<%-- <div class="seach_arm pt20 pb5">
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
				<col style="width:13%">
				<col style="width:20%">
				<col style="width:13%">
				<col style="width:20%">
				<col style="width:13%">
				<col style="width:21%">
			</colgroup>	
			<tbody>
				<tr>
					<th>${e3ps:getMessage('단위')}<span class="required">*</span></th>
					<td>
						<select class="w100" id="unit" name="unit"></select>
					</td>
					<th>${e3ps:getMessage('후처리')}</th>
					<td>
						<input type="text" class="w95" id="treatment" name="treatment">
					</td>
					<th>장비 시리얼 번호</th>
					<td>
						<input type="text" class="w95" id="serialNo" name="serialNo">
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('분류')}</th>
					<td>
						<input type="text" class="w95" id="category" name="category">
					</td>
					<th>${e3ps:getMessage('재질')}</th>
					<td>
						<input type="text" class="w95" id="material" name="material">
					</td>
					<th colspan="3"></th>
				</tr>
			</tbody>
		</table>
	</div> --%>
	
	<!-- 부품 검색 include 화면 -->
<!-- 	<div class="ml30 mr30"> -->
<%-- 		<jsp:include page="${e3ps:getIncludeURLString('/part/include_searchPartList')}" flush="true"> --%>
<%-- 			<jsp:param name="gridHeight" value="200"/> --%>
<%-- 		</jsp:include> --%>
<!-- 	</div> -->
	
	<div class="ml30 mr30">
      <jsp:include page="${e3ps:getIncludeURLString('/common/include_addObject')}" flush="true">
         <jsp:param name="objType" value="doc"/>
         <jsp:param name="pageName" value="relatedDoc"/>
         <jsp:param name="title" value="${e3ps:getMessage('관련 문서')}"/>
         <jsp:param name="gridHeight" value="200"/>
      </jsp:include>
   </div>
	
	<!-- 결재선 지정 include 화면 -->
	<%-- <div class="ml30 mr30">
		<jsp:include page="${e3ps:getIncludeURLString('/approval/include_addApprovalLine')}" flush="true">
			<jsp:param name="gridHeight" value="200"/>
		</jsp:include>
	</div> --%>
</form>
</div>
