<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	setUserInfo();
	
	//즐겨찾기 삭제
	$(document).on('click', '#fav_delete', function() {
		var oid = $(this).data("oid");
		var param = new Object();
		param["oid"] = oid;
		
		url = getURLString("/favorite/deleteFavoriteAction");
		ajaxCallServer(url, param, function(data){
			getFavoriteList();
			var f_oid = $("#favorite").data("oid");
			if(oid == f_oid){
				$("#favorite").attr("src", "/Windchill/jsp/portal/images/favorites_icon_b.png");
				$("#favorite").data("type", false);
				$("#favorite").data("oid", "");	
			}
		}, false);
	});
});

//get user info
function setUserInfo() {
	
	var url	= getURLString("/portal/getUserInfo");
	
	ajaxCallServer(url,null,function(data){
		$("#userFullName").html(data.userName);
	});
}

//logout
function logout() {
	/* if(window.confirm("LOGOUT ${e3ps:getMessage('하시겠습니까?')}")){
		document.execCommand('ClearAuthenticationCache');
		parent.location.href="/Windchill/login/index.html";
		alert("LOGOUT ${e3ps:getMessage('되었습니다.')}");
	} */
	openConfirm("로그아웃 하시겠습니까?", function(){
		var url = getURLString("/portal/logout");
		ajaxCallServer(url, null, function(data){
		});
	});
}

//sitemap
function sitemap(){
	location.href = getURLString("/portal/sitemap");
}

//즐겨찾기
function favoriteList(btn){
	var offsetLeft = $(btn).prop("offsetLeft");
	var offsetTop = $(btn).prop("offsetTop");
	
	$(".favorites_arm").css("left", offsetLeft-70);
	var type = $("#fav_arrow").data("type");
	
	$(document).click(function (e) {
    	if (!$(".favorites_arm, .favorites").has(e.target).length){
    		favoritesHide();
	    }
	})
	
	if(type){
		favoritesHide();
	}else{
		$("#fav_arrow").attr("src", "/Windchill/jsp/portal/images/arrow_top022.png");
		$("#fav_arrow").data("type", true);
		getFavoriteList();
	}
}
// 즐겨찾기 목록
function getFavoriteList(){
	var url = getURLString("/favorite/getFavoriteList");
	// 즐겨찾기 목록
	ajaxCallServer(url, null, function(data){
		$(".favorites_arm").show();
		$(".favorites_list").empty();
		var list = data.list;
		for(var i=0; i<list.length; i++){
			var html = "<li class='favorites_item'>";
			html += "<a href='"+list[i].url+"'>"+ list[i].name +"</a>"
			html += "<img class='fav_delete pointer hide' id='fav_delete' data-oid='"+list[i].oid+"' src='/Windchill/jsp/portal/images/delete-icon2.png'>";
			html += "</li>";
			$(".favorites_list").append(html);
		}
		if($("#editBtn").text() == "${e3ps:getMessage('완료')}"){
			$(".fav_delete").show();
		}
	}, false);
}
// 즐겨찾기 편집
function favoriteEdit(){
	if($("#editBtn").text() == "${e3ps:getMessage('편집')}"){
		$(".fav_delete").show();
		$("#editBtn").text("${e3ps:getMessage('완료')}");
	}else{
		$(".fav_delete").hide();
		$("#editBtn").text("${e3ps:getMessage('편집')}");
	}
}
//즐겨찾기 hide
function favoritesHide(){
	$("#fav_arrow").attr("src", "/Windchill/jsp/portal/images/arrow_top01.png");
	$("#fav_arrow").data("type", false);
	$(".favorites_arm").hide();
	$(".fav_delete").hide();
	$("#editBtn").text("${e3ps:getMessage('편집')}");
}
</script>
<header id="header">
	<!-- logo -->
	<div class="pull_left">
		<a href="javascript:moveLocation('/portal/main')">
 			<img src="/Windchill/jsp/portal/images/logo_img.jpg" width="100%" height="100%" style="object-fit: cover;"> 
		</a>
	</div>
	<!-- //logo -->
	
	<div class="pull_search">
		<!-- <input type="text">
		<img src="/Windchill/jsp/portal/images/search_icon.png"> -->
	</div>
	
	
	<!-- 공지사항 -->
	<div class="pull_center">  	
	</div>
	<!-- //공지사항 -->
	
	<div class="pull_right">
		<ul>
			<li class="admin"><span id="userFullName"></span></li>			
			<li class="favorites"> <span class="pointer" onclick="javascript:favoriteList(this);">${e3ps:getMessage('즐겨찾기')}<img id="fav_arrow" data-type="false" src="/Windchill/jsp/portal/images/arrow_top01.png" alt="화살표"></span> </li>
<%-- 			<li class="sitemap"> <span class="pointer" onclick="javascript:sitemap();">${e3ps:getMessage('사이트맵')}</span> </li> --%>
			<li class="logout"><span class="pointer" onclick="javascript:logout();">${e3ps:getMessage('로그아웃')}</span></li>
		</ul>
	</div>
</header>
	<!-- 즐겨찾기-->
	<div class="favorites_arm hide">
          <div class="favorites_scroll">
          	<ul class="favorites_list"></ul>
          </div>
          <P><button type="button" class="s_bt" id="editBtn" onclick="javascript:favoriteEdit();">${e3ps:getMessage('편집')}</button>  <P>
	</div>
	<!-- //즐겨찾기-->
