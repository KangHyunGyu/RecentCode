<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>       
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<script>
//AUIGrid 생성 후 반환 ID
var licenseGridID;

//AUIGrid 칼럼 설정
var license_columnLayout = [
  {
    dataField: "id",
    headerText: "아이디",
    width: "*%",
    editable: false,
    filter: {
      showIcon: true,
      iconWidth: 30,
    },
    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
      //HTML 템플릿 작성

      var display = value;

      if (item.wtuserDeleted) {
        display += "(삭제됨)";
      }

      return display;
    },
  },
  {
    dataField: "name",
    headerText: "이름",
    width: "*%",
    editable: false,
    filter: {
      showIcon: true,
      iconWidth: 30,
    },
  },
  {
    dataField: "departmentName",
    headerText: "부서",
    width: "*%",
    editable: false,
    filter: {
      showIcon: true,
      iconWidth: 30,
    },
  },
  
  {
      dataField: "dutyDisplay",
      headerText: "직책",
      width: "20%",
      editable: false,
      filter: {
        showIcon: true,
        iconWidth: 30,
      },
    },
    { dataField : "deleteBtn", 
      headerText : "Del", 
      width:"10%", 
        renderer : {
           type : "ImageRenderer",
           altField: null,
           imgTableRef: {
        	   "default": "/Windchill/jsp/portal/images/delete_icon.png" // default
           }
        },
     }
];


//AUIGrid 를 생성합니다.
function createlicenseAUIGrid(columnLayout) {
	// 그리드 속성 설정
	var gridPros = {
	//editable : true,
		rowIdField: "pOid",
		selectionMode: "singleRow",
		showSelectionBorder: false,
		noDataMessage: gridNoDataMessage,
		showRowNumColumn: true,
		showEditedCellMarker: false,
		wrapSelectionMove: true,
		showRowCheckColumn: false,
		enableFilter: true,
		enableMovingColumn: true,
		headerHeight: gridHeaderHeight,
		rowHeight: gridRowHeight,
	};
	
	// 실제로 #grid_wrap 에 그리드 생성
	licenseGridID = AUIGrid.create("#license_grid_wrap", columnLayout, gridPros);
	
	//에디팅 정상 종료 이벤트 바인딩
	AUIGrid.bind(licenseGridID, "cellEditEnd", auiEventHandler);
	
	AUIGrid.bind(licenseGridID, "dropEnd", auiEventHandler);
	
	AUIGrid.bind(licenseGridID, "cellClick", auiEventHandler);
	
	/* AUIGrid.bind(licenseGridID, "addRow", function( event ) {
		console.log('hihi')
		 let itemList = event.items;
		 itemList.forEach((i) => {
			 i.deleteBtn = 'X'
		     AUIGrid.updateRowsById(licenseGridID, i);
		 })
	}); */
	
	var gridData = new Array();
	AUIGrid.setGridData(licenseGridID, gridData);
}



function auiEventHandler(event){ 
	var dataField = event.dataField;
	var rowItem = event.item;
	switch(event.type){
		case 'cellClick' :
			if(dataField == 'deleteBtn'){
				deleteLicenseUser(rowItem.oid);
			}
			break;
		case 'dropEnd' :
			break;
		case 'cellEditEnd' :
			break;
		default :
			break;
	}
}


/**
 * drop end -> save
 */
function dropEventSetLicenseUser(event){
	 
	 var a = $("#LicenseCount").html();
	 var b = $("#LicenseTotal").html();
	 if(a == b){
		 alert("더이상 등록할 수 없습니다.");
		 getLicenseUser();
		 return;
	 }

	var pid = event.pid;
	var pidToDrop = event.pidToDrop;
	var items = event.items;
	if(pid == pidToDrop){
		return false;
	}
	
	var param = new Object();
	param["peoOid"] = event.items[0].oid;
	//console.log(event.items[0].oid + event.items[0].name);
	param["type"] = $("#licenseList").val();
	//param["items"] = items;
	
	var url = getURLString("/admin/setLicenseUsers");
	ajaxCallServer(url, param, function(data){
		//alert(data.result);
		getLicenseUser();
	});
}
 
/**
 * license user delete
 */
function deleteLicenseUser(oid){
	var param = new Object();
	param["peoOid"] = oid;
	param["type"] = $("#licenseList").val();
	
	var url = getURLString("/admin/delLicenseUsers");
	ajaxCallServer(url, param, function(data){
		//alert(data.result);
		getLicenseUser();
	});
}


/*
 * License user grid 
 */
function getLicenseUser(){
	
	var v = $("#licenseList").val();
	
	if(v == ""){
		var gridData = new Array();
		AUIGrid.setGridData(licenseGridID, gridData);
		return;
	}
	
	var param = new Object();
	param["type"] = v;
	
	var url = getURLString("/admin/getLicenseUser");
    ajaxCallServer(url, param, function (data) {
      	// 그리드 데이터
		var gridData = data.list;
		gridData.map(t => t.deleteBtn = 'default')
		$("#LicenseTotal").html(gridData.length);
		$("#LicenseCount").html(data.toLiCount);

      	// 그리드에 데이터 세팅
      	AUIGrid.setGridData(licenseGridID, gridData);
      	AUIGrid.setAllCheckedRows(licenseGridID, false);
      	AUIGrid.removeAjaxLoader(licenseGridID);
    });
}
</script>
<body>
<div class="seach_arm2">
	<div class="leftbt mt10 mb5" style="flex:1">
		라이선스 관리 ( <span id="LicenseTotal">0</span> / <span id="LicenseCount">0</span> 개)
	</div>
	<div class="rightbt mt10 mb5"  style="flex:1;">
		<select id="licenseList" name="licenseList" class="multiSelect noResetBtn"  onchange="getLicenseUser(this)">
			<option value="">선택</option>
			<option value="ptc_wnc_advanced_named">설계자</option>
			<option value="ptc_wnc_navigate_contribute_n">일반사용자</option>
			<option value="ptc_wnc_navigate_view_named">뷰어전용</option>
		</select>
	</div>
</div>
<div class="list" id="license_grid_wrap" style="height: 530px"></div>
</body>
</html>