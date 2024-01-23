package com.e3ps.org.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wt.method.RemoteInterface;
import wt.org.WTGroup;
import wt.org.WTPrincipal;
import wt.org.WTUser;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.util.WTException;

import com.e3ps.common.web.PageQueryBrokerResult;
import com.e3ps.org.Department;
import com.e3ps.org.People;

/**
 * @author Administrator
 *
 */
@RemoteInterface
public interface UserService {
	
	void eventListener(Object _obj, String _event);
	
	void syncStore(WTUser _user);
	
	void syncModify(WTUser _user);
	
	People getPeopleByFullName(String fullName);
	
	WTUser getChiefUser(Department depart);
	
	QuerySpec getUserSearch(String name) throws QueryException;
	
	
	void setAllUserName()throws Exception;
	
	void syncSave(WTUser _user);
	
	void syncDelete(WTUser _user);
	
	void syncWTUser();
	
	People getPeople(WTUser user);
	
	People getPeople(long userid);
	
	WTUser getUser(String id);
	
	String[] getUserInfo(WTUser user) throws Exception;
	
	Department getDepartment(String code) throws Exception;
	
	WTGroup getWTGroup(String code) throws Exception;
	
	String getDepartmentImg() throws WTException;
	
	String getDepartmentImg(WTUser user);
	
	String getDepartmentImg(Department dp);

	/**
	 * 부서 Tree를 조회한다
	 * @param dept
	 * @return level, name, oid, code, sort, poid 가 key 인 Map의 List
	 * @throws Exception
	 */
	List<Map<String, Object>> getDepartmentTree(long dept) throws Exception;
	
	/**
	 * 사용자 tree를 얻는다
	 * @return level, name, oid, code, sort, poid 가 key 인 Map의 List
	 * @throws Exception
	 */
	List<Map<String, Object>> getUserTree() throws Exception;
	Map<String, Object> getUserInfo(String name) throws WTException;
	
	/**
	 * 사용자 식별자로 WTUser 객체를 얻는다
	 * @param name
	 * @return
	 * @throws WTException
	 */
	WTUser getWTUser(String name) throws WTException;
	
	/**
	 * 사용자 식별자로 People 객체를 얻는다
	 * @param name
	 * @return
	 * @throws WTException
	 */
	People getPeople(String name) throws WTException;
	
	/**
	 * WTUser 를 생성한다
	 * @param hash
	 * @return
	 * @throws WTException
	 */
	WTUser createWTUser(Map hash) throws WTException;

	/**
	 * WTUser 를 수정한다
	 * @param hash
	 * @return
	 * @throws WTException
	 */
	WTUser updateWTUser(Map hash) throws WTException;

	/**
	 * HR정보와 PDM사용자 정보를 업데이트한다.
	 * @param hash
	 * @return
	 * @throws WTException
	 */
	void syncUserFromHr() throws WTException;

	void startSyncUserSchedule() throws WTException;

	/**
	 * @Method Name : getRoleUsers
	 * @writer : wslee
	 * @date : 2015. 7. 30.
	 * @Method desc : Key값으로 Role 유저를 가져온다.
	 * @param string
	 * @return
	 * @throws WTException 
	 */
	ArrayList<WTUser> getRoleUsers(String string) throws WTException;

	public PageQueryBrokerResult<Object[]> searchUserInfo(Map<String, Object> searchConditionMap)
			throws WTException;
}
