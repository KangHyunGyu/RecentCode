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
	
	param["instance_id"] = "${receipt.instance_id}";
	
	$("#includePage").load(url, param);
}

// 접수 확인 - CPC 접수 상태값 변경
function saveSubmitOK(){
	$("#form").attr("action", getURLString("/distribute/saveSubmitOK"));
	var param = new Object();
	formSubmit("form", param, "${e3ps:getMessage('접수를 확인 하시겠습니까?')}",function(){ 
		if(opener.window.getGridData) {
			opener.window.getGridData();
		}
	}, true);
}
</script>
<!-- pop -->
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>
		<img border="0" src="/Windchill/netmarkets/images/doc_document.gif">
		${e3ps:getMessage('접수')} - ${receipt.submit_number}, ${receipt.submit_name}
		</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	<!-- //top -->

	<!--tap -->
	<div class="tap pt20">
		<ul>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/distribute/include_detailReceipt')}">${e3ps:getMessage('세부 내용')}</li>
		</ul>
		<div class="tapbutton">
			<c:if test="${receipt.submitBtn()}"> <!-- 접수중 상태일 때만 보임 -->
				<button type="button" class="s_bt03" onclick="saveSubmitOK()">${e3ps:getMessage('접수 확인')}</button>
				<form name="form" id="form" style="display: none;">
					<input type="hidden" name="instance_id" value="${receipt.instance_id}">
				</form>
			</c:if>
		</div>
	</div>
	<!--//tap -->

	<div class="con pl25 pr25 pb15" id="includePage">
	</div>
</div>		
<!-- //pop-->