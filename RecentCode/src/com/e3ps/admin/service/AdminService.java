package com.e3ps.admin.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.e3ps.admin.MasterACLWTUserLink;
import com.e3ps.org.bean.PeopleData;
import com.e3ps.project.EProject;

import wt.admin.AdminDomainRef;
import wt.admin.AdministrativeDomain;
import wt.enterprise.RevisionControlled;
import wt.method.RemoteInterface;
import wt.org.WTGroup;

@RemoteInterface
public interface AdminService {
	
	public abstract void saveDocCodeAction(Map<String, Object> reqMap) throws Exception;
	public abstract void saveDocCodeAction2(Map<String, Object> reqMap) throws Exception;
	public abstract void saveDocCodeToValueAction(Map<String, Object> reqMap, String docTypeOid) throws Exception;
	public abstract Map<String, Object> reassignObjectAction(Map<String, Object> reqMap) throws Exception;
	public abstract Map<String, Object> changeStateAction(Map<String, Object> reqMap) throws Exception;
	public abstract void saveDocValueDefinitionAction(Map<String, Object> reqMap) throws Exception;
	public abstract void createLoginHistory(HttpServletRequest request);
	public abstract MasterACLWTUserLink addMasterAclAction(Map<String, Object> reqMap) throws Exception;
	public abstract void deleteMasterAclAction(Map<String, Object> reqMap) throws Exception;
	public abstract void setAuthToUser(Map<String, Object> reqMap) throws Exception;
	public abstract void setAuthToObject(RevisionControlled per, EProject project) throws Exception;
	public abstract void createAuthorityGroup(Map<String, Object> reqMap) throws Exception;
	
	
	public void setLicenseUser(Map<String, Object> reqMap) throws Exception;
	public void delLicenseUser(Map<String, Object> reqMap) throws Exception;
	public abstract WTGroup createWTGroup(String groupName, String desc) throws Exception ;
	public abstract void deleteAuthorityGroup(Map<String, Object> reqMap) throws Exception;
	public abstract void editGroupUser(Map<String, Object> reqMap) throws Exception;
	void createEsolutionMenu(Map<String, Object> reqMap) throws Exception;
	AdminDomainRef createDomain(String domainName, String description) throws Exception;
	void deleteDomain(AdminDomainRef domainRef) throws Exception;
	void editACL(AdminDomainRef doaminref, String authObject, WTGroup group, String auth, String Type,
			boolean exclusion) throws Exception;
	boolean existACL(AdminDomainRef doaminref, String authObject, WTGroup group, boolean exclusion) throws Exception;
	boolean downloadMultiLangAction(HttpServletRequest request, HttpServletResponse response) throws Exception;
	boolean uploadMultiLangAction(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	
	public List<WTGroup> searchGetGroup(String name, List<WTGroup> list);
	
	public List<String> getLicenseGroupName();
	
	public boolean userIsDisabledAction(Map<String, Object> reqMap) throws Exception;
	void deleteEsolutionMenu(Map<String, Object> reqMap) throws Exception;

}
