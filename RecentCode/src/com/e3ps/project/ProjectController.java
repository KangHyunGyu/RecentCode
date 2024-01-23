package com.e3ps.project;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.e3ps.calendar.service.DsHelper;
import com.e3ps.common.drm.E3PSDRMHelper;
import com.e3ps.common.history.service.HistoryHelper;
import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.AccessControlUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.TypeUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.doc.E3PSDocument;
import com.e3ps.project.beans.OutputTypeStepTreeData;
import com.e3ps.project.beans.ProjectData;
import com.e3ps.project.beans.ProjectGanttEditTaskData;
import com.e3ps.project.beans.ProjectGanttLinkData;
import com.e3ps.project.beans.ProjectGanttTaskData;
import com.e3ps.project.beans.ProjectGanttViewTaskData;
import com.e3ps.project.beans.ProjectMasteredLinkData;
import com.e3ps.project.beans.ProjectOutputData;
import com.e3ps.project.beans.ProjectRoleData;
import com.e3ps.project.beans.ProjectScheduleData;
import com.e3ps.project.beans.ProjectStopHistoryData;
import com.e3ps.project.beans.ProjectTaskData;
import com.e3ps.project.beans.ProjectTreeData;
import com.e3ps.project.key.ProjectKey.STATEKEY;
import com.e3ps.project.service.OutputHelper;
import com.e3ps.project.service.OutputTypeHelper;
import com.e3ps.project.service.ProjectHelper;
import com.e3ps.project.service.ProjectMemberHelper;
import com.e3ps.stagegate.service.SgHelper;

import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.content.ContentServerHelper;
import wt.doc.WTDocument;
import wt.epm.EPMDocument;
import wt.epm.EPMDocumentMaster;
import wt.epm.EPMDocumentMasterIdentity;
import wt.fc.PersistenceHelper;
import wt.folder.Folder;
import wt.org.WTUser;
import wt.session.SessionHelper;
import wt.util.EncodingConverter;
import wt.util.WTException;

@Controller
@RequestMapping("/project")
public class ProjectController {
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PROJECT.getName());
	
	public static final String ROOTLOCATION = "/Default";
	
	@RequestMapping("/searchMyProject")
	public ModelAndView searchMyProject(@RequestParam Map<String, Object> reqMap){
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.searchMyProject");
		}
 
		ModelAndView model = new ModelAndView();
		
		model.setViewName("default:/project/searchMyProject");
		
		return model;
	}
	
	@RequestMapping("/searchMyTask")
	public ModelAndView searchMyTask(@RequestParam Map<String, Object> reqMap){
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.searchMyTask");
		}

		ModelAndView model = new ModelAndView();
		
		model.setViewName("default:/project/searchMyTask");
		
		return model;
	}
	
	@RequestMapping("/searchOutputList")
	public ModelAndView searchOutputList(@RequestParam Map<String, Object> reqMap){
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.searchOutputList");
		}
		
		ModelAndView model = new ModelAndView();
		
		model.setViewName("default:/project/searchOutputList");
		
		return model;
	}
	
	@RequestMapping("/holiday")
	public ModelAndView holiday(HttpServletRequest request, HttpServletResponse response){
		
		if (LOGGER.isDebugEnabled()) {
			
			LOGGER.debug("ProjectController.holiday");
		}
		try {
			Map map = request.getParameterMap();
			boolean popup = TypeUtil.booleanValue(request.getParameter("popup"));
			if(popup){
				return new ModelAndView("popup:/project/Holiday", map);
			}
			return new ModelAndView("default:/project/Holiday", map);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
	}
	
	@RequestMapping("/selectPreTask")
	public ModelAndView selectPreTask(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.selectPreTask");
		}
		try {
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			ETask task = (ETask) CommonUtil.getObject(oid);
			
			EProjectNode root = task.getProject();
			
			String rootOid = CommonUtil.getOIDString(root);
			
			model.addObject("oid", oid);
			model.addObject("rootOid", rootOid);
			model.setViewName("popup:/project/selectPreTask");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}

	/**
	 * @desc	: 프로젝트 선행 태스크 선택 액션
	 * @author	: sangylee
	 * @date	: 2020. 10. 5.
	 * @method	: setPreTask
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/setPreTask")
	public Map<String, Object> setPreTask(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.setPreTask");
		}
		try {
			ProjectHelper.service.setPreTask(reqMap);
			
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
	 * @desc	: 프로젝트 선행 태스크 팝업 화면에서 초기 태스크 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 10. 5.
	 * @method	: getPreTask
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getPreTask")
	public Map<String, Object> getPreTask(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.getPreTask");
		}
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			ETask task = (ETask) CommonUtil.getObject(oid);
			
			List<ProjectTaskData> list = ProjectHelper.manager.getPreTaskList(task);
			
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
	 * @desc	: 프로젝트 선행 태스크 팝업 화면에서 후행 태스크 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 10. 5.
	 * @method	: getPreTask
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getPostTask")
	public Map<String, Object> getPostTask(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.getPostTask");
		}
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			ETask task = (ETask) CommonUtil.getObject(oid);
			
			List<ProjectTaskData> list = ProjectHelper.manager.getPostTaskList(task);
			
			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/updateTask")
	public ModelAndView updateTask(@RequestParam Map<String, Object> reqMap) {

		ModelAndView model = new ModelAndView();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.updateTask");
		}
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			ETask task = (ETask) CommonUtil.getObject(oid);
			
			ProjectTaskData taskData = new ProjectTaskData(task);
			
			model.addObject("oid", oid);
			model.addObject("task", taskData);
			model.setViewName("popup:/project/updateTask");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/updateTaskAction")
	public Map<String, Object> updateTaskAction(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.updateTaskAction");
		}
		try {
			ETask task = ProjectHelper.service.updateTaskAction(reqMap);
			
			ProjectTreeData data = new ProjectTreeData(task);
			
			map.put("task", data);
			map.put("result", true);
			map.put("msg", "수정하였습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/projectOutputList")
	public ModelAndView projectOutputList(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.projectOutputList");
		}
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			model.addObject("oid", oid);
			
			model.setViewName("include:/project/projectOutputList");
			
			return model;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
	}
	
	@ResponseBody
	@RequestMapping("/projectOutputListAction")
	public Map<String, Object> projectOutputListAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			List<ProjectOutputData> list = ProjectHelper.manager.getProjectOutputList(reqMap);
			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
//	@RequestMapping("/masteredLinkList")
//	public ModelAndView masteredLinkList(HttpServletRequest request, HttpServletResponse response) {
//
//		if (LOGGER.isDebugEnabled()) {
//			LOGGER.debug("ProjectController.masteredLinkList");
//		}
//		try {
//			Map map = request.getParameterMap();
//			return new ModelAndView("include:/project/MasteredLinkList", map);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new ControllerException(e);
//		}
//	}
	@RequestMapping("/masteredLinkList")
	public ModelAndView masteredLinkList(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.masteredLinkList");
		}
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			String type = StringUtil.checkNull((String) reqMap.get("type"));
			
			model.addObject("oid", oid);
			model.addObject("type", type);
			model.setViewName("include:/project/masteredLinkList");
			
			return model;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
	}
	
	@ResponseBody
	@RequestMapping("/masteredLinkListAction")
	public Map<String, Object> masteredLinkListAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			List<ProjectMasteredLinkData> list = ProjectHelper.manager.getMasteredLinkList(reqMap);
			
			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/editMember")
	public ModelAndView editMember(@RequestParam Map<String, Object> reqMap) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.editMember");
		}
		
		ModelAndView model = new ModelAndView();
		
		try {
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));

			EProject project = (EProject) CommonUtil.getObject(oid);
			
			List<ProjectRoleData> members = ProjectMemberHelper.manager.getRoleUserList(project);
			
			model.addObject("oid", oid);
			model.addObject("members", members);
			model.setViewName("popup:/project/editMember");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	@RequestMapping("/editMember2")
	public ModelAndView editMember2(@RequestParam Map<String, Object> reqMap) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.editMember2");
		}
		
		ModelAndView model = new ModelAndView();
		
		try {
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));

			EProject project = (EProject) CommonUtil.getObject(oid);
			
			List<ProjectRoleData> members = ProjectMemberHelper.manager.getRoleUserList(project);
			
			model.addObject("oid", oid);
//			model.addObject("members", members);
			model.setViewName("popup:/project/editMember2");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/getEditMemberList")
	public Map<String, Object> getEditMemberList(@RequestBody Map<String, Object> reqMap) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.getEditMemberList");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));

			EProject project = (EProject) CommonUtil.getObject(oid);
			List<ProjectRoleData> members = ProjectMemberHelper.manager.getRoleUserList(project);

			map.put("list", members);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/editMemberAction")
	public Map<String, Object> editMemberAction(@RequestBody Map<String, Object> reqMap) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.editMemberAction");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String msg = ProjectHelper.service.editMember(reqMap);

			map.put("msg", "수정하였습니다.");
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/editMemberAction2")
	public Map<String, Object> editMemberAction2(@RequestBody Map<String, Object> reqMap) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.editMemberAction");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String msg = ProjectHelper.service.editMember2(reqMap);
			
			map.put("msg", "수정하였습니다.");
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/startProjectAction")
	public Map<String, Object> startProjectAction(@RequestBody Map<String, Object> reqMap) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.startProjectAction");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			EProject project = (EProject)CommonUtil.getObject(oid);
			
			Timestamp startDate = DateUtil.getCurrentTimestamp();
			
			project.setStartDate(startDate);
			
			project = (EProject) PersistenceHelper.manager.save(project);
			
			ProjectHelper.service.setState(project, STATEKEY.PROGRESS);
			
			map.put("msg", "시작되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/project/viewMain") + "?oid=" + oid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/updateProject")
	public ModelAndView updateProject(@RequestParam Map<String, Object> reqMap) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.updateProject");
		}
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			EProject project = (EProject) CommonUtil.getObject(oid);
					
			ProjectData projectData = new ProjectData(project);
			
			model.addObject("oid", oid);
			model.addObject("project", projectData);
			model.setViewName("popup:/project/updateProject");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/updateProjectAction")
	public Map<String, Object> updateProjectAction(@RequestBody Map<String, Object> reqMap) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.updateProjectAction");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			String msg = ProjectHelper.service.update(reqMap);
			
			map.put("msg", "수정하였습니다.");
			map.put("result", true);
			map.put("redirectUrl", "closeAndReload");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/deleteProjectAction")
	public Map<String, Object> deleteProjectAction(@RequestBody Map<String, Object> reqMap) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.deleteProjectAction");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String msg = ProjectHelper.service.delete(reqMap);		
			
			map.put("msg", "삭제되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/project/searchProject"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return map;
	}
	
	@RequestMapping("/ganttChart")
	public ModelAndView ganttChart(@RequestParam Map<String, Object> reqMap) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.ganttChart");
		}
		
		ModelAndView model = new ModelAndView();
		
		try {

			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			model.addObject("oid", oid);
			model.setViewName("popup:/project/gantt/Gantt");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	@RequestMapping("/projectHistory")
	public ModelAndView projectHistory(@RequestParam Map<String, Object> reqMap) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.projectHistory");
		}
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			EProject project = (EProject) CommonUtil.getObject(oid);
			
			String projectCode = project.getCode();
			
			List<ProjectData> projectList = ProjectHelper.manager.getProjectListByCode(projectCode);
			
			model.addObject("projectList", projectList);
			model.setViewName("popup:/project/projectHistory");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	@RequestMapping("/stopProject")
	public ModelAndView stopProject(@RequestParam Map<String, Object> reqMap) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.stopProject");
		}
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			EProject project = (EProject) CommonUtil.getObject(oid);
			
			ProjectData projectData = new ProjectData(project); 
			
			model.addObject("oid", oid);
			model.addObject("project", projectData);
			model.setViewName("popup:/project/stopProject");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/stopProjectAction")
	public Map<String, Object> stopProjectAction(@RequestBody Map<String, Object> reqMap) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.stopProject");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			String msg = ProjectHelper.service.stopProject(reqMap);
			
			map.put("msg", "중단하였습니다.");
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/restartProject")
	public ModelAndView restartProject(@RequestParam Map<String, Object> reqMap) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.restartProject");
		}
		
		ModelAndView model = new ModelAndView();
		
		try {
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			EProject project = (EProject) CommonUtil.getObject(oid);
			
			ProjectData projectData = new ProjectData(project); 
			
			model.addObject("oid", oid);
			model.addObject("project", projectData);
			model.setViewName("popup:/project/restartProject");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/restartProjectAction")
	public Map<String, Object> restartProjectAction(@RequestBody Map<String, Object> reqMap) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.restartProjectAction");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String msg = ProjectHelper.service.restartProject(reqMap);
			
			map.put("msg", "재시작하였습니다.");
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/checkIn")
	public ModelAndView checkIn(@RequestParam Map<String, Object> reqMap) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.checkIn");
		}
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			EProject project = (EProject) CommonUtil.getObject(oid);
			
			ProjectData projectData = new ProjectData(project); 
			
			model.addObject("oid", oid);
			model.addObject("project", projectData);
			model.setViewName("popup:/project/checkIn");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/checkInAction")
	public Map<String, Object> checkInAction(@RequestBody Map<String, Object> reqMap) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.checkInAction");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			HashMap<String, Object> returnMap = ProjectHelper.service.checkinProject(reqMap);
			
			map.put("msg", "체크인하였습니다.");
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/checkoutAction")
	public Map<String, Object> checkoutAction(@RequestBody Map<String, Object> reqMap) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.checkoutAction");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			EProject project = ProjectHelper.service.checkoutProject(reqMap);
			
			String oid = CommonUtil.getOIDString(project);
			
			map.put("msg", "체크아웃되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/project/viewMain") + "?oid=" + oid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/undoCheckoutAction")
	public Map<String, Object> undoCheckoutAction(@RequestBody Map<String, Object> reqMap) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.undoCheckoutAction");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			EProject project = ProjectHelper.service.undoCheckoutProject(reqMap);
			
			String oid = CommonUtil.getOIDString(project);
			
			map.put("msg", "체크아웃 취소되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/project/viewMain") + "?oid=" + oid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/viewMain")
	public ModelAndView viewMain(@RequestParam Map<String, Object> reqMap){
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.viewMain");
		}
		
		ModelAndView model = new ModelAndView();
		
		try {
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			ScheduleNode node = (ScheduleNode) CommonUtil.getObject(oid);
			EProject project = null;
			if(node instanceof EProject) {
				project = (EProject) node;
			} else if(node instanceof ETask) {
				ETask task = (ETask) node;
				project = (EProject) task.getProject();
				model.addObject("taskOid", oid);
			}
			
			boolean isAuth = ProjectHelper.manager.isAuth(project);
			boolean isEditState = ProjectHelper.manager.isEditState(project);
			
			
			boolean isPSO = "PSO".equals(project.getOutputType().toString());
			
			model.addObject("oid", CommonUtil.getOIDString(project));
			model.addObject("isPSO", isPSO);
			model.addObject("isAuth", isAuth);
			model.addObject("isEditState", isEditState);
			model.addObject("sg", false);
			boolean sg = SgHelper.manager.checkStageGateList(project.getCode());
			if(sg) {
				model.addObject("sg", true);
			}
			model.setViewName("default:/project/viewProject");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	/**
	 * @desc	: 프로젝트 Root 뷰
	 * @author	: sangylee
	 * @date	: 2020. 9. 24.
	 * @method	: view
	 * @param	: reqMap
	 * @return	: ModelAndView
	 */
	@RequestMapping("/view")
	public ModelAndView view(@RequestParam Map<String, Object> reqMap){
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.viewProject");
		}
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			boolean isAuth = Boolean.valueOf(StringUtil.checkNull((String) reqMap.get("isAuth")));
			boolean isEditState = Boolean.valueOf(StringUtil.checkNull((String) reqMap.get("isEditState")));
			
			EProject root = (EProject) CommonUtil.getObject(oid);
			
			ProjectData data = new ProjectData(root);
			
			model.addObject("oid", oid);
			model.addObject("isAuth", isAuth);
			model.addObject("isEditState", isEditState);
			model.addObject("root", data);
			model.setViewName("include:/project/project_view");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	@RequestMapping("/viewTask")
	public ModelAndView viewTask(@RequestParam Map<String, Object> reqMap){
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.viewTask");
		}
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			boolean isAuth = Boolean.valueOf(StringUtil.checkNull((String) reqMap.get("isAuth")));
			boolean isEditState = Boolean.valueOf(StringUtil.checkNull((String) reqMap.get("isEditState")));
			
			ETask task = (ETask)CommonUtil.getObject(oid);
			ProjectTaskData taskData = new ProjectTaskData(task);
			
			boolean isTaskAuth = ProjectHelper.manager.isTaskAuth(task);
			
			model.addObject("oid", oid);
			model.addObject("task", taskData);
			model.addObject("isAuth", isAuth);
			model.addObject("isTaskAuth", isTaskAuth);
			model.addObject("isEditState", isEditState);
			model.addObject("isTaskEdit", isAuth && isEditState);
			model.setViewName("include:/project/project_task");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/startTask")
	public Map<String, Object> startTask(@RequestBody Map<String, Object> reqMap){
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.startTask");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			ETask task = ProjectHelper.service.startTask(oid);
			
			map.put("oid", CommonUtil.getOIDString(task));
			map.put("result", true);
			map.put("msg", "시작되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/deleteOutputLink")
	public Map<String, Object> deleteOutputLink(@RequestBody Map<String, Object> reqMap){
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.deleteOutputLink");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String msg = OutputHelper.service.deleteOutputDocumentLink(reqMap);
			
			map.put("result", true);
			map.put("msg", "링크가 삭제되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/saveCompletion")
	public Map<String, Object> saveCompletion(@RequestBody Map<String, Object> reqMap){
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.saveCompletion");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			ETask task = ProjectHelper.service.saveCompletion(reqMap);
			
			ProjectTreeData data = new ProjectTreeData(task);
			
			map.put("task", data);
			map.put("result", true);
			map.put("msg", "진행율이 수정되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	
	@RequestMapping("/changeStartDate")
	public ModelAndView changeStartDate(@RequestParam Map<String, Object> reqMap){
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.changeStartDate");
		}
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			ETaskNode task = (ETaskNode)CommonUtil.getObject(oid);
			ProjectTaskData taskData = new ProjectTaskData(task);
			
			model.addObject("oid", oid);
			model.addObject("task", taskData);
			model.setViewName("popup:/project/changeStartDate");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/changeStartDateAction")
	public Map<String, Object> changeStartDateAction(@RequestBody Map<String, Object> reqMap){
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.changeStartDateAction");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			ETask task = ProjectHelper.service.changeStartDate(reqMap);
			
			ProjectTreeData data = new ProjectTreeData(task);
			
			map.put("task", data);
			map.put("result", true);
			map.put("msg", "수정하였습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/workComplete")
	public ModelAndView workComplete(@RequestParam Map<String, Object> reqMap){
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.workComplete");
		}
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			ETaskNode task = (ETaskNode)CommonUtil.getObject(oid);
			ProjectTaskData taskData = new ProjectTaskData(task);
			
			model.addObject("oid", oid);
			model.addObject("task", taskData);
			model.setViewName("popup:/project/workComplete");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}

		return model;
	}
	
	@ResponseBody
	@RequestMapping("/workCompleteAction")
	public Map<String, Object> workCompleteAction(@RequestBody Map<String, Object> reqMap){
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.workCompleteAction");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			ETask task = ProjectHelper.service.workComplete(reqMap);
			
			ProjectTreeData data = new ProjectTreeData(task);
			
			map.put("task", data);
			map.put("result", true);
			map.put("msg", "작업을 완료하였습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/cancelComplete")
	public Map<String, Object> cancelComplete(@RequestBody Map<String, Object> reqMap) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.cancelComplete");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			String msg = ProjectHelper.service.cancelComplete(reqMap);
			
			map.put("result", true);
			map.put("msg", "완료 취소하였습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/createOutputDocLink")
	public Map<String, Object> createOutputDocLink(@RequestBody Map<String, Object> reqMap) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.createOutputDocLink");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String msg = OutputHelper.service.linkOutputDocument(reqMap);
			
			map.put("result", true);
			map.put("msg", "연결되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}

		return map;
	}
	
	@ResponseBody
	@RequestMapping("/createMasterLink")
	public Map<String, Object> createMasterLink(@RequestBody Map<String, Object> reqMap) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.createMasterLink");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			ProjectHelper.service.createMasterLink(reqMap);
			
			map.put("result", true);
			map.put("msg", "등록되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/deleteMasterLink")
	public Map<String, Object> deleteMasterLink(@RequestBody Map<String, Object> reqMap) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.deleteMasterLink");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String oid = (String) reqMap.get("oid");
			
			ProjectHelper.service.deleteMasterLink(oid);
			
			map.put("result", true);
			map.put("msg", "삭제되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/createProject")
	public ModelAndView createProject(@RequestParam Map<String, Object> reqMap) throws WTException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.createProject");
		}
		
		ModelAndView model = new ModelAndView();
		
		model.setViewName("default:/project/createProject");
		
		return model;
	}

	
	@RequestMapping("/searchProject")
	public ModelAndView searchProject(@RequestParam Map<String, Object> reqMap) throws WTException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.searchProject");
		}
		
		ModelAndView model = new ModelAndView();
		
		//model.setViewName("default:/project/searchMyProject");
		//
		//boolean isAdmin = CommonUtil.isAdmin();
		//if(isAdmin) {
		model.setViewName("default:/project/searchProject");
		//}
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/searchProjectAction")
	public Map<String, Object> searchProjectAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			List<ProjectData> list = ProjectHelper.manager.getProjectList(reqMap);
			
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
	@RequestMapping("/searchProjectScrollAction")
	public Map<String, Object> searchProjectScrollAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			map = ProjectHelper.manager.getProjectScrollList(reqMap);
			
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/createProjectAction")
	public Map<String, Object> createProjectAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
//			String number = StringUtil.checkNull((String)reqMap.get("number"));
//			
//			List<ProjectData> list = ProjectHelper.manager.getProjectListByCode(number);
//			
//			if(list.size() > 0) {
//				map.put("result", false);
//				map.put("msg", "프로젝트 번호가 이미 존재합니다.");
//				return map;
//			}
			
			ProjectHelper.service.createProject(reqMap);
			
			map.put("msg", "등록되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/project/searchProject"));
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/downloadContent")
	public void downloadContent(HttpServletRequest request, HttpServletResponse response) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("DownloadController.downloadContent");
		}
		
		LOGGER.debug("DownloadController.downloadContent");
		try {
			String holderOid = request.getParameter("holderOid");
			String appOid = request.getParameter("appOid");

			ContentHolder contentHolder = (ContentHolder) WCUtil.getObject(holderOid);
			ApplicationData appData = (ApplicationData) WCUtil.getObject(appOid);
			
			if( "{$CAD_NAME}".equals(appData.getFileName())){
				EPMDocument epm = (EPMDocument)contentHolder;
				appData.setFileName(epm.getCADName());
			}
			
			URL durl = ContentHelper.service.getDownloadURL(contentHolder, appData);

			response.sendRedirect(durl.toString()); 
			
			WTUser user = (WTUser) SessionHelper.getPrincipal();

			holderOid = CommonUtil.getOIDString(contentHolder);
//			String ip = request.getHeader("X-FORWARDED-FOR");
//	        if (ip == null)
//	            ip = request.getRemoteAddr();
//			ConcurrentHashMap<String, Object> hash = new ConcurrentHashMap<String, Object>();
//			hash.put("dOid", holderOid);
//			hash.put("userId", user.getFullName());
//			hash.put("conIP", ip);
//			DownloadHelper.service.createDownloadHistory(hash);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
	}
	@RequestMapping("/addData")
	public ModelAndView addData(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		LOGGER.debug("oid :: "+oid);
		String type = "single";
		String title = "관련 문서";
		String objType = "doc";
		String pageName = "relatedDoc";
		String autoGridHeight = StringUtil.checkReplaceStr((String) reqMap.get("autoGridHeight"), "false");
		String gridHeight = StringUtil.checkReplaceStr((String) reqMap.get("gridHeight"), "55");
		String moduleType = "doc";
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
		
		model.setViewName("popup:/common/include/addObject");
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/searchMyProjectAction")
	public Map<String, Object> searchMyProjectAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			List<ProjectData> list = ProjectHelper.manager.getMyProjectList(reqMap);
			
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
	@RequestMapping("/searchMyTaskAction")
	public Map<String, Object> searchMyTaskAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			List<ProjectTaskData> list = ProjectHelper.manager.getMyTaskList(reqMap);
			
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
	@RequestMapping("/searchOutputAction")
	public Map<String, Object> searchOutputAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			List<ProjectOutputData> list = ProjectHelper.manager.getOutputList(reqMap);
			
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
	@RequestMapping("/searchOutputScrollAction")
	public Map<String, Object> searchOutputScrollAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			map = ProjectHelper.manager.getOutputScrollList(reqMap);
			
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/addDocOutputPopup")
	public ModelAndView addDocOutputPopup(@RequestParam Map<String, Object> reqMap) throws Exception {
		ModelAndView model = new ModelAndView();
		
		String type = StringUtil.checkNull((String) reqMap.get("type"));
		String outputOid = StringUtil.checkNull((String) reqMap.get("outputOid"));
		String foid = StringUtil.checkNull((String) reqMap.get("foid"));
		String pageName = StringUtil.checkNull((String) reqMap.get("pageName"));
		
		Folder folder = (Folder)CommonUtil.getObject(foid);
		String location = folder.getFolderPath();
		String locationDisplay = location.replace("/Default", "");
		
		model.addObject("type", type);
		model.addObject("location", location);
		model.addObject("pageName", pageName);
		model.addObject("outputOid", outputOid);
		model.addObject("locationDisplay", locationDisplay);
		
		model.setViewName("popup:/doc/addDocOutputPopup");
		
		return model;
	}
	
	/**
	 * @desc	: 관련 프로젝트 autocomplete 검색 액션
	 * @author	: sangylee
	 * @date	: 2020. 9. 4.
	 * @method	: searchRelatedProject
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/searchRelatedProject")
	public Map<String,Object> searchRelatedProject(@RequestBody Map<String, Object> reqMap) {
		Map<String,Object> map = new HashMap<>();
		try {
			List<ProjectData> list = ProjectHelper.manager.getSearchRelatedProject(reqMap);
			map.put("list", list);
			map.put("result", true);
		} catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
	
	/**
	 * @desc	: 관련 프로젝트 include
	 * @author	: sangylee
	 * @date	: 2020. 9. 4.
	 * @method	: relatedProject
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_relatedProject")
	public ModelAndView relatedProject(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		model.addObject("oid", oid);
		
		model.setViewName("include:/project/include/relatedProject");
		
		return model;
	}
	
	/**
	 * @desc	: 관련 프로젝트 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 4.
	 * @method	: getRelatedProject
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getRelatedProject")
	public static Map<String, Object> getRelatedProject(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<>();
		
		try {
			List<ProjectData> list = ProjectHelper.manager.getRelatedProject(reqMap);
			
			map.put("list", list);
			map.put("result",true);
		} catch (Exception e) {
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 프로젝트 산출물 인증 타입 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 15.
	 * @method	: getOutputTypeList
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getOutputTypeList")
	public Map<String, Object> getOutputTypeList(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			List<Map<String, Object>> list = OutputHelper.manager.getOutputType();
			
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
	 * @desc	: 프로젝트 산출물 인증 타입 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 9. 15.
	 * @method	: getOutputStepList
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getOutputStepList")
	public Map<String, Object> getOutputStepList(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String key = StringUtil.checkNull((String) reqMap.get("key"));
			String parentOid = StringUtil.checkNull((String) reqMap.get("parentOid"));
			
			List<Map<String, Object>> list = OutputHelper.manager.getOutputStep(key, parentOid);
			
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
	 * @desc 트리 가져오기
	 * @author sangylee
	 * @date 2020. 09. 25.
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getProjectTreeAUI")
	public Map<String, Object> getProjectTreeAUI(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			List<ProjectTreeData> list = ProjectHelper.manager.getProjectTreeAUI(reqMap);
			
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
	 * @desc 트리 가져오기
	 * @author sangylee
	 * @date 2020. 09. 25.
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getProjectTree")
	public Map<String, Object> getProjectTree(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			List<ProjectTreeData> list = ProjectHelper.manager.getProjectTree(reqMap);
			
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
	 * @desc 프로젝트 진행현황 include
	 * @author sangylee
	 * @date 2020. 09. 28.
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_viewSchedule")
	public ModelAndView viewSchedule(@RequestParam Map<String, Object> reqMap){
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.viewSchedule");
		}
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			boolean isAuth = Boolean.valueOf(StringUtil.checkNull((String) reqMap.get("isAuth")));
			boolean proAuth = Boolean.valueOf(StringUtil.checkNull((String) reqMap.get("proAuth")));
			
			
			ScheduleNode node = (ScheduleNode) CommonUtil.getObject(oid);
			boolean isTaskAuth = false;
			if(node instanceof ETask) {
				isTaskAuth = ProjectHelper.manager.isTaskAuth((ETask)node);
			}
			System.out.println("oid : " + oid);
			ProjectScheduleData data = new ProjectScheduleData(node);
			
			model.addObject("oid", oid);
			model.addObject("schedule", data);
			model.addObject("isAuth", isAuth);
			model.addObject("proAuth", proAuth);
			model.addObject("isTaskAuth", isTaskAuth);
			model.setViewName("include:/project/include/viewSchedule");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	/**
	 * @desc 프로젝트 하위 태스크 진행현황 include
	 * @author sangylee
	 * @date 2020. 09. 28.
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_viewChild")
	public ModelAndView viewChild(@RequestParam Map<String, Object> reqMap){
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.viewChild");
		}
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			List<ProjectTaskData> taskList = ProjectHelper.manager.getProjectTaskDataChildren(oid);
			
			model.addObject("taskList", taskList);
			model.setViewName("include:/project/include/viewChild");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	/**
	 * @desc 프로젝트 구성원 include
	 * @author sangylee
	 * @date 2020. 09. 29.
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_viewMember")
	public ModelAndView viewMember(@RequestParam Map<String, Object> reqMap){
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.viewMember");
		}
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			boolean isAuth = Boolean.valueOf(StringUtil.checkNull((String) reqMap.get("isAuth")));
			boolean isEditState = Boolean.valueOf(StringUtil.checkNull((String) reqMap.get("isEditState")));
			
			EProject project = (EProject) CommonUtil.getObject(oid);
			
			List<ProjectRoleData> members = ProjectMemberHelper.manager.getRoleUserList(project);
			
			model.addObject("oid", oid);
			model.addObject("isAuth", isAuth);
			model.addObject("isEditState", isEditState);
			model.addObject("members", members);
			model.setViewName("include:/project/include/viewMember");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	/**
	 * @desc 프로젝트 중단이력 include
	 * @author sangylee
	 * @date 2020. 09. 29.
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_viewStopHistory")
	public ModelAndView viewStopHistory(@RequestParam Map<String, Object> reqMap){
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.viewStopHistory");
		}
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			EProject project = (EProject) CommonUtil.getObject(oid);
			
			List<ProjectStopHistoryData> stopHistoryList = ProjectHelper.manager.getProjectStopHistoryList(project);
			
			model.addObject("oid", oid);
			model.addObject("stopHistoryList", stopHistoryList);
			model.setViewName("include:/project/include/viewStopHistory");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	/**
	 * @desc 프로젝트 태스크 담당자 include
	 * @author sangylee
	 * @date 2020. 09. 29.
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_viewOwner")
	public ModelAndView viewOwner(@RequestParam Map<String, Object> reqMap){
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.viewOwner");
		}
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			boolean isTaskEdit = Boolean.valueOf(StringUtil.checkNull((String) reqMap.get("isTaskEdit")));
			
			ETask task = (ETask) CommonUtil.getObject(oid);
			
			List<ProjectRoleData> owners = ProjectMemberHelper.manager.getOwner(task);
			
			model.addObject("oid", oid);
			model.addObject("isTaskEdit", isTaskEdit);
			model.addObject("owners", owners);
			model.setViewName("include:/project/include/viewOwner");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	/**
	 * @desc 프로젝트 태스크 산출물 include
	 * @author sangylee
	 * @date 2020. 09. 29.
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_viewTaskOutput")
	public ModelAndView viewTaskOutput(@RequestParam Map<String, Object> reqMap){
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.viewTaskOutput");
		}
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			boolean isTaskAuth = Boolean.valueOf(StringUtil.checkNull((String) reqMap.get("isTaskAuth")));
			boolean isTaskEdit = Boolean.valueOf(StringUtil.checkNull((String) reqMap.get("isTaskEdit")));
			
			ETask task = (ETask) CommonUtil.getObject(oid);
			
			ProjectTaskData taskData = new ProjectTaskData(task);
			
			boolean chkObj = AccessControlUtil.checkPermissionForObject(E3PSDocument.class);
			boolean isAllOutputRegist = true;
			List<ProjectOutputData> outputList = ProjectHelper.manager.getTaskOutputList(task);
			for(ProjectOutputData outputData : outputList) {
				EOutput output = (EOutput) CommonUtil.getObject(outputData.getOid());
				if(chkObj) {
					WTDocument doc = output.getDocument();
					if(doc == null || !"APPROVED".equals(doc.getState().toString())) {
						isAllOutputRegist = false;
					}
				}else {
					isAllOutputRegist = false;
				}
			}
			
			model.addObject("chkObj", chkObj);
			model.addObject("oid", oid);
			model.addObject("isTaskAuth", isTaskAuth);
			model.addObject("isTaskEdit", isTaskEdit);
			model.addObject("task", taskData);
			model.addObject("isAllOutputRegist", isAllOutputRegist);
			model.addObject("outputList", outputList);
			model.setViewName("include:/project/include/viewTaskOutput");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	/**
	 * @desc 프로젝트 선후행 태스크 include
	 * @author sangylee
	 * @date 2020. 10. 5.
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_viewPreTask")
	public ModelAndView viewPreTask(@RequestParam Map<String, Object> reqMap){
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.viewPreTask");
		}
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			boolean isTaskEdit = Boolean.valueOf(StringUtil.checkNull((String) reqMap.get("isTaskEdit")));
			boolean isChild = Boolean.valueOf(StringUtil.checkNull((String) reqMap.get("isChild")));
			
			ETask task = (ETask) CommonUtil.getObject(oid);
			
			List<ProjectTaskData> preTaskList =  ProjectHelper.manager.getPreTaskList(task);
			List<ProjectTaskData> postTaskList = ProjectHelper.manager.getPostTaskList(task);
			
			model.addObject("oid", oid);
			model.addObject("isTaskEdit", isTaskEdit);
			model.addObject("isChild", isChild);
			model.addObject("preTaskList", preTaskList);
			model.addObject("postTaskList", postTaskList);
			model.setViewName("include:/project/include/viewPreTask");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	/**
	 * @desc	: 프로젝트 산출물 등록 팝업
	 * @author	: sangylee
	 * @date	: 2020. 10. 5.
	 * @method	: selectRole
	 * @param	: reqMap
	 * @return	: ModelAndView
	 */
	@RequestMapping("/selectRole")
	public ModelAndView selectRole(@RequestParam Map<String, Object> reqMap) {

		ModelAndView model = new ModelAndView();

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.selectRole");
		}
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			model.addObject("oid", oid);
			model.setViewName("popup:/project/selectRole");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	/**
	 * @desc	: 프로젝트 태스크 Role 가져오기
	 * @author	: sangylee
	 * @date	: 2020. 10. 5.
	 * @method	: getTaskRole
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getTaskRole")
	public Map<String, Object> getTaskRole(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.getTaskRole");
		}
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			ETask task = (ETask) CommonUtil.getObject(oid);
			
			List<Map<String, Object>> list = ProjectHelper.manager.getTaskRoleList(task);
			
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
	 * @desc	: 프로젝트 태스크 Role 선택  Action
	 * @author	: sangylee
	 * @date	: 2020. 10. 5.
	 * @method	: selectRoleAction
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/selectRoleAction")
	public Map<String, Object> selectRoleAction(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.selectRoleAction");
		}
		try {
			ProjectHelper.service.editRole(reqMap);
			
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
	 * @desc	: 프로젝트 산출물 등록 팝업
	 * @author	: sangylee
	 * @date	: 2020. 10. 5.
	 * @method	: createOutput
	 * @param	: reqMap
	 * @return	: ModelAndView
	 */
	@RequestMapping("/createOutput")
	public ModelAndView createOutput(@RequestParam Map<String, Object> reqMap) {

		ModelAndView model = new ModelAndView();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.createOutput");
		}
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			model.addObject("oid", oid);
			model.setViewName("popup:/project/createOutput");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	/**
	 * @desc	: 프로젝트 산출물 등록 Action
	 * @author	: sangylee
	 * @date	: 2020. 10. 5.
	 * @method	: createOutputAction
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/createOutputAction")
	public Map<String, Object> createOutputAction(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.outputCreateAction");
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
	 * @desc	: 프로젝트 산출물 수정화면
	 * @author	: sangylee
	 * @date	: 2020. 10. 5.
	 * @method	: modifyOutput
	 * @param	: reqMap
	 * @return	: ModelAndView
	 */
	@RequestMapping("/modifyOutput")
	public ModelAndView modifyOutput(@RequestParam Map<String, Object> reqMap) {

		ModelAndView model = new ModelAndView();

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.modifyOutput");
		}
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			String taskOid = StringUtil.checkNull((String) reqMap.get("taskOid"));
			
			EOutput output = (EOutput) CommonUtil.getObject(oid);
			
			ProjectOutputData outputData = new ProjectOutputData(output);
			
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
			model.setViewName("popup:/project/modifyOutput");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	/**
	 * @desc	: 프로젝트 산출물 수정  Action
	 * @author	: sangylee
	 * @date	: 2020. 10. 5.
	 * @method	: modifyOutputAction
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/modifyOutputAction")
	public Map<String, Object> modifyOutputAction(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.outputModifyAction");
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
	 * @date	: 2020. 10. 5.
	 * @method	: deleteOutputAction
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/deleteOutputAction")
	public Map<String, Object> deleteOutputAction(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.outputDeleteAction");
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
	 * @desc Step 트리 가져오기
	 * @author sangylee
	 * @date 2020. 10. 07.
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getStepTree")
	public Map<String, Object> getStepTree(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			List<OutputTypeStepTreeData> list = OutputTypeHelper.manager.getStepTree(reqMap);
			
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
	 * @desc	: Project Gantt Chart Editor
	 * @author	: sangylee
	 * @date	: 2020. 10. 21.
	 * @method	: editGanttChart
	 * @param	: reqMap
	 * @return	: ModelAndView
	 */
	@RequestMapping("/editGanttChart")
	public ModelAndView editGanttChart(@RequestParam Map<String, Object> reqMap) {

		ModelAndView model = new ModelAndView();

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.editGanttChart");
		}
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			EProject project = (EProject) CommonUtil.getObject(oid);
			
			ProjectData data = new ProjectData(project);
			
			model.addObject("project", data);
			model.addObject("oid", oid);
			model.setViewName("empty:/project/gantt/editGanttChart");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	/**
	 * @desc gantt chart load(editor)
	 * @author sangylee
	 * @date 2020. 10. 21.
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getGanttEditList")
	public Map<String, Object> getGanttEditList(@RequestBody Map<String, Object> reqMap) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			
			List<ProjectGanttEditTaskData> tasks = ProjectHelper.manager.getProjectGanttEditTask(reqMap);
			List<ProjectGanttLinkData> links = ProjectHelper.manager.getProjectGanttLink(reqMap);
			
			map.put("tasks", tasks);
			map.put("links", links);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return map;
	}
	
	/**
	 * @desc gantt chart load(view)
	 * @author sangylee
	 * @date 2020. 11. 10.
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getGanttViewList")
	public Map<String, Object> getGanttViewList(@RequestBody Map<String, Object> reqMap) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			
			List<ProjectGanttViewTaskData> tasks = ProjectHelper.manager.getProjectGanttViewTask(reqMap);
			List<ProjectGanttLinkData> links = ProjectHelper.manager.getProjectGanttLink(reqMap);
			
			map.put("tasks", tasks);
			map.put("links", links);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return map;
	}
	
	/**
	 * @desc gantt chart add task
	 * @author sangylee
	 * @date 2020. 11. 3.
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/addGanttTaskAction")
	public Map<String, Object> addGanttTaskAction(@RequestBody Map<String, Object> reqMap) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();

		try {

			Iterator<Entry<String,Object>> it = reqMap.entrySet().iterator();
			while(it.hasNext()) {
				Entry<String,Object> ent = it.next();
				LOGGER.debug("key : " + ent.getKey() + ", value : " + ent.getValue());
			}
			
			Map<String, Object> taskMap = (Map<String, Object>) reqMap.get("task");
			
			Iterator<Entry<String,Object>> it2 = taskMap.entrySet().iterator();
			while(it2.hasNext()) {
				Entry<String,Object> ent = it2.next();
				LOGGER.debug("key : " + ent.getKey() + ", value : " + ent.getValue());
			}
			
			ETask task = ProjectHelper.service.addGanttTaskAction(reqMap);
			
			ProjectGanttTaskData data = new ProjectGanttTaskData(task);
			data.setType(ProjectGanttTaskData.TASK);
			
			map.put("task", data);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return map;
	}
	
	/**
	 * @desc gantt chart update task
	 * @author sangylee
	 * @date 2020. 10. 28.
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/updateGanttTaskAction")
	public Map<String, Object> updateGanttTaskAction(@RequestBody Map<String, Object> reqMap) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			
			ProjectHelper.service.updateGanttTaskAction(reqMap);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return map;
	}
	
	/**
	 * @desc gantt chart delete task
	 * @author sangylee
	 * @date 2020. 10. 29.
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/deleteGanttTaskAction")
	public Map<String, Object> deleteGanttTaskAction(@RequestBody Map<String, Object> reqMap) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			
			ProjectHelper.service.deleteGanttTaskAction(reqMap);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return map;
	}
	
	/**
	 * @desc gantt chart update task all
	 * @author sangylee
	 * @date 2020. 10. 29.
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/updateGanttTaskAllAction")
	public Map<String, Object> updateGanttTaskAllAction(@RequestBody Map<String, Object> reqMap) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			
			ProjectHelper.service.updateGanttTaskAllAction(reqMap);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return map;
	}
	
	/**
	 * @desc gantt chart add link
	 * @author sangylee
	 * @date 2020. 10. 29.
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/addGanttLinkAction")
	public Map<String, Object> addGanttLinkAction(@RequestBody Map<String, Object> reqMap) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			
			PrePostLink link = ProjectHelper.service.addGanttLinkAction(reqMap);
			
			ProjectGanttLinkData data = new ProjectGanttLinkData(link);
			
			map.put("link", data);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return map;
	}
	
	/**
	 * @desc gantt chart delete link
	 * @author sangylee
	 * @date 2020. 11. 2.
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/deleteGanttLinkAction")
	public Map<String, Object> deleteGanttLinkAction(@RequestBody Map<String, Object> reqMap) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			
			ProjectHelper.service.deleteGanttLinkAction(reqMap);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return map;
	}
	
	/**
	 * @desc gantt chart move task
	 * @author sangylee
	 * @date 2020. 11. 5.
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/moveGanttTaskAction")
	public Map<String, Object> moveGanttTaskAction(@RequestBody Map<String, Object> reqMap) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			
			ProjectHelper.service.moveGanttTaskAction(reqMap);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return map;
	}
	
	/**
	 * @desc	: Project Gantt Chart View
	 * @author	: sangylee
	 * @date	: 2020. 11. 10.
	 * @method	: viewGanttChart
	 * @param	: reqMap
	 * @return	: ModelAndView
	 */
	@RequestMapping("/viewGanttChart")
	public ModelAndView viewGanttChart(@RequestParam Map<String, Object> reqMap) {

		ModelAndView model = new ModelAndView();

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.viewGanttChart");
		}
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			EProject project = (EProject) CommonUtil.getObject(oid);
			
			ProjectData data = new ProjectData(project);
			
			String dsoid = DsHelper.manager.getDscOidFromLink(project);
			
			boolean isAuth = ProjectHelper.manager.isAuth(project);
			
			model.addObject("isAuth", isAuth);
			model.addObject("project", data);
			model.addObject("oid", oid);
			
			if(!dsoid.equals("")) {
				model.addObject("dsoid", dsoid);
				model.setViewName("empty:/calendar/viewScheduleGantt");
			}else {
				model.setViewName("empty:/project/gantt/viewGanttChart");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}

//	@RequestMapping("/allDownloadAction")
//	public ModelAndView allDownloadAction(@RequestParam Map<String, Object> reqMap){
//		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
//		String type = StringUtil.checkNull((String) reqMap.get("type"));
//		ModelAndView model = new ModelAndView();
//		try {
//			model.addObject("oid", oid);
//			model.addObject("type", type);
//			model.setViewName("popup:/project/appDownloadAll");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return model;
//	}
	@RequestMapping("/allDownloadAction")
	public void allDownloadAction(@RequestParam Map<String, Object> reqMap,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		EProject project = (EProject)CommonUtil.getObject(oid);
		WTUser user = (WTUser) SessionHelper.getPrincipal();
		boolean enableDRMCheck = ConfigImpl.getInstance().getBoolean("drm.enable", false);
		
		List<Object[]> downloadList = ProjectHelper.manager.getDownloadList(reqMap);
		if(downloadList.size()>0){
			
			String fileName = project.getName() + ".zip";
			String strClient = request.getHeader("User-Agent");

			boolean msie = strClient.indexOf("MSIE")>-1;

			if(msie)
			{
			    boolean msie7 = strClient.indexOf("MSIE 7") >= 0;
			    if(msie7)
			    	fileName = EncodingConverter.escape(fileName);
			        // Try to guess user agent's encoding from request headers if not known
			   String encoding = "UTF-8";
			   fileName = ContentHelper.encode(fileName,encoding);
			}
			
			if(strClient.indexOf("MSIE 5.5")>-1) {
			   response.setHeader("Content-Disposition", "filename=" + fileName + ";");
			} else {
			   response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ";");
			}  // end if
			response.setContentType("application/unknown");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=utf-8"); 
			

			BufferedOutputStream outs =null;
			ZipOutputStream zos = null;

			try {
				outs = new BufferedOutputStream(response.getOutputStream());
			    zos = new ZipOutputStream(outs); // ZipOutputStream
			    zos.setLevel(8); // 압축 레벨 - 최대 압축률은 9, 디폴트 8
			    
			    wt.util.WTProperties props = wt.util.WTProperties.getLocalProperties();
				String wthome = props.getProperty("wt.home");
		
			    
				for(int i=0; i< downloadList.size(); i++){
					
					Object[] o = (Object[])downloadList.get(i);
					ContentHolder dholder = (ContentHolder)o[0];
					ApplicationData	pAppData = (ApplicationData)o[1];
					if(enableDRMCheck) {
						String[] rtn = E3PSDRMHelper.manager.download(dholder, pAppData);
						//File destFile = E3PSDRMHelper.manager.download(holder, appData);
						String errorCode = rtn[0];
						String path = rtn[1];
						if("0".equals(errorCode)) {
							try {
							String encoding = "UTF-8";
							fileName = ContentHelper.encode(pAppData.getFileName(), encoding);
							
							response.setContentType("application/octet-stream");
							response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\";");
							
							BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
							BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
							
							int y = 0;
							byte abyte0[] = new byte[512];
								try {
									while ((y = bis.read(abyte0)) != -1) {
										bos.write(abyte0, 0, y);
									}
								} catch (Exception e) {
								    e.printStackTrace();
								} finally {
									bis.close();
									bos.close();
								}
								bis = new BufferedInputStream(new FileInputStream(path));
								if (fileName.contains("{$CAD_NAME}")) {
									EPMDocument depm = (EPMDocument) dholder;
									EPMDocumentMaster emaster = (EPMDocumentMaster) (depm.getMaster());
									EPMDocumentMasterIdentity eidentity = (EPMDocumentMasterIdentity) emaster.getIdentificationObject();
									fileName = eidentity.getCADName();
								}
								ZipEntry zentry = new ZipEntry(fileName);
								zentry.setTime(pAppData.getModifyTimestamp().getTime());
								zos.putNextEntry(zentry);
								
							    byte[] buffer = new byte[2048];
							    int cnt = 0;
							    while ((cnt = bis.read(buffer, 0, 2048)) != -1) {
							        zos.write(buffer, 0, cnt);
							    }
							} finally {
								
							}
							
							
						} else {
							LOGGER.debug(rtn[1]);
							response.setContentType("text/html");
							PrintWriter writer = response.getWriter();
							writer.println("<script>javascript:alert('" + rtn[1] + "');javascript:history.back();</script>");
						}
					}else {
						InputStream in = null;
						BufferedInputStream fin = null;
						BufferedInputStream fin2 = null;
						String downloadFile = null;
						String dstFile = null;
						BufferedOutputStream outfile=null;
						try{
							String adfileName = pAppData.getFileName();
							
							String downloadDir = wthome + File.separator + "temp" + File.separator + "e3ps"+ File.separator +"zip";
							
							File dir = new File(downloadDir);
							if(!dir.exists()){
								dir.mkdirs();
							}
							
							downloadFile = downloadDir+File.separator+adfileName;
				
							in = ContentServerHelper.service.findLocalContentStream(pAppData);
							fin2 = new BufferedInputStream(in);
							outfile = new BufferedOutputStream(new FileOutputStream(downloadFile));
							int read = 0;
							byte b[] = new byte[4096];
							try {
							    while ((read = fin2.read(b)) != -1){
							    	outfile.write(b,0,read);
							    }
							} catch (Exception e) {
							    e.printStackTrace();
							} finally {
								fin2.close();
							    outfile.close();
							}
							
							fin = new BufferedInputStream(new FileInputStream(downloadFile));
							if (adfileName.contains("{$CAD_NAME}")) {
								EPMDocument depm = (EPMDocument) dholder;
								EPMDocumentMaster emaster = (EPMDocumentMaster) (depm.getMaster());
								EPMDocumentMasterIdentity eidentity = (EPMDocumentMasterIdentity) emaster.getIdentificationObject();
								adfileName = eidentity.getCADName();
							}
							ZipEntry zentry = new ZipEntry(adfileName);
							zentry.setTime(pAppData.getModifyTimestamp().getTime());
							zos.putNextEntry(zentry);
							
						    byte[] buffer = new byte[2048];
						    int cnt = 0;
						    while ((cnt = fin.read(buffer, 0, 2048)) != -1) {
						        zos.write(buffer, 0, cnt);
						    }
						}finally{
							
							try{
								if(in!=null)in.close();
								if(fin!=null)fin.close();
								
								if(downloadFile!=null){
								 	File ofile = new File(downloadFile);
									if(ofile.exists()){
										ofile.delete();
									}
								}
								if(dstFile!=null){
									File ofile = new File(dstFile);
									if(ofile.exists()){
										ofile.delete();
									}
								}
							}catch(Exception e){
							}
						}
					}
				}
			} finally {
				if (zos != null) {
				    zos.close();
				}
				if (outs != null) {
				    outs.close();
				}
			}
			// 다운로드 이력 생성
			Map<String, String> map = new HashMap<String, String>();

			map.put("fname", fileName);
			map.put("dOid", oid);
			map.put("userId", user.getName());
			
			HistoryHelper.service.createDownloadHistory(map);
		}else {
			response.setContentType("text/html; charset=utf-8"); 
			PrintWriter writer = response.getWriter();
			writer.println("<script>javascript:alert('주도면이 없습니다.');javascript:history.back();</script>");
		}
	}
	
	/**
	 * ERP 전송 버튼
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/sendErpAction")
	public Map<String, Object> sendErpAction(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.sendErpAction");
		}
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			EProject project = (EProject) CommonUtil.getObject(oid);
			//ERPInterface.send(project);
			
			map.put("result", true);
			map.put("msg", "전송하였습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/startChildTask")
	public Map<String, Object> startChildTask(@RequestBody Map<String, Object> reqMap){
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.startChildTask");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			List<ProjectTaskData> taskList = ProjectHelper.manager.getProjectTaskDataChildren(oid);
			if(taskList.size() > 0) {
				for(ProjectTaskData data : taskList) {
					if(!data.isChild() && STATEKEY.READY.equals(data.getState()) && STATEKEY.PROGRESS.equals(data.getPjtState())) {
						ProjectHelper.service.startTask(data.getOid());
					}
				}
			}
			map.put("oid", oid);
			map.put("result", true);
			map.put("msg", "시작되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/copyProjectCreate")
	public Map<String, Object> copyProject(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			ProjectHelper.service.copyProjectCreate(reqMap);
			
			map.put("msg", "복사되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/project/searchProject"));
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	//Project 실제시작일자 변경 - hgkang
	@RequestMapping("/modifyProjectStartDate")
	public ModelAndView modifyProjectStartDate(@RequestParam Map<String, Object> reqMap){
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.modifyProjectStartDate");
		}
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			boolean isAuth = Boolean.valueOf(StringUtil.checkNull((String) reqMap.get("isAuth")));
			boolean isEditState = Boolean.valueOf(StringUtil.checkNull((String) reqMap.get("isEditState")));
			
			EProject project = (EProject) CommonUtil.getObject(oid);
			
			model.addObject("oid", oid);
			model.addObject("isAuth", isAuth);
			model.addObject("isEditState", isEditState);
			model.setViewName("popup:/project/modifyProjectStartDate");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/modifyProjectStartDateAction")
	public Map<String, Object> modifyProjectStartDateAction(@RequestBody Map<String, Object> reqMap){
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ProjectController.modifyProjectStartDateAction");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			EProject project = ProjectHelper.service.modifyProjectStartDate(reqMap);
			
			map.put("result", true);
			map.put("msg", "수정하였습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/searchProjectRoleUser")
	public Map<String, Object> searchProjectRoleUser(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			map = ProjectMemberHelper.manager.searchProjectRoleUserList(reqMap);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/searchRoleUserPopup")
	public ModelAndView searchUserPopup(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String id = StringUtil.checkNull((String) reqMap.get("id"));
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		model.addObject("id", id);
		model.addObject("oid", oid);
		
		model.setViewName("popup:/project/searchRoleUserPopup");
		
		return model;
	}
}
