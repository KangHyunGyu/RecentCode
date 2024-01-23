<%@page import="com.e3ps.project.key.ProjectKey.STATEKEY"%>
<%@page import="wt.fc.PersistenceHelper"%>
<%@page import="wt.fc.QueryResult"%>
<%@page import="com.e3ps.project.beans.ProjectUtil"%>
<%@page import="com.e3ps.project.beans.ProjectDao"%>
<%@page import="com.e3ps.project.beans.ProjectMemberHelper"%>
<%@page import="wt.session.SessionHelper"%>
<%@page import="wt.org.WTUser"%>
<%@page import="java.util.ArrayList"%>
<%@page import="wt.fc.ReferenceFactory"%>
<%@page import="com.e3ps.project.beans.ProjectNodeData"%>
<%@page import="com.e3ps.common.util.StringUtil"%>
<%@page import="com.e3ps.common.util.CommonUtil"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.e3ps.project.*"%>
<%@page import="java.util.Vector"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="com.e3ps.common.util.DateUtil"%>
<%@page import="wt.project.Role"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<%!
	private String getTaskStateFont(ScheduleNode node)throws Exception {
		
		ProjectNodeData data = new ProjectNodeData(node);
		/*
		66cdaa	=	초록	=	진행-정상
		32AAFF	=	파랑	=	완료
		696969	=	검정	=	대기
		F58282	=	빨강	=	진행-지연
		*/
		String reColor = "32AAFF";
		
		String ss = "";	
		if(node instanceof EProject){
			EProject pjt = (EProject)node;
			ss = pjt.getState().toString();
		}else if(node instanceof ETaskNode){
			ETaskNode task = (ETaskNode)node;
			ss = task.getStatus();
		}
		
		if(  STATEKEY.COMPLETED.equals(ss )) {
			reColor = "66cdaa";
		}else if( STATEKEY.READY.equals(ss )){
			reColor = "696969";
		}else if( STATEKEY.PROGRESS.equals(ss )){
			if(data.isDelay() != 1){
				reColor = "F58282";
			}else{
				reColor = "32AAFF";
			}
		}
		
		return reColor;
		
	}

public static double getDifferDateGap(ScheduleNode node){
	
	//this.taskDuration = this.schedule.getDuration();
	Timestamp taskPlanStartDate = node.getPlanStartDate();
	Timestamp taskPlanEndDate = node.getPlanEndDate();
	
	Calendar currentDate = Calendar.getInstance();
	
	if(currentDate.getTime().before(taskPlanStartDate)){
		return 0;
	}
	
	Calendar startDate = Calendar.getInstance();
    startDate.setTime(taskPlanStartDate);
    int compPer = (int)node.getCompletion();
    int taskDuration = DateUtil.getDuration(taskPlanStartDate, taskPlanEndDate) + 1;//this.schedule.getDuration();
    double d = ((double)compPer)/100D;
    d *= taskDuration * 0x5265c00L;
    
    //startDate.add(5, (int)d);
    double realLength = startDate.getTimeInMillis() + d;
    
    
    
    double continueTimeGap = realLength - (double)currentDate.getTime().getTime();
    double differDateGap = ((double)continueTimeGap) / ((double)0x5265c00L);
	return differDateGap;
}
%>
<%
String oid = request.getParameter("oid");

ReferenceFactory rf = new ReferenceFactory();

%>
<link rel="stylesheet" type="text/css" href="/Windchill/jsp/project/gantt/css/jsgantt.css"/>
<link rel="stylesheet" href="/Windchill/jsp/css/e3ps.css" type="text/css">
<script type="text/javascript" src="/Windchill/jsp/js/common.js"></script>
<script type="text/javascript" src="/Windchill/jsp/project/gantt/js/jsgantt.js"></script>
<%
ScheduleNode snode = (ScheduleNode)rf.getReference(oid).getObject();

EProjectNode root = null;

if(snode instanceof EProjectNode){
	root = (EProjectNode)snode;
}else{
	ETaskNode task = (ETaskNode)snode;
	root = task.getProject();
}
EProject project = (EProject)root;

ProjectNodeData rdata = new ProjectNodeData(root);

long rootKey = root.getPersistInfo().getObjectIdentifier().getId();
String rootId = root.getPersistInfo().getObjectIdentifier().toString();
ArrayList list = ProjectDao.manager.getStructure(rootKey);

WTUser user = (WTUser)SessionHelper.manager.getPrincipal();

boolean isLock = STATEKEY.STOP.equals(project.getState().toString()) || STATEKEY.MODIFY.equals(project.getState().toString());;
boolean isTaskEdit = (!rdata.isStartLifeCycleLock()) && ProjectMemberHelper.service.isPM(root,user) && root.isLastVersion() && (STATEKEY.READY.equals(project.getState().toString()) || STATEKEY.MODIFY.equals(project.getState().toString()));

OutputType otype = root.getOutputType();
String projectNo = root.getCode();
long id = CommonUtil.getOIDLongValue(root);
Timestamp pstartDate = root.getPlanStartDate();
Timestamp pendDate = root.getPlanEndDate();

String start = DateUtil.getTimeFormat(pstartDate, "MM/dd/yyyy");
String end = DateUtil.getTimeFormat(pendDate, "MM/dd/yyyy");
String name = root.getName();
String color = getTaskStateFont(snode);

String duration = ProjectUtil.numberFormat(rdata.getPlanDuration());
String completion = ProjectUtil.numberFormat(root.getCompletion());


%>


<title><%=root.getName()%></title></head>
<body onLoad="screenSize();" onResize="screenSize();">
<div style="position:relative" class="gantt" id="GanttChartDIV"></div>
<script type="text/javascript">


function scrollAll(){
	document.all.leftside.scrollTop = document.all.rightside.scrollTop;
	
}

function screenSize(){
	document.getElementById("leftside").style.height = document.body.clientHeight - 15;
	document.getElementById("rightside").style.height = document.body.clientHeight - 15;
	document.getElementById("rightside").style.width = document.body.clientWidth - 515;
}
  // here's all the html code neccessary to display the chart object

  // Future idea would be to allow XML file name to be passed in and chart tasks built from file.

    var g = new JSGantt.GanttChart('g', document.getElementById('GanttChartDIV'), 'week');

	g.setCustomer('<%=projectNo%>');

	g.setShowRes(1); // Show/Hide Responsible (0/1)
	g.setShowDur(1); // Show/Hide Duration (0/1)
	g.setShowComp(1); // Show/Hide % Complete(0/1)
	
	g.setShowStartDate(1);
	g.setShowEndDate(1);
	
    g.setCaptionType('Complete');  // Set to Show Caption (None,Caption,Resource,Duration,Complete)
	g.setFormatArr("day","week","month");
	
	//g.setDateDisplayFormat('yyyy-mm-dd'); 
	//var gr = new Graphics();

	if( g ) {
		//TaskItem(pID, pName, pStart, pEnd, pColor, pLink, pMile, pRes, pComp, pGroup, pParent, pOpen, pDepend)
		
		g.AddTaskItem(new JSGantt.TaskItem(<%=id%>, "<%=name%>", '<%=start%>', '<%=end%>', '<%=color%>', '', <%=0%>, '', <%=completion%>, <%=1%>, <%=0%>, <%=1%>));
		
		
		<%
		int selIdx = 0;
		for(int i=0; i< list.size(); i++){
			
			String[] s = (String[])list.get(i);
			String level = s[0];
			String pName = s[1];
			String sid = s[2];
			String pParent = s[3];
			String desc = s[4];
			
			ScheduleNode node = (ScheduleNode)rf.getReference(sid).getObject();
			ProjectNodeData data = new ProjectNodeData(node);
			
			
			Timestamp startDate = node.getPlanStartDate();
			Timestamp endDate = node.getPlanEndDate();
			
			String pID = String.valueOf(CommonUtil.getOIDLongValue(node));
			
			
			String pStart = DateUtil.getTimeFormat(startDate, "MM/dd/yyyy");
			String pEnd = DateUtil.getTimeFormat(endDate, "MM/dd/yyyy");
			String pColor = getTaskStateFont(node);
			String pLink = "";
			String pMile = "0";
			String pRes = "";
			
			String perSonRole = data.getRoleName();
			
			Role role = null;
			if(perSonRole != null && !"PM".equals(perSonRole)){
				role = Role.toRole(perSonRole);
			}
			if(role != null){
				pRes = role.getDisplay(Locale.KOREA);
			}else{
				pRes = perSonRole;
			}
			
			if(pRes == null){
				pRes = "";
			}
			
			String pDu = ProjectUtil.numberFormat(data.getPlanDuration());
			String pComp = ProjectUtil.numberFormat(node.getCompletion());
			String pGroup = "0";
			
			QueryResult qr = PersistenceHelper.manager.navigate(node, "child", ParentChildLink.class);
			
			if(qr.size() > 0){
				pGroup = "1";
			}
			
			String pOpen = "1";
			
			%>
			g.AddTaskItem(new JSGantt.TaskItem(<%=pID%>, "<%=pName%>", '<%=pStart%>', '<%=pEnd%>', '<%=pColor%>', '<%=pLink%>', <%=pMile%>, "<%=pRes%>", <%=pComp%>, <%=pGroup%>, <%=pParent%>, <%=pOpen%>));
		 <%
		}
		 %>
    g.Draw();	
    g.DrawDependencies();

  }

</script>
