<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- pop -->
<div class="pop5">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('프로젝트 기본정보 수정')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	
	<div class="pl25 pr25">
		<div class="seach_arm2 pt10 pb5">
			<div class="leftbt"></div>
			<div class="rightbt">
				<button type="button" class="s_bt03" onclick="updateProject();">${e3ps:getMessage('수정')}</button>
			</div>
		</div>
		
		<div class="pro_table">
			<form name="editForm" id="editForm" method="post">
				<input type="hidden" id="oid" name="oid" value="${oid}">
				<table class="mainTable">
					<colgroup>
						<col style="width:16%">
						<col style="width:35%">
						<col style="width:15%">
						<col style="width:35%">
					</colgroup>	
					<tbody>
						<tr>
							<th>${e3ps:getMessage('프로젝트 번호')}</th>
							<td colspan="3">
								${project.code}
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('프로젝트 명')}<span class="required">*</span></th>
							<td colspan="3">
								<input type="text" id="name" name="name" value="${project.name }">
							</td>
						</tr>
<!-- 						<tr> -->
<%-- 						<th scope="col">${e3ps:getMessage('개발구분')}<span class="required">*</span></th> --%>
<!-- 						<td> -->
<!-- 							<select class="w100" id="devType" name="devType"></select> -->
<!-- 						</td> -->
<%-- 						<th scope="col">${e3ps:getMessage('용도')}<span class="required">*</span></th> --%>
<!-- 						<td> -->
<!-- 							<select class="w100" id="purpose" name="purpose"></select> -->
<!-- 						</td> -->
<!-- 						</tr> -->
<!-- 						<tr> -->
<%-- 							<th scope="col">${e3ps:getMessage('고객사')}<span class="required">*</span></th> --%>
<!-- 							<td> -->
<!-- 								<select class="w100" id="customer" name="customer"></select> -->
<!-- 							</td> -->
<!-- 							<th scope="col">END ITEM<span class="required">*</span></th> -->
<!-- 							<td> -->
<!-- 								<select class="w100" id="endItem" name="endItem"></select> -->
<!-- 							</td> -->
<!-- 						</tr> -->
						<tr>
							<th>${e3ps:getMessage('설명')}</th>
							<td class="pd15" colspan="3">
								<div class="textarea_autoSize">
									<textarea name="description" id="description" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)">${project.description}</textarea>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	
});
function updateProject(){
	
	if(!checkValidate()) {
		return;
	}
	
	$("#editForm").attr("action",getURLString("/project/updateProjectAction"));
	
	formSubmit("editForm", null, "${e3ps:getMessage('프로젝트 기본 정보를 수정하시겠습니까?')}",function(){
		if(opener.window.reloadTree) {
			opener.window.reloadTree();
		}
		if(opener.window.viewProject) {
			opener.window.viewProject("${oid}");
		}
		window.close();
	}, true);
}

function checkValidate() {
	
	if($("#name").val() == null ||$("#name").val().length == 0){
		$("#name").focus();
		openNotice("${e3ps:getMessage('프로젝트 명을 선택하세요.')}");
		return false;
	}
	
// 	if($("#devType").val() == null || $("#devType").val() == "") {
// 		$("#devType").focus();
// 		openNotice("${e3ps:getMessage('개발 구분을 선택하세요.')}");
// 		return false;
// 	}
	
// 	if($("#purpose").val() == null || $("#purpose").val() == "") {
// 		$("#purpose").focus();
// 		openNotice("${e3ps:getMessage('용도를 선택하세요.')}");
// 		return false;
// 	}
	
// 	if($("#customer").val() == null || $("#customer").val() == "") {
// 		$("#customer").focus();
// 		openNotice("${e3ps:getMessage('고객사를 선택하세요.')}");
// 		return false;
// 	}
	
// 	if($("#endItem").val() == null || $("#endItem").val() == "") {
// 		$("#endItem").focus();
// 		openNotice("${e3ps:getMessage('END ITEM을 선택하세요.')}");
// 		return false;
// 	}
	
	return true;
}
</script>