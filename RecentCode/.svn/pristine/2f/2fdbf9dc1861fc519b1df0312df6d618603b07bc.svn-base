<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){

	loadIncludePage();
});

function modifyEpm() {
	var url = getURLString("/epm/modifyEpm") + "?oid=${epm.oid}";
	
	location.href = url;
}

function deleteEpm() {
	openConfirm("${e3ps:getMessage('삭제하시겠습니까?')}", function(){
		
		var param = new Object();
		
		param.oid = "${epm.oid}";
		
		var url = getURLString("/epm/deleteEpmAction");
		ajaxCallServer(url, param, function(data){
			if(opener.window.search){
				opener.window.search();				
			}
		});
	});
}

function reviseEpm() {
	openConfirm("${e3ps:getMessage('개정하시겠습니까?')}", function(){
		
		var param = new Object();
		
		param.oid = "${epm.oid}";
		
		var url = getURLString("/epm/reviseEpmAction");
		ajaxCallServer(url, param, function(data){
			if(opener.window.search){
				opener.window.search();				
			}
		}, true);
	});
}

function withdrawEpm() {
	openConfirm("${e3ps:getMessage('폐기하시겠습니까?')}", function(){
		
		var param = new Object();
		
		param.oid = "${epm.oid}";
		
		var url = getURLString("/epm/withdrawEpmAction");
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
	
	param["oid"] = "${epm.oid}";
	
	$("#includePage").load(url, param);
}

function createCADBOMPopup() {
	
	var url = getURLString("/epm/createCADBOMPopup") + "?oid=${epm.oid}";
	
	openPopup(url, "createCADBOMPopup_${epm.oid}", 1000, 600);
}
</script>
<!-- pop -->
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>${epm.icon} ${e3ps:getMessage('도면')} - ${epm.number}, ${epm.name}, ${epm.version}</h2>
		<c:if test="${!epm.lastVersionBtn()}">
			<span style="padding-top:5px;padding-left:5px;">
				<button type="button" class="s_bt03" style="height:30px;" onclick="openView('${epm.lastVersionOid()}')">${e3ps:getMessage('최신 버전 보기')}</button>
			</span>
		</c:if>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	<!-- //top -->

	<!--tap -->
	<div class="tap pt20">
		<ul>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/epm/include_detailEpm')}">${e3ps:getMessage('세부 내용')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/epm/include_relatedObject')}">${e3ps:getMessage('관련 객체')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/common/include_versionHistory')}">${e3ps:getMessage('버전 이력')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/common/include_downloadHistory')}">${e3ps:getMessage('다운로드 이력')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/common/include_approveHistory')}">${e3ps:getMessage('결재 이력')}</li>
			<%-- <li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/admin/include_masterAclList')}">${e3ps:getMessage('권한 목록')}</li> --%>
		</ul>
		<div class="tapbutton">
				<!-- <button type="button" class="s_bt03" onclick="javascript:createCADBOMPopup();">${e3ps:getMessage('CAD BOM 체크')}</button>  -->
			<c:if test="${!isOwnerPart && !epm.isWGM()}">
				<c:if test="${epm.modifyBtn()}">
					<button type="button" class="s_bt03" onclick="javascript:modifyEpm();">${e3ps:getMessage('수정')}</button>
				</c:if>
				<c:if test="${epm.deleteBtn()}">
					<button type="button" class="s_bt03" onclick="javascript:deleteEpm();">${e3ps:getMessage('삭제')}</button>
				</c:if>
				<c:if test="${epm.reviseBtn()}">
					<button type="button" class="s_bt03" onclick="javascript:reviseEpm();">${e3ps:getMessage('개정')}</button>
				</c:if>
				<c:if test="${epm.recallBtn()}">
					<button type="button" class="s_bt03" onclick="javascript:recallApproval('${epm.oid}')">${e3ps:getMessage('회수')}</button>
				</c:if>
				<c:if test="${epm.withdrawnBtn()}">
					<button type="button" class="s_bt03" onclick="javascript:withdrawEpm();">${e3ps:getMessage('폐기')}</button>
				</c:if>
			</c:if>
		</div>
	</div>
	<!--//tap -->

	<div class="con pl25 pr25 pb15" id="includePage">
	</div>
	
</div>		
<!-- //pop-->