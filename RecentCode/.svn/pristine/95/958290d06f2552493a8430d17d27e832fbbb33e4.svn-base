package com.e3ps.change.util;

import java.util.HashMap;
import java.util.Map;

import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.ObjectUtil;
import com.e3ps.common.util.StringUtil;

import wt.epm.EPMDocument;

public class ChangeUtil {
	
	/**
	 * 설계변경에서 도면 개정전 개정후 정보 SET
	 * @methodName : setEPMDocument
	 * @author : tsuam
	 * @date : 2021.12.10
	 * @return : Map<String,Object>
	 * @description :
	 */
	public static Map<String,Object>setEPMDocument(EPMDocument epm,boolean isRevise) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		
		//개정전 
		String oid = CommonUtil.getOIDString(epm);
		String number = epm.getNumber();
		String name = epm.getName();
		String revision = StringUtil.checkNull(epm.getVersionIdentifier().getSeries().getValue());
		String iteration = StringUtil.checkNull(epm.getIterationIdentifier().getSeries().getValue());
		String ver = revision+"."+iteration;
		String stateName = StringUtil.checkNull(epm.getLifeCycleState().getDisplay(MessageUtil.getLocale()));
		map.put("oid",oid);
		map.put("number",number);
		map.put("name",name);
		map.put("ver",ver);
		map.put("stateName",stateName);
		
		//개정후
		String nextVer = "";
		String nextState ="";
		String nextOid = "";
//		if(isRevise) {
		//임시
		EPMDocument nextEPM = (EPMDocument)ObjectUtil.getNextVersion(epm);
		if(nextEPM != null) {
			nextOid = CommonUtil.getOIDString(nextEPM);
			revision = StringUtil.checkNull(nextEPM.getVersionIdentifier().getSeries().getValue());
			iteration = StringUtil.checkNull(nextEPM.getIterationIdentifier().getSeries().getValue());
			
			nextVer = revision+"."+iteration;
			nextState = StringUtil.checkNull(nextEPM.getLifeCycleState().getDisplay(MessageUtil.getLocale()));
		}
//		}
		map.put("nextVer",nextVer);
		map.put("nextState",nextState);
		map.put("nextOid",nextOid);
		
		
		
		return map;
	}
	
}
