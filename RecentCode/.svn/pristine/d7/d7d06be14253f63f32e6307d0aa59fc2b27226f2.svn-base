<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	$("#name").data("required", true);
	$("#name").data("message", "${e3ps:getMessage('폴더 명을 입력하세요.')}");
	
});

function modify() {
	
// 	if($.isNumeric($("#sort").val()) == "") {
// 		alert("소트 넘버는 숫자만 입력할 수 있습니다.");
// 		return;
// 	}
	
	$("#docForm").attr("action",getURLString("/doc/modifyDocFolderAction"));
		
	formSubmit("docForm", null, "${e3ps:getMessage('수정하시겠습니까?')}");
}
</script>
<!-- pop -->
<div class="pop5">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('폴더 수정')}</h2>
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
		<form name="docForm" id="docForm" method="post">
			<input type="hidden" name="oid"  id="oid" value="${data.oid}" />
			<div class="pro_table">
				<table class="mainTable">
					<colgroup>
						<col style="width:25%">
						<col style="width:75%">		
					</colgroup>
					<tbody>
						<tr>
							<th scope="col">${e3ps:getMessage('경로')}</th>
							<td><input type="text" class="w50" id="folderLocation" name="folderLocation" value="${data.path}" readonly="readonly"></td>
						</tr>
						<tr>
							<th scope="col">${e3ps:getMessage('기존 폴더 명')}</th>
							<td><input type="text" class="w50" id="olderName" name="olderName" value="${data.name}" readonly="readonly"></td>
						</tr>
						<tr>
							<th scope="col">${e3ps:getMessage('폴더 명')}</th>
							<td><input type="text" class="w50" id="name" name="name" value=""></td>
						</tr>
					</tbody>
				</table>	
			</div>
		</form>	
		<!-- //pro_table -->
	</div>
</div>		
<!-- //pop-->
