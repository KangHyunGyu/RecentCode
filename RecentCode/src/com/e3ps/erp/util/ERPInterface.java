package com.e3ps.erp.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.e3ps.change.EChangeOrder2;
import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.project.EProject;

import wt.fc.Persistable;
import wt.method.RemoteAccess;
import wt.part.WTPart;

public class ERPInterface implements RemoteAccess, Serializable {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.ERP.getName());
	static final long serialVersionUID = -7046722787051872965L;
	public static final String CONST_EXREG_PART_NO = "^[A-Za-z]{2}-[A-Za-z0-9]{2}-.{10}$";

	/**
	 * @desc : ERP - 전송
	 * @author : gs
	 * @date : 2020. 12. 03.
	 * @method : send
	 * @param : reqMap
	 * @return : Map<String, Object>
	 * @throws Exception
	 */
	public static Map<String, Object> send(Persistable per) throws Exception {
		Map<String, Object> map = new HashMap<>();

		map = send(per, false);

		return map;
	}

	/**
	 * @desc : ERP - 전송
	 * @author : gs
	 * @date : 2020. 12. 03.
	 * @method : send
	 * @param : reqMap
	 * @return : Map<String, Object>
	 * @throws Exception
	 */
//  public static Map<String, Object> send(Persistable per, boolean flagDelete) throws Exception{
//	  
//	Map<String, Object> map = new HashMap<>();
//    ERPDataSender erpSender = new ERPDataSender();
//    boolean enableERPCheck = ConfigImpl.getInstance().getBoolean("erp.enable", false);
//    
//    String sendHistoryMsg = "";
//    try{
//    	if(enableERPCheck) {
//    		if(per instanceof EChangeOrder2){
//        		EChangeOrder2 eco = (EChangeOrder2)per;
//        		map = erpSender.sendECO(eco, flagDelete);
//        		sendHistoryMsg += (String)map.get("msg");
//        		
//    		} else if(per instanceof WTPart){
//    			WTPart part = (WTPart) per;
//    			map = erpSender.sendPart(part, flagDelete);
//    			sendHistoryMsg += (String)map.get("msg");
//        	
//    		} else if(per instanceof EProject){
//    			EProject project = (EProject) per;
//    			map = erpSender.sendProject(project, flagDelete);
//    			sendHistoryMsg += (String)map.get("msg");
//    			
//    		}
//    	}
//    } catch (Exception e) {
//    	e.printStackTrace();
//    	sendHistoryMsg += e.getLocalizedMessage();
//    	throw e;
//    } finally {
//    	ERPUtil.makeErpHistory(per, sendHistoryMsg);
//    }
//    
//    return map;
//  }

	public static Map<String, Object> send(Persistable per, boolean flagDelete) throws Exception {

		Map<String, Object> map = new HashMap<>();
		ERPDataSender erpSender = new ERPDataSender();
		boolean enableERPCheck = ConfigImpl.getInstance().getBoolean("erp.enable", false);

		try {
			if (enableERPCheck) {
				if (per instanceof EChangeOrder2) {
					EChangeOrder2 eco = (EChangeOrder2) per;
					map = erpSender.sendECO(eco, flagDelete);

				} else if (per instanceof WTPart) {
					WTPart part = (WTPart) per;
					map = erpSender.sendPart(part, flagDelete);

				} else if (per instanceof EProject) {
					EProject project = (EProject) per;
					map = erpSender.sendProject(project, flagDelete);
				}
			}
		} catch (Exception e) {
			LOGGER.info(e.getLocalizedMessage());
			e.printStackTrace();
			throw e;
		}

		return map;
	}
}
