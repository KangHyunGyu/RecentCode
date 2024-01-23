<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<!DOCTYPE html>
<html>
	<head>
		<!-- 메타 데이터 설정 -->
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1, maximum-scale=1, minimum-scale=1" />
		
		<!-- JQUERY -->
		<script type="text/javascript" src="/Windchill/jsp/js/jquery/jquery-3.4.1.min.js"></script>
		<!-- WEBIX -->
		<script src="/Windchill/jsp/component/webix/custom.js" type="text/javascript" charset="utf-8"></script>
		<link rel="stylesheet" href="/Windchill/jsp/component/webix/webix.min.css?v=7.4.4" type="text/css">
		<link href="/Windchill/jsp/component/webix/custom.css" rel="stylesheet" type="text/css">
		<script src="/Windchill/jsp/component/webix/webix.min.js?v=7.4.4" type="text/javascript" charset="utf-8"></script>
		
		<!-- AUIGrid Component -->
		<link rel="stylesheet" href="/Windchill/jsp/component/AUIGrid/AUIGrid_style.css">
		<link rel="stylesheet" href="/Windchill/jsp/component/AUIGrid/AUIGrid_custom_style.css">
		<script type="text/javascript" src="/Windchill/jsp/component/AUIGrid/AUIGridLicense.js"></script>
		<script type="text/javascript" src="/Windchill/jsp/component/AUIGrid/AUIGrid.js"></script>
		<script type="text/javascript" src="/Windchill/jsp/component/AUIGrid/pdfkit/AUIGrid.pdfkit.js"></script>
		<script type="text/javascript" src="/Windchill/jsp/component/AUIGrid/AUIGrid_custom.js"></script>
		
		<!-- AXISJ Component -->
		<link rel="stylesheet" type="text/css" href="/Windchill/jsp/component/axisj/ui/bulldog/AXJ.min.css" id="axu-theme-axisj" />
		<script type="text/javascript" src="/Windchill/jsp/component/axisj/dist/AXJ.all.js"></script>
		
		<!-- dhtmlx Component -->
		<link rel="stylesheet" type="text/css" href="/Windchill/jsp/component/dhtmlx/dhtmlx.css" />
		<script type="text/javascript" src="/Windchill/jsp/component/dhtmlx/dhtmlx.js" ></script>
		<script type="text/javascript" src="/Windchill/jsp/component/dhtmlx/dhtmlxPaging.js" ></script>
		
		<!-- select2 Component -->
		<link rel="stylesheet" type="text/css" href="/Windchill/jsp/component/select2/css/select2.min.css" />
		<script type="text/javascript" src="/Windchill/jsp/component/select2/js/select2.min.js" ></script>
		
		<!-- sumoselect Component -->
		<link rel="stylesheet" type="text/css" href="/Windchill/jsp/component/sumoselect/sumoselect.css" />
		<script type="text/javascript" src="/Windchill/jsp/component/sumoselect/jquery.sumoselect.min.js" ></script>
			
		<!-- custom lib -->
		<link rel="stylesheet" type="text/css" href="/Windchill/jsp/css/style.css">
		<script type="text/javascript" src="/Windchill/jsp/js/common.js"></script>
		<script type="text/javascript" src="/Windchill/jsp/js/commonAction.js"></script>
		<script type="text/javascript" src="/Windchill/jsp/js/e3ps.js?apply_version=21011801"></script>
		<script type="text/javascript" src="/Windchill/jsp/js/e3psAction.js"></script>
		
		<script type="text/javascript" src="/Windchill/jsp/js/pview.js?apply_version=23022401""></script>
		<script type="text/javascript" src="/Windchill/jsp/js/pvlaunch.js"></script>

		<title><tiles:insertAttribute name="title" ignore="true" /></title>
	</head>
	<body>
		<!-- body -->
		<tiles:insertAttribute name="body" />
		<!-- //body -->
	</body>
	<div>
		<tiles:insertAttribute name="footer" ignore="true"/>
	</div>
</html>