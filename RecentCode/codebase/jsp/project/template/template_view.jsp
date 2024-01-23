<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="tree_top3 ml10">
	<div class="pro_table mr25 ml25 mb25">
		<div class="seach_arm2 pt5">
			<div class="leftbt">
				<span class="title"><img class="pointer"
					onclick="switchDiv(this);"
					src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('템플릿 기본 정보')}</span>
			</div>
			<div id="total_count" class="rightbt">
				<c:if test="${e3ps:isAdmin() || isManager}">
					<c:if test="${editable}">
						<a href="javascript:editTemplate()">
							<img src="/Windchill/jsp/project/images/img/bt_02.png" alt="${e3ps:getMessage('수정')}" name="leftbtn_011" border="0">
						</a>
					</c:if>
					<a href="javascript:deleteTemplate()">
						<img src="/Windchill/jsp/project/images/img/bt_06.png" alt="${e3ps:getMessage('삭제')}" name="leftbtn_011" border="0">
					</a>
				</c:if>
			</div>
		</div>
		<table class="mainTable mb25">
			<colgroup>
				<col style="width: 20%">
				<col style="width: 30%">
				<col style="width: 20%">
				<col style="width: 30%">
			</colgroup>
			<tr>
				<th>${e3ps:getMessage('템플릿 번호')}</th>
				<td>${root.code}
				</td>
				<th>${e3ps:getMessage('산출물 인증타입')}</th>
				<td>${root.outputDisplay}
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('템플릿 명')}</th>
				<td colspan="3">${root.name}
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('기간')}(${e3ps:getMessage('일')})</th>
				<td>${root.manDay}일
				</td>
				<th>${e3ps:getMessage('등록자')}</th>
				<td>${root.creator}
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('활성화')}</th>
				<td colspan="3">${root.enable == true ? 'Y' : 'N'}</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('최초 등록일')}</th>
				<td>${root.createDate}
				</td>
				<th>${e3ps:getMessage('수정일')}</th>
				<td>${root.modifyDate}
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('설명')}</th>
				<td colspan="3">
					<p>${root.description}</p>
				</td>
			</tr>
		</table>
	</div>
</div>
<script type="text/javascript">
function deleteTemplate(){
	
	openConfirm("${e3ps:getMessage('해당 템플릿을 삭제 하시겠습니까?')}", function(){
		
		var param = new Object();
		
		param.oid = "${root.oid}";
		
		var url = getURLString("/project/template/deleteAction");
		ajaxCallServer(url, param, function(data){
			
		}, true);
	});
}
function editTemplate(){
	var url = getURLString("/project/template/update") + "?oid=${root.oid}";
	
	openPopup(url, "editTemplate", 670, 300);
}
</script>