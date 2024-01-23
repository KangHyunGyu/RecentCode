<%@page import="com.e3ps.project.key.ProjectKey.STATEKEY"%>
<%@page import="com.e3ps.project.EProject"%>
<%@page import="com.e3ps.project.ETaskNode"%>
<%@page import="com.e3ps.project.ETask"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.e3ps.common.util.DateUtil"%>
<%@page import="com.e3ps.project.ScheduleNode"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.e3ps.common.util.CommonUtil"%>
<%@page import="com.e3ps.project.beans.ProjectTreeModel"%>
<%@page import="com.e3ps.project.EProjectNode"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
				
<% /***  The WTContextBean is a JavaBean for use in Java Server pages or Servlets that wish to use Windchill Java client or server APIs ***/ %>
<jsp:useBean id="wtcontext" class="wt.httpgw.WTContextBean" scope="request" />
<jsp:setProperty name="wtcontext" property="request" value="<%= request %>" />

<html>
<head>
<base target="_self">
<link rel="stylesheet" href="/Windchill/jsp/css/e3ps.css">
<title>Project Chart Page</title>
<script type="text/javascript" src="/Windchill/jsp/js/common.js"></script>

<script type="text/javascript">
<!-- 
function clearDate(str) {
	var tartxt = document.getElementById(str);
	tartxt.value = "";
}
function searchPjt(){
	document.forms[0].action = "/Windchill/extcore/kores/project/chart/ProjectProjectChart.jsp";
	document.forms[0].submit();
}
function viewProject(oid){
	openWindow('/Windchill/extcore/kores/project/ProjectViewFrm.jsp?oid='+oid+"&popup=popup", '',1150,800);
}
function excelReport(){
	document.forms[0].action = "/Windchill/extcore/kores/project/chart/ProjectProjectReport.jsp";
	document.forms[0].submit();
}
function printReport(){
	window.print();
}
// -->
</script>
<% String oid = request.getParameter("oid");%>
</head>
<body>
<form action="/Windchill/jsp/project/ProjectChart.jsp">
<!-- hidden begin -->
<input type="hidden" name="oid" value="<%=oid==null?"":oid%>" />
	
	<table border="0" cellpadding="0" bgcolor="black" cellspacing="1"  style="table-layout:fixed">
	
	<tr height="20">
	<td width="300" rowspan="2" bgcolor="151b54" align="center" ><font color="white">개발 세부 Items</font></td>

	<%
	
	//데이타 로드
	EProjectNode project = (EProjectNode)CommonUtil.getObject(oid);
	ProjectTreeModel model = new ProjectTreeModel(project);
	ProjectChartData projectData = new ProjectChartData(model.rootNode);
	
	ArrayList list = new ArrayList();
	list.add(projectData);
	
	HashMap levelCount = new HashMap();
	for(int i=0; i< model.size(); i++){
		ScheduleNode sn = model.get(i);
		int level = model.getLevel(i);
		
		Integer count = (Integer)levelCount.get(new Integer(level-1));
		if(count==null){
			count = new Integer(1);
		}
		levelCount.put(new Integer(level-1),new Integer(count.intValue()+1));
		levelCount.put(new Integer(level),new Integer(1));
		
		list.add(new ProjectChartData(sn,level,count.intValue()));
	}
	
	//전체 시작일, 종료일 크기 정의
	Calendar ca1 = Calendar.getInstance();
	ca1.setTime(model.rootNode.getPlanStartDate());
	ca1.set(Calendar.DATE,1);
	String startdate  = DateUtil.getDateString(ca1.getTime(),"d");
	ca1.setTime(model.rootNode.getPlanEndDate());
	ca1.add(Calendar.MONTH,1);
	ca1.set(Calendar.DATE,1);
	ca1.add(Calendar.DATE,-1);
	String enddate = DateUtil.getDateString(ca1.getTime(),"d");
	
	Calendar currentDate = Calendar.getInstance();
	int today = currentDate.get(Calendar.DATE);
	
	float did = 1f;

	Calendar ca = Calendar.getInstance();
	ca.setTime(DateUtil.parseDateStr(startdate));

	Calendar endCa = Calendar.getInstance();
	endCa.setTime(DateUtil.parseDateStr(enddate));
	endCa.add(Calendar.MONTH,1);
	endCa.set(Calendar.DATE,1);

	SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
	int syear = ca.get(Calendar.YEAR);
	int smonth = ca.get(Calendar.MONTH);
	int colspan = 1;
	int width = 0;
	
	int totalWidth = 0;
	
	// 컬럼 표현
	while(ca.getTimeInMillis() <= endCa.getTimeInMillis()){
		if(syear!= ca.get(Calendar.YEAR)){
			syear = ca.get(Calendar.YEAR);
			ca.add(Calendar.MONTH,-1);
			%>
			<td  bgcolor=151b54 width="<%=width * did %>"  colspan='<%=colspan %>' align=center>
			<font color=white>
			<%=yearFormat.format(new Date(ca.getTimeInMillis())) %></font></td>
			<%
			ca.add(Calendar.MONTH,1);
			colspan = 0;
			width=0;
		}
		if(smonth!= ca.get(Calendar.MONTH)){
			smonth = ca.get(Calendar.MONTH);
			colspan++;
			width--;
		}
		width++;
		totalWidth++;
		ca.add(Calendar.DATE,1);
	}
	if(colspan>1){%>
		<td bgcolor="151b54"  width="<%=width*did %>"  colspan='<%=colspan-1 %>' align="center">
		<font color="white">
		<%=yearFormat.format(new Date(ca.getTimeInMillis())) %></font></td>
		<%
	}
	ca.setTime(DateUtil.parseDateStr(startdate));
	%>
	</tr>
	<tr height="20">
	<%
	SimpleDateFormat tempFormat = new SimpleDateFormat("yyyyMM");
	SimpleDateFormat monthFormat = new SimpleDateFormat("M");
	smonth = ca.get(Calendar.MONTH);
	width = 0;
	while(ca.getTimeInMillis() <= endCa.getTimeInMillis()){
		if(smonth!= ca.get(Calendar.MONTH)){
			smonth = ca.get(Calendar.MONTH);
			ca.add(Calendar.MONTH,-1);
			%>
			<td bgcolor="151b54"  align="center"  width='<%= (width*did-1) %>'>
			<font color="white">
			<%=monthFormat.format(new Date(ca.getTimeInMillis())) %></font></td>
			<%
			ca.add(Calendar.MONTH,1);
			width = 0;
		}
		width++;
		ca.add(Calendar.DATE,1);
	}
	ca.setTime(DateUtil.parseDateStr(startdate));
	%>
	</tr>



	<%
	String toMonth = tempFormat.format(new Date());
	
	for(int i=0; i< list.size(); i++){
        ProjectChartData data = (ProjectChartData)list.get(i);
        
		boolean isProject = false;
		Object o = data.obj;
		if(o instanceof EProjectNode){
			isProject = true;
		}
		
		String bgColor = "white";
		if(isProject)bgColor = "e6e6fa";

	%>
	<tr height="30">
	<td width="300"   bgcolor="<%=bgColor %>" align="left"  title="<%=data.name%>">
	<div style="width:300;border:0;padding:0;margin:0;text-overflow:ellipsis;overflow:hidden;">
		<nobr>
		<% if(!isProject){ 
			for(int j=0; j< data.level; j++){%>&nbsp;&nbsp;&nbsp;<%}
		%>&nbsp;<%=data.count%>)&nbsp;<%}%>
		
		<%=isProject?"<b><font color=darkred>":getTaskStateFont(data, currentDate) %><%=data.name %></font>
		
		</nobr>
	</div>
	</td>
	
	
	<%
	smonth = ca.get(Calendar.MONTH);
	boolean flag = true;
	
	while(ca.getTimeInMillis() <= endCa.getTimeInMillis()){
		
		String bgcolor = bgColor;
				
		if(smonth!= ca.get(Calendar.MONTH)){
			String sd = DateUtil.getDateString(ca.getTime(),"d");
			smonth = ca.get(Calendar.MONTH);
			
			ca.add(Calendar.MONTH,-1);
			boolean isToMonth = false;
			if(toMonth.equals(tempFormat.format(ca.getTime()))){
				bgcolor = "afdcec";
				isToMonth = true;
			}
			ca.add(Calendar.MONTH,1);
			%>
			<td bgcolor="<%=bgcolor %>"  align="left" valign="top" title="<%=DateUtil.getDateString(data.obj.getPlanStartDate(),"d") %> ~ <%=DateUtil.getDateString(data.obj.getPlanEndDate(),"d")%>">
			<%if(i==0 && isToMonth){%>
				<div id="treeproduct" style="Z-INDEX:1;position:absolute;width:<%=today+3 %>; height:<%=31*list.size()%>;border :1px; overflow:none;">
					<table height="<%=31*list.size() %>" width="<%=today+3 %>" border="0" cellspacing="0" cellpadding="0">
					<tr>
					<td width="<%=today %>">
					</td>
					<td  bgcolor="red" width="3" title="<%=DateUtil.getDateString(currentDate.getTime(),"d") %>">
					</td></tr>
					</table>
				</div>
			<%} %>
			
			<%
			if(flag){
				flag = false;
				
				int gap = 0;
				double du = data.pjtDuration;
				
				int xx = getDuration(DateUtil.parseDateStr(startdate),new Date(data.obj.getPlanStartDate().getTime()));
				boolean early = false;
				if(xx<0){
					gap = 0;
					du = du + xx;
					early = true;
				}else{
					gap = xx;
				}
				
				xx = getDuration(DateUtil.parseDateStr(enddate),new Date(data.obj.getPlanEndDate().getTime()));
				boolean over = false;
				if(xx>0){
					over = true;
					du = du - xx;
				}
				
				double perLine = getDuration(DateUtil.parseDateStr(startdate),currentDate.getTime()) - gap - data.df;
				
				if(perLine>du){
					perLine = du;
				}
				
				if(isProject){%>
				<%
		int barTail = 10;
		int barHead = 5;
		if(over)barTail = 0; 
		if(early)barHead = 0;
		%>
	<div id="treeproduct" style="position:absolute;width:<%=totalWidth %>; height:30;border :1px; overflow:none;">
		<table height="30" border="0" cellspacing="0" cellpadding="0">
		<tr height="5"><td></td></tr>
		<tr height="20"><td align="left">
		
		<table height="20" border="0" cellspacing="0" cellpadding="0" title="<%=DateUtil.getDateString(data.obj.getPlanStartDate(),"d") %> ~ <%=DateUtil.getDateString(data.obj.getPlanEndDate(),"d")%>">
		<td width="<%=gap %>">
		</td>
		<%if(!early){ %>
		<td>
			<img src="/Windchill/jsp/project/images/r_start.gif" width="<%=barHead %>" height="20" border="0" />
		</td>
		<%} %>
		<td>
			<img src="/Windchill/jsp/project/images/r_middle.gif" width="<%=du-barHead-barTail %>" height="20" border="0" />
		</td>
		<%if(!over){ %>
		<td>
			<img src="/Windchill/jsp/project/images/r_arrow.gif" width="<%=barTail %>" height="20" border="0" />
		</td>
		<%} %>
		</table>
		</td></tr>
		<tr height="5"><td></td></tr>
		</table>
	</div>
	<div id="treeproduct" style="position:absolute;width:<%=totalWidth %>; height:30;border :1px; overflow:none;">
		<table height="30" border="0" cellspacing="0" cellpadding="0">
		<tr height="6"><td>
		</td></tr>
		<tr height="6">
		<td align="left">
		<table height="6" border="0" cellspacing="0" cellpadding="0" >
		<td width="<%=gap %>">
		</td>
		<td>
		<img src="/Windchill/extcore/kores/portal/images/<%=data.barColor %>_middle.gif" width="<%=perLine %>" height="6" border="0" />
		</td>
		</table>
		</td>
		</tr>
		<tr height="6"><td></td></tr>
		</table>
	</div><%}else{ %>
	<div id="treeproduct" style="position:absolute;width:<%=totalWidth %>; height:30;border :1px; overflow:none;">
		<table height="30" border="0"     cellspacing="0" cellpadding="0">
		<tr height="10"><td></td></tr>
		<tr height="11"><td align="left" >
		
		<table height="11" border="0"   cellspacing="0" cellpadding="0" >
		<td width="<%=gap %>">
		</td>
		<td  title="<%=du %> 일">
		<img src="/Windchill/jsp/project/images/black.gif" width="<%=(du) %>" height=11 border=0>
		</td>
		</table>
		
		</td></tr>
		<tr height="9"><td></td></tr>
		</table>
	</div>
	<%
	double tperline = du * data.completion * 0.01;
	if(tperline>0){
	%>
	<div id="treeproduct" style="position:absolute;width:<%=totalWidth %>; height:30;border :1px; overflow:none;">
		<table height="30" border="0" cellspacing="0" cellpadding="0">
		<tr height="6"><td></td></tr>
		<tr height="6"><td align="left" >
		
		<table height="6" border="0" cellspacing="0" cellpadding="0" >
		<td width="<%=gap %>">
		</td>
		<td title="<%=data.completion %>%">
		<img src="/Windchill/jsp/project/images/p_middle.gif" width="<%=tperline %>" height="6" border="0" />
		</td>
		</table>
		
		</td></tr>
		<tr height="6"><td></td></tr>
		</table>
	</div>
	<%}} %>
	<%
	if(!isProject){ 
		java.sql.Timestamp endDate = data.obj.getEndDate();
		
		if(endDate!=null){
			
		int endPosition = 0;
		
		int xx2 = getDuration(DateUtil.parseDateStr(startdate),new Date(endDate.getTime()));

		if(xx2>0){
			endPosition = xx2;
		}
		
	%>
	
	<div id="endPosition" style="position:absolute;width:<%=endPosition+10 %>; height:30;border :1px; overflow:none;">		
		<table height="30" border="0"     cellspacing="0" cellpadding="0">
		<tr height="2"><td></td></tr>
		<tr height="15"><td align="left" >
		
		<table height="11" border="0"   cellspacing="0" cellpadding="0" title="<%=DateUtil.getDateString(data.obj.getEndDate(),"d") %>">
		<td width="<%=endPosition-5 %>">
		</td>
		<td>
		<img src="/Windchill/jsp/project/images/endPosition.gif" border="0" />
		</td>
		</table>
		
		</td></tr>
		<tr height="13"><td></td></tr>
		</table>
	</div>
	<%}} %>
				<%
			}
			%>
			</td>
			<%
		}
		ca.add(Calendar.DATE,1);
	}
	ca.setTime(DateUtil.parseDateStr(startdate));
	%>
	</tr>
	<%} %>
	
	</table>
</form>	
</body>
</html>
<%!
public int getDuration(Date date1, Date date2){
	Date pre = date1;
	Date after = date2;
	
	long oneDayMillis = 24*60*60*1000;
	
	int day = (int)((after.getTime() - pre.getTime())/oneDayMillis);
	return day;
}

public class ProjectChartData{
	public int count = 1;
	public int level;
	public ScheduleNode obj;
	public String name;
	public String owner;
	public double completion;
	public double pjtDuration;
	public String preferCompStr;
	public double toDuration;
	public String differPerString = "";
	public String barColor = "b";
	
	public String oid;
	public double preferComp;
	public double differPer ;
	
	
	public double df;
	public double  gper;
	public String gperString;
	
	public ProjectChartData(ScheduleNode node){
		this(node,0,1);
	}
	
	public ProjectChartData(ScheduleNode node,int level,int count){
		
		this.level = level;
		this.count = count;
		
		obj = node;
		
		oid = node.getPersistInfo().getObjectIdentifier().toString();
		
		NumberFormat nf = NumberFormat.getInstance();
	    nf.setMaximumFractionDigits(0);
		name = node.getName();

		completion = node.getCompletion();
		owner = "";
		pjtDuration = DateUtil.getDuration(node.getPlanStartDate(), node.getPlanEndDate()) + 1;
		toDuration = getDuration(node.getPlanEndDate(),new Date());
		
	}
	
}

private String getTaskStateFont(ProjectChartData data, Calendar currentDate)throws Exception {
	
	ScheduleNode task = (ScheduleNode)data.obj;
	
	String ss = "";
	
	if(task instanceof ETaskNode){
		ETask tasknode = (ETask)task;
		ss = tasknode.getStatus();
	}
	if(task instanceof EProject){
		EProject pjtnode = (EProject)task;
		ss = pjtnode.getState().toString();
	}
	
	if(STATEKEY.COMPLETED.equals(ss)){
		return "<FONT color='74C600'>";
	}
	if(STATEKEY.PROGRESS.equals(ss)){
		
		if(isDelay(task)){
			return "<FONT color='FF3300'>";//red
		}else{
			return "<FONT color='0033CC'>";//blue
		}
	}
	return "<FONT color='7A8B8B'>";//gray
}

private boolean isDelay(ScheduleNode task ){
	String ss = "";
	if(task instanceof ETaskNode){
		ETask tasknode = (ETask)task;
		ss = tasknode.getStatus();
	}
	if(task instanceof EProject){
		EProject pjtnode = (EProject)task;
		ss = pjtnode.getState().toString();
	}
	if(!STATEKEY.PROGRESS.equals(ss))return false;
		
	String today = DateUtil.getCurrentDateString("d");
	String planEnd = DateUtil.getDateString(task.getPlanEndDate(),"d");
	if(today.compareTo(planEnd)>0){
			return true;
	}
	return false;
}

%>