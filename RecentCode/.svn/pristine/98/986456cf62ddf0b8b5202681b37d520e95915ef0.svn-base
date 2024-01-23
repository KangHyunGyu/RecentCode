<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
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
	</div>
</div>
<!-- //button -->
<div class="list" id="add_${pageName}_grid_wrap">
</div>
<script>
if (!webix.env.touch && webix.env.scrollSize)
	  webix.CustomScroll.init();
	  
webix.ready(function(){
	
	var yCount = 10;
	if("${type}" == "single") {
		yCount = 1;
	}
	
	var grid = webix.ui({
		view:"datatable",
		container:"add_${pageName}_grid_wrap",
		id:"add_${pageName}_grid_wrap",
		select:"row",
		multiselect : true,
		scroll:"xy",
		yCount: yCount,
		tooltip:true,
		editable:true,
		autoConfig:false,
		columns:[
			{ id:"index",   header:"No.",     width:40,	tooltip:false,
				css: "custom_webix_center",		
			},
			{ id:"check", header:{ content:"masterCheckbox" }, editor:"checkbox",  template:"{common.checkbox()}", width:40, tooltip:false,  },
			{ id:"name", header:["${e3ps:getMessage('이름')}"] , width:200,
				css: "custom_webix_ellipsis",
				editor:"text",
				cssFormat: function(value, config){
					return "custom_webix_editable";
				}
			},
			{ id:"startDate", header:["${e3ps:getMessage('시작일')}"] , width:120,
				css: "custom_webix_ellipsis custom_webix_center",	
				editor:"date",
				format:webix.Date.dateToStr("%Y-%m-%d"),
				cssFormat: function(value, config){
					return "custom_webix_editable";
				}	
			},
			{ id:"endDate", header:["${e3ps:getMessage('종료일')}"] , width:120,
				css: "custom_webix_ellipsis custom_webix_center",	
				editor:"date",
				format:webix.Date.dateToStr("%Y-%m-%d"),
				cssFormat: function(value, config){
					return "custom_webix_editable";
				}	
			},
			{ id:"remark", header:["${e3ps:getMessage('설명')}"] , fillspace:1, minWidth:180,
				css: "custom_webix_ellipsis",
				editor:"text",
				cssFormat: function(value, config){
					return "custom_webix_editable";
				}
			},
		],
		on:{
			onAfterEditStop:function(state, editor, ignoreUpdate) {
				
				var table = $$("add_${pageName}_grid_wrap");
				var count = 0;
				var t1 = state.value;
				var row = table.getItem(editor.row);
				var startString = "";
				var endString = "";
				if(editor.column == "startDate"){
					if(undefined != t1 && "" != t1){
						console.log(t1);
						t1 = dateToString(t1);
						var res = table.find(function(obj){
							var t2 = obj.startDate;
							if(undefined == t2){
								t2 = "nd";
							}else{
								t2 = dateToString(t2);
							}
							if(t1 == t2){
								count++;
							}
			            });
						if(count > 1){
							row.startDate = "";
							webix.alert({
							    title:"시작일 중복",
							    ok:"OK",
							    text:"시작일이 중복되었습니다."
							});
							
						}
					}
					if(undefined != row.startDate && "" != row.startDate){
						startString = dateToString(row.startDate);
					}
					if(undefined != row.endDate && "" != row.endDate){
						endString = dateToString(row.endDate);
					}
					if("" != endString){
						if(startString >= endString){
							row.startDate = "";
							webix.alert({
							    title:"시작일",
							    ok:"OK",
							    text:"시작일이 종료일과 같거나 늦을 수 없습니다."
							});
						}
					}
					
				}
				if(editor.column == "endDate"){
					if(undefined != row.startDate && "" != row.startDate){
						startString = dateToString(row.startDate);
					}
					if(undefined != row.endDate && "" != row.endDate){
						endString = dateToString(row.endDate);
					}
					if("" != startString){
						if(startString >= endString){
							row.endDate = "";
							webix.alert({
							    title:"종료일",
							    ok:"OK",
							    text:"종료일이 시작일과 같거나 빠를 수 없습니다."
							});
						}
					}
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
		    	if("number" !== selection.column) {
			    	item.check = true;
		    	}
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
	//get grid data
	if("${oid}".length > 0) {
		add_${pageName}_getGridData();
	}else{
		addDefaultRows();
	}
});
function addDefaultRows(){
	$$("add_${pageName}_grid_wrap").add({
		index:1,
		name:"Award",
	});
$$("add_${pageName}_grid_wrap").add({
		index:1,
		name:"Proto출도",
	});
$$("add_${pageName}_grid_wrap").add({
		index:1,
		name:"Proto",
	});
$$("add_${pageName}_grid_wrap").add({
		index:1,
		name:"DV",
	});
$$("add_${pageName}_grid_wrap").add({
		index:1,
		name:"ESIR",
	});
$$("add_${pageName}_grid_wrap").add({
		index:1,
		name:"양산도 출도",
	});
$$("add_${pageName}_grid_wrap").add({
		index:1,
		name:"양금착수",
	});
$$("add_${pageName}_grid_wrap").add({
		index:1,
		name:"품확 마스터카",
	});
$$("add_${pageName}_grid_wrap").add({
		index:1,
		name:"PV",
	});
$$("add_${pageName}_grid_wrap").add({
		index:1,
		name:"P1",
	});
$$("add_${pageName}_grid_wrap").add({
		index:1,
		name:"P2",
	});
$$("add_${pageName}_grid_wrap").add({
		index:1,
		name:"인증완료",
	});
$$("add_${pageName}_grid_wrap").add({
		index:1,
		name:"ISIR",
	});
$$("add_${pageName}_grid_wrap").add({
		index:1,
		name:"M",
	});
$$("add_${pageName}_grid_wrap").add({
		index:1,
		name:"SOP",
	});
}
function add_${pageName}_getGridData(){
	
	var param = {
			oid : "${oid}",
			objType : "${objType}",
			pageName : "${pageName}",
			fType : "${fType}",
		}
	var loadUrl = getURLString("/calendar/getDsList");
	window.startProgress();
	fetch(loadUrl, {
		method: "POST", 
		headers: {
	        "Content-Type": "application/json; charset=UTF-8",
	    },
	    body: JSON.stringify(param),
	}).then(function(response){
		return response.json();
	}).then(function(data){
		var gridData = data.list;
		$$("add_${pageName}_grid_wrap").parse(gridData);
		window.endProgress();
	});
}

//추가 버튼
function add_${pageName}_addRow() {
	
	if("${type}" == "single") {
		if($$("add_${pageName}_grid_wrap").count() > 0) {
			return; 
		}
	}
	var item = new Object();

	$$("add_${pageName}_grid_wrap").add(item);
}

//삭제 버튼
function add_${pageName}_removeRow() {
	//$$("add_${pageName}_grid_wrap").remove($$("add_${pageName}_grid_wrap").getSelectedId());
	var checkList = $$("add_${pageName}_grid_wrap").data.serialize(true);
	for(var i=0; i<checkList.length; i++) {
		if(checkList[i].check) {
			$$("add_${pageName}_grid_wrap").remove(checkList[i].id);			
		}
	}
}

function dateToString(date){
	if(Date == typeof(date)){
		var dd = date.getDate();
		var mm = date.getMonth()+1;

		var yyyy = date.getFullYear();
		if(dd<10){dd='0'+dd} if(mm<10){mm='0'+mm}
		
		yyyy = yyyy.toString();
		mm = mm.toString();
		dd = dd.toString();

		var s1 = yyyy+mm+dd;
		return s1;
	}else{
		return date;
	}
	
}
</script>