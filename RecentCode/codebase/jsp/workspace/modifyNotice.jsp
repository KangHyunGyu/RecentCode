<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	$("#title").data("required", true);
	$("#title").data("message", "${e3ps:getMessage('제목을 입력하세요.')}");
	$("#notifyType").data("required", true);
	$("#notifyType").data("message", "${e3ps:getMessage('공지사항 분류를 선택하세요.')}");
	
	getNotifyTypeList();
	
	$("#notifyType").val("${notice.notifyType}");
});

function modify(){
	$("#noticeForm").attr("action",getURLString("/workspace/modifyNoticeAction"));
	
	var param = new Object();
	
	formSubmit("noticeForm", param, "${e3ps:getMessage('수정하시겠습니까?')}", null, true);
}
</script>
<div class="product"> 
<form name="noticeForm" id="noticeForm" method="post">
	<input type="hidden" name="oid"  id="oid" value="${notice.oid}" />
	<!-- button -->
	<div class="seach_arm pt20 pb5">
		<div class="leftbt">
			<h4>${e3ps:getMessage('기본 정보')}</h4>
		</div>
		<div class="rightbt">
			<button type="button" class="s_bt03" onclick="modify();">${e3ps:getMessage('수정')}</button>
			<button type="button" class="s_bt03" onclick="javascript:history.back();">${e3ps:getMessage('뒤로가기')}</button>
		</div>
	</div>
	<!-- //button -->
	<div class="pro_table mr30 ml30">
		<table class="mainTable">
			<colgroup>
				<col style="width:20%">
				<col style="width:80%">
			</colgroup>	
			<tbody>
				<tr>
					<th>${e3ps:getMessage('제목')}<span class="required">*</span></th>
					<td>
						<input type="text" class="w50" id="title" name="title" value="${notice.title}">
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('공지사항 분류')}<span class="required">*</span></th>
					<td>
						<select class="w25" id="notifyType" name="notifyType"></select>
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('게시 기한일자')}</th>
					<td class="calendar">
						<input type="text" class="datePicker w50" name="deadline" id="deadline" value="${notice.deadline}" readonly/>
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('설명')}</th>
					<td class="pd15">
						<jsp:include page="${e3ps:getIncludeURLString('/common/include_smartEditor2')}" flush="false">
							<jsp:param name="oid" value="${notice.oid}" />
						</jsp:include>
						<!-- <div class="textarea_autoSize">
							<textarea name="contents" id="contents" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
						</div> -->
					</td>
				</tr>
	            <tr> 
	               <th>${e3ps:getMessage('첨부파일')}</th>
	               <td>
	                  <jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
	                     <jsp:param name="formId" value="noticeForm"/>
	                     <jsp:param name="command" value="insert"/>
	                     <jsp:param name="btnId" value="createBtn" />
	                     <jsp:param name="oid" value="${notice.oid}" />
	                  </jsp:include>
	               </td>
	            </tr>
			</tbody>
		</table>
	</div>
</form>
</div>