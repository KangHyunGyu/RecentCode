<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- pop -->
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('유저 검색')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->
	<div class="semi_content">
		<div class="con semi_table1 ml10 mt10" >
			<jsp:include page="${e3ps:getIncludeURLString('/department/include_departmentTree')}" flush="true">
				<jsp:param name="code" value="00000"/>
				<jsp:param name="autoGridHeight" value="false"/>
				<jsp:param name="gridHeight" value="600"/>
			</jsp:include>
		</div>
		<div class="semi_content2">
			<!-- button -->
			<div class="seach_arm pt10">
				<div class="leftbt">
				</div>
				<div class="rightbt">
					<button type="button" class="s_bt03" id="searchBtn" onclick="preRequestData();">검색</button>
					<button type="button" class="s_bt05" id="resetBtn" onclick="reset();">${e3ps:getMessage('초기화')}</button>
				</div>
			</div>
			<!-- //button -->
			<!-- pro_table -->
			<div class="semi_table2 pt10">
				<div class="table_list">
					<form name="searchForm" id="searchForm" style="margin-bottom:0">
						<input type="hidden" name="perOid" id="perOid" value="${perOid }" />
						<input type="hidden" name="departmentOid" id="departmentOid" value="" />
						<table summary="">
							<caption></caption>
							<colgroup>
								<col style="width:10%">
								<col style="width:20%">
								<col style="width:10%">
								<col style="width:20%">
								<col style="width:20%">
								<col style="width:20%">
							</colgroup>
							
							<tbody>
								<tr>
									<th scope="col">${e3ps:getMessage('아이디')}</th>
									<td><input type="text" id="userId" name="userId" class="w100" /></td>
									<th scope="col">${e3ps:getMessage('이름')}</th>
									<td><input type="text" id="userName" name="userName" class="w100" /></td>
									<th scope="col">${e3ps:getMessage('재직 여부')}</th>
									<td>
										<select class="w100" id="isDisable" name="isDisable" >
											<option value="">${e3ps:getMessage('전체')}</option>
											<option value="false">${e3ps:getMessage('재직')}</option>
											<option value="true">${e3ps:getMessage('퇴사')}</option>
										</select>
									</td>
								</tr>
							</tbody>
						</table>
					</form>
				</div>
			</div>
			<!-- //pro_table -->
			<!-- table list-->
			<div class="table_list">
				<!-- button -->
				<div class="seach_arm2 pt5 pb5">
					<div class="leftbt">검색결과 (<span id="total">0</span> 개)</div>
					<div class="rightbt">
						<button type="button" class="s_bt03" onclick="javascript:selectUser()">${e3ps:getMessage('선택')}</button>
					</div>
				</div>
				<!-- //button -->
				<div class="list" id="grid_wrap"></div>
			</div>
			<!-- //table list-->
		</div>
	</div>
</div>		

<!-- //pop-->
<script type="text/javascript">
if (!webix.env.touch && webix.env.scrollSize)
	  webix.CustomScroll.init();
webix.ready(function(){
	
	let checkHeader = { content:"masterCheckbox" };
	
	let grid = webix.ui({
		view:"datatable",
		container:"grid_wrap",
		id:"grid_wrap",
		clipboard:"block",
		multiselect:true,
		select:"cell",
		dragColumn: true,
		scroll:"xy",
		sort:"multi",
		yCount: 13,
		tooltip:true,
		resizeColumn:true,
		hover:"webix_datatable_hover",
		columns:[
			{ 
				id:"index",
				header:"No.",
				width:40,
				tooltip:false,
				css: "custom_webix_center",		
			},
			{ 
				id:"check",
				header:"",
				editor:"checkbox",
				template: custom_checkbox,
				header: checkHeader,
				width:40,
				tooltip:false,  
				css: "custom_webix_center",			
			},
			{ 
				id:"userId",
				header:"아이디",
				width:120,
				sort:"string",
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ 
				id:"name",
				header:"이름",
				width:100,
				sort:"string",
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ 
				id:"duty",
				header:"직위",
				width:100,
				sort:"string",
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ 
				id:"departmentName",
				header:"부서",
				width:100,
				sort:"string",
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ 
				id:"email",
				header:"이메일",
				fillspace:true,
				sort:"string",
				css: "custom_webix_ellipsis custom_webix_center",		
			},
		],
		on:{
			onHeaderClick:function(header, event, target) {
				let column = $$("grid_wrap").getColumnConfig(header.column);
				if("index" === header.column) {
					return false;
				}
			},
	        "data->onStoreUpdated":function(){
	            this.data.each(function(obj, i){
	                obj.index = i+1;
	            });
	        },
	        
	    },
	});
	
	webix.event(window, "resize", function(){ grid.adjust(); });
	
	webix.extend($$("grid_wrap"), webix.ProgressBar);
	
	$(".webix_excel_filter").click(function(){
		 $$("grid_wrap").unselect(); 
	});
	
	preRequestData();
});

$(document).ready(function(){
	//enter key pressed event
	$("#searchForm").keypress(function(e){
		if(e.keyCode==13){
			preRequestData();
		}
	});
	
});

//최초 데이터 요청
function preRequestData() {
	let param = new Object();
	
	let paramArray = $("#searchForm").serializeArray();
	
	$(paramArray).each(function(idx, obj){
		param[obj.name] = obj.value;
	});
	
	$$("grid_wrap").clearAll();
	var url = getURLString("/admin/getAclUserListAction");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		let gridData = data.list;
		
		$("#total").html(gridData.length);
		
		// 그리드에 데이터 세팅
		$$("grid_wrap").parse(gridData);;
	});
}

function xlsxExport() {
	webix.toExcel($$("grid_wrap"), {
	   	filename: "xlsxExport",
	   	rawValues:true,
	});
}

//검색조건 초기화
function reset(){
	$("#searchForm")[0].reset();
}

function selectUser(){
	let param = new Object();
	let checkItemList = $$("grid_wrap").find(function(obj) {
		return obj.check == true && !obj.authSelected;
	});
	
	if(checkItemList.length == 0) {
		openNotice("${e3ps:getMessage('유저를 선택하세요.')}");
		return;
	}
	param.checkItemList = checkItemList;
	param = getFormParams("searchForm", param);
	console.log(param);
	
	let url = getURLString("/admin/addMasterAclAction");
	ajaxCallServer(url, param, function(data){
		let onTab = opener.window.document.querySelector("div .tap>ul>li.on");
		opener.window.loadIncludePage(onTab);
		window.close();
	}, true);
}

function custom_checkbox(obj, common, value){
	if(obj.authSelected){
		return "<input class='webix_table_checkbox' type='checkbox' checked disabled />";
	}
	
	if (value){
		return "<input class='webix_table_checkbox' type='checkbox' checked  />";
	}else{
		return "<input class='webix_table_checkbox' type='checkbox' />";
    }
};

</script>