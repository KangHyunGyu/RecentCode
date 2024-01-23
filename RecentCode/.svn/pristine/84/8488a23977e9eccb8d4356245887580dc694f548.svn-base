<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript"> 
function createSolution(){
	document.IssueForm.action="/Windchill/worldex/project/issue/createSolution";
	document.IssueForm.submit();
}
function updateSolution(){
	document.IssueForm.action="/Windchill/worldex/project/issue/updateSolution";
	document.IssueForm.submit();
}
function updateRequest(){
	document.IssueForm.action="/Windchill/worldex/project/issue/updateIssue";
	document.IssueForm.submit();
}
function deleteRequest(){
	if (!confirm("삭제 하시겠습니까?"))
		return;
	var url = getURLString("/project/issue/deleteIssueAction");
	
	var param = new Object();
	
	var paramArray = $("#IssueForm").serializeArray();
	
	$(paramArray).each(function(idx, obj){
		param[obj.name] = obj.value;
	});
	
	ajaxCallServer(url, param, function(data){
		// location.reload();
	}, true);
}
</script>

<!-- pop -->
<div class="pop5">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('프로젝트 이슈 상세정보')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"></div>
	<div class="rightbt"></div>
</div>

<form name="IssueForm" id="IssueForm" method="post" > 

<input type="hidden" name="command">
<input type="hidden" name="oid" value="${oid}">

<div class="seach_arm pt10 pb5">
	<div class="leftbt">
		<img class="pointer mb5" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png"> ${e3ps:getMessage('이슈 상세정보')}
	</div>
	<div class="rightbt">
		<c:choose>
			<c:when test="${(isCreator || isAdmin) && !isView }">
			<c:choose>
				<c:when test="${isState && solution ne null}">
					<input type="button" class="s_bt03" value="완료확인" onclick="issueComplete()">
				</c:when>
				<c:when test="${isComplete}">
					<input type="button" class="s_bt03" value="완료취소" onclick="cancelComplete()">
				</c:when>
			</c:choose>
			</c:when>
		</c:choose>
		<c:choose>
			<c:when test="${!isCompleteState}">
				<c:if test="${(isManager || isAdmin) && solution eq null}">
					<button type="button" class="s_bt03" id="searchBtn" onclick="createSolution();">${e3ps:getMessage('해결방안 등록')}</button>
				</c:if>
				<c:if test="${(isCreator || isAdmin) && solution ne null}">
					<button type="button" class="s_bt03" id="searchBtn" onclick="updateSolution();">${e3ps:getMessage('해결방안 수정')}</button>
				</c:if>
				<c:choose>
					<c:when test="${isCreator || isAdmin}">
						<c:choose>
							<c:when test="${isRequestState}">
								<button type="button" class="s_bt03" id="searchBtn" onclick="updateRequest();">${e3ps:getMessage('수정')}</button>
							</c:when>
						</c:choose>
					</c:when>
				</c:choose>
			</c:when>
		</c:choose>
		<button type="button" class="s_bt05" id="resetBtn" onclick="self.close();">${e3ps:getMessage('닫기')}</button>
	</div>
</div>


</form>
	<%-- <jsp:include page="/jsp/project/issue/include/viewIssue.jsp"></jsp:include> --%>
	<jsp:include page="${e3ps:getIncludeURLString('/project/issue/include_viewIssue')}" flush="true">
				<jsp:param name="oid" value="${oid}"/>
	</jsp:include>
	
	<c:if test="${solution ne null}">
		<%-- <jsp:include page="/jsp/project/issue/include/solution.jsp"></jsp:include> --%>
		<jsp:include page="${e3ps:getIncludeURLString('/project/issue/include_solution')}" flush="true">
				<jsp:param name="oid" value="${oid}"/>
				<jsp:param name="command" value="${command}"/>
		</jsp:include>
	</c:if>
</div>

