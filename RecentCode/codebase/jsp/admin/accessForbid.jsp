<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1, maximum-scale=1, minimum-scale=1" />
		<script type="text/javascript" src="/Windchill/jsp/js/common.js"></script>
		<script type="text/javascript" src="/Windchill/jsp/js/e3ps.js"></script>
		<script>
		window.addEventListener('DOMContentLoaded', function(){
			
			alert("${e3ps:getMessage('접근 권한이 금지된 URL 입니다')}.");
			history.back();
			//location.href = getURLString("/portal/main");
		})	
		</script>
	</head>
	<body>
	</body>
</html>