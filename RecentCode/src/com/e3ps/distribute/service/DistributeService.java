package com.e3ps.distribute.service;

import java.util.List;
import java.util.Map;

import com.e3ps.distribute.DistributeDocument;
import com.e3ps.distribute.DistributeRegToPartLink;
import com.e3ps.distribute.DistributeRegistration;
import com.e3ps.distribute.DistributeToPartLink;
import com.e3ps.distribute.bean.DistributeData;

import wt.method.RemoteInterface;

@RemoteInterface
public interface DistributeService {
	
	public List<Map<String,Object>> createDistributeAction(Map<String, Object> reqMap)  throws Exception;

//	public void createDistributePartLink(Map<String,Object> reqMap, DistributeDocument distribute) throws Exception;
	
	public List<DistributeData> searchDistributeList(Map<String, Object> reqMap) throws Exception;
	
	public void deleteDistributeAction(Map<String, Object> reqMap) throws Exception ;
	
	public void eventListener(Object obj, String event);

	public void modifyDistributeAction(Map<String, Object> reqMap) throws Exception;

//	void createDistributePartToEpmLink(DistributeToPartLink link) throws Exception;

	void uploadDistFile(List<Map<String, Object>> ftpFileList) throws Exception;

	List<Map<String, Object>> requestFormDistributeAction(Map<String, Object> reqMap) throws Exception;

	public List<Map<String, Object>> createRegistrationDistributeAction(Map<String, Object> reqMap) throws Exception;

	public void createDistributeRegPartLink(Map<String, Object> reqMap, DistributeRegistration distributeReg) throws Exception;

	void createDistributeRegPartToEpmLink(DistributeRegToPartLink distRegPartLink) throws Exception;

	void modifyDistributeRegistrationAction(Map<String, Object> reqMap) throws Exception;

	void deleteDistributePartLink(DistributeRegistration distributeReg) throws Exception;

	void deleteDistributeRegistrationAction(Map<String, Object> reqMap) throws Exception;

}
