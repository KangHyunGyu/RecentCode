<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<style>
	.allCon, .1Con, .2Con, .3Con, .4Con, .5Con, .6Con, .9Con{
		display : none;
	}
	
</style>
<script>
$(document).ready(function(){
	//팝업 리사이즈
 	popupResize();
	additionalDoc();
// 	let docAtt = document.getElementById("docAttributeCode");
// 	console.log("docAttributeCode ::: " + docAtt.value);
});
function modifyDoc(btn){
	console.log("modifyCheck");
	if(!checkValidate()) {
		return;
	}
	$("#docForm").attr("action",getURLString("/doc/modifyDocAction"));
	
	var param = new Object();
	//공정 개발 검토서 contentsList
	const pdr_contents_list = AUIGrid.getGridData(pdr_content_gridID);
	
	//기타 contentsList
	const etc_contents_list = AUIGrid.getGridData(etc_content_gridID);
	
	//관련 부품
// 	var relatedPartList = $$("add_relatedPart_grid_wrap").data.serialize();
	var relatedPartList = AUIGrid.getGridData(add_relatedPart_myGridID);
	
	param["relatedPartList"] = relatedPartList;
	param.pdrContentsList = pdr_contents_list;
	
	
	param.etcContentsList = etc_contents_list;
	
	formSubmit("docForm", param, "${e3ps:getMessage('수정하시겠습니까?')}",function(){
		if(opener.window.search) {
			opener.window.search();
		}
	}, true);
}
function checkValidate() {
	
// 	if($("#locationDisplay").val().length == 0){
// 		openNotice("${e3ps:getMessage('문서 분류를 로딩중입니다.')}");
// 		return false;
// 	}
	
	if($("#locationDisplay").val() == "/Document"){
		openNotice("${e3ps:getMessage('문서 분류를 선택하세요.')}");
		return false;
	}
// 	if($("#locationDisplay").val() != ${doc.location}){
// 		console.log("location Test ");
// 		openNotice("${e3ps:getMessage('문서 분류와 문서 유형을 동일하게 선택하세요.')}");
// 		return false;
// 	}
	if($("#docName").val() == null || $("#docName").val() == "") {
		$("#docName").focus();
		openNotice("${e3ps:getMessage('문서 명을 입력하세요.')}");
		return false;
	}
	
// 	var primaryCheck = $("#PRIMARY_uploadQueueBox").find(".AXUploadItem");
// 	if(primaryCheck.val() == undefined){
// 		openNotice("${e3ps:getMessage('주 첨부파일을 추가하세요.')}");
// 		return false;
// 	}
	
	var partValid = AUIGrid.validateGridData(add_relatedPart_myGridID, ["number"], "${e3ps:getMessage('필수 필드는 반드시 값을 입력해야 합니다.')}");
// 	$$(add_relatedPart_myGridID).data.each(function(obj){
// 		if(!obj.number || obj.number.length === 0){
// 			partValid = false;
// 		}
// 	});
	
	if(!partValid) {
		$(add_relatedPart_myGridID)[0].scrollIntoView();
		openNotice("${e3ps:getMessage('추가된 관련 부품에 값을 입력하세요.')}");
		return false;
	}
	return true;
}

function additionalDoc(){
	const docAtt = document.getElementById("docAttributeCode");
	const docAttCode = docAtt.value;
	
	if(docAttCode === "D001"){
		$(".1Con").show();	//계약 개발 검토서
		$(".2Con").hide();	//서비스 요청서
		$(".3Con").hide();	//측정 의뢰서
		$(".4Con").hide();	//분석 의뢰서
		$(".5Con").hide();	//공정 변경 요청서
		$(".6Con").hide();	//공정 개발 검토서
		$(".9Con").hide();	//기타
	}else if(docAttCode === "D002"){
		
		$(".1Con").hide();
		$(".2Con").show();
		$(".3Con").hide();
		$(".4Con").hide();
		$(".5Con").hide();
		$(".6Con").hide();
		$(".9Con").hide();
	}else if (docAttCode === "D003"){
		
		$(".1Con").hide();
		$(".2Con").hide();
		$(".3Con").show();
		$(".4Con").hide();
		$(".5Con").hide();
		$(".6Con").hide();
		$(".9Con").hide();
	}else if (docAttCode === "D004"){
		
		$(".1Con").hide();
		$(".2Con").hide();
		$(".3Con").hide();
		$(".4Con").show();
		$(".5Con").hide();
		$(".6Con").hide();
		$(".9Con").hide();
	}else if (docAttCode === "D005"){
		
		$(".1Con").hide();
		$(".2Con").hide();
		$(".3Con").hide();
		$(".4Con").hide();
		$(".5Con").show();
		$(".6Con").hide();
		$(".9Con").hide();
	}else if (docAttCode === "D006"){
		
		$(".1Con").hide();
		$(".2Con").hide();
		$(".3Con").hide();
		$(".4Con").hide();
		$(".5Con").hide();
		$(".6Con").show();
		$(".9Con").hide();
// 	}else if (docAttCode === "D009"){
		
// 		$(".1Con").hide();
// 		$(".2Con").hide();
// 		$(".3Con").hide();
// 		$(".4Con").hide();
// 		$(".5Con").hide();
// 		$(".6Con").hide();
// 		$(".9Con").show();
	}else{
		
		$(".1Con").hide();
		$(".2Con").hide();
		$(".3Con").hide();
		$(".4Con").hide();
		$(".5Con").hide();
		$(".6Con").hide();
		$(".9Con").show();
	}
}

</script>
<!-- pop -->
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('문서 수정')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->

	<div class="pl25 pr25">
		<div class="seach_arm2 pt10 pb10">
			<div class="leftbt"></div>
			<div class="rightbt">
				<button type="button" class="s_bt03" onclick="modifyDoc(this)">${e3ps:getMessage('수정완료')}</button>
				<button type="button" class="s_bt03" onclick="javascript:history.back()">${e3ps:getMessage('뒤로가기')}</button>
				<button type="button" class="s_bt04" onclick="javascript:window.close()">${e3ps:getMessage('닫기')}</button>
			</div>
		</div>
		<form name="docForm" id="docForm" method="post">
			<input type="hidden" id="location" name="location" value="">
			<input type="hidden" name="oid"  id="oid" value="${doc.oid}" />
			<input type="hidden" name="docNumber"  id="docNumber" value="${doc.number}" />
			<input type="hidden" name="docAttributeCode"  id="docAttributeCode" value="${docAttribute.docAttributeCode}" />
			<input type="hidden" name="mode"  id="mode" value="modify" />
			<div class="pro_table">
				<table class="mainTable">
					<colgroup>
						<col style="width:20%">
						<col style="width:80%">
					</colgroup>	
					<tbody>
						<tr style="display: none">
							<th>${e3ps:getMessage('문서 분류')}<span class="required">*</span></th>
							<td>
								<div style="position:relative">
									<input class="w50" type="text" id="locationDisplay" name="locationDisplay" value="${doc.location}" disabled>
 									<button type="button" class="s_bt03" id="toggleBtn" onclick="toggleFolderList(this)">${e3ps:getMessage('분류 지정')}</button>
									<div id="folderList" style="display:none;position:absolute;z-index:999">
										<!-- tree -->
										<jsp:include page="${e3ps:getIncludeURLString('/common/include_folderTree')}" flush="true">
											<jsp:param name="container" value="worldex"/>
											<jsp:param name="renderTo" value="docFolder"/>
											<jsp:param name="formId" value="docForm"/>
											<jsp:param name="rootLocation" value="/Default/Document"/>
											<jsp:param name="location" value="${doc.location}"/>
										</jsp:include>
										<!-- //tree -->
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('문서 유형')}</th>
							<td>
								${docAttribute.docAttributeName}
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('문서 번호')}</th>
							<td>
								${doc.number}
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('문서 명')}<span class="required">*</span></th>
							<td>
								<input class="w50" type="text" id="docName" name="docName" value="${doc.name}">
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('주첨부파일')}<span class="required">*</span></th>
							<td>
								<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
				                     <jsp:param name="formId" value="docForm"/>
				                     <jsp:param name="command" value="insert"/>
				                     <jsp:param name="type" value="PRIMARY"/>
				                     <jsp:param name="btnId" value="createBtn" />
				                     <jsp:param name="oid" value="${doc.oid}"/>
				                 </jsp:include>
							</td>
						</tr>
						<tr> 
							<th>${e3ps:getMessage('첨부파일')}</th>
							<td>
								<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
				                     <jsp:param name="formId" value="docForm"/>
				                     <jsp:param name="command" value="insert"/>
				                     <jsp:param name="btnId" value="createBtn" />
				                     <jsp:param name="oid" value="${doc.oid}"/>
				                 </jsp:include>
							</td>
						</tr>
						
<!-- 						<tr> -->
<%-- 							<th>${e3ps:getMessage('수정 사유')}</th> --%>
<!-- 							<td> -->
<!-- 							<div> -->
<!-- 								<input type="text" class="w100" name="iterationNote" id="iterationNote" value=""/> -->
<!-- 							</div> -->
<!-- 							</td> -->
<!-- 						</tr> -->
					</tbody>
				</table>
			</div>
			<!-- 속성 include 화면 -->
<!-- 			<div id="attributeList"> -->
<%-- 				<jsp:include page="${e3ps:getIncludeURLString('/doc/include_docAttribute')}" flush="true"> --%>
<%-- 				    <jsp:param name="foid" value="${doc.parentFoid}"/> --%>
<%-- 				    <jsp:param name="mode" value="modify"/> --%>
<%-- 			    </jsp:include> --%>
<!-- 			</div> -->

			<!-- 계약 개발 검토서 -->
			<div class="D001 1Con">
				<jsp:include page="${e3ps:getIncludeURLString('/doc/include_modifyContractDevelopmentReview')}"  flush="true">
					<jsp:param name="docNumber" value="D001"/>
					<jsp:param name="mode" value="modify"/>
					<jsp:param name="title" value="${e3ps:getMessage('상세내용')}" />
				</jsp:include>
			</div>
			<!-- 서비스 요청서 -->
			<div class="D002 2Con">
				<jsp:include page="${e3ps:getIncludeURLString('/doc/include_modifyServiceRequest')}"  flush="true">
					<jsp:param name="docNumber" value="D002"/>
					<jsp:param name="mode" value="modify"/>
					<jsp:param name="title" value="${e3ps:getMessage('상세내용')}" />
				</jsp:include>
			</div>
			<!-- 측정 의뢰서 -->
			<div class="D003 3Con">
				<jsp:include page="${e3ps:getIncludeURLString('/doc/include_modifyMeasurementRequest')}"  flush="true">
					<jsp:param name="docNumber" value="D003"/>
					<jsp:param name="mode" value="modify"/>
					<jsp:param name="title" value="${e3ps:getMessage('상세내용')}" />
				</jsp:include>
			</div>
			<!-- 분석 의뢰서 -->
			<div class="D004 4Con">
				<jsp:include page="${e3ps:getIncludeURLString('/doc/include_modifyAnalysisRequest')}"  flush="true">
					<jsp:param name="docNumber" value="D004"/>
					<jsp:param name="mode" value="modify"/>
					<jsp:param name="title" value="${e3ps:getMessage('상세내용')}" />
				</jsp:include>
			</div>
			<!-- 공정 변경 요청서 -->
			<div class="D005 5Con">
				<jsp:include page="${e3ps:getIncludeURLString('/doc/include_modifyProcessChangeRequest')}"  flush="true">
					<jsp:param name="docNumber" value="D005"/>
					<jsp:param name="mode" value="modify"/>
					<jsp:param name="title" value="${e3ps:getMessage('상세내용')}" />
				</jsp:include>
			</div>
			<!-- 공정 개발 검토서 -->
			<div class="D006 6Con">
				<jsp:include page="${e3ps:getIncludeURLString('/doc/include_modifyProcessDevelopmentReview')}"  flush="true">
					<jsp:param name="docNumber" value="D006"/>
					<jsp:param name="mode" value="modify"/>
					<jsp:param name="title" value="${e3ps:getMessage('상세내용')}" />
				</jsp:include>
			</div>
			<!-- 기타 -->
<!-- 			<div class="D009 9Con"> -->
<%-- 				<jsp:include page="${e3ps:getIncludeURLString('/doc/include_modifyDocEtc')}"  flush="true"> --%>
<%-- 					<jsp:param name="docNumber" value="D009"/> --%>
<%-- 					<jsp:param name="mode" value="modify"/> --%>
<%-- 					<jsp:param name="title" value="${e3ps:getMessage('상세내용')}" /> --%>
<%-- 				</jsp:include> --%>
<!-- 			</div> -->
			<div class="gCon ml30 mr30">
				<jsp:include page="${e3ps:getIncludeURLString('/doc/include_modifyDocEtc')}"  flush="true">
					<jsp:param name="mode" value="modify"/>
					<jsp:param name="title" value="${e3ps:getMessage('상세내용')}" />
				</jsp:include>
			</div>
	</form>
	</div>
	<!-- 관련 부품 지정 include 화면 -->
	<div class="pl25 pr25 pb10">
		<jsp:include page="${e3ps:getIncludeURLString('/common/include_addObject')}" flush="true">
			<jsp:param name="oid" value="${doc.oid}"/>
			<jsp:param name="objType" value="part"/>
			<jsp:param name="pageName" value="relatedPart"/>
			<jsp:param name="title" value="${e3ps:getMessage('관련 부품')}"/>
			<jsp:param name="gridHeight" value="200"/>
		</jsp:include>
	</div>
	</div>		
<!-- //pop-->
