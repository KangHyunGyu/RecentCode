package com.e3ps.change.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.e3ps.approval.service.ApprovalHelper;
import com.e3ps.change.EChangeRequest2;
import com.e3ps.change.EcrProjectLink;
import com.e3ps.change.beans.ChangeECRSearch;
import com.e3ps.change.beans.ECOData;
import com.e3ps.change.beans.ECRData;
import com.e3ps.change.beans.EChangeContentsData;
import com.e3ps.change.service.ChangeHelper;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.TypeUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.common.web.PageQueryBroker;
import com.e3ps.epm.bean.EpmData;
import com.e3ps.part.bean.PartData;
import com.e3ps.project.ControllerException;
import com.e3ps.project.EProject;
import com.e3ps.project.RoleUserLink;
import com.e3ps.project.beans.ProjectData;
import com.e3ps.project.service.ProjectMemberHelper;
import com.ibm.icu.util.Calendar;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.org.WTUser;
import wt.query.QuerySpec;
import wt.session.SessionHelper;
import wt.util.WTException;

@Controller
@RequestMapping("/change")
public class ECRController {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.CHANGE.getName());
	/**
	 * ECR 검색
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/searchECR")
	public ModelAndView searchECR(HttpServletRequest request, HttpServletResponse response) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ECRController.searchECR");
		}
		
		try {
			// #. 파라메터 조회
			boolean popup = TypeUtil.booleanValue(request.getParameter("popup"));
			if (popup) {
				return new ModelAndView("popup:/change/searchECR");
			} else {
				return new ModelAndView("default:/change/searchECR");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
	}
	
	@RequestMapping("/searchEcrPopup")
	public ModelAndView searchDoc2(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		String id = StringUtil.checkNull((String) reqMap.get("id"));
		String type = StringUtil.checkReplaceStr((String) reqMap.get("type"), "single");
		String except = StringUtil.checkNull((String) reqMap.get("except"));
		
		model.addObject("id", id);
		model.addObject("type", type);
		model.addObject("except", except);
		
		model.setViewName("popup:/change/searchEcrPopup");
		
		return model;
	}
	
	@RequestMapping("/searchECR2")
	public ModelAndView searchECR2(HttpServletRequest request, HttpServletResponse response) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ECRController.searchECR2");
		}
		
		try {
			// #. 파라메터 조회
			boolean popup = TypeUtil.booleanValue(request.getParameter("popup"));
			if (popup) {
				return new ModelAndView("popup:/change/searchECR2");
			} else {
				return new ModelAndView("default:/change/searchECR2");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
	}

	/**
	 * ECR 목록 Data (Ajax)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/getECRListJson")
	public ModelAndView getECRListJson(HttpServletRequest request, HttpServletResponse response) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("#### ECRController.getECRListJson");
		}
		
		try {
			Map hash = request.getParameterMap();
			
			QuerySpec query = ChangeECRSearch.getECRQuery(hash);
			
			PageQueryBroker broker = new PageQueryBroker(request, query);
			QueryResult qr = broker.search();

			JSONObject jsonResponse = new JSONObject();
			// #. 응답 json 생성
			jsonResponse.put("total", broker.getPageCount());
			jsonResponse.put("page", broker.getCpage());
			jsonResponse.put("records", broker.getTotal());

			JSONObject jsonUserData = new JSONObject();
			jsonUserData.put("sessionid", TypeUtil.longValue(broker.getSessionid()));
			jsonResponse.put("userdata", jsonUserData);

			JSONArray jsonRows = new JSONArray();
			
			JSONArray jsonPart = new JSONArray();
			
			JSONArray jsonDrawing = new JSONArray();

			while (qr.hasMoreElements()) {
				Object[] o = (Object[]) qr.nextElement();
				EChangeRequest2 ecr = (EChangeRequest2) o[0];
				
				Calendar today = Calendar.getInstance();
				String tdate = DateUtil.getDateString(today.getTime(), "d");
				String startTaskDate = DateUtil.getDateString(ecr.getPersistInfo().getCreateStamp(),"d");
				long delayDay = 0;
				if(startTaskDate!=null && startTaskDate!=""){
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					Date beginDate = formatter.parse(startTaskDate);
					Date endDate = formatter.parse(tdate);
					long diff = endDate.getTime() - beginDate.getTime();
					delayDay = diff/(24*60*60*1000);
				}

				String ecrOid = WCUtil.getOid(ecr);
				JSONObject jsonRow = new JSONObject();
				
				QueryResult projects = PersistenceHelper.manager.navigate(ecr,"project",EcrProjectLink.class);
				String refProject = "";
				boolean flag = false;
				JSONArray jsonProjects = new JSONArray();
				while(projects.hasMoreElements()){
					EProject project = (EProject)projects.nextElement();
					if(flag){
						refProject+= ",";
					}else{
						flag =true;
					}
					refProject += project.getName();
					
					Object[] pm = ProjectMemberHelper.service.getPM(project);
					WTUser pmuser = null;
					if( pm != null){
					RoleUserLink user = (RoleUserLink)pm[1];
						pmuser = user.getUser();
					}
					String projectOid = WCUtil.getOid(project);
					
					JSONObject jsonproject = new JSONObject();
					jsonproject.put("id", projectOid);
					jsonproject.put("oid", projectOid);
					jsonproject.put("code", project.getCode());
					jsonproject.put("name", project.getName());
					jsonproject.put("state.state", project.getLifeCycleState().getDisplay(request.getLocale()));
					jsonproject.put("pm", pmuser!=null ? pmuser.getFullName():"");
					jsonproject.put("process", project.getWbsCode());
					jsonproject.put("planStartDate", DateUtil.getDateString(project.getPlanStartDate(),"d"));
					jsonproject.put("planEndDate", DateUtil.getDateString(project.getPlanEndDate(),"d"));
					jsonproject.put("startDate", DateUtil.getDateString(project.getStartDate(),"d"));
					jsonproject.put("endDate", DateUtil.getDateString(project.getEndDate(),"d"));
					jsonproject.put("createDate", DateUtil.getDateString(project.getPersistInfo().getCreateStamp(), "d"));
					jsonproject.put("updateDate", DateUtil.getDateString(project.getPersistInfo().getModifyStamp(), "d"));
					
					jsonProjects.put(jsonproject);
				}
				
				jsonRow.put("id", ecrOid);
				jsonRow.put("oid", ecrOid);
				jsonRow.put("requestNumber", ecr.getRequestNumber());
				jsonRow.put("name", ecr.getName());
				jsonRow.put("state", ecr.getState());//M.get(ApprovalHelper.service.getState(ecr)));
				jsonRow.put("createDate", DateUtil.getDateString(ecr.getPersistInfo().getCreateStamp(),"d"));
				jsonRow.put("updateDate", DateUtil.getDateString(ecr.getPersistInfo().getModifyStamp(),"d"));
				jsonRow.put("tempcreator", ecr.getOwner().getFullName());
				jsonRow.put("project",refProject);
				//jsonRow.put("requestDate", DateUtil.getDateString(ecr.getRequestDate(),"d"));
				jsonRow.put("delayDay", delayDay);
				
				//변경대상[품목]
				jsonPart = ChangeECRSearch.getECOrelatedPartsJSO(ecr);
				//변경대상[도면]
				jsonDrawing = ChangeECRSearch.getECOrelatedDrawingsJSO(ecr);
				
				jsonRow.put("relatedParts",jsonPart);
				jsonRow.put("relatedDrawings",jsonDrawing);
				jsonRow.put("relatedProjects",jsonProjects);
				
				jsonRows.put(jsonRow);
				
			}
			
			jsonResponse.put("rows", jsonRows);
			
			return new ModelAndView("jsonView", "jsonObject", jsonResponse);
		} catch (Exception e) {
			LOGGER.error("ERROR", e);
			throw new ControllerException(e);
		}
	}
	
	/**
	 * 설계변경진행목록 ECR Data (Ajax)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/getMyworkECRListJson")
	public ModelAndView getMyworkECRListJson(HttpServletRequest request, HttpServletResponse response) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("#### ECRController.getMyworkECRListJson");
		}
		
		try {
			
			QuerySpec query = ChangeECRSearch.getMyworkECRQuery();
			
			PageQueryBroker broker = new PageQueryBroker(request, query);
			QueryResult qr = broker.search();

			JSONObject jsonResponse = new JSONObject();
			// #. 응답 json 생성
			jsonResponse.put("total", broker.getPageCount());
			jsonResponse.put("page", broker.getCpage());
			jsonResponse.put("records", broker.getTotal());

			JSONObject jsonUserData = new JSONObject();
			jsonUserData.put("sessionid", TypeUtil.longValue(broker.getSessionid()));
			jsonResponse.put("userdata", jsonUserData);

			JSONArray jsonRows = new JSONArray();
			
			JSONArray jsonPart = new JSONArray();
			
			JSONArray jsonDrawing = new JSONArray();
			
			while (qr.hasMoreElements()) {
				Object[] o = (Object[]) qr.nextElement();
				EChangeRequest2 ecr = (EChangeRequest2) o[0];
				
				Calendar today = Calendar.getInstance();
				String tdate = DateUtil.getDateString(today.getTime(), "d");
				String startTaskDate = DateUtil.getDateString(ecr.getPersistInfo().getCreateStamp(),"d");
				long delayDay = 0;
				if(startTaskDate!=null && startTaskDate!=""){
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					Date beginDate = formatter.parse(startTaskDate);
					Date endDate = formatter.parse(tdate);
					long diff = endDate.getTime() - beginDate.getTime();
					delayDay = diff/(24*60*60*1000);
				}

				String ecrOid = WCUtil.getOid(ecr);
				JSONObject jsonRow = new JSONObject();
				
				QueryResult projects = PersistenceHelper.manager.navigate(ecr,"project",EcrProjectLink.class);
				String refProject = "";
				boolean flag = false;
				JSONArray jsonProjects = new JSONArray();
				while(projects.hasMoreElements()){
					EProject project = (EProject)projects.nextElement();
					if(flag){
						refProject+= ",";
					}else{
						flag =true;
					}
					refProject += project.getName();
					
					Object[] pm = ProjectMemberHelper.service.getPM(project);
					WTUser pmuser = null;
					if( pm != null){
					RoleUserLink user = (RoleUserLink)pm[1];
						pmuser = user.getUser();
					}
					String projectOid = WCUtil.getOid(project);
					
					JSONObject jsonproject = new JSONObject();
					jsonproject.put("id", projectOid);
					jsonproject.put("oid", projectOid);
					jsonproject.put("code", project.getCode());
					jsonproject.put("name", project.getName());
					jsonproject.put("state.state", project.getLifeCycleState().getDisplay(request.getLocale()));
					jsonproject.put("pm", pmuser!=null ? pmuser.getFullName():"");
					jsonproject.put("process", project.getWbsCode());
					jsonproject.put("planStartDate", DateUtil.getDateString(project.getPlanStartDate(),"d"));
					jsonproject.put("planEndDate", DateUtil.getDateString(project.getPlanEndDate(),"d"));
					jsonproject.put("startDate", DateUtil.getDateString(project.getStartDate(),"d"));
					jsonproject.put("endDate", DateUtil.getDateString(project.getEndDate(),"d"));
					jsonproject.put("createDate", DateUtil.getDateString(project.getPersistInfo().getCreateStamp(), "d"));
					jsonproject.put("updateDate", DateUtil.getDateString(project.getPersistInfo().getModifyStamp(), "d"));
					
					jsonProjects.put(jsonproject);
				}
				
				jsonRow.put("id", ecrOid);
				jsonRow.put("oid", ecrOid);
				jsonRow.put("requestNumber", ecr.getRequestNumber());
				jsonRow.put("name", ecr.getName());
				jsonRow.put("state", ApprovalHelper.service.getState(ecr));
				jsonRow.put("createDate", DateUtil.getDateString(ecr.getPersistInfo().getCreateStamp(),"d"));
				jsonRow.put("updateDate", DateUtil.getDateString(ecr.getPersistInfo().getModifyStamp(),"d"));
				jsonRow.put("tempcreator", ecr.getOwner().getFullName());
				jsonRow.put("project",refProject);
				//jsonRow.put("requestDate", DateUtil.getDateString(ecr.getRequestDate(),"d"));
				jsonRow.put("delayDay", delayDay);
				
				//변경대상[품목]
				jsonPart = ChangeECRSearch.getECOrelatedPartsJSO(ecr);
				//변경대상[도면]
				jsonDrawing = ChangeECRSearch.getECOrelatedDrawingsJSO(ecr);
				
				jsonRow.put("relatedParts",jsonPart);
				jsonRow.put("relatedDrawings",jsonDrawing);
				jsonRow.put("relatedProjects",jsonProjects);
				
				jsonRows.put(jsonRow);
			}
			
			jsonResponse.put("rows", jsonRows);
			
			return new ModelAndView("jsonView", "jsonObject", jsonResponse);
		} catch (Exception e) {
			LOGGER.error("ERROR", e);
			throw new ControllerException(e);
		}
	}
	
	@RequestMapping("/createECR")
	public ModelAndView createECR(@RequestParam Map<String, Object> reqMap) throws WTException {
		ModelAndView model = new ModelAndView();
		String rootOid = ChangeHelper.manager.getEcrRoot();
		model.addObject("changeType", "ECR");
		model.addObject("ecrRootOid", rootOid);
		model.setViewName("default:/change/createECR");
		return model;
	}
	
	@RequestMapping("/include_detailECR")
	public ModelAndView detailECR(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			EChangeRequest2 ecr = (EChangeRequest2)CommonUtil.getObject(oid);
			ECRData data = new ECRData(ecr);
			//WTUser p = ecr.getWorker();
			//String worker = p.getFullName();
			model.addObject("oid", oid);
			model.addObject("data", data);
			//model.addObject("worker", worker);
			model.setViewName("include:/change/include/detailECR");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	@RequestMapping("/updateECR")
	public ModelAndView updateECR(@RequestParam Map<String, Object> reqMap) throws Exception {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			EChangeRequest2 ecr = (EChangeRequest2)CommonUtil.getObject(oid);
			ECRData data = new ECRData(ecr);
			//WTUser p = ecr.getWorker();
			//String worker = p.getPersistInfo().getObjectIdentifier().toString();
			//model.addObject("ecr", ecr);
			model.addObject("oid", oid);
			model.addObject("data", data);
			//model.addObject("worker", worker);
//			String rootOid = ChangeHelper.manager.getEcrRoot();
//			model.addObject("changeType", "ECR");
//			model.addObject("ecrRootOid", rootOid);
			model.setViewName("popup:/change/updateECR");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/updateECRAction")
	public Map<String, Object> updateECRAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			EChangeRequest2 change = ChangeHelper.service.updateEcr(reqMap);
			String oid = CommonUtil.getOIDString(change);
			
			map.put("msg", "수정되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/change/viewECR") + "?oid=" + oid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/deleteECRAction")
	public Map<String, Object> deleteECRAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			ChangeHelper.service.deleteEcr(oid);
			
			map.put("msg", "삭제되었습니다.");
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
	 * ECR 목록 Excel 출력
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/downloadECRListExcel")
	public ModelAndView downloadECRListExcel(HttpServletRequest request, HttpServletResponse response) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ECRController.downloadECRListExcel");
		}

		try {
			return new ModelAndView("/extcore/kores/change/downloadECRListExcel.jsp");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
	}
	
	@RequestMapping("/dashboardChange")
	public ModelAndView dashboardChange(HttpServletRequest request, HttpServletResponse response) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ECRController.dashboardChange");
		}
		
		try {
			// #. 파라메터 조회
			boolean popup = TypeUtil.booleanValue(request.getParameter("popup"));
			if (popup) {
				return new ModelAndView("popup:/change/dashboardChange");
			} else {
				return new ModelAndView("default:/change/dashboardChange");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
	}
	
	
	@ResponseBody
	@RequestMapping("/createECRAction")
	public Map<String, Object> createECRAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			ChangeHelper.service.createEcr(reqMap);
			
			map.put("msg", "등록되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/change/searchECR"));
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/searchECRAction")
	public Map<String, Object> searchECRAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			map = ChangeHelper.manager.getECRList(reqMap);
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/viewECR_include")
	public ModelAndView viewECR_include(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			EChangeRequest2 ecr = (EChangeRequest2)CommonUtil.getObject(oid);
			ECRData data = new ECRData(ecr);
			model.addObject("oid", oid);
			model.addObject("data", data);
			model.setViewName("include:/change/include/viewECR_include2");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	@RequestMapping("/viewECR2")
	public ModelAndView viewECR2(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			String isApproval = StringUtil.checkNull((String) reqMap.get("isApproval")); 
			EChangeRequest2 ecr = (EChangeRequest2)CommonUtil.getObject(oid);
			ECRData data = new ECRData(ecr);
			WTUser sessionUser = (WTUser)SessionHelper.manager.getPrincipal();
			String creator = ecr.getOwner().getName();
			boolean isModify = (ecr.getLifeCycleState().toString().equals("INWORK") || 
					ecr.getLifeCycleState().toString().equals("REJECTED")) 
					&& sessionUser.getName().equals(creator);
			boolean isAdmin = CommonUtil.isAdmin();

			model.addObject("oid", oid);
			model.addObject("data", data);
			model.addObject("isModify", isModify);
			model.addObject("isAdmin", isAdmin);
			model.addObject("isApproval", isApproval);
			model.setViewName("popup:/change/viewECR");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	@RequestMapping("/viewECR")
	public ModelAndView viewECR(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			EChangeRequest2 ecr = (EChangeRequest2)CommonUtil.getObject(oid);
			ECRData data = new ECRData(ecr);

			model.addObject("oid", oid);
			model.addObject("data", data);
			model.setViewName("popup:/change/viewECR");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	
	@RequestMapping("/include_ecrContent")
	public ModelAndView ecrContent(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			String type = StringUtil.checkNull((String) reqMap.get("type")); 
			model.addObject("oid", oid);
			model.addObject("type", type);
			model.setViewName("include:/change/include/ecrContent");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/getEchangeContent")
	public Map<String, Object> getEchangeContent(@RequestBody Map<String, Object> reqMap) {
		List<EChangeContentsData> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			
			list = ChangeHelper.manager.getEChangeContents(oid);
			map.put("result", true);
			map.put("list", list);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/setECRData")
	public Map<String, Object> setECRData(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			String oid = (String)reqMap.get("oid");
			map = ChangeHelper.manager.setECRDataAction(oid);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}

		return map;
	}
	
	
	@ResponseBody
	@RequestMapping("/getECRData")
	public Map<String, Object> getECRData(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			String oid = (String)reqMap.get("oid");
			map = ChangeHelper.manager.getECRData(oid);
			
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}

		return map;
	}
}
