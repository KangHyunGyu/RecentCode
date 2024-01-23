<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="sub_menu" id="sub_menu">
	<h2>${e3ps:getMessage('과거차 관리')}</h2>
	
	<jsp:include page="${e3ps:getIncludeURLString('/statistics/include_statistics')}" flush="true">
		<jsp:param name="obj" value="oldcar"/>
		<jsp:param name="active" value="true"/>	
		<jsp:param name="today" value="true"/>	
	</jsp:include>
	 
	<!-- ss_menu -->
	<div class="ss_menu pt20">
		<ul class="line">
			<li>
				<h4><a class="linkMenu" href="javascript:moveLocation('/oldcar/searchOldcar')">${e3ps:getMessage('과거차 검색')}</a></h4>
			</li>
			<li>
				<h4><a class="linkMenu" href="javascript:moveLocation('/oldcar/createOldcar')">${e3ps:getMessage('과거차 등록')}</a></h4>
			</li>
			<li>
				<h4><a class="linkMenu" href="javascript:moveLocation('/oldcar/chartOldcar')">${e3ps:getMessage('과거차 현황판')}</a></h4>
			</li>
		</ul>		
	</div>
	<!-- //ss_menu -->
</div>