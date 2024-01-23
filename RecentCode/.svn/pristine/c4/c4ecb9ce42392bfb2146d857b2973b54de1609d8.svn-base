<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="product">
	<!-- button -->
	<div class="seach_arm pt15 pb5">
		<div class="leftbt">
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
				<table summary="">
					<caption></caption>
					<colgroup>
						<col style="width:18%">
						<col style="width:32%">
						<col style="width:18%">
						<col style="width:32%">
					</colgroup>
					
					<tbody>
						<tr>
							<th scope="col">${e3ps:getMessage('코드')}</th>
							<td><input type="text" id="code" name="code" class="w100" /></td>
							<th scope="col">${e3ps:getMessage('이름')}</th>
							<td><input type="text" id="name" name="name" class="w100" /></td>
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
					<button type="button" class="s_bt03" onclick="addRow()">${e3ps:getMessage('추가')}</button>
					<button type="button" class="s_bt03" onclick="save()">${e3ps:getMessage('저장')}</button>
				</div>
			</div>
			<!-- //button -->
<!-- 			<div class="list" id="grid_wrap" style="height:300px; border-top:2px solid #1064aa;"></div> -->
			<div class="list" id="grid_wrap" style="height:300px; border-top:2px solid #74AF2A;"></div>
			
			<jsp:include page="${e3ps:getIncludeURLString('/admin/include_docTypeDefinitionList')}" flush="true"/>
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
		yCount: 7,
		leftSplit:4,
		tooltip:true,
		resizeColumn:true,
		datafetch:50,
		columns:[
			{ id:"index",   header:"No.", width:40,	tooltip:false, 
				css: "custom_webix_center",		
			},
			{ id:"delete",header:"", width:40 ,
				template:function(obj, common, value, config){
					if(value){
				        return common.trashIcon(obj, common, value, config);
					}
	            	return "";
		        }
			},
			{ id:"mark", header:{ content:"masterCheckbox" }, 
				template:"{common.checkbox()}", checkValue:"On", uncheckValue:"Off", width:40
			},
			{ id:"code", header:["${e3ps:getMessage('코드')}"],	width:200, editor:"text",sort:"string",
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
			{ id:"name", header:["${e3ps:getMessage('이름')}"] , sort:"string", minWidth:200, fillspace:true, editor:"text",
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
		        	}	 */
		        	return value;
				}
			},
			{ id:"engName", header:"${e3ps:getMessage('영문 명')}", width:450, editor:"text", sort:"string",
				editParse: function(value){  
					var gridList = $$("grid_wrap").serialize(true);
					var selectItem = $$("grid_wrap").getEditor();
		        	var engNameFlagInt = 0;
		        	var engNameFlagInt2 = 0;
		        	
		        	if(selectItem.value == value){
		        		engNameFlagInt2 = 1;
		        	}else{
		        		engNameFlagInt2 = 0;
		        	}
		        	
					if(value != null){
				        for(var i = 0; i < gridList.length; i++){
				        	if(gridList[i].engName == value){
				        		++engNameFlagInt;
				        	};
				        }
				        if( engNameFlagInt > engNameFlagInt2 ){
					           text = value + "값은 고유값이 아닙니다.(이미 존재함)다른 값을 입력해 주십시오.";
					           webix.message({ type:"error", text:text }); 
					           return;
				        } 
		        	}
					return value;
				}
			},
			{ id:"sort", header:"${e3ps:getMessage('소트')}", width:200, editor:"text", sort:"int",
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
				template:custom_checkbox, width:200, editor:"inline-checkbox"
			},
		],
		onClick:{
		    "wxi-trash":function(e, id){
		      if(id.column=="delete"){
		        removeFromSpan.call(this, id.row, id.column);
		        return false;
		      }
		    }
		},
	    editable:true,
	    editaction:"dblclick",
	    checkboxRefresh:true,
	    /* rules:{
	    	code:function(obj){ 
	        	var gridList = $$("grid_wrap").serialize(true);
	        	var flagInt = 0;
	        	
	        	if(obj==""){
	        		return false;
	        	}else{
		        	for(var i = 0; i < gridList.length; i++){
		        		if(gridList[i].code == obj){
		        			flagInt++;
		        		};
		        	}
		        	return flagInt < 2; 
	        	}
	        },
	        
			name:function(obj){ 
	        	var gridList = $$("grid_wrap").serialize(true);
	        	var flagInt = 0;
	        	var findString = "/"; 
	        	if(obj==""){
	        		return false;
	        	}else{
		        	for(var i = 0; i < gridList.length; i++){
		        		if(gridList[i].name == obj){
		        			flagInt++;
		        		};
		        	}
		        	return flagInt < 2; 
	        	}
	        	if(obj.indexOf(findString) != -1) {
	        		return false;
	        	}
	        },
	        
	        engName:function(obj){ 
	        	var gridList = $$("grid_wrap").serialize(true);
	        	var flagInt = 0;
	        	if(!obj){
	     			return true;
	        	}else{
		        	for(var i = 0; i < gridList.length; i++){
		        		if(gridList[i].engName == obj){
		        			flagInt++;
		        		};
		        	}
		        	return flagInt < 2; 
	        	}
	        },
	        
	        sort:webix.rules.isNumber,
	        
	        sort:function(obj){ 
	        	var numCheck = obj%2;
	        	
	        	if(obj<0){
	     			return false;
	        	}
	        	
	        	if(numCheck == 1 || numCheck == 0){
	        		return true;
	        	}else{
	        		return false;
	        	}
	        },
	    }, */
	    on:{
	    	//Header Click
			onHeaderClick:function(header, event, target) {
				var column = $$("grid_wrap").getColumnConfig(header.column);
				
				if("server" === column.sort) {
					$("#sessionId").val("");
				}
			},
			
			onItemClick: docTypeManagement_itemClick,
			onAfterEditStart: onAfterEditStart,
			
			//No.
	        "data->onStoreUpdated":function(){
	            this.data.each(function(obj, i){
	                obj.index = i+1;
	            });
	        },
	        
	        /* //Validation Error 메시지
	        onValidationError:function(key, obj){
	        	var gridList = $$("grid_wrap").serialize(true);
	        	var codeFlagInt = 0;
	        	var nameFlagInt = 0;
	        	var engNameFlagInt = 0;
	        	var findString = "/"; 
				text = "sort는 숫자만 입력이 가능합니다.";
				
	        	if(obj.code == ""){
	        		text = "필수 필드는 반드시 값을 입력해야 합니다.";
	        	}else{
	        		for(var i = 0; i < gridList.length; i++){
		        		if(gridList[i].code == obj.code){
		        			codeFlagInt++;
		        		};
		        	}
		        	if( codeFlagInt > 1 ){
			            text = obj.code + "값은 고유값이 아닙니다.(이미 존재함)다른 값을 입력해 주십시오.";
		        	} 
	        	}
	        	
	        	if(obj.name==""){
	        		text = "필수 필드는 반드시 값을 입력해야 합니다.";
	        	}else{
		        	for(var i = 0; i < gridList.length; i++){
		        		if(gridList[i].name == obj.name){
		        			nameFlagInt++;
		        		};
		        	}
		        	if( nameFlagInt > 1 ){
			            text = obj.name + "값은 고유값이 아닙니다.(이미 존재함)다른 값을 입력해 주십시오.";
		        	} 
	        	}
	        	if(obj.name.indexOf(findString) != -1) {
	        		text = "/(슬래시)는 입력할 수 없습니다.";
	        	}
	        	
	        	if(obj.engName != null){
			        for(var i = 0; i < gridList.length; i++){
			        	if(gridList[i].engName == obj.engName){
			        		engNameFlagInt++;
			        	};
			        }
			        if( engNameFlagInt > 1 ){
				           text = obj.engName + "값은 고유값이 아닙니다.(이미 존재함)다른 값을 입력해 주십시오.";
			        } 
	        	}
	        	
	        	var numCheck = (obj.sort)%2;
	        	if(obj.sort<0){
	        		text = "소트는 자연수만 입력이 가능합니다.";
	        	}
	        	if( numCheck != 1 && numCheck != 0 ){
	        		text = "소트는 자연수만 입력이 가능합니다.";
	        	}
	        	
	            webix.message({ type:"error", text:text }); 
	        } */
	    },
	    hover:"webix_datatable_hover",
		select:"cell",
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
	
});

var gridCount = 0;

function custom_checkbox(obj, common, value){
    if (value)
      return "<div class='webix_table_checkbox webix_datatable_checked'> YES </div>";
    else
      return "<div class='webix_table_checkbox webix_datatable_notchecked'> NO </div>";
};

//그리드 데이터 불러오기
function getGridData() {
	
	$("#searchForm").attr("action", getURLString("/admin/getDocCodeList"));

	formSubmit("searchForm", null, null, function(data){

		// 그리드 데이터
		var gridData = data.list;
		console.log("gridData =" + gridData);
		$("#total").html(gridData.length);
		
		// 그리드에 데이터 세팅
		$$("grid_wrap").clearAll();
		$$("grid_wrap").parse(gridData);
		//$$("grid_wrap").hideColumn("delete");
		gridCount = $$("grid_wrap").count(); 
	});
	
}

//셀 클릭 핸들러
function docTypeManagement_itemClick(id) {
	var dataField = event.dataField;
	let item = $$("grid_wrap").getItem(id);
	var oid = item.oid;
	var param = new Object();
	
	param["docTypeOid"] = oid;
	
	if(!(oid==null)){
		var url = getURLString("/admin/searchDocCodeToValueAction");
		ajaxCallServer(url, param, function(data){
			// 그리드 데이터
			var gridData = data.list;
			console.log("gridData =" + gridData);
			$("#totalTypeValue").html(gridData.length);
			
			// 그리드에 데이터 세팅
			$$("docCodeToValue_grid").clearAll();
			$$("docCodeToValue_grid").parse(gridData);
			
			$("#saveDefinition").removeAttr("onclick");
			$("#saveDefinition").attr("onclick", "saveDefinition('"+data.docTypeOid+"')");
			
		});
		getDocValueData();
	}else{
		$$("docCodeToValue_grid").clearAll();
		$("#saveDefinition").removeAttr("onclick");
		$("#saveDefinition").attr("onclick", "saveDefinition()");
	}
	
}

//검색
function search(){
	getGridData();
}

//검색조건 초기화
function reset(){
	$("#searchForm")[0].reset();
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
	item["delete"] = true;
	item["newItem"] = true;
	
	$$("grid_wrap").add(item);
	item.index = $$("grid_wrap").count(); 
	$$("grid_wrap").updateItem(item.id, item);
	$$("grid_wrap").showCell(item.id,"code");
}

//저장 버튼
function save() {
	var codeValid = true;
	$$("grid_wrap").editStop();
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
	
	//그리드 리스트
	var gridList = $$("grid_wrap").data.serialize(true);
	
	//수정된 아이템들
	var editedItemList = $$("grid_wrap").data.serialize(true);
	
	//중복 확인
	for(var i=0; i < editedItemList.length-1; i++){
		for(var j=i+1; j < editedItemList.length; j++) {
			if(editedItemList[i].code == editedItemList[j].code) {
				openNotice("중복되는 코드가 있습니다.");
				return;
			}
		}
	}
	
	var param = new Object();
	
	param["editedItemList"] = editedItemList;
	
	var url = getURLString("/admin/saveDocCodeAction2");
	ajaxCallServer(url, param, function(data){
		$$("grid_wrap").clearAll();
		getGridData();
	});
}

function loadIncludePage(tab) {
	
	if(tab == null) {
		tab = $(".tap>ul>li:first");
	}
	
	$(".tap ul li").removeClass("on");
	
	$(tab).addClass("on");
	
	var url = $(tab).data("url");
	
	var param = new Object();
	
	param["code"] = "ROOT";
	
	$("#includePage").load(url, param);
}

function onAfterEditStart(cell) {
	var id = cell.row;
	var column = cell.column;
	var item = $$("grid_wrap").getItem(id);
	var codeValue = item.code;
	if(codeValue){
		if(column == "code" && item.newItem != true){
			$$("grid_wrap").editStop();
		}
	}
	
}

function removeFromSpan(del_row, del_col){  
	  this.remove(del_row)
}
</script>