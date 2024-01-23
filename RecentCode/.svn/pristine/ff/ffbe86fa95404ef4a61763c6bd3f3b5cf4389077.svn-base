<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- dhtmlx gantt Component -->
<link rel="stylesheet" type="text/css" href="/Windchill/jsp/component/gantt_7.0.9_ultimate/codebase/dhtmlxgantt.css" />
<script type="text/javascript" src="/Windchill/jsp/component/gantt_7.0.9_ultimate/codebase/dhtmlxgantt.js" ></script>
<link rel="stylesheet" type="text/css" href="/Windchill/jsp/project/gantt/css/style.css">
<script type="text/javascript" src="/Windchill/jsp/project/gantt/js/zoom.js" ></script>
<script type="text/javascript" src="/Windchill/jsp/project/gantt/js/zoomToFit.js" ></script>
<script type="text/javascript" src="/Windchill/jsp/project/gantt/js/gridStruct.js" ></script>
<script src="https://export.dhtmlx.com/gantt/api.js"></script>  
<style>
	html,
	body {
		height: 100%;
		padding: 0;
		margin: 0;
		overflow: hidden;
	}

	.weekend {
		background: #f4f7f4 !important;
	}

	[data-column-name='constraint_type'] .gantt_tree_content {
		padding: 1px;
		line-height: 17px;
		white-space: normal;
		text-align: right;
		box-sizing: border-box;
	}

	.gantt_grid_editor_placeholder[data-column-name='constraint_type'] select {
		line-height: 20px;
		white-space: normal;
	}

	.constraint-marker {
		position: absolute;

		-moz-box-sizing: border-box;
		box-sizing: border-box;

		width: 22px;
		height: 22px;
		margin-top: 17px;

		opacity: 0.7;
		z-index: 1;
		background: url("images/baseline-keyboard_tab-24px.svg") center no-repeat;
	}

	.constraint-marker.earliest-start {
		margin-left: -22px;
	}

	.constraint-marker.latest-end {
		margin-left: 0;
		transform: rotate(180deg);
	}
</style>
</head>
<body>
<div class="pop5">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('대일정 보기')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<div class="seach_arm2 pt15 pb5">
		<div class="leftbt">
		</div>
		<div class="rightbt">
			<input type="button" class="s_bt05" value="${e3ps:getMessage('Export')}" onclick="dsExport()">
			<select onchange="zoomScale();" id="zoomScale" >
							<option value="days">day</option>
							<option value="weeks">week</option>
							<option value="quarters">month</option>
						</select>
			<c:if test="${isModify eq true || isAdmin eq true}">
					<input type="button" class="s_bt03" value="${e3ps:getMessage('수정')}" onclick="modifyDs()">
			</c:if>
			<c:if test="${isModify eq true || isAdmin eq true}">
				<input type="button" class="s_bt05" value="${e3ps:getMessage('삭제')}" onclick="deleteDs()">
			</c:if>
		</div>
	</div>
	
	<!-- //top -->
	<form name="ganttForm" id="ganttForm">
			<input type="hidden" id="dsoid" name="dsoid" value="${dsoid}">
	</form>
	<div class="myGantt" id="gantt_ds" style="width:100%; height:calc(50% - 60px)"></div>
	<div>
		<jsp:include page="${e3ps:getIncludeURLString('/calendar/include_dsList')}" flush="true">
			<jsp:param name="oid" value="${dsoid}"/>
			<jsp:param name="title" value="${e3ps:getMessage('자동차 개발 단계')}"/>
		</jsp:include>
	</div>
	
</div>
	
	<script>
	
	gantt.plugins({
		auto_scheduling: true
	});
	gantt.config.columns = [
		{name: "text", label: "${e3ps:getMessage('개발단계')}", tree: true, min_width:110, width:"*"},
		{name: "start_date", label: "${e3ps:getMessage('시작일')}", align:"center", min_width:80},
		{name: "endDate", label: "${e3ps:getMessage('종료일')}", align:"center", min_width:80},
	];
	gantt.config.autosize = "y";
	gantt.config.date_format = "%Y/%m/%d";
	gantt.config.date_grid = "%Y/%m/%d";
	gantt.config.task_date = "%Y/%m/%d";
	gantt.config.auto_types = true;
	gantt.config.auto_scheduling = true;
	gantt.config.auto_scheduling_compatibility = true;
	gantt.locale.labels.section_split = "Display";
	gantt.config.readonly = true;
	gantt.i18n.setLocale("kr");
	gantt.config.lightbox.project_sections = [
		{name: "description", height: 70, map_to: "text", type: "textarea", focus: true},
		{name: "split", type:"checkbox", map_to: "render", options:[
			{key:"split", label:"Split Task"}
		]},
		{name: "time", type: "duration", readonly: true, map_to: "auto"}
	];
	gantt.attachEvent("onTaskDblClick", function(id,e){
		var task = gantt.getTask(id);
	    if(task.type == "project"){
	    	if(task.render == "split"){
	    		task.render = "";
		    	gantt.render();
		    	gantt.open(id);
	    	}else{
	    		task.render = "split";
		    	gantt.render();
		    	gantt.close(id);
	    	}
	    }
	    return true;
	});

	
	function dsExport(){
		gantt.exportToExcel({
			  date_format: "yyyy/mm/dd"
			});
	}
// 	gantt.init("gantt_here");
	function zoomScale(){
		var s = $("#zoomScale").val();
		setZoom(s);
		gantt.render();
	}
	function setZoom(level) {
		console.log(level);
		if(level === "days"){
			gantt.ext.zoom.setLevel("days");
			gantt.templates.scale_cell_class = function (date) {
				if (date.getDay() == 0 || date.getDay() == 6) {
					return "weekend";
				}
			};
			gantt.templates.timeline_cell_class = function (item, date) {
				if (date.getDay() == 0 || date.getDay() == 6) {
					return "weekend";
				}
			};
		}else{
			ganttModules.zoom.setZoom(level);
		}
	}
	function getDsGanttData(){
		var param = {
				oid : "${dsoid}"
			}
		var loadUrl = getURLString("/calendar/getDsGanttData");
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
			console.log("data -> "+data.tasks[0].parent);
			data.tasks = data.tasks.map(function(task){
				
				if(0 === task.parent){
					task = {
						id : task.code,
						text : task.name,
						type: "project",
						render: "split",
						start_date : task.startDate,
						endDate : task.endDate,
					}
				}else{
					task = {
						id : task.code,
						text : task.name,
						type: "milestone",
						parent: task.parent,
						start_date : task.startDate,
						endDate : task.endDate,
					}
				}
				
				
				return task;
			});
			$("#gantt_ds").dhx_gantt().parse(data);
// 			gantt.parse(data);
			window.endProgress();
		});
	}
		
		
		
	$(document).ready(function(){
		$('.mygantt').dhx_gantt();
		getDsGanttData();
	});
	
	function modifyDs() {
		var url = getURLString("/calendar/modifyDs") + "?oid=${dsoid}";
		location.href = url;
	}
	
	function deleteDs() {
		openConfirm("${e3ps:getMessage('삭제하시겠습니까?')}", function(){
			var param = new Object();
			param.oid = "${dsoid}";
			var url = getURLString("/calendar/deleteDsAction");
			ajaxCallServer(url, param, function(data){
				if(opener.window.search){
					opener.window.search();				
				}
			}, true);
		});
	}
	
	</script>

</body>
