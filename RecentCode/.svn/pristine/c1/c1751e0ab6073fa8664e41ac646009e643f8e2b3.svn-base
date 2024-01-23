<%@page import="com.e3ps.project.OutputType"%>
<%@page import="com.e3ps.project.beans.ProjectDao"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.e3ps.project.ETaskNode"%>
<%@page import="com.e3ps.project.EProjectNode"%>
<%@page import="com.e3ps.project.ScheduleNode"%>
<%@page import="wt.fc.ReferenceFactory"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%

String oid = request.getParameter("oid");

ReferenceFactory rf = new ReferenceFactory();
ScheduleNode node = (ScheduleNode)rf.getReference(oid).getObject();

EProjectNode root = null;

if(node instanceof EProjectNode){
	root = (EProjectNode)node;
}else{
	ETaskNode task = (ETaskNode)node;
	root = task.getProject();
}

long rootKey = root.getPersistInfo().getObjectIdentifier().getId();
String rootId = root.getPersistInfo().getObjectIdentifier().toString();

boolean editable = root.isLastVersion();
%>

<html>

<link rel="stylesheet" type="text/css" href="/Windchill/jsp/css/e3ps.css"/>
<link rel="stylesheet" href="/Windchill/jsp/css/css.css" type="text/css">
<link rel="stylesheet" type="text/css" href="/Windchill/jsp/js/dhtmlx/css/dhtmlx.css">
<link rel="stylesheet" type="text/css" href="/Windchill/jsp/js/dhtmlx/css/dhtmlxgrid.css">
<link rel="stylesheet" type="text/css" href="/Windchill/jsp/js/dhtmlx/css/dhtmlxgrid_dhx_skyblue.css">
<link rel="stylesheet" type="text/css" href="/Windchill/jsp/js/dhtmlx/css/dhtmlxcalendar.css">
<link rel="stylesheet" type="text/css" href="/Windchill/jsp/js/dhtmlx/css/dhtmlxmenu_dhx_skyblue.css">
<link rel="stylesheet" type="text/css" href="/Windchill/jsp/js/dhtmlx/css/dhtmlxcalendar_dhx_skyblue.css">

<script src="/Windchill/jsp/js/dhtmlx/js/dhtmlxcommon.js"></script>
<script src="/Windchill/jsp/js/dhtmlx/js/dhtmlxgrid.js"></script>
<script src="/Windchill/jsp/js/dhtmlx/js/dhtmlxgridcell.js"></script>
<script src="/Windchill/jsp/js/dhtmlx/js/dhtmlxtreegrid.js"></script>
<script src="/Windchill/jsp/js/dhtmlx/js/dhtmlxtreegrid_lines.js"></script>
<script src="/Windchill/jsp/js/dhtmlx/js/dhtmlx.js"></script>
<!-- <script src="/Windchill/jsp/dhtmlx/js/dhtmlxcalendar.js"></script> 
<script src="/Windchill/jsp/dhtmlx/js/dhtmlxgrid_excell_dhxcalendar.js"></script> -->
<script src="/Windchill/jsp/js/dhtmlx/js/dhtmlxgrid_deprecated.js"></script>
<script src="/Windchill/jsp/js/dhtmlx/js/dhtmlxmenu.js"></script>  
<style>
	div.gridbox div.ftr td {
		text-align:right;
		background-color:#E7F0FF;
	}
	
</style>
<body>
<form name="tempForm">
<input type="hidden" name="oid" value="<%=oid %>">
<input type="hidden" name="poid" value="">
<input type="hidden" name="soid" value="">
<input type="hidden" id="treeCmd" name="treeCmd" value="">
<br>
<table width="100%" class="pdl10" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<img src="/Windchill/jsp/project/images/title_bullet.gif">
			<span class="ctitle">
				태스크 일정 편집
			</span>
			<input type="button" class="button01" value="위로" onclick="javascript:menuOnClick('up','','')">
			<input type="button" class="button01" value="아래로" onclick="javascript:menuOnClick('down','','')">
			<input type="button" class="button01" value="새로고침" onclick="loadGrid()">
			<!-- <input type="button" class="button01" value="닫기" onclick="opener.parent.location.reload(); self.close();"> -->
			<input type="button" class="button01" value="닫기" onclick="popupClose();">
		</td>
		<!--버튼 영역 시작-->
		<td align="right">
			
		</td>
		<!--버튼 영역 끝-->
	</tr>
	<tr>
		<td bgcolor="#e3e3e3" height="1" colspan="2"></td>
	</tr>
</table>
<table width="100%" height="85%" border="0" cellpadding="1" cellspacing="3">
	<tr valign="top">
		<td valign="top">
			<div id="tempTree" style="width:95%; height:100%; background-color:#f5f5f5; border:1px solid Silver;"></div>
		</td>
	</tr>
</table>
<table>
<tr>
	<td><font color="red">※ 자식 태스크 추가시 선택된 태스크의 산출물 정의한 내용이 있을 경우 추가되지 않습니다.</font>
	</td>
</tr>
</table>
<script type="text/javascript" src="/Windchill/jsp/js/jquery/jquery-1.11.2.min.js"></script>
<script src="/Windchill/jsp/js/xhRequest.js"></script>
<script type="text/javascript" charset="utf-8">

window.onload = function(){
	$.ajax({
		type : "POST",
		url : "/Windchill/worldex/project/rightClickMenu",
		dataType : "json",
		data : {
			oid : 'oid'
		},
		success : function (data, textStatus, jqXHR) {
			loadTree(data.xmlData);
		},
		error : function (jqXHR, textStatus, errorThrown ) {
			alert(errorThrown);
		}
	});
};



var form = document.tempForm;

//트리 그리드 load
function loadTree(rightClickXml){
	//menu
	menu = new dhtmlXMenuObject();
	menu.setIconsPath("/Windchill/jsp/js/dhtmlx/imgs/");
	menu.renderAsContextMenu();
	onLoadFunction = function(){
        // will be called if specified
    }
	menu.loadXMLString(rightClickXml, onLoadFunction);
	menu.attachEvent("onClick", menuOnClick);
	
	//grid
 	grid = new dhtmlXGridObject('tempTree');
	grid.selMultiRows = true;
	grid.imgURL = "/Windchill/jsp/js/dhtmlx/imgs/icons_greenfolders/";
	grid.setHeader("No,이름(Task),기간[공수],시작일,종료일,선행");
	grid.setInitWidths("50,250,100,150,150,*");
	//grid.setColAlign("center,left,center,center,center,center");
	//grid.setColTypes("cntr,tree,ed,dhxCalendar,dhxCalendar,ed");
	grid.setDateFormat("start", "%Y/%m/%d");
	grid.setDateFormat("end", "%Y/%m/%d"); 

	grid.enableStableSorting(true);
	grid.enableContextMenu(menu);
	grid.enableMultiselect(false);
	grid.enableEditEvents(false, true, true);
	grid.enableRowsHover(true,'grid_hover');
	grid.enableTreeGridLines();
	
	grid.attachEvent("onEditCell", doOnCellEdit);
	grid.attachEvent("onBeforeContextMenu", onShowMenu);
	grid.setOnLoadingEnd(loadEnd);
	
	grid.init();
	grid.setSkin("dhx_skyblue");
	//grid.loadXML("/Windchill/e3psFileTemp/xml2592991866069628696.xml");
	//D:\ptc\Windchill\Windchill\codebase\e3psFileTemp
	loadGrid();
}

function popupClose(){
	window.opener.location.href = window.opener.location.href;
	 self.close();
	//window.opener.location.href = window.opener.document.URL;
}
function onShowMenu(rowId, celInd, grid) {
	gridSelectRow(rowId);
	return true;
}
function loadGrid(){
	grid.clearAll();
	$.ajax({
		type : "POST",
		url : "/Windchill/worldex/project/loadEditTaskGridXml",
		dataType : "json",
		data : {
			oid : '<%=oid%>'
		},
		success : function (data, textStatus, jqXHR) {
			//alert(data.xmlData);
			grid.load(data.xmlData);
		},
		error : function (jqXHR, textStatus, errorThrown ) {
			alert(errorThrown);
		}
	});
}
function loadEnd(){
	//전체 펼치기
	//grid.expandAll();
	//grid.expandAllGroups();
	//첫번째 로우 선택
	//grid.selectRow(0);
	//첫번째 row Lock
	grid.lockRow(grid.getRowId(0),true);
}
function gridSelectRow(oid){
	var a = grid.getRowIndex(oid);
	grid.selectRow(a);
}

function menuOnClick(id, zoneId, casState){
	
	var soid = grid.getSelectedId();
	var level = grid.getLevel(soid);
	
	if(level == 0){
		if(id != "new"){
			alert("1 Level 이하의 Task를 선택하세요.");
			return;
		}
	}	
	
	if(id != "new"){
		if(soid != null){
			
			var rowImg = grid.getItemImage(soid);
	    	
			if(id == 'delete'){
				var childIndex = grid.hasChildren(soid);
				if(childIndex){
					alert("하위 Task가 있는 경우 삭제할 수 없습니다.");	
					return;
				}
				if(rowImg == "task_complete.gif"){
		    		alert("완료된 Task의 삭제는 불가능 합니다.");
		    		return false;
		    	}
				
			}else if(id == 'child'){
				if(level > 5){
					alert("6 LEVEL 까지 생성 가능합니다.");
					return;
				}
				
				if(rowImg == "task_complete.gif"){
		    		alert("완료된 Task의 자식추가는 불가능 합니다.");
		    		return false;
		    	}
				
				var text5 = grid.cells(soid,5).getValue();
				if(text5 != "" || text5.length > 0){
					if(!confirm("해당 Task의 선행이 삭제됩니다. 생성하시겠습니까?")){
						return;
					}
				}
			}
			
		}else{
			alert("Task를 선택하세요.");return;	
		}
	}
	
	var params = "cmd="+id+"&"+
				"soid="+soid+"&"+
				"pjtOid="+"<%=oid%>&"+
				"code="+(new Date()).valueOf();
	new xhr.Request("/Windchill/jsp/project/EditTaskActionReq.jsp", params, nodeEvent, 'POST');		
	
}
function nodeEvent(req){
	if(req.readyState == 4){
		if(req.status == 200){
			var xmlDoc = req.responseXML;
			
 			var code = xmlDoc.getElementsByTagName('code').item(0).firstChild.nodeValue;
 			var oid, soid, name, stdate, sort, eddate, duration;
 			
 			if(code != "deleted" && code != "up" && code != "down" && code != "fail"){
	 			oid = xmlDoc.getElementsByTagName('oid').item(0).firstChild.nodeValue;
	 			name = xmlDoc.getElementsByTagName('name').item(0).firstChild.nodeValue;
	 			stdate = xmlDoc.getElementsByTagName('stdate').item(0).firstChild.nodeValue;
	 			eddate = xmlDoc.getElementsByTagName('eddate').item(0).firstChild.nodeValue;
	 			sort = xmlDoc.getElementsByTagName('sort').item(0).firstChild.nodeValue;
	 			duration = xmlDoc.getElementsByTagName('duration').item(0).firstChild.nodeValue;
 			}
 			
 			soid = xmlDoc.getElementsByTagName('soid').item(0).firstChild.nodeValue;
 			
			if(code == 'new' || code == 'child'){
				grid.addRow(oid,[sort,name,duration,stdate,eddate],0,soid);
				
				if(code == 'child'){
					grid.cells(soid,5).setValue('');
					grid.openItem(soid);
				}
				
			}else if(code == 'deleted'){
				//grid.deleteSelectedItem();
				grid.deleteRow(soid);
				oid = grid.getParentId(soid);
				
				var dataJSON = xmlDoc.getElementsByTagName("data").item(0).firstChild.nodeValue;
					var data = eval("(" + dataJSON + ")");
					
				for(var i=0; i<data.length; i++){
					
					grid.cells(data[i].oid,2).setValue(data[i].duration);
					grid.cells(data[i].oid,3).setValue(data[i].start);
					grid.cells(data[i].oid,4).setValue(data[i].end);
					
				}
				
			}else if(code == 'error'){
				var message = xmlDoc.getElementsByTagName('message').item(0).firstChild.nodeValue;
				alert("에러발생 : " + message + " 다시 시도하십시오.");
				
			}else if(code == 'next'){
				grid.addRowAfter(oid,[sort,name,duration,stdate,eddate],soid);
				
			}else if(code == 'prev'){
				grid.addRowBefore(oid,[sort,name,duration,stdate,eddate],soid);
				
			}else if(code == 'up'){
				grid.moveRowUp(soid);
				
			}else if(code == 'down'){
				grid.moveRowDown(soid);
			}else if(code == 'fail'){
				var message = xmlDoc.getElementsByTagName('message').item(0).firstChild.nodeValue;
				alert(message);
			}
			
			if(code != 'error' && code != "up" && code != "down"){
				gridSelectRow(oid);
			}
			
		}else{
			alert("서버 에러 발생 : " + req.status  + " 관리자에게 문의하십시오.");
		}
	}
}

function doSelectRow(oid){
	grid.selectRow(grid.getRowIndex(oid),false,false,true);
}

function delRow(){
	grid.deleteSelectedItem();
}

var oldVal, newVal;
//GRID
function doOnCellEdit(stage, rowId, cellInd) {
	
    if (stage == 0) {
    	
    	var childIndex = grid.hasChildren(rowId);
    	if(cellInd == "2" || cellInd == "3" || cellInd == "4" || cellInd == "5"){
			if(childIndex){
				alert("End Item을 선택하세요.");	
				return false;
			}
    	}
    	
    	var rowImg = grid.getItemImage(rowId);
    	if(rowImg == "task_complete.gif"){
    		alert("완료된 Task의 수정은 불가능 합니다.");
    		return false;
    	}
    	
    	
    	oldVal = grid.cells(rowId, cellInd).getValue();
    	
       if(cellInd == "5"){

    	   var poid = grid.getParentId(rowId);
    	   <%-- var url = "/Windchill/jsp/project/SelectPreTask.jsp?oid=<%=oid%>&selectId="+poid+"&selectChild="+rowId+"&formName=tempForm"; --%>
    	   var url = "/Windchill/worldex/project/editPreTask?oid=<%=oid%>&selectId="+poid+"&selectChild="+rowId;
    	   var sFeatures = "toolbar=0,location=0,menubar=0,status=0"
			+ ",scrollbars=1,resizable=1"
			+ ",width=800px,height=500px";
    	   
    	   window.open(url,"editPreTask",sFeatures);
    	   
    	   return false;
    	   
       }
       
    } else if (stage == 1) {
		
    } else if (stage == 2) {
    	
    	newVal = grid.cells(rowId, cellInd).getValue();
    	
    	if(oldVal == newVal){
	    	return;
	    	
    	}else if(cellInd == "4"){
    		
    		var a = grid.cells(rowId, "3").getValue();
    		var b = grid.cells(rowId, "4").getValue();
    		
    		if(!checkDate(a, b)){
    			alert("계획 종료일은  계획 시작일보다 빠를 수 없습니다.");
    			return;	
    		}
    	}else if(cellInd == "2"){
    		
    		var cell2val = grid.cells(rowId, "2").getValue();
    		
    		if(isNaN(cell2val)){
    			alert("정수로 입력해 주십시오.");
    			return;
    		}else if(cell2val == 0){
    			alert("0보다 큰 수를 입력하여 주십시오.");
    			return;
    		}else if(cell2val > 1000){
    			alert("1000 보다 작은 수를 입력하여 주십시오.");
    			return;
    		}
    	}else if(cellInd == "1"){
    		var desc = grid.cells(rowId, "1").getValue();
    		//글자수 체크
    		var ochar = "";
    		var obite = 0;
    		for(var i=0; i<desc.length; i++){
    			ochar = desc.charAt(i);
    			if(escape(ochar).length>4){
    				obite +=2;
    			}else{
    				obite++;
    			}
    		}

    		if(obite > 40){
    			alert('입력 글자수를 초과하였습니다.');
    			return;
    		}
    		
    		//특수문자 체크
    		var chktext = /[\{\}\?.,;:|*~`!^\+┼<>@\#$%&\'\"\\\=]/gi;
    		
    		if (chktext.test(desc)) {
    		   alert("특수문자는 입력하실 수 없습니다.");
    		   return;
    		}
    	}
    	
		var params = "cmd=editCell&"+
		"soid="+rowId+"&"+
		"pjtOid="+"<%=oid%>&"+
		"newVal="+newVal+"&"+
		"cellId="+cellInd;
		new xhr.Request("/Windchill/jsp/project/EditTaskActionReq.jsp", params, cellEvent, 'POST');		
		//grid.cells(rowId,cellInd).setValue(newVal);
    }
}

function cellEvent(req){
	if(req.readyState == 4){
		if(req.status == 200){
			var xmlDoc = req.responseXML;
 			
			var code = xmlDoc.getElementsByTagName('code').item(0).firstChild.nodeValue;
 			var soid = xmlDoc.getElementsByTagName('soid').item(0).firstChild.nodeValue;
 			var newVal = xmlDoc.getElementsByTagName('newVal').item(0).firstChild.nodeValue;
 			
 			if(code == 'editCell'){
 				
 				var cellId = xmlDoc.getElementsByTagName('cellId').item(0).firstChild.nodeValue;

 				grid.cells(soid,cellId).setValue(newVal);
 				
 				if("2" == cellId || "3" == cellId || "4" == cellId){
 					
 					var dataJSON = xmlDoc.getElementsByTagName("data").item(0).firstChild.nodeValue;
 					var data = eval("(" + dataJSON + ")");
					for(var i=0; i<data.length; i++){
						grid.cells(data[i].oid,2).setValue(data[i].duration);
						grid.cells(data[i].oid,3).setValue(data[i].start);
						grid.cells(data[i].oid,4).setValue(data[i].end);
						
					}
 				}
 				if("1" == cellId){
 					var dataJSON = xmlDoc.getElementsByTagName("data").item(0).firstChild.nodeValue;
 					var data = eval("(" + dataJSON + ")");
					for(var i=0; i<data.length; i++){
						grid.cells(data[i].oid,5).setValue(data[i].text);
					}
 				}
			}else if(code == 'getPlan'){
 			
				grid.cells(soid,5).setValue(newVal);
				
				var dataJSON = xmlDoc.getElementsByTagName("data").item(0).firstChild.nodeValue;
			    var data = eval("(" + dataJSON + ")");
					
				for(var i=0; i<data.length; i++){
					
					grid.cells(data[i].oid,2).setValue(data[i].duration);
					grid.cells(data[i].oid,3).setValue(data[i].start);
					grid.cells(data[i].oid,4).setValue(data[i].end);
					
				}
				
			}else if(code == 'error'){
				var message = xmlDoc.getElementsByTagName('message').item(0).firstChild.nodeValue;
				
				//doOnUndo();
				
				alert("에러발생 : " + message + "다시 시도하십시오.");
			}

 			if(code != 'error'){
 				
				gridSelectRow(soid);
			}
			
		}else{
			alert("서버 에러 발생 : " + req.status  + "관리자에게 문의하십시오.");
		}
	}
}

function checkDate(a,b){
	
	var start = a.split("/");
	var end = b.split("/");
	var strDate = start[0]+ start[1]-1+ start[2];
	var endDate = end[0]+ end[1]-1+ end[2];
	
	if(endDate==null || endDate.length==0)return true;
	
	if(strDate > endDate){
		return false;
	}
	
	return true;
}

function addPreTask(rowId, attache){

	var att0 = attache[0];
	var att1 = attache[1];
	var att2 = attache[2];
	
	if(att0.length == 0) {
		grid.cells(rowId,5).setValue('');
		return;
	}

	var preText="";
	var len = attache.length;
	var arrObj = new Array();
	var idx = 0;
	
	var idx1 = 0;
	var arrObj1 = new Array();
	for(var j=0; j<att1.length; j++){
		arrObj1[idx1++] = att1[j]; 
	}
	
	//arrObj[idx++] = rowId;
	
	/* for(var i = 0; i < att0.length; i++) {
		subarr = att0[i];
		arrObj[idx++] = subarr[0]; 
		
		//if(typeof subarr[1] == "undefined" || subarr[1] == null) {
			
		//}else{
			preText = preText + subarr[1];
		//}
		if(i != len-1){preText=preText+", ";}
		
	} */
	var chkleng = att2.length;
	for(var i=0; i<att2.length; i++){
		
		subarr = att2[i]; 
		arrObj[idx++] = subarr.oid; 
		preText = preText + subarr.name;
		if(i != chkleng-1){
			preText=preText+", ";
		}
	}
	
	grid.cells(rowId,5).setValue(preText);
	//alert(arrObj);
	//arrObj = arrObj +"," +att1;
	
	if(arrObj.length>0){
		setPlanDate(arrObj, rowId, arrObj1);
	}
}

function setPlanDate(arrObj, soid, att1){
	var params = "cmd=getPlan&"+
	"soid="+soid+"&"+
	"att1="+att1+"&"+
	"arrObj="+arrObj;
	new xhr.Request("/Windchill/jsp/project/EditTaskActionReq.jsp", params, cellEvent, 'POST');		
}


/* 
var colInd = grid.getColIndexById(id); // get column index through its id
var colId = grid.getColumnId(ind);
grid.setColumnId(ind, id);
grid.setColumnIds(ids);
grid.cells(i,j).getValue();
grid.cells(i,j).setValue(some);
grid.getSelectedCellIndex();  
grid.selectRowById()
grid.enableKeyboardSupport(true|false); 
grid.enableLightMouseNavigation(true|false);
//dhtmlxgrid_keymap_access.js - for MS Access like keymap.
grid.attachEvent("onCellChanged", function(rId,cInd,nValue){});
grid.attachEvent("onSelectStateChanged", function(id){});
grid.attachEvent("onRightClick", function(id,ind,obj){});

grid.setItemText(grid.getSelectedId(),'new text');
grid.setRowId(grid.getRowIndex(soid),'newid');
grid.getParentId(soid);
grid.getLevel(soid);
grid.getRowIndex(soid);
grid.getAllItemIds();

//grid.attachEvent("onRowDblClicked", doOnRowDblClicked);
//grid.attachEvent("onRightClick", doOnRightClick);
//grid.attachEvent("onCellChanged", function(rId,cInd,nValue){});
//grid.setOnRightClickHandler();
//grid.attachEvent("onEnter", doOnEnter);
//grid.attachEvent("onRowSelect", doOnRowSelect);
mygrid.clearAll(); mygrid.loadXML('../common/first.xml');
mygrid.loadXML('../common/second.xml');
mygrid.updateFromXML('../common/third.xml');
mygrid.enableAlterCss("even", "uneven");
mygrid.clearAll(true);
mygrid.loadXML("../common/gridH3.xml");
mygrid.clearAll();mygrid.parse(document.getElementById('str_xml').value); 

function doOnEnter(rowId, cellInd) {
    alert("User pressed Enter on row with id " + rowId + " and cell index " + cellInd);
}
function doOnRowSelect(id) {
    alert("Selected row: " + id);
}
function doOnUndo() {
	grid.doUndo();
}
 */
</script>
<!-- drag & drop, dept node delete is used -->
<iframe name="action" frameborder="0" scrolling="no" width="0" height="0"></iframe>
</form>
</body>
</html>