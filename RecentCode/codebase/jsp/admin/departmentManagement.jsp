<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript">
$(document).ready(function () {
	//enter key pressed event
	$("#searchForm").keypress(function (e) {
		if (e.keyCode == 13) {
			search();
		}
	});
	
	$("#isDisable").SumoSelect();
	$("#isDisable").change(function () {
		search();
	});
	
	//grid setting
	createUserAUIGrid(user_columnLayout);
	createlicenseAUIGrid(license_columnLayout);
	
	//get grid data
	preRequestData();
	
	
});

function changeEditeMode(editable) {
	if (editable) {
		$("#grid_mode_able").hide();
		$("#grid_mode_disable").show();
	} else {
		$("#grid_mode_able").show();
		$("#grid_mode_disable").hide();
		var editedRowItems = AUIGrid.getEditedRowColumnItems(userGridID);
		console.log("editedRowItems", editedRowItems);
		if (editedRowItems.length === 0) return;
		var url = getURLString("/admin/updateUserAction");
		
		var data = ajaxCallServer(
			url,{
		  		editedRowItems: editedRowItems,
			},null
		);
		
		if (data.result) {
			alert("저장되었습니다.");
			preRequestData();
		} else {
			alert("저장중 문제가 발생하였습니다:" + data.msg);
		}
	
	    //var editedRowItems = AUIGrid.getEditedRowItems(myGridID);
	    //console.log(editedRowItems);
	}
	
	AUIGrid.setProp(userGridID, { editable: editable });// 0 헤더 속성값 변경하기
	AUIGrid.setColumnProp(userGridID, 4, {
		editable: editable,
	});
}

//검색
function search() {
  	preRequestData();
}

function xlsxExport(){
	AUIGrid.setProperty(
		userGridID,
		"exportURL",
		getURLString("/common/xlsxExport")
	);
	
	// 엑셀 내보내기 속성
	var exportProps = {
		postToServer: true,
	};
	// 내보내기 실행
	AUIGrid.exportToXlsx(userGridID, exportProps);
}
  
//검색조건 초기화
function reset() {
	$("#searchForm")[0].reset();
}

//부서 설정
function setUserDepartment() {
	var departmentOid = $("#departmentOid").val();
	
	if (departmentOid == null || departmentOid == "") {
		openNotice("${e3ps:getMessage('부서를 선택하세요.')}");
		return;
	}
	
	var url = getURLString("/admin/viewSetDepartmentPopup")+"?departmentOid="+departmentOid;
	openPopup(url, "setDepartment", 1100, 700);
}

/** 부서 동기화
 *	HR 동기화 - worldex 
 */
function deptSync(){
	alert("동기화를 시작합니다.");
	var url = getURLString("/restFul/deptSyncAction");
	
	ajaxCallServer(url, null, function (data) {
		if(data.result){
			alert("${e3ps:getMessage('동기화가 완료되었습니다.')}");
			search();
			getDepartmentTree();
		}
	}, true);
}


/**
 * 유저 동기화
 * HR 동기화 - worldex
 */
function userSync(){
	var url = getURLString("/restFul/userSyncAction");
	ajaxCallServer(url, null, function (data) {
		if(data.result){
			search();
			alert("${e3ps:getMessage('동기화가 완료되었습니다.')}");
		}
	}, true);
}
</script>
<div class="product">
	<!-- button -->
	<div class="seach_arm pt15 pb5">
		<div class="leftbt"></div>
		<div class="rightbt">
			<button type="button" class="s_bt03" id="searchBtn" onclick="search();">${e3ps:getMessage('검색')}</button>
			<button type="button" class="s_bt05" id="resetBtn" onclick="reset();">${e3ps:getMessage('초기화')}</button>
		</div>
	</div>
	<!-- //button -->

	<div class="semi_content pl30 pr30">
		<div class="semi_table1 pr20">
			<div>
				<button type="button" class="l_read" id="deptSync" onclick="deptSync();">부서동기화</button>
				<button type="button" class="l_read" id="userSync" onclick="userSync();">유저동기화</button>
			</div>
			<jsp:include page="${e3ps:getIncludeURLString('/department/include_departmentTree')}" flush="true">
				<jsp:param name="autoGridHeight" value="false" />
				<jsp:param name="gridHeight" value="550" />
				<jsp:param name="isAdmin" value="true" />
			</jsp:include>
		</div>
		
		<div class="semi_content2">
			<!-- pro_table -->
			<div class="semi_table2">
				<form name="searchForm" id="searchForm">
					<input type="hidden" name="departmentOid" id="departmentOid"
						value="" />
					<table summary="">
						<caption></caption>
						<colgroup>
							<col style="width: 10%" />
							<col style="width: 20%" />
							<col style="width: 10%" />
							<col style="width: 20%" />
							<col style="width: 10%" />
							<col style="width: 20%" />
						</colgroup>

						<tbody>
							<tr>
								<th scope="col">${e3ps:getMessage('아이디')}</th>
								<td><input type="text" id="userId" name="userId" class="w100" /></td>
								<th scope="col">${e3ps:getMessage('이름')}</th>
								<td><input type="text" id="userName" name="userName" class="w100" /></td>
								<th scope="col">${e3ps:getMessage('비 사용')}</th>
								<td><input type="checkbox" name="isDisable" id="isDisable" /></td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="semi_content">
				<!-- button -->
				<div class="pr20" style="flex:1">
					<jsp:include page="/jsp/admin/include/userGrid.jsp" flush="true"/>
				</div>
				<div class="p110" style="flex:1">
					<jsp:include page="/jsp/admin/include/licenseGrid.jsp" flush="true"/>
				</div>
			</div>
		</div>
	</div>
</div>
