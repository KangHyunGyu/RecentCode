package com.e3ps.calendar.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.e3ps.calendar.CalendarProjectLink;
import com.e3ps.calendar.DevelopmentStageCalendar;
import com.e3ps.calendar.bean.DSCData;
import com.e3ps.calendar.bean.DSData;
import com.e3ps.calendar.service.DsHelper;
import com.e3ps.change.EcoProjectLink;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.project.ControllerException;
import com.e3ps.project.EProject;
import com.e3ps.project.beans.ProjectData;
import com.e3ps.project.service.ProjectHelper;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.org.WTUser;
import wt.session.SessionHelper;

@Controller
@RequestMapping("/calendar")
public class DSController {
	@RequestMapping("/createDs")
	public ModelAndView createDoc(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		model.setViewName("default:/calendar/createDs");
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/createDsAction")
	public Map<String, Object> createDsAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String name = StringUtil.checkNull((String) reqMap.get("name"));
			boolean overLap = DsHelper.manager.checkOverlapByName(name);
			if(overLap) {
				map.put("result", false);
				map.put("msg", "대일정 이름이 이미 존재합니다.");
				return map;
			}
			DsHelper.service.createDsAction(reqMap);
			
			map.put("result", true);
			map.put("msg", "등록이 완료되었습니다.");
			map.put("redirectUrl", CommonUtil.getURLString("/calendar/searchDs"));
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/searchDs")
	public ModelAndView searchDs(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		model.setViewName("default:/calendar/searchDs");
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/searchDsScrollAction")
	public Map<String, Object> searchDsScrollAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			map = DsHelper.manager.getDsScrollList(reqMap);
			
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/viewDs")
	public ModelAndView viewDs(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			DevelopmentStageCalendar dsc = (DevelopmentStageCalendar) CommonUtil.getObject(oid);
			DSCData dscData = new DSCData(dsc);
			
			WTUser sessionUser = (WTUser)SessionHelper.manager.getPrincipal();
			String creator = dsc.getOwner().getName();
			boolean isModify = sessionUser.getName().equals(creator);
			boolean isAdmin = CommonUtil.isAdmin();
			
			model.addObject("dsc", dscData);
			model.addObject("dsoid", oid);
			model.addObject("isModify", isModify);
			model.addObject("isAdmin", isAdmin);
			model.setViewName("popup:/calendar/viewDs");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/getDsGanttData")
	public Map<String, Object> getDsGanttData(@RequestBody Map<String, Object> reqMap) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			String fType = StringUtil.checkNull((String) reqMap.get("fType"));
			DevelopmentStageCalendar dsc = (DevelopmentStageCalendar) CommonUtil.getObject(oid);
			List<DSData> dsData = DsHelper.manager.getDSDataList(dsc, fType);
			map.put("tasks", dsData);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return map;
	}
	
	@RequestMapping("/include_addObject")
	public ModelAndView addObject(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String type = StringUtil.checkReplaceStr((String) reqMap.get("type"), "multi");
		String title = StringUtil.checkNull((String) reqMap.get("title"));
		String objType = StringUtil.checkNull((String) reqMap.get("objType"));
		String pageName = StringUtil.checkNull((String) reqMap.get("pageName"));
		String autoGridHeight = StringUtil.checkReplaceStr((String) reqMap.get("autoGridHeight"), "false");
		String gridHeight = StringUtil.checkReplaceStr((String) reqMap.get("gridHeight"), "55");
		String toggle = StringUtil.checkReplaceStr((String) reqMap.get("toggle"), "true");
		String fType = StringUtil.checkNull((String) reqMap.get("fType"));
		String moduleType = StringUtil.checkNull((String) reqMap.get("moduleType"));
		
		if("single".equals(type)) {
			autoGridHeight = "true";
			gridHeight = "55";
		}
		
		model.addObject("type", type);
		model.addObject("oid", oid);
		model.addObject("title", title);
		model.addObject("objType", objType);
		model.addObject("pageName", pageName);
		model.addObject("autoGridHeight", autoGridHeight);
		model.addObject("gridHeight", gridHeight);
		model.addObject("moduleType", moduleType);
		model.addObject("toggle", toggle);
		model.addObject("fType", fType);
		
		model.setViewName("include:/calendar/include/addObject");
		
		return model;
	}
	
	//view only
	@RequestMapping("/include_dsList")
	public ModelAndView include_dsList(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String title = StringUtil.checkNull((String) reqMap.get("title"));
		
		model.addObject("oid", oid);
		model.addObject("title", title);
		model.setViewName("include:/calendar/include/dsList");
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/getDsList")
	public Map<String,Object> getDsList(@RequestBody Map<String, Object> reqMap) {
		Map<String,Object> map = new HashMap<>();
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			String fType = StringUtil.checkNull((String) reqMap.get("fType"));
			DevelopmentStageCalendar dsc = (DevelopmentStageCalendar) CommonUtil.getObject(oid);
			List<DSData> list = DsHelper.manager.getDSDataList(dsc, fType);
			map.put("list", list);
		} catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	@RequestMapping("/modifyDs")
	public ModelAndView modifyDs(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		DevelopmentStageCalendar dsc = (DevelopmentStageCalendar) CommonUtil.getObject(oid);
		DSCData data = new DSCData(dsc);
		model.addObject("data", data);
		model.setViewName("popup:/calendar/modifyDs");
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/modifyDsAction")
	public Map<String, Object> modifyDsAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
//			String name = StringUtil.checkNull((String) reqMap.get("name"));
//			boolean overLap = DsHelper.manager.checkOverlapByName(name);
//			if(overLap) {
//				map.put("result", false);
//				map.put("msg", "대일정 이름이 이미 존재합니다.");
//				return map;
//			}
			DsHelper.service.modifyDsAction(reqMap);
			
			map.put("result", true);
			map.put("msg", "수정되었습니다.");
			map.put("redirectUrl", "close");
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/deleteDsAction")
	public Map<String, Object> deleteDsAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			DsHelper.service.deleteDsAction(reqMap);
			
			map.put("result", true);
			map.put("msg", "삭제되었습니다.");
			map.put("redirectUrl", "close");
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/createLinkPopup")
	public ModelAndView createLinkPopup(@RequestParam Map<String, Object> reqMap) {

		ModelAndView model = new ModelAndView();
		try {
			String poid = StringUtil.checkNull((String) reqMap.get("oid"));
			model.addObject("poid", poid);
			model.addObject("type", "single");
			model.setViewName("popup:/calendar/createLinkPopup");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/createLinkAction")
	public Map<String, Object> createLinkAction(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			DsHelper.service.createLinkAction(reqMap);
			
			map.put("result", true);
			map.put("msg", "등록하였습니다.");
			map.put("redirectUrl", "closeAndReload");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/modifyLinkPopup")
	public ModelAndView modifyLinkPopup(@RequestParam Map<String, Object> reqMap) {

		ModelAndView model = new ModelAndView();
		try {
			String poid = StringUtil.checkNull((String) reqMap.get("oid"));
			model.addObject("poid", poid);
			model.addObject("type", "single");
			model.setViewName("popup:/calendar/modifyLinkPopup");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/modifyLinkAction")
	public Map<String, Object> modifyLinkAction(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			DsHelper.service.modifyLinkAction(reqMap);
			
			map.put("result", true);
			map.put("msg", "수정하였습니다.");
			map.put("redirectUrl", "closeAndReload");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/deleteLinkAction")
	public Map<String, Object> deleteLinkAction(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			DsHelper.service.deleteLinkAction(reqMap);
			
			map.put("result", true);
			map.put("msg", "삭제하였습니다.");
			map.put("redirectUrl", "closeAndReload");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
}
