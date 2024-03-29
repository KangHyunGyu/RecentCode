<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	//팝업 리사이즈
	popupResize();
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
	
}
</script>
<div class="seach_arm2 pt10 pb10">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png"> ${e3ps:getMessage('도면 출도 의뢰서 정보')}</h4></div>
	<div class="rightbt">
	</div>
</div>
<!-- pro_table -->
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
				<th scope="col">${e3ps:getMessage('의뢰서 명')}</th>
				<td>
					${distribute.distName}
				</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('상태')}</th>
				<td>
					${distribute.stateName}
				</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('등록자')}</th>
				<td>
					${distribute.creator}
				</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('등록일')}</th>
				<td>
					${distribute.createFullDate}
				</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('수정일')}</th>
				<td>
					${distribute.updateFullDate}
				</td>
			</tr>
			<tr>
				<th scope="rowgroup" rowspan="2">${e3ps:getMessage('용도')}</th>
				<td>
					<div class='pt5 pb5'>
						<label class="mr10"><input type="radio" name="purpose" value="inside" onclick="return false;"/>${e3ps:getMessage('사내')}</label> 
						<label class="mr10"><input type="radio" name="purpose" value="outside" onclick="return false;"/>${e3ps:getMessage('사외')}</label>
					</div>
				</td>
			</tr>
			<tr>
				<td class='pt5 pb5'>
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
				<td>
					<label class="mr10"><input type="checkbox" name="markingConfirm" value="M001" onclick="return false;" />${e3ps:getMessage('공차 표기')}</label> 
					<label class="mr10"><input type="checkbox" name="markingConfirm" value="M002" onclick="return false;" />${e3ps:getMessage('노트 표기')}</label> 
					<label><input type="checkbox" name="markingConfirm" value="M003" onclick="return false;" />${e3ps:getMessage('OEM P/N 표기')}</label>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('파일형식')}</th>
				<td>
					<label class="mr10"><input type="checkbox" name="fileType" value="F001" onclick="return false;" />PDF</label> 
					<label class="mr10"><input type="checkbox" name="fileType" value="F002" onclick="return false;" />2D CAD</label> 
					<label><input type="checkbox" name="fileType" value="F004" onclick="return false;" />${e3ps:getMessage('기타')} <span id="fileType"></span></label>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('배포일')}</th>
				<td class="calendar">
					${distribute.distDate}
				</td>
			</tr>
			<tr>
				<th scope="col">
					<div class="purposeInside"  style="display:none">${e3ps:getMessage('회수')}</div>
					<div class="purposeOutside" style="display:none">${e3ps:getMessage('다운로드 기한')}</div>
							
				</th>
				<td>
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
				<th>${e3ps:getMessage('기타기입사항')}</th>
				<td class="pd10">
					<div class="textarea_autoSize">
						<textarea name="description" id="description" readonly><c:out value="${distribute.description}" escapeXml="false" /></textarea>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<br>
