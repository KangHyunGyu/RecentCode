<%@page import="com.e3ps.common.util.CommonUtil"%>
<%@page import="com.e3ps.common.util.StringUtil"%>
<%@page import="com.e3ps.common.util.DateUtil"%>
<%@page import="wt.session.SessionHelper"%>
<%@page import="wt.org.WTUser"%>
<%@page import="com.e3ps.project.beans.IssueData"%>
<%@page import="com.e3ps.project.issue.IssueSolution"%>
<%@page import="com.e3ps.project.issue.IssueRequest"%>
<%@page import="wt.fc.ReferenceFactory"%>

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- pop -->
<div class="pop5">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('해결방안 수정')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>

<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"></div>
	<div class="rightbt"></div>

<form name="IssueForm" id="IssueForm" method="post"   enctype="multipart/form-data" > 

<input type="hidden" name="command">
<input type="hidden" name="oid" value="${oid}">

<div class="seach_arm pt10 pb5">
	<div class="leftbt">
		<img class="pointer mb5" onclick="switchSearchMenu(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('해결방안 등록')}
	</div>
	<div class="rightbt">
		<button type="button" class="s_bt03" id="searchBtn" onclick="saveSolution();">${e3ps:getMessage('등록')}</button>
		<button type="button" class="s_bt05" id="resetBtn" onclick="history.back();">${e3ps:getMessage('뒤로')}</button>
		<button type="button" class="s_bt05" id="resetBtn" onclick="self.close();">${e3ps:getMessage('닫기')}</button>
	</div>
</div>

<div class="pro_table mr30 ml30">
	<table class="mainTable">
		<colgroup>
			<col style="width:20%">
			<col style="width:30%">
			<col style="width:20%">
			<col style="width:30%">
		</colgroup>	
		<tbody>
			<tr>
			    <th>답변 제기자</th>
				<td>
				&nbsp;${fullName}
			    </td>
			    <th>${e3ps:getMessage('완료 예정일')}<font color="red">*</font></th>
					<td class="calendar">
						<input type="text" class="datePicker w50" name="requestDate" id="requestDate" value="${requestDate}" readonly/>
				</td>
			</tr>
			<tr height="150">
			    <th>해결방안<font color="red">*</font></th>
				<td colspan="3">
					<div class="textarea_autoSize">
						<textarea name="solution" id="solution" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)">${solution}</textarea>
					</div>
			    </td>
			</tr>
			<tr> 
	               <th>${e3ps:getMessage('첨부파일')}</th>
	               <td colspan="3">
	                 <jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
				          <jsp:param name="formId" value="IssueForm"/>
				          <jsp:param name="command" value="insert"/>
				          <jsp:param name="btnId" value="createBtn" />
				          <jsp:param name="oid" value="${solutionOid}"/>
				     </jsp:include>
	               </td>
	        </tr>
<!-- 			<tr> -->
<!-- 				<th> -->
<!-- 					참조파일 -->
<!-- 				</th> -->
<!-- 				<td class="tdwhiteL" colspan="3"> -->
<%-- 					<jsp:include page="/extcore/kores/common/include/attachedFile.jsp" flush="true"> --%>
<%-- 						<jsp:param name="mode" value="update"/> --%>
<%-- 						<jsp:param name="choid" value="<%=CommonUtil.getOIDString(solution)%>"/> --%>
<%-- 						<jsp:param name="role" value="secondary"/> --%>
<%-- 					</jsp:include> --%>
<!-- 				</td> -->
<!-- 			</tr> -->
		</tbody>
	</table>
</div>

</form>

</div>
</div>
<script type="text/javascript"> 

function saveSolution(){
 	if (document.IssueForm.requestDate.value == ''){
 		alert('완료 예정일을 입력해 주십시오');
 		return;
 	}
	if (document.IssueForm.solution.value == ''){
		alert('해결방안을 입력해 주십시오');
		return;
	}
	
	var url = getURLString("/project/issue/updateSolutionAction");
	
	var param = new Object();
	
	var paramArray = $("#IssueForm").serializeArray();
	
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
		location.reload();
		if (opener.window.getGridData) {
			opener.window.getGridData();
		}
	}, true);
}

</script>