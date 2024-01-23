<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript">
$(document).ready(function(){
	
	loadIncludePage();
	
	//결재선 프로세스 표시 그리드
	//approvalProcess_createAUIGrid(approvalProcess_columnLayout);
	
	//approvalProcess_getGridData();
	
});

var approvalProcess_myGridID;

var approvalProcess_columnLayout = [
	{ 
		dataField : "localeNameDisplay", 
		headerText : "${e3ps:getMessage('업무 구분')}", 
		width:"25%",	
		style:"AUIGrid_Left", 
		filter : {
			showIcon : true,
			iconWidth:20
		}},
	{ 
		dataField : "localeDescription", 
		headerText : "${e3ps:getMessage('결재 프로세스')}", 
		width:"*",	
		style:"AUIGrid_Left",
		labelFunction : function(rowIndex, columnIndex, value, headerText, item){
			return value;
		},
		filter : {
			showIcon : true,
			iconWidth:20
		}},
];

//AUIGrid 를 생성합니다.
/* function approvalProcess_createAUIGrid(approvalProcess_columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
		selectionMode : "multipleCells",
		
		showSelectionBorder : true,
		
		noDataMessage : gridNoDataMessage,
		
		rowIdField : "_$uid",
		
		showRowNumColumn : true,
		
		showEditedCellMarker : false,
		
		wrapSelectionMove : true,
		
		showRowCheckColumn : false,
		
		enableFilter : true,
		
		enableMovingColumn : true,
		
		headerHeight : gridHeaderHeight,
		
		rowHeight : gridRowHeight,
		
		independentAllCheckBox : true,
		
	};

	// 실제로 #grid_wrap 에 그리드 생성
	approvalProcess_myGridID = AUIGrid.create("#approvalProcess_grid_wrap", approvalProcess_columnLayout, gridPros);
	
	var gridData = new Array();
	AUIGrid.setGridData(approvalProcess_myGridID, gridData);
} */

/* function approvalProcess_getGridData(){
	
	var param = new Object();
	param.codeType = "APPROVALPROCESS";
	
	var list = getNumberCodeDataList(null, param, null, null, null);
	
	AUIGrid.setGridData(approvalProcess_myGridID, list);
	
} */

function applyAppLine(){
	openConfirm("${e3ps:getMessage('기존 결재선을 삭제하고 현재 결재선을 추가하시겠습니까?')}", function(){
		var list = new Array();
		
		$(".approvalLineGrid").each(function(){
			var appLineList = AUIGrid.getGridData("#" + $(this).attr("id"));

			for(var i=0; i < appLineList.length; i++){
				appLineList[i]["roleType"] = $(this).data("roletype");
				list.push(appLineList[i]);
			}
		});
		
		if (opener.window.app_line_getAppLineFromPopup) {
			opener.window.app_line_getAppLineFromPopup(list);
			
			self.close();
		}
	});
}

function openTitleWindow(){
	var width = $("#appLineTitlePopup").width();
	var height = $("#appLineTitlePopup").height();
	
	$("#appLineTitlePopup").css("position","absolute");
	$("#appLineTitlePopup").css("top",(window.innerHeight/2)-(height/2));
	$("#appLineTitlePopup").css("left",(window.innerWidth/2)-(width/2));
	$("#appLineTitlePopup").show();
}

function closeTitleWindow(){
	$("#appLineTitlePopup").hide();
}

function createTemplate(){
	var title = $("#appLineTitle").val();
	
	if(title != null && title.length > 0) {
		if(confirm("${e3ps:getMessage('저장 하시겠습니까?')}"))
		var param = new Object();
		
		$(".approvalLineGrid").each(function(){
			var appLineList = AUIGrid.getGridData("#" + $(this).attr("id"));
			
			param[$(this).data("roletype")] = appLineList;
			
			//console.log(appLineList);
			
		});
		
		param["title"] = title;
		
		var url = getURLString("/approval/createTemplateAction");
		
		ajaxCallServer(url, param, function(data){
			$("#appLineTitlePopup").hide();
			getAppLineTemplate();
		});
		
	} else {
		openNotice("${e3ps:getMessage('결재 라인 이름을 입력하세요.')}");
	}
}

function loadIncludePage(tab) {
	
	if(tab == null) {
		tab = $(".tap>ul>li:first");
	}
	
	$(".tap ul li").removeClass("on");
	
	$(tab).addClass("on");
	
	var url = $(tab).data("url");
	
	var param = new Object();
	
	$("#includePage").load(url, param);
}
</script>
<!-- pop -->
<div class="pop">
<form id="addApprovalLineForm" name="addApprovalLineForm">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('결재선 지정')}</h2>
		<span class="close">
			<a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a>
		</span>
	</div>
	
	<!--tap -->
	<div class="tap pt20">
		<ul>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/approval/include_orgUserList')}">${e3ps:getMessage('조직')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/approval/include_appLineTemplate')}">${e3ps:getMessage('결재선 Template')}</li>
		</ul>
		<div class="tapbutton">
			<button type="button" class="s_bt03" style="120" onclick="applyAppLine();">${e3ps:getMessage('결재선 적용')}</button>
			<button type="button" class="s_bt03" style="120" onclick="openTitleWindow();">${e3ps:getMessage('Template 저장')}</button>
		</div>
	</div>
	<!--//tap -->
	   
	<div class="pop_semi">
		<div class="con pop_semitable1 mt20 mr10" id="includePage">
		</div>
		<%-- <div class="pop_semitable2 mt200 mb200 pl30 pr30">
			<h3>${e3ps:getMessage('결재 방법')}</h3>
			<ul>
				<li>
					<input type="radio" name="_listUp" value="discuss">
					<label>${e3ps:getMessage('합의')}</label>
				</li>
				<li>
					<input type="radio" name="_listUp" value="approve" checked>
					<label>${e3ps:getMessage('결재')}</label>
				</li>
				<li>
					<input type="radio" name="_listUp" value="receive">
					<label>${e3ps:getMessage('수신')}</label>
				</li>
			</ul>

			<ul class="pt20">
				<li><button type="button" class="s_bt03">${e3ps:getMessage('추가')}</button></li>
				<li><button type="button" class="s_bt03">${e3ps:getMessage('삭제')}</button></li>
				<li><button type="button" class="s_bt03">${e3ps:getMessage('모두 삭제')}</button></li>
			</ul>
		</div> --%>
		<div class="pop_semitable3 mt20 ml10">
			<jsp:include page="${e3ps:getIncludeURLString('/approval/include_approvalLineGrid')}" flush="true">
				<jsp:param name="title" value="${e3ps:getMessage('합의')}"/>
				<jsp:param name="roleType" value="DISCUSS"/>
			</jsp:include>
			<jsp:include page="${e3ps:getIncludeURLString('/approval/include_approvalLineGrid')}" flush="true">
				<jsp:param name="title" value="${e3ps:getMessage('승인')}"/>
				<jsp:param name="roleType" value="APPROVE"/>
			</jsp:include>
			<jsp:include page="${e3ps:getIncludeURLString('/approval/include_approvalLineGrid')}" flush="true">
				<jsp:param name="title" value="${e3ps:getMessage('수신')}"/>
				<jsp:param name="roleType" value="RECEIVE"/>
			</jsp:include>	
		</div>
	</div>
</form>

<!-- <div class="pop_semitable1 mr20 mt20">
	<div class="list" id="approvalProcess_grid_wrap" style="height:200px"></div>
</div> -->

</div>
<!-- pop -->
<div id="appLineTitlePopup" class="pop2">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('결재 라인 이름 입력')}</h2>
		<span class="close"><a href="javascript:closeTitleWindow()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->   
	<!-- pro_table -->
	<div class="pro_table pt20 pl25 pr25 pb15">
		<table class="mainTable">
			<colgroup>
				<col style="width:40%">
				<col style="width:*%">							
			</colgroup>	
			<tbody>
				<tr>
					<th scope="col">${e3ps:getMessage('결재 라인 이름')}</th>
					<td><input type="text" id="appLineTitle" class="w100"></td>							
				</tr>						
			</tbody>
		</table>	
	</div>
	<!-- //pro_table -->
	<!-- button -->
	<div class="bt_arm pb20 ">				
		<div class="rightbt">
			<button type="button" class="s_bt03" onclick="createTemplate()">${e3ps:getMessage('확인')}</button>
		</div>
	</div>
	<!-- //button -->	
</div>
