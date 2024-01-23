package com.e3ps.part.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.DateUtil;
import wt.method.MethodContext;
import wt.part.WTPart;
import wt.part.WTPartMaster;
import wt.part.WTPartUsageLink;
import wt.pom.DBProperties;
import wt.pom.WTConnection;
import wt.services.StandardManager;
import wt.util.WTException;

@SuppressWarnings("serial")
public class StandardBomService extends StandardManager implements BomService {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PART.getName());
	public static StandardBomService newStandardBomService() throws WTException {
		final StandardBomService instance = new StandardBomService();
		instance.initialize();
		return instance;
	}
	
	@Override
	public void doubleBomClear(){
		String log = "";
		try{
			List<Map<String,String>> list = doubleBomClearList();
			
			for(Map map :  list){
				
	        	String paretnNumber = (String)map.get("paretnNumber");
	        	String childNumber = (String)map.get("childNumber");
	        	
				WTPart pPart = PartHelper.manager.getPart(paretnNumber);
				WTPart cPart = PartHelper.manager.getPart(childNumber);
				
				if(pPart == null){
					continue;
				}
				
				if(cPart == null){
					continue;
				}
				
				
				List<WTPartUsageLink> bomlist  = BomHelper.manager.getUsageLink(cPart, pPart);
				
				for(WTPartUsageLink link : bomlist){
					String cadSys = link.getCadSynchronized().toString();
					
					WTPart pLinkPart = (WTPart)link.getRoleAObject();
					WTPartMaster cLinkPart = (WTPartMaster)link.getRoleBObject();
					
					if("no".equals(cadSys)){
						log = "Delete BOM pNumber =" + pLinkPart.getNumber() +",cNumber = " + cLinkPart.getNumber() + ",CadSynchronized = " + cadSys +"," + link;
						LOGGER.info(log);
						createLog(log, "DouBomClear.txt");
					}
				
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private List<Map<String,String>> doubleBomClearList(){
		
		MethodContext methodcontext = null;
		WTConnection wtconnection = null;

        PreparedStatement st = null;
        ResultSet rs = null;
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        
		try {
			
			methodcontext = MethodContext.getContext();
			wtconnection = (WTConnection) methodcontext.getConnection();
			Connection con = wtconnection.getConnection();
			
		 	StringBuffer sb = new StringBuffer();
	        sb.append(" SELECT A0.WTPARTNUMBER AS PP,A2.WTPARTNUMBER AS CP ");
	        sb.append(" FROM VIEW_LAST_WTPART A0, WTPARTMASTER A2, ");
	        sb.append("(SELECT IDA3A5, IDA3B5  FROM WTPARTUSAGELINK ");
	        sb.append(" GROUP BY IDA3A5, IDA3B5 HAVING COUNT(*) > 1) DU ");
	        sb.append(" WHERE DU.IDA3A5 = A0.PARTOID ");
	        sb.append(" AND DU.IDA3B5 = A2.IDA2A2 ");
	        
	        LOGGER.info(sb.toString());
	        
	        st = con.prepareStatement(sb.toString());
	        
	        rs = st.executeQuery();
	        
	        while (rs.next()) {
	        	Map<String,String> map = new HashMap<String, String>();
	        	String paretnNumber = rs.getString("PP");
	        	String childNumber = rs.getString("CP");
	        	map.put("paretnNumber", paretnNumber);
	        	map.put("childNumber", childNumber);
	        	String log = "doubleBomClearList pNumber =" + paretnNumber +",cNumber = " + childNumber ;
	        	createLog(log, "DoulbeLIST.txt");
	        	list.add(map);
	        }
		
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
            if ( rs != null ) {
                try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            if ( st != null ) {
                try {
					st.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
			if (DBProperties.FREE_CONNECTION_IMMEDIATE
					&& !wtconnection.isTransactionActive()) {
				MethodContext.getContext().freeConnection();
			}
		}
		
		return list;
	}
	
	private  void createLog(String log,String fileName) {
		//LOGGER.info("========== "+fileName+" ===========");
		String toDay = DateUtil.getToDay();
		toDay = toDay.replace("/", "-");
		String filePath = "D:\\e3ps\\loader\\DouBomClear\\"+toDay;
		
		File folder = new File(filePath);
		
		if(!folder.isDirectory()){
			
			folder.mkdirs();
		}
		fileName = fileName.replace(",", "||");
		
		toDay = com.e3ps.common.util.StringUtil.changeString(toDay, "/", "-");
		String logFileName = fileName+"_" + toDay.concat(".log");
		String logFilePath = filePath.concat(File.separator).concat(logFileName);
		File file = new File(logFilePath);
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(file, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		PrintWriter out = new PrintWriter(new BufferedWriter(fw), true);
		out.write(log);
		//LOGGER.info(log);
		out.write("\n");
		out.close();
	}
}
