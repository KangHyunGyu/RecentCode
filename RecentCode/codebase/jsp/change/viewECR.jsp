<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script src="/Windchill/jsp/js/popup.js"></script>
<script type=text/javascript>
$(document).ready(function(){
	
	loadIncludePage();
// 	console.log("${oid}");
// 	console.log("${data.oid}");
	
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
	param.type = "ECR";
	
	$("#includePage").load(url, param);
}
function modifyEcr() {
	var url = getURLString("/change/updateECR") + "?oid=${oid}";
	location.href = url;
}
function deleteEcr() {
	openConfirm("${e3ps:getMessage('삭제하시겠습니까?')}", function(){
		var param = new Object();
		param.oid = "${data.oid}";
		var url = getURLString("/change/deleteECRAction");
		ajaxCallServer(url, param, function(data){
			if(opener.window.search){
				opener.window.search();				
			}
		}, true);
	});
    
}

function createECO(){
	var url = getURLString("/change/createECO");
	opener.window.location.href = url + "?ecrOid="+$("#oid").val();
	window.open("about:blank", "_self");
	window.close();
}
</script>
<!-- pop -->
<div class="pop">
	<form name="changeForm" method="post">
	<input type="hidden" id="oid" name="oid" value="${data.oid}">
	<!-- top -->
	<div class="top">
		<h2>${data.icon} ${e3ps:getMessage('ECR')} - ${data.ecrNumber}, ${data.name}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	<!-- //top -->

	<!--tap -->
	<div class="tap pt20">
		<ul>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/change/include_detailECR')}">${e3ps:getMessage('세부 내용')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/change/include_relatedObject')}">${e3ps:getMessage('관련 객체')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/common/include_approveHistory')}">${e3ps:getMessage('결재 이력')}</li>
			<%-- <li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/change/include_activity')}">${e3ps:getMessage('단계 활동')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/common/include_downloadHistory')}">${e3ps:getMessage('다운로드 이력')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/common/include_approveHistory')}">${e3ps:getMessage('결재 이력')}</li> --%>
		</ul>
		<div class="tapbutton">
		
			<c:if test="${data.isCreateECO()}">
				<input type="button" class="s_bt03" value="${e3ps:getMessage('ECO 등록')}" onclick="createECO()">
			</c:if>
			
			<c:if test="${data.modifyBtn()}">
				<input type="button" class="s_bt03" value="${e3ps:getMessage('수정')}" onclick="modifyEcr()">
			</c:if>
			<c:if test="${data.deleteBtn()}">
				<input type="button" class="s_bt05" value="${e3ps:getMessage('삭제')}" onclick="deleteEcr()">
			</c:if>
		</div>
	</div>
	<!--//tap -->
	<div class="con pl25 pr25 pb15" id="includePage">
	</div>
	</form>
</div>		
<!-- //pop-->
