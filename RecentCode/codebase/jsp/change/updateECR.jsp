<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript">
$(document).ready(function(){
	//팝업 리사이즈
 	popupResize();
	
 	dataInit_ServiceRe();
 	
 	spaceRemove();
	
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

function dataInit_ServiceRe(){
	let aDate = "${data.applyDateName}";
	
	if(aDate === "재고소진 후") {
        const outOfStockRadio = document.querySelector("input[name='applyDate'][value='outOfStock']");
        outOfStockRadio.checked = true;
    } else if(aDate == "즉시") {
    	const immediatelyRadio = document.querySelector("input[name='applyDate'][value='immediately']");
    	immediatelyRadio.checked = true;
    } else {
    	document.querySelector("input[name=applyDate][type=text]").disabled=false;
    	document.querySelector("input[name=applyDate][type=text]").value = "${data.specificDate}";
    	const specificDateRadio = document.querySelector("input[name='applyDate'][value='specificDate']");
    	specificDateRadio.checked = true;
    }
	
}
function changeHandler(target){
	
	let value = target.value;
	
	console.log(value);
	
	if(target.id == 'product'){
		let productItem = searchObj.find(item => item.oid == value);
		document.getElementById('pDrawingNo').innerText = productItem.attributes.DRAW_NO?productItem.attributes.DRAW_NO:'';
		document.getElementById('pMaterial').innerText = productItem.attributes.MATERIAL?productItem.attributes.MATERIAL:'';
		document.getElementById('pOempn').innerText = productItem.attributes.OEM_PART_NO?productItem.attributes.OEM_PART_NO:'';
	}
	
}

function spaceRemove() {
	
	var name = "${data.name}".trimLeft();
	var customer = "${data.customer}".trimLeft();
	var echangeReason = "${data.echangeReason}".trimLeft();
	var equipmentName = "${data.equipmentName}".trimLeft();
	
	
	document.getElementById('name').value = name;
	document.getElementById('customer').value = customer;
	document.getElementById('echangeReason').value = echangeReason;
	
	document.getElementById('equipmentName').value = equipmentName;
	
}

function spaceCheck(val) {
	return !val || /^\s*$/.test(val);
}

function saveEco(ele) {
	
	const ecr_contents_list = AUIGrid.getGridData(ecr_content_gridID);
	
	let applyDateCalendar = document.querySelector("input[name=applyDate][type=text]");
// 	let partList = AUIGrid.getGridData(add_relatedPart_myGridID);
	
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
		}
// 		else if(partList.length == 0){
// 			throw new Error("${e3ps:getMessage('관련 품목을 추가해 주세요')}");
			
// 		}
		
		
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
	param.applyDate = typeof param.applyDate == 'string'? param.applyDate:param.applyDate.join(',');
	param.ecrContentsList = ecr_contents_list;
// 	param.partList = partList;
	console.log('param',param)
	
	var url	= getURLString("/change/updateECRAction");
	ajaxCallServer(url, param, function(){
		if(opener.window.search) {
			opener.window.search();
		}
	}, true);
	
	
}
</script>
<div class="pop pb10">
	<div class="top">
		<h2>${e3ps:getMessage('ECR 수정')}, ${e3ps:getMessage('ECR')} - ${data.ecrNumber}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<div class="pl25 pr25">
		<div class="seach_arm2 pt10 pb10">
			<div class="leftbt">
			</div>
			<div class="rightbt">
				<button type="button" class="s_bt03" onclick="saveEco(this)">${e3ps:getMessage('수정')}</button>
				<button type="button" class="s_bt03" onclick="javascript:history.back()">${e3ps:getMessage('뒤로가기')}</button>
				<button type="button" class="s_bt04" onclick="javascript:window.close()">${e3ps:getMessage('닫기')}</button>
			</div>
		</div>
		<form name="ecoForm" id="ecoForm" method="post" enctype="multipart/form-data">
		<input type="hidden" name="oid" value="${data.oid}"/>
			<div class="seach_arm2 pb10">
				<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('기본 정보')}</h4></div>
				<div class="rightbt"></div>
			</div>
			<div class="pro_table">
				<table class="mainTable">
					<colgroup>
						<col width="15%">
						<col width="35%">
						<col width="15%">
						<col width="35%">
					</colgroup>
					<tr>
				        <th scope="col">${e3ps:getMessage('ECR 번호')}</th>
				        <td colspan="3">${data.ecrNumber}</td>
      				</tr>
					<tr>
						<th>${e3ps:getMessage('ECR 제목')}<span style="color:red;">*</span></th>
						<td>
							<input type="text" id="name" name="name" class="input01 w100" value="">
						</td>
						<th>${e3ps:getMessage('거래처명')}<span style="color:red;">*</span></th>
						<td>
							<input type="text" id="customer" name="customer" class="input01 w100" value="">
						</td>
					</tr>
					
					<tr>
						<th>${e3ps:getMessage('장비명')}</th>
						<td>
							<input type="text" id="equipmentName" name="equipmentName" class="input01 w40">
						</td>
						
						<th>${e3ps:getMessage('완료 희망일')}</th>
						<td>
							<input type="text" class="datePicker w40" name="completeHopeDate" id="completeHopeDate" value="${data.completeHopeDate }" readonly/>
						</td>
					</tr>
			
					<tr>
						<th>${e3ps:getMessage('요청자')}</th>
						<td>
							<input type="text" id="" name="" class="input01 w40">
						</td>
				
					
						<th>${e3ps:getMessage('설계변경 사유')}<span style="color:red;">*</span></th>
						<td>
							<input type="text" id="echangeReason" name="echangeReason" class="input01 w40" value="">
						</td>
					</tr>
					
					
					
					<tr> 
						<th>${e3ps:getMessage('내용')}<span style="color:red;">*</span></th>
						<td colspan="3">
							<jsp:include page="${e3ps:getIncludeURLString('/change/include_ecrContent')}" flush="true">
								<jsp:param name="oid" value="${data.oid}" />
								<jsp:param name="type" value="modify" />
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
								<textarea name="description" id="description" escapeXml="false">${data.description}</textarea>
							</div>
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
				</table>
			</div>
			
<!-- 			<div> -->
<%-- 				<jsp:include page="${e3ps:getIncludeURLString('/common/include_addObject')}" flush="true"> --%>
<%-- 					<jsp:param name="oid" value="${data.oid}"/> --%>
<%-- 					<jsp:param name="objType" value="part"/> --%>
<%-- 					<jsp:param name="moduleType" value="ECR"/> --%>
<%-- 					<jsp:param name="pageName" value="relatedPart"/> --%>
<%-- 					<jsp:param name="title" value="${e3ps:getMessage('관련 부품')}"/> --%>
<%-- 					<jsp:param name="gridHeight" value="200"/> --%>
<%-- 				</jsp:include> --%>
<!-- 			</div> -->
		</form>
	</div>
</div>