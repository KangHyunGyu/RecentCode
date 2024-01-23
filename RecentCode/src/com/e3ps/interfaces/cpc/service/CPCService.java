package com.e3ps.interfaces.cpc.service;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.e3ps.common.db.DBConnectionManager;
import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.FTPUtil;
import com.e3ps.distribute.DistributeDocument;
import com.e3ps.distribute.DistributeRegPartToEpmLink;
import com.e3ps.distribute.DistributeRegToPartLink;
import com.e3ps.distribute.DistributeRegistration;
import com.e3ps.distribute.bean.DistributeData;
import com.e3ps.distribute.bean.DistributeRegPartToEpmData;
import com.e3ps.distribute.bean.DistributeRegToPartData;
import com.e3ps.distribute.bean.DistributeRegistrationData;
import com.e3ps.distribute.service.DistributeHelper;
import com.e3ps.interfaces.util.InterfaceUtil;

import wt.content.ApplicationData;

public class CPCService {
	
	public static final CPCService service = new CPCService();
	
	private final String RECEPTION_TABLE = "RECEPTION";
	
	private final String RECEPTIONPART_TABLE = "RECEPTIONPART";
	
	private final String RECEPTIONEPM_TABLE = "RECEPTIONEPM";
	
	private final String ATTACHFILE_TABLE = "ATTACHFILE";
	
	private final String CPC_ATTACH_LOCATION = "/PLM";
	
	private final boolean enableCPC = ConfigImpl.getInstance().getBoolean("cpc.send.enable", true);
	
	
	/**
	 * @desc : 구매 요청 인터페이스 INSERT
	 * @author : shjeong
	 * @date : 2023. 04. 24.
	 * @method : insertDistribute
	 * @param PurchaseRequest
	 * @throws Exception void
	 */
	public void insertDistribute(DistributeDocument distribute , DistributeRegistration distributeReg) throws Exception {
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String queryResult = "";
		
		try {
			
			if(!enableCPC) {
				return;
			}
			//DB 커넥션
			conn = InterfaceUtil.getCPCConnection();
			conn.setAutoCommit(false);
			//생성
			StringBuffer sql = new StringBuffer();
			
//			sql.append("INSERT INTO " + RECEPTION_TABLE);
//			sql.append(" ( DIST_NO, DIST_NAME, DIST_TYPE, DIST_CLASSIFICATION," );
//			sql.append(" DISTRIBUTOR, COMPANY_ID, COMPANY_NAME, REQUESTER_NAME, DOWNLOAD_DEADLINE, REMARKS, ISPURCHASE, ORDER_TYPE )");
//			sql.append(" VALUES ( ");
//			sql.append(" ?, ?, ?, ?, ?, ?, " );
//			sql.append(" ?, ?, ?, ?, ?, ? " );
//			sql.append(" )");
			
			/* 
			 * CPC SERVER DB INSERT TABLE RECEPTION
			 * 
			 * INSERT INTO public.reception (
			 * DIST_NO, DIST_NAME, DOWNLOAD_DEADLINE, DOWNLOAD_COUNT,
			 * C_DATE, DIST_PURPOSE, DIST_MARKINGCONFIRM, DIST_FILETYPE,
			 * DIST_DATE, DIST_WITHDRAW, DIST_DESCRIPTION, DIST_DESCRIPTIONDRF,
			 * REQUESTER_NAME, DIST_CLASSIFICATION, COMPANY_ID, COMPANY_NAME)
			 * 
			 * VALUES(
			 * '', '', '', 0, 
			 * CURRENT_TIMESTAMP, '', '', '', 
			 * '', '', '', '', 
			 * '', '', '', '');
			 */
			
			sql.append("INSERT INTO " + RECEPTION_TABLE);
			sql.append(" ( DIST_NO, DIST_NAME, DOWNLOAD_COUNT, C_DATE," );
			sql.append(" DIST_PURPOSE, DIST_MARKINGCONFIRM, DIST_FILETYPE, DIST_DATE," );
			sql.append(" DIST_WITHDRAW, DIST_DESCRIPTION, DIST_DESCRIPTIONDRF, REQUESTER_NAME," );
			sql.append(" DIST_CLASSIFICATION, COMPANY_ID, COMPANY_NAME, DOWNLOAD_DEADLINE, C_DATESTR," );
			sql.append(" DISTRIBUTETARGET )");
			
			sql.append(" VALUES ( ");
			sql.append(" ?, ?, ?, CURRENT_TIMESTAMP, " );
			sql.append(" ?, ?, ?, ?, " );
			sql.append(" ?, ?, ?, ?, " );
			sql.append(" ?, ?, ?, ?, ?, " );
			sql.append(" ? )");
			
			queryResult = sql.toString();
			
			//쿼리 준비
			ps = conn.prepareStatement(queryResult);
			
			DistributeData dist = new DistributeData(distribute);
			DistributeRegistrationData distReg = new DistributeRegistrationData(distributeReg);
			
			//DATA SET
			int idx = 1;
			
			//distNumber -> DIST_NO
			ps.setString(idx++, dist.getDistNumber());
			queryResult = queryResult.replaceFirst( "\\?", "'"+dist.getDistNumber()+"'" );
			//distName -> DIST_NAME
			ps.setString(idx++, dist.getDistName());
			queryResult = queryResult.replaceFirst( "\\?", "'"+dist.getDistName()+"'" );
			//downloadCount -> DOWNLOAD_COUNT
			ps.setInt(idx++, Integer.parseInt(dist.getDownloadCount()));
			
			//purpose -> DIST_PURPOSE
			ps.setString(idx++, dist.getPurpose());
			queryResult = queryResult.replaceFirst( "\\?", "'"+dist.getPurpose()+"'" );
			//markingConfirm -> DIST_MARKINGCONFIRM
			ps.setString(idx++, dist.getMarkingConfirm());
			queryResult = queryResult.replaceFirst( "\\?", "'"+dist.getMarkingConfirm()+"'" );
			//fileType -> DIST_FILETYPE
			ps.setString(idx++, dist.getFileType());
			queryResult = queryResult.replaceFirst( "\\?", "'"+dist.getFileType()+"'" );
			//distDate -> DIST_DATE
			ps.setString(idx++, dist.getDistDate());
			queryResult = queryResult.replaceFirst( "\\?", "'"+dist.getDistDate()+"'" );
			
			//withdraw -> DIST_WITHDRAW
			ps.setString(idx++, dist.getWithdraw());
			queryResult = queryResult.replaceFirst( "\\?", "'"+dist.getWithdraw()+"'" );
			//description -> DIST_DESCRIPTION
			ps.setString(idx++, dist.getDescription());
			queryResult = queryResult.replaceFirst( "\\?", "'"+dist.getDescription()+"'" );
			//descriptionDRF -> DIST_DESCRIPTIONDRF
			ps.setString(idx++, distReg.getDescriptionDRF());
			queryResult = queryResult.replaceFirst( "\\?", "'"+distReg.getDescriptionDRF()+"'" );
			//creator -> REQUESTER_NAME
			ps.setString(idx++, dist.getCreator());
			queryResult = queryResult.replaceFirst( "\\?", "'"+dist.getCreator()+"'" );
			
			//creator -> DIST_CLASSIFICATION
			ps.setString(idx++, "");
			queryResult = queryResult.replaceFirst( "\\?", "''" );
			//distributeCompany -> COMPANY_ID
			ps.setString(idx++, distReg.getDistributeCompany());
			queryResult = queryResult.replaceFirst( "\\?", "'"+distReg.getDistributeCompany()+"'" );
			//COMPANY_NAME -> COMPANY_NAME
			ps.setString(idx++, "(주)월덱스");
			queryResult = queryResult.replaceFirst( "\\?", "'(주)월덱스'" );
			//downloadDate -> DOWNLOAD_DEADLINE
			ps.setString(idx++, dist.getDownloadDate());
			queryResult = queryResult.replaceFirst( "\\?", "'"+dist.getDownloadDate()+"'" );
			//todayString -> C_DATESTR
			ps.setString(idx++, DateUtil.getCurrentDateString("d"));
			queryResult = queryResult.replaceFirst( "\\?", "'"+DateUtil.getCurrentDateString("d")+"'" );
			
			//distributeTarget -> DISTRIBUTETARGET
			ps.setString(idx++, distReg.getDistributeTarget());
			queryResult = queryResult.replaceFirst( "\\?", "'"+distReg.getDistributeTarget()+"'" );
			
			//purchaseParts -> PARTLIST
//			ps.setString(idx++, dist.getCreator());
//			queryResult = queryResult.replaceFirst( "\\?", "'"+dist.getCreator()+"'" );
			
//			System.out.println("DIST_NO : " + dist.getDistNumber());
//			System.out.println("DIST_NAME : " + dist.getDistName());
//			System.out.println("DOWNLOAD_COUNT : " + Integer.parseInt(dist.getDownloadCount()));
//			System.out.println("C_date : ");
//			System.out.println("DIST_PURPOSE : " + dist.getPurpose());
//			System.out.println("DIST_MARKINGCONFIRM : " + dist.getMarkingConfirm());
//			System.out.println("DIST_FILETYPE : " + dist.getFileType());
//			System.out.println("DIST_DATE : " + dist.getDistDate());
//			System.out.println("DIST_WITHDRAW : " + dist.getWithdraw());
//			System.out.println("DIST_DESCRIPTION : " + dist.getDescription());
//			System.out.println("DIST_DESCRIPTIONDRF : " + dist.getDescriptionDRF());
//			System.out.println("REQUESTER_NAME : " + dist.getCreator());
//			System.out.println("DIST_CLASSIFICATION : ");
//			System.out.println("COMPANY_ID : ");
//			System.out.println("COMPANY_NAME : ");
//			System.out.println("DOWNLOAD_DEADLINE : " + dist.getDownloadDate());
//			System.out.println("C_DATESTR : " + DateUtil.getCurrentDateString("d"));
			
			
			
			
//-----------------------------------------------------------------------------------------------------
//			//DIST_NO
//			ps.setString(idx++, dist.getDistNumber());
//			queryResult = queryResult.replaceFirst( "\\?", "'"+dist.getDistNumber()+"'" );
//			
//			//DIST_NAME
//			ps.setString(idx++, dist.getDistName());
//			queryResult = queryResult.replaceFirst( "\\?", "'"+dist.getDistName()+"'" );
			
			//DIST_TYPE
//			ps.setString(idx++, dist.getDistType());
//			queryResult = queryResult.replaceFirst( "\\?", "'"+dist.getDistType()+"'" );
			
			//DIST_CLASSIFICATION
			//ps.setString(idx++, dist.getDistClassification());
			//queryResult = queryResult.replaceFirst( "\\?", "'"+dist.getDistClassification()+"'" );
			
//			//DISTRIBUTOR
//			ps.setString(idx++, dist.getCreator());
//			queryResult = queryResult.replaceFirst( "\\?", "'"+dist.getCreator()+"'" );
//			
//			//COMPANY_ID
//			//ps.setString(idx++, dist.getCompany());
//			//queryResult = queryResult.replaceFirst( "\\?", "'"+dist.getCompany()+"'" );
//			
//			//COMPANY_NAME
//			//ps.setString(idx++, dist.getCompanyName());
//			//queryResult = queryResult.replaceFirst( "\\?", "'"+dist.getCompanyName()+"'" );
//			
//			//REQUESTER_NAME
//			ps.setString(idx++, dist.getCreator());
//			queryResult = queryResult.replaceFirst( "\\?", "'"+dist.getCreator()+"'" );
//			
//			//DOWNLOAD_DEADLINE
//			//ps.setTimestamp(idx++, distribute.getRequestDate());
//			//queryResult = queryResult.replaceFirst( "\\?", "'"+dist.getDownloadDate()+"'" );
//			
//			//REMARKS
//			//ps.setString(idx++, pd.getDescription());
//			//queryResult = queryResult.replaceFirst( "\\?", "'"+dist.getDescription()+"'" );
//			
//			//ISPURCHASE
//			ps.setString(idx++, "N");
//			queryResult = queryResult.replaceFirst( "\\?", "'N'" );
//			
//			//ORDER_TYPE
//			ps.setString(idx++, "E");
//			queryResult = queryResult.replaceFirst( "\\?", "'E'" );
			
			
//			System.out.println("DISTRIBUTE DMS INTERFACE :: " + distribute.getDistributeNumber());
			System.out.println("<<------ "+"" + "INSERT QUERY ------>>");
//			System.out.println(queryResult);
			
			//실행
			ps.executeUpdate();
			
			conn.commit();
			
		}catch(Exception e) {
			e.printStackTrace();
			conn.rollback();
		}finally {
			DBConnectionManager.closeDB(conn, ps, rs);
		}
	}
	
	/**
	 * @desc : 배포 요청 품목 인터페이스 INSERT
	 * @author : shjeong - hgkang
	 * @date : 2023. 04. 25.
	 * @method : insertDistributePart
	 * @param PurchaseRequestPartLink
	 * @throws Exception void
	 */
	public void insertDistributePart(DistributeRegistration distributeReg) throws Exception {
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String queryResult = "";
		
		List<DistributeRegToPartLink> partList = DistributeHelper.manager.getDistributeRegToParts_By_DistributeRegistration(distributeReg);
		
//		System.out.println("DistributeRegPartToEpmLink _ partList ::: " + partList);
		//com.e3ps.distribute.DistributeRegToPartLink:4880408
		
		try {
			
			if(!enableCPC) {
				return;
			}
			
			//DB 커넥션
			conn = InterfaceUtil.getCPCConnection();
			conn.setAutoCommit(false);
			//생성
			StringBuffer sql = new StringBuffer();
			
//			INSERT INTO public.receptionpart
//			(DIST_NO, PART_NO, PART_NAME, PART_VERSION, C_DATE, M_DATE, EPM_NO, DOWNLOAD_DEADLINE, PART_STATE, DISTDOC_USER, DISTDOC_DEPT_NAME)
//			VALUES('', '', '', '', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '', '', '', '', '');

			
			sql.append("INSERT INTO " + RECEPTIONPART_TABLE);
			sql.append("( DIST_NO, PART_NO, PART_NAME, PART_VERSION," );
			sql.append(" C_DATE, M_DATE, EPM_NO, DOWNLOAD_DEADLINE," );
			sql.append(" PART_STATE, DISTDOC_USER, DISTDOC_DEPT_NAME )" );
			sql.append(" VALUES ( ");
			sql.append(" ?, ?, ?, ?, " );
			sql.append(" CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ?, ?, " );
			sql.append(" ?, ?, ?  " );
			sql.append(" ) ");
			
			
			//쿼리 준비
			ps = conn.prepareStatement(sql.toString());
			
//			System.out.println("<<------ "+"" + "partList INSERT QUERY ------>>");
			
			
			for(DistributeRegToPartLink link : partList) {
				
				queryResult = sql.toString();
				
				DistributeRegToPartData data = new DistributeRegToPartData(link); 
				DistributeData dist = new DistributeData(distributeReg.getDistribute());
				
				int idx = 1;
				
				//DIST_NO
				ps.setString(idx++, dist.getDistNumber());
				queryResult = queryResult.replaceFirst( "\\?", "'"+distributeReg.getDistribute().getDistributeNumber()+"'" );
				//PART_NO
				ps.setString(idx++, data.getDistPartNumber());
				queryResult = queryResult.replaceFirst( "\\?", "'"+data.getDistPartNumber()+"'" );
				//PART_NAME
				ps.setString(idx++, data.getDistPartName());
				queryResult = queryResult.replaceFirst( "\\?", "'"+data.getDistPartName()+"'" );
				//PART_VERSION
				ps.setString(idx++, data.getVersion());
				queryResult = queryResult.replaceFirst( "\\?", "'"+data.getVersion()+"'" );

				//C_DATE , M_DATE
				//EPM_NO
				ps.setString(idx++, data.getEpm_no());
				queryResult = queryResult.replaceFirst( "\\?", "'"+data.getEpm_no()+"'" );
				//DOWNLOAD_DEADLINE
				ps.setString(idx++, dist.getDownloadDate());
				queryResult = queryResult.replaceFirst( "\\?", "'"+dist.getDownloadDate()+"'" );
				
				//PART_STATE
				ps.setString(idx++, data.getStateName());
				queryResult = queryResult.replaceFirst( "\\?", "'"+data.getStateName()+"'" );
				//DISTDOC_USER
				ps.setString(idx++, dist.getCreator());
				queryResult = queryResult.replaceFirst( "\\?", "'"+dist.getCreator()+"'" );
				//DISTDOC_DEPT_NAME
				ps.setString(idx++, dist.getCreator_deft());
				queryResult = queryResult.replaceFirst( "\\?", "'"+dist.getCreator_deft()+"'" );
				
//				System.out.println("DIST_NO :: " + dist.getDistNumber());
//				System.out.println("PART_NO :: " + data.getDistPartNumber());
//				System.out.println("PART_NAME ::" + data.getDistPartName());
//				System.out.println("PART_VERSION :: " + data.getVersion());
//				System.out.println("EPM_NO :: " + data.getEpm_no());
//				System.out.println("DOWNLOAD_DEADLINE :: " + dist.getDownloadDate());
//				System.out.println("PART_STATE :: " + data.getStateName());
//				System.out.println("DISTDOC_USER :: " + dist.getCreator());
//				System.out.println("DISTDOC_DEPT_NAME :: " + dist.getCreator_deft());
//				
//				System.out.println("<<------ "+"" + "partList INSERT QUERY///////////////////// ------>>");
				
				//Batch 담기
				ps.addBatch();
	 
	            //파라미터 Clear
				ps.clearParameters();
			}
			
			//배치 실행
			ps.executeBatch();
			
			//배치 Clear
			ps.clearBatch(); 
			
			conn.commit();
			
		}catch(Exception e) {
			e.printStackTrace();
			conn.rollback();
		}finally {
			DBConnectionManager.closeDB(conn, ps, rs);
		}
	}
//	
//	/**
//	 * @desc : 배포 요청 품목 도면 인터페이스 INSERT
//	 * @author : shjeong
//	 * @date : 2023. 04. 25.
//	 * @method : insertDistributeEpm
//	 * @param PurchaseRequestPartLink
//	 * @throws Exception void
//	 */
//	public void insertDistributeEpm(PurchaseRequest purchase) throws Exception {
//		
//		Connection conn = null;
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		String queryResult = "";
//		
//		List<PurchasePartDistributeData> epmList = PurchaseHelper.manager.getPurchasePartDistributeDataList(purchase.getPersistInfo().getObjectIdentifier().getStringValue());
//		
//		try {
//			
//			if(!enableDMS) {
//				return;
//			}
//			
//			//DB 커넥션
//			conn = InterfaceUtil.getDMSConnection();
//			conn.setAutoCommit(false);
//			//생성
//			StringBuffer sql = new StringBuffer();
//			
//			sql.append("INSERT INTO " + RECEPTIONEPM_TABLE);
//			sql.append("( DIST_NO, EPM_NO, EPM_NAME, EPM_VERSION," );
//			sql.append(" DOWNLOAD_DEADLINE )" );
//			sql.append(" VALUES ( ");
//			sql.append(" ?, ?, ?, " );
//			sql.append(" ?, ?  " );
//			sql.append(" ) ");
//			
//			
//			//쿼리 준비
//			ps = conn.prepareStatement(sql.toString());
//			
//			System.out.println("<<------ "+"" + "INSERT QUERY ------>>");
//			
//			
//			for(PurchasePartDistributeData link : epmList) {
//				
//				queryResult = sql.toString();
//				
//				
//				int idx = 1;
//				
//				//DIST_NO
//				ps.setString(idx++, purchase.getPurchaseNumber());
//				queryResult = queryResult.replaceFirst( "\\?", "'"+purchase.getPurchaseNumber()+"'" );
//				
//				//EPM_NO
//				ps.setString(idx++, link.getDisNumber());
//				queryResult = queryResult.replaceFirst( "\\?", "'"+link.getDisNumber()+"'" );
//				
//				//EPM_NAME
//				ps.setString(idx++, link.getDisName());
//				queryResult = queryResult.replaceFirst( "\\?", "'"+link.getDisName()+"'" );
//				
//				//EPM_VERSION
//				ps.setString(idx++, link.getDisVersion());
//				queryResult = queryResult.replaceFirst( "\\?", "'"+link.getDisVersion()+"'" );
//				
//				//DOWNLOAD_DEADLINE
//				ps.setTimestamp(idx++, purchase.getRequestDate());
//				queryResult = queryResult.replaceFirst( "\\?", "'"+purchase.getRequestDate()+"'" );
//				
//				
//				System.out.println(queryResult);
//				
//				//Batch 담기
//				ps.addBatch();
//	 
//	            //파라미터 Clear
//				ps.clearParameters();
//			}
//			
//			//배치 실행
//			ps.executeBatch();
//			
//			//배치 Clear
//			ps.clearBatch(); 
//			
//			conn.commit();
//			
//		}catch(Exception e) {
//			e.printStackTrace();
//			conn.rollback();
//		}finally {
//			DBConnectionManager.closeDB(conn, ps, rs);
//		}
//	}
//	
	/**
	 * @desc : 배포 요청 첨부파일 인터페이스 INSERT
	 * @author : shjeong - hgkang
	 * @date : 2023. 04. 25.
	 * @method : insertDistributeAttachFile
	 * @param DistributeRegToPartLink
	 * @throws Exception void
	 */
	public List<Map<String,Object>> insertDistributeAttachFile(DistributeRegToPartLink distributeRegPart) throws Exception {
		
		List<Map<String,Object>> ftpFileList = new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String queryResult = "";
		
		String distNumber = distributeRegPart.getDistributeReg().getDistribute().getDistributeNumber();
		
		DistributeRegistration oid = (DistributeRegistration) DistributeHelper.manager.getDistributeRegistrationToPartLink(distributeRegPart);
		
		String path = CPC_ATTACH_LOCATION + File.separator + distNumber;
		List<DistributeRegPartToEpmData> distributeDataList = DistributeHelper.manager.getPartDistributeRegDataList(oid);
		
		
		try {
			
			if(!enableCPC) {
				return ftpFileList;
			}
			
			//DB 커넥션
			conn = InterfaceUtil.getCPCConnection();
			conn.setAutoCommit(false);
			//생성
			StringBuffer sql = new StringBuffer();
			
			sql.append("INSERT INTO " + ATTACHFILE_TABLE);
			sql.append("( MAIN_OBJ_NO, SUB_OBJ_NO, FILE_NAME, LOCATION, SIZE, C_DATE, EPM_NO, FILE_VERSION, SUB_OBJ_NAME ) " );
			sql.append(" VALUES ( ");
			sql.append(" ?, ?, ?, ?," );
			sql.append(" ?, CURRENT_TIMESTAMP, ?, ?, ? " );
			sql.append(" ) ");
			
			
			//쿼리 준비
			ps = conn.prepareStatement(sql.toString());
			
//			System.out.println("<<------ "+"" + "INSERT QUERY ------>>");
			
			
			for(DistributeRegPartToEpmData data : distributeDataList) {
				
				List<ApplicationData> attachList = data.getDataList();
				
				for (ApplicationData attach : attachList) {
					
					queryResult = sql.toString();
					
					int idx = 1;
					
					//MAIN_OBJ_NO
					ps.setString(idx++, distNumber);
					queryResult = queryResult.replaceFirst( "\\?", "'"+distNumber+"'" );
					
					//SUB_OBJ_NO
					ps.setString(idx++, data.getDisNumber());
					queryResult = queryResult.replaceFirst( "\\?", "'"+data.getDisNumber()+"'" );
					
					//FILE_NAME
					ps.setString(idx++, attach.getFileName());
					queryResult = queryResult.replaceFirst( "\\?", "'"+attach.getFileName()+"'" );
					
					//LOCATION
					String location = FTPUtil.CPC_FILE_PATH + path + File.separator + attach.getFileName();
					ps.setString(idx++, location);
					queryResult = queryResult.replaceFirst( "\\?", "'"+location+"'" );
					
					//SIZE
					ps.setInt(idx++, Long.valueOf(attach.getFileSize()).intValue() );
					queryResult = queryResult.replaceFirst( "\\?", "'"+Long.valueOf(attach.getFileSize()).intValue()+"'" );
					
					//EPM_NO
					ps.setString(idx++, data.getPartNumber());
					queryResult = queryResult.replaceFirst( "\\?", "'"+data.getPartNumber()+"'" );
					
					//FILE_VERSION
					ps.setString(idx++, data.getDisVersion());
					queryResult = queryResult.replaceFirst( "\\?", "'"+data.getDisVersion()+"'" );
					
					//SUB_OBJ_NAME
					ps.setString(idx++, data.getDisName());
					queryResult = queryResult.replaceFirst( "\\?", "'"+data.getDisName()+"'" );
					
					
//					System.out.println("LastQR ------------ :::::::::: " + queryResult);
					
					//Batch 담기
					ps.addBatch();
					
					//파라미터 Clear
					ps.clearParameters();
					
					Map<String,Object> item = new HashMap();
					item.put("app", attach);
					item.put("path", path);
					ftpFileList.add(item);
				}
				
			}
			
			//배치 실행
			ps.executeBatch();
			
			//배치 Clear
			ps.clearBatch(); 
			
			conn.commit();
			
		}catch(Exception e) {
			ftpFileList = new ArrayList<>();
			e.printStackTrace();
			conn.rollback();
		}finally {
			DBConnectionManager.closeDB(conn, ps, rs);
		}
		
		return ftpFileList;
	}

}
