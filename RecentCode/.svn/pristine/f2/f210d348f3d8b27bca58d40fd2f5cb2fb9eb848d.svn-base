<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<style>
</style>
<script type="text/javascript">
$(document).ready(function(){
			
			let currentSearchValue = '';
	
			//getAuthorityGroupTypeList();
			AUIGrid.destroy(myGridID);
			//grid setting
			createAUIGrid(columnLayout);

			
			document.querySelector('#searchName').addEventListener('keyup', function(evt){
				let term = evt.target.value.trim();
				let direction = true;
				if(term == '') {
					AUIGrid.setAllCheckedRows(myGridID, false); 
					AUIGrid.clearSelection(myGridID);
				}
				if(evt.keyCode == 38 ){
					direction = false;
				}
				
				var options = {
					direction: direction,
					caseSensitive: false,
					wholeWord: false,
					wrapSearch: true
				};
				
				if(currentSearchValue != term){
					AUIGrid.searchAll(myGridID, term, options);
				}else if(evt.keyCode == 38 || evt.keyCode == 40 || evt.keyCode == 13){
					AUIGrid.searchAll(myGridID, term, options);
				}
				
				currentSearchValue = term;
		
			})
			//changeAuthorityClassification();
			
});

	//AUIGrid 생성 후 반환 ID
	var myGridID;

	//AUIGrid 칼럼 설정
	var columnLayout = [

		{
		    dataField: "id",
		    headerText: "아이디",
		    width: "20%",
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
		 ];

	//AUIGrid 를 생성합니다.
	function createAUIGrid(columnLayout) {
		// 그리드 속성 설정
		var gridPros = {
			editable : true,

			selectionMode : "multipleCells",

			rowIdField : "_$uid",

			showSelectionBorder : true,

			noDataMessage : gridNoDataMessage,

			showRowNumColumn : true,

			wrapSelectionMove : true,

			showRowCheckColumn : true,

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
		
		var param = new Object();
		param["oid"] = document.getElementById('groupOid').value;
		
		AUIGrid.showAjaxLoader(myGridID);
		var url = getURLString("/admin/getGroupUserList");
		ajaxCallServer(url, param, function(data){
			var gridData = data.list;
			$("#total").html(' ('+gridData.length+')');
			AUIGrid.setGridData(myGridID, gridData);
			AUIGrid.setAllCheckedRows(myGridID, false);
			AUIGrid.removeAjaxLoader(myGridID);
			
		},false);
	}

	/*
	//권한 분류 변경
	function changeAuthorityClassification(codeTypeValue) {
		
		AUIGrid.setGridData(myGridID, []);
		
		let selectValue = document.querySelector('#codeType').value;
		
		if(codeTypeValue){
			selectValue = codeTypeValue;
			document.querySelector('#codeType').value = selectValue;
		}
		
		AUIGrid.destroy(tree_myGridID);
		createCodeTypeTreeGrid(tree_columnLayout);
		resizeGrid();

		getAuthorityGroupList(selectValue);

		setTimeout(() => {
			getGridData();
		}, 300);
		

	}
	 */

	//유저 삭제 버튼
	function deleteGroupUser() {
		
		let checkItemList = AUIGrid.getCheckedRowItemsAll(myGridID);
		
		if(checkItemList.length == 0){
			alert("${e3ps:getMessage('삭제할 사용자를 선택해주세요')}.")
			return;
		}else if(!confirm("${e3ps:getMessage('선택하신 사용자를 삭제하시겠습니까')}?")){
			return false;
		}

		let param = new Object();
		param["addUserList"] = checkItemList.map((item)=>item.oid);
		param["oid"] = document.getElementById('groupOid').value;
		param["edit"] = false;
		
		let url = getURLString("/admin/editGroupUserAction");
		ajaxCallServer(url, param, function(data) {
			getGridData();
			preRequestData();
		});
		
	}

	//저장 버튼
	function saveGroupUser(checkItemList){
		//그룹에 이미 유저가 존재한다면 저장할 리스트에서 제거
		checkItemList = checkItemList.filter((item)=> !AUIGrid.getGridData(myGridID).some((x)=>x.oid == item.oid))
		let param = new Object();
		param["addUserList"] = checkItemList.map((item)=>item.oid);
		param["oid"] = document.getElementById('groupOid').value;
		param["edit"] = true;
		
		let url = getURLString("/admin/editGroupUserAction");
		ajaxCallServer(url, param, function(data) {
			getGridData();
			preRequestData();
		});
	}
	
	var isExpanded = false;
	function expand() {
		if (!isExpanded) {
			AUIGrid.expandAll(menuTree_myGridID);
			isExpanded = true;
		} else {
			AUIGrid.collapseAll(menuTree_myGridID);
			isExpanded = false;
		}
	}

	function changeOption(item){
		var selectedRow;
		var displayName;
		if("${type}" == 'folder'){
			selectedRow = AUIGrid.getSelectedRows(folderTree_myGridID)[0];
			displayName = selectedRow.name;
		}else if("${type}" == 'object'){
			selectedRow = AUIGrid.getSelectedRows(objectTree_myGridID)[0];
			displayName = selectedRow.display;
		}
		
		if(!selectedRow.readGroupOid || !selectedRow.permissionGroupOid) return;
		
		switch (item.value) {
		case 'ALL': item.value = 'READ';
					 item.innerText = "${e3ps:getMessage('읽기/다운')}"	;
					 item.className = 'full_close';
					 document.getElementById('groupOid').value=selectedRow.readGroupOid;
					 document.querySelectorAll('.gridTitle').forEach((ele)=>{
							ele.innerText = "[ "+displayName+" - ${e3ps:getMessage('읽기/다운')} ]";
					 })
			break;
		case 'READ': item.value = 'ALL';
					 item.innerText = "${e3ps:getMessage('전체 권한')}"	;
					 item.className = 'full_delete';
					 document.getElementById('groupOid').value=selectedRow.permissionGroupOid;
					 document.querySelectorAll('.gridTitle').forEach((ele)=>{
							ele.innerText = "[ "+displayName+" - ${e3ps:getMessage('전체 권한')} ]";
					 })
			break;
		default : break;
		}
		document.getElementById('parentGroupOid').value=selectedRow.parentGroupOid;
		getGridData();
		preRequestData();
	}
</script>
	<!-- //button -->
	<div class="semi_content pl30 pr30" style="height:600px;">
		<div class="semi_table4 pr20" id="codeTypeTree">
			<c:if test="${type eq 'menu'}">
				<div class="seach_arm3">
					<div class="leftbt">
						<span style="font-weight:bold;font-size:15px;">MENU</span>
					</div>
					<div class="rightbt">
						<button type="button" class="s_bt03" onclick="expand();">${e3ps:getMessage('확장/축소')}</button>
					</div>
				</div>
				<jsp:include page="${e3ps:getIncludeURLString('/admin/include_authorityMenuTree')}" flush="true">
					<jsp:param name="autoGridHeight" value="false" />
					<jsp:param name="gridHeight" value="600" />
				</jsp:include>
			</c:if>
			<c:if test="${type eq 'folder'}">
				<div class="seach_arm3">
					<div class="leftbt">
						<span style="font-weight:bold;font-size:15px;">FOLDER</span>
					</div>
					<div class="rightbt">
						<button type="button" class="full_delete" id="authTypeBtn" style="width:80px;height:27px" value="ALL" onclick="changeOption(this);">${e3ps:getMessage('전체 권한')}</button>
					</div>
				</div>
				<jsp:include page="${e3ps:getIncludeURLString('/admin/include_authorityFolderTree')}" flush="true">
					<jsp:param name="autoGridHeight" value="false" />
					<jsp:param name="gridHeight" value="600" />
				</jsp:include>
			</c:if>
			<c:if test="${type eq 'object'}">
				<div class="seach_arm3">
					<div class="leftbt">
						<span style="font-weight:bold;font-size:15px;">OBJECT</span>
					</div>
					<div class="rightbt">
						<button type="button" class="full_delete" id="authTypeBtn" style="width:80px;height:27px" value="ALL" onclick="changeOption(this);">${e3ps:getMessage('전체 권한')}</button>
					</div>
				</div>
				<jsp:include page="${e3ps:getIncludeURLString('/admin/include_authorityObjectTree')}" flush="true">
					<jsp:param name="autoGridHeight" value="false" />
					<jsp:param name="gridHeight" value="600" />
				</jsp:include>
			</c:if>
		</div>
		<div class="semi_content3 mr20">
			<div class="seach_arm3">
				<div class="leftbt" style="width:70% !important">
					<span class="gridTitle"></span><span> - ${e3ps:getMessage('권한 사용자 수')}</span><span id="total"> (0)</span>
				</div>
				<div class="rightbt" style="width:30% !important">
					<input type="text" id="searchName" class="w50" style="height:20px;" placeholder="검색"/>
					<%-- <button type="button" class="i_update" onclick="openUserPopup('', 'multi');">${e3ps:getMessage('추가')}</button> --%>
					<button type="button" class="i_delete" onclick="deleteGroupUser();">${e3ps:getMessage('삭제')}</button>
				</div>
			</div>
			
			<!-- //button -->
			<!-- 			<div class="list" id="grid_wrap" style="height:500px; border-top:2px solid #1064aa;"></div> -->
			<div class="list" id="grid_wrap" style="height: 100%; border-top: 2px solid #74AF2A;"></div>
		</div>
		
		<jsp:include page="${e3ps:getIncludeURLString('/admin/include_authorityDeptUser')}" flush="true">
				<jsp:param name="autoGridHeight" value="false" />
				<jsp:param name="gridHeight" value="600" />
		</jsp:include>
	</div>