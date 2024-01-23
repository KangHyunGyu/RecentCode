<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function() {
	//팝업 리사이즈
	popupResize();
	
	//data 셋팅
	dataInit();
		
});


function dataInit(){
	
	let purposeList = '${distribute.purpose}'.split(',');
	let purpose1 = purposeList[0];
					purposeList.shift();
	let purpose2 = purposeList;
	let purposeInside = document.querySelectorAll('.purposeInside');
	let purposeOutside = document.querySelectorAll('.purposeOutside');
	
	if(purpose1 == 'inside'){
		purposeInside.forEach((element)=> {
			element.style.display = 'block';
		})
		enableInputTag(purposeInside);
	}else{
		purposeOutside.forEach((element)=> {
			element.style.display = 'block';
		})
		enableInputTag(purposeOutside);
	}
	
	
	//사내 사외 체크
	document.getElementsByName('purpose').forEach((tag) => {
		if(purpose1 == tag.value){
			tag.checked = true;
		}
	})
	
	//용도 하위 체크
// 	purpose2.forEach((val)=> {
// 		let exist = false;
// 		document.getElementsByName(purpose1+'Purpose').forEach((tag) => {
// 			if(val == tag.value){
// 				tag.checked = true;
// 				exist = true;
// 			}
// 		})
// 		if(!exist){
// 			const textInput = document.querySelector('#'+purpose1+'Purpose');
// 			textInput.disabled = false;
// 			textInput.value=val;
// 		}
// 	})
	purpose2.forEach((val)=> {
		let exist = false;
		document.getElementsByName(purpose1+'Purpose').forEach((tag) => {
			if(val == tag.value){
				tag.checked = true;
				exist = true;
			}
		})
		if(!exist){
			document.querySelector('#'+purpose1+'Purpose').value=val;
			document.querySelector('#'+purpose1+'Purpose').disabled=exist;
		}else{
			document.querySelector('#'+purpose1+'Purpose').disabled=exist;
		}
	})
	//표기 확인
	"${distribute.markingConfirm}".split(',').forEach((val)=> {
		document.getElementsByName('markingConfirm').forEach((tag) => {
			if(val == tag.value){
				tag.checked = true;
			}
		})
	})
	//파일 형식
// 	"${distribute.fileType}".split(',').forEach((val)=> {
// 		let exist = false;
// 		document.getElementsByName('fileType').forEach((tag) => {
// 			if(val == tag.value){
// 				tag.checked = true;
// 				exist = true;
// 			}
// 		})
// 		if(!exist){
// 			const textInput = document.querySelector('input[name=fileType][type=text]')
// 			textInput.disabled = false;
// 			textInput.value=val;
// 		}
// 	})
	"${distribute.fileType}".split(',').forEach((val)=> {
		let exist = false;
		document.getElementsByName('fileType').forEach((tag) => {
			if(val == tag.value){
				tag.checked = true;
				exist = true;
			}
		})
		if(!exist){
			document.querySelector('#fileType').value=val;
			document.querySelector('#fileType').disabled=exist;
		}
	})
	//회수
	document.getElementsByName('withdraw').forEach((tag) => {
		if("${distribute.withdraw}" == tag.value){
			tag.checked = true;
		}
	})
	
	if(document.getElementById("downloadDate"))document.getElementById("downloadDate").value = "${distribute.downloadDate}";
	document.getElementById("distDate").value = "${distribute.distDate}";
	
}

function distEventHandler(ele){
	
	const eleName = ele.getAttribute('name');
	
	if(eleName == 'purpose'){
		
		let purposeInside = document.querySelectorAll('.purposeInside');
		let purposeOutside = document.querySelectorAll('.purposeOutside');
		
		switch(ele.value){
			case 'inside' :
				purposeInside.forEach((element)=> {
					element.style.display = 'block';
				})
				
				purposeOutside.forEach((element)=> {
					element.style.display = 'none';
				})
				enableInputTag(purposeInside);
				targetReset(purposeOutside);
				
				break;
			default :
				
				purposeInside.forEach((element)=> {
					element.style.display = 'none';
				})
				
				purposeOutside.forEach((element)=> {
					element.style.display = 'block';
				})
				enableInputTag(purposeOutside);
				targetReset(purposeInside);
		}
	}else{
		
		let textInputNode;
		Array.prototype.slice.call(document.querySelectorAll('label')).forEach((item) => {
			let labelChildArray = Array.prototype.slice.call(item.childNodes)
			let textExist = labelChildArray.some((l)=>l.nodeName='INPUT' && l.type == 'text'  && l.name == eleName )
			let targetElementExist = labelChildArray.some((l)=>l.nodeName='INPUT' && l.type == ele.type && l.name == eleName )
			labelChildArray.forEach((x)=> {
				if(x.nodeName=='INPUT' && x.type == ele.type && x.name == eleName && textExist){
					ele = x;
				}
				
				if(x.nodeName=='INPUT' && x.type == 'text' && x.name == eleName && targetElementExist){
					textInputNode = x;
				}
			})
		})
		if(ele.checked){
			textInputNode.disabled = false;
		}else{
			textInputNode.disabled = true;
		}
	}
}

function targetReset(elements){
	
	elements.forEach((element) => {
		
		const allChildElement = getAllChildElements(element);
		
		allChildElement.forEach((item) => {
			if(item.nodeName == 'INPUT'){
				item.disabled = true;
				if(item.type == 'text'){
					item.value = '';
					const itemClassNames = item.className;
				}else if(item.type == 'checkbox' || item.type == 'radio'){
					item.checked = false;
					if(item.className.includes('resetTarget')){
						item.checked = true;
					}
				}
			}
		})
		
	});
}

function enableInputTag(elements){
	elements.forEach((element) => {
		const allChildElement = getAllChildElements(element);
		allChildElement.forEach((item) => {
			if(item.nodeName == 'INPUT' && !(item.className.includes('resetTarget') && item.type == 'text')) item.disabled = false;
		})
	})
}

function checkboxConfirm(name){
	let result = false;
	document.getElementsByName(name).forEach((item) => {
		if(item.checked){
			result = true;
		}
	})
	return result;
}

function onchangeEventHandler(e){
	
	let elementId = e.id;
	
	switch(elementId){
		case 'distType' :
			let distType = document.getElementById('distType').value;
			break;
		default :
			break;
	}
}

function save(){
	
// 	const addedItemList = AUIGrid.getGridData(modify_distParts_myGridID);
	
	try{
		
		if(isEmpty(getElementValue('distName'))){
			throw new Error("${e3ps:getMessage('배포 명을 작성해주세요')}");
			
		}else if(!checkboxConfirm(document.querySelector('input[name="purpose"]:checked').value+'Purpose')){
			throw new Error("${e3ps:getMessage('용도를 체크해주세요')}");
				
		}else if(!checkboxConfirm('fileType')){
			throw new Error("${e3ps:getMessage('파일 형식을 체크해주세요')}");
				
		}else if(isEmpty(getElementValue('distDate'))){
			throw new Error("${e3ps:getMessage('배포일을 선택해주세요')}");
			
		}else if(document.querySelector('input[name="purpose"]:checked').value=='outside'){
			if(isEmpty(getElementValue('downloadDate'))){
				throw new Error("${e3ps:getMessage('다운로드 기한일을 선택해주세요')}");
			}
		}
		
	}catch(err){
		if(err instanceof validationError){
			focusOneCell(add_distributePartList_myGridID, err.item.rowIndex,err.item.columnIndex);
		}
		alert(err.message);
		return;
	}
		
		
	if(!confirm("${e3ps:getMessage('수정하시겠습니까?')}")){
		return;
	}
	
	let param = new Object(); 
// 	param.purchaseParts = addedItemList;
	param = getFormParams('distributeForm', param);
	const purpose = param.purpose;
	param.oid = '${distribute.oid}';
	param.purpose = param.purpose +',';
	param.purpose += typeof param[purpose+'Purpose'] == 'string'? param[purpose+'Purpose']:param[purpose+'Purpose'].join(',')
	if(param.markingConfirm)param.markingConfirm = typeof param.markingConfirm == 'string'? param.markingConfirm:param.markingConfirm.join(',');
	if(param.fileType)param.fileType = typeof param.fileType == 'string'? param.fileType:param.fileType.join(',');
	
	console.log('param',param)
	var url	= getURLString("/distribute/modifyDistributeDocumentAction");
	ajaxCallServer(url, param, null, true);
}

</script>


<div class="pop">
<form name="distributeForm" id="distributeForm" method="post">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('도면 출도 의뢰서 수정')}</h2>
		<span class="close"><a href="javascript:window.close()"><img
				src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->

	<div class="pl25 pr25">
			<div class="seach_arm2 pt10 pb10">
				<div class="leftbt">
					<h4>
						<img class="pointer" onclick="switchPopupDiv(this);"
							src="/Windchill/jsp/portal/images/minus_icon.png">
						${e3ps:getMessage('도면 출도 의뢰서 정보')}
					</h4>
				</div>
				<div class="rightbt">
					<button type="button" class="i_update" style="width: 70px" onclick="javascript:save();">${e3ps:getMessage('저장')}</button>
					<button type="button" class="i_delete" style="width: 70px" onclick="window.history.back();">${e3ps:getMessage('취소')}</button>
				</div>
			</div>
			<div class="pro_table">
				<table class="mainTable">
					<colgroup>
						<col style="width: 20%">
						<col style="width: 80%">
					</colgroup>
					<tbody>
						<tr>
							<th scope="col">${e3ps:getMessage('의뢰서 번호')}</th>
							<td>
								${distribute.distNumber}
							</td>
						</tr>
						<tr>
							<th scope="col">${e3ps:getMessage('의뢰서 명')}<span class="required">*</span></th>
							<td><input type="text" class="w50" id="distName" name="distName" value="${distribute.distName }"></td>
						</tr>
						<tr>
							<th scope="col">${e3ps:getMessage('상태')}</th>
							<td>
								${distribute.stateName}
							</td>
						</tr>
						<tr>
							<th scope="rowgroup" rowspan="2">${e3ps:getMessage('용도')}<span class="required">*</span></th>
							<td class='pt5 pb5'>
								<label class="mr10"><input type="radio" name="purpose" onclick="distEventHandler(this)" value="inside" checked/>${e3ps:getMessage('사내')}</label>
								<label class="mr10"><input type="radio" name="purpose" onclick="distEventHandler(this)" value="outside"/>${e3ps:getMessage('사외')}</label>
							</td>
						</tr>
						<tr>
							<td class='pt5 pb5'>
								<div class="purposeInside" style="display:none">
									<label class="mr10"><input type="checkbox" name="insidePurpose" value="I001"/>${e3ps:getMessage('각 부서 참고용(생산/품질/영업)')}</label>
									<label class="mr10"><input type="checkbox" name="insidePurpose" value="I002"/>${e3ps:getMessage('검사성적서용')}</label>
									<label class="mr10"><input type="checkbox" name="insidePurpose" value="I003"/>${e3ps:getMessage('견적용')}</label>
									<label><input type="checkbox" name="insidePurpose" value="I004" onclick="distEventHandler(this)"/>${e3ps:getMessage('기타')} ( <input type="text" class="resetTarget" name="insidePurpose" id="insidePurpose" class="w20" disabled/> )</label>
								</div>
								
								<div class="purposeOutside" style="display:none">
									<label class="mr10"><input type="radio" checked onchange="distEventHandler(this)" class="resetTarget" name="outsidePurpose" value="O001" disabled>${e3ps:getMessage('고객사 제출용 ( ')}<input type="text" id="outsidePurpose" name="outsidePurpose" class="w10" disabled> )</label>
									<label>${e3ps:getMessage('외주가공용 ( ')}</label>
									<label class="mr10"><input type="radio" onchange="distEventHandler(this)" name="outsidePurpose" value="O002" disabled>${e3ps:getMessage('완제품')}</label>
									<label class="mr10"><input type="radio" onchange="distEventHandler(this)" name="outsidePurpose" value="O003" disabled>${e3ps:getMessage('반제품')}</label> )
								</div>
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('표기 확인')}</th>
							<td>
								<label class="mr10"><input type="checkbox" name="markingConfirm" value="M001" />${e3ps:getMessage('공차 표기')}</label> 
								<label class="mr10"><input type="checkbox" name="markingConfirm" value="M002" />${e3ps:getMessage('노트 표기')}</label> 
								<label><input type="checkbox" name="markingConfirm" value="M003" />${e3ps:getMessage('OEM P/N 표기')}</label>
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('파일형식')}<span class="required">*</span></th>
							<td>
								<label class="mr10"><input type="checkbox" name="fileType" value="F001" />PDF</label> 
								<label class="mr10"><input type="checkbox" name="fileType" value="F002" />2D CAD</label> 
								<label><input type="checkbox" name="fileType" onclick="distEventHandler(this)" value="F004" />${e3ps:getMessage('기타')} ( <input type="text" name="fileType" id="fileType" class="w20" disabled/> )</label>
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('배포일')}<span class="required">*</span></th>
							<td class="calendar">
								<input type="text" class="datePicker w10" name="distDate" id="distDate" readonly />
							</td>
						</tr>
						<tr>
							<th scope="col">
								<div class="purposeInside" style="display:none">${e3ps:getMessage('회수')}<span class="required">*</span></div>
								<div class="purposeOutside" style="display:none">${e3ps:getMessage('다운로드 기한')}<span class="required">*</span></div>
								
							</th>
							<td>
								<div class="purposeInside" style="display:none">
									<label class="mr10"><input type="radio" class="resetTarget"  name="withdraw" value="W001" checked/>${e3ps:getMessage('필요')}</label>
									<label><input type="radio" name="withdraw" value="W002"/>${e3ps:getMessage('불필요 (자체파기 유무)')}</label>
								</div>
								<div class="purposeOutside calendar" style="display:none">
									<input type="text" id="downloadDate" name="downloadDate" class="datePicker w10" readonly disabled>
								</div>
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('기타기입사항')}</th>
							<td class="pd10">
								<div class="textarea_autoSize">
									<textarea name="description" id="description" escapeXml="false" /><c:out value="${distribute.description}" escapeXml="false" /></textarea>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
<%-- 			<jsp:include --%>
<%-- 				page="${e3ps:getIncludeURLString('/distribute/include_modifyDistributePartList')}" --%>
<%-- 				flush="true"> --%>
<%-- 				<jsp:param name="oid" value="${distribute.oid}" /> --%>
<%-- 				<jsp:param name="gridHeight" value="400" /> --%>
<%-- 			</jsp:include> --%>

	</div>
</form>
</div>
