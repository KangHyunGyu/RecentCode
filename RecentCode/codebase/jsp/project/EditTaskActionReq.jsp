<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.e3ps.project.service.ProjectHelper"%>
<%@page import="com.e3ps.project.service.TemplateHelper"%>
<%@page import="com.e3ps.project.beans.TaskEditHelper"%>
<%@page import="com.e3ps.project.beans.ProjectNodeData"%>
<%@page import="com.e3ps.project.beans.ProjectUtil"%>
<%@page import="com.e3ps.project.TaskOutputLink"%>
<%@page import="com.e3ps.project.PrePostLink"%>
<%@page import="wt.fc.QueryResult"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.Date"%>
<%@page import="com.e3ps.project.EProjectNode"%>
<%@page import="com.e3ps.project.ScheduleNode"%>
<%@page import="com.e3ps.common.util.DateUtil"%>
<%@page import="com.e3ps.common.util.CommonUtil"%>
<%@page import="com.e3ps.project.ETaskNode"%>
<%@page import="com.e3ps.project.ETask"%>
<%@page import="java.util.Hashtable"%>
<%@page import="wt.fc.ReferenceFactory"%>
<%@page import="wt.fc.PersistenceHelper"%>
<%@page import="com.e3ps.org.Department"%>
<%@page import="com.e3ps.common.util.StringUtil"%>
<%@ page contentType="text/xml; charset=UTF-8" %>

<%
	String cmd = StringUtil.checkNull(request.getParameter("cmd"));
	String soid = StringUtil.checkNull(request.getParameter("soid"));
	String pjtOid = StringUtil.checkNull(request.getParameter("pjtOid"));
	
	String oid = "";
	String message = "";
	
	System.out.println("==========="+cmd);
	
	Hashtable hash = new Hashtable();
	try{
		ArrayList list = null;
		String cellId = "";
		String newVal = "";
		if("delete".equals(cmd) || "getPlan".equals(cmd) || "editCell".equals(cmd)){
			if("delete".equals(cmd)){
				hash.put("selectChild", soid);
				hash.put("oid", oid);
				list = ProjectHelper.service.deleteTask(hash);
				cmd = "deleted";
				
			}else if("getPlan".equals(cmd)){
				
				String arrObj = StringUtil.checkNull(request.getParameter("arrObj"));
				String att1 = StringUtil.checkNull(request.getParameter("att1"));
				
				if(arrObj != null){
					String[] slist = arrObj.split(",");
					
					ArrayList arr = new ArrayList();
					int len = slist.length;
					for(int i=0; i<len; i++){
						arr.add(slist[i]);
						//if(soid != slist[i]){
							ETaskNode node = (ETaskNode)CommonUtil.getObject(slist[i]);
							newVal = newVal + node.getName();
							
							if(i != len-1){newVal=newVal+", ";}
						//}
					}
					arr.add(soid);
					ArrayList hslist =  TaskEditHelper.service.getPlan(arr);
					
					String[] alist = att1.split(",");
					for(int i=0; alist!=null && i<alist.length; i++){
						ScheduleNode sc = (ScheduleNode)CommonUtil.getObject(alist[i]);
						if(sc != null){
							hslist.add(sc);
						}
					}
					
					// ArrayList 중복제거
					HashSet hs = new HashSet(hslist);
					list = new ArrayList(hs);
				}
				
			}else if("editCell".equals(cmd)){
				
				cellId = StringUtil.checkNull(request.getParameter("cellId"));
				newVal = StringUtil.checkNull(request.getParameter("newVal"));
				
				hash.put("soid", soid);
				hash.put("cellId", cellId);
				hash.put("pjtOid", pjtOid);
				hash.put("newVal", newVal);
				ArrayList hslist =  TaskEditHelper.service.editCell(hash);
				// ArrayList 중복제거
				HashSet hs = new HashSet(hslist);
				list = new ArrayList(hs);
				
			}
		%>
		<result>
			<code><%=cmd%></code>
			<soid><%=soid%></soid>
			<cellId><%=cellId%></cellId>
			<newVal><%=newVal%></newVal>
			<%if("2".equals(cellId) || "3".equals(cellId) || "4".equals(cellId) || "deleted".equals(cmd) || "getPlan".equals(cmd)){%>
			<data><![CDATA[
			[
			<%
			int size = list.size();
			int index = 0;
			
				for(int i=0; i<list.size(); i++){
					
					ScheduleNode node = (ScheduleNode)list.get(i);
					
					/* ProjectNodeData data = new ProjectNodeData(node);
					data.getDuration();
					data.getDurationHoliday(); */
					
					Timestamp st = node.getPlanStartDate();
					Timestamp ed = node.getPlanEndDate();
					
					//int duration = DateUtil.getDuration(new Date(st.getTime()),new Date(ed.getTime())) + 1;
					int duration = ProjectUtil.getDuration(st,ed);
					int durationHoliday = ProjectUtil.getDurationHoliday(st,ed);
					
					index ++;
				%>
				{
					oid : '<%=node.getPersistInfo().getObjectIdentifier().toString()%>',
					start : '<%=DateUtil.getDateString(st,"d")%>',
					end : '<%=DateUtil.getDateString(ed,"d")%>',
					duration : '<%=durationHoliday%>'
				}
				<%if(index < size){%>,<%}
				}
				%>
				]
			]]></data>
			<%}
			if("1".equals(cellId)){
				ArrayList<ETaskNode> hslist =  TaskEditHelper.service.getPlan(soid);
				HashSet hs = new HashSet(hslist);
				list = new ArrayList(hs);
			%>
			<data><![CDATA[
			[
				<%
				
				int idx = 0;
				for(int i=0; i<list.size(); i++){
					ETaskNode task = (ETaskNode)list.get(i);
					String text = "";
					String oidText = "";
					QueryResult qr = PersistenceHelper.manager.navigate(task, "pre",
							PrePostLink.class);
					int len = qr.size();
					int qridx = 0;
					while (qr.hasMoreElements()) {
						ETaskNode sgnode = (ETaskNode) qr.nextElement();
						text = text + sgnode.getName();
						oidText = CommonUtil.getOIDString(task);
						if(qridx != len-1){text=text+", ";}
					}
					if(qr.size() == 0){
						continue;
					}
					idx++;
				%>
				{
					oid : '<%=oidText%>',
					text : '<%=text%>'
				}
				<%if(idx < hslist.size()){%>,<%}	
				}%>
			]
			]]></data>
			<%
			}
			%>
		</result>
		<%
		}else{
		
			int sort = 0;
			
			ScheduleNode sNode = (ScheduleNode)CommonUtil.getObject(soid);
			
			if("new".equals(cmd) || sNode instanceof EProjectNode){
				ScheduleNode pjtNode = (ScheduleNode)CommonUtil.getObject(pjtOid);
				sort = TemplateHelper.manager.getMaxSeq(pjtNode);
				
			}else if("child".equals(cmd)){
				sort = TemplateHelper.manager.getMaxSeq(sNode);
				
				QueryResult qr2 = PersistenceHelper.manager.navigate(sNode,"output",TaskOutputLink.class);
				if(qr2.size() > 0){
					cmd = "fail";
					message = "Task의 산출물이 등록되어 있는 경우 하위 Task 추가가 불가능 합니다.";
				}
			}
			
			if(sort == 0){ sort = 1; }
			String name = "새태스크";
			
			Timestamp st = sNode.getPlanStartDate();
			Timestamp ed = sNode.getPlanEndDate();
			
			int duration = DateUtil.getDuration(new Date(st.getTime()),new Date(ed.getTime())) + 1;
			
			String stdate = DateUtil.getDateString(st, "d"); 
			String eddate = DateUtil.getDateString(ed, "d");
			
			if("new".equals(cmd) || "child".equals(cmd)){
				
				if("new".equals(cmd)){
					hash.put("selectId", pjtOid);
					
					stdate = DateUtil.getToDay(); 
					eddate = DateUtil.getToDay(); 
					
					duration = 1;
					
					soid = pjtOid
							;
				}else if("child".equals(cmd)){
					hash.put("selectId", soid);
				}
				
				hash.put("cname", name);
				hash.put("description", "");
				hash.put("sort", Integer.toString(sort));
				hash.put("planStartDate", stdate);
				hash.put("planEndDate", eddate);
				
				ETaskNode task = ProjectHelper.service.createTask(hash);
				oid = task.getPersistInfo().getObjectIdentifier().toString();
			}else if("next".equals(cmd) || "prev".equals(cmd)){
				
				hash.put("cname", name);
				hash.put("description", "");
				hash.put("sort", Integer.toString(sort));
				hash.put("planStartDate", stdate);
				hash.put("planEndDate", eddate);
				
				hash.put("selectId", soid);
				hash.put("oid", oid);
				hash.put("cmd", cmd);
				
				ETaskNode task = ProjectHelper.service.nextSave(hash);
				oid = task.getPersistInfo().getObjectIdentifier().toString();
				sort = task.getSort();
				name = "새태스크 ";
			}else if("up".equals(cmd) || "down".equals(cmd)){
				
				ScheduleNode snode = (ScheduleNode)CommonUtil.getObject(soid);
				String poid = soid;
				ETaskNode task = null;
				
				if(snode instanceof ETaskNode){
					poid = snode.getParent().getPersistInfo().getObjectIdentifier().toString();
				}
				
				HashMap<String, Object> hashMap = new HashMap<>();
				
				hashMap.put("oid", pjtOid);
				hashMap.put("selectChild", soid);
				hashMap.put("selectId", poid);
				
				hash.put("oid", pjtOid);
				hash.put("selectChild", soid);
				hash.put("selectId", poid);
				
				if("up".equals(cmd)){
					
					QueryResult qr = PersistenceHelper.manager.navigate(snode,"pre",PrePostLink.class);
					if(qr.size() > 0){
						cmd = "fail";
						message = "선행 태스크가 있는 경우 위로 이동할 수 없습니다.";
					}else{
						Map<String, Object> returnMap = ProjectHelper.service.moveUpTask(hashMap);
						task = (ETaskNode) returnMap.get("task");
					}
				}else{
					
					QueryResult qr = PersistenceHelper.manager.navigate(snode,"post",PrePostLink.class);
					if(qr.size() > 0){
						cmd = "fail";
						message = "후행 태스크가 있는 경우 아래로 이동할 수 없습니다.";
					}else{
						Map<String, Object> returnMap = ProjectHelper.service.moveDownTask(hashMap);
						task = (ETaskNode) returnMap.get("task");
					}
				}
			}
			%>
			<result>
				<code><%=cmd%></code>
				<oid><%=oid%></oid>
				<soid><%=soid%></soid>
				<name><%=name%></name>
				<duration><%=duration%></duration>
				<stdate><%=stdate%></stdate>
				<eddate><%=eddate%></eddate>
				<sort><%=sort%></sort>
				<message><%=message%></message>
			</result>
			<%	
		}
	}catch(Exception e){
		e.printStackTrace();
	%>
	<result>
		<code>error</code>
		<message><![CDATA[<%=e.getMessage()%>]]></message>
	</result>
	<%
	}
 %>