<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<style>
.SumoSelect {
	width: 35%;
}
</style>

<script>

//제품 검색 캐시
let product_cache;


$(document).ready(function() {
		
		//DMS 배포 코드 셋팅(id, 변수값 같아야함)
		//setDistCode();
		
		
		//업체 정보 자동완성 셋팅
		//getCompanys('#company_noAuto select');
		
		
		/* ajaxCallServer(getURLString("/common/getCurrentPeople"), new Object(), function(data){
				let people = data.item;
				if(people){
					const node = document.createElement("option");
					node.appendChild(document.createTextNode(people.name + "[" + people.erpid + "]"));
					node.value = people.oid;
					document.getElementById('erpid').appendChild(node);
				}
		}, true);
		 */
		 
});

function distEventHandler(ele){
	
	const elementName = ele.getAttribute('name');
	
	if(elementName == 'purpose'){
		
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
		Array.from(document.querySelectorAll('label')).forEach((item) => {
			
			let checkTag;
			let textTag;
			
			Array.from(item.childNodes).forEach((x)=> {
				if(x.nodeName=='INPUT' && x.type == ele.type && x.name == elementName ){
					checkTag = x;
				}
				if(x.nodeName=='INPUT' && x.type == 'text' && x.name == elementName ){
					textTag = x;
				}
			})
			
			if(checkTag && textTag){
				textTag.disabled = !checkTag.checked;
			}
			
		})
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

<%----------------------------------------------------------
*                      SAVE
----------------------------------------------------------%>
function save() {
	
	const addedItemList = AUIGrid.getGridData(add_distributePartList_myGridID);
	const approvalList = AUIGrid.getGridData(app_line_myGridID);
	
	try{
		
		if(isEmpty(getElementValue('distName'))){
			throw new Error("${e3ps:getMessage('배포 명을 작성해주세요')}");
			
		}else if(!checkboxConfirm(document.querySelector('input[name="purpose"]:checked').value+'Purpose')){
			throw new Error("${e3ps:getMessage('용도를 체크해주세요')}");
			
		}else if(!checkboxConfirm('markingConfirm')){
			throw new Error("${e3ps:getMessage('표기 확인을 체크해주세요')}");
			
		}else if(!checkboxConfirm('fileType')){
			throw new Error("${e3ps:getMessage('파일 형식을 체크해주세요')}");
			
		}else if(isEmpty(getElementValue('distDate'))){
			throw new Error("${e3ps:getMessage('배포일을 선택해주세요')}");
			
		}else if(document.querySelector('input[name="purpose"]:checked').value=='outside'){
			if(isEmpty(getElementValue('downloadDate'))){
				throw new Error("${e3ps:getMessage('다운로드 기한일을 선택해주세요')}");
			}
			
		}else if(approvalList.length == 0){
			throw new Error("${e3ps:getMessage('결재선을 지정해주세요')}");
			
		}else if(!approvalList.some((item) => item.roleType=='APPROVE')){
			throw new Error("${e3ps:getMessage('결재 승인자를 지정해주세요')}");
			
		}
		
		
		/* else if(addedItemList.length == 0){
			throw new Error("${e3ps:getMessage('배포 요청 품목을 추가해주세요')}");
		}
		
		addedItemList.forEach((item) => {
			
			if(addedItemList.length > 1 && isEmpty(item.distPartNumber)){
				AUIGrid.removeRowByRowId(add_distributePartList_myGridID, item._$uid);
			}
			
			let indexInfo;
			
			if(isEmpty(item.distPartNumber)){
				indexInfo = getCellIndex(add_distributePartList_myGridID,item._$uid,"distPartNumber");
				throw new validationError(indexInfo,"${e3ps:getMessage('품목을 선택해주세요')}");
			}
			
			
		}) */
		
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
	param = getFormParams('distributeForm', param)
	console.log('param1',param)
	const purpose = param.purpose;
	param.purpose = param.purpose +',';
	param.purpose += typeof param[purpose+'Purpose'] == 'string'? param[purpose+'Purpose']:param[purpose+'Purpose'].join(',')
	param.markingConfirm = typeof param.markingConfirm == 'string'? param.markingConfirm:param.markingConfirm.join(',');
	param.fileType = typeof param.fileType == 'string'? param.fileType:param.fileType.join(',');
	param.purchaseParts = addedItemList;
	param.approvalList = approvalList;
	console.log('param2',param)
	
	var url	= getURLString("/distribute/createDistributeAction");
	ajaxCallServer(url, param, null, true);
	
	
}

</script>
<br>
<div class="product" style="margin-bottom:20rem !important;">
	<form name="distributeForm" id="distributeForm" method="post" enctype="multipart/form-data">
		<!-- button -->
		<div class="seach_arm pt20 pb5">
			<div class="leftbt">
				<span class="title"> <img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png"> ${e3ps:getMessage('기본 정보')}
				</span>
			</div>
			<div class="rightbt">
				<button type="button" class="s_bt03"
					style="width: 70px" onclick="save()">${e3ps:getMessage('등록')}</button>
			</div>
		</div>
		<!-- //button -->
		<div class="pro_table mr30 ml30">
			<table class="mainTable">
				<colgroup>
					<col style="width: 20%">
					<col style="width: 80%">
				</colgroup>
				<tbody>
					<tr>
						<th scope="col">${e3ps:getMessage('배포 명')}<span class="required">*</span></th>
						<td>
							<input type="text" class="w50" id="distName" name="distName">
						</td>
					</tr>
					<tr>
						<th scope="rowgroup" rowspan="2">${e3ps:getMessage('용도')}</th>
						<td class='pt5 pb5'>
							<label class="mr10"><input type="radio" name="purpose" onclick="distEventHandler(this)" value="inside" checked/>${e3ps:getMessage('사내')}</label>
							<label class="mr10"><input type="radio" name="purpose" onclick="distEventHandler(this)" value="outside"/>${e3ps:getMessage('사외')}</label>
						</td>
					</tr>
					<tr>
						<td class='pt5 pb5'>
							<div class="purposeInside">
								<label class="mr10"><input type="checkbox" name="insidePurpose" value="I001"/>${e3ps:getMessage('각 부서 참고용(생산/품질/영업)')}</label>
								<label class="mr10"><input type="checkbox" name="insidePurpose" value="I002"/>${e3ps:getMessage('검사성적서용')}</label>
								<label class="mr10"><input type="checkbox" name="insidePurpose" value="I003"/>${e3ps:getMessage('견적용')}</label>
								<label><input type="checkbox" name="insidePurpose" value="I004" onclick="distEventHandler(this)"/>${e3ps:getMessage('기타')} ( <input type="text" class="resetTarget" name="insidePurpose" class="w20" disabled/> )</label>
							</div>
							
							<div class="purposeOutside" style="display:none">
								<label class="mr10"><input type="radio" checked onchange="distEventHandler(this)" class="resetTarget" name="outsidePurpose" value="O001" disabled>${e3ps:getMessage('고객사 제출용 ( ')}<input type="text" name="outsidePurpose" class="w10" disabled> )</label>
								<label>${e3ps:getMessage('외주가공용 ( ')}</label>
								<label class="mr10"><input type="radio" onchange="distEventHandler(this)" name="outsidePurpose" value="O002" disabled>${e3ps:getMessage('완제품')}</label>
								<label class="mr10"><input type="radio" onchange="distEventHandler(this)" name="outsidePurpose" value="O003" disabled>${e3ps:getMessage('반제품')}</label> )
							</div>
						</td>
					</tr>
				<%-- 	<tr>
						<th scope="col" colspan="2">${e3ps:getMessage('배포 타입')}<span class="required">*</span></th>
						<td>
							<input type="text" class="w70" id="distType" name="distType">
							<!-- <select id="distType" name="distType" class="w70" onchange="onchangeEventHandler(this)"></select>  -->
						</td>
						<th scope="col">${e3ps:getMessage('업체')}<span class="required">*</span></th>
						<td>
							<input type="text" class="w70" id="company" name="company">
							<!-- <span id="company_noAuto"> 
								<select id="company" name="company" data-param="company" data-width="70%"></select>
								<span class="pointer verticalMiddle" onclick="javascript:deleteUser('company');">
									<img class="verticalMiddle" src="/Windchill/jsp/portal/images/delete_icon.png" />
								</span>
							</span> -->
						</td>
					</tr> --%>
					<tr>
						<th>${e3ps:getMessage('표기 확인')}</th>
						<td>
							<label class="mr10"><input type="checkbox" name="markingConfirm" value="M001"/>${e3ps:getMessage('공차 표기 유무')}</label>
							<label class="mr10"><input type="checkbox" name="markingConfirm" value="M002"/>${e3ps:getMessage('노트 표기 유무')}</label>
							<label><input type="checkbox" name="markingConfirm" value="M003"/>${e3ps:getMessage('OEM P/N 표기 유무')}</label>
						</td>
					</tr>
					<tr>
						<th>${e3ps:getMessage('파일형식')}</th>
						<td>
							<label class="mr10"><input type="checkbox" name="fileType" value="F001"/>PDF</label>
							<label class="mr10"><input type="checkbox" name="fileType" value="F002"/>2D CAD</label>
							<label class="mr10"><input type="checkbox" name="fileType" value="F003"/>3D Step</label>
							<label><input type="checkbox" name="fileType" onclick="distEventHandler(this)" value="F004"/>${e3ps:getMessage('기타')} ( <input type="text" name="fileType" class="w20" disabled/> )</label>
						</td>
					</tr>
					<tr>
						<th>${e3ps:getMessage('배포일')}</th>
						<td class="calendar">
							<input type="text" class="datePicker w10" name="distDate" id="distDate" readonly />
						</td>
					</tr>
					<tr>
						<th scope="col">
							<div class="purposeInside">${e3ps:getMessage('회수')}<span class="required">*</span></div>
							<div class="purposeOutside" style="display:none">${e3ps:getMessage('다운로드 기한')}<span class="required">*</span></div>
							
						</th>
						<td>
							<div class="purposeInside">
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
								<textarea name="description" id="description" escapeXml="false" /></textarea>
							</div>
						</td>
					</tr>
				</tbody>
			</table>

		</div>

		<div class="ml30 mr30">
			<jsp:include
				page="${e3ps:getIncludeURLString('/distribute/include_distributePartList')}"
				flush="true">
				<jsp:param name="gridHeight" value="400" />
				<jsp:param name="title" value="${e3ps:getMessage('배포 요청 품목')}" />
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

<!-- //pop-->


