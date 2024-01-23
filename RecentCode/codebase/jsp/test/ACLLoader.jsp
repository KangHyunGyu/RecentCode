<%@page import="wt.access.AccessPermissionSet"%>
<%@page import="wt.access.AccessPermission"%>
<%@page import="wt.access.AccessControlHelper"%>
<%@page import="wt.access.AccessControlRule"%>
<%@page import="com.e3ps.common.message.util.MessageUtil"%>
<%@page import="wt.admin.AdministrativeDomain"%>
<%@page import="wt.org.OrganizationServicesHelper"%>
<%@page import="wt.org.WTRolePrincipal"%>
<%@page import="wt.org.WTOrganization"%>
<%@page import="jxl.Cell"%>
<%@page import="jxl.Sheet"%>
<%@page import="jxl.Workbook"%>
<%@page import="com.e3ps.common.util.JExcelUtil"%>
<%@page import="com.e3ps.common.jdf.config.ConfigImpl"%>
<%@page import="java.io.File"%>
<%@page import="wt.inf.container.WTContainerRef"%>
<%@page import="wt.inf.container.WTContainerHelper"%>
<%@page import="wt.query.OrderBy"%>
<%@page import="wt.util.WTAttributeNameIfc"%>
<%@page import="wt.query.ClassAttribute"%>
<%@page import="com.e3ps.distribute.DistributeRegistration"%>
<%@page import="com.e3ps.distribute.DistributeDocument"%>
<%@page import="com.e3ps.common.db.DBConnectionManager"%>
<%@page import="com.e3ps.interfaces.util.InterfaceUtil"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="wt.inf.container.WTContainer"%>
<%@page import="wt.query.SearchCondition"%>
<%@page import="wt.vc.config.ConfigHelper"%>
<%@page import="wt.epm.EPMDocConfigSpec"%>
<%@page import="wt.pds.StatementSpec"%>
<%@page import="wt.vc.config.LatestConfigSpec"%>
<%@page import="wt.vc.config.ConfigSpec"%>
<%@page import="wt.epm.EPMDocument"%>
<%@page import="wt.query.QuerySpec"%>
<%@page import="org.json.simple.parser.JSONParser"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="wt.org.WTPrincipalReference"%>
<%@page import="wt.org.WTUser"%>
<%@page import="com.e3ps.org.People"%>
<%@page import="wt.fc.PersistenceHelper"%>
<%@page import="com.e3ps.common.util.WCUtil"%>
<%@page import="com.e3ps.change.service.ChangeService"%>
<%@page import="com.e3ps.change.EChangeActivity"%>
<%@page import="com.e3ps.common.util.DateUtil"%>
<%@page import="wt.admin.AdminDomainRef"%>
<%@page import="wt.admin.AdministrativeDomainHelper"%>
<%@page import="wt.folder.SubFolder"%>
<%@page import="wt.session.SessionHelper"%>
<%@page import="wt.org.WTPrincipal"%>
<%@page import="wt.method.MethodContext"%>
<%@page import="wt.method.RemoteMethodServer"%>
<%@page import="com.e3ps.admin.AuthorityGroup"%>
<%@page import="com.e3ps.admin.util.ObjectKey"%>
<%@page import="wt.folder.FolderMemberLink"%>
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
<%@page import="com.ptc.wvs.common.cadagent.CadProxy"%>
<%@page import="wt.lifecycle.State"%>
<%@page import="wt.vc.VersionControlHelper"%>
<%@page import="wt.fc.QueryResult"%>
<%@page import="wt.fc.ReferenceFactory"%>
<%@page import="wt.part.WTPart"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
loadGroup();
%>
<%!
public void loadGroup() throws Exception {
	
	//상태별 객체에 대한 기본 ACL 설정 로더
	//LifeCycle 이 존재하는 객체에 대해서만 작성할 것!
	
	String filePath ="D:\\ptc\\Windchill_12.1\\Windchill\\loadFiles\\e3ps\\ACLLoader.xls";
    File newfile =  new File(filePath);
    Workbook wb = JExcelUtil.getWorkbook(newfile);
    Sheet[] sheets = wb.getSheets();
	
	String orgName = ConfigImpl.getInstance().getString("org.context.name");
	String productName = ConfigImpl.getInstance().getString("product.context.name");
	
	//정책 생성
	
	int rows = sheets[0].getRows();
     
    System.out.println("rows = " +rows);
    Cell[] cell = sheets[0].getRow(1);
     
    for (int j = 1; j < rows; j++){
    	
    	cell = sheets[0].getRow(j);
    	
    	
		String context = JExcelUtil.getContent(cell, 0).trim();
		String rootPath = JExcelUtil.getContent(cell, 1).trim();
    	String targetClassName = JExcelUtil.getContent(cell, 2).trim();
    	String state = JExcelUtil.getContent(cell, 3).trim().toUpperCase();
    	System.out.println("클래스명:"+targetClassName+" /상태:"+state);
    	
    	String contextPath="";
    	
    	if("사이트".equals(context)) {
    		contextPath="/wt.inf.container.ExchangeContainer=사이트";
    	}else if("조직".equals(context)){
    		contextPath="/wt.inf.container.OrgContainer="+orgName;
    	}else {
    		contextPath="/wt.inf.container.OrgContainer="+orgName+"/wt.pdmlink.PDMLinkProduct="+productName;
    	}
    	
    	WTContainerRef container_ref = WTContainerHelper.service.getByPath(contextPath);
    	WTOrganization wtorg = OrganizationServicesHelper.manager.getOrganization(orgName);
    	WTRolePrincipal teamMember = OrganizationServicesHelper.manager.getRolePrincipal("teamMembers", container_ref, true, MessageUtil.getLocale());
    	//조직>제품 컨텍스트 아래 /Default 도메인 가져오기
    	AdministrativeDomain domain = AdministrativeDomainHelper.manager.getDomain(rootPath, container_ref);
    	AdminDomainRef doaminref = AdminDomainRef.newAdminDomainRef(domain);
    	WTPrincipalReference prinRef = WTPrincipalReference.newWTPrincipalReference(teamMember);
    	if("사이트".equals(context)) prinRef = WTPrincipalReference.newWTPrincipalReference(wtorg);
    	
    	createACL(doaminref, targetClassName, state, prinRef, cell);
    }
	
}

private void createACL(AdminDomainRef doaminref, String objectClassName, String state, WTPrincipalReference prinRef, Cell[] cell) throws Exception {
	
	//유형 - 정책 적용할 대상 객체
	String targetObject = "WCTYPE|" + objectClassName;
	
	//상태 - 적용할 LifeCycle
	State lifeCycle = null;
	if(state != null && state.length() > 0) lifeCycle = State.toState(state);
	
	//String orgName = ConfigImpl.getInstance().getString("org.context.name");
	//WTOrganization wtorg = OrganizationServicesHelper.manager.getOrganization(orgName);
	//참여자 - 현재는 조직으로 되어있으나 팀, 사용자, 그룹 단위로도 가능
	
	AccessPermissionSet grant = new AccessPermissionSet();
	AccessPermissionSet deny = new AccessPermissionSet();
	AccessPermissionSet absolutDeny = new AccessPermissionSet();
	
	String all = JExcelUtil.getContent(cell, 4).trim();
	String read = JExcelUtil.getContent(cell, 5).trim();
	String download = JExcelUtil.getContent(cell, 6).trim();
	String modify = JExcelUtil.getContent(cell, 7).trim();
	String modify_content = JExcelUtil.getContent(cell, 8).trim();
	String modify_identity = JExcelUtil.getContent(cell, 9).trim();
	String modify_security_labels = JExcelUtil.getContent(cell, 10).trim();
	String create_by_move = JExcelUtil.getContent(cell, 11).trim();
	String create = JExcelUtil.getContent(cell, 12).trim();
	String set_state = JExcelUtil.getContent(cell, 13).trim();
	String revise = JExcelUtil.getContent(cell, 14).trim();
	String new_view_version = JExcelUtil.getContent(cell, 15).trim();
	String change_domain = JExcelUtil.getContent(cell, 16).trim();
	String change_context = JExcelUtil.getContent(cell, 17).trim();
	String change_permissions = JExcelUtil.getContent(cell, 18).trim();
	String delete = JExcelUtil.getContent(cell, 19).trim();
	String administrative = JExcelUtil.getContent(cell, 20).trim();
	
	if(all.length()>0 && "O".equals(all)) {
		grant.add(AccessPermission.ALL);
	}else if(all.length()>0 && "X".equals(all)) {
		deny.add(AccessPermission.ALL);
	}
	
	if(read.length()>0 && "O".equals(read)) {
		grant.add(AccessPermission.READ);
	}else if(read.length()>0 && "X".equals(read)){
		deny.add(AccessPermission.READ);
	}
	
	if(download.length()>0 && "O".equals(download)) {
		grant.add(AccessPermission.DOWNLOAD);
	}else if(download.length()>0 && "X".equals(download)){
		deny.add(AccessPermission.DOWNLOAD);
	}
	
	if(modify.length()>0 && "O".equals(modify)) {
		grant.add(AccessPermission.MODIFY);
	}else if(modify.length()>0 && "X".equals(modify)){
		deny.add(AccessPermission.MODIFY);
	}
	
	if(modify_content.length()>0 && "O".equals(modify_content)) {
		grant.add(AccessPermission.MODIFY_CONTENT);
	}else if(modify_content.length()>0 && "X".equals(modify_content)){
		deny.add(AccessPermission.MODIFY_CONTENT);
	}
	
	if(modify_identity.length()>0 && "O".equals(modify_identity)) {
		grant.add(AccessPermission.MODIFY_IDENTITY);
	}else if(modify_identity.length()>0 && "X".equals(modify_identity)){
		deny.add(AccessPermission.MODIFY_IDENTITY);
	}
	
	if(modify_security_labels.length()>0 && "O".equals(modify_security_labels)) {
		grant.add(AccessPermission.MODIFY_SECURITY_LABELS);
	}else if(modify_security_labels.length()>0 && "X".equals(modify_security_labels)){
		deny.add(AccessPermission.MODIFY_SECURITY_LABELS);
	}
	
	if(create_by_move.length()>0 && "O".equals(create_by_move)) {
		grant.add(AccessPermission.CREATE_BY_MOVE);
	}else if(create_by_move.length()>0 && "X".equals(create_by_move)){
		deny.add(AccessPermission.CREATE_BY_MOVE);
	}
	
	if(create.length()>0 && "O".equals(create)) {
		grant.add(AccessPermission.CREATE);
	}else if(create.length()>0 && "X".equals(create)){
		deny.add(AccessPermission.CREATE);
	}
	
	if(set_state.length()>0 && "O".equals(set_state)) {
		grant.add(AccessPermission.SET_STATE);
	}else if(set_state.length()>0 && "X".equals(set_state)){
		deny.add(AccessPermission.SET_STATE);
	}
	
	if(revise.length()>0 && "O".equals(revise)) {
		grant.add(AccessPermission.REVISE);
	}else if(revise.length()>0 && "X".equals(revise)){
		deny.add(AccessPermission.REVISE);
	}
	
	if(new_view_version.length()>0 && "O".equals(new_view_version)) {
		grant.add(AccessPermission.NEW_VIEW_VERSION);
	}else if(new_view_version.length()>0 && "X".equals(new_view_version)){
		deny.add(AccessPermission.NEW_VIEW_VERSION);
	}
	
	if(change_domain.length()>0 && "O".equals(change_domain)) {
		grant.add(AccessPermission.CHANGE_DOMAIN);
	}else if(change_domain.length()>0 && "X".equals(change_domain)){
		deny.add(AccessPermission.CHANGE_DOMAIN);
	}
	
	if(change_context.length()>0 && "O".equals(change_context)) {
		grant.add(AccessPermission.CHANGE_CONTEXT);
	}else if(change_context.length()>0 && "X".equals(change_context)){
		deny.add(AccessPermission.CHANGE_CONTEXT);
	}
	
	if(change_permissions.length()>0 && "O".equals(change_permissions)) {
		grant.add(AccessPermission.CHANGE_PERMISSIONS);
	}else if(change_permissions.length()>0 && "X".equals(change_permissions)){
		deny.add(AccessPermission.CHANGE_PERMISSIONS);
	}
	
	if(delete.length()>0 && "O".equals(delete)) {
		grant.add(AccessPermission.DELETE);
	}else if(delete.length()>0 && "X".equals(delete)){
		deny.add(AccessPermission.DELETE);
	}
	
	if(administrative.length()>0 && "O".equals(administrative)) {
		grant.add(AccessPermission.ADMINISTRATIVE);
	}else if(administrative.length()>0 && "X".equals(administrative)){
		deny.add(AccessPermission.ADMINISTRATIVE);
	}
	
	if(!checkACL(doaminref, targetObject, lifeCycle, prinRef, false)) AccessControlHelper.manager.createAccessControlRule(doaminref, targetObject, lifeCycle, prinRef, false, grant, deny, absolutDeny);
}


private boolean checkACL(AdminDomainRef doaminref, String authObject, State state, WTPrincipalReference prinRef, boolean exclusion) throws Exception {
	
	boolean existACL = false;
	
	AccessControlRule rule = AccessControlHelper.manager.getAccessControlRule(doaminref, authObject, state, prinRef, exclusion);
	
	if(rule!=null) existACL = true;
	
	return existACL;
}

%>

