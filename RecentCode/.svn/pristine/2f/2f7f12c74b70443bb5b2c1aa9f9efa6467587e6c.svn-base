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
var userGridID;

//AUIGrid 칼럼 설정
var user_columnLayout = [
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
        display += "(미사용)";
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
    renderer : {
		type : "LinkRenderer",
		baseUrl : "javascript", // 자바스크립 함수 호출로 사용하고자 하는 경우에 baseUrl 에 "javascript" 로 설정
		// baseUrl 에 javascript 로 설정한 경우, 링크 클릭 시 callback 호출됨.
		jsCallback : function(rowIndex, columnIndex, value, item) {
			//javascript:moveLocation('/workspace/changePassword');
			var url = getURLString("/workspace/changePassword") + "?isPop=true&id="+item.id;
    		openPopup(url,"setPassword", 1200, 600);
		}
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
    dataField: "email",
    headerText: "이메일",
    width: "30%",
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
    
    {
        dataField: "wtuserDeleted",
        headerText: "비 사용",
        width: "10%",
        editable: false,
        filter: {
          showIcon: true,
          iconWidth: 30,
        },
        renderer : {
			type : "CheckBoxEditRenderer",
			editable : false, // 체크박스 편집 활성화 여부(기본값 : false)
       		//checkValue: false,//true, false 인 경우가 기본
       		//unCheckValue: false,
       		//사용자가 체크 상태를 변경하고자 할 때 변경을 허락할지 여부를 지정할 수 있는 함수 입니다.
       		checkableFunction: function (rowIndex, columnIndex, value, isChecked, item, dataField) {
       			//console.log("isDisable1 : " + document.getElementById("isDisable").checked);
       			var isChecked = document.getElementById("isDisable").checked;
       			var msg = "";
       			if(isChecked){
       				msg = "되돌리시겠습니까?";
       			}else{
       				msg = "라이센스 적용유저는 제외됩니다. 진행하시겠습니까?";
       			}
       			if(confirm(msg)){
       				var param = new Object();
					param["checkValue"] = !value;
					param["pOid"] = item.pOid;
       				
       				var url = getURLString("/admin/userIsDisabledAction");
       			    ajaxCallServer(url, param, function (data) {
       			    	if(data.result){
       			    		preRequestData();  //all user search
       			    		getLicenseUser();  //license user search
       			    	}
       			    });
       			    return true;
       			}else{
       				return false;
       			}
       		}
        },
      },
  /* {
    dataField: "erpid",
    headerText: "ERP ID",
    width: "10%",
    editable: false,
    filter: {
      showIcon: true,
      iconWidth: 30,
    },
  }, */
  /* 	{ dataField : "officeTel",		headerText : "회사번호",		width:"13%", editable:false,
		filter : {
			showIcon : true,
			iconWidth:30
		}
	},
	{ dataField : "cellTel",		headerText : "개인번호",		width:"13%", editable:false,
		filter : {
			showIcon : true,
			iconWidth:30
		}
	},
	{ dataField : "disableKor",		headerText : "재직 여부",		width:"10%", editable:false,
		filter : {
			showIcon : true,
			iconWidth:30
		}
	}, */
];


//AUIGrid 를 생성합니다.

function createUserAUIGrid(columnLayout) {
	// 그리드 속성 설정
	var gridPros = {
		//editable : true,
		rowIdField : "pOid",
		selectionMode : "multipleRows",
		showSelectionBorder : false,
		noDataMessage : gridNoDataMessage,
		showRowNumColumn : true,
		showEditedCellMarker : false,
		wrapSelectionMove : true,
		showRowCheckColumn : false,
		enableFilter : true,
		enableMovingColumn : true,
		headerHeight : gridHeaderHeight,
		rowHeight : gridRowHeight,

		//드래그 앤 드랍 프로퍼티
		editable : true, // 드래깅 행 이동 가능 여부 (기본값 : false)
		enableDrag : true, // 다수의 행을 한번에 이동 가능 여부 (기본값 : true)
		enableMultipleDrag : true, // 셀에서 바로 드래깅 해 이동 가능 여부 (기본값 : false) - enableDrag=true 설정이 선행되야 함
		enableDragByCellDrag : true, // 드랍 가능 여부 (기본값 : true)
		enableDrop : true, // 드랍을 받아줄 그리드가 다른 그리드에도 있는지 여부 (기본값 : false) - 그리드 간 행 이동인지 여부
		dropToOthers : true,
	};

	// 실제로 #grid_wrap 에 그리드 생성
	userGridID = AUIGrid.create("#user_grid_wrap", columnLayout, gridPros);

	//에디팅 정상 종료 이벤트 바인딩
	AUIGrid.bind(userGridID, "cellEditEnd", auiUserCellEditingHandler);

	// drag copy
	AUIGrid.bind(userGridID, "dropEndBefore", function(event) {
		if ($("#licenseList").val() == "") {
			alert("License Type Select.");
			return false;
		}
		if (event.items.length == 0) return false;
		event.isMoveMode = false;

		var pidToDrop = event.pidToDrop;
		var item = event.items[0];
		var notHave = AUIGrid.isUniqueValue(pidToDrop, "id", item.id);

		if (!notHave) {
			return false;
		}
		//dropEventSetLicenseUser(event);
	});
	
	// Drag End 시 Event 호출	
	AUIGrid.bind(userGridID, "dropEnd", dropEventSetLicenseUser);

	var gridData = new Array();
	AUIGrid.setGridData(userGridID, gridData);
}

//최초 데이터 요청
function preRequestData() {
    var param = new Object();

    var paramArray = $("#searchForm").serializeArray();

    $(paramArray).each(function (idx, obj) {
		param[obj.name] = obj.value;
    });

    const check = document.getElementById("isDisable").checked;

    if (check) {
		param.isDisable = "true";
    } else {
		param.isDisable = "false";
    }

    AUIGrid.showAjaxLoader(userGridID);
    var url = getURLString("/admin/searchUserActionAdmin");
    ajaxCallServer(url, param, function (data) {
      	// 그리드 데이터
		var gridData = data.list;
		gridData.map(t => t.deleteBtn = 'default')
		$("#userTotal").html(gridData.length);

      	// 그리드에 데이터 세팅
      	AUIGrid.setGridData(userGridID, gridData);
      	AUIGrid.setAllCheckedRows(userGridID, false);
      	AUIGrid.removeAjaxLoader(userGridID);
    });
}

//편집 핸들러
function auiUserCellEditingHandler(event) {
	var dataField = event.dataField;

	if (event.type == "cellEditEnd") {
		if (dataField == "duty") {
			var param = new Object();
			param["oid"] = event.item.oid;
			param["dutyName"] = event.value;

			var url = getURLString("/admin/setDutyAction");
			ajaxCallServer(url, param, function(data) {
				isLastPage = false;
				preRequestData();
			});
		}
	}
}

//필터 초기화
function resetFilter(){
    AUIGrid.clearFilterAll(userGridID);
}
</script>
<body>
<div class="seach_arm2">
	<div class="leftbt mt10 mb5"  style="flex:1">
		PLM 유저 ( <span id="userTotal">0</span> 명)
	</div>
	<div class="rightbt  mt10 mb5"  style="flex:1">
		<button type="button" class="l_update" id="setDept" onclick="setUserDepartment();">부서설정</button><br>
		<button type="button" class="s_bt03" onclick="resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
		<button type="button" class="l_read" id="excelDown" onclick="xlsxExport();">엑셀 다운로드</button>
	</div>
</div>

<div class="list" id="user_grid_wrap" style="height: 530px"></div>
</body>
</html>