<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="product"> 
<div class="pro_table mr30 ml30">
	<div class="seach_arm2 pt5">
		<div class="leftbt">
			<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
			ECR 상세보기</span>
		</div>
		<div class="rightbt">
		</div> 
	</div>
<jsp:include page="${e3ps:getIncludeURLString('/change/viewECR_include')}" flush="true">
	<jsp:param name="oid" value="${oid }"/>
</jsp:include>
</div>
</div>
