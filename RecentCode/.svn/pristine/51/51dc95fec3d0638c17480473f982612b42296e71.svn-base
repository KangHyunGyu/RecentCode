<%@page import="wt.query.ConstantExpression"%>
<%@page import="java.util.Enumeration"%>
<%@page import="wt.fc.collections.WTList"%>
<%@page import="wt.fc.collections.WTArrayList"%>
<%@page import="com.e3ps.admin.service.AdminHelper"%>
<%@page import="com.e3ps.admin.AuthorityGroupType"%>
<%@page import="com.e3ps.admin.ObjectAuthGroup"%>
<%@page import="com.e3ps.admin.AuthorityObjectType"%>
<%@page import="wt.inf.container.OrgContainer"%>
<%@page import="wt.org.WTGroup"%>
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
	
	//그룹 생성, 정책 생성, 객체 생성 
	
	AuthorityObjectType[] codeType = AuthorityObjectType.getAuthorityObjectTypeSet();
	
	Map<String, Object> reqMap = new HashMap<String,Object>();
	
	String orgName = ConfigImpl.getInstance().getString("org.context.name");
	String productName = ConfigImpl.getInstance().getString("product.context.name");
	WTContainerRef container_ref = WTContainerHelper.service.getByPath("/wt.inf.container.OrgContainer="+orgName+"/wt.pdmlink.PDMLinkProduct="+productName);
	
	AdministrativeDomain domain = AdministrativeDomainHelper.manager.getDomain("/Default", container_ref);
	AdminDomainRef doaminref = AdminDomainRef.newAdminDomainRef(domain);
	
	for(int i=0; i < codeType.length; i++){	
		
		String objKey = codeType[i].toString();
		System.out.println("OBJECT KEY : " + objKey + " /OBJECT CLASSNAME : " + codeType[i].getDisplay());
		WTGroup authParentGroup = null;
		WTGroup permissionGroup = null;
		WTGroup readGroup = null;
		
		authParentGroup = getGroup(objKey+"_AUTH");
		if(authParentGroup==null) {
			authParentGroup = AdminHelper.service.createWTGroup(objKey+"_AUTH", objKey+" auth management parent group");
		}
		
		permissionGroup = getGroup(objKey+"_PERMISSION");
		if(permissionGroup==null) {
			permissionGroup = AdminHelper.service.createWTGroup(objKey+"_PERMISSION", objKey+" permission");
		}
		
		readGroup = getGroup(objKey+"_READ");
		if(readGroup==null) {
			readGroup = AdminHelper.service.createWTGroup(objKey+"_READ", objKey+" read and download");
		}
		
		System.out.println("Permission Group : " + permissionGroup.getName() + " /Read Group : " + readGroup.getName());
		
		List<String> addGroupList = new ArrayList<String>();
		
		//parent group 추가
		reqMap.put("oid", CommonUtil.getOIDString(authParentGroup));
		reqMap.put("edit", true);
		
		boolean existPermissionGroupParent = false;
		
		Enumeration<?> permissionGroupParents = permissionGroup.parentGroups();
		
		while(permissionGroupParents.hasMoreElements()){
			Object o = (Object)permissionGroupParents.nextElement();
			
			if(o instanceof WTPrincipalReference){
				WTPrincipalReference u = (WTPrincipalReference)o;
				WTGroup g = (WTGroup)u.getPrincipal();
				if(g.getName().equals(authParentGroup.getName())) existPermissionGroupParent = true;
			}
		}
		
		if(!existPermissionGroupParent) {
			addGroupList.add(CommonUtil.getOIDString(permissionGroup));
		}
		
		
		boolean existReadGroupParent = false;
		
		Enumeration<?> readGroupParents = readGroup.parentGroups();
		
		while(readGroupParents.hasMoreElements()){
			Object o = (Object)readGroupParents.nextElement();
			
			if(o instanceof WTPrincipalReference){
				WTPrincipalReference u = (WTPrincipalReference)o;
				WTGroup g = (WTGroup)u.getPrincipal();
				if(g.getName().equals(authParentGroup.getName())) existReadGroupParent = true;
			}
		}
		
		if(!existReadGroupParent) {
			addGroupList.add(CommonUtil.getOIDString(readGroup));
		}
		
		reqMap.put("addGroupList", addGroupList);
		AdminHelper.service.editGroupUser(reqMap);
		System.out.println(objKey+" Group Member Add!");
		
		if(!existACL_YN(doaminref, objKey, authParentGroup, true)) {
			//부모 그룹을 제외한 나머지에 대해 거부 권한 설정
			AdminHelper.service.editACL(doaminref, objKey, authParentGroup, "DENY", "create", true);
		}
		
		if(!existACL_YN(doaminref, objKey, permissionGroup, false)) {
			//허용 그룹에 대해 모든 권한 허용
			AdminHelper.service.editACL(doaminref, objKey, permissionGroup, "PERMISSION", "create", false);
		}
		
		if(!existACL_YN(doaminref, objKey, readGroup, false)) {
			//읽기 그룹에 대해 읽기 권한 허용
			AdminHelper.service.editACL(doaminref, objKey, readGroup, "READ", "create", false);
		}
		
		System.out.println(objKey+" ACL Create!");
		WTList list = new WTArrayList();
		ObjectAuthGroup permissionLink = AdminHelper.manager.getObjectAuthGroupByWTGroup(permissionGroup);
		if(permissionLink==null) {
			ObjectAuthGroup oagPermissionGroup = ObjectAuthGroup.newObjectAuthGroup();
			oagPermissionGroup.setAuthObjectType(codeType[i]);
			oagPermissionGroup.setObjectGroup(permissionGroup);
			oagPermissionGroup.setAuthGroupType(AuthorityGroupType.toAuthorityGroupType("PERMISSION"));
			list.add(oagPermissionGroup);
		}
		
		ObjectAuthGroup readLink = AdminHelper.manager.getObjectAuthGroupByWTGroup(readGroup);
		if(readLink==null) {
			ObjectAuthGroup oagReadGroup = ObjectAuthGroup.newObjectAuthGroup();
			oagReadGroup.setAuthObjectType(codeType[i]);
			oagReadGroup.setObjectGroup(readGroup);
			oagReadGroup.setAuthGroupType(AuthorityGroupType.toAuthorityGroupType("READ"));
			list.add(oagReadGroup);
		}
		System.out.println("list.size()=>"+ list.size());
		if(list.size() > 0) {
			PersistenceHelper.manager.save(list);
		}
		
		System.out.println(objKey+" ObjectAuthGroup Create!");
		System.out.println("============================================================================");
	}

	
}


public WTGroup getGroup(String name) throws Exception {
	
	WTGroup group = null;
	
	 String targetOrgName = ConfigImpl.getInstance().getString("org.context.name");
	 
	 QuerySpec qs = new QuerySpec();
	 
	 int groupIndex = qs.appendClassList(WTGroup.class, true);
	 int orgIndex = qs.appendClassList(OrgContainer.class, false);
	 
	 SearchCondition sc = new SearchCondition( new ClassAttribute(WTGroup.class, WTGroup.CONTAINER_ID), SearchCondition.EQUAL, new ClassAttribute(OrgContainer.class, WTAttributeNameIfc.ID_NAME));
	 qs.appendWhere( sc , new int[] {groupIndex, orgIndex});
	 
	 qs.appendAnd();
	 
	 sc = new SearchCondition( new ClassAttribute(OrgContainer.class, OrgContainer.NAME), SearchCondition.EQUAL, new ConstantExpression(targetOrgName));
	 qs.appendWhere( sc , new int[] {orgIndex});
	 
	 qs.appendAnd();
	 
	 sc = new SearchCondition( new ClassAttribute(WTGroup.class, WTGroup.NAME), SearchCondition.EQUAL, new ConstantExpression(name));
	 qs.appendWhere( sc , new int[] {groupIndex});
	 
	 
	 QueryResult qr = PersistenceHelper.manager.find( qs );
	 if( qr.hasMoreElements() ){
		 Object [] o = ( Object[] ) qr.nextElement();
		 group = (WTGroup) o[0];
	 }
	
	return group;
	
}

private boolean existACL_YN(AdminDomainRef doaminref, String authObject, WTGroup group, boolean exclusion) throws Exception {
	
	AuthorityObjectType objType = AuthorityObjectType.toAuthorityObjectType(authObject);
	
	boolean existACL = false;
	
	String targetObject = "WCTYPE|" + objType.getDisplay();
	
	WTPrincipalReference prinRef = WTPrincipalReference.newWTPrincipalReference(group);
	
	AccessControlRule rule = AccessControlHelper.manager.getAccessControlRule(doaminref, targetObject, null, prinRef, exclusion);
	
	if(rule!=null) existACL = true;
	
	return existACL;
}

%>

