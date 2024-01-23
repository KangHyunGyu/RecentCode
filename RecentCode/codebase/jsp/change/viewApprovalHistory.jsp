<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="/Windchill/jsp/css/style.css">
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>

<form name="mainForm">
<div class="pro_table mr30 ml30">
	<jsp:include page="${e3ps:getIncludeURLString('/common/include_approveHistory')}" flush="true">
		<jsp:param name="gridHeight" value="200"/>
	</jsp:include>
</div>
</form>