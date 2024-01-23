<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript">
$(document).ready(function(){
	
	$("#searchForm").keypress(function(e){
		if(e.keyCode==13){
			search();
		}
	});
	
	getLifecycleList("LC_Default");
	
	createAUIGrid(columnLayout);
	
	getGridData();
	
});

//팝업창에서 현재 열려있는 페이지를 확인하기 위한 변수
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
    headerText: "${e3ps:getMessage('의뢰서 요청 번호')}",
    width: "13.5%",
    renderer: {
      type: "LinkRenderer",
      baseUrl: "javascript",
      jsCallback: function (rowIndex, columnIndex, value, item) {
        var oid = item.oid;
        openView(oid);
      },
    },
    filter: {
      showIcon: true,
      iconWidth: 30,
    },
  },
  {
    dataField: "distName",
    headerText: "${e3ps:getMessage('의뢰서 요청 명')}", style:"AUIGrid_Left",	
    width: "*%",
    filter: {
      showIcon: true,
      iconWidth: 30,
    },
  },
  {
    dataField: "purposeDisplay",
    headerText: "${e3ps:getMessage('용도')}",
    width: "6%",
    filter: {
      showIcon: true,
      iconWidth: 30,
    },
  },
  {
    dataField: "distDate",
    headerText: "${e3ps:getMessage('배포일')}",
    width: "8%",
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
  }
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
		showRowCheckColumn : true,
		enableFilter: true,
		enableMovingColumn: false,
		headerHeight: gridHeaderHeight,
		rowHeight: gridRowHeight,
		rowCheckToRadio:true
	};

	
	// 실제로 #grid_wrap 에 그리드 생성
	myGridID = AUIGrid.create("#grid_wrap", columnLayout, gridPros);
	
	 // 스크롤 체인지 이벤트 바인딩
    AUIGrid.bind(myGridID, "vScrollChange", vScollChangeHandelr);
	
	var gridData = new Array();
	AUIGrid.setGridData(myGridID, gridData);
}

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
  param["state"] = '${state}';
  param["regException"] = 'true';
 console.log('param',param)
  $("#searchForm").attr("action", getURLString("/distribute/searchDistributeScrollAction"));

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

function selectDistributeDoc(){
	var checkItemList = AUIGrid.getCheckedRowItems(myGridID);
	checkItemList = checkItemList.map((x)=>x.item)
	
	if(checkItemList.length == 0) {
		openNotice("${e3ps:getMessage('출도 의뢰서를 선택해주세요')}");
		return;
	}
	
	if(opener.window.addDistributeDoc&& typeof opener.window.addDistributeDoc == 'function') {
		if(!confirm("${e3ps:getMessage('선택하신 의뢰서를 추가하시겠습니까?')}")){
			return false;
		}
		opener.window.addDistributeDoc(checkItemList[0]);
	}
	
	window.close();
}
</script>
<!-- pop -->
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('유저 검색')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->
	<div class="semi_content">
		<div class="semi_content2">
			<!-- button -->
			<div class="seach_arm pt10 pb5">
				<div class="leftbt">
					<span class="title"><img class="pointer" onclick="switchSearchMenu(this);" src="/Windchill/jsp/portal/images/minus_icon.png" />&nbsp;${e3ps:getMessage('검색 조건')}</span>
				</div>
				<div class="rightbt">
					<button type="button" class="s_bt03" id="searchBtn" onclick="search();">${e3ps:getMessage('검색')}</button>
					<button type="button" class="s_bt05" id="resetBtn" onclick="reset();">${e3ps:getMessage('초기화')}</button>
				</div>
			</div>
			<!-- //button -->
			<!-- pro_table -->
			<div class="semi_table2 pt10">
				<div class="table_list">
					<form name="searchForm" id="searchForm" style="margin-bottom:0">
						<input type="hidden" name="oid" /> <input type="hidden" name="isManager" />
						<table class="mainTable">
							<colgroup>
								<col style="width: 15%" />
								<col style="width: 35%" />
								<col style="width: 15%" />
								<col style="width: 35%" />
							</colgroup>
							<tbody>
								<tr>
									<th scope="col">${e3ps:getMessage('의뢰서 요청 명')}</th>
									<td colspan="1"><input type="text" id="distName" name="distName" class="w60" /></td>
									<th scope="col">${e3ps:getMessage('의뢰서 요청 번호')}</th>
									<td><input type="text" id="distNumber" name="distNumber" class="w60" /></td>
								</tr>
								<tr class="calendar">
									<th scope="col">${e3ps:getMessage('작성일')}</th>
									<td><input type="text" class="datePicker w30" name="pre_createDate" id="pre_createDate" readonly /> ~ <input type="text" class="datePicker w30" name="post_createDate" id="post_createDate" readonly /></td>
									<th scope="col">${e3ps:getMessage('수정일')}</th>
									<td><input type="text" class="datePicker w30" name="pre_modifyDate" id="pre_modifyDate" readonly /> ~ <input type="text" class="datePicker w30" name="post_modifyDate" id="post_modifyDate" readonly /></td>
								</tr>
								<tr>
									<th scope="col">${e3ps:getMessage('작성자')}</th>
									<td colspan="3">
										<div class="pro_view">
											<select class="searchUser" id="creator" name="creator" multiple data-width="25%"></select> <span class="pointer verticalMiddle" onclick="javascript:openUserPopup('creator', 'multi');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span> <span class="pointer verticalMiddle" onclick="javascript:deleteUser('creator');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
										</div>
									</td>
								</tr>
							</tbody>
						</table>
					</form>
				</div>
			</div>
			<!-- //pro_table -->
			<!-- table list-->
			<div class="table_list">
				<!-- button -->
				<div class="seach_arm2 pt5 pb5">
					<div class="leftbt">
						<span> ${e3ps:getMessage('검색 결과')} (<span id="count">0</span> / <span id="total">0</span>)
						</span>
					</div>
					<div class="rightbt">
						<button type="button" class="s_bt03" onclick="javascript:selectDistributeDoc()">${e3ps:getMessage('선택')}</button>
						<button type="button" class="s_bt03" onclick="resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
					</div>
				</div>
				<!-- //button -->
				<div class="list" id="grid_wrap" style="height: 500px;"></div>
			</div>
			<!-- //table list-->
		</div>
	</div>
</div>		
