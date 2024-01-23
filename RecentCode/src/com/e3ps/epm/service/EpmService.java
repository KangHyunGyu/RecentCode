package com.e3ps.epm.service;

import java.util.Map;

import wt.epm.EPMDocument;
import wt.epm.EPMDocumentMaster;
import wt.epm.EPMDocumentType;
import wt.method.RemoteInterface;
import wt.part.WTPart;
import wt.util.WTException;

@RemoteInterface
public interface EpmService {

	public abstract EPMDocument createEpmAction(Map<String, Object> reqMap) throws Exception;
	
	public abstract EPMDocument modifyEpmAction(Map<String, Object> reqMap) throws Exception;
	
	public abstract void deleteEpmAction(Map<String, Object> reqMap) throws Exception;

	public abstract boolean isFileNameCheck(String fileName);

	public abstract EPMDocumentType getEPMDocumentType(String fileName);

	public abstract void createDesignEPM(String cadType, WTPart part, String container, String location) throws Exception;

	public abstract EPMDocument createDesignEpmAction(Map<String, Object> reqMap) throws Exception;

	public abstract EPMDocument createControlEPM(WTPart part, String container, String location, String primary) throws Exception;

	public abstract Map<String, Object> createMultiEpmAction(Map<String, Object> reqMap) throws Exception;

	public abstract EPMDocument reviseEpmAction(Map<String, Object> reqMap) throws Exception;

	public abstract EPMDocument withdrawEpmAction(Map<String, Object> reqMap) throws Exception;

	public abstract void createCADBOMAction(Map<String, Object> reqMap) throws Exception;

	void changePartNO(String oid);
	
	public abstract EPMDocument createEpmActionNT(Map<String, Object> reqMap) throws Exception;

	public abstract void epmNonPublishBatch(String cadType, String product);

	public abstract void publish(EPMDocument epm);
	
	EPMDocument getLastEPMDocument(String number) throws WTException;
	
	public EPMDocument getEPMDocument(String number, String version)
			throws WTException;
	
	public EPMDocument getEPMDocument(String number) throws Exception;
	
	public EPMDocument getEPMDocument(WTPart _part) throws Exception;
	
	public EPMDocument getEPM2D(EPMDocumentMaster master);

	public void createEpmFolderAction(Map<String, Object> reqMap) throws Exception;

	void eventListener(Object obj, String event);
}
