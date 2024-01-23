package com.e3ps.common.service;

import java.util.Map;

import wt.enterprise.RevisionControlled;
import wt.lifecycle.LifeCycleManaged;
import wt.method.RemoteInterface;

@RemoteInterface
public interface CommonService {

	void changeRevisionName(RevisionControlled rc, String name) throws Exception;

	LifeCycleManaged setVersiondDefault(LifeCycleManaged ver, Map<String, Object> reqMap) throws Exception;

	void saveFolderAction(Map<String, Object> reqMap) throws Exception;

	public void changeLCState(LifeCycleManaged lcm, String state)  throws Exception;

	
}
