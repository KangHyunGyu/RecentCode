<%@page import="com.e3ps.project.key.ProjectKey.STATEKEY"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function () {
	getNumberCodeChildList("groupCode", "PROJECTNUMBERPROP", "GROUP", true, false, true);
	getNumberCodeChildList("materialCode", "PROJECTNUMBERPROP", "MATERIAL", true, false, true);
	getNumberCodeChildList("levelCode", "PROJECTNUMBERPROP", "LEVEL", true, false, true);
		
    //enter key pressed event
    $("#searchForm").keypress(function (e) {
      if (e.keyCode == 13) {
        search();
      }
    });

    //lifecycle list
    getLifecycleList("LC_Project");

    //grid setting
    createAUIGrid(columnLayout);

    //get grid data
    getGridData();
  });
  

  // 현재 페이지를 구분하기 위한 상수값
  var PROJECTSEARCHPAGE = true;

  //AUIGrid 생성 후 반환 ID
  var myGridID;

  //현재 페이지
  var page = 1;

  //row 로딩 개수
  var rows = 20;

  // 중복 요청을 피하기 위한 플래그
  var nowRequesting = false;

  //마지막 페이지 여부
  var isLastPage = false;

  //소트 값
  var sortValue = "";

  //소트 값으로 소팅되었는지 체크하는 값
  var sortCheck = true;

  //AUIGrid 칼럼 설정
  var columnLayout = [
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
      width: "*%",
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
      dataField: "planStartDate",
      headerText: "${e3ps:getMessage('계획 시작일')}",
      width: "7%",
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
      dataField: "stateTag",
      headerText: "${e3ps:getMessage('작업 현황')}",
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
      dataField: "pmName",
      headerText: "PM",
      width: "6%",
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

  //AUIGrid 를 생성합니다.
  function createAUIGrid(columnLayout) {
    // 그리드 속성 설정
    var gridPros = {
      selectionMode: "multipleCells",
      showSelectionBorder: true,
      noDataMessage: gridNoDataMessage,
      rowIdField: "_$uid",
      showRowNumColumn: true,
      showEditedCellMarker: false,
      wrapSelectionMove: true,
      showRowCheckColumn: false,
      enableFilter: true,
      enableMovingColumn: true,
      headerHeight: gridHeaderHeight,
      rowHeight: gridRowHeight,
      // 고정칼럼 카운트 지정
      fixedColumnCount: 4,
    };

    // 실제로 #grid_wrap 에 그리드 생성
    myGridID = AUIGrid.create("#grid_wrap", columnLayout, gridPros);

    // 셀 클릭 이벤트 바인딩
    AUIGrid.bind(myGridID, "cellClick", auiGridCellClickHandler);

    // 스크롤 체인지 이벤트 바인딩
    AUIGrid.bind(myGridID, "vScrollChange", vScollChangeHandelr);

    // 헤더 클릭 이벤트 바인딩
    //AUIGrid.bind(myGridID, "headerClick", auiGridHeaderClickHandler);
    
    AUIGrid.addRow(myGridID, new Object(), "last");
    var gridData = new Array();
    //AUIGrid.setGridData(myGridID, gridData);
  }

  function getGridData() {
    $("#searchForm").attr(
      "action",
      getURLString("/project/searchProjectScrollAction")
    );

    var param = new Object();

    param["page"] = page;
    param["rows"] = rows;
    param["sortValue"] = sortValue;
    param["sortCheck"] = sortCheck;

    if (page == 1) {
      AUIGrid.showAjaxLoader(myGridID);
    }

    formSubmit("searchForm", param, null, function (data) {
      // 그리드 데이터
      var gridData = data.list;
      var count = $("#count").html();
      
      if (page == 1) {
        // 그리드에 데이터 세팅(첫 요청)
        AUIGrid.setGridData(myGridID, gridData);

        count = gridData.length;
      } else {
        // 그리드에 데이터 세팅(추가 요청)
        AUIGrid.appendData(myGridID, gridData);

        count = parseInt(count) + gridData.length;
      }

      $("#count").html(count);
      $("#total").html(data.totalSize);
      $("#sessionId").val(data.sessionId);

      if (gridData.length == 0) {
        isLastPage = true;
      }
      AUIGrid.removeAjaxLoader(myGridID);

      nowRequesting = false;
    });
  }
  

  //셀 클릭 핸들러
  function auiGridCellClickHandler(event) {
    var dataField = event.dataField;
    var oid = event.item.oid;
  }

//------------------------이전 헤더 클릭 핸들러
/*
  //헤더 클릭 핸들러
  function auiGridHeaderClickHandler(event) {
    isLastPage = false;
    page = 1;
    $("#sessionId").val("");

    var dataField = event.dataField;

    if (event.item.sortValue == null) {
      return true;
    }

    if (sortValue == event.item.sortValue) {
      if (sortCheck == false) {
        sortValue = "";
        sortCheck = true;
        getGridData();

        return false;
      }
      sortCheck = !sortCheck;
    }

    sortValue = event.item.sortValue;

    getGridData();

    return false;
  }
*/
//------------------------이전 헤더 클릭 핸들러

  //스크롤 체인지 핸들어에서 무리한 작업을 하면 그리드 퍼포먼스가 떨어집니다.
  //따라서 무리한 DOM 검색은 자제하십시오.
  function vScollChangeHandelr(event) {
    // 스크롤 위치가 마지막과 일치한다면 추가 데이터 요청함
    if (event.position == event.maxPosition) {
      if (!isLastPage) {
        if (!nowRequesting) {
          page++;
          getGridData();
        }
      }
    }
  }

  //검색
  function search() {
    isLastPage = false;
    page = 1;
    sortValue = "";
    $("#sessionId").val("");
    getGridData();
  }

  //검색조건 초기화
  function reset() {
    var locationDisplay = $("#locationDisplay").val();
    $("#searchForm")[0].reset();
    $("#locationDisplay").val(locationDisplay);
  }

  //필터 초기화
  function resetFilter() {
    AUIGrid.clearFilterAll(myGridID);
  }

  function xlsxExport() {
    AUIGrid.setProperty(
      myGridID,
      "exportURL",
      getURLString("/common/xlsxExport")
    );

//     // 엑셀 내보내기 속성
//     var exportProps = {
//       postToServer: true,
//     };
    
    
    // 내보내기 실행
    AUIGrid.exportToXlsx(myGridID);
  }

  function changeLoadingRows(selected) {
    rows = parseInt($(selected).val());
    isLastPage = false;
    page = 1;
    sortValue = "";
    $("#sessionId").val("");
    getGridData();
  }

  function deleteProcess(id) {
    $("#" + id).val("");
    $("#" + id).trigger("change");
  }

  function getProductTemplate(element) {
    $("#project_code").val("").trigger("change");
    autocompleteProject("project_code", $("#product").val());
    search();
  }
</script>
<div class="product">
	<!-- button -->
	<div class="seach_arm pt10 pb5">
		<div class="leftbt">
		</div>
		<div class="rightbt">
			<button type="button" class="s_bt03" id="searchBtn" onclick="search();">${e3ps:getMessage('검색')}</button>
			<button type="button" class="s_bt05" id="resetBtn" onclick="reset();">${e3ps:getMessage('초기화')}</button>
		</div>
	</div>
	<!-- //button -->
	<!-- pro_table -->
	<div class="pro_table mr30 ml30">
		<form name="searchForm" id="searchForm">
			<input type="hidden" id="sessionId" name="sessionId" value="">
			<table class="mainTable">
				<colgroup>
					<col style="width:15%">
					<col style="width:35%">
					<col style="width:15%">
					<col style="width:35%">
				</colgroup>	
				<tbody>
					<tr>
						<th scope="col">${e3ps:getMessage('프로젝트번호')}</th>
						<td><input type="text" class="w90" id="number" name="number"></td>
						<th scope="col">${e3ps:getMessage('프로젝트명')}</th>
						<td><input type="text" class="w100" id="name" name="name"></td>
					</tr>
<!-- 					<tr> -->
<%-- 						<th scope="col">${e3ps:getMessage('개발구분')}</th> --%>
<!-- 						<td><select class="w100" id="devType" name="devType"></select></td> -->
<%-- 						<th scope="col">${e3ps:getMessage('용도')}</th> --%>
<!-- 						<td><select class="w100" id="purpose" name="purpose"></select></td> -->
<!-- 					</tr> -->
<!-- 					<tr> -->
<%-- 						<th scope="col">${e3ps:getMessage('고객사')}</th> --%>
<!-- 						<td><select class="w100" id="customer" name="customer"></select></td> -->
<!-- 						<th scope="col">END ITEM</th> -->
<!-- 						<td><select class="w100" id="endItem" name="endItem"></select></td> -->
<!-- 					</tr> -->
					<tr>
						<th scope="col">${e3ps:getMessage('계획시작일')}</th>
						<td class="calendar">
					        <input type="text" class="datePicker w25" name="planStartDate" id="planStartDate" readonly/>
							~
							<input type="text" class="datePicker w25" name="planStartDateE" id="planStartDateE" readonly/>
						</td>
						<th scope="col">${e3ps:getMessage('계획종료일')}</th>
						<td class="calendar">
					        <input type="text" class="datePicker w25" name="planEndDate" id="planEndDate" readonly/>
							~
							<input type="text" class="datePicker w25" name="planEndDateE" id="planEndDateE" readonly/>
						</td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('작업현황')}</th>
						<td>
							<select class="multiSelect w30" name="state" id="state" multiple style="height:20px;overflow-y: hidden;"></select>
						</td>
						<th scope="col">PM</th>
						<td>
							<div class="pro_view">
								<select class="searchUser" id="pm" name="pm" multiple data-width="60%">
								</select>
								<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('pm', 'multi');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
								<span class="pointer verticalMiddle" onclick="javascript:deleteUser('pm');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
							</div>
						</td>
					</tr>
					
					<tr>
						<th>Group</th>
						<td><select class="w100" id="groupCode" name="groupCode"></select></td>
						<th>Material</th>
						<td><select class="w100" id="materialCode" name="materialCode"></select></td>
					</tr>
					
					<tr>
						<th>Level</th>
						<td><select class="w100" id="levelCode" name="levelCode"></select></td>
						<th></th>
						<td></td>
					</tr>
				</tbody>
			</table>
		</form>	
	</div>
	<!-- //pro_table -->
	
	<!-- button -->
	<div class="seach_arm pt5 pb5">
		<div class="leftbt">
			<span>
				${e3ps:getMessage('검색 결과')} (<span id="total">0</span>)
			</span>
		</div>
		<div class="rightbt">
			<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0"/>
			<button type="button" class="s_bt03" onclick="resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
			<button type="button" class="s_bt03" onclick="excelDown('searchForm', 'excelDownProject');">${e3ps:getMessage('엑셀 다운로드')}</button>
<%-- 			<button type="button" class="s_bt03" onclick="xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button> --%>
		</div>
	</div>
	<!-- //button -->
	<!-- table list-->
	<div class="table_list">
		<div class="list" id="grid_wrap" style="height:500px"></div>
	</div>
	
	
	<!-- //table list-->
</div>
