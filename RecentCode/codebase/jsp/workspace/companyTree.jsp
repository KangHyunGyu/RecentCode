
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="product">
	<!-- button -->
	<div class="seach_arm pt15 pb5">
		<div class="leftbt"></div>
		<div class="rightbt">
			<button type="button" class="m_read" id="searchBtn" onclick="search();">검색</button>
			<button type="button" class="s_bt05" id="resetBtn" onclick="reset();">${e3ps:getMessage('초기화')}</button>
		</div>
	</div>
	<!-- //button -->

	<div class="semi_content pl30 pr30">
		<div class="semi_table1 pr20">
			<jsp:include page="${e3ps:getIncludeURLString('/department/include_departmentTree')}" flush="true">
				<jsp:param name="code" value="00000" />
				<jsp:param name="autoGridHeight" value="false" />
				<jsp:param name="gridHeight" value="600" />
			</jsp:include>
		</div>
		<div class="semi_content2">
			<!-- pro_table -->
			<div class="semi_table2">
				<form name="searchForm" id="searchForm">
					<input type="hidden" name="departmentOid" id="departmentOid" value="" />
					<table summary="">
						<caption></caption>
						<colgroup>
							<col style="width: 10%">
							<col style="width: 20%">
							<col style="width: 10%">
							<col style="width: 20%">
							<col style="width: 10%">
							<col style="width: 20%">
						</colgroup>

						<tbody>
							<tr>
								<th scope="col">${e3ps:getMessage('아이디')}</th>
								<td><input type="text" id="userId" name="userId" class="w100" /></td>
								<th scope="col">${e3ps:getMessage('이름')}</th>
								<td><input type="text" id="userName" name="userName" class="w100" /></td>
								<%-- <th scope="col">${e3ps:getMessage('계정 사용 여부')}</th>
								<td><select class="w80" id="isDisable" name="isDisable">
										<option value="">${e3ps:getMessage('전체')}</option>
										<option value="false">${e3ps:getMessage('사용중')}</option>
										<option value="true">${e3ps:getMessage('미사용')}</option>
								</select></td> --%>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
			<!-- //pro_table -->
			<!-- table list-->
			<div>
				<!-- button -->
				<div class="seach_arm2 pt10 pb5">
					<div class="leftbt">
						검색결과 (<span id="total">0</span> 개)
					</div>
					<div class="rightbt">
						<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0" />
						<button type="button" class="s_bt03" onclick="resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
						<button type="button" class="m_read" id="excelDown" onclick="xlsxExport();">엑셀 다운로드</button>
					</div>
				</div>
				<!-- //button -->
				<div class="list" id="user_grid_wrap" style="height: 500px"></div>
			</div>
			<!-- //table list-->
		</div>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		//enter key pressed event
		$("#searchForm").keypress(function(e) {
			if (e.keyCode == 13) {
				preRequestData();
			}
		});

		createUserAUIGrid(user_columnLayout);
		preRequestData();
	});

	var userGridID;

	function createUserAUIGrid(columnLayout) {
		var gridPros = {
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
			rowHeight : gridRowHeight
		}

		userGridID = AUIGrid.create("#user_grid_wrap", columnLayout, gridPros);
		
		var gridData = new Array();
		AUIGrid.setGridData(userGridID, gridData);
	}
	
	var user_columnLayout = [
		{
			dataField : "id",
			headerText : "아이디",
			width : "*%",
			editable : false,
			filter : {
				showIcon : true,
				iconWidth : 30,
			},
			
		},
		{
			dataField : "name",
			headerText : "이름",
			width : "*%",
			editable : false,
			filter : {
				showIcon : true,
				iconWidth : 30,
			}
		}, {
			dataField : "duty",
			headerText : "직책",
			width : "20%",
			editable : false,
			filter : {
				showIcon : true,
				iconWidth : 30,
			},
		}, {
			dataField : "departmentName",
			headerText : "부서",
			width : "*%",
			editable : false,
			filter : {
				showIcon : true,
				iconWidth : 30,
			},
		}, {
			dataField : "email",
			headerText : "이메일",
			width : "30%",
			editable : false,
			filter : {
				showIcon : true,
				iconWidth : 30,
			},
		}
	];

	
	//최초 데이터 요청
	function preRequestData() {
		var param = new Object();
		var paramArray = $("#searchForm").serializeArray();

		$(paramArray).each(function(idx, obj) {
			param[obj.name] = obj.value;
		});

		var url = getURLString("/admin/searchUserAction");
		ajaxCallServer(url, param, function(data) {
			// 그리드 데이터
			var gridData = data.list;
			AUIGrid.setGridData(userGridID, gridData);
	      	AUIGrid.setAllCheckedRows(userGridID, false);
	      	AUIGrid.removeAjaxLoader(userGridID);
			$("#total").html(gridData.length);
		});
	}

	//검색
	function search() {
		preRequestData();
	}
	
	function xlsxExport() {
		AUIGrid.setProperty(userGridID, "exportURL", getURLString("/common/xlsxExport"));
		 // 엑셀 내보내기 속성
		  var exportProps = {
				 postToServer : true,
		  };
		  // 내보내기 실행
		  AUIGrid.exportToXlsx(userGridID, exportProps);
	}
	
	function reset() {
		var locationDisplay = $("#locationDisplay").val();
		$("#searchForm")[0].reset();
		$("#locationDisplay").val(locationDisplay);

	}
	//필터 초기화
	function resetFilter(){
	    AUIGrid.clearFilterAll(userGridID);
	}
</script>