package com.e3ps.groupware.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeMap;
import java.util.Vector;

import wt.clients.vc.CheckInOutTaskLogic;
import wt.enterprise.Master;
import wt.enterprise.RevisionControlled;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.fc.WTObject;
import wt.folder.FolderEntry;
import wt.folder.FolderHelper;
import wt.introspection.ClassInfo;
import wt.introspection.WTIntrospector;
import wt.lifecycle.LifeCycleHelper;
import wt.lifecycle.LifeCycleManaged;
import wt.lifecycle.LifeCycleTemplate;
import wt.lifecycle.PhaseTemplate;
import wt.lifecycle.State;
import wt.method.RemoteMethodServer;
import wt.org.WTPrincipal;
import wt.org.WTUser;
import wt.ownership.OwnershipHelper;
import wt.pds.DatabaseInfoUtilities;
import wt.pom.PersistenceException;
import wt.pom.Transaction;
import wt.query.ClassAttribute;
import wt.query.KeywordExpression;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.StandardManager;
import wt.session.SessionHelper;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;
import wt.vc.VersionControlHelper;
import wt.vc.Versioned;
import wt.vc.wip.NonLatestCheckoutException;
import wt.vc.wip.WorkInProgressException;
import wt.vc.wip.Workable;
import wt.workflow.work.WorkItem;

import com.e3ps.common.jdf.config.Config;
import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.jdf.log.Logger;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.query.SearchUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.common.workflow.E3PSWorkflowHelper;
import com.e3ps.groupware.workprocess.AppPerLink;
import com.e3ps.groupware.workprocess.ApprovalLineTemplate;
import com.e3ps.groupware.workprocess.AsmApproval;
import com.e3ps.groupware.workprocess.WFItem;
import com.e3ps.groupware.workprocess.WFItemUserLink;
import com.e3ps.groupware.workprocess.WFObjectWFItemLink;
import com.e3ps.groupware.workprocess.WorkProcessForm;
import com.e3ps.org.People;
import com.e3ps.org.bean.PeopleData;
import com.e3ps.org.service.UserHelper;

public class StandardWFItemService extends StandardManager implements WFItemService {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.GROUPWARE.getName());
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default factory for the class.
	 * @return
	 * @throws WTException
	 */
	public static StandardWFItemService newStandardWFItemService() throws WTException {
		StandardWFItemService instance = new StandardWFItemService();
		instance.initialize();
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#eventListener(java.lang.Object, java.lang.String)
	 */
	@Override
	public void eventListener(Object _obj, String _event) {
		if (_event.equals("STATE_CHANGE")) {
			setState((WTObject) _obj);
		}
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#setState(wt.fc.WTObject)
	 */
	@Override
	public void setState(WTObject _obj) {
		WFItem wfItem = WFItemHelper.service.getWFItem((WTObject) _obj);
		try {
			if (wfItem != null) {
				// WFItem 수정 //
				LifeCycleManaged lm = (LifeCycleManaged) _obj;
				wfItem.setObjectState(lm.getLifeCycleState().toString());
				PersistenceHelper.manager.modify(wfItem);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#getWFItem(wt.fc.WTObject)
	 */
	@Override
	public WFItem getWFItem(WTObject wtobject) {
		WFItem item = null;
		String oid = CommonUtil.getOIDString(wtobject);
		long oidLong = 0;
		String versionStr = " ";
		try {
			if (wtobject instanceof LifeCycleManaged) {
				if (wtobject instanceof RevisionControlled) {
					RevisionControlled rc = (RevisionControlled) wtobject;
					versionStr = rc.getVersionIdentifier().getValue();
					oid = CommonUtil.getOIDString((Master) rc.getMaster());
					oidLong = CommonUtil.getOIDLongValue((Master) rc.getMaster());
				} else {
					oidLong = CommonUtil.getOIDLongValue(wtobject);
				}
			}

			Class target = WFItem.class;
			QuerySpec query = new QuerySpec();
			int idx = query.appendClassList(target, true);
			query.appendWhere(
					new SearchCondition(target, "wfObjectReference.key.classname", "=", oid.substring(0,
							oid.lastIndexOf(":"))), new int[] { idx });
			query.appendAnd();
			query.appendWhere(new SearchCondition(target, "wfObjectReference.key.id", "=", oidLong),
					new int[] { idx });
			query.appendAnd();
			if (!" ".equals(versionStr))
				query.appendWhere(new SearchCondition(target, "objectVersion", "=", versionStr),
						new int[] { idx });
			else
				query.appendWhere(new SearchCondition(target, "objectVersion", true), new int[] { idx });
			QueryResult qr = PersistenceHelper.manager.find(query);
			while (qr.hasMoreElements()) {
				Object[] obj = (Object[]) qr.nextElement();
				WFItem element = (WFItem) obj[0];
				item = element;
			}
			Logger.user.println("> WFItemHelper.manager.getWFItem : query = " + query);
			LOGGER.info("> WFItemHelper.manager.getWFItem : query = " + query);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return item;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#isRemainProcessLine(wt.fc.WTObject)
	 */
	@Override
	public boolean isRemainProcessLine(WTObject wtobject) {
		LOGGER.info("## isRemainProcessLink##");
		boolean result = false;

		// WFItemUserLink 가 유무 검사
		WFItem wfItem = getWFItem(wtobject);
		if (wfItem != null) {
			try {
				QuerySpec query = new QuerySpec();
				query.addClassList(WTUser.class, true);
				int linkIdx = query.addClassList(WFItemUserLink.class, true);
				query.appendWhere(new SearchCondition(WFItemUserLink.class, "state", true),
						new int[] { linkIdx });
				query.appendAnd();
				query.appendWhere(new SearchCondition(WFItemUserLink.class, "processOrder", ">", 0),
						new int[] { linkIdx });
				query.appendAnd();
				query.appendWhere(new SearchCondition(WFItemUserLink.class, "activityName", "<>", "수신"),
						new int[] { linkIdx });
				query.appendAnd();
				query.appendWhere(new SearchCondition(WFItemUserLink.class, "activityName", "<>", "기안"),
						new int[] { linkIdx });
				query.appendAnd();
				query.appendWhere(new SearchCondition(WFItemUserLink.class, "disabled",
						SearchCondition.IS_FALSE), new int[] { linkIdx });

				QueryResult qr = PersistenceHelper.manager.navigate(wfItem, "user", query, false);

				LOGGER.info("[isRemainProcessLine]:" + qr.size());
				result = qr.hasMoreElements() ? true : false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#getNextActivityName(wt.fc.WTObject)
	 */
	@Override
	public String getNextActivityName(WTObject wtobject) {
		String result = "결재";

		WFItem wfItem = getWFItem(wtobject);
		WFItemUserLink link = null;
		LOGGER.info("[wfItem OID]: " + wfItem.getPersistInfo().getObjectIdentifier().toString());
		if (wfItem != null) {
			try {
				QuerySpec query = new QuerySpec();
				query.addClassList(WTUser.class, true);
				int linkIdx = query.addClassList(WFItemUserLink.class, true);
				query.appendWhere(new SearchCondition(WFItemUserLink.class, "state", true),
						new int[] { linkIdx });
				query.appendAnd();
				query.appendWhere(new SearchCondition(WFItemUserLink.class, "disabled",
						SearchCondition.IS_FALSE), new int[] { linkIdx });
				query.appendAnd();
				query.appendWhere(new SearchCondition(WFItemUserLink.class, "processOrder", ">", 0),
						new int[] { linkIdx });
				SearchUtil.setOrderBy(query, WFItemUserLink.class, linkIdx, "processOrder", "asc");

				LOGGER.info("getNextActivityName	:	:	:" + query);
				QueryResult qr = PersistenceHelper.manager.navigate(wfItem, "user", query, false);
				LOGGER.info("getNextActivityName22	:	:	:" + qr.size());
				int tempOrder = -1;
				while (qr.hasMoreElements()) {
					link = (WFItemUserLink) qr.nextElement();
					if (tempOrder == -1) {
						LOGGER.info("wfItem tempOrder == -1: "
								+ wfItem.getPersistInfo().getObjectIdentifier().toString());
						tempOrder = link.getProcessOrder();
						LOGGER.info("getNextActivityName33	:	:	:" + tempOrder);

						result = link.getActivityName();
						LOGGER.info("getNextActivityName44	:	:	:" + result);
						LOGGER.info("getNextActivityName44-1	:	:	:" + wtobject);

						E3PSWorkflowHelper.manager.deleteRole((LifeCycleManaged) wtobject, "APPROVER");
						LOGGER.info("getNextActivityName55	:	:	:");
						E3PSWorkflowHelper.manager.addParticipant((LifeCycleManaged) wtobject, "APPROVER",
								link.getUser().getName());
						LOGGER.info("getNextActivityName66	:	:	:");
						link.setReceiveDate(new java.sql.Timestamp(new java.util.Date().getTime()));
						PersistenceHelper.manager.modify(link);
					} else if (tempOrder == link.getProcessOrder()) {
						LOGGER.info("wfItem link.getProcessOrder(): " + link.getProcessOrder());
						// 협조, 합의 일때 세팅됨.
						E3PSWorkflowHelper.manager.addParticipant((LifeCycleManaged) wtobject, "APPROVER",
								link.getUser().getName());
						result = link.getActivityName();
						link.setReceiveDate(new java.sql.Timestamp(new java.util.Date().getTime()));
						PersistenceHelper.manager.modify(link);
					} else
						break;
					// LOGGER.info(">> =" + link.getActivityName() + ":" + link.getUser().getName() +
					// ":" + link.getProcessOrder());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// LOGGER.info("wfItem link.result(): " + result);
		return result;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#getProcessingUser(com.e3ps.groupware.workprocess.WFItem)
	 */
	@Override
	public ArrayList getProcessingUser(WFItem wfItem) {
		WFItemUserLink link = null;
		ArrayList result = new ArrayList();
		if (wfItem != null) {
			Logger.user.println("ObjectState = " + wfItem.getObjectState());
			if ("REWORK".equals(wfItem.getObjectState()) || "RETURN".equals(wfItem.getObjectState())) {
				try {
					result.add((WTUser) wfItem.getOwnership().getOwner().getPrincipal());
					return result;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					QuerySpec query = new QuerySpec();
					query.addClassList(WTUser.class, true);
					int linkIdx = query.addClassList(WFItemUserLink.class, true);
					query.appendWhere(new SearchCondition(WFItemUserLink.class, "state", true),
							new int[] { linkIdx });
					query.appendAnd();
					query.appendWhere(new SearchCondition(WFItemUserLink.class, "disabled",
							SearchCondition.IS_FALSE), new int[] { linkIdx });
					query.appendAnd();
					query.appendWhere(new SearchCondition(WFItemUserLink.class, "processOrder", ">", 0),
							new int[] { linkIdx });
					SearchUtil.setOrderBy(query, WFItemUserLink.class, linkIdx, "processOrder", "asc");
					QueryResult qr = PersistenceHelper.manager.navigate(wfItem, "user", query, false);
					// LOGGER.info("WFItemHelper::getProcessingUser:
					// "+qr.size()+" User(s)");

					int tempOrder = -1;
					while (qr.hasMoreElements()) {
						link = (WFItemUserLink) qr.nextElement();
						if (tempOrder == -1) {
							tempOrder = link.getProcessOrder();
							result.add(link.getUser());
						} else if (tempOrder == link.getProcessOrder()) {
							result.add(link.getUser());
						} else
							break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#setWFItemState(wt.fc.WTObject, java.lang.String)
	 */
	@Override
	public void setWFItemState(WTObject wtobject, String state) {
		E3PSWorkflowHelper.manager.changeLCState((LifeCycleManaged) wtobject, state);

		WFItem wfItem = getWFItem(wtobject);
		try {
			if (wfItem != null) {

				LifeCycleManaged lm = (LifeCycleManaged) wtobject;

				/*
				 * if(wtobject instanceof EChangeOrder2){
				 * if(lm.getLifeCycleState().toString().equals("APPROVED")){ return; } }
				 */

				wfItem.setObjectState(lm.getLifeCycleState().toString());
				PersistenceHelper.manager.modify(wfItem);
				LOGGER.info("[WFItemHelper][WFItem][oid]"
						+ wfItem.getPersistInfo().getObjectIdentifier().toString());
				LOGGER.info("[WFItemHelper][WFItem][state]" + state);
				LOGGER.info("[WFItemHelper][WFItem][lifecycle]" + lm.getLifeCycleState().toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#reSet(wt.fc.WTObject)
	 */
	@Override
	public void reSet(WTObject wtobject) throws Exception {
		WFItem wfItem = getWFItem(wtobject);

		LOGGER.info("[WFItemHelper][WFItemUserLink_reSet] Start");
		QueryResult qr = null;
		QuerySpec query = getLinkQuerySpec(wfItem, "asc");
		qr = PersistenceHelper.manager.navigate(wfItem, "user", query, false);

		LOGGER.info("[WFItemHelper][reSetLink]qr.size=" + qr.size());

		while (qr.hasMoreElements()) {
			WFItemUserLink link = (WFItemUserLink) qr.nextElement();

			// 기안자 정보는 변경하지 않는다.
			// if (link.getProcessOrder() == 0)
			// {
			// LOGGER.info("getRoleA | WTUser 1: " +
			// link.getUser().getPersistInfo().getObjectIdentifier().toString());
			// LOGGER.info("getRoleA | WTUser 2: " +
			// link.getRoleAObject().getPersistInfo().getObjectIdentifier().toString());
			// continue;
			// }

			LOGGER.info("[WFItemHelper][reSetLink] : "
					+ link.getPersistInfo().getObjectIdentifier().toString());
			// link.setComment(null);
			// link.setState(null);
			// link.setProcessDate(null);
			// link.setReceiveDate(null);
			link.setDisabled(false);
			PersistenceHelper.manager.modify(link);
		}
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#setWFItemUserLinkState(wt.workflow.work.WorkItem, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void setWFItemUserLinkState(WorkItem workitem, String activityName, String comment, String state) {
		try {
			activityName = this.getWFItemActivityName(activityName);
			System.out
					.println("===================WFItemHelper::setWFItemUserLinkState START =================");
			wt.fc.Persistable persistable = WorklistHelper.getPBO(workitem);
			WFItem wfItem = getWFItem((WTObject) persistable);
			if (wfItem == null) {
				return;
			}

			WTUser owner = (WTUser) workitem.getOwnership().getOwner().getPrincipal();
			WFItemUserLink link = this.getOwnerApplineLink(owner, wfItem, activityName);
			LOGGER.info("=================== activityName =" + activityName);
			LOGGER.info("=================== link =" + link);
			if (link == null) {
				return;
			} else {
				link.setState(state);
				link.setApprover(((WTUser) SessionHelper.manager.getPrincipal()).getFullName());
				link.setComment(StringUtil.checkNull(comment) + "\n");
				link.setProcessDate(new java.sql.Timestamp(new java.util.Date().getTime()));
				PersistenceHelper.manager.modify(link);
			}

			System.out
					.println("===================WFItemHelper::setWFItemUserLinkState END =================");
		} catch (WTException e) {
			e.printStackTrace();
		} catch (WTPropertyVetoException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#setWFItemUserLinkState(wt.workflow.work.WorkItem, java.lang.String, java.lang.String)
	 */
	@Override
	public void setWFItemUserLinkState(WorkItem workitem, String activityName, String comment) {
		setWFItemUserLinkState(workitem, activityName, comment, "수신");
		System.out
				.println("###################### WItemHelper.setWFItemUserLinkState 사용하지 않음 ################################");
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#setReworkAppLineSet(wt.fc.WTObject)
	 */
	@Override
	public void setReworkAppLineSet(WTObject wtobject) {
		System.out
		.println("###################### WItemHelper.setReworkAppLineSet 사용하지 않음 ################################");
		/*
		 * try{ WFItem wfItem = WFItemHelper.manager.getWFItem(wtobject); QuerySpec query =
		 * getLinkQuerySpec(wfItem, "asc"); QueryResult rt = PersistenceHelper.manager.navigate(wfItem,
		 * "user", query, false); LOGGER.info("===================WFItemHelper::setReworkAppLineSet=" +
		 * rt.size()); int cout = rt.size(); while(rt.hasMoreElements()){
		 * 
		 * WFItemUserLink link = (WFItemUserLink)rt.nextElement(); WFItemUserLink newLink =
		 * WFItemUserLink.newWFItemUserLink(link.getUser(), wfItem);
		 * LOGGER.info("===================link.getActivityName()" + link.getActivityName() +":"
		 * +link.getProcessOrder()); if(link.getActivityName().equals("수신")){ link.setProcessOrder(cout++);
		 * PersistenceHelper.manager.modify(link); }else{
		 * WFItemHelper.manager.newWFItemUserLink(link.getUser(), wfItem, link.getActivityName(), cout++, 1);
		 * }
		 * 
		 * } }catch(Exception e){ e.printStackTrace(); }
		 */
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#newWFItem(wt.fc.WTObject, wt.org.WTPrincipal)
	 */
	@Override
	public WFItem newWFItem(WTObject obj, WTPrincipal owner) {
		if (obj == null) {
			return null;
		}
		WFItem wfItem = null;
		WTObject tempObj = null;
		try {
			wfItem = WFItem.newWFItem();

			if (obj instanceof RevisionControlled) {
				RevisionControlled rc = (RevisionControlled) obj;
				wfItem.setObjectVersion(VersionControlHelper.getVersionIdentifier((Versioned) rc).getValue());
				wfItem.setObjectState(rc.getLifeCycleState().toString());
				tempObj = (WTObject) rc.getMaster();
				wfItem.setWfObject(tempObj);
			} else {
				LifeCycleManaged lc = (LifeCycleManaged) obj;

				wfItem.setObjectVersion("");
				wfItem.setObjectState(lc.getLifeCycleState().toString());
				wfItem.setWfObject(obj);
			}
			wfItem = (WFItem) PersistenceHelper.manager.save(wfItem);
			wfItem = (WFItem) OwnershipHelper.service.assignOwnership(wfItem, owner);
			WFObjectWFItemLink link = null;
			if (obj instanceof RevisionControlled)
				link = WFObjectWFItemLink.newWFObjectWFItemLink(wfItem, tempObj);
			else
				link = WFObjectWFItemLink.newWFObjectWFItemLink(wfItem, obj);
			PersistenceHelper.manager.save(link);
			int seq = getMaxSeq(wfItem);
			newWFItemUserLink((WTUser) owner, wfItem, "기안", 0, seq);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wfItem;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#newWFItemUserLink(wt.org.WTUser, com.e3ps.groupware.workprocess.WFItem, java.lang.String, int)
	 */
	@Override
	public void newWFItemUserLink(WTUser user, WFItem item, String activity, int order) {
		try {
			int seq = getMaxSeq(item);
			newWFItemUserLink(user, item, activity, order, seq);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#newWFItemUserLink(wt.org.WTUser, com.e3ps.groupware.workprocess.WFItem, java.lang.String, int, int)
	 */
	@Override
	public void newWFItemUserLink(WTUser user, WFItem item, String activity, int order, int seq) {
		try {
			LOGGER.info("owner=" + item.getOwnership().getOwner().getFullName());
			WFItemUserLink link = WFItemUserLink.newWFItemUserLink(user, item);

			PeopleData pData = new PeopleData(user);
			link.setDepartmentName(pData.getDepartmentName());
			link.setActivityName(activity);
			link.setProcessOrder(order);

			// history 생성을 위해.
			link.setDisabled(false);
			link.setSeqNo(seq);

			PersistenceHelper.manager.save(link);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#getLinkQueryResult(com.e3ps.groupware.workprocess.WFItem, java.lang.String)
	 */
	@Override
	public QueryResult getLinkQueryResult(WFItem wfItem, String activity) {
		QueryResult qr = null;
		try {
			QuerySpec query = new QuerySpec();
			query.addClassList(WTUser.class, true);
			int linkIdx = query.addClassList(WFItemUserLink.class, true);
			query.appendWhere(new SearchCondition(WFItemUserLink.class, "activityName", "=", activity),
					new int[] { linkIdx });
			query.appendAnd();
			query.appendWhere(
					new SearchCondition(WFItemUserLink.class, "disabled", SearchCondition.IS_FALSE),
					new int[] { linkIdx });
			query.appendAnd();
			query.appendWhere(new SearchCondition(WFItemUserLink.class, "processOrder", ">", 0),
					new int[] { linkIdx });
			SearchUtil.setOrderBy(query, WFItemUserLink.class, linkIdx, "processOrder", "asc");
			qr = PersistenceHelper.manager.navigate(wfItem, "user", query, false);
		} catch (Exception e) {
			e.printStackTrace();
			qr = null;
		}
		return qr;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#getLinkQuerySpec(com.e3ps.groupware.workprocess.WFItem, java.lang.String)
	 */
	@Override
	public QuerySpec getLinkQuerySpec(WFItem wfItem, String order) {
		QuerySpec query = null;
		try {
			query = new QuerySpec();
			query.addClassList(WTUser.class, true);
			int linkIdx = query.addClassList(WFItemUserLink.class, true);

			// query.appendWhere(new SearchCondition(WFItemUserLink.class, "state", false), new int[] {
			// linkIdx });
			// query.appendAnd();
			query.appendWhere(
					new SearchCondition(WFItemUserLink.class, "disabled", SearchCondition.IS_FALSE),
					new int[] { linkIdx });
			// query.appendAnd();
			// query.appendWhere(new SearchCondition(WFItemUserLink.class, "processOrder", ">", 0), new int[]
			// { linkIdx });
			SearchUtil.setOrderBy(query, WFItemUserLink.class, linkIdx, "processOrder", order);
			// qr = PersistenceHelper.manager.navigate(wfItem, "user", query,
			// false);
		} catch (Exception e) {
			e.printStackTrace();
			query = null;
		}
		return query;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#deleteWFItem(wt.fc.Persistable)
	 */
	@Override
	public void deleteWFItem(Persistable persistable) {
		if (persistable instanceof WTObject) {
			WFItem wfitem = getWFItem((WTObject) persistable);
			deleteWFItem(wfitem);
		}
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#deleteWFItem(com.e3ps.groupware.workprocess.WFItem)
	 */
	@Override
	public void deleteWFItem(WFItem wfitem) {
		if (wfitem == null) {
			return;
		}
		try {
			QueryResult qr = PersistenceHelper.manager.navigate(wfitem, "user", WFItemUserLink.class, false);
			while (qr.hasMoreElements()) {
				WFItemUserLink link = (WFItemUserLink) qr.nextElement();
				PersistenceHelper.manager.delete(link);
			}
			PersistenceHelper.manager.delete(wfitem);
		} catch (WTException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#processRemainWork(wt.fc.WTObject)
	 */
	@Override
	public void processRemainWork(WTObject _wtobject) throws WTException {
		if (_wtobject == null)
			return;
		LOGGER.info("processRemainWork = " + _wtobject);
		// E3PSWorkflowHelper.manager.deleteRole((LifeCycleManaged) _wtobject, "APPROVER");

		// if (_wtobject instanceof com.e3ps.part.ChangePartInfo)
		// e3ps.part.E3PSPartHelper.changePartInfo((com.e3ps.part.ChangePartInfo) _wtobject);
		// else
		if (_wtobject instanceof com.e3ps.groupware.workprocess.WorkProcessForm) {
			WorkProcessForm form = (WorkProcessForm) _wtobject;
			WPFormHelper.service.sendMail(form);
		}
		/*
		 * else if (_wtobject instanceof E3PSPBO) { E3PSPBO pbo = (E3PSPBO) _wtobject; try { if
		 * (pbo.getTarget() instanceof E3PSProject) { // 프로젝트 관련 정보 세팅 Logger.user.println("프로젝트 관련 정보 세팅");
		 * 
		 * 
		 * E3PSProject ep = (E3PSProject)pbo.getTarget();
		 * ep.setProjectState(ProjectStateFlag.PROJECT_STATE_CONTINUE); PersistenceHelper.manager.modify(ep);
		 * 
		 * ProjectHelper.manager.setCompleteProject((E3PSProject) pbo.getTarget(), false);
		 * 
		 * } else if (pbo.getTarget() instanceof E3PSTask) { // 태스트 관련 종료 정보 세팅
		 * Logger.user.println("태스트 관련 종료 정보 세팅"); TaskHelper.manager.setCompleteTask((E3PSTask)
		 * pbo.getTarget(), true); } } catch (Exception e) { e.printStackTrace(); } }
		 */
		/*
		 * else if (_wtobject instanceof E3PSDocument) {// 프로젝트 산출물 관련 Config conf = ConfigImpl.getInstance();
		 * TaskHelper.manager.autoCompleteTask((E3PSDocument) _wtobject);
		 * 
		 * if("on".equals(conf.getString("project.sendnoti"))) { Logger.info.println("action = E3PSDocument");
		 * ProjectOutputHelper.manager.sendMail(_wtobject, "approve"); } }
		 */
		/*
		 * else if (_wtobject instanceof DistributeProcess) {// 배포관련 DistributeProcess dwp =
		 * (DistributeProcess) _wtobject; LOGGER.info("배포한다. dwp.getName = " + dwp.getName());
		 * 
		 * try { DistributeDocUtil.distribute(dwp); } catch (Exception e) { e.printStackTrace(); } }
		 */
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#createAsm(java.util.Hashtable)
	 */
	@Override
	public String createAsm(Hashtable hash) {
		if (!RemoteMethodServer.ServerFlag) {
			try {
				Class argTypes[] = { Hashtable.class };
				Object argValues[] = { hash };
				return (String) RemoteMethodServer.getDefault().invoke("createAsm",
						"e3ps.groupware.workprocess.beans.WFItemHelper", null, argTypes, argValues);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		String msg = "성공적으로 생성되었습니다";
		Transaction trx = new Transaction();
		try {
			String[] oids = null;
			try {
				oids = (String[]) hash.get("oid");
			} catch (ClassCastException e) {
				oids = new String[] { (String) hash.get("oid") };
			}

			AsmApproval asm = AsmApproval.newAsmApproval();
			asm.setNumber(WFItemHelper.makeNo());
			asm.setName((String) hash.get("processName"));
			asm.setDescription((String) hash.get("processDesc"));

			FolderHelper.assignLocation((FolderEntry) asm, "/WorkProcess/ASM");
			asm = (AsmApproval) LifeCycleHelper.setLifeCycle(asm,
					LifeCycleHelper.service.getLifeCycleTemplate((String) hash.get("lifecycle")));

			trx.start();
			asm = (AsmApproval) PersistenceHelper.manager.save(asm);

			for (int i = 0; i < oids.length; i++) {
				AppPerLink link = null;
				Persistable persistable = (Persistable) CommonUtil.getObject(oids[i]);
				if (persistable instanceof RevisionControlled)
					link = AppPerLink.newAppPerLink(((RevisionControlled) persistable).getMaster(), asm);
				else
					link = AppPerLink.newAppPerLink(persistable, asm);
				PersistenceHelper.manager.save(link);
			}
			trx.commit();
		} catch (WTException e) {
			trx.rollback();
			msg = e.getLocalizedMessage(Locale.KOREA);
			e.printStackTrace();
		} catch (WTPropertyVetoException e) {
			trx.rollback();
			msg = e.getLocalizedMessage(Locale.KOREA);
			e.printStackTrace();
		}
		return msg;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#backup(wt.fc.WTObject)
	 */
	@Override
	public void backup(WTObject wtobject) {
		Logger.info.println("=====>Backup");
		WFItem wfItem = getWFItem(wtobject);
		LOGGER.info("wfItem = " + wfItem);
		if (wfItem == null)
			return;
		try {
			QuerySpec query = new QuerySpec(WFItemUserLink.class);
			SearchUtil.appendEQUAL(query, WFItemUserLink.class, "roleBObjectRef.key.id", wfItem
					.getPersistInfo().getObjectIdentifier().getId(), 0);
			SearchUtil.setOrderBy(query, WFItemUserLink.class, 0, "processOrder", "asc");

			QueryResult qr = PersistenceHelper.manager.find(query);

			int bSize = qr.size() * 2;

			while (qr.hasMoreElements()) {
				WFItemUserLink link = (WFItemUserLink) qr.nextElement();

				if (link.getProcessOrder() > -1) {
					link.setProcessOrder(link.getProcessOrder() - bSize);
					PersistenceHelper.manager.modify(link);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#reworkDataInit(com.e3ps.groupware.workprocess.WFItem)
	 */
	@Override
	public void reworkDataInit(WFItem wfItem) {
		try {
			WTUser creator = null;
			LOGGER.info("=====   REWORK  ======");

			QueryResult qr = null;
			QuerySpec query = getLinkQuerySpec(wfItem, "asc");
			qr = PersistenceHelper.manager.navigate(wfItem, "user", query, false);
			LOGGER.info("===================WFItemHelper::setWFItemState:qr.size=" + qr.size());
			while (qr.hasMoreElements()) {
				WFItemUserLink link = (WFItemUserLink) qr.nextElement();

				if (link.getProcessOrder() == 0) {
					creator = link.getUser();
					LOGGER.info("REWORK CREATOR FOUND - " + creator.getName());
				} else {
					link.setComment(null);
					link.setState(null);
					link.setProcessDate(null);
					link.setReceiveDate(null);
					PersistenceHelper.manager.modify(link);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#getEnabledRouteMap()
	 */
	@Override
	public TreeMap getEnabledRouteMap() {
		Config conf = ConfigImpl.getInstance();

		TreeMap vMap = new TreeMap();

		TreeMap rMap = getRouteMap();
		Iterator itr = rMap.keySet().iterator();
		while (itr.hasNext()) {
			String key = (String) itr.next();
			if ("false".equals(conf.getString("process.route.enable." + key).trim().toLowerCase())) {
				continue;
			}
			vMap.put(key, (String) rMap.get(key));
		}
		return vMap;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#getRouteMap()
	 */
	@Override
	public TreeMap getRouteMap() {
		Config conf = ConfigImpl.getInstance();
		String[] routeArr = conf.getArray("process.route.type");

		TreeMap treeMap = new TreeMap();
		String route = "";
		for (int i = 0; i < routeArr.length; i++) {
			route = routeArr[i];
			treeMap.put(route, conf.getString("process.route.type." + route));
		}
		return treeMap;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#getActivityType(java.lang.String)
	 */
	@Override
	public String getActivityType(String name) {
		TreeMap map = getRouteMap();
		if (!map.containsValue(name)) {
			return "";
		}

		Iterator itr = map.keySet().iterator();
		while (itr.hasNext()) {
			String key = (String) itr.next();

			String value = (String) map.get(key);
			if (value.equals(name)) {
				return key;
			}
		}

		return "";
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#getActivityName(java.lang.String)
	 */
	@Override
	public String getActivityName(String type) {
		return getRouteMap().get(type) == null ? "" : (String) getRouteMap().get(type);
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#isRecipientLine(wt.fc.WTObject)
	 */
	@Override
	public boolean isRecipientLine(WTObject wtobject) {
		LOGGER.info("## isRemainProcessLink##");
		boolean result = false;

		WFItem wfItem = getWFItem(wtobject);
		if (wfItem != null) {
			try {
				Config conf = ConfigImpl.getInstance();

				QuerySpec query = new QuerySpec();
				query.addClassList(WTUser.class, true);
				int linkIdx = query.addClassList(WFItemUserLink.class, true);
				query.appendWhere(new SearchCondition(WFItemUserLink.class, "state", true),
						new int[] { linkIdx });
				query.appendAnd();
				query.appendWhere(new SearchCondition(WFItemUserLink.class, "disabled",
						SearchCondition.IS_FALSE), new int[] { linkIdx });
				query.appendAnd();
				query.appendWhere(new SearchCondition(WFItemUserLink.class, "processOrder", ">", 0),
						new int[] { linkIdx });
				query.appendAnd();
				query.appendWhere(new SearchCondition(WFItemUserLink.class, "activityName", "=", "수신"),
						new int[] { linkIdx });
				QueryResult qr = PersistenceHelper.manager.navigate(wfItem, "user", query, false);

				LOGGER.info("Receive Line People Size = " + qr.size());

				result = qr.hasMoreElements();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#setRecipientLine(wt.fc.WTObject)
	 */
	@Override
	public void setRecipientLine(WTObject wtobject) throws Exception {
		WFItem wfItem = getWFItem(wtobject);

		try {

			Vector<WFItemUserLink> vec = getAppline(wfItem, false, "", "수신");
			for (int i = 0; i < vec.size(); i++) {
				WFItemUserLink link = vec.get(i);
				LOGGER.info("## setRecipientLine##"
						+ link.getUser().getPersistInfo().getObjectIdentifier().toString());
				E3PSWorkflowHelper.manager.addParticipant((LifeCycleManaged) wtobject, "RECIPIENT", link
						.getUser().getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getLocalizedMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#getWFitemUserLink(java.lang.String)
	 */
	@Override
	public WFItemUserLink getWFitemUserLink(String oid) {
		WFItemUserLink link = null;
		try {
			long longoid = CommonUtil.getOIDLongValue(oid);
			ClassAttribute classattribute = null;
			ClassAttribute classattribute2 = null;
			ClassAttribute classattribute3 = null;
			SearchCondition sc = null;

			QuerySpec qs = new QuerySpec();

			Class cls1 = WFItem.class;
			Class cls2 = WFItemUserLink.class;
			int idx1 = qs.addClassList(cls1, false);
			int idx2 = qs.addClassList(cls2, true);

			classattribute2 = new ClassAttribute(cls1, "thePersistInfo.theObjectIdentifier.id");
			classattribute3 = new ClassAttribute(cls2, "roleBObjectRef.key.id");
			sc = new SearchCondition(classattribute2, "=", classattribute3);
			sc.setFromIndicies(new int[] { idx1, idx2 }, 0);
			sc.setOuterJoin(0);
			qs.appendWhere(sc, new int[] { idx1, idx2 });

			qs.appendAnd();
			qs.appendWhere(new SearchCondition(cls1, "wfObjectReference.key.id", SearchCondition.EQUAL,
					longoid), new int[] { 0 });

			qs.appendOrderBy(new OrderBy(new ClassAttribute(cls2, "thePersistInfo.createStamp"), true),
					new int[] { idx2 });

			LOGGER.info(qs.toString());
			QueryResult rt = PersistenceHelper.manager.find(qs);
			while (rt.hasMoreElements()) {
				Object[] obj = (Object[]) rt.nextElement();
				link = (WFItemUserLink) obj[0];
				return link;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return link;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#getLifeCycleState(java.lang.String)
	 */
	@Override
	public Vector<State> getLifeCycleState(String lifeCycle) {
		Vector<wt.lifecycle.State> stateVec = new Vector<wt.lifecycle.State>();
		try {
			LifeCycleTemplate lct = LifeCycleHelper.service.
					getLifeCycleTemplate(lifeCycle, WCUtil.getWTContainerRef());
			Vector states = LifeCycleHelper.service.getPhaseTemplates(lct);
			PhaseTemplate pt = null;
			wt.lifecycle.State lcState = null;
			for (int i = 0; i < states.size(); i++) {
				pt = (PhaseTemplate) states.get(i);
				lcState = pt.getPhaseState();
				stateVec.add(lcState);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return stateVec;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#getMaxOrderNumber(com.e3ps.groupware.workprocess.WFItem, java.lang.String)
	 */
	@Override
	public int getMaxOrderNumber(WFItem item, String StateYn) {
		if (!wt.method.RemoteMethodServer.ServerFlag) {
			Class argTypes[] = new Class[] { WFItem.class };
			Object args[] = new Object[] { item, StateYn };
			try {
				Object obj = RemoteMethodServer.getDefault().invoke("getMaxOrderNumber",
						WFItemHelper.class.getName(), null, argTypes, args);
				return ((Integer) obj).intValue();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		int maxCount = 0;

		try {

			QuerySpec qs = new QuerySpec();

			int idx = qs.appendClassList(WFItemUserLink.class, false);

			// SQLFunction maxFunction = SQLFunction.newSQLFunction( SQLFunction.MAXIMUM, new ClassAttribute(
			// WFItemUserLink.class, WFItemUserLink.PROCESS_ORDER ));
			// qs.appendSelect( maxFunction, idx, false );
			// qs.setAdvancedQueryEnabled( true );
			// qs.setDescendantQuery(false);

			ClassInfo classinfo = WTIntrospector.getClassInfo(WFItemUserLink.class);
			String task_seqColumnName = DatabaseInfoUtilities.getValidColumnName(classinfo,
					WFItemUserLink.PROCESS_ORDER);
			qs.setAdvancedQueryEnabled(true);
			qs.setDescendantQuery(false);
			qs.addClassList(WFItemUserLink.class, false);
			KeywordExpression ke = new KeywordExpression("NVL(Max(" + task_seqColumnName + "), 0) ");
			ke.setColumnAlias("task_seq");
			qs.appendSelect(ke, new int[] { 0 }, false);

			qs.appendWhere(new SearchCondition(WFItemUserLink.class, "roleBObjectRef.key.id",
					SearchCondition.EQUAL, CommonUtil.getOIDLongValue(item)), new int[] { idx });
			if ("Y".equals(StateYn)) {
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(WFItemUserLink.class, "state", false), new int[] { idx });
			}

			QueryResult rt = PersistenceHelper.manager.find(qs);
			Object obj[] = (Object[]) rt.nextElement();
			maxCount = ((java.math.BigDecimal) obj[0]).intValue();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return maxCount;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#getMaxSeq(com.e3ps.groupware.workprocess.WFItem)
	 */
	@Override
	public int getMaxSeq(WFItem item) throws Exception {
		if (!wt.method.RemoteMethodServer.ServerFlag) {
			Class argTypes[] = new Class[] { WFItem.class };
			Object args[] = new Object[] { item };
			try {
				Object obj = RemoteMethodServer.getDefault().invoke("getMaxSeq",
						WFItemHelper.class.getName(), null, argTypes, args);
				return ((Integer) obj).intValue();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		int result = 0;

		try {

			Class taskClass = WFItemUserLink.class;
			ClassInfo classinfo = WTIntrospector.getClassInfo(taskClass);
			String task_seqColumnName = DatabaseInfoUtilities.getValidColumnName(classinfo,
					WFItemUserLink.SEQ_NO);

			QuerySpec spec = new QuerySpec();
			spec.setAdvancedQueryEnabled(true);
			spec.setDescendantQuery(false);
			spec.addClassList(taskClass, false);
			KeywordExpression ke = new KeywordExpression("NVL(Max(" + task_seqColumnName + "), 0) ");
			ke.setColumnAlias("task_seq");
			spec.appendSelect(ke, new int[] { 0 }, false);

			long id = 0;

			if (item != null) {
				id = item.getPersistInfo().getObjectIdentifier().getId();
			}

			spec.appendWhere(new SearchCondition(taskClass, "roleBObjectRef.key.id", "=", id),
					new int[] { 0 });

			QueryResult qr = PersistenceHelper.manager.find(spec);

			if (qr.hasMoreElements()) {
				Object o[] = (Object[]) qr.nextElement();
				BigDecimal number = (BigDecimal) o[0];
				result = number.intValue();
				LOGGER.info("** BigDecimal result : " + result);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#newOrderWFItemUserLink(com.e3ps.groupware.workprocess.WFItem)
	 */
	@Override
	public void newOrderWFItemUserLink(WFItem item) {
		try {

			QuerySpec query = new QuerySpec();
			int linkIdx = query.addClassList(WFItemUserLink.class, true);

			query.appendWhere(new SearchCondition(WFItemUserLink.class, "state", false),
					new int[] { linkIdx });
			query.appendAnd();
			query.appendWhere(
					new SearchCondition(WFItemUserLink.class, "disabled", SearchCondition.IS_FALSE),
					new int[] { linkIdx });
			query.appendAnd();
			query.appendWhere(new SearchCondition(WFItemUserLink.class, "processOrder", "=", 0),
					new int[] { linkIdx });
			query.appendAnd();
			query.appendWhere(new SearchCondition(WFItemUserLink.class, "roleBObjectRef.key.id", "=", item
					.getPersistInfo().getObjectIdentifier().getId()), new int[] { linkIdx });

			QueryResult qr = PersistenceHelper.manager.find(query);

			while (qr.hasMoreElements()) {
				Object[] o = (Object[]) qr.nextElement();

				WFItemUserLink link = (WFItemUserLink) o[0];

				WFItemUserLink newlink = WFItemUserLink.newWFItemUserLink(link.getUser(), item);
				newlink.setActivityName(link.getActivityName());
				newlink.setDepartmentName(link.getDepartmentName());
				newlink.setProcessOrder(0);
				newlink.setComment(link.getComment());
				newlink.setApprover(newlink.getApprover());
				newlink.setDisabled(false);
				int seq = getMaxSeq(item) + 1;
				newlink.setSeqNo(seq);
				PersistenceHelper.manager.save(newlink);

				link.setDisabled(true);
				PersistenceHelper.manager.modify(link);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#getReOrder(com.e3ps.groupware.workprocess.WFItem)
	 */
	@Override
	public int getReOrder(WFItem wfItem) {
		int porder = 0;
		try {

			QuerySpec query = new QuerySpec();
			int linkIdx = query.addClassList(WFItemUserLink.class, true);

			query.appendWhere(new SearchCondition(WFItemUserLink.class, "roleBObjectRef.key.id",
					SearchCondition.EQUAL, CommonUtil.getOIDLongValue(wfItem)), new int[] { linkIdx });
			query.appendAnd();
			query.appendWhere(new wt.query.SearchCondition(WFItemUserLink.class, "processOrder", ">", 0),
					new int[] { linkIdx });
			query.appendAnd();
			query.appendWhere(new SearchCondition(WFItemUserLink.class, "state", false),
					new int[] { linkIdx });

			LOGGER.info(query.toString());

			QueryResult qr = PersistenceHelper.manager.find(query);
			LOGGER.info("======= >>> " + qr.size());
			porder = qr.size();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return porder;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#newWFItemUserLink(wt.org.WTUser, com.e3ps.groupware.workprocess.WFItem, java.lang.String, java.lang.String)
	 */
	@Override
	public WFItemUserLink newWFItemUserLink(WTUser user, WFItem wfItem, String actName, String actYn) {
		WFItemUserLink newlink = null;

		try {
			if (wfItem != null) {
				newlink = WFItemUserLink.newWFItemUserLink(user, wfItem);

				int porder = getMaxOrderNumber(wfItem, actYn);
				int seqNo = getMaxSeq(wfItem);
				People people = UserHelper.service.getPeople(user);

				newlink.setActivityName(actName);
				if (people.getDepartment() != null) {
					newlink.setDepartmentName(people.getDepartment().getName());
				}
				newlink.setSeqNo(seqNo);
				newlink.setProcessOrder(++porder);
				newlink.setDisabled(true);

				PersistenceHelper.manager.save(newlink);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newlink;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#getLastApprover(wt.fc.WTObject)
	 */
	@Override
	public WTUser getLastApprover(WTObject obj) {
		try {
			Config conf = ConfigImpl.getInstance();
			String activity = conf.getString("process.route.type.1");
			if (obj == null)
				return null;
			WFItem wfItem = WFItemHelper.service.getWFItem(obj);

			if (wfItem == null)
				return null;
			Long wfItemOid = CommonUtil.getOIDLongValue(wfItem);
			QuerySpec qs = new QuerySpec(WFItemUserLink.class);

			SearchCondition sc = new SearchCondition(WFItemUserLink.class, "roleBObjectRef.key.id", "=",
					wfItemOid);
			qs.appendWhere(sc, new int[] { 0 });

			qs.appendAnd();
			sc = new SearchCondition(WFItemUserLink.class, "activityName", "=", activity);
			qs.appendWhere(sc, new int[] { 0 });

			qs.appendOrderBy(new OrderBy(new ClassAttribute(WFItemUserLink.class, "processOrder"), true),
					new int[] { 0 });

			QueryResult rt = PersistenceHelper.manager.find(qs);

			LOGGER.info(":::::: getLastApprover ::::::");
			LOGGER.info(qs.toString());
			while (rt.hasMoreElements()) {
				WFItemUserLink wfLink = (WFItemUserLink) rt.nextElement();

				return wfLink.getUser();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#createApprovalTemplate(java.lang.String, java.lang.String[], java.lang.String[], java.lang.String[], java.lang.String[])
	 */
	@Override
	public void createApprovalTemplate(String title, String[] preDiscussUser, String[] discussUser,
			String[] postDiscussUser, String[] tempUser) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();

			ReferenceFactory rf = new ReferenceFactory();

			ApprovalLineTemplate template = ApprovalLineTemplate.newApprovalLineTemplate();
			template.setTitle(title);

			WTUser user = (WTUser) SessionHelper.manager.getPrincipal();
			template.setOwner(user.getName());

			ArrayList approveList = new ArrayList();

			for (int i = 0; preDiscussUser != null && i < preDiscussUser.length; i++) {
				approveList.add(preDiscussUser[i]);
			}
			template.setPreDiscussList(approveList);
			approveList = new ArrayList();

			for (int i = 0; discussUser != null && i < discussUser.length; i++) {
				approveList.add(discussUser[i]);
			}
			template.setDiscussList(approveList);
			approveList = new ArrayList();

			for (int i = 0; postDiscussUser != null && i < postDiscussUser.length; i++) {
				approveList.add(postDiscussUser[i]);
			}
			template.setPostDiscussList(approveList);
			approveList = new ArrayList();

			for (int i = 0; tempUser != null && i < tempUser.length; i++) {
				approveList.add(tempUser[i]);
			}
			template.setTempList(approveList);
			PersistenceHelper.manager.save(template);

			trx.commit();
			trx = null;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new WTException(ex);
		} finally {
			if (trx != null)
				trx.rollback();
		}
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#deleteApprovalTemplate(java.lang.String)
	 */
	@Override
	public void deleteApprovalTemplate(String oid) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();
			LOGGER.info("========================= " + oid);
			ReferenceFactory rf = new ReferenceFactory();
			ApprovalLineTemplate template = (ApprovalLineTemplate) CommonUtil.getObject(oid);
			PersistenceHelper.manager.delete(template);

			trx.commit();
			trx = null;
		} catch (Exception ex) {
			trx.rollback();
			ex.printStackTrace();
			throw new WTException(ex);
		} finally {
			if (trx != null)
				trx.rollback();
		}
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#createAppLine(java.util.HashMap)
	 */
	@Override
	public boolean createAppLine(HashMap map) throws WTException {
		Transaction trx = new Transaction();
		boolean result = false;
		try {
			trx.start();

			Config conf = ConfigImpl.getInstance();
			String activity = conf.getString("process.route.type.0"); // 기안
			String appLine = (String) map.get("appLine");
			String agrLine = (String) map.get("agrLine");
			String tempLine = (String) map.get("tempLine");
			String workItemOid = (String) map.get("workItemOid");

			WorkItem workItem = (WorkItem) CommonUtil.getObject(workItemOid);
			WTObject obj = (WTObject) workItem.getPrimaryBusinessObject().getObject();
			WFItem wfItem = WFItemHelper.service.getWFItem(obj);

			if (wfItem != null) {

				Vector<WFItemUserLink> vec = getAppline(wfItem, false, "", ""); // 결재라인 사용중인 link
				for (int i = 0; i < vec.size(); i++) {
					WFItemUserLink link = vec.get(i);
					if (link.getActivityName().equals(activity))
						continue;

					PersistenceHelper.manager.delete(link);
				}
			} else {
				wfItem = WFItemHelper.service.newWFItem(obj, workItem.getOwnership().getOwner()
						.getPrincipal());
			}

			int j = WFItemHelper.service.getMaxOrderNumber(wfItem, "") + 1;
			int seq = WFItemHelper.service.getMaxSeq(wfItem) + 1;

			// 합의라인
			if (agrLine.length() > 0) {
				String[] userOid = agrLine.split(",");
				for (int i = 0; i < userOid.length; i++) {
					WTUser wtuser = (WTUser) CommonUtil.getObject(userOid[i]);
					LOGGER.info("[create 합의 Link Line] " + "[" + j + "] :" + userOid[i]);
					WFItemHelper.service.newWFItemUserLink(wtuser, wfItem,
							conf.getString("process.route.type.2"), j++, seq);
				}
			}

			// 결재라인
			if (appLine.length() > 0) {
				String[] userOid2 = appLine.split(",");
				for (int i = 0; i < userOid2.length; i++) {
					WTUser wtuser = (WTUser) CommonUtil.getObject(userOid2[i]);
					LOGGER.info("[create 결재 Link Line] " + "[" + j + "] : " + userOid2[i]);
					WFItemHelper.service.newWFItemUserLink(wtuser, wfItem,
							conf.getString("process.route.type.1"), j++, seq);
				}
			}

			// 수신라인
			if (tempLine.length() > 0) {
				String[] tempOid = tempLine.split(",");
				for (int i = 0; i < tempOid.length; i++) {
					WTUser wtuser = (WTUser) CommonUtil.getObject(tempOid[i]);
					LOGGER.info("[create 수신 Link Line] " + "[" + j + "] :" + tempOid[i]);
					WFItemHelper.service.newWFItemUserLink(wtuser, wfItem,
							conf.getString("process.route.type.4"), j++, seq);
				}
			}
			result = true;
			trx.commit();
			trx = null;
		} catch (Exception e) {
			trx.rollback();
			e.printStackTrace();

		} finally {
			if (trx != null)
				trx.rollback();
		}
		LOGGER.info(":::::::::::: createAppLine START ::::::::::::::");
		return result;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#getAppline(com.e3ps.groupware.workprocess.WFItem, boolean, java.lang.String, java.lang.String)
	 */
	@Override
	public Vector<WFItemUserLink> getAppline(WFItem wfItem, boolean disabled, String state,
			String activityName) throws Exception {
		Vector<WFItemUserLink> vec = new Vector<WFItemUserLink>();
		try {

			QuerySpec query = new QuerySpec();
			int idx = query.addClassList(WTUser.class, true);
			int linkIdx = query.addClassList(WFItemUserLink.class, true);

			if (disabled) {
				query.appendWhere(new SearchCondition(WFItemUserLink.class, WFItemUserLink.DISABLED,
						SearchCondition.IS_TRUE), new int[] { linkIdx });
			} else {
				query.appendWhere(new SearchCondition(WFItemUserLink.class, WFItemUserLink.DISABLED,
						SearchCondition.IS_FALSE), new int[] { linkIdx });
			}

			if (state.length() > 0) {
				query.appendAnd();
				query.appendWhere(new SearchCondition(WFItemUserLink.class, WFItemUserLink.STATE,
						SearchCondition.EQUAL, state), new int[] { linkIdx });
			}

			if (activityName.length() > 0) {
				query.appendAnd();
				query.appendWhere(new SearchCondition(WFItemUserLink.class, WFItemUserLink.ACTIVITY_NAME,
						SearchCondition.EQUAL, activityName), new int[] { linkIdx });
			}

			SearchUtil.setOrderBy(query, WFItemUserLink.class, linkIdx, WFItemUserLink.PROCESS_ORDER, "asc");
			SearchUtil.setOrderBy(query, WFItemUserLink.class, linkIdx, "thePersistInfo.createStamp", "asc");
			QueryResult qr = PersistenceHelper.manager.navigate(wfItem, "user", query, false);
			while (qr.hasMoreElements()) {
				WFItemUserLink link = (WFItemUserLink) qr.nextElement();
				vec.add(link);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}

		return vec;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#getTotalAppline(com.e3ps.groupware.workprocess.WFItem)
	 */
	@Override
	public Vector<WFItemUserLink> getTotalAppline(WFItem wfItem) {
		Vector<WFItemUserLink> vec = new Vector<WFItemUserLink>();
		try {

			QuerySpec query = new QuerySpec();
			int linkIdx = query.addClassList(WFItemUserLink.class, true);
			SearchCondition sc = new SearchCondition(WFItemUserLink.class, "roleBObjectRef.key.id", "=",
					CommonUtil.getOIDLongValue(wfItem));
			query.appendWhere(sc, new int[] { linkIdx });
			SearchUtil.setOrderBy(query, WFItemUserLink.class, linkIdx, WFItemUserLink.PROCESS_ORDER, "asc");
			QueryResult qr = PersistenceHelper.manager.find(query);
			while (qr.hasMoreElements()) {
				Object[] ob = (Object[]) qr.nextElement();
				WFItemUserLink link = (WFItemUserLink) ob[0];
				vec.add(link);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return vec;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#getOwnerApplineLink(wt.org.WTUser, com.e3ps.groupware.workprocess.WFItem, java.lang.String)
	 */
	@Override
	public WFItemUserLink getOwnerApplineLink(WTUser owner, WFItem wfItem, String activityName) {
		try {

			QuerySpec query = new QuerySpec();

			int linkIdx = query.addClassList(WFItemUserLink.class, true);

			query.appendWhere(new SearchCondition(WFItemUserLink.class, "state", true), new int[] { linkIdx });

			SearchCondition sc = new SearchCondition(WFItemUserLink.class, "roleBObjectRef.key.id", "=",
					CommonUtil.getOIDLongValue(wfItem));
			query.appendAnd();
			query.appendWhere(sc, new int[] { linkIdx });

			query.appendAnd();
			query.appendWhere(new SearchCondition(WFItemUserLink.class, "roleAObjectRef.key.id",
					SearchCondition.EQUAL, CommonUtil.getOIDLongValue(owner)), new int[] { linkIdx });

			query.appendAnd();
			query.appendWhere(new SearchCondition(WFItemUserLink.class, WFItemUserLink.DISABLED,
					SearchCondition.IS_FALSE), new int[] { linkIdx });

			query.appendAnd();
			query.appendWhere(new SearchCondition(WFItemUserLink.class, WFItemUserLink.ACTIVITY_NAME,
					SearchCondition.EQUAL, activityName), new int[] { linkIdx });

			SearchUtil.setOrderBy(query, WFItemUserLink.class, linkIdx, WFItemUserLink.PROCESS_ORDER, "asc");

			QueryResult qr = PersistenceHelper.manager.find(query);

			LOGGER.info("WFItemHelper.getOwnerApplineLink = " + query);
			while (qr.hasMoreElements()) {
				Object[] ob = (Object[]) qr.nextElement();
				WFItemUserLink link = (WFItemUserLink) ob[0];
				return link;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#setReworkAppLine(wt.fc.WTObject)
	 */
	@Override
	public void setReworkAppLine(WTObject wtobject) {
		LOGGER.info(":::::::::::: WFItemHelper.setReworkAppLine START ::::::::::::::");
		try {
			Config conf = ConfigImpl.getInstance();
			WFItem wfItem = getWFItem(wtobject);

			int j = WFItemHelper.service.getMaxOrderNumber(wfItem, "") + 1;
			int seq = WFItemHelper.service.getMaxSeq(wfItem) + 1;

			Vector<WFItemUserLink> vec = getAppline(wfItem, false, "", ""); // 결재라인 사용
			for (int i = 0; i < vec.size(); i++) {
				WFItemUserLink link = vec.get(i);

				if ("위임".equals(link.getState())) {
					link.setDisabled(true);
					PersistenceHelper.manager.modify(link);
					continue; // 위임의 결재이력은 제외
				}

				if (link.getActivityName().equals(conf.getString("process.route.type.0"))) { // 기안
					this.newWFItemUserLink(link.getUser(), wfItem, conf.getString("process.route.type.0"),
							j++, seq);
				} else if (link.getActivityName().equals(conf.getString("process.route.type.1"))) { // 결재
					this.newWFItemUserLink(link.getUser(), wfItem, conf.getString("process.route.type.1"),
							j++, seq);
				} else if (link.getActivityName().equals(conf.getString("process.route.type.2"))) { // 합의
					this.newWFItemUserLink(link.getUser(), wfItem, conf.getString("process.route.type.2"),
							j++, seq);
				} else if (link.getActivityName().equals(conf.getString("process.route.type.4"))) { // 수신
					this.newWFItemUserLink(link.getUser(), wfItem, conf.getString("process.route.type.4"),
							j++, seq);
				}
				link.setDisabled(true);
				PersistenceHelper.manager.modify(link);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		LOGGER.info(":::::::::::: WFItemHelper.setReworkAppLine END ::::::::::::::");
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#getWFItemActivityName(java.lang.String)
	 */
	@Override
	public String getWFItemActivityName(String activityName) {
		if (activityName.equals("결재선지정")) {
			activityName = "기안";
		} else if (activityName.equals("결재")) {
			activityName = activityName;
		} else if (activityName.equals("합의")) {
			activityName = activityName;
		} else if (activityName.equals("협조")) {
			activityName = activityName;
		} else if (activityName.equals("반려확인")) {
			activityName = activityName;
		} else if (activityName.equals("재작업")) {
			activityName = "기안";
		} else {
			activityName = activityName;
		}

		return activityName;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.WFItemService#getListWFitemQuery(java.util.HashMap)
	 */
	@Override
	public QuerySpec getListWFitemQuery(HashMap map) {
		QuerySpec query = null;
		try {

			String state = (String) map.get("state");

			query = new QuerySpec();
			int idx = query.addClassList(WFItem.class, true);
			int idx_Link = query.addClassList(WFItemUserLink.class, true);

			SearchCondition sc1 = new SearchCondition(new ClassAttribute(WFItem.class,
					"thePersistInfo.theObjectIdentifier.id"), "=", new ClassAttribute(WFItemUserLink.class,
					"roleBObjectRef.key.id"));
			sc1.setFromIndicies(new int[] { idx, idx_Link }, 0);
			sc1.setOuterJoin(0);
			query.appendWhere(sc1, new int[] { idx, idx_Link });

			long longOid = CommonUtil.getOIDLongValue((WTUser) SessionHelper.manager.getPrincipal());
			if (!CommonUtil.isAdmin()) {
				if (query.getConditionCount() > 0)
					query.appendAnd();
				query.appendWhere(new SearchCondition(WFItemUserLink.class, "roleAObjectRef.key.id", "=",
						longOid), new int[] { idx_Link });
			}
			if (query.getConditionCount() > 0)
				query.appendAnd();
			query.appendWhere(new SearchCondition(WFItem.class, "wfObjectReference.key.classname",
					SearchCondition.NOT_EQUAL, "com.e3ps.change.EChangeActivity"), new int[] { idx });

			if ("ing".equals(state)) {
				if (query.getConditionCount() > 0)
					query.appendAnd();
				query.appendOpenParen();
				query.appendWhere(new SearchCondition(WFItem.class, WFItem.OBJECT_STATE,
						SearchCondition.NOT_EQUAL, "COMPLETED"), new int[] { idx });
				query.appendAnd();
				query.appendWhere(new SearchCondition(WFItem.class, WFItem.OBJECT_STATE,
						SearchCondition.NOT_EQUAL, "APPROVED"), new int[] { idx });
				query.appendAnd();
				query.appendWhere(new SearchCondition(WFItem.class, WFItem.OBJECT_STATE,
						SearchCondition.NOT_EQUAL, "CANCELLED"), new int[] { idx });
				query.appendOpenParen();
			}
			if ("complete".equals(state)) {
				if (query.getConditionCount() > 0)
					query.appendAnd();
				query.appendOpenParen();
				query.appendWhere(new SearchCondition(WFItem.class, WFItem.OBJECT_STATE,
						SearchCondition.EQUAL, "COMPLETED"), new int[] { idx });
				query.appendAnd();
				query.appendWhere(new SearchCondition(WFItem.class, WFItem.OBJECT_STATE,
						SearchCondition.EQUAL, "APPROVED"), new int[] { idx });
				query.appendAnd();
				query.appendWhere(new SearchCondition(WFItem.class, WFItem.OBJECT_STATE,
						SearchCondition.EQUAL, "CANCELLED"), new int[] { idx });
				query.appendOpenParen();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return query;
	}

	/**
	 * RevisionControlled 객체들을 체크아웃 시킨다
	 * 
	 * @param wtobject
	 * @throws WTException
	 * @throws PersistenceException
	 * @throws WTPropertyVetoException
	 * @throws WorkInProgressException
	 * @throws NonLatestCheckoutException
	 */
	private void doRework(WTObject wtobject, WTUser creator) throws NonLatestCheckoutException,
			WorkInProgressException, WTPropertyVetoException, PersistenceException, WTException {
		if (wtobject instanceof Workable && !CheckInOutTaskLogic.isCheckedOut((Workable) wtobject)) {
			LOGGER.info("> Check Out WTObject= " + wtobject);
			SessionHelper.manager.setPrincipal(creator.getName());

			// if (wtobject instanceof com.e3ps.doc.E3PSDocument)
			// DocumentCheckInOutHelper.manager.checkout(wtobject);
			// else WorkInProgressHelper.service.checkout((Workable) wtobject,
			// CheckInOutTaskLogic.getCheckoutFolder(), "");
		}
	}
}
