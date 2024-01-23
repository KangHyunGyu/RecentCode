package com.e3ps.interfaces.cpc.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.e3ps.common.code.service.CodeHelper;
import com.e3ps.common.db.DBConnectionManager;
import com.e3ps.common.jdf.config.Config;
import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.util.StringUtil;
import com.e3ps.interfaces.util.InterfaceUtil;

import wt.util.WTProperties;

public class CPCHelper {
	
	public static final CPCHelper manager = new CPCHelper();
	
	private final String CODE_TABLE = "NUMBERCODE";
	
	private final String COMPANY_TABLE = "COMPANY";
	
	private final String COMPANYUSER_TABLE = "COMPANYUSER";
	
	private final String RECEPTION_TABLE = "RECEPTION";
	
	private final Config CONF = ConfigImpl.getInstance();
	
	
//	/**
//	 * @desc : DMS CODE LIST 가져오기
//	 * @author : shjeong
//	 * @date : 2023. 04. 19.
//	 * @method : getCodeList
//	 * @param  String
//	 * @throws Exception void
//	 */
//	public List<Map<String,String>> getCodeList(String codeType) {
//		
//		List<Map<String,String>> itemList = new ArrayList<>();
//		Map<String,String> item = null;
//		Connection conn = null;
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		try {
//			conn = InterfaceUtil.getDMSConnection();
//			
//			
//			String query = "SELECT * FROM " + CODE_TABLE;
//			query += " WHERE ENABLED='Y' ";
//			query += " AND CODETYPE='" + codeType + "' ";
//			
//			ps = conn.prepareStatement(query);
//			rs = ps.executeQuery();
//			while(rs.next()) {
//				item = new HashMap<>();
//				item.put("key", rs.getString("CODE"));
//				item.put("value", rs.getString("NAME"));
//				item.put("pcode", rs.getString("PCODE"));
//				itemList.add(item);
//			}
//			
//		}catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			DBConnectionManager.closeDB(conn, ps, rs);
//		}
//		
//		return itemList;
//	}
//	
//	
	/**
	 * @desc : CPC 업체 가져오기
	 * @author : shjeong - hgkang
	 * @date : 2023. 04. 19.
	 * @method : getCodeList
	 * @param  String
	 * @throws Exception void
	 */
	public List<Map<String,String>> getCompanys() throws Exception {
		
		List<Map<String,String>> itemList = new ArrayList<>();
		Map<String,String> item = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = InterfaceUtil.getCPCConnection();
			
			String query = "SELECT COMPANY_ID, COMPANY_NAME FROM " + COMPANY_TABLE;
			
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				item = new HashMap<>();
				item.put("company_id", rs.getString("COMPANY_ID"));
				item.put("company_name", rs.getString("COMPANY_NAME"));
				itemList.add(item);
			}
			
		} finally {
			DBConnectionManager.closeDB(conn, ps, rs);
		}
		
		return itemList;
	}
	
	public String getCompanyName(String id) throws Exception {
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = InterfaceUtil.getCPCConnection();
			
			String query = "SELECT COMPANY_NAME FROM " + COMPANY_TABLE + " WHERE COMPANY_ID='" + id + "'";
			
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			if(rs.next()) {
				return rs.getString("COMPANY_NAME");
			}
			
		} finally {
			DBConnectionManager.closeDB(conn, ps, rs);
		}
		
		return "";
	}
	
	public String getCompanyUserName(String id) throws Exception {
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = InterfaceUtil.getCPCConnection();
			
			String query = "SELECT NAME FROM " + COMPANYUSER_TABLE + " WHERE ID='" + id + "'";
			
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			if(rs.next()) {
				return rs.getString("NAME");
			}
			
		} finally {
			DBConnectionManager.closeDB(conn, ps, rs);
		}
		
		return "";
	}
	
	public List<Map<String,String>>  getCompanyByKeyword(Map<String,Object> reqMap) throws Exception {
		List<Map<String,String>> itemList = new ArrayList<>();
		Map<String,String> item = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String keyword = StringUtil.checkNull((String) reqMap.get("keyword"));
		try {
			conn = InterfaceUtil.getCPCConnection();
			
			String query = "SELECT COMPANY_ID, COMPANY_NAME FROM " + COMPANY_TABLE + " WHERE UPPER(COMPANY_NAME) LIKE '%" + keyword.toUpperCase() + "%'";
			
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				item = new HashMap<>();
				item.put("company_id", rs.getString("COMPANY_ID"));
				item.put("company_name", rs.getString("COMPANY_NAME"));
				itemList.add(item);
			}
			
		} finally {
			DBConnectionManager.closeDB(conn, ps, rs);
		}
		
		return itemList;
	}
	
	public List<Map<String,String>>  getCompanyUserByKeyword(Map<String,Object> reqMap) throws Exception {
		List<Map<String,String>> itemList = new ArrayList<>();
		Map<String,String> item = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String keyword = StringUtil.checkNull((String) reqMap.get("keyword"));
		try {
			conn = InterfaceUtil.getCPCConnection();
			
			String query = "SELECT ID, NAME FROM " + COMPANYUSER_TABLE + " WHERE UPPER(NAME) LIKE '%" + keyword.toUpperCase() + "%'";
			
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				item = new HashMap<>();
				item.put("id", rs.getString("ID"));
				item.put("name", rs.getString("NAME"));
				itemList.add(item);
			}
			
		} finally {
			DBConnectionManager.closeDB(conn, ps, rs);
		}
		
		return itemList;
	}
	
	
	
	/**
	 * @desc : CPC 업체user 가져오기
	 * @author : shjeong - hgkang
	 * @date : 2023. 04. 19.
	 * @method : getCodeList
	 * @param  String
	 * @throws Exception void
	 */
	public List<Map<String,String>> getCompanyUsers(Map<String,Object> reqMap) throws Exception {
		
		String keyword = StringUtil.checkNull((String) reqMap.get("keyword"));
		String companyID = StringUtil.checkNull((String) reqMap.get("companyID"));
		List<Map<String,String>> itemList = new ArrayList<>();
		Map<String,String> item = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = InterfaceUtil.getCPCConnection();
			
			String query = "SELECT C1.COMPANY_ID, C2.ID, C2.NAME, C2.DEPARTMENT FROM " + COMPANY_TABLE + " C1," + COMPANYUSER_TABLE + " C2";
			query += " WHERE C2.ENABLED='Y' ";
			query += " AND C1.COMPANY_ID=C2.COMPANY_ID ";
			query += " AND C1.COMPANY_ID='"+companyID+"' ";
			query += " AND (UPPER(C2.NAME) LIKE UPPER('%" + keyword + "%') ";
			query += " OR UPPER(C2.ID) LIKE UPPER('%" + keyword + "%') )";
			
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				item = new HashMap<>();
				item.put("cpc_id", rs.getString("ID"));
				item.put("cpc_name", rs.getString("NAME"));
				item.put("cpc_department", rs.getString("DEPARTMENT"));
				itemList.add(item);
			}
			
		} finally {
			DBConnectionManager.closeDB(conn, ps, rs);
		}
		
		return itemList;
	}
//	
//	/**
//	 * @desc : RECEPTION 테이블에서 배포 요청 중복 확인
//	 * @author : shjeong
//	 * @date : 2023. 04. 25.
//	 * @method : duplicateCheckReceptionByNumber
//	 * @param  String 
//	 * @throws Exception void
//	 */
//	public boolean duplicateCheckReceptionByNumber(String number) throws Exception {
//		
//		boolean exist = false;
//		Connection conn = null;
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		
//		try {
//			conn = InterfaceUtil.getDMSConnection();
//			String query = "select * from " + RECEPTION_TABLE;
//			query += " WHERE DIST_NO='" + number + "' ";
//			
//			
//			ps = conn.prepareStatement(query);
//			rs = ps.executeQuery();
//			
//			if(rs.next()) {
//				exist = true;
//			}
//			
//		} finally {
//			DBConnectionManager.closeDB(conn, ps, rs);
//		}
//		
//		return exist;
//	}
	
}
