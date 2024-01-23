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
				<th scope="col">${e3ps:getMessage('번호')}</th>
				<td>${receipt.submit_number}</td>
				<th scope="col">${e3ps:getMessage('발신 종류')}</th>
				<td>${receipt.order_typeStr}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('제목')}</th>
				<td>${receipt.submit_name}</td>
				<td colspan="2"></td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('타입')}</th>
				<td>${receipt.submit_typeStr}</td>
				<th scope="col">${e3ps:getMessage('분류')}</th>
				<td>${receipt.submit_classification_name}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('프로젝트 번호')}</th>
				<td>${receipt.pjt_no}</td>
				<th scope="col">${e3ps:getMessage('업체')}</th>
				<td>${receipt.company_name}</td>
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('작성일자')}</th>
				<td>${receipt.c_date}</td>
				<th scope="col">${e3ps:getMessage('상태')}</th>
				<td>${receipt.state_name}</td>
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('접수일자')}</th>
				<td>${receipt.reception_date}</td>
				<th scope="col">${e3ps:getMessage('접수 담당자')}</th>
				<td>${receipt.receptionist_name}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('비고')}</th>
				<td colspan="3" class="pd10">
					<div class="textarea_autoSize">
						<textarea name="" id="" readonly><c:out value="${receipt.remarks }"/></textarea>
					</div>
				</td>								
			</tr>	
		</tbody>
	</table>	
</div>
<!-- /pro_table -->
<jsp:include page="${e3ps:getIncludeURLString('/distribute/include_fileView')}" flush="true">
	<jsp:param name="instance_id" value="${receipt.instance_id}"/>
	<jsp:param name="gridHeight" value="200"/>
</jsp:include>