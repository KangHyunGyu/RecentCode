<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//권한 분류
	getAuthorityGroupTypeList();
	
	//정책 도메인 리스트
	getDomainList('codeType');
	
	//권한 타겟 리스트
	getAuthObjectList();
	
	//권한 리스트
	getAuthList();
	
	
	document.getElementById('codeType').addEventListener("change", (evt) => {
		let codeTypeValue = document.getElementById('codeType').value;
		let authObjectSelect  = document.querySelector('#authObject');
		let authTargetDiv = document.querySelectorAll('.authTargetDiv');
		if(codeTypeValue == 'MODULE'){
			authTargetDiv.forEach(item => item.style.display='block');
		}else if(codeTypeValue == 'DOCFOLDER'){
			authTargetDiv.forEach(item => item.style.display='none');
			authObjectSelect.value = Array.from(authObjectSelect.options).find(item => item.text == '문서').value;
		}else{
			authTargetDiv.forEach(item => item.style.display='none');
			authObjectSelect.value = Array.from(authObjectSelect.options).find(item => item.text == '프로젝트').value;
		}
		getDomainList('codeType');
	})
});
function save(){
	
	if(isEmpty(getElementValue('name'))){
		alert("${e3ps:getMessage('그룹 명을 입력해주세요')}.")
		return;
	}else if(!confirm("${e3ps:getMessage('그룹을 추가하시겠습니까')}?")){
		return false;
	}
	const codeTypeValue = document.getElementById('codeType').value;
	
	var param = new Object();
	param = getFormParams('groupForm', param);
	ajaxCallServer(getURLString("/admin/createAuthorityGroupAction"), param, function(){
		opener.parent.changeAuthorityClassification(codeTypeValue);
	}, false);
	
}
</script>
<div class="pop" style="min-width:560px;"> 
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('그룹 추가')}</h2>
	</div>
	<!-- button -->
	<div class="seach_arm2 pt10 pb5 pl15 pr15">
		<div class="leftbt">
		</div>
		<div class="rightbt">
			<button type="button" class="s_bt03" onclick="save();">${e3ps:getMessage('추가')}</button>
		</div>
	</div>
	<!-- //button -->
	<form name="groupForm" id="groupForm" method="post">
		<div class="pro_table mr15 ml15 pl15 pr15">
			<table class="mainTable">
				<colgroup>
					<col style="width:30%">
					<col style="width:70%">
				</colgroup>	
				<tbody>
					<tr>
						<th>${e3ps:getMessage('권한 분류')}<span class="required">*</span></th>
						<td>
							<select id="codeType" name="codeType" class="w20"></select>
						</td>
					</tr>
					<tr>
						<th>${e3ps:getMessage('도메인')}<span class="required">*</span></th>
						<td>
							<select id="domainPath" name="domainPath" class="w20"></select>
						</td>
					</tr>
					<tr>
						<th><div class="authTargetDiv">${e3ps:getMessage('권한 타겟')}<span class="required">*</span></div></th>
						<td>
							<div class="authTargetDiv"><select id="authObject" name="authObject"  class="w20"></select></div>
						</td>
					</tr>
					<tr>
						<th>${e3ps:getMessage('그룹 명')}<span class="required">*</span></th>
						<td>
							<input type="text" class="w50" id="name" name="name" autofocus/>
						</td>
					</tr>
					<%-- <tr>
						<th>${e3ps:getMessage('그룹 코드')}<span class="required">*</span></th>
						<td>
							<input type="text" class="w50" id="groupName" name="groupName" />
						</td>
					</tr> --%>
					<tr>
						<th scope="col">${e3ps:getMessage('권한')}<span class="required">*</span></th>
						<td>
							<div id="authList"></div>
						</td>
					</tr>
					<tr>
						<th>${e3ps:getMessage('설명')}</th>
						<td class="pd10">
							<div class="textarea_autoSize">
								<textarea name="description" id="description" escapeXml="false" /></textarea>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</form>
</div>