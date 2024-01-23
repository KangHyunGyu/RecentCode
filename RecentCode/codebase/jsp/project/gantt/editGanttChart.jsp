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
		<h2>${e3ps:getMessage('프로젝트 일정 편집')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->
<form name="ganttForm" id="ganttForm">
			<input type="hidden" id="oid" name="oid" value="${oid}">
	</form>
	<div class="ml25 mr25 mt10 mb10 body-container">
		<div class="gantt-container">
			<div class="header gantt-header">
				<ul class="gantt-controls">
<!-- 					<li class="gantt-menu-item"><a id="undoWork" href="javascript:undoWork();"><img src="/Windchill/jsp/project/gantt/css/img/ic_undo_24.png">Undo</a></li> -->
					<li class="gantt-menu-item"><a id="collapseAll" href="javascript:collapseAll();"><img src="/Windchill/jsp/project/gantt/css/img/ic_collapse_all_24.png">Collapse All</a></li>
					<li class="gantt-menu-item gantt-menu-item-last"><a id="expandAll" href="javascript:expandAll();"><img src="/Windchill/jsp/project/gantt/css/img/ic_expand_all_24.png">Expand All</a></li>			
					<li class="gantt-menu-item"><a id="autoScheduling" href="javascript:autoScheduling();"><img src="/Windchill/jsp/project/gantt/css/img/ic_auto_scheduling_24.png">Auto Scheduling</a></li>
<!-- 					<li class="gantt-menu-item"><a onclick="excelDown('ganttForm', 'excelDownGantt');"><img src="/Windchill/jsp/project/gantt/css/img/ic_critical_path_24.png">Excel Export</a></li> -->
					<li class="gantt-menu-item"><a onclick="javascript:exlExportGantt();"><img src="/Windchill/jsp/project/gantt/css/img/ic_critical_path_24.png">Excel Export</a></li>	
<!-- 					<li class="gantt-menu-item"><a onclick="javascript:createLinkPopup();"><img src="/Windchill/jsp/project/gantt/css/img/ic_critical_path_24.png">대일정 등록</a></li> -->
<!-- 					<li class="gantt-menu-item"><a href=""><img src="/Windchill/jsp/project/gantt/css/img/ic_critical_path_24.png">Critical Path</a></li> -->
					<li class="gantt-menu-item-right"><a id="fullScreen" href="javascript:fullScreen();"><img src="/Windchill/jsp/project/gantt/css/img/ic_fullscreen_24.png">Fullscreen</a></li>
					<li class="gantt-menu-item-right gantt-menu-item-last"><a id="zoomToFit" href="javascript:zoomToFit();"><img src="/Windchill/jsp/project/gantt/css/img/ic_zoom_to_fit_24.png">Zoom to Fit</a></li>
 					<!-- <li class="gantt-menu-item-right"><a id="zoomOut" href="javascript:zoomOut();"><img src="/Windchill/jsp/project/gantt/css/img/ic_zoom_out.png">Zoom Out</a></li>
 					<li class="gantt-menu-item-right"><a id="zoomIn" href="javascript:zoomIn();"><img src="/Windchill/jsp/project/gantt/css/img/ic_zoom_in.png">Zoom In</a></li> -->
					<li class="gantt-menu-item-right gantt-menu-item-last"><a>보기 설정
						<select onchange="zoomScale();" id="zoomScale" >
							<option value="days">day</option>
							<option value="weeks">week</option>
							<option value="quarters">month</option>
						</select></a>
					</li>
					<li class="gantt-menu-item-right gantt-menu-item-last"><a id="zoomToFit" href="javascript:zoominScale();"><img src="/Windchill/jsp/project/gantt/css/img/ic_zoom_in.png">확대</a></li>
					<li class="gantt-menu-item-right gantt-menu-item-last"><a id="zoomToFit" href="javascript:zoomoutScale();"><img src="/Windchill/jsp/project/gantt/css/img/ic_zoom_out.png">축소</a></li>
					<li class="gantt-menu-item-right gantt-menu-item-last"><a id="deleteBtn">선택삭제</a></li>
				</ul>
		    </div>
			
			<div class="main-content">
				<div class="list" id="ganttChart"></div>
			</div>
		</div>
	</div>
</div>
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
	auto_scheduling: true,
	multiselect: true,
	undo: true,
});

//gantt 기본 세팅
var formatter = gantt.ext.formatters.durationFormatter({
	enter: "day",
	store: "day",
	format: "day"
});
var linksFormatter = gantt.ext.formatters.linkFormatter({ durationFormatter: formatter });

var editors = {
	text: { type: "text", map_to: "text" },
	start_date: { type: "date", map_to: "start_date"},
	end_date: { type: "date", map_to: "endDate"},
	duration: { type: "duration", map_to: "duration", formatter: formatter },
};

gantt.config = {
	...gantt.config,
	drag_progress : false, //진척률 표시 안함
	date_format : "%Y/%m/%d", //date format
	date_grid : "%Y/%m/%d", //date format
	task_date : "%Y/%m/%d", //date format
	open_tree_initially : true, //초기에 gantt tree 열기
	order_branch : true, //task 순서 변경
	work_time : true, //주말 제외 duration
	auto_types : true,	//태스크 추가시 상위 태스크 자동으로 타입 지정
	row_height : 25,
	drag_multiple : true,
	columns : [
		{name: "", width: 15, resize: false, template: function (task) { return "<span class='gantt_grid_wbs'>" + gantt.getWBSCode(task) + "</span>" } },
		{name: "text", label: "${e3ps:getMessage('태스크 명')}", editor: editors.text, tree: true, min_width:180, width:"*"},
		{name: "start_date", label: "${e3ps:getMessage('계획 시작일')}", editor: editors.start_date, align:"center", min_width:80},
		{name: "endDate", label: "${e3ps:getMessage('계획 종료일')}", editor: editors.end_date, align:"center", min_width:80		},
		{name: "duration", label: "${e3ps:getMessage('공수')}(${e3ps:getMessage('일')})", /* editor: editors.duration,  */align:"center", min_width:30},
		/* {name: "end_date", label: "End Date" , align:"center", min_width:80}, */
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
		{name: "add", label:"" },
	],
	
	
	lightbox: {
		...gantt.config.lightbox,
		sections : [
			{name:"description", height:70, map_to:"text", type:"textarea", focus:true},
			{name:"time", map_to:{start_date:"start_date",end_date:"endDate"}, type:"time", time_format:["%Y","%m","%d"]}
		]
	}
};


//add 버튼 헤더에 안나오게 하기
gantt.templates.grid_header_class = function(columnName, column){
	if("add" == columnName) {
	    return "rm_gantt_add";
	}
	return "";
};

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
        "<br/><b>${e3ps:getMessage('계획 종료일')}:</b> "+date_to_str(new Date(end.valueOf() - 1)); 
    if(predecessors){
        html +=  "<br><b>${e3ps:getMessage('선행')}:</b>" + predecessors;
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

//Event
//inlineEditor
//inlineEditor 수정 시작 전
var inlineEditors = gantt.ext.inlineEditors;
inlineEditors.attachEvent("onBeforeEditStart", function(state){
	var task = gantt.getTask(state.id);
	//type이 task(최하위)일 경우 편집, type이 task 아닐 경우에는 태스크명만 편집 가능
	console.log(task);
	if(task.$rendered_type != "task") {
		if(task.$index === 0) {
			return false
		}
		if(state.columnName != "text") {
			return false;
		}
	}
	return true;
});
//inlineEditor 저장 전
inlineEditors.attachEvent("onBeforeSave", function(state){
	var task = gantt.getTask(state.id);
	if(state.columnName == "endDate") {
		var newDate = new Date(state.newValue);
		newDate.setDate(newDate.getDate() + 1);
		task.end_date = newDate;
		gantt.render();
	}
	return true;
});
//inlineEditor 저장 후
inlineEditors.attachEvent("onSave", function(state){
	return true;
});
//에디팅 완료 후
inlineEditors.attachEvent("onEditEnd", async function(state){
	if(gantt.config.auto_scheduling) {
		gantt.autoSchedule();
		setAllTaskEndDate();
	}
	gantt.render();
});

//태스크 드래그
//화면에서 드래그로 일정 조절할 경우
gantt.attachEvent("onTaskDrag", function(id, mode, task, original){
	//드래그 중에 endDate 화면 업데이트
	setTaskEndDate(task);
	setParentEndDate(task);
});
gantt.attachEvent("onAfterTaskDrag", function(id, mode, event){
	//드래그 끝난 후 endDate 정확한 날짜로 재 조정(onTaskDrag에서 끝날 시 드래그 해제했을 때의 위치에 해당하는 시간이 남아있음)
	var task = gantt.getTask(id);
	setTaskEndDate(task);
	setParentEndDate(task);
	gantt.render();
});

//LightBox 오픈 막음
gantt.attachEvent("onBeforeLightbox", function(id) {
	var task = gantt.getTask(id);
	if(task.$rendered_parent == undefined){
		return false;
	}else{
		return true;
	}
// 	if(task.$rendered_type == "task") {
// 		return true;
// 	} else {
// 	    return false;
// 	}
});

//LightBox에서 Save 버튼 눌렀을 때
gantt.attachEvent("onLightboxSave", function(id, task, is_new){
    var old = gantt.getTask(id);
    
    //같은 날짜일 때 1시간 더해지는 문제(범위가 1시간단위라 발생함)
    setTask_end_date(task);
    return true;
})
//LightBox에서 삭제 눌렀을 때
gantt.attachEvent("onLightboxDelete", function(id, task, is_new){
	var data = gantt.getTask(id);
	if(data.$rendered_type == "project"){
		var check = confirm("삭제 시 하위 태스크가 모두 삭제됩니다.");
		if (check == true) {
			return true;
		} else {
			return false;
		}
	} else {
		return true;
	}
    
})

//task 삭제 한 후
gantt.attachEvent("onAfterTaskDelete", function(id,item){
	//id : oid, item : object
	deleteTask(id);
    setParentEndDate(item);
    gantt.render();
    updateTaskAll();
});

$("#deleteBtn").on("click", function(){
	
	let selectTaskId = gantt.getSelectedTasks();
	if(selectTaskId.length == 0){
		return false;
	}
	
	let check = confirm("선택한 Task를 모두 삭제하시겠습니까?");
	
	if(check == true){
		//선택된 아이템 id 받기 - array
		
		for(let i = 0; i<selectTaskId.length; i++){
			let id = selectTaskId[i];
			gantt.deleteTask(id);
		}
	}
	
});

//태스크 추가 버튼 눌렀을 때
gantt.attachEvent("onTaskCreated", function(task){
	//상위 태스크에 연결된 링크가 있거나 산출물이 있는 경우 태스크 추가 불가 메세지
   	var parent = gantt.getTask(task.parent);
   	if(parent.$source.length != 0 || parent.$target.length != 0 || parent.output) {
   		gantt.alert({
   		    title:"하위 태스크 생성 불가",
   		    type:"alert-info",
   		    text:"선후행이 연결되었거나 산출물이 등록된 태스크는 하위 태스크 생성이 불가능합니다."
   		});
       	return false;
	}
	task.endDate = new Date(task.start_date);
    return true;
});
//새 태스크를 추가하기 이전
gantt.attachEvent("onBeforeTaskAdd", function(id,item){
    console.log("onBeforeTaskAdd");
    return true;
});
//새 태스크를 추가 완료 후
gantt.attachEvent("onAfterTaskAdd", async function(id,item){
    console.log("onAfterTaskAdd");
    await addTask(item);
});

//태스크가 어디서든지 수정되고 난 후
var onAfterTaskUpdateEvent = gantt.attachEvent("onAfterTaskUpdate", async function(id,item){
	console.log("onAfterTaskUpdate");
	setAllTaskEndDate();
	//autoscheduling이 발동하지 않은 경우에 사용하도록 조치 필요!
	if(!isAutoScheduling){
		updateTaskAll();
	}
});

//링크 관련
//링크 연결시 validation
gantt.attachEvent("onBeforeLinkAdd", function(id,link){
	var sourceTask = gantt.getTask(link.source);
	var targetTask = gantt.getTask(link.target);
	if(sourceTask.$rendered_type != "task" || targetTask.$rendered_type != "task") {
		return false;
	}
	
    if(link.type == "0") {	// Finish To Start
    	return true;
    } else {
    	return false;
    }
});
//링크 연결 후
gantt.attachEvent("onAfterLinkAdd", async function(id,link){
	await addLink(link);
});

//링크 삭제 후
gantt.attachEvent("onAfterLinkDelete", function(id,item){
	deleteLink(item);
});

//태스크 이동시 자기 부모 내부에서만 이동
gantt.attachEvent("onBeforeTaskMove", function(id, parent, tindex){
    var task = gantt.getTask(id);
    if(task.parent != parent)
        return false;
    return true;
});
//태스크 이동 완료 후
gantt.attachEvent("onRowDragEnd", async function(id, target) {
	if(id.parent == target.parent) {
		var task = gantt.getTask(id);
		await moveTask(task);
	}
});

//Auto Scheduling 시작 전
let isAutoScheduling = false;
gantt.attachEvent("onBeforeAutoSchedule",function(taskId, updatedTasks){
    console.log("onBeforeAutoSchedule");
    isAutoScheduling = true;
    return true;
});
//Auto Scheduling 진행 되었을 때
gantt.attachEvent("onAfterAutoSchedule",function(taskId, updatedTasks){
    console.log("onAfterAutoSchedule");
    isAutoScheduling = false;
});

//기본 Zoom 상태 세팅
setZoom("days");

//언어 설정
gantt.i18n.setLocale("kr");
//gantt 적용
gantt.init("ganttChart");

//gantt 데이터 로드
var param = {
	oid : "${oid}"
}


var loadUrl = getURLString("/project/getGanttEditList");
window.startProgress();

let response = ajaxCallServer(getURLString("/project/getGanttEditList"), param, null);

	response.tasks = response.tasks.map(function(task){
	task = {
		...task,
		endDate : new Date(task.endDate)
	}
	return task;
});

gantt.parse(response);
window.endProgress();


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
// 			endDate : new Date(task.endDate)
// 		}
// 		return task;
// 	});
// 	gantt.parse(data);
// 	window.endProgress();
// })


//Functions
function collapseAll() {
	gantt.eachTask(function(task){
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
function autoScheduling() {
	gantt.config.auto_scheduling = !gantt.config.auto_scheduling;
	gantt.config.auto_scheduling_strict = !gantt.config.auto_scheduling_strict;
	gantt.config.auto_scheduling_compatibility = !gantt.config.auto_scheduling_compatibility;
	if(gantt.config.auto_scheduling){
		gantt.autoSchedule();
		setAllTaskEndDate();
		gantt.render();
		updateTaskAll();
	}
	document.querySelector("#autoScheduling").classList.toggle("menu-item-active");
}
function setTask_end_date(task) {
	var end_date = new Date(task.endDate);
	end_date.setDate(end_date.getDate() + 1);
	task.end_date = end_date;
}
function setTaskEndDate(task) {
	var endDate = new Date(task.end_date);
	endDate.setDate(endDate.getDate() - 1);
	task.endDate = endDate;
}
function setParentEndDate(task) {
	
	var parentTask = gantt.getTask(task.parent);
	
	setTaskEndDate(parentTask);
	
	if(parentTask.parent) {
		setParentEndDate(parentTask);
	}
}
function setAllTaskEndDate() {
	var taskList = gantt.getTaskByTime();
	for(var task of taskList) {
		setTaskEndDate(task);
	}
}

function undoWork(){
	gantt.ext.undo.undo();
}
//서버 업데이트
//단일 태스크 업데이트(태스크명 수정)
async function updateTask(task){
	var url = getURLString("/project/updateGanttTaskAction");
	var param = {
		task : task
	}
	
	var response = await fetch(url, {
		method: "POST", 
		headers: {
	        "Content-Type": "application/json; charset=UTF-8",
	    },
	    body: JSON.stringify(param),
	}).then(function(response){
		return true;
	}).catch(function(e) {
		return false;
	})
	
	return response;
}
//전체 태스크 업데이트(날짜변경 있을 경우)
async function updateTaskAll(){
	console.log("updateTaskAll");
	var taskList = gantt.getTaskByTime();
	var url = getURLString("/project/updateGanttTaskAllAction");
	var param = {
		taskList : taskList
	}
	
	var response = await fetch(url, {
		method: "POST", 
		headers: {
	        "Content-Type": "application/json; charset=UTF-8",
	    },
	    body: JSON.stringify(param),
	}).then(function(response){
		return true;
	}).catch(function(e) {
		return false;
	})
	
	return response;
}
//태스크 추가
async function addTask(task){
	
	var parentTask = gantt.getTask(task.parent);
	var children = gantt.getChildren(task.parent);
	
	var url = getURLString("/project/addGanttTaskAction");
	var param = {
		task : task,
		sort : children.length + 1
	}
	
	const response = await fetch(url, {
		method: "POST", 
		headers: {
	        "Content-Type": "application/json; charset=UTF-8",
	    },
	    body: JSON.stringify(param),
	}).then(function(response){
		return response.json();
	}).then(function(data){
		let newTask = data.task;
		let start_date = new Date(newTask.start_date);
		let endDate =  new Date(newTask.endDate);
		let end_date = new Date(endDate);
		end_date.setDate(end_date.getDate() + 1);
		
		newTask = {
			...task,
			start_date,
			endDate,
			end_date
		}
		gantt.updateTask(task.id, newTask);
		gantt.changeTaskId(task.id, data.task.id);
		gantt.render();
		return true;
	}).catch(function(e) {
		console.error(e);
		return false;
	})
	
	return response;
}
//링크 추가
async function addLink(link){
	
	var url = getURLString("/project/addGanttLinkAction");
	var param = {
		source : link.source,
		target : link.target
	}
	var response = await fetch(url, {
		method: "POST", 
		headers: {
	        "Content-Type": "application/json; charset=UTF-8",
	    },
	    body: JSON.stringify(param),
	}).then(function(response){
		return response.json();
	}).then(function(data){
		var newLink = data.link;
		gantt.changeLinkId(link.id, newLink.id);
		return data.link;
	}).catch(function(e) {
		console.error(e);
		return false;
	})
	return response;
}
//태스크 삭제
async function deleteTask(id){
	
	var url = getURLString("/project/deleteGanttTaskAction");
	var param = {
		id : id
	}
	
	var response = await fetch(url, {
		method: "POST", 
		headers: {
	        "Content-Type": "application/json; charset=UTF-8",
	    },
	    body: JSON.stringify(param),
	}).then(function(response){
		return true;
	}).catch(function(e) {
		return false;
	})
	
	return response;
}
//링크 삭제
async function deleteLink(link){
	
	var url = getURLString("/project/deleteGanttLinkAction");
	var param = {
		linkOid : link.id
	}
	
	var response = await fetch(url, {
		method: "POST", 
		headers: {
	        "Content-Type": "application/json; charset=UTF-8",
	    },
	    body: JSON.stringify(param),
	}).catch(function(e) {
		return false;
	})
	return response;
}
//태스크 정렬
async function moveTask(task){
	
	var children = new Array();
	gantt.eachTask(function(task) {
		children.push(task);
	}, task.parent);
	
	var url = getURLString("/project/moveGanttTaskAction");
	var param = {
		children
	}
	
	var response = await fetch(url, {
		method: "POST", 
		headers: {
	        "Content-Type": "application/json; charset=UTF-8",
	    },
	    body: JSON.stringify(param),
	}).catch(function(e) {
		return false;
	})
	return response; 
}

function zoomScale(){
	var s = $("#zoomScale").val();
	ganttModules.zoomToFit.disable();
	document.querySelector("#zoomToFit").classList.remove("menu-item-active");
	setZoom(s);
	gantt.render();
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