<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	//팝업 리사이즈
// 	popupResize();
});
function chagePartNo(){
	openConfirm("${e3ps:getMessage('부품 번호를 변경하시겠습니까?')}", function(){
		
		var param = new Object();
		
		param.oid = "${epm.oid}";
		
		var url = getURLString("/epm/chagePartNo");
		ajaxCallServer(url, param, function(data){
			location.reload();
		}, true);
	});
}

function rePublish(){
	
	openConfirm("${e3ps:getMessage('재변환 하시겠습니까?')}", function(){
		
		var param = new Object();
		
		param.oid = "${epm.oid}";
		
		var url = getURLString("/epm/rePublishAction");
		ajaxCallServer(url, param, function(data){
			if(opener.window.search){
				opener.window.search();				
			}
		}, true);
	});
	
}

</script>
<div class="seach_arm2 pt10 pb10">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('기본 정보')}</h4></div>
	<div class="rightbt">
		<%-- 
		<c:if test="${epm.ChagePartNoBtn()}">
			<button type="button" class="s_bt03" onclick="javascript:chagePartNo();">${e3ps:getMessage('부품 번호 변경')}</button>
		</c:if>
		 --%>
	</div>
</div>
<!-- pro_table -->
<div class="pro_table">
	<table class="mainTable">
		<colgroup>
			<col style="width:11%">
			<col style="width:22%">
			<col style="width:11%">	
			<col style="width:22%">
			<col style="width:33%">			
		</colgroup>
		<tbody>
			<tr>
				<th scope="col">${e3ps:getMessage('도면 분류')}</th>
				<td colspan="3">${epm.location}</td>
				<td rowspan="6" style="border-left:1px solid #dedede;" align="center">
					<a href="${epm.publishURL}" >
						<img src="${epm.thumbnail}"  border="0" style="display:block; height:100%; width:auto;">
					</a>
					<a href="javascript:rePublish()"><button type="button" class="s_bt03">${e3ps:getMessage('재변환')}</button></a>
					<div style="max-width: 1px; max-height: 1px;">
						<form action="" id="pvsForm" style="visibility: hidden;">
						<iframe name="executeFrame"></iframe>
						</form>
					</div>
				</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('도면 번호')}</th>
				<td>${epm.number}</td>
				<th scope="col">${e3ps:getMessage('도면 명')}</th>
				<td>${epm.name}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('상태')}</th>
				<td>${epm.stateName}</td>	
				<th scope="col">${e3ps:getMessage('버전')}</th>
				<td>${epm.version}</td>				
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('등록자')}</th>
				<td>${epm.creatorFullName}</td>	
				<th scope="col">${e3ps:getMessage('등록일')}</th>
				<td>${epm.createDate}</td>
								
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('최종 수정자')}</th>
				<td>${epm.modifierFullName}</td>
				<th scope="col">${e3ps:getMessage('최종 수정일')}</th>
				<td>${epm.modifyDate}</td>					
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('주 부품')}</th>
				<td>
					<a href="javascript:openView('${epm.partOid()}')">
						${epm.partNum()}
					</a>
				</td>
				<th scope="col">${e3ps:getMessage('CAD 파일')}</th>
				<td>
					<a href="${epm.CADFile()}">
						${epm.CADName()}
					</a>
				</td>					
			</tr>
			<!-- 
			<tr>
				<th scope="col">${e3ps:getMessage('변환 파일')}</th>
				<td colspan="4">
					${epm.drawingPublishFile()}
				</td>								
			</tr>
			 -->
			<tr>
				<th scope="col">${e3ps:getMessage('첨부파일')}</th>
				<td colspan="4">
					<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileView')}" flush="true">
						<jsp:param name="oid" value="${epm.oid}"/>
						<jsp:param name="type" value="SECONDARY"/>
					 </jsp:include>
				</td>									
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('변환파일')}</th>
				<td colspan="4">
					<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileView')}" flush="true">
						<jsp:param name="oid" value="${epm.oid}"/>
						<jsp:param name="type" value="ADDITIONAL_PV"/>
						<jsp:param name="fileType" value="PDF DWG"/>
					 </jsp:include>
				</td>									
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('설명')}</th>
				<td colspan="4" class="pd15">
					<div class="textarea_autoSize">
						<textarea name="description" id="description" readonly><c:out value="${epm.description}" escapeXml="false" /></textarea>
					</div>
				</td>								
			</tr>	
		</tbody>
	</table>
</div>
<!-- //pro_table -->

<jsp:include page="${e3ps:getIncludeURLString('/epm/include_epmAttributes')}" flush="true">
	<jsp:param name="oid" value="${epm.oid}"/>
	<jsp:param name="module" value="view"/>
</jsp:include>

<%-- 
<c:if test="${epm.userOnGoingApproval}">
   	<jsp:include page="${e3ps:getIncludeURLString('/approval/include_onItemDetailApproval')}" flush="true">
	    <jsp:param name="oid" value="${epm.userOnGoingApprovalOid}"/>
	    <jsp:param name="type" value="approval"/>
   	</jsp:include>
</c:if>
 --%>