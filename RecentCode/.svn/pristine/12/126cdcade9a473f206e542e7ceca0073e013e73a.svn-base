<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	loadIncludePage();
});

function modifyMulti() {
	var url = getURLString("/multi/modifyMulti") + "?oid=${multi.oid}&objectType=${multi.objectType}";
	
	location.href = url;
}

function deleteMulti() {
	openConfirm("${e3ps:getMessage('삭제하시겠습니까?')}", function(){
		
		var param = new Object();
		
		param.oid = "${multi.oid}";
		
		var url = getURLString("/multi/deleteMultiAction");
		ajaxCallServer(url, param, function(data){
			if(opener.getGridData){
				opener.getGridData();				
			}
		});
	});
}

function loadIncludePage(tab) {
	
	if(tab == null) {
		tab = $(".tap>ul>li:first");
	}
	
	$(".tap ul li").removeClass("on");
	
	$(tab).addClass("on");
	
	var url = $(tab).data("url");
	
	var param = new Object();
	
	param["oid"] = "${multi.oid}";
	
	$("#includePage").load(url, param);
}
</script>
<!-- pop -->
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2> ${e3ps:getMessage('일괄결재')} - ${multi.number}, ${multi.name}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	<!-- //top -->

	<!--tap -->
	<div class="tap pt20">
		<ul>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/multi/include_detailMulti')}">${e3ps:getMessage('세부 내용')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/multi/include_relatedObject')}">${e3ps:getMessage('관련 객체')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/common/include_downloadHistory')}">${e3ps:getMessage('다운로드 이력')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/common/include_approveHistory')}">${e3ps:getMessage('결재 이력')}</li>
		</ul>
		<div class="tapbutton">
			<c:if test="${multi.modifyBtn()}">
				<button type="button" class="s_bt03" onclick="javascript:modifyMulti()">${e3ps:getMessage('수정')}</button>
			</c:if>
			<%-- <c:if test="${multi.recallBtn()}">
				<button type="button" class="s_bt03" onclick="javascript:recallApproval('${multi.oid}')">${e3ps:getMessage('회수')}</button>
			</c:if> --%>
			<c:if test="${multi.deleteBtn()}">
				<button type="button" class="s_bt03" onclick="javascript:deleteMulti()">${e3ps:getMessage('삭제')}</button>
			</c:if>
		</div>
	</div>
	<!--//tap -->
	
	<div class="con pl25 pr25 pb15" id="includePage">
	</div>
</div>
<!-- //pop-->
	
