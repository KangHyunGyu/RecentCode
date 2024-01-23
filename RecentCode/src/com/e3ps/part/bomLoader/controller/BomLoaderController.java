package com.e3ps.part.bomLoader.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.part.bomLoader.bean.LoadBomData;
import com.e3ps.part.bomLoader.service.BomLoaderHelper;

@Controller
@RequestMapping("/bomLoader")
public class BomLoaderController {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PART.getName());
	
	/**
	 * @desc : 부품 검증
	 * @author : sangylee
	 * @date : 2019. 12. 22.
	 * @method : checkPartAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/checkPartAction")
	public static Map<String, Object> checkPartAction(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<>();
		
		try {
			List<LoadBomData> list = BomLoaderHelper.service.checkPartAction(reqMap);
			
			map.put("result",true);
			map.put("list", list);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc : 부품 로드 Action
	 * @author : sangylee
	 * @date : 2019. 12. 22.
	 * @method : loadPartAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/loadPartAction")
	public static Map<String, Object> loadPartAction(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<>();
		
		try {
			List<LoadBomData> list = BomLoaderHelper.service.loadPartAction(reqMap);
			
			map.put("result",true);
			map.put("list", list);
			map.put("msg", "부품 로드가 완료되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc : BOM 검증
	 * @author : sangylee
	 * @date : 2019. 12. 12.
	 * @method : checkBomAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/checkBomAction")
	public static Map<String, Object> checkBomAction(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<>();
		
		try {
			List<LoadBomData> list = BomLoaderHelper.service.checkBomAction(reqMap);
			
			map.put("result",true);
			map.put("list", list);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc : Bom 로드 Action
	 * @author : sangylee
	 * @date : 2019. 12. 10.
	 * @method : loadBomAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/loadBomAction")
	public static Map<String, Object> loadBomAction(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<>();
		
		try {
			List<LoadBomData> list = BomLoaderHelper.service.loadBomAction(reqMap);
			LOGGER.debug("end real");
			map.put("list", list);
			map.put("result", true);
			map.put("msg", "BOM 로드가 완료되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
}
