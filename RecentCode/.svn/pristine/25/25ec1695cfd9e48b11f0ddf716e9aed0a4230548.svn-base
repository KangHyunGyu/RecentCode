<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- pop -->
<div class="pop5">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('태스크 기본정보 수정')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->

	<div class="pl25 pr25">
		<div class="seach_arm2 pt10 pb10">
			<div class="leftbt"></div>
			<div class="rightbt">
				<button type="button" class="s_bt03" onclick="javascript:updateTask()">${e3ps:getMessage('수정')}</button>
			</div>
		</div>
		<form name="taskForm" id="taskForm" method="post" action="">
			<input type="hidden" name="oid" value="${task.oid}" />
			<div class="pro_table">
				<table class="mainTable">
					<colgroup>
						<col style="width:20%">
						<col style="width:80%">
					</colgroup>	
					<tbody>
						<tr>
							<th>${e3ps:getMessage('이름')}</th>
							<td>
								<input type="text" name="name" id="name" class="w100" value="${task.name}">
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('설명')}</th>
							<td>
								<div class="textarea_autoSize">
									<textarea name="description" id="description" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)">${task.description}</textarea>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</form>
	</div>
</div>

<script type="text/javascript"> 
function updateTask(){

	if(!checkValidate()) {
		return;
	}
	
	$("#taskForm").attr("action",getURLString("/project/updateTaskAction"));
	
	formSubmit("taskForm", null, "${e3ps:getMessage('수정하시겠습니까?')}",function(data){
		if(opener.window.viewTask) {
			opener.window.viewTask("${oid}");
		}
		if(opener.window.updateTaskData) {
			opener.window.updateTaskData(data.task);
		}
		window.close();
	}, true);
}

function checkValidate() {
	
	if($("#name").val() == null ||$("#name").val().length == 0){
		$("#name").focus();
		openNotice("${e3ps:getMessage('이름을 입력해 주십시오.')}");
		return false;
	}
	
	return true;
}
</script>
