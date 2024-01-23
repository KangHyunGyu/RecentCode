<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- pop -->
<script type="text/javascript">
$(document).ready(function(){
	getNumberCodeList("projectRole", "PROJECTROLE", false, false);
	
	getGridData();
});
function saveMember(){
	
// 	if(!checkValidate()) {
// 		return;
// 	}
	
	$("#editForm").attr("action",getURLString("/project/editMemberAction2"));
	formSubmit("editForm", null, "${e3ps:getMessage('수정하시겠습니까?')}",function(){
		if(opener.window.viewProject){
			opener.window.viewProject();
		}
		window.close();
	}, true);
}

// function checkValidate() {
	
// 	var listPm = roleName.val("pm");
	
// 	if(listPm.length > 1 || listPm.length == 0){
		
// 		openNotice("${e3ps:getMessage('PM을 한명만 선택하세요.')}");
// 		return false;
// 	} 
	
// 	return true;
// }

function getGridData(){
	
	let params = new Object();
	params["oid"] = "${oid}";
	let data = ajaxCallServer(getURLString("/project/getEditMemberList"), params, null);
	let list = data.list;
	let dupCheck = new Set();
	let htmlString = "";
	let roleUserObj = {};
	list.forEach(function(item){
		
		if(!dupCheck.has(item.roleCode)){
			dupCheck.add(item.roleCode);
			htmlString += "<tr>";
			htmlString += "<td>" + item.roleName + "</td>";
			htmlString += "<td>";
			htmlString += "<div class='pro_view'>";
			if(item.roleCode == 'PR01'){
				htmlString += "<select class='searchUser' id='"+item.roleCode+"' name='"+item.roleCode+"' data-width='60%'>";
			}else{
				htmlString += "<select class='searchUser' id='"+item.roleCode+"' name='"+item.roleCode+"' multiple data-width='60%'>";
			}
			htmlString += "</select>";
			htmlString += "	<span class='pointer verticalMiddle' onclick=\"javascript:openUserPopup('"+item.roleCode+"','multi');"+"\">";
			htmlString += "	<img class='verticalMiddle' src='/Windchill/jsp/portal/images/search_icon2.png'></span>";
			htmlString += "	<span class='pointer verticalMiddle' onclick=\"javascript:deleteUser('"+item.roleCode+"','multi');"+"\">";
			htmlString += "	<img class='verticalMiddle' src='/Windchill/jsp/portal/images/delete_icon.png'></span>";
			htmlString += "</div>";
			htmlString += "</td>";
			htmlString += "</tr>";
		}
		if(roleUserObj[item.roleCode] == null){
			roleUserObj[item.roleCode] = [];
		}
		if(item.userName != null){
			roleUserObj[item.roleCode].push(item);
		}
	});
	
	$("#tableDataList").append(htmlString);
	
	let roleCodeList = Object.keys(roleUserObj);
	for(let i = 0; i < roleCodeList.length; i++){
		let roleCode = roleCodeList[i];
		let userList = roleUserObj[roleCode];
		
		for(let j = 0; j < userList.length; j++){
			let user = userList[j];
			var newOption = new Option(user.userName, user.peopleOid, true, true);
			$('#'+roleCode).append(newOption);
		}
		$('#'+roleCode).trigger('change');
	}
	
}
</script>

<div class="pop5">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('구성원 수정')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	
	<div class="pl25 pr25">
		<div class="seach_arm2 pt10 pb5">
			<div class="leftbt"></div>
			<div class="rightbt">
				<button type="button" class="s_bt03" onclick="saveMember();">${e3ps:getMessage('저장')}</button>
				<button type="button" class="s_bt05" onclick="self.close();">${e3ps:getMessage('닫기')}</button>
			</div>
		</div>
		
		<div class="pro_table">
			<form name="editForm" id="editForm">
				<input type="hidden" id="oid" name="oid" value="${oid}">
				<table class="mainTable">
					<colgroup>
						<col style="width:30%">
						<col style="width:70%">
					</colgroup>	
					<tbody id="tableDataList">
						<tr>
							<th>${e3ps:getMessage('프로젝트 Role')}</th>
					    	<th>${e3ps:getMessage('이름')}</th>
						</tr>
						<%-- <c:forEach var="member" items="${members}" varStatus="status"> --%>
						<%-- </c:forEach> --%>
					</tbody>
				</table>
			</form>
		</div>
	</div>
</div>
