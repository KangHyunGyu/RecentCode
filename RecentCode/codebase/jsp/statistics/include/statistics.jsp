<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	if(${active}){
		getToDoCount();
		getApprovalCount();
	}
	if(${today}){
		getToDayCount();
		getToDayMyCount();
	}
	
});
//활동(ToDo) Count
function getToDoCount(){
	var url = getURLString("/statistics/getToDoCount");
	ajaxCallServer(url, null, function(data){
		$("#todoCount").text(data.count);
	}, true);
}
//결재 Count
function getApprovalCount(){
	var url = getURLString("/statistics/getApprovalCount");
	ajaxCallServer(url, null, function(data){
		$("#approvalCount").text(data.count);
	}, true);
}
//ToDay All Count
function getToDayCount(){
	var param = new Object();
	param["obj"] = "${obj}";
	
	var url = getURLString("/statistics/getToDayCount");
	ajaxCallServer(url, param, function(data){
		$("#toDayCount").text(data.count);
	}, true);
}
//ToDay My Count
function getToDayMyCount(){
	var param = new Object();
	param["obj"] = "${obj}";
	
	var url = getURLString("/statistics/getToDayMyCount");
	ajaxCallServer(url, param, function(data){
		$("#toDayMyCount").text(data.count);
	}, true);
}

</script>
	<!-- s1 -->
	<c:if test="${active}">
		<div class="s_all">	
			<div class="s1 pointer" onclick="javascript:moveLocation('/workspace/listToDo')">
				<p class="tt01" id="todoCount">0</p>
				<p class="tt02">ㅡ</p>
				<p class="tt03">${e3ps:getMessage('설변 업무')}</p>
			</div>
			<div class="s1 pointer" onclick="javascript:moveLocation('/workspace/listWorkItem?type=approval')">
				<p class="tt01" id="approvalCount">0</p>
				<p class="tt02">ㅡ</p>
				<p class="tt03">${e3ps:getMessage('결재')}</p>
			</div>			
		</div>
	</c:if>
	<!-- //s1 -->

	<!-- s2 -->
	<c:if test="${today}">
		<div class="s_all2 pt20">
			<div class="s2">
				<ul>
					<li class="tt01">TODAY</li>
					<li class="tt02" id="toDayCount"></li>
				</ul>
			</div>
			<div class="s2">
				<ul>
					<li class="tt01">MY</li>
					<li class="tt02" id="toDayMyCount"></li>
				</ul>
			</div>		
		</div>
	</c:if>
	<!--// s2 -->