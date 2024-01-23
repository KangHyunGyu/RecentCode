package com.e3ps.org.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.e3ps.common.bean.FolderData;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.service.CommonHelper;
import com.e3ps.common.util.FolderUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.org.Department;
import com.e3ps.org.bean.DepartmentData;
import com.e3ps.org.service.DepartmentHelper;

import wt.folder.Folder;

@Controller
@RequestMapping("/department")
public class DepartmentController {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.ORG.getName());
	
	/**
	 * @desc	: 부서 트리 화면
	 * @author	: shkim
	 * @date	: 2019. 6. 12.
	 * @method	: departmentTree
	 * @return	: ModelAndView
	 * @param reqMap
	 * @return
	 */
	@RequestMapping("/include_departmentTree")
	public ModelAndView include_departmentTree(@RequestParam Map<String, Object> reqMap) throws Exception{
		ModelAndView model = new ModelAndView();
		
		String autoGridHeight = StringUtil.checkReplaceStr((String) reqMap.get("autoGridHeight"), "false");
		String gridHeight = StringUtil.checkReplaceStr((String) reqMap.get("gridHeight"), "300");
		boolean isAdmin = StringUtil.booleanValue(reqMap.get("isAdmin"), false);
		
		String rootLocation = DepartmentHelper.manager.getDepartmentRootLocation();
		
		model.addObject("autoGridHeight", autoGridHeight);
		model.addObject("gridHeight", gridHeight);
		model.addObject("rootLocation", rootLocation);
		model.addObject("isAdmin", isAdmin);

		model.setViewName("include:/common/include/departmentTree");

		return model;
	}
	
	/**
	 * @desc	: 부서 트리 가져오기
	 * @author	: sangylee
	 * @date	: 2019. 6. 12.
	 * @method	: getDepartmentTree
	 * @return	: Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getDepartmentTree")
	public Map<String, Object> getDepartmentTree(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<>();
		
		String rootLocation = (String) reqMap.get("rootLocation");
		boolean isAdmin = StringUtil.booleanValue(reqMap.get("isAdmin"), false);
		
		try {
			if(rootLocation.length() > 0) {
				Department dept = DepartmentHelper.manager.getDepartment(rootLocation);
				
				if(dept != null) {
					List<DepartmentData> list = new ArrayList<>();
					
					list = DepartmentHelper.manager.getDepartmentTree(dept, list, isAdmin);
					
					map.put("list", list);
					map.put("result", true);
				} else {
					List<DepartmentData> list = new ArrayList<>();
					
					map.put("list", list);
					map.put("result", true);
				}
			}else {
				map.put("result", false);
				map.put("msg", "관리자에게 문의하세요.");
			}
		}catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}

	/**
	 * @desc	: 특정 부서 트리 자식 리스트 가져오기
	 * @author	: tsjeong
	 * @date	: 2020. 08. 26.
	 * @method	: getDepartmentChildrenList
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getDepartmentChildrenList")
	public static Map<String, Object> getDepartmentChildrenList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			String departmentCode = StringUtil.checkNull((String) reqMap.get("departmentCode"));
			Department dept = (Department)DepartmentHelper.manager.getDepartment(departmentCode);
			List<DepartmentData> list = DepartmentHelper.manager.getSubDepartmentList(dept);
			
			map.put("list", list);
			map.put("result",true);
		} catch (Exception e) {
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/createDepartment")
	public ModelAndView createDepartment(@RequestParam Map<String, Object> reqMap) throws Exception {
		ModelAndView model = new ModelAndView();
		
		String parentOid = StringUtil.checkNull((String) reqMap.get("parentOid")); 
		
		DepartmentData data = new DepartmentData(parentOid);
		
		model.addObject("parent", data);
		
		model.setViewName("popup:/admin/createDepartment");
		
		return model;
	}
	
	/**
	 * @desc	: 부서 등록 Action
	 * @author	: shkim
	 * @date	: 2019. 6. 12.
	 * @method	: createDepartmentAction
	 * @param   : reqMap
	 * @return	: Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/createDepartmentAction")
	public Map<String, Object> createDepartmentAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			
			boolean result = DepartmentHelper.service.createDepartmentAction(reqMap);
			
			if(result) {
				map.put("result", result);
				map.put("msg", "등록되었습니다.");
				map.put("redirectUrl", "closeAndReload");
			}else {
				map.put("result", result);
				map.put("msg", "부서 코드가 존재합니다.");
			}
		}catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 부서 정보 화면
	 * @author	: sangylee
	 * @date	: 2019. 7. 31.
	 * @method	: departmentManagement
	 * @param reqMap
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/viewDepartment")
	public ModelAndView departmentManagement(@RequestParam Map<String, Object> reqMap) throws Exception {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
		
		DepartmentData data = new DepartmentData(oid);
		
		model.addObject("department", data);
		
		model.setViewName("popup:/admin/viewDepartment");
		
		return model;
	}
	
	/**
	 * @desc	: 부서 수정 화면
	 * @author	: sangylee
	 * @date	: 2019. 7. 31.
	 * @method	: modifyDepartment
	 * @param reqMap
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/modifyDepartment")
	public ModelAndView modifyDepartment(@RequestParam Map<String, Object> reqMap) throws Exception {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
		
		DepartmentData data = new DepartmentData(oid);
		
		model.addObject("department", data);
		
		model.setViewName("popup:/admin/modifyDepartment");
		
		return model;
	}
	
	/**
	 * @desc	: 부서 수정 Action
	 * @author	: shkim
	 * @date	: 2019. 6. 12.
	 * @method	: modifyDepartmentAction
	 * @param   : reqMap
	 * @return	: Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/modifyDepartmentAction")
	public Map<String, Object> modifyDepartmentAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			
			boolean result = DepartmentHelper.service.modifyDepartmentAction(reqMap);
			
			if(result) {
				map.put("result", result);
				map.put("msg", "수정되었습니다.");
				map.put("redirectUrl", "closeAndReload");
			}else {
				map.put("result", result);
				map.put("msg", "부서 코드가 존재합니다.");
			}
		}catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 부서 삭제 Action
	 * @author	: shkim
	 * @date	: 2019. 6. 13.
	 * @method	: deleteDepartmentAction
	 * @return	: Map<String,Object>
	 * @param   : reqMap
	 * @return  : Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/deleteDepartmentAction")
	public Map<String, Object> deleteDepartmentAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			boolean isPopup = (boolean) reqMap.get("isPopup");
			boolean result = DepartmentHelper.service.deleteDepartmentAction(reqMap);
			
			if(result) {
				map.put("result", result);
				map.put("msg", "삭제되었습니다.");
				if(isPopup) {
					map.put("redirectUrl", "closeAndReload");
				}
			}else {
				map.put("result", result);
				map.put("msg", "하위 부서가 존재합니다.");
			}
		}catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR = " + e.getLocalizedMessage());
		}
		return map;
	}
}
