package com.e3ps.common.code.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.e3ps.common.code.NumberCode;
import com.e3ps.common.code.NumberCodeType;
import com.e3ps.common.code.bean.NumberCodeData;
import com.e3ps.common.code.service.CodeHelper;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.util.StringUtil;

public class NumberCodeUtil {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	public  static List<Map<String,String>> getCodeTypeList() {
		
		NumberCodeType[] codeType = NumberCodeType.getNumberCodeTypeSet();
		
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		
		for(int i=0; i < codeType.length; i++){	
			Map<String,String> map = new HashMap<String,String>();
			
			map.put("key", codeType[i].toString());
			map.put("value", codeType[i].getDisplay(MessageUtil.getLocale()));
			
			map.put("isTree", codeType[i].getAbbreviatedDisplay());
			if(!codeType[i].toString().equals("SG"))
			list.add(map);
		}
		
		return list;
	}
	
	public static List<Map<String,String>> getProjectRoleCodeList(Map<String, Object> reqMap) throws Exception {
		
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		
		reqMap.put("disabledCheck", true);
		
		List<NumberCodeData> codeDataList = CodeHelper.manager.getNumberCodeList(reqMap);
		
		for(NumberCodeData data : codeDataList){	
			Map<String,String> map = new HashMap<String,String>();
			if(!"PM".equals(data.getName())) {
				map.put("oid", data.getOid());
				map.put("key", data.getCode());
				map.put("value", data.getName());
				map.put("description", data.getDescription());
				
				list.add(map);
			}
		}
		return list;
	}
	
	
	public static List<Map<String,String>> getCodeList(Map<String, Object> reqMap) throws Exception {
		
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		
		reqMap.put("disabledCheck", true);
		
		List<NumberCodeData> codeDataList = CodeHelper.manager.getNumberCodeList(reqMap);
		
		for(NumberCodeData data : codeDataList){	
			Map<String,String> map = new HashMap<String,String>();

			map.put("oid", data.getOid());
			map.put("key", data.getCode());
			map.put("value", data.getName());
			map.put("description", data.getDescription());
			
			list.add(map);
		}
		
		return list;
	}
	
	/**
	 * @desc	: 2Level 넘버코드 리스트 가져오기
	 * @author	: shkim
	 * @date	: 2020. 9. 17.
	 * @method	: getNumberCodeChildListQuery
	 * @param   : reqMap
	 * @return  : QuerySpec
	 */
	public static List<Map<String,String>> getCodeChildList(Map<String, Object> reqMap) throws Exception {
		
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		
		List<NumberCodeData> codeDataList = CodeHelper.manager.getNumberCodeChildList(reqMap);
		
		for(NumberCodeData data : codeDataList){	
			Map<String,String> map = new HashMap<String,String>();

			map.put("key", data.getCode());
			map.put("value", data.getName());
			map.put("description", data.getDescription());
			
			list.add(map);
		}
		
		return list;
	}
	
	/**
	 * @desc	: 프로젝트 명 (넘버코드 : PROJECTCODE) 리스트 가져오기
	 * @author	: shkim
	 * @date	: 2020. 9. 17.
	 * @method	: getProjectCodeList
	 * @param   : reqMap
	 * @return  : List<Map<String, String>>
	 */
	public static List<Map<String, String>> getProjectCodeList() throws Exception {
		List<Map<String, String>> list = new ArrayList<>();
		
		Map<String, Object> reqMap = new HashMap<>();
		
		reqMap.put("disabledCheck", true);
		reqMap.put("codeType", "PROJECTCODE");
		
		List<NumberCodeData> codeDataList = CodeHelper.manager.getNumberCodeList(reqMap);
		
		for(NumberCodeData data : codeDataList) {
			Map<String,String> map = new HashMap<String,String>();

			map.put("key", data.getCode());
			map.put("value", data.getName());
			map.put("description", data.getDescription());
			
			list.add(map);
		}
		
		return list;
	}
	
	/**
	 * NumberCodeData Name 다국어 처리
	 * @param code
	 * @return
	 */
	public static String getLangugeValue(NumberCode code ){
		
		String value = code.getName();
		return value;
	}
	
	/**
	 * NumberCodeData Name 다국어 처리
	 * @param code
	 * @return
	 */
	public static String getLangugeValue(NumberCodeData code ){
		
		String value = code.getName();
		
		if(!MessageUtil.isLangKor()){
			value = code.getEngName();
			
			if(!StringUtil.checkString(value)){
				value = code.getName();
				
			}
		}
		
		return value;
	}
	
	public static Map<String,Object> getCreateNumberPropCodeList(Map<String, Object> reqMap) throws Exception {
		
		String codeType = StringUtil.checkNull((String) reqMap.get("codeType"));
		
		Map<String,Object> list = new HashMap<String,Object>();
		
		List<NumberCodeData> codeDataList = CodeHelper.manager.getNumberCodeList(codeType, false);
		
		codeDataList.stream().forEach((d) -> {
			if(d.getParentCode()==null) {
				List<NumberCodeData> childList = new ArrayList<NumberCodeData>();
				list.put(d.getCode(), childList);
				codeDataList.stream().forEach((a) -> {
					if(d.getCode().equals(a.getParentCode())) {
						childList.add(a);
					}
				});
			}
		});
		

		return list;
	}
}
