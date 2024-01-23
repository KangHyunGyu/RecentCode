<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	getNumberCodeList("step", "EOSTEP", true, true);
	getNumberCodeList("activeType", "EOACTIVETYPE", true, true);
	$("#activeType").val("${activeType}");
	$("#step").val("${step}");
});
function updateDefinition(){
	if($("#name").val().length == 0){
 		openNotice("${e3ps:getMessage('이름을 입력하세요.')}");
 		return;
 	}
	
	if($("#step").val().length == 0){
 		openNotice("${e3ps:getMessage('단계를 선택하세요.')}");
 		return;
 	}
	
	if($("#activeType").val().length == 0){
 		openNotice("${e3ps:getMessage('활동구분을 선택하세요.')}");
 		return;
 	}
	
	var url = getURLString("/admin/updateDefinitionAction");
	
	var param = new Object();
	
	var paramArray = $("#changeForm").serializeArray();
	
	$(paramArray).each(function(idx, obj){
		param[obj.name] = obj.value;
	});
	
	ajaxCallServer(url, param, function(data){
		if(opener.window.reGrid){
			opener.window.reGrid("${rootOid}");	
		}
	}, true);
}
</script>
<form name="changeForm" id="changeForm" method="post">
<input type="hidden" name="oid" value="${oid}">
<div class="seach_arm pt20 pb5">
	<div class="leftbt">
		<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('설계변경 활동 수정')}</span>
	</div>
	<div class="rightbt">
		<input onclick="updateDefinition()" class="s_bt03" type="button" value="저장"> 
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
					<input type="text" id="name" name="name" value="${name}" size="85" class="w50"/>
					</td>
				</tr>
				<tr>
					<th>영문명</th>
					<td>
					<input type="text" id="engName" name="engName"  value="${name_eng}" size="85" class="w50"/>
					</td>
				</tr>
				<tr> 
					<th>단계</th>
					<td>
						<select class="w30" id="step" name="step">
						</select>
					</td>
				</tr>
				<tr>
					<th>활동구분</th>
					<td>
						<select class="w30" id="activeType" name="activeType">
						</select>
					</td>
				</tr>
				<tr>
					<th>담당자</th>
					<td>
						<div class="pro_view">
							<select class="searchUser" id="userId" name="userId" multiple data-width="70%">
							</select>
							<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('userId', 'multi');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
							<span class="pointer verticalMiddle" onclick="javascript:deleteUser('userId');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
						</div>
					</td>
				</tr>
				<tr>
					<th>Sort</th>
					<td>
					<input type="text" id="sortNumber" name="sortNumber" value="${sortNumber}"  size="85" class="input01" style="width:98%" />
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