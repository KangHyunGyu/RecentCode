<%@page import="com.e3ps.project.key.ProjectKey.IssueKey"%>
<%@page import="com.e3ps.project.service.StandardIssueService"%>
<%@page import="com.e3ps.project.beans.IssueData"%>
<%@page import="com.e3ps.project.beans.ProjectNodeData"%>
<%@page import="com.e3ps.project.beans.IssueHelper"%>
<%@page import="wt.fc.PersistenceHelper"%>
<%@page import="wt.fc.QueryResult"%>
<%@page import="com.e3ps.common.web.PageQueryBroker"%>
<%@page import="wt.query.ClassAttribute"%>
<%@page import="wt.query.OrderBy"%>
<%@page import="wt.query.SearchCondition"%>
<%@page import="com.e3ps.project.issue.IssueRequest"%>
<%@page import="com.e3ps.project.ETaskNode"%>
<%@page import="com.e3ps.project.ScheduleNode"%>
<%@page import="wt.query.QuerySpec"%>
<%@page import="wt.fc.ReferenceFactory"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="/Windchill/jsp/css/style.css">
<%
String oid = request.getParameter("oid");
String viewType = request.getParameter("viewType");

ReferenceFactory rf = new ReferenceFactory();
ScheduleNode node = (ScheduleNode)rf.getReference(oid).getObject();

if(node instanceof ETaskNode){
	node = ((ETaskNode)node).getProject();
}

QuerySpec qs = new QuerySpec();
int ii = qs.addClassList(IssueRequest.class,true);
int jj = qs.addClassList(ETaskNode.class,true);
qs.appendWhere(new SearchCondition(IssueRequest.class,"taskReference.key.id",ETaskNode.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{ii,jj});
qs.appendAnd();
qs.appendWhere(new SearchCondition(ETaskNode.class,"projectReference.key.id","=",node.getPersistInfo().getObjectIdentifier().getId()),new int[]{jj});

qs.appendOrderBy(new OrderBy(new ClassAttribute(IssueRequest.class,"thePersistInfo.createStamp"),true),new int[]{ii});

PageQueryBroker broker = new PageQueryBroker(request,qs);
broker.setPsize(15);
QueryResult qr = broker.search();

qs = new QuerySpec();
ii = qs.addClassList(IssueRequest.class,true);
jj = qs.addClassList(ETaskNode.class,false);
qs.appendWhere(new SearchCondition(IssueRequest.class,"taskReference.key.id",ETaskNode.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{ii,jj});
qs.appendAnd();
qs.appendWhere(new SearchCondition(ETaskNode.class,"projectReference.key.id","=",node.getPersistInfo().getObjectIdentifier().getId()),new int[]{jj});
qs.appendAnd();
qs.appendWhere(new SearchCondition(IssueRequest.class,IssueRequest.STATE,"=",IssueKey.ISSUE_COMPLETE),new int[]{ii});

QueryResult qqr = PersistenceHelper.manager.find(qs);
%>
<form name="viewForm" method="post" action="/Windchill/worldex/project/issue/listIssue">
<input type="hidden" name="command"/>
<input type="hidden" name="oid" value="<%=oid %>"/>
<input type="hidden" name="viewType" value="<%=viewType==null?"":viewType %>"/>

<div class="pro_table mr30 ml30">
<div class="seach_arm2 pt5">
	<div class="leftbt">
		<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
		프로젝트 전체 이슈 목록</span>
	</div>
	<div class="rightbt">
		[등록(<%=qqr.size() %>)/전체(<%=broker.getTotal() %>)]
	</div> 
</div>

<table class="mainTable">
	<tr align="center" >
		<th width="3%" >NO</th>
		<th width="20%">태스크 명</th>
		<th width="30%">이슈</th>
		<th width="12%">제기자</th>
		<th width="12%">제기일자</th>
		<th width="12%">담당자</th>
		<th width="10%">작업현황</th>
	</tr>
	<%
	int count = broker.getPsize() * (broker.getCpage()-1) + 1;

	while(qr.hasMoreElements()){
		Object[] o = (Object[])qr.nextElement();
		IssueRequest issue = (IssueRequest)o[0];
		ETaskNode task = (ETaskNode)o[1];
		ProjectNodeData tdata = new ProjectNodeData(task);
		IssueData data = new IssueData(issue);
	%>
		<tr onMouseOver="this.style.backgroundColor='#EFF7FF';" onMouseOut="this.style.backgroundColor='#FFFFFF';">
			<td align="center"><%=count++ %></td>
			<td title="<%=task.getName() %>">
			<div style="width:150;border:0;padding:0;margin:0;text-overflow:ellipsis;overflow:hidden;">
				<nobr>
				<a href="JavaScript:parent.selectTreeItem('<%=task.getPersistInfo().getObjectIdentifier().toString() %>')">
				<%=task.getName() %></a>
				</nobr>
			</div>
			</td>
			<td title="<%=data.getTitle() %>">
			<div style="width:150;border:0;padding:0;margin:0;text-overflow:ellipsis;overflow:hidden;">
				<nobr>
				<a href="JavaScript:viewIssue('<%=issue.getPersistInfo().getObjectIdentifier().toString() %>')" >
				<%=data.getTitle() %></a>
				</nobr>
			</div>
			</td>
			<td align="center"><%=data.getCreatorFullName() %> </td>
			<td align="center"><%=data.getCreateDate() %> </td>
			<td align="center"><%=data.getManagerFullName() %> </td>
			<td align="center"><%=data.getState() %> </td>
		</tr>
	<%} %>
</table>
</div>
</form>

<script type="text/javascript">
function viewIssue(ooid){
	var loc= "/Windchill/worldex/project/issue/viewIssue?oid=" + ooid;
	openPopup(loc, "viewIssue", 1000, 800);
}
</script>