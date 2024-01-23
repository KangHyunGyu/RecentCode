<%@page import="com.e3ps.change.beans.ChangeECRSearch"%>
<%@page import="java.util.List"%>
<%@page import="com.e3ps.approval.service.ApprovalService"%>
<%@page import="com.e3ps.common.util.CommonUtil"%>
<%@page import="wt.lifecycle.LifeCycleManaged"%>
<%@page import="wt.doc.WTDocument"%>
<%@page import="wt.epm.EPMDocument"%>
<%@page import="wt.part.WTPart"%>
<%@page import="wt.vc.VersionControlHelper"%>
<%@page import="wt.fc.WTObject"%>
<%@page import="com.e3ps.change.EcrProjectLink"%>
<%@page import="com.e3ps.change.EChangeOrder2"%>
<%@page import="com.e3ps.common.util.WebUtil"%>
<%@page import="wt.org.WTUser"%>
<%@page import="com.e3ps.approval.service.ApprovalHelper"%>
<%@page import="com.e3ps.change.service.ChangeHelper"%>
<%@page import="wt.vc.Versioned"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.e3ps.common.util.DateUtil"%>
<%@page import="com.e3ps.common.code.service.CodeHelper"%>
<%@page import="com.e3ps.project.RoleUserLink"%>
<%@page import="com.e3ps.project.service.ProjectMemberHelper"%>
<%@page import="com.e3ps.project.EProject"%>
<%@page import="com.e3ps.change.RequestOrderLink"%>
<%@page import="wt.fc.PersistenceHelper"%>
<%@page import="wt.fc.QueryResult"%>
<%@page import="wt.query.SearchCondition"%>
<%@page import="wt.query.QuerySpec"%>
<%@page import="com.e3ps.change.EChangeRequest2"%>
<%@page import="com.e3ps.approval.CommonActivity"%>
<%@page import="wt.fc.ReferenceFactory"%>
<%@page contentType="text/html; charset=UTF-8"%>
<%
    String oid = request.getParameter("oid");
    ReferenceFactory rf = new ReferenceFactory();
    CommonActivity ca = (CommonActivity)rf.getReference(oid).getObject();
    EChangeRequest2 ecr = null;
    String pboOid = "";
    QuerySpec qs = new QuerySpec(EChangeRequest2.class);
    qs.appendWhere(new SearchCondition(EChangeRequest2.class,"workReference.key.id","=",ca.getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
    QueryResult qrq = PersistenceHelper.manager.find(qs);
    if(qrq.hasMoreElements()){
		ecr = (EChangeRequest2)qrq.nextElement();
		pboOid = CommonUtil.getOIDString(ecr);
	}
    QueryResult ecoQr = PersistenceHelper.manager.navigate(ecr,"order",RequestOrderLink.class);
    boolean existEco = false;
    boolean isReady = false;
    while(ecoQr.hasMoreElements()){
    	existEco = true;
    	EChangeOrder2 eco = (EChangeOrder2)ecoQr.nextElement();
    	String state = ApprovalHelper.service.getState(eco);
    	if(ApprovalService.MASTER_INWORK.equals(state)){
    		isReady = true;
    	}
    }
    
	 // #. 변경품목
 	List<WTPart> relatedParts = new ArrayList<WTPart>();
 	relatedParts = ChangeECRSearch.getECOrelatedParts(ecr);
 	
 	// #. 변경도면
 	List<Object> relatedDrawings = new ArrayList<Object>();
 	relatedDrawings = ChangeECRSearch.getECOrelatedDrawings(ecr);
 	
 	request.setAttribute("relatedParts", relatedParts);
 	request.setAttribute("relatedDrawings", relatedDrawings);
%>
    
<script>

function gotoEOCreate(){
	
	<%if(existEco){%>
	if(!confirm("이미 등록된 ECO가 있습니다. "추가로 등록 하시겠습니까?")){
		return;
	}
	<%}%>
	var sURL = "/Windchill/worldex/change/createECO";
	var	params = { popup : "true", ecrOid : "<%=pboOid%>" };
	var callbackName = "";
	var callbackObj = "";
	sURL = appendCallbackParams(sURL, callbackName, params, callbackObj);
	var sName = "createECO";
	var nWidth = 1200;
	var nHeight = 500;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

function checkFunction(){

	<%if(!existEco){%>
		alert("ECO 를 등록 해 주십시오");
		return false;
	<%}%>
	
	<%if(isReady){%>
		alert("작성중인 ECO가 있습니다. 결재요청 한 후에 완료 할 수 있습니다.");
		return false;
	<%}%>
	
    return true;
}

</script>
<input type=hidden name=ecrOid value="<%=pboOid%>">
<div class="product"> 
<div class="pro_table mr30 ml30">
	<div class="seach_arm2 pt5">
		<div class="leftbt">
			<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
			임무</span>
		</div>
		<div class="rightbt">
		</div> 
	</div>
<table class="mainTable">
	<colgroup>
		<col width="10%">
		<col width="90%">
	</colgroup>
	<tr> 
		<th>임무</th>
		<td height="100px">
		ECO의 등록자로 지정 되었습니다. 아래의 승인된 ECR의 정보를 확인한 후 ECO를 작성해 주십시오.
	                 <%
	                  String isWorker = request.getParameter("isWorker");
	                  if("true".equals(isWorker)){
	              %><br>
	              <input type="button" class="button02" value="ECO 등록" onclick="gotoEOCreate()">
	              <%}%>
		</td>
	</tr>
</table>

<div class="pro_table mr30 ml30">
	<div class="seach_arm2 pt5">
		<div class="leftbt">
			<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
			ECR 상세정보</span>
		</div>
		<div class="rightbt">
		</div> 
	</div>
	<jsp:include page="/jsp/change/include/viewECR_include2.jsp" flush="true">
				<jsp:param value="<%=pboOid %>" name="oid"/>
				</jsp:include>
</div>
</div>
</div>