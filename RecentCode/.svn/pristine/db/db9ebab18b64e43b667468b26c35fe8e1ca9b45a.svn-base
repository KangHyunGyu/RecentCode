<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	loadIncludePage();
	
});

// 과거차 수정
function modifyOldcar() {
	var url = getURLString("/oldcar/modifyOldcar") + "?oid=${oldcar.oid}";
	
	location.href = url;
}

// 과거차 삭제
function deleteOldcar() {
	openConfirm("${e3ps:getMessage('삭제하시겠습니까?')}", function(){
		
		var param = new Object();
		
		param.oid = "${oldcar.oid}";
		
		var url = getURLString("/oldcar/deleteOldcarAction");
		ajaxCallServer(url, param, function(data){
			if(opener.window.search){
				opener.window.search();				
			}
		}, true);
	});
}

// 과거차 폐기
function discardOldcar(){
	openConfirm("${e3ps:getMessage('폐기하시겠습니까?')}", function(){
		
		var param = new Object();
		
		param.oid = "${oldcar.oid}";
		param.appState = "WITHDRAWN";
		
		var url = getURLString("/oldcar/withdrawnOldcarAction");
		ajaxCallServer(url, param, function(data){
			if(opener.window.search){
				opener.window.search();				
			}
		}, true);
	});
}

// 과거차 개정
function reviseOldcar(){
openConfirm("${e3ps:getMessage('개정하시겠습니까?')}", function(){
		
		var param = new Object();
		
		param.oid = "${oldcar.oid}";
		param.appState = "TEMP_STORAGE";
		
		var url = getURLString("/oldcar/reviseOldcarAction");
		ajaxCallServer(url, param, function(data){
			if(opener.window.search){
				opener.window.search();				
			}
		}, true);
	});
}
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
	
	param["oid"] = "${oldcar.oid}";
	
	$("#includePage").load(url, param);
}
</script>
<!-- pop -->
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>${oldcar.icon} ${e3ps:getMessage('과거차 문제점')} - ${oldcar.oldcarNumber}, ${oldcar.oldcarName}, ${oldcar.version}</h2>
		<c:if test="${!oldcar.lastVersionBtn()}">
			<span style="padding-top:5px;padding-left:5px;">
				<button type="button" class="s_bt03" style="height:30px;" onclick="openView('${oldcar.lastVersionOid()}')">${e3ps:getMessage('최신 버전 보기')}</button>
			</span>
		</c:if>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	<!-- //top -->

	<!--tap -->
	<div class="tap pt20">
		<ul>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/oldcar/include_detailOldcar')}">${e3ps:getMessage('세부 내용')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/oldcar/include_relatedObject')}">${e3ps:getMessage('관련 객체')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/common/include_versionHistory')}">${e3ps:getMessage('버전 이력')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/common/include_downloadHistory')}">${e3ps:getMessage('다운로드 이력')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/common/include_approveHistory')}">${e3ps:getMessage('결재 이력')}</li>
		</ul>
		<div class="tapbutton">
			<c:if test="${oldcar.modifyBtn()}">
				<button type="button" class="s_bt03" onclick="javascript:modifyOldcar()">${e3ps:getMessage('수정')}</button>
			</c:if>
			<c:if test="${oldcar.deleteBtn()}">
				<button type="button" class="s_bt03" onclick="javascript:deleteOldcar()">${e3ps:getMessage('삭제')}</button>
			</c:if>
			<c:if test="${oldcar.reviseBtn()}">
				<button type="button" class="s_bt03" onclick="javascript:reviseOldcar()">${e3ps:getMessage('개정')}</button>
			</c:if>
			<c:if test="${oldcar.recallBtn()}">
				<button type="button" class="s_bt03" onclick="javascript:recallApproval('${oldcar.oid}')">${e3ps:getMessage('회수')}</button>
			</c:if>
			<c:if test="${oldcar.withdrawnBtn()}">
				<button type="button" class="s_bt03" onclick="javascript:discardBenchmarking()">${e3ps:getMessage('폐기')}</button>
			</c:if>
		</div>
	</div>
	<!--//tap -->

	<div class="con pl25 pr25 pb15" id="includePage">
	</div>
</div>		
<!-- //pop-->