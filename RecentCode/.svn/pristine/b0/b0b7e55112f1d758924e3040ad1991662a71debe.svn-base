package com.e3ps.project;

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
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.TypeUtil;
import com.e3ps.common.util.WebUtil;
import com.e3ps.org.People;
import com.e3ps.org.service.UserHelper;
import com.e3ps.project.beans.IssueData;
import com.e3ps.project.issue.IssueRequest;
import com.e3ps.project.issue.IssueSolution;
import com.e3ps.project.key.ProjectKey.IssueKey;
import com.e3ps.project.key.ProjectKey.STATEKEY;
import com.e3ps.project.service.IssueHelper;

import wt.fc.ReferenceFactory;
import wt.org.WTUser;
import wt.session.SessionHelper;
import wt.util.WTException;
import wt.util.WTRuntimeException;

@Controller
@RequestMapping("/project/issue")
public class IssueController {
	protected static final Logger logger = LoggerFactory.getLogger(IssueController.class);
	
	@RequestMapping("/searchMyIssue")
	public ModelAndView searchMyIssue(@RequestParam Map<String, Object> reqMap){

		ModelAndView model = new ModelAndView();
		if (logger.isDebugEnabled()) {
			
			logger.debug("ProjectController.searchMyIssue");
		}
		model.setViewName("default:/project/searchMyIssue");
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/searchMyIssueAction")
	public Map<String, Object> searchMyIssueAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			List<IssueData> list = IssueHelper.manager.getMyIssueList(reqMap);
			
			map.put("list", list);
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		return map;
	}
	
	@RequestMapping("/searchIssue")
	public ModelAndView searchIssue(@RequestParam Map<String, Object> reqMap){

		ModelAndView model = new ModelAndView();
		if (logger.isDebugEnabled()) {
			
			logger.debug("ProjectController.searchIssue");
		}
		model.setViewName("default:/project/searchIssue");

		return model;
	}
	
	@ResponseBody
	@RequestMapping("/searchIssueAction")
	public Map<String, Object> searchIssueAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			List<IssueData> list = IssueHelper.manager.getIssueList(reqMap);
			
			map.put("list", list);
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/viewIssue")
	public ModelAndView viewIssue(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		if (logger.isDebugEnabled()) {
			
			logger.debug("IssueController.viewIssue");
		}
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String command = StringUtil.checkNull((String) reqMap.get("command"));
		boolean isView = TypeUtil.booleanValue(reqMap.get("isView"));
		boolean isState = false;
		boolean isComplete = false;
		if(oid == null) {
			oid = "";
		}
		
		try {
			ReferenceFactory rf = new ReferenceFactory();
			IssueRequest issue = (IssueRequest)rf.getReference(oid).getObject();
		
			IssueData data = new IssueData(issue);
			IssueSolution solution = data.Solution();
			
			WTUser user = (WTUser)SessionHelper.manager.getPrincipal();
			boolean isCreator = data.isCreator(user);
			boolean isManager = data.isManager(user);
			boolean isAdmin = CommonUtil.isAdmin();
			boolean isCompleteState = IssueKey.ISSUE_COMPLETE.equals(issue.getState());
			boolean isRequestState = IssueKey.ISSUE_REQUEST.equals(issue.getState());
			
			isComplete = IssueKey.ISSUE_COMPLETE.equals(data.getState());
			isState = IssueKey.ISSUE_CHECK.equals(data.getState());
			
			model.addObject("isCreator", isCreator);
			model.addObject("isManager", isManager);
			model.addObject("isAdmin", isAdmin);
			model.addObject("isCompleteState", isCompleteState);
			model.addObject("isRequestState", isRequestState);
			model.addObject("solution", solution);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addObject("isComplete", isComplete);
		model.addObject("isState", isState);
		model.addObject("isView", isView);
		model.addObject("oid", oid);
		model.addObject("command", command);
		model.setViewName("popup:/project/issue/viewIssue");
		return model;
	}

	@RequestMapping("/createIssue")
	public ModelAndView createIssue(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String taskOid = StringUtil.checkNull((String) reqMap.get("taskOid"));
		ETask task =(ETask)CommonUtil.getObject(taskOid);
		
		if(taskOid == null) {
			taskOid = "";
		}
		model.addObject("taskOid", taskOid);
		model.addObject("projectOid", CommonUtil.getOIDString(task.getProject()));
		if (logger.isDebugEnabled()) {

			logger.debug("IssueController.createIssue");
		}
		model.setViewName("popup:/project/issue/createIssue");
		return model;
	}

	@ResponseBody
	@RequestMapping("/createIssueAction")
	public Map<String, Object> createIssueAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		if (logger.isDebugEnabled()) {

			logger.debug("IssueController.createIssueAction");
		}
		try {
			IssueHelper.service.saveIssue(reqMap);
			
			map.put("callbackName", "opener.location.reload()");
			map.put("result", true);
			map.put("msg", "등록이 완료되었습니다.");
			map.put("redirectUrl", "close");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}

	@RequestMapping("/updateIssue")
	public ModelAndView updateIssue(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String command = StringUtil.checkNull((String) reqMap.get("command"));
		String name = "";
		String fullName = "";
		String peopleOid = "";
		String manager = "";
		String problem = "";
		IssueSolution solution = null;
		try {
			ReferenceFactory rf = new ReferenceFactory();
			IssueRequest issue = (IssueRequest)rf.getReference(oid).getObject();
			IssueData data;
			data = new IssueData(issue);
			solution = data.Solution();
	
			People people = null;
	
			if(issue.getManager() != null){
				people = UserHelper.service.getPeople(issue.getManager());
			}
			WTUser p = issue.getManager();
			String worker = p.getPersistInfo().getObjectIdentifier().toString();
			name = data.getName();
			fullName = data.getManagerFullName();
			peopleOid = CommonUtil.getOIDString(people);
			if(people != null) {
				manager = people.getPersistInfo().getObjectIdentifier().toString();
			}
			if(issue.getProblem()!=null) {
				problem = issue.getProblem();
			}
			model.addObject("data", data);
			model.addObject("worker", worker);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addObject("oid", oid);
		model.addObject("command", command);
		model.addObject("name", name);
		model.addObject("fullName", fullName);
		model.addObject("peopleOid", peopleOid);
		model.addObject("manager", manager);
		model.addObject("problem", problem);
		model.addObject("solution", solution);
		
		model.setViewName("popup:/project/issue/updateIssue");
		
		return model;
	}

	@ResponseBody
	@RequestMapping("/updateIssueAction")
	public Map<String, Object> updateIssueAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		if (logger.isDebugEnabled()) {

			logger.debug("IssueController.updateIssueAction");
		}
		try {
			HashMap<String, Object> issueMap = IssueHelper.service.updateIssue(reqMap);
			String oid = (String) issueMap.get("oid");
			map.put("callbackName", "opener.location.reload()");
			map.put("result", true);
			map.put("msg", "수정이 완료되었습니다.");
			map.put("redirectUrl", CommonUtil.getURLString("/project/issue/viewIssue") + "?oid=" + oid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}

	@RequestMapping("/createSolution")
	public ModelAndView createSolution(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		if (logger.isDebugEnabled()) {

			logger.debug("IssueController.createSolution");
		}
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		if(oid == null) {
			oid = "";
		}
		String command = StringUtil.checkNull((String) reqMap.get("command"));
		boolean isView = TypeUtil.booleanValue(reqMap.get("isView"));
		String manager = "";
		try {
			WTUser user = (WTUser)SessionHelper.manager.getPrincipal();
			manager = user.getFullName();
			
			ReferenceFactory rf = new ReferenceFactory();
			
			IssueRequest issue = (IssueRequest)rf.getReference(oid).getObject();
			
			IssueData data;
			data = new IssueData(issue);

			// WTUser user = (WTUser)SessionHelper.manager.getPrincipal();
			boolean isCreator = data.isCreator(user);

			model.addObject("data", data);
			model.addObject("pjtName", data.getProjectName());
			model.addObject("requestDate", data.getRequestDate());
			model.addObject("createDate", data.getCreateDate());
			model.addObject("problem", data.getProblem());
			model.addObject("isCreator", isCreator);
			model.addObject("isView", isView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addObject("oid", oid);
		model.addObject("command", command);
		model.addObject("manager", manager);
		
		model.setViewName("popup:/project/issue/createSolution");
		
		return model;
	}

	@ResponseBody
	@RequestMapping("/createSolutionAction")
	public Map<String, Object> createSolutionAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		if (logger.isDebugEnabled()) {

			logger.debug("IssueController.createSolutionAction");
		}
		
		try {
			IssueHelper.service.createSolution(reqMap);
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			map.put("callbackName", "opener.location.reload()");
			map.put("result", true);
			map.put("msg", "등록이 완료되었습니다.");
			map.put("redirectUrl",  CommonUtil.getURLString("/project/issue/viewIssue")+ "?oid=" + oid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}

	@RequestMapping("/updateSolution")
	public ModelAndView updateSolution(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		if (logger.isDebugEnabled()) {
			
			logger.debug("IssueController.updateSolution");
		}
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String command = StringUtil.checkNull((String) reqMap.get("command"));
		String solution = "";
		String fullName = "";
		String solutionOid = "";
		if(oid == null) {
			oid = "";
		}
		
		try {
			ReferenceFactory rf = new ReferenceFactory();
			IssueRequest issue;
			issue = (IssueRequest)rf.getReference(oid).getObject();
			IssueData data = new IssueData(issue);
			IssueSolution issueSolution = data.Solution();
			solution = issueSolution.getSolution();
			WTUser user = (WTUser)SessionHelper.manager.getPrincipal();
			fullName = user.getFullName();
			solutionOid = CommonUtil.getFullOIDString(issueSolution);
			model.addObject("solutionOid", solutionOid);
			model.addObject("solution", solution);
			model.addObject("fullName", fullName);
			model.addObject("requestDate", data.getRequestDate());
		} catch (WTRuntimeException e) {
			e.printStackTrace();
		} catch (WTException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addObject("command", command);
		model.addObject("oid", oid);

		model.setViewName("popup:/project/issue/updateSolution");

		return model;
	}

	@ResponseBody
	@RequestMapping("/updateSolutionAction")
	public Map<String, Object> updateSolutionAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		
		if (logger.isDebugEnabled()) {

			logger.debug("IssueController.updateSolutionAction");
		}
		try {
			IssueHelper.service.updateSolution(reqMap);
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			map.put("callbackName", "opener.location.reload()");
			map.put("msg", "수정이 완료되었습니다.");
			map.put("redirectUrl",  CommonUtil.getURLString("/project/issue/viewIssue")+ "?oid=" + oid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}

	@RequestMapping("/listIssue")
	public ModelAndView listIssue(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		if (logger.isDebugEnabled()) {
			logger.debug("IssueController.listIssue");
		}
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String viewType = StringUtil.checkNull((String) reqMap.get("viewType"));
		
		model.addObject("oid", oid);
		model.addObject("viewType", viewType);
		
		model.setViewName("popup:/project/issue/listIssue");

		return model;
	}
	
	/**
	 * @desc : 이슈 리스트
	 * @author : tsjeong
	 * @date : 2020. 9. 21.
	 * @method : include_issueList
	 * @param : reqMap
	 * @return : ModelAndView
	 */
	@RequestMapping("/include_issueList")
	public ModelAndView include_issueList(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String isView = StringUtil.checkNull((String) reqMap.get("isView"));
		boolean isTaskEdit = Boolean.valueOf(StringUtil.checkNull((String) reqMap.get("isTaskEdit")));
		
		try {
			ReferenceFactory rf = new ReferenceFactory();
			ETaskNode node;
			node = (ETaskNode)rf.getReference(oid).getObject();
			EProjectNode project = node.getProject();
			EProject pjt = (EProject)project;
			
			boolean isCreateIssueState = false;
			if((!STATEKEY.READY.equals(pjt.getState().toString())) && (!STATEKEY.SIGN.equals(pjt.getState().toString())) && 
					(!STATEKEY.MODIFY.equals(pjt.getState().toString()))){
				isCreateIssueState = true;
			}
			
			model.addObject("isTaskEdit", isTaskEdit);
			model.addObject("isCreateIssueState", isCreateIssueState);
			
		} catch (WTRuntimeException | WTException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addObject("oid", oid);
		model.addObject("isView", isView);
		
		model.setViewName("include:/project/issue/include/issueList");
		return model;
	}
	
	/**
	 * @desc	: 이슈 리스트 검색 Action
	 * @author	: tsjeong
	 * @date	: 2020. 9. 21.
	 * @method	: issueListAction
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/issueListAction")
	public Map<String, Object> issueListAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			List<IssueData> list = IssueHelper.manager.getIssueRequestList(reqMap);
			
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
	@RequestMapping("/deleteIssueAction")
	public Map<String, Object> deleteAction(@RequestBody Map<String, Object> reqMap) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("IssueController.deleteIssueAction");
		}
		try {
			IssueHelper.service.deleteIssue(reqMap);

			map.put("callbackName", "opener.location.reload()");
			map.put("msg", "삭제가 완료되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/admin/updateEChangeActivityDefinition"));
		} catch (WTException ex) {
			throw new ControllerException(ex);
		}

		return map;
	}

	@ResponseBody
	@RequestMapping("/deleteSolutionAction")
	public Map<String, Object> deleteSolutionAction(@RequestBody Map<String, Object> reqMap) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("IssueController.deleteSolutionAction");
		}
		try {
			IssueHelper.service.deleteSolution(reqMap);
			
			map.put("msg", "삭제가 완료되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/project/issue/viewIssue"));
		} catch (Exception ex) {
			throw new ControllerException(ex);
		}

		return map;
	}

	@ResponseBody
	@RequestMapping("/complate")
	public Map<String, Object> complate(@RequestBody Map<String, Object> reqMap) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("IssueController.complate");
		}
		try {
			IssueHelper.service.issueComplete(reqMap);
			map.put("callbackName", "opener.location.reload()");

			map.put("msg", "완료되었습니다.");
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
		
		return map;
	}

	@ResponseBody
	@RequestMapping("/complateCancle")
	public Map<String, Object> complateCancle(@RequestBody Map<String, Object> reqMap) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("IssueController.complateCancle");
		}
		try {
			IssueHelper.service.cancelComplete(reqMap);
			map.put("callbackName", "opener.location.reload()");
		
			map.put("msg", "완료가 취소되었습니다.");
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}

		return map;
	}
	
	/**
	 * @desc : 이슈 상세보기
	 * @author : tsjeong
	 * @date : 2020. 9. 21.
	 * @method : include_viewIssue
	 * @param : reqMap
	 * @return : ModelAndView
	 */
	@RequestMapping("/include_viewIssue")
	public ModelAndView include_viewIssue(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String command = StringUtil.checkNull((String) reqMap.get("command"));
		String problem = "";
		String pjtName = "";
		boolean isView = TypeUtil.booleanValue(reqMap.get("isView"));
		boolean isCreator = false;
		boolean isAdmin = false;
		boolean isState = false;
		boolean isComplete = false;
		
		try {
			ReferenceFactory rf = new ReferenceFactory();

			IssueRequest issue = (IssueRequest)rf.getReference(oid).getObject();

			IssueData data = new IssueData(issue);

			WTUser user = (WTUser)SessionHelper.manager.getPrincipal();
			
			isCreator = data.isCreator(user);
			isAdmin =  CommonUtil.isAdmin();
			isState = IssueKey.ISSUE_CHECK.equals(data.getState());
			isComplete = IssueKey.ISSUE_COMPLETE.equals(data.getState());
			problem = data.getProblem();
			pjtName = data.getProjectName();
			
			model.addObject("data", data);
		} catch (WTRuntimeException | WTException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addObject("oid", oid);
		model.addObject("command", command);
		model.addObject("isView", isView);
		model.addObject("isCreator", isCreator);
		model.addObject("isAdmin", isAdmin);
		model.addObject("isState", isState);
		model.addObject("isComplete", isComplete);
		model.addObject("problem", problem);
		model.addObject("pjtName", pjtName);
		
		model.setViewName("include:/project/issue/include/viewIssue");
		return model;
	}
	
	/**
	 * @desc : solution 상세보기
	 * @author : tsjeong
	 * @date : 2020. 9. 21.
	 * @method : include_solution
	 * @param : reqMap
	 * @return : ModelAndView
	 */
	@RequestMapping("/include_solution")
	public ModelAndView include_solution(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String command = StringUtil.checkNull((String) reqMap.get("command"));
		String createStamp = "";
		String fullName = "";
		String solution="";
		String solutionOid = "";
		try {
			ReferenceFactory rf = new ReferenceFactory();
			IssueRequest issue = (IssueRequest)rf.getReference(oid).getObject();
			IssueData data = new IssueData(issue);
			IssueSolution issueSolution = data.Solution();
			
			createStamp = DateUtil.getDateString(issueSolution.getPersistInfo().getCreateStamp(),"d");
			fullName = issueSolution.getCreator().getFullName();
			solution = WebUtil.getHtml(issueSolution.getSolution());
			solutionOid = CommonUtil.getFullOIDString(issueSolution);
			model.addObject("data", data);
			model.addObject("solutionOid", solutionOid);
		} catch (WTRuntimeException | WTException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addObject("oid", oid);
		model.addObject("command", command);
		model.addObject("createStamp", createStamp);
		model.addObject("fullName", fullName);
		model.addObject("solution", solution);
		
		model.setViewName("include:/project/issue/include/solution");
		return model;
	}
}
