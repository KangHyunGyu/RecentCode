<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	// 결재 업무 처리 여부 체크
	var isWorking = "${isWorking}";
	
	if(isWorking == "false"){
		alert("${e3ps:getMessage('해당 업무는 진행중 상태가 아닙니다.')}")
		var url ="";
		if("tempStorage" == "${type}") {
			var url = getURLString("/workspace/listWorkItem")+ "?type=tempStorage" //임시저장함
		} else {
			var url = getURLString("/workspace/listWorkItem")+ "?type=approval"	//결재함
		}
		location.href = url;
	}
	
	
	var temp1 = document.getElementById("test111");
	switchPopupDiv(temp1);
	
});
<%----------------------------------------------------------
*                      승인 
----------------------------------------------------------%>
function approveAction(btn){
	if("REJECT" != $(btn).val() && "approval" != "${type}"){
		if(!checkEchange()) {
			return;
		}
	}
	// 배포 승인인 경우 업체 선정 (배포 || 최종 승인자 || 발주타입이 초도배포(I) 인 경우만 적용)
	var checkSupplier = $("#checkSupplier").val();
	var checkSupplierValue = $("#checkSupplierValue").val();
	
	var btnType = $(btn).val();
	
	if(checkSupplier == "true"){
		if($("#supplierId").val() == null || $("#supplierId").val() == "") {
			$("#supplierId").focus();
			openNotice("${e3ps:getMessage('업체를 입력하여 주십시오.')}");
			return;
		}
		if(checkSupplierValue == "false"){
			openNotice("${e3ps:getMessage('업체를 저장하여 주십시오.')}");
			return;
		}
	}
	
	$("#approveForm").attr("action",getURLString("/approval/approveAction"));
	
	var param = new Object();

	//결재선 지정 리스트
	if("tempStorage" == "${type}") {
		
		var appLineValid = true;
		 $$("app_line_grid_wrap").data.each(function(obj){
			if(!obj.roleType || obj.roleType.length === 0){
				appLineValid = false;
			} else if(!obj.name || obj.name.length === 0) {
				appLineValid = false;
			}
		});
		 
		if(!appLineValid) {
			//$("#app_line_grid_wrap")[0].scrollIntoView();

			openNotice("${e3ps:getMessage('추가된 결재선에 값을 입력하세요.')}");
			return false;
		}
		
		if(btnType == "COMPLETE") {
			if(!checkApproveLine()){
				return;
			}
		}
		
		var approvalList = $$("app_line_grid_wrap").serialize(true);
		
		param["approvalList"] = approvalList;
	}
	
	param["lineState"] = $(btn).val();
	param["type"] ="${type}";
	
	formSubmit("approveForm", param, $(btn).html() + "${e3ps:getMessage('하시겠습니까?')}", function(data){
		
		if(data.result){
			self.close();
		}
	});
}
<%----------------------------------------------------------
*                      DROP 
----------------------------------------------------------%>
function dropAction(btn){
	$("#approveForm").attr("action",getURLString("/approval/dropAction"));
	
	var param = new Object();
	
	formSubmit("approveForm", param, "${e3ps:getMessage('Drop하시겠습니까?')}", function(data){
		
		if(data.result){
			self.close();
		}
	});
}
<%----------------------------------------------------------
*                     위임
----------------------------------------------------------%>
function delegateAction(){
	var delegateUser = $("#delegateUser").val();
	
	if(delegateUser == null || delegateUser == "") {
		$("#delegateUser").focus();
		openNotice("${e3ps:getMessage('위임자를 선택해주세요.')}");
		return;
	}
	
	$("#approveForm").attr("action",getURLString("/approval/delegateAction"));
	var param = new Object();
	formSubmit("approveForm", param, "${e3ps:getMessage('위임 하시겠습니까?')}", function(data){
		
		if(data.result){
			self.close();
		}
	});
}

<%----------------------------------------------------------
*                     EChange 결재 객체  산출물 체크
----------------------------------------------------------%>
function checkEchange(){
	if($$("rel_part_grid_wrap") != undefined){
		var relatedPartList = $$("rel_part_grid_wrap").data.serialize();
		if(relatedPartList.length == 0) {
			openNotice("${e3ps:getMessage('변경결과[부품]을 입력하세요.')}");
			return false;
		}else{
			for(var i=0;i<relatedPartList.length;i++){
				var resultPart = relatedPartList[i];
				if(resultPart.state != "APPROVED"){
					openNotice("${e3ps:getMessage('"+resultPart.name+" : 승인된 부품이 아닙니다.')}");
					return false;
				}
			}
		}
	}
	if($$("result_epm_grid_wrap") != undefined){
		var relatedEpmList = $$("result_epm_grid_wrap").data.serialize();
		if(relatedEpmList.length == 0) {
			openNotice("${e3ps:getMessage('관련도면을 입력하세요.')}");
			return false;
		}
// 		else{
// 			for(var i=0;i<relatedEpmList.length;i++){
// 				var resultEpm = relatedEpmList[i];
// 				if(resultEpm.state != "APPROVED"){
// 					openNotice("${e3ps:getMessage('"+resultEpm.name+" : 승인된 도면이 아닙니다.')}");
// 					return false;
// 				}
// 			}
// 		}
	}
	if($$("ecaDoc_grid_wrap") != undefined){
		var relatedBeforeDocList = $$("ecaDoc_grid_wrap").data.serialize();
		if(relatedBeforeDocList.length == 0) {
			openNotice("${e3ps:getMessage('변경 전[문서]를 입력하세요.')}");
			return false;
		}
	}
	if($$("ecaDoc_grid_wrap2") != undefined){
		var relatedAfterDocList = $$("ecaDoc_grid_wrap2").data.serialize();
		if(relatedAfterDocList.length == 0) {
			openNotice("${e3ps:getMessage('변경 후[문서]를 입력하세요.')}");
			return false;
		}else{
			for(var i=0;i<relatedAfterDocList.length;i++){
				var afterDoc = relatedAfterDocList[i];
				
				if(afterDoc.n_state != "승인됨"){
					openNotice("${e3ps:getMessage('"+afterDoc.n_name+" : 승인된 문서가 아닙니다.')}");
					return false;
				}
			}
		}
	}
	return true;
}
</script>
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" id="test111" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">&nbsp;${e3ps:getMessage('결재 진행하기')}</h4></div>
	<div class="rightbt"></div>
</div>
<!-- pro_table -->
<div class="pro_table">
<form name="approveForm" id="approveForm" method="post" enctype="multipart/form-data">
<input type="hidden" name="oid" id="oid" value="${oid}">
	<!-- 결재 정보 -->
	<jsp:include page="${e3ps:getIncludeURLString('/approval/include_approvalInfo')}" flush="true">
		<jsp:param name="oid" value="${oid}"/>
	</jsp:include>
	
	<!-- 설변 객체 상세화면 -->
	<c:if test="${className eq 'EChangeRequest2'}">
	<jsp:include page="${e3ps:getIncludeURLString('/change/include_activityECR')}" flush="true">
		<jsp:param name="oid" value="${cOid}"/>
	</jsp:include>
	</c:if>
	<c:if test="${className eq 'EChangeOrder2'}">
	<jsp:include page="${e3ps:getIncludeURLString('/change/include_activityECO')}" flush="true">
		<jsp:param name="oid" value="${cOid}"/>
	</jsp:include>
	</c:if>
	<c:if test="${className eq 'EChangeActivity'}">
	<jsp:include page="${e3ps:getIncludeURLString('/change/include_activityECA')}" flush="true">
		<jsp:param name="oid" value="${cOid}"/>
	</jsp:include>
	</c:if>
	
	<!-- 결재 위임 -->
	<c:if test= "${type ne 'tempStorage'}">
		<jsp:include page="${e3ps:getIncludeURLString('/approval/include_delegateApprove')}" flush="true">
			<jsp:param name="oid" value="${oid}"/>
		</jsp:include>
	</c:if>
	
	<%-- <c:choose>
	<c:when test= "${type == 'tempStorage'}">
		<!-- 결재선 지정 include 화면 -->
		<div class="ml30 mr30">
		<jsp:include page="${e3ps:getIncludeURLString('/approval/include_addApprovalLine')}" flush="true">
			<jsp:param name="oid" value="${oid}"/>
		</jsp:include>
	</div>
	</c:when>
	<c:otherwise>
		<!-- 결재선 리스트 뷰 -->
	<div class="ml30 mr30">
		<jsp:include page="${e3ps:getIncludeURLString('/approval/include_approvalLineView')}" flush="true">
			<jsp:param name="oid" value="${oid}"/>
			<jsp:param name="searchRoleType" value="EXCEPT_RECEIVE"/>
		</jsp:include>
		<jsp:include page="${e3ps:getIncludeURLString('/approval/include_approvalRecLineView')}" flush="true">
			<jsp:param name="oid" value="${oid}"/>
			<jsp:param name="searchRoleType" value="RECEIVE"/>
		</jsp:include>
	</div>
	</c:otherwise>
	</c:choose> --%>
	
	</form>
</div>