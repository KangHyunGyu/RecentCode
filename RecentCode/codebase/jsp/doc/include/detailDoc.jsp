<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<style>
	.aCon, .bCon, .cCon, .dCon, .eCon, .fCon, .gCon{
		display : none;
	}
	
</style>
<script>
$(document).ready(function(){
	//팝업 리사이즈
// 	popupResize();
	dataCorrection();
	
});

function dataCorrection(){
	const docNumber = document.getElementById("docNum");
	const selectedValue = docNumber.value;
	
	if(selectedValue === "D001"){
		$(".aCon").show();
		$(".bCon").hide();
		$(".cCon").hide();
		$(".dCon").hide();
		$(".eCon").hide();
		$(".fCon").hide();
		$(".gCon").hide();
	}else if(selectedValue === "D002"){
		$(".aCon").hide();
		$(".bCon").show();
		$(".cCon").hide();
		$(".dCon").hide();
		$(".eCon").hide();
		$(".fCon").hide();
		$(".gCon").hide();
	}else if (selectedValue === "D003"){
		$(".aCon").hide();
		$(".bCon").hide();
		$(".cCon").show();
		$(".dCon").hide();
		$(".eCon").hide();
		$(".fCon").hide();
		$(".gCon").hide();
	}else if (selectedValue === "D004"){
		$(".aCon").hide();
		$(".bCon").hide();
		$(".cCon").hide();
		$(".dCon").show();
		$(".eCon").hide();
		$(".fCon").hide();
		$(".gCon").hide();
	}else if (selectedValue === "D005"){
		$(".aCon").hide();
		$(".bCon").hide();
		$(".cCon").hide();
		$(".dCon").hide();
		$(".eCon").show();
		$(".fCon").hide();
		$(".gCon").hide();
	}else if (selectedValue === "D006"){
		$(".aCon").hide();
		$(".bCon").hide();
		$(".cCon").hide();
		$(".dCon").hide();
		$(".eCon").hide();
		$(".fCon").show();
		$(".gCon").hide();
// 	}else if (selectedValue === "D009"){
// 		$(".aCon").hide();
// 		$(".bCon").hide();
// 		$(".cCon").hide();
// 		$(".dCon").hide();
// 		$(".eCon").hide();
// 		$(".fCon").hide();
// 		$(".gCon").show();
	}else{
		$(".aCon").hide();
		$(".bCon").hide();
		$(".cCon").hide();
		$(".dCon").hide();
		$(".eCon").hide();
		$(".fCon").hide();
		$(".gCon").show();
	}
}

</script>
<div class="seach_arm2 pt10 pb5">
	<input type="hidden" id="docNum" name="docNum" value="${docNum}">
	<input type="hidden" id="docDataCheck" name="docDataCheck" value="${doc}">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">&nbsp;${e3ps:getMessage('기본 정보')}</h4></div>
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
				<th scope="col">${e3ps:getMessage('문서 분류')}</th>
				<td colspan="3">${doc.location}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('문서 번호')}</th>
				<td>${doc.number}</td>
				<th scope="col">${e3ps:getMessage('문서 명')}</th>
				<td>${doc.name}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('버전')}</th>
				<td>${doc.version}</td>				
				<th scope="col">${e3ps:getMessage('상태')}</th>
				<td>${doc.stateName}</td>	
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('작성자')}</th>
				<td>${doc.creatorFullName}</td>	
				<th scope="col">${e3ps:getMessage('작성일')}</th>
				<td>${doc.createDate}</td>
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('최종 수정자')}</th>
				<td>${doc.modifierFullName}</td>				
				<th scope="col">${e3ps:getMessage('최종 수정일')}</th>
				<td>${doc.modifyDate}</td>					
			</tr>	
<!-- 			<tr> -->
<%-- 				<th scope="col">${e3ps:getMessage('설명')}</th> --%>
<!-- 				<td colspan="3" class="pd10"> -->
<!-- 					<div class="textarea_autoSize"> -->
<%-- 						<textarea name="description" id="description" readonly><c:out value="${doc.description }" escapeXml="false" /></textarea> --%>
<!-- 					</div> -->
<!-- 				</td>								 -->
<!-- 			</tr>	 -->
			<tr>
				<th scope="col">${e3ps:getMessage('주첨부파일')}</th>
				<td colspan="3">
					<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileView')}" flush="true">
						<jsp:param name="oid" value="${doc.oid}"/>
						<jsp:param name="type" value="PRIMARY"/>
					 </jsp:include>
				</td>								
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('첨부파일')}</th>
				<td colspan="3">
					<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileView')}" flush="true">
						<jsp:param name="oid" value="${doc.oid}"/>
					 </jsp:include>
				</td>								
			</tr>	
		</tbody>
	</table>	
</div>
<!-- /pro_table -->
<!-- 속성 include 화면 -->
<div>
<%-- 	<jsp:include page="${e3ps:getIncludeURLString('/doc/include_docAttribute')}" flush="true"> --%>
<%-- 	    <jsp:param name="foid" value="${doc.parentFoid}"/> --%>
<%-- 	    <jsp:param name="mode" value="view"/> --%>
<%--     </jsp:include> --%>
    
<%--     <c:if test="${doc.userOnGoingApproval}"> --%>
<%--     	<jsp:include page="${e3ps:getIncludeURLString('/approval/include_onItemDetailApproval')}" flush="true"> --%>
<%-- 		    <jsp:param name="oid" value="${doc.userOnGoingApprovalOid}"/> --%>
<%-- 		    <jsp:param name="type" value="approval"/> --%>
<%--     	</jsp:include> --%>
<%--     </c:if> --%>
</div>
<!-- 계약 개발 검토서 -->
	<div class="D001 aCon">
		<jsp:include page="${e3ps:getIncludeURLString('/doc/include_viewContractDevelopmentReview')}" flush="true">
				<jsp:param name="title" value="${e3ps:getMessage('상세내용')}" />
				<jsp:param name="oid" value="${doc.oid}"/>
		</jsp:include>
	</div>
<!-- 서비스 요청서 -->
	<div class="D002 bCon">
		<jsp:include page="${e3ps:getIncludeURLString('/doc/include_viewServiceRequest')}"  flush="true">
			<jsp:param name="title" value="${e3ps:getMessage('상세내용')}" />
			<jsp:param name="oid" value="${doc.oid}"/>
		</jsp:include>
	</div>
<!-- 측정 의뢰서 -->
	<div class="D003 cCon">
		<jsp:include page="${e3ps:getIncludeURLString('/doc/include_viewMeasurementRequest')}"  flush="true">
			<jsp:param name="title" value="${e3ps:getMessage('상세내용')}" />
			<jsp:param name="oid" value="${doc.oid}"/>
		</jsp:include>
	</div>
<!-- 분석 의뢰서 -->
	<div class="D004 dCon">
		<jsp:include page="${e3ps:getIncludeURLString('/doc/include_viewAnalysisRequest')}"  flush="true">
			<jsp:param name="title" value="${e3ps:getMessage('상세내용')}" />
			<jsp:param name="oid" value="${doc.oid}"/>
		</jsp:include>
	</div>
<!-- 공정 변경 요청서 -->
	<div class="D005 eCon">
		<jsp:include page="${e3ps:getIncludeURLString('/doc/include_viewProcessChangeRequest')}"  flush="true">
			<jsp:param name="title" value="${e3ps:getMessage('상세내용')}" />
			<jsp:param name="oid" value="${doc.oid}"/>
		</jsp:include>
	</div>
<!-- 공정 개발 검토서 -->
	<div class="D006 fCon">
		<jsp:include page="${e3ps:getIncludeURLString('/doc/include_viewProcessDevelopmentReview')}"  flush="true">
			<jsp:param name="title" value="${e3ps:getMessage('상세내용')}" />
			<jsp:param name="oid" value="${doc.oid}"/>
		</jsp:include>
	</div>
<!-- 기타 -->
	<div class="gCon">
		<jsp:include page="${e3ps:getIncludeURLString('/doc/include_viewDocEtc')}"  flush="true">
			<jsp:param name="title" value="${e3ps:getMessage('상세내용')}" />
			<jsp:param name="oid" value="${doc.oid}"/>
		</jsp:include>
	</div>

