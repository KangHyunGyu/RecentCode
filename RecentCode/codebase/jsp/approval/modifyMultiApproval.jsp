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

function modify(){
	if(!checkValidate()) {
		return;
	}
	//console.log("==== modify")
	$("#multiForm").attr("action",getURLString("/multi/modifyMultiAction"));
	
	var param = new Object();
	
	//일괄 결재 목록
	let multiObjectList = AUIGrid.getGridData(add_${pageName}_myGridID);
	
	param["multiObjectList"] = multiObjectList;
	
	formSubmit("multiForm", param, "${e3ps:getMessage('수정하시겠습니까?')}");
}

function checkValidate() {
	if($("#name").val() == null || $("#name").val() == "") {
		$("#name").focus();
		openNotice("${e3ps:getMessage('일괄 결재 제목을 입력하세요.')}");
		return false;
	}
	
	let multiObjectList = AUIGrid.getGridData(add_${pageName}_myGridID);
	if(multiObjectList.length == 0) {
		openNotice("${e3ps:getMessage('일괄 결재를 추가하세요.')}");
		return false;
	}
	
	var multiObjectValid = true;
	multiObjectList.forEach((obj)=> {
		if(!obj.number || obj.number.length === 0){
			multiObjectValid = false;
		}
	})
	
	if(!multiObjectValid) {
		//$(add_${pageName}_grid_wrap)[0].scrollIntoView();
		openNotice("${e3ps:getMessage('추가된 일괄 결재에 값을 입력하세요.')}");
		return false;
	}
	
	return true;
}
</script>
<!-- pop -->
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('일괄결재 수정')}</h2>
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
		<form name="multiForm" id="multiForm" method="post">
			<input type="hidden" name="oid"  id="oid" value="${multi.oid}" />
			<div class="pro_table">
				<table class="mainTable">
					<colgroup>
						<col style="width:20%">
						<col style="width:80%">
					</colgroup>	
					<tbody>
						<tr>
							<th>${e3ps:getMessage('일괄 결재 번호')}<span class="required">*</span></th>
							<td>
								${multi.number}
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('일괄 결재 명')}<span class="required">*</span></th>
							<td>
								<input class="w50" type="text" id="name" name="name" value="${multi.name}">
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('설명')}</th>
							<td>
								<div class="textarea_autoSize">
									<textarea name="description" id="description" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)">${multi.description}</textarea>
								</div>
							</td>
						</tr>
						
						<tr> 
							<th>${e3ps:getMessage('첨부파일')}</th>
							<td>
								<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
				                     <jsp:param name="formId" value="multiForm"/>
				                     <jsp:param name="command" value="insert"/>
				                     <jsp:param name="btnId" value="createBtn" />
				                     <jsp:param name="oid" value="${multi.oid}"/>
				                 </jsp:include>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</form>
	</div>
	<!-- 문서 지정 include 화면 -->
	<div class="ml30 mr30">
		<jsp:include page="${e3ps:getIncludeURLString('/common/include_addObject')}" flush="true">
			<jsp:param name="oid" value="${multi.oid}"/>
			<jsp:param name="objType" value="${multi.objectType}"/>
			<jsp:param name="pageName" value="${pageName}"/>
			<jsp:param name="title" value="${title}<span class='required'>*</span>"/>
			<jsp:param name="gridHeight" value="200"/>
		</jsp:include>
	</div>
</div>		
<!-- //pop-->
