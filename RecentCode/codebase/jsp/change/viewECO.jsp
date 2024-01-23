<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script src="/Windchill/jsp/js/popup.js"></script>
<script type=text/javascript>
$(document).ready(function(){
	
	loadIncludePage();
	
});
function loadIncludePage(tab) {
	
	if(tab == null) {
		tab = $(".tap>ul>li:first");
	}
	
	$(".tap ul li").removeClass("on");
	
	$(tab).addClass("on");
	
	var url = $(tab).data("url");
	var param = $(tab).data("param");
	
	if(param == null) {
		param = new Object();
	}
	
	param["oid"] = "${data.oid}";
	param.type = "ECO";
	
	$("#includePage").load(url, param);
}
function modifyEco() {
	var url = getURLString("/change/updateECO") + "?oid=${data.oid}";
	console.log(url);
	location.href = url;
}
function deleteEco() {
	openConfirm("${e3ps:getMessage('삭제하시겠습니까?')}", function(){
		var param = new Object();
		param.oid = "${data.oid}";
		var url = getURLString("/change/deleteECOAction");
		ajaxCallServer(url, param, function(data){
			if(opener.window.search){
				opener.window.search();				
			}
		}, true);
	});
}
function stopEco(s){
	var url = getURLString("/change/stopECO") + "?oid="+"${data.oid}"+"&stop="+s;
	openPopup(url, "stopEco" ,500, 200);
}

function callbackStopECO(){
	document.changeForm.submit();
}

// function requestApproval(oid, params) {
//   var url = getURLString("/approval/requestApproval");
//   url = url + "?oid=" + oid + (params ? params : "");
//   openPopup(url, "Request Approval", "805", "805");
// }


function sendErpEco(){
	openConfirm("${e3ps:getMessage('보내겠습니까?')}", function() {
		var url = getURLString("/change/sendErpAction");
		var param = new Object();
		param.oid = "${oid}";
		
		ajaxCallServer(url, param, function(data){
		}, true);
	});
}
</script>
<!-- 식별자 -->
<input type="hidden" name="popup" value="true">
<input type="hidden" name="callbackName" value="${param.callbackName}">
<input type="hidden" name="mode" value="update">

<!-- pop -->
<div class="pop">
	<form name="changeForm" id="changeForm" method="post">
	<input type="hidden" name="oid" value="${data.oid}">
	<!-- top -->
	<div class="top">
		<h2>${data.icon} ${e3ps:getMessage('ECO')} - ${data.orderNumber}, ${data.name}</h2>
		<%-- <c:if test="${e3ps:isAdmin()}">
			<span style="padding-top:5px;padding-left:5px;">
				<button type="button" class="s_bt03" style="height:30px;" onclick="sendErpEco()">${e3ps:getMessage('ERP 전송')}</button>
			</span>
		</c:if> --%>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	<!-- //top -->

	<!--tap -->
	<div class="tap pt20">
		<ul>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/change/include_detailECO')}">${e3ps:getMessage('세부 내용')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/change/include_relatedObject')}">${e3ps:getMessage('관련 객체')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/change/include_activity')}">${e3ps:getMessage('단계 활동')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/common/include_downloadHistory')}">${e3ps:getMessage('다운로드 이력')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/common/include_approveHistory')}">${e3ps:getMessage('결재 이력')}</li>
		</ul>
		<div class="tapbutton">
<%-- 				<c:if test="${isEcaComplite eq true}"> --%>
<%-- 					<button type="button" class="i_create" style="width: 70px" onclick="javascript:requestApproval('${oid}');">${e3ps:getMessage('결재요청')}</button> --%>
<%-- 				</c:if> --%>
			<c:if test="${data.modifyBtn()}">
				<input type="button" class="s_bt03" value="${e3ps:getMessage('수정')}" onclick="modifyEco()">
			</c:if>
			<c:if test="${data.deleteBtn()}">
				<input type="button" class="s_bt05" value="${e3ps:getMessage('삭제')}" onclick="deleteEco()">
			</c:if>
<%-- 				<c:if test="${isStop eq true}"> --%>
<%-- 					<input type="button" class="s_bt05" value="${e3ps:getMessage('중단')}" onclick="stopEco(true)"> --%>
<%-- 				</c:if> --%>
<%-- 				<c:if test="${isRestart eq true}"> --%>
<%-- 					<input type="button" class="s_bt03" value="${e3ps:getMessage('재시작')}" onclick="stopEco(false)"> --%>
<%-- 				</c:if> --%>
		</div>
	</div>
	<!--//tap -->
	<div class="con pl25 pr25 pb15" id="includePage">
	</div>
	</form>
</div>		
<!-- //pop-->