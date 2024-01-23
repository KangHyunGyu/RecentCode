<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="pop_left_2 mt10">
	<div class="left pr10" style="float: left;">
		<div class="seach_arm2 pt10 pb5">
			<div class="leftbt">${e3ps:getMessage('문서 분류별 속성')}</div>
			<div class="rightbt">
				<button type="button" class="s_bt03 mr10" id="saveDefinition" onclick="saveDefinition()">${e3ps:getMessage('저장')}</button>
			</div>
		</div>
		<div class="docCodeToValue_grid" id="docCodeToValue_grid" style="height:300px"></div>
	</div>
	<div class="left pr10" style="float: right;">
		<div class="seach_arm2 pt10 pb5">
			<div class="leftbt" style="margin-bottom: 5px;">${e3ps:getMessage('문서 속성')}(<span id="totalType">0</span>)</div>
			<%-- <div class="rightbt">
				<button type="button" class="s_bt03 mr10" id="saveDocValueDefinition" onclick="saveDocValueDefinition()">${e3ps:getMessage('속성 저장')}</button>
			</div> --%>
		</div>
		<div class="docValue_grid" id="docValue_grid" style="height:300px"></div>
	</div>
</div>
<script>
webix.ready(function(){
	var inputTypeList = ["TEXT", "SELECT", "DATE"];
	
	var docCodeToValue_grid = webix.ui({
		view:"datatable",
		container : "docCodeToValue_grid",
		id:"docCodeToValue_grid",
		columns:[
			{ id:"index",   header:"No.",     width:40,	tooltip:false,
				css: "custom_webix_center",		
			},
			{ id:"active", header:"${e3ps:getMessage('사용 여부')}",
				template:"{common.checkbox()}", width:110,
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"code", header:"${e3ps:getMessage('코드')}" , minWidth:80, fillspace:true,
				css: "custom_webix_center",	
			},
			{ id:"name", header:"${e3ps:getMessage('이름')}", width:110,
				css: "custom_webix_center",	
			},
			{ id:"inputType", header:"${e3ps:getMessage('입력방식')}", width:110,
				css: "custom_webix_center",	
			}
		],
		hover:"webix_datatable_hover",
		drag:true, 
		on: {
			//onItemClick: ${roleType}_line_itemClick,
			"data->onStoreUpdated":function(){
	            this.data.each(function(obj, i){
	                obj.index = i+1;
	            });
	        },
	        onAfterDrop : docCodeToValueAfterDrop_Handler,
	        onBeforeDrop : docCodeToValueBeforeDrop_Handler,
		}
	});
	
	var docValue_grid = webix.ui({
		view:"datatable",
		container : "docValue_grid",
		id:"docValue_grid",
		columns:[
			{ id:"index",   header:"No.",     width:40,	tooltip:false,
				css: "custom_webix_center",		
			},
			{ id:"code", header:"${e3ps:getMessage('코드')}" , width:110,
				css: "custom_webix_center",	
			},
			{ id:"name", header:"${e3ps:getMessage('이름')}" , minWidth:80, fillspace:true,
				css: "custom_webix_center",	
			},
			{ id:"inputType", header:"${e3ps:getMessage('입력방식')}", width:200,  editor:"richselect", collection:inputTypeList,  sort:"string",
				css:{"color":"#0000FF", "text-align":"center","background-color":"#EBF7FF", "font-weight":"bold"},
			},
		],
		hover:"webix_datatable_hover",
		drag:true, 
		editable:false,
		on: {
			//onItemClick: ${roleType}_line_itemClick,
			"data->onStoreUpdated":function(){
	            this.data.each(function(obj, i){
	                obj.index = i+1;
	            });
	        },
	        onAfterDrop : docCodeToValueAfterDrop_Handler,
		}
	});
});

$(document).ready(function(){
	
	//gridData 불러오기
	getDocValueData();
	
});

//문서 속성 불러오기
function getDocValueData(){
	
	var url = getURLString("/admin/searchDocValueAction");
	ajaxCallServer(url, null, function(data){
		// 그리드 데이터
		var gridData = data.list;
		$("#totalType").html(gridData.length);
		
		// 그리드에 데이터 세팅
		$$("docValue_grid").clearAll();
		$$("docValue_grid").parse(gridData);
	});
}

function docCodeToValueBeforeDrop_Handler(context, native_event) {
	var start_id = context.start; // 드래깅 되고 있는 첫번째 행
	let item = $$("docValue_grid").getItem(start_id);
	var oid = item.oid;
	var name = item.name;
	var code = item.code;
	var inputType = item.inputType

	if(name == null || name == "" || inputType == null || inputType == "") {
		openNotice("이름이나 입력방식이 정의되지 않았습니다.");
		return false;
	}
	
	item["active"] = true;

	var notHave = true;
	var docCodeToValueList = $$("docCodeToValue_grid").serialize(true);

	for(var i=0; i < docCodeToValueList.length; i++){
		if(docCodeToValueList[i].code == code){
			notHave = false;
		}
	}
	
	if(!notHave) {
		openNotice("이미 문서 분류별 속성에 정의되어있는 속성입니다.");
		return false; // 기본 행위 안함.
	}
	
	return true;
}

//docValue 드래드 앤 드랍 핸들러
function docCodeToValueAfterDrop_Handler(context, native_event) {
	getDocValueData();
}

//저장 버튼
function saveDefinition(docTypeOid) {
	//그리드 리스트
	var gridList = $$("docCodeToValue_grid").serialize(true);
	
	//중복 확인
	for(var i=0; i < gridList.length - 1; i++) {
		for(var j=i+1; j < gridList.length; j++) {
			if(gridList[i].code == gridList[j].code) {
				openNotice("중복되는 코드가 있습니다.");
				return;
			}
		}
	}
	
	var param = new Object();
	param["gridList"] = gridList;
	param["docTypeOid"] = docTypeOid;
	
	if(docTypeOid == null){
		openNotice("문서 타입을 선택해주세요.");
		return;
	}
	
	var url = getURLString("/admin/saveDocCodeToValueAction");
	ajaxCallServer(url, param, function(data){
		var doc=data.docTypeOid;
	});
}

/* 
function saveDocValueDefinition() {
	//그리드 리스트
	var gridList = $$("docValue_grid").serialize(true);
	
	var param = new Object();
	param["gridList"] = gridList;
	for(var i=0; i < gridList.length; i++) {
		alert(gridList[i].inputType);
	}
	var url = getURLString("/admin/saveDocValueDefinitionAction");
	ajaxCallServer(url, param, function(data){
	});
} */

</script>