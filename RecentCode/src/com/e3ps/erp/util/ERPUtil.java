package com.e3ps.erp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.e3ps.approval.service.ApprovalHelper;
import com.e3ps.change.EChangeOrder2;
import com.e3ps.common.history.ERPHistory;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.part.bean.BomTreeData;
import com.e3ps.part.bean.PartData;
import com.e3ps.part.service.BomHelper;
import com.e3ps.project.EProject;
import com.e3ps.project.beans.ProjectData;
import com.e3ps.project.key.ProjectKey.STATEKEY;

import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.org.WTPrincipal;
import wt.part.WTPart;
import wt.session.SessionHelper;

public class ERPUtil {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.ERP.getName());
	
	/**
	 * Part의 품목계정 리턴
	 * 
	 * @author hckim
	 * @param partNumber
	 * @return
	 */
	public static String erpItemAcct(String partNumber) throws Exception {

		String returnItemAcct = "";

		String[] productUpg = new String[] { // 제품
				"35900", "35910", "35912" };
		String[] semiProductUpg = new String[] { // 반제품
				"51" };
		String[] originalMaterialUpg = new String[] { // 원자재
				"21", "22", "23", "25", "26", "45", "46", "47", "48", "52", "53", "35" };
		String[] secondMaterialUpg = new String[] { // 부자재
				"91" };

		if (partNumber != null && !"".equals(partNumber)) {

			for (String ojj : originalMaterialUpg) {
				if (partNumber.startsWith(ojj)) {
					returnItemAcct = "30";
					break;
				}
			}

			for (String jp : productUpg) {
				if (partNumber.startsWith(jp)) {
					returnItemAcct = "10";
					return returnItemAcct;
				}
			}

			for (String bjp : semiProductUpg) {
				if (partNumber.startsWith(bjp)) {
					returnItemAcct = "20";
					return returnItemAcct;
				}
			}

			for (String bjj : secondMaterialUpg) {
				if (partNumber.startsWith(bjj)) {
					returnItemAcct = "35";
					return returnItemAcct;
				}
			}

		}

		if ("".equals(returnItemAcct)) {
			LOGGER.info("유효한 부품번호가 아님 : "+partNumber);
			throw new Exception("ERP Interface Failed : " + partNumber + "은(는) 유효한 부품 번호가 아닙니다.");
		}

		return returnItemAcct;
	}

	/**
	 * Project의 Project Type 리턴
	 * 
	 * @author hckim
	 * @param prjType
	 * @return
	 */
	public static String erpProjectType(String prjType) {
		String returnType = "";
		/*
		 * DT01 선행 DT02 양산 DT03 정부과제 DT04 기술용역 DT00 기타
		 */
		switch (prjType) {
		case "DT01":
			returnType = "1";
			break;
		case "DT02":
			returnType = "2";
			break;
		case "DT03":
			returnType = "3";
			break;
		case "DT04":
			returnType = "4";
			break;
		case "DT00":
			returnType = "9";
			break;
		default:
			returnType = "9";
			break;
		}

		return returnType;
	}

	/**
	 * Project의 Lifecycle State 리턴
	 * 
	 * @author hckim
	 * @param project
	 * @return
	 */
	public static String erpProjectState(EProject project) {

		String returnStateValue = "";
		String state = project.getState().toString();

		switch (state) {
		case STATEKEY.READY:// 준비중
			returnStateValue = "1";
			break;
		case STATEKEY.SIGN:// 결재중
			returnStateValue = "2";
			break;
		case STATEKEY.PROGRESS:// 진행중
			returnStateValue = "3";
			break;
		case STATEKEY.MODIFY:// 수정중
			returnStateValue = "4";
			break;
		case STATEKEY.STOP:// 중단됨
			returnStateValue = "5";
			break;
		case STATEKEY.COMPLETED:// 완료됨
			returnStateValue = "6";
			break;
		case STATEKEY.CANCELLED:// 취소됨
			returnStateValue = "7";
			break;
		}

		return returnStateValue;
	}

	/**
	 * PLM BOM 구조 가져오기
	 * 
	 * @author hckim
	 * @param part
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> plmEcoBomData(WTPart part) throws Exception {

		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("oid", PersistenceHelper.getObjectIdentifier(part).getStringValue());
		reqMap.put("initLevel", "ALL");
		List<BomTreeData> bomDataList = BomHelper.manager.getBomRoot(reqMap);

		Stack<BomTreeData> stackParent = new Stack<BomTreeData>();
		stackParent.push(bomDataList.get(0));

		while (!stackParent.empty()) {

			BomTreeData parentBomData = stackParent.pop();

			if (parentBomData.getChildren() != null && parentBomData.getChildren().size() > 0) {
				int childItemSeq = 1;

				for (BomTreeData childBomData : parentBomData.getChildren()) {
					if (childBomData != null) {

						ERPUtil.erpItemAcct(childBomData.getNumber());

//						Map<String, Object> attributes = IBAUtil.getAttributes(part);
//					    String upg = (String) attributes.get("UPG");

						Map<String, Object> sendData = new HashMap<String, Object>();
						sendData.put("parentPart", (WTPart) CommonUtil.getObject(parentBomData.getOid()));
						sendData.put("childPart", (WTPart) CommonUtil.getObject(childBomData.getOid()));

						sendData.put("PRNT_ITEM_CD", parentBomData.getNumber());
						sendData.put("PRNT_ITEM_UNIT", parentBomData.getUnit().toUpperCase());
						sendData.put("CHILD_ITEM_SEQ", childItemSeq++);
						sendData.put("CHILD_ITEM_CD", childBomData.getNumber());
						sendData.put("PRNT_ITEM_QTY", 1);
						sendData.put("CHILD_ITEM_QTY", childBomData.getQuantity());
						sendData.put("CHILD_ITEM_UNIT", childBomData.getUnit().toUpperCase());

						returnList.add(sendData);
						stackParent.push(childBomData);
					}
				}
			}

		} // while

		return returnList;
	}

	/**
	 * PLM ECO 관련 Part Bom 일괄 승인
	 * 
	 * @author hckim
	 * @param bomList
	 * @throws Exception
	 */
	public static void approveRelatedPart(List<Map<String, Object>> bomList) throws Exception {

		if (bomList.size() > 0) {

			List<WTPart> approveTargetParts = new ArrayList<WTPart>();

			for (Map<String, Object> bom : bomList) {
				WTPart parent = (WTPart) bom.get("parentPart");
				WTPart child = (WTPart) bom.get("childPart");

				if (!approveTargetParts.contains(parent)) {
					approveTargetParts.add(parent);
				}
				if (!approveTargetParts.contains(child)) {
					approveTargetParts.add(child);
				}
			}

			// 관련 객체 승인처리
			for (WTPart part : approveTargetParts) {
				ERPUtil.erpItemAcct(part.getNumber());
				LOGGER.debug("다음 부품 승인 시도 : "+part.getNumber());
				ApprovalHelper.service.changeStatePart(part, "APPROVED");
			}

		} // end if

	}

	/**
	 * Site, Administrator의 ERP DB length 인한 String 자르기
	 * 
	 * @author hckim
	 */
	public static String erpSiteAdminString(String name) {
		String returnName = StringUtil.checkNull(name);
		if(returnName != "") {
			if (returnName.indexOf("Site, Administrator") > -1) {
				returnName = "Administrator";
			}
		}
		

		return returnName;
	}

	/**
	 * sysdate 넣기위한 method
	 * 
	 * @author hckim
	 */
	public static String erpSysdate() throws Exception {
		String returnDate = "";
		try {
			returnDate = DateUtil.getToDayTimestamp().toString();
			returnDate = returnDate.substring(0, returnDate.lastIndexOf("."));

		} catch (Exception e) {
			throw e;
		}
		return returnDate;
	}

	/**
	 * date 형식 method
	 * 
	 * @author hckim
	 */
	public static String erpDate(String time) {
		String returnTime = time.replace("/", "-");
		if (returnTime.indexOf(".") > -1) {
			returnTime = returnTime.substring(0, returnTime.lastIndexOf("."));
		}
		return returnTime;
	}

	public static void makeErpHistory(Persistable per, String msg) throws Exception {

		ERPHistory history = new ERPHistory();
		String sendType = "";
		StringBuilder sendMsg = new StringBuilder();
		String sendOid = "";
		String sendState = "";

		WTPrincipal currentUser = SessionHelper.manager.getPrincipal();
		String curUserName = currentUser.getName();

		if (per instanceof EChangeOrder2) {
			EChangeOrder2 eco = (EChangeOrder2) per;
			sendType = "BOM";
			sendMsg.append(eco.getOrderNumber()).append(" ").append(msg);
			sendOid = CommonUtil.getOIDString(eco);
			sendState = eco.getLifeCycleState().getDisplay(MessageUtil.getLocale());

		} else if (per instanceof WTPart) {
			WTPart part = (WTPart) per;
			PartData data = new PartData(part);
			sendType = "ITEM";
			sendMsg.append(data.getNumber()).append(" ").append(msg);
			sendOid = data.getOid();
			sendState = data.getStateName() + ", " + data.getRevision();

		} else if (per instanceof EProject) {
			EProject project = (EProject) per;
			ProjectData data = new ProjectData(project);
			sendType = "PMS";
			sendMsg.append(data.getCode()).append(" ").append(msg);
			sendOid = data.getOid();
			sendState = data.getStateName() + ", " + data.getVersion();
		}

		history.setMsg(sendMsg.toString());
		history.setOid(sendOid);
		history.setSender(curUserName);
		history.setState(sendState);
		history.setSendType(sendType);
		PersistenceHelper.manager.save(history);
	}

}
