<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>

<script>
if (!webix.env.touch && webix.env.scrollSize)
	  webix.CustomScroll.init();
	  
webix.ready(function(){
	var grid = webix.ui({
		view:"datatable",
		container:"app_line_grid_wrap",
		id:"app_line_grid_wrap",
		columns:[
			{ id:"mark", header:"", template:"{common.checkbox()}", checkValue:"On", uncheckValue:"Off", width:40},
			{ id:"roleType", header:[{text:"${e3ps:getMessage('구분')}", content:"excelFilter", mode:"text"}] , width:200,sort:"string"},
			{ id:"name", header:"${e3ps:getMessage('이름')}",fillspace:true, sort:"string"},
			{ id:"departmentName", header:"${e3ps:getMessage('부서')}", sort:"string"},
			{ id:"duty", header:"${e3ps:getMessage('직위')}", sort:"string"},
			{ id:"id", header:"${e3ps:getMessage('아이디')}", sort:"string"},
			{ id:"oid", header:"${e3ps:getMessage('oid')}", sort:"string"},
			{ id:"pOid", header:"${e3ps:getMessage('poid')}", sort:"string"},
			{ id:"wtuserOID", header:"${e3ps:getMessage('wtuserOID')}", sort:"string"}
		],
		select:"row",
		scroll:"y",
		sort:"multi"
	});
		  
	webix.event(window, "resize", function(){ grid.adjust(); });
	//getGridData();
});
function addApprovalLinePopup() {

	var url = getURLString("/approval/addApprovalLinePopup") + "?oid=${appLineOid}";
	
	openPopup(url, "addApprovalLinePopup");
}
function app_line_getAppLineFromPopup(list) {
	console.log(list);
	if(list.length > 0){
		for(var i=0;i<list.length;i++){
			var data = {roleType: list[i].roleType, code: list[i].number, name: list[i].name, 
					creator: list[i].creator, createData: list[i].createDate, 
					stateName: list[i].stateName, oid: list[i].oid,
					pOid: list[i].pOid, wtuserOID: list[i].wtuserOID};
			$$("app_line_grid_wrap").add(data, i);
		}
	}
	
// 	AUIGrid.setGridData(app_line_myGridID, list);
			
}
</script>
<!-- button -->
<div class="seach_arm2 pt15 pb5">
	<div class="leftbt">
		<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('결재선 지정')}</span>
		<c:if test="${taskType eq 'CreateTRCR'}">
			<span class="required">*${e3ps:getMessage('결재선 지정 시 품질팀 담당자를 최종 결재자로 지정 바랍니다.')}</span>
		</c:if>
	</div>
	<div class="rightbt">
<%-- 		<button type="button" class="s_bt03" onclick="app_line_removeRow()">${e3ps:getMessage('삭제')}</button> --%>
<%-- 		<button type="button" class="s_bt03" onclick="app_line_addRow()">${e3ps:getMessage('추가')}</button> --%>
		<button type="button" class="s_bt03" onclick="addApprovalLinePopup()">${e3ps:getMessage('결재선 추가')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="app_line_grid_wrap" style="height:${gridHeight}px">
</div>