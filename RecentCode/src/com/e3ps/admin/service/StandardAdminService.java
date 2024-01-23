package com.e3ps.admin.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;

import com.e3ps.admin.AuthorityGroup;
import com.e3ps.admin.AuthorityGroupType;
import com.e3ps.admin.AuthorityObjectType;
import com.e3ps.admin.EsolutionMenu;
import com.e3ps.admin.MasterACLWTUserLink;
import com.e3ps.admin.util.AdminUtil;
import com.e3ps.admin.util.ObjectKey;
import com.e3ps.common.content.FileRequest;
import com.e3ps.common.history.service.HistoryHelper;
import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.message.PLMMsgConfigJSON;
import com.e3ps.common.message.PLMMsgConfigJSON.LANG;
import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.message.util.MultiLangUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.FolderUtil;
import com.e3ps.common.util.JExcelUtil;
import com.e3ps.common.util.LoginAuthUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.common.web.ParamUtil;
import com.e3ps.doc.DocCodeToValueDefinitionLink;
import com.e3ps.doc.DocCodeType;
import com.e3ps.doc.DocValueDefinition;
import com.e3ps.doc.service.DocHelper;
import com.e3ps.org.People;
import com.e3ps.project.EProject;
import com.e3ps.project.beans.ProjectRoleData;
import com.e3ps.project.service.ProjectMemberHelper;

import wt.access.AccessControlHelper;
import wt.access.AccessControlRule;
import wt.access.AccessPermission;
import wt.access.AccessPermissionSet;
import wt.admin.AdminDomainRef;
import wt.admin.AdministrativeDomain;
import wt.admin.AdministrativeDomainHelper;
import wt.enterprise.Master;
import wt.enterprise.RevisionControlled;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.collections.WTArrayList;
import wt.fc.collections.WTCollection;
import wt.fc.collections.WTHashSet;
import wt.fc.collections.WTSet;
import wt.folder.Folder;
import wt.folder.FolderHelper;
import wt.inf.container.ExchangeContainer;
import wt.inf.container.OrgContainer;
import wt.inf.container.WTContained;
import wt.inf.container.WTContainerHelper;
import wt.inf.container.WTContainerRef;
import wt.lifecycle.LifeCycleHelper;
import wt.lifecycle.LifeCycleManaged;
import wt.lifecycle.LifeCycleTemplate;
import wt.lifecycle.State;
import wt.org.DirectoryContextProvider;
import wt.org.LicenseGroups;
import wt.org.OrganizationServicesHelper;
import wt.org.WTGroup;
import wt.org.WTOrganization;
import wt.org.WTPrincipal;
import wt.org.WTPrincipalReference;
import wt.org.WTUser;
import wt.pom.Transaction;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.StandardManager;
import wt.util.WTException;
import wt.util.WTProperties;

@SuppressWarnings("serial")
public class StandardAdminService extends StandardManager implements AdminService {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.ADMIN.getName());
	
	public static StandardAdminService newStandardAdminService() throws Exception {
		final StandardAdminService instance = new StandardAdminService();
		instance.initialize();
		return instance;
	}

	//문서 코드 타입 수정 및 추가 사항 저장
	@SuppressWarnings("unchecked")
	@Override
	public void saveDocCodeAction(Map<String, Object> reqMap) throws Exception {
		
		Transaction trx = new Transaction();
		
		try {
			trx.start();
			
			List<Map<String, Object>> addedItemList = (List<Map<String, Object>>) reqMap.get("addedItemList");
			List<Map<String, Object>> editedItemList = (List<Map<String, Object>>) reqMap.get("editedItemList");
			
			//추가된 리스트
			for(Map<String, Object> addedItem : addedItemList) {
				
				String code = StringUtil.checkNull((String) addedItem.get("code"));
				String name = StringUtil.checkNull((String) addedItem.get("name"));
				String engName = StringUtil.checkNull((String) addedItem.get("engName"));

				int sort = 0;
				
				if(addedItem.get("sort") instanceof Integer) {
					sort  = (int)addedItem.get("sort");
				}
				
				if(addedItem.get("sort") instanceof String) {
					sort = ParamUtil.getInt(addedItem, "sort");
				}
				
				boolean disabled = !((boolean) addedItem.get("active"));
				
				if(code.length() > 0 && name.length() > 0) {
					DocCodeType dCode = AdminHelper.manager.getDocCodeType(code);
					
					if(dCode == null) {
						dCode = DocCodeType.newDocCodeType();
						
						dCode.setCode(code);
						dCode.setName(name);
						dCode.setEngName(engName);
						dCode.setSort(sort);
						dCode.setDisabled(disabled);
						
						Folder folder = createFolder(name);
						
						dCode.setFolder(folder);
						
						PersistenceHelper.manager.save(dCode);
					}
				}
			}
			
			//수정된 리스트
			for(Map<String, Object> editedItem : editedItemList) {
				
				String code = StringUtil.checkNull((String) editedItem.get("code"));
				String name = StringUtil.checkNull((String) editedItem.get("name"));
				String engName = StringUtil.checkNull((String) editedItem.get("engName"));
				
				int sort = 0;
				
				if(editedItem.get("sort") instanceof Integer) {
					sort  = (int)editedItem.get("sort");
				}
				
				if(editedItem.get("sort") instanceof String) {
					sort = ParamUtil.getInt(editedItem, "sort");
				}
				
				boolean disabled = !((boolean) editedItem.get("active"));
				
				if(code.length() > 0 && name.length() > 0) {
					DocCodeType dCode = AdminHelper.manager.getDocCodeType(code);
					
					dCode.setCode(code);
					dCode.setName(name);
					dCode.setEngName(engName);
					dCode.setSort(sort);
					dCode.setDisabled(disabled);
					
					PersistenceHelper.manager.save(dCode);
				}
			}
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}		
	}
	
	//문서 코드 타입 수정 및 추가 사항 저장
	@SuppressWarnings("unchecked")
	@Override
	public void saveDocCodeAction2(Map<String, Object> reqMap) throws Exception {
		
		Transaction trx = new Transaction();
		
		try {
			trx.start();
			
			List<Map<String, Object>> editedItemList = (List<Map<String, Object>>) reqMap.get("editedItemList");
			
			//추가된 리스트
			for(Map<String, Object> editedItem : editedItemList) {
				
				String code = StringUtil.checkNull((String) editedItem.get("code"));
				String name = StringUtil.checkNull((String) editedItem.get("name"));
				String engName = StringUtil.checkNull((String) editedItem.get("engName"));
				
				int sort = 0;
				
				if(editedItem.get("sort") instanceof Integer) {
					sort  = (int)editedItem.get("sort");
				}
				
				if(editedItem.get("sort") instanceof String) {
					sort = ParamUtil.getInt(editedItem, "sort");
				}
				
				/* boolean disabled = !((boolean) editedItem.get("active")); */
				String disabledFlag = String.valueOf(editedItem.get("active"));
				boolean disabled = true;
				//LOGGER.info(disabledFlag);
				if(disabledFlag.equals("0")) {
					disabled = true;
				}else if(disabledFlag.equals("1")) {
					disabled = false;
				}else {
					disabled = !((boolean) editedItem.get("active"));
				}
				
				if(code.length() > 0 && name.length() > 0) {
					DocCodeType dCode = AdminHelper.manager.getDocCodeType(code);
					
					if(dCode == null) {
						dCode = DocCodeType.newDocCodeType();
						
						dCode.setCode(code);
						dCode.setName(name);
						dCode.setEngName(engName);
						dCode.setSort(sort);
						dCode.setDisabled(disabled);
						
						Folder folder = createFolder(name);
						
						dCode.setFolder(folder);
						
						PersistenceHelper.manager.save(dCode);
					}else {
						dCode.setCode(code);
						dCode.setName(name);
						dCode.setEngName(engName);
						dCode.setSort(sort);
						dCode.setDisabled(disabled);
						
						PersistenceHelper.manager.save(dCode);
					}
				}
			}
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}		
	}
	
	//문서 코드 타입 수정 및 추가 사항 저장 Action
	@SuppressWarnings("unchecked")
	@Override
	public void saveDocCodeToValueAction(Map<String, Object> reqMap, String docTypeOid) throws Exception {
		
		Transaction trx = new Transaction();
		
		try {
			trx.start();
			
			List<Map<String, Object>> gridList = (List<Map<String, Object>>) reqMap.get("gridList");
			
			DocCodeType docCode = null;
			if (docTypeOid.length() > 0) {
				docCode = (DocCodeType) CommonUtil.getObject(docTypeOid);
				if("ROOT".equals(docCode.getCode())) {
					docCode = null;
				}
			}
			
			//기존 링크 삭제
			List<DocCodeToValueDefinitionLink> linkList = AdminHelper.manager.getDocValueDefinitionLink(docCode);
			for(DocCodeToValueDefinitionLink link : linkList){
				PersistenceHelper.manager.delete(link);
			}
			
			//리스트
			for(Map<String, Object> gridItem : gridList) {
				String code = StringUtil.checkNull((String) gridItem.get("code"));
				String name = StringUtil.checkNull((String) gridItem.get("name"));
				String inputType = StringUtil.checkNull((String) gridItem.get("inputType"));
				
				String disabledFlag = String.valueOf(gridItem.get("active"));
				boolean disabled = true;
				//LOGGER.info(disabledFlag);
				if(disabledFlag.equals("0")) {
					disabled = true;
				}else if(disabledFlag.equals("1")) {
					disabled = false;
				}else {
					disabled = !((boolean) gridItem.get("active"));
				}
				
				if(code.length() > 0 && name.length() > 0 && inputType.length() > 0) {
					// DocCodeType 가져오기
					DocCodeType docCodeType = (DocCodeType)DocHelper.manager.getDoc(DocCodeType.class, docCode.getCode());
					
					// DocValueDefinition 가져오기
					DocValueDefinition definition = (DocValueDefinition) DocHelper.manager.getDoc(DocValueDefinition.class, code);
					DocCodeToValueDefinitionLink link = null;
					if(docCodeType != null && definition != null){
						link = DocHelper.manager.getDocCodeToValueDefinitionLink(docCodeType, definition);
						if(link != null) continue;	// link가 이미 존재하면 다음 행으로 넘어감
						link = DocCodeToValueDefinitionLink.newDocCodeToNumberCodeLink();
						link.setDocCode(docCodeType);
						link.setValueDefiniton(definition);
						link.setDisabled(disabled);
						PersistenceHelper.manager.save(link);
					}
				}
			}
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}		
	}
	
	@Override
	public void saveDocValueDefinitionAction(Map<String, Object> reqMap) throws Exception {
		
		Transaction trx = new Transaction();
		
		try {
			trx.start();
			
			List<Map<String, Object>> gridList = (List<Map<String, Object>>) reqMap.get("gridList");
			
			//리스트
			for(Map<String, Object> gridItem : gridList) {
				String code = StringUtil.checkNull((String) gridItem.get("code"));
				String name = StringUtil.checkNull((String) gridItem.get("name"));
				String inputType = StringUtil.checkNull((String) gridItem.get("inputType"));
				
				if(code.length() > 0 && name.length() > 0 && inputType.length() > 0) {
					DocValueDefinition definition = (DocValueDefinition) DocHelper.manager.getDoc(DocValueDefinition.class, code);
					
					if(definition == null){
						definition = DocValueDefinition.newDocValueDefinition();
						
						definition.setCode(code);
						definition.setName(name);
						definition.setInputType(inputType);
						if("SELECT".equals(inputType)) {
							definition.setNumberCodeType(code);
						}
						
						PersistenceHelper.manager.save(definition);
					}
				}
			}
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}		
	}
	
	/**
	 * 
	 * @desc	: DocCode로 이름으로 Folder 생성
	 * @author	: tsuam
	 * @date	: 2019. 9. 10.
	 * @return	: Folder
	 * @param name
	 * @return
	 * @throws Exception
	 */
	private Folder createFolder(String name) throws Exception{
		
		String rootLocation ="/Default/Document";
		WTContainerRef wtContainerRef = WCUtil.getWTContainerRef();
		
		Folder folder = FolderHelper.service.getFolder(rootLocation, wtContainerRef);
		
		if(folder == null) {
			folder = FolderUtil.createFolder(rootLocation);
		}
		
		Folder subFolder = FolderUtil.createFolder(folder, name);
		
		return subFolder ;
	}
	
	@Override
	public Map<String,Object> reassignObjectAction(Map<String, Object> reqMap) throws Exception {
		
		Map<String,Object> result = new HashMap<String,Object>();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String lifecycle = StringUtil.checkNull((String) reqMap.get("lifecycle"));
		
		Object obj = CommonUtil.getObject(oid);
		
		WTContained contained = (WTContained)obj;
		
		LifeCycleTemplate lct = LifeCycleHelper.service.getLifeCycleTemplate(lifecycle, contained.getContainerReference());
		LifeCycleHelper.service.reassign((LifeCycleManaged)obj, lct.getLifeCycleTemplateReference(),contained.getContainerReference());
		
		result.put("lifecycle", lifecycle);
		result.put("message", "Reassign 완료");
		
		return result;
	}
	
	@Override
	public Map<String,Object> changeStateAction(Map<String, Object> reqMap) throws Exception {
		
		Map<String,Object> result = new HashMap<String,Object>();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String state = StringUtil.checkNull((String) reqMap.get("state"));
		String terminate = StringUtil.checkNull((String) reqMap.get("terminate"));
		
		Object obj = CommonUtil.getObject(oid);
		
		LifeCycleManaged lcm = (LifeCycleManaged)obj;
		
		lcm = LifeCycleHelper.service.setLifeCycleState(lcm, State.toState(state), "true".equals(terminate.toLowerCase()));
		
		result.put("state", State.toState(state).toString());
		result.put("message", "상태 변경완료.");
		
		return result;
	}
	
	@Override
	public void createLoginHistory(HttpServletRequest request) {
		
		boolean check = LoginAuthUtil.checkLoginAuth(request);
		
		if(check) {
			String id = (String) request.getAttribute("j_username");
			String browser = CommonUtil.getBrowser(request);
			String ip = CommonUtil.getClientIP(request);
			
			HistoryHelper.service.createLoginHistory(id, browser, ip);
		}
	}
	
	@Override
	public MasterACLWTUserLink addMasterAclAction(Map<String, Object> reqMap) throws Exception {
		MasterACLWTUserLink acl = null;
		
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			List<Map<String, Object>> checkItemList = (List<Map<String, Object>>) reqMap.get("checkItemList");
			String perOid = StringUtil.checkNull((String) reqMap.get("perOid"));
			
			RevisionControlled per = (RevisionControlled) CommonUtil.getObject(perOid);
			Master master = (Master) per.getMaster();
			
			for(Map<String, Object> chkMap : checkItemList) {
				String wtUserOid = StringUtil.checkNull((String) chkMap.get("wtuserOID"));
				WTUser user = (WTUser) CommonUtil.getObject(wtUserOid);
				
				boolean isExist = AdminHelper.manager.isExist(user, master);
				if(!isExist) {
					MasterACLWTUserLink link = MasterACLWTUserLink.newMasterACLWTUserLink(user, master);
					PersistenceHelper.manager.save(link);
				}
				
			}
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw e;
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		
		return acl;
	}

	@Override
	public void deleteMasterAclAction(Map<String, Object> reqMap) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			List<Map<String, Object>> checkItemList = (List<Map<String, Object>>) reqMap.get("checkItemList");
			
			for(Map<String, Object> chkMap : checkItemList) {
				String oid = StringUtil.checkNull((String) chkMap.get("oid"));
				MasterACLWTUserLink link = (MasterACLWTUserLink) CommonUtil.getObject(oid);
				PersistenceHelper.manager.delete(link);
			}
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw e;
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
	}


	@Override
	public void setAuthToUser(Map<String, Object> reqMap) throws Exception {
		Transaction trx = new Transaction();
		
		try {
			trx.start();
			List<Map<String, Object>> userList = (List<Map<String, Object>>) reqMap.get("userList");
			
			//리스트
			for(Map<String, Object> user : userList) {
				Object chief = user.get("chief");
				if(chief instanceof Integer) {
					String oid = StringUtil.checkNull((String) user.get("oid"));
					People people = (People) CommonUtil.getObject(oid);
					int i = ((Integer) chief).intValue();
					boolean c = false;
					if(i == 1) 
						c = true;
					
					people.setChief(c);
					PersistenceHelper.manager.modify(people);
				}
				
			}
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		
	}

	@Override
	public void setAuthToObject(RevisionControlled per, EProject project) throws Exception {
		Transaction trx = new Transaction();
		WTUser user = null;
		Master master = null;
		MasterACLWTUserLink link = null;
		
		try {
			trx.start();
					
			//작성자 권한
			user = (WTUser) per.getCreator().getObject();
			master = (Master) per.getMaster();
			
			boolean isExist = AdminHelper.manager.isExist(user, master);
			if(!isExist) {
				link = MasterACLWTUserLink.newMasterACLWTUserLink(user, master);
				PersistenceHelper.manager.save(link);
			}
			
			
			//관리자 리스트 권한
//			List<PeopleData> list = AdminUtil.getAuthUser();
//			for(PeopleData data : list) {
//				user = (WTUser) CommonUtil.getObject(data.getWtuserOID());
//				isExist = AdminHelper.manager.isExist(user, master);
//				if(!isExist) {
//					link = MasterACLWTUserLink.newMasterACLWTUserLink(user, master);
//					PersistenceHelper.manager.save(link);
//				}
//			}
			
			//프로젝트 멤버 권한
			if(project != null) {
				List<ProjectRoleData> members = ProjectMemberHelper.manager.getRoleUserList(project);
				for(ProjectRoleData data : members) {
					user = (WTUser) CommonUtil.getObject(data.getUserOid());
					if(user != null) {
						isExist = AdminHelper.manager.isExist(user, master);
						if(!isExist) {
							link = MasterACLWTUserLink.newMasterACLWTUserLink(user, master);
							PersistenceHelper.manager.save(link);
						}
					}
					
				}
			}
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		
	}
	
	
	
	
	/**
	 * @param peoOid
	 * @param list
	 * @throws WTException
	 */
	public void setLicenseGroupUser(String peoOid, List<WTGroup> list) throws WTException{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			People pp = (People)CommonUtil.getObject(peoOid);
			for(int i=0; i<list.size(); i++) {
				WTGroup group = (WTGroup)list.get(i);
				group.addMember(pp.getUser());
			}
		}catch(Exception e) {
			map.put("result", false);
			e.printStackTrace();
		}
		return ;
		
	}
	
	@Override
	public List<String> getLicenseGroupName(){
		String serverType = ConfigImpl.getInstance().getString("server.type");
		
		List<String> gNameList = new ArrayList<String>();
		gNameList.add(LicenseGroups.PTC_WINDCHILL_ADVANCED_LICENSE.name);
		gNameList.add(LicenseGroups.PTC_CONTRIBUTOR_LICENSE.name);
		gNameList.add(LicenseGroups.SEARCH_VIEW_PRINT.name);
		
		if("plm".equals(serverType)) {
			gNameList.add(LicenseGroups.PTC_MECHANICAL_DESIGN_I_LICENSE.name);
		}else{
			gNameList.add(LicenseGroups.PTC_PDMLINK_MODULE_LICENSE.name);
		}
		return gNameList;
	}
	
	/**
	 * @param name
	 * @param list
	 * @return
	 */
	@Override
	public List<WTGroup> searchGetGroup(String name, List<WTGroup> list){
		try {
			DirectoryContextProvider dcp = OrganizationServicesHelper.manager.newDirectoryContextProvider((String[]) null, (String[]) null);
			WTGroup advancedGroup = OrganizationServicesHelper.manager.getGroup(name, dcp);
			if(advancedGroup != null) {
				list.add(advancedGroup);
			}
		} catch (WTException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	
	/**
	 * worldex
	 */
	@Override
	public void setLicenseUser(Map<String, Object> reqMap) {
		try {
			String serverType = ConfigImpl.getInstance().getString("server.type");
			
			String type = StringUtil.checkNull((String) reqMap.get("type"));
			String peoOid = StringUtil.checkNull((String) reqMap.get("peoOid"));
			
			List<WTGroup> list = new ArrayList<WTGroup>();
			
			String name = "";
			if("ptc_wnc_advanced_named".equals(type)) {
				name = LicenseGroups.PTC_WINDCHILL_ADVANCED_LICENSE.name;
				list = searchGetGroup(name, list);
				
				if("plm".equals(serverType)) {
					name = LicenseGroups.PTC_MECHANICAL_DESIGN_I_LICENSE.name;
					list = searchGetGroup(name, list);
				}else {
					name = LicenseGroups.PTC_PDMLINK_MODULE_LICENSE.name;
					list = searchGetGroup(name, list);
				}
			}else if("ptc_wnc_navigate_contribute_n".equals(type)) {
				name = LicenseGroups.PTC_CONTRIBUTOR_LICENSE.name;
				list = searchGetGroup(name, list);
			}else if("ptc_wnc_navigate_view_named".equals(type)) {
				name = LicenseGroups.SEARCH_VIEW_PRINT.name;
				list = searchGetGroup(name, list);
			}
			
			if(!peoOid.isEmpty()) {
				setLicenseGroupUser(peoOid, list);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	
	/**
	 * @param peoOid
	 * @param list
	 * @throws WTException
	 */
	public void delLicenseGroupUser(String peoOid, List<WTGroup> list) throws WTException{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			People pp = (People)CommonUtil.getObject(peoOid);
			for(int i=0; i<list.size(); i++) {
				WTGroup group = (WTGroup)list.get(i);
				group.removeMember(pp.getUser());
			}
		}catch(Exception e) {
			map.put("result", false);
			e.printStackTrace();
		}
		return ;
	}
	
	
	/**
	 * worldex
	 */
	@Override
	public void delLicenseUser(Map<String, Object> reqMap)throws Exception {
		String serverType = ConfigImpl.getInstance().getString("server.type");
		
		List<WTGroup> list = new ArrayList<WTGroup>();
		
		String type = StringUtil.checkNull((String) reqMap.get("type"));
		String peoOid = StringUtil.checkNull((String) reqMap.get("peoOid"));
		
		String name = "";
		if("ptc_wnc_advanced_named".equals(type)) {
			name = LicenseGroups.PTC_WINDCHILL_ADVANCED_LICENSE.name;
			list = searchGetGroup(name, list);
			
			if("plm".equals(serverType)) {
				name = LicenseGroups.PTC_MECHANICAL_DESIGN_I_LICENSE.name;
				list = searchGetGroup(name, list);
			}else {
				name = LicenseGroups.PTC_PDMLINK_MODULE_LICENSE.name;
				list = searchGetGroup(name, list);
			}
		}else if("ptc_wnc_navigate_contribute_n".equals(type)) {
			name = LicenseGroups.PTC_CONTRIBUTOR_LICENSE.name;
			list = searchGetGroup(name, list);
		}else if("ptc_wnc_navigate_view_named".equals(type)) {
			name = LicenseGroups.SEARCH_VIEW_PRINT.name;
			list = searchGetGroup(name, list);
		}
		
		if(!peoOid.isEmpty()) {
			delLicenseGroupUser(peoOid, list);
		}
		return;
	}

	@Override
	public void createAuthorityGroup(Map<String, Object> reqMap) throws Exception {
		Transaction trx = new Transaction();
		
		String name = StringUtil.checkNull((String) reqMap.get("name"));
		String codeType = StringUtil.checkNull((String) reqMap.get("codeType"));
		String auth = StringUtil.checkNull((String) reqMap.get("auth"));
		String authObject = StringUtil.checkNull((String) reqMap.get("authObject"));
		String domainPath = StringUtil.checkNull((String) reqMap.get("domainPath"));
		String description = StringUtil.checkNull((String) reqMap.get("description"));
		
		try {
			trx.start();
			
			WTGroup group = createWTGroup(authObject + "_" + auth, name);
			
			AdministrativeDomain domain = WCUtil.getAdministrativeDomain(domainPath);
			
			AdminDomainRef doaminref = AdminDomainRef.newAdminDomainRef(domain);
			
			editACL(doaminref, authObject, group, auth, "create", false);
			
			AuthorityGroup ag = AuthorityGroup.newAuthorityGroup();
			ag.setName(name);
			ag.setGroup(group);
			ag.setAuth(auth);
			ag.setAuthObjectType(authObject);
			ag.setDomainPath(domainPath);
			ag.setDomain(domain);
			ag.setCodeType(AuthorityGroupType.toAuthorityGroupType(codeType));
			ag.setDescription(description);
			
			PersistenceHelper.manager.save(ag);
			
			trx.commit();
			trx = null;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}	
		
	}
	
	@Override
	public void editACL(AdminDomainRef doaminref, String authObject, WTGroup group, String auth, String Type, boolean exclusion) throws Exception {
		AuthorityObjectType objType = AuthorityObjectType.toAuthorityObjectType(authObject);
		
		String targetObject = "WCTYPE|" + objType.getDisplay();
		
		WTPrincipalReference prinRef = WTPrincipalReference.newWTPrincipalReference(group);
		
		AccessPermissionSet grant = new AccessPermissionSet();
		AccessPermissionSet deny = new AccessPermissionSet();
		AccessPermissionSet absolutDeny = new AccessPermissionSet();
		
		if("READ".equals(auth)) {
			//읽기, 다운로드 허용
			grant.add(AccessPermission.READ);
			grant.add(AccessPermission.DOWNLOAD);
			grant.add(AccessPermission.SET_STATE);
			
			//나머지 거부
			deny.add(AccessPermission.MODIFY);
			deny.add(AccessPermission.MODIFY_SECURITY_LABELS);
			deny.add(AccessPermission.CREATE_BY_MOVE);
			deny.add(AccessPermission.CREATE);
			deny.add(AccessPermission.CHANGE_DOMAIN);
			deny.add(AccessPermission.CHANGE_PERMISSIONS);
			deny.add(AccessPermission.DELETE);
			deny.add(AccessPermission.ADMINISTRATIVE);
			deny.add(AccessPermission.CHANGE_CONTEXT);
			deny.add(AccessPermission.MODIFY_CONTENT);
			deny.add(AccessPermission.NEW_VIEW_VERSION);
			deny.add(AccessPermission.REVISE);
			deny.add(AccessPermission.MODIFY_IDENTITY);
			
		}else if("PERMISSION".equals(auth)) {
			grant.add(AccessPermission.ALL);
		}else if("DENY".equals(auth)) {
			deny.add(AccessPermission.ALL);
		}else if("ABSOLUTEDENY".equals(auth)) {	
			absolutDeny.add(AccessPermission.ALL);
		}	
		
		
		if("create".equals(Type)) {
			AccessControlHelper.manager.createAccessControlRule(doaminref, targetObject, null, prinRef, exclusion, grant, deny, absolutDeny);
		}else if("update".equals(Type)) {
			AccessControlHelper.manager.updateAccessControlRule(doaminref, targetObject, null, prinRef, exclusion, grant, deny, absolutDeny);
		}else if("delete".equals(Type)) {
			AccessControlHelper.manager.deleteAccessControlRule(doaminref, targetObject, null, prinRef, exclusion);
		}
	}
	
	@Override
	public boolean existACL(AdminDomainRef doaminref, String authObject, WTGroup group, boolean exclusion) throws Exception {
		
		AuthorityObjectType objType = AuthorityObjectType.toAuthorityObjectType(authObject);
		
		boolean existACL = false;
		
		String targetObject = "WCTYPE|" + objType.getDisplay();
		
		WTPrincipalReference prinRef = WTPrincipalReference.newWTPrincipalReference(group);
		
		AccessControlRule rule = AccessControlHelper.manager.getAccessControlRule(doaminref, targetObject, null, prinRef, exclusion);
		
		if(rule!=null) existACL = true;
		
		return existACL;
	}
	
		
	
	@Override
	public WTGroup createWTGroup(String groupName, String desc) throws Exception {
		
		WTGroup checkGroup = WCUtil.getGroup(groupName);
		if(checkGroup != null) throw new Exception( "[" + groupName + "] 이미 생성된 그룹입니다.");
		
		WTGroup group = null;
		String targetOrgName = ConfigImpl.getInstance().getString("org.context.name");
		
		Enumeration orgs = OrganizationServicesHelper.manager.findLikeOrganizations(WTOrganization.NAME, targetOrgName, ((ExchangeContainer) WTContainerHelper.getExchangeRef().getContainer()).getContextProvider());
		WTOrganization targetOrg = null;
		if (orgs.hasMoreElements()){
			targetOrg = (WTOrganization) orgs.nextElement();
			OrgContainer org = WTContainerHelper.service.getOrgContainer(targetOrg);
			DirectoryContextProvider dcp = WTContainerHelper.service.getPublicContextProvider(org,WTGroup.class);
			group = WTGroup.newWTGroup(groupName, dcp);
			group.setContainer(org);
			if(desc.length() > 0) group.setDescription(desc);
			OrganizationServicesHelper.manager.createPrincipal(group);
		}
		
		return group;
		
	}
	
	@Override
	public void deleteAuthorityGroup(Map<String, Object> reqMap) throws Exception {
		
		List<String> oids = (List<String>) reqMap.get("oids");
		
		WTSet linkList = new WTHashSet();
		WTCollection groupList = new WTArrayList();
		
		for(String oid : oids ) {
			AuthorityGroup ag = (AuthorityGroup) CommonUtil.getObject(oid);
			linkList.add(ag);
			groupList.add(ag.getGroup());
		}
		
		PersistenceHelper.manager.delete(linkList);
		OrganizationServicesHelper.manager.deleteGroups(groupList);
		
	}
	
	/**
	 * 
	  * @desc : 그룹내 유저 추가 삭제 로직
	  * @author : shjeong
	  * @date : 2023. 11. 14.
	  * @method : editGroupUser
	  * @param Map<String, Object> reqMap
	  * @return
	  * @throws Exception WTContainer
	 */
	@Override
	public void editGroupUser(Map<String, Object> reqMap) throws Exception {
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		boolean edit = (boolean) reqMap.get("edit");
		List<String> addUserList = (List<String>) reqMap.get("addUserList");
		if(addUserList==null) addUserList = new ArrayList<String>();
		List<String> addGroupList = (List<String>) reqMap.get("addGroupList");
		if(addGroupList==null) addGroupList = new ArrayList<String>();
		int totalLength = addUserList.size() + addGroupList.size();
		WTGroup group = (WTGroup)CommonUtil.getObject(oid);
		
		WTPrincipal[] principalsInGroup = new WTPrincipal[totalLength];
		for(int i = 0;  i < addUserList.size(); i ++ ) {
			People p = (People)CommonUtil.getObject(addUserList.get(i));
			WTPrincipal principal = (WTPrincipal) p.getUser();
			if(principal != null) {
				principalsInGroup[i] = principal;
			}
		}
		
		for(int i = 0;  i < addGroupList.size(); i ++ ) {
			WTGroup g = (WTGroup)CommonUtil.getObject(addGroupList.get(i));
			WTPrincipal principal = (WTPrincipal) g;
			if(principal != null) {
				principalsInGroup[i] = principal;
			}
		}
		
		if (principalsInGroup.length > 0) {
			if(edit) {
				OrganizationServicesHelper.manager.addMembers(group, principalsInGroup);
			}else {
				OrganizationServicesHelper.manager.removeMembers(group, principalsInGroup);
			}
		}
		
	}
	
	@Override
	public void createEsolutionMenu(Map<String, Object> reqMap) throws Exception{
		
		List<Map<String,Object>> editRowItems = (List<Map<String,Object>>)reqMap.get("editRowItems");
		List<Map<String,Object>> addRowItems = (List<Map<String,Object>>)reqMap.get("addRowItems");
		WTCollection menuList = new WTArrayList();
		
		for(Map<String,Object> editRowItem : editRowItems ) {
			int sort =(int)editRowItem.get("sort");
			int menuLevel = (int)editRowItem.get("menuLevel");
			boolean isDisabled = (boolean)editRowItem.get("disabled");
			
			EsolutionMenu emCode = (EsolutionMenu) CommonUtil.getObject((String)editRowItem.get("oid"));
			emCode.setCode(StringUtil.checkNull((String)editRowItem.get("code")));
        	emCode.setName(StringUtil.checkNull((String)editRowItem.get("name")));
        	emCode.setName_en(StringUtil.checkNull((String)editRowItem.get("name_en")));
        	emCode.setHref(StringUtil.checkNull((String)editRowItem.get("href")));
        	emCode.setImgsrc(StringUtil.checkNull((String)editRowItem.get("imgsrc")));
        	emCode.setSort(sort);
        	emCode.setMenuLevel(menuLevel);
        	emCode.setDisabled(isDisabled);
        	menuList.add(emCode);
		}
		
		for(Map<String,Object> addRowItem : addRowItems ) {
			int sort =(int)addRowItem.get("sort");
			int menuLevel = (int)addRowItem.get("menuLevel");
			boolean isDisabled = (boolean)addRowItem.get("disabled");
			
			EsolutionMenu emCode  = EsolutionMenu.newEsolutionMenu();
			EsolutionMenu parentMenu = partParentCode(StringUtil.checkNull((String)addRowItem.get("parentCode")));
        	emCode.setCode(StringUtil.checkNull((String)addRowItem.get("code")));
        	emCode.setName(StringUtil.checkNull((String)addRowItem.get("name")));
        	emCode.setName_en(StringUtil.checkNull((String)addRowItem.get("name_en")));
        	emCode.setHref(StringUtil.checkNull((String)addRowItem.get("href")));
        	emCode.setImgsrc(StringUtil.checkNull((String)addRowItem.get("imgsrc")));
        	emCode.setSort(sort);
        	emCode.setMenuLevel(menuLevel);
        	emCode.setParent(parentMenu);
        	if(parentMenu!=null)emCode.setAlias(parentMenu.getAlias());
        	WTGroup group = AdminHelper.service.createWTGroup(StringUtil.checkNull((String)addRowItem.get("code")), StringUtil.checkNull((String)addRowItem.get("name")));
        	emCode.setGroup(group);
        	emCode.setDisabled(isDisabled);
        	menuList.add(emCode);
		}
		
		PersistenceHelper.manager.save(menuList);
		
	}
	
	@Override
	public void deleteEsolutionMenu(Map<String, Object> reqMap) throws Exception{
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		EsolutionMenu eMenu = (EsolutionMenu)CommonUtil.getObject(oid);
		WTGroup group = eMenu.getGroup();
		PersistenceHelper.manager.delete(eMenu);
		OrganizationServicesHelper.manager.delete(group);
		
	}
	
	
	public EsolutionMenu partParentCode(String pCodeID) throws Exception{
		if( pCodeID.length() == 0) return null;
		
		QuerySpec select = new QuerySpec(EsolutionMenu.class);
		select.appendWhere(new SearchCondition(EsolutionMenu.class, EsolutionMenu.CODE, "=", pCodeID), new int[] { 0 });
		QueryResult result = PersistenceHelper.manager.find(select);
		
		if (result.hasMoreElements())
		{
			EsolutionMenu pCode = (EsolutionMenu)result.nextElement();
			return pCode;
		}
	    return null;
    }
	
	/**
	 * 
	  * @desc : 도메인 생성 로직 / 문서 폴더 정책
	  * @author : shjeong
	  * @date : 2023. 11. 14.
	  * @method : createDomain
	  * @param String domainName, String description
	  * @return
	  * @throws Exception WTContainer
	 */
	@Override	
	public AdminDomainRef createDomain(String domainName, String description) throws Exception {
		AdminDomainRef result = null;
		String orgName = ConfigImpl.getInstance().getString("org.context.name");
		String productName = ConfigImpl.getInstance().getString("product.context.name");
		String parentDomainPath = "/Default/DOCFOLDER";
		
		WTContainerRef container_ref = WTContainerHelper.service.getByPath("/wt.inf.container.OrgContainer="+orgName+"/wt.pdmlink.PDMLinkProduct="+productName);
		
		//DOCFOLDER 도메인 없을 시 생성
		AdministrativeDomain domain = AdministrativeDomainHelper.manager.getDomain(parentDomainPath, container_ref);
		if(domain==null) {
			AdministrativeDomain rootDomain = AdministrativeDomainHelper.manager.getDomain("/Default", container_ref);
			AdminDomainRef doaminref = AdminDomainRef.newAdminDomainRef(rootDomain);
			doaminref = AdministrativeDomainHelper.manager.createDomain(doaminref, "DOCFOLDER", "DOCFOLDER", container_ref);
			domain = (AdministrativeDomain) doaminref.getObject();
		}
		
		// /Default/DOCFOLDER 하위에 도메인 생성
		AdministrativeDomain checkDomain = AdministrativeDomainHelper.manager.getDomain(parentDomainPath + "/" + domainName, container_ref);
		if(checkDomain == null) {
			AdminDomainRef doaminref = AdminDomainRef.newAdminDomainRef(domain);
			result = AdministrativeDomainHelper.manager.createDomain(doaminref, domainName, description, container_ref);
		}
		
		return result;
	}
	
	/**
	 * 
	  * @desc : 도메인 제거 로직
	  * @author : shjeong
	  * @date : 2023. 11. 14.
	  * @method : deleteDomain
	  * @param AdminDomainRef domainRef
	  * @return
	  * @throws Exception WTContainer
	 */
	@Override	
	public void deleteDomain(AdminDomainRef domainRef) throws Exception {
		AdministrativeDomainHelper.manager.delete(domainRef);
	}
	
	@Override
	public boolean downloadMultiLangAction(HttpServletRequest request, HttpServletResponse response) throws Exception {

		boolean result = false;
		XSSFWorkbook workbook = null;
		try {
			String resultFileName = "Template_Lang_" + DateUtil.getCurrentDateString("d") + ".xlsx";
			resultFileName = URLEncoder.encode(resultFileName, "UTF-8");
			workbook = writeTemplateLang();

			// 엑셀 출력
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + resultFileName);

			workbook.write(response.getOutputStream());
			workbook.close();
			result = true;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {
				if (workbook != null) {
					workbook.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception(e.getMessage());
			}
		}

		return result;
	}
	
	private XSSFWorkbook writeTemplateLang() throws Exception {
		XSSFWorkbook workbook = null;
		int SHEET_NUM = 0;
		FileInputStream file = null;

		try {
			// Load DATA
			
			PLMMsgConfigJSON msg = PLMMsgConfigJSON.getInstance().newInstance();
			JSONObject ko = msg.getKO();
			JSONObject en = msg.getEN();

			Set<String> keys = ko.keySet();
			for (String key : keys) {
				String newVal = (String) en.get(key);
				ko.put(key, newVal);
			}

			// Load Excel
//	          String wt_home = WTProperties.getServerProperties().getProperty("wt.home");
//
//	          StringBuffer sb = new StringBuffer();
//	          sb.append(wt_home);
//	          sb.append(File.separator);
//	          sb.append("codebase");
//	          sb.append(File.separator);
//	          sb.append("com");
//	          sb.append(File.separator);
//	          sb.append("e3ps");
//	          sb.append(File.separator);
//	          sb.append(properties_filename);
			
			StringBuffer filePath = new StringBuffer();
			String wtHome = WTProperties.getServerProperties().getProperty("wt.home");
			filePath.append(wtHome);
			filePath.append(File.separator);
			filePath.append("codebase");
			filePath.append(File.separator);
			filePath.append("jsp");
			filePath.append(File.separator);
			filePath.append("admin");
			filePath.append(File.separator);
			filePath.append("plm_template_lang.xlsx");
			
			File orgFile = new File(filePath.toString());
			String fileName = orgFile.getName();
			String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
			file = new FileInputStream(orgFile);

			// Start Write
			// log.error("excel ext=="+ext);
			if (!"xlsx".equalsIgnoreCase(ext)) {
				if (file != null) {
					file.close();
				}
				throw new Exception(MultiLangUtil.getMessage("업로드할 파일의 확장자는 XLSX이어야 합니다."));
			}

			workbook = new XSSFWorkbook(file);

			if (workbook.getSheetAt(SHEET_NUM) == null) {
				if (file != null) {
					file.close();
				}
				if (workbook != null) {
					workbook.close();
				}
				throw new Exception(MultiLangUtil.getMessage("데이터 문제 : 읽을 시트가 없습니다."));
			}
			XSSFSheet sheet = workbook.getSheetAt(SHEET_NUM);

			// Write Data - row, col (0부터 시작)
			XSSFRow sourceRow = null;
			XSSFCell cell = null;
			CellStyle cs = null;
			
			XSSFCellStyle style = workbook.createCellStyle();
			style.setBorderBottom(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			
			int row = 2; // 3번째 줄부터 시작

			for (String key : keys) {
				String value = (String) ko.get(key);

				sourceRow = sheet.getRow(row);
				if (sourceRow == null) {
					sourceRow = sheet.createRow(row);
				}
				// key
				cell = sourceRow.getCell(1);
				if(cell==null){
					cell = sourceRow.createCell(1);
					cell.setCellStyle(style);
				}
				cell.setCellValue(key);

				// value
				cell = sourceRow.getCell(2);
				if(cell==null){
					cell = sourceRow.createCell(2);
					cell.setCellStyle(style);
				}
				cell.setCellValue(value);
				// log.error(row + ". " + key + " : " + value);

				// save cs
				cs = cell.getCellStyle();
				cell.setCellStyle(cs);
				row++;
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {
				if (file != null) {
					file.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new Exception(e.getMessage());
			}
		}

		return workbook;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean uploadMultiLangAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		boolean result = false;
		
		try {
			
			FileRequest fileReq = new FileRequest(request);
			String fileLoc = fileReq.getFileLocation("uploadTemplate");
			
			
//			//기존 파일 삭제
//			String pathKor = SRMMsgConfigJSON.PATH_KO;
//			String pathEnglish = SRMMsgConfigJSON.PATH_EN;
//			SRMFileUtil.fileDelete(pathKor);
//			SRMFileUtil.fileDelete(pathEnglish);
			String ko_path = PLMMsgConfigJSON.PATH_KO;
			String en_path = PLMMsgConfigJSON.PATH_EN;
			AdminUtil.fileRename(ko_path, "");
			AdminUtil.fileRename(en_path, "");
			
			
			//load
			JSONObject rootJson = loadMessageFile(fileLoc);
			JSONObject KO = new JSONObject();
			JSONObject EN = new JSONObject();
			
			Set<String> keys = rootJson.keySet();
			for(String key : keys) {
				
				String value = StringUtil.checkNull(rootJson.get(key).toString());
				
				KO.put(key, key);
				if(value.length() > 0 && !"\"\"".equals(value)){
					EN.put(key, value);
				}
			}
			
			//기존 파일 새로 저장
			result = saveMessageFile(KO, ko_path, "ko") && saveMessageFile(EN, en_path, "en");
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject loadMessageFile(String filePath) throws Exception {
		
		FileInputStream file = null;
		XSSFWorkbook workbook = null;
		JSONObject json = null;
		try {
			filePath = StringUtil.checkNull(filePath);
			
			if(!(filePath.length() > 0)) {
				throw new Exception("The upload template is required");
			}
			
			
			File orgFile = new File(filePath);
			String fileName = orgFile.getName();
			String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
			file = new FileInputStream(orgFile);
			
			if(!"xlsx".equalsIgnoreCase(ext)) {
				if(file != null) {
					file.close();
				}
				throw new Exception(MessageUtil.getMessage("지원하는 형식이 아닙니다."));
			}
			
			//init
			workbook = new XSSFWorkbook(file);
			json = new JSONObject();
			
			//Sheet
			int FINALSHEETNUM = 1;
			for(int sheetNum=0; sheetNum<FINALSHEETNUM; sheetNum++) {
				if(workbook.getSheetAt(sheetNum) == null) {
					if(file != null) {
						file.close();
					}
					if(workbook != null) {
						workbook.close();
					}
					throw new Exception("workbook.getSheetAt(" + sheetNum + ") == null");
				}
				XSSFSheet sheet = workbook.getSheetAt(sheetNum);
				//Row - 3번째 줄부터 시작
				for(int row=2; row<sheet.getPhysicalNumberOfRows(); row++) {
					XSSFRow sourceRow = sheet.getRow(row);
					
					if(sourceRow == null){
						continue;
					}
					
					XSSFCell checkCell = sourceRow.getCell(1);
					String checkStr = AdminUtil.getExcelData(workbook, sheet, checkCell);
					if (StringUtil.isNullEmpty(checkStr)) {
						break;
					}
					
					//Column
					String key = "";
					String value = "";
					for (int col = 0; col < AdminUtil.getSheetColumnFORXLSX(sheet); col++) {
						//Cell
						XSSFCell cell = sourceRow.getCell(col);
						
						//get Data
						String dataStr = AdminUtil.getExcelData(workbook, sheet, cell);
						switch(col) {
						//key : 한글명
						case 1:
							key = dataStr;
							break;
						//value : 영문명
						case 2:
							value = dataStr;
							break;
						}
					}//-- for col
					
					//add
//					if(!StringUtil.isNullEmpty(value)) {
						json.put(key, value);
//					}
					//log.error(row + ". " + key + " : " + value);
					
				}//-- for row
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}finally {
			try {
				if(file != null) {
					file.close();
				}
				if(workbook != null) {
					workbook.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception(e.getMessage());
			}
		}
		
		return json;
	}
	
	private boolean saveMessageFile(JSONObject json, String pathLang, String lang) throws Exception {
		boolean result = false;
		Writer writer = null;
		try {
			//encoding : UTF-8
			//log.error("pathEnglish = "+pathLang);
			writer = new FileWriterWithEncoding(pathLang, "UTF-8");
			
			String writeStr = json.toJSONString();
			//writeStr = writeStr.replaceAll("\\{", "\\{\n");
			//writeStr = writeStr.replaceAll("\",", "\",\n");
			//writeStr = writeStr.replaceAll("\\}", "\n\\}");
			//log.error(writeStr);
			writer.write(writeStr);
			writer.flush();
			
			//message new init
			PLMMsgConfigJSON msg = PLMMsgConfigJSON.getInstance();
			if(LANG.ko.toString().equals(lang) || LANG.ko_KR.toString().equals(lang)) {
				msg.setKO(json);
			}else {
				msg.setEN(json);
			}
			
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}finally {
			try {
				if(writer != null) {
					writer.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception(e.getMessage());
			}
		}
		
		return result;
	}

	
	
	/**
	 * admin managed user isDisable set and license del
	 * 2023-12-07
	 * cklee
	 * return boolean
	 */
	@Override
	public boolean userIsDisabledAction(Map<String, Object> reqMap) throws Exception {
		boolean returnV = false;
		try {
			boolean checkedValue = (boolean)reqMap.get("checkValue");
			String pOid = StringUtil.checkNull((String)reqMap.get("pOid"));
			if(pOid.length() > 0) {
				People peo = (People)CommonUtil.getObject(pOid);
				peo.setIsDisable(checkedValue);
				PersistenceHelper.manager.modify(peo);
				//System.out.println("modifyEND");
				
				List<WTGroup> gNameList = AdminHelper.manager.getLicenseGroupNameWTGroup();
				delLicenseGroupUser(pOid, gNameList);
				//System.out.println("LicenseDel Success");
			}
			returnV = true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return returnV;
	}
}
