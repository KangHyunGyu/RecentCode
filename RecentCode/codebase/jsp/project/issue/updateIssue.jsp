<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 <!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript"> 
$(document).ready(function(){
	getNumberCodeList("issueType", "ISSUETYPE", false, true);

	$("#issueType").val("${data.issueTypeKey}");
});

function modifyIssue(){

	if (document.modifyIssueForm.name.value == ''){
		alert('이슈명을 입력해 주십시오');
		document.modifyIssueForm.name.focus();
		return;
	}
	if (document.modifyIssueForm.issueType.value == ''){
		alert('이슈 타입을 입력해 주십시오');
		document.modifyIssueForm.issueType.focus();
		return;
	}
	if (document.modifyIssueForm.manager.value == ''){
		alert('담당자를 지정해 주십시오');
		document.modifyIssueForm.manager.focus();
		return;
	}
	if (document.modifyIssueForm.problem.value == ''){
		alert('이슈 내용을 입력해 주십시오');
		document.modifyIssueForm.problem.focus();
		return;
	}
	/* if(!textAreaLengthCheckId('problem','2000','이슈내용')){
		return;
	} */
	var url = getURLString("/project/issue/updateIssueAction");
	
	var param = new Object();
	
	var paramArray = $("#modifyIssueForm").serializeArray();
	
	var secondaryList = new Array();
	var delocIdsList = new Array();

	$(paramArray).each(function(idx, obj){
		if(obj.name == "SECONDARY"){
			secondaryList.push(obj.value);
		}else if(obj.name == "delocIds"){
			delocIdsList.push(obj.value);
		}else{
			param[obj.name] = obj.value;
		}
	});
	
	if(secondaryList.length > 0){
		param["SECONDARY"] = secondaryList;
	}
	if(delocIdsList.length > 0){
		param["delocIds"] = delocIdsList;
	}
	
	ajaxCallServer(url, param, function(data){
		// location.reload();
		if (opener.window.getGridData) {
			opener.window.getGridData();
		}
	}, true);
}

function set_userInfo(sel_value, flag){
		alert("set_userInfo");
		var r_count = sel_value.length;
		var target;
		var tempTarget;
		
		target = document.modifyIssueForm.manager;
		tempTarget = document.modifyIssueForm.tempmanager;
		
		for(i = 0;i<r_count;i++){
			toValue = sel_value[i];
			tp_id = toValue.substring(0, toValue.indexOf('|'));
			tmpStr = toValue.substring(toValue.indexOf('|')+1);
			tp_nm = tmpStr.substring(0, tmpStr.indexOf('|'));
			
			tempTarget.value = tp_nm;
			target.value = tp_id;
		}
}
</script>
</head>
<body>
<!-- pop -->
<div class="pop5">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('이슈 수정')}</h2>
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
		<button type="button" class="s_bt03" id="searchBtn" onclick="modifyIssue();">${e3ps:getMessage('수정')}</button>
		<button type="button" class="s_bt05" id="resetBtn" onclick="history.back()">${e3ps:getMessage('뒤로')}</button>
		<button type="button" class="s_bt05" id="resetBtn" onclick="self.close()">${e3ps:getMessage('닫기')}</button>
	</div>
</div>

<form name="modifyIssueForm" id="modifyIssueForm" method="post"  enctype="multipart/form-data"> 
<input type="hidden" name="command">
<input type="hidden" name="oid" value="${oid}">
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
					<input type="text" class="w50" id="name" name="name" value="${name}">
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('이슈 타입')}<span class="required">*</span></th>
				<td>
					<select class="w15" id="issueType" name="issueType"></select>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('이슈 내용')}<span class="required">*</span></th>
				<td class="pd15">
					<div class="textarea_autoSize">
						<textarea name="problem" id="problem" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)">${problem}</textarea>
					</div>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('이슈 담당자')}<span class="required">*</span></th>
				<td>
				  <%--  <%
					   String creatorInputURL = "/jsp/project/include/creatorInput.jsp";
				   %> --%>
					<div class="pro_view">
						<select class="searchUser" id="manager" name="manager" data-width="60%">
							<option value="${manager}">${data.managerFullName}</option>
						</select>
						<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('manager');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
						<span class="pointer verticalMiddle" onclick="javascript:deleteUser('manager');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
					</div>
				</td>
			</tr>
			<tr> 
	               <th>${e3ps:getMessage('첨부파일')}</th>
	               <td>
	                 <jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
				          <jsp:param name="formId" value="modifyIssueForm"/>
				          <jsp:param name="command" value="insert"/>
				          <jsp:param name="btnId" value="createBtn" />
				          <jsp:param name="oid" value="${oid}"/>
				     </jsp:include>
	               </td>
	        </tr>
		</tbody>
	</table>
</div>
</form>
</div>
