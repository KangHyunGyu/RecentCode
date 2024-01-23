<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- pop -->
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('도면 검색')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->

	<div class="pl25 pr25">
		<div class="seach_arm2 pt10 pb10">
			<div class="leftbt"></div>
			<div class="rightbt">
				<%-- <button type="button" class="s_bt03" id="switchDetailBtn" onclick="switchDetailBtn();">${e3ps:getMessage('상세검색')}</button> --%>
				<button type="button" class="s_bt03" id="searchBtn" onclick="search();">${e3ps:getMessage('검색')}</button>
				<button type="button" class="s_bt05" id="resetBtn" onclick="reset();">${e3ps:getMessage('초기화')}</button>
				<button type="button" class="s_bt03" onclick="javascript:addEpm()">${e3ps:getMessage('추가')}</button>
				<button type="button" class="s_bt04" onclick="javascript:window.close()">${e3ps:getMessage('닫기')}</button>
			</div>
		</div>
		<form name="searchForm" id="searchForm">
			<input type="hidden" id="location" name="location" value="">
			<input type="hidden" id="sessionId" name="sessionId" value="">
			<div class="pro_table">
				<table class="mainTable">
				<colgroup>
					<col style="width:10%">
					<col style="width:23%">
					<col style="width:10%">
					<col style="width:23%">
					<col style="width:10%">
					<col style="width:24%">
				</colgroup>	
				<tbody>
					<tr>
						<th scope="col">${e3ps:getMessage('도면 분류')}</th>
						<td>
							<div style="position:relative">
								<input type="text" id="locationDisplay" name="locationDisplay" class="w60" disabled> <button type="button" class="s_bt03" id="toggleBtn" onclick="toggleFolderList(this)">목록</button>
								<div id="folderList" style="display:none;position:absolute;z-index:999">
									<!-- tree -->
									<jsp:include page="${e3ps:getIncludeURLString('/common/include_folderTree')}" flush="true">
										<jsp:param name="container" value="worldex"/>
										<jsp:param name="renderTo" value="epmFolder"/>
										<jsp:param name="formId" value="searchForm"/>
										<jsp:param name="rootLocation" value="/Default"/>
										<jsp:param name="gridHeight" value="300"/>
										<jsp:param name="gridWidth" value="250"/>
									</jsp:include>
									<!-- //tree -->
								</div>
							</div>
						</td>
						<th scope="col">${e3ps:getMessage('도면 번호')}</th>
						<td><input type="text" id="number" name="number" class="w100"></td>
						<th scope="col">${e3ps:getMessage('도면 명')}</th>
						<td ><input type="text" id="name" name="name" class="w100"></td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('작성자')}</th>
						<td>
							<div class="pro_view">
								<select class="searchUser" id="creator" name="creator" multiple data-width="60%">
								</select>
								<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('creator', 'multi');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
								<span class="pointer verticalMiddle" onclick="javascript:deleteUser('creator');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
							</div>
						</td>
						<th scope="col">${e3ps:getMessage('작성일')}</th>
						<td class="calendar">
							<input type="text" class="datePicker w25" name="predate" id="predate" readonly/>
							~
							<input type="text" class="datePicker w25" name="postdate" id="postdate" readonly/>
						</td>
						<th scope="col">${e3ps:getMessage('버전')}</th>
						<td>
							<input type="radio" id="version" name="version" value="new" checked>
							<label>${e3ps:getMessage('최신 버전')}</label>
							<input type="radio" id="version" name="version" value="all">
							<label>${e3ps:getMessage('모든 버전')}</label>
						</td>
					</tr>	
					<tr>
						<th scope="col">${e3ps:getMessage('최종 수정자')}</th>
						<td>
							<div class="pro_view">
								<select class="searchUser" id="modifier" name="modifier" multiple data-width="60%">
								</select>
								<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('modifier', 'multi');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
								<span class="pointer verticalMiddle" onclick="javascript:deleteUser('modifier');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
							</div>
						</td>
						<th scope="col">${e3ps:getMessage('수정일')}</th>
						<td class="calendar">
							<input type="text" class="datePicker w25" name="predate_modify" id="predate_modify" readonly/>
							~
							<input type="text" class="datePicker w25" name="postdate_modify" id="postdate_modify" readonly/>
						</td>
						<th scope="col">${e3ps:getMessage('상태')}</th>
						<td>
							<div class="pro_view">
								<select class="multiSelect w40" id="state" name="state" multiple style="height:20px;overflow-y: hidden;">
								</select>
							</div>
						</td>						
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('관련 프로젝트')}</th>
						<td>
						<div class="pro_view">
							<select class="searchRelatedProject" id="relatedProject" name="relatedProject" multiple></select>
						</div>
						</td>
						<%-- <th scope="col">${e3ps:getMessage('CAD 구분')}</th>
						<td>
							<select class="multiSelect w30" id="cadDivision" name="cadDivision" multiple style="height:20px; overflow-y:hidden;">
							</select>
						</td> --%>
						<th scope="col">${e3ps:getMessage('CAD 타입')}</th>
						<td>
							<select class="multiSelect w30" id="cadType" name="cadType" multiple style="height:20px; overflow-y:hidden;">
							</select>
						</td>
						<th scope="col">${e3ps:getMessage('하위 폴더 미포함')}</th>
						<td>
							<input type="checkbox" id="searchChildFolder" name="searchChildFolder"  value="false" checked="checked"/>
						</td>					
					</tr>
					<tr class="switchDetail">
						<th>${e3ps:getMessage('고객사')}</th>
						<td>
							<select class="w100" id="customer" name="customer"></select>
						</td>
						<th>UPG</th>
						<td>
							<input type="text" class="w95" id="upg" name="upg">
						</td>
						<th>${e3ps:getMessage('확장자')}</th>
						<td>
							<select class="w100" id="extensions" name="extensions"></select>
						</td>
					</tr>
					<tr class="switchDetail">
						<th>${e3ps:getMessage('설계자')}</th>
						<td>
							<input type="text" class="w95" id="designed" name="designed">
						</td>
						<th>${e3ps:getMessage('고객품번')}</th>
						<td>
							<input type="text" class="w95" id="customer_part_number" name="customer_part_number">
						</td>
						<th>${e3ps:getMessage('EO번호')}</th>
						<td>
							<input type="text" class="w95" id="eo_number" name="eo_number">
						</td>
					</tr>
					<tr class="switchDetail">
						<th>${e3ps:getMessage('재질')}</th>
						<td>
							<input type="text" class="w95" id="material" name="material">
						</td>
						<th>${e3ps:getMessage('중량')}</th>
						<td>
							<input type="text" class="w95" id="weight" name="weight">
						</td>
						<th>${e3ps:getMessage('두께')}</th>
						<td>
							<input type="text" class="w95" id="thickness" name="thickness">
						</td>
					</tr>
					<tr class="switchDetail">
						<th>FINISH</th>
						<td>
							<input type="text" class="w95" id="finish" name="finish">
						</td>
						<th></th>
						<td></td>
						<th></th>
						<td></td>
					</tr>
				</tbody>
			</table>
			</div>
		</form>
	</div>
	<!-- button -->
	<div class="seach_arm pt5 pb5">
		<div class="leftbt">
			<span>
				${e3ps:getMessage('검색 결과')} (<span id="total">0</span>)
			</span>
		</div>
		<div class="rightbt">
<%-- 			<button type="button" class="s_bt03" onclick="reset();">${e3ps:getMessage('필터 초기화')}</button> --%>
			<button type="button" class="s_bt03" onclick="xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
		</div>
	</div>
	<!-- //button -->
	<!-- table list-->
	<div class="table_list">
		<div class="list" id="grid_wrap"></div>
	</div>
	<!-- //table list-->
</div>		
<!-- //pop-->
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
		container:"grid_wrap",
		id:"grid_wrap",
		select:"row",
		dragColumn: true,
		multiselect : multiSelect,
		scroll:"xy",
		yCount: 10,
		tooltip:true,
		resizeColumn:true,
		datafetch:100000,
		columns:[
			{ id:"index",   header:"No.",     width:40,	tooltip:false,
				css: "custom_webix_center",		
			},
			{ id:"check", header:checkHeader, editor:"checkbox",  width:40, tooltip:false,  
				template:function(obj, common, value, config){
					if("${moduleType}" == "multiApproval"){
						if(obj.select){
					        return common.checkbox(obj, common, value, config);
						}
					}else{
						if("${type}" == "single") {
							return common.radio(obj, common, value, config);
						}else {
							return common.checkbox(obj, common, value, config);
						}
					}
					return "";
		        }	
			},
			{ id:"icon", header:"" , sort:"string",width:40,	tooltip:false,
				css:"custom_webix_imgCenter",
			},
			{ id:"cadDivision", header:"${e3ps:getMessage('CAD 구분')}" , width:90,
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"number", header:["${e3ps:getMessage('도면 번호')}"] , width:200,sort:"server",
				css:"custom_webix_ellipsis custom_webix_center",
				template:function(obj){
					var oid = obj.oid;
					return "<a href='#' onclick='openView(\""+oid+"\")'>"+obj.name+"</a>";
				}
			},
			{ id:"rev", header:["${e3ps:getMessage('버전')}"] , width:60,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"thumbnail",  header:"", width:40,	tooltip:false,
				css:"custom_webix_imgCenter pointer",
				template:custom_webix_thumbnailTemplate
			},
			{ id:"name", header:["${e3ps:getMessage('도면 명')}"] , minWidth:180,	fillspace:true, sort:"server",
				css: "custom_webix_ellipsis",
				template:function(obj){
					return "<a href='javascript:openView(\"" + obj.oid+ "\")'>" + obj.name + "</a>"
				}
			},
			{ id:"location", header:["${e3ps:getMessage('부품 분류')}"] , width:180,
				css: "custom_webix_ellipsis",	
			},
			{ id:"stateName", header:"${e3ps:getMessage('상태')}", width:80,  sort:"server",
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"creatorFullName", header:"${e3ps:getMessage('작성자')}", 	width:80,
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"createDateFormat", header:"${e3ps:getMessage('작성일')}", width:100,  sort:"server",
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"modifyDateFormat", header:"${e3ps:getMessage('수정일')}", width:100,  sort:"server",
				css: "custom_webix_ellipsis custom_webix_center",
			},
		],
		on:{
			onHeaderClick:function(header, event, target) {
				var column = $$("grid_wrap").getColumnConfig(header.column);
				
				if("server" === column.sort) {
					$("#sessionId").val("");
				}
			},
			onCheck: function(row, column, state) {
		        //if (state) {
		        //    this.select(row, true);
		        //} else {
		        //    this.unselect(row);
		        //}
		        if (state) {
		        	if(!multiSelect){
		        		this.unselectAll();
		        	}
		        	var item = $$("grid_wrap").getItem(row);
		        	if(item.select){
		        		this.select(row, true);
		        	}
		        } else {
		            this.unselect(row);
		        }
		    },
		    onBeforeSelect: function(selection, preserve){
		    	var item = $$("grid_wrap").getItem(selection.row);
		    	
		    	if(item.select){
			    	item.check = true;
		    	}
			},
			onBeforeUnSelect:function(selection){
				var item = $$("grid_wrap").getItem(selection.row);
				if(item){
					if(item.select){
						item.check = false;
					}
				}
			},
			onItemClick:function(id, event, node) {
				
				let item = $$("grid_wrap").getItem(id);
				
				if("thumbnail" == id.column){
					openMiniWVSPopup(item.minPublishURL);
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
	
	webix.extend($$("grid_wrap"), webix.ProgressBar);
	
	//getGridData();
});

$(document).ready(function(){
	//enter key pressed event
	$("#searchForm").keypress(function(e){
		if(e.keyCode==13){
			search();
		}
	});
	
	//lifecycle list
	getLifecycleList("LC_Default");
	
	//Cad Division list
	getCadDivisionList();
	
	//Cad Type list
	getCadTypeList();
	
	getNumberCodeChildList("customer", "CUSTOMER", "", true, false, true);
	getNumberCodeList("extensions", "EXTENSIONS", false, true);
});

function getGridData(){
	
	$$("grid_wrap").load(function(params){
		
		if(!params){
			params = new Object();
			params["start"] = 0;
			params["count"] = $$("grid_wrap").config.datafetch;
			params["continue"] = true;
		}
		
		if("${moduleType}".length > 0) {
			params["moduleType"] = "${moduleType}";
		}
		
		getFormParams("searchForm", params);
		
		let response = ajaxCallServer(getURLString("/epm/searchEpmScrollAction"), params, null);
	      $("#total").html(response.total_count);
	      $("#sessionId").val(response.sessionId);
	      
	      return response;
		
// 		params = JSON.stringify(params);
// 		return webix.ajax().headers({
// 		    	"Content-Type": "application/json; charset=UTF-8"
// 		    }).post(getURLString("/epm/searchEpmScrollAction"), params)
// 		    .then(function(response){
// 		    	var data = response.json();
		    	
// 				$("#total").html(data.total_count);
// 				$("#sessionId").val(data.sessionId);
// 		    	return data;
// 		    });
	
	});
}

//검색
function search(){
	$$("grid_wrap").clearAll();
	$("#sessionId").val("");
	getGridData();
}

//검색조건 초기화
function reset(){
	var locationDisplay = $("#locationDisplay").val();
	$("#searchForm")[0].reset();
	$("#locationDisplay").val(locationDisplay);
}

//필터 초기화
function resetFilter(){
    
}

function xlsxExport() {
	webix.toExcel($$("grid_wrap"), {
		filename: "xlsxExport"
	});
}

function addEpm(){
	var selectedItem = new Array();
	
// 	var checkList = $$("grid_wrap").data.serialize(true);
// 	selectedItem = selectedItem.map(function(item){
// 		delete item.check;
// 		return item;
// 	});

	var checkList = $$("grid_wrap").getSelectedItem();
	if(checkList.length == undefined){
		selectedItem.push(checkList);
	}else{
		for(var i=0; i<checkList.length; i++) {
			selectedItem.push(checkList[i]);
		}
	}
	
	
	var listStringData = JSON.stringify(selectedItem);

	if(opener.window.add_${pageName}_addObjectList){
		opener.window.add_${pageName}_addObjectList(listStringData);
	} else if(opener.window.add_ecaEPMDocument){ // ECA 활동에서 도면 등록
		var aoid = "${aoid}";
		var activeLinkType = "${activeLinkType}";
		var activeLinkOid = "${activeLinkOid}";
		opener.window.add_ecaEPMDocument(listStringData, aoid, activeLinkType, activeLinkOid);
	} else if(opener.window.createMasteredLink){ // 프로젝트 부품, 도면 링크
		opener.window.createMasteredLink(listStringData);
// 		window.close();
	}
	
	if("${type}" == "single") {
		window.close();
	}
}
</script>