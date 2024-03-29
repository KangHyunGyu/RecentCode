package com.e3ps.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.e3ps.admin.bean.AuthorityGroupData;
import com.e3ps.admin.bean.EsolutionMenuData;
import com.e3ps.admin.bean.ObjectAuthGroupData;
import com.e3ps.admin.service.AdminHelper;
import com.e3ps.admin.util.AdminUtil;
import com.e3ps.change.EChangeActivityDefinition;
import com.e3ps.change.EChangeActivityDefinitionRoot;
import com.e3ps.change.beans.EChangeActivityDefinitionData;
import com.e3ps.change.service.ChangeECOHelper;
import com.e3ps.common.code.NumberCode;
import com.e3ps.common.code.bean.NumberCodeData;
import com.e3ps.common.code.service.CodeHelper;
import com.e3ps.common.history.bean.DownloadHistoryData;
import com.e3ps.common.history.bean.LoginHistoryData;
import com.e3ps.common.history.service.HistoryHelper;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.log4j.ObjectLogger;
import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.doc.DocCodeType;
import com.e3ps.doc.bean.DocCodeToValueDefinitionLinkData;
import com.e3ps.doc.bean.DocCodeTypeData;
import com.e3ps.doc.bean.DocValueDefinitionData;
import com.e3ps.org.Department;
import com.e3ps.org.bean.CompanyState;
import com.e3ps.org.bean.PeopleData;
import com.e3ps.org.service.PeopleHelper;
import com.e3ps.project.ControllerException;
import com.e3ps.project.OutputTypeStep;
import com.e3ps.project.beans.OutputTypeStepData;
import com.e3ps.project.service.OutputTypeHelper;

import wt.enterprise.RevisionControlled;
import wt.licenseusage.licensing.LicenseCacheManager;
import wt.org.WTUser;
import wt.session.SessionHelper;
import wt.util.WTException;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.ADMIN.getName());
	
	/* 부서 관리 */
	/**
	 * @desc : 부서관리 메인 화면
	 * @author : shkim
	 * @date : 2019. 5. 31.
	 * @method : departmentManagement
	 * @param : reqMap
	 * @return : ModelAndView
	 */
	@RequestMapping("/departmentManagement")
	public ModelAndView departmentManagement(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		model.setViewName("default:/admin/departmentManagement");
		return model;
	}

	/**
	 * @desc : 부서 트리 화면
	 * @author : shkim
	 * @date : 2019. 5. 31.
	 * @method : include_departmentTree
	 * @param : reqMap
	 * @return : ModelAndView
	 */
	@RequestMapping("/include_departmentTree")
	public ModelAndView include_departmentTree(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		model.setViewName("include:/admin/include/departmentTree");
		return model;
	}

	/**
	 * @desc : 사용자 검색 화면
	 * @author : shkim
	 * @date : 2019. 5. 31.
	 * @method : include_searchUser
	 * @param : reqMap
	 * @return : ModelAndView
	 */
	@RequestMapping("/include_searchUser")
	public ModelAndView include_searchUser(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		model.setViewName("include:/admin/include/searchUser");
		return model;
	}

	/**
	 * @desc : 사용자 검색 Action
	 * @author : shkim
	 * @date : 2019. 5. 31.
	 * @method : searchUserAction
	 * @param : reqMap
	 * @return : Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/searchUserAction")
	public Map<String, Object> searchUserAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<PeopleData> list = PeopleHelper.manager.getLicenseAllUserList(reqMap);
			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
	
	
	/**
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/searchUserActionAdmin")
	public Map<String, Object> searchUserActionAdmin(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<PeopleData> list = PeopleHelper.manager.getUserListAction(reqMap);
			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
	
	

	/**
	 * @desc : 사용자 부서 설정 화면 (Popup)
	 * @author : shkim
	 * @date : 2019. 6. 13.
	 * @method : viewDepartmentPopup
	 * @return : ModelAndView
	 * @param : departmentOid
	 */
	@RequestMapping("/viewSetDepartmentPopup")
	public ModelAndView viewSetDepartmentPopup(@RequestParam("departmentOid") String departmentOid) {

		ModelAndView model = new ModelAndView();
		model.addObject("departmentOid", departmentOid);
		model.addObject("departmentName", ((Department) CommonUtil.getObject(departmentOid)).getName());

		model.setViewName("popup:/admin/viewSetDepartmentPopup");
		return model;
	}

	/**
	 * @desc : 사용자 부서 설정 Action
	 * @author : shkim
	 * @date : 2019. 6. 13.
	 * @method : setDeptAction
	 * @return : boolean
	 * @param : reqmap
	 */
	@ResponseBody
	@RequestMapping("/setDepartmentAction")
	public Map<String, Object> setDepartmentAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			PeopleHelper.service.setDepartmentAction(reqMap);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	/**
	 * @desc : 부서장 설정 Action
	 * @author : shkim
	 * @date : 2019. 6. 3.
	 * @method : onChiefAction
	 * @param : userOid
	 * @return : boolean
	 */
	@ResponseBody
	@RequestMapping("/setChiefAction")
	public Map<String, Object> setChiefAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			boolean checked = (boolean) reqMap.get("checked");
			PeopleHelper.service.setChiefAction(reqMap);
			map.put("result", true);
			if (checked) {
				map.put("msg", MessageUtil.getMessage("부서장 설정이 완료 되었습니다."));
			} else {
				map.put("msg", MessageUtil.getMessage("부서장 해제가 완료 되었습니다."));
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	/**
	 * @desc : 직급 명 리스트 가져오기
	 * @author : shkim
	 * @date : 2019. 6. 17.
	 * @method : getDutyNameList
	 * @return : List<String>
	 */
	@ResponseBody
	@RequestMapping("/getDutyNameList")
	public Map<String, Object> getDutyNameList() {
		Map<String, Object> map = new HashMap<>();
		List<String> list = CompanyState.dutyNameList;
		map.put("list", list);
		return map;
	}

	/**
	 * @desc : 직급 설정 Action
	 * @author : shkim
	 * @date : 2019. 6. 3.
	 * @method : setDutyAction
	 * @param : reqMap
	 * @return : boolean
	 */
	@ResponseBody
	@RequestMapping("/setDutyAction")
	public Map<String, Object> setDutyAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			PeopleHelper.service.setDutyAction(reqMap);
			map.put("result", true);
			map.put("msg", MessageUtil.getMessage("직급 설정이 완료 되었습니다."));
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	/**
	 * @desc : 선택한 부서에 해당하는 사용자 검색 Action
	 * @author : shkim
	 * @date : 2019. 6. 3.
	 * @method : searchDeptUserListAction
	 * @param : oid
	 * @return : Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/searchDeptUserListAction")
	public Map<String, Object> searchDeptUserListAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<PeopleData> list = PeopleHelper.manager.getDeptUserListAction(reqMap);
			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	/**
	 * @desc : 선택한 부서에 해당하지 않는 사용자 검색 Action
	 * @author : shkim
	 * @date : 2019. 6. 14.
	 * @method : searchNonDeptUserListAction
	 * @return : Map<String,Object>
	 * @param : reqMap
	 */
	@ResponseBody
	@RequestMapping("/searchNonDeptUserListAction")
	public Map<String, Object> searchNonDeptUserListAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<PeopleData> list = PeopleHelper.manager.getNonDeptUserListAction(reqMap);
			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	/* 문서 타입 관리 */
	/**
	 * @desc : 문서 타입 관리 화면
	 * @author : tsjeong
	 * @date : 2019. 9. 6.
	 * @method : docTypeManagement
	 * @return : ModelAndView
	 * @param reqMap
	 * @return
	 */
	@RequestMapping("/docTypeManagement")
	public ModelAndView docTypeManagement(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		model.setViewName("default:/admin/docTypeManagement");

		return model;
	}

	/**
	 * @desc : 문서 분류별 속성, 문서 속성 화면
	 * @author : tsjeong
	 * @date : 2019. 9. 10.
	 * @method : docTypeDefinitionList
	 * @return : ModelAndView
	 * @param reqMap
	 * @return
	 */
	@RequestMapping("/include_docTypeDefinitionList")
	public ModelAndView docTypeDefinitionList(@RequestParam Map<String, Object> reqMap) {

		ModelAndView model = new ModelAndView();

		String rootLocation = StringUtil.checkNull((String) reqMap.get("code"));
		if (rootLocation == null) {
			rootLocation = "ROOT";
		}

		model.addObject("rootLocation", rootLocation);

		model.setViewName("include:/admin/include/docTypeDefinitionList");

		return model;
	}

	/**
	 * @desc : 문서코드 리스트 가져오기
	 * @author : tsjeong
	 * @date : 2019. 9. 10.
	 * @method : getDocCodeList
	 * @return : Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getDocCodeList")
	public Map<String, Object> getDocCodeList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<DocCodeTypeData> list = AdminHelper.manager.getDocCodeList(reqMap);

			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	/**
	 * @desc : 문서 코드 저장
	 * @author : tsjeong
	 * @date : 2019. 9. 10.
	 * @method : saveDocCodeAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveDocCodeAction")
	public Map<String, Object> saveDocCodeAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			AdminHelper.service.saveDocCodeAction(reqMap);

			map.put("result", true);
			map.put("msg", MessageUtil.getMessage("저장이 완료되었습니다."));
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	/**
	 * @desc : 문서 코드 저장
	 * @author : tsjeong
	 * @date : 2019. 9. 10.
	 * @method : saveDocCodeAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveDocCodeAction2")
	public Map<String, Object> saveDocCodeAction2(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			AdminHelper.service.saveDocCodeAction2(reqMap);

			map.put("result", true);
			map.put("msg", MessageUtil.getMessage("저장이 완료되었습니다."));
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	/**
	 * @desc : 문서 속성 검색
	 * @author : tsjeong
	 * @date : 2019. 9. 10.
	 * @method : searchDocValueAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/searchDocValueAction")
	public Map<String, Object> searchDocValueAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<DocValueDefinitionData> list = AdminHelper.manager.getSearchDocValueListAction(reqMap);
			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	/**
	 * @desc : 문서 분류별 속성 가져오기
	 * @author : tsjeong
	 * @date : 2019. 9. 10.
	 * @method : searchDocCodeToValueAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/searchDocCodeToValueAction")
	public Map<String, Object> searchDocCodeToValueAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		String docTypeOid = StringUtil.checkNull((String) reqMap.get("docTypeOid"));

		DocCodeType docCode = null;
		if (docTypeOid.length() > 0) {
			docCode = (DocCodeType) CommonUtil.getObject(docTypeOid);
			if ("ROOT".equals(docCode.getCode())) {
				docCode = null;
			}
		}

		try {
			List<DocCodeToValueDefinitionLinkData> list = AdminHelper.manager.getDocValueDefinition(docCode);
			map.put("docTypeOid", docTypeOid);
			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	/**
	 * @desc : DocCodeToValueDefinitionLink - 문서분류별속성 리스트 저장
	 * @author : tsjeong
	 * @date : 2019. 9. 25.
	 * @method : saveDocCodeToValueAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveDocCodeToValueAction")
	public Map<String, Object> saveDocCodeToValueAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		String docTypeOid = (String) reqMap.get("docTypeOid");

		try {
			AdminHelper.service.saveDocCodeToValueAction(reqMap, docTypeOid);
			map.put("docTypeOid", docTypeOid);
			map.put("result", true);
			map.put("msg", MessageUtil.getMessage("저장이 완료되었습니다."));
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	/**
	 * @desc : saveDocValueDefinitionAction
	 * @author : sangylee
	 * @date : 2020. 4. 28.
	 * @method : saveDocValueDefinitionAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveDocValueDefinitionAction")
	public Map<String, Object> saveDocValueDefinitionAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			AdminHelper.service.saveDocValueDefinitionAction(reqMap);
			map.put("result", true);
			map.put("msg", MessageUtil.getMessage("저장이 완료되었습니다."));
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	/* 코드 관리 */
	/**
	 * @desc : 코드 관리 화면
	 * @author : sangylee
	 * @date : 2019. 7. 30.
	 * @method : numberCodeManagement
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/numberCodeManagement")
	public ModelAndView numberCodeManagement(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		model.setViewName("default:/admin/numberCodeManagement");

		return model;
	}

	/**
	 * @desc : 넘버코드 리스트 가져오기
	 * @author : sangylee
	 * @date : 2019. 7. 30.
	 * @method : getNumberCodeList
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getNumberCodeList")
	public Map<String, Object> getNumberCodeList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<NumberCodeData> list = CodeHelper.manager.getNumberCodeList(reqMap);

			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	/**
	 * @desc : 코드타입 트리
	 * @author : sangylee
	 * @date : 2019. 7. 30.
	 * @method : codeTypeTree
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_codeTypeTree")
	public ModelAndView codeTypeTree(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		String autoGridHeight = StringUtil.checkReplaceStr((String) reqMap.get("autoGridHeight"), "false");
		String gridHeight = StringUtil.checkReplaceStr((String) reqMap.get("gridHeight"), "200");

		model.addObject("autoGridHeight", autoGridHeight);
		model.addObject("gridHeight", gridHeight);

		model.setViewName("include:/common/include/codeTypeTree");

		return model;
	}

	/**
	 * @desc : 코드 타입 하위 리스트
	 * @author : sangylee
	 * @date : 2019. 7. 30.
	 * @method : getCodeTypeTree
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getCodeTypeTree")
	public Map<String, Object> getCodeTypeTree(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {

			List<Map<String, Object>> list = CodeHelper.manager.getCodeTypeTree(reqMap);

			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	/**
	 * @desc : 코드 타입 리스트 액션
	 * @author : tsjeong
	 * @date : 2020. 09. 11.
	 * @method : getCodeTypeTree2
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getCodeTypeTree2")
	public Map<String, Object> getCodeTypeTree2(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {

			List<NumberCodeData> list = CodeHelper.manager.getCodeTypeTree2(reqMap);

			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	/**
	 * @desc : 특정 numbercode 트리 자식 리스트 가져오기
	 * @author : tsjeong
	 * @date : 2020. 9. 11.
	 * @method : getCodeTypeChildrenList
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getCodeTypeChildrenList")
	public static Map<String, Object> getCodeTypeChildrenList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			String parentOid = StringUtil.checkNull((String) reqMap.get("parentOid"));
			String codeType = StringUtil.checkNull((String) reqMap.get("codeType"));
			String code = StringUtil.checkNull((String) reqMap.get("code"));

			NumberCode numbercode = null;
			if (code != null) {
				numbercode = CodeHelper.manager.getNumberCode(codeType, code);
			}
			List<NumberCodeData> list = CodeHelper.manager.getSubNumbercodeList(numbercode, codeType);

			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}

		return map;
	}

	@ResponseBody
	@RequestMapping("/saveNumberCodeAction")
	public Map<String, Object> saveNumberCodeAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			CodeHelper.service.saveNumberCodeAction(reqMap);

			map.put("result", true);
			map.put("msg", "저장이 완료되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	@ResponseBody
	@RequestMapping("/saveNumberCodeAction2")
	public Map<String, Object> saveNumberCodeAction2(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			CodeHelper.service.saveNumberCodeAction2(reqMap);

			map.put("result", true);
			map.put("msg", "저장이 완료되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	/* 설계변경 활동 관리 */

	/* 다운로드 관리 */
	@RequestMapping("/downloadHistory")
	public ModelAndView downloadHistory(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		model.setViewName("default:/admin/downloadHistory");

		return model;
	}

	/**
	 * @desc : 다운로드 이력 리스트
	 * @author : sangylee
	 * @date : 2019. 7. 23.
	 * @method : getDownloadHistory
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getDownloadHistory")
	public Map<String, Object> getDownloadHistory(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<DownloadHistoryData> list = HistoryHelper.manager.getDownloadHistory(reqMap);
			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	/**
	 * @desc : 다운로드 검색 스크롤 Action
	 * @author : tsjeong
	 * @date : 2019. 12. 2.
	 * @method : searchDownloadHistoryScrollAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/searchDownloadHistoryScrollAction")
	public Map<String, Object> searchDownloadHistoryScrollAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {

			map = HistoryHelper.manager.getDownloadHistoryScrollList(reqMap);

			map.put("result", true);

		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}

		return map;
	}

	/**
	 * @desc : 다운로드 검색 스크롤 Action
	 * @author : tsjeong
	 * @date : 2020. 09. 07.
	 * @method : searchDownloadHistoryScrollAction2
	 * @return : Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/searchDownloadHistoryScrollAction2")
	public Map<String, Object> searchDownloadHistoryScrollAction2(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {

			map = HistoryHelper.manager.getDownloadHistoryScrollList2(reqMap);

			map.put("result", true);

		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}

		return map;
	}

	/* 접속 이력 */
	/**
	 * @desc : 로그인 이력 화면
	 * @author : sangylee
	 * @date : 2019. 7. 22.
	 * @method : loginHistory
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/loginHistory")
	public ModelAndView loginHistory(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		model.setViewName("default:/admin/loginHistory");

		return model;
	}

	/**
	 * @desc : 로그인 이력 리스트
	 * @author : sangylee
	 * @date : 2019. 7. 23.
	 * @method : getLoginHistory
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getLoginHistory")
	public Map<String, Object> getLoginHistory(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<LoginHistoryData> list = HistoryHelper.manager.getLoginHistory(reqMap);
			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	@ResponseBody
	@RequestMapping("/searchLoginHistoryScrollAction")
	public Map<String, Object> searchLoginHistoryScrollAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {

			map = HistoryHelper.manager.getLoginHistoryScrollList(reqMap);

			map.put("result", true);

		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}

		return map;
	}

	@ResponseBody
	@RequestMapping("/searchLoginHistoryScrollAction2")
	public Map<String, Object> searchLoginHistoryScrollAction2(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			map = HistoryHelper.manager.getLoginHistoryScrollList2(reqMap);

			map.put("result", true);

		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}

		return map;
	}

//	/**
//	 * @desc : PLM 사용자 -> CPC 인터페이스
//	 * @author : mnyu
//	 * @date : 2019. 9. 27.
//	 * @method : sendPLMUserToCPC
//	 * @param reqMap
//	 * @return Map<String, Object>
//	 */
//	@ResponseBody
//	@RequestMapping("/sendPLMUserToCPC")
//	public Map<String, Object> sendPLMUserToCPC(@RequestBody Map<String, Object> reqMap) {
//		Map<String, Object> map = new HashMap<>();
//
//		try {
//			List<PeopleData> list = PeopleHelper.manager.getUserList(reqMap);
//
//			StandardCPCService service = new StandardCPCService();
//			service.sendPLMUserToCPC(null, list);
//
//			map.put("result", true);
//			map.put("msg", "전송이 완료되었습니다.");
//		} catch (Exception e) {
//			e.printStackTrace();
//			map.put("result", false);
//			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
//		}
//		return map;
//	}

	/* 공용 ID */
	/**
	 * @desc : 공용 ID 화면
	 * @author : tsjeong
	 * @date : 2019. 11. 15.
	 * @method : publicIdManagement
	 * @param : reqMap
	 * @return : ModelAndView
	 */
	@RequestMapping("/publicIdManagement")
	public ModelAndView publicIdManagement(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		model.setViewName("default:/admin/publicIdManagement");
		return model;
	}

	/**
	 * @desc : 모듈 종류 option 가져오기
	 * @author : tsjeong
	 * @date : 2019. 11. 15.
	 * @method : getModuleList
	 * @return : Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getModuleList")
	public Map<String, Object> getModuleList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			List<Map<String, String>> list = AdminUtil.getModuleTypeList();

			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}

		return map;
	}

	/**
	 * @desc : 객체 상태 변경 화면
	 * @author : sangylee
	 * @date : 2020. 2. 14.
	 * @method : changeObjectState
	 * @return : ModelAndView
	 * @param reqMap
	 */
	@RequestMapping("/changeObjectState")
	public ModelAndView changeObjectState(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		model.setViewName("default:/admin/changeObjectState");
		return model;
	}

	/**
	 * @desc : Object 정보 가져오기
	 * @author : sangylee
	 * @date : 2020. 2. 14.
	 * @method : searchObjectAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/searchObjectAction")
	public Map<String, Object> searchObjectAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			Map<String, Object> result = AdminHelper.manager.searchObjectAction(reqMap);

			if (result.get("command") != null && "error".equals(((String) result.get("command")))) {
				map.put("msg", result.get("message"));
			}

			map.put("data", result);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}

		return map;
	}

	@ResponseBody
	@RequestMapping("/reassignObjectAction")
	public Map<String, Object> reassignObjectAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			Map<String, Object> result = AdminHelper.service.reassignObjectAction(reqMap);

			map.put("data", result);
			map.put("msg", result.get("message"));
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}

		return map;
	}

	@ResponseBody
	@RequestMapping("/changeStateAction")
	public Map<String, Object> changeStateAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			Map<String, Object> result = AdminHelper.service.changeStateAction(reqMap);

			map.put("data", result);
			map.put("msg", result.get("message"));
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}

		return map;
	}

	@RequestMapping("/updateEChangeActivityDefinition")
	public ModelAndView updateEChangeActivityDefinition(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		model.setViewName("default:/admin/updateEChangeActivityDefinition");

		return model;
	}

	/**
	 * @desc : 설계변경 활동 Root 생성 화면
	 * @author : tsjeong
	 * @date : 2020. 9. 16.
	 * @method : createRootDefinition
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/createRootDefinition")
	public ModelAndView folderTree(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		String oid = StringUtil.checkNull((String) reqMap.get("oid"));

		model.addObject("oid", oid);

		model.setViewName("popup:/admin/createRootDefinition");

		return model;
	}

	/**
	 * @desc : 설변 활동 Root 생성
	 * @author : tsjeong
	 * @date : 2020. 9. 16.
	 * @method : createRootDefinitionAction
	 * @param reqMap
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/createRootDefinitionAction")
	public Map<String, Object> createRootDefinitionAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			EChangeActivityDefinitionRoot root = ChangeECOHelper.service.createRootDefinition(reqMap);
			map.put("callbackName", "opener.selectRoot");
			map.put("callbackArg", "'" + CommonUtil.getOIDString(root) + "'");
			map.put("msg", "등록이 완료되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", "close");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}

		return map;
	}

	/**
	 * @desc : 설계변경 활동 Root 수정
	 * @author : tsjeong
	 * @date : 2020. 9. 16.
	 * @method : folderTree
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/updateRootDefinition")
	public ModelAndView updateRootDefinition(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		String oid = StringUtil.checkNull((String) reqMap.get("oid"));

		EChangeActivityDefinitionRoot root = null;
		if (oid.length() > 0) {
			root = (EChangeActivityDefinitionRoot) CommonUtil.getObject(oid);
		}

		model.addObject("oid", oid);
		model.addObject("name", root.getName());
		model.addObject("engName", root.getName_eng() == null ? "" : root.getName_eng());
		model.addObject("sort", root.getSortNumber());
		model.addObject("description", root.getDescription() == null ? "" : root.getDescription());

		model.setViewName("popup:/admin/updateRootDefinition");

		return model;
	}

	/**
	 * @desc : 설변 활동 Root 수정
	 * @author : tsjeong
	 * @date : 2020. 9. 17.
	 * @method : updateRootDefinitionAction
	 * @param reqMap
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/updateRootDefinitionAction")
	public Map<String, Object> updateRootDefinitionAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			EChangeActivityDefinitionRoot root = ChangeECOHelper.service.updateRootDefinition(reqMap);
			map.put("callbackName", "opener.selectRoot");
			map.put("callbackArg", "'" + CommonUtil.getOIDString(root) + "'");
			map.put("msg", "수정이 완료되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", "close");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}

		return map;
	}

	/**
	 * @desc : 설변 활동 Root 삭제
	 * @author : tsjeong
	 * @date : 2020. 9. 17.
	 * @method : deleteRootDefinitionAction
	 * @param reqMap
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/deleteRootDefinitionAction")
	public Map<String, Object> deleteRootDefinitionAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			ChangeECOHelper.service.deleteRootDefinition(reqMap);
			map.put("msg", "삭제가 완료되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/admin/updateEChangeActivityDefinition"));
		} catch (WTException ex) {
			throw new ControllerException(ex);
		}

		return map;
	}

	/**
	 * @desc : 활동 추가
	 * @author : tsjeong
	 * @date : 2020. 9. 16.
	 * @method : createDefinition
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/createDefinition")
	public ModelAndView createDefinition(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		String rootOid = StringUtil.checkNull((String) reqMap.get("root"));

		model.addObject("rootOid", rootOid);

		model.setViewName("popup:/admin/createDefinition");

		return model;
	}

	/**
	 * @desc : 활동 추가
	 * @author : tsjeong
	 * @date : 2020. 9. 17.
	 * @method : createDefinitionAction
	 * @param reqMap
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/createDefinitionAction")
	public Map<String, Object> createDefinitionAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			EChangeActivityDefinition def = ChangeECOHelper.service.createDefinition(reqMap);
			map.put("callbackName", "opener.selectRoot");
			map.put("callbackArg", "'" + CommonUtil.getOIDString(def.getRoot()) + "'");
			map.put("msg", "활동이 추가되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", "close");
		} catch (WTException ex) {
			throw new ControllerException(ex);
		}

		return map;
	}

	/**
	 * @desc : 설계변경 활동 수정
	 * @author : tsjeong
	 * @date : 2020. 9. 16.
	 * @method : updateDefinition
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/updateDefinition")
	public ModelAndView updateDefinition(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		EChangeActivityDefinition def = (EChangeActivityDefinition) CommonUtil.getObject(oid);

		String rootOid = StringUtil.checkNull((String) reqMap.get("root"));
		
		model.addObject("rootOid", rootOid);
		model.addObject("oid", oid);
		model.addObject("name", def.getName());
		model.addObject("name_eng", def.getName_eng());
		model.addObject("step", def.getStep());
		model.addObject("activeType", def.getActiveType());
		model.addObject("sortNumber", def.getSortNumber());
		model.addObject("description", def.getDescription() == null ? "" : def.getDescription());

		model.setViewName("popup:/admin/updateDefinition");

		return model;
	}

	/**
	 * @desc : 활동 수정
	 * @author : tsjeong
	 * @date : 2020. 9. 17.
	 * @method : updateDefinitionAction
	 * @param reqMap
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/updateDefinitionAction")
	public Map<String, Object> updateDefinitionAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			EChangeActivityDefinition def = ChangeECOHelper.service.updateDefinition(reqMap);
			map.put("callbackName", "opener.selectRoot");
			map.put("callbackArg", "'" + CommonUtil.getOIDString(def.getRoot()) + "'");
			map.put("msg", "수정되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", "close");
		} catch (WTException ex) {
			throw new ControllerException(ex);
		}

		return map;
	}

	/**
	 * @desc : 활동 삭제
	 * @author : tsjeong
	 * @date : 2020. 9. 14.
	 * @method : deleteDefinitionAction2
	 * @return : Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteDefinitionAction")
	public Map<String, Object> deleteDefinitionAction2(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			
			ChangeECOHelper.service.deleteDefinition(reqMap);

			map.put("result", true);
			map.put("msg", "삭제되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	/**
	 * @desc : 프로젝트 단계
	 * @author : tsjeong
	 * @date : 2020. 9. 17.
	 * @method : projectOutput
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/projectOutput")
	public ModelAndView projectOutput(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		model.setViewName("default:/admin/projectOutput");

		return model;
	}

	/**
	 * @desc : 설계변경 활동 단계 리스트 가져오기
	 * @author : tsjeong
	 * @date : 2020. 9. 14.
	 * @method : getActivityStepList
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getActivityStepList")
	public static Map<String, Object> getActivityStepList(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<>();

		try {
			List<EChangeActivityDefinitionData> list = AdminHelper.manager.getActivityStep(reqMap);

			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}

		return map;
	}

	/**
	 * @desc : PSOTree 하위 리스트
	 * @author : tsjeong
	 * @date : 2020. 9. 15.
	 * @method : getPSOTree
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getPSOTree")
	public Map<String, Object> getPSOTree(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {

			List<Map<String, Object>> list = OutputTypeHelper.manager.getPSOTree(reqMap);

			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	/**
	 * @desc : 특정 PSOTree 트리 자식 리스트 가져오기
	 * @author : tsjeong
	 * @date : 2020. 9. 15.
	 * @method : getPSOTreeChildrenList
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getPSOTreeChildrenList")
	public static Map<String, Object> getPSOTreeChildrenList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			String parentOid = StringUtil.checkNull((String) reqMap.get("parentOid"));
			String codeType = StringUtil.checkNull((String) reqMap.get("codeType"));
			String code = StringUtil.checkNull((String) reqMap.get("code"));

			OutputTypeStep outputStep = null;
			if (code != null) {
				outputStep = OutputTypeHelper.manager.getOutputTypeStep(codeType, code);
			}
			List<OutputTypeStepData> list = OutputTypeHelper.manager.getOutputTypeStepList(outputStep, codeType);
			// getSubNumbercodeList
			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}

		return map;
	}

	/**
	 * @desc : PSOCode 리스트 가져오기
	 * @author : tsjeong
	 * @date : 2020. 9. 15.
	 * @method : getPSOCodeList
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getPSOCodeList")
	public Map<String, Object> getPSOCodeList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<OutputTypeStepData> list = OutputTypeHelper.manager.getPSOCodeList(reqMap);

			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	/**
	 * @desc : PSOCode 저장
	 * @author : tsjeong
	 * @date : 2020. 9. 15.
	 * @method : savePSOCodeAction
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/savePSOCodeAction")
	public Map<String, Object> savePSOCodeAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			OutputTypeHelper.service.savePSOCodeAction(reqMap);

			map.put("result", true);
			map.put("msg", "저장이 완료되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	/**
	 * 
	 * @desc : 설계변경 활동 단계 root 리스트
	 * @author : tsjeong
	 * @date : 2020. 9. 15.
	 * @method : getRootList
	 * @return : Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getRootList")
	public Map<String, Object> getRootList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			String changeType = StringUtil.checkNull((String) reqMap.get("changeType"));
			List<Map<String, String>> list = AdminUtil.getRoot(changeType);

			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}

		return map;
	}
	
	@RequestMapping("/stageGateManagement")
	public ModelAndView stageGateManagement(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		model.setViewName("default:/admin/stageGateManagement");
		return model;
	}
	
	@RequestMapping("/erpHistory")
	public ModelAndView erpHistory(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		model.setViewName("default:/admin/erpHistory");
		return model;
	}
	
	/**
	 * @desc : ERPHistory 검색 스크롤 Action
	 * @author : gs
	 * @date : 2020. 12. 03.
	 * @method : searchDownloadHistoryScrollAction2
	 * @return : Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/searchErpHistoryAction")
	public Map<String, Object> searchErpHistoryAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {

			map = HistoryHelper.manager.getErpHistory(reqMap);

			map.put("result", true);

		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}

		return map;
	}
	
	@ResponseBody
	@RequestMapping("/getSGCodeList")
	public Map<String, Object> getSGCodeList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<NumberCodeData> list = CodeHelper.manager.getSGCodeList(reqMap);

			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
	
	@RequestMapping("/sgCodeManagement")
	public ModelAndView sgCodeManagement(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		model.setViewName("default:/admin/sgCodeManagement");

		return model;
	}
	
	@RequestMapping("/include_sgCodeTypeTree")
	public ModelAndView sgCodeTypeTree(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		String autoGridHeight = StringUtil.checkReplaceStr((String) reqMap.get("autoGridHeight"), "false");
		String gridHeight = StringUtil.checkReplaceStr((String) reqMap.get("gridHeight"), "200");

		model.addObject("autoGridHeight", autoGridHeight);
		model.addObject("gridHeight", gridHeight);

		model.setViewName("include:/common/include/sgCodeTypeTree");

		return model;
	}
	
	@ResponseBody
	@RequestMapping("/getSgCodeTypeTree")
	public Map<String, Object> getSgCodeTypeTree(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {

			List<Map<String, Object>> list = CodeHelper.manager.getSgCodeTypeTree(reqMap);

			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
	
	/**
	 * 
	  * @Method Name : searchAclUserPopup
	  * @작성일 : 2021. 5. 20.
	  * @작성자 : mjroh
	  * @Method 설명 : 권한 설정 유저 추가 팝업
	  * @param reqMap
	  * @return
	 */
	@RequestMapping("/searchAclUserPopup")
	public ModelAndView searchAclUserPopup(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String perOid = StringUtil.checkNull((String) reqMap.get("perOid"));
		
		model.addObject("perOid", perOid);
		model.setViewName("popup:/common/searchAclUserPopup");
		
		return model;
	}
	
	/**
	 * 
	  * @Method Name : masterAclList
	  * @작성일 : 2021. 5. 20.
	  * @작성자 : mjroh
	  * @Method 설명 : 권한 설정
	  * @param reqMap
	  * @return
	 */
	@RequestMapping("/include_masterAclList")
	public ModelAndView docAclList(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			
			boolean isAdmin = CommonUtil.isAdmin();
			RevisionControlled per = (RevisionControlled)CommonUtil.getObject(oid);
			WTUser user = (WTUser) SessionHelper.manager.getPrincipal();
			WTUser crUser = (WTUser) per.getCreator().getObject();
			
			PeopleData data = new PeopleData(user);
			boolean isCustomAdmin = AdminHelper.manager.isCustomAdmin(data.getOid());
			
			if(user.equals(crUser) || isCustomAdmin) {
				model.addObject("isAuth", true);
			}
			model.addObject("isAdmin", isAdmin);
			model.addObject("oid", oid);
			
			model.setViewName("include:/common/include/masterAclList");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	/**
	 * 
	  * @Method Name : searchMasterAclAction
	  * @작성일 : 2021. 5. 20.
	  * @작성자 : mjroh
	  * @Method 설명 : 권한 설정 리스트
	  * @param data
	  * @return
	 */
	@ResponseBody
    @RequestMapping("/searchMasterAclAction")
    public Map<String, Object> searchMasterAclAction(@RequestBody Map<String, Object> reqMap){
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			map = AdminHelper.manager.getSearchMasterAclList(reqMap);
		    
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
    }
	
	/**
	 * 
	  * @Method Name : addMasterAclAction
	  * @작성일 : 2021. 5. 20.
	  * @작성자 : mjroh
	  * @Method 설명 : 권한 설정 유저 추가 액션
	  * @param data
	  * @return
	 */
	@ResponseBody
    @RequestMapping("/addMasterAclAction")
    public Map<String, Object> addMasterAclAction(@RequestBody Map<String, Object> reqMap){
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			AdminHelper.service.addMasterAclAction(reqMap);
			
			map.put("result", true);
			map.put("msg", "등록이 완료되었습니다.");
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		return map;
    }
	
	/**
	 * 
	  * @Method Name : deleteMasterAclAction
	  * @작성일 : 2021. 5. 21.
	  * @작성자 : mjroh
	  * @Method 설명 : 권한 설정 유저 삭제 액션
	  * @param reqMap
	  * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteMasterAclAction")
	public Map<String, Object> deleteMasterAclAction(@RequestBody Map<String, Object> reqMap){
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			AdminHelper.service.deleteMasterAclAction(reqMap);
			
			map.put("result", true);
			map.put("msg", "삭제되었습니다.");
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		return map;
	}
	
	/**
	 * 
	  * @Method Name : getAclUserListAction
	  * @작성일 : 2021. 5. 20.
	  * @작성자 : mjroh
	  * @Method 설명 : 권한 유저 리스트 출력
	  * @param reqMap
	  * @return
	 */
	@ResponseBody
	@RequestMapping("/getAclUserListAction")
	public Map<String, Object> getAclUserListAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<PeopleData> list = AdminHelper.manager.getAclUserList(reqMap);
			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/setAuthToUser")
	public Map<String, Object> setAuthToUser(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			AdminHelper.service.setAuthToUser(reqMap);
			
			map.put("result", true);
			map.put("msg", "저장이 완료되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
	
	
	
	
	/**
	 * License getUser
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/getLicenseUser")
	public Map<String, Object> getLicenseUser(@RequestBody Map<String, Object> reqMap)throws Exception {
		Map<String, Object> map = new HashMap<>();
		try {
			String type = StringUtil.checkNull((String) reqMap.get("type"));
			System.out.println("### getLicenseUser : " + type);
			
			List<PeopleData> list = AdminHelper.manager.getLicenseUserList(type);
			
			LicenseCacheManager licenseCacheManager = new LicenseCacheManager();
			long toLiCount = licenseCacheManager.getLicenseFeatureTotalCount(type);
			//List<PeopleData> list = PeopleHelper.manager.getUserListAction(reqMap);
			
			map.put("toLiCount", toLiCount);
			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * Worldex License Setting
	 * cklee
	 * @param reqMap
	 * @return
	 */
	
	@ResponseBody
	@RequestMapping("/delLicenseUsers")
	public Map<String, Object> delLicenseUsers(@RequestBody Map<String, Object> reqMap){
		Map<String, Object> map = new HashMap<>();
		
		try {
			AdminHelper.service.delLicenseUser(reqMap);
			map.put("result", true);
		}catch(Exception e) {
			map.put("result", false);
			e.printStackTrace();
		}
		
		return map;
	}
	
	/**
	 * Worldex License Setting
	 * cklee
	 * @param reqMap
	 * @return
	 */
	
	@ResponseBody
	@RequestMapping("/setLicenseUsers")
	public Map<String, Object> setLicenseUsers(@RequestBody Map<String, Object> reqMap){
		Map<String, Object> map = new HashMap<>();
		
		try {
			AdminHelper.service.setLicenseUser(reqMap);
			map.put("result", true);
		}catch(Exception e) {
			map.put("result", false);
			e.printStackTrace();
		}
		
		return map;
	}
	
	/**
	 * @methodName : searchDocValueAction
	 * @author : hckim
	 * @date : 2021.09.13
	 * @return : Map<String,Object>
	 * @description : 문서 속성(NumberCode.class) 검색
	 */
	@ResponseBody
	@RequestMapping("/searchDocAttrAction")
	public Map<String, Object> searchDocAttrAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<NumberCodeData> list = AdminHelper.manager.getSearchDocAttrListAction(reqMap);
			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
	
	
	/**
	 * @desc : 권한 그룹 멤버 관리 화면
	 * @author : shjeong
	 * @date : 2023. 09. 14.
	 * @method : authorityGroupMemberManagement
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/authorityManagement")
	public ModelAndView authorityGroupMemberManagement(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		model.setViewName("default:/admin/authorityManagement");
		return model;
	}
	/**
	 * @desc : 권한 그룹 정책 관리 화면
	 * @author : shjeong
	 * @date : 2023. 09. 19.
	 * @method : authorityGroupPolicyManagement
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/authorityGroupPolicyManagement")
	public ModelAndView authorityGroupPolicyManagement(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		model.setViewName("default:/admin/authorityGroupPolicyManagement");
		return model;
	}
	
	/**
	 * @desc : 권한 트리 화면
	 * @author : shjeong
	 * @date : 2023. 09. 14.
	 * @method : include_authorityTree
	 * @param : reqMap
	 * @return : ModelAndView
	 */
	@RequestMapping("/include_authorityMenuTree")
	public ModelAndView include_authorityTree(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		String autoGridHeight = StringUtil.checkReplaceStr((String) reqMap.get("autoGridHeight"), "false");
		String gridHeight = StringUtil.checkReplaceStr((String) reqMap.get("gridHeight"), "200");

		model.addObject("autoGridHeight", autoGridHeight);
		model.addObject("gridHeight", gridHeight);
		model.setViewName("include:/admin/include/authorityMenuTree");
		
		return model;
	}
	
	/**
	 * @desc : 권한 트리 화면
	 * @author : shjeong
	 * @date : 2023. 09. 14.
	 * @method : include_authorityTree
	 * @param : reqMap
	 * @return : ModelAndView
	 */
	@RequestMapping("/include_authorityFolderTree")
	public ModelAndView include_authorityFolderTree(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		String autoGridHeight = StringUtil.checkReplaceStr((String) reqMap.get("autoGridHeight"), "false");
		String gridHeight = StringUtil.checkReplaceStr((String) reqMap.get("gridHeight"), "200");

		model.addObject("autoGridHeight", autoGridHeight);
		model.addObject("gridHeight", gridHeight);
		model.setViewName("include:/admin/include/authorityFolderTree");
		
		return model;
	}
	
	@RequestMapping("/include_authorityObjectTree")
	public ModelAndView include_authorityObjectTree(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		String autoGridHeight = StringUtil.checkReplaceStr((String) reqMap.get("autoGridHeight"), "false");
		String gridHeight = StringUtil.checkReplaceStr((String) reqMap.get("gridHeight"), "200");

		model.addObject("autoGridHeight", autoGridHeight);
		model.addObject("gridHeight", gridHeight);
		model.setViewName("include:/admin/include/authorityObjectTree");
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/getAuthList")
	public Map<String, Object> getAuthList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			List<Map<String, String>> list = AdminHelper.manager.getAuthList();
			
			map.put("list", list);
			map.put("result", true);
		} catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/getAuthObjectList")
	public Map<String, Object> getAuthObjectList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			List<Map<String, String>> list = AdminHelper.manager.getAuthObjectList();
			
			map.put("list", list);
			map.put("result", true);
		} catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/getAuthorityGroupTypeList")
	public Map<String, Object> getAuthorityGroupTypeList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			List<Map<String, String>> list = AdminHelper.manager.getAuthorityGroupTypeList();
			
			map.put("list", list);
			map.put("result", true);
		} catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/getAuthorityObjectTypeList")
	public Map<String, Object> getAuthorityObjectTypeList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			List<Map<String, String>> list = AdminHelper.manager.getAuthorityObjectTypeList();
			
			map.put("list", list);
			map.put("result", true);
		} catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/getDomainList")
	public Map<String, Object> getDomainList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			Map<String,List<Map<String,String>>> resultMap = AdminHelper.manager.getDomainMap();
			
			map.put("list", resultMap);
			map.put("result", true);
		} catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	
	/**
	 * @desc : 권한 그룹 추가
	 * @author : shjeong
	 * @date : 2023. 09. 14.
	 * @method : createAuthorityGroupAction
	 * @param reqMap
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/createAuthorityGroupAction")
	public Map<String, Object> createAuthorityGroupAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			
			AdminHelper.service.createAuthorityGroup(reqMap);
			
			map.put("result", true);
			map.put("msg", "권한 그룹이 추가되었습니다.");
			map.put("redirectUrl", "close");
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR : " + e.getLocalizedMessage());
		}

		return map;
	}
	
	/**
	 * @desc : 권한 그룹 리스트 가져오기
	 * @author : shjeong
	 * @date : 2023. 09. 14.
	 * @method : getAuthorityGroupList
	 * @param reqMap
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/getAuthorityGroupList")
	public Map<String, Object> getAuthorityGroupList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			
			List<AuthorityGroupData> list = AdminHelper.manager.getAuthorityGroupList(reqMap);
			
			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg",  e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}

		return map;
	}
	
	/**
	 * @desc : 그룹 추가 팝업 화면 (Popup)
	 * @author : shjeong
	 * @date : 2023. 09. 14.
	 * @method : createGroupPopup
	 * @return : ModelAndView
	 * @param : Map<String, Object>
	 */
	@RequestMapping("/createGroupPopup")
	public ModelAndView createGroupPopup(@RequestParam Map<String, Object> reqMap) {

		ModelAndView model = new ModelAndView();
		model.setViewName("popup:/admin/createGroupPopup");
		
		return model;
	}
	
	/**
	 * @desc : 그룹 삭제
	 * @author : shjeong
	 * @date : 2023. 09. 15.
	 * @method : deleteAuthorityGroupAction
	 * @param reqMap
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/deleteAuthorityGroupAction")
	public Map<String, Object> deleteAuthorityGroupAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			
			AdminHelper.service.deleteAuthorityGroup(reqMap);
			
			map.put("result", true);
			map.put("msg", "선택한 그룹이 삭제되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR : " + e.getLocalizedMessage());
		}

		return map;
	}
	
	/**
	 * @desc : 그룹 유저 가져오기
	 * @author : shjeong
	 * @date : 2023. 09. 15.
	 * @method : getGroupUserList
	 * @param reqMap
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/getGroupUserList")
	public Map<String, Object> getGroupUserList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			List<PeopleData> list = AdminHelper.manager.getGroupUserList(reqMap);
			
			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR : " + e.getLocalizedMessage());
		}

		return map;
	}
	
	/**
	 * @desc : 그룹내 유저 편집
	 * @author : shjeong
	 * @date : 2023. 09. 15.
	 * @method : editGroupUserAction
	 * @param reqMap
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/editGroupUserAction")
	public Map<String, Object> editGroupUserAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean edit = (boolean) reqMap.get("edit");
		
		try {
			
			AdminHelper.service.editGroupUser(reqMap);
			
			map.put("result", true);
			map.put("msg", edit?"저장이 완료되었습니다.":"삭제가 완료되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR : " + e.getLocalizedMessage());
		}

		return map;
	}
	
	@ResponseBody
	@RequestMapping("/getEsolutionMenu")
	public Map<String, Object> getEsolutionMenu(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		boolean disabled = (boolean)reqMap.get("disabled");
		
		try {
			
			List<EsolutionMenuData> list = AdminHelper.manager.getEsolutionMenu(disabled);

			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
	
	@RequestMapping("/esolutionMenuManagement")
	public ModelAndView esolutionMenuManagement(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		model.setViewName("default:/admin/esolutionMenuManagement");
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/createEsolutionMenuAction")
	public Map<String, Object> createEsolutionMenuAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			AdminHelper.service.createEsolutionMenu(reqMap);

			map.put("msg", MessageUtil.getMessage("저장되었습니다"));
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/deleteEsolutionMenuAction")
	public Map<String, Object> deleteEsolutionMenuAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			AdminHelper.service.deleteEsolutionMenu(reqMap);

			map.put("msg", MessageUtil.getMessage("선택한 메뉴가 삭제되었습니다"));
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
	
	@RequestMapping("/include_authorityByMenu")
	public ModelAndView authorityByMenu(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			model.addObject("type","menu");
			model.setViewName("include:/admin/include/authorityGroupManagement");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	@RequestMapping("/include_authorityByFolder")
	public ModelAndView authorityByFolder(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			model.addObject("type","folder");
			model.setViewName("include:/admin/include/authorityGroupManagement");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	@RequestMapping("/include_authorityByObject")
	public ModelAndView authorityByObject(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			model.addObject("type","object");
			model.setViewName("include:/admin/include/authorityGroupManagement");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	@RequestMapping("/include_authorityDeptUser")
	public ModelAndView include_authorityDeptUser(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		String autoGridHeight = StringUtil.checkReplaceStr((String) reqMap.get("autoGridHeight"), "false");
		String gridHeight = StringUtil.checkReplaceStr((String) reqMap.get("gridHeight"), "200");

		model.addObject("autoGridHeight", autoGridHeight);
		model.addObject("gridHeight", gridHeight);
		model.setViewName("include:/admin/include/authorityDeptUser");
		
		return model;
	}
	
	/**
	 * @desc : 객체 그룹 리스트 가져오기
	 * @author : shjeong
	 * @date : 2023. 11. 17.
	 * @method : getObjectAuthGroupList
	 * @return : Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getObjectAuthGroupList")
	public Map<String, Object> getObjectAuthGroupList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<ObjectAuthGroupData> list = AdminHelper.manager.getObjectAuthGroupList();
			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
	
	@RequestMapping("/multiLangManagement")
	public ModelAndView multiLangManagement(@RequestParam Map<String, Object> reqMap){
		
		ModelAndView model = new ModelAndView();
		model.setViewName("default:/admin/multiLangManagement");
//		model.addObject(MESKey.mesSesionUserID, request.getSession().getAttribute(MESKey.mesSesionUserID));
		
		return model;
	}
	
	@RequestMapping("/multiLangTemplateDownload")
	public void multiLangTemplateDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
        try {
        	boolean result = AdminHelper.service.downloadMultiLangAction(request, response);
        	ObjectLogger.debug(result, "multiLangTemplateDownload result");
        	
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
	@ResponseBody
	@RequestMapping("/multiLangTemplateUpload")
	public Map<String, Object> multiLangTemplateUpload(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {

			
			boolean result = AdminHelper.service.uploadMultiLangAction(request, response);
			ObjectLogger.debug(result, "multiLangTemplateUpload result");
			
			map.put("uploadResult", result);
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}

		return map;
	}
	
	
	/**
	 * 관리자 유저 퇴사처리 (라이센스 자동 제외)
	 * cklee
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/userIsDisabledAction")
	public Map<String, Object> userIsDisabledAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			
			boolean success = AdminHelper.service.userIsDisabledAction(reqMap);
			map.put("result", success);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}

		return map;
	}
	
}
