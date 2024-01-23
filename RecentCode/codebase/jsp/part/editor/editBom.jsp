<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="pop">
	<!-- top -->
	<div class="top">
		<c:choose>
			<c:when test="${isPart}">
				<h2>${part.icon} ${e3ps:getMessage('부품')} - ${part.number}, ${part.name}, ${part.version}</h2>
			</c:when>
			<c:otherwise>
				<h2>${e3ps:getMessage('BOM 에디터')}</h2>
			</c:otherwise>
		</c:choose>
		<span style="padding-top:5px;padding-left:5px;">
			<button type="button" class="s_bt03" style="height:30px;" onclick="searchPartPopup()">${e3ps:getMessage('부품 선택')}</button>
		</span>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	<!-- //top -->
	<div class="pop_semi">
		<div class="pop_bomEditor_bomList">
			<div class="seach_arm2 mb5">
				<div class="leftbt">
					<span class="title">BOM</span>
					<c:if test="${isPart}">
						<input type="text" id="partNo" name="partNo" style="width:150px">
						<button type="button" class="s_bt03" onclick="bomTree_search();">${e3ps:getMessage('검색')}</button>
					</c:if>
				</div>
				<div class="rightbt">
					<c:if test="${isPart}">
						<button type="button" id="checkOutBtn" name="checkOutBtn" class="s_bt03" onclick="bomTree_checkOutBomPart();">${e3ps:getMessage('체크 아웃')}</button>
					</c:if>
				</div>
			</div>
			<div class="list" id="bomTree_grid_wrap">
			</div>
		</div>
		<div class="pop_bomEditor_childrenList">
			<input type="hidden" id="parentOid">
			<input type="hidden" id="parentTreeId">
			<div class="seach_arm2 mb5">
				<div class="leftbt">
					<span class="title">${e3ps:getMessage('하위 부품')}</span>
				</div>
				<div class="rightbt">
					<c:if test="${isPart}">
						<button type="button" id="addBtn" name="addBtn" class="s_bt03" onclick="children_searchBomPart();">${e3ps:getMessage('추가')}</button>
						<button type="button" id="deleteBtn" name="deleteBtn" class="s_bt03" onclick="children_deleteBomPart();">${e3ps:getMessage('삭제')}</button>
					</c:if>
				</div>
			</div>
			<div class="list" id="children_grid_wrap">
			</div>
		</div>
	</div>
</div>
<script>
if (!webix.env.touch && webix.env.scrollSize)
	  webix.CustomScroll.init();
	  
webix.ready(function(){
	
	var bomTree = webix.ui({
		view : "treetable",
		container : "bomTree_grid_wrap",
		id : "bomTree_grid_wrap",
		select : "row",
		clipboard:"selection",
		dragColumn: true,
		multiselect : true,
		scroll : "y",
		scrollAlignY:true,
		tooltip: true,
		yCount: 13,
		columns:[
			{ id:"number",	header:"${e3ps:getMessage('부품 번호')}",	fillspace:true,
				css:"custom_webix_ellipsis",
				template:"{common.treetable()}#number#",
				tooltip:function(data){
					return data.number;
				}
			},
			{ id:"stateName",	header:"${e3ps:getMessage('상태')}", 	width:70,
				css: "custom_webix_ellipsis custom_webix_center",		
			},
			{ id:"version",	header:"${e3ps:getMessage('버전')}", 	width:100,
				css: "custom_webix_ellipsis custom_webix_center",		
			},
			{ id:"quantity",	header:"${e3ps:getMessage('수량')}", 	width:60,
				css: "custom_webix_ellipsis custom_webix_center",		
			},
		],
		
		type:{
		    folder:function(obj){
				var img = obj.img;
				
				var imgSrc = "/Windchill/wtcore/images/part.gif";
	
				if(obj.checkoutState == "c/o" || obj.checkoutState == "wrk") {
					if(obj.ownerCheckout){
						imgSrc = "/Windchill/jsp/portal/images/icon_1.gif"; //자신이 체크아웃 함
					}else{
						imgSrc = "/Windchill/jsp/portal/images/icon_2.gif"; //타인이 체크아웃 함 
					}
				}
				
				return "<img src='" + imgSrc + "' style='float:left; margin:10px 5px 10px 5px;'>";
		    }
		},
		on: {
	        onDataRequest: bomTree_loadChildren,
	        onItemClick: bomTree_itemClick,
	        onBeforeContextMenu: bomTree_beforeContextMenu,
		}
	});
	
	var bomChildren = webix.ui({
		view : "datatable",
		container : "children_grid_wrap",
		id : "children_grid_wrap",
		clipboard:"block",
		multiselect:true,
		select:"cell",
		dragColumn: true,
		multiselect : true,
		scroll : "y",
		scrollAlignY:true,
		editable:true,
	    autoConfig:false,
		tooltip: true,
		yCount: 13,
		columns:[
			{ id:"index", header:"No.",	width:40, css:"custom_webix_center", tooltip:false},
			{ id:"icon", header:"",	width:30, css:"custom_webix_imgCenter", tooltip:false},
			{ id:"number",	header:"${e3ps:getMessage('부품 번호')}",	fillspace:1,
				css:"custom_webix_ellipsis",
				tooltip:function(data){
					return data.number ? data.number : "";
				},
				editor:"combo",
				suggest:{
					body: {
						id: "children_grid_search_number",
						dataFeed:function(text) {
							this.clearAll();
							this.load(function(){
								var param = new Object();
								
								param["number"] = text;
								param["objType"] = "part";
								
								const json = JSON.stringify(param);
								
								return webix.ajax().headers({
							    	"Content-Type": "application/json; charset=UTF-8"
							    }).post(getURLString("/common/searchObjectAction"), json)
							    .then(function(response) {
							    	var data = response.json();
							    	var list = data.list;
							    	
							    	var exceptionList = $$("children_grid_wrap").serialize();
									var parentItem = new Object();
									parentItem["oid"] = $("#parentOid").val();
									exceptionList.push(parentItem);
									
									exceptionList.forEach(function(child){
										list = list.filter(function(item, index, arr){
											return item.oid != child.oid;
										});
									})
									
									var result = new Array();
									list.forEach(function(item){
										var temp = item;
										temp.id = item.number;
										temp.value = item.number;
										
										delete temp.icon;
										result.push(temp);
									})
									
									return result;
							    });
							});
						}
					},
					css:"custom_webix_ellipsis",
					fitMaster:false,
					width: 180,
				},
				cssFormat: function(value, config){
					if(config.oid == null) {
						return "custom_webix_editable";
					}
					return "";
				}
			},
			{ id:"name",	header:"${e3ps:getMessage('부품 명')}", 	fillspace:1.5,
				css:"custom_webix_ellipsis",
			},
			{ id:"stateName",	header:"${e3ps:getMessage('상태')}", 	width:70,
				css:"custom_webix_ellipsis custom_webix_center",
			},
			{ id:"version",	header:"${e3ps:getMessage('버전')}", 	width:100,
				css:"custom_webix_ellipsis custom_webix_center",
			},
			{ id:"quantity",	header:"${e3ps:getMessage('수량')}", 	width:60,
				css:"custom_webix_ellipsis custom_webix_center",
				editor:"text", numberFormat:"1,111",
				cssFormat: function(value, config){
					if(config.oid != null) {
						if(config.cadSync === "yes") {
							if(config.epm === true) {
								return "";
							} else {
								return "custom_webix_editable";
							}
						} else {
							return "custom_webix_editable";
						}
					}
					return "";
				}
			},
		],
		scheme:{
	        $init:function(obj){ obj.index = this.count(); }
	    },
	    on: {
	        onBeforeEditStart: children_beforeEditStart,
	        onAfterEditStop: children_afterEditStop,
	        onBeforeSelect: children_beforeSelect,
		},
	});
	
	webix.event(window, "resize", function(){ bomChildren.adjust(); });
	 
	webix.extend($$("bomTree_grid_wrap"), webix.ProgressBar);
	webix.extend($$("children_grid_wrap"), webix.ProgressBar);
	
	$(".webix_excel_filter").click(function(){
		 $$("bomTree_grid_wrap").unselect(); 
	});

	$(".webix_excel_filter").click(function(){
		 $$("children_grid_wrap").unselect(); 
	});

	if(${isPart} == true) {
		bomTree_getGridData();
		
		children_getGridData();	
	}
	
	webix.ui({
		view:"contextmenu",
		id:"bomTreeMenu",
		width:200,
		css: "custom_webix_contextMenu",
		data:[
			{
				id : "refresh",
				value : "${e3ps:getMessage('새로고침')}",
			},
			{ $template:"Separator" },
			{
				id : "checkIn",
				value : "${e3ps:getMessage('체크 인')}",
			},
			{
				id : "checkOut",
				value : "${e3ps:getMessage('체크 아웃')}",
			},
			{
				id : "undoCheckout",
				value : "${e3ps:getMessage('체크 아웃 명령 취소')}",
			},
			{ $template:"Separator" },
			{
				id : "revise",
				value : "${e3ps:getMessage('개정')}",
			},
			{ $template:"Separator" },
			{
				id : "openBomEditor",
				value : "${e3ps:getMessage('새 창에서 BOM 에디터 열기')}",
			},
			{
				id : "openBomTree",
				value : "${e3ps:getMessage('BOM 보기')}",
			},
			{
				id : "openView",
				value : "${e3ps:getMessage('상세정보 페이지 보기')}",
			}
		],
		on : {
			onBeforeShow : function(event) {
				this.enableItem("checkIn");
				this.enableItem("undoCheckout");
				this.enableItem("checkOut");
				this.enableItem("revise");
				
				var selectedItem = $$("bomTree_grid_wrap").getSelectedItem(true);
				for(var i=0; i < selectedItem.length; i++) {
					var item = selectedItem[i];
					if(item.state === "INWORK") {
						if(item.checkoutState === "c/i") {
							this.disableItem("checkIn");
							this.disableItem("undoCheckout");
						} else if(item.checkoutState == "c/o" || item.checkoutState == "wrk") {
							this.disableItem("checkOut");
						}
						this.disableItem("revise");
					} else {
						this.disableItem("checkIn");
						this.disableItem("checkOut");
						this.disableItem("undoCheckout");
						
						if(item.state !== "APPROVED") {
							this.disableItem("revise");
						}
					}
				}
			},
			onMenuItemClick : function(id, event, node){
				var selectedItem = $$("bomTree_grid_wrap").getSelectedItem(true);
				
				if("refresh" === id) {
					refresh(selectedItem);
				} else if("checkIn" === id) {
					checkIn(selectedItem);
				} else if("checkOut" === id) {
					checkout(selectedItem);
				} else if("undoCheckout" === id) {
					undoCheckout(selectedItem);
				} else if("revise" === id) {
					revise(selectedItem);
				} else if("openBomEditor" === id) {
					bomTree_openBomEditor(selectedItem);
				} else if("openBomTree" === id) {
					bomTree_openBomTree(selectedItem);
				} else if("openView" === id) {
					bomTree_openView(selectedItem);
				}
				
			}
		}
	});

	$$("bomTreeMenu").attachTo($$("bomTree_grid_wrap"));
})

//bom 트리 불러오기
function bomTree_getGridData(){
	
	var param = new Object();
	
	if(${isPart} == true) {
		param.oid = "${part.oid}";	
	} else {
		param.oid = $("#partOid").val();
	}
	
	$$("bomTree_grid_wrap").clearAll();
	$$("bomTree_grid_wrap").showProgress();
	var url = getURLString("/bomEditor/getBomRoot");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		if(gridData[0].children !== null && gridData[0].children.length > 0) {
			gridData[0].webix_kids = true;
		}
		
		// 그리드에 데이터 세팅
		$$("bomTree_grid_wrap").parse(gridData[0]);
		$$("bomTree_grid_wrap").open(gridData[0].id);
		
		$$("bomTree_grid_wrap").hideProgress();
	});
}

//bomTree 
function bomTree_loadChildren(id) {
	
	let item = $$("bomTree_grid_wrap").getItem(id);
	
	var oid = item.oid;
	var level = item.level;
	var treeId = item.treeId;
	
	// 자식 데이터 요청
	var param = new Object();
	
	param.oid = oid;
	param.level = level;
	param.treeId = treeId;

	$$("bomTree_grid_wrap").showProgress();
	var url = getURLString("/bomEditor/getBomChildren");
	ajaxCallServer(url, param, function(data){
		// 그리드 데이터
		var childrenList = data.list;

		//child.children === null  ::: children 없음
		//child.children !== null(list length 0) ::: children 있음
		childrenList = childrenList.map(function(child) {
			if(child.children !== null) {
				child.webix_kids = true;
			}
			
			return child;
		});
		
		// 성공 시 완전한 배열 객체로 삽입하십시오.
		const children = {
    		parent : id,
    		data : childrenList
    	};
		$$("bomTree_grid_wrap").parse(children);
		$$("bomTree_grid_wrap").hideProgress();
	});
}

function bomTree_itemClick(id, event, node) {
	
	let item = $$("bomTree_grid_wrap").getItem(id);
	
	children_getGridData(item);
}

function bomTree_beforeContextMenu(id, event, node) {
	var selectedItem = $$("bomTree_grid_wrap").getSelectedItem(true);
	
	if(selectedItem.length < 2) {
		$$("bomTree_grid_wrap").select(id);
	}
}

//children 불러오기
function children_getGridData(item){

	var oid;
	var level;
	var treeId;
	var state;
	
	if(item == null){
		if(${isPart} == true) {
			oid = "${part.oid}";
		} else {
			oid = $("#partOid").val();
		}
		level = 0;
		treeId = "0_0";		
		state = "${part.state}";
	} else {
		oid = item.oid;
		level = item.level;
		treeId = item.treeId;	
		state = item.state;	
	}
	
	$("#parentOid").val(oid);
	$("#parentTreeId").val(treeId);
	
	var param = new Object();
	
	param["oid"] = oid;
	param["level"] = level;
	param["treeId"] = treeId;
	
	$$("children_grid_wrap").clearAll();
	$$("children_grid_wrap").showProgress();
	var url = getURLString("/bomEditor/getBomChildren");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		var isModify = data.isModify;
		
		// 그리드에 데이터 세팅
		$$("children_grid_wrap").parse(data.list);
		
		if(isModify){
			if(state == "INWORK") {
				$("#addBtn").css("display", "");
				$("#deleteBtn").css("display", "");
				children_addRow();
				$$("children_grid_wrap").editable = true;
				//AUIGrid.setProp(children_myGridID, "editable", true); 
			} else {
				$("#addBtn").css("display", "none");
				$("#deleteBtn").css("display", "none");
				$$("children_grid_wrap").editable = false;
				//AUIGrid.setProp(children_myGridID, "editable", false); 
			}
		}else{
			$("#addBtn").css("display", "none");
			$("#deleteBtn").css("display", "none");
		}
		
		$$("children_grid_wrap").hideProgress();
	});
}

//children beforeEditStart
function children_beforeEditStart(cell) {
	
	let item = $$("children_grid_wrap").getItem(cell);
	
	if(cell.column === "quantity") {
		if(item.state == "WITHDRAWN") {
			return false;
		}
		// 추가된 행 아이템인지 조사하여 추가된 행이 아닌 경우만 에디팅 진입 허용
		if(item.cadSync == "yes") {
			if(item.epm == true) {
				return false;
			} else {
				return true;
			}
		} else if(item.id === $$("children_grid_wrap").getLastId()) {
			return false; 
		} else {
			return true; // false 반환하면 기본 행위 안함(즉, cellEditBegin 의 기본행위는 에디팅 진입임)
		}
	} else if(cell.column === "number") {
		if(item.id === $$("children_grid_wrap").getLastId()) {
			return true; 
		}
	}
	
	return false;
};

//children afterEditStop
function children_afterEditStop(state, editor, ignoreUpdate) {
	if(ignoreUpdate) {
		return false;
	}
	let item = $$("children_grid_wrap").getItem(editor.row);
	
	if("quantity" === editor.column && state.value !== state.old) {
		children_modifyBomPart(item);
	} else if("number" === editor.column) {
		
	     var selectedItem = editor.getPopup().getList().getItem(state.value);
	     if (!selectedItem) {
	         return false;
	     }
		$$("children_grid_wrap").updateItem(editor.row, selectedItem);
		
		children_addedBomPartSave(selectedItem);
	}
}

//children beforeSelect
function children_beforeSelect(selection, preserve) {
	let item = $$("children_grid_wrap").getItem(selection.row);
	
	if(item.oid == null) {
		return false;
	}
	return true;
}

//children 추가
function children_addRow() {
	var item = new Object();
	
	$$("children_grid_wrap").add(item);
	
	item.index = $$("children_grid_wrap").count(); 
	
	$$("children_grid_wrap").updateItem(item.id,item);
}

//부품 수정
function children_modifyBomPart(item) {

	//부모 Oid
	var parentOid = $("#parentOid").val();
	
	var param = new Object();
	
	param["parentOid"] = parentOid;
	param["modifiedPart"] = item;
	
	var url = getURLString("/bomEditor/modifyBomPartAction");
	ajaxCallServer(url, param, function(data){
		
		bomTree_modifyPartReloadBomChildren();
	}, true);
}

//부품 수정(수량) 후 업데이트 부품 로드
function bomTree_modifyPartReloadBomChildren() {
	
	var param = new Object();
	
	var parentTreeId = $("#parentTreeId").val();
	
	var item = $$("bomTree_grid_wrap").getItem(parentTreeId);
	
	var items = new Array();
	
	items.push(item);
	
	param.parentItems = items;
	
	var url = getURLString("/bomEditor/getUpdatedBomData");
	ajaxCallServer(url, param, function(data){

		var list = data.list;
		
		if(list.length > 0) {
			var newItem = list[0]; // check out 된 아이템

			modifyPartUpdateBomTree(newItem);
			
			//자식 그리드 재호출
			children_getGridData(newItem);
		}
	});
}

//부품 수정 시(수량) 그리드 업데이트
function modifyPartUpdateBomTree(newItem) {
	
	var sameNumberList = $$("bomTree_grid_wrap").find(function(item){
		return item.number === newItem.number;
	});
	
	var newChildrenList = newItem.children;
	
	sameNumberList.forEach(function(sameItem){
		$$("bomTree_grid_wrap").updateItem(sameItem.id, newItem);
		
		$$("bomTree_grid_wrap").data.eachChild(sameItem.id, function(child){
			newChildrenList.forEach(function(newChildItem){
				if(child.number === newChildItem.number) {
					$$("bomTree_grid_wrap").updateItem(child.id, newChildItem);
				}
			})
		});
	});
}

//추가 부품 저장
function children_addedBomPartSave(item) {

	//부모 Oid
	var parentOid = $("#parentOid").val();
	
	var param = new Object();
	
	param["parentOid"] = parentOid;
	param["addedPart"] = item;
	
	var url = getURLString("/bomEditor/saveAddedPartAction");
	ajaxCallServer(url, param, function(data){
		
		bomTree_addPartReloadBomChildren();
		
		//result 리스트 초기화
		$$("children_grid_search_number").clearAll();
	}, true);
	
}

//부품 추가 시 bom, children 리로드
function bomTree_addPartReloadBomChildren() {
	
	var param = new Object();
	
	var parentTreeId = $("#parentTreeId").val();
	
	var item = $$("bomTree_grid_wrap").getItem(parentTreeId);
	
	var items = new Array();
	
	items.push(item);
	
	param.parentItems = items;
	
	var url = getURLString("/bomEditor/getUpdatedBomData");
	ajaxCallServer(url, param, function(data){

		var list = data.list;
		
		if(list.length > 0) {
			var newItem = list[0]; // check out 된 아이템
			
			addPartUpdateBomTree(newItem);
			
			//자식 그리드 재호출
			children_getGridData(newItem);
		}
	});
}

//부품 추가 시 그리드 업데이트
function addPartUpdateBomTree(newItem) {
	
	var sameNumberList = $$("bomTree_grid_wrap").find(function(item){
		return item.number === newItem.number;
	});
	
	var newChildrenList = newItem.children;
	var copyChildrenList = newChildrenList.slice();
	sameNumberList.forEach(function(sameItem){
		
		$$("bomTree_grid_wrap").updateItem(sameItem.id, newItem);
		
		var oldChildrenList = $$("bomTree_grid_wrap").data.getBranch(sameItem.id).slice();
		
		oldChildrenList = oldChildrenList.reverse();
		
		for(var i=0; i < oldChildrenList.length; i++) {
			var oldChild = oldChildrenList[i];
			newChildrenList.forEach(function(newChildItem){
				if(oldChild.number === newChildItem.number) {
					$$("bomTree_grid_wrap").updateItem(oldChild.id, newChildItem);
					$$("bomTree_grid_wrap").data.changeId(oldChild.id, newChildItem.id);
				}
			})
		}
		
		var addedList = newChildrenList.slice();
		oldChildrenList.forEach(function(item){
			addedList = addedList.filter(function(newChild){
				return newChild.number !== item.number;
			});
		});
		
		addedList = addedList.map(function(child){
			if(child.children !== null) {
				child.webix_kids = true;
			}
			
			return child;
		});
		
		addedList.forEach(function(child){
			var idx = child.id.split("_").reverse()[0];
			$$("bomTree_grid_wrap").add(child, idx, sameItem.id);
		});
	});
}

//체크 아웃 
function bomTree_checkOutBomPart(){
	
	var checkItemList = $$("bomTree_grid_wrap").getSelectedItem(true);
	
	var checkOutItemList = new Array();
	
	for(var i = 0; i < checkItemList.length; i++){
		var item = checkItemList[i];
		
		if(item.checkoutState == "c/o" || item.checkoutState == "wrk") {
			openNotice("${e3ps:getMessage('이미 체크 아웃된 부품이 있습니다.')}");
			return;
		}
		
		if(item.state != "INWORK") {
			openNotice("${e3ps:getMessage('작업중 상태인 부품만 체크아웃 할 수 있습니다. 개정을 진행하십시오.')}");
			return;
		}
		
		checkOutItemList.push(item);
	}
	
	openConfirm("${e3ps:getMessage('체크아웃 하시겠습니까?')}", function() {
		
		var param = new Object();
		
		param.checkOutItemList = checkOutItemList;
		
		var url = getURLString("/bomEditor/multiCheckOut");
		ajaxCallServer(url, param, function(data){
			
			bomTree_getGridData();
			children_getGridData();
		});
	});
}

//새로고침 
function refresh(items) {
		
	var param = new Object();
	
	param.parentItems = items;
	
	var url = getURLString("/bomEditor/getRefreshBomData");
	ajaxCallServer(url, param, function(data){

		var list = data.list;
		
		for(var i=0; i < list.length; i++) {
			var newItem = list[i];
			refreshPartUpdateBomTree(newItem);
		}
			
		if(list.length > 0) {
			children_getGridData(list[0]);
		}
	}, true);
}

//새로고침 시 그리드 업데이트
function refreshPartUpdateBomTree(newItem) {
	
	var sameNumberList = $$("bomTree_grid_wrap").find(function(item){
		return item.number === newItem.number;
	});
	
	var newChildrenList = newItem.children;
	
	sameNumberList.forEach(function(sameItem){
		
		$$("bomTree_grid_wrap").updateItem(sameItem.id, newItem);
		
		const isBranchOpen = $$("bomTree_grid_wrap").isBranchOpen(sameItem.id);
		
		var oldChildrenList = $$("bomTree_grid_wrap").data.getBranch(sameItem.id).slice();
		
		oldChildrenList.forEach(function(child){
			$$("bomTree_grid_wrap").remove(child.id);
		});
		
		newChildrenList = newChildrenList.map(function(child){
			if(child.children !== null) {
				child.webix_kids = true;
			}
			
			return child;
		});
		
		newChildrenList.forEach(function(child){
			$$("bomTree_grid_wrap").add(child, null, sameItem.id);
		});
		
		if(isBranchOpen) {
			$$("bomTree_grid_wrap").open(sameItem.id)
		}
	});
}

//체크 아웃 
function checkout(items){
	
	openConfirm("${e3ps:getMessage('체크아웃 하시겠습니까?')}", function() {
		
		var param = new Object();
		
		param.items = items;
		
		var url = getURLString("/bomEditor/checkout");
		ajaxCallServer(url, param, function(data){
			
			bomTree_checkoutPartReloadBomChildren(items);
			
		}, true);
	});
}

//체크 아웃 시 bom, children 리로드
function bomTree_checkoutPartReloadBomChildren(items) {
	
	var param = new Object();
	
	param.parentItems = items;
	
	var url = getURLString("/bomEditor/getUpdatedBomData");
	ajaxCallServer(url, param, function(data){

		var list = data.list;
		
		for(var i=0; i < list.length; i++) {
			var newItem = list[i];
			checkoutPartUpdateBomTree(newItem);
		}
		
		if(list.length > 0) {
			children_getGridData(list[0]);
		}
	});
}

//체크 아웃 시 그리드 업데이트
function checkoutPartUpdateBomTree(newItem) {
	
	var sameNumberList = $$("bomTree_grid_wrap").find(function(item){
		return item.number === newItem.number;
	});
	
	var newChildrenList = newItem.children;
	
	sameNumberList.forEach(function(sameItem){
		$$("bomTree_grid_wrap").updateItem(sameItem.id, newItem);
		
		$$("bomTree_grid_wrap").data.eachChild(sameItem.id, function(child){
			newChildrenList.forEach(function(newChildItem){
				if(child.number === newChildItem.number) {
					$$("bomTree_grid_wrap").updateItem(child.id, newChildItem);
				}
			})
		});
	});
}

//체크 인 
function checkIn(items) {
	
	openConfirm("${e3ps:getMessage('체크인 하시겠습니까?')}", function() {
		
		var param = new Object();
		
		param.items = items;
		
		var url = getURLString("/bomEditor/checkin");
		ajaxCallServer(url, param, function(data){
			
			bomTree_checkinPartReloadBomChildren(items);
		}, true);
	});
}

//체크 인 시 bom, children 리로드
function bomTree_checkinPartReloadBomChildren(items) {
	
	var param = new Object();
	
	param.parentItems = items;
	
	var url = getURLString("/bomEditor/getUpdatedBomData");
	ajaxCallServer(url, param, function(data){

		var list = data.list;
		
		for(var i=0; i < list.length; i++) {
			var newItem = list[i];
			checkinPartUpdateBomTree(newItem);
		}
		
		//자식 그리드 재호출
		if(list.length > 0) {
			children_getGridData(list[0]);
		}
	});
}

//체크 인 시 그리드 업데이트
function checkinPartUpdateBomTree(newItem) {
	
	var sameNumberList = $$("bomTree_grid_wrap").find(function(item){
		return item.number === newItem.number;
	});
	
	var newChildrenList = newItem.children;
	
	sameNumberList.forEach(function(sameItem){
		
		$$("bomTree_grid_wrap").updateItem(sameItem.id, newItem);
		
		const isBranchOpen = $$("bomTree_grid_wrap").isBranchOpen(sameItem.id);
		
		var oldChildrenList = $$("bomTree_grid_wrap").data.getBranch(sameItem.id).slice();
		
		oldChildrenList.forEach(function(child){
			$$("bomTree_grid_wrap").remove(child.id);
		});
		
		newChildrenList = newChildrenList.map(function(child){
			if(child.children !== null) {
				child.webix_kids = true;
			}
			
			return child;
		});
		
		newChildrenList.forEach(function(child){
			$$("bomTree_grid_wrap").add(child, null, sameItem.id);
		});
		
		if(isBranchOpen) {
			$$("bomTree_grid_wrap").open(sameItem.id)
		}
	});
}

//체크 아웃 명령 취소
function undoCheckout(items) {
	
	openConfirm("${e3ps:getMessage('체크아웃을 취소하시겠습니까?')}", function() {
		
		var param = new Object();
		
		param.items = items;
		
		var url = getURLString("/bomEditor/undoCheckout");
		ajaxCallServer(url, param, function(data){
			
			var undoList = data.undoList;
			
			items.forEach(function(item){
				var tempArr = undoList.filter(function(undoItem){
			 		return undoItem.number === item.number;
				});
			
				if(tempArr.length > 0) {
					item.oid = tempArr[0].oid;
				}
			});
			
			bomTree_undoCheckoutPartReloadBomChildren(items);
			
		}, true);
	});
}

//체크 아웃 명령 취소 시 bom, children 리로드
function bomTree_undoCheckoutPartReloadBomChildren(items) {
	
	var param = new Object();
	
	param.parentItems = items;
	
	var url = getURLString("/bomEditor/getUpdatedBomData");
	ajaxCallServer(url, param, function(data){

		var list = data.list;
		
		for(var i=0; i < list.length; i++) {
			var newItem = list[i];
			undoCheckoutPartUpdateBomTree(newItem);
		}
		
		//자식 그리드 재호출
		if(list.length > 0) {
			children_getGridData(list[0]);
		}
	});
}

//체크 아웃 명령 취소 시 그리드 업데이트
function undoCheckoutPartUpdateBomTree(newItem) {
	
	var sameNumberList = $$("bomTree_grid_wrap").find(function(item){
		return item.number === newItem.number;
	});
	
	var newChildrenList = newItem.children;
	
	sameNumberList.forEach(function(sameItem){
		
		$$("bomTree_grid_wrap").updateItem(sameItem.id, newItem);
		
		const isBranchOpen = $$("bomTree_grid_wrap").isBranchOpen(sameItem.id);
		
		var oldChildrenList = $$("bomTree_grid_wrap").data.getBranch(sameItem.id).slice();
		
		oldChildrenList.forEach(function(child){
			$$("bomTree_grid_wrap").remove(child.id);
		});
		
		newChildrenList = newChildrenList.map(function(child){
			if(child.children !== null) {
				child.webix_kids = true;
			}
			
			return child;
		});
		
		newChildrenList.forEach(function(child){
			$$("bomTree_grid_wrap").add(child, null, sameItem.id);
		});
		
		if(isBranchOpen) {
			$$("bomTree_grid_wrap").open(sameItem.id)
		}
	});
}

//children 삭제 버튼
function children_deleteBomPart() {

	openConfirm("${e3ps:getMessage('삭제하시겠습니까?')}", function(){
		
		var selectedItem = $$("children_grid_wrap").getSelectedItem(true);
		
		for(var i=0; i < selectedItem.length; i++) {
			var item = selectedItem[i];
			if(item.cadSync == "yes") {
				if(item.epm == true) {
					openNotice("${e3ps:getMessage('도면이 연결된 부품은 삭제할 수 없습니다.')}");
					return;
				}
			}
		}
		
		var param = new Object();
		
		var parentOid = $("#parentOid").val();

		param["parentOid"] = parentOid;
		param["removedItemList"] = selectedItem;
		
		var url = getURLString("/bomEditor/deleteBomPartAction");
		ajaxCallServer(url, param, function(data){
			
			bomTree_deletePartReloadBomChildren();
			
		});
	}, true)
}

//자식 삭제 시 bom, children 리로드
function bomTree_deletePartReloadBomChildren() {
	
	var param = new Object();
	
	var parentTreeId = $("#parentTreeId").val();
	
	var item = $$("bomTree_grid_wrap").getItem(parentTreeId);
	
	var items = new Array();
	
	items.push(item);
	
	param.parentItems = items;
	
	var url = getURLString("/bomEditor/getUpdatedBomData");
	ajaxCallServer(url, param, function(data){

		var list = data.list;
		
		if(list.length > 0) {
			var newItem = list[0]; // check out 된 아이템

			deletePartUpdateBomTree(newItem);
			
			//자식 그리드 재호출
			children_getGridData(newItem);
		}
	});
}

//부품 삭제 시 그리드 업데이트
function deletePartUpdateBomTree(newItem) {
	
	var sameNumberList = $$("bomTree_grid_wrap").find(function(item){
		return item.number === newItem.number;
	});
	
	var newChildrenList = newItem.children;
	
	sameNumberList.forEach(function(sameItem){
		
		$$("bomTree_grid_wrap").updateItem(sameItem.id, newItem);
		
		const isBranchOpen = $$("bomTree_grid_wrap").isBranchOpen(sameItem.id);
		
		var oldChildrenList = $$("bomTree_grid_wrap").data.getBranch(sameItem.id).slice();
		
		oldChildrenList.forEach(function(child){
			$$("bomTree_grid_wrap").remove(child.id);
		});
		
		newChildrenList = newChildrenList.map(function(child){
			if(child.children !== null) {
				child.webix_kids = true;
			}
			
			return child;
		});
		
		newChildrenList.forEach(function(child){
			$$("bomTree_grid_wrap").add(child, null, sameItem.id);
		});
		
		if(isBranchOpen) {
			$$("bomTree_grid_wrap").open(sameItem.id)
		}
	});
}

//개정
function revise(items) {
	
	openConfirm("${e3ps:getMessage('개정 하시겠습니까?')}", function() {
		
		var param = new Object();
		
		param.items = items;
		
		var url = getURLString("/bomEditor/revise");
		ajaxCallServer(url, param, function(data){
			
			var reviseList = data.reviseList;
			
			items.forEach(function(item){
				if(item.treeId == "0_0") {
					var url = getURLString("/bomEditor/editBom") + "?oid=" + item.oid;
					
					location.href = url;
					return;
				}
				
				var tempArr = reviseList.filter(function(reviseItem){
					return reviseItem.number === item.number;
				});
				
				if(tempArr.length > 0) {
					item.oid = tempArr[0].oid;
				}
			});
			
			bomTree_revisePartReloadBomChildren(items);	
		}, true);
	});
}

//개정 시 bom, children 리로드
function bomTree_revisePartReloadBomChildren(items) {
	
	var param = new Object();
	
	param.parentItems = items;
	
	var url = getURLString("/bomEditor/getUpdatedBomData");
	ajaxCallServer(url, param, function(data){

		var list = data.list;
		
		for(var i=0; i < list.length; i++) {
			var newItem = list[i];
			revisePartUpdateBomTree(newItem);
		}
		
		//자식 그리드 재호출
		if(list.length > 0) {
			children_getGridData(list[0]);
		}
	});
}

//개정 시 그리드 업데이트
function revisePartUpdateBomTree(newItem) {
	
	var sameNumberList = $$("bomTree_grid_wrap").find(function(item){
		return item.number === newItem.number;
	});
	
	var newChildrenList = newItem.children;
	
	sameNumberList.forEach(function(sameItem){
		$$("bomTree_grid_wrap").updateItem(sameItem.id, newItem);
		
		$$("bomTree_grid_wrap").data.eachChild(sameItem.id, function(child){
			newChildrenList.forEach(function(newChildItem){
				if(child.number === newChildItem.number) {
					$$("bomTree_grid_wrap").updateItem(child.id, newChildItem);
				}
			})
		});
	});
}

//bom tree 검색
function bomTree_search(){
	var partNo = $("#partNo").val();

	if(partNo.trim() == "") {
		alert("${e3ps:getMessage('검색할 번호를 입력하십시오.')}");
		return;
	}
	
	var bomTree = $$("bomTree_grid_wrap");
	
	bomTree.clearCss("custom_webix_marker", true);
	
	if(partNo){
		partNo = partNo.toLowerCase();
		var res = bomTree.find(function(obj){
			return obj.number.toLowerCase().indexOf(partNo) != -1;
		});
		/* for (var i = 0; i < res.length; i++) {
			bomTree.addCss(res[i].id, "custom_webix_marker", true);
		} */
		var openLevel;
		for (var i = 0; i < res.length; i++) {
			bomTree.addCss(res[i].id, "custom_webix_marker", true);
			if(openLevel != null) {
				if(openLevel < res[i].level) {
					openLevel = res[i].level;
				}
			} else {
				openLevel = res[i].level;
			}
		}
		if(openLevel != null) {
			for(openLevel ; openLevel > -1; openLevel--) {
				$$("bomTree_grid_wrap").filter("#level#",Number(openLevel));
			}
		}
	}
	bomTree.refresh();
}

//BOM에디터 오픈 
function bomTree_openBomEditor(items) {
	for(var i=0; i < items.length; i++) {
		openBomEditor(items[i].oid);
	}
}

//BOM 트리 오픈 
function bomTree_openBomTree(items) {
	for(var i=0; i < items.length; i++) {
		openBomTree(items[i].oid);
	}
}

//View 오픈 
function bomTree_openView(items) {
	for(var i=0; i < items.length; i++) {
		openView(items[i].oid);
	}
}

function children_searchBomPart() {
	var parentOid = $("#parentOid").val();
	
	var url = getURLString("/bomEditor/searchBomPartPopup") + "?type=multi&parentOid=" + parentOid;
	
	openPopup(url, "searchBomPartPopup", 1100, 700);
}

function searchPartPopup() {

	var url = getURLString("/bomEditor/searchBomPartPopup") + "?type=single";
	
	openPopup(url, "searchBomPartPopup", 1100, 700);
}

function setRootPart(list) {
	if(list.length > 0) {
		var item = list[0];
		
		var url = getURLString("/bomEditor/editBom") + "?oid=" + item.oid;
		
		openPopup(url,"bomEditor_", 1124, 600);
		
		/* openBomEditor(item.oid);
		
		window.close(); */
	}
}
</script>