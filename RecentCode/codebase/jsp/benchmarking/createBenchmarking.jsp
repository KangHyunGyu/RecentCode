<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript">
<%----------------------------------------------------------
*                      임시 저장 및 등록 버튼
----------------------------------------------------------%>
function save(btn){
	if(!checkValidate()) {
		return;
	}
	
	$("#bmForm").attr("action",getURLString("/benchmarking/createBenchmarkingAction"));
	var param = new Object();

	//결재선 지정 리스트
	var approvalList = $$("app_line_grid_wrap").data.serialize();
	
	//관련 부품
	var relatedPartList = $$("add_relatedPart_grid_wrap").data.serialize();
	
	var appState = $(btn).val();
	
	param["approvalList"] = approvalList;
	param["relatedPartList"] = relatedPartList;
	param["appState"] = appState;
	
	var msg = "";
	
	if(appState == "APPROVING") {
		if(checkApproveLine()) {
			msg = "${e3ps:getMessage('등록하시겠습니까?')}";	
		} else {
			return;
		}
	} else if(appState == "TEMP_STORAGE") {
		msg = "${e3ps:getMessage('임시저장하시겠습니까?')}";
	}
	
	formSubmit("bmForm", param, msg, null, true);
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
	
	if($("[id=PRIMARY]").length == 0){
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

	var appLineValid = true;
	 $$("app_line_grid_wrap").data.each(function(obj){
		if(!obj.roleType || obj.roleType.length === 0){
			appLineValid = false;
		} else if(!obj.name || obj.name.length === 0) {
			appLineValid = false;
		}
	});
	 
	if(!appLineValid) {
		$("#app_line_grid_wrap")[0].scrollIntoView();
		openNotice("${e3ps:getMessage('추가된 결재선에 값을 입력하세요.')}");
		return false;
	}
	
	return true;
}
</script>
<div class="product"> 
<form name="bmForm" id="bmForm" method="post">
	<!-- button -->
	<div class="seach_arm pt20 pb5">
		<div class="leftbt">
			<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('기본 정보')}</span>
		</div>
		<div class="rightbt">
			<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0"/>
			<button type="button" value="TEMP_STORAGE" class="s_bt03" onclick="save(this)">${e3ps:getMessage('임시 저장')}</button>
			<button type="button" value="APPROVING" class="s_bt03" onclick="save(this)">${e3ps:getMessage('등록')}</button>
		</div>
	</div>
	<!-- //button -->
	<div class="pro_table mr30 ml30">
		<table class="mainTable">
			<colgroup>
				<col style="width:15%">
				<col style="width:35%">
				<col style="width:15%">
				<col style="width:35%">
			</colgroup>	
			<tbody>
				<tr>
					<th>${e3ps:getMessage('제목')}<span class="required">*</span></th>
					<td><input type="text" class="w95" id="name" name="name"></td>
					<th>${e3ps:getMessage('BM 대상 (차종 및 샘플명)')}<span class="required">*</span></th>
					<td><input type="text" class="w95" id="target" name="target"></td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('BM 시작일')}</th>
					<td class="calendar"><input type="text" class="datePicker w50" id="bmStartDate" name="bmStartDate"></td>
					<th>${e3ps:getMessage('BM 완료일')}</th>
					<td class="calendar"><input type="text" class="datePicker w50" id="bmEndDate" name="bmEndDate"></td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('샘플 확보 방안 및 내역')}</th>
					<td colspan="3"><input type="text" class="w95" id="sampleDesc" name="sampleDesc"></td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('벤치마킹 내용')}<span class="required">*</span></th>
					<td class="pd15" colspan="3">
						<div class="textarea_autoSize">
							<textarea name="bmDesc" id="bmDesc" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
						</div>
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('비파괴 시험 항목')}</th>
					<td class="pd15" colspan="3">
						<div class="textarea_autoSize">
							<textarea name="nonDestruction" id="nonDestruction" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
						</div>
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('파괴 시험 항목')}</th>
					<td class="pd15" colspan="3">
						<div class="textarea_autoSize">
							<textarea name="destruction" id="destruction" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
						</div>
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('비용')}</th>
					<td colspan="3"><input type="number" id="cost" name="cost" class="w30"></td>
				</tr>
				<tr>
	               <th>${e3ps:getMessage('주첨부파일')}<span class="required">*</span></th>
	               <td class="primary" colspan="3">
	                  <jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
	                     <jsp:param name="formId" value="bmForm"/>
	                     <jsp:param name="command" value="insert"/>
	                     <jsp:param name="type" value="PRIMARY"/>
	                     <jsp:param name="btnId" value="createBtn" />
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
	                  </jsp:include>
	               </td>
	            </tr>
			</tbody>
		</table>
	</div>
	<!-- button -->
	<!-- 관련 부품 지정 include 화면 -->
	<div class="ml30 mr30">
		<jsp:include page="${e3ps:getIncludeURLString('/common/include_addObject')}" flush="true">
			<jsp:param name="objType" value="part"/>
			<jsp:param name="pageName" value="relatedPart"/>
			<jsp:param name="title" value="${e3ps:getMessage('관련 부품')}"/>
		</jsp:include>
	</div>
	<!-- 결재선 지정 include 화면 -->
	<div class="ml30 mr30">
		<jsp:include page="${e3ps:getIncludeURLString('/approval/include_addApprovalLine')}" flush="true">
			<jsp:param name="gridHeight" value="200"/>
		</jsp:include>
	</div>
</form>
</div>
