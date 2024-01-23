package com.e3ps.part.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.e3ps.common.code.bean.NumberCodeData;
import com.e3ps.common.code.service.CodeHelper;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.message.util.MessageUtil;

import wt.part.QuantityUnit;
import wt.part.WTPart;
import wt.session.SessionHelper;
import wt.vc.wip.WorkInProgressHelper;

public class PartUtil {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PART.getName());
	/**
	 * @desc : 부품 단위 리스트
	 * @author : sangylee
	 * @date : 2019. 9. 4.
	 * @method : getUnitList
	 * @return : List<Map<String,String>>
	 */
	public static List<Map<String,String>> getUnitList(){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();

		QuantityUnit[] unitType = QuantityUnit.getQuantityUnitSet();
		
//		List<String> useList = getUseUnitList();
		
		for(QuantityUnit type : unitType) {
			if(!type.isSelectable()) continue;
			Map<String,String> map = new HashMap<String,String>();
			
//			if(useList.contains(type.toString())) {
				map.put("key", type.toString());
				map.put("value", type.toString().toUpperCase() +" | " +type.getDisplay());
				list.add(map);
//			}
		}
		
		return list;
	}
	
	/**
	 * @desc : 부품 단위 리스트 커스텀
	 * @author : sangylee
	 * @date : 2019. 9. 4.
	 * @method : getUseUnitList
	 * @return : List<String>
	 */
	public static List<String> getUseUnitList(){
		
		List<String> list = new ArrayList<>();
		
		return list;
	}
	
	/**
	 * @desc : 부품 재질 리스트
	 * @author : sangylee
	 * @date : 2019. 9. 4.
	 * @method : getMaterialList
	 * @return : List<Map<String,String>>
	 * @throws Exception
	 */
	public static List<Map<String,String>> getMaterialList() throws Exception{
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();

		Map<String, Object> reqMap = new HashMap<>();
		
		reqMap.put("disabledCheck", true);
		reqMap.put("codeType", "MATERIAL");
		
		List<NumberCodeData> codeDataList = CodeHelper.manager.getNumberCodeList(reqMap);
		
		for(NumberCodeData data : codeDataList) {
			Map<String,String> map = new HashMap<String,String>();
			
			map.put("key", data.getName());
			map.put("value", data.getName());
			list.add(map);
		}
		
		return list;
	}
	
	/**
	 * @desc : Material Type 가져오기
	 * @author : sangylee
	 * @date : 2019. 9. 6.
	 * @method : getMaterialTypeList
	 * @return : List<Map<String,String>>
	 * @throws Exception
	 */
	public static List<Map<String,String>> getMaterialTypeList() throws Exception{
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();

		Map<String,String> map = new HashMap<String,String>();
		
		map.put("key", "M");
		map.put("value", "Module");
		list.add(map);
		
		map = new HashMap<String,String>();
		
		map.put("key", "U");
		map.put("value", "Unit");
		list.add(map);
		
		map = new HashMap<String,String>();
		
		map.put("key", "A");
		map.put("value", "Ass'y");
		list.add(map);
		
		map = new HashMap<String,String>();
		
		map.put("key", "P");
		map.put("value", "Part");
		map.put("selected", "selected");
		list.add(map);
		
		return list;
	}
	
	/**
	 * 
	 * @desc	: 제어 부품 여부 체크 ( P5~, P6~, P7~ / A5~, A6~, A7~)
	 * @author	: tsuam
	 * @date	: 2020. 1. 11.
	 * @method	: isContorlPart
	 * @return	: boolean
	 * @return
	 */
	public static boolean isContorlPart(String partNumber){
		
		boolean isControl = false;
		
		String firstNumber = partNumber.substring(0, 2);
		
		if("A5".equals(firstNumber) || "A6".equals(firstNumber) || "A7".equals(firstNumber) || "P5".equals(firstNumber) || "P6".equals(firstNumber) || "P7".equals(firstNumber)){
			isControl = true;
		}
		
		return isControl;
	}
	
	/**
	 * 
	 * @desc	: SW 부품 여부 체크(P9013~ / A9010~)
	 * @author	: plmadmin
	 * @date	: 2020. 2. 18.
	 * @method	: isSwPart
	 * @return	: boolean
	 * @param partNumber
	 * @return
	 */
	public static boolean isSwPart(String partNumber){
		boolean isSw = false;
		String firstNumber = partNumber.substring(0, 5);
		
		if("P9013".equals(firstNumber) || "A9010".equals(firstNumber)){
			isSw = true;
		}
				
		
		return isSw;
	}
	
	/**
	 * 
	 * @desc	: 설계 부품중 CPC 제외 부품 A1, A2, P1, P2 
	 * @author	: tsuam
	 * @date	: 2020. 2. 19.
	 * @method	: isDesginException
	 * @return	: boolean
	 * @param partNumber
	 * @return
	 */
	public static boolean isDesginException(String partNumber){
		boolean isDesginE = false;
		
		String firstNumber = partNumber.substring(0, 2);
		
		if("A1".equals(firstNumber) || "A2".equals(firstNumber) || "P1".equals(firstNumber) ||  "P2".equals(firstNumber)){
			isDesginE = true;
		}
	
		return isDesginE;
	}
	
	
	/**
	 * 
	 * @desc	: 배포 대상 여부 부품 (제어 ,SW 제외)
	 * @author	: plmadmin
	 * @date	: 2020. 2. 18.
	 * @method	: isDistributePart
	 * @return	: boolean
	 * @return
	 */
	public static boolean isDistributePart(String partNumber){
		
		//제어 P5~, P6~, P7~ / A5~, A6~, A7~
		if(isContorlPart(partNumber)){
			return false;
		}
		
		//SW P9013~ / A9010~
		if(isSwPart(partNumber)){
			return false;
		}
		
		//설계 제외 부품  부품 A1, A2, P1, P2 
		if(isDesginException(partNumber)){
			return false;
		}
		
		return true;
	}
	
	public static boolean isOwnerCheckOut(WTPart part){
		boolean isOwnerCheckOut = false;
		boolean isCheckout = false;
		try{
			if(WorkInProgressHelper.isWorkingCopy(part)){
				isCheckout = true;
			}else{
				if(WorkInProgressHelper.isCheckedOut(part)){
					isCheckout = true;
				}
			}
			
			if(isCheckout){
				if( part.getModifierName().equals(SessionHelper.getPrincipal().getName())){
					isOwnerCheckOut = true;
				}
			}else{
				isOwnerCheckOut = true;
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return isOwnerCheckOut;
	}
	
	public static boolean isCheckOut(WTPart part) throws Exception {
		boolean isCheckout = false;
		if(WorkInProgressHelper.isWorkingCopy(part)){
			isCheckout = true;
		}else{
			if(WorkInProgressHelper.isCheckedOut(part)){
				isCheckout = true;
			}
		}
		
		return isCheckout;
	}
	
}
