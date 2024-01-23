<%@page import="com.e3ps.project.beans.TaskEditHelper"%>
<%@page import="com.e3ps.project.service.TemplateHelper"%>
<%@page import="com.e3ps.common.util.CommonUtil"%>
<%@page import="com.e3ps.common.util.DateUtil"%>
<%@page import="com.e3ps.project.beans.ProjectTreeNode"%>
<%@page import="com.e3ps.project.PrePostLink"%>
<%@page import="wt.fc.PersistenceHelper"%>
<%@page import="wt.fc.QueryResult"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.e3ps.project.beans.ProjectTreeModel"%>
<%@page import="com.e3ps.project.ETaskNode"%>
<%@page import="com.e3ps.project.ScheduleNode"%>
<%@page import="wt.fc.ReferenceFactory"%>
<%@page import="com.e3ps.project.service.ProjectHelper"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Map"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<%

String oid = request.getParameter("oid");
String selectId = request.getParameter("selectId");
String selectChild = request.getParameter("selectChild");

ReferenceFactory rf = new ReferenceFactory();

ScheduleNode top = (ScheduleNode)rf.getReference(oid).getObject();

ETaskNode child = (ETaskNode)rf.getReference(selectChild).getObject();

ProjectTreeModel model = new ProjectTreeModel(top); 
ArrayList list = model.list;

QueryResult qr = PersistenceHelper.manager.navigate(child,"pre",PrePostLink.class);

ArrayList clist = new ArrayList();
while(qr.hasMoreElements()){
	ScheduleNode sgnode = (ScheduleNode)qr.nextElement();
	clist.add(sgnode.getPersistInfo().getObjectIdentifier().toString());
}
%>
<!-- pop -->
<div class="pop5">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('프로젝트 선행TASK 수정')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>

	<div class="pl25 pr25">
		<div class="seach_arm2 pt10 pb10">
			<div class="leftbt"></div>
			<div class="rightbt">
				<input type="button" class="s_bt03" value="수정"  onclick="rePreTask()"/>
       			<input type="button" class="s_bt05" value="닫기"  onclick="self.close()"/>
			</div>
		</div>
		
		<div class="pro_table">
			<form name="selectPreTaskList" method="post">
				<input type="hidden" name="command" />
				<input type="hidden" name="oid" value="<%=oid!=null?oid:""%>" />
				<input type="hidden" id="selectChild" name="selectChild"  value="<%=selectChild!=null?selectChild:""%>" />
				<table class="mainTable" width="100%" cellspacing="0" cellpadding="0" border="0">
					<tr>
						<th width="10%">
						<input type="checkbox"  onclick="allCheck(this.checked)"></input></th>
						<th width="10%">LEVEL</th>
						<th width="50%">이름</th>
						<th width="15%">계획 시작일</th>
						<th width="15%">계획 종료일</th>
					</tr>
						<%
						boolean enable = true;
						
						for(int i=0; i< list.size(); i++){ 
							String[] s = (String[])list.get(i);
							int level = Integer.parseInt(s[0]);
							
							boolean isSelf = true;
							
							if(s[2].equals(selectChild)){
								enable=false;
								isSelf=false;
							}
							
							ProjectTreeNode treeNode = (ProjectTreeNode)model.nodeMap.get(s[2]);
							
							ETaskNode cnode = (ETaskNode)rf.getReference(s[2]).getObject();
						double du = cnode.getManDay();
						
						boolean isPostLink = TaskEditHelper.service.isPrePostLink(child, cnode);
							%>
						<tr>
							<td align="center">
							<%if(treeNode.getChildCount()==0 && !isPostLink &&isSelf) { //enable &&%>
								<input type="checkbox" name="preTask" value="<%=s[2] %>" <%=clist.contains(s[2])?"checked":"" %>></input>
							<%}else{ %>
								&nbsp;
							<%} %>
							</td>
							<td align="center"><%=level+1 %></td>
							<td>
							<%for(int j=0; j< level; j++){ %>&nbsp;&nbsp;<%} %>
							<%=s[1] %><%if(CommonUtil.isAdmin()){ %>///<%=s[2] %><%} %>
							</td>
							<td align="center"><%=DateUtil.getDateString(cnode.getPlanStartDate(),"d") %></td>
							<td align="center"><%=DateUtil.getDateString(cnode.getPlanEndDate(),"d") %></td>
						</tr>
						<%} %>
				</table>
			</form>
		</div>
	</div>
</div>
<script src="/Windchill/jsp/js/xhRequest.js"></script>
<script type="text/javascript"> 
function allCheck(a) {
	var t = document.selectPreTaskList.preTask;

	if (t.length==null) {
		t.checked = a;
	} else {

		for(var i=0; i< t.length; i++){
			t[i].checked = a;
		}
	}
}
 
function checkList() {
	form = document.selectPreTaskList;

	var arr = new Array();
	var subarr = new Array();
	/* if(!isCheckedCheckBox()) {
		return arr;
	} */

	len = form.preTask.length;
	
	var idx = 0;
	if(len) {
		for(var i = 0; i < len; i++) {			
			if(form.preTask[i].checked == true) {
				subarr = new Array();
				//subarr = form.preTask[i].value.split("†");
				
				subarr[0] = form.preTask[i].value;
				//subarr[1] = form.preTask[i].sname;
				arr[idx++] = subarr;
			}
		}
	} else {
		if(form.preTask.checked == true) {
			subarr = new Array();
				subarr[0] = form.preTask.value;
				//subarr[1] = form.preTask.sname;
			arr[idx++] = subarr;
		}
	}
	return arr;
}

function isCheckedCheckBox() {
	form = document.selectPreTaskList;
	if(form.preTask == null) {
		return false;
	}

	len = form.preTask.length;
	if(len) {
		for(var i = 0; i < len;i++) {
			if(form.preTask[i].checked == true) {
				return true;
			}
		}
	}
	else {
		if(form.preTask.checked == true) {
			return true;
		}
	}

	return false;

}

function rePreTask(){
	
	var arr = checkList();
	
/* 	if(arr.length == 0) {
		self.close();
	} */
	
	var params = "oid=<%=oid%>&"+
	"selectId=<%=selectId%>&"+
	"preTask="+arr+"&"+
	"selectChild=<%=selectChild%>";
	new xhr.Request("/Windchill/jsp/project/EditPreTaskReq.jsp", params, rePreTaskReq, 'POST');		
	
}
 
function rePreTaskReq(req){
	if(req.readyState == 4){
		if(req.status == 200){
			var xmlDoc = req.responseXML;
 			
			var code = xmlDoc.getElementsByTagName('code').item(0).firstChild.nodeValue;
			var arr = new Array();	
			var chkarr = new Array();	
			
			if(code == 'posttask'){
				var name = xmlDoc.getElementsByTagName('name').item(0).firstChild.nodeValue;
				alert(name+" 태스크와 선행을 이을 수 없습니다. 이미 다른 태스크와 관련되어 있습니다.");
			}else if(code == 'pretask'){
 				var dataJSON = xmlDoc.getElementsByTagName("data").item(0).firstChild.nodeValue;
				var data = eval("(" + dataJSON + ")");
				
				for(var i=0; i<data.length; i++){
					arr[i]= data[i].oid;
				}
				
				var chkJSON = xmlDoc.getElementsByTagName("chklist").item(0).firstChild.nodeValue;
				var chkdata = eval("(" + chkJSON + ")");
				
				saveParentMove(arr, chkdata);
 			}else if(code == 'error'){
				
				var message = xmlDoc.getElementsByTagName('message').item(0).firstChild.nodeValue;
				alert("에러발생 : " + message + "다시 시도하십시오.");
				
			}else if(code == 'pretaskNull'){
				saveParentMove(arr, 0);
			}
			
		}else{
			alert("서버 에러 발생 : " + req.status  + "관리자에게 문의하십시오.");
		}
	}
}
function saveParentMove(arrRe, chkdata){

	var arr = checkList();
	var arrObj = new Array();
	
	arrObj[0] = arr;
	arrObj[1] = arrRe;
	arrObj[2] = chkdata;
	var rowId = document.selectPreTaskList.selectChild.value;
	opener.addPreTask(rowId, arrObj);
	
	self.close();
}
</script>
</form>
</body>
</html>
