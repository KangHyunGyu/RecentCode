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
<!-- pop -->
<div class="pop5">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('프로젝트 일정')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->
	<form name="ganttForm" id="ganttForm">
			<input type="hidden" id="oid" name="oid" value="${oid}">
			<input type="hidden" id="dsoid" name="dsoid" value="${dsoid}">
	</form>
	<div class="ml25 mr25 mt10 mb10 body-container">
		<div class="gantt-container">
			<div class="header gantt-header">
				<ul class="gantt-controls">
					<li class="gantt-menu-item"><a id="collapseAll" href="javascript:collapseAll();"><img src="/Windchill/jsp/project/gantt/css/img/ic_collapse_all_24.png">Collapse All</a></li>
					<li class="gantt-menu-item gantt-menu-item-last"><a id="expandAll" href="javascript:expandAll();"><img src="/Windchill/jsp/project/gantt/css/img/ic_expand_all_24.png">Expand All</a></li>
					<li class="gantt-menu-item"><a onclick="javascript:exlExportGantt();"><img src="/Windchill/netmarkets/images/excel_edit.png">Excel Export</a></li>
					<li class="gantt-menu-item-right"><a id="fullScreen" href="javascript:fullScreen();"><img src="/Windchill/jsp/project/gantt/css/img/ic_fullscreen_24.png">Fullscreen</a></li>
					<li class="gantt-menu-item-right gantt-menu-item-last"><a id="zoomToFit" href="javascript:zoomToFit();"><img src="/Windchill/jsp/project/gantt/css/img/ic_zoom_to_fit_24.png">Zoom to Fit</a></li>
<!-- 					<li class="gantt-menu-item-right"><a id="zoomOut" href="javascript:zoomOut();"><img src="/Windchill/jsp/project/gantt/css/img/ic_zoom_out.png">Zoom Out</a></li> -->
<!-- 					<li class="gantt-menu-item-right"><a id="zoomIn" href="javascript:zoomIn();"><img src="/Windchill/jsp/project/gantt/css/img/ic_zoom_in.png">Zoom In</a></li> -->
					<li class="gantt-menu-item-right"><a>
						<select onchange="zoomScale();" id="zoomScale" >
							<option value="days">day</option>
							<option value="weeks">week</option>
							<option value="quarters">month</option>
						</select><img src="/Windchill/jsp/project/gantt/css/img/ic_zoom_in.png">Zoom</a>
					</li>			
					<li class="gantt-menu-item-right appendPjtPaddingTop"><input type="button" style="height: 28px;" value="보기 추가" onclick="javascript:appendProjectView();"/></li>
					<li class="gantt-menu-item-right">
					<div class="appendPjtPaddingTop"><select class="searchRelatedProject" id="relatedProject" name="relatedProject"></select></div>
					</li>
				</ul>
				
		    </div>
		    <div class="header gantt-header">
		    	<ul class="gantt-controls">
					<li class="gantt-menu-item"><a onclick="dsExport();"><img src="/Windchill/netmarkets/images/excel_edit.png">대일정 Excel Export</a></li>
					 <c:if test="${isAuth}">	
						<li class="gantt-menu-item"><a onclick="javascript:modifyLinkPopup();"><img src="/Windchill/jsp/project/gantt/css/img/ic_critical_path_24.png">대일정 수정</a></li>
					</c:if>
				</ul>
		    </div>
			
			<div class="main-content">
				<div class="myGantt" id="gantt_ds"></div>
				<div class="myGantt list" id="ganttChart" style="height:calc(100% - 60px)"></div>
			</div>
		</div>
	</div>
</div>
<style>
/* Project Append 시 Grid Background Color Style */
.appendPjtColorGray{
	background-color:#EAEAEA!important;
}
.appendPjtColorWhite{
	background-color:#FFFFFF!important;
}
.appendPjtPaddingTop{
	padding-top: 5px;
}


/* move task lines upper */
.gantt_task_line, .gantt_line_wrapper {
	margin-top: -9px;
}

.gantt_side_content {
	margin-bottom: 7px;
}

.gantt_task_link .gantt_link_arrow {
	margin-top: -10px
}

.gantt_side_content.gantt_right {
	bottom: 0;
}
	
.baseline {
	position: absolute;
	border-radius: 2px;
	opacity: 0.6;
	margin-top: -7px;
	height: 12px;
	background: #2FED28;
	border: 1px solid #1DDB16;
}
</style>
<script src="https://export.dhtmlx.com/gantt/api.js"></script>  
<script type="text/javascript">

//Extension 기능 활성화
gantt.plugins({
	fullscreen: true,
	tooltip: true,
	marker: true,
});

//gantt 기본 세팅
var formatter = gantt.ext.formatters.durationFormatter({
	enter: "day",
	store: "day",
	format: "day"
});
var linksFormatter = gantt.ext.formatters.linkFormatter({ durationFormatter: formatter });

gantt.config = {
	...gantt.config,
	readonly : true,	//읽기전용
	drag_progress : false, //진척률 드래그
	date_format : "%Y/%m/%d", //date format
	date_grid : "%Y/%m/%d", //date format
	task_date : "%Y/%m/%d", //date format
	open_tree_initially : true, //초기에 gantt tree 열기
	work_time : true, //주말 제외 duration
	show_progress : true, //진척률 표시
	row_height : 40,
	task_height : 16,
	columns : [
		{name: "", width: 15, resize: false, template: function (task) { return "<span class='gantt_grid_wbs'>" + gantt.getWBSCode(task) + "</span>" } },
		{name: "text", label: "${e3ps:getMessage('태스크 명')}", tree: true, min_width:180, width:"*"},
		{name: "progress",   label:"%",  template:function(obj){
			return Math.round(obj.progress*100)+"%";
		}, align: "center", width:60 },
		{name: "start_date", label: "${e3ps:getMessage('계획 시작일')}", align:"center", min_width:80},
		{name: "endDate", label: "${e3ps:getMessage('계획 종료일')}", align:"center", min_width:80		},
		{name: "duration", label: "${e3ps:getMessage('공수')}(${e3ps:getMessage('일')})", align:"center", min_width:30},
		{name: "predecessors", label: "${e3ps:getMessage('선행')}", width: 80, align: "left", resize: true, template: function (task) {
				var links = task.$target;
				var labels = [];
				for (var i = 0; i < links.length; i++) {
					var link = gantt.getLink(links[i]);
					labels.push(linksFormatter.format(link));
				}
				return labels.join(", ")
			}
		},
	],
}

var date_to_str = gantt.date.date_to_str(gantt.config.task_date);
//EndDate 세팅
gantt.templates.task_end_date = function(date){
   return gantt.templates.task_date(new Date(date.valueOf() - 1)); 
};

//fullscreen 세팅
gantt.ext.fullscreen.getFullscreenElement = function(){
    return document.querySelector(".gantt-container");
};

//marker 세팅
var today = new Date();
gantt.addMarker({
    start_date: today,
    css: "today",
    text: "Today",
    title:"Today: "+ date_to_str(today)
});
var start = new Date("${project.planStartDate}");
gantt.addMarker({
    start_date: start,
    css: "status_line",
    text: "Start project",
    title:"Start project: "+ date_to_str(start)
});

//Tooltip 세팅
gantt.templates.tooltip_text = function(start,end,task){
	var links = task.$target;
    var labels = [];
    for (var i = 0; i < links.length; i++) {
        var link = gantt.getLink(links[i]);
        labels.push(linksFormatter.format(link));
    }
    var predecessors = labels.join(", ");

    var html = "<b>${e3ps:getMessage('태스크 명')}:</b> "+task.text+"<br/><b>${e3ps:getMessage('계획 시작일')}:</b> " + 
   		date_to_str(start)+ 
        "<br/><b>${e3ps:getMessage('계획 종료일')}:</b> "+date_to_str(new Date(end.valueOf() - 1))+
        "<br/><b>${e3ps:getMessage('진행')}:</b> "+Math.round(task.progress*100)+"%";; 
    if(predecessors){
        html +=  "<br><b>${e3ps:getMessage('선행')}:</b>" + predecessors;
    }
    if(task.real_start && task.real_end){
        html +=  "<br/><b>${e3ps:getMessage('실제 시작일')}:</b> " + 
	   		date_to_str(task.real_start)+ 
	        "<br/><b>${e3ps:getMessage('실제 종료일')}:</b> "+date_to_str(task.real_end);
    }
    
	if(task.taskRole != null && task.taskRole.length > 0){
		
		html += "<br/><b>담당자</b><hr>";
		
	    for(var i = 0; i<task.taskRole.length; i++){
	    	var userName = task.taskRole[i].userName;
	    	if(userName == null){
	    		userName = "미지정";
	    	}
	    	
	    	html+=userName+"("+task.taskRole[i].roleName+")<br/>";
	    }
	}
    return html;
};

//실제 시작일 종료일 세팅
gantt.addTaskLayer(function draw_real(task) {
	if (task.real_start && task.real_end) {
		var real_end = new Date(task.real_end);
		real_end.setDate(real_end.getDate() + 1);
		var sizes = gantt.getTaskPosition(task, task.real_start, real_end);
		var el = document.createElement('div');
		el.className = 'baseline';
		el.style.left = sizes.left + 'px';
		el.style.width = sizes.width + 'px';
		el.style.top = sizes.top + gantt.config.task_height + 13 + 'px';
		return el;
	}
	return false;
});

//기본 Zoom 상태 세팅 //zoom.js
setZoom("days");

//언어 설정
gantt.i18n.setLocale("kr");
//gantt 적용
gantt.init("ganttChart");

//gantt 데이터 로드
var param = {
	oid : "${oid}"
}
var loadUrl = getURLString("/project/getGanttViewList");
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
	data.tasks = data.tasks.map(function(task){
		task = {
			...task,
			endDate : new Date(task.endDate),
			real_start : task.real_start ? new Date(task.real_start) : null,
			real_end :task.real_end ? new Date(task.real_end) : null,
		}
		
		if(task.type == "project") {
			task.color = "#484848";
		} else if(task.type == "task") {
			if(task.state == "COMPLETED") {
				task.color = "#65B720";
			} else if(task.state == "READY" || task.state == "INWORK") {
				task.color = "#A2A2A2";
			} else if(task.state == "PROGRESS"){
				
				var endDate = new Date(task.endDate);
				endDate.setDate(endDate.getDate() + 1);
				
				if(endDate < new Date()){
					if(task.progress > 0){
						task.color = "orange";
					}else{
						task.color = "#FF4848";
					}
				} else {
					if(task.progress == 1) {
						task.color = "#65B720";
					}else {
						task.color = "#3db9d3";
					}
				}
			}
		}
		return task;
	});
	gantt.parse(data);
	window.endProgress();
})

//Functions
function appendProjectView(){
	var projectId = $("#relatedProject").val();
	
	if($("#relatedProject").val() == null || $("#relatedProject").val() == "") {
		$("#relatedProject").focus();
		openNotice("${e3ps:getMessage('프로젝트를 입력해주세요.')}");
		return false;
	}
	
	openConfirm("${e3ps:getMessage('프로젝트를 차트에 추가하시겠습니까?')}", function(){
		
		var param = {
			oid : projectId
		}
		
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
			data.tasks = data.tasks.map(function(task){
				task = {
					...task,
					endDate : new Date(task.endDate),
					real_start : task.real_start ? new Date(task.real_start) : null,
					real_end :task.real_end ? new Date(task.real_end) : null,
				}
				
				if(task.type == "project") {
					task.color = "#484848";
				} else if(task.type == "task") {
					if(task.state == "COMPLETED") {
						task.color = "#65B720";
					} else if(task.state == "READY" || task.state == "INWORK") {
						task.color = "#A2A2A2";
					} else if(task.state == "PROGRESS"){
						
						var endDate = new Date(task.endDate);
						endDate.setDate(endDate.getDate() + 1);
						
						if(endDate < new Date()){
							if(task.progress > 0){
								task.color = "orange";
							}else{
								task.color = "#FF4848";
							}
						} else {
							if(task.progress == 1) {
								task.color = "#65B720";
							}else {
								task.color = "#3db9d3";
							}
						}
					}
				}
				
				return task;
			});
			
			gantt.parse(data);
			gridBackgroundColor();
			$("#relatedProject").text("");
			window.endProgress();
		})
	});
}

function gridBackgroundColor(){
	gantt.templates.grid_row_class = function(start, end, task){
		
		var wbsCode = gantt.getWBSCode(task).substring(0,1);
		if(wbsCode%2 == 0){
			return "appendPjtColorGray";
		}else{
			return "appendPjtColorWhite";
		}
		
	}
	gantt.render();
}

function collapseAll() {
	gantt.eachTask(function(task){
		//console.log(task);
		task.$open = false;
	});
	gantt.render();
}

function expandAll() {
	gantt.eachTask(function(task){
		task.$open = true;
	});
	gantt.render();
}
function fullScreen() {
	gantt.ext.fullscreen.toggle();
}
function setZoom(level) {
	ganttModules.zoom.setZoom(level);
}
function zoomIn() {
	ganttModules.zoomToFit.disable();
	ganttModules.zoom.zoomIn();
	document.querySelector("#zoomToFit").classList.remove("menu-item-active");
}
function zoomOut() {
	ganttModules.zoomToFit.disable();
	ganttModules.zoom.zoomOut();
	document.querySelector("#zoomToFit").classList.remove("menu-item-active");
}
function zoomToFit() {
	ganttModules.zoom.deactivate();
	ganttModules.zoomToFit.toggle();
	document.querySelector("#zoomToFit").classList.toggle("menu-item-active");
	zoomScale_ds('years');
	
}
function zoomScale(){
	var s = $("#zoomScale").val();
	ganttModules.zoomToFit.disable();
	document.querySelector("#zoomToFit").classList.remove("menu-item-active");
	setZoom(s);
	gantt.render();
	zoomScale_ds();
}

function setOrDefault(object, defVal){
	var returnVal = object;
	if(object==null){
		returnVal = defVal;
	}
	return returnVal;
}

function exlExportGantt(){
	
	var tasks = gantt.getTaskByTime();
	
	var tempExcelForm = document.createElement("form");
	tempExcelForm.setAttribute("id", "tempExcelForm");
	
	for(var i = 0; i < tasks.length; i++){
		var task = tasks[i];
		
		inputTask = document.createElement("input");
		inputTask.setAttribute("type", "hidden");
		inputTask.setAttribute("name", "excelGanttWBS");
		inputTask.setAttribute("value", gantt.getWBSCode(task));
		tempExcelForm.appendChild(inputTask);
		
		//프로젝트 객체
		if(Number.isInteger( Number(gantt.getWBSCode(task)) )){
			inputProject = document.createElement("input");
			inputProject.setAttribute("type", "hidden");
			inputProject.setAttribute("name", "excelProjectList");
			inputProject.setAttribute("value", task.id);
			
			tempExcelForm.appendChild(inputProject);
		}
		
		
	}
	
	document.body.appendChild(tempExcelForm);
	excelDown("tempExcelForm", "excelDownGanttExcel2");
    
    /*  제거
    var parent = document.getElementById("ul-1");
    var li3 = document.getElementById("li-3");
 
    parent.removeChild(li3);
    
    */
    
}

//////////////////////////////////////////////대일정 간트차트 구성

function dsExport(){
	gantt_ds.exportToExcel({
	  date_format: "yyyy/mm/dd"
	});
}
	
//대일정 Scale 변경
function zoomScale_ds(f){
	
	var s = $("#zoomScale").val();
	if(undefined != f){
		s = f;
	}
	setZoom_ds(s);
	gantt_ds.render();
}

//대일정 Scale 변경
function setZoom_ds(level) {
	if(level === "days"){
		gantt_ds.ext.zoom.setLevel("days");
		gantt_ds.templates.scale_cell_class = function (date) {
			if (date.getDay() == 0 || date.getDay() == 6) {
				return "weekend";
			}
		};
		gantt_ds.templates.timeline_cell_class = function (item, date) {
			if (date.getDay() == 0 || date.getDay() == 6) {
				return "weekend";
			}
		};
	}else{
		gantt_ds.ext.zoom.setLevel(level);
	}
}

//대일정 간트차트 설정
const gantt_ds = Gantt.getGanttInstance();
gantt_ds.plugins({
	auto_scheduling: true,
	fullscreen: true,
	tooltip: true,
});
gantt_ds.config.columns = [
	{name: "text", label: "${e3ps:getMessage('개발단계')}", tree: true, min_width:110, width:"*"},
	{name: "start_date", label: "${e3ps:getMessage('시작일')}", align:"center", min_width:80, type:"date",template: function(task){
		return date_to_str(task.start_date);
	}},
	{name: "endDate", label: "${e3ps:getMessage('종료일')}", align:"center", min_width:80, type:"date" ,template: function(task){
		return date_to_str(task.endDate);
	}},
// 	{name: "duration", label: "${e3ps:getMessage('공수')}", align:"center", min_width:80},
];
gantt_ds.i18n.setLocale("kr");

gantt_ds.config.row_height = 40;
gantt_ds.config.task_height = 18;
gantt_ds.config.autosize = "y";
gantt_ds.config.drag_move = false;
gantt_ds.config.drag_links = false;
// gantt_ds.config.readonly = true;
// gantt_ds.config.date_format = "%Y/%m/%d";
// gantt_ds.config.date_grid = "%Y/%m/%d";
// gantt_ds.config.task_date = "%Y/%m/%d";
gantt_ds.config.auto_scheduling = true;
gantt_ds.config.auto_scheduling_compatibility = true;
gantt_ds.locale.labels.section_split = "Display";
gantt_ds.config.work_time = true;
gantt_ds.config.lightbox.project_sections = [
	{name: "description", height: 70, map_to: "text", type: "textarea", focus: true},
	{name: "split", type:"checkbox", map_to: "render", options:[
		{key:"split", label:"Split Task"}
	]},
];

gantt_ds.config.lightbox.sections = [
    {name:"description", height:38, map_to:"text", type:"textarea",focus:true},
];

//대일정 이벤트 설정
gantt_ds.attachEvent("onBeforeLightbox", function(id) {
    var task = gantt_ds.getTask(id);
    if("project" == task.type){
    	return false;
    }
    return true;
});

gantt_ds.attachEvent("onTaskDblClick", function(id,e){
	var task = gantt_ds.getTask(id);
    if(task.type == "project"){
    	if(task.render == "split"){
    		task.render = "";
    		gantt_ds.render();
    		gantt_ds.open(id);
    	}else{
    		task.render = "split";
    		gantt_ds.render();
    		gantt_ds.close(id);
    	}
    }
    return true;
});

//대일정에 프로젝트 태스크 추가
gantt.attachEvent("onTaskDblClick", function(id,e){
	var task = gantt.getTask(id);
	var taskId = gantt_ds.addTask({
		id : task.code,
		text : task.text,
		type: "task",
		start_date :task.start_date,
		startDate :task.start_date,
		endDate : task.endDate,
		duration : task.duration,
		color : task.color,
	},task.name);
	
	console.log(task);
    return true;
});


// 대일정 태스크 이름표시
gantt_ds.templates.rightside_text = function (start, end, task) {
	if (task.type == gantt.config.types.milestone) {
		return "<b>"+task.text+"</b>";
	}
	return "";
};

//대일정 툴팁
gantt_ds.templates.tooltip_text = function(start,end,task){
    var html = "<b>${e3ps:getMessage('개발단계')}:</b> "+task.text
  		  +"<br/><b>${e3ps:getMessage('시작일')}:</b> "+date_to_str(task.start_date)
       	  +"<br/><b>${e3ps:getMessage('종료일')}:</b> "+date_to_str(task.endDate)
       	  +"<br/><b>${e3ps:getMessage('설명')}:</b> "+task.remark;
    return html;
};

// 주석 해제 시 Bar형태 출력
// gantt_ds.addTaskLayer(function draw_real(task) {
// 	console.log(task, task.start_date, task.endDate);
// 	var sizes = gantt_ds.getTaskPosition(task, task.start_date, task.endDate);
// 	var el = document.createElement('div');
// 	el.className = 'baseline';
// 	el.style.left = sizes.left + 'px';
// 	el.style.width = sizes.width + 'px';
// 	el.style.top = sizes.top + gantt_ds.config.task_height + 13 + 'px';
// 	return el;
// });

var ds_zoomConfig = {
		minColumnWidth: 80,
		maxColumnWidth: 150,
		levels: [
			{
				name: "days",
				scales: [
					{unit: "month", step: 1, format: "%M"},
					{unit: "week", step: 1, format: "%W"},
					{unit: "day", step: 1, format: "%d (%D)"},
				],
				round_dnd_dates: true,
				min_column_width: 60,
				scale_height: 60
			},
			{
				name: "weeks",
				scales: [
					{unit: "year", step: 1, format: "%Y"},
					{unit: "month", step: 1, format: "%M"},
					{unit: "week", step: 1, format: "%W"},
				],
				round_dnd_dates: false,
				min_column_width: 60,
				scale_height: 60
			},
			{
				name: "quarters",
				scales: [
					{unit: "year", step: 1, format: "%Y"},
					{unit: "quarter", step: 1, format: function quarterLabel(date) {
						var month = date.getMonth();
						var q_num;
		
						if (month >= 9) {
							q_num = 4;
						} else if (month >= 6) {
							q_num = 3;
						} else if (month >= 3) {
							q_num = 2;
						} else {
							q_num = 1;
						}
		
						return "Q" + q_num;
					}},
					{unit: "month", step: 1, format: "%M"}
				],
				round_dnd_dates: false,
				min_column_width: 50,
				scale_height: 60
			},
			{
				name: "years",
				scales: [
					{unit: "year", step: 1, format: "%Y"},
				],
				round_dnd_dates: false,
				min_column_width: 50,
				scale_height: 60
			}
		],
	}

gantt_ds.ext.zoom.init(ds_zoomConfig);
gantt_ds.ext.zoom.setLevel("days");
gantt_ds.init("gantt_ds");
var param = {
		oid : "${dsoid}"
	}
var loadUrl2 = getURLString("/calendar/getDsGanttData");
window.startProgress();
fetch(loadUrl2, {
	method: "POST", 
	headers: {
        "Content-Type": "application/json; charset=UTF-8",
    },
    body: JSON.stringify(param),
}).then(function(response){
	return response.json();
}).then(function(data){
	data.tasks = data.tasks.map(function(task){
		console.log(task.endDate);
		if(0 === task.parent){
			task = {
				id : task.code,
				text : task.name,
				type: "project",
				render: "split",
				start_date : new Date(task.startDate),
				endDate : new Date(task.endDate),
				duration : task.duration,
				remark : task.remark,
			}
		}else{
			task = {
				id : task.code,
				text : task.name,
				type: "milestone",
				parent: task.parent,
				start_date : new Date(task.startDate),
				endDate : new Date(task.endDate),
				duration : task.duration,
				remark : task.remark,
			}
		}
		
		return task;
	});
	gantt_ds.parse(data);
	window.endProgress();
})

function modifyLinkPopup(){
	var url = getURLString("/calendar/modifyLinkPopup") + "?oid=${oid}";
	
	openPopup(url,"modifyLinkPopup","1000","500");
}
</script>