<%@page import="com.e3ps.project.key.ProjectKey.STATEKEY"%>
<%@page import="com.e3ps.common.util.TypeUtil"%>
<%@page import="com.e3ps.common.util.StringUtil"%>
<%@page import="com.e3ps.common.util.CommonUtil"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.e3ps.project.beans.*"%>
<%@page import="com.e3ps.project.*"%>
<%@page import="java.util.Vector"%>
<%@page import="wt.query.*,wt.fc.*,wt.org.*,wt.session.*,wt.org.*"%>
<%@page import="java.util.*"%>

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<head>
<%
String module = request.getParameter("module");
String menu = request.getParameter("menu");
String oid = request.getParameter("oid");
String pjtOid = request.getParameter("pjtOid");
String viewType = request.getParameter("viewType");

boolean isView = TypeUtil.booleanValue(request.getParameter("isView"));
boolean isAuth = TypeUtil.booleanValue(request.getParameter("isAuth"));
boolean isEditState = TypeUtil.booleanValue(request.getParameter("isEditState"));

ReferenceFactory rf = new ReferenceFactory();
ScheduleNode snode = (ScheduleNode)rf.getReference(pjtOid).getObject();
EProject pjt = (EProject)CommonUtil.getObject(pjtOid);
EProjectNode root = (EProjectNode)snode;

ProjectNodeData rdata = new ProjectNodeData(root);
String state = pjt.getLifeCycleState().toString();

long rootKey = root.getPersistInfo().getObjectIdentifier().getId();
String rootId = root.getPersistInfo().getObjectIdentifier().toString();
ArrayList list = ProjectDao.manager.getStructure(rootKey);

boolean isLock = STATEKEY.STOP.equals(state) || STATEKEY.MODIFY.equals(state);

OutputType otype = root.getOutputType();
%>
<link rel="stylesheet" href="/Windchill/jsp/css/e3ps.css" type="text/css">
<link rel="stylesheet" type="text/css" href="/Windchill/jsp/css/style.css">
<!-- <link rel="stylesheet" href="/Windchill/jsp/css/dtree.css" type="text/css"> -->
<script type="text/javascript" src="/Windchill/jsp/js/dtree.js"></script>
<script type="text/javascript" src="/Windchill/jsp/js/script.js"></script>
<script type="text/javascript" src="/Windchill/jsp/js/jquery/jquery-3.4.1.min.js"></script>

<script type="text/javascript">

$(document).ready(function(){
	
	$("#btPart").on("click", function(){
		viewData('/Windchill/worldex/project/masteredLinkList?type=drawing&oid=<%=oid %>');
	});
	$("#btDrw").on("click", function(){
		viewData('/Windchill/worldex/project/masteredLinkList?type=part&oid=<%=oid %>');
	}); 
	$("#btOutput").on("click", function(){
		viewData('/Windchill/worldex/project/projectOutputList?oid=<%=oid %>');
	}); 
	$("#btTask").on("click", function(){
		taskEditPop('<%=rootId%>');
	}); 
	$("#btReload").on("click", function(){
		reloadTree();
	}); 
	
	
	
});

function selectTreeItem(soid){
	var isView = document.treeForm.isView.value;
	var isAuth= document.treeForm.isAuth.value;
	var isEditState= document.treeForm.isEditState.value;
	
	document.treeForm.oid.value = soid;
	
	var param = "?oid="+soid+"&isView="+isView+"&isAuth="+isAuth+"&isEditState="+isEditState;

	if( soid.indexOf("ETask") > 0) {
		parent.pjtBody.location.href = "/Windchill/worldex/project/viewTask"+param;
	}else{	//EProjectTemplate
		parent.pjtBody.location.href = "/Windchill/worldex/project/viewProject"+param;
	}
}

function viewData(loc){
	parent.pjtBody.location.href = loc;
}
function viewOpen(loc){
	openWindow(loc, "task_edit", 1000, 600); 
}

function reloadTree(){
	location.reload();
}
function excelReport(){
	document.treeForm.action = "/Windchill/jsp/project/report/ProjectWbsReport.jsp";
	document.treeForm.submit();
}
function viewDefault(){
	defaultTree.style.display = 'block';
	outputTypeTree.style.display = 'none';
	document.treeForm.viewType.value="default";
}
function viewOutputType(){
	defaultTree.style.display = 'none';
	outputTypeTree.style.display = 'block';
	document.treeForm.viewType.value="outputType";
}
function onResize(e){
	
}
function taskEditPop(oid){
	var url = '/Windchill/jsp/project/EditTaskPlan.jsp?oid='+oid;
	var opts = "toolbar=0,location=0,directory=0,status=1,menubar=0,scrollbars=1,resizable=1,";
	leftpos = (screen.availWidth - 1000)/ 2; 
	toppos = (screen.availHeight - 60 - 600) / 2 ; 
	rest = "width=" + 1000 + ",height=" + 600+',left=' + leftpos + ',top=' + toppos;
	var newwin = open( url , "task_edit", opts+rest);
	newwin.focus();
}

function taskEditPop2(oid){
	var url = '/Windchill/jsp/project/EditTaskPlan2.jsp?oid='+oid;
	var opts = "toolbar=0,location=0,directory=0,status=1,menubar=0,scrollbars=1,resizable=1,";
	leftpos = (screen.availWidth - 1000)/ 2; 
	toppos = (screen.availHeight - 60 - 600) / 2 ; 
	rest = "width=" + 1000 + ",height=" + 600+',left=' + leftpos + ',top=' + toppos;
	var newwin = open( url , "task_edit", opts+rest);
	newwin.focus();
}

function expendTree(level){
	try{
	if(wbs!=null)
		wbs.openLevel(level);
	if(otree!=null)
		otree.openLevel(level);
	}catch(e){}
}
</script>
</head>
<form name="treeForm" method="get">
<input type="hidden" name="module" value="<%=module==null?"":module%>"/>
<input type="hidden" name="menu" value="<%=menu==null?"":menu%>"/>
<input type="hidden" name="oid" value="<%=oid %>"/>
<input type="hidden" name="isView" value="<%=isView%>" />
<input type="hidden" name="isAuth" value="<%=isAuth%>" />
<input type="hidden" name="isEditState" value="<%=isEditState%>" />
<input type="hidden" name="viewType" value="<%=viewType==null?"":viewType %>"/>
<div class="pl30">
	<div class="tree_top">
	   <div class="tree_t01 pt15">
			<p><img class="pointer" id="btDrw" src="/Windchill/jsp/project/images/img/bt_12.png"></p>
			<p><img class="pointer" id="btPart" src="/Windchill/jsp/project/images/img/bt_13.png"></p>
			<p><img class="pointer" id="btOutput" src="/Windchill/jsp/project/images/img/bt_14.png"></p>
			<p><img class="pointer" id="btTask" src="/Windchill/jsp/project/images/img/bt_15.png"></p>
			<p><img class="pointer" id="btReload" src="/Windchill/jsp/project/images/img/bt_16.png"></p>			
	   </div>
	   <div class="tree_t02 pt15" style="padding-left: 22px">
			<p> 
				<select onchange="expendTree(this.value)">
					<option level="">열기</option>
					<option value="1">1 Level</option>
					<option value="2">2 Level</option>
					<option value="3">3 Level</option>
					<option value="1000">전체 Level</option>
				</select>
				<button type="button" class="s_bt07" onclick="JavaScript:excelReport();">엑셀</button>
			</p>
	   </div>

		<div class="tree_t03 pt15 pb15">
			<p><img src="/Windchill/jsp/project/images/img/state_g.png"> 완료</p>
			<p><img src="/Windchill/jsp/project/images/img/state_b.png"> 진행</p>
			<p><img src="/Windchill/jsp/project/images/img/state_r.png"> 지연</p>
			<p><img src="/Windchill/jsp/project/images/img/state_d.png"> 예정</p>
				
		</div>
		<div>
		<%if("PSO".equals(otype.toString())){%>
			<table border="0" cellpadding="0" cellspacing="4" align="center">
	        <tr><td>
			<input type="button" value="기본"  class="s_bt03" onclick="javascript:viewDefault()">
			</td><td>
			<input type="button" value="PSO" class="s_bt03" onclick="viewOutputType()">
			</td></tr>
			</table>
		</div>
	<%}%>
	</div>
<%-- <table border="0" cellpadding="0" cellspacing="0" width="100%">
||||||| .r57
=======
<tr><td>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
>>>>>>> .r95
	<tr  >
		<td height=10 >&nbsp; </td>
		<td align="center">
		<span>
			<a href="javascript:viewData('/Windchill/worldex/project/masteredLinkList?type=drawing&oid=<%=oid %>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('leftbtn_06','','/Windchill/jsp/project/images/project/draw_1.gif',1)">
			<img src="/Windchill/jsp/project/images/project/draw.gif" alt="도면 목록" name="leftbtn_06" width="23" height="37" border="0">
			</a>
		</span> 
		<span>
			<a href="javascript:viewData('/Windchill/worldex/project/masteredLinkList?type=part&oid=<%=oid %>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('leftbtn_06','','/Windchill/jsp/project/images/project/part_1.gif',1)">
			<img src="/Windchill/jsp/project/images/project/part.gif" alt="부품 목록" name="leftbtn_06" width="23" height="37" border="0">
			</a>
		</span> 
		<!-- //유사품 목록 -->
<!-- 		<span> -->
			<a href="javascript:viewData('/Windchill/worldex/project/masteredLinkList2?type=part&oid=<%=oid %>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('leftbtn_06','','/Windchill/jsp/project/images/project/output_1.gif',1)">
<!-- 			<img src="/Windchill/jsp/project/images/project/doc.gif" alt="유사품 목록" name="leftbtn_06" width="23" height="37" border="0"> -->
<!-- 			</a> -->
<!-- 		</span>  -->
		<!-- //산출물 목록 -->
		<span>
			<a href="javascript:viewData('/Windchill/worldex/project/projectOutputList?oid=<%=oid %>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('leftbtn_06','','/Windchill/jsp/project/images/project/output_1.gif',1)">
			<img src="/Windchill/jsp/project/images/project/output.gif" alt="산출물 목록" name="leftbtn_06" width="23" height="37" border="0">
			</a>
		</span> 
		<!-- //이슈 목록 -->
		<%if(!STATEKEY.READY.equals(state)){ %>
		<span>
			<a href="javascript:viewData('/Windchill/worldex/project/issue/listIssue?oid=<%=oid %>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('leftbtn_07','','/Windchill/jsp/project/images/project/issue_1.gif',1)">
			<img src="/Windchill/jsp/project/images/project/issue.gif" alt="이슈 목록" name="leftbtn_07" width="23" height="37" border="0">
			</a>
		</span> 
		<%}
		System.out.println("isAuth : " + isAuth);
		System.out.println("isEditState : " + isEditState);
		System.out.println("isView : " + isView);
		
		if(isAuth && isEditState &&(!isView)){ %>
		<!-- //태스크 수정 -->
		<span>
			<a href="javascript:taskEditPop('<%=rootId%>');" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('leftbtn_03','','/Windchill/jsp/project/images/project/task_1.gif',1)">
			<img src="/Windchill/jsp/project/images/project/task.gif" alt="태스크 편집" name="leftbtn_03" width="23" height="37" border="0">
			</a>
		</span>
		<%} %>
		<!-- //새로고침 -->
		<span>
			<a href="javascript:reloadTree();" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('leftbtn_02','','/Windchill/jsp/project/images/project/refrash_1.gif',1)">
			<img src="/Windchill/jsp/project/images/project/refrash.gif" alt="새로고침" name="leftbtn_02" width="23" height="37" border="0">
			</a>
		</span>
		</td>
	</tr>	
</table>

</td>
</tr>

<tr><td>


<table id='treetop' border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td class="tab_btm5"></td>
	</tr>
	<tr>
		<td align="center">
			<div style="width:300;border:0;padding:0;margin:0;text-overflow:ellipsis;overflow:hidden;">
				<nobr>
					<select onchange="expendTree(this.value)">
						<option level="">열기</option>
						<option value="1">1 Level</option>
						<option value="2">2 Level</option>
						<option value="3">3 Level</option>
						<option value="1000">전체 Level</option>
					</select>
				<a href="JavaScript:excelReport()" title="Excel Report"><img src="/Windchill/jsp/project/images/fileicon/xls.gif" border=0></a>
				</nobr>
			</div>
		</td>
	</tr>
	<%if("PSO".equals(otype.toString())){%>
	<tr>
		<td  align="center">
			<table border="0" cellpadding="0" cellspacing="4" align="center">
	        <tr><td>
			<input type="button" value="기본"  class="whitebtn" onclick="javascript:viewDefault()">
			</td><td>
			<input type="button" value="PSO" class="whitebtn" onclick="viewOutputType()">
			</td></tr>
			</table>
		</td>
	</tr>
	<%}%>
</table>
<!-- 열기 -->




<table width="99%" border="0" cellpadding="0" cellspacing="0" >
	<tr>
		<td  align="center" valign="top">
			<font size="1" color="green"><img src="/Windchill/jsp/project/images/tree/task_complete.gif" >&nbsp;완료</font>
			<font size="1" color="blue"><img src="/Windchill/jsp/project/images/tree/task_progress.gif">&nbsp;진행</font>
			<font size="1" color="red"><img src="/Windchill/jsp/project/images/tree/task_red.gif">&nbsp;지연</font>
			<font size="1" color="black"><img src="/Windchill/jsp/project/images/tree/task_ready.gif">&nbsp;예정</font>
		</td>
	</tr>	
</table> --%>





</div>


<div class="pl30">
	<div class="tree_top2">

	<div style="width:100%;overflow-x:hidden;overflow-y:auto;border:0px;border-style:solid;border-color:#5F9EA0;padding:0px;margin:0px 0px;">
	<div id="scrollBox" style="OVERFLOW-Y:auto;OVERFLOW-X:auto; width:300; height:700" onscroll="true" >

	<table cellpadding="0" cellspacing="0" width="90%" id="defaultTree" style="display:<%="outputType".equals(viewType)?"none":""%>">
 		<tr>
  			<td class="pd20">

			    <script type="text/javascript">
			        var wbs = new dTree('wbs','/Windchill/jsp/project/images/tree');
					
			        wbs.add(<%=rootKey%>,-1,'<img src=<%=rdata.getIconUrl()%>><%=root.getName()%><%=isLock?" &nbsp;<font color=red>["+rdata.getState()+"]</font>":"<font color=blue>["+rdata.getState()+"]</font>"%>',"JavaScript:selectTreeItem('<%=rootId%>')",'<%=root.getName()%>');
			            <%
			            int selIdx = 0;
			            for(int i=0; i< list.size(); i++){
			            	
			                String[] s = (String[])list.get(i);
			                String level = s[0];
			                String name = s[1];
			                String sid = s[2];
			                String parent = s[3];
			                String desc = s[4];
			                
			                ScheduleNode node = (ScheduleNode)rf.getReference(sid).getObject();
			                ProjectNodeData data = new ProjectNodeData(node);
			                String iconUrl = data.getIconUrl();
			                
			                String key = sid.substring(sid.lastIndexOf(":")+1);
			                
			                if(sid.equals(oid)){
			                	selIdx = i+1; 
			                }
			
			                int lev = Integer.parseInt(level);
							
			                String tlink = "javascript:selectTreeItem('"+sid+"')";
			
			                %>
			                    var node= wbs.add(<%=key%>,<%=parent%>,'<%=name%>',"<%=tlink%>",'<%=s[1]%>',null, '<%=iconUrl%>' , '<%=iconUrl%>');
			                    node.setLevel(<%=level%>);
			                <%
			            }
			        %>
			        document.write(wbs);
			        wbs.s(<%=selIdx%>);
					
			    </script>

 		
</td></tr></table>
<%-- PSO TREE --%>
<table cellpadding="0" cellspacing="0" width="90%" id="outputTypeTree" style="display:<%="outputType".equals(viewType)?"":"none"%>">
<tr><td>
<%



if("PSO".equals(otype.toString())){
	String otypeId = otype.toString();
	ArrayList slist = ProjectDao.manager.getStepTree(root.getPersistInfo().getObjectIdentifier().getId(),otypeId);
%>
    <script type="text/javascript">
        <!--
        var otree = new dTree('otree','/Windchill/jsp/project/images/tree');

        	otree.add(<%=rootKey%>,-1,'<%=otype.getDisplay(Locale.KOREA)%><%=isLock?" &nbsp;<font color=red>["+rdata.getState()+"]</font>":"<font color=blue>["+rdata.getState()+"]</font>"%>',"JavaScript:selectTreeItem('<%=rootId%>')",'<%=root.getName()%>');
            <%
            ArrayList temp = new ArrayList();
            
            selIdx = 0;
            int count = 0;
            for(int i=0; i< slist.size(); i++){
        		String[] ss = (String[])slist.get(i);
        		String level = ss[0];
        		String stepName = ss[1];
        		String stepOidLong = ss[2];
        		String outputOidLong = ss[3];
        		String outputName = ss[4];
        		String etaskOid = ss[5];
        		String parent = ss[6];
        		long parentOid = rootKey;
        		if(!"0".equals(parent)){
        			parentOid = Long.parseLong(parent);//rootKey;//Long.parseLong(parent);
        		}else{
        			parentOid = rootKey;//rootKey;
        		}
        		if(!temp.contains(stepOidLong)){
        			%>
                    var node2= otree.add(<%=stepOidLong%>,<%=parentOid%>,'<%=stepName%>',null,null,null,'/Windchill/jsp/project/images/tree/step.gif','/Windchill/jsp/project/images/tree/step.gif');
                    node2.setLevel(<%=level%>);
                	<%
                	count++;
        			temp.add(stepOidLong);
        		}
        		
        		if(outputOidLong!=null){
        			
        			if(etaskOid.equals(oid)){
                    	selIdx = count+1; 
                    }
        			String tlink = "javascript:selectTreeItem('"+etaskOid+"')";
        			
	        		%>
	                var node2= otree.add(<%=outputOidLong%>,<%=stepOidLong%>,'<%=outputName%>',"<%=tlink%>",null,null,'/Windchill/jsp/project/images/tree/imgfolder.gif','/Windchill/jsp/project/images/tree/imgfolder.gif');
	                node2.setLevel(<%=level%>);
	                
	            	<%count++;
        		}
        	}

        %>
        document.write(otree);
        otree.s(<%=selIdx%>);
		
        //-->
    </script>
    <%} %>
</td>
</tr>
</table>

<!-- PSO TREE     -->

</div>
</div>
</div>
</div>

</form>