<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	$("#name").data("required", true);
	$("#name").data("message", "${e3ps:getMessage('부서 명을 입력하세요.')}");
	
	$("#code").data("required", true);
	$("#code").data("message", "${e3ps:getMessage('부서 코드를 입력하세요.')}");
	
	$("#sort").data("required", true);
	$("#sort").data("message", "${e3ps:getMessage('소트 넘버를 입력하세요.')}");
	
});

function modify() {
	
	if($.isNumeric($("#sort").val()) == "") {
		alert("소트 넘버는 숫자만 입력할 수 있습니다.");
		return;
	}
	
	$("#departmentForm").attr("action",getURLString("/department/modifyDepartmentAction"));
		
	formSubmit("departmentForm", null, "${e3ps:getMessage('수정하시겠습니까?')}");
}
</script>
<!-- pop -->
<div class="pop5">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('부서 수정')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	<!-- //top -->

	<div class="con on pl25 pr25 pb15" id="tab01">	
		<div class="seach_arm2 pt10 pb10">
			<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('기본 정보')}</h4></div>
			<div class="rightbt">
				<button type="button" class="s_bt03" onclick="javascript:modify()">${e3ps:getMessage('수정')}</button>
			</div>
		</div>
		<!-- pro_table -->
		<form name="departmentForm" id="departmentForm" method="post">
			<input type="hidden" name="oid"  id="oid" value="${department.oid}" />
			<div class="pro_table">
				<table class="mainTable">
					<colgroup>
						<col style="width:25%">
						<col style="width:75%">		
					</colgroup>
					<tbody>
						<tr>
							<th scope="col">${e3ps:getMessage('부서 명')}</th>
							<td><input type="text" class="w50" id="name" name="name" value="${department.name}"></td>
						</tr>
						<tr>
							<th scope="col">${e3ps:getMessage('부서 코드')}</th>
							<td><input type="text" class="w50" id="code" name="code" value="${department.code}"></td>
						</tr>
						<tr>
							<th scope="col">${e3ps:getMessage('소트 넘버')}</th>
							<td><input type="text" class="w50" id="sort" name="sort" value="${department.sort}"></td>
						</tr>
						<tr>
							<th scope="col">${e3ps:getMessage('상위 부서 코드')}</th>
							<td><input type="text" class="w50" id="parentCode" name="parentCode" value="${department.parentCode}"></td>
						</tr>		
					</tbody>
				</table>	
			</div>
		</form>	
		<!-- //pro_table -->
	</div>
</div>		
<!-- //pop-->
