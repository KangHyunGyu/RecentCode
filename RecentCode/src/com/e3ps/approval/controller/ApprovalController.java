package com.e3ps.approval.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.e3ps.approval.ApprovalLine;
import com.e3ps.approval.ApprovalMaster;
import com.e3ps.approval.bean.ApprovalData;
import com.e3ps.approval.bean.ApprovalLineData;
import com.e3ps.approval.bean.ApprovalLineTemplateData;
import com.e3ps.approval.service.ApprovalHelper;
import com.e3ps.approval.service.MultiApprovalHelper;
import com.e3ps.approval.util.ApprovalUtil;
import com.e3ps.change.service.ChangeHelper;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.org.bean.PeopleData;
import com.e3ps.org.service.DepartmentHelper;

import wt.org.WTPrincipal;
import wt.util.WTException;

@Controller
@RequestMapping("/approval")
public class ApprovalController {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.APPROVAL.getName());

	/**
	 * @desc : 결재선 지정 화면
	 * @author : sangylee
	 * @date : 2019. 7. 10.
	 * @method : addApprovalLine
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_addApprovalLine")
	public ModelAndView addApprovalLine(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String autoGridHeight = StringUtil.checkReplaceStr((String) reqMap.get("autoGridHeight"), "false");
		String gridHeight = StringUtil.checkReplaceStr((String) reqMap.get("gridHeight"), "200");

		model.addObject("appLineOid", oid);
		model.addObject("autoGridHeight", autoGridHeight);
		model.addObject("gridHeight", gridHeight);

		model.setViewName("include:/approval/include/addApprovalLine");

		return model;
	}

	/**
	 * @desc : 결재선 지정 리스트 가져오기
	 * @author : sangylee
	 * @date : 2019. 7. 10.
	 * @method : getApprovalLine
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getApprovalLine")
	public static Map<String, Object> getApprovalLine(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<>();

		try {
			List<ApprovalLineData> list = ApprovalHelper.manager.getApprovalLine(reqMap);

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
	 * @desc : 결재선 추가 팝업
	 * @author : sangylee
	 * @date : 2019. 7. 10.
	 * @method : addParticipant
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/addApprovalLinePopup")
	public ModelAndView addApprovalLinePopup(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		model.setViewName("popup:/approval/addApprovalLinePopup");

		return model;
	}
	
	/**
	 * @desc : 승인자 중간 수신인 추가 팝업창
	 * @author : hckim
	 */
	@RequestMapping("/appendReceiveLinePopup")
	public ModelAndView appendReceiveLinePopup(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		model.setViewName("popup:/approval/appendReceiveLinePopup");

		return model;
	}
	
	@ResponseBody
	@RequestMapping("/appendReceiveLineAction")
	public Map<String, Object> appendReceiveLineAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			
			String appLineOid = StringUtil.checkNull((String)reqMap.get("appLineOid"));
			List<Map<String, Object>> appendReceiveList = (List<Map<String, Object>>) reqMap.get("appendReceiveList");
			
			ApprovalLine line = (ApprovalLine)CommonUtil.getObject(appLineOid);
			ApprovalMaster appMaster = line.getMaster();
			List<WTPrincipal> wtUserOidList = appendReceiveList.stream().map(item -> (WTPrincipal)CommonUtil.getObject((String)item.get("wtuserOID"))).collect(Collectors.toList());
			
			ApprovalHelper.service.appendReceiveList(appMaster, wtUserOidList);
			
			
			
//			ApprovalHelper.service.approveAction(reqMap);
//
//			String lineState = StringUtil.checkNull((String) reqMap.get("lineState"));
//			String type = StringUtil.checkNull((String) reqMap.get("type"));

//			if ("tempStorage".equals(type)) {
//				map.put("redirectUrl", CommonUtil.getURLString("/workspace/listWorkItem") + "?type=tempStorage");
//			} else if ("eca".equals(type)) {
//				map.put("redirectUrl", CommonUtil.getURLString("/workspace/listToDo"));
//			} else {
//				map.put("redirectUrl", CommonUtil.getURLString("/workspace/listWorkItem") + "?type=approval");
//			}

			map.put("result", true);
			map.put("msg", "처리 되었습니다.");

		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	/**
	 * @desc : 결재선 지정 라인 그리드
	 * @author : sangylee
	 * @date : 2019. 7. 16.
	 * @method : approvalLineGrid
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_approvalLineGrid")
	public ModelAndView approvalLineGrid(@RequestParam Map<String, Object> reqMap) {

		ModelAndView model = new ModelAndView();

		String title = StringUtil.checkNull((String) reqMap.get("title"));
		String roleType = StringUtil.checkNull((String) reqMap.get("roleType"));

		model.addObject("title", title);
		model.addObject("roleType", roleType);

		model.setViewName("include:/approval/include/approvalLineGrid");

		return model;
	}
	
	@RequestMapping("/include_approvalLineGridForAppend")
	public ModelAndView approvalLineGridForAppend(@RequestParam Map<String, Object> reqMap) {

		ModelAndView model = new ModelAndView();

		String title = StringUtil.checkNull((String) reqMap.get("title"));
		String roleType = StringUtil.checkNull((String) reqMap.get("roleType"));
		String appLineOid = StringUtil.checkNull((String)reqMap.get("appLineOid"));

		model.addObject("title", title);
		model.addObject("roleType", roleType);
		model.addObject("appLineOid", appLineOid);

		model.setViewName("include:/approval/include/approvalLineGridForAppend");

		return model;
	}

	/**
	 * @desc : 결재선 유저 리스트
	 * @author : sangylee
	 * @date : 2019. 7. 16.
	 * @method : orgUserList
	 * @param reqMap
	 * @return ModelAndView
	 * @throws WTException
	 */
	@RequestMapping("/include_orgUserList")
	public ModelAndView orgUserList(@RequestParam Map<String, Object> reqMap) throws WTException {

		ModelAndView model = new ModelAndView();

		String rootLocation = DepartmentHelper.manager.getDepartmentRootLocation();

		model.addObject("rootLocation", rootLocation);

		model.setViewName("include:/approval/include/orgUserList");

		return model;
	}

	/**
	 * @desc : 결재선 Template
	 * @author : sangylee
	 * @date : 2019. 7. 16.
	 * @method : appLineTemplate
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_appLineTemplate")
	public ModelAndView appLineTemplate(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		model.setViewName("include:/approval/include/appLineTemplate");

		return model;
	}

	/**
	 * @desc : 결재 템플릿 생성
	 * @author : sangylee
	 * @date : 2019. 8. 12.
	 * @method : createTemplate
	 * @param reqMap
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/createTemplateAction")
	public Map<String, Object> createTemplate(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			ApprovalHelper.service.createTemplate(reqMap);

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
	 * @desc : 결재 템플릿 삭제
	 * @author : sangylee
	 * @date : 2019. 8. 12.
	 * @method : deleteTemplateAction
	 * @param reqMap
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/deleteTemplateAction")
	public Map<String, Object> deleteTemplateAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			ApprovalHelper.service.deleteTemplate(reqMap);

			map.put("result", true);
			map.put("msg", "삭제가 완료되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	/**
	 * @desc : 결재 템플릿 가져오기
	 * @author : sangylee
	 * @date : 2019. 7. 16.
	 * @method : getAppLineTemplate
	 * @param reqMap
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/getAppLineTemplate")
	public Map<String, Object> getAppLineTemplate(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<ApprovalLineTemplateData> list = ApprovalHelper.manager.getApprovalLineTemplateList(reqMap);

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
	 * @desc : 결재 상세 가져오기
	 * @author : sangylee
	 * @date : 2019. 7. 16.
	 * @method : getDetailTemplate
	 * @param reqMap
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/getDetailTemplate")
	public Map<String, Object> getDetailTemplate(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<PeopleData> list = ApprovalHelper.manager.getDetailTemplateList(reqMap);

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
	 * @desc : 결재 위임 화면
	 * @author : sangylee
	 * @date : 2019. 7. 22.
	 * @method : delegateApprove
	 * @param reqMap
	 * @return
	 */
	@RequestMapping("/include_delegateApprove")
	public ModelAndView delegateApprove(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		String type = StringUtil.checkNull((String) reqMap.get("type"));
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));

		model.addObject("type", type);
		model.addObject("oid", oid);
		model.setViewName("include:/approval/include/delegateApprove");

		return model;
	}

	/**
	 * @desc : 결재 정보 화면
	 * @author : sangylee
	 * @date : 2019. 7. 22.
	 * @method : approvalInfo
	 * @param reqMap
	 * @return
	 */
	@RequestMapping("/include_approvalInfo")
	public ModelAndView approvalInfo(@RequestParam Map<String, Object> reqMap) throws Exception {
		ModelAndView model = new ModelAndView();

		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		ApprovalData data = ApprovalHelper.manager.getApprovalData(oid);
		model.addObject("oid", oid);
		model.addObject("appData", data);
		model.setViewName("include:/approval/include/approvalInfo");

		return model;
	}

	/**
	 * @desc : 결재선 리스트
	 * @author : sangylee
	 * @date : 2019. 7. 23.
	 * @method : approvalLineView
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_approvalLineView")
	public ModelAndView approvalLineView(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String searchRoleType = StringUtil.checkNull((String) reqMap.get("searchRoleType"));

		model.addObject("oid", oid);
		model.addObject("searchRoleType", searchRoleType);

		model.setViewName("include:/approval/include/approvalLineView");

		return model;
	}

	/**
	 * @desc : 결재선 수신 리스트
	 * @author : hgkang
	 * @date : 2023. 2. 16.
	 * @method : approvalRecLineView
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_approvalRecLineView")
	public ModelAndView approvalRecLineView(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String receiveRole = StringUtil.checkNull((String) reqMap.get("receiveRole"));
		model.addObject("oid", oid);
		model.setViewName("include:/approval/include/approvalRecLineView");

		return model;
	}

	/**
	 * 
	 * @desc : 결재 Action(임시저장, 작업함)
	 * @author : tsuam
	 * @date : 2019. 7. 24.
	 * @method : approveAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/approveAction")
	public Map<String, Object> approveAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			ApprovalHelper.service.approveAction(reqMap);

			String lineState = StringUtil.checkNull((String) reqMap.get("lineState"));
			String type = StringUtil.checkNull((String) reqMap.get("type"));

			if ("tempStorage".equals(type)) {
				map.put("redirectUrl", CommonUtil.getURLString("/workspace/listWorkItem") + "?type=tempStorage");
			} else if ("eca".equals(type)) {
				map.put("redirectUrl", CommonUtil.getURLString("/workspace/listToDo"));
			} else {
				map.put("redirectUrl", CommonUtil.getURLString("/workspace/listWorkItem") + "?type=approval");
			}

			map.put("result", true);
			map.put("msg", "처리 되었습니다.");

		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	@ResponseBody
	@RequestMapping("/dropAction")
	public Map<String, Object> dropAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			ApprovalHelper.service.dropAction(reqMap);

			map.put("redirectUrl", CommonUtil.getURLString("/workspace/listWorkItem") + "?type=approval");
			map.put("result", true);
			map.put("msg", "처리 되었습니다.");

		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	/**
	 * 
	 * @desc : 위임 Action
	 * @author : tsuam
	 * @date : 2019. 7. 24.
	 * @method : delegateAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delegateAction")
	public Map<String, Object> delegateAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {

			ApprovalHelper.service.delegateAction(reqMap);

			map.put("redirectUrl", CommonUtil.getURLString("/workspace/listWorkItem") + "?type=approval");

			map.put("result", true);
			map.put("msg", "처리 되었습니다.");

		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	/**
	 * @desc : 일괄 결재 등록
	 * @author : sangylee
	 * @date : 2019. 7. 24.
	 * @method : createMultiApproval
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/createMultiApproval")
	public ModelAndView createMultiApproval(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		/* 일괄 결재 타입에 따른 include pamrm 결정 */
		String objType = StringUtil.checkReplaceStr((String) reqMap.get("objType"), "doc"); // part,epm
//		System.out.println("objType :::: " + objType);
		Map<String, String> map = ApprovalUtil.getMultiTypeInfo(objType);
		String pageName = map.get("pageName");// multiAppPart,multiAppEpm
		String title = map.get("title"); // 폼목,도면

		model.addObject("objType", objType);
		model.addObject("pageName", pageName);
		model.addObject("title", title);
		model.setViewName("default:/" + objType + "/createMultiApproval");

		return model;
	}

	/**
	 * @desc : 일괄 결재 등록 Action
	 * @author : sangylee
	 * @date : 2019. 7. 24.
	 * @method : createMultiApprovalAction
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/createMultiApprovalAction")
	public Map<String, Object> createMultiApprovalAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {

			LOGGER.debug(reqMap.toString());

			List<Map<String, Object>> approvalList = (List<Map<String, Object>>) reqMap.get("approvalList");
//			System.out.println("approvalList ::: " + approvalList);
			List<Map<String, Object>> multiObjectList = (List<Map<String, Object>>) reqMap.get("multiObjectList");
			// List<Map<String, Object>> multiAppPartList = (List<Map<String, Object>>)
			// reqMap.get("multiAppPartList");
			// List<Map<String, Object>> multiAppEpmList = (List<Map<String, Object>>)
			// reqMap.get("multiAppEpmList");

			String appState = StringUtil.checkNull((String) reqMap.get("appState"));

			LOGGER.debug(approvalList.toString());
			LOGGER.debug(multiObjectList.toString());

			MultiApprovalHelper.service.createMultiApprovalAction(reqMap);

			// LOGGER.debug(multiAppPartList);
			// LOGGER.debug(multiAppEpmList);
			LOGGER.debug(appState);

			map.put("result", true);
			map.put("msg", "등록이 완료되었습니다.");
			map.put("redirectUrl", CommonUtil.getURLString("/workspace/searchMultiApproval"));

		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}

		return map;
	}

	@ResponseBody
	@RequestMapping("/recallAction")
	public Map<String, Object> recallAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			ApprovalHelper.service.recallAction(reqMap);

			map.put("result", true);
			map.put("msg", "회수되었습니다.");
			map.put("redirectUrl", "Reload");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}

	@RequestMapping("/include_addApprovalLine2")
	public ModelAndView addApprovalLine2(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String autoGridHeight = StringUtil.checkReplaceStr((String) reqMap.get("autoGridHeight"), "false");
		String gridHeight = StringUtil.checkReplaceStr((String) reqMap.get("gridHeight"), "200");

		model.addObject("appLineOid", oid);
		model.addObject("autoGridHeight", autoGridHeight);
		model.addObject("gridHeight", gridHeight);

		model.setViewName("include:/approval/include/addApprovalLine2");

		return model;
	}
	
	@RequestMapping("/include_onItemDetailApproval")
	public ModelAndView onItemDetailApproval(@RequestParam Map<String, Object> reqMap) throws Exception {
		ModelAndView model = new ModelAndView();
		
		String type = StringUtil.checkNull((String) reqMap.get("type"));
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		ApprovalLine line = (ApprovalLine)CommonUtil.getObject(oid);
		boolean isWorking = false;
		if(line != null) {
			String state = line.getState().toString();
			isWorking = state.equals(ApprovalUtil.STATE_LINE_ONGOING); 
		}
		ApprovalData data = ChangeHelper.manager.getChangeObject(oid);
		String className = "";
		String cOid = "";
		if(!data.equals(null)) {
			className = data.obj.getClass().getName();
			className = className.substring(className.lastIndexOf(".")+1);
			cOid = CommonUtil.getOIDString(data.obj);
		}
		model.addObject("isWorking", isWorking);
		model.addObject("workData", data);
		model.addObject("type", type);
		model.addObject("cOid", cOid);
		model.addObject("className", className);
		model.addObject("oid", oid);
		model.setViewName("include:/approval/include/onItemDetailApproval");
		
		return model;
	}
	
//	@RequestMapping("/requestApproval")
//	public ModelAndView requestApproval(@RequestParam Map<String, Object> reqMap) {
//		ModelAndView model = new ModelAndView();
//		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
//		
//		model.addObject("oid",oid);
//		model.setViewName("popup:/approval/requestApproval");
//		
//		return model;
//	}
//	
//	@ResponseBody
//	@RequestMapping("/registrationApprovalAction")
//	public Map<String, Object> registrationApprovalAction(@RequestBody Map<String, Object> reqMap, HttpServletRequest request) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		
//		try {
//			
//			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
//			List<Map<String, Object>> approvalList = (List<Map<String, Object>>) reqMap.get("approvalList");
//			String appState = StringUtil.checkNull((String) reqMap.get("appState"));
//			ApprovalHelper.service.registApproval(CommonUtil.getObject(oid), approvalList, appState, null);
//			
//			
//			map.put("result", true);
//			map.put("msg", "등록이 완료되었습니다.");
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			map.put("result", false);
//			map.put("msg", "ERROR = " + e.getLocalizedMessage());
//		}
//		
//		return map;
//	}
}
