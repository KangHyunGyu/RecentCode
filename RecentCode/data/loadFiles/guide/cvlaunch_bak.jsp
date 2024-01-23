<%@ page import="com.ptc.wvs.server.ui.UIHelper"%>
<%@ page import="wt.util.HTMLEncoder"%>
<%response.setDateHeader("Expires", 0L);%>
<%response.setHeader("Cache-Control", "no-cache");%>
<%@ page import="com.ptc.wvs.common.util.WVSProperties"%>
<%@ include file="/wtcore/jsp/wvs/dialog.jspf"%>
<%@ include file="/netmarkets/jsp/components/beginWizard.jspf"%>

<wctags:createVisualizerDialogWindow/>

<%@ include file="setup.jspf" %>     
<%       
    // The main purpose of this JSP is to support the use case of an email URL to a 
    // representation in Winchill, of which invoked, will launch Creo View.
    
    String legacyURL = request.getRequestURL().toString() + "?" + HTMLEncoder.encodeForJavascript(request.getQueryString()); 
    legacyURL = legacyURL.replace("cvlaunch.jsp", "edrview.jsp");
    legacyURL = legacyURL.replace("&elink=true","").replace("elink=true","");
    boolean usePV = UIHelper.useStandardPV();
%>       
           

<TABLE WIDTH=100% BORDER=0 CELLSPACING=0 CELLPADDING=0>     
   <TR><TD ALIGN=center><p style="font-size:30px"><%=msg.getMsg(messageResource.STARTING_CREO_VIEW_CLIENT)%></p></TD></TR>
</TABLE>
</BODY>

<SCRIPT language="Javascript">

(function() {
   // Launch Creo View using the legacy URL.
   createCDialogWindow('<%=legacyURL%>', '<%=UIHelper.getWindowTarget(usePV)%>', '<%=UIHelper.getWindowXSize(usePV)%>', '<%=UIHelper.getWindowYSize(usePV)%>','<%=UIHelper.getShowStatusBar(usePV)%>','<%=UIHelper.getShowScrollBar(usePV)%>');
})();       
</SCRIPT> 

<%@ include file="/netmarkets/jsp/util/end.jspf"%>