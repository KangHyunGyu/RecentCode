package com.e3ps.dashboard.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.e3ps.dashboard.bean.ProjectDashboardData;
import com.e3ps.dashboard.service.ProjectDashboardHelper;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
	
	
	@RequestMapping("/projectLevelDashboard")
	public ModelAndView projectLevelDashboard(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		model.setViewName("default:/dashboard/projectLevelDashboard");

		return model;
	}
	
	
	
	/**
	 * @desc : 현황판에서 프로젝트 검색 - 제품군별, 제조사별, 진척현황
	 * @author : Administrator
	 * @date : 2023. 2. 16.
	 * @method : projects
	 * @param reqMap
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/projects")
	public Map<String, Object> projects(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			List<ProjectDashboardData> list = ProjectDashboardHelper.manager.searchProject(reqMap);
			result.put("result", true);
			result.put("list", list);
		} catch(Exception e) {
			e.printStackTrace();
			result.put("result", false);
			result.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		return result;
	}
	
	
	
	/**
	 * @desc : 프로젝트 현황판 관리
	 * @author : Administrator
	 * @date : 2023. 2. 16.
	 * @method : projectDashboardSetting
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/setting")
	public ModelAndView projectDashboardSetting(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		model.setViewName("default:/dashboard/projectDashboardSetting");

		return model;
	}
	
	
	/**
	 * @desc : 프로젝트 진척현황
	 * @author : Administrator
	 * @date : 2023. 2. 16.
	 * @method : projectLevelDashboard
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/progress")
	public ModelAndView projectProgressDashboard(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		model.setViewName("default:/dashboard/projectProgress");

		return model;
	}
}
