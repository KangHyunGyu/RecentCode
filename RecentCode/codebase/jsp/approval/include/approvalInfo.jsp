<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript" src="/Windchill/jsp/js/distribute.js"></script>
<script>
var modifySupplier = "${appData.objectType}"=='DIS' && ${appData.lastApproval} && "${appData.orderType}" == 'I';
$(document).ready(function(){
	// 배포 승인인 경우 업체 선정 (배포 || 최종 승인자 || 발주타입이 초도배포(I) 인 경우만 적용)
	$("#checkSupplier").val(modifySupplier);
	if("${appData.supplierId}" == null || "${appData.supplierId}" == ""){
		$("#checkSupplierValue").val("false");
	}else{
		$("#checkSupplierValue").val("true");
	}
});
// 업체 저장
function saveSupplier(){
	if(!confirm("${e3ps:getMessage('저장하시겠습니까?')}")) return;
	
	if($("#supplierId").val() == null || $("#supplierId").val() == "") {
		$("#supplierId").focus();
		openNotice("${e3ps:getMessage('업체를 입력하여 주십시오.')}");
		return false;
	}
	
	var param = new Object();
	param["supplierId"] = $("#supplierId").val();
	param["supplierName"] = $("#supplierName").val();
	param["oid"] = "${appData.objectOid}";
	var url = getURLString("/distribute/modifySupplier");
	ajaxCallServer(url, param, function(data){
		if(data.result){
			$("#checkSupplierValue").val("true");
		}else{
			$("#checkSupplierValue").val("false");
		}
	}, true);
}

</script>
<!-- button -->
<div class="seach_arm pt10 pb10">
	<div class="leftbt">
		<h4>${e3ps:getMessage('결재 정보')}</h4>
	</div>
	<div class="rightbt">
		<c:choose>
			<c:when test= "${appData.roleType eq 'DRAFT'}">
				<button type="button" class="s_bt03" value="COMPLETE" onclick="approveAction(this);" id="submit">${e3ps:getMessage('제출')}</button>
				<button type="button" class="s_bt03" value="STANDING" onclick="approveAction(this);" id="standing">${e3ps:getMessage('저장')}</button>
				<%-- <button type="button" class="s_bt03" value="CANCEL" onclick="approveAction(this)" id="cancel">${e3ps:getMessage('취소')}</button> --%>
			</c:when>
			<c:when test= "${appData.roleType eq 'DISCUSS'}">
				<button type="button" class="s_bt03" value="COMPLETE" onclick="approveAction(this);" id="discuss">${e3ps:getMessage('합의')}</button>
				<button type="button" class="s_bt03" value="APPEAL" onclick="approveAction(this);" id="appeal">${e3ps:getMessage('이의')}</button>
			</c:when>
			<c:when test="${appData.objectOid.contains('EChangeActivity')}">
				<button type="button" class="i_create" style="width:70px" value="COMPLETE" onclick="approveAction(this);" id="complete">${e3ps:getMessage('활동 완료')}</button>
			</c:when>
			<c:when test= "${appData.roleType eq 'APPROVE'}">
				<button type="button" class="s_bt03" value="COMPLETE" onclick="approveAction(this);" id="approve">${e3ps:getMessage('승인')}</button>
				<button type="button" class="s_bt03" value="REJECT" onclick="approveAction(this);" id="reject">${e3ps:getMessage('반려')}</button>
				<c:if test="${appData.objectType eq 'ECR'}">
<!-- 					<button type="button" class="s_bt03" value="DROP" onclick="dropAction(this);" id="drop">Drop</button> -->
				</c:if>
			</c:when>
			<c:when test= "${appData.roleType eq 'RECEIVE'}">
				<button type="button" class="s_bt03" value="COMPLETE" onclick="approveAction(this);" id="receive">${e3ps:getMessage('수신확인')}</button>
			</c:when>
		</c:choose>
		<button type="button" class="s_bt03" onclick="javascript:history.back();">${e3ps:getMessage('뒤로가기')}</button>
	</div>
</div>
<!-- //button -->
<!-- pro_table -->
<div class="pro_table mr30 ml30">
	<table class="mainTable">
		<colgroup>
			<col style="width:15%">
			<col style="width:35%">
			<col style="width:15%">
			<col style="width:35%">
		</colgroup>	
		<tbody>
			<tr>
				<th scope="col">${e3ps:getMessage('번호')}</th>
				<td colspan="3">
				<c:if test="${appData.changeOid ne ''}">
					<a href="javascript:openView('${appData.changeOid}')">
					${appData.number}
					</a>
				</c:if>
				<c:if test="${appData.changeOid eq ''}">
					<a href="javascript:openView('${appData.objectOid}')">
					${appData.number}
					</a>
				</c:if>
				</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('제목')}</th>
				<td>${appData.name}</td>
            	<th scope="col">${e3ps:getMessage('활동')}</th>
				<td>${appData.roleName}</td>
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('작업자')}</th>
				<td>${appData.ownerFullName}</td>
				<th scope="col">${e3ps:getMessage('상태')}</th>
				<td>${appData.stateName}</td>
			</tr>	
			<input type="hidden" name="checkSupplier" id="checkSupplier" value="">
			<input type="hidden" name="checkSupplierValue" id="checkSupplierValue" value="">
			<c:if test="${appData.objectType eq 'DIS' && appData.lastApproval && appData.orderType eq 'I'}">
				<tr>
					<th scope="col">${e3ps:getMessage('배포업체')}</th>
					<td colspan="2">
						<input type="hidden" name="supplierName" id="supplierName" value="${appData.supplierName}">
						<select class="searchSupplier" data-width="70%" name="supplierId" id="supplierId" onchange="javascript:setSupplierData(this);">
							<option value="${appData.supplierId}" >${appData.supplierId} : ${appData.supplierName}</option>
						</select>
						<span class="pointer verticalMiddle" onclick="javascript:openSupplierPopup();"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
						<span class="pointer verticalMiddle" onclick="javascript:deleteCode('supplierId');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
						<button type="button" class="s_bt03" onclick="saveSupplier();">${e3ps:getMessage('업체 저장')}</button>
					</td>
					<td colspan="1"></td>
				</tr>
			</c:if>
		</tbody>
	</table>
</div>
<!-- //pro_table -->
<!-- 결재 의견 -->
<div class="seach_arm pt10 pb5">
	<div class="leftbt">
		<h4>${e3ps:getMessage('결재  의견')}</h4>
	</div>
</div>
<div class="pro_table mr30 ml30">
	<table class="mainTable">
		<tr>
			<td class="pd15">
			<div class="textarea_autoSize">
				<textarea name="description" id="description" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)">${appData.description}</textarea>
			</div>
			</td>
		</tr>
	</table>
</div>
<!-- 결재 의견 -->