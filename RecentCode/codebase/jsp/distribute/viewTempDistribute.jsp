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
	
	param["oid"] = "${data.oid}";
	
	$("#includePage").load(url, param);
}

function createDistribute(){
	var tab = $(".tap .on").data("url");
	if(tab.indexOf("relatedObjectTemp") == -1){
		openNotice("${e3ps:getMessage('관련 객체에서 부품을 선택하세요.')}");
		return;
	}
	var checkPartList = AUIGrid.getCheckedRowItems(rel_part_myGridID);
	if(checkPartList.length == 0){
		openNotice("${e3ps:getMessage('관련 객체에서 부품을 선택하세요.')}");
		return;
	}
	var oid = "${data.oid}";
	var url = getURLString("/distribute/createDistributePopup?tempOid="+oid);
	openPopup(url, "createDistributePopup", 1124, 600);
}

function getCheckPartList(){
	var checkPartList = AUIGrid.getCheckedRowItems(rel_part_myGridID);
	var partList = new Array();
	
	for(var i = 0; i < checkPartList.length; i++){
		partList.push(checkPartList[i].item);
	}
	return partList;
}
function getTempOid(){
	var tempOid = "${data.oid}";
	return tempOid;
}
</script>
<!-- pop -->
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>
		<img border="0" src="/Windchill/netmarkets/images/doc_document.gif">
		${e3ps:getMessage('비정규 임시')} - ${data.tempNumber}, ${data.title}
		</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	<!-- //top -->
	<!--tap -->
	<div class="tap pt20">
		<ul>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/distribute/include_detailDistributeTemp')}">${e3ps:getMessage('세부 내용')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/distribute/include_relatedObjectTemp')}">${e3ps:getMessage('관련 객체')}</li>
		</ul>
		<div class="tapbutton">
			<button type="button" class="s_bt03" onclick="javascript:createDistribute()">${e3ps:getMessage('배포 등록')}</button>
		</div>
	</div>
	<!--//tap -->

	<div class="con pl25 pr25 pb15" id="includePage">
	</div>
</div>		
<!-- //pop-->