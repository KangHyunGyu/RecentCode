<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
});

function modifyDepartment() {
	var url = getURLString("/department/modifyDepartment") + "?oid=${department.oid}";
	
	location.href = url;
}

function deleteDepartment() {
	openConfirm("${e3ps:getMessage('삭제하시겠습니까?')}", function(){
		
		var param = new Object();
		
		param["oid"] = "${department.oid}";
		param["isPopup"] = true;
		
		var url = getURLString("/department/deleteDepartmentAction");
		ajaxCallServer(url, param, function(data){
			
		});
	});
}
</script>
<!-- pop -->
<div class="pop5">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('부서 정보')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	<!-- //top -->

	<div class="con on pl25 pr25 pb15" id="tab01">	
		<div class="seach_arm2 pt10 pb10">
			<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('기본 정보')}</h4></div>
			<div class="rightbt">
				<button type="button" class="s_bt03" onclick="javascript:modifyDepartment()">${e3ps:getMessage('수정')}</button>
				<button type="button" class="s_bt03" onclick="javascript:deleteDepartment()">${e3ps:getMessage('삭제')}</button>
			</div>
		</div>
		<!-- pro_table -->
		<div class="pro_table">
			<table class="mainTable">
				<colgroup>
					<col style="width:25%">
					<col style="width:75%">		
				</colgroup>
				<tbody>
					<tr>
						<th scope="col">${e3ps:getMessage('부서 명')}</th>
						<td>${department.name}</td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('부서 코드')}</th>
						<td>${department.code}</td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('소트 넘버')}</th>
						<td>${department.sort}</td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('상위 부서 코드')}</th>
						<td>${department.parentCode}</td>
					</tr>		
				</tbody>
			</table>	
		</div>
		<!-- //pro_table -->
	</div>
</div>		
<!-- //pop-->
