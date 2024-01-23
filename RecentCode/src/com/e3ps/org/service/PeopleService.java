package com.e3ps.org.service;

import java.util.Map;

import wt.org.WTUser;

public interface PeopleService {

	void setChiefAction(Map<String, Object> reqMap) throws Exception;
	
	void setDutyAction(Map<String, Object> reqMap) throws Exception;
	
	void setDepartmentAction(Map<String, Object> reqMap) throws Exception;
	
	void eventListener(Object obj, String event);
	
	void syncStore(WTUser user);
	
	void syncModify(WTUser user);
	
	void syncDelete(WTUser user);
	
	void syncWTUser();

	void deleteWTUser(WTUser user);
}
