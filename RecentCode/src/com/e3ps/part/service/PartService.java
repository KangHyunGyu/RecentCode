package com.e3ps.part.service;

import java.util.List;
import java.util.Map;

import com.e3ps.doc.E3PSDocument;

import wt.epm.build.EPMBuildRule;
import wt.method.RemoteInterface;
import wt.part.WTPart;
import wt.part.WTPartDescribeLink;
import wt.part.WTPartMaster;
import wt.util.WTException;

@RemoteInterface
public interface PartService {

	public abstract WTPart createPartAction(Map<String, Object> reqMap) throws Exception;
	
	public abstract WTPart modifyPartAction(Map<String, Object> reqMap) throws Exception;
	
	public abstract void deletePartAction(Map<String, Object> reqMap) throws Exception;

	public abstract WTPart revisePartAction(Map<String, Object> reqMap) throws Exception;

	public abstract WTPart withdrawPartAction(Map<String, Object> reqMap) throws Exception;

	public abstract WTPart createProjectPartAction(String pjtNo, String pjtName) throws Exception;

	public abstract WTPart modifyPartAttributeAction(Map<String, Object> reqMap) throws Exception;

	public abstract void setWithdrawnPart(WTPart part, String endDate) throws Exception;
	
	WTPart getLastWTPart(WTPartMaster master) throws Exception;
	
	public WTPart getPart(String number, String version) throws WTException;
	
	public WTPart getPart(String number) throws Exception;
	
	public EPMBuildRule getBuildRule(Object obj) throws WTException;
	
	public abstract WTPart createPartAction2(Map<String, Object> reqMap) throws Exception;

	List<Map<String, Object>> createPartMultiAction(Map<String, Object> reqMap) throws Exception;

	void deleteWTPartDescribeLink(WTPart part) throws Exception;

	void addPartToDocLink(WTPart part, List<Map<String, Object>> docList, boolean isDelete)
			throws Exception;
	
	public void setAttributes(WTPart part, Map<String, Object> reqMap);
	
	WTPart addRevision(Map<String, Object> reqMap) throws Exception;

	List<String> checkPartRevision(WTPart part) throws Exception;
	
}
