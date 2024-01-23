<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	loadIncludePage();
});
//문서 수정
function modifyDoc() {
	var url = getURLString("/doc/modifyDoc") + "?oid=${doc.oid}";
	
	location.href = url;
}
//문서 삭제
function deleteDoc() {
	openConfirm("${e3ps:getMessage('삭제하시겠습니까?')}", function(){
		
		var param = new Object();
		
		param.oid = "${doc.oid}";
		
		var url = getURLString("/doc/deleteDocAction");
		ajaxCallServer(url, param, function(data){
			if(opener.window.search){
				opener.window.search();				
			}
		}, true);
	});
}
// 문서 폐기
// function discardDoc(){
// 	openConfirm("${e3ps:getMessage('폐기하시겠습니까?')}", function(){
		
// 		var param = new Object();
		
// 		param.oid = "${doc.oid}";
// 		param.appState = "WITHDRAWN";
		
// 		var url = getURLString("/doc/withdrawnDocAction");
// 		ajaxCallServer(url, param, function(data){
// 			if(opener.window.search){
// 				opener.window.search();				
// 			}
// 		}, true);
// 	});
// }
// 문서 개정
function reviseDoc(){
openConfirm("${e3ps:getMessage('개정하시겠습니까?')}", function(){
		
		var param = new Object();
		
		param.oid = "${doc.oid}";
		param.appState = "TEMP_STORAGE";
		
		var url = getURLString("/doc/reviseDocAction");
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
	
	param["oid"] = "${doc.oid}";
	
	$("#includePage").load(url, param);
}
</script>
<!-- pop -->
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>${doc.icon} ${e3ps:getMessage('문서')} - ${doc.number}, ${doc.name}, ${doc.version}</h2>
		<c:if test="${!doc.lastVersionBtn()}">
			<span style="padding-top:5px;padding-left:5px;">
				<button type="button" class="s_bt03" style="height:30px;" onclick="openView('${doc.lastVersionOid()}')">${e3ps:getMessage('최신 버전 보기')}</button>
			</span>
		</c:if>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	<!-- //top -->

	<!--tap -->
	<div class="tap pt20">
		<ul>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/doc/include_detailDoc')}">${e3ps:getMessage('세부 내용')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/doc/include_relatedObject')}">${e3ps:getMessage('관련 객체')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/common/include_versionHistory')}">${e3ps:getMessage('버전 이력')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/common/include_downloadHistory')}">${e3ps:getMessage('다운로드 이력')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/common/include_approveHistory')}">${e3ps:getMessage('결재 이력')}</li>
			<%-- <li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/admin/include_masterAclList')}">${e3ps:getMessage('권한 목록')}</li> --%>
		</ul>
		<div class="tapbutton">
			<c:if test="${docModify eq true || isAdmin eq true}">
				<button type="button" class="s_bt03" onclick="javascript:modifyDoc()">${e3ps:getMessage('수정')}</button>
			</c:if>
<%-- 			<c:if test="${doc.deleteBtn()}"> --%>
			<c:if test="${docModify eq true || isAdmin eq true}">
				<button type="button" class="s_bt03" onclick="javascript:deleteDoc()">${e3ps:getMessage('삭제')}</button>
			</c:if>
<%-- 			<c:if test="${doc.reviseBtn()}"> --%>
			<c:if test="${docApp eq true || isAdmin eq true}">
				<button type="button" class="s_bt03" onclick="javascript:reviseDoc()">${e3ps:getMessage('개정')}</button>
			</c:if>
<%-- 			<c:if test="${doc.recallBtn()}"> --%>
<%-- 				<button type="button" class="s_bt03" onclick="javascript:recallApproval('${doc.oid}')">${e3ps:getMessage('회수')}</button> --%>
<%-- 			</c:if> --%>
<%-- 			<c:if test="${doc.withdrawnBtn()}"> --%>
<%-- 				<button type="button" class="s_bt03" onclick="javascript:discardDoc()">${e3ps:getMessage('폐기')}</button> --%>
<%-- 			</c:if> --%>
		</div>
	</div>
	<!--//tap -->

	<div class="con pl25 pr25 pb15" id="includePage">
	</div>
</div>		
<!-- //pop-->