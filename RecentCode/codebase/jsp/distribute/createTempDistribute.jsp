<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript" src="/Windchill/jsp/js/distribute.js"></script>
<script type="text/javascript">
var isCheckDWG = false;
$(document).ready(function(){
});

<%----------------------------------------------------------
*                      임시 저장 및 등록 버튼
----------------------------------------------------------%>
function save(btn){
	
	if(!checkValidate()) {
		return;
	}
	
	$("#distributeForm").attr("action",getURLString("/distribute/createTempDistributeAction"));
	
	var param = new Object();
	
	var relatedPartList = $$("add_relatedPart_grid_wrap").serialize();
	var relatedEpmList = $$("check_drawing_grid_wrap").serialize();
	
	param["relatedPartList"] = relatedPartList;
	param["relatedEpmList"] = relatedEpmList;
	
	formSubmit("distributeForm", param, "${e3ps:getMessage('등록하시겠습니까?')}", null, true);
	
}
// 필수 여부 체크
function checkValidate() {
	if($("#title").val() == null || $("#title").val() == "") {
		$("#title").focus();
		openNotice("${e3ps:getMessage('배포 제목을 입력하여 주십시오.')}");
		return false;
	}
	
	if(!gridLengthCheck("part")){
		openNotice("${e3ps:getMessage('부품을 선택하세요.')}");
		return false;
	}
	if(!$$("add_relatedPart_grid_wrap").validate()) {
		openNotice("${e3ps:getMessage('추가된 관련 부품에 값을 입력하세요.')}");
		return false;
	}
	/*
	if(!isCheckDWG){
		openNotice("${e3ps:getMessage('부품이 추가/삭제되었습니다. 도면 검증을 진행하세요.')}");
		return false;
	}
	if(!gridLengthCheck("dwg")){
		openNotice("${e3ps:getMessage('한 개 이상의 도면이 있어야 임시등록할 수 있습니다.')}");
		return false;
	}
	$$("check_drawing_grid_wrap").data.each(function(obj){
		if(obj.linkStateStr.length > 1 || obj.linkStateStr != "정상"){
			openNotice("${e3ps:getMessage('배포 상태가 정상인 도면만 임시등록할 수 있습니다.')}");
			return;
		}
	});
	*/
	return true;
}
</script>
<div class="product"> 
<form name="distributeForm" id="distributeForm" method="post">
	<input type="hidden" id="location" name="location" value="/Default/Distribute">
	<!-- button -->
	<div class="seach_arm pt20 pb5">
		<div class="leftbt">
			<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('기본 정보')}</span>
		</div>
		<div class="rightbt">
			<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0"/>
			<button type="button" class="s_bt03" onclick="checkDrawing()">${e3ps:getMessage('검증')}</button>
			<button type="button" class="s_bt03" onclick="save(this)">${e3ps:getMessage('등록')}</button>
		</div>
	</div>
	<!-- //button -->
	<div class="pro_table mr30 ml30">
		<table class="mainTable">
			<colgroup>
				<col style="width:15%">
				<col style="width:35%">
				<col style="width:15%">
				<col style="width:35%">
			</colgroup>	
			<tbody>
				<tr>
					<th>${e3ps:getMessage('배포 제목')}<span class="required">*</span></th>
					<td colspan="3">
						<input type="text" class="w100" id="title" name="title">
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('CAD 타입')}</th>
					<td colspan="3">
						<input type="checkbox" id="cadType" name="cadType" value="CADCOMPONENT">
						<label for="cadType">${e3ps:getMessage('3D')}</label>
					</td>
				</tr>
				<tr>
	               <th>${e3ps:getMessage('비고')}</th>
	               <td class="pd15" colspan="3">
						<div class="textarea_autoSize">
							<textarea name="description" id="description" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
						</div>
					</td>
	            </tr>
			</tbody>
		</table>
	</div>
	<!-- 관련 부품 지정 include 화면 -->
	<div class="ml30 mr30">
		<jsp:include page="${e3ps:getIncludeURLString('/common/include_addObject')}" flush="true">
			<jsp:param name="objType" value="part"/>
			<jsp:param name="pageName" value="relatedPart"/>
			<jsp:param name="title" value="${e3ps:getMessage('관련 부품')}<span class='required'>*</span>"/>
			<jsp:param name="gridHeight" value="200"/>
			<jsp:param name="moduleType" value="distributeTemp"/>
		</jsp:include>
	</div>
	<!-- 도면 검증 -->
	<div class="ml30 mr30">
		<jsp:include page="${e3ps:getIncludeURLString('/distribute/include_checkDrawingList')}" flush="true">
			<jsp:param name="gridHeight" value="500"/>
		</jsp:include>
	</div>
</form>
</div>
