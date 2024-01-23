<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>

<script src="/Windchill/jsp/js/xhRequest.js"></script>
<script src="/Windchill/jsp/js/popup.js"></script>
<script>
$(document).ready(function(){
	//팝업 리사이즈
	popupResize();
	
	getNumberCodeList("occurrenceModel", "CARTYPE", false, true);
	getNumberCodeList("occurrenceStep", "OCCURRENCESTEP", false, true);
	getNumberCodeList("occurrencePlace", "OCCURRENCEPLACE", false, true);
	getNumberCodeList("oldcarState", "OLDCARSTEP", false, true);
	
	$("#occurrenceModel").val("${oldcar.occurrenceModel}");
	$("#occurrenceStep").val("${oldcar.occurrenceStep}");
	$("#occurrencePlace").val("${oldcar.occurrencePlace}");
	$("#oldcarState").val("${oldcar.oldcarState}");
});
function modify(){
	if(!checkValidate()) {
		return;
	}
	$("#oldcarForm").attr("action",getURLString("/oldcar/modifyOldcarAction"));
	
	var param = new Object();
	
	//관련 부품
	var relatedPartList = $$("add_relatedPart_grid_wrap").data.serialize();
	
	param["relatedPartList"] = relatedPartList;
	
	formSubmit("oldcarForm", param, "${e3ps:getMessage('수정하시겠습니까?')}",function(){
		if(opener.window.search) {
			opener.window.search();
		}
	}, true);
}
function checkValidate() {
	
	if($("#oldcarName").val() == null || $("#oldcarName").val() == "") {
		$("#oldcarName").focus();
		openNotice("${e3ps:getMessage('제목을 입력하세요.')}");
		return false;
	}
	if($("#occurrenceDate").val() == null || $("#occurrenceDate").val() == "") {
		$("#occurrenceDate").focus();
		openNotice("${e3ps:getMessage('발생일을 입력하세요.')}");
		return false;
	}
	if($("#occurrenceStep").val() == null || $("#occurrenceStep").val() == "") {
		$("#occurrenceStep").focus();
		openNotice("${e3ps:getMessage('발생시점을 입력하세요.')}");
		return false;
	}
	if($("#occurrenceModel").val() == null || $("#occurrenceModel").val() == "") {
		$("#occurrenceModel").focus();
		openNotice("${e3ps:getMessage('차종을 입력하세요.')}");
		return false;
	}
	if($("#occurrencePlace").val() == null || $("#occurrencePlace").val() == "") {
		$("#occurrencePlace").focus();
		openNotice("${e3ps:getMessage('발생장소를 입력하세요.')}");
		return false;
	}
	if($("#oldcarState").val() == null || $("#oldcarState").val() == "") {
		$("#oldcarState").focus();
		openNotice("${e3ps:getMessage('처리상태를 입력하세요.')}");
		return false;
	}
	if($("#problemContent").val() == null || $("#problemContent").val() == "") {
		$("#problemContent").focus();
		openNotice("${e3ps:getMessage('문제내용을 입력하세요.')}");
		return false;
	}
	
// 	if($("[id=PRIMARY]").length == 0){
// 		openNotice("${e3ps:getMessage('주 첨부파일을 추가하세요.')}");
// 		return false;
// 	}
	var primaryCheck = $("#PRIMARY_uploadQueueBox").find(".AXUploadItem");
	if(primaryCheck.val() == undefined){
		openNotice("${e3ps:getMessage('주 첨부파일을 추가하세요.')}");
		return false;
	}
	
	var partValid = true;
	$$("add_relatedPart_grid_wrap").data.each(function(obj){
		if(!obj.number || obj.number.length === 0){
			partValid = false;
		}
	});
	
	if(!partValid) {
		$("#add_relatedPart_grid_wrap")[0].scrollIntoView();
		openNotice("${e3ps:getMessage('추가된 관련 부품에 값을 입력하세요.')}");
		return false;
	}
	
	return true;
}

</script>
<!-- pop -->
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('과거차 수정')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->

	<div class="pl25 pr25">
		<div class="seach_arm2 pt10 pb10">
			<div class="leftbt"></div>
			<div class="rightbt">
				<button type="button" class="s_bt03" onclick="javascript:modify()">${e3ps:getMessage('수정')}</button>
				<button type="button" class="s_bt03" onclick="javascript:history.back()">${e3ps:getMessage('뒤로가기')}</button>
				<button type="button" class="s_bt04" onclick="javascript:window.close()">${e3ps:getMessage('닫기')}</button>
			</div>
		</div>
		<form name="oldcarForm" id="oldcarForm" method="post">
			<input type="hidden" name="oid"  id="oid" value="${oldcar.oid}" />
			<input type="hidden" name="mode"  id="mode" value="modify" />
			<div class="pro_table">
				<table class="mainTable">
					<colgroup>
						<col style="width:13%">
						<col style="width:37%">
						<col style="width:13%">
						<col style="width:37%">
					</colgroup>	
					<tbody>
						<tr>
							<th>${e3ps:getMessage('과거차 번호')}</th>
							<td colspan="3">${oldcar.oldcarNumber}</td>
						</tr>
						<tr>
						<th>${e3ps:getMessage('제목')}<span class="required">*</span></th>
							<td colspan="3">
								<input type="text" class="w95" id="oldcarName" name="oldcarName" value="${oldcar.oldcarName}">
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('발생일')}<span class="required">*</span></th>
							<td class="calendar">
								<input type="text" class="datePicker w25" id="occurrenceDate" name="occurrenceDate" readonly value="${oldcar.occurrenceDate}">
							</td>
							<th>${e3ps:getMessage('발생시점')}<span class="required">*</span></th>
							<td>
								<select class="w50" id="occurrenceStep" name="occurrenceStep"></select>
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('차종')}<span class="required">*</span></th>
							<td>
								<select class="w50" id="occurrenceModel" name="occurrenceModel"></select>
							</td>
							<th>${e3ps:getMessage('발생장소')}<span class="required">*</span></th>
							<td>
								<select class="w50" id="occurrencePlace" name="occurrencePlace"></select>
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('시행자')}</th>
							<td>
								<div class="pro_view">
									<select class="searchUser" id="worker" name="worker" data-width="60%">
										<option value="${oldcar.workerPOID }">${oldcar.workerName }</option>
									</select>
									<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('worker');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
									<span class="pointer verticalMiddle" onclick="javascript:deleteUser('worker');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
								</div>
							</td>
							<th>${e3ps:getMessage('처리상태')}<span class="required">*</span></th>
							<td>
								<select class="w50" id="oldcarState" name="oldcarState"></select>
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('관리검증기준')}</th>
							<td colspan="3">
								<input type="text" class="w95" id="criteria" name="criteria" value="${oldcar.criteria}">
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('문제공정')}</th>
							<td class="pd15" colspan="3">
								<div class="textarea_autoSize">
									<textarea name="problemProcess" id="problemProcess" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)">${oldcar.problemProcess }</textarea>
								</div>
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('문제내용')}<span class="required">*</span></th>
							<td class="pd15" colspan="3">
								<div class="textarea_autoSize">
									<textarea name="problemContent" id="problemContent" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)">${oldcar.problemContent }</textarea>
								</div>
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('문제점 및 원인')}</th>
							<td class="pd15" colspan="3">
								<div class="textarea_autoSize">
									<textarea name="cause" id="cause" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)">${oldcar.cause }</textarea>
								</div>
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('개선대책 및 처리내용')}</th>
							<td class="pd15" colspan="3">
								<div class="textarea_autoSize">
									<textarea name="improve" id="improve" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)">${oldcar.improve }</textarea>
								</div>
							</td>
						</tr>
						<tr>
			               <th>${e3ps:getMessage('주첨부파일')}<span class="required">*</span></th>
			               <td colspan="3">
			                  <jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
			                     <jsp:param name="formId" value="oldcarForm"/>
			                     <jsp:param name="command" value="insert"/>
			                     <jsp:param name="type" value="PRIMARY"/>
			                     <jsp:param name="btnId" value="createBtn" />
			                     <jsp:param name="oid" value="${oldcar.oid}"/>
			                  </jsp:include>
			               </td>
			            </tr>
			            <tr> 
			               <th>${e3ps:getMessage('첨부파일')}</th>
			               <td colspan="3">
			                  <jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
			                     <jsp:param name="formId" value="oldcarForm"/>
			                     <jsp:param name="command" value="insert"/>
			                     <jsp:param name="btnId" value="createBtn" />
			                     <jsp:param name="oid" value="${oldcar.oid}"/>
			                  </jsp:include>
			               </td>
			            </tr>
					</tbody>
				</table>
			</div>
		</form>
	</div>
	<!-- 관련 부품 지정 include 화면 -->
	<div class="pl25 pr25 pb10">
		<jsp:include page="${e3ps:getIncludeURLString('/common/include_addObject')}" flush="true">
			<jsp:param name="oid" value="${oldcar.oid}"/>
			<jsp:param name="objType" value="part"/>
			<jsp:param name="pageName" value="relatedPart"/>
			<jsp:param name="title" value="${e3ps:getMessage('관련 부품')}"/>
		</jsp:include>
	</div>
</div>		
<!-- //pop-->
