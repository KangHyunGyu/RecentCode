package com.e3ps.org.service;

import java.util.Map;

import wt.method.RemoteInterface;

@RemoteInterface
public interface DepartmentService {

	boolean createDepartmentAction(Map<String, Object> reqMap) throws Exception;
	
	boolean modifyDepartmentAction(Map<String, Object> reqMap) throws Exception;
	
	boolean deleteDepartmentAction(Map<String, Object> reqMap) throws Exception;

}
