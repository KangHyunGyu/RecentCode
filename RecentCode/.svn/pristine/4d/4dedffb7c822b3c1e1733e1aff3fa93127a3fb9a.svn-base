package com.e3ps.interfaces.cpc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3ps.interfaces.cpc.service.CPCHelper;

@Controller
@RequestMapping("/cpc")
public class CPCController {
	

//	/**
//	 * @desc : purchase oid로 구매요청 erp 이력 가져오기
//	 * @author : shjeong
//	 * @date : 2022. 11. 15.
//	 * @method : getPurchaseRequestFromERP
//	 * @param reqMap
//	 * @return Map<String,Object>
//	 */
//	@ResponseBody
//	@RequestMapping("/getPurchaseRequestFromERP")
//	public Map<String,Object> getPurchaseRequestFromERP(@RequestBody Map<String,Object> reqMap) {
//		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
//		String purchaseNumber = StringUtil.checkNull((String) reqMap.get("purchaseNumber"));
//		Map<String,Object> result = new HashMap<String,Object>();
//		try {
//			
//			ERPHeaderData returnData = ERPHelper.manager.selectPurchaseRequest(purchaseNumber);
//			result.put("result", true);
//			result.put("list", returnData);
//			
//		} catch(Exception e) {
//			e.printStackTrace();
//			result.put("result", false);
//			result.put("msg", "ERROR = " + e.getLocalizedMessage());
//		}
//		
//		return result;
//	}
//	
//	/**
//	 * @desc : dms 코드 리스트 가져오기
//	 * @author : shjeong
//	 * @date : 2023. 04. 19.
//	 * @method : getCode
//	 * @param reqMap
//	 * @return Map<String,Object>
//	 */
//	@ResponseBody
//	@RequestMapping("/getCodeList")
//	public Map<String,Object> getCodeList(@RequestBody Map<String,Object> reqMap) {
//		String codeType = StringUtil.checkNull((String) reqMap.get("codeType"));
//		Map<String,Object> result = new HashMap<String,Object>();
//		try {
//		
//			List<Map<String,String>> list = DMSHelper.manager.getCodeList(codeType);
//			result.put("result", true);
//			result.put("list", list);
//			
//		} catch(Exception e) {
//			e.printStackTrace();
//			result.put("result", false);
//			result.put("msg", "ERROR = " + e.getLocalizedMessage());
//		}
//		
//		return result;
//	}
//	
	/**
	 * @desc : cpc 코드 리스트 가져오기
	 * @author : shjeong - hgkang
	 * @date : 2023. 04. 19.
	 * @method : getCode
	 * @param reqMap
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/searchCompanyUsers")
	public Map<String,Object> searchCompanyUsers(@RequestBody Map<String,Object> reqMap) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
		
			List<Map<String,String>> list = CPCHelper.manager.getCompanyUsers(reqMap);
			result.put("result", true);
			result.put("list", list);
			
		} catch(Exception e) {
			e.printStackTrace();
			result.put("result", false);
			result.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/companyAutoSearch")
	public Map<String,Object> companyAutoSearch(@RequestBody Map<String,Object> reqMap) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
		
			List<Map<String,String>> list = CPCHelper.manager.getCompanyByKeyword(reqMap);
			result.put("result", true);
			result.put("list", list);
			
		} catch(Exception e) {
			e.printStackTrace();
			result.put("result", false);
			result.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/companyUserAutoSearch")
	public Map<String,Object> companyUserAutoSearch(@RequestBody Map<String,Object> reqMap) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
		
			List<Map<String,String>> list = CPCHelper.manager.getCompanyUserByKeyword(reqMap);
			result.put("result", true);
			result.put("list", list);
			
		} catch(Exception e) {
			e.printStackTrace();
			result.put("result", false);
			result.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return result;
	}
	
}
