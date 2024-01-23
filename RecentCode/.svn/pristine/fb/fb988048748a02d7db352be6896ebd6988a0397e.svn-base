<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="sub_menu" id="sub_menu">
	<h2>${e3ps:getMessage('도면 관리')}</h2>
	
	<jsp:include page="${e3ps:getIncludeURLString('/statistics/include_statistics')}" flush="true">
		<jsp:param name="obj" value="epm"/>
		<jsp:param name="active" value="true"/>	
		<jsp:param name="today" value="true"/>	
	</jsp:include>
	
	<jsp:include page="${e3ps:getIncludeURLString('/portal/include_menuContents')}" flush="true">
		<jsp:param name="alias" value="epm" />
	</jsp:include>
	 
	<%-- <!-- ss_menu -->
	<div class="ss_menu pt20">
		<ul class="line">
			<li>
				<h4><a class="linkMenu" href="javascript:moveLocation('/epm/searchEpm')">${e3ps:getMessage('도면 검색')}</a></h4>
			</li>
 			<li>
 				<h4><a class="linkMenu" href="javascript:moveLocation('/epm/createEpm')">${e3ps:getMessage('도면 등록')}</a></h4>
 			</li>
<!-- 			<li> -->
				<h4><a class="linkMenu" href="javascript:moveLocation('/epm/createMultiEpm')">${e3ps:getMessage('도면 일괄 등록')}</a></h4>
<!-- 			</li> -->
			<li>
				<h4><a class="linkMenu" href="javascript:moveLocation('/epm/createMultiApproval')">${e3ps:getMessage('일괄 결재 등록')}</a></h4>
			</li>
			<li>
				<h4><a class="linkMenu" href="javascript:moveLocation('/epm/createEpmFolder')">${e3ps:getMessage('폴더 구조 정렬')}</a></h4>
			</li>
<!-- 			<li> -->
				<h4><a class="linkMenu" href="javascript:moveLocation('/epm/searchEpmPartState')">${e3ps:getMessage('도면 부품 상태')}</a></h4>
<!-- 			</li> -->
		</ul>		
	</div>
	<!-- //ss_menu --> --%>
	
	<!-- tree -->
	<jsp:include page="${e3ps:getIncludeURLString('/common/include_folderTree')}" flush="true">
		<jsp:param name="container" value="worldex"/>
		<jsp:param name="renderTo" value="epmFolder"/>
		<jsp:param name="formId" value="searchForm"/>
		<jsp:param name="rootLocation" value="/Default/Drawing_Part"/>
		<jsp:param name="gridHeight" value="350"/>
	</jsp:include>
	<!-- //tree -->
	
</div>