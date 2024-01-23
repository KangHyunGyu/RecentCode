<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript">
$(document).ready(function(){
	
let url = window.location.href;

if(url.indexOf("search") != -1){
	document.getElementById('searchPartUrl').style.display = 'block';
}

})
</script>
<div class="sub_menu" id="sub_menu">
	<h2>${e3ps:getMessage('부품 관리')}</h2>
	
	<jsp:include page="${e3ps:getIncludeURLString('/statistics/include_statistics')}" flush="true">
		<jsp:param name="obj" value="part"/>
		<jsp:param name="active" value="true"/>	
		<jsp:param name="today" value="true"/>	
	</jsp:include>
	
	<jsp:include page="${e3ps:getIncludeURLString('/portal/include_menuContents')}" flush="true">
		<jsp:param name="alias" value="part" />
	</jsp:include>

	<%-- <!-- ss_menu -->
	<div class="ss_menu pt20">
		<ul class="line">
			<li>
				<h4><a class="linkMenu" href="javascript:moveLocation('/part/searchPart')">${e3ps:getMessage('부품 검색')}</a></h4>
			</li>
 			<li>
 				<h4><a class="linkMenu" href="javascript:moveLocation('/part/createPart')">${e3ps:getMessage('부품 채번')}</a></h4> 
			</li>
			<li>
				<h4><a class="linkMenu" href="javascript:moveLocation('/part/createRepairPart')">${e3ps:getMessage('Repair 부품 채번')}</a></h4>
			</li>
			<li>
				<h4><a class="linkMenu" href="javascript:moveLocation('/part/createMultiApproval')">${e3ps:getMessage('일괄 결재 등록')}</a></h4>
			</li>
			<li>
				<h4><a class="linkMenu" href="javascript:openBomEditor()">${e3ps:getMessage('BOM 에디터')}</a></h4>
			</li>
			<li>
				<h4><a class="linkMenu" href="javascript:moveLocation('/part/loadBom')">${e3ps:getMessage('BOM 로드')}</a></h4>
			</li>
		</ul>		
	</div>
	<!-- //ss_menu --> --%>
	<!-- tree -->
	<div id="searchPartUrl" style="display: none">
	<jsp:include page="${e3ps:getIncludeURLString('/common/include_folderTree')}" flush="true">
		<jsp:param name="container" value="worldex"/>
		<jsp:param name="renderTo" value="partFolder"/>
		<jsp:param name="formId" value="searchForm"/>
		<jsp:param name="rootLocation" value="/Default/Drawing_Part"/>
		<jsp:param name="gridHeight" value="350"/>
	</jsp:include>
	</div>
	<!-- //tree -->
	
</div>