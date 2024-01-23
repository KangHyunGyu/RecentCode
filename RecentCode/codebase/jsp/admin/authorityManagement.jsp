<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript">
document.addEventListener("DOMContentLoaded", (evt) => {
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
		
		//param["oid"] = "${part.oid}";
		
		$("#includePage").load(url, param);
	}	
</script>
<div class="product">
	<!-- button -->
	<input type="hidden" id="authGroupId" name="authGroupId" value=""/>
	<div class="tap pt20">
		<ul>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/admin/include_authorityByMenu')}">${e3ps:getMessage('메뉴별 권한')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/admin/include_authorityByFolder')}">${e3ps:getMessage('폴더별 권한')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/admin/include_authorityByObject')}">${e3ps:getMessage('객체별 권한')}</li>
		</ul>
		<div class="tapbutton">
			<%-- <button type="button" class="s_bt03" onclick="openBomEditor('${part.oid}');">BOM 편집</button>
			<button type="button" class="s_bt03" onclick="openBomTree('${part.oid}');">BOM</button> --%>
		</div>
	</div>
	
	<div class="con" id="includePage">
	</div>
</div>