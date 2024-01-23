/**
 * 
 */
package com.e3ps.statistics.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.StringUtil;
import com.e3ps.statistics.service.StatisticsHelper;


@Controller
@RequestMapping("/statistics")
public class StatisticsController {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.STATISTICS.getName());
	
	/**
	 * @desc	: 서브메뉴 (활동, 결재, ToDay, My)
	 * @author	: mnyu
	 * @date	: 2019. 12. 02.
	 * @method	: statistics
	 * @param reqMap
	 * @return ModelAndView
	 * @throws Exception 
	 */
	@RequestMapping("/include_statistics")
	public ModelAndView statistics(@RequestParam Map<String, Object> reqMap) throws Exception {
		ModelAndView model = new ModelAndView();
		String obj = StringUtil.checkNull((String) reqMap.get("obj"));		// 모듈
		boolean active = StringUtil.booleanValue(reqMap.get("active"));		// 활동(ToDo), 결재
		boolean today = StringUtil.booleanValue(reqMap.get("today"));		// ToDay, My
		
		//StatisticsData data = StatisticsHelper.manager.getStatistics(reqMap);
		
		model.addObject("obj", obj);
		model.addObject("active", active);
		model.addObject("today", today);
		//model.addObject("data", data);
		model.setViewName("include:/statistics/include/statistics");
		
		return model;
	}
	
	/**
	 * @desc	: 결재 Count
	 * @author	: mnyu
	 * @date	: 2019. 12. 31.
	 * @method	: getApprovalCount
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getApprovalCount")
	public Map<String, Object> getApprovalCount() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			int count = StatisticsHelper.manager.getApprovalCount();
			map.put("count", count);
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	/**
	 * @desc	: ToDay All Count
	 * @author	: mnyu
	 * @date	: 2019. 12. 31.
	 * @method	: getToDayCount
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getToDayCount")
	public Map<String, Object> getToDayCount(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String obj = StringUtil.checkNull((String) reqMap.get("obj"));		// 모듈
			
			int count = StatisticsHelper.manager.getToDayCount(obj, true);
			map.put("count", count);
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	/**
	 * @desc	: ToDay My Count
	 * @author	: mnyu
	 * @date	: 2019. 12. 31.
	 * @method	: getToDayMyCount
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getToDayMyCount")
	public Map<String, Object> getToDayMyCount(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String obj = StringUtil.checkNull((String) reqMap.get("obj"));		// 모듈
			
			int count = StatisticsHelper.manager.getToDayCount(obj, false);
			map.put("count", count);
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/getToDoCount")
	public Map<String, Object> getToDoCount() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			int count = StatisticsHelper.manager.getToDoCount();
			map.put("count", count);
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
}
