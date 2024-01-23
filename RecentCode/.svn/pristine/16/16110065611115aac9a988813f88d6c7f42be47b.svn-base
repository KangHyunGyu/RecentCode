package com.e3ps.admin.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.e3ps.admin.AuthorityGroup;
import com.e3ps.admin.AuthorityGroupType;
import com.e3ps.admin.AuthorityObjectType;
import com.e3ps.admin.EsolutionMenu;
import com.e3ps.admin.FolderAuthGroup;
import com.e3ps.admin.MasterACLWTUserLink;
import com.e3ps.admin.ObjectAuthGroup;
import com.e3ps.admin.bean.AuthorityGroupData;
import com.e3ps.admin.bean.EsolutionMenuData;
import com.e3ps.admin.bean.MasterAclWTUserData;
import com.e3ps.admin.bean.ObjectAuthGroupData;
import com.e3ps.admin.util.AuthKey;
import com.e3ps.admin.util.ObjectKey;
import com.e3ps.change.EChangeActivityDefinition;
import com.e3ps.change.EChangeActivityDefinitionRoot;
import com.e3ps.change.beans.EChangeActivityDefinitionData;
import com.e3ps.change.service.ChangeECOHelper;
import com.e3ps.common.code.NumberCode;
import com.e3ps.common.code.bean.NumberCodeData;
import com.e3ps.common.code.service.CodeHelper;
import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.common.web.PageQueryBroker;
import com.e3ps.doc.DocCodeToValueDefinitionLink;
import com.e3ps.doc.DocCodeType;
import com.e3ps.doc.DocKey;
import com.e3ps.doc.DocValueDefinition;
import com.e3ps.doc.E3PSDocument;
import com.e3ps.doc.bean.DocCodeToValueDefinitionLinkData;
import com.e3ps.doc.bean.DocCodeTypeData;
import com.e3ps.doc.bean.DocValueDefinitionData;
import com.e3ps.org.Department;
import com.e3ps.org.People;
import com.e3ps.org.WTUserPeopleLink;
import com.e3ps.org.bean.PeopleData;
import com.e3ps.org.service.PeopleHelper;

import wt.admin.AdministrativeDomain;
import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.enterprise.Master;
import wt.enterprise.RevisionControlled;
import wt.epm.EPMDocument;
import wt.fc.PagingQueryResult;
import wt.fc.PagingSessionHelper;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.folder.SubFolder;
import wt.introspection.ColumnDescriptor;
import wt.introspection.WTIntrospector;
import wt.lifecycle.LifeCycleManaged;
import wt.lifecycle.LifeCycleTemplate;
import wt.org.DirectoryContextProvider;
import wt.org.LicenseGroups;
import wt.org.OrganizationServicesHelper;
import wt.org.WTGroup;
import wt.org.WTPrincipal;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.query.SubSelectExpression;
import wt.services.ServiceFactory;
import wt.session.SessionHelper;
import wt.util.WTAttributeNameIfc;

public class AdminHelper {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.ADMIN.getName());
	public static final AdminService service = ServiceFactory.getService(AdminService.class);
	public static final AdminHelper manager = new AdminHelper();
	
	private static final String BASIC_DOMAIN_PATH = "/Default";
	/**
	 * @desc	: 문서 코드 타입 가져오기
	 * @author	: tsjeong
	 * @date	: 2019. 9. 11.
	 * @method	: getDocCodeList
	 * @return	: List<DocCodeTypeData>
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	public List<DocCodeTypeData> getDocCodeList(Map<String, Object> reqMap) throws Exception {
		List<DocCodeTypeData> list = new ArrayList<DocCodeTypeData>();
		
		QuerySpec qs = getDocCodeListQuery(reqMap);
		
		if(qs != null) {
			QueryResult qr = PersistenceHelper.manager.find(qs);
			
			while(qr.hasMoreElements()) {
				Object[] o = (Object[])qr.nextElement();
				DocCodeType code = (DocCodeType)o[0];
				DocCodeTypeData data = new DocCodeTypeData(code);
				list.add(data);
			}
		}
		
		return list;
	}

	/**
	 * 
	 * @desc	: 설계변경 활동 단계
	 * @author	: tsjeong
	 * @date	: 2020. 9. 14.
	 * @method	: getActivityStep
	 * @return	: List<ApprovalLineData>
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	public List<EChangeActivityDefinitionData> getActivityStep(Map<String, Object> reqMap) throws Exception {
		
		String rootOid = StringUtil.checkNull((String) reqMap.get("rootOid"));
		List<EChangeActivityDefinition> definitions = null;
		EChangeActivityDefinitionRoot root = null;
		List<EChangeActivityDefinitionData> list = new ArrayList<EChangeActivityDefinitionData>();
		if(rootOid!=null && rootOid.length()>0){
			root = (EChangeActivityDefinitionRoot)CommonUtil.getObject(rootOid);
			definitions = ChangeECOHelper.service.getActiveDefinition(root.getPersistInfo().getObjectIdentifier().getId());
		}
		
		if (definitions != null) {
			Map<String, List<EChangeActivityDefinition>> activityDefStepMap = new HashMap<String, List<EChangeActivityDefinition>>();
			for (EChangeActivityDefinition def : definitions) {
				List<EChangeActivityDefinition> slist = activityDefStepMap.get(def.getStep());
				if (slist == null) {
					slist = new ArrayList<EChangeActivityDefinition>();
				} 
				slist.add(def);
				activityDefStepMap.put(def.getStep(), slist);
			}
			QueryResult qr = CodeHelper.service.getCodeNum("EOSTEP");
			if(qr !=null){
				while(qr.hasMoreElements()){
					NumberCode nc = (NumberCode)qr.nextElement();
					List<EChangeActivityDefinition> slist = activityDefStepMap.get(nc.getCode());
					if (slist != null) {
						boolean rowspan = true;
						for (EChangeActivityDefinition def : slist) {
							EChangeActivityDefinitionData data = new EChangeActivityDefinitionData(def, slist.size());
							list.add(data);
						}
					} 
				}
			}
		}
		
		return list;
	}
	
	/**
	 * @desc	: 문서 코드 타입 쿼리 실행
	 * @author	: tsjeong
	 * @date	: 2019. 9. 11.
	 * @method	: getDocCodeListQuery
	 * @return	: QuerySpec
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	public QuerySpec getDocCodeListQuery(Map<String, Object> reqMap) throws Exception {
		
		String code = StringUtil.checkNull((String) reqMap.get("code"));
		String name = StringUtil.checkNull((String) reqMap.get("name"));
		
		boolean disabledCheck = false;
		if(reqMap.get("disabledCheck") != null) {
			disabledCheck = (boolean) reqMap.get("disabledCheck");
		}
		
		QuerySpec qs = null;
		
		qs = new QuerySpec();
		
		int idx = qs.addClassList(DocCodeType.class, true);
			
		if(disabledCheck) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			qs.appendWhere(new SearchCondition(DocCodeType.class, "disabled", SearchCondition.IS_FALSE), new int[] { idx });	
		}
			
		if(code.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			qs.appendWhere(new SearchCondition(DocCodeType.class, DocCodeType.CODE, SearchCondition.LIKE, "%" + code + "%", false), new int[] { idx });
		}
		
		if(name.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			qs.appendWhere(new SearchCondition(DocCodeType.class, DocCodeType.NAME, SearchCondition.LIKE, "%" + name + "%", false), new int[] { idx });
		}
		
		qs.appendOrderBy(new OrderBy(new ClassAttribute(DocCodeType.class, DocCodeType.SORT), false), new int[] { idx });
		
		qs.appendOrderBy(new OrderBy(new ClassAttribute(DocCodeType.class, DocCodeType.NAME), false), new int[] { idx });
		
		return qs;
	}
	
	/**
	 * @desc	: DocCodeType 가져오기
	 * @author	: tsjeong
	 * @date	: 2019. 9. 25.
	 * @method	: getDocCodeType
	 * @return	: DocCodeType
	 * @param code
	 * @return
	 */
	public DocCodeType getDocCodeType(String code){
		try{
			QuerySpec query = new QuerySpec(DocCodeType.class);

			query.appendWhere(new SearchCondition(DocCodeType.class, "code", "=", code), new int[] { 0 });

			query.appendOrderBy(new OrderBy(new ClassAttribute(DocCodeType.class,DocCodeType.CODE),false),new int[]{0});
			QueryResult qr = PersistenceHelper.manager.find(query);

			if(qr.hasMoreElements()){
				DocCodeType dct = (DocCodeType)qr.nextElement();
				return dct;
			}
			return  null;
		}
		catch(Exception ex){
			return null;
		}
	}
	
	/**
	 * @desc	: 문서 속성 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 4. 28.
	 * @method	: getSearchDocValueListAction
	 * @return	: List<DocValueDefinitionData>
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	public List<DocValueDefinitionData> getSearchDocValueListAction(Map<String, Object> reqMap) throws Exception {
		
		List<DocValueDefinitionData> list = new ArrayList<DocValueDefinitionData>();
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<DocValueDefinitionData> docValueList = getDocValueList();
		for(DocValueDefinitionData data : docValueList) {
			map.put(data.getCode(), data);
		}
		
		List<String> docColumnList = getDocColumnList();
		for(String columnName : docColumnList) {
			DocValueDefinitionData data = new DocValueDefinitionData();
			if(map.containsKey(columnName)) {
				data = (DocValueDefinitionData) map.get(columnName);
			} else {
				data.setCode(columnName);
			}
			list.add(data);
		}
		
		return list;
	}
	
	public List<DocValueDefinitionData> getDocValueList() throws Exception {
		
		List<DocValueDefinitionData> list = new ArrayList<DocValueDefinitionData>();
		
		QuerySpec qs = getDocValueListQuery();
		
		if(qs != null) {
			QueryResult qr = PersistenceHelper.manager.find(qs);
			
			while(qr.hasMoreElements()) {
				Object[] o = (Object[])qr.nextElement();
				DocValueDefinition code = (DocValueDefinition)o[0];
				DocValueDefinitionData data = new DocValueDefinitionData(code);
				list.add(data);
			}
		}
		
		return list;
	}
	
	public List<String> getDocColumnList() throws Exception{
		
		
		List<String> columnList = new ArrayList<>();
		
		ColumnDescriptor[] descriptors = WTIntrospector.getDatabaseInfo("com.e3ps.doc.E3PSDocument").getBaseTableInfo().getColumnDescriptors();
		for (int nIndex = 0 ; nIndex < descriptors.length ; nIndex++) {
			String sColName = descriptors[nIndex].getColumnName();
			columnList.add(sColName);
		}
		
		descriptors = WTIntrospector.getDatabaseInfo("wt.doc.WTDocument").getBaseTableInfo().getColumnDescriptors();
		for (int nIndex = 0 ; nIndex < descriptors.length ; nIndex++) {
			String sColName = descriptors[nIndex].getColumnName();
			columnList.remove(sColName);
		}
		
		return columnList;
	}
	/**
	 * @desc	: 문서 속성 쿼리 실행
	 * @author	: tsjeong
	 * @date	: 2019. 9. 11.
	 * @method	: getDocValueListQuery
	 * @return	: QuerySpec
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	public QuerySpec getDocValueListQuery() throws Exception {
		
		QuerySpec qs = null;
		
		qs = new QuerySpec();
		
		int idx = qs.addClassList(DocValueDefinition.class, true);
			
		qs.appendOrderBy(new OrderBy(new ClassAttribute(DocValueDefinition.class, DocValueDefinition.NAME), false), new int[] { idx });
		
		return qs;
	}
	
	/**
	 * 
	 * @desc	: 문서 타입별 속성 릳크
	 * @author	: plmadmin
	 * @date	: 2019. 9. 11.
	 * @method	: getDocValueDefinitionLink
	 * @return	: List<DocCodeToValueDefinitionLink>
	 * @param docCode
	 * @return
	 * @throws Exception
	 */
	public List<DocCodeToValueDefinitionLink> getDocValueDefinitionLink(DocCodeType docCode) throws Exception {
		List<DocCodeToValueDefinitionLink> list = new ArrayList<DocCodeToValueDefinitionLink>();
		QueryResult qr = PersistenceHelper.manager.navigate(docCode, DocCodeToValueDefinitionLink.VALUE_DEFINITON_ROLE, DocCodeToValueDefinitionLink.class,false);
		 
		 while ( qr.hasMoreElements()) {
			 DocCodeToValueDefinitionLink link = (DocCodeToValueDefinitionLink)qr.nextElement();
			 list.add(link);
	     }
		
		return list;
		
	}
	
	/**
	 * 
	 * @desc	: 문서 타입별 속성 Data;
	 * @author	: plmadmin
	 * @date	: 2019. 9. 11.
	 * @method	: getDocValueDefinition
	 * @return	: List<DocCodeToValueDefinitionLinkData>
	 * @param docCode
	 * @return
	 * @throws Exception
	 */
	public List<DocCodeToValueDefinitionLinkData> getDocValueDefinition(DocCodeType docCode) throws Exception {
		
		List<DocCodeToValueDefinitionLinkData> listData = new ArrayList<DocCodeToValueDefinitionLinkData>();
		
		List<DocCodeToValueDefinitionLink> list = getDocValueDefinitionLink(docCode);
		
		for(DocCodeToValueDefinitionLink toLink : list){
			DocCodeToValueDefinitionLinkData linkData = new DocCodeToValueDefinitionLinkData(toLink);
			listData.add(linkData);
		}
		
		return listData;
		
	}
	
	public Persistable getDoc(Class cls) {
		try {
			QuerySpec qs = new QuerySpec();
			int idx = qs.addClassList(cls, true);
			
			QueryResult qr = PersistenceHelper.manager.find(qs);
			if(qr.hasMoreElements()){
				Object[] obj = (Object[]) qr.nextElement();
				if(obj[0] instanceof DocValueDefinition){
					return (DocValueDefinition) obj[0];
				}else if(obj[0] instanceof DocCodeType){
					return (DocCodeType) obj[0];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		
		try{
			String oid = "com.e3ps.doc.DocCodeType:94106";
			DocCodeType docCode = (DocCodeType)CommonUtil.getObject(oid);
			List<DocCodeToValueDefinitionLinkData> list = AdminHelper.manager.getDocValueDefinition(docCode);
			
			for(DocCodeToValueDefinitionLinkData data : list){
				LOGGER.info("name = " + data.getName());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}

	public Map<String, Object> searchObjectAction(Map<String, Object> reqMap) throws Exception{

		Map<String, Object> result = new HashMap<>();
		
		Object obj = null;
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		
		if(oid.length() > 0) {
			obj = CommonUtil.getObject(oid);
		}
		
		if((obj == null)) {
			result.put("command", "error");
			result.put("message", "객체가 존재하지 않습니다.");
		}
		else if( (obj != null) && !(obj instanceof LifeCycleManaged) ) {
			result.put("command", "error");
			result.put("message", "wt.lifecycle.LifeCycleManaged가 아닙니다.");
		}else {
			LifeCycleManaged lcm = (LifeCycleManaged)obj;
			LifeCycleTemplate lct = (LifeCycleTemplate)lcm.getLifeCycleTemplate().getObject();
			
			List<Map<String,String>> cL = new ArrayList<Map<String,String>>();
			if(obj instanceof ContentHolder) {
				ContentHolder holder = (ContentHolder)obj;
				holder = wt.content.ContentHelper.service.getContents(holder);
				
				Vector cotents = ContentHelper.getContentListAll(holder);
				if(cotents.size() > 0) {

					wt.content.ContentItem contentItem = null;
					for(int i = 0; i < cotents.size(); i++) {
						contentItem = (wt.content.ContentItem)cotents.get(i);
						if(contentItem instanceof ApplicationData) {

							ApplicationData appData = (ApplicationData)contentItem;
							
							URL downloadURL = ContentHelper.getDownloadURL ( holder , appData );
							
							Map<String,String> cMap = new HashMap<String, String>();
							cMap.put("url", downloadURL.toString());
							cMap.put("name", appData.getFileName());
							
							cL.add(cMap);
						}
					}
				}
			}
			
			String targetOid = CommonUtil.getOIDString(lcm);
			String lifecycle = lct.getName();
			String state = lcm.getLifeCycleState().toString();
			
			result.put("targetOid", targetOid);
			result.put("lifecycle", lifecycle);
			result.put("state", state);
			result.put("content", cL);
		}
		
		return result;
	}
	
	public void tempDeleteBatchSchedule() throws IOException {
		String wtHome = wt.util.WTProperties.getLocalProperties().getProperty("wt.home", "");
		String path = wtHome+"\\temp\\e3ps";
		deleteFolder(path);
		
		File loc = new File(path);
		if (!loc.exists()) {
			loc.mkdirs();
		}
	}
	
	public void deleteFolder(String path) {
		File folder = new File(path);
	    try {
	    	if(folder.exists()){
                File[] folder_list = folder.listFiles();
				
				for (int i = 0; i < folder_list.length; i++) {
				    if(folder_list[i].isFile()) {
				    	folder_list[i].delete();
//				    	LOGGER.info(folder_list[i].getName()+" 삭제성공"); 
				    }else {
				    	deleteFolder(folder_list[i].getPath());
//				    	LOGGER.info(folder_list[i].getName()+" 삭제성공"); 
				    }
				    folder_list[i].delete();
				}
				
				folder.delete();
	       }
	   } catch (Exception e) {
		   e.getStackTrace();
	   }
	}
	
	/**
	 * 
	  * @Method Name : getSearchMasterAclList
	  * @작성일 : 2021. 5. 20.
	  * @작성자 : mjroh
	  * @Method 설명 : 권한 리스트
	  * @param dto
	  * @return
	  * @throws Exception
	 */
	public Map<String, Object> getSearchMasterAclList(Map<String, Object> reqMap) throws Exception{
		
		Map<String, Object> map = new HashMap<>();
		
		List<MasterAclWTUserData> list = new ArrayList<MasterAclWTUserData>();
		
		int start = (Integer) reqMap.get("start");
		int count = (Integer) reqMap.get("count");
		String sessionId = StringUtil.checkNull((String) reqMap.get("sessionId"));
		
		PagingQueryResult result = null;
		
		if (sessionId.length() > 0) {
			result = PagingSessionHelper.fetchPagingSession(start, count, Long.valueOf(sessionId));
		} else {
			QuerySpec query = getSearchMasterAclListQuery(reqMap);
			
			result = PageQueryBroker.openPagingSession(start, count, query, true);
		}
		
		int totalSize = result.getTotalSize();
		
		while (result.hasMoreElements()) {
			Object[] obj = (Object[]) result.nextElement();
		    MasterACLWTUserLink link = (MasterACLWTUserLink) obj[0];
		    People people = (People) obj[1];
		    MasterAclWTUserData rdto = new MasterAclWTUserData(link, people);
		    list.add(rdto);
		}
		
		map.put("list", list);
		map.put("totalSize", totalSize);
		map.put("sessionId", result.getSessionId() == 0 ? "" : result.getSessionId());
		
		//webix
		map.put("data", list);
		map.put("total_count", totalSize);
		map.put("pos", start);
		
		return map;
    }
	
	private QuerySpec getSearchMasterAclListQuery(Map<String, Object> reqMap) throws Exception{
		//소팅
		Map<String, Object> sort = reqMap.get("sort") != null ? (Map<String, Object>) reqMap.get("sort") : new HashMap<>();
		
		String perOid = StringUtil.checkNull((String) reqMap.get("perOid"));
		
		RevisionControlled rc = (RevisionControlled) CommonUtil.getObject(perOid);
		Master master = (Master) rc.getMaster();
		
		QuerySpec qs = new QuerySpec();
		int ll = qs.appendClassList(MasterACLWTUserLink.class, true);
		int ii = qs.appendClassList(People.class, true);
		int dd = qs.appendClassList(Department.class, false);
		
		if (qs.getConditionCount() > 0) {
			qs.appendAnd();
		}
		qs.appendWhere(new SearchCondition(People.class, "userReference.key.id", MasterACLWTUserLink.class,
				MasterACLWTUserLink.ROLE_AOBJECT_REF + ".key.id"), new int[] { ii, ll });
		
		if (qs.getConditionCount() > 0) {
			qs.appendAnd();
		}
		SearchCondition sc = new SearchCondition(People.class, People.DEPARTMENT_REFERENCE + ".key.id", Department.class,
				"thePersistInfo.theObjectIdentifier.id");
		sc.setOuterJoin(SearchCondition.RIGHT_OUTER_JOIN);
		qs.appendWhere(sc, new int[] { ii, dd });
		
		if (qs.getConditionCount() > 0) {
			qs.appendAnd();
		}
		qs.appendWhere(new SearchCondition(MasterACLWTUserLink.class, MasterACLWTUserLink.ROLE_BOBJECT_REF + ".key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(master)), 
				new int[] {ll});
		
		// 소팅
		if (sort.size() > 0) {
			String id = (String) sort.get("id");
			String dir = (String) sort.get("dir");
			
			String sortValue = "";
			if("userId".equals(id)) {
				sortValue = People.ID;
				qs.appendOrderBy(new OrderBy(new ClassAttribute(People.class, sortValue), "desc".equals(dir)), new int[] { ii });
				
			} else if("userName".equals(id)) {
				sortValue = People.NAME;
				qs.appendOrderBy(new OrderBy(new ClassAttribute(People.class, sortValue), "desc".equals(dir)), new int[] { ii });
				
			} else if("duty".equals(id)) {
				sortValue = People.DUTY;
				qs.appendOrderBy(new OrderBy(new ClassAttribute(People.class, sortValue), "desc".equals(dir)), new int[] { ii });
				
			} else if("deptName".equals(id)) {
				sortValue = Department.NAME;
				qs.appendOrderBy(new OrderBy(new ClassAttribute(Department.class, sortValue), "desc".equals(dir)), new int[] { dd });
			}
			
		} else {
			qs.appendOrderBy(new OrderBy(new ClassAttribute(People.class, People.SORT_NUM), false), new int[] { ii });
			qs.appendOrderBy(new OrderBy(new ClassAttribute(People.class, People.ID), false), new int[] { ii });
		}
		
		LOGGER.info(String.valueOf(qs));
		return qs;
	}
	
	/**
	 * 
	  * @Method Name : getAclUserList
	  * @작성일 : 2021. 5. 21.
	  * @작성자 : mjroh
	  * @Method 설명 : 권한 유저 리스트 출력
	  * @param reqMap
	  * @return
	  * @throws Exception
	 */
	public List<PeopleData> getAclUserList(Map<String, Object> reqMap) throws Exception {
		
		List<PeopleData> list = new ArrayList<>();
		
		Map<String, String> aclUserMap = getUserListFromACLQuery(reqMap);
		
		QueryResult result = null;
		
		QuerySpec query = PeopleHelper.manager.getUserListQuery(reqMap);
		
		result = PersistenceHelper.manager.find(query);
		
		while(result.hasMoreElements()) {
			Object[] obj = (Object[]) result.nextElement();
			PeopleData pd = new PeopleData(obj[0]);
			
			boolean isAuthSelected = StringUtil.checkString(aclUserMap.get(pd.getId()));
			pd.setAuthSelected(isAuthSelected);
			
			list.add(pd);
		}
		
		return list;
	}
	
	private Map<String, String> getUserListFromACLQuery(Map<String, Object> reqMap) throws Exception{
		
		Map<String, String> map = new HashMap<String, String>();
		
		String oid = StringUtil.checkNull((String) reqMap.get("perOid"));
		
		Persistable per = CommonUtil.getPersistable(oid);
		
		
		QuerySpec qs = new QuerySpec();
		int ll = qs.appendClassList(MasterACLWTUserLink.class, true);
		if(per instanceof E3PSDocument) {
			int ee = qs.appendClassList(E3PSDocument.class, false);
			
			qs.appendWhere(new SearchCondition(E3PSDocument.class, E3PSDocument.PERSIST_INFO + ".theObjectIdentifier.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(oid)),
					new int[] { ee });
			if(qs.getConditionCount() > 0)
				qs.appendAnd();
			qs.appendWhere(new SearchCondition(new ClassAttribute(E3PSDocument.class, E3PSDocument.MASTER_REFERENCE + ".key.id"), 
					SearchCondition.EQUAL, 
					new ClassAttribute(MasterACLWTUserLink.class, MasterACLWTUserLink.ROLE_BOBJECT_REF + ".key.id")),
					new int[] { ee, ll });
			
		}else if(per instanceof EPMDocument) {
			int ee = qs.appendClassList(EPMDocument.class, false);
			
			qs.appendWhere(new SearchCondition(EPMDocument.class, EPMDocument.PERSIST_INFO + ".theObjectIdentifier.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(oid)),
					new int[] { ee });
			if(qs.getConditionCount() > 0)
				qs.appendAnd();
			qs.appendWhere(new SearchCondition(new ClassAttribute(EPMDocument.class, EPMDocument.MASTER_REFERENCE + ".key.id"), 
					SearchCondition.EQUAL, 
					new ClassAttribute(MasterACLWTUserLink.class, MasterACLWTUserLink.ROLE_BOBJECT_REF + ".key.id")),
					new int[] { ee, ll });
			
		}else if(per instanceof WTPart) {
			int ee = qs.appendClassList(WTPart.class, false);
			
			qs.appendWhere(new SearchCondition(WTPart.class, WTPart.PERSIST_INFO + ".theObjectIdentifier.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(oid)),
					new int[] { ee });
			if(qs.getConditionCount() > 0)
				qs.appendAnd();
			qs.appendWhere(new SearchCondition(new ClassAttribute(WTPart.class, WTPart.MASTER_REFERENCE + ".key.id"), 
					SearchCondition.EQUAL, 
					new ClassAttribute(MasterACLWTUserLink.class, MasterACLWTUserLink.ROLE_BOBJECT_REF + ".key.id")),
					new int[] { ee, ll });
			
		}else {
			return map;
		}
		
		
		LOGGER.info(qs.toString());
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		while(qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			MasterACLWTUserLink link = (MasterACLWTUserLink) obj[0];
			MasterAclWTUserData acl = new MasterAclWTUserData(link);
			String userId = acl.getUserId();
			map.put(userId, userId);
		}
		
		return map;
	}
	
	/**
	 * 
	  * @Method Name : getAuthQuerySpec
	  * @작성일 : 2021. 5. 26.
	  * @작성자 : mjroh
	  * @Method 설명 : 특정 객체에 대한 권한 쿼리
	  * @param qs
	  * @param classType
	  * @param perIdx
	  * @return
	  * @throws Exception
	 */
	public QuerySpec getAuthQuerySpec(QuerySpec qs, String classType, int perIdx) throws Exception {
		if(qs == null || !StringUtil.checkString(classType)) {
			return qs;
		}
		
		boolean isAdmin = CommonUtil.isAdmin();
		
		//현재 유저
		WTUser sessionUser = (WTUser) SessionHelper.manager.getPrincipal();
		
		String peopleOid = "";
		QueryResult qr = PersistenceHelper.manager.navigate(sessionUser, "people", WTUserPeopleLink.class);
		if(qr.hasMoreElements()) {
			peopleOid = CommonUtil.getOIDString((People)qr.nextElement());
		}
		
		boolean isCustomAdmin = AdminHelper.manager.isCustomAdmin(peopleOid);
		
    	//Exist Query : 링크가 있다 > 현재 사용자(11)에 해당하는 것만 출력
    	ClassAttribute masterCls = new ClassAttribute(MasterACLWTUserLink.class, MasterACLWTUserLink.ROLE_BOBJECT_REF + ".key.id");
    	ClassAttribute perMasterCls = null;
    	if("E3PSDocument".equals(classType)) {
    		perMasterCls = new ClassAttribute(E3PSDocument.class, E3PSDocument.MASTER_REFERENCE + ".key.id");
    		
    	}else if("EPMDocument".equals(classType)) {
    		perMasterCls = new ClassAttribute(EPMDocument.class, EPMDocument.MASTER_REFERENCE + ".key.id");
    		
    	}else if("WTPart".equals(classType)) {
    		perMasterCls = new ClassAttribute(WTPart.class, WTPart.MASTER_REFERENCE + ".key.id");
    		
    	}
    	
    	QuerySpec existQuery = new QuerySpec(); 
    	existQuery.getFromClause().setAliasPrefix("B");
    	int exIdx = existQuery.appendClassList(MasterACLWTUserLink.class, false);
    	existQuery.appendSelect(masterCls, new int[] { exIdx }, true);
    	
    	//관리자는 항상 전체검색
    	if(!isAdmin && !isCustomAdmin) {
    		existQuery.appendWhere(new SearchCondition(MasterACLWTUserLink.class, MasterACLWTUserLink.ROLE_AOBJECT_REF + ".key.id",
        			SearchCondition.EQUAL, CommonUtil.getOIDLongValue(sessionUser)), new int[] {exIdx});
		}
    	
    	
    	//Not Exist Query : 링크가 없다 > 모두 출력
    	QuerySpec notExistQuery = new QuerySpec(); 
    	notExistQuery.getFromClause().setAliasPrefix("B");
    	int notExIdx = notExistQuery.appendClassList(MasterACLWTUserLink.class, false);
    	notExistQuery.appendSelect(masterCls, new int[] { notExIdx }, true);
    	
    	if (qs.getConditionCount() > 0) {
    		qs.appendAnd();
    	}
    	qs.appendOpenParen();
    	qs.appendWhere(new SearchCondition(perMasterCls, SearchCondition.IN, new SubSelectExpression(existQuery)), new int[] {perIdx});
    	qs.appendOr();
    	qs.appendWhere(new SearchCondition(perMasterCls, SearchCondition.NOT_IN, new SubSelectExpression(notExistQuery)), new int[] {perIdx});
    	qs.appendCloseParen();
    	
    	return qs;
	}
	
	public boolean isExist(WTUser user, Master master) {
		boolean result = false;
		long userOid = CommonUtil.getOIDLongValue(user);
		long masterOid = CommonUtil.getOIDLongValue(master);
		
		try {
			QuerySpec qs = new QuerySpec();
			int idx = qs.addClassList(MasterACLWTUserLink.class, true);
			
			qs.appendWhere(new SearchCondition(MasterACLWTUserLink.class, "roleAObjectRef.key.id", SearchCondition.EQUAL, userOid),
					new int[] { idx });
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(MasterACLWTUserLink.class, "roleBObjectRef.key.id", SearchCondition.EQUAL, masterOid),
					new int[] { idx });
			
			QueryResult qr = PersistenceHelper.manager.find(qs);
			if(qr.hasMoreElements()){
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public boolean isEmpty(Master master) {
		boolean result = true;
		long masterOid = CommonUtil.getOIDLongValue(master);
		
		try {
			QuerySpec qs = new QuerySpec();
			int idx = qs.addClassList(MasterACLWTUserLink.class, true);
			qs.appendWhere(new SearchCondition(MasterACLWTUserLink.class, "roleBObjectRef.key.id", SearchCondition.EQUAL, masterOid),
					new int[] { idx });
			
			QueryResult qr = PersistenceHelper.manager.find(qs);
			if(qr.hasMoreElements()){
				result = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public boolean isCustomAdmin(String oid) {
		boolean result = false;
		oid = oid.substring(oid.lastIndexOf(":") + 1);
		
		try {
			QuerySpec qs = new QuerySpec();
			int idx = qs.addClassList(People.class, true);
			qs.appendWhere(new SearchCondition(People.class, People.CHIEF, SearchCondition.IS_TRUE),
					new int[] { idx });
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(People.class, "thePersistInfo.theObjectIdentifier.id", SearchCondition.EQUAL, Long.parseLong(oid)),
					new int[] { idx });
			
			QueryResult qr = PersistenceHelper.manager.find(qs);
			if(qr.hasMoreElements()){
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public boolean isAuth(Persistable per) throws Exception {
		boolean result = true;
		
		RevisionControlled rc = (RevisionControlled)per;
		WTUser user = (WTUser) SessionHelper.manager.getPrincipal();
		Master master = (Master) rc.getMaster();
		
		String peopleOid = "";
		QueryResult qr = PersistenceHelper.manager.navigate(user, "people", WTUserPeopleLink.class);
		if(qr.hasMoreElements()) {
			peopleOid = CommonUtil.getOIDString((People)qr.nextElement());
		}
		
		boolean isAdmin = CommonUtil.isAdmin();
		boolean isCustomAdmin = AdminHelper.manager.isCustomAdmin(peopleOid);
		boolean isExist = AdminHelper.manager.isExist(user, master);
		boolean isEmpty = AdminHelper.manager.isEmpty(master);
		
		if(!isAdmin && !isExist && !isEmpty && !isCustomAdmin) {
			result = false;
		}
		
		return result;
		
	}
	
	/**
	 * @param type
	 * @return
	 * @throws Exception
	 * worldex
	 */
	
	@SuppressWarnings("unchecked")
	public List<PeopleData> getLicenseUserList(String type) throws Exception{
		
		List<PeopleData> list = new ArrayList<>();
		
		String name = "";

		if("ptc_wnc_advanced_named".equals(type)) {
			name = LicenseGroups.PTC_WINDCHILL_ADVANCED_LICENSE.name;
		}else if("ptc_wnc_navigate_contribute_n".equals(type)) {
			name = LicenseGroups.PTC_CONTRIBUTOR_LICENSE.name;
		}else if("ptc_wnc_navigate_view_named".equals(type)) {
			name = LicenseGroups.SEARCH_VIEW_PRINT.name;
		}
		
		DirectoryContextProvider dcp = OrganizationServicesHelper.manager.newDirectoryContextProvider((String[]) null, (String[]) null);
		WTGroup advancedGroup = OrganizationServicesHelper.manager.getGroup(name, dcp);
		if(advancedGroup != null) {
			Enumeration<WTPrincipal> e = advancedGroup.members();
			while(e.hasMoreElements()){
				WTPrincipal prin = (WTPrincipal)e.nextElement();
				
				WTUser user = (WTUser)prin;
				PeopleData p = new PeopleData(user);
				
				System.out.println(user.getFullName());
				list.add(p);
			}
		}

		return list;
	}

	/**
	 * @methodName : getSearchDocAttrListAction
	 * @author : hckim
	 * @date : 2021.09.13
	 * @return : List<DocAttributeData>
	 * @description : 문서 속성
	 */
	public List<NumberCodeData> getSearchDocAttrListAction(Map<String, Object> reqMap) throws Exception {

		List<NumberCodeData> list = new ArrayList<NumberCodeData>();
		QuerySpec qs = getDocAttrListQuery(reqMap);
		if (qs != null) {
			QueryResult qr = PersistenceHelper.manager.find(qs);

			while (qr.hasMoreElements()) {
				Object[] o = (Object[]) qr.nextElement();
				NumberCode code = (NumberCode) o[0];
				NumberCodeData data = new NumberCodeData(code);
				list.add(data);
			}
		}
		return list;
	}
	
	/**
	 * @methodName : getDocValueListQuery
	 * @author : hckim
	 * @date : 2021.09.13
	 * @return : QuerySpec
	 * @description : NumberCode>DOCCODETYPE SEARCH QUERY
	 */
	public QuerySpec getDocAttrListQuery(Map<String, Object> reqMap) throws Exception {

		QuerySpec qs = null;
		qs = new QuerySpec();
		int idx = qs.addClassList(NumberCode.class, true);
		if (qs.getConditionCount() > 0) {
			qs.appendAnd();
		}
		qs.appendWhere(new SearchCondition(NumberCode.class, NumberCode.CODE_TYPE, SearchCondition.EQUAL,
				DocKey.NUMBERCODE_DOCATTRIBUTE.getKey(), false), new int[] { idx });

		qs.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class, NumberCode.CODE), false), new int[] { idx });
		
		return qs;
	}
	
	public List<AuthorityGroupData> getAuthorityGroupList(Map<String, Object> reqMap) throws Exception {
		
		List<AuthorityGroupData> list = new ArrayList<AuthorityGroupData>();
		
		//권한분류 넘버코트
		String code = StringUtil.checkNull((String) reqMap.get("code"));
		String auth = StringUtil.checkNull((String) reqMap.get("auth"));
		String authObjectType = StringUtil.checkNull((String) reqMap.get("authObjectType"));
		
		QuerySpec qs = new QuerySpec();
		
		int idx = qs.addClassList(AuthorityGroup.class, true);
		SearchCondition sc = null;
		
		if(code.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(AuthorityGroup.class, AuthorityGroup.CODE_TYPE, SearchCondition.EQUAL, code);
			qs.appendWhere(sc, new int[] {idx});
		}
		
		if(auth.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(AuthorityGroup.class, AuthorityGroup.AUTH, SearchCondition.EQUAL, auth);
			qs.appendWhere(sc, new int[] {idx});
		}
		
		if(authObjectType.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(AuthorityGroup.class, AuthorityGroup.AUTH_OBJECT_TYPE, SearchCondition.EQUAL, authObjectType);
			qs.appendWhere(sc, new int[] {idx});
		}
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		while(qr.hasMoreElements()) {
			Object[] o = (Object[]) qr.nextElement();
			AuthorityGroup ag = (AuthorityGroup)o[0];
			list.add(new AuthorityGroupData(ag));
		}
		
		return list;
	}
	
	public AuthorityGroup getAuthorityGroup(Map<String, Object> reqMap) throws Exception {
		
		//권한분류 넘버코트
		String code = StringUtil.checkNull((String) reqMap.get("code"));
		String auth = StringUtil.checkNull((String) reqMap.get("auth"));
		String authObjectType = StringUtil.checkNull((String) reqMap.get("authObjectType"));
		
		QuerySpec qs = new QuerySpec();
		
		int idx = qs.addClassList(AuthorityGroup.class, true);
		SearchCondition sc = null;
		
		if(code.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(AuthorityGroup.class, AuthorityGroup.CODE_TYPE, SearchCondition.EQUAL, code);
			qs.appendWhere(sc, new int[] {idx});
		}
		
		if(auth.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(AuthorityGroup.class, AuthorityGroup.AUTH, SearchCondition.EQUAL, auth);
			qs.appendWhere(sc, new int[] {idx});
		}
		
		if(authObjectType.length() > 0) {
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(AuthorityGroup.class, AuthorityGroup.AUTH_OBJECT_TYPE, SearchCondition.EQUAL, authObjectType);
			qs.appendWhere(sc, new int[] {idx});
		}
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		if(qr.hasMoreElements()) {
			Object[] o = (Object[]) qr.nextElement();
			AuthorityGroup ag = (AuthorityGroup)o[0];
			return ag;
		}
		
		return null;
	}
	
	public List<PeopleData> getGroupUserList(Map<String, Object> reqMap) throws Exception {
		List<PeopleData> pdList = new ArrayList<PeopleData>();
		String oid = StringUtil.checkNull((String)reqMap.get("oid"));
		WTGroup group = (WTGroup) CommonUtil.getObject(oid);
		//WTGroup group = link.getGroup();
		Enumeration<?> members = group.members();
		while(members.hasMoreElements()){
			WTUser user = (WTUser)members.nextElement();
			pdList.add(new PeopleData(user));
		}
		return pdList;
	}
	
	public List<Map<String,String>> getAuthorityGroupTypeList() {
		
		AuthorityGroupType[] codeType = AuthorityGroupType.getAuthorityGroupTypeSet();
		
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		
		for(int i=0; i < codeType.length; i++){	
			Map<String,String> map = new HashMap<String,String>();
			map.put("key", codeType[i].toString());
			map.put("value", codeType[i].getDisplay(MessageUtil.getLocale()));
			list.add(map);
		}
		
		return list;
	}
	
	public List<Map<String,String>> getAuthorityObjectTypeList() {
		
		AuthorityObjectType[] codeType = AuthorityObjectType.getAuthorityObjectTypeSet();
		
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		
		for(int i=0; i < codeType.length; i++){	
			Map<String,String> map = new HashMap<String,String>();
			map.put("key", codeType[i].toString());
			map.put("value", codeType[i].getFullDisplay());
			list.add(map);
		}
		
		return list;
	}
	
	public List<Map<String,String>> getAuthList() {
		
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		
		Map<String,String> map = null;
		
		Iterator<String> keys = AuthKey.AUTHKEY.getKeyMap().keySet().iterator();
		
		while( keys.hasNext() ){
			map = new HashMap<String,String>();
            String key = keys.next();
            String value = AuthKey.AUTHKEY.getKeyMap().get(key);
            map.put("key", key);
			map.put("value", value);
			list.add(map);
        }
		
		return list;
	}
	
	public List<Map<String,String>> getAuthObjectList() {
		
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		
		Map<String,String> map = null;
		
		List<Map<String,Object>> objectKeyList = ObjectKey.OBJECTKEY.getKeyList();
		
		for(Map<String,Object> item : objectKeyList) {
			map = new HashMap<String,String>();
			String code = (String)item.get("code");
			String name = (String)item.get("name");
			map.put("code", code);
			map.put("name", name);
			list.add(map);
		}
		
		return list;
	}
	
	public Map<String,List<Map<String,String>>> getDomainMap() throws Exception {
		
		Map<String,List<Map<String,String>>> result = new HashMap<String,List<Map<String,String>>>();
		
		List<Map<String,String>> list = null;
		
		Map<String,String> map = null;
		
		AuthorityGroupType[] codeType = AuthorityGroupType.getAuthorityGroupTypeSet();
		
		
		for(int i=0; i < codeType.length; i++){	
			String code = codeType[i].toString();
			if("MODULE".equals(code)) {
				AdministrativeDomain rootDomain = WCUtil.getAdministrativeDomain(BASIC_DOMAIN_PATH);
				list = new ArrayList<Map<String,String>>();
				map = new HashMap<String,String>();
				map.put("code", "/"+rootDomain.getName());
				map.put("name", rootDomain.getDescription());
				list.add(map);
				result.put(code, list);
			}else {
				list = getDomainList(code);
				result.put(code, list);
			}
		}
		
		return result;
	}
	
	public List<Map<String,String>> getDomainList(String parentDomainName) throws Exception {
		
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		
		Map<String,String> map = null;
		
		AdministrativeDomain parentDomain = WCUtil.getAdministrativeDomain(BASIC_DOMAIN_PATH + "/" + parentDomainName);
		
		QuerySpec qs = new QuerySpec();
		
		int idx = qs.addClassList(AdministrativeDomain.class, true);
		SearchCondition sc = null;
		
		sc = new SearchCondition(AdministrativeDomain.class, AdministrativeDomain.DOMAIN_REF + "." + WTAttributeNameIfc.REF_OBJECT_ID, SearchCondition.EQUAL, parentDomain.getPersistInfo().getObjectIdentifier().getId());
		qs.appendWhere(sc, new int[] {idx});
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		while(qr.hasMoreElements()) {
			Object[] o = (Object[]) qr.nextElement();
			AdministrativeDomain domain = (AdministrativeDomain)o[0];
			map = new HashMap<String,String>();
			map.put("code", BASIC_DOMAIN_PATH+"/"+parentDomain.getName()+"/"+domain.getName());
			map.put("name", domain.getDescription());
			list.add(map);
		}
		
		return list;
	}
	
	public List<EsolutionMenuData> getEsolutionMenu(boolean disabled) throws Exception {
		
		List<EsolutionMenuData> list = new ArrayList<EsolutionMenuData>();
		
		QuerySpec qs = new QuerySpec();
		
		int idx = qs.addClassList(EsolutionMenu.class, true);
		
		qs.appendWhere(new SearchCondition(EsolutionMenu.class, EsolutionMenu.MENU_LEVEL, SearchCondition.EQUAL, 1), new int[] {idx});
		if(!disabled) {
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(EsolutionMenu.class, EsolutionMenu.DISABLED, SearchCondition.IS_FALSE), new int[] {idx});
		}
		qs.appendOrderBy(new OrderBy(new ClassAttribute(EsolutionMenu.class, EsolutionMenu.SORT), false), new int[] {idx});
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		while(qr.hasMoreElements()) {
			Object[] o = (Object[]) qr.nextElement();
			EsolutionMenu em = (EsolutionMenu)o[0];
			WTPrincipal principal = SessionHelper.manager.getPrincipal();
			boolean isMember = OrganizationServicesHelper.manager.isMember(em.getGroup(), principal);
			if((!disabled && isMember) || CommonUtil.isAdmin()) list.add(getChildMenuData(new EsolutionMenuData(em), disabled));
		}
		return list;
	}
	
	public EsolutionMenuData getEsolutionMenu(String alias) throws Exception {
		
		EsolutionMenuData data = null;
		
		QuerySpec qs = new QuerySpec();
		
		int idx = qs.addClassList(EsolutionMenu.class, true);
		
		qs.appendWhere(new SearchCondition(EsolutionMenu.class, EsolutionMenu.ALIAS, SearchCondition.EQUAL, alias), new int[] {idx});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(EsolutionMenu.class, EsolutionMenu.MENU_LEVEL, SearchCondition.EQUAL, 1), new int[] {idx});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(EsolutionMenu.class, EsolutionMenu.DISABLED, SearchCondition.IS_FALSE), new int[] {idx});
		qs.appendOrderBy(new OrderBy(new ClassAttribute(EsolutionMenu.class, EsolutionMenu.SORT), false), new int[] {idx});
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		if(qr.hasMoreElements()) {
			Object[] o = (Object[]) qr.nextElement();
			EsolutionMenu em = (EsolutionMenu)o[0];
			data = getChildMenuData(new EsolutionMenuData(em), false);
		}
		
		return data;
	}
	
	public List<EsolutionMenu> getEsolutionMenuAll() throws Exception {
		
		List<EsolutionMenu> list = new ArrayList<EsolutionMenu>();
		
		QuerySpec qs = new QuerySpec();
		
		int idx = qs.addClassList(EsolutionMenu.class, true);
		
		qs.appendWhere(new SearchCondition(EsolutionMenu.class, EsolutionMenu.DISABLED, SearchCondition.IS_FALSE), new int[] {idx});
		
		qs.appendOrderBy(new OrderBy(new ClassAttribute(EsolutionMenu.class, EsolutionMenu.SORT), false), new int[] {idx});
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		while(qr.hasMoreElements()) {
			Object[] o = (Object[]) qr.nextElement();
			EsolutionMenu em = (EsolutionMenu)o[0];
//			WTPrincipal principal = SessionHelper.manager.getPrincipal();
//			boolean isMember = OrganizationServicesHelper.manager.isMember(em.getGroup(), principal);
//			if(isMember || CommonUtil.isAdmin())
				list.add(em);
		}
		return list;
	}
	
	public List<EsolutionMenu> getEsolutionAuthMenuAll() throws Exception {
		
		List<EsolutionMenu> list = new ArrayList<EsolutionMenu>();
		
		QuerySpec qs = new QuerySpec();
		
		int idx = qs.addClassList(EsolutionMenu.class, true);
		
		qs.appendWhere(new SearchCondition(EsolutionMenu.class, EsolutionMenu.DISABLED, SearchCondition.IS_FALSE), new int[] {idx});
		
		qs.appendOrderBy(new OrderBy(new ClassAttribute(EsolutionMenu.class, EsolutionMenu.SORT), false), new int[] {idx});
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		while(qr.hasMoreElements()) {
			Object[] o = (Object[]) qr.nextElement();
			EsolutionMenu em = (EsolutionMenu)o[0];
			WTPrincipal principal = SessionHelper.manager.getPrincipal();
			boolean isMember = OrganizationServicesHelper.manager.isMember(em.getGroup(), principal);
			if(isMember || CommonUtil.isAdmin()) list.add(em);
		}
		
		return list;
	}
	
	
	public EsolutionMenuData getChildMenuData(EsolutionMenuData emData, boolean disabled) throws Exception{
		
		List<EsolutionMenuData> list = new ArrayList<EsolutionMenuData>();
		
		QuerySpec qs = new QuerySpec();
		
		int idx = qs.addClassList(EsolutionMenu.class, true);
		
		qs.appendWhere(new SearchCondition(EsolutionMenu.class, EsolutionMenu.PARENT_REFERENCE+"."+WTAttributeNameIfc.REF_OBJECT_ID, SearchCondition.EQUAL, CommonUtil.getOIDLongValue(emData.getOid())), new int[] {idx});
		if(!disabled) {
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(EsolutionMenu.class, EsolutionMenu.DISABLED, SearchCondition.IS_FALSE), new int[] {idx});
		}
		qs.appendOrderBy(new OrderBy(new ClassAttribute(EsolutionMenu.class, EsolutionMenu.SORT), false), new int[] {idx});
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		while(qr.hasMoreElements()) {
			Object[] o = (Object[]) qr.nextElement();
			EsolutionMenu em = (EsolutionMenu)o[0];
			WTPrincipal principal = SessionHelper.manager.getPrincipal();
			boolean isMember = OrganizationServicesHelper.manager.isMember(em.getGroup(), principal);
			if((!disabled && isMember) || CommonUtil.isAdmin()) list.add(new EsolutionMenuData(em));
		}
		
		
		if(list.size() > 0) {
			
			for(EsolutionMenuData child_emData: list) {
				child_emData = getChildMenuData(child_emData, disabled);
			}
			
			emData.setChildren(list);
		}
		
		return emData;
		
	}
	
	
	public FolderAuthGroup getFolderAuthGroupBySubFolder(SubFolder folder, String auth) throws Exception{
		
		FolderAuthGroup authGroup = null;
		
		QuerySpec qs = new QuerySpec();
		
		int idx = qs.addClassList(FolderAuthGroup.class, true);
		
		qs.appendWhere(new SearchCondition(FolderAuthGroup.class, FolderAuthGroup.ROLE_AOBJECT_REF + "." + WTAttributeNameIfc.REF_OBJECT_ID, SearchCondition.EQUAL, CommonUtil.getOIDLongValue(folder)), new int[] {idx});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(FolderAuthGroup.class, FolderAuthGroup.AUTH_FOLDER_GROUP_TYPE, SearchCondition.EQUAL, auth), new int[] {idx});
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		if(qr.hasMoreElements()) {
			Object[] o = (Object[]) qr.nextElement();
			authGroup = (FolderAuthGroup)o[0];
		}
		
		return authGroup;
		
	}
	
	public ObjectAuthGroup getObjectAuthGroupByWTGroup(WTGroup group) throws Exception{
		
		ObjectAuthGroup authGroup = null;
		
		QuerySpec qs = new QuerySpec();
		
		int idx = qs.addClassList(ObjectAuthGroup.class, true);
		
		qs.appendWhere(new SearchCondition(ObjectAuthGroup.class, ObjectAuthGroup.OBJECT_GROUP_REFERENCE + "." + WTAttributeNameIfc.REF_OBJECT_ID, SearchCondition.EQUAL, CommonUtil.getOIDLongValue(group)), new int[] {idx});
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		if(qr.hasMoreElements()) {
			Object[] o = (Object[]) qr.nextElement();
			authGroup = (ObjectAuthGroup)o[0];
		}
		
		return authGroup;
		
	}
	
	public List<ObjectAuthGroup> getObjectAuthGroup(AuthorityObjectType authObjectType) throws Exception {
		List<ObjectAuthGroup> list = new ArrayList<ObjectAuthGroup>();
		
		QuerySpec qs = new QuerySpec();
		
		int idx = qs.addClassList(ObjectAuthGroup.class, true);
		SearchCondition sc = null;
		
		sc = new SearchCondition(ObjectAuthGroup.class, ObjectAuthGroup.AUTH_OBJECT_TYPE, SearchCondition.EQUAL, authObjectType.toString());
		qs.appendWhere(sc,  new int[] {idx});
		//qs.appendOrderBy(new OrderBy(new ClassAttribute(ObjectAuthGroup.class,ObjectAuthGroup.AUTH_OBJECT_TYPE),false), new int[] {idx});
		//qs.appendOrderBy(new OrderBy(new ClassAttribute(ObjectAuthGroup.class,ObjectAuthGroup.OBJECT_GROUP_REFERENCE+".key.id"),false), new int[] {idx});
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		while(qr.hasMoreElements()) {
			Object[] o = (Object[])qr.nextElement();
			ObjectAuthGroup oag = (ObjectAuthGroup)o[0];
			list.add(oag);
		}
		
		return list;
	}
	
	public List<ObjectAuthGroupData> getObjectAuthGroupList() throws Exception {
		List<ObjectAuthGroupData> list = new ArrayList<ObjectAuthGroupData>();
		
		AuthorityObjectType[] codeType = AuthorityObjectType.getAuthorityObjectTypeSet();
		
		for(int i=0; i < codeType.length; i++){
			ObjectAuthGroupData data = new ObjectAuthGroupData(codeType[i]);
			list.add(data);
		}
		
		return list;
	}
	
	public List<WTGroup> getLicenseGroupNameWTGroup(){
		String serverType = ConfigImpl.getInstance().getString("server.type");
		
		List<WTGroup> gNameList = new ArrayList<WTGroup>();
		gNameList = AdminHelper.service.searchGetGroup(LicenseGroups.PTC_WINDCHILL_ADVANCED_LICENSE.name, gNameList);
		gNameList = AdminHelper.service.searchGetGroup(LicenseGroups.PTC_CONTRIBUTOR_LICENSE.name, gNameList);
		gNameList = AdminHelper.service.searchGetGroup(LicenseGroups.SEARCH_VIEW_PRINT.name, gNameList);
		
		if("plm".equals(serverType)) {
			gNameList = AdminHelper.service.searchGetGroup(LicenseGroups.PTC_MECHANICAL_DESIGN_I_LICENSE.name, gNameList);
		}else{
			gNameList = AdminHelper.service.searchGetGroup(LicenseGroups.PTC_PDMLINK_MODULE_LICENSE.name, gNameList);
		}
		return gNameList;
	}
	
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
	
}
