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
				<th scope="col">${e3ps:getMessage('업체명')}</th>
				<td>${supplier.supplierName }</td>
				<th scope="col">${e3ps:getMessage('업체코드')}</th>
				<td>${supplier.supplierCode}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('대표자')}</th>
				<td colspan="3">${supplier.president}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('업태')}</th>
				<td>${supplier.businessCondition}</td>
				<th scope="col">${e3ps:getMessage('업종')}</th>
				<td>${supplier.businessType}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('전화번호')}</th>
				<td>${supplier.telNo}</td>
				<th scope="col">${e3ps:getMessage('팩스번호')}</th>
				<td>${supplier.faxNo}</td>
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('등록일')}</th>
				<td>${supplier.createDate}</td>
				<th scope="col">${e3ps:getMessage('수정일')}</th>
				<td>${supplier.modifyDate}</td>
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('주소')}</th>
				<td colspan="3">${supplier.address}</td>
			</tr>	
		</tbody>
	</table>	
</div>
