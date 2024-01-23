<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
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
	
	param["oid"] = "${part.oid}";
	
	$("#includePage").load(url, param);
}
function sendBom() {
	openConfirm("${e3ps:getMessage('전송하시겠습니까?')}", function(){
		var param = new Object();
		param.oid = "${part.oid}";
		var url = getURLString("/bom/sendBom");
		ajaxCallServer(url, param, function(data){
			
		}, true);
	});
}
</script>
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>${part.icon} ${e3ps:getMessage('부품')} - ${part.number}, ${part.name}, ${part.version}</h2>
		<c:if test="${!part.lastVersionBtn()}">
			<span style="padding-top:5px;padding-left:5px;">
				<button type="button" class="s_bt03" style="height:30px;" onclick="openBomTree('${part.lastVersionOid()}')">${e3ps:getMessage('최신 버전 보기')}</button>
			</span>
		</c:if>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	<!-- //top -->

	<!--tap -->
	<div class="tap pt20">
		<ul>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/bom/include_bomTree')}">BOM</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/bom/include_bomItemList')}">BOM ITEM</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/bom/include_bomPartList')}" data-param='{"type":"up"}'>${e3ps:getMessage('상위 부품')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/bom/include_bomPartList')}" data-param='{"type":"down"}'>${e3ps:getMessage('하위 부품')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/bom/include_bomPartList')}" data-param='{"type":"end"}'>END ITEM</li>
		</ul>
		<div class="tapbutton">
			<button type="button" class="s_bt03" onclick="openBomEditor('${part.oid}');">BOM 편집</button>
<!-- 			<button type="button" class="s_bt03" onclick="sendBom();">BOM 전송</button> -->
		</div>
	</div>
	<!--//tap -->
	
	<div class="con pl25 pr25 pb15" id="includePage">
	</div>
</div>