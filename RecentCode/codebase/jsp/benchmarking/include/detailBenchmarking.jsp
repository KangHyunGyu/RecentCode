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
				<td>${bm.number}</td>
				<th scope="col">${e3ps:getMessage('제목')}</th>
				<td>${bm.name}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('BM 대상 (차종 및 샘플명)')}</th>
				<td>${bm.target}</td>				
				<th scope="col">${e3ps:getMessage('상태')}</th>
				<td>${bm.stateName}</td>	
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('BM 시작일')}</th>
				<td>${bm.bmStartDate}</td>				
				<th scope="col">${e3ps:getMessage('BM 완료일')}</th>
				<td>${bm.bmEndDate}</td>	
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('샘플 확보 방안 및 내역')}</th>
				<td colspan="3">${bm.sampleDesc}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('벤치마킹 내용')}</th>
				<td colspan="3" class="pd10">
					<div class="textarea_autoSize">
						<textarea name="bmDesc" id="bmDesc" readonly><c:out value="${bm.bmDesc }" escapeXml="false" /></textarea>
					</div>
				</td>								
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('비파괴 시험 항목')}</th>
				<td colspan="3" class="pd10">
					<div class="textarea_autoSize">
						<textarea name="nonDestruction" id="nonDestruction" readonly><c:out value="${bm.nonDestruction }" escapeXml="false" /></textarea>
					</div>
				</td>								
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('파괴 시험 항목')}</th>
				<td colspan="3" class="pd10">
					<div class="textarea_autoSize">
						<textarea name="destruction" id="destruction" readonly><c:out value="${bm.destruction }" escapeXml="false" /></textarea>
					</div>
				</td>								
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('비용')}</th>
				<td>${bm.cost}</td>
				<th scope="col">${e3ps:getMessage('최종수정일자')}</th>
				<td>${bm.modifyDate}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('주첨부파일')}</th>
				<td colspan="3">
					<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileView')}" flush="true">
						<jsp:param name="oid" value="${bm.oid}"/>
						<jsp:param name="type" value="PRIMARY"/>
					 </jsp:include>
				</td>								
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('첨부파일')}</th>
				<td colspan="3">
					<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileView')}" flush="true">
						<jsp:param name="oid" value="${bm.oid}"/>
					 </jsp:include>
				</td>								
			</tr>	
		</tbody>
	</table>	
</div>
<!-- /pro_table -->