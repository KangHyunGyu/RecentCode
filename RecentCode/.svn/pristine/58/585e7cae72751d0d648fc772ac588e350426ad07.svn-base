package com.e3ps.org.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.e3ps.admin.service.AdminHelper;
import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.org.Department;
import com.e3ps.org.People;
import com.e3ps.org.bean.PeopleData;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.org.LicenseGroupHelper;
import wt.org.LicenseGroups;
import wt.org.WTUser;
import wt.pds.StatementSpec;
import wt.query.ArrayExpression;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.ServiceFactory;
import wt.session.SessionHelper;
import wt.util.WTAttributeNameIfc;
import wt.util.WTException;

public class PeopleHelper {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.ORG.getName());
	public static final PeopleService service = ServiceFactory.getService(PeopleService.class);

	public static final PeopleHelper manager = new PeopleHelper();

	public List<PeopleData> getUserList(Map<String, Object> reqMap) throws Exception{
		List<PeopleData> list = new ArrayList<PeopleData>();
		
		QuerySpec query = getUserListQuery(reqMap);
		QueryResult result = PersistenceHelper.manager.find((StatementSpec)query);
		
		while(result.hasMoreElements()){
			Object[] obj = (Object[]) result.nextElement();
			PeopleData pd = new PeopleData(obj[0]);
			list.add(pd);
		}
		
		return list;
	}
	
	/**
	 * 라이선스 있는 유저만 리턴
	 * @return
	 * @throws Exception
	 */
	public List<PeopleData> getLicenseAllUserList(Map<String, Object> reqMap) throws Exception{
		
		List<PeopleData> list = new ArrayList<>();
		
		QuerySpec query = getSearchUserLicenseQuery(reqMap);
		
		QueryResult qr = PersistenceHelper.manager.find(query);
		
		while(qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			Set<LicenseGroups> g = LicenseGroupHelper.getLicenseGroupsForUser((WTUser)obj[1]);
			if(g.size()>0) {
				PeopleData pd = new PeopleData(obj[0]);
				list.add(pd);
			}
		}
		return list;
		
	}
	
	/**
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	public QuerySpec getSearchUserLicenseQuery(Map<String, Object> reqMap) throws Exception{
		QuerySpec qs = new QuerySpec();
		
		String name = StringUtil.checkNull((String)reqMap.get("name"));
		String userId = StringUtil.checkNull((String) reqMap.get("userId"));
		String userName = StringUtil.checkNull((String) reqMap.get("userName"));
		String departmentOid = StringUtil.checkNull((String) reqMap.get("departmentOid"));
		String groupOid = StringUtil.checkNull((String) reqMap.get("groupOid"));
		String parentGroupOid = StringUtil.checkNull((String) reqMap.get("parentGroupOid"));
		
		boolean excludeSessionUser = reqMap.get("excludeSessionUser")==null?false:(boolean)reqMap.get("excludeSessionUser");
		
		if(userName.isEmpty() && !name.isEmpty()) {
			userName = name;
		}
		
		Department dept = null;
		if (departmentOid != null && departmentOid.length() > 0) {
			dept = (Department) CommonUtil.getObject(departmentOid);
			if("ROOT".equals(dept.getCode())) {  //Root(로더) //1 (I/F)
				dept = null;
			}
		}
		
		int ii = qs.addClassList(People.class, true);
		int jj = qs.addClassList(WTUser.class, true);

		if (qs.getConditionCount() > 0) {
			qs.appendAnd();
		}
		qs.appendWhere(new SearchCondition(People.class, "userReference.key.id", WTUser.class, "thePersistInfo.theObjectIdentifier.id"), new int[] { ii, jj });

		if (userId != null && !userId.equals("")) {
			if (qs.getConditionCount() > 0)
				qs.appendAnd();
			qs.appendWhere(new SearchCondition(WTUser.class, WTUser.NAME, SearchCondition.LIKE, "%" + userId + "%", false), new int[] { jj });
		}

		if (userName.length() > 0) {
			if (qs.getConditionCount() > 0)
				qs.appendAnd();
			qs.appendWhere(new SearchCondition(People.class, People.NAME, SearchCondition.LIKE, "%" + userName + "%"), new int[] { ii });
		}
		
		if (dept != null) {
			int kk = qs.addClassList(Department.class, true);
			
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			qs.appendWhere(new SearchCondition(People.class, "departmentReference.key.id", Department.class,
					"thePersistInfo.theObjectIdentifier.id"), new int[] { ii, kk });
			
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			qs.appendOpenParen();
			qs.appendWhere(new SearchCondition(Department.class, "thePersistInfo.theObjectIdentifier.id", "=",
					dept.getPersistInfo().getObjectIdentifier().getId()), new int[] { kk });
			List<Department> childDeptList = DepartmentHelper.manager.getAllChildList(dept, new ArrayList<Department>());
			for(Department dm : childDeptList) {
				qs.appendOr();
				qs.appendWhere(new SearchCondition(Department.class, "thePersistInfo.theObjectIdentifier.id", "=",
						CommonUtil.getOIDLongValue(dm)), new int[] { kk });
			}
			
			qs.appendCloseParen();
		}
		
		if(excludeSessionUser) {
			WTUser sessionUser = (WTUser) SessionHelper.manager.getPrincipal();
			if (qs.getConditionCount() > 0)qs.appendAnd();
			qs.appendWhere(new SearchCondition(People.class, "userReference.key.id", SearchCondition.NOT_EQUAL, CommonUtil.getOIDLongValue(sessionUser)), new int[] { ii });
			
		}
		
		List<PeopleData> groupPeopleList = new ArrayList<PeopleData>();
		
		if(parentGroupOid.length() > 0) {
			reqMap.put("oid", parentGroupOid);
			groupPeopleList = AdminHelper.manager.getGroupUserList(reqMap);
		}else{
			if(groupOid.length() > 0){
				reqMap.put("oid", groupOid);
				groupPeopleList = AdminHelper.manager.getGroupUserList(reqMap);
			}
		}
		
		if(groupPeopleList.size() > 0) {
			List<Long> userOidLongValueList = new ArrayList<>();
			for (PeopleData pd : groupPeopleList) {
				userOidLongValueList.add(CommonUtil.getOIDLongValue(pd.getOid()));
			}
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			qs.appendWhere(new SearchCondition(new ClassAttribute(People.class, WTAttributeNameIfc.ID_NAME),
					SearchCondition.NOT_IN, new ArrayExpression(userOidLongValueList.toArray())), new int[] { ii });
		}
		
		qs.appendOrderBy(new OrderBy(new ClassAttribute(People.class, People.SORT_NUM), false), new int[] { ii });
		qs.appendOrderBy(new OrderBy(new ClassAttribute(People.class, People.ID), false), new int[] { ii });
		
		return qs;
	}
	
	
	/**
	 * @desc	: 사용자 검색 Action
	 * @author	: shkim
	 * @date	: 2019. 6. 13.
	 * @method	: getUserListPagingAction
	 * @return	: List<PeopleData>
	 * @param   : reqMap
	 * @throws  : Exception
	 */
	public List<PeopleData> getUserListAction(Map<String, Object> reqMap) throws Exception {
		
		List<PeopleData> list = new ArrayList<>();
		
		QuerySpec query = getUserListQuery(reqMap);
		
		QueryResult qr = PersistenceHelper.manager.find(query);
		
		while(qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			PeopleData pd = new PeopleData(obj[0]);
			list.add(pd);
		}

		return list;
	}
	 
	/**
	 * @desc	: 사용자 검색 Query
	 * @author	: mnyu
	 * @date	: 2019. 10. 29.
	 * @method	: getuserListQuery
	 * @return	: QuerySpec
	 * @param reqMap
	 * @return
	 */
	public QuerySpec getUserListQuery(Map<String, Object> reqMap) throws Exception{
		String userId = StringUtil.checkNull((String) reqMap.get("userId"));
		String userName = StringUtil.checkNull((String) reqMap.get("userName"));
		String departmentOid = StringUtil.checkNull((String) reqMap.get("departmentOid"));
		String isDisable = StringUtil.checkNull((String) reqMap.get("isDisable"));
		String authUser = StringUtil.checkNull((String) reqMap.get("authUser"));
		
		Department dept = null;
		if (departmentOid != null && departmentOid.length() > 0) {
			dept = (Department) CommonUtil.getObject(departmentOid);
			if(dept!=null) {
				if("ROOT".equals(dept.getCode())) {  //Root(로더) //1 (I/F)
					dept = null;
				}
			}
		}
		QuerySpec qs = new QuerySpec();

		int ii = qs.addClassList(People.class, true);
		int jj = qs.addClassList(WTUser.class, true);

		if (qs.getConditionCount() > 0) {
			qs.appendAnd();
		}
		qs.appendWhere(new SearchCondition(People.class, "userReference.key.id", WTUser.class, "thePersistInfo.theObjectIdentifier.id"), new int[] { ii, jj });

		
		/**
		 * 라이선스 적용 유저만 검색되게
		 * */
		//if (qs.getConditionCount() > 0) {
		//	qs.appendAnd();
		//}
		//qs.appendWhere(new SearchCondition(People.class, People.IS_DISABLE, SearchCondition.IS_FALSE), new int[] { ii });
		
		if (isDisable.length() > 0) {
			if (qs.getConditionCount() > 0)
				qs.appendAnd();
			if(StringUtil.booleanValue(isDisable)){
				qs.appendWhere(new SearchCondition(People.class, People.IS_DISABLE, SearchCondition.IS_TRUE), new int[] { ii });
			}else{
				qs.appendWhere(new SearchCondition(People.class, People.IS_DISABLE, SearchCondition.IS_FALSE), new int[] { ii });
			}
		}
		
		if (dept != null) {
			int kk = qs.addClassList(Department.class, true);
			
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			qs.appendWhere(new SearchCondition(People.class, "departmentReference.key.id", Department.class,
					"thePersistInfo.theObjectIdentifier.id"), new int[] { ii, kk });
			
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			qs.appendOpenParen();
			qs.appendWhere(new SearchCondition(Department.class, "thePersistInfo.theObjectIdentifier.id", "=",
					dept.getPersistInfo().getObjectIdentifier().getId()), new int[] { kk });
			List<Department> childDeptList = DepartmentHelper.manager.getAllChildList(dept, new ArrayList<Department>());
			for(Department dm : childDeptList) {
				qs.appendOr();
				qs.appendWhere(new SearchCondition(Department.class, "thePersistInfo.theObjectIdentifier.id", "=",
						CommonUtil.getOIDLongValue(dm)), new int[] { kk });
			}
			
			qs.appendCloseParen();
		}

		if (userId != null && !userId.equals("")) {
			if (qs.getConditionCount() > 0)
				qs.appendAnd();
			qs.appendWhere(new SearchCondition(WTUser.class, WTUser.NAME, SearchCondition.LIKE,
					"%" + userId + "%", false), new int[] { jj });
		}

		if (userName.length() > 0) {
			if (qs.getConditionCount() > 0)
				qs.appendAnd();
			qs.appendWhere(new SearchCondition(People.class, People.NAME, SearchCondition.LIKE, "%" + userName + "%"),
					new int[] { ii });
		}
		
		if (authUser.equals("search")) {
			if (qs.getConditionCount() > 0)
				qs.appendAnd();
			qs.appendWhere(new SearchCondition(People.class, People.CHIEF, SearchCondition.IS_TRUE),
					new int[] { ii });
			
		}
		qs.appendOrderBy(new OrderBy(new ClassAttribute(People.class, People.SORT_NUM), false), new int[] { ii });
		qs.appendOrderBy(new OrderBy(new ClassAttribute(People.class, People.ID), false), new int[] { ii });
		
		LOGGER.info(qs.toString());
		return qs;
	}

	/**
	 * @desc	: 사용자 전체 검색
	 * @author	: shkim
	 * @date	: 2019. 6. 13.
	 * @method	: getSearchUserListAction
	 * @return	: List<PeopleData>
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	public List<PeopleData> getSearchUserListAction(Map<String, Object> reqMap) throws Exception {
		
		List<PeopleData> list = new ArrayList<>();

		String name = StringUtil.checkNull((String) reqMap.get("name"));
		String departmentOid = StringUtil.checkNull((String) reqMap.get("departmentOid"));

		Department dept = null;
		if (departmentOid.length() > 0) {
			dept = (Department) CommonUtil.getObject(departmentOid);
			if("ROOT".equals(dept.getCode())) {
				dept = null;
			}
		}
		QuerySpec qs = new QuerySpec();

		int ii = qs.addClassList(People.class, true);
		int jj = qs.addClassList(WTUser.class, false);
		
		SearchCondition sc = null;
		
		qs.appendWhere(new SearchCondition(People.class, "userReference.key.id", WTUser.class,
				"thePersistInfo.theObjectIdentifier.id"), new int[] { ii, jj });

		if(name.length() > 0) {
			if(qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(People.class, People.NAME, SearchCondition.LIKE, "%" + name + "%", false);
			qs.appendWhere(sc, new int[] { ii });
		}
		
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(WTUser.class, WTUser.DISABLED, SearchCondition.IS_FALSE), new int[] { jj });

		if (dept != null) {
			
			int kk = qs.addClassList(Department.class, false);
			
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			
			qs.appendWhere(new SearchCondition(People.class, "departmentReference.key.id", Department.class,
					"thePersistInfo.theObjectIdentifier.id"), new int[] { ii, kk });
			
			if (qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			qs.appendOpenParen();
			qs.appendWhere(new SearchCondition(Department.class, "thePersistInfo.theObjectIdentifier.id", "=",
					dept.getPersistInfo().getObjectIdentifier().getId()), new int[] { kk });
			List<Department> childDeptList = DepartmentHelper.manager.getAllChildList(dept, new ArrayList<Department>());
			for(Department dm : childDeptList) {
				qs.appendOr();
				qs.appendWhere(new SearchCondition(Department.class, "thePersistInfo.theObjectIdentifier.id", "=",
						CommonUtil.getOIDLongValue(dm)), new int[] { kk });
			}
			
			qs.appendCloseParen();
		}
		
		qs.appendOrderBy(new OrderBy(new ClassAttribute(People.class, People.SORT_NUM), false), new int[] { ii });
		qs.appendOrderBy(new OrderBy(new ClassAttribute(People.class, People.NAME), false), new int[] { ii });
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		while(qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			PeopleData pd = new PeopleData(obj[0]);
			list.add(pd);
		}

		return list;
	}
	
	/**
	 * @desc	: 부서 사용자 검색
	 * @author	: shkim
	 * @date	: 2019. 6. 13.
	 * @method	: getDeptUserListAction
	 * @return	: List<PeopleData>
	 * @param   : reqMap
	 * @throws  : Exception
	 */
	public List<PeopleData> getDeptUserListAction(Map<String, Object> reqMap) throws Exception {
		
		List<PeopleData> list = null;
		
		String departmentOid = StringUtil.checkNull((String) reqMap.get("departmentOid"));

		Department dept = null;
		if (departmentOid != null && departmentOid.length() > 0) {
			dept = (Department) CommonUtil.getObject(departmentOid);
			if("ROOT".equals(dept.getCode())) {
				dept = null;
			}
		}
		
		if(dept != null) {
			list = new ArrayList<>();
			
			QuerySpec qs = new QuerySpec();
	
			int ii = qs.addClassList(People.class, true);
			int kk = qs.addClassList(Department.class, true);
	
			qs.appendWhere(new SearchCondition(People.class, "departmentReference.key.id", Department.class,
					"thePersistInfo.theObjectIdentifier.id"), new int[] { ii, kk });
			
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(Department.class, "thePersistInfo.theObjectIdentifier.id", "=",
					dept.getPersistInfo().getObjectIdentifier().getId()), new int[] { kk });
	
			QueryResult qr = PersistenceHelper.manager.find(qs);
			
			while(qr.hasMoreElements()) {
				Object[] obj = (Object[]) qr.nextElement();
				PeopleData pd = new PeopleData(obj[0]);
				list.add(pd);
			}
		}

		return list;
	}
	
	/**
	 * @desc	: 선택한 부서에 해당하지 않는 사용자 검색 Action
	 * @author	: shkim
	 * @date	: 2019. 6. 14.
	 * @method	: getNonDeptUserListAction
	 * @param   : reqMap
	 * @return  : List<PeopleData>
	 * @throws  : Exception
	 */
	public List<PeopleData> getNonDeptUserListAction(Map<String, Object> reqMap) throws Exception {
		
		List<PeopleData> list = new ArrayList<>();
		
		String departmentOid = StringUtil.checkNull((String) reqMap.get("departmentOid"));
		Department dept = (Department) CommonUtil.getObject(departmentOid);

		QuerySpec qs = new QuerySpec();

		int ii = qs.addClassList(People.class, true);

		qs.appendWhere(new SearchCondition(People.class, "departmentReference.key.id", SearchCondition.NOT_EQUAL, 
				CommonUtil.getOIDLongValue(dept)), new int[] { ii });

		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		while(qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			PeopleData pd = new PeopleData(obj[0]);
			list.add(pd);
		}
		return list;
	}
	
	/**
	 * @desc	: WTUser로 People 정보 얻기
	 * @author	: shkim
	 * @date	: 2019. 6. 17.
	 * @method	: getPeople
	 * @return	: People
	 * @param user
	 * @return
	 */
	public People getPeople(WTUser user) {
		return getPeople(user.getPersistInfo().getObjectIdentifier().getId());
	}

	/**
	 * @desc	: PeopleOid로 People정보 얻기
	 * @author	: shkim
	 * @date	: 2019. 6. 17.
	 * @method	: getPeople
	 * @return	: People
	 * @param userid
	 * @return
	 */
	public People getPeople(long userid) {
		try {
			QuerySpec spec = new QuerySpec();
			int mainClassPos = spec.addClassList(People.class, true);
			spec.appendWhere(new SearchCondition(People.class, "userReference.key.id", "=", userid), new int[] { mainClassPos });
			QueryResult qr = PersistenceHelper.manager.find(spec);
			if (qr.hasMoreElements()) {
				Object[] obj = (Object[]) qr.nextElement();
				return (People) obj[0];
			}
		} catch (QueryException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @desc	: PeopleOid로 People정보 얻기
	 * @author	: tsjeong
	 * @date	: 2020. 11. 04.
	 * @method	: getPeople
	 * @return	: People
	 * @param userid
	 * @return
	 */
	public People getPeopleInfo(long peopleOid) {
		try {
			QuerySpec spec = new QuerySpec();
			int mainClassPos = spec.addClassList(People.class, true);
			spec.appendWhere(new SearchCondition(People.class, "thePersistInfo.theObjectIdentifier.id", "=", peopleOid), new int[] { mainClassPos });
			QueryResult qr = PersistenceHelper.manager.find(spec);
			if (qr.hasMoreElements()) {
				Object[] obj = (Object[]) qr.nextElement();
				return (People) obj[0];
			}
		} catch (QueryException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public WTUser getWTUser(String name) {
		try {
			QuerySpec spec = new QuerySpec();
			int userPos = spec.addClassList(WTUser.class, true);
			spec.appendWhere(new SearchCondition(WTUser.class, WTUser.NAME, "=", name), new int[] { userPos });
			spec.appendAnd();
			spec.appendWhere(new SearchCondition(WTUser.class, WTUser.DISABLED,false), new int[] { userPos });
			QueryResult qr = PersistenceHelper.manager.find(spec);
			if (qr.hasMoreElements()) {
				Object[] obj = (Object[]) qr.nextElement();
				return (WTUser) obj[0];
			}
		} catch (QueryException e) {
			e.printStackTrace();
		} catch (WTException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @desc	: 부서별 유저
	 * @author	: tsuam
	 * @date	: 2019. 10. 30.
	 * @method	: getDepartmentUserList
	 * @return	: List<People>
	 * @param depart
	 * @return
	 * @throws Exception
	 */
	public List<People> getDepartmentUserList(Department depart) throws Exception{
		
		List<People> userList = new ArrayList<People>();
		long departLong = CommonUtil.getOIDLongValue(depart);
		QuerySpec spec = new QuerySpec();
		int mainClassPos = spec.addClassList(People.class, true);
		spec.appendWhere(new SearchCondition(People.class, People.DEPARTMENT_REFERENCE+".key.id", SearchCondition.EQUAL, departLong), new int[] { mainClassPos });
		QueryResult qr = PersistenceHelper.manager.find(spec);
		
		while(qr.hasMoreElements()){
			Object[] obj = (Object[])qr.nextElement();
			People pp = (People)obj[0];
			
			userList.add(pp);
		}
		
		return userList;
		
	}

	/**
	 * @desc	: 아이디 공용 여부 확인
	 * @author	: mnyu
	 * @date	: 2019. 11. 6.
	 * @method	: isPublic
	 * @return	: boolean
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	public boolean isPublic(WTUser user) throws Exception {
		if(user == null) return false;
		boolean isPublic = false;
		
		QuerySpec qs = new QuerySpec();
		qs.addClassList(People.class, true);
		qs.appendWhere(new SearchCondition(People.class, "userReference.key.id", "=", CommonUtil.getOIDLongValue(user)), new int[]{0});
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		if(qr.hasMoreElements()){
			isPublic = true;
		}
		
		return isPublic;
	}

	
	/**
	 * 
	 * @desc	: 유저 ID로 People 찾기
	 * @author	: tsuam
	 * @date	: 2019. 11. 15.
	 * @method	: getPeople
	 * @return	: People
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	public People getIDPeople(String userID) throws Exception{
		
		People pp = null;
		QuerySpec qs = new QuerySpec();
		int idx = qs.addClassList(People.class, true);
		qs.appendWhere(new SearchCondition(People.class, People.ID, "=",userID), new int[] { idx });
		
		QueryResult  rt = PersistenceHelper.manager.find(qs);
		
		while(rt.hasMoreElements()){
			
			Object[] obj = (Object[])rt.nextElement();
			pp = (People)obj[0];
			
		}
		
		return pp;
		
		
		//return 
		
	}
	
	public People getChiefFromDepartment(Department dept) throws Exception {
		People people = null;
		
		String chiefCode = ConfigImpl.getInstance().getString("depart.chief.code", "70");
		
		QuerySpec qs = new QuerySpec();

		int ii = qs.addClassList(People.class, true);

		qs.appendWhere(new SearchCondition(People.class, "departmentReference.key.id", SearchCondition.EQUAL, 
				CommonUtil.getOIDLongValue(dept)), new int[] { ii });

		qs.appendAnd();
		
		qs.appendWhere(new SearchCondition(People.class, "dutyCode", SearchCondition.EQUAL, chiefCode), new int[] { ii });
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		if(qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			people = (People) obj[0];
		}
		
		return people;
	}
	
	public List<People> getPeopleByDepartmentChild(Department dept) throws Exception {
		List<People> list = new ArrayList<>();
		
		List<Long> teamOidLongValueList = new ArrayList<>();
		
		if(dept != null) {
			String deptOid = CommonUtil.getOIDString(dept);
			
			teamOidLongValueList.add(CommonUtil.getOIDLongValue(deptOid));
			
			List<Department> relatedDeptList = new ArrayList<>();
			relatedDeptList = DepartmentHelper.manager.getAllChildList(dept, relatedDeptList);
			
			for(Department relatedDept : relatedDeptList) {
				if(!teamOidLongValueList.contains(CommonUtil.getOIDLongValue(relatedDept))) {
					teamOidLongValueList.add(CommonUtil.getOIDLongValue(relatedDept));
				}
			}
		} else {
			teamOidLongValueList.add(0L);
		}
		
		QuerySpec qs = new QuerySpec();

		int ii = qs.addClassList(People.class, true);
		
		qs.appendWhere(new SearchCondition(new ClassAttribute(People.class, "departmentReference.key.id"), SearchCondition.IN, 
				new ArrayExpression(teamOidLongValueList.toArray())), new int[] { ii });

		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		while(qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			People people = (People) obj[0];
			list.add(people);
		}
		
		return list;
	}
}
