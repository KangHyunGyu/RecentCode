<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
if (!webix.env.touch && webix.env.scrollSize)
	  webix.CustomScroll.init();
	  
webix.ready(function(){
	var checkType = "{common.checkbox()}";
	var checkHeader = { content:"masterCheckbox" };
	var multiSelect = true;
	if("${type}" === "single") {
		checkType = "{common.radio()}";
		checkHeader = "";
		multiSelect = false;
	}
	var grid = webix.ui({
		view:"datatable",
		container:"add_${pageName}_grid_wrap",
		id:"add_${pageName}_grid_wrap",
		select:"row",
		scroll:"xy",
		yCount: 5,
		tooltip:true,
		editable:true,
		autoConfig:false,
		columns:[
			{ id:"index",   header:"No.",     width:40,	tooltip:false,
				css: "custom_webix_center",		
			},
			{ id:"check", header:"", editor:"checkbox",  template:checkType, width:40, tooltip:false,  },
			{ id:"orderNumber", header:[{text:"${e3ps:getMessage('번호')}", content:"excelFilter", mode:"text"}] , width:200,sort:"string"},
			{ id:"name", header:"${e3ps:getMessage('이름')}" ,css:{"text-align":"center"},fillspace:true, sort:"string",
				template:function(obj){
					var oid = obj.oid;
					return "<a href='#' onclick='openView(\""+oid+"\")'>"+obj.name+"</a>";
				}
			},
			{ id:"state", header:"${e3ps:getMessage('상태')}", sort:"string"},
			{ id:"tempcreator", header:"${e3ps:getMessage('등록자')}", sort:"string"},
			{ id:"createDate", header:"${e3ps:getMessage('최초등록일')}", sort:"string"}
		],
		on:{
			onEditorChange:function(cell, value) {
				if("orderNumber" === cell.column) {
					if("" === value) {
						$$("add_${pageName}_grid_wrap").editCancel();
					}
				}
			},
			onBeforeEditStop: function(state, editor, ignoreUpdate){
				if(ignoreUpdate) {
					return;
				}
				
				if("orderNumber" === editor.column) {
					var selectedItem = editor.getPopup().getList().getItem(state.value);
					if (!selectedItem) {
					    return;
					}
					$$("add_${pageName}_grid_wrap").updateItem(editor.row, selectedItem);
				}
			},
			onBeforeEditStart: function(cell){
				if("check" === cell.column) {
					return false;
				}
			},
		    onCheck: function(row, column, state) {
		        if (state) {
		            this.select(row, true);
		        } else {
		            this.unselect(row);
		        }
		    },
		    onBeforeSelect: function(selection, preserve){
		    	var item = $$("add_${pageName}_grid_wrap").getItem(selection.row);
		    	item.check = true;
			},
			onBeforeUnSelect:function(selection){
				var item = $$("add_${pageName}_grid_wrap").getItem(selection.row);
				if(item){
					item.check = false;
				}
			},
			"data->onStoreUpdated":function(){
	            this.data.each(function(obj, i){
	                obj.index = i+1;
	            });
	        }
		}
	});
		  
	webix.event(window, "resize", function(){ grid.adjust(); });
	if("${oid}".length > 0) {
		add_${pageName}_getGridData();
	}
	//getGridData();
});
function add_${pageName}_getGridData(){

	var param = new Object();
	
	param["oid"] = "${oid}";
	param["objType"] = "${objType}";
	param["pageName"] = "${pageName}";
	
	var url = getURLString("/change/getEcrList");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		$$("add_${pageName}_grid_wrap").parse(gridData);
		
	});
}

function add_${pageName}_searchObjectPopup() {

	var url = getURLString("/change/searchObjectPopup2") + "?objType=${objType}&pageName=${pageName}&type=${type}&moduleType=${moduleType}";
	
	openPopup(url, "searchObjectPopup", 1200, 700);
}

//추가 버튼
function add_${pageName}_addRow() {
	
	if("${type}" == "single") {
		if($$("add_${pageName}_grid_wrap").getVisibleCount() > 0) {
			return; 
		}
	}
	var item = new Object();

	$$("add_${pageName}_grid_wrap").add(item);
}

//삭제 버튼
function add_${pageName}_removeRow() {
	$$("add_${pageName}_grid_wrap").remove($$("add_${pageName}_grid_wrap").getSelectedId());
}

function add_${pageName}_addObjectList(list) {
	
	if("${type}" == "single") {
		$$("add_${pageName}_grid_wrap").clearAll();
	} else {
		var gridList = $$("add_${pageName}_grid_wrap").serialize();
		
		for(var i=0; i < gridList.length; i++) {
			list = list.filter(function(item, index, arr){
			    return item.oid != gridList[i].oid;
			});
		}
	}

	for(var i=0; i < list.length; i++) {
		var item = list[i];
		$$("add_${pageName}_grid_wrap").add(item);
	}
	
};

</script>

<!-- button -->
<div class="seach_arm2 pt15 pb5">
	<div class="leftbt">
		<span class="title">
			<c:if test="${toggle}">
				<img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
			</c:if>
			${title}
		</span>
	</div>
	<div class="rightbt">
		<button type="button" class="s_bt03" onclick="add_${pageName}_removeRow()">${e3ps:getMessage('삭제')}</button>
		<button type="button" class="s_bt03" onclick="add_${pageName}_addRow()">${e3ps:getMessage('추가')}</button>
		<button type="button" class="s_bt03" onclick="add_${pageName}_searchObjectPopup()">${e3ps:getMessage('검색 추가')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="add_${pageName}_grid_wrap">
</div>