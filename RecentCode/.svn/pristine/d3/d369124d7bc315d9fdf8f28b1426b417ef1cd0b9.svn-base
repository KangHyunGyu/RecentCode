// 버튼 이벤트
const collapseAll = () => {
  gantt.eachTask((task) => {
    task.$open = false;
  });
  gantt.render();
};

const expandAll = () => {
  gantt.eachTask((task) => {
    task.$open = true;
  });
  gantt.render();
};
const fullScreen = () => {
  gantt.ext.fullscreen.toggle();
};
const setZoom = (level) => {
  ganttModules.zoom.setZoom(level);
};
const zoomIn = () => {
  ganttModules.zoomToFit.disable();
  ganttModules.zoom.zoomIn();
  document.querySelector("#zoomToFit").classList.remove("menu-item-active");
};
const zoomOut = () => {
  ganttModules.zoomToFit.disable();
  ganttModules.zoom.zoomOut();
  document.querySelector("#zoomToFit").classList.remove("menu-item-active");
};
const zoomToFit = () => {
  ganttModules.zoom.deactivate();
  ganttModules.zoomToFit.toggle();
  document.querySelector("#zoomToFit").classList.toggle("menu-item-active");
};
const zoomScale = () => {
  var s = document.querySelector("#zoomScale").value;
  ganttModules.zoomToFit.disable();
  document.querySelector("#zoomToFit").classList.remove("menu-item-active");
  setZoom(s);
  gantt.render();
};

//Extension 기능 활성화
gantt.plugins({
  fullscreen: true,
  tooltip: true,
  marker: true,
});

//gantt 기본 세팅
const formatter = gantt.ext.formatters.durationFormatter({
  enter: "day",
  store: "day",
  format: "day",
});
const linksFormatter = gantt.ext.formatters.linkFormatter({
  durationFormatter: formatter,
});

gantt.config = {
  ...gantt.config,
  readonly: true, //읽기전용
  drag_progress: false, //진척률 드래그
  date_format: "%Y/%m/%d", //date format
  date_grid: "%Y/%m/%d", //date format
  task_date: "%Y/%m/%d", //date format
  open_tree_initially: true, //초기에 gantt tree 열기
  work_time: true, //주말 제외 duration
  show_progress: true, //진척률 표시
  row_height: 40,
  task_height: 16,
  columns: [
    {
      name: "",
      width: 15,
      resize: false,
      template: function (task) {
        return (
          "<span class='gantt_grid_wbs'>" + gantt.getWBSCode(task) + "</span>"
        );
      },
    },
    {
      name: "text",
      label: "태스크 명",
      tree: true,
      min_width: 180,
      width: "*",
    },
    {
      name: "progress",
      label: "%",
      template: function (obj) {
        return Math.round(obj.progress * 100) + "%";
      },
      align: "center",
      width: 60,
    },
    {
      name: "start_date",
      label: "계획 시작일",
      align: "center",
      min_width: 80,
    },
    {
      name: "endDate",
      label: "계획 종료일",
      align: "center",
      min_width: 80,
    },
    {
      name: "duration",
      label: "공수일",
      align: "center",
      min_width: 30,
    },
    {
      name: "predecessors",
      label: "선행",
      width: 80,
      align: "left",
      resize: true,
      template: function (task) {
        const links = task.$target;
        const labels = [];
        for (let i = 0; i < links.length; i++) {
          let link = gantt.getLink(links[i]);
          labels.push(linksFormatter.format(link));
        }
        return labels.join(", ");
      },
    },
  ],
};

const date_to_str = gantt.date.date_to_str(gantt.config.task_date);
//EndDate 세팅
gantt.templates.task_end_date = function (date) {
  return gantt.templates.task_date(new Date(date.valueOf() - 1));
};

//fullscreen 세팅
gantt.ext.fullscreen.getFullscreenElement = function () {
  return document.querySelector(".gantt-container");
};

//marker 세팅
const today = new Date();
gantt.addMarker({
  start_date: today,
  css: "today",
  text: "Today",
  title: "Today: " + date_to_str(today),
});
const start = new Date(2019, 01, 01);
gantt.addMarker({
  start_date: start,
  css: "status_line",
  text: "Start project",
  title: "Start project: " + date_to_str(start),
});

//Tooltip 세팅
gantt.templates.tooltip_text = function (start, end, task) {
  var links = task.$target;
  var labels = [];
  for (var i = 0; i < links.length; i++) {
    var link = gantt.getLink(links[i]);
    labels.push(linksFormatter.format(link));
  }
  var predecessors = labels.join(", ");

  var html =
    "<b>태스크 명</b> " +
    task.text +
    "<br/><b>계획 시작일</b> " +
    date_to_str(start) +
    "<br/><b>계획 종료일</b> " +
    date_to_str(new Date(end.valueOf() - 1)) +
    "<br/><b>진행</b> " +
    Math.round(task.progress * 100) +
    "%";
  if (predecessors) {
    html += "<br><b>선행</b>" + predecessors;
  }
  if (task.real_start && task.real_end) {
    html +=
      "<br/><b>실제 시작일</b> " +
      date_to_str(task.real_start) +
      "<br/><b>실제 종료일</b> " +
      date_to_str(task.real_end);
  }

  if (task.taskRole != null && task.taskRole.length > 0) {
    html += "<br/><b>담당자</b><hr>";

    for (var i = 0; i < task.taskRole.length; i++) {
      var userName = task.taskRole[i].userName;
      if (userName == null) {
        userName = "미지정";
      }

      html += userName + "(" + task.taskRole[i].roleName + ")<br/>";
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
    var el = document.createElement("div");
    el.className = "baseline";
    el.style.left = sizes.left + "px";
    el.style.width = sizes.width + "px";
    el.style.top = sizes.top + gantt.config.task_height + 13 + "px";
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

gantt.parse(datas);
