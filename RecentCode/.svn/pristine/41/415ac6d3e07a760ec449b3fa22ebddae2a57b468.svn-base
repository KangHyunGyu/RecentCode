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
});
function modify(){
	if(!checkValidate()) {
		return;
	}
	$("#bmForm").attr("action",getURLString("/benchmarking/modifyBenchmarkingAction"));
	
	var param = new Object();
	
	//관련 부품
	var relatedPartList = $$("add_relatedPart_grid_wrap").data.serialize();
	
	param["relatedPartList"] = relatedPartList;
	
	formSubmit("bmForm", param, "${e3ps:getMessage('수정하시겠습니까?')}",function(){
		if(opener.window.search) {
			opener.window.search();
		}
	}, true);
}
function checkValidate() {
	
	if($("#name").val() == null || $("#name").val() == "") {
		$("#name").focus();
		openNotice("${e3ps:getMessage('제목을 입력하세요.')}");
		return false;
	}
	
	if($("#target").val() == null || $("#target").val() == "") {
		$("#target").focus();
		openNotice("${e3ps:getMessage('BM 대상을 입력하세요.')}");
		return false;
	}
	
	if($("#bmDesc").val() == null || $("#bmDesc").val() == "") {
		$("#bmDesc").focus();
		openNotice("${e3ps:getMessage('벤치마킹 내용을 입력하세요.')}");
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
		<form name="bmForm" id="bmForm" method="post">
			<input type="hidden" name="oid"  id="oid" value="${bm.oid}" />
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
							<th>${e3ps:getMessage('벤치마킹 번호')}</th>
							<td colspan="3">${bm.number}</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('제목')}<span class="required">*</span></th>
							<td><input class="w95" type="text" id="name" name="name" value="${bm.name}"></td>
							<th>${e3ps:getMessage('BM 대상 (차종 및 샘플명)')}<span class="required">*</span></th>
							<td><input type="text" class="w95" id="target" name="target" value="${bm.target }"></td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('BM 시작일')}</th>
							<td class="calendar"><input type="text" class="datePicker w50" id="bmStartDate" name="bmStartDate" value="${bm.bmStartDate }"></td>
							<th>${e3ps:getMessage('BM 완료일')}</th>
							<td class="calendar"><input type="text" class="datePicker w50" id="bmEndDate" name="bmEndDate" value="${bm.bmEndDate }"></td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('샘플 확보 방안 및 내역')}</th>
							<td colspan="3"><input type="text" class="w95" id="sampleDesc" name="sampleDesc" value="${bm.sampleDesc }"></td>
						</tr>
						<tr>
						<th>${e3ps:getMessage('벤치마킹 내용')}<span class="required">*</span></th>
							<td class="pd15" colspan="3">
								<div class="textarea_autoSize">
									<textarea name="bmDesc" id="bmDesc" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)">${bm.bmDesc }</textarea>
								</div>
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('비파괴 시험 항목')}</th>
							<td class="pd15" colspan="3">
								<div class="textarea_autoSize">
									<textarea name="nonDestruction" id="nonDestruction" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)">${bm.nonDestruction }</textarea>
								</div>
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('파괴 시험 항목')}</th>
							<td class="pd15" colspan="3">
								<div class="textarea_autoSize">
									<textarea name="destruction" id="destruction" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)">${bm.destruction }</textarea>
								</div>
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('비용')}</th>
							<td colspan="3"><input type="number" id="cost" name="cost" class="w30" value="${bm.cost }"></td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('주첨부파일')}<span class="required">*</span></th>
							<td colspan="3">
								<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
				                     <jsp:param name="formId" value="bmForm"/>
				                     <jsp:param name="command" value="insert"/>
				                     <jsp:param name="type" value="PRIMARY"/>
				                     <jsp:param name="btnId" value="createBtn" />
				                     <jsp:param name="oid" value="${bm.oid}"/>
				                 </jsp:include>
							</td>
						</tr>
						<tr> 
							<th>${e3ps:getMessage('첨부파일')}</th>
							<td colspan="3">
								<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
				                     <jsp:param name="formId" value="bmForm"/>
				                     <jsp:param name="command" value="insert"/>
				                     <jsp:param name="btnId" value="createBtn" />
				                     <jsp:param name="oid" value="${bm.oid}"/>
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
			<jsp:param name="oid" value="${bm.oid}"/>
			<jsp:param name="objType" value="part"/>
			<jsp:param name="pageName" value="relatedPart"/>
			<jsp:param name="title" value="${e3ps:getMessage('관련 부품')}"/>
		</jsp:include>
	</div>
</div>		
<!-- //pop-->
