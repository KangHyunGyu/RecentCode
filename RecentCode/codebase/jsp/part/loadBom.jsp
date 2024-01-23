<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
});

var checkPartFinish = false;
var loadPartFinish = false;
var checkBomFinish = false;

function checkPart(){
	if(!checkPartValidate()) {
		return;
	}
	
	$("#bomLoaderForm").attr("action",getURLString("/bomLoader/checkPartAction"));
	
	var param = new Object();

	formSubmit("bomLoaderForm", param, null, function(data){
		var gridData = data.list;
		
		$$("load_bom_grid_wrap").clearAll();
		$$("load_bom_grid_wrap").parse(gridData);
		
		checkPartFinish = true;
		loadPartFinish = false;
		checkBomFinish = false;
	});
}

function loadPart(){
	if(!loadPartValidate()) {
		return;
	}
	
	$("#bomLoaderForm").attr("action",getURLString("/bomLoader/loadPartAction"));
	
	var param = new Object();
	
	var bomList = $$("load_bom_grid_wrap").data.serialize();
	
	param["bomList"] = bomList;
	
	formSubmit("bomLoaderForm", param, "${e3ps:getMessage('부품을 등록하시겠습니까?')}", function(data) {
		var gridData = data.list;
		
		$$("load_bom_grid_wrap").clearAll();
		$$("load_bom_grid_wrap").parse(gridData);
		
		loadPartFinish = true;
		checkBomFinish = false;
	}, true);
}

function checkBom(){
	if(!checkBomValidate()) {
		return;
	}
	
	$("#bomLoaderForm").attr("action",getURLString("/bomLoader/checkBomAction"));
	
	var param = new Object();
	
	var bomList = $$("load_bom_grid_wrap").data.serialize();
	
	param["bomList"] = bomList;

	formSubmit("bomLoaderForm", param, null, function(data){
		var gridData = data.list;
		
		$$("load_bom_grid_wrap").clearAll();
		$$("load_bom_grid_wrap").parse(gridData);
		
		checkBomFinish = true;
	});
}

function loadBom(){
	if(!loadBomValidate()) {
		return;
	}
	
	$("#bomLoaderForm").attr("action",getURLString("/bomLoader/loadBomAction"));
	
	var param = new Object();
	
	var bomList = $$("load_bom_grid_wrap").data.serialize();
	
	param["bomList"] = bomList;
	
	formSubmit("bomLoaderForm", param, "${e3ps:getMessage('BOM을 등록하시겠습니까?')}", function(data){
		var gridData = data.list;
		
		$$("load_bom_grid_wrap").clearAll();
		$$("load_bom_grid_wrap").parse(gridData);
		
	}, true);
}

function checkPartValidate() {
	
	if($("[id=PRIMARY]").length == 0){
		openNotice("${e3ps:getMessage('BOM 엑셀 파일을 첨부하여 주십시오.')}");
		return false;
	}
	
	return true;
}

function loadPartValidate() {
	
	if($("[id=PRIMARY]").length == 0){
		openNotice("${e3ps:getMessage('BOM 엑셀 파일을 첨부하여 주십시오.')}");
		return false;
	}
	
	if(!checkPartFinish) {
		openNotice("${e3ps:getMessage('부품 검증을 진행하세요.')}");
		return false;
	}
	
	var verification = true;
	$$("load_bom_grid_wrap").data.each(function(obj){
		if(!obj.verification || obj.number.verification === "실패"){
			verification = false;
		}
	});
	
	if(!verification) {
		openNotice("${e3ps:getMessage('검증 실패한 부품이 있습니다.')}");
		return false;
	}
	
	return true;
}

function checkBomValidate() {
	
	if($("[id=PRIMARY]").length == 0){
		openNotice("${e3ps:getMessage('BOM 엑셀 파일을 첨부하여 주십시오.')}");
		return false;
	}
	
	if(!checkPartFinish) {
		openNotice("${e3ps:getMessage('부품 검증을 진행하세요.')}");
		return false;
	}
	
	if(!loadPartFinish) {
		openNotice("${e3ps:getMessage('부품 등록을 진행하세요.')}");
		return false;
	}
	
	
	return true;
}

function loadBomValidate() {
	
	if($("[id=PRIMARY]").length == 0){
		openNotice("${e3ps:getMessage('BOM 엑셀 파일을 첨부하여 주십시오.')}");
		return false;
	}
	
	if(!checkPartFinish) {
		openNotice("${e3ps:getMessage('부품 검증을 진행하세요.')}");
		return false;
	}
	
	if(!loadPartFinish) {
		openNotice("${e3ps:getMessage('부품 등록을 진행하세요.')}");
		return false;
	}
	
	if(!checkBomFinish) {
		openNotice("${e3ps:getMessage('BOM 검증을 진행하세요.')}");
		return false;
	}
	
	var verification = true;
	$$("load_bom_grid_wrap").data.each(function(obj){
		if(!obj.verification || obj.number.verification === "실패"){
			verification = false;
		}
	});
	
	if(!verification) {
		openNotice("${e3ps:getMessage('검증 실패한 부품이 있습니다.')}");
		return false;
	}
	
	return true;
}
</script>
<div class="product">
<form name="bomLoaderForm" id="bomLoaderForm" method="post">
	<!-- button -->
	<div class="seach_arm pt20 pb5">
		<div class="leftbt">
			<h4>${e3ps:getMessage('BOM 로드')}</h4>
		</div>
		<div class="rightbt">
			<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0"/>
			<button type="button" class="s_bt03" onclick="javascript:checkPart();">${e3ps:getMessage('부품 검증')}</button>
			<button type="button" class="s_bt03" onclick="javascript:loadPart();">${e3ps:getMessage('부품 등록')}</button>
			<button type="button" class="s_bt03" onclick="javascript:checkBom();">${e3ps:getMessage('BOM 검증')}</button>
			<button type="button" class="s_bt03" onclick="javascript:loadBom();">${e3ps:getMessage('BOM 등록')}</button>
		</div>
	</div>
	<!-- //button -->
	<div class="pro_table mr30 ml30">
		<table class="mainTable">
			<colgroup>
				<col style="width:20%">
				<col style="width:80%">
			</colgroup>	
			<tbody>
				<tr>
					<th scope="col">${e3ps:getMessage('BOM 엑셀 파일')}<a href="/Windchill/excelTemplate/bomExcelTemplate.xls">&nbsp;<img src="/Windchill/jsp/portal/icon/fileicon/xls.gif" title="${e3ps:getMessage('템플릿 다운로드')}" border="0"></a></th>
					<td>
						<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
							<jsp:param name="formId" value="bomLoaderForm"/>
							<jsp:param name="command" value="insert"/>
							<jsp:param name="type" value="PRIMARY"/>
							<jsp:param name="btnId" value="createBtn" />
							<jsp:param name="moduleType" value="bomLoad" />
						</jsp:include>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<div class="ml30 mr30">
		<jsp:include page="${e3ps:getIncludeURLString('/part/include_loadBomList')}" flush="true"/>
	</div>
</form>
</div>