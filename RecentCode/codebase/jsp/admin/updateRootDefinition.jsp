<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
function updateDefinitionRoot(){
 	if($("#name").val().length == 0){
 		openNotice("${e3ps:getMessage('이름을 입력하세요.')}");
 		return;
 	}
	
	var url = getURLString("/admin/updateRootDefinitionAction");
	
	var param = new Object();
	
	var paramArray = $("#changeForm").serializeArray();
	
	$(paramArray).each(function(idx, obj){
		param[obj.name] = obj.value;
	});
	
	ajaxCallServer(url, param, function(data){
		opener.window.location.reload();	
	}, true);
}
</script>
<form name="changeForm" method="post" id="changeForm">
<input type="hidden" name="oid" value="${oid}">
<div class="seach_arm pt20 pb5">
	<div class="leftbt">
		<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('설계변경 활동 ROOT 수정')}</span>
	</div>
	<div class="rightbt">
		<input onclick="updateDefinitionRoot()" class="s_bt03" type="button" value="저장"> 
		<input onclick="self.close()" class="s_bt05" type="button" value="닫기">
	</div>
</div>

<div class="pro_table mr30 ml30">
		<table class="mainTable">
			<colgroup>
			<col width="150">
			<col width="*">
			</colgroup>
				<tr> 
					<th>이름</th>
					<td>
					<input type="text" name="name" id="name" value="${name}" size="85" class="w50" />
					</td>
				</tr>
				<tr> 
					<th>영문명</th>
					<td>
					<input type="text" name="engName" value="${engName}"  size="85" class="w50" />
					</td>
				</tr>
				<tr>
					<th>Sort</th>
					<td>
					<input type="text" name="sortNumber" value="${sort}"  size="85" class="w50" />
					</td>
				</tr>
				<tr>
					<th>설명</th>
					<td>
						<div class="textarea_autoSize">
							<textarea name="description" id="description" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)">${description}</textarea>
						</div>
					</td>
				</tr>
			</table>	
			</div>				
<!--내용끝-->
</form>