<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- pop -->
<div class="pop5">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('프로젝트 중단')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	
	<div class="pl25 pr25">
		<div class="seach_arm2 pt10 pb5">
			<div class="leftbt"></div>
			<div class="rightbt">
				<button type="button" class="s_bt03" id="searchBtn" onclick="stopProject();">${e3ps:getMessage('중단')}</button>
			</div>
		</div>
		
		<div class="pro_table">
			<form name="editForm" id="editForm" method="post">
				<input type="hidden" id="oid" name="oid" value="${oid}">
				<table class="mainTable">
					<colgroup>
						<col style="width:30%">
						<col style="width:70%">
					</colgroup>	
					<tbody>
						<tr>
							<th>${e3ps:getMessage('중단 사유')}<span class="required">*</span></th>
					    	<td class="pd15">
								<div class="textarea_autoSize">
									<textarea name="stopComment" id="stopComment" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
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
function stopProject() {

	if(!checkValidate()) {
		return;
	}
	
	$("#editForm").attr("action",getURLString("/project/stopProjectAction"));
	
	formSubmit("editForm", null, "${e3ps:getMessage('프로젝트 진행을 중단하시겠습니까?')}",function(){
		if(opener.window.reloadTree) {
			opener.window.reloadTree();
		}
		if(opener.window.viewTask) {
			opener.window.viewProject("${oid}");
		}
		window.close();
	}, true);
}

function checkValidate() {
	
	if($("#stopComment").val() == null ||$("#stopComment").val().length == 0){
		$("#stopComment").focus();
		openNotice("${e3ps:getMessage('중단 사유를 입력해 주십시오.')}");
		return false;
	}
	
	return true;
}
</script>