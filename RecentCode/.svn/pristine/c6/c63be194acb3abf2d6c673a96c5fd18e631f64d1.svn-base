package com.e3ps.change.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wt.doc.WTDocument;
import wt.epm.EPMDocument;
import wt.fc.Persistable;
import wt.fc.QueryResult;
import wt.method.RemoteInterface;
import wt.part.WTPart;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;
import wt.vc.Versioned;

import com.e3ps.approval.ApprovalMaster;
import com.e3ps.approval.CommonActivity;
import com.e3ps.change.EChangeActivity;
import com.e3ps.change.EChangeActivityDefinition;
import com.e3ps.change.EChangeActivityDefinitionRoot;
import com.e3ps.change.EChangeOrder2;
import com.e3ps.change.EcoPartLink;
import com.e3ps.common.content.uploader.WBFile;
import com.e3ps.doc.E3PSDocument;

@RemoteInterface
public interface ChangeECOService {
	
	public static final Logger logger = LoggerFactory.getLogger(ChangeECOService.class);
	
	String deleteEco(String oid)throws Exception;
	
	void startActivity(Persistable eco)throws Exception;
	
	void commitActivity(EChangeActivity activity, String state, String desc)throws Exception;
	
	EChangeOrder2 updateCompleteApproval(EChangeOrder2 eco, Hashtable hash)throws Exception;
	
	void commitECO(CommonActivity com, String state, EChangeOrder2 eco)	throws Exception;
	
	void commitPartStateChange(Vector vecPart) throws Exception;
	
	void reviseObject(Vector vec) throws Exception;
	
	Vector getEoReviseHistory(EcoPartLink link);
	
	void reviseDelete(String[] linkOid, String delType) throws WTException;
	
	QueryResult getRequestOrderLink(EChangeOrder2 eco);
	
	QueryResult searchECOPartLink(EChangeOrder2 eco, String partType);
	
	QueryResult searchECOPartResultLink(EChangeOrder2 eco);
	
	void startECOApprove(EChangeOrder2 eco);
	
	void sendNotice(EChangeOrder2 eco) throws Exception;
	
	ArrayList getECOActivityMaster(EChangeOrder2 eco) throws Exception;
	
	ArrayList getECOActivity(EChangeOrder2 eco) throws Exception;
	
	void registerNotice(ApprovalMaster master, String user);
	
	void autoCadPdfPSend(EPMDocument epm);
	
	void gPartPdfSend(WTPart part);
	
	void rejectEco(EChangeOrder2 eco);
	
	void publishDelete(EPMDocument epm);

	List<EChangeActivityDefinitionRoot> getActiveDefinitionRoot()
			throws WTException;

	List<EChangeActivityDefinition> getActiveDefinition(long rootOid)
			throws WTException;

	EChangeActivityDefinitionRoot createRootDefinition(Map<String, Object> map)throws WTException;

	EChangeActivityDefinitionRoot updateRootDefinition(Map<String, Object> map) throws WTException;

	EChangeActivityDefinitionRoot deleteRootDefinition(Map<String, Object> map) throws WTException;

	EChangeActivityDefinition createDefinition(Map<String, Object> map) throws WTException;

	EChangeActivityDefinition updateDefinition(Map<String, Object> map) throws WTException;

	EChangeActivityDefinition deleteDefinition(Map<String, Object> reqMap) throws WTException;

	ArrayList<Versioned> getTargetList(EChangeOrder2 eco) throws WTException;

	void connectEca(E3PSDocument doc, Map hash) throws WTException,
			WTPropertyVetoException;
	
	void connectEcaPart(WTPart doc, Map hash) throws WTException,
	WTPropertyVetoException;

	String deleteNotifyItem(String oid) throws Exception;

	void updateNotifyItem(Map map) throws WTException;

	List<EChangeOrder2> searchEcoFromTraget(Versioned target) throws Exception;

	EChangeOrder2 stopECO(Map<String, Object> reqMap) throws WTException;

	EChangeOrder2 restartECO(Map<String, Object> reqMap) throws WTException;

	QueryResult getStopStartHistory(EChangeOrder2 eco) throws WTException;
	
	EChangeOrder2 createEco(Map<String, Object> reqMap)throws WTException;
	
	void connectEca(E3PSDocument doc, String activeOid, String activeLinkOid, String activeLinkType) throws WTException,
	WTPropertyVetoException;
	
	public EChangeOrder2 updateEco(Map<String, Object> reqMap) throws WTException;
	
	public Map<String,Object> batchECOPartRevise(Map<String, Object> reqMap) throws Exception;
	
}
