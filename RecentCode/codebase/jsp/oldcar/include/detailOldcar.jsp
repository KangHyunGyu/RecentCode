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
				<td>${oldcar.oldcarNumber}</td>
				<th scope="col">${e3ps:getMessage('제목')}</th>
				<td>${oldcar.oldcarName}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('버전')}</th>
				<td>${oldcar.version}</td>				
				<th scope="col">${e3ps:getMessage('승인상태')}</th>
				<td>${oldcar.stateName}</td>	
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('작성자')}</th>
				<td>${oldcar.creatorFullName}</td>	
				<th scope="col">${e3ps:getMessage('최종 수정일')}</th>
				<td>${oldcar.modifyDate}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('발생일')}</th>
				<td>${oldcar.occurrenceDate}</td>
				<th scope="col">${e3ps:getMessage('발생시점')}</th>
				<td>${oldcar.occurrenceStepName}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('차종')}</th>
				<td>${oldcar.occurrenceModelName}</td>
				<th scope="col">${e3ps:getMessage('발생장소')}</th>
				<td>${oldcar.occurrencePlaceName}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('시행자')}</th>
				<td>${oldcar.workerName}</td>
				<th scope="col">${e3ps:getMessage('처리상태')}</th>
				<td>${oldcar.oldcarStateName}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('관리검증기준')}</th>
				<td colspan="3">${oldcar.criteria}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('문제공정')}</th>
				<td colspan="3" class="pd10">
					<div class="textarea_autoSize">
						<textarea name="problemProcess" id="problemProcess" readonly><c:out value="${oldcar.problemProcess }" escapeXml="false" /></textarea>
					</div>
				</td>								
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('문제내용')}</th>
				<td colspan="3" class="pd10">
					<div class="textarea_autoSize">
						<textarea name="problemContent" id="problemContent" readonly><c:out value="${oldcar.problemContent }" escapeXml="false" /></textarea>
					</div>
				</td>								
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('문제점 및 원인')}</th>
				<td colspan="3" class="pd10">
					<div class="textarea_autoSize">
						<textarea name="cause" id="cause" readonly><c:out value="${oldcar.cause }" escapeXml="false" /></textarea>
					</div>
				</td>								
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('개선대책 및 처리내용')}</th>
				<td colspan="3" class="pd10">
					<div class="textarea_autoSize">
						<textarea name="improve" id="improve" readonly><c:out value="${oldcar.improve }" escapeXml="false" /></textarea>
					</div>
				</td>								
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('주첨부파일')}</th>
				<td colspan="3">
					<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileView')}" flush="true">
						<jsp:param name="oid" value="${oldcar.oid}"/>
						<jsp:param name="type" value="PRIMARY"/>
					 </jsp:include>
				</td>								
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('첨부파일')}</th>
				<td colspan="3">
					<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileView')}" flush="true">
						<jsp:param name="oid" value="${oldcar.oid}"/>
					 </jsp:include>
				</td>								
			</tr>	
		</tbody>
	</table>	
</div>
<!-- /pro_table -->