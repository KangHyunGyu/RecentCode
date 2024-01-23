<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript">
document.addEventListener("DOMContentLoaded", (evt) => {

			//AuthorityGroupTypeList
			getAuthorityGroupTypeList();

			//grid setting
			createAUIGrid(columnLayout);

			//initial setting
			changeAuthorityClassification();
			
});

	//AUIGrid 생성 후 반환 ID
	var myGridID;

	//AUIGrid 칼럼 설정
	var columnLayout = [
		 {
		    dataField: "domainPathDisplayName",
		    headerText: "${e3ps:getMessage('도메인')}",
		    width: "15%",
		    editable: false
		  },
		  {
		    dataField: "authObjectTypeName",
		    headerText: "${e3ps:getMessage('권한 타겟')}",
		    width: "15%",
		    editable: false
		  },
		{
		    dataField: "groupName",
		    headerText: "${e3ps:getMessage('그룹 코드')}",
		    width: "15%",
		    editable: false,
		    filter: {
		      showIcon: true,
		      iconWidth: 30,
		    }
		  },
		  {
		    dataField: "name",
		    headerText: "${e3ps:getMessage('이름')}",
		    width: "15%",
		    editable: false
		  },
		  {
		    dataField: "authDisplayName",
		    headerText: "${e3ps:getMessage('권한')}",
		    width: "15%",
		    editable: false,
		    styleFunction: function (
		            rowIndex,
		            columnIndex,
		            value,
		            headerText,
		            item,
		            dataField
		          ) {
		            if (item.oid != null) {
		              return "aui-grid-non-editable-cell";
		            }
		            return null;
		          }
		  },
		  {
			dataField: "description",
			headerText: "${e3ps:getMessage('설명')}",
			width: "*%",
			editable: false
		  },
		 ];

	//AUIGrid 를 생성합니다.
	function createAUIGrid(columnLayout) {
		// 그리드 속성 설정
		var gridPros = {
			editable : true,

			selectionMode : "multipleCells",

			rowIdField : "oid",

			showSelectionBorder : true,

			noDataMessage : gridNoDataMessage,

			showRowNumColumn : true,

			wrapSelectionMove : true,

			showRowCheckColumn : true,

			enableFilter : false,

			enableMovingColumn : true,

			headerHeight : gridHeaderHeight,

			rowHeight : gridRowHeight,
		};

		// 실제로 #grid_wrap 에 그리드 생성
		myGridID = AUIGrid.create("#grid_wrap", columnLayout, gridPros);
		
		// 선택 변경 이벤트 바인딩
		AUIGrid.bind(myGridID, "selectionChange", (evt) => {
			AUIGrid.setCheckedRowsByIds(myGridID, evt.selectedItems.map((x)=>x.rowIdValue))
		});
		
		var gridData = new Array();
		AUIGrid.setGridData(myGridID, gridData);

	}

	function getGridData() {
		
		let codeTypeValue = document.getElementById('codeType').value;
		
		var param = new Object();
		param["code"] = codeTypeValue;
		
		AUIGrid.showAjaxLoader(myGridID);
		var url = getURLString("/admin/getAuthorityGroupList");
		ajaxCallServer(url, param, function(data){
			var gridData = data.list;
			$("#total").html(gridData.length);
			AUIGrid.setGridData(myGridID, gridData);
			AUIGrid.setAllCheckedRows(myGridID, false);
			AUIGrid.removeAjaxLoader(myGridID);
			
		},false);
	}


	//권한 분류 변경
	function changeAuthorityClassification(codeTypeValue) {
		if(codeTypeValue) document.getElementById('codeType').value = codeTypeValue;
		getGridData();
	}


	function addGroup() {
		var url = getURLString("/admin/createGroupPopup");
		openPopup(url, "createGroupPopup", 700, 500);
	}
	
	function deleteGroup(){
		var selectedItems = AUIGrid.getSelectedItems(myGridID).map((x) => x.rowIdValue );
		if(selectedItems.length == 0){
			alert("${e3ps:getMessage('삭제할 그룹을 선택해주세요')}.")
			return;
		}else if(!confirm("${e3ps:getMessage('선택하신 ')}" +selectedItems.length+"${e3ps:getMessage('개의 그룹을 삭제하시겠습니까')}?")){
			return false;
		}
		let param = new Object();
		param.oids = selectedItems;
		
		var url = getURLString("/admin/deleteAuthorityGroupAction");
		ajaxCallServer(url, param, function(data) {
			getGridData();
		});
	}
	
</script>
<div class="product">
	<!-- button -->
	<input type="hidden" id="authGroupId" name="authGroupId" value=""/>
	<div class="seach_arm pt15 pb5">
		<div class="leftbt">
			<select id="codeType" name="codeType" onchange="changeAuthorityClassification();">
			</select>
			${e3ps:getMessage('검색결과')}(<span id="total">0</span>)
		</div>
		<div class="rightbt">
			<button type="button" class="s_bt03" onclick="addGroup();">${e3ps:getMessage('그룹 추가')}</button>
			<button type="button" class="s_bt03" onclick="deleteGroup();">${e3ps:getMessage('그룹 삭제')}</button>
		</div>
	</div>
	<!-- //button -->
	<div class="semi_content pl30 pr30">
		<div class="semi_content2">
			<!-- //button -->
			<!-- 			<div class="list" id="grid_wrap" style="height:500px; border-top:2px solid #1064aa;"></div> -->
			<div class="list" id="grid_wrap" style="height: 594px; border-top: 2px solid #74AF2A;"></div>
		</div>
	</div>
</div>