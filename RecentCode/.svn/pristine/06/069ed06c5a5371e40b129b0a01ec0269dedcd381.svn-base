<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<!DOCTYPE html>
<html>
	<head>
		<!-- 메타 데이터 설정 -->
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1, maximum-scale=1, minimum-scale=1" />
		
		<!-- dhtmlx Component -->
		<link rel="stylesheet" type="text/css" href="/Windchill/jsp/component/dhtmlx/dhtmlx.css" />
		<script type="text/javascript" src="/Windchill/jsp/component/dhtmlx/dhtmlx.js" ></script>
		<script type="text/javascript" src="/Windchill/jsp/component/dhtmlx/dhtmlxPaging.js" ></script>

		<!-- JQUERY -->
		<script type="text/javascript" src="/Windchill/jsp/js/jquery/jquery-3.4.1.min.js"></script>
		
		<!-- custom lib -->
		<script type="text/javascript" src="/Windchill/jsp/js/common.js"></script>
		<script type="text/javascript" src="/Windchill/jsp/js/commonAction.js"></script>
		<script type="text/javascript" src="/Windchill/jsp/js/e3ps.js?apply_version=21011801"></script>
		<script type="text/javascript" src="/Windchill/jsp/js/e3psAction.js"></script>
		
		<!-- select2 Component -->
		<link rel="stylesheet" type="text/css" href="/Windchill/jsp/component/select2/css/select2.min.css" />
		<script type="text/javascript" src="/Windchill/jsp/component/select2/js/select2.min.js" ></script>
		
		<title><tiles:insertAttribute name="title" ignore="true" /></title>
	</head>
	<body>
		<div id="progressWindow">
			<div id="progressCenter">
				<img src="/Windchill/jsp/portal/images/loding.gif">
				<br>
				<span id="progressDate"></span>
			</div>
		</div>
		<tiles:insertAttribute name="body" />
	</body>
	<div>
		<tiles:insertAttribute name="footer" ignore="true"/>
	</div>
</html>