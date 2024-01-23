<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="pop">
	<!-- button -->
	<div class="seach_arm pt15 pb5">
		<div class="leftbt">
			<select id="codeType" name="codeType">
				<option value="CLASSIFICATION">CLASSIFICATION</option>
			</select>
		</div>
		<div class="rightbt">
			<button type="button" class="s_bt03" id="searchBtn" onclick="search();">${e3ps:getMessage('검색')}</button>
			<button type="button" class="s_bt05" id="resetBtn" onclick="reset();">${e3ps:getMessage('초기화')}</button>
		</div>
	</div>
	<!-- //button -->
	
	<div class="semi_content pl30 pr30">
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
					<button type="button" class="s_bt03" onclick="selectCode()">${e3ps:getMessage('추가')}</button>
				</div>
			</div>
			<!-- //button -->
<!-- 			<div class="list" id="code_grid_wrap" style="height:500px; border-top:2px solid #1064aa;"></div> -->
			<div class="list" id="code_grid_wrap" style="height:500px; border-top:2px solid #74AF2A;"></div>
		</div>
	</div>
</div>
<script type="text/javascript">
if (!webix.env.touch && webix.env.scrollSize)
	  webix.CustomScroll.init();
webix.ready(function(){
	var grid = webix.ui({
		view:"datatable",
		container:"code_grid_wrap",
		id:"code_grid_wrap",
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
				template:"{common.radio()}", checkValue:"On", uncheckValue:"Off", width:40
			},
			{ id:"code", header:["${e3ps:getMessage('코드')}"],	width:100, sort:"string",
				css: "webix_non_editable custom_webix_ellipsis custom_webix_center",
			},
			{ id:"name", header:["${e3ps:getMessage('이름')}"] , sort:"string", width:150,
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"engName", header:"${e3ps:getMessage('영문 명')}", width:200,sort:"string",
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"description", header:"${e3ps:getMessage('설명')}", minWidth:150, fillspace:true, sort:"string",
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"sort", header:"${e3ps:getMessage('소트')}", width:100, sort:"int",
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"active", header:"${e3ps:getMessage('사용 여부')}",
				css: "custom_webix_ellipsis custom_webix_center",
			},
		],
	    editable:true,
	    checkboxRefresh:true,
	    on:{
	    	//Header Click
			onHeaderClick:function(header, event, target) {
				var column = $$("code_grid_wrap").getColumnConfig(header.column);
				
				if("server" === column.sort) {
					$("#sessionId").val("");
				}
			},
			
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
		 $$("code_grid_wrap").unselect(); 
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
	
	$("#searchForm").attr("action", getURLString("/admin/getNumberCodeList"));

	var param = new Object();
	param["codeType"] = $("#codeType").val();
	param["parentCode"] = $("#parentCode").val();
	
	formSubmit("searchForm", param, null, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		$("#total").html(gridData.length);
		
		// 그리드에 데이터 세팅
		$$("code_grid_wrap").clearAll();
		$$("code_grid_wrap").parse(gridData);
		gridCount = $$("code_grid_wrap").count(); 
	});
}

//검색
function search(){
	getGridData();
}

function selectCode(){
	var selectedItem = new Array();
	var checkList = $$("code_grid_wrap").data.serialize(true);
	for(var i=0; i<checkList.length; i++) {
		if(checkList[i].mark == "On") {
			selectedItem.push(checkList[i]);
		}
	}
	var listStringData = JSON.stringify(selectedItem);
	if(opener.window.addClassification){
		opener.window.addClassification(selectedItem);
	}
	window.close();
	
}
</script>