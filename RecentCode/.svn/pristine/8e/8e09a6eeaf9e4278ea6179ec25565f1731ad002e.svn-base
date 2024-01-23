<%@page import="wt.epm.EPMDocumentHelper"%>
<%@page import="com.ptc.core.htmlcomp.upload.NewGenericEPMDocumentTaskSubmitHelper"%>
<%@page import="wt.epm.EPMDocumentType"%>
<%@page import="com.lowagie.text.pdf.PdfPublicKeySecurityHandler"%>
<%@page import="com.ptc.wvs.common.ui.Publisher"%>
<%@page import="com.e3ps.epm.util.EpmUtil"%>
<%@page import="wt.content.ContentRoleType"%>
<%@page import="wt.content.ContentHelper"%>
<%@page import="wt.content.ContentItem"%>
<%@page import="com.ptc.wvs.server.publish.Publish"%>
<%@page import="wt.vc.config.ConfigSpec"%>
<%@page import="wt.fc.PersistenceHelper"%>
<%@page import="wt.query.ClassAttribute"%>
<%@page import="wt.query.SearchCondition"%>
<%@page import="wt.viewmarkup.DerivedImage"%>
<%@page import="wt.query.QuerySpec"%>
<%@page import="wt.epm.EPMDocument"%>
<%@page import="com.e3ps.epm.service.EpmHelper"%>
<%@page import="wt.epm.util.EPMHelper"%>
<%@page import="wt.content.ApplicationData"%>
<%@page import="com.e3ps.part.bean.PartData"%>
<%@page import="java.util.Map"%>
<%@page import="com.e3ps.part.service.BomHelper"%>
<%@page import="com.e3ps.part.bean.BomTreeData"%>
<%@page import="java.util.List"%>
<%@page import="com.e3ps.common.util.StringUtil"%>
<%@page import="com.e3ps.common.util.CommonUtil"%>
<%@page import="com.e3ps.part.service.PartHelper"%>
<%@page import="java.util.HashMap"%>
<%@page import="wt.vc.views.ViewHelper"%>
<%@page import="wt.vc.views.View"%>
<%@page import="wt.vc.baseline.Baseline"%>
<%@page import="wt.lifecycle.State"%>
<%@page import="wt.vc.VersionControlHelper"%>
<%@page import="wt.fc.QueryResult"%>
<%@page import="wt.fc.ReferenceFactory"%>
<%@page import="wt.part.WTPart"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%!
public static void publish(EPMDocument epm) throws Exception {

	boolean flag1 = true;

	String fileName = "";
	QueryResult result = ContentHelper.service.getContentsByRole (epm ,ContentRoleType.PRIMARY );
	if (result.hasMoreElements ()) {
		ContentItem primaryFile = (ContentItem) result.nextElement ();
		fileName = ((ApplicationData)primaryFile).getFileName();
	}
	
	String authoringType = EpmUtil.getExtension(fileName).toUpperCase();
	
	if (authoringType.equals("DSN") || authoringType.equals("BRD") || authoringType.equals("ZIP") || authoringType.equals("PDF")){
		flag1 = false;
	}
	//LOGGER.info("Publish Check ::: flag1"+ flag1+"\tfileName="+fileName);
	if (flag1) {
		//LOGGER.info("Publish Check222222222222222222 ::: flag1"+ flag1+"\tfileName="+fileName);
		ConfigSpec configspec = null;
		ConfigSpec configspec1 = null;
		Publish.doPublish(false, true, epm, configspec, null, false, null, null, 5, null, 2, null);
	}
}

public static List<EPMDocument> getNonPublishedDocument(String cadType) throws Exception{
	
	
	
	
	
	return null;
}
%>
<%
	/*
	select '"'|| e.classnamea2a2 || ':' || e.ida2a2 || '",', m.documentnumber
	from derivedimage d,
	     epmdocument e,
	     epmdocumentmaster m
	where e.ida2a2 = d.ida3a6(+) and 
	      d.ida3a6 is null and
	      m.ida2a2(+) = e.ida3masterreference and
	      m.authoringapplication = 'INVENTOR' and
	      m.documentnumber not like 'PTC%' and
	      m.documentnumber like '%IAM';
	*/



	String[] oids = new String[]{
			"wt.epm.EPMDocument:699928",
			"wt.epm.EPMDocument:632804",
			"wt.epm.EPMDocument:737907"
	};
	

	for(String oid : oids){
		EPMDocument epm = (EPMDocument)CommonUtil.getObject(oid);
		
		//파트일 경우
		//EpmHelper.service.publish(epm);
		
		//어셈블리일 경우
		publish(epm);
	}


%>

