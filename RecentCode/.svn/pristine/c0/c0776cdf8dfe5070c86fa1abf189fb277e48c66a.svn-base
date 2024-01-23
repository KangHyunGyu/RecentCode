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
				<td>${data.tempNumber }</td>
				<th scope="col">${e3ps:getMessage('배포 제목')}</th>
				<td>${data.title}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('프로젝트 번호')}</th>
				<td>${data.pjtNo}</td>
				<th scope="col">${e3ps:getMessage('프로젝트 명')}</th>
				<td>${data.pjtName}</td>
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('작성자')}</th>
				<td>${data.creator}</td>
				<th scope="col">${e3ps:getMessage('작성일')}</th>
				<td>${data.createDate}</td>
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('CAD 타입')}</th>
				<td>${data.cadTypeStr}</td>
				<td colspan="2"></td>
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('비고')}</th>
				<td colspan="3" class="pd10">
					<div class="textarea_autoSize">
						<textarea name="" id="" readonly><c:out value="${data.description }" escapeXml="false" /></textarea>
					</div>
				</td>								
			</tr>	
		</tbody>
	</table>	
</div>
<!-- /pro_table -->