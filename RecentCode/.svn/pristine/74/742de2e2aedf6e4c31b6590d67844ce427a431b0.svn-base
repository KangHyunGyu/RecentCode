<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt">
		<span class="title"><img id="searchPartBtn" class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/add_icon.png">${e3ps:getMessage('부품 검색')}</span>
		<button type="button" class="s_bt03" onclick="searchPart();">${e3ps:getMessage('검색')}</button>
	</div>
	<div class="rightbt">
	</div>
</div>
<!-- //button -->
<div class="list" id="search_part_grid_wrap" style="display:none;">
</div>
<script>
if (!webix.env.touch && webix.env.scrollSize)
	  webix.CustomScroll.init();
	  
webix.ready(function(){
	
	var grid = webix.ui({
		view:"datatable",
		container:"search_part_grid_wrap",
		id:"search_part_grid_wrap",
		clipboard:"block",
		multiselect:true,
		select:"cell",
		dragColumn: true,
		multiselect : true,
		scroll:"xy",
		yCount: 5,
		tooltip:true,
		resizeColumn:true,
		columns:[
			{ id:"index",   header:"No.",     width:40,	tooltip:false,
				css: "custom_webix_center",		
			},
			{ id:"select",   header:"${e3ps:getMessage('선택')}",     width:70,	tooltip:false,
				css: "custom_webix_center",	
				template:function(obj){
					var template = "<button type='button' onclick='select(" + obj.id + ");'>${e3ps:getMessage('선택')}</button>";
					return template;
				}	
			},
			{ id:"icon", header:"",	width:30, tooltip:false,
				css:"custom_webix_imgCenter"
			},
			{ id:"number", header:["${e3ps:getMessage('부품 번호')}"] , sort:"server", width:180,
				css: "custom_webix_ellipsis",
				template:function(obj){
					return "<a href='javascript:openView(\"" + obj.oid+ "\")'>" + obj.number + "</a>"
				}
			},
			{ id:"name", header:["${e3ps:getMessage('부품 명')}"] , fillspace:1, minWidth:180, sort:"server",
				css: "custom_webix_ellipsis",
				template:function(obj){
					return "<a href='javascript:openView(\"" + obj.oid+ "\")'>" + obj.name + "</a>"
				}	
			},
			{ id:"stateName", header:"${e3ps:getMessage('상태')}", width:80,  sort:"server",
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"rev", header:["${e3ps:getMessage('버전')}"] , width:60,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"creatorFullName", header:"${e3ps:getMessage('등록자')}", 	width:80,
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"createDateFormat", header:"${e3ps:getMessage('작성일')}", width:100,  sort:"server",
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"modifyDateFormat", header:"${e3ps:getMessage('최종 수정일')}", width:100,  sort:"server",
				css: "custom_webix_ellipsis custom_webix_center",
			},
		],
		on:{
			"data->onStoreUpdated":function(){
	            this.data.each(function(obj, i){
	                obj.index = i+1;
	            });
	        }
		}
	});
	
	webix.event(window, "resize", function(){ grid.adjust(); });
	
	webix.extend($$("search_part_grid_wrap"), webix.ProgressBar);
	
	$(".webix_excel_filter").click(function(){
		 $$("search_part_grid_wrap").unselect(); 
	});
});

function search_part_getGridData(){

	var param = new Object();
	
	var number = $("#number").val();
	param["number"] = number.substring(0,7);
	
	$$("search_part_grid_wrap").clearAll();
	$$("search_part_grid_wrap").showProgress();
	var url = getURLString("/part/searchPartListAction");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		$$("search_part_grid_wrap").parse(gridData);
	});
}

function searchPart() {
	
// 	if($("#upgCode").val() == null || $("#upgCode").val() == "") {
// 		openNotice("${e3ps:getMessage('UPG를 선택하여 주십시오.')}");
// 		return;
// 	}
	
	$("#search_part_grid_wrap").css("display","");
	$("#searchPartBtn").attr("src", "/Windchill/jsp/portal/images/minus_icon.png");
	search_part_getGridData();
}

function select(id) {
	
// 	if($("#upgCode").val() == null || $("#upgCode").val() == "") {
// 		openNotice("${e3ps:getMessage('UPG를 선택하여 주십시오.')}");
// 		return;
// 	}
	
	var item = $$("search_part_grid_wrap").getItem(id);
	
	var param = new Object();
	
	param["oid"] = item.oid;
	
	var url = getURLString("/part/getPartInfo");
	ajaxCallServer(url, param, function(data){
		var part = data.part;
		
		$("#name").val(part.name);
		$("#unit").val(part.unit);
		
		regedSelectPart(part.number);
		
		var attributes = part.attributes;
		
		for(key in attributes){
			if(key == "SPECIAL_ATTRIBUTE") {
				console.log(key);
				var specialAttr = attributes[key];
				$.each(specialAttr.split(","), function(i, e){
					$('#special_attribute')[0].sumo.selectItem(e);
				});
			}else if(key == "CERTIFICATION_REGULATIONS") {
				var certification = attributes[key];
				$.each(certification.split(","), function(i, e){
					$('#certification_regulations')[0].sumo.selectItem(e);
				});
			}else {
				$("#" + key.toLowerCase()).val(attributes[key]);
			}
		}
		
	});
	
	$("#search_part_grid_wrap").css("display","none");
}

function regedSelectPart(partNumber) {
	
	var partCode = partNumber.substring(2,5); // Part Code
	findCode("partCode", "PARTCODE", partCode); // Part Code
	
	var projectCode = partNumber.substring(5,7); // Project Code
	findCode("projectCode", "PROJECTCODE", projectCode); // Project Code
	
	
	setTimeout("setPartNumber();", 20);
}

function findCode(id, codeType, code) {
	
	var param = new Object();
	
	param.codeType = codeType;
	param.code = code;

	var url = getURLString("/common/getNumberCode");
	ajaxCallServer(url, param, function(data){
		var item = data.code;
		
		if(item != null) {
			$("#" + id).trigger("change");
			$("#" + id).append("<option value='" + item.code + "' selected>" + item.code + " : "+ item.name + "</option>");
			$("#" + id).val(item.code);
		} else {
			$("#" + id).val("");
			$("#" + id).empty();
			$("#" + id).trigger("change");
		}
	}, false);
}
</script>