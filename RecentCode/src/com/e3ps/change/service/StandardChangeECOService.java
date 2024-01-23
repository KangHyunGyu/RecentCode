package com.e3ps.change.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.e3ps.approval.ApprovalLine;
import com.e3ps.approval.ApprovalMaster;
import com.e3ps.approval.CommonActivity;
import com.e3ps.approval.bean.ApprovalData;
import com.e3ps.approval.service.ApprovalHelper;
import com.e3ps.approval.service.ApprovalService;
import com.e3ps.change.DocumentActivityOutput;
import com.e3ps.change.ECOReviseObject;
import com.e3ps.change.EChangeActivity;
import com.e3ps.change.EChangeActivityDefinition;
import com.e3ps.change.EChangeActivityDefinitionRoot;
import com.e3ps.change.EChangeContents;
import com.e3ps.change.EChangeNoticeItem;
import com.e3ps.change.EChangeOrder2;
import com.e3ps.change.EChangeRequest2;
import com.e3ps.change.EChangeStopStartHistory;
import com.e3ps.change.EOEul;
import com.e3ps.change.EcoPartLink;
import com.e3ps.change.EcoTargetLink;
import com.e3ps.change.EcoTargetResultLink;
import com.e3ps.change.EcrPartLink;
import com.e3ps.change.OrderActivityLink;
import com.e3ps.change.PartReviseObjectLink;
import com.e3ps.change.RequestOrderLink;
import com.e3ps.change.beans.ChangeECOSearch;
import com.e3ps.change.beans.ECOData;
import com.e3ps.common.content.service.CommonContentHelper;
import com.e3ps.common.iba.AttributeKey;
import com.e3ps.common.iba.IBAUtil;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.service.CommonHelper;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.ObjectUtil;
import com.e3ps.common.util.SequenceDao;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.common.web.ParamUtil;
import com.e3ps.common.workflow.E3PSWorkflowHelper;
import com.e3ps.doc.E3PSDocument;
import com.e3ps.epm.bean.EpmPublishUtil;
import com.e3ps.epm.service.EpmHelper;
import com.e3ps.org.People;
import com.e3ps.org.bean.PeopleData;
import com.e3ps.org.service.UserHelper;
import com.e3ps.part.bean.PartData;
import com.e3ps.part.service.PartHelper;
import com.ptc.wvs.server.util.PublishUtils;

import wt.clients.folder.FolderTaskLogic;
import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.content.ContentItem;
import wt.content.ContentRoleType;
import wt.doc.WTDocument;
import wt.enterprise.RevisionControlled;
import wt.epm.EPMDocument;
import wt.epm.EPMDocumentMaster;
import wt.epm.build.EPMBuildHistory;
import wt.epm.build.EPMBuildRule;
import wt.epm.structure.EPMDescribeLink;
import wt.fc.ObjectReference;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.PersistenceServerHelper;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.fc.collections.WTArrayList;
import wt.fc.collections.WTCollection;
import wt.folder.Folder;
import wt.folder.FolderEntry;
import wt.folder.FolderHelper;
import wt.iba.value.IBAHolder;
import wt.inf.container.WTContainerRef;
import wt.org.WTPrincipalReference;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.part.WTPartDescribeLink;
import wt.part.WTPartMaster;
import wt.pom.Transaction;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.representation.Representation;
import wt.services.StandardManager;
import wt.session.SessionHelper;
import wt.util.WTException;
import wt.util.WTProperties;
import wt.util.WTPropertyVetoException;
import wt.vc.VersionControlHelper;
import wt.vc.Versioned;
import wt.vc.struct.StructHelper;

/**
 * <pre>
 * ChangeECO 서비스 서버 구현체
 *
 * [변경이력]
 * - 2015.02.04 (dlee) : 클래스 생성
 * </pre>
 */
public class StandardChangeECOService extends StandardManager implements
		ChangeECOService {

	private static final long serialVersionUID = 1L;
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.CHANGE.getName());

	/**
	 * Default factory for the class.
	 * 
	 * @return
	 * @throws WTException
	 */
	public static StandardChangeECOService newStandardChangeECOService()
			throws WTException {
		StandardChangeECOService instance = new StandardChangeECOService();
		instance.initialize();
		return instance;
	}

	@Override
	public String deleteEco(String oid) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();
			if (oid != null) {
				ReferenceFactory f = new ReferenceFactory();
				EChangeOrder2 changeEco = (EChangeOrder2) f.getReference(oid)
						.getObject();
				
				QueryResult qr4 = PersistenceHelper.manager.navigate(changeEco,"activity",OrderActivityLink.class);
				while(qr4.hasMoreElements()){
					EChangeActivity oldAct = (EChangeActivity)qr4.nextElement();
					ApprovalHelper.service.removeProcess(oldAct);
					PersistenceHelper.manager.delete(oldAct);
				}
				deleteECOPartLink(changeEco);
				ApprovalHelper.service.removeProcess(changeEco);
				PersistenceHelper.manager.delete(changeEco);
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
		return "삭제 되었습니다.";
	}

	@Override
	public void startActivity(Persistable per) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			if(per instanceof EChangeOrder2){
				EChangeOrder2 eco = (EChangeOrder2)per;
//				LifeCycleHelper.service.setLifeCycleState((LifeCycleManaged)eco, State.toState(ChangeService.ECO_ECA_WORKING), false);
//				//eco.setOrderState(ChangeService.ECO_ECA_WORKING);
//				eco = (EChangeOrder2) PersistenceHelper.manager.modify(eco);
				E3PSWorkflowHelper.manager.changeLCState(eco, "ECAWORKING");
			}
//			else if(per instanceof EChangeRequest2){
//				EChangeRequest2 ecr = (EChangeRequest2)per;
//				ecr.setState();
//				ecr = (EChangeRequest2) PersistenceHelper.manager.modify(ecr);
//			}
			ArrayList list = getFirstActivity(CommonUtil.getOIDLongValue(per));

			if (list != null) {

				for (int i = 0; i < list.size(); i++) {

					Object[] o = (Object[]) list.get(i);

					EChangeActivity act = (EChangeActivity) o[0];

					act.setActiveState(ChangeService.ACTIVITY_WORKING);
					act = (EChangeActivity) PersistenceHelper.manager
							.modify(act);

					String[] activityUser = new String[1];
					activityUser[0] = act.getOwner().getPrincipal()
							.getPersistInfo().getObjectIdentifier().toString();
					;

					ApprovalHelper.service.registWork(act, (WTUser)act.getOwner().getPrincipal());
					
					ChangeHelper.service.sendActivityAssignMail(act);
				}
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
	public void commitActivity(EChangeActivity activity, String state, String desc)
			throws Exception {
		
		List<PartData> orgList = new ArrayList<PartData>();
		Transaction trx = new Transaction();
		try {
			trx.start();
			if (ApprovalService.MASTER_APPROVED.equals(state)) {
				activity.setActiveState(ChangeService.ACTIVITY_APPROVED);
				activity.setComments(desc);
				activity.setEcafinishDate(new Timestamp(System.currentTimeMillis()));
			} else {
				activity.setActiveState(ChangeService.ACTIVITY_CANCELED);
			}

			activity = (EChangeActivity) PersistenceHelper.manager.modify(activity);
			
			EChangeOrder2 eco = (EChangeOrder2)activity.getOrder();
			ECOData data = new ECOData(eco);
			data.ecoRelatedPartList();
			List<PartData> partList = data.getPartList();
			
			for(PartData pd : partList) {
				WTPart orgPart = (WTPart)CommonUtil.getObject(pd.getOid());
				WTPart newPart = (WTPart)ObjectUtil.getNextPartVersion(orgPart);
				ApprovalHelper.service.changeStatePart(newPart, "APPROVED");
			}
			
			ApprovalHelper.service.registApproval(eco, null, "TEMP_STORAGE", null);
			
			//ApprovalHelper.service.registWork(eco, (WTUser)eco.getOwner().getPrincipal());

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
	public EChangeOrder2 updateCompleteApproval(EChangeOrder2 eco,
			Hashtable hash) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();

			String[] approveUser2 = (String[]) hash.get("approveUser2");
			String[] approveUser3 = (String[]) hash.get("approveUser3");
			String[] approveUser4 = (String[]) hash.get("approveUser4");
			String[] approveUser5 = (String[]) hash.get("approveUser5");
			String[] tempUser = (String[]) hash.get("tempUser");
			String discussType = (String) hash.get("discussType");
			String approval = (String) hash.get("approval");

			CommonActivity ca = eco.getComplete();

			CommonActivity oldCa = ca;

			ca.setGubun(ChangeService.ACTIVE_WORK_LAST_APPROVAL);
			ca.setTitle(eco.getName());
			ca.setOwner(SessionHelper.manager.getPrincipalReference());
			ca = (CommonActivity) PersistenceHelper.manager.save(ca);

			//eco.setOrderState(ChangeService.ECO_COMPLETE);
			eco = (EChangeOrder2) PersistenceHelper.manager.modify(eco);

			boolean isApproval = "true".equals(approval);
			ApprovalHelper.service.registApproval(ca, hash);

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
	public void commitECO(CommonActivity com, String state, EChangeOrder2 eco)
			throws Exception {
		// ::::::::::::: ECO 상태 변경 :::::::::::::::::
		E3PSWorkflowHelper.manager.changeLCState(eco, "APPROVED");
		//eco.setOrderState(ChangeService.ECO_COMPLETE);

		// :::::::::::::ERPHistory Create ::::::::::::::::: ERPHistory history =
//		ERPHistory.newERPHistory();
//		history.setEo(ObjectReference.newObjectReference(eco));
//		PersistenceHelper.manager.save(history);

		// ::::::::::::: EoEul(BOM) 적용 :::::::::::::::::
		Vector vecEul = ChangeHelper.service.getEoEul(eco);
		for (int i = 0; i < vecEul.size(); i++) {
			EOEul eul = (EOEul) vecEul.get(i);
			ChangeHelper.service.commitEOEul(eul, state);
		}

		// ::::::::::::: ERP 전송 ::::::::::::::::: //

		// ERPECOHelper.manager.erpECO(eco,ERPUtil.HISTORY_TYPE_COMPLETE, history);

		// ::::::::::::: 수신자 :::::::::::::::::::
		this.sendNotice(eco);
	}

	@Override
	public void commitPartStateChange(Vector vecPart) throws Exception {
		for (int i = 0; i < vecPart.size(); i++) {
			/* Part change */
			WTPart part = (WTPart) vecPart.get(i);
			// commitPartStateChange(part);
		}
	}

	@Override
	public void reviseObject(Vector vec) throws Exception {
		try {

			for (int i = 0; i < vec.size(); i++) {

				String[] partoids = (String[]) vec.get(i);
				Transaction trx = new Transaction();
				trx.start();
				try {

					String oid = partoids[0];
					String linkOid = partoids[1];
					EcoPartLink link = (EcoPartLink) CommonUtil
							.getObject(linkOid);

					/********************** PART Revise *********************************/
					WTPart part = (WTPart) CommonUtil.getObject(oid);

					Folder folder = FolderHelper.service
							.getFolder((FolderEntry) part);
					WTPart newPart = (WTPart) VersionControlHelper.service
							.newVersion(part);
					Folder pfolder = FolderHelper.service
							.getPersonalCabinet(SessionHelper.manager
									.getPrincipal());
					FolderHelper.assignLocation((FolderEntry) newPart, pfolder);

					newPart = (WTPart) PersistenceHelper.manager.save(newPart);

					/* 관련 문서 link */
					QueryResult linkQr = PersistenceHelper.manager.navigate(
							part, "describedBy", WTPartDescribeLink.class);

					WTPartDescribeLink dlink = null;
					if ((linkQr != null) && (linkQr.size() > 0)) {
						while (linkQr.hasMoreElements()) {
							WTDocument doc = (WTDocument) linkQr.nextElement();
							dlink = WTPartDescribeLink.newWTPartDescribeLink(
									newPart, doc);
							dlink = (WTPartDescribeLink) PersistenceHelper.manager
									.save(dlink);
						}
					}

					newPart = (WTPart) FolderHelper.service.changeFolder(
							newPart, folder);

					/* Revision History */
					ECOReviseObject reviseObject = ECOReviseObject
							.newECOReviseObject();
					reviseObject.setEcoPart(link);
					reviseObject.setGubun("PART");
					reviseObject.setVersion(newPart.getVersionIdentifier()
							.getSeries().getValue());
					reviseObject.setOid(CommonUtil.getOIDString(newPart));
					reviseObject.setReviseObject(ObjectReference
							.newObjectReference(newPart));
					PersistenceHelper.manager.save(reviseObject);

					/********************** EPM Revise *********************************/
					EPMDocument newEPM = null;
					EPMDocument newEPM2D = null;
					EPMDocument epm = EpmHelper.service.getEPMDocument(newPart);
					if (epm != null) {

						folder = FolderHelper.service
								.getFolder((FolderEntry) epm);
						newEPM = (EPMDocument) VersionControlHelper.service
								.newVersion(epm);
						pfolder = FolderHelper.service
								.getPersonalCabinet(SessionHelper.manager
										.getPrincipal());
						FolderHelper.assignLocation((FolderEntry) newEPM,
								pfolder);

						newEPM = (EPMDocument) PersistenceHelper.manager
								.save(newEPM);

						newEPM = (EPMDocument) FolderHelper.service
								.changeFolder(newEPM, folder);

						/* Revision History */
						reviseObject = ECOReviseObject.newECOReviseObject();
						reviseObject.setEcoPart(link);
						reviseObject.setGubun("EPM");
						reviseObject.setVersion(newEPM.getVersionIdentifier()
								.getSeries().getValue());
						reviseObject.setOid(CommonUtil.getOIDString(newEPM));
						reviseObject.setReviseObject(ObjectReference
								.newObjectReference(newEPM));
						PersistenceHelper.manager.save(reviseObject);

						/* 주부품 연결 */
						EPMBuildRule newEbr = PartHelper.service.getBuildRule(newPart);
						newEbr.setBuildSource(newEPM);
						PersistenceServerHelper.manager.update(newEbr);

						// EPMBuildHistory ebh =
						// PartSearchHelper.getBuildHistory(newPart, newEPM); if
						// (ebh != null) { ebh.setBuiltBy(newEPM);
						// PersistenceServerHelper.manager.update(ebh); }

						/* publish 실행 */
						newEPM = (EPMDocument) PersistenceHelper.manager
								.refresh(newEPM);
						EpmPublishUtil.publish(newEPM);
						/********************** EPM 2DRevise *********************************/

						EPMDocument epm2D = EpmHelper.service
								.getEPM2D((EPMDocumentMaster) newEPM
										.getMaster());
						if (epm2D != null) {

							folder = FolderHelper.service
									.getFolder((FolderEntry) epm2D);
							newEPM2D = (EPMDocument) VersionControlHelper.service
									.newVersion(epm2D);
							pfolder = FolderHelper.service
									.getPersonalCabinet(SessionHelper.manager
											.getPrincipal());
							FolderHelper.assignLocation((FolderEntry) newEPM2D,
									pfolder);

							newEPM2D = (EPMDocument) PersistenceHelper.manager
									.save(newEPM2D);

							newEPM2D = (EPMDocument) FolderHelper.service
									.changeFolder(newEPM2D, folder);
							/* publish 실행 */
							EpmPublishUtil.publish(newEPM2D);

							/* Revision History */
							reviseObject = ECOReviseObject.newECOReviseObject();
							reviseObject.setEcoPart(link);
							reviseObject.setGubun("EPM2D");
							reviseObject.setVersion(newEPM2D
									.getVersionIdentifier().getSeries()
									.getValue());
							reviseObject.setOid(CommonUtil
									.getOIDString(newEPM2D));
							reviseObject.setReviseObject(ObjectReference
									.newObjectReference(newEPM2D));
							PersistenceHelper.manager.save(reviseObject);
						}
					}
					trx.commit();
					trx = null;
				} catch (Exception e) {
					System.out
							.println(":::::::::::::::::::::::::::: ERROR2 ::::::::::::::::::::::::::::::");
					e.printStackTrace();
					throw e;
				} finally {
					if (trx != null) {
						trx.rollback();
					}
				}
			}

		} catch (Exception e) {
			System.out
					.println(":::::::::::::::::::::::::::: ERROR3 ::::::::::::::::::::::::::::::");
			e.printStackTrace();
		}

		// assy = (WTPart)VersionControlHelper.service.newVersion(assy);
		// Folder pfolder =
		// FolderHelper.service.getPersonalCabinet(SessionHelper.manager.getPrincipal());
		// FolderHelper.assignLocation((FolderEntry)assy, pfolder);
		// assy = (WTPart)PersistenceHelper.manager.save(assy);
	}

	@Override
	public Vector getEoReviseHistory(EcoPartLink link) {
		Vector vec = new Vector();
		try {

			QueryResult rt = PersistenceHelper.manager.navigate(link,
					"reviseObject", PartReviseObjectLink.class, true);

			while (rt.hasMoreElements()) {

				ECOReviseObject reObject = (ECOReviseObject) rt.nextElement();

				vec.add(reObject);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return vec;
	}

	@Override
	public void reviseDelete(String[] linkOid, String delType)
			throws WTException {
		for (int i = 0; i < linkOid.length; i++) {
			EcoPartLink link = (EcoPartLink) CommonUtil.getObject(linkOid[i]);

			Vector reVec = ChangeECOHelper.service.getEoReviseHistory(link);
			for (int h = 0; h < reVec.size(); h++) {
				ECOReviseObject reObject = (ECOReviseObject) reVec.get(h);
				if (delType.equals("PART_DELETE")) {
					if (reObject.getGubun().equals("PART")) {

						try {
							WTPart part = (WTPart) reObject.getReviseObject()
									.getObject();
							if (part != null)
								PersistenceHelper.manager.delete(part);
							PersistenceHelper.manager.delete(reObject);
						} catch (Exception e) {
							e.printStackTrace();
						}

					} else if (reObject.getGubun().equals("EPM")) {

						try {
							EPMDocument epm = (EPMDocument) reObject
									.getReviseObject().getObject();
							if (epm != null)
								PersistenceHelper.manager.delete(epm);
							PersistenceHelper.manager.delete(reObject);
						} catch (Exception e) {
							e.printStackTrace();
						}

					} else {
						EPMDocument epm2D = (EPMDocument) reObject
								.getReviseObject().getObject();
						if (epm2D != null)
							PersistenceHelper.manager.delete(epm2D);
						PersistenceHelper.manager.delete(reObject);
					}
				} else {

					try {
						EPMDocument epm2D = (EPMDocument) reObject
								.getReviseObject().getObject();
						if (epm2D != null)
							PersistenceHelper.manager.delete(epm2D);
						PersistenceHelper.manager.delete(reObject);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}
		}
	}

	@Override
	public QueryResult getRequestOrderLink(EChangeOrder2 eco) {
		QueryResult qr = null;
		try {
			qr = PersistenceHelper.manager.navigate(eco, "request",
					RequestOrderLink.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return qr;
		// while(qr.hasMoreElements()){
		// EChangeRequest2 doc = (EChangeRequest2) qr.nextElement();
	}

	@Override
	public QueryResult searchECOPartLink(EChangeOrder2 eco, String partType) {
		QueryResult rt = null;
		try {
			QuerySpec qs = new QuerySpec(EcoPartLink.class);
			// int idxA = qs.addClassList(EcoPartLink.class, true);
			qs.appendWhere(
					new SearchCondition(EcoPartLink.class,
							"roleBObjectRef.key.id", SearchCondition.EQUAL,
							CommonUtil.getOIDLongValue(eco)), new int[] { 0 });

			if (partType != null) {
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(EcoPartLink.class,
						"partType", SearchCondition.EQUAL, partType),
						new int[] { 0 });
			}

			qs.appendAnd();
			qs.appendWhere(new SearchCondition(EcoPartLink.class, "gubun",
					"<>", "result"), new int[] { 0 });

			// LOGGER.info(qs);
			rt = PersistenceHelper.manager.find(qs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rt;
	}

	@Override
	public QueryResult searchECOPartResultLink(EChangeOrder2 eco) {
		QueryResult rt = null;
		try {
			QuerySpec qs = new QuerySpec(EcoPartLink.class);
			// int idxA = qs.addClassList(EcoPartLink.class, true);
			qs.appendWhere(
					new SearchCondition(EcoPartLink.class,
							"roleBObjectRef.key.id", SearchCondition.EQUAL,
							CommonUtil.getOIDLongValue(eco)), new int[] { 0 });

			qs.appendAnd();
			qs.appendWhere(new SearchCondition(EcoPartLink.class, "gubun", "=",
					"result"), new int[] { 0 });

			// LOGGER.info(qs);
			rt = PersistenceHelper.manager.find(qs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rt;
	}

	@Override
	public void startECOApprove(EChangeOrder2 eco) {
		// LOGGER.info(":::::::::::::::::::::: startECOApprove ::::::::::::::");
		try {
			CommonActivity activity = eco.getComplete();
			ApprovalMaster master = ApprovalHelper.service.getApprovalMaster(activity);
			ApprovalData data = new ApprovalData(master);
			int active = 0;
			WTUser notiUser = null;
			if (master != null) {
				QueryResult qr = ApprovalHelper.service.getApprovalLine(master);
				ApprovalLine al = null;

				while (qr.hasMoreElements()) {

					String gubun = "EO 최종결재";
					al = (ApprovalLine) qr.nextElement();
					if (al.getName().equals(ApprovalService.WORKING_REVIEWER)
							&& al.getSeq() > 1)
						continue;

					if (al.getName().equals(ApprovalService.WORKING_DISCUSSER)
							|| al.getName().equals(
									ApprovalService.WORKING_REVIEWER)) {

						active = al.getSeq();
						PersistenceHelper.manager.modify(al);

						if (al.getName().equals(
								ApprovalService.WORKING_DISCUSSER))
							gubun = gubun + " 협의 ";
						if (al.getName()
								.equals(ApprovalService.WORKING_REVIEWER))
							gubun = gubun + "승인 ";
						gubun = "[" + gubun + "]";

						notiUser = UserHelper.getWTUser(al.getOwner().getFullName());
						HashMap toHash = new HashMap();
						toHash.put(notiUser.getEMail(), notiUser.getFullName());

						String creatorName = ApprovalHelper.service.getCreatorFullName(eco);

						toHash.put("createDate",
								ApprovalHelper.service.getCreateTime(eco));
						toHash.put("creater", creatorName);
						toHash.put("title", data.getTitle());
						toHash.put("eMail", notiUser.getEMail());
						toHash.put("fullName", notiUser.getFullName());

//						com.e3ps.common.mail.MailUtil.mailObjMailSendSetting(
//								al, (Object) activity, toHash, "[PLM 전자결재]"
//										+ gubun + " 결재 요청이 접수되었습니다.",
//								"WorkCenter에서 확인하세요.", "requestApproval");
					}

					if (al.getName().equals(ApprovalService.WORKING_REPORTER)) {

						Timestamp time = new Timestamp(
								new java.util.Date().getTime());
						al.setApproveDate(time);
						PersistenceHelper.manager.modify(al);
					}

				}

				/* ApprovalMaster 상태 */
				master.setActive(active);
				PersistenceHelper.manager.modify(master);

				/* ECO STATE */
				//eco.setOrderState(ChangeService.ECO_AFTER_APPROVING);
				PersistenceHelper.manager.modify(eco);
			}

			// LINE_APPROVING
		} catch (Exception e) {
			System.out
					.println("::::::::::::::::::: startECOApprove ERROR ::::::::::::::::");
			e.printStackTrace();
		}
	}

	@Override
	public void sendNotice(EChangeOrder2 eco) throws Exception {
		System.out
				.println(":::::::::::::::::::::::::::::: sendNotice START :::::::::::::::::::::");
		ArrayList allList = new ArrayList();
		QuerySpec qs = new QuerySpec();
		CommonActivity activity = eco.getComplete();

		if (activity == null)
			return;

		// 사후 결재자
		ApprovalMaster lastMaster = ApprovalHelper.service
				.getApprovalMaster(activity);

		if (lastMaster == null)
			return;

		// 사전 결재자
		ApprovalMaster ecoMaster = ApprovalHelper.service
				.getApprovalMaster(eco);
		ArrayList temp = new ArrayList();
		if (ecoMaster != null) {
			QueryResult rt = ApprovalHelper.service.getApprovalLine(ecoMaster);
			while (rt.hasMoreElements()) {
				ApprovalLine line = (ApprovalLine) rt.nextElement();
				if (line.getName().equals(ApprovalService.WORKING_REPORTER))
					continue;

				String owner = line.getOwner().getFullName();

				if (!temp.contains(owner)) {
					temp.add(owner);
				}
			}
		}

		/* ECA */
		ArrayList list = getECOActivity(eco);
		for (int i = 0; i < list.size(); i++) {
			EChangeActivity eca = (EChangeActivity) list.get(i);

			String owner = eca.getOwner().getName();

			if (!temp.contains(owner)) {
				temp.add(owner);
			}
		}

		QueryResult rt = ApprovalHelper.service.getApprovalLine(lastMaster);
		int idx = rt.size();
		while (rt.hasMoreElements()) {
			ApprovalLine line = (ApprovalLine) rt.nextElement();
			if (line.getName().equals(ApprovalService.WORKING_TEMP))
				continue;
			allList.add(line.getOwner());

			String owner = line.getOwner().getFullName();

			if (!temp.contains(owner)) {
				temp.add(owner);
			}
		}

		QueryResult qr = PersistenceHelper.manager.navigate(eco, "request",
				RequestOrderLink.class);
		while (qr.hasMoreElements()) {
			EChangeRequest2 ecr = (EChangeRequest2) qr.nextElement();

			String owner = ecr.getOwner().getName();

			if (!temp.contains(owner)) {
				temp.add(owner);
			}
		}

		// WTGroup group = UserHelper.service.getWTGroup("Administrators");
		// Enumeration en = OrganizationServicesHelper.manager.members(group);
		// while ( en.hasMoreElements() ) {
		// WTPrincipal pp = (WTPrincipal) en.nextElement();
		// this.registerNotice(lastMaster, pp.getName());
		// }

		for (int i = 0; i < temp.size(); i++) {
			String owner = (String) temp.get(i);
			this.registerNotice(lastMaster, owner);
		}

		System.out
				.println(":::::::::::::::::::::::::::::: sendNotice END :::::::::::::::::::::");
	}

	@Override
	public ArrayList getECOActivityMaster(EChangeOrder2 eco) throws Exception {
		QuerySpec qs = new QuerySpec(EChangeActivity.class);

		qs.appendWhere(new SearchCondition(EChangeActivity.class,
				"orderReference.key.id", "=", eco.getPersistInfo()
						.getObjectIdentifier().getId()), new int[] { 0 });

		QueryResult result = PersistenceHelper.manager.find(qs);
		ArrayList list = new ArrayList();
		while (result.hasMoreElements()) {

			EChangeActivity activity = (EChangeActivity) result.nextElement();
			ApprovalMaster master = ApprovalHelper.service
					.getApprovalMaster(activity);
			list.add(master);
		}

		return list;
	}

	@Override
	public ArrayList getECOActivity(EChangeOrder2 eco) throws Exception {
		QuerySpec qs = new QuerySpec(EChangeActivity.class);
		qs.appendWhere(new SearchCondition(EChangeActivity.class,
				"orderReference.key.id", "=", eco.getPersistInfo()
						.getObjectIdentifier().getId()), new int[] { 0 });

		QueryResult result = PersistenceHelper.manager.find(qs);
		ArrayList list = new ArrayList();
		while (result.hasMoreElements()) {

			EChangeActivity activity = (EChangeActivity) result.nextElement();

			list.add(activity);
		}

		return list;
	}

	@Override
	public void registerNotice(ApprovalMaster master, String user) {
		try {
			// LOGGER.info(":::::::::: NOTICE user : " + user);
			ApprovalLine line = ApprovalLine.newApprovalLine();
			line.setName(ApprovalService.WORKING_TEMP);
			line.setSeq(100);
			line.setReadCheck(false);
			line.setMaster(master);
			line.setStepName(ApprovalService.APPROVE_NOTIFICATE);
			line = (ApprovalLine) PersistenceHelper.manager.save(line);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void autoCadPdfPSend(EPMDocument epm) {
		System.out
				.println(">>>>>>>>>>>>>>>>> autoCadPdfPSend <<<<<<<<<<<<<<<<<<<<<<");
		try {
			String tempDir = WTProperties.getLocalProperties().getProperty(
					"wt.temp");
			ContentItem item = null;

			byte[] buffer = new byte[1024];

			QueryResult result = ContentHelper.service.getContentsByRole(
					(ContentHolder) epm, ContentRoleType.SECONDARY);
			while (result.hasMoreElements()) {
				item = (ContentItem) result.nextElement();
				ApplicationData pAppData = (ApplicationData) item;
			}
			ApplicationData adata = (ApplicationData) item;

			/* EPMPDFLink link = EpmUtil.getPDFSendList(epm); */
			HashMap map = new HashMap();
			// LOGGER.info(":::::::::::::: AutoCad adata :" + adata);
			/*
			 * if(link != null){ map.put("oid", CommonUtil.getOIDString(adata));
			 * map.put("tempDir", link.getFolder()); map.put("pdfFileName",
			 * link.getFileName()); map.put("epmType", "AutoCad");
			 * 
			 * HashMap mapRe = FileDown.pdfDown(map);
			 * 
			 * String result1 = (String)mapRe.get("result"); String message =
			 * (String)mapRe.get("message");
			 * 
			 * link.setResult(result1); link.setMessage(message);
			 * PersistenceHelper.manager.modify(link);
			 * 
			 * 
			 * }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void gPartPdfSend(WTPart part) {
		/* EPMDocument epm = PartHelper.service.getGPartDWG(part); */
	}

	@Override
	public void rejectEco(EChangeOrder2 eco) {
		System.out
				.println("::::::::::::::::::: ECO rejectECO ::::::::::::::::::::");
		ArrayList allList = new ArrayList();
		try {

			/* eco 상태 변경 */
			//eco.setOrderState(ChangeService.ECO_REJECTED);
			PersistenceHelper.manager.modify(eco);

			/* ERP eco 상태 변경 */

			// if(eco.getProcess().equals(ChangeService.ECR_EXIST)){
			// ERPECOHelper.manager.upDatePDMECO(eco); }

			/* 반려 Email */
			CommonActivity activity = eco.getComplete();
			String gubun = "[EO 최종결재]";
			String process = eco.getProcess();
			WTUser notiUser = null;
			if (activity == null)
				return;
			// 사후 결재자
			ApprovalMaster lastMaster = ApprovalHelper.service
					.getApprovalMaster(activity);
			ApprovalData data = new ApprovalData(lastMaster);
			if (lastMaster == null)
				return;

			if (process.equals(ChangeService.ECR_EXIST)) {
				// 사전 결재자
				ApprovalMaster ecoMaster = ApprovalHelper.service
						.getApprovalMaster(eco);

				if (ecoMaster != null) {
					QueryResult rt = ApprovalHelper.service
							.getApprovalLine(ecoMaster);
					while (rt.hasMoreElements()) {
						ApprovalLine line = (ApprovalLine) rt.nextElement();
						if (line.getName().equals(
								ApprovalService.WORKING_REPORTER))
							continue;

						notiUser = UserHelper.getWTUser(line.getOwner().getFullName());
						HashMap toHash = new HashMap();
						toHash.put(notiUser.getEMail(), notiUser.getFullName());

						String creatorName = ApprovalHelper.service
								.getCreatorFullName(eco);
						// LOGGER.info("::::::::::: ecoMaster : "
						// +notiUser.getFullName() );
						toHash.put("createDate",
								ApprovalHelper.service.getCreateTime(eco));
						toHash.put("creater", creatorName);
						toHash.put("title", data.getTitle());
						toHash.put("eMail", notiUser.getEMail());
						toHash.put("fullName", notiUser.getFullName());
//						com.e3ps.common.mail.MailUtil.mailObjMailSendSetting(
//								line, (Object) ApprovalHelper.service
//										.getApprovalObject(ecoMaster), toHash,
//								"[PLM 전자결재]" + gubun + " 결재가 반려되었습니다.",
//								"PDM System에서 확인하세요.", "notify");
					}
				}
			}

			/* ECA */
			ArrayList list = getECOActivity(eco);
			for (int i = 0; i < list.size(); i++) {
				EChangeActivity eca = (EChangeActivity) list.get(i);

				ApprovalMaster ecaMaster = ApprovalHelper.service
						.getApprovalMaster(eca);

				if (ecaMaster != null) {
					QueryResult rt = ApprovalHelper.service
							.getApprovalLine(ecaMaster);
					while (rt.hasMoreElements()) {
						ApprovalLine line = (ApprovalLine) rt.nextElement();
						if (line.getName().equals(
								ApprovalService.WORKING_REPORTER))
							continue;

						notiUser = UserHelper.getWTUser(line.getOwner().getFullName());
						HashMap toHash = new HashMap();
						toHash.put(notiUser.getEMail(), notiUser.getFullName());

						String creatorName = ApprovalHelper.service
								.getCreatorFullName(eco);
						// LOGGER.info("::::::::::: ECA : "
						// +notiUser.getFullName() );
						toHash.put("createDate",
								ApprovalHelper.service.getCreateTime(eco));
						toHash.put("creater", creatorName);
						toHash.put("title", data.getTitle());
						toHash.put("eMail", notiUser.getEMail());
						toHash.put("fullName", notiUser.getFullName());
//						com.e3ps.common.mail.MailUtil.mailObjMailSendSetting(
//								line, (Object) ApprovalHelper.service
//										.getApprovalObject(ecaMaster), toHash,
//								"[PLM 전자결재]" + gubun + " 결재가 반려되었습니다.",
//								"PDM System에서 확인하세요.", "notify");
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public List<EChangeActivityDefinitionRoot> getActiveDefinitionRoot() throws WTException {
		List<EChangeActivityDefinitionRoot> list = new ArrayList<EChangeActivityDefinitionRoot>();

		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(EChangeActivityDefinitionRoot.class, true);
		qs.appendOrderBy(new OrderBy(new ClassAttribute(
				EChangeActivityDefinitionRoot.class, "sortNumber"), false),
				new int[] { 0 });
		QueryResult result = PersistenceHelper.manager.find(qs);

		while (result.hasMoreElements()) {
			Object[] o = (Object[]) result.nextElement();
			list.add((EChangeActivityDefinitionRoot) o[0]);
		}
		return list;
	}

	@Override
	public List<EChangeActivityDefinition> getActiveDefinition(long rootOid) throws WTException{
		List<EChangeActivityDefinition> list= new ArrayList<EChangeActivityDefinition>();

		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(EChangeActivityDefinition.class, true);
		qs.appendWhere(new SearchCondition(EChangeActivityDefinition.class,"rootReference.key.id","=",rootOid),new int[]{ii});
		qs.appendOrderBy(new OrderBy(new ClassAttribute(
				EChangeActivityDefinition.class, "sortNumber"), false),
				new int[] { 0 });
		QueryResult result = PersistenceHelper.manager.find(qs);

		while (result.hasMoreElements()) {
			Object[] o = (Object[]) result.nextElement();
			list.add((EChangeActivityDefinition) o[0]);
		}

		return list;
	}

	@Override
	public void publishDelete(EPMDocument epm) {
		try {

			Representation representation = PublishUtils.getRepresentation(epm);
			if (representation == null)
				return;
			representation = (Representation) ContentHelper.service
					.getContents(representation);
			Vector contentList = ContentHelper.getContentList(representation);
			for (int l = 0; l < contentList.size(); l++) {
				ContentItem contentitem = (ContentItem) contentList
						.elementAt(l);
				if (contentitem instanceof ApplicationData) {
					ApplicationData drawAppData = (ApplicationData) contentitem;

					if (drawAppData.getRole().toString().equals("SECONDARY")
							&& drawAppData.getFileName().lastIndexOf("pdf") > 0) {
						PersistenceHelper.manager.delete(drawAppData);
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ####################### private Method #######################

	private boolean checkStepComplete(EChangeActivity activity)
			throws Exception {

		String step = activity.getStep();

		QuerySpec qs = new QuerySpec();
		int jj = qs.addClassList(EChangeActivity.class, false);
		qs.appendSelect(new ClassAttribute(EChangeActivity.class,
				"thePersistInfo.theObjectIdentifier.id"), new int[] { jj },
				false);

		qs.appendWhere(new SearchCondition(EChangeActivity.class,
				"orderReference.key.id", "=", activity.getOrder()
						.getPersistInfo().getObjectIdentifier().getId()),
				new int[] { jj });
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(EChangeActivity.class,
				"step", "=", step), new int[] { jj });
		qs.appendAnd();
		qs.appendOpenParen();
		qs.appendWhere(new SearchCondition(EChangeActivity.class,
				EChangeActivity.ACTIVE_STATE, "=",
				ChangeService.ACTIVITY_APPROVING), new int[] { jj });
		qs.appendOr();
		qs.appendWhere(new SearchCondition(EChangeActivity.class,
				EChangeActivity.ACTIVE_STATE, "=",
				ChangeService.ACTIVITY_STANDBY), new int[] { jj });
		qs.appendOr();
		qs.appendWhere(new SearchCondition(EChangeActivity.class,
				EChangeActivity.ACTIVE_STATE, "=",
				ChangeService.ACTIVITY_WORKING), new int[] { jj });
		qs.appendCloseParen();

		QueryResult result = PersistenceHelper.manager.find(qs);

		return result.size() == 0;

	}

	private ArrayList getNextActivity(EChangeActivity activity)
			throws Exception {
		QuerySpec qs = new QuerySpec();
		int jj = qs.addClassList(EChangeActivity.class, true);

		qs.appendWhere(new SearchCondition(EChangeActivity.class,
				"orderReference.key.id", "=", activity.getOrder()
						.getPersistInfo().getObjectIdentifier().getId()),
				new int[] { jj });
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(EChangeActivity.class,
				"sortNumber", ">", activity.getSortNumber()),
				new int[] { jj });
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(EChangeActivity.class,
				"step", "<>", activity.getStep()),
				new int[] { jj });

		qs.appendOrderBy(new OrderBy(new ClassAttribute(
				EChangeActivity.class, "sortNumber"), false),
				new int[] { jj });

		QueryResult result = PersistenceHelper.manager.find(qs);

		ArrayList list = new ArrayList();
		String firstStep = null;
		while (result.hasMoreElements()) {
			Object[] o = (Object[]) result.nextElement();
			EChangeActivity ed = (EChangeActivity) o[0];
			String step = ed.getStep();
			if (firstStep == null) {
				firstStep = step;
			}
			if (step.equals(firstStep)) {
				list.add(o);
			} else {
				break;
			}
		}
		return list;
	}

	private ArrayList getFirstActivity(long changeOid) throws Exception {
		QuerySpec qs = new QuerySpec();
		int jj = qs.addClassList(EChangeActivity.class, true);

		qs.appendWhere(new SearchCondition(EChangeActivity.class,
				"orderReference.key.id", "=", changeOid), new int[] { jj });
		qs.appendOrderBy(new OrderBy(new ClassAttribute(
				EChangeActivity.class, "sortNumber"), false),
				new int[] { 0 });
		QueryResult result = PersistenceHelper.manager.find(qs);

		ArrayList list = new ArrayList();
		String firstStep = null;
		while (result.hasMoreElements()) {
			Object[] o = (Object[]) result.nextElement();
			EChangeActivity ed = (EChangeActivity) o[0];
			String step = ed.getStep();
			if (firstStep == null) {
				firstStep = step;
			}
			if (step.equals(firstStep)) {
				list.add(o);
			} else {
				break;
			}
		}
		return list;
	}

	@Override
	public EChangeActivityDefinitionRoot createRootDefinition(Map<String, Object> reqMap) throws WTException {
		EChangeActivityDefinitionRoot def = null;
		Transaction trx = new Transaction();
		try {
			trx.start();

			String name = StringUtil.checkNull((String) reqMap.get("name"));
			String engName = StringUtil.checkNull((String) reqMap.get("engName"));
			String description = StringUtil.checkNull((String) reqMap.get("description"));
			int sortNumber = ParamUtil.getInt(reqMap,"sortNumber");
			
			def = EChangeActivityDefinitionRoot.newEChangeActivityDefinitionRoot();
			def.setName(name);
			def.setName_eng(engName);
			def.setDescription(description);
			def.setSortNumber(sortNumber);
			def = (EChangeActivityDefinitionRoot)PersistenceHelper.manager.save(def);
			
			trx.commit();
			trx = null;
			
		} catch (Exception e) {
			logger.error("ERROR", e);
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return def;
	}
	
	@Override
	public EChangeActivityDefinitionRoot updateRootDefinition(Map<String, Object> reqMap) throws WTException {
		EChangeActivityDefinitionRoot def = null;
		Transaction trx = new Transaction();
		try {
			trx.start();

			String oid = ParamUtil.get(reqMap,"oid");
			String name = ParamUtil.get(reqMap,"name");
			String engName = ParamUtil.get(reqMap,"engName");
			String description = ParamUtil.get(reqMap,"description");
			int sortNumber = ParamUtil.getInt(reqMap,"sortNumber");
			def = (EChangeActivityDefinitionRoot)CommonUtil.getObject(oid);
			def.setName(name);
			def.setName_eng(engName);
			def.setDescription(description);
			def.setSortNumber(sortNumber);
			def = (EChangeActivityDefinitionRoot)PersistenceHelper.manager.modify(def);
			
			trx.commit();
			trx = null;

		} catch (Exception e) {
			logger.error("ERROR", e);
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return def;
	}
	
	@Override
	public EChangeActivityDefinitionRoot deleteRootDefinition(Map<String, Object> map) throws WTException {

		Transaction trx = new Transaction();
		EChangeActivityDefinitionRoot root = null;
		try {
			trx.start();

			String oid = ParamUtil.get(map,"root");
			root = (EChangeActivityDefinitionRoot)CommonUtil.getObject(oid);
			
			List<EChangeActivityDefinition> list = getActiveDefinition(root.getPersistInfo().getObjectIdentifier().getId());
			if(list!=null){
				for(EChangeActivityDefinition def : list){
					PersistenceHelper.manager.delete(def);
				}
			}
			root = (EChangeActivityDefinitionRoot)PersistenceHelper.manager.delete(root);
			
			trx.commit();
			trx = null;

		} catch (Exception e) {
			logger.error("ERROR", e);
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return root;
	}
	
	@Override
	public EChangeActivityDefinition createDefinition(Map<String, Object> map) throws WTException {
		EChangeActivityDefinition def = null;
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String root = ParamUtil.get(map,"root");
			String name = ParamUtil.get(map,"name");
			String engName = ParamUtil.get(map,"engName");
			String description = ParamUtil.get(map,"description");
			int sortNumber = ParamUtil.getInt(map,"sortNumber");
			String activeType = ParamUtil.get(map,"activeType");
			String step = ParamUtil.get(map,"step");
//			String userId = ParamUtil.get(map,"userId");
			def = EChangeActivityDefinition.newEChangeActivityDefinition();
			def.setName(name);
			def.setName_eng(engName);
			def.setDescription(description);
			def.setSortNumber(sortNumber);
			def.setActiveType(activeType);
            def.setStep(step);
            def.setWorker(SessionHelper.manager.getPrincipalReference());
//            WTPrincipalReference owner = null;
//            People people = (People)CommonUtil.getObject(userId);
//			PeopleData pd = new PeopleData(people);
//			
//			if(pd.getWtuserOID()!=null){
//				WTUser wtUser = (WTUser) CommonUtil.getObject(pd.getWtuserOID());
//				owner = WTPrincipalReference.newWTPrincipalReference(wtUser);
//			}
            def.setRoot((EChangeActivityDefinitionRoot)CommonUtil.getObject(root));
			def = (EChangeActivityDefinition)PersistenceHelper.manager.save(def);
			
			trx.commit();
			trx = null;
			
		} catch (Exception e) {
			logger.error("ERROR", e);
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return def;
	}
	
	@Override
	public EChangeActivityDefinition updateDefinition(Map<String, Object> map) throws WTException {
		EChangeActivityDefinition def = null;
		Transaction trx = new Transaction();
		try {
			trx.start();

			String oid = ParamUtil.get(map,"oid");
			String name = ParamUtil.get(map,"name");
			String engName = ParamUtil.get(map,"engName");
			String description = ParamUtil.get(map,"description");
			int sortNumber = ParamUtil.getInt(map,"sortNumber");
			String activeType = ParamUtil.get(map,"activeType");
			String step = ParamUtil.get(map,"step");
			String userId = ParamUtil.get(map,"userId");
			def = (EChangeActivityDefinition)CommonUtil.getObject(oid);
			
			boolean isUser = false;
			if(userId!=null){
				People people = (People)CommonUtil.getObject(userId);
				PeopleData pd = new PeopleData(people);
				WTPrincipalReference owner = null;
				if(pd.getWtuserOID()!=null){
					WTUser wtUser = (WTUser) CommonUtil.getObject(pd.getWtuserOID());
					owner = WTPrincipalReference.newWTPrincipalReference(wtUser);
					def.setWorker(owner);
					isUser = true;
				}
			}
			if(!isUser){
				def.setWorker(null);
			}
			def.setName(name);
			def.setName_eng(engName);
			def.setDescription(description);
			def.setSortNumber(sortNumber);
			def.setActiveType(activeType);
            def.setStep(step);
			def = (EChangeActivityDefinition)PersistenceHelper.manager.modify(def);
			
			trx.commit();
			trx = null;

		} catch (Exception e) {
			logger.error("ERROR", e);
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return def;
	}
	
	@Override
	public EChangeActivityDefinition deleteDefinition(Map<String, Object> reqMap) throws WTException {
		
		Transaction trx = new Transaction();
		EChangeActivityDefinition def = null;
		try {
			trx.start();
			
			
			List<Map<String, Object>> oids = (List<Map<String, Object>>) reqMap.get("activity");
			for(Map<String, Object> map : oids) {
	    		String oid = (String) map.get("oid");
	    		def = (EChangeActivityDefinition)CommonUtil.getObject(oid);
	    		def = (EChangeActivityDefinition)PersistenceHelper.manager.delete(def);
	    	}
			
			trx.commit();
			trx = null;
			
		} catch (Exception e) {
			logger.error("ERROR", e);
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return def;
	}
	
	@Override
	public ArrayList<Versioned> getTargetList(EChangeOrder2 eco) throws WTException{
		ArrayList<Versioned> list = new ArrayList<Versioned>();
		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(EcoTargetLink.class,true);
		qs.appendWhere(new SearchCondition(EcoTargetLink.class,"roleAObjectRef.key.id","=",eco.getPersistInfo().getObjectIdentifier().getId()),new int[]{ii});
		qs.appendOrderBy(new OrderBy(new ClassAttribute(EcoTargetLink.class,"roleAObjectRef.key.classname"),false),new int[]{ii});
		QueryResult qr = PersistenceHelper.manager.find(qs);
		while(qr.hasMoreElements()){
			Object[] o = (Object[])qr.nextElement();
			EcoTargetLink link = (EcoTargetLink)o[0];
			list.add(link.getTarget());
		}
		return list;
	}
	
	@Override
	public void connectEca(E3PSDocument doc , Map hash) throws WTException, WTPropertyVetoException{
		String activeOid = ParamUtil.get(hash, "activeOid");
		String activeLinkOid = ParamUtil.get(hash, "activeLinkOid");
		String activeLinkType = ParamUtil.get(hash, "activeLinkType");
		//관련 설계변경
		if(activeOid!=null && activeOid.length()>0){
			EChangeActivity act = (EChangeActivity)CommonUtil.getObject(activeOid);
			
			DocumentActivityOutput output = null;
			if(activeLinkOid!=null && activeLinkOid.length()>0 && !"undefined".equals(activeLinkOid)){
				output = (DocumentActivityOutput)CommonUtil.getObject(activeLinkOid);
			}else{
				output = DocumentActivityOutput.newDocumentActivityOutput();
			}
			output.setActivity(act);
			String number = null;
			String version = null;
			if(DocumentActivityOutput.OLD_LINK.equals(activeLinkType)){
				output.setDocumentOldNumber(doc.getNumber());
				output.setDocumentOldVersion(VersionControlHelper.getVersionIdentifier(doc).getSeries().getValue());
			}else{
				output.setDocumentNewNumber(doc.getNumber());
				output.setDocumentNewVersion(VersionControlHelper.getVersionIdentifier(doc).getSeries().getValue());
			}
			PersistenceHelper.manager.save(output);
		}
	}
	
	@Override
	public void connectEcaPart(WTPart doc , Map hash) throws WTException, WTPropertyVetoException{
		String activeOid = ParamUtil.get(hash, "activeOid");
		String activeLinkOid = ParamUtil.get(hash, "activeLinkOid");
		String activeLinkType = ParamUtil.get(hash, "activeLinkType");
		//관련 설계변경
		if(activeOid!=null && activeOid.length()>0){
			EChangeActivity act = (EChangeActivity)CommonUtil.getObject(activeOid);
			
			
			ReferenceFactory rf = new ReferenceFactory();
			
			if(act.getOrder() instanceof EChangeOrder2){
				EChangeOrder2 eco =  (EChangeOrder2)act.getOrder();
				EcoPartLink link = EcoPartLink.newEcoPartLink((WTPartMaster)doc.getMaster(), eco); 
				link.setGubun(activeLinkType);
				link.setVersion(VersionControlHelper.getVersionDisplayIdentifier(doc).toString());
				PersistenceHelper.manager.save(link);
			}else if(act.getOrder() instanceof EChangeRequest2){
				EChangeRequest2 ecr =  (EChangeRequest2)act.getOrder();
				EcrPartLink link = EcrPartLink.newEcrPartLink((WTPartMaster)doc.getMaster(), ecr); 
				link.setVersion(VersionControlHelper.getVersionDisplayIdentifier(doc).toString());
				PersistenceHelper.manager.save(link);
			}
			
			
		}
	}
	
	
	
	@Override
	public String deleteNotifyItem(String oid) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();
			if (oid != null) {
				ReferenceFactory f = new ReferenceFactory();
				EChangeNoticeItem noticeItem = (EChangeNoticeItem) f.getReference(oid).getObject();
				
				PersistenceHelper.manager.delete(noticeItem);
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
		return "삭제 되었습니다.";
	}
	
	@Override
	public void updateNotifyItem(Map hash) throws WTException{
		EChangeNoticeItem ecn = null;
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String oid 					= ParamUtil.get(hash,"oid");
			
			String noti_changeType 		= ParamUtil.get(hash,"changeType");
			String noti_partNumber 		= ParamUtil.get(hash,"partNumber");
			String noti_partName 		= ParamUtil.get(hash,"partName");
			String noti_drawingNumber 	= ParamUtil.get(hash,"drawing");
			String noti_stockQuantity 	= ParamUtil.get(hash,"stockQuantity");
			String noti_orderQuantity 	= ParamUtil.get(hash,"orderQuantity");
			String noti_sign 			= ParamUtil.get(hash,"sign");
			String noti_stock 			= ParamUtil.get(hash,"stock");
//			String stockCode = NumberCodeHelper.service.getName("STOCKCOMP", noti_stock);
			String noti_applyDate 		= ParamUtil.get(hash,"applyDate");
			String noti_before 			= ParamUtil.get(hash,"before");
			String noti_after 			= ParamUtil.get(hash,"after");
			
			ReferenceFactory rf = new ReferenceFactory();
			ecn = (EChangeNoticeItem)rf.getReference(oid).getObject();
			
			ecn.setChangeType(noti_changeType);
			ecn.setPartNumber(noti_partNumber);
			ecn.setPartName(noti_partName);
			ecn.setDrawingNumber(noti_drawingNumber);
			ecn.setStockQuantity(noti_stockQuantity);
			ecn.setOrderQuantity(noti_orderQuantity);
//			ecn.setStock(stockCode);
			ecn.setApplyDate(noti_applyDate);
			ecn.setBefore(noti_before);
			ecn.setAfter(noti_after);
			ecn.setSign(noti_sign);
			
			ecn = (EChangeNoticeItem) PersistenceHelper.manager.modify(ecn);

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
	public List<EChangeOrder2> searchEcoFromTraget(Versioned target) throws Exception {
		List<EChangeOrder2> list = new ArrayList<EChangeOrder2>();
		
		String number = "";
		Class targetClass = null;
		if(target instanceof E3PSDocument){
			targetClass = E3PSDocument.class;
			number = ((E3PSDocument)target).getNumber();
		}else if(target instanceof EPMDocument){
			targetClass = EPMDocument.class;
			number = ((EPMDocument)target).getNumber();
		}else if(target instanceof WTPart){
			targetClass = WTPart.class;
			number = ((WTPart)target).getNumber();
		}
		
		String version = VersionControlHelper.getVersionIdentifier(target).getSeries().getValue();
		
		
		QuerySpec qs = new QuerySpec();
		int ii = qs.addClassList(EChangeOrder2.class,true);
		int jj = qs.addClassList(EChangeActivity.class,false);
		int kk = qs.addClassList(DocumentActivityOutput.class,false);
		
		qs.appendWhere(new SearchCondition(EChangeOrder2.class,"thePersistInfo.theObjectIdentifier.id",EChangeActivity.class,"orderReference.key.id"),new int[]{ii,jj});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(EChangeActivity.class,"thePersistInfo.theObjectIdentifier.id",DocumentActivityOutput.class,"activityReference.key.id"),new int[]{jj,kk});
		qs.appendAnd();
		qs.appendOpenParen();
		
		qs.appendOpenParen();
		qs.appendWhere(new SearchCondition(DocumentActivityOutput.class,DocumentActivityOutput.DOCUMENT_NEW_NUMBER,"=",number),new int[]{kk});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(DocumentActivityOutput.class,DocumentActivityOutput.DOCUMENT_NEW_VERSION,"=",version),new int[]{kk});
		qs.appendCloseParen();
		qs.appendOr();
		qs.appendOpenParen();
		qs.appendWhere(new SearchCondition(DocumentActivityOutput.class,DocumentActivityOutput.DOCUMENT_OLD_NUMBER,"=",number),new int[]{kk});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(DocumentActivityOutput.class,DocumentActivityOutput.DOCUMENT_OLD_VERSION,"=",version),new int[]{kk});
		qs.appendCloseParen();
		
		qs.appendCloseParen();
		QueryResult  aqr = PersistenceHelper.manager.find(qs);
		
		boolean isCompletable = aqr.size()>0;
		
		
		while(aqr.hasMoreElements()){
			Object[] o = (Object[])aqr.nextElement();	
			EChangeOrder2 eco = (EChangeOrder2)o[0];	
			if(!list.contains(eco)){
				list.add(eco);
			}
		}
		
		qs = new QuerySpec();
		ii = qs.addClassList(EChangeOrder2.class,true);
		jj = qs.addClassList(EcoTargetLink.class,false);
		int dd = qs.addClassList(EcoTargetResultLink.class,false);
		kk = qs.addClassList(targetClass,false);
		qs.appendOpenParen();
		qs.appendWhere(new SearchCondition(EChangeOrder2.class,"thePersistInfo.theObjectIdentifier.id",EcoTargetLink.class,"roleAObjectRef.key.id"),new int[]{ii,jj});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(EcoTargetLink.class,"roleBObjectRef.key.id",targetClass,"thePersistInfo.theObjectIdentifier.id"),new int[]{jj,kk});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(targetClass,"master>number","=",number),new int[]{kk});
		qs.appendCloseParen();
		qs.appendOr();
		qs.appendOpenParen();
		qs.appendWhere(new SearchCondition(EChangeOrder2.class,"thePersistInfo.theObjectIdentifier.id",EcoTargetResultLink.class,"roleAObjectRef.key.id"),new int[]{ii,dd});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(EcoTargetResultLink.class,"roleBObjectRef.key.id",targetClass,"thePersistInfo.theObjectIdentifier.id"),new int[]{dd,kk});
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(targetClass,"master>number","=",number),new int[]{kk});
		qs.appendCloseParen();
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(targetClass,"versionInfo.identifier.versionId","=",version),new int[]{kk});
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		while(qr.hasMoreElements()){
			Object[] o = (Object[])qr.nextElement();
			EChangeOrder2 eco = (EChangeOrder2)o[0];
			if(!list.contains(eco)){
				list.add(eco);
			}
		}
		
		return list;
	}
	
	@Override
	public EChangeOrder2 stopECO(Map<String, Object> reqMap)throws WTException{
		EChangeOrder2 eco = null;
		Transaction trx = new Transaction();
		try {
			trx.start();
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			String comment = StringUtil.checkNull((String) reqMap.get("comment"));
		    
		    eco = (EChangeOrder2)CommonUtil.getObject(oid);
		   // eco.setOrderState(ChangeService.ECO_STOPPED);
		    eco = (EChangeOrder2)PersistenceHelper.manager.modify(eco);
			
		    QuerySpec qs = new QuerySpec();
		    int ii = qs.addClassList(EChangeActivity.class,true);
		    qs.appendWhere(new SearchCondition(EChangeActivity.class,"orderReference.key.id","=",eco.getPersistInfo().getObjectIdentifier().getId()),new int[]{ii});
		    qs.appendOrderBy(new OrderBy(new ClassAttribute(EChangeActivity.class,"sortNumber"),false),new int[]{ii});
		    QueryResult result = PersistenceHelper.manager.find(qs);
		    
		    while(result.hasMoreElements()){
		    	Object[] o = (Object[]) result.nextElement();
		    	EChangeActivity act = (EChangeActivity)o[0];
		    	
		    	String state = act.getActiveState();
		    	
		    	if(ChangeService.ACTIVITY_WORKING.equals(state)){
		    		act.setActiveState(ChangeService.ACTIVITY_STOPPED);
		    		
		    		ApprovalMaster master = ApprovalHelper.service.getApprovalMaster(act);
		            
		    		if(master!=null){
			            QuerySpec qs2 = new QuerySpec(ApprovalLine.class);
			            qs2.appendWhere(new SearchCondition(ApprovalLine.class,"masterReference.key.id","=",master.getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
			            qs2.appendAnd();
			            qs2.appendWhere(new SearchCondition(ApprovalLine.class, "state", "=",
			    				ApprovalService.LINE_WORKING), new int[] { 0 });
			            QueryResult qr2 = PersistenceHelper.manager.find(qs2);
			            
			            while(qr2.hasMoreElements()){
			            	ApprovalLine line = (ApprovalLine)qr2.nextElement();
			            	PersistenceHelper.manager.modify(line);
			            }
			            
			            PersistenceHelper.manager.modify(master);
		    		}
		    		
		    		PersistenceHelper.manager.modify(act);
		    	}
		    }
		    
		    EChangeStopStartHistory ssh = EChangeStopStartHistory.newEChangeStopStartHistory();
		    ssh.setComments(comment);
		    ssh.setActiveDate(DateUtil.getCurrentTimestamp());
		    ssh.setOwner(SessionHelper.manager.getPrincipalReference());
		    ssh.setContainer(WCUtil.getPDMLinkProduct());
		    ssh.setEco(eco);
		    ssh.setStop(true);
		    ssh = (EChangeStopStartHistory)PersistenceHelper.manager.save(ssh);
		    
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return eco;
	}
	
	@Override
	public EChangeOrder2 restartECO(Map<String, Object> reqMap)throws WTException{
		EChangeOrder2 eco = null;
		Transaction trx = new Transaction();
		try {
			trx.start();

			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			String comment = StringUtil.checkNull((String) reqMap.get("comment"));
		    LOGGER.info("oid -> "+oid+"/ comment -> "+comment);
		    eco = (EChangeOrder2)CommonUtil.getObject(oid);
		   // eco.setOrderState(ChangeService.ECO_ECA_WORKING);
		    eco = (EChangeOrder2)PersistenceHelper.manager.modify(eco);
			
		    QuerySpec qs = new QuerySpec();
		    int ii = qs.addClassList(EChangeActivity.class,true);
		    qs.appendWhere(new SearchCondition(EChangeActivity.class,"orderReference.key.id","=",eco.getPersistInfo().getObjectIdentifier().getId()),new int[]{ii});
		    qs.appendOrderBy(new OrderBy(new ClassAttribute(EChangeActivity.class,"sortNumber"),false),new int[]{ii});
		    QueryResult result = PersistenceHelper.manager.find(qs);
		    
		    while(result.hasMoreElements()){
		    	Object[] o = (Object[]) result.nextElement();
		    	EChangeActivity act = (EChangeActivity)o[0];
		    	
		    	String state = act.getActiveState();
		    	
		    	if(ChangeService.ACTIVITY_STOPPED.equals(state)){
		    		act.setActiveState(ChangeService.ACTIVITY_WORKING);
		    		
		    		ApprovalMaster master = ApprovalHelper.service.getApprovalMaster(act);
		            
		    		if(master!=null){
			            QuerySpec qs2 = new QuerySpec(ApprovalLine.class);
			            qs2.appendWhere(new SearchCondition(ApprovalLine.class,"masterReference.key.id","=",master.getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
			            qs2.appendAnd();
			            qs2.appendWhere(new SearchCondition(ApprovalLine.class, "state", "=",
			    				ApprovalService.LINE_CANCELED), new int[] { 0 });
			            QueryResult qr2 = PersistenceHelper.manager.find(qs2);
			            
			            while(qr2.hasMoreElements()){
			            	ApprovalLine line = (ApprovalLine)qr2.nextElement();
			            	PersistenceHelper.manager.modify(line);
			            }
			            
			            PersistenceHelper.manager.modify(master);
		    		}
		    		
		    		PersistenceHelper.manager.modify(act);
		    	}
		    }
		    
		    EChangeStopStartHistory ssh = EChangeStopStartHistory.newEChangeStopStartHistory();
		    ssh.setComments(comment);
		    ssh.setActiveDate(DateUtil.getCurrentTimestamp());
		    ssh.setOwner(SessionHelper.manager.getPrincipalReference());
		    ssh.setContainer(WCUtil.getPDMLinkProduct());
		    ssh.setEco(eco);
		    ssh.setStop(false);
		    ssh = (EChangeStopStartHistory)PersistenceHelper.manager.save(ssh);
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return eco;
	}
	
	@Override
	public QueryResult getStopStartHistory(EChangeOrder2 eco)throws WTException{
		QuerySpec qs = new QuerySpec();
		
		int ii = qs.addClassList(EChangeStopStartHistory.class, true);
		qs.appendWhere(new SearchCondition(EChangeStopStartHistory.class,"ecoReference.key.id","=",eco.getPersistInfo().getObjectIdentifier().getId()),new int[]{ii});
		qs.appendOrderBy(new OrderBy(new ClassAttribute(EChangeStopStartHistory.class,EChangeStopStartHistory.ACTIVE_DATE),false),new int[]{ii});
		
		return PersistenceHelper.manager.find(qs);
	}

	@Override
	public EChangeOrder2 createEco(Map<String, Object> reqMap) throws WTException {
		EChangeOrder2 eco = null;
		Transaction trx = new Transaction();
		try {
			trx.start();
			String name = StringUtil.checkNull((String) reqMap.get("name"));
			String applyDate = StringUtil.checkNull((String) reqMap.get("applyDate"));
			String relatedECR = StringUtil.checkNull((String) reqMap.get("relatedECR"));
			String relatedProject = StringUtil.checkNull((String) reqMap.get("relatedProject"));
			String primary = StringUtil.checkNull((String) reqMap.get("PRIMARY"));
			String customer = StringUtil.checkNull((String) reqMap.get("customer"));
			String echangeReason = StringUtil.checkNull((String) reqMap.get("echangeReason"));
			String description = StringUtil.checkNull((String) reqMap.get("description"));
			String appState = StringUtil.checkNull((String) reqMap.get("appState"));
			String changeOwner = StringUtil.checkNull((String) reqMap.get("changeOwner"));
			String finishDate = StringUtil.checkNull((String) reqMap.get("finishDate"));
			
			People cOwner = (People) CommonUtil.getObject(changeOwner);
			
			List<String> secondary = StringUtil.checkReplaceArray(reqMap.get("SECONDARY"));
			List<String> delocIds = StringUtil.checkReplaceArray(reqMap.get("delocIds"));
			List<Map<String,Object>> ecrContentsList = (List<Map<String,Object>>)reqMap.get("ecrContentsList");
			List<Map<String, Object>> approvalList = (List<Map<String, Object>>) reqMap.get("approvalList");
			
			ReferenceFactory rf = new ReferenceFactory();
			
			eco = EChangeOrder2.newEChangeOrder2();
			String number = "ECO-" + DateUtil.getCurrentDateString("month")	+ "-";
			String seqNo = SequenceDao.manager.getSeqNo(number, "0000",	"EChangeOrder2", "orderNumber");
			number = number + seqNo;
			
			LOGGER.info("applyDate -> "+applyDate);
			eco.setOrderNumber(number);
			eco.setName(name);
			eco.setOwner(SessionHelper.manager.getPrincipalReference());
			eco.setContainer(WCUtil.getPDMLinkProduct());
			eco.setCustomer(customer);
			eco.setEchangeReason(echangeReason);
			eco.setDescription(description);
			eco.setChangeOwner(cOwner.getName());
			
			if(applyDate.contains("specificDate")) {
				String[] applyDateArr = applyDate.split(",");
				eco.setApplyDate(applyDateArr[0]);
				eco.setSpecificDate(DateUtil.convertDate(applyDateArr[1]));
			} else {
				eco.setApplyDate(applyDate);
			}
			
			reqMap.put("lifecycle", "LC_ECO");
			reqMap.put("location", "/Default/EChange/ECO");
			
			WTContainerRef wtContainerRef = WCUtil.getWTContainerRef();
			Folder folder= FolderTaskLogic.getFolder("/Default/EChange/ECO", wtContainerRef);
			FolderHelper.assignLocation((FolderEntry) eco, folder);
			eco = (EChangeOrder2)CommonHelper.service.setVersiondDefault(eco, reqMap);
			
			eco = (EChangeOrder2) PersistenceHelper.manager.save(eco);
			
			List<Map<String,Object>> partList = (List<Map<String,Object>>)reqMap.get("partList");
			createECOPartLink(partList, eco);
			
			WTCollection wtc = new WTArrayList();
			
			for(int i=0; i <ecrContentsList.size(); i++) {
				EChangeContents ec = EChangeContents.newEChangeContents();
				ec.setName(StringUtil.checkNull((String)ecrContentsList.get(i).get("name")));
				ec.setContents(StringUtil.checkNull((String)ecrContentsList.get(i).get("contents")));
				ec.setSort(i);
				ec.setEchange(eco);
				wtc.add(ec);			
			}
			
			PersistenceHelper.manager.save(wtc);
			
			if(!relatedECR.equals("")) {
				EChangeRequest2 ecr = (EChangeRequest2) rf.getReference(relatedECR).getObject();
				RequestOrderLink ecrlink = RequestOrderLink.newRequestOrderLink(eco, ecr);
				PersistenceHelper.manager.save(ecrlink);
			}
			
			int sortCount = 0;
			List<Map<String, Object>> ecaList = (List<Map<String, Object>>) reqMap.get("ecaList");
			for(Map<String, Object> map : ecaList) {
	    		String epmOid = (String) map.get("oid");
	    		EChangeActivity changeActivity = EChangeActivity.newEChangeActivity();
	    		changeActivity.setActiveState(ChangeService.ACTIVITY_STANDBY);
				changeActivity.setContainer(WCUtil.getPDMLinkProduct());
				changeActivity.setOrder(eco);
				changeActivity.setEcafinishDate(null);
				changeActivity.setName((String) map.get("activityName"));
				changeActivity.setStep((String) map.get("step"));
				changeActivity.setSortNumber(sortCount);
				changeActivity.setActiveType((String) map.get("activityType"));
				changeActivity.setFinishDate(DateUtil.convertDate(finishDate));
				changeActivity.setDescription((String) map.get("desc"));
				People people = null;
				people = cOwner;
				WTUser user = people.getUser();
				WTPrincipalReference ref = WTPrincipalReference.newWTPrincipalReference(user);
				changeActivity.setOwner(ref);
				PersistenceHelper.manager.save(changeActivity);
				sortCount++;
	    	}
			
			if(secondary != null) {
				CommonContentHelper.service.attach(eco, null, secondary, delocIds,false);
			}
			
			// 결재
			ApprovalHelper.service.registApproval(eco, approvalList, appState, null);
			eco = (EChangeOrder2)PersistenceHelper.manager.refresh(eco);

			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return eco;
	}
	
	/**
	 * ECO 관련 품목 Link 생성
	 * @methodName : createECOPartLink
	 * @author : tsuam
	 * @date : 2021.11.16
	 * @return : void
	 * @description :
	 */
	private void createECOPartLink(List<Map<String,Object>> partList,EChangeOrder2 eco) throws Exception{
		
		//ObjectLogger.debug(relPartOids, "relPartOids");
		if(partList != null) {
			for(Map<String,Object> map : partList) {
				String partOid = (String)map.get("oid");
				WTPart part = (WTPart)CommonUtil.getObject(partOid);
				if(part != null) {
					String version = VersionControlHelper.getVersionIdentifier(part).getSeries().getValue();
					
					EcoPartLink link = EcoPartLink.newEcoPartLink((WTPartMaster)part.getMaster(),eco);
					link.setVersion(version);
					PersistenceHelper.manager.save(link);
				}
			}
		}
		
	}

	@Override
	public void connectEca(E3PSDocument doc, String activeOid, String activeLinkOid, String activeLinkType)
			throws WTException, WTPropertyVetoException {
		if(activeOid!=null && activeOid.length()>0){
			EChangeActivity act = (EChangeActivity)CommonUtil.getObject(activeOid);
			
			DocumentActivityOutput output = null;
			if(activeLinkOid!=null && activeLinkOid.length()>0 && !"undefined".equals(activeLinkOid)){
				output = (DocumentActivityOutput)CommonUtil.getObject(activeLinkOid);
			}else{
				output = DocumentActivityOutput.newDocumentActivityOutput();
			}
			output.setActivity(act);
			String number = null;
			String version = null;
			if(DocumentActivityOutput.OLD_LINK.equals(activeLinkType)){
				output.setDocumentOldNumber(doc.getNumber());
				output.setDocumentOldVersion(VersionControlHelper.getVersionIdentifier(doc).getSeries().getValue());
			}else{
				output.setDocumentNewNumber(doc.getNumber());
				output.setDocumentNewVersion(VersionControlHelper.getVersionIdentifier(doc).getSeries().getValue());
			}
			PersistenceHelper.manager.save(output);
		}
		
	}

	@Override
	public EChangeOrder2 updateEco(Map<String, Object> reqMap) throws WTException {
		EChangeOrder2 eco = null;
		EChangeActivity changeActivity = null;
		Transaction trx = new Transaction();
		try {
			trx.start();
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			String name = StringUtil.checkNull((String) reqMap.get("name"));
			String applyDate = StringUtil.checkNull((String) reqMap.get("applyDate"));
			String relatedECR = StringUtil.checkNull((String) reqMap.get("relatedECR"));
			String relatedProject = StringUtil.checkNull((String) reqMap.get("relatedProject"));
			String primary = StringUtil.checkNull((String) reqMap.get("PRIMARY"));
			String customer = StringUtil.checkNull((String) reqMap.get("customer"));
			String echangeReason = StringUtil.checkNull((String) reqMap.get("echangeReason"));
			String description = StringUtil.checkNull((String) reqMap.get("description"));
			String appState = StringUtil.checkNull((String) reqMap.get("appState"));
			String changeOwner = StringUtil.checkNull((String) reqMap.get("changeOwner"));
			String finishDate = StringUtil.checkNull((String) reqMap.get("finishDate"));
			String ecaOid = StringUtil.checkNull((String) reqMap.get("ecaOid"));
			List<String> changeOwners = StringUtil.checkReplaceArray(reqMap.get("changeOwner"));
			
			People cOwner = (People) CommonUtil.getObject(changeOwner);
			
			List<String> secondary = StringUtil.checkReplaceArray(reqMap.get("SECONDARY"));
			List<String> delocIds = StringUtil.checkReplaceArray(reqMap.get("delocIds"));
			List<Map<String,Object>> ecrContentsList = (List<Map<String,Object>>)reqMap.get("ecrContentsList");
			
			eco = (EChangeOrder2) CommonUtil.getObject(oid);
			
			eco.setName(name);
			eco.setEchangeReason(echangeReason);
			eco.setDescription(description);
			eco.setChangeOwner(cOwner.getName());
			
			if(applyDate.contains("specificDate")) {
				String[] applyDateArr = applyDate.split(",");
				eco.setApplyDate(applyDateArr[0]);
				eco.setSpecificDate(DateUtil.convertDate(applyDateArr[1]));
			} else {
				eco.setApplyDate(applyDate);
			}
			
			eco = (EChangeOrder2) PersistenceHelper.manager.modify(eco);
			
			ChangeHelper.service.deleteEChangeContents(oid);
			
			List<Map<String,Object>> partList = (List<Map<String,Object>>)reqMap.get("partList");
			modifyECOPartLink(partList, eco);
			
			WTCollection wtc = new WTArrayList();
			
			for(int i=0; i <ecrContentsList.size(); i++) {
				EChangeContents ec = EChangeContents.newEChangeContents();
				ec.getEchangeReference();
				ec.setName(StringUtil.checkNull((String)ecrContentsList.get(i).get("name")));
				ec.setContents(StringUtil.checkNull((String)ecrContentsList.get(i).get("contents")));
				ec.setSort(i);
				ec.setEchange(eco);
				wtc.add(ec);			
			}
			
			PersistenceHelper.manager.save(wtc);
			
			changeActivity = (EChangeActivity) CommonUtil.getObject(ecaOid);
			
			People people = null;
			people = cOwner;
			WTUser user = people.getUser();
			WTPrincipalReference ref = WTPrincipalReference
					.newWTPrincipalReference(user);
			changeActivity.setOwner(ref);
			changeActivity.setFinishDate(DateUtil.convertDate(finishDate));
			
			changeActivity = (EChangeActivity) PersistenceHelper.manager.modify(changeActivity);
			
			
			CommonContentHelper.service.attach((ContentHolder)eco, null, secondary, delocIds,false);

			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return eco;
	}
	
	//TODO
	/***********************************************************************************************************/
	
	/**
	 * 도면 BOM 변경에서 선택적으로 선택한 품목 개정 
	 * @methodName : batchECOPartRevise
	 * @author : tsuam
	 * @date : 2022.02.25
	 * @return : Map<String,Object>
	 * @description :
	 */
	@Override
	public Map<String,Object> batchECOPartRevise(Map<String, Object> reqMap) throws Exception{
		String ecoOid = (String)reqMap.get("oid");
		EChangeOrder2 eco = (EChangeOrder2)CommonUtil.getObject(ecoOid);
		//List<EcoPartLink> linkList = ECOHelper.manager.getECOPartLink(eco);
		Map<String,Object> returnMap = new HashMap<String, Object>();
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> reviseList =  new ArrayList<Map<String,Object>>();
		Transaction trx = null;
		try {
			trx = new Transaction();
			trx.start();
			
			boolean isAllRevise = true;
			boolean isDwgRevise = true;
			List<Map<String,Object>> checkItemList = (List<Map<String,Object>>)reqMap.get("checkItemList");
			//정합서 체크 linkOid //linkOid
			//for(EcoPartLink link : linkList) {
			 for(Map<String,Object> checkMap : checkItemList) {
				//String linkOid = checkMap
				Map<String,Object> item = (Map<String,Object>)checkMap.get("item");
				String linkOid = (String)item.get("linkOid");	
				
				EcoPartLink link = (EcoPartLink)CommonUtil.getObject(linkOid);
						
				WTPartMaster master = link.getPart();
				String number = master.getNumber();
				String version = link.getVersion();
				boolean isRevise = link.isRevise();
				
				if(!isRevise) {
					WTPart part = PartHelper.manager.getPart(number, version);
					String state = part.getLifeCycleState().toString();
					System.out.println("number =" + number +",state=" + state);
					if(!state.equals("APPROVED")) {
						continue;
					}
					
					Map<String,Object> map = ChangeECOHelper.manager.isRevisePart(part, true);
					boolean isRevisePart = (boolean)map.get("isRevisePart");
					
					String message = (String)map.get("message");
					if(!isRevisePart) {
						isDwgRevise = false;
					}
					
					System.out.println("number =" + number +", isRevisePart =" + isRevisePart +",message=" + message);
					Map<String,Object> partMap = new HashMap<String, Object>();
					
					String oid = CommonUtil.getOIDString(part);
					//String number = part.getNumber(); oid,number,reviseResult,message
					partMap.put("linkOid", CommonUtil.getOIDString(link));
					partMap.put("oid", oid);
					partMap.put("number", number);
					partMap.put("reviseResult", isRevisePart);
					partMap.put("message", message);
					partMap.put("isDelete", true);
					
					returnList.add(partMap);
					
				}
				
			}
			
			System.out.println("isDwgRevise =" + isDwgRevise +",returnList =" +returnList.size());
			
			boolean isBatchReviseResult = true;
			//도면 정합성에 이상이 없을시 개정 실행 
			if(isDwgRevise) {
				
				
				reviseList = batchECOPartReviseAction(eco, returnList,reqMap);
				isBatchReviseResult = isBatchReviseResult(reviseList);
				
				System.out.println("isBatchReviseResult =" + isBatchReviseResult);
				returnMap.put("result", isBatchReviseResult);
				returnMap.put("list", reviseList);
			}else {
				returnMap.put("result", false);
				returnMap.put("list", returnList);
			}
			
			if(isBatchReviseResult) {
				trx.commit();
				trx = null;
			}else {
				trx.rollback();
				trx = null;
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("result", false);
			
			throw e;
		} finally {
			if (trx != null) {
				trx.rollback();
				trx = null;
			}
		}
		
		return returnMap;
	}
	
	
	/**
	 * ECO 도면/BOM 변경에서 일괄 개정 
	 * @param eco
	 * @param returnList
	 * @return
	 */
	public List<Map<String,Object>> batchECOPartReviseAction(EChangeOrder2 eco,List<Map<String,Object>> returnList,Map<String, Object> reqMap) {
		
		List<Map<String,Object>> reviseList = new ArrayList<Map<String,Object>>();
		try {
			
			String eoNumber = eco.getOrderNumber();
			for(Map<String,Object> map : returnList) {
				
				//oid,number,reviseResult,message
				
				String oid = (String)map.get("oid");
				String linkOid = (String)map.get("linkOid");
				EcoPartLink link = (EcoPartLink)CommonUtil.getObject(linkOid);
				WTPart part = (WTPart)CommonUtil.getObject(oid);
				
				try {
					
					EPMDocument oldEpm = null;
					EPMDocument newEpm = null;
					
					String reviseNote = "설변" + " ["+eoNumber+"] 를 통해 개정 되었습니다.";
					
					EPMBuildRule ebr = EpmHelper.manager.getBuildRule(part);
					//품목 개정
					WTPart newPart = (WTPart) ObjectUtil.reviseNote(part,reviseNote);
					//속성 업데이트
					reviseAttributeUpdate(newPart);
					
					if(ebr != null) {
						oldEpm = (EPMDocument) ebr.getBuildSource();
						//3D 개정
						newEpm = (EPMDocument) ObjectUtil.reviseNote(oldEpm,reviseNote);
						//3D 속성 업데이트
						reviseAttributeUpdate(newEpm);
						
						EPMBuildRule newEbr = EpmHelper.manager.getBuildRule(newPart);
						newEbr.setBuildSource(newEpm);
						PersistenceServerHelper.manager.update(newEbr);

						EPMBuildHistory ebh = getBuildHistory(newPart, oldEpm);
						if (ebh != null) {
							ebh.setBuiltBy(newEpm);
							PersistenceServerHelper.manager.update(ebh);
						}
						
						QueryResult qr = StructHelper.service.navigateDescribes(oldEpm, false);
						if (qr.size() > 0) {
							while (qr.hasMoreElements()) {
								EPMDescribeLink epmdescribelink = (EPMDescribeLink) qr.nextElement();
								WTPart tempPart = (WTPart) epmdescribelink.getDescribes();
								createEPMDescribeLink(tempPart, newEpm);
							}
						}
					}
					
					//2D 개정
					if(newEpm != null) {
						EPMDocument epm2d =  EpmHelper.manager.getDrawing((EPMDocumentMaster)newEpm.getMaster());
						if(epm2d !=  null){
							EPMDocument newEpm2D = (EPMDocument)ObjectUtil.reviseNote(epm2d,reviseNote);
						}
						
					}
					
					//관련 도면 개정
					if(newPart != null){
						//String applicaionType = epm.getOwnerApplication().toString();  //PUBLISH_AUTO,EPM,MANUAL
						List<EPMDocument> reflist = EpmHelper.manager.getEPMDesribeEPM(newPart);
						
						for(EPMDocument oldEPMDesc: reflist) {
							
							String applicaionType = oldEPMDesc.getOwnerApplication().toString(); 
							//if(!applicaionType.equals("PUBLISH_AUTO")) {
							//개정
							EPMDocument newEPMDesc =(EPMDocument)ObjectUtil.reviseNote(oldEPMDesc,reviseNote);
							
							//삭제
							deleteDescribeLink(newPart, oldEPMDesc);
							
							//추가
							createEPMDescribeLink(newPart, newEPMDesc);
							
							reviseAttributeUpdate(newEPMDesc);
							//}
							
						}
					}
					
					String nextRev = newPart.getVersionIdentifier().getSeries().getValue()+"."+newPart.getIterationIdentifier().getSeries().getValue();
					String nextStateName = newPart.getLifeCycleState().getDisplay(MessageUtil.getLocale());
					
					map.put("reviseResult", true);
					map.put("message", "");
					map.put("nextRev",nextRev);
					map.put("nextStateName",nextStateName);
					map.put("isDelete",false);
					map.put("isRevise",true);
					
					map.put("nextOid",CommonUtil.getOIDString(newPart));
					
					link.setRevise(true);
					PersistenceHelper.manager.modify(link);
				}catch(Exception e) {
					
					System.out.println(" Error Part = " + part.getNumber() +"," + e.getLocalizedMessage());
					e.printStackTrace();
					map.put("reviseResult", false);
					map.put("message", e.getLocalizedMessage());
				}
				
				reviseList.add(map);
			}
			
			
		}catch(Exception e) {
			System.out.println("=========ERRROR ======");
			//e.printStackTrace();
			
			
		}
		System.out.println("reviseList ======" + reviseList.size());
		return reviseList;
	}
	
	/**
	 * 개정 후 속성 업데이트
	 * @methodName : reviseAttributeUpdate
	 * @author : tsuam
	 * @date : 2021.12.22
	 * @return : void
	 * @description :
	 */
	private void reviseAttributeUpdate(RevisionControlled rc) throws Exception{
		
		IBAUtil.deleteIBA((IBAHolder)rc, AttributeKey.EPMKey.IBA_DWGNO, "string");
		IBAUtil.deleteIBA((IBAHolder)rc, AttributeKey.EPMKey.IBA_CHECK, "string");
		IBAUtil.deleteIBA((IBAHolder)rc, AttributeKey.EPMKey.IBA_APPROVAL, "string");
		IBAUtil.deleteIBA((IBAHolder)rc, AttributeKey.EPMKey.IBA_PARTCHECK, "string");
		IBAUtil.deleteIBA((IBAHolder)rc, AttributeKey.EPMKey.IBA_DRAWING, "string");
		IBAUtil.deleteIBA((IBAHolder)rc, AttributeKey.EPMKey.IBA_DWGNO, "string");
		
	}
	
	/**
	 * 품목과 도면 연결
	 * @methodName : createEPMDescribeLink
	 * @author : tsuam
	 * @date : 2021.12.22
	 * @return : boolean
	 * @description :
	 */
	private  boolean createEPMDescribeLink(WTPart _part, EPMDocument _epm) throws Exception{
		
		try {
			EPMDescribeLink link = EPMDescribeLink.newEPMDescribeLink(_part, _epm);
			PersistenceServerHelper.manager.insert(link);
		}catch(Exception e) {
			
			e.printStackTrace();
			throw new Exception(e.getLocalizedMessage());
		}
		return true;
	}
	
	/**
	 * 품목과 도명의 EPMBuildHistory
	 * @methodName : getBuildHistory
	 * @author : tsuam
	 * @date : 2021.12.22
	 * @return : EPMBuildHistory
	 * @description :
	 */
	private  EPMBuildHistory getBuildHistory(WTPart wtpart, EPMDocument epmdocument) throws Exception {
		QueryResult qr = PersistenceHelper.manager.find(EPMBuildHistory.class, wtpart, "built", epmdocument);
		return qr.hasMoreElements() ? (EPMBuildHistory) qr.nextElement() : null;
	}
	
	/**
	 * 품목의 관련 도면 링크 삭제
	 * @methodName : deleteDescribeLink
	 * @author : tsuam
	 * @date : 2021.12.22
	 * @return : boolean
	 * @description :
	 */
	private  boolean deleteDescribeLink(WTPart wtpart, EPMDocument _epm) throws Exception{
		
		try {
			QueryResult qr = PersistenceHelper.manager.navigate(wtpart, "describedBy", EPMDescribeLink.class, false);
			while (qr.hasMoreElements()) {
				EPMDescribeLink link = (EPMDescribeLink) qr.nextElement();
				EPMDocument epmDoc = (EPMDocument) link.getDescribedBy();

				if (CommonUtil.getOIDString(epmDoc).equals(CommonUtil.getOIDString(_epm)))
					PersistenceServerHelper.manager.remove(link);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getLocalizedMessage());
		}
		return true;
	}
	
	/**
	 * 품목 개정 중 에러 존재 여부 체크
	 * @param reviseList
	 * @return
	 */
	private boolean isBatchReviseResult(List<Map<String,Object>> reviseList) {
		
		boolean isBatchReviseResult = true;
		
		for(Map<String,Object> map : reviseList) {
			
			boolean reviseResult = (boolean)map.get("reviseResult");
			
			if(!reviseResult) {
				isBatchReviseResult = false;
				break;
			}
		}
		
		return isBatchReviseResult;
	}
	
	private void modifyECOPartLink(List<Map<String,Object>> partList,EChangeOrder2 eco) throws Exception{
		
		//삭제 
		deleteECOPartLink(eco);
		
		//추가
		createECOPartLink(partList, eco);
	}
	
	
	private void deleteECOPartLink(EChangeOrder2 eco) throws Exception{
		//삭제 
		List<EcoPartLink> linkList = ChangeHelper.manager.getECOPartLink(eco);
		
		for(EcoPartLink link : linkList) {
			PersistenceHelper.manager.delete(link);
		}
	}
}
