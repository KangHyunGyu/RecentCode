<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//팝업 리사이즈
	popupResize();
	
	getNumberCodeChildList("customer", "CUSTOMER", "", false, false, true);
	getNumberCodeList("extensions", "EXTENSIONS", false, true);
	
	$("#customer").val("${epm.customer}");
	$("#extensions").val("${epm.extensions}");
	
	$("#epmName").data("required", true);
	$("#epmName").data("message", "${e3ps:getMessage('도면 명을 입력하세요.')}");
});

function modify(){
	
	if(!checkValidate()) {
		return;
	}
	
	$("#epmForm").attr("action",getURLString("/epm/modifyEpmAction"));
	
	var param = new Object();
	param["container"] = $("#container").val();
	formSubmit("epmForm", param, "${e3ps:getMessage('수정하시겠습니까?')}", null, true);
	
}

function checkValidate() {
	
	if($("#locationDisplay").val().length == 0){
		openNotice("${e3ps:getMessage('도면 분류를 로딩중입니다.')}");
		return false;
	}
	
// 	if($("[name=PRIMARY]").length == 0){
// 		openNotice("${e3ps:getMessage('파일을 첨부하여 주세요.')}");
// 		return false;
// 	}
	var primaryCheck = $("#PRIMARY_uploadQueueBox").find(".AXUploadItem");
	if(primaryCheck.val() == undefined){
		openNotice("${e3ps:getMessage('주 첨부파일을 추가하세요.')}");
		return false;
	}
	return true;
}
</script>
<!-- pop -->
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('도면 수정')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->

	<div class="pl25 pr25">
		<div class="seach_arm2 pt10 pb5">
			<div class="leftbt">
				<h4>${e3ps:getMessage('기본 정보')}</h4>
			</div>
			<div class="rightbt">
				<button type="button" class="s_bt03" onclick="javascript:modify()">${e3ps:getMessage('수정')}</button>
				<button type="button" class="s_bt03" onclick="javascript:history.back()">${e3ps:getMessage('뒤로가기')}</button>
				<button type="button" class="s_bt04" onclick="javascript:window.close()">${e3ps:getMessage('닫기')}</button>
			</div>
		</div>
		<form name="epmForm" id="epmForm" method="post">
			<input type="hidden" id="location" name="location" value="">
			<input type="hidden" name="oid"  id="oid" value="${epm.oid}" />
			<div class="pro_table">
				<table class="mainTable">
					<colgroup>
						<col style="width:20%">
						<col style="width:80%">
					</colgroup>	
					<tbody>
						<tr>
							<th>${e3ps:getMessage('도면 분류')}<span class="required">*</span></th>
							<td>
								<div style="position:relative">
									<input type="text" id="locationDisplay" name="locationDisplay" disabled><button type="button" class="s_bt03" id="toggleBtn" onclick="toggleFolderList(this)">목록</button>
									<div id="folderList" style="display:none;position:absolute;z-index:999">
										<!-- tree -->
										<jsp:include page="${e3ps:getIncludeURLString('/common/include_folderTree')}" flush="true">
											<jsp:param name="container" value="${epm.container()}"/>
											<jsp:param name="renderTo" value="epmFolder"/>
											<jsp:param name="formId" value="epmForm"/>
											<jsp:param name="rootLocation" value="/Default"/>
											<jsp:param name="location" value="${epm.location}"/>
											<jsp:param name="autoGridHeight" value="false"/>
											<jsp:param name="gridHeight" value="200"/>
										</jsp:include>
										<!-- //tree -->
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('도면 번호')}</th>
							<td>
								${epm.number}
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('도면 명')}</th>
							<td>
								${epm.name}
							</td>
						</tr>
						<tr> 
							<th>${e3ps:getMessage('주첨부파일')}<span class="required">*</span></th>
							<td>
								<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
									<jsp:param name="formId" value="epmForm"/>
									<jsp:param name="command" value="insert"/>
									<jsp:param name="type" value="PRIMARY"/>
									<jsp:param name="btnId" value="createBtn"/>
									<jsp:param name="epmCheck" value="ok"/>
									<jsp:param name="oid" value="${epm.oid}"/>
				            	</jsp:include>
							</td>
						</tr>
						<tr> 
							<th>${e3ps:getMessage('첨부파일')}</th>
							<td>
								<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
									<jsp:param name="formId" value="epmForm"/>
									<jsp:param name="command" value="insert"/>
									<jsp:param name="type" value="SECONDARY"/>
									<jsp:param name="btnId" value="createBtn"/>
									<jsp:param name="oid" value="${epm.oid}"/>
				            	</jsp:include>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<jsp:include page="${e3ps:getIncludeURLString('/epm/include_epmAttributes')}" flush="true">
				<jsp:param name="oid" value="${epm.oid}"/>
				<jsp:param name="module" value="modify"/>
			</jsp:include>
		</form>
	</div>
</div>		
<!-- //pop-->
