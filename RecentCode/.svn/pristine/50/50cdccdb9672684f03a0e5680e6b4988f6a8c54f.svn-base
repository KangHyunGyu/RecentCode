package com.e3ps.stagegate.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.e3ps.stagegate.SGObject;
import com.e3ps.stagegate.SGObjectMaster;
import com.e3ps.stagegate.StageGate;

import wt.method.RemoteInterface;

@RemoteInterface
public interface SgService {
	public abstract StageGate createGateAction(Map<String, Object> reqMap) throws Exception;
	
	public abstract void deleteGateAction(StageGate sg) throws Exception;
	
	public abstract void modifyObjectValue(Map<String, Object> reqMap) throws Exception;
	
	public abstract void uploadFile(Map<String, Object> reqMap) throws Exception;

	public abstract void upsertObjectValue(SGObject obj, Map<String, Object> valueMap) throws Exception;
	
	public abstract void modifyLight(Map<String, Object> map) throws Exception;
	
	public abstract SGObjectMaster revisionGate(Map<String, Object> reqMap) throws Exception;
	
	public void exportExcelRisk(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	public void exportExcelCStop(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	public void importExcelCStop(Map<String, Object> reqMap) throws Exception;
	
	public void fileUpload(Map<String, Object> reqMap) throws Exception;
	
	public void uploadPrimary(Map<String, Object> fileMap) throws Exception;
}
