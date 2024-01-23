package com.e3ps.common.bean;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;

import wt.access.AccessControlHelper;
import wt.access.AccessPermission;
import wt.admin.AdminDomainRef;
import wt.admin.AdministrativeDomain;
import wt.admin.AdministrativeDomainHelper;
import wt.doc.WTDocument;
import wt.fc.PersistenceHelper;
import wt.fc.WTObject;
import wt.inf.container.WTContainerHelper;
import wt.inf.container.WTContainerRef;
import wt.org.WTPrincipal;
import wt.session.SessionHelper;

public class AccessControlData {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
	private String oid; // oid
	private boolean isModify;
	private boolean isDelete;
	private boolean isRevise;
	private boolean isDownload;
	private boolean isRead;
	private boolean isApprovalObject; // 결재 객체 View 여부 체크
	
	public AccessControlData() throws Exception{
		
	} 
	
	public AccessControlData(String oid) throws Exception { 
		
		WTObject obj = (WTObject) CommonUtil.getObject(oid);
		
		_AccessControlData(obj);
	}
	
	public AccessControlData(WTObject obj) throws Exception { 
		
		_AccessControlData(obj);
	}
	
	public void _AccessControlData(WTObject obj) throws Exception { 
		
		this.oid = CommonUtil.getOIDString(obj);
		WTPrincipal user = SessionHelper.getPrincipal();
		//AccessControlHelper.manager.hasAccess(user, obj, AccessPermission.CREATE);
		isModify = AccessControlHelper.manager.hasAccess(user, obj, AccessPermission.MODIFY);
		isDelete = AccessControlHelper.manager.hasAccess(user, obj, AccessPermission.DELETE);
		isRevise = AccessControlHelper.manager.hasAccess(user, obj, AccessPermission.REVISE);
		isDownload = AccessControlHelper.manager.hasAccess(user, obj, AccessPermission.DOWNLOAD);
		isRead = AccessControlHelper.manager.hasAccess(user, obj, AccessPermission.READ);
		
		WTContainerRef container_ref = WTContainerHelper.service.getByPath("/wt.inf.container.OrgContainer=worldex/wt.pdmlink.PDMLinkProduct=worldex");
		AdministrativeDomain domain = AdministrativeDomainHelper.manager.getDomain("/Default", container_ref);
    	AdminDomainRef doaminref = AdminDomainRef.newAdminDomainRef(domain);
		
		boolean chk = AccessControlHelper.manager.hasAccess(user, "WCTYPE|"+WTDocument.class.getName(), doaminref, null, AccessPermission.READ);
		
		/* 
		System.out.println(user.getName() + " => "
				+ "isModify[" + isModify 
				+ "], isDelete["+ isDelete 
				+ "], isRevise["+ isRevise
				+ "], isDownolad["+ isDownload
				+ "], isRead["+ isRead+"]");
				
		*/

	} 

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public boolean isModify() {
		return isModify;
	}

	public void setModify(boolean isModify) {
		this.isModify = isModify;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public boolean isRevise() {
		return isRevise;
	}

	public void setRevise(boolean isRevise) {
		this.isRevise = isRevise;
	}

	public boolean isDownload() {
		return isDownload;
	}

	public void setDownload(boolean isDownload) {
		this.isDownload = isDownload;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public boolean isApprovalObject() {
		return isApprovalObject;
	}

	public void setApprovalObject(boolean isApprovalObject) {
		this.isApprovalObject = isApprovalObject;
	}

	
	
}
