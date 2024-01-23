<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="sub_menu" id="sub_menu">
	<h2>${e3ps:getMessage('설계 변경')}</h2>
	
	<jsp:include page="${e3ps:getIncludeURLString('/statistics/include_statistics')}" flush="true">
		<jsp:param name="obj" value="eco"/>
		<jsp:param name="active" value="true"/>	
		<jsp:param name="today" value="true"/>	
	</jsp:include>
	 
	<jsp:include page="${e3ps:getIncludeURLString('/portal/include_menuContents')}" flush="true">
		<jsp:param name="alias" value="change" />
	</jsp:include>
	
</div>