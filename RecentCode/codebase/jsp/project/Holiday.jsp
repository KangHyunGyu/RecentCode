<%@page import="com.e3ps.common.util.CommonUtil"%>
<%@page import="com.e3ps.project.Holiday"%>
<%@page import="com.e3ps.common.web.PageQueryBroker"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.e3ps.common.util.WCUtil"%>
<%@page import="wt.query.ClassAttribute"%>
<%@page import="wt.query.OrderBy"%>
<%@page import="wt.query.QuerySpec"%>
<%@page import="wt.query.SearchCondition"%>
<%@page import="wt.fc.PersistenceHelper"%>
<%@page import="wt.fc.QueryResult"%>
<%@page import="wt.fc.ReferenceFactory"%>
<%@page import="com.e3ps.common.code.NumberCode"%>
<%@page import="com.e3ps.common.code.NumberCodeType"%>
<%@page import="com.e3ps.common.util.StringUtil" %>
<%@page import="com.e3ps.common.util.WCUtil"%>

<%@ page contentType="text/html;charset=UTF-8"%>

<%
    String command = request.getParameter("command");
    String holiday = StringUtil.checkNull(request.getParameter("holiday"));
    String oid = StringUtil.checkNull(request.getParameter("oid"));
    String enable = request.getParameter("enabled");
    boolean enabled = false;
    if("true".equals(enable)){
    	enabled = true;
    }
	
    String msg = "";
	String result = "";
	
	QuerySpec query = new QuerySpec(Holiday.class);
    query.appendOrderBy(new OrderBy(new ClassAttribute(Holiday.class,Holiday.HOLIDAY),false),new int[]{0});
    PageQueryBroker broker = new PageQueryBroker(request,query);
	QueryResult qr = broker.search();
	
	
    if("create".equals(command)) {
  		msg = "작성";
  		Holiday day = new Holiday();
  		day.setHoliday(holiday);
  		day.setEnable(enabled);
  		PersistenceHelper.manager.save(day);
 %>
 	<script type="text/javascript">
 		
 		var re = "<%=result%>";
 		var msg = "<%=msg%>";
 		alert(msg);
 		location.href="Holiday.jsp";
 		
 	</script>
 <%       
    }
    if("update".equals(command)){
    	Holiday day = (Holiday)CommonUtil.getObject(oid);
        day.setHoliday(holiday);
  		day.setEnable(enabled);
        day = (Holiday) PersistenceHelper.manager.modify(day);
		msg = "수정";
%>
		 <script type="text/javascript">
 			var re = "<%=result%>";
 			var msg = "<%=msg%>";
 			alert(msg);
 			location.href="Holiday.jsp";
 		</script>
 <%    }
    if("delete".equals(command)){
    	Holiday day = (Holiday)CommonUtil.getObject(oid);
     	PersistenceHelper.manager.delete(day);
        oid = null;
        msg = "삭제";
 %>  
          	<script type="text/javascript">
        	var msg = "<%=msg%>";
 			alert(msg);
 			location.href="Holiday.jsp";
 			</script>
     <%}
	%>

<link rel="stylesheet" type="text/css" href="/Windchill/extcore/kores/css/e3ps.css" />
<script type="text/javascript">
    function create1(){
		var cForm = document.codeForm;
		
		if(cForm.holiday.value == ""){
			alert("휴무일을 입력하세요.");
			return;
		}
        cForm['command'].value="create";
        cForm.submit();
    }

    function update1(){
        var cForm = document.codeForm;
        
        if(cForm.holiday.value == ""){
        	alert("휴무일을 입력하세요.");
			return;
		}
        cForm['command'].value="update";
        cForm.submit();
    }

    function delete1(){
        var cForm = document.codeForm;
        cForm['command'].value="delete";
        cForm.submit();
    }

    function selectNode(oid){
        var cForm = document.codeForm;
        cForm['oid'].value=oid;
        cForm.submit();
    }
    function search(){
    	var cForm = document.codeForm;
    	cForm['command'].value="search";
    	cForm.sessionid.value='';
    	cForm.submit();
    }
</script>

<form name="codeForm" method="post" >
    <input type="hidden" name="command">
    <input type="hidden" name="oid" value="<%=oid%>" />

    <table width="100%" border="0" cellpadding="0" cellspacing="20" > <!--//여백 테이블-->
        <tr align="center" height=5>
            <td>
                <table width="100%" border="0" cellpadding="10" cellspacing="3" >
                    <tr align="center" height=5>
                        <td valign="top" style="padding:0px 0px 0px 0px" height=3 >
                            <table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="ffffff" align="center">
                                <tr>
                                    <td align="left" height="50">
                                    <%
                                    ReferenceFactory rf = new ReferenceFactory();
                                    Holiday day = (Holiday)CommonUtil.getObject(oid);
                                    if(day != null){
                                    	holiday = day.getHoliday();
                                    	enabled = day.isEnable();
                                    }
                                    %>
                                        Holiday :  <jsp:include page="/extcore/kores/common/include/dateInput.jsp" flush="true">
													<jsp:param name="dateId" value="holiday"/>
													<jsp:param name="holiday" value="<%=holiday%>"/>
												</jsp:include>
                                    	 활성화 : <input type="checkbox" name="enabled" value="true" <%=enabled?"checked":""%>>
                                    </td>
                                    <td align="right">
                                    	<%if("".equals(oid)){ %> 
                                        <input type="button" value="등록" onclick="create1()">
	                                    <%}else{ %>
	                                        <input type="button" value="수정" onclick="update1()">
	                                        <input type="button" value="삭제" onclick="delete1()">
	                                    <%} %>
                                   </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr align="center">
                        <td valign="top" style="padding:0px 0px 0px 0px">
                            <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="black" align="center">
                                <tr bgcolor="ffffff" align="center">
                                    <td>휴무일</td>
                                    <td width="7%">활성화</td>
                                </tr>
								<%
								    while(qr.hasMoreElements()){
								    	Object[] o = (Object[]) qr.nextElement();
								    	Holiday listday = (Holiday) o[0];
								    	//NumberCode ncode = (NumberCode)qr.nextElement();
								%>
                                <tr bgcolor="ffffff" >
                            	    <td><a href="#" onclick="selectNode('<%=WCUtil.getOIDString(listday)%>')"><%=listday.getHoliday()%></a></td>
                                    <td><%=listday.isEnable()%></td>
                                </tr>
<%
    }
%>
                            </table>
                            
                            <table width="100%" border="0" cellpadding="2" cellspacing="1" align="center" valign="top">
								<tr>
								<td>
							
								<%=broker.getHtml("codeForm")%>
								
								</td></tr>
							</table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</form>
