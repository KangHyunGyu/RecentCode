<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script src="/Windchill/jsp/dashboard/js/dashboard.project.js"></script>

<style>
.product {
	height: calc(100% - 38px) !important;
	width: 100%;
	box-sizing: border-box;
}

.highchart-wrapper {
	width: 100%;
	height: 100%;
	display: grid;
	grid-template-rows: 35px 1fr 1fr;
	grid-template-columns: 1fr 1fr 1fr;
}

.highchart-wrapper div {
	text-align: center;
	vertical-align: middle;
}

.highchart-wrapper p {
	grid-column: 1/4;
}

#items {
	display: flex;
	gap: 10px;
	justify-content: flex-end;
	padding-right: 25px;
}

#items div {
	display: flex;
	align-items: center;
	gap: 3px;
}

#items span {
	display: block;
	width: 10px;
	height: 10px;
}

.highchart-wrapper section {
	height: 100%;
}
</style>

<script>
	var STATUS = {
		PROGRESS : {  label: "${e3ps:getMessage('진행 중')}", color: COLORINPROGRESS },
		DELAY: { label: "${e3ps:getMessage('지연됨')}", color: COLORDELAY },
		ABORTED: { label: "${e3ps:getMessage('중단됨')}", color: COLORABORTED },
		COMPLETED: { label: "${e3ps:getMessage('완료됨')}", color: COLORCOMPLETED }
	};

	$(document).ready(function () {
		renderStateCategories();
		search();
	});

	function search() {
		var projects = getDatas();
		//projects = [...projects, ...projects, ...projects];
		
		var height = projects.length * 60 + 100;
		
		renderChart(projects, height);
	}

	function getDatas() {
		$("#searchForm").attr("action", getURLString("/dashboard/projects"));
		var result = formSubmit( "searchForm", { projecttype: ["O"], }, null, null );
		if (result.result) {
			return result.list;
		}
		return null;
	}

function renderChart(projects, height) {
	var categories = [];
	var data = [];
	
	projects.forEach((project, index) => {
    	categories.push(project.order_no);
    	var state = project.state === "PROGRESS" && project.isDelay ? "DELAY" : project.state;
    	var color = STATUS[getStateGrouping(state)].color;
    	var barColor = color;
    	data.push({
			x: 0,
			x2: 100,
			y: index,
			color: BASEBACKGROUNDCOLOR,
			partialFill: {
				amount: project.completion / 100,
				fill: barColor,
			},
			project: project,
		});
	});

		var chart = {
		  chart: {
		    type: "xrange",
		    width: null,
		    height: height,
		  },
		  title: {
		    text: "",
		  },
		  xAxis: {
		    tickInterval: 10,
		    gridLineWidth: 1,
		    labels: {
		      format: "{text}%",
		    },
		  },
		  yAxis: {
		    title: {
		      text: "",
		    },
		    gridLineWidth: 0,
		    categories: categories,
		    reversed: true,
		  },
		  legend: false,
		  credits: false,
		  exporting: false,
		  tooltip: {
		    pointFormat: "",
		    formatter: function (item) {
		      var html =
		        "[" +
		        this.point.project.code +
		        "] " +
		        this.point.project.name +
		        " : " +
		        this.point.project.completion +
		        " / 100 (%)" +
		        "<br />" +
		        this.point.project.planStartDate +
		        " ~ " +
		        this.point.project.planEndDate;
		      return html;
		    },
		  },
		  plotOptions: {
		    xrange: {
		      partialFill: {},
		    },
		  },
		  series: [
		    {
		      pointWidth: 30,
		      data: data,
		      dataLabels: {
		        enabled: false,
		      },
		    },
		  ],
		};

		//Highcharts.merge(gaugeOptions, {});
		Highcharts.chart("dashboard-project-progress", chart);
}

  	//검색조건 초기화
	function reset() {
		var locationDisplay = $("#locationDisplay").val();
		$("#searchForm")[0].reset();
		$("#locationDisplay").val(locationDisplay);
	}
</script>


<div class="product pb10" style="display: flex; flex-direction: column">
	<!-- button -->
	<div class="seach_arm pt10 pb5">
		<div class="leftbt">
			<img class="pointer mb5" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png" />&nbsp;${e3ps:getMessage('프로젝트')} ${e3ps:getMessage('진척현황')}
		</div>
		<div class="rightbt">
			<button type="button" class="i_read" style="width: 70px" id="searchBtn" onclick="search();">${e3ps:getMessage('검색')}</button>
			<button type="button" class="i_update" style="width: 70px" id="resetBtn" onclick="reset();">${e3ps:getMessage('초기화')}</button>
		</div>
	</div>

	<div class="pro_table mr30 ml30">
		<form name="searchForm" id="searchForm" style="margin-bottom: 0px">
			<table class="mainTable">
				<colgroup>
					<col style="width: 15%" />
					<col style="width: 35%" />
					<col style="width: 15%" />
					<col style="width: 35%" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="col">${e3ps:getMessage('계획 시작일자')}</th>
						<td class="calendar" colspan="1"><input type="text" class="datePicker w25" name="planStartDate" id="planStartDate" readonly /> ~ <input type="text" class="datePicker w25" name="planEndDate" id="planEndDate" readonly /></td>
						<th></th>
						<td></td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>

	<div class="table_list" style="height: 30px; padding-top: 5px; box-sizing: border-box">
		<p id="items"></p>
	</div>

	<div class="table_list pt10 pb10" style="height: 100%; overflow-y: auto">
		<div id="dashboard-project-progress"></div>
	</div>
</div>

