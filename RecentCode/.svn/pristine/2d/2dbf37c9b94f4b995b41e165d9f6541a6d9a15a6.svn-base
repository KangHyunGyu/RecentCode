<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<%@page import="com.e3ps.common.util.TypeUtil"%>
<!-- pop -->
<div class="pop3">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('폴더 검색')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->
	<div class="seach_arm2 pt10 pb5">
		<div class="leftbt"></div>
		<div class="rightbt">
			<button class="s_bt03" onclick="">${e3ps:getMessage('선택')}</button>
		</div>
	</div>

	<div class="pl25 pr25">
		<form name="mainForm" method="post">
			<!-- tree -->
			<jsp:include page="${e3ps:getIncludeURLString('/common/include_folderTree')}" flush="true">
				<jsp:param name="container" value="${container}"/>
				<jsp:param name="renderTo" value="${renderTo}"/>
				<jsp:param name="formId" value="${formId}"/>
				<jsp:param name="rootLocation" value="${rootLocation}"/>
				<jsp:param name="gridWidth" value="450"/>
			</jsp:include>
			<!-- //tree -->
		</form>
	</div>
</div>
<script>
</script>
