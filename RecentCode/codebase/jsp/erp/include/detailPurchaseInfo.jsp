<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	//팝업 리사이즈
	popupResize();
});
</script>
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('기본 정보')}</h4></div>
	<div class="rightbt"></div>
</div>
<!-- pro_table -->
<div class="pro_table">
	<table class="mainTable">
		<colgroup>
			<col style="width:15%">
			<col style="width:35%">
			<col style="width:15%">	
			<col style="width:35%">			
		</colgroup>
		<tbody>
			<tr>
				<th scope="col">${e3ps:getMessage('구매발주서 번호')}</th>
				<td>${data.request_number}</td>
				<th scope="col">${e3ps:getMessage('프로젝트 번호')}</th>
				<td>${data.pjt_number}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('협력업체 코드')}</th>
				<td>${data.company_id}</td>
				<th scope="col">${e3ps:getMessage('PO 담당자 이름')}</th>
				<td>${data.po_user_name}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('PO 담당자 사번')}</th>
				<td>${data.po_user_id}</td>
				<th scope="col">${e3ps:getMessage('PO 담당자 e-mail')}</th>
				<td>${data.po_user_email}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('발주서 상태')}</th>
				<td>${data.purchase_state}</td>				
				<th scope="col">${e3ps:getMessage('I/F 상태')}</th>
				<td>${data.if_statusStr}</td>	
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('I/F 생성일')}</th>
				<td>${data.if_cdateStr}</td>				
				<th scope="col">${e3ps:getMessage('I/F 수령일')}</th>
				<td>${data.if_rdateStr}</td>					
			</tr>	
		</tbody>
	</table>	
</div>
<!-- /pro_table -->
