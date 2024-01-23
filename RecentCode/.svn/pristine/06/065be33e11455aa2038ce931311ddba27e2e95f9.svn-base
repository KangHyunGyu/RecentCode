<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript">
$(document).ready(function(){
});

function changePassword() {
	var newPw = $("#newPw").val();
	var _newPw = $("#_newPw").val();
	var trimnewpw = newPw.replace(/^\s*|\s*$/g,"");
	
	if(newPw != trimnewpw){
		openNotice("${e3ps:getMessage('공백은 입력할 수 없습니다.')}");
		return;
	}
	
	if(newPw != "") {
		if(_newPw.length == 0)	{
			openNotice("${e3ps:getMessage('신규 비밀번호 확인을 입력하세요.')}");
			return;
		}
		
		if(newPw != _newPw)	{
			openNotice("${e3ps:getMessage('비밀번호가 일치하지 않습니다.')}");
			return;
		}
		$("#changePasswordForm").attr("action", getURLString("/workspace/changePasswordAction"));
		formSubmit("changePasswordForm", null, "${e3ps:getMessage('변경하시겠습니까?')}", function(){}, true);
	} else {
		openNotice("${e3ps:getMessage('신규 비밀번호를 입력하세요.')}");
		$("#newPw").focus();
		return;
	}
}
</script>
<div class="pop">
<form name="changePasswordForm" id="changePasswordForm" method="post">
	<input type="hidden" name="id" id="id" value="${id}">
	<input type="hidden" name="isPop" id="isPop" value="${isPop}">
	<!-- button -->
	<div class="seach_arm pt20 pb5">
		<div class="leftbt">
		</div>
		<div class="rightbt">
			<button type="button" id="changeBtn" class="s_bt03" onclick="changePassword();">${e3ps:getMessage('변경')}</button>
		</div>
	</div>
	<!-- //button -->
	<div class="pro_table mr30 ml30">
		<table class="mainTable">
			<colgroup>
				<col style="width:30%">
				<col style="width:70%">
			</colgroup>	
			<tbody>
				<tr>
					<th>ID</th>
					<td>
						${id}
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('이름')}</th>
					<td>
						${name}
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('신규 비밀번호 입력')}<span class="required">*</span></th>
					<td>
						<input type="password" class="w50" id="newPw" name="newPw">
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('신규 비밀번호 확인')}<span class="required">*</span></th>
					<td>
						<input type="password" class="w50" id="_newPw" name="_newPw">
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</form>
</div>