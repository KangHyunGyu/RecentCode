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
				<th scope="col">${e3ps:getMessage('배포 번호')}</th>
				<td>${distribute.distributeNumber }</td>
				<th scope="col">${e3ps:getMessage('발주 타입')}</th>
				<td>${distribute.orderTypeStr}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('배포 제목')}</th>
				<td colspan="3">${distribute.distributeName}</td>
			</tr>
			<tr>	
				<th scope="col">${e3ps:getMessage('업체')}</th>
				<td>${distribute.supplierName}</td>
				<c:choose>
					<c:when test="${distribute.orderType == 'P'}">
						<th scope="col">${e3ps:getMessage('발주 번호')}</th>
					</c:when>
					<c:when test="${distribute.orderType == 'E'}">
						<th scope="col">${e3ps:getMessage('견적 요청 번호')}</th>
					</c:when>
				</c:choose>
				<td>${distribute.requestNumber}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('담당자')}</th>
				<td>${distribute.requesterName}</td>
				<th scope="col">${e3ps:getMessage('작성일')}</th>
				<td>${distribute.createDate}</td>
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('비고')}</th>
				<td colspan="3" class="pd10">
					<div class="textarea_autoSize">
						<textarea name="" id="" readonly><c:out value="${distribute.description}" escapeXml="false" /></textarea>
					</div>
				</td>								
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('버전 변경 이력')}</th>
				<td colspan="3" class="pd10">
					<div class="textarea_autoSize">
						<textarea name="" id="" readonly><c:out value="${distribute.versionHistory}" escapeXml="false" /></textarea>
					</div>
				</td>								
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('삭제 이력')}</th>
				<td colspan="3" class="pd10">
					<div class="textarea_autoSize">
						<textarea name="" id="" readonly><c:out value="${distribute.deleteHistory}" escapeXml="false" /></textarea>
					</div>
				</td>								
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('추가 이력')}</th>
				<td colspan="3" class="pd10">
					<div class="textarea_autoSize">
						<textarea name="" id="" readonly><c:out value="${distribute.addHistory}" escapeXml="false" /></textarea>
					</div>
				</td>								
			</tr>	
		</tbody>
	</table>	
</div>
<!-- /pro_table -->