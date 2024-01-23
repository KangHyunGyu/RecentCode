<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>

<!-- jQuery , jQuery UI 사용 -->
<script src="https://code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
<link rel="stylesheet" href="https://code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
<script src="/Windchill/jsp/component/AUIGrid/e3ps_auicomponent.js"></script>

<style>
/* 헤더 메뉴 스타일 정의 */
#headerMenu {
	position: absolute;
	display: none;
	z-index: 999;
}
/* jQuery UI Menu 스타일 재정의 */
.ui-menu {
	width: 160px;
	font-size: 12px;
	box-shadow: 3px 3px 3px rgba(0, 0, 0, 0.3);
	-webkit-box-shadow: 3px 3px 3px rgba(0, 0, 0, 0.3);
	-moz-box-shadow: 3px 3px 3px rgba(0, 0, 0, 0.3);
}  
</style>
<script>

$(document).ready(function() {
	
	//도면 출도 의뢰서 가져오기 Select 활성화
	searchDistributeDoc('distributeDocument');
	
	//업체User 정보
	getCompanyUsers('#companyUser_set select');
	
	//업체 정보
	let companyJson = JSON.parse('${companyList}');
	$("#distributeCompany").find("option").remove();
	$("#distributeCompany").append("<option value=''>${e3ps:getMessage('선택')}</option>");
	companyJson.forEach((item) => {
		$("#distributeCompany").append("<option value='" + item.company_id + "'>" + item.company_name + "</option>");
	})
	
	
});

let objArr=[];

//도면 출도 의뢰서 정보
let distributeDocData={};

//도면 출도 의뢰서 팝업 창을 통해 추가시
function addDistributeDoc(addItemList){
	distributeDocData = addItemList;
	$("#distributeDocument").append("<option value='" + addItemList.oid + "' selected>" + addItemList.distNumber + "</option>");
	$("input[name='distributeDocument']").val(addItemList.oid);
	document.getElementById('distributeDocument').dispatchEvent(new Event('change'));
}


function disDocChange(){
	
	let distributeDocValue = document.getElementById('distributeDocument').value;
	
	// X 버튼 눌러 데이터 지울 시
	if(!distributeDocValue){
		distributeDocForm_reset();
		document.querySelectorAll('.distributeForm').forEach((element)=> {
			element.style.display = 'none';
		})
		//Grid 길이가 깨져서 resize 처리
		AUIGrid.resize(add_distributePartList_myGridID);
		AUIGrid.resize(app_line_myGridID);
		return;
	}
	
	if(!distributeDocData || distributeDocData.oid != distributeDocValue){
		distributeDocData = objArr.find(obj => obj.oid == distributeDocValue);
	}
	
	distributeDocForm_reset();
	
	document.querySelectorAll('.distributeForm').forEach((element)=> {
		element.style.display = '';
	})
	let purposeList = distributeDocData.purpose.split(',');
	let purpose1 = purposeList[0];
				   purposeList.shift();
	let purpose2 = purposeList;
	
	let purposeInside = document.querySelectorAll('.purposeInside');
	let purposeOutside = document.querySelectorAll('.purposeOutside');
	
	if(purpose1 == 'inside'){
		purposeInside.forEach((element)=> {
			element.style.display = 'block';
		})
		
		purposeOutside.forEach((element)=> {
			element.style.display = 'none';
		})
	}else{
		purposeOutside.forEach((element)=> {
			element.style.display = 'block';
		})
		
		purposeInside.forEach((element)=> {
			element.style.display = 'none';
		})
	}
	
	document.getElementById('distributeName').innerHTML=distributeDocData.distName
	document.getElementById('distributeCreator').innerHTML=distributeDocData.creator
	document.getElementById('distributeCreateDate').innerHTML=distributeDocData.createFullDate
	document.getElementById('distributeUpdateDate').innerHTML=distributeDocData.updateFullDate
	
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
	distributeDocData.markingConfirm.split(',').forEach((val)=> {
		document.getElementsByName('markingConfirm').forEach((tag) => {
			if(val == tag.value){
				tag.checked = true;
			}
		})
	})
	//파일 형식
	distributeDocData.fileType.split(',').forEach((val)=> {
		let exist = false;
		document.getElementsByName('fileType').forEach((tag) => {
			if(val == tag.value){
				tag.checked = true;
				exist = true;
			}
		})
		if(!exist)document.querySelector('#fileType').innerHTML="( "+val+" )";
	})
	
	//배포일
	document.getElementById('distributeDate').innerHTML=distributeDocData.distDate;
		
	//회수
	document.getElementsByName('withdraw').forEach((tag) => {
		if(distributeDocData.withdraw == tag.value){
			tag.checked = true;
		}
	})
	
	//다운로드 기한
	document.getElementById('downloadDate').innerHTML=distributeDocData.downloadDate;
	
	//기타 기입사항
	document.getElementById('description').value=distributeDocData.description;
	
	//Grid 길이가 깨져서 resize 처리
	AUIGrid.resize(add_distributePartList_myGridID);
	AUIGrid.resize(app_line_myGridID);
	
}

//도면 출도 의뢰서 초기화
function distributeDocForm_reset(){
	let allChildElement = getAllChildElements(document.querySelector('form'));
	allChildElement.forEach((element) => {
		if(element.nodeName == 'INPUT'){
			if(element.type == 'text'){
				element.value = '';
			}else if(element.type == 'checkbox' || element.type == 'radio'){
				element.checked = false;
			}
		}
	})
	
	document.querySelectorAll('.resetTarget').forEach((element)=>{
		element.innerHTML = '';
	})
	
}

function save(btn){
	const addedItemList = AUIGrid.getGridData(add_distributePartList_myGridID);
	const approvalList = AUIGrid.getGridData(app_line_myGridID);
	const appState = btn.value;
	try{
		
		if(isEmpty(getElementValue('distributeDocument'))){
			throw new Error("${e3ps:getMessage('도면 출도 의뢰서를 선택해주세요')}.");
			
		}else if(isEmpty(getElementValue('distributeCompany'))){
			throw new Error("${e3ps:getMessage('배포 업체를 선택해주세요')}.");
			
		}else if(isEmpty(getElementValue('distributeTarget'))){
			throw new Error("${e3ps:getMessage('배포할 유저를 선택해주세요')}.");
			
		}else if(addedItemList.length == 0 || !addedItemList[0].oid){
			throw new Error("${e3ps:getMessage('배포 요청할 품목을 선택해주세요')}.");
			
		}else if(approvalList.length == 0 && appState == 'APPROVING'){
			throw new Error("${e3ps:getMessage('결재선을 지정해주세요')}.");
			
		}else if(!approvalList.some((item) => item.roleType=='APPROVE') && appState == 'APPROVING'){
			throw new Error("${e3ps:getMessage('결재 승인자를 지정해주세요')}.");
			
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
	param = getFormParams('distributeForm', param)
	param.oid = document.getElementById('distributeDocument').value;	
	param.purchaseParts = addedItemList;
	param.approvalList = approvalList;
	param.appState = appState;
	
	var url	= getURLString("/distribute/createRegistrationDistributeAction");
	ajaxCallServer(url, param, function(){}, true);
}
</script>
<div class="product">
  <form name="distributeForm" id="distributeForm" method="post" enctype="multipart/form-data">
	<!-- button -->
		<div class="seach_arm pt20 pb5">
			<div class="leftbt">
				<span class="title"> <img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png"> ${e3ps:getMessage('도면 출도 의뢰서 정보')}
				</span>
			</div>
			<div class="rightbt">
			    <button type="button" value="TEMP_STORAGE" class="s_bt03" onclick="save(this)">${e3ps:getMessage('임시 저장')}</button>
				<button type="button" value="APPROVING" class="s_bt03" style="width: 70px" onclick="save(this)">${e3ps:getMessage('등록')}</button>
			</div>
		</div>
		<!-- //button -->
		<div class="pro_table" style="padding-left: 30px; padding-right: 30px;">
		<table class="mainTable">
			<colgroup>
				<col style="width: 20%">
				<col style="width: 30%">
				<col style="width: 20%">
				<col style="width: 30%">
			</colgroup>
			<tbody>
				<tr>
					<th>${e3ps:getMessage('도면 출도 의뢰서 가져오기')}<span class="required">*</span></th>
					<td  colspan="3">
						<div class="pro_view">
							<select id="distributeDocument" name="distributeDocument" data-width="20%" onchange="disDocChange();">
							</select>
							<span class="pointer verticalMiddle" onclick="javascript:openDistributeDocPopup('distributeDocument', 'APPROVED');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
							<span class="pointer verticalMiddle" onclick="javascript:deleteDistributeDoc('distributeDocument');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
						</div>
					</td>
				</tr>
				<tr style="display:none;" class="distributeForm">
					<th scope="col">${e3ps:getMessage('의뢰서 명')}</th>
					<td colspan="3">
						<span class="resetTarget" id="distributeName"></span>
					</td>
				</tr>
				<tr style="display:none;" class="distributeForm">
					<th scope="col">${e3ps:getMessage('등록자')}</th>
					<td>
						<span class="resetTarget" id="distributeCreator"></span>
					</td>
				</tr>
				<tr style="display:none;" class="distributeForm">
					<th scope="col">${e3ps:getMessage('등록일')}</th>
					<td>
						<span class="resetTarget" id="distributeCreateDate"></span>
					</td>
				</tr>
				<tr style="display:none;" class="distributeForm">
					<th scope="col">${e3ps:getMessage('수정일')}</th>
					<td>
						<span class="resetTarget" id="distributeUpdateDate"></span>
					</td>
				</tr>
				<tr style="display:none;" class="distributeForm">
					<th scope="rowgroup" rowspan="2">${e3ps:getMessage('용도')}</th>
					<td colspan="3">
						<div class='pt5 pb5'>
							<label class="mr10"><input type="radio" name="purpose" value="inside" onclick="return false;"/>${e3ps:getMessage('사내')}</label> 
							<label class="mr10"><input type="radio" name="purpose" value="outside" onclick="return false;"/>${e3ps:getMessage('사외')}</label>
						</div>
					</td>
				</tr>
				<tr style="display:none;" class="distributeForm">
					<td colspan="3" class='pt5 pb5'>
						<div class="purposeInside" style="display:none">
							<label class="mr10"><input type="checkbox" name="insidePurpose" value="I001" onclick="return false;"/>${e3ps:getMessage('각 부서 참고용(생산/품질/영업)')}</label>
							<label class="mr10"><input type="checkbox" name="insidePurpose" value="I002" onclick="return false;"/>${e3ps:getMessage('검사성적서용')}</label>
							<label class="mr10"><input type="checkbox" name="insidePurpose" value="I003" onclick="return false;"/>${e3ps:getMessage('견적용')}</label>
							<label><input type="checkbox" name="insidePurpose" value="I004" onclick="return false;" />${e3ps:getMessage('기타')} <span class="resetTarget" id="insidePurpose"></span></label>
						</div>
								
						<div class="purposeOutside" style="display:none">
							<label class="mr10"><input type="radio" name="outsidePurpose" value="O001"  onclick="return false;">${e3ps:getMessage('고객사 제출용 ')}<span class="resetTarget" id="outsidePurpose"></span></label>
							<label>${e3ps:getMessage('외주가공용 ( ')}</label>
							<label class="mr10"><input type="radio" name="outsidePurpose" value="O002"  onclick="return false;">${e3ps:getMessage('완제품')}</label>
							<label class="mr10"><input type="radio" name="outsidePurpose" value="O003"  onclick="return false;">${e3ps:getMessage('반제품')}</label> )
						</div>
					</td>
				</tr>
				<tr style="display:none;" class="distributeForm">
					<th>${e3ps:getMessage('표기 확인')}</th>
					<td colspan="3">
						<label class="mr10"><input type="checkbox" name="markingConfirm" value="M001" onclick="return false;" />${e3ps:getMessage('공차 표기')}</label> 
						<label class="mr10"><input type="checkbox" name="markingConfirm" value="M002" onclick="return false;" />${e3ps:getMessage('노트 표기')}</label> 
						<label><input type="checkbox" name="markingConfirm" value="M003" onclick="return false;" />${e3ps:getMessage('OEM P/N 표기')}</label>
					</td>
				</tr>
				<tr style="display:none;" class="distributeForm">
					<th>${e3ps:getMessage('파일형식')}</th>
					<td colspan="3">
						<label class="mr10"><input type="checkbox" name="fileType" value="F001" onclick="return false;" />PDF</label> 
						<label class="mr10"><input type="checkbox" name="fileType" value="F002" onclick="return false;" />2D CAD</label> 
						<label><input type="checkbox" name="fileType" value="F004" onclick="return false;" />${e3ps:getMessage('기타')} <span class="resetTarget" id="fileType"></span></label>
					</td>
				</tr>
				<tr style="display:none;" class="distributeForm">
					<th>${e3ps:getMessage('배포일')}</th>
					<td colspan="3" class="calendar">
						<span class="resetTarget" id="distributeDate"></span>
					</td>
				</tr>
				<tr style="display:none;" class="distributeForm">
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
							<span class="resetTarget" id="downloadDate"></span>
						</div>
					</td>
				</tr>
				<tr style="display:none;" class="distributeForm">
					<th>${e3ps:getMessage('기타기입사항')}</th>
					<td colspan="3" class="pd10">
						<div class="textarea_autoSize">
							<textarea name="description" id="description" readonly></textarea>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
		<div class="seach_arm pt20 pb5">
			<div class="leftbt">
				<span class="title"> <img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png"> ${e3ps:getMessage('배포 요청서 정보')}
				</span>
			</div>
			<div class="rightbt">
			</div>
		</div>
		<!-- //button -->
		<div class="pro_table" style="padding-left: 30px; padding-right: 30px;">
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
							<select id=distributeCompany name="distributeCompany" style="width: 65%;"></select>
						</td>
						<th>${e3ps:getMessage('배포 유저')}<span class="required">*</span></th>
						<td class="pd10">
							<span id="companyUser_set"> 
								<select id="distributeTarget" name="distributeTarget" data-param="keyword" data-width="65%"></select> 
								<span class="pointer verticalMiddle" onclick="javascript:deleteUser('distributeTarget');"> 
									<img class="verticalMiddle" src="/Windchill/jsp/portal/images/delete_icon.png" />
								</span>
							</span>
						</td>
					</tr>
					<tr>
						<th>${e3ps:getMessage('추가입력사항')}</th>
						<td colspan="3" class="pd10">
							<div class="textarea_autoSize">
								<textarea name="descriptionDRF" id="descriptionDRF" escapeXml="false" /></textarea>
							</div>
						</td>
					</tr>
					<tr> 
	               <th>${e3ps:getMessage('첨부파일')}</th>
	               <td colspan="3" class="pd10">
	                  <jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
	                     <jsp:param name="formId" value="distributeForm"/>
	                     <jsp:param name="command" value="insert"/>
	                     <jsp:param name="type" value="SECONDARY" />
	                     <jsp:param name="btnId" value="createBtn" />
	                  </jsp:include>
	               </td>
	            </tr>
				</tbody>
			</table>
		</div>
		<!-- 관련 부품 지정 include 화면 -->
	<div class="ml30 mr30">
		<jsp:include
			page="${e3ps:getIncludeURLString('/distribute/include_distributePartList')}"
			flush="true">
			<jsp:param name="gridHeight" value="180" />
			<jsp:param name="title" value="${e3ps:getMessage('배포 요청 품목')}" />
		</jsp:include>
	</div>
	<!-- 결재선 지정 include 화면 -->
	<div class="ml30 mr30">
		<jsp:include page="${e3ps:getIncludeURLString('/approval/include_addApprovalLine')}" flush="true">
			<jsp:param name="gridHeight" value="180"/>
			<jsp:param name="oid" value=""/>
		</jsp:include>
	</div>
	</form>
</div>
