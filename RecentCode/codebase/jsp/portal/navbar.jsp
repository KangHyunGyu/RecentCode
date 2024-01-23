<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="navbar">
	<img onclick="JavaScript:switchMenu()" id="subMenuArrow" src="/Windchill/jsp/portal/images/s_menu_icon03.png" class="pointer">
	<img src="/Windchill/jsp/portal/images/home_icon.png"> <span id="menuLocation" style="font-weight:bold"></span>
</div>