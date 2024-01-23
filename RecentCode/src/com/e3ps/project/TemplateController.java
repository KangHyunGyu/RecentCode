package com.e3ps.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.project.beans.TemplateData;
import com.e3ps.project.beans.TemplateOutputData;
import com.e3ps.project.beans.TemplateTaskData;
import com.e3ps.project.beans.TemplateTreeData;
import com.e3ps.project.service.OutputHelper;
import com.e3ps.project.service.TemplateHelper;

import wt.org.WTUser;
import wt.session.SessionHelper;
import wt.util.WTException;

@Controller
@RequestMapping("/project/template")
public class TemplateController {
	protected static final Logger logger = LoggerFactory.getLogger(TemplateController.class);

	@RequestMapping("/searchTemplate")
	public ModelAndView searchTemplate(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		model.setViewName("default:/project/template/searchTemplate");
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/searchTemplateAction")
	public Map<String, Object> searchTemplateAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			List<TemplateData> list = TemplateHelper.manager.getTemplateList(reqMap);
			
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
	 * @desc	: 탬플릿 등록 화면
	 * @author	: sangylee
	 * @date	: 2020. 9. 18.
	 * @method	: createTemplate
	 * @param	: reqMap
	 * @return	: ModelAndView
	 */
	@RequestMapping("/createTemplate")
	public ModelAndView createTemplate(@RequestParam Map<String, Object> reqMap) throws WTException {
		ModelAndView model = new ModelAndView();
		
		model.setViewName("default:/project/template/createTemplate");
		
		return model;
	}
	
	/**
	 * @desc	: 관련 템플릿 리스트
	 * @author	: sangylee
	 * @date	: 2020. 9. 22.
	 * @method	: searchRelatedTemplate
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/searchRelatedTemplate")
	public Map<String, Object> searchRelatedTemplate(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			List<TemplateData> list = TemplateHelper.manager.getRelatedTemplate(reqMap);
			
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
	 * @desc	: 탬플릿 등록 Action
	 * @author	: sangylee
	 * @date	: 2020. 9. 18.
	 * @method	: createTemplateAction
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/createTemplateAction")
	public Map<String, Object> createTemplateAction(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.createTemplateAction");
		}
		try {
			EProjectTemplate template = TemplateHelper.service.save(reqMap);		

			String oid = CommonUtil.getOIDString(template);
			
			map.put("result", true);
			map.put("msg", "등록이 완료되었습니다.");
			map.put("redirectUrl", CommonUtil.getURLString("/project/template/viewMain") + "?oid=" + oid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 탬플릿 upload Action
	 * @author	: hckim
	 * @date	: 2020. 12. 3
	 * @method	: uploadTemplateAction
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/uploadTemplateAction")
	public Map<String, Object> uploadTemplateAction(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.uploadTemplateAction");
		}
		try {
			EProjectTemplate template = TemplateHelper.service.upload(reqMap);		

			map.put("msg", "등록에 실패하였습니다.");
			if(template!=null) {
				String oid = CommonUtil.getOIDString(template);
				map.put("result", true);
				map.put("msg", "등록이 완료되었습니다.");
				map.put("redirectUrl", CommonUtil.getURLString("/project/template/viewMain") + "?oid=" + oid);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 탬플릿 COPY Action
	 * @author	: sangylee
	 * @date	: 2020. 9. 21.
	 * @method	: copyTemplateAction
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/copyTemplateAction")
	public Map<String, Object> copyTemplateAction(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.createTemplateAction");
		}
		try {
			EProjectTemplate template = TemplateHelper.service.copyTemplate(reqMap);		

			String oid = CommonUtil.getOIDString(template);
			
			map.put("result", true);
			map.put("msg", "등록이 완료되었습니다.");
			map.put("redirectUrl", CommonUtil.getURLString("/project/template/viewMain") + "?oid=" + oid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 탬플릿 메인
	 * @author	: sangylee
	 * @date	: 2020. 9. 15.
	 * @method	: viewMain
	 * @param	: reqMap
	 * @return	: ModelAndView
	 */
	@RequestMapping("/viewMain")
	public ModelAndView viewMain(@RequestParam Map<String, Object> reqMap) {

		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.viewMain");
		}
		
		ModelAndView model = new ModelAndView();
		
		try {
			
			String oid = (String) reqMap.get("oid");

			model.addObject("oid", oid);
			model.setViewName("default:/project/template/viewTemplate");
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	/**
	 * @desc	: 탬플릿 산출물 리스트
	 * @author	: sangylee
	 * @date	: 2020. 9. 15.
	 * @method	: outputList
	 * @param	: reqMap
	 * @return	: ModelAndView
	 */
	@RequestMapping("/outputList")
	public ModelAndView outputList(@RequestParam Map<String, Object> reqMap) {

		ModelAndView model = new ModelAndView();
		
		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.outputList");
		}
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			model.addObject("oid", oid);
			
			model.setViewName("include:/project/template/template_output");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}

	/**
	 * @desc	: 탬플릿 산출물 리스트 Action (수정)
	 * @author	: sangylee
	 * @date	: 2020. 9. 21.
	 * @method	: searchTemplateOutputAction
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/searchTemplateOutputAction")
	public Map<String, Object> searchTemplateOutputAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			List<TemplateOutputData> list = TemplateHelper.manager.getTemplateOutputList(reqMap);
			
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
	 * @desc	: 탬플릿 Root 뷰
	 * @author	: sangylee
	 * @date	: 2020. 9. 15.
	 * @method	: view
	 * @param	: reqMap
	 * @return	: ModelAndView
	 */
	@RequestMapping("/view")
	public ModelAndView view(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.view");
		}
		try {
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));

			EProjectTemplate root = (EProjectTemplate)CommonUtil.getObject(oid);

			TemplateData data = new TemplateData(root);

			boolean editable = root.isLastVersion();

			WTUser user = (WTUser)SessionHelper.manager.getPrincipal();
			boolean isManager = false;
			if(user != null){
				if(user.getName().equals(root.getCreator().getName())){
					isManager = true;
				}
			}

			model.addObject("root", data);
			model.addObject("editable", editable);
			model.addObject("isManager", isManager);
			model.setViewName("include:/project/template/template_view");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	/**
	 * @desc	: 탬플릿 태스크 뷰
	 * @author	: sangylee
	 * @date	: 2020. 9. 15.
	 * @method	: taskView
	 * @param	: reqMap
	 * @return	: ModelAndView
	 */
	@RequestMapping("/viewTask")
	public ModelAndView viewTask(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.taskView");
		}
		try {
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			String templateOid = StringUtil.checkNull((String) reqMap.get("templateOid"));
			
			
			EProjectTemplate ept = null;
			if(!templateOid.isEmpty()) {
				ept = (EProjectTemplate)CommonUtil.getObject(templateOid);
				if(ept != null) {
					WTUser user = (WTUser)SessionHelper.manager.getPrincipal();
					boolean isManager = false;
					if(user != null){
						if(user.getName().equals(ept.getCreator().getName())){
							isManager = true;
							model.addObject("isManager", isManager);
						}
					}
				}
			}
			
			ETaskNode task = (ETaskNode)CommonUtil.getObject(oid);
			TemplateTaskData taskData = new TemplateTaskData(task);
			
			List<TemplateTaskData> preTaskList = TemplateHelper.manager.getPreTaskList(task);
			List<TemplateTaskData> postTaskList = TemplateHelper.manager.getPostTaskList(task);
			List<TemplateOutputData> outputList = TemplateHelper.manager.getTaskOutputList(task);
			List<Map<String, Object>> roleList = TemplateHelper.manager.getTaskRoleList(task);
			
			model.addObject("oid", oid);
			model.addObject("task", taskData);
			model.addObject("preTaskList", preTaskList);
			model.addObject("postTaskList", postTaskList);
			model.addObject("outputList", outputList);
			model.addObject("roleList", roleList);
			model.setViewName("include:/project/template/template_task");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}

	/**
	 * @desc	: 탬플릿 업데이트 화면
	 * @author	: sangylee
	 * @date	: 2020. 9. 15.
	 * @method	: update
	 * @param	: reqMap
	 * @return	: ModelAndView
	 */
	@RequestMapping("/update")
	public ModelAndView update(@RequestParam Map<String, Object> reqMap) {

		ModelAndView model = new ModelAndView();
		
		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.update");
		}
		try {
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));

			EProjectTemplate template = (EProjectTemplate)CommonUtil.getObject(oid);

			TemplateData data = new TemplateData(template);
			
			model.addObject("template", data);
			model.setViewName("popup:/project/template/template_update");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	/**
	 * @desc	: 탬플릿 업데이트
	 * @author	: sangylee
	 * @date	: 2020. 9. 15.
	 * @method	: updateAction
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/updateAction")
	public Map<String, Object> updateAction(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.updateAction");
		}
		try {
			
			TemplateHelper.service.update(reqMap);
			
			map.put("msg", "수정되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", "closeAndReload");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 탬플릿 삭제
	 * @author	: sangylee
	 * @date	: 2020. 9. 15.
	 * @method	: deleteAction
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/deleteAction")
	public Map<String, Object> deleteAction(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.deleteAction");
		}
		try {
			String msg = TemplateHelper.service.delete(reqMap);

			map.put("msg", "삭제되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/project/template/searchTemplate"));
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 탬플릿 Task 업데이트
	 * @author	: sangylee
	 * @date	: 2020. 9. 15.
	 * @method	: updateTaskAction
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/updateTaskAction")
	public Map<String, Object> updateTaskAction(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.updateTaskAction");
		}
		try {
			ETask task = TemplateHelper.service.updateTask(reqMap);
			
			TemplateTreeData data = new TemplateTreeData(task);
			
			map.put("task", data);
			map.put("msg", "수정되었습니다.");
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 탬플릿 선행 태스크 선택 팝업
	 * @author	: sangylee
	 * @date	: 2020. 9. 15.
	 * @method	: selectPreTask
	 * @param	: reqMap
	 * @return	: ModelAndView
	 */
	@RequestMapping("/selectPreTask")
	public ModelAndView selectPreTask(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();

		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.selectPreTask");
		}
		try {
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			ETask task = (ETask) CommonUtil.getObject(oid);
			
			EProjectNode root = task.getProject();
			
			String rootOid = CommonUtil.getOIDString(root);
			
			model.addObject("oid", oid);
			model.addObject("rootOid", rootOid);
			model.setViewName("popup:/project/template/selectPreTask");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	/**
	 * @desc	: 탬플릿 선행 태스크 선택 액션
	 * @author	: sangylee
	 * @date	: 2020. 9. 15.
	 * @method	: setPreTask
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/setPreTask")
	public Map<String, Object> setPreTask(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.setPreTask");
		}
		try {
			TemplateHelper.service.setPreTask(reqMap);
			
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
	 * @desc	: 탬플릿 선행 태스크 팝업 화면에서 초기 태스크 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 15.
	 * @method	: getPreTask
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getPreTask")
	public Map<String, Object> getPreTask(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.getPreTask");
		}
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			ETask task = (ETask) CommonUtil.getObject(oid);
			
			List<TemplateTaskData> list = TemplateHelper.manager.getPreTaskList(task);
			
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
	 * @desc	: 탬플릿 선행 태스크 팝업 화면에서 후행 태스크 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 15.
	 * @method	: getPreTask
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getPostTask")
	public Map<String, Object> getPostTask(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.getPostTask");
		}
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			ETask task = (ETask) CommonUtil.getObject(oid);
			
			List<TemplateTaskData> list = TemplateHelper.manager.getPostTaskList(task);
			
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
	 * @desc	: 탬플릿 산출물 등록 팝업
	 * @author	: sangylee
	 * @date	: 2020. 9. 15.
	 * @method	: createOutput
	 * @param	: reqMap
	 * @return	: ModelAndView
	 */
	@RequestMapping("/createOutput")
	public ModelAndView createOutput(@RequestParam Map<String, Object> reqMap) {

		ModelAndView model = new ModelAndView();
		
		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.createOutput");
		}
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			model.addObject("oid", oid);
			model.setViewName("popup:/project/template/createOutput");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	/**
	 * @desc	: 탬플릿 산출물 등록 Action
	 * @author	: sangylee
	 * @date	: 2020. 9. 16.
	 * @method	: createOutputAction
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/createOutputAction")
	public Map<String, Object> createOutputAction(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.outputCreateAction");
		}
		try {
			
			OutputHelper.service.saveOutput(reqMap);

			map.put("result", true);
			map.put("msg", "등록하였습니다.");
			map.put("redirectUrl", "close");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 탬플릿 산출물 등록 팝업
	 * @author	: sangylee
	 * @date	: 2020. 9. 15.
	 * @method	: selectRole
	 * @param	: reqMap
	 * @return	: ModelAndView
	 */
	@RequestMapping("/selectRole")
	public ModelAndView selectRole(@RequestParam Map<String, Object> reqMap) {

		ModelAndView model = new ModelAndView();

		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.selectRole");
		}
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			model.addObject("oid", oid);
			model.setViewName("popup:/project/template/selectRole");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	/**
	 * @desc	: 탬플릿 태스크 Role 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 15.
	 * @method	: getTaskRole
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getTaskRole")
	public Map<String, Object> getTaskRole(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.getTaskRole");
		}
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			ETask task = (ETask) CommonUtil.getObject(oid);
			
			List<Map<String, Object>> list = TemplateHelper.manager.getTaskRoleList(task);
			
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
	 * @desc	: 탬플릿 태스크 Role 선택  Action
	 * @author	: sangylee
	 * @date	: 2020. 9. 16.
	 * @method	: selectRoleAction
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/selectRoleAction")
	public Map<String, Object> selectRoleAction(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.selectRoleAction");
		}
		try {
			TemplateHelper.service.editRole(reqMap);
			
			map.put("result", true);
			map.put("msg", "등록하였습니다.");
			map.put("redirectUrl", "close");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 템플릿 산출물 view
	 * @author	: sangylee
	 * @date	: 2020. 9. 18.
	 * @method	: viewOutput
	 * @param	: reqMap
	 * @return	: ModelAndView
	 */
	@RequestMapping("/viewOutput")
	public ModelAndView viewOutput(@RequestParam Map<String, Object> reqMap) {

		ModelAndView model = new ModelAndView();

		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.viewOutput");
		}
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			String taskOid = StringUtil.checkNull((String) reqMap.get("taskOid"));
			
			EOutput output = (EOutput) CommonUtil.getObject(oid);
			
			TemplateOutputData outputData = new TemplateOutputData(output);
			
			model.addObject("oid", oid);
			model.addObject("output", outputData);
			model.addObject("taskOid", taskOid);
			model.setViewName("popup:/project/template/viewOutput");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}

	/**
	 * @desc	: 탬플릿 산출물 수정화면
	 * @author	: sangylee
	 * @date	: 2020. 9. 18.
	 * @method	: modifyOutput
	 * @param	: reqMap
	 * @return	: ModelAndView
	 */
	@RequestMapping("/modifyOutput")
	public ModelAndView modifyOutput(@RequestParam Map<String, Object> reqMap) {

		ModelAndView model = new ModelAndView();

		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.modifyOutput");
		}
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			String taskOid = StringUtil.checkNull((String) reqMap.get("taskOid"));
			
			EOutput output = (EOutput) CommonUtil.getObject(oid);
			
			TemplateOutputData outputData = new TemplateOutputData(output);
			
			String stepOid = outputData.getStepOid();
			
			OutputTypeStep step = (OutputTypeStep) CommonUtil.getObject(stepOid);
			
			String outputStepOid = "";
			String outputChildStepOid = "";
			if(step != null) {
				if(step.getParent() != null) {
					outputStepOid = CommonUtil.getOIDString(step.getParent());
					outputChildStepOid = stepOid;
				} else {
					outputStepOid = stepOid;
				}
			}
			
			model.addObject("oid", oid);
			model.addObject("output", outputData);
			model.addObject("taskOid", taskOid);
			model.addObject("outputStepOid", outputStepOid);
			model.addObject("outputChildStepOid", outputChildStepOid);
			model.setViewName("popup:/project/template/modifyOutput");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	/**
	 * @desc	: 탬플릿 산출물 수정  Action
	 * @author	: sangylee
	 * @date	: 2020. 9. 18.
	 * @method	: modifyOutputAction
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/modifyOutputAction")
	public Map<String, Object> modifyOutputAction(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.outputModifyAction");
		}
		try {
			String msg = OutputHelper.service.updateOutput(reqMap);
			
			map.put("result", true);
			map.put("msg", "수정하였습니다.");
			map.put("redirectUrl", "close");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 탬플릿 산출물 삭제  Action
	 * @author	: sangylee
	 * @date	: 2020. 9. 18.
	 * @method	: deleteOutputAction
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/deleteOutputAction")
	public Map<String, Object> deleteOutputAction(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.outputDeleteAction");
		}
		try {
			String msg = OutputHelper.service.deleteOutput(reqMap);

			map.put("result", true);
			map.put("msg", "삭제하였습니다.");
			map.put("redirectUrl", "close");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	
	/**
	 * @desc 트리 가져오기
	 * @author sangylee
	 * @date 2020. 09. 11.
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getTemplateTree")
	public Map<String, Object> getTemplateTree(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			List<TemplateTreeData> list = TemplateHelper.manager.getTemplateTree(reqMap);
			
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
	 * @desc	: 템플릿 트리 노드 하위 추가
	 * @author	: sangylee
	 * @date	: 2020. 9. 17.
	 * @method	: addChildTask
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/addChildTask")
	public Map<String, Object> addChildTask(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.addChildTask");
		}
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));

			Map<String, Object> returnMap = TemplateHelper.service.addChildTask(reqMap);
			
			if(returnMap.get("msg") != null) {
				map.put("msg", returnMap.get("msg"));
				map.put("result", true);
			} else {
				ETask newTask = (ETask) returnMap.get("newTask");
				
				TemplateTreeData data = new TemplateTreeData(newTask);
				
				map.put("newTask", data);
				map.put("result", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 템플릿 트리 Next 노드 추가
	 * @author	: sangylee
	 * @date	: 2020. 9. 17.
	 * @method	: addNextTask
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/addNextTask")
	public Map<String, Object> addNextTask(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.addNextTask");
		}
		try {
			Map<String, Object> returnMap = TemplateHelper.service.addNextTask(reqMap);
			
			if(returnMap.get("msg") != null) {
				map.put("msg", returnMap.get("msg"));
				map.put("result", true);
			} else {
				ETask newTask = (ETask) returnMap.get("newTask");
				TemplateTreeData data = new TemplateTreeData(newTask);
				map.put("newTask", data);
				
				List<TemplateTreeData> nextTaskList = new ArrayList<>();
				List<ETask> nextList = (List<ETask>) returnMap.get("nextList");
				for(ETask next : nextList) {
					TemplateTreeData nextData = new TemplateTreeData(next);
					
					nextTaskList.add(nextData);
				}
				map.put("nextTaskList", nextTaskList);
				map.put("result", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 템플릿 트리 노드 삭제
	 * @author	: sangylee
	 * @date	: 2020. 9. 17.
	 * @method	: deleteTask
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/deleteTask")
	public Map<String, Object> deleteTask(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.deleteTask");
		}
		try {

			Map<String, Object> returnMap = TemplateHelper.service.deleteTask(reqMap);
			
			if(returnMap.get("msg") != null) {
				map.put("msg", returnMap.get("msg"));
				map.put("result", true);
			} else {
				map.put("result", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 템플릿 트리 노드 위로 이동
	 * @author	: sangylee
	 * @date	: 2020. 9. 18.
	 * @method	: moveUpTask
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/moveUpTask")
	public Map<String, Object> moveUpTask(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.moveUpTask");
		}
		try {

			Map<String, Object> returnMap = TemplateHelper.service.moveUpTask(reqMap);
			
			if(returnMap.get("msg") != null) {
				map.put("msg", returnMap.get("msg"));
			} else {
				ETask task = (ETask) returnMap.get("task");
				ETask prevTask = (ETask) returnMap.get("prevTask");
				
				if(prevTask != null) {
					TemplateTreeData data = new TemplateTreeData(task);
					TemplateTreeData prevData = new TemplateTreeData(prevTask);
					
					map.put("task", data);
					map.put("prevTask", prevData);
					map.put("result", true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 템플릿 트리 노드 아래로 이동
	 * @author	: sangylee
	 * @date	: 2020. 9. 18.
	 * @method	: moveDownTask
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/moveDownTask")
	public Map<String, Object> moveDownTask(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("TemplateController.moveDownTask");
		}
		try {

			Map<String, Object> returnMap = TemplateHelper.service.moveDownTask(reqMap);
			
			if(returnMap.get("msg") != null) {
				map.put("msg", returnMap.get("msg"));
			} else {
				ETask task = (ETask) returnMap.get("task");
				ETask nextTask = (ETask) returnMap.get("nextTask");
				
				if(nextTask != null) {
					TemplateTreeData data = new TemplateTreeData(task);
					TemplateTreeData nextData = new TemplateTreeData(nextTask);
					
					map.put("task", data);
					map.put("nextTask", nextData);
					map.put("result", true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
}
