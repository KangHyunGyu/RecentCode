<%@page import="com.e3ps.org.service.PeopleHelper"%>
<%@page import="com.e3ps.org.People"%>
<%@page import="com.e3ps.common.util.StringUtil"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Collection"%>
<%@page import="com.e3ps.common.util.LoginAuthUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<html>
	<head>
	<meta charset="utf-8">	
	<title></title>
	
	<link href="/Windchill/login/css/style.css" rel="stylesheet">
	<link href='http://fonts.googleapis.com/css?family=Roboto:400,300,100,500,700' rel='stylesheet' type='text/css'>
	<script type="text/javascript" src="/Windchill/login/js/jquery/jquery-3.4.1.min.js"></script>
	
	<script type="text/javascript" src="/Windchill/login/js/common.js"></script>
	<script type="text/javascript" src="/Windchill/login/js/e3ps.js?apply_version=21011801"></script>
	
	<title>WORLDEX PLM</title>
</head>

<%
String companyCd = StringUtil.checkNull(request.getParameter("companyCd"));
String empId = StringUtil.checkNull(request.getParameter("empId"));
String apiTime = StringUtil.checkNull(request.getParameter("apiTime"));
String signiture = StringUtil.checkNull(request.getParameter("signiture"));
%>

<script type="text/javascript">
$(document).ready(function() {
	
	$("#loginForm").keypress(function(e){
		if(e.keyCode==13){
			$("#empId").val("");
			login();
		}
	});
	
	
	var empId = $("#empId").val();
	if(empId != ""){
		$("#j_username").val(empId);
		var apiTime = $("#apiTime").val();
		var signiture = $("#signiture").val();
		var companyCd = $("#companyCd").val();
		$.ajax({
			type : "POST",
			url : "/Windchill/login/auidGetPass.jsp?empId="+empId+"&apiTime="+apiTime+"&signiture="+signiture+"&companyCd="+companyCd,
			//url : "/Windchill/worldex/portal/auidGetPass",
			data : {
				/*
				apiTime : $("#apiTime").val(),
				signiture : $("#signiture").val(),
				companyCd : $("#companyCd").val(),
				empId : empId
				*/
			},
			timeout : 30000,
			cache : false,
			dataType : "json",
			traditional : true,
			
			success : function(data) {
				if(data.isChk==true){
					$("#j_password").val(data.pass);
					login();
				}else{
					alert("ID가 잘못되었거나 인증되지 않았습니다.");
				}
			}
		})
	}else{
		
	}
	//getId();
});

function login() {
	$id = $("#j_username");
	$password = $("#j_password");
	
	if($id.val() == "") {
		openNotice("아이디를 입력하세요.");
		$id.focus();
		return;
	}
	
	if($password.val() == "") {
		openNotice("비밀번호를 입력하세요.");
		$password.focus();
		return;
	}

	$id_save = $("#id_save");
	
	if($id_save.prop("checked") == true) {
		setCookie("setID", $id.val(), 9999);
	}else{
		setCookie("setID", $id.val(), -1);
	}
	
	$.ajax({
		type : "POST",
		url : "/Windchill/login/logincheck.jsp",
		data : {
			command : "login",
			id : $id.val(),
			password : $password.val()
		},
		timeout : 30000,
		cache : false,
		dataType : "json",
		traditional : true,
		
		success : function(data) {
			if(data.success){
				$("form").attr("action", "/Windchill/j_security_check").submit();
			}else{
				history.replaceState({}, null, location.pathname);
				location.href = "/Windchill/worldex/portal/main";
				alert(data.message);
				return;
			}
		}
	})
// 	document.getElementById('loginForm').submit();
}

function getId() {
	var val = "";

	$id = $("#j_username");
	$password = $("#j_password");
	$id_save = $("#id_save");
	
	val = getCookie("setID");
	
	if (val != "") {
		$id.val(val);
		$id_save.prop("checked", true);
		$password.focus();
	} else {
		$id_save.prop("checked", false);
		$id.focus();
	}
}
</script>
<body style="height:auto;">
<input type="hidden" id="companyCd" name="companyCd" value="<%=companyCd %>">
<input type="hidden" id="empId" name="empId" value="<%=empId %>">
<input type="hidden" id="apiTime" name="apiTime" value="<%=apiTime %>">
<input type="hidden" id="signiture" name="signiture" value="<%=signiture %>">
<div class="login_arm">
	<div class="arm01">
		<div class="left">Product LifeCycle Management</div>
 		<div class="right"><img src="/Windchill/jsp/portal/images/logo_img.jpg" alt="로고" width="100px"></div>
	</div>
	<div class="line"></div>
	<div class="login">
		 <form id="loginForm" method="post" action="/Windchill/j_security_check">
			<p class="text_c">LOGIN</p>			
			<span id="t" ></span>
			<div class="ax-input">                                    
				<input type="text" name="j_username" id="j_username" placeholder="ID" value="<%=empId %>" style="ime-mode:inactive;">
			</div>
			<div class="ax-input">
				<input type="password" name="j_password" id="j_password" placeholder="PW" >
			</div>	
			
			<!-- 
			<div class="check_style">
				<input class="check_purple" name="id_save" id="id_save" type="checkbox">
				<label for="id_save">ID SAVE</label>
			</div>
			-->
 
			<div class="ax-funcs" >
				<input type="button" id="" class="login_bt"  value="LOGIN" onclick="javascript:login();">				
			</div>
		</form>
	</div>
	<div class="line pt10 pb10"></div>
	<div class="copy">Copyright  ⓒ  2023  <span class="b_text">(C) WORLDEX </span> CO.LTD  All Right Reserved</div>
</div>
</body>
</html>