if(!window.ganttModules){
	window.ganttModules = {};
}

//Zoom to Fit 기능 세팅
ganttModules.zoomToFit = (function(gantt){
	var cachedSettings = {};

	function saveConfig() {
		var config = gantt.config;
		cachedSettings = {};
		cachedSettings.scales = config.scales;
		cachedSettings.template = gantt.templates.date_scale;
		cachedSettings.start_date = config.start_date;
		cachedSettings.end_date = config.end_date;
	}

	function restoreConfig() {
		applyConfig(cachedSettings);
	}

	function applyConfig(config, dates) {
		if (config.scales[0].date) {
			gantt.templates.date_scale = null;
		}
		else {
			gantt.templates.date_scale = config.scales[0].template;
		}

		gantt.config.scales = config.scales;

		if (dates && dates.start_date && dates.start_date) {
			gantt.config.start_date = gantt.date.add(dates.start_date, -1, config.scales[0].subscale_unit);
			gantt.config.end_date = gantt.date.add(gantt.date[config.scales[0].subscale_unit + "_start"](dates.end_date), 2, config.scales[0].subscale_unit);
		} else {
			gantt.config.start_date = gantt.config.end_date = null;
		}
	}


	function zoomToFit() {
		var project = gantt.getSubtaskDates(),
			areaWidth = gantt.$task.offsetWidth;

		for (var i = 0; i < scaleConfigs.length; i++) {
			var columnCount = getUnitsBetween(project.start_date, project.end_date, scaleConfigs[i].scales[0].subscale_unit, scaleConfigs[i].scales[0].step);
			if ((columnCount + 2) * gantt.config.min_column_width <= areaWidth) {
				break;
			}
		}

		if (i == scaleConfigs.length) {
			i--;
		}

		applyConfig(scaleConfigs[i], project);
		ganttModules.zoomToFit.setWeekend(scaleConfigs[i]);
		gantt.render();
	}

	// get number of columns in timeline
	function getUnitsBetween(from, to, unit, step) {
		var start = new Date(from),
			end = new Date(to);
		var units = 0;
		while (start.valueOf() < end.valueOf()) {
			units++;
			start = gantt.date.add(start, step, unit);
		}
		return units;
	}

	//Setting available scales
	var scaleConfigs = [
		// minutes
		/* {
			scales: [
				{subscale_unit: "minute", unit: "hour", step: 1, format: "%H"},
				{unit: "minute", step: 1, format: "%H:%i"}
			]
		}, */
		// hours
		/* {
			scales: [
				{subscale_unit: "hour", unit: "day", step: 1, format: "%j %M"},
				{unit: "hour", step: 1, format: "%H:%i"}

			]
		}, */
		// days
		{
			scales: [
				{subscale_unit: "day", unit: "month", step: 1, format: "%F"},
				{unit: "day", step: 1, format: "%j"}
			]
		},
		// weeks
		{
			scales: [
				{subscale_unit: "week", unit: "month", step: 1, date: "%F"},
				{
					unit: "week", step: 1, template: function (date) {
						var dateToStr = gantt.date.date_to_str("%d %M");
						var endDate = gantt.date.add(gantt.date.add(date, 1, "week"), -1, "day");
						return dateToStr(date) + " - " + dateToStr(endDate);
					}
				}
			]
		},
		// months
		/* {
			scales: [
				{subscale_unit: "month", unit: "year", step: 1, format: "%Y"},
				{unit: "month", step: 1, format: "%M"}
			]
		}, */
		// quarters
		{
			scales: [
				{subscale_unit: "quarter", unit: "year", step: 1, format: "%Y"},
				{subscale_unit: "month", unit: "quarter", step: 1, format: function quarterLabel(date) {
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
			]
		},
		// years
		{
			scales: [
				{subscale_unit: "year", unit: "year", step: 1, date: "%Y"},
				{
					unit: "year", step: 5, template: function (date) {
						var dateToStr = gantt.date.date_to_str("%Y");
						var endDate = gantt.date.add(gantt.date.add(date, 5, "year"), -1, "day");
						return dateToStr(date) + " - " + dateToStr(endDate);
					}
				}
			]
		},
		// decades
		{
			scales: [
				{
					subscale_unit: "year", unit: "year", step: 10, template: function (date) {
						var dateToStr = gantt.date.date_to_str("%Y");
						var endDate = gantt.date.add(gantt.date.add(date, 10, "year"), -1, "day");
						return dateToStr(date) + " - " + dateToStr(endDate);
					}
				},
				{
					unit: "year", step: 100, template: function (date) {
						var dateToStr = gantt.date.date_to_str("%Y");
						var endDate = gantt.date.add(gantt.date.add(date, 100, "year"), -1, "day");
						return dateToStr(date) + " - " + dateToStr(endDate);
					}
				}
			]
		}
	];

	var enabled = false;
	return {
		enable: function(){
			if(!enabled) {
				enabled = true;
				saveConfig();
				zoomToFit();
				gantt.render();
			}
		},
		isEnabled: function(){
			return enabled;
		},
		toggle: function(){
			if(this.isEnabled()){
				this.disable();
			}else{
				this.enable();
			}
		},
		disable: function(){
			if(enabled) {
				enabled = false;
				restoreConfig();
				gantt.render();
			}
		},
		//days 일 경우 css 적용
		setWeekend : function(level){
			if(level === 0) {
				gantt.templates.timeline_cell_class = function (item, date) {
					if (!gantt.isWorkTime(date)) {
						return "weekend";
					}
				};
			} else {
				gantt.templates.timeline_cell_class = function (item, date) {
					return "";
				};
			}
		}
	};

})(gantt);