package com.e3ps.erp.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import com.e3ps.change.EChangeOrder2;
import com.e3ps.common.db.DBConnectionManager;
import com.e3ps.common.iba.IBAUtil;
import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.part.bean.PartData;
import com.e3ps.part.service.PartHelper;
import com.e3ps.project.EProject;
import com.e3ps.project.beans.ProjectData;

import wt.part.WTPart;
import wt.pom.Transaction;
import wt.util.WTContext;

public class ERPDataSender {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.ERP.getName());

	Locale locale = null;
	String[] querys;
	HashMap<String, String> cols = new HashMap<String, String>();
	HashMap<String, String> param = new HashMap<String, String>();
	String erpType = ConfigImpl.getInstance().getString("erp.type");

	public ERPDataSender() {
		this.locale = WTContext.getContext().getLocale();
	}

	/**
	 * PLM Project 최초 등록 및 CheckIn 시 PLM->ERP Interface
	 * 
	 * @author hckim
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> sendProject(EProject project, boolean flagDelete) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Connection con = null;
		try {
			con = dbConnection(erpType);

			resultMap.put("result", "true");
			ProjectData prjData = new ProjectData(project);

			this.cols.clear();
			this.cols.put("PRJ_CD", prjData.getCode());
			this.querys = QueryBuilder.getQueryString("ERP_IF_PMS", this.cols, new String[] { "PRJ_CD" },
					QueryType.SELECT);
			resultMap = ExecuteQuery(con, this.querys);
			if ("false".equals(resultMap.get("result"))) {
				return resultMap;
			}

			// *******************************
			LOGGER.info("PRJ_CD :: " + prjData.getCode());
			LOGGER.info("PRJ_NM :: " + prjData.getName());
			//LOGGER.info("BP_CD :: " + prjData.getCustomer());
			LOGGER.info("PRJTYPE :: " + prjData.getProjectType());
			LOGGER.info("PRJTYPEDIS :: " + prjData.getProjectTypeDisplay());
			//LOGGER.info("PRJ_TYPE :: " + ERPUtil.erpProjectType(prjData.getDevType()));
			LOGGER.info("REQ_DATE :: " + prjData.getPlanStartDate());
			LOGGER.info("DLVY_DATE :: " + prjData.getPlanEndDate());
			LOGGER.info("PJT_PRSN :: " + prjData.getPmName());
			LOGGER.info("PROJECT_STATUS :: " + prjData.getState());
			LOGGER.info("PROJECT_STATUS :: " + prjData.getStateName());
			LOGGER.info("PROJECT_STATUS :: " + prjData.getStateTag());
			LOGGER.info("INSRT_USER_ID :: " + prjData.getCreatorFullName());
			// *******************************

			this.cols.clear();
			this.cols.put("PRJ_CD", prjData.getCode());// 코드
			this.cols.put("PRJ_NM", prjData.getName());// 명
			//this.cols.put("BP_CD", prjData.getCustomer());// 고객사
			//this.cols.put("PRJ_TYPE", ERPUtil.erpProjectType(prjData.getDevType()));// 개발구분
//			this.cols.put("TOTAL_AMT", "0");//총금액
			this.cols.put("REQ_DATE", ERPUtil.erpDate(prjData.getPlanStartDate()));// 시작일
			this.cols.put("DLVY_DATE", ERPUtil.erpDate(prjData.getPlanEndDate()));// 종료일
			this.cols.put("PJT_PRSN", ERPUtil.erpSiteAdminString(prjData.getPmName()));// 담당자
			this.cols.put("PROJECT_STATUS", ERPUtil.erpProjectState(project));// 프로젝트 상태
			this.cols.put("INSRT_USER_ID", ERPUtil.erpSiteAdminString(prjData.getCreatorFullName()));
			this.cols.put("INSRT_DT", ERPUtil.erpSysdate());
			if (flagDelete) {// STATUS(상태)가 'C' 이면 신규, 'U'이면 수정으로 ERP에 반영 Create, Delete, Update
				this.cols.put("STATUS", "D");
			} else {
				String status = (String) resultMap.get("state");
				this.cols.put("STATUS", status);
			}
			this.cols.put("APPLY_FLAG", "N");// DEFAULT = 'N' 적용되면 'Y'

			this.querys = QueryBuilder.getQueryString("ERP_IF_PMS", this.cols, new String[] { "PRJ_CD" },
					QueryType.INSERT_OR_UPDATE);
			resultMap = ExecuteQuery(con, this.querys);

		} catch (Exception e) {
			LOGGER.info(" ==> FAILED.");
			resultMap.put("msg", "ERP Interface Failed : " + e.getLocalizedMessage());
			resultMap.put("result", "false");

			throw e;
		} finally {
			clearStatement(con, null, null);
			LOGGER.info(project.getCode()+" ERP HISTORY 생성");
			ERPUtil.makeErpHistory(project, (String) resultMap.get("msg"));
		}

		return resultMap;
	}

	/**
	 * @desc : ERP - ECO 완료 시 BOM 전송
	 * @author : hckim
	 * @date : 2020. 12. 03.
	 * @method : sendECO
	 * @param : reqMap
	 * @return : Map<String, Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> sendECO(EChangeOrder2 eco, boolean flagDelete) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("result", "true");
		Connection con = null;
		Transaction trx = null;
		try {
			trx = new Transaction();
			trx.start();

			con = dbConnection(erpType);
			con.setAutoCommit(false);

			String oid = CommonUtil.getOIDString(eco);
			Map<String, Object> reqMap = new HashMap<>();
			reqMap.put("oid", oid);

			List<PartData> list = PartHelper.manager.getRelatedPart(reqMap);

			LOGGER.info(":::::SEND ECO Start:::::");
			LOGGER.info("size :: " + list.size());
			for (PartData data : list) {
				WTPart part = PartHelper.manager.getPart(data.getNumber());
				List<Map<String, Object>> bomList = ERPUtil.plmEcoBomData(part);
				
				LOGGER.info(":::::SEND BOM 관련 객체 승인처리 START");
				// 승인처리
				ERPUtil.approveRelatedPart(bomList);

				LOGGER.info("변경되서 삭제할 부품 수집");
				// 변경되어 삭제할 부품
				Map<String, List<String>> plmBomNumbers = new HashMap<String, List<String>>();
				for (Map<String, Object> map : bomList) {
					String parentNumber = String.valueOf(map.get("PRNT_ITEM_CD"));
					String childNumber = String.valueOf(map.get("CHILD_ITEM_CD"));

					List<String> childList = plmBomNumbers.getOrDefault(parentNumber, new ArrayList<String>());
					if (!childList.contains(childNumber)) {
						childList.add(childNumber);
						plmBomNumbers.put(parentNumber, childList);
					}
				}

				LOGGER.info("PLM BOM");
				for (Entry<String, List<String>> entry : plmBomNumbers.entrySet()) {
					LOGGER.info("PLM BOM PARENT :: " + entry.getKey());
					LOGGER.info("PLM BOM CHILD  :: " + entry.getValue());
				}

				StringBuilder queryErpBom = new StringBuilder();
				queryErpBom.append(" WITH TREE_QUERY AS ( ");
				queryErpBom.append(" 	SELECT PRNT_ITEM_CD, CHILD_ITEM_CD ");
				queryErpBom.append(" 	FROM ERP_IF_BOM_PLM ");
				queryErpBom.append(" 	WHERE PRNT_ITEM_CD = '" + part.getNumber() + "' ");
				queryErpBom.append(" 	UNION ALL ");
				queryErpBom.append(" 	SELECT TA.PRNT_ITEM_CD, TA.CHILD_ITEM_CD ");
				queryErpBom.append(" 	FROM ERP_IF_BOM_PLM TA ");
				queryErpBom.append(" 	INNER JOIN TREE_QUERY TB ");
				queryErpBom.append(" 	ON TA.PRNT_ITEM_CD = TB.CHILD_ITEM_CD ");
				queryErpBom.append(" ) ");
				queryErpBom.append(" SELECT PRNT_ITEM_CD, CHILD_ITEM_CD FROM TREE_QUERY ");

				this.cols.clear();
				resultMap = ExecuteQuery(con, queryErpBom.toString(), QueryType.SELECT);
				Map<String, List<String>> erpBomNumbers = (Map<String, List<String>>) resultMap.get("erpBomNumbers");

				LOGGER.info("ERP BOM");
				for (Entry<String, List<String>> entry : erpBomNumbers.entrySet()) {
					LOGGER.info("ERP BOM PARENT :: " + entry.getKey());
					LOGGER.info("ERP BOM CHILD  :: " + entry.getValue());
				}

				for (Entry<String, List<String>> entry : erpBomNumbers.entrySet()) {
					String erpParentNumber = entry.getKey();
					List<String> erpChildNumbers = entry.getValue();

					List<String> plmChildNumbers = plmBomNumbers.get(erpParentNumber);

					for (String erpChildNumber : erpChildNumbers) {

						if (plmChildNumbers == null || !plmChildNumbers.contains(erpChildNumber)) {
							this.cols.clear();
							this.cols.put("PRNT_ITEM_CD", erpParentNumber);// 모품목코드
//    	    			    this.cols.put("PRNT_ITEM_UNIT", String.valueOf(map.get("PRNT_ITEM_UNIT")));//모품목단위
//    	    		  		this.cols.put("CHILD_ITEM_SEQ", String.valueOf(map.get("PRNT_ITEM_UNIT")));//자품목SEQ
							this.cols.put("CHILD_ITEM_CD", erpChildNumber);// 자품목코드
//    	    		 	   	this.cols.put("PRNT_ITEM_QTY", String.valueOf(map.get("PRNT_ITEM_QTY")));//모품목수량
//    	    		  	  	this.cols.put("CHILD_ITEM_QTY", String.valueOf(map.get("CHILD_ITEM_QTY")));//자품목수량
//    	    		    	this.cols.put("CHILD_ITEM_UNIT", String.valueOf(map.get("CHILD_ITEM_UNIT")));//자품목단위

//    	    		    	this.cols.put("VALID_FROM_DT", String.valueOf(map.get("PRNT_ITEM_UNIT")));//시작일
//    	    		    	this.cols.put("VALID_TO_DT", String.valueOf(map.get("PRNT_ITEM_UNIT")));//종료일
//    	    		    	this.cols.put("REMARK", null);//REMARK
//    	    		    	this.cols.put("INSRT_USER_ID", String.valueOf(map.get("PRNT_ITEM_UNIT")));//입력자		
							this.cols.put("INSRT_DT", ERPUtil.erpSysdate());// 입력일
							this.cols.put("STATUS", "D");
							this.cols.put("APPLY_FLAG", "N");// 적용여부
							this.querys = QueryBuilder.getQueryString("ERP_IF_BOM_PLM", this.cols,
									new String[] { "PRNT_ITEM_CD", "CHILD_ITEM_CD" }, QueryType.UPDATE);
							resultMap = ExecuteQuery(con, this.querys);
						}
					}
				}

				// 추가, 변경
				for (Map<String, Object> map : bomList) {

					this.cols.clear();
					this.cols.put("PRNT_ITEM_CD", String.valueOf(map.get("PRNT_ITEM_CD")));
					this.cols.put("CHILD_ITEM_CD", String.valueOf(map.get("CHILD_ITEM_CD")));
					this.querys = QueryBuilder.getQueryString("ERP_IF_BOM_PLM", this.cols,
							new String[] { "PRNT_ITEM_CD", "CHILD_ITEM_CD" }, QueryType.SELECT);
					resultMap = ExecuteQuery(con, this.querys);
					if ("false".equals(resultMap.get("result"))) {
						return resultMap;
					}

					String status = (String) resultMap.get("state");
					this.querys = QueryBuilder.getQueryString("ERP_IF_BOM_PLM", this.cols,
							new String[] { "PRNT_ITEM_CD", "CHILD_ITEM_CD" }, QueryType.SELECT);

					this.cols.clear();
					this.cols.put("PRNT_ITEM_CD", String.valueOf(map.get("PRNT_ITEM_CD")));// 모품목코드
					this.cols.put("PRNT_ITEM_UNIT", String.valueOf(map.get("PRNT_ITEM_UNIT")));// 모품목단위
					this.cols.put("CHILD_ITEM_SEQ", String.valueOf(map.get("CHILD_ITEM_SEQ")));// 자품목SEQ
					this.cols.put("CHILD_ITEM_CD", String.valueOf(map.get("CHILD_ITEM_CD")));// 자품목코드
					this.cols.put("PRNT_ITEM_QTY", String.valueOf(map.get("PRNT_ITEM_QTY")));// 모품목수량
					this.cols.put("CHILD_ITEM_QTY", String.valueOf(map.get("CHILD_ITEM_QTY")));// 자품목수량
					this.cols.put("CHILD_ITEM_UNIT", String.valueOf(map.get("CHILD_ITEM_UNIT")));// 자품목단위

					WTPart childPart = (WTPart) map.get("childPart");
					Map<String, Object> attributes = IBAUtil.getAttributes(childPart);
					String remark = (String) attributes.get("DESCRIPTION");

					this.cols.put("VALID_FROM_DT", ERPUtil.erpDate(new PartData(childPart).getCreateDate()));// 시작일
					this.cols.put("VALID_TO_DT", "2999-12-31");// 종료일
					this.cols.put("REMARK", StringUtil.checkReplaceStr(remark, ""));// REMARK
					this.cols.put("INSRT_USER_ID",
							ERPUtil.erpSiteAdminString(childPart.getIterationInfo().getCreator().getDisplayName()));// 입력자
					this.cols.put("INSRT_DT", ERPUtil.erpSysdate());// 입력일
					if (flagDelete) {// STATUS(상태)가 'C' 이면 신규, 'U'이면 수정으로 ERP에 반영 Create, Delete, Update
						this.cols.put("STATUS", "D");
					} else {
						this.cols.put("STATUS", status);
					}
					this.cols.put("APPLY_FLAG", "N");// 적용여부
					this.querys = QueryBuilder.getQueryString("ERP_IF_BOM_PLM", this.cols,
							new String[] { "PRNT_ITEM_CD", "CHILD_ITEM_CD" }, QueryType.INSERT_OR_UPDATE);
					resultMap = ExecuteQuery(con, this.querys);

				}

			}

			con.commit();
			con.setAutoCommit(true);
			trx.commit();
			trx = null;

		} catch (Exception e) {
			LOGGER.info(" ==> FAILED.");
			resultMap.put("msg", "ERP Interface Failed : " + e.getLocalizedMessage());
			resultMap.put("result", "false");

			if (trx != null) {
				trx.rollback();
				trx = null;
			}
			
			throw e;
		} finally {
			clearStatement(con, null, null);
			LOGGER.info(eco.getOrderNumber()+" ERP HISTORY 생성");
			ERPUtil.makeErpHistory(eco, (String) resultMap.get("msg"));
		}

		return resultMap;
	}

	/**
	 * @desc : ERP - WTPart 승인 시 전송
	 * @author : hckim
	 * @date : 2020. 12. 03.
	 * @method : sendPart
	 * @param : reqMap
	 * @return : Map<String, Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> sendPart(WTPart part, boolean flagDelete) throws Exception {

		Connection con = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			con = dbConnection(erpType);

			resultMap.put("result", "true");
			Map<String, Object> attributes = IBAUtil.getAttributes(part);
			String itemAcct = ERPUtil.erpItemAcct(part.getNumber());

			String spec = (String) attributes.get("SPECIFICATION");
			String remark = (String) attributes.get("DESCRIPTION");

			// ******************************************
			LOGGER.info("ITEM_CD :: " + part.getNumber());
			LOGGER.info("ITEM_NM :: " + part.getName());
			LOGGER.info("SPEC :: " + spec);
			LOGGER.info("BASIC_UNIT :: " + part.getDefaultUnit().toString().toUpperCase());
			LOGGER.info("ITEM_ACCT :: " + itemAcct);
			LOGGER.info("VALID_FROM_DT :: " + part.getPersistInfo().getCreateStamp().toString());
			LOGGER.info("REMARK :: " + remark);
			LOGGER.info("INSRT_USER_ID :: " + part.getIterationInfo().getCreator().getDisplayName());
			LOGGER.info("INSRT_DT :: " + part.getPersistInfo().getCreateStamp().toString());
			// ******************************************

			this.cols.clear();
			this.cols.put("ITEM_CD", part.getNumber());
			this.querys = QueryBuilder.getQueryString("ERP_IF_ITEM_PLM", this.cols, new String[] { "ITEM_CD" },
					QueryType.SELECT);
			resultMap = ExecuteQuery(con, this.querys);

			this.cols.clear();
			this.cols.put("ITEM_CD", part.getNumber());
			this.cols.put("ITEM_NM", part.getName());
			this.cols.put("SPEC", StringUtil.checkReplaceStr(spec, ""));
			this.cols.put("BASIC_UNIT", part.getDefaultUnit().toString().toUpperCase());// length 3
			this.cols.put("ITEM_ACCT", itemAcct);// 품목 계정
			this.cols.put("VALID_FROM_DT", ERPUtil.erpDate(new PartData(part).getCreateDate()));// 시작일
			this.cols.put("VALID_TO_DT", "2999-12-31");
			this.cols.put("REMARK", StringUtil.checkReplaceStr(remark, ""));
			this.cols.put("INSRT_USER_ID",
					ERPUtil.erpSiteAdminString(part.getIterationInfo().getCreator().getDisplayName()));
			this.cols.put("INSRT_DT", ERPUtil.erpSysdate());
			if (flagDelete) {
				this.cols.put("STATUS", "D");
			} else {
				String status = (String) resultMap.get("state");
				this.cols.put("STATUS", status);
			}
			this.cols.put("APPLY_FLAG", "N");

			// this.cols.put("IF_SEQ", part.getNumber().substring(part.getNumber().length()
			// - 3, part.getNumber().length()));
			// this.cols.put("SPEC", spec);
			// this.cols.put("VERSION", part.getVersionIdentifier().getSeries().getValue());
			// this.cols.put("ITEM_STATUS",
			// part.getLifeCycleState().getDisplay(MessageUtil.getLocale()));
			this.querys = QueryBuilder.getQueryString("ERP_IF_ITEM_PLM", this.cols,
					new String[] { "ITEM_CD", "ITEM_NM" }, QueryType.INSERT_OR_UPDATE);
			resultMap = ExecuteQuery(con, this.querys);

			LOGGER.info("ITEM INSERT 완료");

		} catch (Exception e) {
			LOGGER.info(" ==> FAILED.");
			resultMap.put("msg", "ERP Interface Failed : " + e.getLocalizedMessage());
			resultMap.put("result", "false");

			throw e;
		} finally {
			clearStatement(con, null, null);
			LOGGER.info(part.getNumber()+" ERP HISTORY 생성");
			ERPUtil.makeErpHistory(part, (String) resultMap.get("msg"));
		}

		return resultMap;
	}

	/**
	 * @desc : ERP - 쿼리 실행
	 * @author : gs
	 * @date : 2020. 12. 03.
	 * @method : ExecuteQuery
	 * @param : reqMap
	 * @return : Map<String, Object>
	 * @throws Exception
	 */
	private Map<String, Object> ExecuteQuery(Connection con, String[] querys) throws Exception {
		Statement stmt = null;
		ResultSet result = null;
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("result", "true");
		try {

			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			int q = 0;
			do {
				if (querys == null) {
					break;
				}

				LOGGER.info("QUERY : " + querys[q]);
				stmt.execute(querys[q]);
				LOGGER.info(" ==> SUCCEED.");
				resultMap.put("msg", "ERP Interface Succeed");
				if (querys[q].startsWith("SELECT")) {
					result = stmt.getResultSet();
					resultMap.put("selectResult", result);

					if (result.next()) {
						resultMap.put("state", "U");
					} else {
						resultMap.put("state", "C");
					}

					if (querys[q].indexOf("ERP_IF_BOM_PLM") > -1) {
						result.beforeFirst();
						List<String> erpBomNumbers = new ArrayList<String>();

						while (result.next()) {
							// String parentNumber = result.getString("PRNT_ITEM_CD");
							String childNumber = result.getString("CHILD_ITEM_CD");

							erpBomNumbers.add(childNumber);
						}

						resultMap.put("erpBomNumbers", erpBomNumbers);
					}

				}

				q++;

			} while (q < querys.length);

		} catch (Exception e) {
			throw e;
		} finally {
			clearStatement(null, stmt, result);
		}

		return resultMap;
	}

	/**
	 * 재귀 쿼리를 위한 Method
	 * 
	 * @author hckim
	 * @param con
	 * @param query
	 * @param queryType
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> ExecuteQuery(Connection con, String query, Enum<QueryType> queryType) throws Exception {
		Statement stmt = null;
		ResultSet result = null;
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("result", "true");
		try {

			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			LOGGER.info("QUERY : " + query);
			stmt.execute(query);
			LOGGER.info(" ==> SUCCEED.");
			resultMap.put("msg", "ERP Interface Succeed.");

			if (queryType == QueryType.SELECT) {
				result = stmt.getResultSet();
				resultMap.put("selectResult", result);

				if (result.next()) {
					resultMap.put("state", "U");
				} else {
					resultMap.put("state", "C");
				}

				if (query.indexOf("ERP_IF_BOM_PLM") > -1) {
					result.beforeFirst();

					Map<String, List<String>> erpBomNumbers = new HashMap<String, List<String>>();
					while (result.next()) {
						String parentNumber = result.getString("PRNT_ITEM_CD");
						String childNumber = result.getString("CHILD_ITEM_CD");

						List<String> childList = erpBomNumbers.getOrDefault(parentNumber, new ArrayList<String>());
						if (!childList.contains(childNumber)) {
							childList.add(childNumber);
							erpBomNumbers.put(parentNumber, childList);
						}
					}

					resultMap.put("erpBomNumbers", erpBomNumbers);
				}

			}

		} catch (Exception e) {
			throw e;
		} finally {
			clearStatement(null, stmt, result);
		}

		return resultMap;
	}

	private Connection dbConnection(String dbName) throws Exception {
		return DBConnectionManager.dbConnection(dbName);
	}

	private void clearStatement(Connection con, Statement stmt, ResultSet rs) {
		try {
			if (rs != null) {
				if (!rs.isClosed()) {
					rs.close();
				}
				rs = null;
			}
			if (stmt != null) {
				if (!stmt.isClosed()) {
					stmt.close();
				}
				stmt = null;
			}
			if (con != null) {
				if (!con.isClosed()) {
					if (!con.getAutoCommit()) {
						con.rollback();
					}
					con.close();
				}
				con = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
