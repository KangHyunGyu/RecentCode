<%@page import="com.e3ps.common.util.CommonUtil"%>
<%@page import="com.e3ps.change.EChangeActivityDefinitionRoot"%>
<%@page import="java.util.List"%>
<%@page import="com.e3ps.change.service.ChangeECOHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script src="/Windchill/jsp/js/popup.js"></script>
<script src="/Windchill/jsp/js/checkbox.js"></script>
<!-- jquery-ui -->
<link rel="stylesheet" type="text/css" href="/Windchill/jsp/js/jquery/jquery-ui-1.11.4.redmond/jquery-ui.css">
<link rel="stylesheet" type="text/css" href="/Windchill/jsp/js/jquery/jquery-ui-1.11.4.redmond/jquery-ui.structure.css">
<link rel="stylesheet" type="text/css" href="/Windchill/jsp/js/jquery/jquery-ui-1.11.4.redmond/jquery-ui.theme.css">
<script type="text/javascript" src="/Windchill/jsp/js/jquery/jquery-ui-1.11.4.redmond/jquery-ui.min.js"></script>

<script type="text/javascript">
$(document).ready(function(){
	document.querySelectorAll("input[name=applyDate][type=radio]").forEach((ele)=>{
		ele.addEventListener('click',(evt) => {
			let target = evt.target;
			if(target.value == 'specificDate'){
				document.querySelector("input[name=applyDate][type=text]").disabled=false;
			}else{
				document.querySelector("input[name=applyDate][type=text]").disabled=true;
			}
		})
	})
	
	//$("#rootOid").val() = rootOid;
	getRootList2();
	
	setDefaultUser("changeOwner");
	
	setAutoECRData("${ecrOid}");
	
});

let searchObj = [];
var ecrOid;
var ecrDateName;
var specificDate;
var ecaList;

function setDefaultUser(id){
	var oid = "${peo.oid}";
	var name = "${peo.name}";
	$("#" + id).append("<option value='" + oid + "' selected>" + name + "</option>");
	$("input[name=" + id + "]").val(oid);
}

function setAutoECRData(ecrOid){
	if("" == ecrOid){
		return;
	}else{
		// 내용 
		ecrContentGetGridData(ecrOid);
		
		//data set
		setEcrAutoData(ecrOid);
		//ecrDateName = item.applyDateName;
		//specificDate = item.specificDate;
		
		//dataInit_ServiceRe(ecrDateName, specificDate);
	}
}

function setEcrAutoData(ecrOid){
	var url = getURLString("/change/getECRData");
	var param = new Object(); 
	param.oid = ecrOid;
	
	ajaxCallServer(url, param, function(data){
		var ecrD = data.ecrData;
// 		console.log("==>> " + ecrD);
		console.log('ecrD :: ' , ecrD);
		
		//searchObj = { id : ecrD.oid, text : ecrD.name };
		//$("#relatedECR").value(ecrD.oid);
		//$("#relatedECR").trigger("change");
		setRelatedECR(ecrD);
		setRelatedValue(ecrD);
	});
}

function setRelatedValue(item){
	var optionHtml = "<option value='" + item.oid + "'>" + item.name + "</option>";
    $("#relatedECR").append(optionHtml);
}

function changeHandler(target){
	let value = target.value;
	let ecrItem = searchObj.find(item => item.oid == value);
	
	setRelatedECR(ecrItem);
}

function setRelatedECR(item) {
	console.log("=== > " + item);
	
	//document.getElementById('relatedECR').text = item.name?item.name:'';
    //document.getElementById('relatedECR').value = item.oid?item.oid:'';
	
    document.getElementById('ecrNumber').innerText = item.ecrNumber?item.ecrNumber:'';
	document.getElementById('customerTD').innerText = item.customer?item.customer:'';
	document.getElementById('customer').value = item.customer ? item.customer:'';
	document.getElementById('echangeReason').value = item.echangeReason?item.echangeReason:'';
	document.getElementById('description').innerText = item.description?item.description:'';
	
	document.getElementById('completeHopeDate').innerText = item.completeHopeDate?item.completeHopeDate:'';
	document.getElementById('equipmentName').innerText = item.equipmentName?item.equipmentName:'';
	
	
	ecrOid = item.oid;
	ecrDateName = item.applyDateName;
	specificDate = item.specificDate;
	
	ecrContentGetGridData(ecrOid);
	dataInit_ServiceRe(ecrDateName, specificDate);
	//setRelatedPart(item.partList);
}

function setRelatedPart(data){
	AUIGrid.setGridData(add_relatedPart_myGridID, data);
	AUIGrid.setAllCheckedRows(add_relatedPart_myGridID, false);
	AUIGrid.removeAjaxLoader(add_relatedPart_myGridID);
}

function dataInit_ServiceRe(date, specificDate){
	let aDate = date;
	
	if(aDate === "재고소진 후") {
        const outOfStockRadio = document.querySelector("input[name='applyDate'][value='outOfStock']");
        outOfStockRadio.checked = true;
    } else if(aDate == "즉시") {
    	const immediatelyRadio = document.querySelector("input[name='applyDate'][value='immediately']");
    	immediatelyRadio.checked = true;
    } else {
    	document.querySelector("input[name=applyDate][type=text]").disabled=false;
    	document.querySelector("input[id=applyDate][type=text]").value = specificDate;
    	const specificDateRadio = document.querySelector("input[name='applyDate'][value='specificDate']");
    	specificDateRadio.checked = true;
    }
	
}

function changeActivityData(roid) {
	var param = new Object();
	
	param["rootOid"] = "${param.root}";
	if(roid != null){
		param["rootOid"] = roid;
	}
	var url = getURLString("/admin/getActivityStepList");
	ajaxCallServer(url, param, function(data){
		ecaList = data.list;
	});
	
}

function spaceCheck(val) {
	return !val || /^\s*$/.test(val);
}

function saveEco(ele){
	
	var appState = ele.value;
	const ecr_contents_list = AUIGrid.getGridData(ecr_content_gridID);
	const approvalList = AUIGrid.getGridData(app_line_myGridID);
	let partList = AUIGrid.getGridData(add_relatedPart_myGridID);
	
	let applyDateCalendar = document.querySelector("input[name=applyDate][type=text]");
	let finishDateCalendar = document.querySelector("input[name=finishDate][type=text]");
	
	var rer = true;
	
	if(spaceCheck(getElementValue('relatedECR'))){
		alert("${e3ps:getMessage('관련 ECR을 입력해 주세요')}");
		
	}else if(spaceCheck(getElementValue('name'))){
		alert("${e3ps:getMessage('ECO 명을 입력해 주세요')}");
		
	}else if(spaceCheck(getElementValue('ecoChangeOption'))){
		alert("${e3ps.getMessage('설변대상 옵션을 선택해주세요.')}");
		
	}else if(spaceCheck(getElementValue('changeOwner'))){
		alert("${e3ps:getMessage('설계 변경 담당자를 입력해 주세요')}");
		
	}else if(spaceCheck(getElementValue('echangeReason'))){
		alert("${e3ps:getMessage('설계 변경 사유를 입력해 주세요')}");
		
	}else if(finishDateCalendar.value == 0){
		alert("${e3ps:getMessage('완료 예정일을 선택해 주세요')}");
		
	}else if(ecr_contents_list.length == 0){
		alert("${e3ps:getMessage('변경 내용을 추가해 주세요')}");
		
	}else if(ecr_contents_list.some(x => spaceCheck(x.name) || spaceCheck(x.contents))){
		alert("${e3ps:getMessage('변경 내용을 작성해 주세요')}");
		
	}else if(!applyDateCalendar.disabled && applyDateCalendar.value == 0){
		alert("${e3ps:getMessage('적용 시점 특정 일자를 선택해 주세요')}");
		
	}else if(partList.length == 0){
		alert("${e3ps:getMessage('관련 품목을 추가해 주세요')}");
		
	}else if(appState == "APPROVING" && approvalList.length == 0){
		alert("${e3ps:getMessage('결재선을 지정해 주세요')}");
		
	}else if(appState == "APPROVING" && !approvalList.some((item) => item.roleType=='APPROVE')){
		alert("${e3ps:getMessage('결재 승인자를 지정해 주세요')}");
	}else{
		rer = false;
	}
		
	if(rer){
		return;
	}
		
	if(!confirm("${e3ps:getMessage('등록하시겠습니까?')}")){
		return;
	}
	
	let param = new Object(); 
	param = getFormParams('ecoForm', param)
	param.appState = appState;
	param.applyDate = typeof param.applyDate == 'string'? param.applyDate:param.applyDate.join(',');
	param.ecrContentsList = ecr_contents_list;
	param.approvalList = approvalList;
	param.ecaList = ecaList;
	param.partList = partList;
	console.log(param);
	
	var url	= getURLString("/change/createECOAction");
	ajaxCallServer(url, param, function(){}, true);
}

function getRootList2() {
	var url	= getURLString("/admin/getRootList");
	
	var param = new Object();
	
	param["changeType"] = $("#changeType").val();
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	var rootOid = list[1].key;
	
	changeActivityData(rootOid);
}

function setECRData(obj){
	
	//alert($('#priority > option:selected').val());
	var oid = obj.value;//$('#relatedEO > option:selected').val();
	
	if(isEmpty(oid)){
		return;
	}
	
	//console.log("eoNumber =" + eoNumber);
	var param = new Object();
	param.oid = oid;
	
	var url	= getURLString("/change/setECRData");
	var data = ajaxCallServer(url, param, null);
	
	console.log(data);
	
// 	var ecrData = data.ecrData;
	
// 	var isECOCheckPart = ecrData.isECOCheckPart;     //품목에 대한 정합성 true, false
// 	var ecoCheckPartList = ecrData.ecoCheckPartList; //false 인경우 품목 
	
// 	console.log("isECOCheckPart =" + isECOCheckPart);
// 	console.log("ecoCheckPartList =" + ecoCheckPartList);
	
// 	if(!isECOCheckPart){
// 		alert(ecoCheckPartList);
		
// 		deleteUser("relatedEO"); //ECR 삭제
// 		return;
// 	}
	
	// 그리드에 데이터 세팅
	AUIGrid.setGridData(add_relatedPart_myGridID, data.partList);
	AUIGrid.setAllCheckedRows(add_relatedPart_myGridID, false);
	AUIGrid.removeAjaxLoader(add_relatedPart_myGridID);
}

</script>
<div class="product"> 
	<form name="ecoForm" id="ecoForm" method="post" enctype="multipart/form-data">
		<input type="hidden" id ="rOid" name="rOid" value=""/>
		<input type="hidden" id="customer" name="customer" value=""/>
	
		<!-- START -->
		<div class="seach_arm pt20 pb5">
			<div class="leftbt">
				<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage(' ECO 등록')}</span>
			</div>
			<div class="rightbt">
				<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0"/>
				<button type="button" value="TEMP_STORAGE" class="s_bt03" onclick="saveEco(this)">${e3ps:getMessage('임시등록')}</button>
				<button type="button" value="APPROVING" class="s_bt03" onclick="saveEco(this)">${e3ps:getMessage('결재요청')}</button>
			</div>
		</div>
	
	
		<div class="pro_table mr30 ml30">
			<table class="mainTable">
				<colgroup>
					<col width="10%">
					<col width="40%">
					<col width="10%">
					<col width="40%">
				</colgroup>
				<tr>
					<th>${e3ps:getMessage('관련 ECR')}<span style="color:red;">*</span></th>
					<td colspan="3"	>
						<div class="pro_view"> 
							<select class="searchRelatedECR" id="relatedECR"  name="relatedECR"  data-width="50%" onchange="changeHandler(this);"></select>
							<span class="pointer verticalMiddle" onclick="javascript:openSearchECRPopup('relatedECR', 'single');">
								<img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png" />
							</span>
							<span class="pointer verticalMiddle" onclick="javascript:deleteUser('relatedECR');">
								<img class="verticalMiddle" src="/Windchill/jsp/portal/images/delete_icon.png" />
							</span>
						</div>
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('ECR 번호')}</th>
					<td id="ecrNumber" colspan="3">
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('ECO 제목')}<span style="color:red;">*</span></th>
					<td colspan="3">
						<input type="text" id="name" name="name" class="input01 w100">
					</td>
				</tr>
				
				<tr>
					<th>${e3ps:getMessage('설변대상 옵션')}<span style="color:red;">*</span></th>
					<td colspan="3">
						<input type="radio" name="ecoChangeOption" id="ecoChangeOption" value="re"/>리비전
						<input type="radio" name="ecoChangeOption" id="ecoChangeOption" value="ne"/>개정
					</td>
				</tr>
				
				<tr>
					<th>${e3ps:getMessage('거래처명')}</th>
					<td id="customerTD">
					</td>
					<th>${e3ps:getMessage('담당자')}<span style="color:red;">*</span></th>
					<td>
						<div class="pro_view"> 
							<select class="searchUser" id="changeOwner" name="changeOwner" data-width="70%"></select> 
							<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('changeOwner', 'single');">
								<img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png" />
							</span>
							<span class="pointer verticalMiddle" onclick="javascript:deleteUser('changeOwner');">
								<img class="verticalMiddle" src="/Windchill/jsp/portal/images/delete_icon.png" />
							</span>
						</div>
					</td>
				</tr>
				
				<tr>
					<th>${e3ps:getMessage('설계변경 사유')}<span style="color:red;">*</span></th>
					<td>
						<input type="text" id="echangeReason" name="echangeReason" class="input01 w40" value="">
					</td>
					
					<th>${e3ps:getMessage('완료예정일')}<span style="color:red;">*</span></th>
					<td class="calendar">
						<input type="text" class="datePicker w50" name="finishDate" id="finishDate" readonly/>
					</td>
				</tr>
				
				<tr>
					<th>${e3ps:getMessage('장비명')}</th>
					<td id ="equipmentName">
					</td>
					
					<th>${e3ps:getMessage('ECR 요청희망일')}</th>
					<td id="completeHopeDate">
					</td>
				</tr>
				
				<tr>
					<th>${e3ps:getMessage('ECR 요청자')}</th>
					<td colspan="3">
					</td>
				</tr>
				
				<tr> 
					<th>${e3ps:getMessage('내용')}<span style="color:red;">*</span></th>
					<td colspan="3">
						<jsp:include page="${e3ps:getIncludeURLString('/change/include_ecrContent')}" flush="true">
							<jsp:param name="oid" value="" />
							<jsp:param name="type" value="create" />
						</jsp:include>
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('적용요구시점')}<span style="color:red;">*</span></th>
					<td colspan="3" class="calendar">
						<label><input type="radio" name="applyDate" value="outOfStock" /> ${e3ps:getMessage('재고소진 후')}</label>
						<label><input type="radio" name="applyDate" value="immediately" /> ${e3ps:getMessage('즉시')}</label>
						<label><input type="radio" name="applyDate" value="specificDate" /> ${e3ps:getMessage('특정일시')}</label> ( <input type="text" class="datePicker w10" name="applyDate" id="applyDate" readonly disabled/> )
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('비고')}</th>
					<td colspan="3" class="pd10">
						<div class="textarea_autoSize">
							<textarea name="description" id="description" escapeXml="false"></textarea>
						</div>
					</td>
				</tr>
				<tr> 
	               <th>${e3ps:getMessage('첨부파일')}</th>
	               <td colspan="3" class="pd10">
	                  <jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
	                     <jsp:param name="formId" value="ecoForm"/>
	                     <jsp:param name="command" value="insert"/>
	                     <jsp:param name="btnId" value="createBtn" />
	                  </jsp:include>
	               </td>
	            </tr>
	            <%-- <tr>
					<th>${e3ps:getMessage('단계/활동')}<span style="color:red;">*</span></th>
					<td colspan="3">
						<jsp:include page="${e3ps:getIncludeURLString('/change/setECADefinition')}" flush="true">
							<jsp:param name="oid" value="${oid}"/>
							<jsp:param name="changeType" value="ECO"/>
						</jsp:include>
					</td>
				</tr> --%>
			</table>
		</div>
		<div class="ml30 mr30">
			<jsp:include page="${e3ps:getIncludeURLString('/common/include_addObject')}" flush="true">
				<jsp:param name="oid" value=""/>
				<jsp:param name="type" value="single"/>
				<jsp:param name="objType" value="part"/>
				<jsp:param name="moduleType" value="ECO"/>
				<jsp:param name="pageName" value="relatedPart"/>
				<jsp:param name="title" value="${e3ps:getMessage('관련 부품')}"/>
				<jsp:param name="gridHeight" value="200"/>
			</jsp:include>
		</div>
		<div class="ml30 mr30 mb30">
			<jsp:include page="${e3ps:getIncludeURLString('/approval/include_addApprovalLine')}" flush="true">
				<jsp:param name="gridHeight" value="200"/>
			</jsp:include>
		</div>
	</form>
</div>