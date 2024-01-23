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
	
	setTimeout(function(){ 
		//approveAction 함수에서 form submit 시 param 이 겹치는 부분으로 인해 상세내용 name 속성 모두 제거 (설변 활동 제외)
		let approvalObjectDetailEleList = getAllChildElements(document.getElementById('approvalObjectDetail'));
		approvalObjectDetailEleList.forEach((ele)=>{
			if('${className}' != 'EChangeActivity'){
				ele.removeAttribute("name");
			}
		})
	}, 100);
	
	
});
<%----------------------------------------------------------
*                      승인 
----------------------------------------------------------%>
function approveAction(btn){
	
	if("${type}" == ""){
		if(!checkEchange()) {
			return;
		}
	}
	
	// 배포 승인인 경우 업체 선정 (배포 || 최종 승인자 || 발주타입이 초도배포(I) 인 경우만 적용)
	var checkSupplier = $("#checkSupplier").val();
	var checkSupplierValue = $("#checkSupplierValue").val();
	
	var btnType = $(btn).val();
	
	if(btnType == 'REJECT' && document.getElementById('description').value.length == 0){
		openNotice("${e3ps:getMessage('반려 의견을 작성해주세요')}.");
		return;
	}
	
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
		
		var approvalList = AUIGrid.getGridData(app_line_myGridID);
		
		var appLineValid = true;
		approvalList.forEach((obj) => {
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
		
		param["approvalList"] = approvalList;
	}
	
	param["lineState"] = $(btn).val();
	param["type"] ="${type}";
	
	formSubmit("approveForm", param, $(btn).html() + "${e3ps:getMessage('하시겠습니까?')}", function(){}, true);
}
<%----------------------------------------------------------
*                      DROP 
----------------------------------------------------------%>
function dropAction(btn){
	$("#approveForm").attr("action",getURLString("/approval/dropAction"));
	
	var param = new Object();
	
	formSubmit("approveForm", param, "${e3ps:getMessage('Drop하시겠습니까?')}");
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
	formSubmit("approveForm", param, "${e3ps:getMessage('위임 하시겠습니까?')}");
}

<%----------------------------------------------------------
*                     EChange 결재 체크
----------------------------------------------------------%>
function checkEchange(){
	
	if(rel_part_myGridID != undefined) {
		var rel_part = AUIGrid.getGridData(rel_part_myGridID);
		var returnNotice = false;
		rel_part.forEach(item => {
			console.log(item);
			if(item.nextRev == ""){
				returnNotice = true;
				return;
			}
		});
		if(returnNotice){
			openNotice("${e3ps:getMessage('개정을 진행하여 주십시오.')}");
			return false;
		}
	}
	
	return true;
}
</script>
<div class="product">
<form name="approveForm" id="approveForm" method="post" enctype="multipart/form-data">
<input type="hidden" name="oid" id="oid" value="${oid}">
<input type="hidden" name="cOid" id="cOid" value="${cOid}">
	<!-- 결재 위임 -->
	<c:if test= "${type ne 'tempStorage'}">
		<jsp:include page="${e3ps:getIncludeURLString('/approval/include_delegateApprove')}" flush="true">
			<jsp:param name="oid" value="${oid}"/>
		</jsp:include>
	</c:if>
	
	<!-- 결재 정보 -->
	<jsp:include page="${e3ps:getIncludeURLString('/approval/include_approvalInfo')}" flush="true">
		<jsp:param name="oid" value="${oid}"/>
	</jsp:include>
	
	<div id="approvalObjectDetail">
	<c:choose>
		<c:when test= "${className == 'E3PSDocument'}">
			<div style="margin:0 30px 0 30px;">
				<jsp:include page="${e3ps:getIncludeURLString('/doc/include_detailDoc')}" flush="true">
					<jsp:param name="oid" value="${cOid}"/>
				</jsp:include>
			</div>
		</c:when>
		<c:when test= "${className == 'DistributeDocument'}">
			<div style="margin:0 30px 0 30px;">
				<jsp:include page="${e3ps:getIncludeURLString('/distribute/include_detailDistribute')}" flush="true">
					<jsp:param name="oid" value="${cOid}"/>
				</jsp:include>
			</div>
		</c:when>
		<c:when test= "${className == 'DistributeRegistration'}">
			<div style="margin:0 30px 0 30px;">
				<jsp:include page="${e3ps:getIncludeURLString('/distribute/include_detailDistributeRegistration')}" flush="true">
					<jsp:param name="oid" value="${cOid}"/>
				</jsp:include>
			</div>
		</c:when>
		<c:when test= "${className == 'MultiApproval'}">
			<div style="margin:0 30px 0 30px;">
				<jsp:include page="${e3ps:getIncludeURLString('/multi/include_detailMulti')}" flush="true">
					<jsp:param name="oid" value="${cOid}"/>
				</jsp:include>
				<jsp:include page="${e3ps:getIncludeURLString('/multi/include_relatedObject')}" flush="true">
					<jsp:param name="oid" value="${cOid}"/>
				</jsp:include>
			</div>
		</c:when>
		<c:when test= "${className == 'EChangeRequest2'}">
			<div style="margin:0 30px 0 30px;">
				<jsp:include page="${e3ps:getIncludeURLString('/change/include_detailECR')}" flush="true">
					<jsp:param name="oid" value="${cOid}"/>
				</jsp:include>
			</div>
		</c:when>
		<c:when test= "${className == 'EChangeOrder2'}">
			<div style="margin:0 30px 0 30px;">
				<jsp:include page="${e3ps:getIncludeURLString('/change/include_detailECO')}" flush="true">
					<jsp:param name="oid" value="${cOid}"/>
				</jsp:include>
			</div>
		</c:when>
		<c:when test= "${className == 'EChangeActivity'}">
			<jsp:include page="${e3ps:getIncludeURLString('/change/include_ecaActiveCreate')}" flush="true">
				<jsp:param name="oid" value="${cOid}"/>
			</jsp:include>
		</c:when>	
	</c:choose>
	</div>
	<!-- 설변 객체 상세화면 -->
<%-- 	<c:if test="${className eq 'EChangeRequest2'}"> --%>
<%-- 	<jsp:include page="${e3ps:getIncludeURLString('/change/include_activityECR')}" flush="true"> --%>
<%-- 		<jsp:param name="oid" value="${cOid}"/> --%>
<%-- 	</jsp:include> --%>
<%-- 	</c:if> --%>
<%-- 	<c:if test="${className eq 'EChangeOrder2'}"> --%>
<%-- 	<jsp:include page="${e3ps:getIncludeURLString('/change/include_activityECO')}" flush="true"> --%>
<%-- 		<jsp:param name="oid" value="${cOid}"/> --%>
<%-- 	</jsp:include> --%>
<%-- 	</c:if> --%>
	<%-- <c:if test="${className eq 'EChangeActivity'}">
	<jsp:include page="${e3ps:getIncludeURLString('/change/include_activityECA')}" flush="true">
		<jsp:param name="oid" value="${cOid}"/>
	</jsp:include>
	</c:if>  --%>
	
	<c:choose>
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
	<div class="ml30 mr30" style="margin-bottom:10%">
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
	</c:choose>
	</form>
</div>