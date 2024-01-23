<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>

<div class="product"> 
<form name="mainForm" id="mainForm" method="post">
<input type="hidden" id="changeType" name="changeType" value="${changeType}"/>
<div class="seach_arm pt20 pb5">
	<div class="leftbt">
		<div id="selectDiv">
			<span style="font-size:medium; ;" id="selectSpan">ROOT :</span> 
			<select id="root" name="root" onchange="selectRoot(this.value);">
			</select>
		</div>
	</div>
	<div class="rightbt">
		<span id="rootBtn">
		<input type="button" class="s_bt03" value="활동 추가" onclick="addDefinition()"> 
		<input type="button" class="s_bt05" id="delButton" value="활동 삭제" onclick="deleteDefinition()">
		</span>
	</div>
</div>
	<!-- table list-->
	<div class="table_list">
		<div class="list" id="eca_def_wrap"></div>
	</div>
	<!-- //table list-->
</form>
</div>
<script>
if (!webix.env.touch && webix.env.scrollSize)
	  webix.CustomScroll.init();
webix.ready(function(){
	var grid = webix.ui({
		view:"datatable",
		drag:true,
		container:"eca_def_wrap",
		id:"eca_def_wrap",
		select:"row",
		multiselect : true,
		scroll:"xy",
		yCount: 10,
		tooltip:true,
		editable:true,
		autoConfig:false,
		columns:[
			{ id:"step", header:["${e3ps:getMessage('단계')}"] ,width:100,
				css: "custom_webix_ellipsis",	
				tooltip:function(data){
					return data.step ? data.step : "";
				},
				editor:"select", 
				options:["STEP1","STEP2", "STEP3","STEP4"],
				cssFormat: function(value, config){
					return "custom_webix_editable";
				}	
			},
			{ id:"check", header:{ content:"masterCheckbox" }, editor:"checkbox",  template:"{common.checkbox()}", width:40, tooltip:false,  },
			{ id:"activityName", header:["${e3ps:getMessage('활동명')}"] , fillspace:true, minWidth:180,
				css: "custom_webix_ellipsis",	
				tooltip:function(data){
					return data.activityName ? data.activityName : "";
				},
				editor:"text", 
				cssFormat: function(value, config){
					return "custom_webix_editable";
				}
			},
			{ id:"activityType", header:["${e3ps:getMessage('활동구분')}"] , width:100,
				css: "custom_webix_ellipsis",	
				tooltip:function(data){
					return data.activityType ? data.activityType : "";
				},
				editor:"select", 
				options:["COMMON","DOCUMENT","DRAWING","PART"],
				cssFormat: function(value, config){
					return "custom_webix_editable";
				}
			},
			{ id:"departmentName", header:["${e3ps:getMessage('담당부서')}"] , width:100,
				css: "custom_webix_ellipsis custom_webix_center",		
			},
			{ id:"name", header:["${e3ps:getMessage('담당자')}"] , width:100,
				css: "custom_webix_ellipsis",	
				tooltip:function(data){
					return data.name ? data.name : "";
				},
				editor:"combo",
				suggest:{
					body: {
						dataFeed:function(text) {
							this.clearAll();
							if(text.length === 0) {
								return;
							}
							this.load(function(){
								var param = new Object();
								
								param["userName"] = text;
								const json = JSON.stringify(param);
								
								
								let response = ajaxCallServer(getURLString("/admin/searchUserAction"), param, null);
								
								if(response.list != null){
									var result = new Array();
									var list = response.list;
									list.forEach(function(item){
 										if(!item.disable && item.disableKor=="사용중"){
 											var temp = item;
 											temp.id = item.name;
 											temp.value = item.name;
 											result.push(temp);
 										}
 									});
									return result;
								}
								
 								//return webix.ajax().headers({
 							    //	"Content-Type": "application/json; charset=UTF-8"
 							    //}).post(getURLString("/admin/searchUserAction"), json)
 							    //.then(function(response) {
 							    //	var data = response.json();
 							    //	var list = data.list;
 							    //	var exceptionList = $$("eca_def_wrap").serialize();
 								//	var parentItem = new Object();
 								//	parentItem["pOid"] = $("#parentOid").val();
 								//	console.log(data);
 								//	console.log(response);
 								//	var result = new Array();
 								//	list.forEach(function(item){
 								//		if(!item.disable && item.disableKor=="사용중"){
 								//			var temp = item;
 								//			temp.id = item.name;
 								//			temp.value = item.name;
 								//			result.push(temp);
 								//		}
 								//	})
 								//	return result;
 							    //});
							});
						}
					},
					css:"custom_webix_ellipsis",
					fitMaster:false,
					width: 180,
				},
				cssFormat: function(value, config){
					return "custom_webix_editable";
				}
			},
			{ id:"finishDate", header:["${e3ps:getMessage('요청일')}"] , width:200,
				css: "custom_webix_ellipsis custom_webix_center",	
				editor:"date",
				format:webix.Date.dateToStr("%Y-%m-%d"),
				cssFormat: function(value, config){
					return "custom_webix_editable";
				}	
			},
			{ id:"desc", header:["${e3ps:getMessage('비고')}"] , width:200,
				css: "custom_webix_ellipsis",	
				tooltip:function(data){
					return data.desc ? data.desc : "";
				},
				editor:"text", 
				cssFormat: function(value, config){
					return "custom_webix_editable";
				}	
			},
		],
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
	    on:{
			onEditorChange:function(cell, value) {
				if("name" === cell.column) {
					if("" === value) {
						$$("eca_def_wrap").editCancel();
					}
				}
			},
			onBeforeEditStop: function(state, editor, ignoreUpdate){
				if(ignoreUpdate) {
					return;
				}
				
				if("name" === editor.column) {
					var selectedItem = editor.getPopup().getList().getItem(state.value);
					if (!selectedItem) {
					    return;
					}
					$$("eca_def_wrap").updateItem(editor.row, selectedItem);
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
		    	var item = $$("eca_def_wrap").getItem(selection.row);
		    	if("number" !== selection.column) {
			    	item.check = true;
		    	}
			},
			onBeforeUnSelect:function(selection){
				var item = $$("eca_def_wrap").getItem(selection.row);
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
	webix.editors.$popup.date = {
			view:"popup",
		    body:{
		      view:"calendar",
		      width: 350,
		      height:200
		    }
			};
	webix.event(window, "resize", function(){ grid.adjust(); });
// 	if("${changeType}" == "ECR"){
// 		selectRoot("${ecrRootOid}");
// 	}else{
// 		selectRoot("");
// 	}
	selectRoot("");
	getRootList();
	getGridData();
	if("${oid}".length > 0) {
		$("#rootBtn").show();
		getGridDataFirst();
	}
});

//초기 데이터 불러오기
function getGridDataFirst() {
	var param = new Object();
	
	param["oid"] = "${oid}";
	var url = getURLString("/change/getActivityDef");
	$$("eca_def_wrap").clearAll();
	ajaxCallServer(url, param, function(data){
		var gridData = data.list;
		$$("eca_def_wrap").parse(gridData);
	});
	
}

//그리드 데이터 불러오기
function getGridData(roid) {
	var param = new Object();
	
	param["rootOid"] = "${param.root}";
	if(roid != null){
		param["rootOid"] = roid;
	}
	var url = getURLString("/admin/getActivityStepList");
	$$("eca_def_wrap").clearAll();
	ajaxCallServer(url, param, function(data){
		var gridData = data.list;
		$$("eca_def_wrap").parse(gridData);
	});
	
}

function createRootDefinition() {
	var url = getURLString("/admin/createRootDefinition");
	openPopup(url, "createRootDefinition",1000, 600);
}

function updateRootDefinition() {
	var roid = $("#root").val();
	var url = getURLString("/admin/updateRootDefinition") + "?oid="+roid;
	openPopup(url, "updateRootDefinition",1000, 600);
}

function deleteRootDefinition() {
	if (!confirm("삭제 하시겠습니까?"))
		return;
	var url = getURLString("/admin/deleteRootDefinitionAction");
	
	var param = new Object();
	
	var paramArray = $("#mainForm").serializeArray();
	
	$(paramArray).each(function(idx, obj){
		param[obj.name] = obj.value;
	});
	
	ajaxCallServer(url, param, function(data){
		// location.reload();
	}, true);
}

function selectRoot(roid) {
	getGridData(roid);
	if(roid != ""){
		$("#rootBtn").show();
	}else{
		$("#rootBtn").hide();
	}
}



function updateDefinition(doid) {
	var url = getURLString("/admin/updateDefinition") + "?oid="+doid;
	openPopup(url, "updateDefinition",1000, 600);
}

//function addDefinition() {
//	var roid = $("#root").val();
//	var url = getURLString("/admin/createDefinition") + "?oid="+roid;
//	openPopup(url, "createDefinition",1000, 600);
//}
function addDefinition() {
	
	if("${type}" == "single") {
		if($$("eca_def_wrap").getVisibleCount() > 0) {
			return; 
		}
	}
	var item = new Object();

	$$("eca_def_wrap").add(item);
}

function deleteDefinition() {
	$$("eca_def_wrap").editCancel();
	$$("eca_def_wrap").remove($$("eca_def_wrap").getSelectedId());
}

function reGrid(roid){
	$$("eca_def_wrap").clearAll();
	getGridData(roid);
}

</script>