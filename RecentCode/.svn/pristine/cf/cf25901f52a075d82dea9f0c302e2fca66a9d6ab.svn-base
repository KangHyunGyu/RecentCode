<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>

<script>
if (!webix.env.touch && webix.env.scrollSize)
	  webix.CustomScroll.init();

var roleTypeList = [];//[{"value":"a","name":"1"}];

var userList = [];

webix.ready(function(){
	
	getApprovalRoleTypeList();
	getUserList();
	app_line_getGridData();
	var appGrid = webix.ui({
		view:"datatable",
		container:"app_line_grid_wrap",
		id:"app_line_grid_wrap",
		areaselect: true,
		resizeColumn:true,
		yCount:4,
		drag:true,
		columns:[
			{ id:"index",   header:"No.",     width:40,	tooltip:false,
				css: "custom_webix_center",		
			},
			{ id:"mark", header:{ content:"masterCheckbox" }, template:"{common.checkbox()}", checkValue:"On", uncheckValue:"Off", width:40},
			{ id:"roleType", header:"${e3ps:getMessage('구분')}", editor:"richselect", collection:roleTypeList,  sort:"string", width:150,
				css:{"color":"#0000FF", "text-align":"center","background-color":"#EBF7FF", "font-weight":"bold"},
			},
			{ id:"name", header:"${e3ps:getMessage('이름')}", editor:"combo", sort:"string", fillspace:true,
				template: '#name#',
				css:{"color":"#0000FF", "text-align":"center","background-color":"#EBF7FF", "font-weight":"bold"},
				options:userList, suggest:{
			        template:'#id#', //template of the input when editor is opened, default
			        filter:function(item,id){ //redefines default webix combo filter
			          if (item.id.toString().toLowerCase().indexOf(id.toLowerCase())===0) return true;
			          return false;
			        },
			        body:{
			          template:'#id#', //template of list items
			          yCount:7, //10 by default
			          on:{
			            onItemClick:function(id, item){
			            	console.log(this.getItem(id).value);
			            },
			          }
			        }
			     }
			},
			{ id:"departmentName", header:"${e3ps:getMessage('부서')}", sort:"string", fillspace:true,
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"duty", header:"${e3ps:getMessage('직위')}", sort:"string", fillspace:true,
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"userId", header:"${e3ps:getMessage('아이디')}", sort:"string", fillspace:true,
				css: "custom_webix_ellipsis custom_webix_center",
			},
		],
		editable:true,
		select:"row",
		scroll:"y",
		sort:"multi",
		hover:"webix_datatable_hover",
		on: {
			onAfterEditStop: app_line_EditStop,
			"data->onStoreUpdated":function(){
	            this.data.each(function(obj, i){
	                obj.index = i+1;
	            });
	        }
		},
	});
		  
	webix.event(window, "resize", function(){ appGrid.adjust(); });
	//getGridData();
});

/**************************************************************
*                     결재 Role  리스트 
****************************************************************/
function getApprovalRoleTypeList() { 
	
	var url	= getURLString("/common/getApprovalRoleTypeList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	for(var i = 0 ; i < data.list.length ; i++){
		if("DRAFT" != data.list[i].key){
			var keyData = data.list[i].key;
			var valueData = data.list[i].value;
			roleTypeList.push({ id : keyData, value : valueData});
		}
	}
}

//user List
function getUserList(oid) { 
	var param = new Object();
	
	param["departmentOid"] = oid;
	
	var url = getURLString("/common/searchUserAction");
	var data = ajaxCallServer(url, param, null);
	
	for(var i = 0 ; i < data.list.length ; i++){
		var keyData = data.list[i].oid;
		var valueData = data.list[i].name;
		userList.push({ id : valueData, value : keyData});
	}
}

//변경한 이름에 대한 정보 Update
function updateUserRow(name, row, afterRoleType, afterRoleName){

	var param = new Object();
	
	param["name"] = name;
	var url = getURLString("/common/searchUserAction");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var selectedItem = data.list;
		selectedItem[0].roleType = afterRoleType;
		selectedItem[0].roleName = afterRoleName;
		$$("app_line_grid_wrap").updateItem(row, selectedItem[0]);
	});
	
}

function app_line_itemEdit(cell, value) {
	var dataField = event.dataField;
}

//이름 변경
function app_line_EditStop(state, editor, ignoreUpdate) {
	const gridList = $$("app_line_grid_wrap").serialize(true);
	var flag = 0;
	var inputValue = state.value;
	
	if(inputValue == ""){
		return;
	}
	
	for(var i = 0; i < gridList.length; i++){
		if(inputValue == gridList[i].name){
			flag++;
		}
	}
	if(flag > 1 && inputValue != ""){
		openNotice("${e3ps:getMessage('이미 추가된 사용자 입니다.')}");
		var item = new Object();
		item["name"] = "";
		item["departmentName"] = "";
		item["duty"] = "";
		item["userId"] = "";
		$$("app_line_grid_wrap").updateItem(editor.row, item);
		return;
		
	}
	
	var editColumn = editor.column;
	var afterRoleType = $$("app_line_grid_wrap").getItem(editor.row).roleType;
	var afterRoleName = $$("app_line_grid_wrap").getItem(editor.row).roleName;
	if(editColumn == "name"){
		updateUserRow(state.value, editor.row, afterRoleType, afterRoleName);//변경한 이름에 대한 정보 Update
	}else{
		return false;
	}
}

function app_line_getGridData(){

	var param = new Object();
	
	param["oid"] = "${appLineOid}";
	var url = getURLString("/approval/getApprovalLine");
	ajaxCallServer(url, param, function(data){
		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		$$("app_line_grid_wrap").parse(gridData);
	});
} 


function addApprovalLinePopup() {

	var url = getURLString("/approval/addApprovalLinePopup") + "?oid=${appLineOid}";
	
	openPopup(url, "addApprovalLinePopup", "1300", "750");
}

//팝업에서 리스트 가져오기
function app_line_getAppLineFromPopup(listStringData) {
	
	var list = new Array();
	var list = JSON.parse(listStringData);
	
	$$("app_line_grid_wrap").clearAll();
	getApprovalRoleTypeList();
	getUserList();
	$$("app_line_grid_wrap").parse(list);
}

//행 추가 버튼
function app_line_addRow() {
	var item = new Object();
	item["name"] = "";
	$$("app_line_grid_wrap").add(item);
	item.index = $$("app_line_grid_wrap").count(); 
	$$("app_line_grid_wrap").updateItem(item.id, item);
	$(':focus').blur();
}

//행 삭제 버튼
function app_line_removeRow() {
	var checkList = $$("app_line_grid_wrap").data.serialize(true);
	for(var i = 0; i < checkList.length; i++){
		if(checkList[i].mark == "On"){
			$$("app_line_grid_wrap").remove(checkList[i].id);
		}
	}
}

//팝업으로 리스트 보내기
function app_line_sendAppLineToPopup() {
	
	var list = $$("app_line_grid_wrap").serialize(true);
	
	return list;
}

 </script>
<!-- button -->
<div class="seach_arm2 pt15 pb5">
	<div class="leftbt">
		<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('결재선 지정')}</span>
		<c:if test="${taskType eq 'CreateTRCR'}">
			<span class="required">*${e3ps:getMessage('결재선 지정 시 품질팀 담당자를 최종 결재자로 지정 바랍니다.')}</span>
		</c:if>
	</div>
	<div class="rightbt">
		<button type="button" class="s_bt03" onclick="app_line_removeRow()">${e3ps:getMessage('삭제')}</button>
		<button type="button" class="s_bt03" onclick="app_line_addRow()">${e3ps:getMessage('추가')}</button>
		<button type="button" class="s_bt03" onclick="addApprovalLinePopup()">${e3ps:getMessage('결재선 추가')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="app_line_grid_wrap" >
</div>