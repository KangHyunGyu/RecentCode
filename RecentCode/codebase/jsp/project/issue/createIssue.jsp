<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- pop -->
<div class="pop5">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('프로젝트 이슈 등록')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>

<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"></div>
	<div class="rightbt"></div>
</div>

<div class="seach_arm pt10 pb5">
	<div class="leftbt">
		<img class="pointer mb5" onclick="switchSearchMenu(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
	</div>
	<div class="rightbt">
		<button type="button" class="s_bt03" id="searchBtn" onclick="createIssue();">${e3ps:getMessage('등록')}</button>
		<button type="button" class="s_bt05" id="resetBtn" onclick="self.close();">${e3ps:getMessage('닫기')}</button>
	</div>
</div>

<form name="createIssueForm" id="createIssueForm" method="post"  enctype="multipart/form-data"> 
<input type="hidden" name="command"/>
<input type="hidden" name=taskOid value="${taskOid}"/>

<div class="pro_table mr30 ml30">
	<table class="mainTable">
		<colgroup>
			<col style="width:20%">
			<col style="width:80%">
		</colgroup>	
		<tbody>
			<tr>
				<th>${e3ps:getMessage('이슈 제목')}<span class="required">*</span></th>
				<td>
					<input type="text" class="w50" id="name" name="name">
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('이슈 타입')}<span class="required">*</span></th>
				<td>
					<select class="w15" id="issueType" name="issueType">
					</select>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('이슈 내용')}<span class="required">*</span></th>
				<td class="pd15">
					<div class="textarea_autoSize">
						<textarea name="problem" id="problem" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
					</div>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('이슈 담당자')}<span class="required">*</span></th>
				<td>
					<div class="pro_view">
						<select id="manager" name="manager" data-width="60%">
						</select>
						<span class="pointer verticalMiddle" onclick="javascript:openRoleUserPopup('manager', '${projectOid}');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
						<span class="pointer verticalMiddle" onclick="javascript:deleteUser('manager');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
					</div>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('완료요청일')}<span style="color:red;">*</span></th>
				<td colspan="2" class="calendar">
					<input type="text" class="datePicker w50" name="deadLine" id="deadLine" 
					 readonly/>
				</td>
			</tr>
			<tr> 
	               <th>${e3ps:getMessage('첨부파일')}</th>
	               <td>
	                  <jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
	                     <jsp:param name="formId" value="createIssueForm"/>
	                     <jsp:param name="command" value="insert"/>
	                     <jsp:param name="btnId" value="createBtn" />
	                  </jsp:include>
	               </td>
	            </tr>
		</tbody>
	</table>
</div>
</form>

</div>
<script type="text/javascript"> 
$(document).ready(function(){
	getNumberCodeList("issueType", "ISSUETYPE", false, true);
	searchProjectRoleUser("manager", "${projectOid}"); 
});
function createIssue(){

	if (document.createIssueForm.name.value == ''){
		alert('이슈명을 입력해 주십시오');
		document.createIssueForm.name.focus();
		return;
	}
	if (document.createIssueForm.issueType.value == ''){
		alert('이슈 타입을 입력해 주십시오');
		document.createIssueForm.issueType.focus();
		return;
	}
	if (document.createIssueForm.manager.value == ''){
		alert('담당자를 지정해 주십시오');
		document.createIssueForm.manager.focus();
		return;
	}
	if (document.createIssueForm.problem.value == ''){
		alert('이슈 내용을 입력해 주십시오');
		
		document.createIssueForm.problem.focus();
		return;
	}
	if (document.createIssueForm.deadLine.value == ''){
		alert('완료 요청일을 선택해주세요');
		
		document.createIssueForm.deadLine.focus();
		return;
	}
	if (!confirm("등록하시겠습니까?")){
		return;
	}
	
	var url = getURLString("/project/issue/createIssueAction");
	
	var param = new Object();
	
	var paramArray = $("#createIssueForm").serializeArray();
	
	var secondaryList = new Array();

	$(paramArray).each(function(idx, obj){
		if(obj.name == "SECONDARY"){
			secondaryList.push(obj.value);
		}else{
			param[obj.name] = obj.value;
		}
	});
	
	if(secondaryList.length > 0){
		param["SECONDARY"] = secondaryList;
	}
	
	ajaxCallServer(url, param, function(data){
		opener.window.location.reload();
	}, true);
	
}

function setRoleUser(id, list){
	var oldList = $("#" + id).val();
	$("#" + id).trigger("change");
	for(var i=0; i < list.length; i++) {
		var item = list[i];
		var flag = true;
		if(oldList != null) {
			for(var j=0; j < oldList.length; j++) {
				if(oldList[j] == item.peopleOid) {
					flag = false;
					break;				
				}
			}	
		}
		
		if(flag) {
			$("#" + id).append("<option value='" + item.peopleOid + "' selected>" + item.roleName + " / " + item.duty +" "+item.userName + "</option>");
			$("input[name=" + id + "]").val(item.peopleOid);
		}
	}
}
</script>
