<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- <script src="/Windchill/jsp/js/popup.js"></script> -->
<!-- <script src="/Windchill/jsp/js/checkbox.js"></script> -->
<!-- <link rel="stylesheet" type="text/css" href="/Windchill/jsp/js/jquery/jquery-ui-1.11.4.redmond/jquery-ui.css"> -->
<!-- <link rel="stylesheet" type="text/css" href="/Windchill/jsp/js/jquery/jquery-ui-1.11.4.redmond/jquery-ui.structure.css"> -->
<!-- <link rel="stylesheet" type="text/css" href="/Windchill/jsp/js/jquery/jquery-ui-1.11.4.redmond/jquery-ui.theme.css"> -->
<!-- <script type="text/javascript" src="/Windchill/jsp/js/jquery/jquery-ui-1.11.4.redmond/jquery-ui.min.js"></script> -->

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
});

let searchObj = [];

function changeHandler(target){
	let value = target.value;
	if(target.id == 'product'){
		let productItem = searchObj.find(item => item.oid == value);
		document.getElementById('pDrawingNo').innerText = productItem.attributes.DRAW_NO?productItem.attributes.DRAW_NO:'';
		document.getElementById('pMaterial').innerText = productItem.attributes.MATERIAL?productItem.attributes.MATERIAL:'';
		document.getElementById('pOempn').innerText = productItem.attributes.OEM_PART_NO?productItem.attributes.OEM_PART_NO:'';
	}
	
}

function spaceCheck(val) {
	return !val || /^\s*$/.test(val);
}

<%----------------------------------------------------------
*                      SAVE
----------------------------------------------------------%>
function saveEco(ele) {
	
	var appState = ele.value;
	const ecr_contents_list = AUIGrid.getGridData(ecr_content_gridID);
	const approvalList = AUIGrid.getGridData(app_line_myGridID);
	
	let applyDateCalendar = document.querySelector("input[name=applyDate][type=text]");
// 	let partList = AUIGrid.getGridData(add_relatedPart_myGridID);
	
	console.log(getElementValue('name'));
	
	try{
		if(spaceCheck(getElementValue('name'))){
			throw new Error("${e3ps:getMessage('ECR 명을 입력해 주세요')}");
			
		}else if(spaceCheck(getElementValue('customer'))){
			throw new Error("${e3ps:getMessage('거래처명을 입력해 주세요')}");
			
		}else if(spaceCheck(getElementValue('echangeReason'))){
			throw new Error("${e3ps:getMessage('설계 변경 사유를 입력해 주세요')}");
			
		}else if(ecr_contents_list.length == 0){
			throw new Error("${e3ps:getMessage('변경 내용을 추가해 주세요')}");
			
		}else if(ecr_contents_list.some(x => spaceCheck(x.name) || spaceCheck(x.contents))){
			throw new Error("${e3ps:getMessage('변경 내용을 작성해 주세요')}");
			
		}else if(!applyDateCalendar.disabled && applyDateCalendar.value == 0){
			throw new Error("${e3ps:getMessage('적용 시점 특정 일자를 선택해 주세요')}");
			
// 		}else if(partList.length == 0){
// 			throw new Error("${e3ps:getMessage('관련 품목을 추가해 주세요')}");
			
		}else if(appState == "APPROVING" && approvalList.length == 0){
			throw new Error("${e3ps:getMessage('결재선을 지정해 주세요')}");
			
		}else if(appState == "APPROVING" && !approvalList.some((item) => item.roleType=='APPROVE')){
			throw new Error("${e3ps:getMessage('결재 승인자를 지정해 주세요')}");
			
		}
		
		
	}catch(err){
		if(err instanceof validationError){
			focusOneCell(add_distributePartList_myGridID, err.item.rowIndex,err.item.columnIndex);
		}
		alert(err.message);
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
// 	param.partList = partList;
	console.log('param',param)
	
	var url	= getURLString("/change/createECRAction");
	ajaxCallServer(url, param, function(){}, true);
	
	
}

</script>
<div class="product"> 
<form name="ecoForm" id="ecoForm" method="post" enctype="multipart/form-data">

<!-- START -->
<div class="seach_arm pt20 pb5">
	<div class="leftbt">
		<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('ECR 등록')}</span>
	</div>
	<div class="rightbt">
		<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0"/>
		<button type="button" value="TEMP_STORAGE" class="s_bt03" onclick="saveEco(this)">${e3ps:getMessage('임시 저장')}</button>
		<button type="button" value="APPROVING" class="s_bt03" onclick="saveEco(this)">${e3ps:getMessage('등록')}</button>
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
				<th>${e3ps:getMessage('ECR 제목')}<span style="color:red;">*</span></th>
				<td colspan="3">
					<input type="text" id="name" name="name" class="input01 w40">
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('거래처명')}<span style="color:red;">*</span></th>
				<td>
					<input type="text" id="customer" name="customer" class="input01" style="width:20%">
				</td>
				
				<th>${e3ps:getMessage('설계변경 사유')}<span style="color:red;">*</span></th>
				<td>
					<input type="text" id="echangeReason" name="echangeReason" class="input01 w40">
				</td>
			</tr>
			
			<tr>
				<th>${e3ps:getMessage('장비명')}</th>
				<td>
					<input type="text" id="equipmentName" name="equipmentName" class="input01 w40">
				</td>
				
				<th>${e3ps:getMessage('완료 희망일')}</th>
				<td>
					<input type="text" class="datePicker w40" name="completeHopeDate" id="completeHopeDate" readonly/>
				</td>
			</tr>
			
			<tr>
				<th>${e3ps:getMessage('요청자')}</th>
				<td>
					<input type="text" id="" name="" class="input01 w40">
				</td>
				
				<th>${e3ps:getMessage('적용요구시점')}<span style="color:red;">*</span></th>
				<td colspan="3" class="calendar">
					<label><input type="radio" name="applyDate" value="outOfStock" checked/> ${e3ps:getMessage('재고소진 후')}</label>
					<label><input type="radio" name="applyDate" value="immediately" /> ${e3ps:getMessage('즉시')}</label>
					<label><input type="radio" name="applyDate" value="specificDate" /> ${e3ps:getMessage('특정일시')}</label> ( <input type="text" class="datePicker w20" name="applyDate" id="applyDate" readonly disabled/> )
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
			   <th>${e3ps:getMessage('첨부파일')}</th>
               <td colspan="3">
                  <jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
                     <jsp:param name="formId" value="ecoForm"/>
                     <jsp:param name="command" value="insert"/>
                     <jsp:param name="btnId" value="createBtn" />
                  </jsp:include>
               </td>
			</tr>
			
			<tr>
				<th>${e3ps:getMessage('비고')}</th>
				<td colspan="3" class="pd10">
					<div class="textarea_autoSize">
						<textarea name="description" id="description" escapeXml="false" /></textarea>
					</div>
				</td>
			</tr>
	</table>
</div>

<div class="ml30 mr30 mb30">
	<jsp:include page="${e3ps:getIncludeURLString('/approval/include_addApprovalLine')}" flush="true">
		<jsp:param name="gridHeight" value="200"/>
	</jsp:include>
</div>
</form>
</div>