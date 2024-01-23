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
	
	param["instance_id"] = "${data.instance_id}";
	
	$("#includePage").load(url, param);
}
</script>
<!-- pop -->
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('프로젝트')} - ${data.pjt_number}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	<!-- //top -->

	<!--tap -->
	<div class="tap pt20">
		<ul>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/erp/include_detailProjectInfo')}">${e3ps:getMessage('세부 내용')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/erp/include_relatedObject_project')}">${e3ps:getMessage('관련 객체')}</li>
		</ul>
		<div class="tapbutton">
		</div>
	</div>
	<!--//tap -->

	<div class="con pl25 pr25 pb15" id="includePage">
	</div>
</div>		
<!-- //pop-->