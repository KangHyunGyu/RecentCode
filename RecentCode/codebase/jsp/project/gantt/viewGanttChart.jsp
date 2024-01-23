<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- dhtmlx gantt Component -->
<link rel="stylesheet" type="text/css"
	href="/Windchill/jsp/component/gantt_7.0.9_ultimate/codebase/dhtmlxgantt.css" />
<script type="text/javascript"
	src="/Windchill/jsp/component/gantt_7.0.9_ultimate/codebase/dhtmlxgantt.js"></script>
<link rel="stylesheet" type="text/css"
	href="/Windchill/jsp/project/gantt/css/style.css">
<script type="text/javascript"
	src="/Windchill/jsp/project/gantt/js/zoom.js"></script>
<script type="text/javascript"
	src="/Windchill/jsp/project/gantt/js/zoomToFit.js"></script>
<script type="text/javascript"
	src="/Windchill/jsp/project/gantt/js/gridStruct.js"></script>
<!-- pop -->
<div class="pop5">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('프로젝트 일정')}</h2>
		<span class="close"><a href="javascript:window.close()"><img
				src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->
	<form name="ganttForm" id="ganttForm">
		<input type="hidden" id="oid" name="oid" value="${oid}">
	</form>
	<div class="ml25 mr25 mt10 mb10 body-container">
		<div class="gantt-container">
			<div class="header gantt-header">
				<ul class="gantt-controls">
					<li class="gantt-menu-item"><a id="collapseAll"
						href="javascript:collapseAll();"><img
							src="/Windchill/jsp/project/gantt/css/img/ic_collapse_all_24.png">Collapse
							All</a></li>
					<li class="gantt-menu-item gantt-menu-item-last"><a
						id="expandAll" href="javascript:expandAll();"><img
							src="/Windchill/jsp/project/gantt/css/img/ic_expand_all_24.png">Expand
							All</a></li>
					<li class="gantt-menu-item"><a
						onclick="javascript:exlExportGantt();"><img
							src="/Windchill/netmarkets/images/excel_edit.png">Excel
							Export</a></li>
					<!-- 					<li class="gantt-menu-item"><a onclick="excelDown('ganttForm', 'excelDownGantt');"><img src="/Windchill/jsp/project/gantt/css/img/ic_critical_path_24.png">Template Export</a></li>	 -->

					<li class="gantt-menu-item-right"><a id="fullScreen"
						href="javascript:fullScreen();"><img
							src="/Windchill/jsp/project/gantt/css/img/ic_fullscreen_24.png">Fullscreen</a></li>
					<li class="gantt-menu-item-right gantt-menu-item-last"><a
						id="zoomToFit" href="javascript:zoomToFit();"><img
							src="/Windchill/jsp/project/gantt/css/img/ic_zoom_to_fit_24.png">Zoom
							to Fit</a></li>
					<!-- 					<li class="gantt-menu-item-right"><a id="zoomOut" href="javascript:zoomOut();"><img src="/Windchill/jsp/project/gantt/css/img/ic_zoom_out.png">Zoom Out</a></li> -->
					<!-- 					<li class="gantt-menu-item-right"><a id="zoomIn" href="javascript:zoomIn();"><img src="/Windchill/jsp/project/gantt/css/img/ic_zoom_in.png">Zoom In</a></li> -->
					<li class="gantt-menu-item-right gantt-menu-item-last"><a>보기 설정
						<select onchange="zoomScale();" id="zoomScale" >
							<option value="days">day</option>
							<option value="weeks">week</option>
							<option value="quarters">month</option>
						</select></a>
					</li>
					<li class="gantt-menu-item-right gantt-menu-item-last"><a id="zoomToFit" href="javascript:zoominScale();"><img src="/Windchill/jsp/project/gantt/css/img/ic_zoom_in.png">확대</a></li>
					<li class="gantt-menu-item-right gantt-menu-item-last"><a id="zoomToFit" href="javascript:zoomoutScale();"><img src="/Windchill/jsp/project/gantt/css/img/ic_zoom_out.png">축소</a></li>
					<!-- <li class="gantt-menu-item-right appendPjtPaddingTop"><input
						type="button" style="height: 28px;" value="보기 추가"
						onclick="javascript:appendProjectView();" /></li> -->
					<!-- <li class="gantt-menu-item-right">
						<div class="appendPjtPaddingTop">
							<select class="searchRelatedProject" id="relatedProject"
								name="relatedProject"></select>
						</div>
					</li> -->
				</ul>
			</div>
			
			<!-- 
			<c:if test="${isAuth}">
				<div class="header gantt-header">
					<ul class="gantt-controls">
						<li class="gantt-menu-item"><a onclick="javascript:createLinkPopup();"><img src="/Windchill/jsp/project/gantt/css/img/ic_critical_path_24.png">대일정 등록</a></li>
					</ul>
				</div>
			</c:if>
			-->
			 
			<div class="main-content">
				<div class="list" id="ganttChart"></div>
			</div>
		</div>
	</div>
</div>
<style>
/* Project Append 시 Grid Background Color Style */
.appendPjtColorGray {
	background-color: #EAEAEA !important;
}

.appendPjtColorWhite {
	background-color: #FFFFFF !important;
}

.appendPjtPaddingTop {
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
function zoominScale(){
	
	if(gantt.config.min_column_width < 160){
		ganttModules.zoomToFit.disable();
		document.querySelector("#zoomToFit").classList.remove("menu-item-active");
		gantt.config.min_column_width = gantt.config.min_column_width+20;
		gantt.render();
	}
} 
function zoomoutScale(){
	
	if(gantt.config.min_column_width > 1){
		ganttModules.zoomToFit.disable();
		document.querySelector("#zoomToFit").classList.remove("menu-item-active");
		gantt.config.min_column_width = gantt.config.min_column_width-20;
		gantt.render();
	}
}

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
        	task.real_start
// 	   		date_to_str(task.real_start)+ 
	        "<br/><b>${e3ps:getMessage('실제 종료일')}:</b> "+
// 	        date_to_str(task.real_end);
	        task.real_end;
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

let response = ajaxCallServer(getURLString("/project/getGanttViewList"), param, null);
response.tasks = response.tasks.map(function(task){
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
gantt.parse(response);
	window.endProgress();
	console.log("response",response);
// fetch(loadUrl, {
// 	method: "POST", 
// 	headers: {
//         "Content-Type": "application/json; charset=UTF-8",
//     },
//     body: JSON.stringify(param),
// }).then(function(response){
// 	return response.json();
// }).then(function(data){
// 	data.tasks = data.tasks.map(function(task){
// 		task = {
// 			...task,
// 			endDate : new Date(task.endDate),
// 			real_start : task.real_start ? new Date(task.real_start) : null,
// 			real_end :task.real_end ? new Date(task.real_end) : null,
// 		}
		
// 		if(task.type == "project") {
// 			task.color = "#484848";
// 		} else if(task.type == "task") {
// 			if(task.state == "COMPLETED") {
// 				task.color = "#65B720";
// 			} else if(task.state == "READY" || task.state == "INWORK") {
// 				task.color = "#A2A2A2";
// 			} else if(task.state == "PROGRESS"){
				
// 				var endDate = new Date(task.endDate);
// 				endDate.setDate(endDate.getDate() + 1);
				
// 				if(endDate < new Date()){
// 					if(task.progress > 0){
// 						task.color = "orange";
// 					}else{
// 						task.color = "#FF4848";
// 					}
// 				} else {
// 					if(task.progress == 1) {
// 						task.color = "#65B720";
// 					}else {
// 						task.color = "#3db9d3";
// 					}
// 				}
// 			}
// 		}
		
// 		return task;
// 	});
// 	gantt.parse(data);
// 	window.endProgress();
// })

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
}
function zoomScale(){
	var s = $("#zoomScale").val();
	ganttModules.zoomToFit.disable();
	document.querySelector("#zoomToFit").classList.remove("menu-item-active");
	setZoom(s);
	gantt.render();
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

function createLinkPopup(){
	var url = getURLString("/calendar/createLinkPopup") + "?oid=${oid}";
	
	openPopup(url,"createLinkPopup","1000","500");
}

</script>