<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('변경 문서')}</h4></div>
	<div class="rightbt">
<%-- 		<button type="button" class="s_bt03" onclick="rel_epm_resetFilter();">${e3ps:getMessage('필터 초기화')}</button> --%>
<%-- 		<button type="button" class="s_bt03" onclick="rel_epm_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button> --%>
	</div>
</div>
<!-- //button -->
<div>
<div class="list" id="ecaDoc_grid_wrap" style="width:49%;float: left;">
</div>
<div class="list" id="ecaDoc_grid_wrap2"style="width:50%;float: right;">
</div>
<c:if test="${isEdit eq 'true'}">
<div>
	<div style="width:49%;float: left; text-align: right;">
		<input type="button" class="s_bt03" value="직접작성" onclick="openCreateDoc2('${oid }','OLD','')">
        <input type="button" class="s_bt03" value="링크등록" onclick="inputLinkOutput('${oid }','OLD')">
	</div>
	<div style="width:50%;float: right;text-align: right;">
		<input type="button" class="s_bt03" value="직접작성" onclick="openCreateDoc2('${oid }','NEW','')">
        <input type="button" class="s_bt03" value="링크등록" onclick="inputLinkOutput('${oid }','NEW')">
	</div>
</div>
</c:if>
</div>
<script>
if (!webix.env.touch && webix.env.scrollSize)
	  webix.CustomScroll.init();
	  
webix.ready(function(){
	var grid = webix.ui({
		view:"datatable",
		container:"ecaDoc_grid_wrap",
		id:"ecaDoc_grid_wrap",
		select:"row",
		scroll:"xy",
		sort:"multi",
		yCount: 5,
		tooltip:true,
		resizeColumn:true,
		columns:[
			{ id:"o_number",  header:[
	            {text:"${e3ps:getMessage('변경 전')}",colspan:5},
	            "${e3ps:getMessage('문서번호')}",
	        ], width:150,	tooltip:false,
				css: "custom_webix_ellipsis",
				template:function(obj){
					return "<a href='javascript:openView(\"" + obj.o_oid+ "\")'>" + obj.o_number + "</a>"
				}	
			},
			{ id:"o_name",	header:[
	            {text:""},
	            "${e3ps:getMessage('제목')}",
	        ], 	fillspace:1.5,
				css: "custom_webix_ellipsis",
				template:function(obj){
					return "<p>" + obj.o_icon + obj.o_name + "</p>"
				}	
			},
			{ id:"o_version",	header:[
	            {text:""},
	            "${e3ps:getMessage('버전')}",
	        ], 	width:60,
				css:"custom_webix_ellipsis custom_webix_center",		
			},
			{ id:"o_state",	header:[
	            {text:""},
	            "${e3ps:getMessage('작업현황')}",
	        ], 	width:80,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"o_act",	header:[
	            {text:""},
	            "${e3ps:getMessage('')}",
	        ], 	width:40,
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
	if("false" === "${isEdit}"){
		$$("ecaDoc_grid_wrap").hideColumn("o_act");
	}
	webix.event(window, "resize", function(){ grid.adjust(); });
	
	webix.extend($$("ecaDoc_grid_wrap"), webix.ProgressBar);
	
	rel_epm_getGridData();
	
	var grid2 = webix.ui({
		view:"datatable",
		container:"ecaDoc_grid_wrap2",
		id:"ecaDoc_grid_wrap2",
		select:"row",
		scroll:"xy",
		sort:"multi",
		yCount: 5,
		tooltip:true,
		resizeColumn:true,
		columns:[
			{ id:"n_number", header:[
	            {text:"${e3ps:getMessage('변경 후')}",colspan:5},
	            "${e3ps:getMessage('문서번호')}",
	        ],	width:150,
				css: "custom_webix_ellipsis",
				template:function(obj){
					return "<a href='javascript:openView(\"" + obj.n_oid+ "\")'>" + obj.n_number + "</a>"
				}
			},
			{ id:"n_name", header:[
	            {text:""},
	            "${e3ps:getMessage('제목')}",
	        ],  fillspace:1.5,
				css: "custom_webix_ellipsis",
				template:function(obj){
					return "<p>" + obj.n_icon + obj.n_name + "</p>"
				}		
			},
			{ id:"n_version", header:[
	            {text:""},
	            "${e3ps:getMessage('버전')}",
	        ],	width:60,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"n_state", header:[
	            {text:""},
	            "${e3ps:getMessage('작업현황')}",
	        ],  width:80,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"n_act",	header:[
	            {text:""},
	            "${e3ps:getMessage('')}",
	        ], 	width:40,
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
	if("false" === "${isEdit}"){
		$$("ecaDoc_grid_wrap2").hideColumn("n_act");
	}
	webix.event(window, "resize", function(){ grid2.adjust(); });
	
	webix.extend($$("ecaDoc_grid_wrap2"), webix.ProgressBar);
	
	rel_epm_getGridData2();
});

function rel_epm_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	param.docType = "OLD";
	var url = getURLString("/change/getEcaDocOLD");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		$$("ecaDoc_grid_wrap").parse(gridData);
	});
}

function rel_epm_getGridData2(){

	var param = new Object();
	
	param.oid = "${oid}";
	param.docType = "NEW";
	var url = getURLString("/change/getEcaDocNEW");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		$$("ecaDoc_grid_wrap2").parse(gridData);
	});
}

//필터 초기화
function rel_epm_resetFilter(){
    
}

function rel_epm_xlsxExport() {
	webix.toExcel($$("ecaDoc_grid_wrap"), {
		filename: "xlsxExport",
		rawValues:true,
	});
}

var aoid = '';
var activeLinkType = '';
var activeLinkOid = '';
function inputLinkOutput(oid,alt, ao){
	aoid = oid;
	activeLinkType = alt;
	activeLinkOid = ao;
	var url = getURLString("/common/searchObjectPopup") + "?objType=doc&pageName=ecaDocument&type=multi";
	url += "&aoid="+aoid+"&activeLinkType="+activeLinkType+"&activeLinkOid="+activeLinkOid;
	openPopup(url, "searchObjectPopup",1400, 600);
}

function add_ecaDocument(data, a, b, c){
	var values = "";
	var list = JSON.parse(data);
	if(list.length > 0){
		for(var i=0;i<list.length;i++){
			if(i>0){
				values = values + ",";
			}
			values = values + list[i].oid;
		}
		$.ajax({
			type : "POST",
			url : "/Windchill/worldex/change/createActivityDocLink",
			data : {
				activeOid : a,
				activeLinkType : b,
				activeLinkOid : c,
				loid : values
			},
			dataType : "json",
			success : function (data, textStatus, jqXHR) {
				location.reload();
			},
			error : function (jqXHR, textStatus, errorThrown ) {
				alert(errorThrown);
			}
		});
	}
}

function openCreateDoc2(oid, activeLinkType, activeLinkOid){
	var url = getURLString("/doc/createDocPopup2") + "?objType=doc&pageName=ecaDocument";
	url += "&activeOid="+oid+"&activeLinkType="+activeLinkType+"&activeLinkOid="+activeLinkOid;
	openPopup(url, "createDocPopup");
}

function deleteActiveDocLink(loid,activeLinkType){

	if(!confirm("선택한 문서는 삭제 되지 않습니다. 해당 문서 와 설계변경활동과의 연결을 끊으시겠습니까?")){
		return;
	}
	// #. 요청
	$.ajax({
		type : "POST",
		url : "/Windchill/worldex/change/deleteActivityDocLink",
		data : {
			loid : loid,
			activeLinkType : activeLinkType
		},
		dataType : "json",
		success : function (data, textStatus, jqXHR) {
			alert("해당 문서와의 연결이 끊어졌습니다");
			location.reload();
		},
		error : function (jqXHR, textStatus, errorThrown ) {
			alert(errorThrown);
		}
	});
	
}
</script>