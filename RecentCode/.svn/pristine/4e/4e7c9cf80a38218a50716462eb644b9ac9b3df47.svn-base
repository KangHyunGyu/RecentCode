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
<div class="seach_arm2 pt10 pb10">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('기본 정보')}</h4></div>
	<div class="rightbt"></div>
</div>
<!-- pro_table -->
<div class="pro_table">
	<table class="mainTable">
		<colgroup>
			<col style="width:11%">
			<col style="width:22%">
			<col style="width:11%">	
			<col style="width:22%">
			<col style="width:11%">			
			<col style="width:23%">
		</colgroup>
		<tbody>
			<tr>
				<th scope="col">${e3ps:getMessage('부품 번호')}</th>
				<td>${part.partNumber}</td>
				<th scope="col">${e3ps:getMessage('부품 명')}</th>
				<td>${part.partName}</td>
				<th scope="col">${e3ps:getMessage('상태')}</th>
				<td>${part.stateState}</td>	
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('버전')}</th>
				<td>${part.version}</td>				
				<th scope="col">${e3ps:getMessage('등록자')}</th>
				<td>${part.creator}</td>	
				<th scope="col">${e3ps:getMessage('등록일')}</th>
				<td>${part.createDate}</td>
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('최종 수정일')}</th>
				<td>${part.latestModifyDate}</td>					
				<th scope="col">${e3ps:getMessage('단위')}</th>
				<td colspan="3">${part.unit}</td>
			</tr>
		</tbody>
	</table>
</div>
<!-- //pro_table -->

<div class="seach_arm2 pt10 pb10">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('속성')}</h4></div>
	<div class="rightbt"></div>
</div>
<!-- pro_table -->
<div class="pro_table">
	<table class="mainTable">
		<colgroup>
			<col style="width:11%">
			<col style="width:22%">
			<col style="width:11%">
			<col style="width:22%">
			<col style="width:11%">
			<col style="width:23%">
		</colgroup>
		<tbody>
			<tr>
				<th scope="col">${e3ps:getMessage('자재 타입')}</th>
				<td>${part.isBujaje}</td>
				<th scope="col">${e3ps:getMessage('규격')}</th>
				<td>${part.specification}</td>
				<th scope="col">${e3ps:getMessage('재질')}</th>
				<td>${part.material}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('중량')}</th>
				<td>${part.weight}</td>
				<th scope="col">${e3ps:getMessage('종료일자')}</th>
				<td>${part.endDate}</td>
				<th scope="col">${e3ps:getMessage('SN 관리 여부')}</th>
				<td>${part.snok}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('표면 처리')}</th>
				<td>${part.surfaceTreatment}</td>
				<th scope="col">Maker</th>
				<td>${part.maker}</td>
				<th scope="col">${e3ps:getMessage('참조부품')}</th>
				<td>${part.cPartNum}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('구매 여부')}</th>
				<td colspan="5">${part.purchaseFlag}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('개요')}</th>
				<td colspan="5" class="pt5 pb5">
					<div class="textarea_autoSize">
						<textarea name="contents" id="contents" readonly><c:out value="${part.summary}" escapeXml="false" /></textarea>
					</div>
				</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('비고')}</th>
				<td colspan="5" class="pt5 pb5">
					<div class="textarea_autoSize">
						<textarea name="contents" id="contents" readonly><c:out value="${part.notice}" escapeXml="false" /></textarea>
					</div>
				</td>
			</tr>
		</tbody>
	</table>	
</div>