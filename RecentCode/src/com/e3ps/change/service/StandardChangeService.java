package com.e3ps.change.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.e3ps.approval.CommonActivity;
import com.e3ps.approval.service.ApprovalHelper;
import com.e3ps.approval.service.ApprovalService;
import com.e3ps.change.ApplyChangeState;
import com.e3ps.change.ApplyHistory;
import com.e3ps.change.EChangeActivity;
import com.e3ps.change.EChangeActivityDefinition;
import com.e3ps.change.EChangeActivityDefinitionRoot;
import com.e3ps.change.EChangeContents;
import com.e3ps.change.EChangeOrder2;
import com.e3ps.change.EChangeRequest2;
import com.e3ps.change.EOEul;
import com.e3ps.change.EcoPartLink;
import com.e3ps.change.EcrPartLink;
import com.e3ps.change.EcrTargetLink;
import com.e3ps.change.EulBaselineLink;
import com.e3ps.change.EulPartLink;
import com.e3ps.change.beans.ChangeECRSearch;
import com.e3ps.change.editor.BEContext;
import com.e3ps.change.editor.EOActionTempAssyData;
import com.e3ps.change.editor.EOActionTempItemData;
import com.e3ps.change.editor.service.EulPartHelper;
import com.e3ps.change.util.EChangeMailForm;
import com.e3ps.common.content.service.CommonContentHelper;
import com.e3ps.common.iba.IBAUtil;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.service.CommonHelper;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.SequenceDao;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.common.web.ParamUtil;
import com.e3ps.doc.E3PSDocument;
import com.e3ps.org.People;
import com.e3ps.part.service.PartHelper;
import com.e3ps.queue.E3PSQueueHelper;

import wt.clients.folder.FolderTaskLogic;
import wt.clients.vc.CheckInOutTaskLogic;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.fc.collections.WTArrayList;
import wt.fc.collections.WTCollection;
import wt.fc.collections.WTHashSet;
import wt.fc.collections.WTSet;
import wt.folder.Folder;
import wt.folder.FolderEntry;
import wt.folder.FolderHelper;
import wt.inf.container.WTContainerRef;
import wt.lifecycle.LifeCycleHelper;
import wt.lifecycle.LifeCycleManaged;
import wt.lifecycle.LifeCycleTemplate;
import wt.lifecycle.State;
import wt.org.WTPrincipalReference;
import wt.org.WTUser;
import wt.part.Quantity;
import wt.part.QuantityUnit;
import wt.part.WTPart;
import wt.part.WTPartMaster;
import wt.part.WTPartUsageLink;
import wt.pdmlink.PDMLinkProduct;
import wt.pds.StatementSpec;
import wt.pom.Transaction;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.StandardManager;
import wt.session.SessionHelper;
import wt.util.WTException;
import wt.util.WTProperties;
import wt.util.WTPropertyVetoException;
import wt.vc.VersionControlHelper;
import wt.vc.Versioned;
import wt.vc.baseline.BaselineHelper;
import wt.vc.baseline.ManagedBaseline;
import wt.vc.wip.CheckoutLink;
import wt.vc.wip.WorkInProgressHelper;

/**
 * <pre>
 * Change 서비스 서버 구현체
 * 
 * [변경이력]
 * - 2015.02.04 (dlee) : 클래스 생성
 * </pre>
 */
public class StandardChangeService extends StandardManager implements ChangeService {

	private static final long serialVersionUID = 1L;
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.CHANGE.getName());
	
	/**
	 * Default factory for the class.
	 * 
	 * @return
	 * @throws WTException
	 */
	public static StandardChangeService newStandardChangeService()
			throws WTException {
		StandardChangeService instance = new StandardChangeService();
		instance.initialize();
		return instance;
	}
	
	@Override
	public ArrayList<Versioned> getTargetList(EChangeRequest2 ecr) throws WTException{
		ArrayList<Versioned> list = new ArrayList<Versioned>();
		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(EcrTargetLink.class,true);
		qs.appendWhere(new SearchCondition(EcrTargetLink.class,"roleAObjectRef.key.id","=",ecr.getPersistInfo().getObjectIdentifier().getId()),new int[]{ii});
		qs.appendOrderBy(new OrderBy(new ClassAttribute(EcrTargetLink.class,"roleAObjectRef.key.classname"),false),new int[]{ii});
		QueryResult qr = PersistenceHelper.manager.find(qs);
		while(qr.hasMoreElements()){
			Object[] o = (Object[])qr.nextElement();
			EcrTargetLink link = (EcrTargetLink)o[0];
			list.add(link.getTarget());
		}
		return list;
	}

	@Override
	public void deleteEcr(String oid) throws WTException {
		Transaction trx = new Transaction();
		try {
			trx.start();
			if (oid != null) {
				ReferenceFactory f = new ReferenceFactory();
				EChangeRequest2 changeEcr = (EChangeRequest2) f.getReference(oid).getObject();
				changeEcr = (EChangeRequest2)PersistenceHelper.manager.refresh(changeEcr);
				deleteECRPartLink(changeEcr);
				ApprovalHelper.service.removeProcess(changeEcr);
				PersistenceHelper.manager.delete(changeEcr); 
			}
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
	}


	@Override
	public void commitRequest(EChangeRequest2 ecr, String state)
			throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();

			if (ApprovalService.MASTER_APPROVED.equals(state)) {
				//WTUser user = (WTUser) ecr.getWorker();
				CommonActivity ca = CommonActivity.newCommonActivity();
				ca.setGubun(ACTIVE_WORK_REGIST);
				ca.setTitle("[ " + ecr.getRequestNumber() + " - 결재완료 ] "
						+ ACTIVE_WORK_REGIST_TITLE);
				ca.setOwner(SessionHelper.manager.getPrincipalReference());
				ca.setContainer(WCUtil.getPDMLinkProduct());
				ca = (CommonActivity) PersistenceHelper.manager.save(ca);

				//ecr.setWork(ca);
				ecr = (EChangeRequest2) PersistenceHelper.manager.modify(ecr);

				//ApprovalHelper.service.registWork(ca, user);
			}
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw e;
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
	}

	@Override
	public EChangeOrder2 registCompleteApproval(EChangeOrder2 eco,
			Hashtable hash) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();
			String activityOid = ParamUtil.get(hash, "activityOid");
			CommonActivity ca = null;
			if(activityOid==null || activityOid.length()==0){
				ca = CommonActivity.newCommonActivity();
				ca.setGubun(ACTIVE_WORK_LAST_APPROVAL);
				ca.setTitle(eco.getName());
				ca.setOwner(SessionHelper.manager.getPrincipalReference());
				ca.setContainer(WCUtil.getPDMLinkProduct());
				ca = (CommonActivity) PersistenceHelper.manager.save(ca);
	
				eco.setComplete(ca);
				//eco.setOrderState(ECO_COMPLETE);
				eco = (EChangeOrder2) PersistenceHelper.manager.modify(eco);
				ApprovalHelper.service.registApprovalChange(eco, hash);
				//ApprovalHelper.service.registApproval(eco, hash);
			}else{
				ca = (CommonActivity)CommonUtil.getObject(activityOid);
				ApprovalHelper.service.registApproval(ca, hash);
			}

			trx.commit();
			trx = null;

			return eco;

		} catch (Exception e) {
			throw e;
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
	}

	@Override
	public String approveEul(Hashtable hash) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();

			String oid = (String) hash.get("oid");

			ReferenceFactory rf = new ReferenceFactory();
			EOEul eul = (EOEul) rf.getReference(oid).getObject();

			ApprovalHelper.service.registApproval(eul, hash);

			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw e;
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return "제출 되었습니다.";
	}

	@Override
	public void commitEOEul(EOEul eul, String state) throws Exception {
		// if(ApprovalHelper.MASTER_APPROVED.equals(state)){
		saveEulData(eul);
		// }
	}

	@Override
	public Hashtable getSapData(EOEul eul) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder documentbuilder = factory.newDocumentBuilder();
		InputStream inStream = stringToInputStream(eul.getXml().getContents());
		Document document = documentbuilder.parse(inStream);
		inStream.close();

		return null;//EditorServerHelper.service.getSapData(document);
	}

	@Override
	public void saveEulData(EOEul eul) throws Exception {
		try {

			Hashtable hash = getSapData(eul);

			EChangeOrder2 order = eul.getEco();
			//String purpose = order.getPurpose();
			// boolean revision = purpose.indexOf("BOM") < 0;
			boolean revision = false;
			saveChangeData(hash, revision, eul);

		} catch (Exception ex) {
			throw new WTException(ex);
		}
	}

	@Override
	public void saveChangeData(Hashtable hash, boolean revision, EOEul eul) {
		ApplyHistory history = null;

		try {

			history = ApplyHistory.newApplyHistory();
			history.setOwner(SessionHelper.manager.getPrincipalReference());
			if (eul != null) {
				history.setEul(eul);
			}
			history = (ApplyHistory) PersistenceHelper.manager.save(history);

			LOGGER.info("==설계변경 적용 시작==");
			LOGGER.info("revision : " + revision);

			Enumeration en = hash.keys();

			boolean flag = true;

			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				EOActionTempAssyData eta = (EOActionTempAssyData) hash.get(key);
				if (!applyChangeData(eul, eta, revision, history)) {
					flag = false;
				}
			}

			if (eul != null) {
				ReferenceFactory rf = new ReferenceFactory();
				WTPart part = (WTPart) rf.getReference(eul.getTopAssyOid())
						.getObject();
				createBaseline(part, eul);
			}

			if (!flag) {
				history.setStatus("ERROR");
				history.setError("Item Error");
			} else {
				history.setStatus("SUCCESS");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				history.setStatus("ERROR");
				history.setError(ex.getLocalizedMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			if (history != null) {
				try {
					PersistenceHelper.manager.modify(history);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public boolean applyChangeData(EOEul eul, EOActionTempAssyData data,
			boolean revision, ApplyHistory history) throws Exception {
		ApplyChangeState changeState = null;
		WTUser user = null;
		try {
			changeState = ApplyChangeState.newApplyChangeState();
			changeState.setHistory(history);
			changeState.setAssyNumber(data.assyPart);
			changeState.setAssyVersion(data.nextAssyVersion);
			changeState = (ApplyChangeState) PersistenceHelper.manager
					.save(changeState);

			user = (WTUser) SessionHelper.manager.getPrincipal();
			SessionHelper.manager.setPrincipal("wcadmin");
			appleyChangeData(eul, data, revision);

			changeState.setStatus("SUCCESS");
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			changeState.setStatus("ERROR");
			changeState.setError(e.getLocalizedMessage());
			return false;
		} finally {
			if (changeState != null) {
				try {
					PersistenceHelper.manager.modify(changeState);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			if (user != null) {
				SessionHelper.manager.setPrincipal(user.getName());
			}
		}
	}

	public ApplyChangeState applyChangeData(EOEul eul,
			EOActionTempAssyData data, boolean revision,
			ApplyChangeState changeState) throws Exception {
		WTUser user = null;

		try {
			changeState.setAssyNumber(data.assyPart);
			changeState.setAssyVersion(data.nextAssyVersion);

			user = (WTUser) SessionHelper.manager.getPrincipal();
			SessionHelper.manager.setPrincipal("wcadmin");

			WTPart part = appleyChangeData(eul, data, revision);

			changeState.setStatus("SUCCESS");
			changeState.setError("");

			try {
				ManagedBaseline baseline = (ManagedBaseline) eul.getBaseline();
				BaselineHelper.service.addToBaseline(part, baseline);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
			changeState.setStatus("ERROR");
			changeState.setError(e.getLocalizedMessage());

		} finally {
			if (changeState != null) {
				try {
					changeState = (ApplyChangeState) PersistenceHelper.manager
							.modify(changeState);

					ApplyHistory ah = changeState.getHistory();
					if (isAllSuccess(ah)) {
						ah.setStatus("SUCCESS");
						PersistenceHelper.manager.modify(ah);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			if (user != null) {
				SessionHelper.manager.setPrincipal(user.getName());
			}
		}

		return changeState;
	}

	@Override
	public boolean isAllSuccess(ApplyHistory ah) throws Exception {
		QuerySpec qs = new QuerySpec(ApplyChangeState.class);
		qs.appendWhere(new SearchCondition(ApplyChangeState.class,
				"historyReference.key.id", "=", ah.getPersistInfo()
						.getObjectIdentifier().getId()), new int[] { 0 });
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(ApplyChangeState.class, "status",
				"=", "ERROR"), new int[] { 0 });
		QueryResult qr = PersistenceHelper.manager.find(qs);
		return qr.size() == 0;
	}

	@Override
	public void cancelChangeData(EOEul eul, ApplyChangeState acs)
			throws Exception {
		WTUser user = null;

		Transaction trx = new Transaction();
		try {
			trx.start();

			QueryResult qr = PersistenceHelper.manager.navigate(eul, "part",
					EulPartLink.class, false);

			while (qr.hasMoreElements()) {
				EulPartLink link = (EulPartLink) qr.nextElement();
				link.setLinkType(1);
				PersistenceHelper.manager.modify(link);
			}

			acs.setStatus("CANCEL");
			acs = (ApplyChangeState) PersistenceHelper.manager.modify(acs);

			ApplyHistory ah = acs.getHistory();
			if (isAllSuccess(ah)) {
				ah.setStatus("SUCCESS");
				PersistenceHelper.manager.modify(ah);
			}

			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
	}

	@Override
	public void reApply(EOEul eul, ApplyChangeState acs) throws Exception {
		WTUser user = null;

		Transaction trx = new Transaction();
		try {
			trx.start();

			QueryResult qr = PersistenceHelper.manager.navigate(eul, "part",
					EulPartLink.class, false);

			while (qr.hasMoreElements()) {
				EulPartLink link = (EulPartLink) qr.nextElement();
				link.setLinkType(0);
				PersistenceHelper.manager.modify(link);
			}

			acs.setStatus("ERROR");
			acs = (ApplyChangeState) PersistenceHelper.manager.modify(acs);

			ApplyHistory ah = acs.getHistory();
			ah.setStatus("ERROR");
			PersistenceHelper.manager.modify(ah);

			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
	}

	public WTPart appleyChangeData(EOEul eul, EOActionTempAssyData data,
			boolean revision) throws Exception {
		ArrayList list = data.itemList;

		WTPart assy = (WTPart) EulPartHelper.service.getPart(data.assyPart);

		boolean action = false;

		try {

			Folder folder = FolderHelper.service.getFolder(assy);

			if (revision) {
				assy = (WTPart) VersionControlHelper.service.newVersion(assy);
				Folder pfolder = FolderHelper.service
						.getPersonalCabinet(SessionHelper.manager
								.getPrincipal());
				FolderHelper.assignLocation((FolderEntry) assy, pfolder);
				assy = (WTPart) PersistenceHelper.manager.save(assy);
				action = true;
			} else {
				if (!WorkInProgressHelper.isCheckedOut(assy)) {
					Folder cfolder = WorkInProgressHelper.service
							.getCheckoutFolder();
					CheckoutLink link = CheckInOutTaskLogic.checkOutObject(
							assy, cfolder, "");
					assy = (WTPart) link.getWorkingCopy();
				} else {
					assy = (WTPart) WorkInProgressHelper.service
							.workingCopyOf(assy);
				}
				action = true;
			}

			if (data.stdQuantity != null
					&& !data.stdQuantity.equals(data.orgStdQuantity)) {
				IBAUtil.changeIBAValue(assy, "STD_QUANTITY", data.stdQuantity);
			}

			for (int i = 0; i < list.size(); i++) {
				EOActionTempItemData idata = (EOActionTempItemData) list.get(i);

				if ("I".equals(idata.editType)) {

					LOGGER.info("I >> " + idata.newPart);
					WTPart item = (WTPart) EulPartHelper.service.getPart(idata.newPart);
					WTPartUsageLink link = WTPartUsageLink.newWTPartUsageLink(
							assy, (WTPartMaster) item.getMaster());
					link.setQuantity(Quantity.newQuantity(
							Double.parseDouble(idata.newQuantity),
							QuantityUnit.toQuantityUnit(idata.newUnit)));
					link = (WTPartUsageLink) PersistenceHelper.manager
							.save(link);
					// IBAUtil.changeIBAValue(link, "ItemSeq",
					// idata.newItemSeq);
				} else if ("D".equals(idata.editType)) {

					LOGGER.info("D >> " + idata.oldPart);
					WTPart item = (WTPart) EulPartHelper.service.getPart(idata.oldPart);

					// LOGGER.info(item+","+assy+","+idata.oldItemSeq);
					WTPartUsageLink link = BEContext.getLink(item, assy,
							idata.oldItemSeq);
					if (link == null)
						continue;
					LOGGER.info(link.toString());

					PersistenceHelper.manager.delete(link);
				} else if ("C".equals(idata.editType)) {

					LOGGER.info("C >> " + idata.oldPart + ">"
							+ idata.newPart);
					WTPart oitem = (WTPart) EulPartHelper.service.getPart(idata.oldPart);
					WTPartUsageLink link = BEContext.getLink(oitem, assy,
							idata.oldItemSeq);

					WTPart item = (WTPart) EulPartHelper.service.getPart(idata.newPart);
					link.setUses(item.getMaster());
					link.setQuantity(Quantity.newQuantity(
							Double.parseDouble(idata.newQuantity),
							QuantityUnit.toQuantityUnit(idata.newUnit)));
					link = (WTPartUsageLink) PersistenceHelper.manager
							.modify(link);
					// IBAUtil.changeIBAValue(link, "ItemSeq",
					// idata.newItemSeq);
				}
			}

			// sendSap(eul,data);

			if (revision) {
				assy = (WTPart) FolderHelper.service.changeFolder(assy, folder);
				action = false;
			} else {
				assy = (WTPart) WorkInProgressHelper.service.checkin(assy, "");
				action = false;
			}
			return assy;

		} catch (Exception ex) {
			throw new WTException(ex);
		} finally {
			if (action) {
				if (revision) {
					PersistenceHelper.manager.delete(assy);
				} else {
					WorkInProgressHelper.service.undoCheckout(assy);
				}
			}
		}
	}

	public void sendSap(EOEul eul, EOActionTempAssyData data) throws Exception {
		EChangeOrder2 eco = eul.getEco();
		// SAPDelegate sAPDelegate = new SAPDelegate();
		// sAPDelegate.createModifyBOM(eco, data);
	}

	@Override
	public ManagedBaseline createBaseline(WTPart wtpart, EOEul eulb)
			throws Exception {
		Date date = new Date();
		String baselineName = "EO Baseline : " + wtpart.getNumber() + " : "
				+ date;

		WTProperties wtproperties = WTProperties.getLocalProperties();
		String s = "/Default/Part";
		String s2 = wtproperties.getProperty("baseline.lifecycle");

		Folder folder = null;
		LifeCycleTemplate lifecycletemplate = null;

		if (s != null)
			folder = FolderHelper.service.getFolder(s,
					WCUtil.getWTContainerRef());
		else
			folder = FolderTaskLogic.getFolder(wtpart.getLocation(),
					WCUtil.getWTContainerRef());

		if (s2 != null)
			lifecycletemplate = LifeCycleHelper.service.getLifeCycleTemplate(
					s2, WCUtil.getWTContainerRef());
		else
			lifecycletemplate = (LifeCycleTemplate) wtpart
					.getLifeCycleTemplate().getObject();

		ManagedBaseline mb = BEContext.createBaseline(wtpart, baselineName,
				folder, lifecycletemplate);

		if (eulb != null) {
			eulb = (EOEul) PersistenceHelper.manager.refresh(eulb);
			eulb.setBaseline(mb);
			eulb = (EOEul) PersistenceHelper.manager.modify(eulb);
		}
		return mb;
	}

	@Override
	public EChangeOrder2 getEO(ManagedBaseline bl) throws Exception {
		QueryResult qr = PersistenceHelper.manager.navigate(bl, "eul",
				EulBaselineLink.class);
		if (qr.hasMoreElements()) {
			EOEul eul = (EOEul) qr.nextElement();
			return eul.getEco();
		}
		return null;
	}

	@Override
	public String rework(String oid) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();
			if (oid != null) {
				ReferenceFactory f = new ReferenceFactory();
				EChangeOrder2 changeEco = (EChangeOrder2) f.getReference(oid)
						.getObject();

				if (!ApprovalHelper.service
						.isAccessModify(changeEco.getComplete(), changeEco
								.getOwner().getName())) {
					throw new WTException("복구 할 수 없는 상태 입니다");
				}
				//changeEco.setOrderState(ORDER_WORK);
				PersistenceHelper.manager.modify(changeEco);
			}
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw e;
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return "작업단계로 복구 되었습니다";
	}

	@Override
	public String getLocaleString(String ss) {
		if (CommonUtil.isUSLocale()) {
			if (ORDER_REGIST.equals(ss))
				ss = "Regist"; // 등록
			else if (ORDER_WORK.equals(ss))
				ss = "Working"; // 진행
			else if (ORDER_COMPLETE.equals(ss))
				ss = "Complete"; // 완료

			else if (ACTIVITY_STANDBY.equals(ss))
				ss = "Standing"; // 대기중
			else if (ACTIVITY_WORKING.equals(ss))
				ss = "Working"; // 작업중
			else if (ACTIVITY_APPROVING.equals(ss))
				ss = "Review"; // 승인중
			else if (ACTIVITY_CANCELED.equals(ss))
				ss = "Rejected"; // 반려됨
			else if (ACTIVITY_APPROVED.equals(ss))
				ss = "Complete"; // 작업완료

			else if (ACTIVE_WORK_LAST_APPROVAL.equals(ss))
				ss = "Approve EO"; // EO최종결재
			else if (ACTIVE_WORK_COMPLETE.equals(ss))
				ss = "Complete EO"; // EO완료
			else if (ACTIVE_WORK_REGIST.equals(ss))
				ss = "Regist EO"; // EO등록

			else if (ACTIVE_WORK_REGIST_TITLE.equals(ss))
				ss = "Please register a new EO"; // 신규 EO 를 등록해 주십시오
			else if (ACTIVE_WORK_COMPLETE_TITLE.equals(ss))
				ss = "All EO activities have been completed"; // EO활동이 모두 완료
																// 되었습니다

			else if (ApprovalService.CENCEL_WORK.equals(ss))
				ss = "Rejected/Rework";

		}
		return ss;
	}

	@Override
	public boolean addWorkingActive(HashMap map) {
		String eadOid = (String) map.get("eadOid");
		String ecoOid = (String) map.get("ecoOid");
		String ecaOid = (String) map.get("ecaOid");
		String poid = (String) map.get("poid");
		String finishDate = (String) map.get("finishDate");
		String eaOid = "";
		EChangeActivity ea = (EChangeActivity) CommonUtil.getObject(ecaOid);

		boolean isAdd = false;
		try {

			// EChangeActivity ADD
			EChangeActivityDefinition ead = (EChangeActivityDefinition) CommonUtil
					.getObject(eadOid);
			EChangeOrder2 eco = (EChangeOrder2) CommonUtil.getObject(ecoOid);
			People people = (People) CommonUtil.getObject(poid);

			EChangeActivity changeActivity = EChangeActivity
					.newEChangeActivity();
			changeActivity.setActiveState(ACTIVITY_STANDBY);// "대기중",ACTIVITY_STANDBY
			changeActivity.setContainer(WCUtil.getPDMLinkProduct());
			changeActivity.setDefinition(ead);
			changeActivity.setOrder(eco);
			changeActivity.setFinishDate(DateUtil.convertDate(finishDate));
			changeActivity.setActiveType(ACTIVE_ROLE_WORKING); // "WORKING"
																// ACTIVE_TYPE_CHIEF
			WTUser user = people.getUser();
			WTPrincipalReference ref = WTPrincipalReference
					.newWTPrincipalReference(user);
			changeActivity.setOwner(ref);

			changeActivity = (EChangeActivity) PersistenceHelper.manager
					.save(changeActivity);
			LOGGER.info(">>>>>>>>>>> changeActivity1" + changeActivity);
			isAdd = true;
			// ApprovalLine Add

			/*
			 * if(changeActivity != null){
			 * LOGGER.info(">>>>>>>>>>> changeActivity2" +
			 * changeActivity); ApprovalMaster appMaster =
			 * ApprovalHelper.manager
			 * .searchApprovalMaster((Object)ea);//getApprovalMaster
			 * (changeActivity); LOGGER.info(">>>>>>>>>>> appMaster1" +
			 * appMaster);
			 * 
			 * ApprovalHelper.manager.addApprovalLine(appMaster,
			 * ApprovalHelper.WORKING_WORKING, user.getName(),
			 * ApprovalHelper.LINE_STANDING, 2);
			 * LOGGER.info(">>>>>>>>>>> appMaster2" + appMaster); return
			 * true; }
			 */
			// eaOid = CommonUtil.getOIDString(changeActivity);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isAdd;
	}

	@Override
	public boolean delWorkingActive(String eaOid) {
		EChangeActivity act = (EChangeActivity) CommonUtil.getObject(eaOid);
		boolean isDel = false;
		try {
			PersistenceHelper.manager.delete(act);

			isDel = true;
		} catch (WTException e) {
			isDel = false;
			e.printStackTrace();
		}
		return isDel;
	}

	@Override
	public EChangeActivity getECAWorking(EChangeActivity activity) {
		EChangeActivityDefinition ead = activity.getDefinition();
		long tid = CommonUtil.getOIDLongValue(ead);

		EChangeActivity ecaWorking = null;
		try {
			QuerySpec qs2 = new QuerySpec();
			int iii = qs2.addClassList(EChangeActivityDefinition.class, false);
			int jjj = qs2.addClassList(EChangeActivity.class, true);

			qs2.appendWhere(new SearchCondition(
					EChangeActivityDefinition.class,
					"thePersistInfo.theObjectIdentifier.id",
					EChangeActivity.class, "definitionReference.key.id"),
					new int[] { iii, jjj });

			qs2.appendAnd();
			qs2.appendWhere(new SearchCondition(EChangeActivity.class,
					"orderReference.key.id", "=", activity.getOrder().getPersistInfo()
							.getObjectIdentifier().getId()), new int[] { jjj });

			qs2.appendAnd();
			qs2.appendWhere(new SearchCondition(EChangeActivity.class,
					"definitionReference.key.id", "=", tid), new int[] { jjj });

			qs2.appendAnd();
			qs2.appendWhere(new SearchCondition(EChangeActivity.class,
					"activeType", "=", "WORKING"), new int[] { jjj });

			QueryResult result2 = PersistenceHelper.manager.find(qs2);

			while (result2.hasMoreElements()) {
				Object[] o = (Object[]) result2.nextElement();
				ecaWorking = (EChangeActivity) o[0];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ecaWorking;
	}

	@Override
	public EChangeOrder2 getEO(CommonActivity com) throws Exception {
		QuerySpec qs = new QuerySpec(EChangeOrder2.class);
//		qs.appendWhere(
//				new SearchCondition(EChangeOrder2.class,
//						"completeReference.key.id", SearchCondition.EQUAL,
//						CommonUtil.getOIDLongValue(com)), new int[] { 0 });
		qs.appendWhere(
				new SearchCondition(EChangeOrder2.class,
						"completeWorkReference.key.id", SearchCondition.EQUAL,
						CommonUtil.getOIDLongValue(com)), new int[] { 0 });
		
		QueryResult qr = PersistenceHelper.manager.find(qs);

		if (qr.hasMoreElements()) {
			EChangeOrder2 eco = (EChangeOrder2) qr.nextElement();
			return eco;
		}
		return null;
	}

	@Override
	public Vector getEoEul(CommonActivity com) {
		Vector vec = new Vector();
		try {
			QuerySpec qs = new QuerySpec();
			int eulInt = qs.addClassList(EOEul.class, true);
			int ecaInt = qs.addClassList(EChangeOrder2.class, false);

			qs.appendWhere(new SearchCondition(EOEul.class,
					"ecoReference.key.id", EChangeOrder2.class,
					"thePersistInfo.theObjectIdentifier.id"), new int[] {
					eulInt, ecaInt });
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(EChangeOrder2.class,
					"completeWorkReference.key.id", SearchCondition.EQUAL,
					CommonUtil.getOIDLongValue(com)), new int[] { ecaInt });

			QueryResult qr = PersistenceHelper.manager.find(qs);

			while (qr.hasMoreElements()) {

				Object[] oo = (Object[]) qr.nextElement();
				EOEul eul = (EOEul) oo[0];
				vec.add(eul);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return vec;
	}

	@Override
	public Vector getEoEul(EChangeOrder2 eco) {
		Vector vec = new Vector();
		if (eco == null)
			return vec;
		try {
			QuerySpec qs = new QuerySpec();
			int eulInt = qs.addClassList(EOEul.class, true);

			qs.appendWhere(
					new SearchCondition(EOEul.class, "ecoReference.key.id",
							SearchCondition.EQUAL, CommonUtil
									.getOIDLongValue(eco)),
					new int[] { eulInt });

			QueryResult qr = PersistenceHelper.manager.find(qs);

			while (qr.hasMoreElements()) {

				Object[] oo = (Object[]) qr.nextElement();
				EOEul eul = (EOEul) oo[0];
				vec.add(eul);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return vec;
	}

	@Override
	public Vector getEcoPartList(EChangeOrder2 eco) {
		Vector vec = new Vector();
		try {
			QueryResult qr = this.getEcoPartLink(eco);// QueryResult qr =
														// PersistenceHelper.manager.navigate(eco,"part",EcoPartLink.class,false);
			while (qr.hasMoreElements()) {
				EcoPartLink link = (EcoPartLink) qr.nextElement();
				String version = link.getVersion();
				WTPartMaster master = (WTPartMaster) link.getPart();
				WTPart part = PartHelper.service.getPart(master.getNumber(),
						version);
				vec.add(part);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return vec;
		// PersistenceHelper.manager.navigate(arg0, arg1, arg2)
	}

	@Override
	public QueryResult getEcoPartLink(EChangeOrder2 eco) {
		QueryResult qr = null;
		try {
			qr = PersistenceHelper.manager.navigate(eco, "part",
					EcoPartLink.class, false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return qr;
	}

	@Override
	public void commitPartStateChange(Vector vecPart) throws Exception {
		for (int i = 0; i < vecPart.size(); i++) {
			/* Part change */
			WTPart part = (WTPart) vecPart.get(i);
			// commitPartStateChange(part);
		}
	}

	// ####################### private Method #######################

	private InputStream stringToInputStream(String xml) {
		ByteArrayInputStream bis = new ByteArrayInputStream(xml.getBytes());
		InputStream ins = new BufferedInputStream(bis);
		return ins;
	}
	@Override
	public ArrayList getSelectStateList(){
		ArrayList stateList = new ArrayList();
		
		stateList.add(ChangeService.ECO_WORKING);
		stateList.add(ChangeService.ECO_BEFORE_APPROVING);
		stateList.add(ChangeService.ECO_ECA_WORKING);
		stateList.add(ChangeService.ECO_ECA_COMPLETE);
		stateList.add(ChangeService.ECO_AFTER_APPROVING);
		stateList.add(ChangeService.ECO_COMPLETE);
		stateList.add(ChangeService.ECO_REJECTED);
		stateList.add(ChangeService.ECO_CANCELLED);
		
		return stateList;
	}
	
	@Override
	public EChangeActivityDefinitionRoot getActiveDefinitionECR() throws WTException {
		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(EChangeActivityDefinitionRoot.class, true);
		qs.appendWhere(new SearchCondition(EChangeActivityDefinitionRoot.class,"name",SearchCondition.EQUAL,"ECR"),new int[]{ii});
		QueryResult result = PersistenceHelper.manager.find(qs);

		while (result.hasMoreElements()) {
			Object[] o = (Object[]) result.nextElement();
			return (EChangeActivityDefinitionRoot) o[0];
		}
		return null;
	}
	
	@Override
	public EChangeActivity getChangeActivity(long changeOid, long defOid) throws WTException {
		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(EChangeActivity.class, true);
		qs.appendWhere(new SearchCondition(EChangeActivity.class,"orderReference.key.id",SearchCondition.EQUAL,changeOid),new int[]{ii});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(EChangeActivity.class,"definitionReference.key.id",SearchCondition.EQUAL,defOid),new int[]{ii});
		QueryResult result = PersistenceHelper.manager.find((StatementSpec)qs);
		while (result.hasMoreElements()) {
			Object[] o = (Object[]) result.nextElement();
			return (EChangeActivity) o[0];
		}
		return null;
	}
	
	private EChangeRequest2 deleteChangeActivity(EChangeRequest2 ecr) throws Exception {
		QuerySpec qs = new QuerySpec(EChangeActivity.class);
		qs.appendWhere(new SearchCondition(EChangeActivity.class,
				"orderReference.key.id", "=", ecr.getPersistInfo()
						.getObjectIdentifier().getId()), new int[] { 0 });
		QueryResult result = PersistenceHelper.manager.find(qs);
		while (result.hasMoreElements()) {
			EChangeActivity activity = (EChangeActivity) result.nextElement();
			LOGGER.info("delete Activity : " + CommonUtil.getOIDString(activity));
			PersistenceHelper.manager.delete(activity);
		}

		return ecr;
	}
	private EChangeRequest2 setChangeActivity(Map<String, Object> reqMap, EChangeRequest2 ecr) throws WTException, WTPropertyVetoException{
		ArrayList<Object> active_name = (ArrayList<Object>) reqMap.get("active_name");
		ArrayList<Object> step = (ArrayList<Object>) reqMap.get("step");
		ArrayList<Object> active_type = (ArrayList<Object>) reqMap.get("active_type");
		ArrayList<Object> active_owner = (ArrayList<Object>) reqMap.get("active_owner");
		ArrayList<Object> active_finishDate = (ArrayList<Object>) reqMap.get("active_finishDate");
		ArrayList<Object> active_defOid = (ArrayList<Object>) reqMap.get("defOid");
		ReferenceFactory rf = new ReferenceFactory();
		
		for(int i=0; i<active_name.size(); i++){
			
			People people = null;
			if (active_owner.get(i).toString()!= null && active_owner.get(i).toString().length() > 0) {
				people = (People) rf.getReference(active_owner.get(i).toString()).getObject();
				if(people != null){
					
					EChangeActivity changeActivity = EChangeActivity
							.newEChangeActivity();
					changeActivity.setActiveState(ChangeService.ACTIVITY_STANDBY);
					changeActivity.setContainer(WCUtil.getPDMLinkProduct());
					changeActivity.setOrder(ecr);
					
					changeActivity.setName(active_name.get(i).toString());
					changeActivity.setStep(step.get(i).toString());
					changeActivity.setSortNumber(i);
					changeActivity.setActiveType(active_type.get(i).toString());
					if (active_finishDate.get(i).toString() != null && active_finishDate.get(i).toString().length() > 0) {
						String[] c = active_finishDate.get(i).toString().split("/");
						String n = c[2]+"-"+c[0]+"-"+c[1]+" 00:00:00.0";
						Timestamp ts = Timestamp.valueOf(n);
						changeActivity.setFinishDate(ts);
					}
					
					WTUser user = people.getUser();
					WTPrincipalReference ref = WTPrincipalReference
							.newWTPrincipalReference(user);
					changeActivity.setOwner(ref);
					
					EChangeActivityDefinition def = (EChangeActivityDefinition)rf.getReference(active_defOid.get(i).toString()).getObject();
					if(def != null){
						changeActivity.setDefinition(def);
						changeActivity.setDescription(def.getName());
					}
					changeActivity = (EChangeActivity) PersistenceHelper.manager
							.save(changeActivity);
				}
			}
		}
		return ecr;
	}

	@SuppressWarnings("unchecked")
	@Override
	public EChangeRequest2 createEcr(Map<String, Object> reqMap) throws WTException {
		EChangeRequest2 ecr = null;
		Transaction trx = new Transaction();
		try {
			trx.start();
			String name = StringUtil.checkNull((String) reqMap.get("name"));
			String customer = StringUtil.checkNull((String) reqMap.get("customer"));
			String echangeReason = StringUtil.checkNull((String) reqMap.get("echangeReason"));
			String applyDate = StringUtil.checkNull((String) reqMap.get("applyDate"));
			String description = StringUtil.checkNull((String) reqMap.get("description"));
			String appState = StringUtil.checkNull((String) reqMap.get("appState"));
			List<Map<String,Object>> ecrContentsList = (List<Map<String,Object>>)reqMap.get("ecrContentsList");
			List<Map<String,Object>> approvalList = (List<Map<String,Object>>)reqMap.get("approvalList");
			
			
			String equipmentName = StringUtil.checkNull((String) reqMap.get("equipmentName"));
			String completeHopeDate = StringUtil.checkNull((String) reqMap.get("completeHopeDate"));
			

			List<String> secondary = StringUtil.checkReplaceArray(reqMap.get("SECONDARY"));
			List<String> delocIds = StringUtil.checkReplaceArray(reqMap.get("delocIds"));
			
			
			
			ecr = EChangeRequest2.newEChangeRequest2();
			
			String number = "ECR-" + DateUtil.getCurrentDateString("m") + "-";
			String seqNo = SequenceDao.manager.getSeqNo(number, "0000",
					"EChangeRequest2", "requestNumber");
			number = number + seqNo;

			ecr.setRequestNumber(number);
			ecr.setName(name);
			ecr.setContainer(WCUtil.getPDMLinkProduct());
			ecr.setCustomer(customer);
			ecr.setEchangeReason(echangeReason);
			ecr.setDescription(description);
			ecr.setOwner(SessionHelper.manager.getPrincipalReference());
			
			ecr.setEquipmentName(equipmentName);
			ecr.setCompleteHopeDate(DateUtil.convertDate(completeHopeDate));
			
			
			
			//적용요구시점
			if(applyDate.contains("specificDate")) {
				String[] applyDateArr = applyDate.split(",");
				ecr.setApplyDate(applyDateArr[0]);
				ecr.setSpecificDate(DateUtil.convertDate(applyDateArr[1]));
			}else {
				ecr.setApplyDate(applyDate);
			}
			
			reqMap.put("lifecycle", "LC_Default");
			reqMap.put("location", "/Default/EChange/ECR");
			
			WTContainerRef wtContainerRef = WCUtil.getWTContainerRef();
			Folder folder= FolderTaskLogic.getFolder("/Default/EChange/ECR", wtContainerRef);
			FolderHelper.assignLocation((FolderEntry) ecr, folder);
			ecr = (EChangeRequest2)CommonHelper.service.setVersiondDefault(ecr, reqMap);
			
			ecr = (EChangeRequest2) PersistenceHelper.manager.save(ecr);
			
			//관련품목
			//List<Map<String,Object>> partList = (List<Map<String,Object>>)reqMap.get("partList");
			//createECRPartLink(partList, ecr);
			
			//EchangeContents
			WTCollection wtc = new WTArrayList();
			
			for(int i=0; i <ecrContentsList.size(); i++) {
				EChangeContents ec = EChangeContents.newEChangeContents();
				ec.setName(StringUtil.checkNull((String)ecrContentsList.get(i).get("name")));
				ec.setContents(StringUtil.checkNull((String)ecrContentsList.get(i).get("contents")));
				ec.setSort(i);
				ec.setEchange(ecr);
				wtc.add(ec);			
			}
			
			PersistenceHelper.manager.save(wtc);
			
			if(secondary != null) {
				CommonContentHelper.service.attach(ecr, null, secondary, delocIds,false);
			}
			
			ApprovalHelper.service.registApproval(ecr, approvalList, appState, null);
			
			ecr = (EChangeRequest2)PersistenceHelper.manager.refresh(ecr);
    	    
			trx.commit();
			trx = null;
		} catch (WTException e) {
			throw new WTException(e.getLocalizedMessage());
		} catch (Exception e) {
			throw new WTException(e.getLocalizedMessage());
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return ecr;
	}
	
	/**
	 * ECR 관련 품목 Link 생성
	 * @methodName : createECRPartLink
	 * @author : tsuam
	 * @date : 2021.11.16
	 * @return : void
	 * @description :
	 */
	private void createECRPartLink(List<Map<String,Object>> partList,EChangeRequest2 ecr) throws Exception{
		
		if(partList != null && partList.size() > 0) {
			for(Map<String,Object> map : partList) {
				String partOid = (String)map.get("oid");
				WTPart part = (WTPart)CommonUtil.getObject(partOid);
				if(part != null) {
					String version = VersionControlHelper.getVersionIdentifier(part).getSeries().getValue();
					
					EcrPartLink link = EcrPartLink.newEcrPartLink((WTPartMaster)part.getMaster(),ecr);
					link.setVersion(version);
					PersistenceHelper.manager.save(link);
				}
			}
		}
		
	}

	@Override
	public EChangeRequest2 updateEcr(Map<String, Object> reqMap) throws WTException {
		EChangeRequest2 ecr = null;
		Transaction trx = new Transaction();
		try {
			trx.start();
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			String name = StringUtil.checkNull((String) reqMap.get("name"));
			String customer = StringUtil.checkNull((String) reqMap.get("customer"));
			String echangeReason = StringUtil.checkNull((String) reqMap.get("echangeReason"));
			String applyDate = StringUtil.checkNull((String) reqMap.get("applyDate"));
			String description = StringUtil.checkNull((String) reqMap.get("description"));
			
			String equipmentName = StringUtil.checkNull((String) reqMap.get("equipmentName"));
			String completeHopeDate = StringUtil.checkNull((String) reqMap.get("completeHopeDate"));
			
			List<Map<String,Object>> ecrContentsList = (List<Map<String,Object>>)reqMap.get("ecrContentsList");
			
			List<String> secondary = StringUtil.checkReplaceArray(reqMap.get("SECONDARY"));
			List<String> delocIds = StringUtil.checkReplaceArray(reqMap.get("delocIds"));
			
			ecr = (EChangeRequest2)CommonUtil.getObject(oid);
			ecr.setName(name);
			ecr.setCustomer(customer);
			ecr.setEchangeReason(echangeReason);
			ecr.setDescription(description);
			ecr.setEquipmentName(equipmentName);
			ecr.setCompleteHopeDate(DateUtil.convertDate(completeHopeDate));
			
			//적용요구 시점
			if(applyDate.contains("specificDate")) {
				String[] applyDateArr = applyDate.split(",");
				ecr.setApplyDate(applyDateArr[0]);
				ecr.setSpecificDate(DateUtil.convertDate(applyDateArr[1]));
			}else {
				ecr.setApplyDate(applyDate);
			}
			
			ecr = (EChangeRequest2) PersistenceHelper.manager.modify(ecr);

			//EChangeContents 재생성
			deleteEChangeContents(oid);
			
			//관련품목
			//List<Map<String,Object>> partList = (List<Map<String,Object>>)reqMap.get("partList");
			//modifyECRPartLink(partList, ecr);
			
			//EChangeContents 생성
			WTCollection wtc = new WTArrayList();
			
			for(int i=0; i <ecrContentsList.size(); i++) {
				EChangeContents ec = EChangeContents.newEChangeContents();
				ec.setName(StringUtil.checkNull((String)ecrContentsList.get(i).get("name")));
				ec.setContents(StringUtil.checkNull((String)ecrContentsList.get(i).get("contents")));
				ec.setSort(i);
				ec.setEchange(ecr);
				wtc.add(ec);			
			}
			
			PersistenceHelper.manager.save(wtc);
			
			if(secondary != null) {
				CommonContentHelper.service.attach(ecr, null, secondary, delocIds,false);
			}
			
			ecr = (EChangeRequest2)PersistenceHelper.manager.refresh(ecr);
						
			trx.commit();
			trx = null;
		} catch (WTException e) {
			//e.printStackTrace();
			throw new WTException(e.getLocalizedMessage());
		} catch (Exception e) {
			//e.printStackTrace();
			throw new WTException(e.getLocalizedMessage());
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return ecr;
	}
	
	/**
	 * 관련 품목 삭제 및 품목 링크 생성
	 * @methodName : modifyECRPartLink
	 * @author : tsuam
	 * @date : 2021.11.18
	 * @return : void
	 * @description :
	 */
	private void modifyECRPartLink(List<Map<String,Object>> partList,EChangeRequest2 ecr) throws Exception{
		
		//삭제 
		deleteECRPartLink(ecr);
		
		//추가
		createECRPartLink(partList, ecr);
	}
	
	
	private void deleteECRPartLink(EChangeRequest2 ecr) throws Exception{
		//삭제 
		List<EcrPartLink> linkList = ChangeECRSearch.manager.getECRPartLink(ecr);
		
		for(EcrPartLink link : linkList) {
			PersistenceHelper.manager.delete(link);
		}
	}
	
	@Override
	public void deleteEChangeContents(String oid) throws Exception {
		
		QueryResult qr = ChangeHelper.manager.getEChangeContents_QR(oid);
		
		WTSet set = new WTHashSet(qr);
		
		PersistenceHelper.manager.delete(set);
	}
	
	/**
	 * @desc	: 설계변경 활동 메일 발송
	 * @author	: tsuam
	 * @date	: 2019. 7. 31.
	 * @method	: approvalMail
	 * @return	: void
	 * @param line
	 */
	@Override
	public void sendActivityAssignMail(EChangeActivity eca) throws Exception {
		Hashtable<String, Object> mailHash = EChangeMailForm.setActivityAssignMailInfo(eca);
		if(mailHash.size() > 0 ) {
			mailHash.put(E3PSQueueHelper.queueName, E3PSQueueHelper.QueueName_Mail);
			mailHash.put(E3PSQueueHelper.className, E3PSQueueHelper.ClassName_Mail);
			mailHash.put(E3PSQueueHelper.methodName, E3PSQueueHelper.MethodName_EO_Assign_Mail);
			E3PSQueueHelper.manager.createQueue(mailHash);
		}
	}
}
