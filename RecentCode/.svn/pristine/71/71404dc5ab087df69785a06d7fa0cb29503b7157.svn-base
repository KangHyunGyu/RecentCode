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
function modifyBenchmarking() {
	var url = getURLString("/benchmarking/modifyBenchmarking") + "?oid=${bm.oid}";
	
	location.href = url;
}

// 과거차 삭제
function deleteBenchmarking() {
	openConfirm("${e3ps:getMessage('삭제하시겠습니까?')}", function(){
		
		var param = new Object();
		
		param.oid = "${bm.oid}";
		
		var url = getURLString("/benchmarking/deleteBenchmarkingAction");
		ajaxCallServer(url, param, function(data){
			if(opener.window.search){
				opener.window.search();				
			}
		}, true);
	});
}

// 과거차 폐기
function discardBenchmarking(){
	openConfirm("${e3ps:getMessage('폐기하시겠습니까?')}", function(){
		
		var param = new Object();
		
		param.oid = "${bm.oid}";
		param.appState = "WITHDRAWN";
		
		var url = getURLString("/benchmarking/withdrawnBenchmarkingAction");
		ajaxCallServer(url, param, function(data){
			if(opener.window.search){
				opener.window.search();				
			}
		}, true);
	});
}

// 과거차 개정
function reviseBenchmarking(){
openConfirm("${e3ps:getMessage('개정하시겠습니까?')}", function(){
		
		var param = new Object();
		
		param.oid = "${bm.oid}";
		param.appState = "TEMP_STORAGE";
		
		var url = getURLString("/benchmarking/reviseBenchmarkingAction");
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
	
	param["oid"] = "${bm.oid}";
	
	$("#includePage").load(url, param);
}
</script>
<!-- pop -->
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>${bm.icon} ${e3ps:getMessage('벤치마킹 상세보기')} - ${bm.number}, ${bm.name}, ${bm.version}</h2>
		<c:if test="${!bm.lastVersionBtn()}">
			<span style="padding-top:5px;padding-left:5px;">
				<button type="button" class="s_bt03" style="height:30px;" onclick="openView('${bm.lastVersionOid()}')">${e3ps:getMessage('최신 버전 보기')}</button>
			</span>
		</c:if>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	<!-- //top -->

	<!--tap -->
	<div class="tap pt20">
		<ul>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/benchmarking/include_detailBenchmarking')}">${e3ps:getMessage('세부 내용')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/benchmarking/include_relatedObject')}">${e3ps:getMessage('관련 객체')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/common/include_versionHistory')}">${e3ps:getMessage('버전 이력')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/common/include_downloadHistory')}">${e3ps:getMessage('다운로드 이력')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/common/include_approveHistory')}">${e3ps:getMessage('결재 이력')}</li>
		</ul>
		<div class="tapbutton">
			<c:if test="${bm.modifyBtn()}">
				<button type="button" class="s_bt03" onclick="javascript:modifyBenchmarking()">${e3ps:getMessage('수정')}</button>
			</c:if>
			<c:if test="${bm.deleteBtn()}">
				<button type="button" class="s_bt03" onclick="javascript:deleteBenchmarking()">${e3ps:getMessage('삭제')}</button>
			</c:if>
			<c:if test="${bm.reviseBtn()}">
				<button type="button" class="s_bt03" onclick="javascript:reviseBenchmarking()">${e3ps:getMessage('개정')}</button>
			</c:if>
			<c:if test="${bm.recallBtn()}">
				<button type="button" class="s_bt03" onclick="javascript:recallApproval('${bm.oid}')">${e3ps:getMessage('회수')}</button>
			</c:if>
			<c:if test="${bm.withdrawnBtn()}">
				<button type="button" class="s_bt03" onclick="javascript:discardBenchmarking()">${e3ps:getMessage('폐기')}</button>
			</c:if>
		</div>
	</div>
	<!--//tap -->

	<div class="con pl25 pr25 pb15" id="includePage">
	</div>
</div>		
<!-- //pop-->