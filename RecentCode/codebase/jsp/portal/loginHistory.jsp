<%@page import="com.e3ps.common.history.service.HistoryHelper"%>
<%@page import="com.e3ps.common.util.CommonUtil"%>
<%@page import="com.e3ps.common.service.CommonHelper"%>
<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@page import ="wt.session.*,wt.org.*,java.util.Calendar,wt.query.*,wt.fc.*"%>
<script type="text/javascript" src="/Windchill/login/js/jquery/jquery-3.4.1.min.js"></script>



<%
System.out.println("jsp History		==	start");
String browser = CommonUtil.getBrowser(request);
String ip = CommonUtil.getClientIP(request);
HistoryHelper.service.createLoginHistory(browser, ip);
System.out.println("jsp History		==	end");
%>
<script type="text/javascript">
$(document).ready(function() {
	location.href="/Windchill/worldex/portal/main";
});
</script>

