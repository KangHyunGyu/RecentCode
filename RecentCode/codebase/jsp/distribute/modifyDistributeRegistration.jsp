<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function() {
	//팝업 리사이즈
	popupResize();
	
	//업체User 정보
	getCompanyUsers('#companyUser_set select');
	
	//업체 정보
	let companyJson = JSON.parse('${companyList}');
	$("#distributeCompany").find("option").remove();
	$("#distributeCompany").append("<option value=''>${e3ps:getMessage('선택')}</option>");
	companyJson.forEach((item) => {
		$("#distributeCompany").append("<option value='" + item.company_id + "'>" + item.company_name + "</option>");
	})
	
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
	}else{
		purposeOutside.forEach((element)=> {
			element.style.display = 'block';
		})
	}
	
	//사내 사외 체크
	document.getElementsByName('purpose').forEach((tag) => {
		if(purpose1 == tag.value){
			tag.checked = true;
		}
	})
	
	//용도 하위 체크
	purpose2.forEach((val)=> {
		let exist = false;
		document.getElementsByName(purpose1+'Purpose').forEach((tag) => {
			if(val == tag.value){
				tag.checked = true;
				exist = true;
			}
		})
		if(!exist)document.querySelector('#'+purpose1+'Purpose').innerHTML="( "+val+" )";
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
	"${distribute.fileType}".split(',').forEach((val)=> {
		let exist = false;
		document.getElementsByName('fileType').forEach((tag) => {
			if(val == tag.value){
				tag.checked = true;
				exist = true;
			}
		})
		if(!exist)document.querySelector('#fileType').innerHTML="( "+val+" )";
	})
	//회수
	document.getElementsByName('withdraw').forEach((tag) => {
		if("${distribute.withdraw}" == tag.value){
			tag.checked = true;
		}
	})
	
	document.getElementById('distributeCompany').value = '${distribute.distributeCompany}';
	
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


function onchangeEventHandler(e){
	
	document.getElementById('distributeTarget').options.length = 0;
}

function save(){
	
	const addedItemList = AUIGrid.getGridData(add_distributePartList_myGridID);
	
	try{
		
		if(isEmpty(getElementValue('distributeCompany'))){
			throw new Error("${e3ps:getMessage('배포 업체를 선택해주세요')}.");
			
		}else if(isEmpty(getElementValue('distributeTarget'))){
			throw new Error("${e3ps:getMessage('배포할 유저를 선택해주세요')}.");
			
		}else if(addedItemList.length == 0 || !addedItemList[0].oid){
			throw new Error("${e3ps:getMessage('배포 요청할 품목을 선택해주세요')}.");
			
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
	param.distributeParts = addedItemList;
	param = getFormParams('distributeForm', param);
	param.oid = '${distribute.oid}';
	
	var url	= getURLString("/distribute/modifyDistributeRegistrationAction");
	ajaxCallServer(url, param, null, true);
}

</script>


<div class="pop">
<form name="distributeForm" id="distributeForm" method="post">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('배포 요청 수정')}</h2>
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
						<col style="width: 30%">
						<col style="width: 20%">
						<col style="width: 30%">
					</colgroup>
					<tbody>
							<tr>
								<th scope="col">${e3ps:getMessage('의뢰서 번호')}</th>
								<td colspan="3">
									${distribute.distNumber}
								</td>
							</tr>
							<tr>
								<th scope="col">${e3ps:getMessage('의뢰서 명')}</th>
								<td colspan="3">
									${distribute.distName}
								</td>
							</tr>
							<tr>
								<th scope="rowgroup" rowspan="2">${e3ps:getMessage('용도')}</th>
								<td colspan="3">
									<div class='pt5 pb5'>
										<label class="mr10"><input type="radio" name="purpose" value="inside" onclick="return false;"/>${e3ps:getMessage('사내')}</label> 
										<label class="mr10"><input type="radio" name="purpose" value="outside" onclick="return false;"/>${e3ps:getMessage('사외')}</label>
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="3" class='pt5 pb5'>
									<div class="purposeInside" style="display:none">
										<label class="mr10"><input type="checkbox" name="insidePurpose" value="I001" onclick="return false;"/>${e3ps:getMessage('각 부서 참고용(생산/품질/영업)')}</label>
										<label class="mr10"><input type="checkbox" name="insidePurpose" value="I002" onclick="return false;"/>${e3ps:getMessage('검사성적서용')}</label>
										<label class="mr10"><input type="checkbox" name="insidePurpose" value="I003" onclick="return false;"/>${e3ps:getMessage('견적용')}</label>
										<label><input type="checkbox" name="insidePurpose" value="I004" onclick="return false;" />${e3ps:getMessage('기타')} <span id="insidePurpose"></span></label>
									</div>
											
									<div class="purposeOutside" style="display:none">
										<label class="mr10"><input type="radio" name="outsidePurpose" value="O001"  onclick="return false;">${e3ps:getMessage('고객사 제출용 ')}<span id="outsidePurpose"></span></label>
										<label>${e3ps:getMessage('외주가공용 ( ')}</label>
										<label class="mr10"><input type="radio" name="outsidePurpose" value="O002"  onclick="return false;">${e3ps:getMessage('완제품')}</label>
										<label class="mr10"><input type="radio" name="outsidePurpose" value="O003"  onclick="return false;">${e3ps:getMessage('반제품')}</label> )
									</div>
								</td>
							</tr>
							<tr>
								<th>${e3ps:getMessage('표기 확인')}</th>
								<td colspan="3">
									<label class="mr10"><input type="checkbox" name="markingConfirm" value="M001" onclick="return false;" />${e3ps:getMessage('공차 표기')}</label> 
									<label class="mr10"><input type="checkbox" name="markingConfirm" value="M002" onclick="return false;" />${e3ps:getMessage('노트 표기')}</label> 
									<label><input type="checkbox" name="markingConfirm" value="M003" onclick="return false;" />${e3ps:getMessage('OEM P/N 표기')}</label>
								</td>
							</tr>
							<tr>
								<th>${e3ps:getMessage('파일형식')}</th>
								<td colspan="3">
									<label class="mr10"><input type="checkbox" name="fileType" value="F001" onclick="return false;" />PDF</label> 
									<label class="mr10"><input type="checkbox" name="fileType" value="F002" onclick="return false;" />2D CAD</label> 
									<label><input type="checkbox" name="fileType" value="F004" onclick="return false;" />${e3ps:getMessage('기타')} <span id="fileType"></span></label>
								</td>
							</tr>
							<tr>
								<th>${e3ps:getMessage('배포일')}</th>
								<td colspan="3" class="calendar">
									${distribute.distDate}
								</td>
							</tr>
							<tr>
								<th scope="col">
									<div class="purposeInside"  style="display:none">${e3ps:getMessage('회수')}</div>
									<div class="purposeOutside" style="display:none">${e3ps:getMessage('다운로드 기한')}</div>
											
								</th>
								<td colspan="3">
									<div class="purposeInside"  style="display:none">
										<label class="mr10"><input type="radio" name="withdraw" value="W001"  onclick="return false;"/>${e3ps:getMessage('필요')}</label>
										<label><input type="radio" name="withdraw" value="W002"  onclick="return false;"/>${e3ps:getMessage('불필요 (자체파기 유무)')}</label>
									</div>
									<div class="purposeOutside calendar" style="display:none">
										${distribute.downloadDate}
									</div>
								</td>
							</tr>
							<tr>
								<th>${e3ps:getMessage('추가입력사항')}</th>
								<td colspan="3" class="pd10">
									<div class="textarea_autoSize">
										<textarea name="description" id="description" readonly><c:out value="${distribute.description}" escapeXml="false" /></textarea>
									</div>
								</td>
							</tr>
							<tr> 
							<th>${e3ps:getMessage('첨부파일')}</th>
							<td colspan="3">
								<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
				                     <jsp:param name="formId" value="distributeForm"/>
				                     <jsp:param name="command" value="insert"/>
				                     <jsp:param name="type" value="SECONDARY" />
				                     <jsp:param name="btnId" value="createBtn" />
				                     <jsp:param name="oid" value="${distribute.oid}"/>
				                 </jsp:include>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="seach_arm2 pt10 pb10">
				<div class="leftbt">
					<span class="title"> <img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png"> ${e3ps:getMessage('배포 요청서 정보')}
					</span>
				</div>
				<div class="rightbt"></div>
			</div>
			<div class="pro_table">
				<table class="mainTable">
					<colgroup>
						<col style="width: 20%">
						<col style="width: 30%">
						<col style="width: 20%">
						<col style="width: 30%">
					</colgroup>
					<tbody>
						<tr>
							<th>${e3ps:getMessage('배포 업체')}<span class="required">*</span></th>
							<td class="pd10">
								<select id=distributeCompany name="distributeCompany" style="width: 65%;" onchange="onchangeEventHandler(this)"></select>
							</td>
							<th>${e3ps:getMessage('배포 유저')}<span class="required">*</span></th>
							<td class="pd10">
								<span id="companyUser_set"> 
									<select id="distributeTarget" name="distributeTarget" data-param="keyword" data-width="65%">
										<option value="${distribute.distributeTarget}">${distribute.distributeTargetName}</option>
									</select> 
									<span class="pointer verticalMiddle" onclick="javascript:deleteUser('distributeTarget');"> 
										<img class="verticalMiddle" src="/Windchill/jsp/portal/images/delete_icon.png" />
									</span>
								</span>
							</td>
						</tr>
						<tr>
							<th scope="col">${e3ps:getMessage('상태')}</th>
							<td colspan="3">
								${distribute.stateName}
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('추가입력사항')}</th>
							<td colspan="3" class="pd10">
								<div class="textarea_autoSize">
									<textarea name="descriptionDRF" id="descriptionDRF"><c:out value="${distribute.descriptionDRF}" escapeXml="false"/></textarea>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div style="margin-bottom:100px;">
				<jsp:include
					page="${e3ps:getIncludeURLString('/distribute/include_distributePartList')}"
					flush="true">
					<jsp:param name="oid" value="${distribute.oid}" />
					<jsp:param name="gridHeight" value="180" />
					<jsp:param name="title" value="${e3ps:getMessage('배포 요청 품목')}" />
					<jsp:param name="type" value="modify" />
				</jsp:include>
			</div>

	</div>
</form>
</div>
