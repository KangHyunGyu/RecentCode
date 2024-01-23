<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>

<!-- jQuery , jQuery UI 사용 -->
<script src="https://code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
<link rel="stylesheet" href="https://code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
<script src="/Windchill/jsp/component/AUIGrid/e3ps_auicomponent.js"></script>

<style>
/* 헤더 메뉴 스타일 정의 */
#headerMenu {
	position: absolute;
	display: none;
	z-index: 999;
}
/* jQuery UI Menu 스타일 재정의 */
.ui-menu {
	width: 160px;
	font-size: 12px;
	box-shadow: 3px 3px 3px rgba(0, 0, 0, 0.3);
	-webkit-box-shadow: 3px 3px 3px rgba(0, 0, 0, 0.3);
	-moz-box-shadow: 3px 3px 3px rgba(0, 0, 0, 0.3);
}  
</style>

<script>
  // 팝업창에서 현재 열려있는 페이지를 확인하기 위한 변수
  var SEARCHPRODUCTION = true;
  var myGridID;
  //현재 페이지
  var page = 1;

  //row 로딩 개수
  var rows = 20;

  //
  var sessionId = "";

  // 중복 요청을 피하기 위한 플래그
  var nowRequesting = false;

  //마지막 페이지 여부
  var isLastPage = false;

  //소트 값
  var sortValue = "";

  //소트 값으로 소팅되었는지 체크하는 값
  var sortCheck = true;
  var columnLayout = [
    {
      dataField: "distNumber",
      headerText: "${e3ps:getMessage('배포 요청 번호')}",
      width: "10%",
      renderer: {
        type: "LinkRenderer",
        baseUrl: "javascript",
        jsCallback: function (rowIndex, columnIndex, value, item) {
          var oid = item.oid;
          var url = getURLString("/distribute/viewRereptionDistribute") + "?oid="+oid;
          openPopup(url,"setPassword", 1100, 880);
        },
      },
      filter: {
        showIcon: true,
        iconWidth: 30,
      },
    },
    {
      dataField: "distName",
      headerText: "${e3ps:getMessage('배포 요청 명')}", style:"AUIGrid_Left",	
      width: "*",
      filter: {
        showIcon: true,
        iconWidth: 30,
      },
    },
    {
      dataField: "stateName",
      headerText: "${e3ps:getMessage('상태')}",
      width: "6%",
      filter: {
        showIcon: true,
        iconWidth: 30,
      },
    },
    {
      dataField: "distTypeName",
      headerText: "${e3ps:getMessage('배포 타입')}",
      width: "7%",
      filter: {
        showIcon: true,
        iconWidth: 30,
      },
    },
    {
      dataField: "companyName",
      headerText: "${e3ps:getMessage('배포 업체')}",
      width: "10%",
      filter: {
        showIcon: true,
        iconWidth: 30,
      },
    }, 
    {
      dataField: "creator",
      headerText: "${e3ps:getMessage('작성자')}",
      width: "6%",
      filter: {
        showIcon: true,
        iconWidth: 30,
      },
    },
    {
      dataField: "createDate",
      headerText: "${e3ps:getMessage('작성일')}",
      width: "6%",
      filter: {
        showIcon: true,
        iconWidth: 30,
      },
    },
    {
      dataField: "updateDate",
      headerText: "${e3ps:getMessage('수정일')}",
      width: "6%",
      filter: {
        showIcon: true,
        iconWidth: 30,
      },
    },
    {
       dataField: "downloadDate",
       headerText: "${e3ps:getMessage('다운로드 기한')}",
       width: "6%",
       filter: {
         showIcon: true,
         iconWidth: 30,
       },
     },
  ];

  $(document).ready(function () {
	  
    myGridID = AUIGrid.create("#grid_wrap", columnLayout, {
      selectionMode: "multipleCells",
      showSelectionBorder: true,
      noDataMessage: gridNoDataMessage,
      rowIdField: "_$uid",
      showRowNumColumn: true,
      showEditedCellMarker: false,
      wrapSelectionMove: true,
      showRowCheckColumn: false,
      enableFilter: true,
      enableMovingColumn: false,
      headerHeight: gridHeaderHeight,
      rowHeight: gridRowHeight,
    });

    // 스크롤 체인지 이벤트 바인딩
    AUIGrid.bind(myGridID, "vScrollChange", vScollChangeHandelr);
    

    //autoCompletedOrderNo("#order_noAuto select", "#product");
    getLifecycleList("LC_Default");
	
    //getProductionTypeList("#productionType");
    getGridData();
    
  });

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

  //검색조건 초기화
  function reset() {
    $("#searchForm")[0].reset();
  }

  function search() {
    isLastPage = false;
    page = 1;
    sortValue = "";
    sessionId = "";
    getGridData();
  }

  function getGridData() {
    var param = new Object();

    param["page"] = page;
    param["rows"] = rows;
    param["sortValue"] = sortValue;
    param["sortCheck"] = sortCheck;
    param["sessionId"] = String(sessionId);

    $("#searchForm").attr("action", getURLString("/distribute/distributeRereptionScrollAction"));

    AUIGrid.showAjaxLoader(myGridID);

    formSubmit("searchForm", param, null, function (data) {
      // 그리드 데이터
      var gridData = data.list;
      var count = $("#count").html();
		console.log('gridData',gridData)
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
      //$("#sessionId").val(data.sessionId);
      sessionId = data.sessionId;

      if (gridData.length < rows) {
        isLastPage = true;
      }

      AUIGrid.removeAjaxLoader(myGridID);

      nowRequesting = false;
    });
  }

  function changeLoadingRows(selected) {
    rows = parseInt($(selected).val());
    isLastPage = false;
    page = 1;
    sortValue = "";
    $("#sessionId").val("");
    getGridData();
  }
  /**************************************************************
   *           검색 : 자동검색 -> like 검색 함수
   ****************************************************************/
  function valueSearchOption(checkEle, autoContainer, textContainer) {
    if ($(checkEle).is(":checked")) {
      if ($("#" + autoContainer + " select").length > 0) {
        $("#" + autoContainer + " select").removeAttr("name");
      }
      if ($("#" + textContainer + " input[type=text]").length > 0) {
        $("#" + textContainer + " input[type=text]")
          .removeAttr("name")
          .attr("name", checkEle.dataset.name);
      }

      $("#" + autoContainer).hide();
      $("#" + textContainer).show();
    } else {
      if ($("#" + autoContainer + " select").length > 0) {
        $("#" + autoContainer + " select")
          .removeAttr("name")
          .attr("name", checkEle.dataset.name);
      }
      if ($("#" + textContainer + " input[type=text]").length > 0) {
        $("#" + textContainer + " input[type=text]").removeAttr("name");
      }

      $("#" + autoContainer).show();
      $("#" + textContainer).hide();
    }
  }
</script>
<div class="product">
  <!-- button -->
  <div class="seach_arm pt10 pb5">
    <div class="leftbt">
      <span class="title"
        ><img
          class="pointer"
          onclick="switchSearchMenu(this);"
          src="/Windchill/jsp/portal/images/minus_icon.png"
        />&nbsp;${e3ps:getMessage('검색 조건')}</span
      >
    </div>
    <div class="rightbt">
      <button type="button" class="s_bt03" id="searchBtn" onclick="search();">
        ${e3ps:getMessage('검색')}
      </button>
      <button
        type="button"
        class="s_bt03"
        id="searchBtn"
        onclick="switchDetailBtn();"
      >
        ${e3ps:getMessage('상세 검색')}
      </button>
      <button type="button" class="s_bt05" id="resetBtn" onclick="reset();">
        ${e3ps:getMessage('초기화')}
      </button>
    </div>
  </div>
  <!-- //button -->
  <!-- pro_table -->
  <div class="pro_table mr30 ml30">
    <form name="searchForm" id="searchForm">
      <input type="hidden" name="oid" />
      <input type="hidden" name="isManager" />
      <table class="mainTable">
        <colgroup>
          <col style="width: 15%" />
          <col style="width: 35%" />
          <col style="width: 15%" />
          <col style="width: 35%" />
        </colgroup>
        <tbody>
          
		  <!-- row 2  -->
          <tr>
          	<th scope="col">${e3ps:getMessage('배포 요청 명')}</th>
            <td colspan="1">
              <input type="text" id="distName" name="distName" class="w60"/>
            </td>
            <th scope="col">${e3ps:getMessage('배포 요청 번호')}</th>
            <td>
               <input type="text" id="distNumber" name="distNumber" class="w60"/>
            </td>
          </tr>
          <tr>
             <th scope="col">${e3ps:getMessage('배포 요청 구분')}</th>
            <td colspan="1">
              <!-- <input type="text" id="purchaseType" name="purchaseType" class="w100"/> -->
                <select id="purchaseType" name="purchaseType"
							class="w60">
						</select>
			</td>
          	<th scope="col">${e3ps:getMessage('상태')}</th>
			<td>
				<select class="multiSelect w10" id="state" name="state" multiple style="height:20px;overflow-y: hidden;"></select>
			</td>
          </tr>

		  <!-- row 3  -->
          <tr class="switchDetail calendar">
            <th scope="col">${e3ps:getMessage('작성자')}</th>
            <td>
            	<div class="pro_view">
					<select class="searchUser" id="creator" name="creator" multiple data-width="60%"></select>
					<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('creator', 'multi');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
					<span class="pointer verticalMiddle" onclick="javascript:deleteUser('creator');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
				</div>
            </td>
            <th scope="col">${e3ps:getMessage('작성일')}</th>
            <td>
              <input
                type="text"
                class="datePicker w30"
                name="pre_createDate"
                id="pre_createDate"
                readonly
              />
              ~
              <input
                type="text"
                class="datePicker w30"
                name="post_createDate"
                id="post_createDate"
                readonly
              />
            </td>
          </tr>
          
		  <!-- row 4  -->
          <tr class="switchDetail calendar">
          	<th scope="col">${e3ps:getMessage('품목')}</th>
			 <td>
				<span id="autoPartSearch">
				<select class="searchRelatedObject" id="relatedPart" name="relatedPart" multiple data-param="part" data-width="60%"></select>
				<span class="pointer verticalMiddle" onclick="javascript:deleteUser('relatedPart');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
			</td>
            <th scope="col">${e3ps:getMessage('수정일')}</th>
            <td>
              <input
                type="text"
                class="datePicker w30"
                name="pre_modifyDate"
                id="pre_modifyDate"
                readonly
              />
              ~
              <input
                type="text"
                class="datePicker w30"
                name="post_modifyDate"
                id="post_modifyDate"
                readonly
              />
            </td>
          </tr>
          
		  <!-- row 5  -->
          <tr class="switchDetail">
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
        ${e3ps:getMessage('검색 결과')} (<span id="count">0</span> /
        <span id="total">0</span>)
      </span>
    </div>
    <div class="rightbt">
      <img
        class="pointer mb5"
        id="favorite"
        data-type="false"
        data-oid=""
        onclick="add_favorite();"
        src="/Windchill/jsp/portal/images/favorites_icon_b.png"
        border="0"
      />
      <select
        id="rows"
        name="rows"
        style="width: 100px"
        onchange="javascript:changeLoadingRows(this)"
      >
        <option value="20">20</option>
        <option value="40">40</option>
        <option value="60">60</option>
        <option value="80">80</option>
        <option value="100">100</option>
      </select>
      <button type="button" class="s_bt03" onclick="resetFilter();">
        ${e3ps:getMessage('필터 초기화')}
      </button>

      <button type="button" class="s_bt03" onclick="xlsxExport();">
        ${e3ps:getMessage('엑셀 다운로드')}
      </button>
      <img class="pointer mb5" onclick="excelDown('searchForm', 'excelDownPurchaseRequest');" src="/Windchill/jsp/portal/icon/fileicon/xls.gif" border="0">
    </div>
  </div>
  <!-- //button -->
  <!-- table list-->
  
	<!-- ColumnSelectTest -->
	<!--  헤더 메뉴 구성하기 위한 메뉴 -->
			<ul id="headerMenu">
				<li id="h_item_1">오름 차순 정렬</li>
				<li id="h_item_2">내림 차순 정렬</li>
				<li id="h_item_3">정렬 초기화</li>
				<li></li>
				<li id="h_item_4">현재 칼럼 숨기기</li>
				<li id="h_item_5">칼럼 보이기/숨기기<ul id="h_item_ul">
					</ul>
				</li>
				<li></li>
				<li id="h_item_6">모든 칼럼 보이기</li>
			</ul>
  	<!-- //ColumnSelectTest -->
  
  <div class="table_list">
    <div class="list" id="grid_wrap" style="height: 500px"></div>
  </div>
  <!-- //table list-->
</div>
