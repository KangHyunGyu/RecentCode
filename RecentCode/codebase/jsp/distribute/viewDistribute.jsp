<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){

	loadIncludePage();
	
});


function modifyDistribute() {
	var url = getURLString("/distribute/modifyDistribute") + "?oid=${oid}";
	location.href = url;
}

function deleteDistribute() {
	openConfirm("${e3ps:getMessage('삭제하시겠습니까?')}", function(){
		
		var param = new Object();
		
		param.oid = "${oid}";
		
		var url = getURLString("/distribute/deleteDistributeAction");
		ajaxCallServer(url, param, function(data){
			if(opener.window.search){
				opener.window.search();				
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
	var param = $(tab).data("param");
	
	if(param == null) {
		param = new Object();
	}
	param["oid"] = "${oid}";
	
	$("#includePage").load(url, param);
}
</script>
<!-- pop -->
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('도면 출도 의뢰서')} - ${distribute.distNumber}, ${distribute.distName} </h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	<!-- //top -->

	<!--tap -->
	<div class="tap pt20">
		<ul>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/distribute/include_detailDistribute')}">${e3ps:getMessage('상세보기')}</li>
			<%-- <li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/distribute/include_relatedObject')}">${e3ps:getMessage('관련 객체')}</li> --%>
			<%-- <li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/purchase/include_erpHistory')}">${e3ps:getMessage('ERP 이력')}</li> --%>
			<%-- <li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/distribute/include_epmOfDistributePartList')}">${e3ps:getMessage('도면')}</li> --%>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/common/include_approveHistory')}">${e3ps:getMessage('결재 이력')}</li>
		</ul>
		<div class="tapbutton">
			<c:if test="${distribute.modifyBtn()}">
				<button type="button" class="i_update" style="width:70px" onclick="javascript:modifyDistribute();">${e3ps:getMessage('수정')}</button>
			</c:if>	
			<c:if test="${distribute.deleteBtn()}">
				<button type="button" class="i_delete" style="width:70px"onclick="javascript:deleteDistribute();">${e3ps:getMessage('삭제')}</button>
			</c:if>	
		</div>
	</div>
	<!--//tap -->

	<div class="con pl25 pr25 pb15" id="includePage">
	</div>
	
</div>		
<!-- //pop-->