<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="product">
	<!-- button -->
	<div class="seach_arm pt15 pb5">
		<div class="leftbt">
			<select id="codeType" name="codeType" onchange="changeCodeType(this);">
				<option value="SG">SG코드</option>
			</select>
		</div>
		<div class="rightbt">
			<button type="button" class="s_bt03" id="searchBtn" onclick="search();">${e3ps:getMessage('검색')}</button>
			<button type="button" class="s_bt05" id="resetBtn" onclick="reset();">${e3ps:getMessage('초기화')}</button>
		</div>
	</div>
	<!-- //button -->
	
	<div class="semi_content pl30 pr30">
		<div class="semi_table1 pr20" id="codeTypeTree">
			<jsp:include page="${e3ps:getIncludeURLString('/admin/include_sgCodeTypeTree')}" flush="true">
				<jsp:param name="autoGridHeight" value="false"/>
				<jsp:param name="gridHeight" value="600"/>
			</jsp:include>
		</div>
		<div class="semi_content2">
			<!-- pro_table -->
			<div class="semi_table2">
			<form name="searchForm" id="searchForm">
			<input type="hidden" name="codeTypeValue" id="codeTypeValue" value="">
				<table summary="">
					<caption></caption>
					<colgroup>
						<col style="width:11%">
						<col style="width:22%">
						<col style="width:11%">
						<col style="width:22%">
						<col style="width:11%">
						<col style="width:23%">
					</colgroup>
					
					<tbody>
						<tr>
							<th scope="col">${e3ps:getMessage('코드')}</th>
							<td><input type="text" id="code" name="code" class="w100" /></td>
							<th scope="col">${e3ps:getMessage('이름')}</th>
							<td><input type="text" id="name" name="name" class="w100" /></td>
							<th scope="col">${e3ps:getMessage('영문 명')}</th>
							<td><input type="text" id="engName" name="engName" class="w100" /></td>
						</tr>
					</tbody>
				</table>
			</form>
			</div>
			<!-- //pro_table -->
			<!-- button -->
			<div class="seach_arm2 pt10 pb5">
				<div class="leftbt">${e3ps:getMessage('검색결과')} (<span id="total">0</span>)</div>
				<div class="rightbt">
					<button type="button" class="s_bt03" onclick="excelDown('searchForm', 'excelDownNumberCode');">${e3ps:getMessage('엑셀 다운로드')}</button>
					<%--<img class="pointer mb5" onclick="excelDown('searchForm', 'excelDownNumberCode');" src="/Windchill/jsp/portal/icon/fileicon/xls.gif" border="0">
					<button type="button" class="s_bt03" onclick="xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button> --%>
					<button type="button" class="s_bt03" onclick="addRow()">${e3ps:getMessage('추가')}</button>
					<button type="button" class="s_bt03" onclick="removeRow()">${e3ps:getMessage('삭제')}</button>
					<button type="button" class="s_bt03" onclick="save()">${e3ps:getMessage('저장')}</button>
				</div>
			</div>
			<!-- //button -->
<!-- 			<div class="list" id="grid_wrap" style="height:500px; border-top:2px solid #1064aa;"></div> -->
			<div class="list" id="grid_wrap" style="height:500px; border-top:2px solid #74AF2A;"></div>
		</div>
	</div>
</div>
<script type="text/javascript">
if (!webix.env.touch && webix.env.scrollSize)
	  webix.CustomScroll.init();
webix.ready(function(){
	var grid = webix.ui({
		view:"datatable",
		container:"grid_wrap",
		id:"grid_wrap",
		clipboard:"block",
		multiselect:true,
		select:"cell",
		dragColumn: true,
		scroll:"xy",
		sort:"multi",
		yCount: 13,
		leftSplit:4,
		tooltip:true,
		resizeColumn:true,
		datafetch:50,
		columns:[
			{ id:"index",   header:"No.", width:40,	tooltip:false,
				css: "custom_webix_center",		
			},
			{ id:"mark", header:{ content:"masterCheckbox" }, 
				template:"{common.checkbox()}", checkValue:"On", uncheckValue:"Off", width:40
			},
			{ id:"code", header:["${e3ps:getMessage('코드')}"],	width:140, editor:"text", sort:"string",
				css: "webix_non_editable custom_webix_ellipsis custom_webix_center",
				editParse: function(value){  
					var gridList = $$("grid_wrap").serialize(true);
					var selectItem = $$("grid_wrap").getEditor();
		        	var codeFlagInt = 0;
		        	var codeFlagInt2 = 0;
		        	
		        	if(selectItem.value == value){
		        		codeFlagInt2 = 1;
		        	}else{
		        		codeFlagInt2 = 0;
		        	}
		        	
		        	if(value == ""){
		        		text = "필수 필드는 반드시 값을 입력해야 합니다.";
		        		webix.message({ type:"error", text:text }); 
		        		return;
		        	}else{
		        		for(var i = 0; i < gridList.length; i++){
			        		if(gridList[i].code == value){
			        			++codeFlagInt;
			        		};
			        	}
			        	if( codeFlagInt > codeFlagInt2 ){
				            text = value + "값은 고유값이 아닙니다.(이미 존재함)다른 값을 입력해 주십시오.";
				            webix.message({ type:"error", text:text }); 
			        		return;
			        	} 
		        	}
		        	return value;
				}
			},
			{ id:"name", header:["${e3ps:getMessage('이름')}"] , sort:"string", fillspace:true, editor:"text",
				css: "custom_webix_ellipsis custom_webix_center",
				editParse: function(value){  
					var gridList = $$("grid_wrap").serialize(true);
					var selectItem = $$("grid_wrap").getEditor();
					var nameFlagInt = 0;
					var nameFlagInt2 = 0;
		        	//var findString = "/";
		        	
		        	if(selectItem.value == value){
		        		nameFlagInt2 = 1;
		        	}else{
		        		nameFlagInt2 = 0;
		        	}
		        	
					if(value==""){
		        		text = "필수 필드는 반드시 값을 입력해야 합니다.";
		        		webix.message({ type:"error", text:text }); 
		        		return;
		        	}else{
			        	for(var i = 0; i < gridList.length; i++){
			        		if(gridList[i].name == value){
			        			++nameFlagInt;
			        		};
			        	}
			        	if( nameFlagInt > nameFlagInt2 ){
				            text = value + "값은 고유값이 아닙니다.(이미 존재함)다른 값을 입력해 주십시오.";
				            webix.message({ type:"error", text:text }); 
				            return;
			        	} 
		        	}
		        	/* if(value.indexOf(findString) != -1) {
		        		text = "/(슬래시)는 입력할 수 없습니다.";
		        		webix.message({ type:"error", text:text }); 
		        		return;
		        	} */	
		        	return value;
				}
			},
			{ id:"description", header:"${e3ps:getMessage('설명')}", minWidth:150, fillspace:true, editor:"text",sort:"string",
			},
			{ id:"sort", header:"${e3ps:getMessage('소트')}", width:100, editor:"text",sort:"int",
				css: "custom_webix_ellipsis custom_webix_center",
				editParse: function(value){  
					var numCheck = (value)%2;
		        	if(value < 0){
		        		text = "소트는 자연수만 입력이 가능합니다.";
			            webix.message({ type:"error", text:text }); 
			        	return;
		        	}
		        	if( numCheck != 1 && numCheck != 0 ){
		        		text = "소트는 자연수만 입력이 가능합니다.";
			            webix.message({ type:"error", text:text }); 
			        	return;
		        	}
		        	return value;
			    },
			},
			{ id:"active", header:"${e3ps:getMessage('사용 여부')}",
				template:custom_checkbox, width:150, editor:"inline-checkbox"
			},
		],
	    editable:true,
	    checkboxRefresh:true,
	    on:{
	    	//Header Click
			onHeaderClick:function(header, event, target) {
				var column = $$("grid_wrap").getColumnConfig(header.column);
				
				if("server" === column.sort) {
					$("#sessionId").val("");
				}
			},
			
			onAfterEditStart: onAfterEditStart,
			//No.
	        "data->onStoreUpdated":function(){
	            this.data.each(function(obj, i){
	                obj.index = i+1;
	            });
	        },
	    },
	    hover:"webix_datatable_hover",
	    editaction:"dblclick",
		scroll:"y",
		sort:"multi"
	});
	webix.event(window, "resize", function(){ grid.adjust(); });
	
	$(".webix_excel_filter").click(function(){
		 $$("grid_wrap").unselect(); 
	});

	getGridData();
});

$(document).ready(function(){
	//enter key pressed event
	$("#searchForm").keypress(function(e){
		if(e.keyCode==13){
			search();
		}
	});
	
	//initial setting
	changeCodeType();
	
	//get grid data
	getGridData();
});

//삭제된 아이템들
var removeList = new Array();

var gridCount = 0;

function custom_checkbox(obj, common, value){
    if (value)
      return "<div class='webix_table_checkbox webix_datatable_checked'> YES </div>";
    else
      return "<div class='webix_table_checkbox webix_datatable_notchecked'> NO </div>";
};

function getGridData() {
	
	$("#searchForm").attr("action", getURLString("/admin/getSGCodeList"));

	var param = new Object();
	
	param["codeType"] = $("#codeType").val();
	param["parentCode"] = $("#parentCode").val();
	
	formSubmit("searchForm", param, null, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		$("#total").html(gridData.length);
		
		// 그리드에 데이터 세팅
		$$("grid_wrap").clearAll();
		$$("grid_wrap").parse(gridData);
		gridCount = $$("grid_wrap").count(); 
	});
}

//검색
function search(){
	getGridData();
}

function xlsxExport() {
	webix.toExcel($$("grid_wrap"), {
	   	filename: "xlsxExport",
	   	rawValues:true,
	});
}

//Code Type 변경
function changeCodeType() {
	$("#codeTypeTree").show();
	
	getCodeTypeTree();	
	
	$("#parentCode").val("");
	
	getGridData();
	
	$("#codeTypeValue").val($("#codeType").val());
}

//추가 버튼
function addRow() {
	var item = new Object();
	
	var lastItemId = $$("grid_wrap").getLastId();
	var lastItem = $$("grid_wrap").getItem(lastItemId);
	
	if(lastItem != null) {
		item["sort"] = lastItem.sort + 1;
	} else {
		item["sort"] = 1;
	}
	
	item["active"] = true;
	item["newItem"] = true;
	
	$$("grid_wrap").add(item);
	item.index = $$("grid_wrap").count(); 
	$$("grid_wrap").updateItem(item.id, item);
	$$("grid_wrap").showCell(item.id,"code");
	
}

//삭제 버튼
function removeRow() {

	var checkList = $$("grid_wrap").data.serialize(true);
	for(var i = 0; i < checkList.length; i++){
		if(checkList[i].mark == "On"){
			$$("grid_wrap").remove(checkList[i].id);
			if(checkList[i].oid != null){
				removeList.push(checkList[i]);
				gridCount = gridCount - 1 ;
			}
		}
	}
}

//저장 버튼
function save() {
	$$("grid_wrap").editStop();
	var codeValid = true;
	$$("grid_wrap").data.each(function(obj){
		if(!obj.code || obj.code.length === 0){
			codeValid = false;
		}
	});
	
	if(!codeValid) {
		//$("#grid_wrap")[0].scrollIntoView();
		openNotice("${e3ps:getMessage('필수 필드는 반드시 값을 입력해야 합니다.')}");
		return;
	}
	
	var nameValid = true;
	$$("grid_wrap").data.each(function(obj){
		if(!obj.name || obj.name.length === 0){
			nameValid = false;
		}
	});
	
	if(!nameValid) {
		//$("#grid_wrap")[0].scrollIntoView();
		openNotice("${e3ps:getMessage('필수 필드는 반드시 값을 입력해야 합니다.')}");
		return;
	}
	
	//grid
	var editedItemList = $$("grid_wrap").serialize(true);
	
	//삭제된 아이템들
	var removedItemList = removeList;
	
	//중복 확인
	for(var i=0; i < editedItemList.length - 1; i++) {
		for(var j=i+1; j < editedItemList.length; j++) {
			if(editedItemList[i].code == editedItemList[j].code) {
				openNotice("중복되는 코드가 있습니다.");
				return;
			}
		}
	}
	
	var param = new Object();
	
	param["codeType"] = $("#codeType").val();
	param["parentCode"] = $("#parentCode").val();
	param["parentOid"] = $("#parentOid").val();
	
	param["editedItemList"] = editedItemList;
	param["removedItemList"] = removeList;
	
	var url = getURLString("/admin/saveNumberCodeAction2");
	ajaxCallServer(url, param, function(data){
		removeList.length = 0;
		getCodeTypeTree();	
		getGridData();
	}); 
}

//검색조건 초기화
function reset(){
	$("#searchForm")[0].reset();
}

function onAfterEditStart(cell) {
	var id = cell.row;
	var column = cell.column;
	var item = $$("grid_wrap").getItem(id);
	var codeValue = item.code;
	
	if(codeValue && item.newItem != true){
		if(column == "code"){
			$$("grid_wrap").editStop();
		}
	}
	
}
</script>