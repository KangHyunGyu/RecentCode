package com.e3ps.org.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.PersistenceServerHelper;
import wt.fc.QueryResult;
import wt.introspection.ClassInfo;
import wt.introspection.WTIntrospector;
import wt.method.MethodContext;
import wt.org.OrganizationServicesHelper;
import wt.org.WTGroup;
import wt.org.WTPrincipal;
import wt.org.WTPrincipalReference;
import wt.org.WTUser;
import wt.pds.DatabaseInfoUtilities;
import wt.pds.StatementSpec;
import wt.pom.DBProperties;
import wt.pom.Transaction;
import wt.pom.WTConnection;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.query.StringSearch;
import wt.queue.ScheduleQueueEntry;
import wt.scheduler.ScheduleItem;
import wt.scheduler.SchedulingHelper;
import wt.services.StandardManager;
import wt.session.SessionHelper;
import wt.util.WTException;
import wt.util.WTProperties;
import wt.util.WTPropertyVetoException;

import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.TypeUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.common.web.PageQueryBroker;
import com.e3ps.common.web.PageQueryBrokerResult;
import com.e3ps.common.web.ParamUtil;
import com.e3ps.org.Department;
import com.e3ps.org.People;
import com.e3ps.org.WTUserPeopleLink;
import com.e3ps.project.EProject;
import com.infoengine.util.Base64;


public class StandardUserService extends StandardManager implements UserService {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.ORG.getName());
	private static final long serialVersionUID = 1L;
	
	static String dataStore = "Oracle"; //SQLServer ....
	static {
		try {
			dataStore = WTProperties.getLocalProperties().getProperty("wt.db.dataStore");
		} catch (Exception ex) {
			dataStore = "Oracle";
		}
	}
	
	/**
	 * Default factory for the class.
	 * 
	 * @return
	 * @throws WTException
	 */
	public static StandardUserService newStandardUserService()
			throws WTException {
		StandardUserService instance = new StandardUserService();
		instance.initialize();
		return instance;
	}
	
	public static boolean nameChange = false;
	
	@Override
	public void eventListener(Object _obj, String _event) {
		if (_obj instanceof WTUser) {
			if (_event.equals("POST_DISABLE")) {
				syncDelete((WTUser) _obj);
			} else if (_event.equals("POST_ENABLE")) {
				// 처리 상태 모호...
			} else if (_event.equals("POST_DELETE")) {
				syncDelete((WTUser) _obj);
			} else if (_event.equals("POST_MODIFY")) {
				syncModify((WTUser) _obj);
			} else if (_event.equals("POST_STORE")) {
				syncStore((WTUser) _obj);
			}
		}
	}

	@Override
	public void syncStore(WTUser _user) {
		try {
			People people = People.newPeople();
			people.setUser(_user);
			// people.setId(_user.getName());
			people.setName(_user.getFullName());
			// people.setEmail((_user.getEMail() == null) ? "" :
			// _user.getEMail());
			// people.setPwChangeDate(new Timestamp(new Date().getTime()));
			LOGGER.info("[PeopleHelper][Create]" + _user.getName() + "|"
					+ _user.getFullName());
			PersistenceHelper.manager.save(people);
		} catch (WTException e) {
			e.printStackTrace();
		} catch (WTPropertyVetoException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void syncModify(WTUser _user) {
		try {
			QueryResult qr = PersistenceHelper.manager.navigate(_user,
					"people", WTUserPeopleLink.class);
			if (qr.hasMoreElements()) {
				People people = (People) qr.nextElement();
				// people.setId(_user.getName());
				people.setName(_user.getFullName());
				// people.setEmail((_user.getEMail() == null) ? "" :
				// _user.getEMail());
				PersistenceHelper.manager.modify(people);
			} else {
				syncStore(_user);
			}
		} catch (WTException e) {
			e.printStackTrace();
		} catch (WTPropertyVetoException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public People getPeopleByFullName(String fullName) {
		try {
			QuerySpec spec = new QuerySpec();
			Class mainClass = People.class;
			int mainClassPos = spec.addClassList(mainClass, true);
			spec.appendWhere(new SearchCondition(mainClass, People.NAME, "=",
					fullName), new int[] { mainClassPos });
			QueryResult qr = PersistenceHelper.manager.find(spec);
			if (qr.hasMoreElements()) {
				Object[] obj = (Object[]) qr.nextElement();
				return (People) obj[0];
			}
		} catch (QueryException e) {
			e.printStackTrace();
		} catch (WTException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public WTUser getChiefUser(Department depart) {
		try {
			QuerySpec spec = new QuerySpec(People.class);
			spec.appendWhere(
					new SearchCondition(People.class,
							"departmentReference.key.id", "=", CommonUtil
									.getOIDLongValue(depart)), new int[] { 0 });
			spec.appendAnd();
			spec.appendWhere(new SearchCondition(People.class, People.CHIEF,
					SearchCondition.NOT_NULL, true), new int[] { 0 });
			QueryResult qr = PersistenceHelper.manager.find(spec);

			LOGGER.info("getChiefUser = " + spec);
			while (qr.hasMoreElements()) {
				return ((People) qr.nextElement()).getUser();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public QuerySpec getUserSearch(String name) throws QueryException {
		QuerySpec spec = new QuerySpec();
		try {
			int idx = spec.addClassList(People.class, true);

			spec.appendWhere(new SearchCondition(People.class, People.NAME,
					SearchCondition.LIKE, "%" + name + "%"), new int[] { idx });

		} catch (Exception e) {
			e.printStackTrace();
		}
		return spec;
	}
	
	@Override
	public void setAllUserName() throws Exception {
		if (!nameChange) {
			QuerySpec query = new QuerySpec();
			QueryResult qr = null;
			int idx = query.addClassList(WTUser.class, true);
			SearchCondition sc = new SearchCondition(WTUser.class, "last",
					"<>", "");
			query.appendWhere(sc, new int[] { idx });
			qr = PersistenceHelper.manager.find(query);
			String fullName = "";

			while (qr.hasMoreElements()) {
				Object[] o = (Object[]) qr.nextElement();
				WTUser wtuser = (WTUser) o[0];
				fullName = wtuser.getFullName();
				int i = fullName.indexOf(", ");
				if (i > 0) {
					fullName = fullName.substring(0, i)
							+ fullName.substring(i + 2);
					wtuser.setFullName(fullName);
					PersistenceServerHelper.manager.update(wtuser);
				}
			}
			nameChange = true;
			LOGGER.info("유저 변경완료!!!");
		}
	}

	@Override
	public void syncSave(WTUser _user) {
		LOGGER.info("syncSave");
		try {
			if (_user.isDisabled()) {
				syncDelete(_user);
				return;
			}

			// Windchill 10 이후 이름 변경
			String fullName = _user.getFullName();
			int i = fullName.indexOf(", ");
			if (i > 0) {
				fullName = fullName.substring(0, i) + fullName.substring(i + 2);
				_user.setFullName(fullName);
				PersistenceServerHelper.manager.update(_user);
			}
			//

			People people = getPeople(_user);

			if (people == null) {
				people = People.newPeople();
				people.setNameEn(_user.getFullName());
			}

			people.setUser(_user);
			people.setName(_user.getFullName());

			PersistenceHelper.manager.save(people);
		} catch (WTException e) {
			e.printStackTrace();
		} catch (WTPropertyVetoException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void syncDelete(WTUser _user) {
		LOGGER.info("syncDelete");
		try {
			QueryResult qr = PersistenceHelper.manager.navigate(_user,
					"people", WTUserPeopleLink.class);
			if (qr.hasMoreElements()) {
				People people = (People) qr.nextElement();
				people.setIsDisable(true);
				PersistenceHelper.manager.modify(people);
			}
		} catch (WTException e) {
			e.printStackTrace();
		} catch (WTPropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void syncWTUser() {
		try {
			QuerySpec query = new QuerySpec(WTUser.class);
			QueryResult result = PersistenceHelper.manager.find(query);
			WTUser wtuser = null;
			while (result.hasMoreElements()) {
				wtuser = (WTUser) result.nextElement();
				if (!wtuser.isDisabled())
					syncSave(wtuser);
			}
		} catch (WTException e) {
			e.printStackTrace();
		}
	}

	@Override
	public People getPeople(WTUser user) {
		return getPeople(user.getPersistInfo().getObjectIdentifier().getId());
	}

	@Override
	public People getPeople(long userid) {
		try {
			QuerySpec spec = new QuerySpec();
			Class mainClass = People.class;
			int mainClassPos = spec.addClassList(mainClass, true);
			spec.appendWhere(new SearchCondition(mainClass,
					"userReference.key.id", "=", userid),
					new int[] { mainClassPos });
			QueryResult qr = PersistenceHelper.manager.find(spec);
			if (qr.hasMoreElements()) {
				Object[] obj = (Object[]) qr.nextElement();
				return (People) obj[0];
			}
		} catch (QueryException e) {
			e.printStackTrace();
		} catch (WTException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public WTUser getUser(String id) {
		try {
			QuerySpec spec = new QuerySpec();
			Class mainClass = WTUser.class;
			int mainClassPos = spec.addClassList(mainClass, true);
			spec.appendWhere(new SearchCondition(mainClass, "name", "=", id),
					new int[] { mainClassPos });
			QueryResult qr = PersistenceHelper.manager.find(spec);
			if (qr.hasMoreElements()) {
				Object obj[] = (Object[]) qr.nextElement();
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
	 * @deprecated
	 */
	@Override
	public String[] getUserInfo(WTUser user) throws Exception {
		People people = UserHelper.service.getPeople(user);
		Department dept1 = people.getDepartment();

		String[] infos = new String[4];

		String useroid = StringUtil.checkNull(PersistenceHelper
				.getObjectIdentifier(user).getStringValue());
		String peopleoid = StringUtil.checkNull(PersistenceHelper
				.getObjectIdentifier(people).getStringValue());

		String deptoid = "";
		if (dept1 != null) {
			deptoid = StringUtil.checkNull(PersistenceHelper
					.getObjectIdentifier(dept1).getStringValue());
		}

		String id = StringUtil.checkNull(user.getName());
		String name = StringUtil.checkNull(user.getFullName());

		String departmentname = dept1 == null ? "" : StringUtil.checkNull(dept1
				.getName());
		String duty = StringUtil.checkNull(people.getDuty());
		String dutycode = StringUtil.checkNull(people.getDutyCode());
		String email = StringUtil.checkNull(user.getEMail());
		String temp = StringUtil.checkNull(people.getName());

		String values = useroid + "," + peopleoid + "," + deptoid + "," + id
				+ "," + name + "," + departmentname + "," + duty + ","
				+ dutycode + "," + email + "," + temp;

		infos[0] = departmentname;
		infos[1] = id;
		infos[2] = duty;
		infos[3] = values;

		return infos;
	}
	
	@Override
	public Department getDepartment(String code) throws Exception {
		QuerySpec qs = new QuerySpec(Department.class);
		qs.appendWhere(new SearchCondition(Department.class, Department.CODE,
				"=", code), new int[] { 0 });
		QueryResult qr = PersistenceHelper.manager.find(qs);
		if (qr.hasMoreElements()) {
			Department dept = (Department) qr.nextElement();
			return dept;
		}
		return null;
	}

	@Override
	public WTGroup getWTGroup(String code) throws Exception {
		QuerySpec qs = new QuerySpec(WTGroup.class);
		qs.appendWhere(new SearchCondition(WTGroup.class, WTGroup.NAME, "=",
				code), new int[] { 0 });
		QueryResult qr = PersistenceHelper.manager.find(qs);
		if (qr.hasMoreElements()) {
			WTGroup dept = (WTGroup) qr.nextElement();
			return dept;
		}
		return null;
	}

	@Override
	public String getDepartmentImg() throws WTException {
		WTUser user = (WTUser) SessionHelper.getPrincipal();
		return getDepartmentImg(user);
	}


	@Override
	public String getDepartmentImg(Department dp) {
		String imgURL = "\\Windchill\\jsp\\portal\\images\\img_menu\\";
		try {

			String img = "JY_LOGIN.gif";
			if (dp != null) {
				String departCode = dp.getCode();
				if (departCode.toUpperCase().startsWith("JY")
						|| departCode.toUpperCase().startsWith("UJ")) {
					img = "topMenu_left2.png";
				} else if (departCode.toUpperCase().startsWith("MS")) {
					img = "MS_LOGIN.gif";
				} else if (departCode.toUpperCase().startsWith("BJ")) {
					img = "BJ_LOGIN.gif";
				} else if (departCode.toUpperCase().startsWith("CJ")) {
					img = "CJ_LOGIN.gif";
				}
			}
			imgURL = imgURL + img;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return imgURL;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.org.service.UserService#getDepartmentTree(long)
	 */
	@Override
	public List<Map<String, Object>> getDepartmentTree(long dept) throws Exception {
		String tableName = "";
		ClassInfo classinfo = WTIntrospector.getClassInfo(Department.class);
		if (DatabaseInfoUtilities.isAutoNavigate(classinfo)) {
			tableName = DatabaseInfoUtilities.getBaseTableName(classinfo);
		} else {
			tableName = DatabaseInfoUtilities.getValidTableName(classinfo);
		}

		String parentKeyColumnName = DatabaseInfoUtilities.getValidColumnName(
				classinfo, "parentReference.key.id");
		String parentKeyColumnClassName = DatabaseInfoUtilities.getValidColumnName(
				classinfo, "parentReference.key.classname"); 
		String deptOIDColumnName = DatabaseInfoUtilities.getValidColumnName(
				classinfo, "thePersistInfo.theObjectIdentifier.id");
		String deptOIDColumnClassName = DatabaseInfoUtilities.getValidColumnName(
				classinfo, "thePersistInfo.theObjectIdentifier.classname");


		MethodContext methodcontext = null;
		WTConnection wtconnection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
		try {
			methodcontext = MethodContext.getContext();
			wtconnection = (WTConnection) methodcontext.getConnection();
			Connection con = wtconnection.getConnection();

			StringBuffer sb = null;

			if("Oracle".equals(dataStore)) {

				sb = new StringBuffer().append("select LEVEL,NAME,")
				.append(deptOIDColumnClassName+"||':'||"+deptOIDColumnName)
				.append(",CODE,SORT,")
				.append(parentKeyColumnClassName+"||':'||"+parentKeyColumnName)
				.append(", (select CODE from " +tableName+ " T2 where T2."+deptOIDColumnName+" = T1."+parentKeyColumnName+
						" AND T2."+deptOIDColumnClassName+" = T1."+parentKeyColumnClassName+") pcode")
				.append(" from "+tableName+" T1")
				.append(" start with ")
				.append(parentKeyColumnName)
				.append("=? connect by prior ")
				.append(deptOIDColumnName)
				.append("=")
				.append(parentKeyColumnName)
				.append(" ORDER SIBLINGS BY SORT");

			}else {

				sb = new StringBuffer().append("with cte (name, ")
				.append(deptOIDColumnClassName + ", " + deptOIDColumnName)
				.append(", code, sort, ")
				.append(parentKeyColumnClassName + ", " + parentKeyColumnName)
				.append(", level) as ( ")
				.append("select name, ")
				.append(deptOIDColumnClassName + ", " + deptOIDColumnName)
				.append(", code, sort, ")
				.append(parentKeyColumnClassName + ", " + parentKeyColumnName)
				.append(", 1 as level ")
				.append("from wcadmin."+tableName)
				.append(" where ")
				.append(parentKeyColumnName)
				.append("=? ")
				.append("union all ")
				.append("select a.name, a.")
				.append(deptOIDColumnClassName + ", a." + deptOIDColumnName)
				.append(", a.code, a.sort, a.")
				.append(parentKeyColumnClassName + ", a." + parentKeyColumnName)
				.append(", level+1 ")
				.append("from wcadmin.Department a, cte b ")
				.append("where a.")
				.append(parentKeyColumnName + "=b." + deptOIDColumnName + ") ")
				.append("select level, name, ")
				.append(deptOIDColumnClassName+"+':'+convert(nvarchar, "+deptOIDColumnName+")")
				.append(", code, sort, ")
				.append(parentKeyColumnClassName+"+':'+convert(nvarchar, "+parentKeyColumnName+")")
				.append(" from cte ")
				.append("order by sort ");

			}
			st = con.prepareStatement(sb.toString());
			st.setLong(1, 0);

			rs = st.executeQuery();

			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			while (rs.next()) {
				list.add(createRowMap(rs));
			}

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
            if ( rs != null ) {
                rs.close();
            }
            if ( st != null ) {
                st.close();
            }
			if (DBProperties.FREE_CONNECTION_IMMEDIATE
					&& !wtconnection.isTransactionActive()) {
				MethodContext.getContext().freeConnection();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.e3ps.org.service.UserService#getUserTree()
	 */
	@Override
	public List<Map<String, Object>> getUserTree() throws Exception {
		String tableName = "";
		ClassInfo classinfo = WTIntrospector.getClassInfo(Department.class);
		if (DatabaseInfoUtilities.isAutoNavigate(classinfo)) {
			tableName = DatabaseInfoUtilities.getBaseTableName(classinfo);
		} else {
			tableName = DatabaseInfoUtilities.getValidTableName(classinfo);
		}

		String parentKeyColumnName = DatabaseInfoUtilities.getValidColumnName(
				classinfo, "parentReference.key.id");
		String parentKeyColumnClassName = DatabaseInfoUtilities.getValidColumnName(
				classinfo, "parentReference.key.classname"); 
		String deptOIDColumnName = DatabaseInfoUtilities.getValidColumnName(
				classinfo, "thePersistInfo.theObjectIdentifier.id");
		String deptOIDColumnClassName = DatabaseInfoUtilities.getValidColumnName(
				classinfo, "thePersistInfo.theObjectIdentifier.classname");


		MethodContext methodcontext = null;
		WTConnection wtconnection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
		try {
			methodcontext = MethodContext.getContext();
			wtconnection = (WTConnection) methodcontext.getConnection();
			Connection con = wtconnection.getConnection();

			StringBuffer sb = null;

			if("Oracle".equals(dataStore)) {

				sb = new StringBuffer().append("select LEVEL,NAME,")
				.append(deptOIDColumnClassName+"||':'||"+deptOIDColumnName)
				.append(",CODE,SORT,")
				.append(parentKeyColumnClassName+"||':'||"+parentKeyColumnName+" from ")
				.append(tableName)
				.append(" start with ")
				.append(parentKeyColumnName)
				.append("=? connect by prior ")
				.append(deptOIDColumnName)
				.append("=")
				.append(parentKeyColumnName)
				.append(" ORDER SIBLINGS BY SORT");

			}else {

				sb = new StringBuffer().append("with cte (name, ")
				.append(deptOIDColumnClassName + ", " + deptOIDColumnName)
				.append(", code, sort, ")
				.append(parentKeyColumnClassName + ", " + parentKeyColumnName)
				.append(", level) as ( ")
				.append("select name, ")
				.append(deptOIDColumnClassName + ", " + deptOIDColumnName)
				.append(", code, sort, ")
				.append(parentKeyColumnClassName + ", " + parentKeyColumnName)
				.append(", 1 as level ")
				.append("from wcadmin."+tableName)
				.append(" where ")
				.append(parentKeyColumnName)
				.append("=? ")
				.append("union all ")
				.append("select a.name, a.")
				.append(deptOIDColumnClassName + ", a." + deptOIDColumnName)
				.append(", a.code, a.sort, a.")
				.append(parentKeyColumnClassName + ", a." + parentKeyColumnName)
				.append(", level+1 ")
				.append("from wcadmin.Department a, cte b ")
				.append("where a.")
				.append(parentKeyColumnName + "=b." + deptOIDColumnName + ") ")
				.append("select level, name, ")
				.append(deptOIDColumnClassName+"+':'+convert(nvarchar, "+deptOIDColumnName+")")
				.append(", code, sort, ")
				.append(parentKeyColumnClassName+"+':'+convert(nvarchar, "+parentKeyColumnName+")")
				.append(" from cte ")
				.append("order by sort ");

			}

			LOGGER.info(sb.toString());

			st = con.prepareStatement(sb.toString());
			st.setLong(1, 0);

			rs = st.executeQuery();

			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			while (rs.next()) {
				list.add(createRowMap(rs));
			}

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
            if ( rs != null ) {
                rs.close();
            }
            if ( st != null ) {
                st.close();
            }
			if (DBProperties.FREE_CONNECTION_IMMEDIATE
					&& !wtconnection.isTransactionActive()) {
				MethodContext.getContext().freeConnection();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.e3ps.org.service.UserService#getUserInfo(java.lang.String)
	 */
	@Override
	public Map<String, Object> getUserInfo(String name) throws WTException {

		// #. 쿼리 작성
		QuerySpec qs = new QuerySpec();

		int peopleIndex = qs.addClassList(People.class, true);
		int wtUserIndex = qs.addClassList(WTUser.class, true);
		int deptIndex = qs.addClassList(Department.class, true);

		qs.appendWhere(new SearchCondition(People.class,
				"departmentReference.key.id", Department.class,
				"thePersistInfo.theObjectIdentifier.id"), new int[] {
				peopleIndex, deptIndex });

		qs.appendAnd();
		qs.appendWhere(new SearchCondition(People.class,
				"userReference.key.id", WTUser.class,
				"thePersistInfo.theObjectIdentifier.id"), new int[] {
				peopleIndex, wtUserIndex });

		qs.appendAnd();
		qs.appendWhere(new SearchCondition(WTUser.class, WTUser.DISABLED,
				SearchCondition.IS_FALSE), new int[] { wtUserIndex });

		qs.appendAnd();
		qs.appendWhere(new SearchCondition(WTUser.class, WTUser.NAME,
				SearchCondition.EQUAL, name), new int[] { wtUserIndex });

		// #. 검색
		QueryResult qr = null;
		qr = PersistenceHelper.manager.find((StatementSpec) qs);

		Map<String, Object> userInfoMap = null;
		if (qr.hasMoreElements()) {
			Object[] objs = (Object[])qr.nextElement();
			People people = (People)objs[0];
			WTUser wtUser = (WTUser)objs[1];
			Department department = (Department)objs[2];
			
			userInfoMap = new HashMap<String, Object>();
			userInfoMap.put("people", people);
			userInfoMap.put("wtUser", wtUser);
			userInfoMap.put("department", department);
			userInfoMap.put("objs", objs);
		}
		
		return userInfoMap;
	}
	
	/* (non-Javadoc)
	 * @see com.e3ps.org.service.UserService#searchUserInfo(java.util.Map)
	 */
	@Override
	public WTUser getWTUser(String name) throws WTException {
		Map<String, Object> userInfo = UserHelper.service.getUserInfo(name);
		if (userInfo != null) {
			return (WTUser)userInfo.get("wtUser");
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.e3ps.org.service.UserService#getPeople(java.lang.String)
	 */
	@Override
	public People getPeople(String name) throws WTException {
		Map<String, Object> userInfo = UserHelper.service.getUserInfo(name);
		if (userInfo != null) {
			return (People)userInfo.get("people");
		} else {
			return null;
		}
	}

	/**
	 * 결과셋을 맵형태로 변환한다
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Map<String, Object> createRowMap(ResultSet rs) throws SQLException {
		Map<String, Object> rowMap = new HashMap<String, Object>();
		rowMap.put("level", rs.getInt(1));
		rowMap.put("name", rs.getString(2));
		rowMap.put("oid", rs.getString(3));
		rowMap.put("code", rs.getString(4));
		rowMap.put("sort", rs.getString(5));
		rowMap.put("poid", rs.getString(6));
		rowMap.put("pcode", rs.getString(7));
		
		return rowMap;
	}
	
	public static void syncUserScheduleMethod(){
		try {
			LOGGER.info("syncUserScheduleMethod");
			UserHelper.service.syncUserFromHr();
		} catch (WTException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void startSyncUserSchedule () throws WTException {
		
		LOGGER.info("startSyncUserSchedule");
		
		String scheduleQueueName = "updateUserSchedule";
		String targetClassName = StandardUserService.class.getName();
		String targetMethodName = "syncUserScheduleMethod";
		
		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(ScheduleItem.class,true);
		int jj = qs.addClassList(ScheduleQueueEntry.class, false);
		qs.appendWhere(new SearchCondition(ScheduleItem.class,"qeReference.key.id",ScheduleQueueEntry.class,"thePersistInfo.theObjectIdentifier.id"),new int[]{ii,jj});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(ScheduleItem.class,ScheduleItem.QUEUE_NAME,"=",scheduleQueueName),new int[]{ii});
		QueryResult qr = PersistenceHelper.manager.find(qs);
		if(qr.size()==0){
			ScheduleItem item = ScheduleItem.newScheduleItem();     
	        item.setQueueName ( scheduleQueueName );
		    item.setTargetClass ( targetClassName );
		    item.setTargetMethod ( targetMethodName );
		    item.setItemName ( scheduleQueueName );
		    item.setItemDescription ( scheduleQueueName );  
		    
		    item.setStartDate (new Timestamp (System.currentTimeMillis ()));
		    item.setToBeRun (Long.MAX_VALUE); //횟수
		    item.setPeriodicity (60*60*12); //주기 (초)
		    item = SchedulingHelper.service.addItem (item, null);  
		}
	}
	
	public static void main(String args[]){
		try {
			UserHelper.service.startSyncUserSchedule ();
		} catch (WTException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getDepartmentImg(WTUser user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WTUser createWTUser(Map hash) throws WTException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WTUser updateWTUser(Map hash) throws WTException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void syncUserFromHr() throws WTException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<WTUser> getRoleUsers(String string) throws WTException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageQueryBrokerResult<Object[]> searchUserInfo(Map<String, Object> searchConditionMap) throws WTException {
		try {

			// #. 파라메터 조회
			int tpage = TypeUtil.intValue(searchConditionMap.get("tpage"), 1);
			long sessionid = TypeUtil.longValue(searchConditionMap.get("sessionid"));
			int psize = TypeUtil.intValue(searchConditionMap.get("psize"), 20);
			String searchChk = TypeUtil.stringValue(searchConditionMap.get("searchChk")).trim();
			String sortKey = TypeUtil.stringValue(searchConditionMap.get("sortKey")).trim();
			String sortType = TypeUtil.stringValue(searchConditionMap.get("sortType")).trim();
			
			String deptOid = TypeUtil.stringValue(searchConditionMap.get("deptOid")).trim();
			Department dept = null;
			if (deptOid != null && deptOid.length() > 0 && !deptOid.equals("null") && !deptOid.equals("root")) {
				dept = (Department)WCUtil.getPersistable(deptOid);
			}
			String userFullName = TypeUtil.stringValue(searchConditionMap.get("userFullName")).trim();
			String userName = TypeUtil.stringValue(searchConditionMap.get("userName")).trim();
			
			if (sortType == null || sortType.length() == 0) {
				sortType = "false";
			}
			
			// #. 쿼리 작성
			QuerySpec qs = new QuerySpec();

			int peopleIndex = qs.addClassList(People.class, true);
			int wtUserIndex = qs.addClassList(WTUser.class, true);
			int deptIndex = qs.addClassList(Department.class, true);

			qs.appendWhere(new SearchCondition(
					People.class, "departmentReference.key.id", 
					Department.class, "thePersistInfo.theObjectIdentifier.id"), 
				new int[] { peopleIndex, deptIndex });
			
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(
					People.class, "userReference.key.id", 
					WTUser.class, "thePersistInfo.theObjectIdentifier.id"), 
				new int[] { peopleIndex, wtUserIndex });
			
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(WTUser.class, 
					WTUser.DISABLED, SearchCondition.IS_FALSE),
				new int[] { wtUserIndex });

			if (dept != null) {
				if (qs.getConditionCount() > 0) qs.appendAnd();
				qs.appendOpenParen();
				qs.appendWhere(new SearchCondition(Department.class, 
						"parentReference.key.id", SearchCondition.EQUAL, WCUtil.getLongOid(dept)), 
					new int[] { deptIndex });
				qs.appendOr();
				qs.appendWhere(new SearchCondition(Department.class, 
						"thePersistInfo.theObjectIdentifier.id", SearchCondition.EQUAL, WCUtil.getLongOid(dept)), 
					new int[] { deptIndex });
				qs.appendCloseParen();
			}
			
			
			if (userFullName.length() > 0) {
				if (qs.getConditionCount() > 0) qs.appendAnd();
				StringSearch stringSearch = new StringSearch(People.NAME);
				stringSearch.setValue(userFullName);
				qs.appendWhere(stringSearch.getSearchCondition(People.class),
					new int[] { peopleIndex });
			}
			
			if (userName.length() > 0) {
				if (qs.getConditionCount() > 0) qs.appendAnd();
				StringSearch stringSearch = new StringSearch(WTUser.NAME);
				stringSearch.setValue(userName);
				qs.appendWhere(stringSearch.getSearchCondition(WTUser.class),
					new int[] { wtUserIndex });
			}
			
			if (sortKey.length() > 0) {
				qs.appendOrderBy(
						new OrderBy(new ClassAttribute(People.class, sortKey), Boolean.getBoolean(sortType)),
						new int[] { peopleIndex });
			}else{
				qs.appendOrderBy(
						new OrderBy(new ClassAttribute(People.class, People.DUTY_CODE), false),
						new int[] { peopleIndex });
			}
			
			// #. 검색 (pSize 가 0 이면 전체 조회)
			PageQueryBrokerResult<Object[]> result = null;
			
			if (psize > 0) {
				PageQueryBroker broker = new PageQueryBroker(qs, sessionid, "", tpage, psize);
				if (sessionid > 0L && searchChk.equals("false")) {
					broker.setSessionid(sessionid);
				} else {
					broker.setSessionid(0);
				}
				result = broker.getPageQueryBrokerResult();
			} else {
				List<Object[]> list = new ArrayList<Object[]>();
				QueryResult qr = PersistenceHelper.manager.find((StatementSpec) qs);
				while (qr.hasMoreElements()) {
					list.add((Object[])qr.nextElement());
				}
				result = new PageQueryBrokerResult<Object[]>();
				result.setCpage(1);
				result.setTotal(qr.size());
				result.setList(list);
			}
			return result;
		} catch (WTPropertyVetoException e) {
			throw new WTException(e);
		}
	}

}
