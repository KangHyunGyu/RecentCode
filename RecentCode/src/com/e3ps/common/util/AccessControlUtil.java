package com.e3ps.common.util;

import com.e3ps.change.EChangeOrder2;
import com.e3ps.change.EChangeRequest2;
import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.distribute.DistributeDocument;
import com.e3ps.distribute.DistributeRegistration;
import com.e3ps.doc.E3PSDocument;
import com.e3ps.project.EProject;

import wt.access.AccessControlHelper;
import wt.access.AccessPermission;
import wt.admin.AdminDomainRef;
import wt.admin.AdministrativeDomain;
import wt.admin.AdministrativeDomainHelper;
import wt.epm.EPMDocument;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.folder.IteratedFolderMemberLink;
import wt.folder.SubFolder;
import wt.inf.container.WTContainerHelper;
import wt.inf.container.WTContainerRef;
import wt.org.WTPrincipal;
import wt.part.WTPart;
import wt.query.ClassAttribute;
import wt.query.ConstantExpression;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.query.SubSelectExpression;
import wt.session.SessionHelper;
import wt.util.WTAttributeNameIfc;

public class AccessControlUtil {
	
	private AccessControlUtil() {}
	
	public static boolean checkPermissionForObject(Class class1) throws Exception {
		
		WTPrincipal sessionUser = SessionHelper.getPrincipal();
		String orgName = ConfigImpl.getInstance().getString("org.context.name");
		String productName = ConfigImpl.getInstance().getString("product.context.name");
		String targetObject = "WCTYPE|" + class1.getName();
		WTContainerRef container_ref = WTContainerHelper.service.getByPath("/wt.inf.container.OrgContainer="+orgName+"/wt.pdmlink.PDMLinkProduct="+productName);
		AdministrativeDomain domain = AdministrativeDomainHelper.manager.getDomain("/Default", container_ref);
    	AdminDomainRef doaminref = AdminDomainRef.newAdminDomainRef(domain);
    	
		return AccessControlHelper.manager.hasAccess(sessionUser, targetObject, doaminref, null, AccessPermission.READ);
	}
	
	public static boolean checkPermissionForOid(String oid) throws Exception {
		
		boolean isPermission = false;
		String className = oid.substring(0, oid.lastIndexOf(":"));
		Long docIda2a2 = Long.parseLong(oid.substring(oid.lastIndexOf(":") + 1));
		
		WTPrincipal sessionUser = SessionHelper.getPrincipal();
		String orgName = ConfigImpl.getInstance().getString("org.context.name");
		String productName = ConfigImpl.getInstance().getString("product.context.name");
		String targetObject = "WCTYPE|" + className;
		WTContainerRef container_ref = WTContainerHelper.service.getByPath("/wt.inf.container.OrgContainer="+orgName+"/wt.pdmlink.PDMLinkProduct="+productName);
		AdministrativeDomain domain = AdministrativeDomainHelper.manager.getDomain("/Default", container_ref);
    	AdminDomainRef doaminref = AdminDomainRef.newAdminDomainRef(domain);
    	
    	boolean objPermission = AccessControlHelper.manager.hasAccess(sessionUser, targetObject, doaminref, null, AccessPermission.READ);
    	
    	if(className.contains("E3PSDocument") && docIda2a2!=null) {
    		SubFolder docFolder = getDocFolder(docIda2a2);
    		doaminref = docFolder.getDomainRef();
    		boolean folderPermission = AccessControlHelper.manager.hasAccess(sessionUser, targetObject, doaminref, null, AccessPermission.READ);
    		isPermission = objPermission && folderPermission;
    	}else {
    		isPermission = objPermission;
    	}
    	
		return isPermission;
	}
	
	public static boolean checkPermissionForObject(Class class1, WTPrincipal user) throws Exception {
		
		String orgName = ConfigImpl.getInstance().getString("org.context.name");
		String productName = ConfigImpl.getInstance().getString("product.context.name");
		String targetObject = "WCTYPE|" + class1.getName();
		WTContainerRef container_ref = WTContainerHelper.service.getByPath("/wt.inf.container.OrgContainer="+orgName+"/wt.pdmlink.PDMLinkProduct="+productName);
		AdministrativeDomain domain = AdministrativeDomainHelper.manager.getDomain("/Default", container_ref);
    	AdminDomainRef doaminref = AdminDomainRef.newAdminDomainRef(domain);
    	
		return AccessControlHelper.manager.hasAccess(user, targetObject, doaminref, null, AccessPermission.READ);
	}
	
	public static boolean checkPermissionForObject(String oid, WTPrincipal user) throws Exception {
		
		boolean isPermission = false;
		String className = oid.substring(0, oid.lastIndexOf(":"));
		Long docIda2a2 = Long.parseLong(oid.substring(oid.lastIndexOf(":") + 1));
		
		String orgName = ConfigImpl.getInstance().getString("org.context.name");
		String productName = ConfigImpl.getInstance().getString("product.context.name");
		String targetObject = "WCTYPE|" + className;
		WTContainerRef container_ref = WTContainerHelper.service.getByPath("/wt.inf.container.OrgContainer="+orgName+"/wt.pdmlink.PDMLinkProduct="+productName);
		AdministrativeDomain domain = AdministrativeDomainHelper.manager.getDomain("/Default", container_ref);
    	AdminDomainRef doaminref = AdminDomainRef.newAdminDomainRef(domain);
    	
    	boolean objPermission = AccessControlHelper.manager.hasAccess(user, targetObject, doaminref, null, AccessPermission.READ);
    	
    	if(className.contains("E3PSDocument") && docIda2a2!=null) {
    		SubFolder docFolder = getDocFolder(docIda2a2);
    		doaminref = docFolder.getDomainRef();
    		boolean folderPermission = AccessControlHelper.manager.hasAccess(user, targetObject, doaminref, null, AccessPermission.READ);
    		isPermission = objPermission && folderPermission;
    	}else {
    		isPermission = objPermission;
    	}
    	
		return isPermission;
	}
	
	public static boolean checkPermissionForObject(String className) throws Exception {
		String targetObject = "WCTYPE|";
		switch (className) {
		case "doc":
			targetObject += E3PSDocument.class.getName();
			break;
		case "part":
			targetObject += WTPart.class.getName();
			break;
		case "epm":
			targetObject += EPMDocument.class.getName();
			break;
		case "ecr":
			targetObject += EChangeRequest2.class.getName();
			break;
		case "eco":
			targetObject += EChangeOrder2.class.getName();
			break;
		case "project":
			targetObject += EProject.class.getName();
			break;
		case "distdoc":
			targetObject += DistributeDocument.class.getName();
			break;
		case "distreg":
			targetObject += DistributeRegistration.class.getName();
			break;	
		default:
			targetObject += E3PSDocument.class.getName();
			break;
		}
		
		
		WTPrincipal sessionUser = SessionHelper.getPrincipal();
		String orgName = ConfigImpl.getInstance().getString("org.context.name");
		String productName = ConfigImpl.getInstance().getString("product.context.name");
		WTContainerRef container_ref = WTContainerHelper.service.getByPath("/wt.inf.container.OrgContainer="+orgName+"/wt.pdmlink.PDMLinkProduct="+productName);
		AdministrativeDomain domain = AdministrativeDomainHelper.manager.getDomain("/Default", container_ref);
    	AdminDomainRef doaminref = AdminDomainRef.newAdminDomainRef(domain);
    	
		return AccessControlHelper.manager.hasAccess(sessionUser, targetObject, doaminref, null, AccessPermission.READ);
	}
	
	
	public static SubFolder getDocFolder(Long docIda2a2) throws Exception {
		QuerySpec qs = new QuerySpec();
		int idx = qs.addClassList(SubFolder.class, true);
		qs.setAdvancedQueryEnabled(true);
		SearchCondition sc = null;

		QuerySpec subQs = new QuerySpec();
		subQs.setAdvancedQueryEnabled(true);
		int sub_idx = subQs.addClassList(IteratedFolderMemberLink.class, false);
		int sub_docIdx = subQs.addClassList(E3PSDocument.class, false);
		subQs.appendSelect(new ClassAttribute(IteratedFolderMemberLink.class, WTAttributeNameIfc.ROLEA_OBJECT_ID), false);
		sc = new SearchCondition(IteratedFolderMemberLink.class, "roleBObjectRef.key.branchId", E3PSDocument.class, "iterationInfo.branchId");
		subQs.appendWhere(sc, new int[] {sub_idx, sub_docIdx});
		subQs.appendAnd();
		sc = new SearchCondition(new ClassAttribute(E3PSDocument.class, WTAttributeNameIfc.ID_NAME), SearchCondition.EQUAL, new ConstantExpression(docIda2a2));
		subQs.appendWhere(sc, new int[] {sub_docIdx});

		sc = new SearchCondition(new ClassAttribute(SubFolder.class, WTAttributeNameIfc.ID_NAME), SearchCondition.EQUAL, new SubSelectExpression(subQs));
		qs.appendWhere(sc, new int[] {idx});
		QueryResult qr = PersistenceHelper.manager.find(qs);

		if(qr.hasMoreElements()){
			Object[] o = (Object[])qr.nextElement();
			SubFolder folder = (SubFolder) o[0];
			return folder;
		}
		
		return null;
	}
	
}
