<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- pop -->
<div class="pop5">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('Template 수정')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>

	<div class="pl25 pr25">
		<div class="seach_arm2 pt10 pb10">
			<div class="leftbt"></div>
			<div class="rightbt">
				<button type="button" class="s_bt03" onclick="javascript:save_template()">${e3ps:getMessage('수정')}</button>
				<button type="button" class="s_bt05" onclick="javascript:window.close()">${e3ps:getMessage('닫기')}</button>
			</div>
		</div>
		<form name="templateForm" id="templateForm" method="post" action="" >
			<input type="hidden" name="oid" value="${template.oid}" />
			<input type="hidden" name="command" value="update" />
			<div class="pro_table">
				<table class="mainTable">
					<colgroup>
						<col style="width:20%">
						<col style="width:80%">
					</colgroup>	
					<tbody>
						<tr>
							<th>${e3ps:getMessage('템플릿 명')}</th>
							<td>
								<input type="text" id="name" name="name" class="w100" value="${template.name}">
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('활성화 여부')} </th>
							<td>
								<input type="checkbox" id="enabled" name="enabled" value="true" ${template.enable == true ? 'checked' : ''}>
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('설명')}</th>
							<td>
								<div class="textarea_autoSize">
									<textarea name="description" id="description" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)">${template.description}</textarea>
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
function save_template(){
	
	if(!checkValidate()) {
		return;
	}
	
	$("#templateForm").attr("action",getURLString("/project/template/updateAction"));
	
	formSubmit("templateForm", null, "${e3ps:getMessage('수정하시겠습니까?')}",function(){
	}, true);
}

function checkValidate() {
	
	if($("#name").val() == null ||$("#name").val().length == 0){
		$("#name").focus();
		openNotice("${e3ps:getMessage('템플릿 명을 입력해 주십시오.')}");
		return false;
	}
	
	return true;
}
</script>