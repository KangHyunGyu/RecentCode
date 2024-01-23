// cLevel1,  subsidiary

// COLOR 설정
var BASEBACKGROUNDCOLOR = "#eff3f6";
var COLORINPROGRESS = "#2242ba";
var COLORDELAY = "#dd4650";
var COLORABORTED = "#f3c200";
var COLORCOMPLETED = "#1dac70";

//AUIGrid 칼럼 설정
var PROJECTDASHBOARDCOLUMN = [
  {
    dataField: "productName",
    headerText: "${e3ps:getMessage('사업구분')}",
    width: "5%",
    sortValue: "productName",
    filter: {
      showIcon: true,
      iconWidth: 30,
    },
  },
  {
    dataField: "code",
    headerText: "${e3ps:getMessage('프로젝트 번호')}",
    width: "8%",
    sortValue: "code",
    filter: {
      showIcon: true,
      iconWidth: 30,
    },
  },
  {
    dataField: "name",
    headerText: "${e3ps:getMessage('프로젝트 명')}",
    width: "25%",
    sortValue: "name",
    style: "AUIGrid_Left",
    filter: {
      showIcon: true,
      iconWidth: 30,
    },
    renderer: {
      type: "LinkRenderer",
      baseUrl: "javascript", // 자바스크립 함수 호출로 사용하고자 하는 경우에 baseUrl 에 "javascript" 로 설정
      // baseUrl 에 javascript 로 설정한 경우, 링크 클릭 시 callback 호출됨.
      jsCallback: function (rowIndex, columnIndex, value, item) {
        var oid = item.oid;
        openView(oid);
      },
    },
  },
  {
    dataField: "stateTag",
    headerText: "${e3ps:getMessage('상태')}",
    width: "6%",
    renderer: {
      type: "TemplateRenderer",
    },
    filter: {
      showIcon: true,
      iconWidth: 30,
    },
  },
  {
    dataField: "projectTypeDisplay",
    headerText: "${e3ps:getMessage('프로젝트 타입')}",
    width: "0",
    filter: {
      showIcon: true,
      iconWidth: 30,
    },
  },
  {
    dataField: "planStartDate",
    headerText: "${e3ps:getMessage('계획 시작일')}",
    width: "7%",
    filter: {
      showIcon: true,
      iconWidth: 30,
    },
  },
  {
    dataField: "planDuration",
    headerText: "${e3ps:getMessage('기간')}",
    width: "6%",
    filter: {
      showIcon: true,
      iconWidth: 30,
    },
  },
  {
    dataField: "planEndDate",
    headerText: "${e3ps:getMessage('계획 종료일')}",
    width: "7%",
    filter: {
      showIcon: true,
      iconWidth: 30,
    },
  },
  {
    dataField: "completion",
    headerText: "${e3ps:getMessage('진척률')}",
    width: "5%",
    dataType: "numeric",
    formatString: "##0.##",
    filter: {
      showIcon: true,
      iconWidth: 30,
    },
  },
  {
    dataField: "pmName",
    headerText: "PM",
    width: "6%",
    filter: {
      showIcon: true,
      iconWidth: 30,
    },
  },
  {
    dataField: "order_no",
    headerText: "${e3ps:getMessage('수주번호')}",
    width: "8%",
    filter: {
      showIcon: true,
      iconWidth: 30,
    },
  },
  {
    dataField: "cLevel1_nm",
    headerText: "${e3ps:getMessage('중분류')}",
    width: "7%",
    filter: {
      showIcon: true,
      iconWidth: 30,
    },
  },
  {
    dataField: "cLevel2_nm",
    headerText: "${e3ps:getMessage('세부분류')}",
    width: "7%",
    filter: {
      showIcon: true,
      iconWidth: 30,
    },
  },
  {
    dataField: "cLevel3_nm",
    headerText: "${e3ps:getMessage('소분류')}",
    width: "7%",
    filter: {
      showIcon: true,
      iconWidth: 30,
    },
  },
  {
    dataField: "creatorFullName",
    headerText: "${e3ps:getMessage('등록자')}",
    width: "6%",
    filter: {
      showIcon: true,
      iconWidth: 30,
    },
  },
  {
    dataField: "createDate",
    headerText: "${e3ps:getMessage('등록일')}",
    width: "7%",
    filter: {
      showIcon: true,
      iconWidth: 30,
    },
  },
];

function renderStateCategories() {
  for (const key in STATUS) {
    var html =
      "<div> <span style='background-color: " +
      STATUS[key].color +
      "'></span> <h4> " +
      STATUS[key].label +
      " </h4> </div>";
    $("#items").append(html);
  }
}

function renderChart(title, datas) {
  return {
    chart: {
      plotBackgroundColor: null,
      plotBorderWidth: 0,
      plotShadow: false,
      backgroundColor: "transparent",
      margin: [0, 0, 0, 0],
      spacing: [0, 0, 0, 0],
      events: {
        redraw: function () {},
      },
    },
    title: {
      text: title,
      align: "center",
      verticalAlign: "middle",
    },
    tooltip: {
      // useHTML: true,
      headerFormat:
        "{point.key}: <strong>{point.percentage:.1f}%</strong> <em>( {point.y} / {point.total} )</em>",
      pointFormat: "",
      // pointFormatter: function () {
      //   console.log(this, this.items, this.x, this.y, this.total);
      //   return this.items.map((ii) => ii.number).join(",");
      // },
    },
    accessibility: {
      point: {
        valueSuffix: "%",
      },
    },
    plotOptions: {
      pie: {
        allowPointSelect: true,
        dataLabels: {
          enabled: true,
          distance: -25,
          format: "{point.percentage:.1f} %",
          style: {
            fontWeight: "bold",
            color: "white",
          },
        },
      },
      series: {
        point: {
          events: {
            click: function (event) {
              // 차트 선택 초기화
              var charts = Highcharts.charts;

              charts.forEach((chart) => {
                if (chart && chart.getSelectedPoints().length) {
                  chart.getSelectedPoints()[0].select(null);
                }
              });

              var gridTitle =
                this.series.chart.title.textStr + " - " + this.name;
              if (AUIGrid.isCreated("#project-dashboard-grid")) {
                $("#gridTitle").text(gridTitle);
                AUIGrid.setGridData(myGridID, this.items);
              }
            },
          },
        },
      },
    },
    credits: false,
    exporting: false,
    series: [
      {
        type: "pie",
        innerSize: "50%",
        data: datas,
      },
    ],
  };
}

function getStateGrouping(state) {
  switch (state) {
    // 준비중,결재중,수정중,반려됨,수정중
    case "READYING":
    case "PAYING":
    case "IN PROGRESS":
    case "PROGRESS":
    case "EDITING":
    case "RETURN":
    case "READY":
      return "PROGRESS";
    // 지연
    case "DELAY":
      return "DELAY";
    // 완료됨
    case "COMPLETED":
      return "COMPLETED";
    // 중단됨,취소됨
    case "ABORTED":
    case "CANCELLED":
      return "ABORTED";
  }
}
