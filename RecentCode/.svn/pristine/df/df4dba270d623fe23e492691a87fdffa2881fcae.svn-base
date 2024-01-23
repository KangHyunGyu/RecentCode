package com.e3ps.workspace.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.e3ps.approval.ApprovalLine;
import com.e3ps.approval.bean.ApprovalData;
import com.e3ps.approval.bean.ApprovalListData;
import com.e3ps.approval.bean.MultiApprovalData;
import com.e3ps.approval.service.ApprovalHelper;
import com.e3ps.approval.service.MultiApprovalHelper;
import com.e3ps.approval.util.ApprovalUtil;
import com.e3ps.change.EChangeActivity;
import com.e3ps.change.service.ChangeHelper;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.workspace.bean.NoticeData;
import com.e3ps.workspace.notice.Notice;
import com.e3ps.workspace.service.WorkspaceHelper;
import com.e3ps.workspace.util.WorkspaceUtil;

import wt.fc.Persistable;
import wt.org.OrganizationServicesMgr;
import wt.org.WTUser;
import wt.session.SessionHelper;

/**
 * @author sangylee
 */
@Controller
@RequestMapping("/workspace")
public class WorkspaceController {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.WORKSPACE.getName());
	
	/**
	 * @desc	: 공지사항 검색
	 * @author	: sangylee
	 * @date	: 2019. 7. 12.
	 * @method	: searchNotice
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/searchNotice")
	public ModelAndView searchNotice(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		model.setViewName("default:/workspace/searchNotice");
		
		return model;
	}
	
	/**
	 * @desc	: 공지사항 검색 Action
	 * @author	: sangylee
	 * @date	: 2019. 7. 29.
	 * @method	: searchNotice
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/searchNoticeScrollAction")
	public Map<String, Object> searchNoticeScrollAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			map = WorkspaceHelper.manager.getNoticeList(reqMap);
			
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}

	/**
	 * @desc	: 공지사항 검색 Action
	 * @author	: tsjeong
	 * @date	: 2020. 9. 17.
	 * @method	: searchNoticeScrollAction2
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/searchNoticeScrollAction2")
	public Map<String, Object> searchNoticeScrollAction2(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			map = WorkspaceHelper.manager.getNoticeList2(reqMap);
			
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 문서 등록 화면
	 * @author	: sangylee
	 * @date	: 2019. 7. 29.
	 * @method	: createNotice
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/createNotice")
	public ModelAndView createNotice(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		model.setViewName("default:/workspace/createNotice");
		
		return model;
	}
	
	/**
	 * @desc	: 문서 등록 Action
	 * @author	: sangylee
	 * @date	: 2019. 7. 29.
	 * @method	: createNoticeAction
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/createNoticeAction")
	public Map<String, Object> createNoticeAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			WorkspaceHelper.service.createNoticeAction(reqMap);
			
			map.put("result", true);
			map.put("msg", "등록이 완료되었습니다.");
			map.put("redirectUrl", CommonUtil.getURLString("/workspace/searchNotice"));
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 공지사항 상세보기
	 * @author	: sangylee
	 * @date	: 2019. 7. 29.
	 * @method	: viewNotice
	 * @param reqMap
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/viewNotice")
	public ModelAndView viewNotice(@RequestParam Map<String, Object> reqMap) throws Exception{
		
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
		
		Notice notice = (Notice) CommonUtil.getObject(oid);
		
		WorkspaceHelper.service.modifyViewCntAction(reqMap);
		
		notice.setCnt(notice.getCnt()+1);
		NoticeData noticeData = new NoticeData(notice);
		
		model.addObject("notice", noticeData);
		
		model.setViewName("default:/workspace/viewNotice");
		
		return model;
	}
	
	/**
	 * @desc : 공지사항 분류 리스트
	 * @author : tsjeong
	 * @date : 2019. 11. 18.
	 * @method : getNotifyTypeList
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/getNotifyTypeList")
	public Map<String, Object> getNotifyTypeList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			List<Map<String, String>> list = WorkspaceUtil.getNotifyTypeList();
			
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
	 * @desc	: 결재 작업함, 임시저장함 리스트
	 * @author	: sangylee
	 * @date	: 2019. 7. 18.
	 * @method	: listWorkItem
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/listWorkItem")
	public ModelAndView listWorkItem(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String type = (String) reqMap.get("type"); //tempStorage 임시저장,approval 작업함
		
		model.addObject("type", type);
		model.setViewName("default:/workspace/listWorkItem");
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/listWorkItemAction")
	public Map<String, Object> listWorkItemAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ApprovalListData> list = new ArrayList<ApprovalListData>();
		try {
			String type = (String)reqMap.get("type");
			if(type.equals("tempStorage")) {
				list = ApprovalHelper.manager.searchTempApproval(reqMap);
			}else {
				list = ApprovalHelper.manager.searchWorkingApproval(reqMap, false);
			}
			
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
	 * @desc	: 결재 상세 화면
	 * @author	: sangylee
	 * @date	: 2019. 7. 22.
	 * @method	: approvalDetail
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/detailApproval")
	public ModelAndView detailApproval(@RequestParam Map<String, Object> reqMap) throws Exception {
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
		model.setViewName("default:/workspace/detailApproval");
		
		return model;
	}
	
	/**
	 * @desc	: 결재 진행함, 완료함, 수신함 화면
	 * @author	: sangylee
	 * @date	: 2019. 7. 18.
	 * @method	: listItem
	 * @param reqMap
	 * @return
	 */
	@RequestMapping("/listItem")
	public ModelAndView listItem(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String type = (String) reqMap.get("type");
		
		model.addObject("type", type);
		model.setViewName("default:/workspace/listItem");
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/listItemAction")
	public Map<String, Object> listItemAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ApprovalListData> list = new ArrayList<ApprovalListData>();
		try {
			String type = (String)reqMap.get("type");
			
			list = ApprovalHelper.manager.searchListItemAction(reqMap);
			
			map.put("list", list);
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/listItemScrollAction")
	public Map<String, Object> listItemScrollAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			
			map = ApprovalHelper.manager.searchListItemScrollAction(reqMap);
			
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	
	/**
	 * @desc	: 결재 진행함, 완료함, 수신함 화면
	 * @author	: sangylee
	 * @date	: 2019. 7. 18.
	 * @method	: listItem
	 * @param reqMap
	 * @return
	 */
	@RequestMapping("/listSendItem")
	public ModelAndView listSendItem(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String type = (String) reqMap.get("type");
		System.out.println("type ::: " + type);
		model.addObject("type", type);
		model.setViewName("default:/workspace/listSendItem");
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/listSendItemAction")
	public Map<String, Object> listSendItemAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ApprovalListData> list = new ArrayList<ApprovalListData>();
		try {
			String type = (String)reqMap.get("type");
			
			list = ApprovalHelper.manager.searchSendItemAction(reqMap);
			
			map.put("list", list);
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/listSendItemScrollAction")
	public Map<String, Object> listSendItemScrollAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			map = ApprovalHelper.manager.searchSendItemScrollAction(reqMap);
			
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 일괄 결재 검색
	 * @author	: sangylee
	 * @date	: 2019. 7. 24.
	 * @method	: searchMultiApproval
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/searchMultiApproval")
	public ModelAndView searchMultiApproval(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		model.setViewName("default:/workspace/searchMultiApproval");
		
		return model;
	}
	
	/**
	 * @desc	: 일괄 결재 검색 Action
	 * @author	: sangylee
	 * @date	: 2019. 7. 24.
	 * @method	: searchMultiApprovalAction
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/searchMultiApprovalAction")
	public Map<String, Object> searchMultiApprovalAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			List<MultiApprovalData> list = MultiApprovalHelper.manager.getMultiListALL(reqMap);
			
			map.put("list", list);
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/searchMultiApprovalScrollAction")
	public Map<String, Object> searchMultiApprovalScrollAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			map = MultiApprovalHelper.manager.getMultiScrollListALL(reqMap);
			
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 일괄 결재 검색
	 * @author	: tsjeong
	 * @date	: 2020. 9. 17.
	 * @method	: searchMultiApprovalScrollAction2
	 * @param reqMap
	 * @return ModelAndView
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/searchMultiApprovalScrollAction2")
	public Map<String, Object> searchMultiApprovalScrollAction2(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			map = MultiApprovalHelper.manager.getMultiScrollListALL2(reqMap);
			
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 비밀번호 변경 화면
	 * @author	: sangylee
	 * @date	: 2019. 7. 12.
	 * @method	: changePassword
	 * @param reqMap
	 * @return ModelAndView
	 * @throws Exception 
	 */
	@RequestMapping("/changePassword")
	public ModelAndView changePassword(@RequestParam Map<String, Object> reqMap) throws Exception {
		
		ModelAndView model = new ModelAndView();

		String id = StringUtil.checkNull((String) reqMap.get("id"));
		String isPop = StringUtil.checkNull((String) reqMap.get("isPop"));
		
		String name = "";
		if (id.length() == 0) {
			WTUser user = (WTUser) SessionHelper.manager.getPrincipal();

			id = user.getName();
			name = user.getFullName();
			if (id.equals("Administrator")) {
				id = "wcadmin";				
			}
		} else {
			WTUser user = OrganizationServicesMgr.getUser(id);
			
			if(user != null) {
				name = user.getFullName();
			}
		}

		model.addObject("id", id);
		model.addObject("name", name);
		model.addObject("isPop", isPop);

		if ("true".equals(isPop)) {
			model.setViewName("popup:/workspace/changePassword");
		} else {
			model.setViewName("default:/workspace/changePassword");
		}
		
		return model;
	}
	
	/**
	 * @desc	: 비밀번호 변경 Action
	 * @author	: sangylee
	 * @date	: 2019. 7. 29.
	 * @method	: changePasswordAction
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/changePasswordAction")
	public Map<String, Object> changePasswordAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String isPop = StringUtil.checkNull((String) reqMap.get("isPop"));
			
			WorkspaceHelper.service.changePasswordAction(reqMap);

			if ("true".equals(isPop)) {
				map.put("redirectUrl", "close");
			} else {
				map.put("redirectUrl", CommonUtil.getURLString("/portal/main"));
			}
			map.put("result", true);
			map.put("msg", "비밀번호 변경이 완료되었습니다.");

		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		return map;
	}
	
	/**
	 * @desc	: 조직도 화면
	 * @author	: sangylee
	 * @date	: 2019. 7. 12.
	 * @method	: companyTree
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/companyTree")
	public ModelAndView companyTree(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		model.setViewName("default:/workspace/companyTree");
		
		return model;
	}
	
	/**
	 * @desc	: 공지 글 수정
	 * @author	: tsjeong
	 * @date	: 2019. 10. 1.
	 * @method	: modifyNotice
	 * @return	: ModelAndView
	 * @param reqMap
	 * @return
	 */
	@RequestMapping("/modifyNotice")
	public ModelAndView modifyNotice(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			
			Notice notice = (Notice) CommonUtil.getObject(oid);
			
			NoticeData noticeData = new NoticeData(notice);
			
			model.addObject("notice", noticeData);
			
			model.setViewName("default:/workspace/modifyNotice");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	/**
	 * @desc	: 공지 글 수정 Action
	 * @author	: tsjeong
	 * @date	: 2019. 10. 1.
	 * @method	: modifyNoticeAction
	 * @return	: Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/modifyNoticeAction")
	public Map<String, Object> modifyNoticeAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			Notice notice = WorkspaceHelper.service.modifyNoticeAction(reqMap);
			
			String oid = CommonUtil.getOIDString(notice);
			
			map.put("msg", "수정되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/workspace/viewNotice") + "?oid=" + oid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 공지 글 삭제
	 * @author	: tsjeong
	 * @date	: 2019. 10. 1.
	 * @method	: deleteNoticeAction
	 * @return	: Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteNoticeAction")
	public Map<String, Object> deleteNoticeAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			WorkspaceHelper.service.deleteNoticeAction(reqMap);
			
			map.put("msg", "삭제되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/workspace/searchNotice"));
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/listToDo")
	public ModelAndView listToDo(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		model.setViewName("default:/workspace/listToDo");
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/listToDoAction")
	public Map<String, Object> listToDoAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<ApprovalListData> list = ApprovalHelper.manager.searchWorkingApproval(reqMap, true);
			
			map.put("list", list);
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
}
