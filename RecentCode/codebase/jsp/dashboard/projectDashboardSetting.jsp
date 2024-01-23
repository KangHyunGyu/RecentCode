<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>

<style>
  .mainContainer {
    display: flex;
    flex-direction: column;
    height: calc(100% - 40px);
    box-sizing: border-box;
  }

  .header {
    display: flex;
    justify-content: space-between;
    height: 30px;
/*     border-bottom: 2px solid #1064aa; */
    border-bottom: 2px solid #74AF2A;
  }

  .setting-tap {
    display: flex;
    gap: 5px;
  }

  .setting-tap li {
    width: 110px;
    height: 30px;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #f5f5f5;
    color: #9ba3af;
    font-size: 12px;
  }

  .setting-tap li:hover,
  .setting-tap li.selected {
/*     background-color: #1064aa; */
    background-color: #74AF2A;
    color: #fff;
  }

  .setting-tap a {
    display: inline-block;
    width: 100%;
    text-align: center;
    padding: 5px 0;
    text-decoration: none;
    color: inherit;
  }

  .mainContainer .list {
    height: calc(100% - 40px);
    box-sizing: border-box;
    border-top: none;
    padding-top: 10px;
  }

  /* Unopened lightbox */
  .setting-content {
    display: none;
  }

  /* Opened lightbox */
  .setting-content:target {
    display: flex;
    flex-direction: column;
    height: 100%;
  }

  .setting-content ul.select-product {
    display: flex;
    width: 100%;
    height: 40px;
  }

  .setting-content ul.select-product li {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
  }

  .setting-content ul.select-product li.selected {
    background-color: bisque;
    color: black;
  }

  .setting-content section.chart-setting-container {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    gap: 3px;
  }

  .setting-content section.chart-setting-container.hide {
    display: none;
  }

  .setting-content details {
    position: relative;
    min-height: 30px;
    box-sizing: border-box;
    border: 2px solid #dedede;
    background-color: #fff;
  }

  .setting-content details summary {
    height: 30px;
/*     background-color: #004a9c; */
    background-color: #74AF2A;
    color: white;
    cursor: pointer;
    box-sizing: border-box;
    padding-top: 4px;
    padding-left: 10px;
  }

  .setting-content details:not([open]) {
    height: 30px;
  }

  .setting-content details[open] {
    height: 100%;
    min-height: 115px;
  }

  details .chart-title-container {
    height: 30px;
    background-color: #f7f7f7;
    color: #373c44;
    box-sizing: border-box;
    padding-top: 3px;
    padding-left: 5px;
  }

  details .chart-title-container label {
    color: #373c44;
    font-size: 12px;
  }

  details .chart-title-container input {
    width: 250px;
  }

  details .chart-value-container {
    display: flex;
    flex-direction: row;
    height: calc(100% - 60px);
  }

  .chart-value-container button {
    margin: 0 0 3px 0;
    height: 20px;
    font-size: 14px;
    padding: 0 2px;
  }

  .chart-value-container > div {
    width: 100px;
    padding: 5px;
    box-sizing: border-box;
  }

  .chart-value-container > div:last-child {
    overflow-y: auto;
    width: 100%;
    height: 100%;
  }
</style>

<div class="product mr30 ml30 pt10 pb10 mainContainer">
  <div class="header">
    <ul class="setting-tap">
      <li class="tap-cLevel1">
        <a href="#cLevel1"> ${e3ps:getMessage('제품군별')} </a>
      </li>
      <li class="tap-subsidiary">
        <a href="#subsidiary"> ${e3ps:getMessage('계열사별')} </a>
      </li>
    </ul>
    <div>
      <button
        type="submit"
        form="searchForm"
        class="i_read"
        style="width: 70px"
      >
        ${e3ps:getMessage('저장')}
      </button>
    </div>
  </div>

  <div class="list">
    <form name="searchForm" id="searchForm">
      <div class="setting-content" id="cLevel1">
        <section class="chart-setting-container"></section>
      </div>
      <div class="setting-content" id="subsidiary">
        <section class="chart-setting-container"></section>
      </div>
    </form>
  </div>
</div>

<script>
  $(document).ready(function () {
    $(window).on("hashchange", function (e) {
      var target = location.hash.slice(1);
      loadedPage(target);
    });

    init();

    var levels = getLevels();
    var subsidiaries = getSubsidiary();

    $("#cLevel1 select").each(function (index, element) {
      $(element).select2({
        data: levels.map(convertData),
        placeholder: "Input Search keyword",
        multiple: "multiple",
        minimumInputLength: 0,
        templateResult: function (item) {
          return "(" + item.id + ")" + item.text;
        },
      });
    });

    $("#subsidiary select").each(function (index, element) {
      $(element).select2({
        data: subsidiaries.map(convertData),
        placeholder: "Input Search keyword",
        multiple: "multiple",
        minimumInputLength: 0,
        templateResult: function (item) {
          return "(" + item.id + ")" + item.text;
        },
      });
    });

    $(".selectAll").each(function (index, element) {
      $(element).click(function (event) {
        $(event.target.parentElement.nextElementSibling.firstElementChild)
          .find("option")
          .prop("selected", true);
        $(
          event.target.parentElement.nextElementSibling.firstElementChild
        ).trigger("change");
      });
    });

    $(".clear").each(function (index, element) {
      $(element).click(function (event) {
        $(event.target.parentElement.nextElementSibling.firstElementChild)
          .val(null)
          .trigger("change");
      });
    });

    $("#searchForm").submit(saveData);

    location.href = "#cLevel1";
    if (location.hash) loadedPage(location.hash.slice(1));

    initLoadDataSet();
  });

  function initLoadDataSet() {
    var url = getURLString("/dashboard/getSetting");

    var param = new Object();

    var data = ajaxCallServer(
      url,
      {
        viewType: "",
      },
      null
    );

    if (data.result) {
      var items = data.list;

      items.forEach(function (item) {
        var section = $($("#" + item.viewType + " section")).find("details")[
          item.sort
        ];
        var sectionJ = $(section);
        if (item.title) sectionJ.find("input[type=text]").val(item.title);
        if (item.value) {
          sectionJ.find("select").val(item.value.split(","));
          sectionJ.find("select").trigger("change");
        }
      });
    }
  }

  function saveData(event) {
    event.preventDefault();

    var result = [];
    ["cLevel1", "subsidiary"].forEach(function (type) {
      var section = $("#" + type + " section");
      $(section)
        .find("details")
        .each(function (index, se) {
          var jSE = $(se);
          result.push({
            product: "",
            type: type,
            title: jSE.find("input[type=text]").val(),
            value: jSE.find("select").val().join(","),
            index: index,
          });
        });
    });
    var url = getURLString("/dashboard/saveSetting");

    var data = ajaxCallServer(
      url,
      {
        data: result,
      },
      null
    );
    if (data.result) {
      alert("저장되었습니다.");
      return data.list;
    }
    alert("저장중 문제가 발생하였습니다:" + data.msg);
    return [];
  }

  function loadedPage(target) {
    var typeTap = $(".setting-tap li");
    typeTap.each(function (_, element) {
      $(element).removeClass("selected");
      if ($(element).hasClass("tap-" + target)) {
        $(element).addClass("selected");
      }
    });
  }

  function createDetailsHtml(title) {
    return (
      "<details open>" +
      "<summary>" +
      title +
      "</summary>" +
      '<p class="chart-title-container">' +
      "<label> Title : </label>" +
      '<input type="text" placeholder="Chart Title." name="title" />' +
      "</p>" +
      '<div class="chart-value-container">' +
      "<div>" +
      '<button type="button" class="selectAll">Select All</button>' +
      '<button type="button" class="clear">Clear</button>' +
      "</div>" +
      "<div>" +
      '<select name="value" data-width="98%"></select>' +
      "</div>" +
      "</div>" +
      "</details>"
    );
  }

  function init() {
    var levelSection = $("#cLevel1 section.chart-setting-container");
    var subsidiarySection = $("#subsidiary section.chart-setting-container");
    ["Chart1", "Chart2", "Chart3", "Chart4", "Chart5", "Chart6"].forEach(
      function (title) {
        var html = createDetailsHtml(title);
        levelSection.append(html);
        subsidiarySection.append(html);
      }
    );
  }

  function convertData(item) {
    return {
      id: item.code_value,
      text: item.etc1,
    };
  }

	function getLevels() {
		var url = getURLString("/crm/searchMiddleClassCode");
		
		var param = new Object();
		
		var data = ajaxCallServer(url, param, null);
		if (data.result) {
			return data.list;
		}
		return [];
	}

	function getSubsidiary() {
		var url = getURLString("/crm/getSubsidiaryList");
		
		var param = new Object();
		
		var data = ajaxCallServer(url, param, null);
		if (data.result) {
			return data.list;
		}
		return [];
	}
</script>
