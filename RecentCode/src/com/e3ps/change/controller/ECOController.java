package com.e3ps.change.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.e3ps.change.DocumentActivityOutput;
import com.e3ps.change.EChangeActivity;
import com.e3ps.change.EChangeOrder2;
import com.e3ps.change.EChangeRequest2;
import com.e3ps.change.EcoPartLink;
import com.e3ps.change.EcoTargetResultLink;
import com.e3ps.change.EcrPartLink;
import com.e3ps.change.EcrTargetLink;
import com.e3ps.change.beans.ActivityDocDataNEW;
import com.e3ps.change.beans.ActivityDocDataOLD;
import com.e3ps.change.beans.ChangeECOSearch;
import com.e3ps.change.beans.ChangeECRSearch;
import com.e3ps.change.beans.ECAData;
import com.e3ps.change.beans.ECOData;
import com.e3ps.change.beans.ECRData;
import com.e3ps.change.beans.EChangeActivityDefinitionData;
import com.e3ps.change.beans.EChangeStopStartHistoryData;
import com.e3ps.change.service.ChangeECOHelper;
import com.e3ps.change.service.ChangeHelper;
import com.e3ps.change.service.ChangeService;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.doc.E3PSDocument;
import com.e3ps.org.bean.PeopleData;
import com.e3ps.project.ControllerException;

import wt.epm.EPMDocument;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.part.WTPartMaster;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.session.SessionContext;
import wt.session.SessionHelper;
import wt.util.WTException;

@Controller
@RequestMapping("/change")
public class ECOController {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.CHANGE.getName());
	@RequestMapping("/searchECO")
	public ModelAndView searchECO(@RequestParam Map<String, Object> reqMap) throws WTException {
		ModelAndView model = new ModelAndView();
		model.setViewName("default:/change/searchECO");
		return model;
	}
	
	@RequestMapping("/createECO")
	public ModelAndView createECO(@RequestParam Map<String, Object> reqMap) throws Exception {
		ModelAndView model = new ModelAndView();
		
		String ecrOid = StringUtil.checkNull((String)reqMap.get("ecrOid"));
		model.addObject("ecrOid", ecrOid);
		
		PeopleData pd = new PeopleData();
		model.addObject("peo", pd);
		
		model.addObject("changeType", "ECO");
		model.setViewName("default:/change/createECO");
		return model;
	}
	
	
	@RequestMapping("/viewECO2")
	public ModelAndView viewECO2(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			String isApproval = StringUtil.checkNull((String) reqMap.get("isApproval")); 
			EChangeOrder2 eco = (EChangeOrder2)CommonUtil.getObject(oid);
			ECOData data = new ECOData(eco);
			WTUser sessionUser = (WTUser)SessionHelper.manager.getPrincipal();
			String creator = eco.getOwner().getName();
			String changeOwner = eco.getChangeOwner();
			
			boolean isModify = eco.getLifeCycleState().toString().equals("INWORK") 
					&& sessionUser.getName().equals(creator);
			boolean isStop = ChangeService.ECO_ECA_WORKING.equals(data.getState()) 
					&& sessionUser.getName().equals(creator);
			boolean isRestart = ChangeService.ECO_STOPPED.equals(data.getState())
					&& sessionUser.getName().equals(creator);
			boolean isAdmin = CommonUtil.isAdmin();
			
			boolean isEcaComplite = eco.getLifeCycleState().toString().equals("ECACOMPLETE") 
					&& sessionUser.getFullName().equals(changeOwner);
			
			model.addObject("oid", oid);
			model.addObject("data", data);
			model.addObject("isModify", isModify);
			model.addObject("isStop", isStop);
			model.addObject("isRestart", isRestart);
			model.addObject("isAdmin", isAdmin);
			model.addObject("isApproval", isApproval);
			model.addObject("isEcaComplite", isEcaComplite);
			model.setViewName("popup:/change/viewECO");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	@RequestMapping("/viewECO")
	public ModelAndView viewECO(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			EChangeOrder2 eco = (EChangeOrder2)CommonUtil.getObject(oid);
			ECOData data = new ECOData(eco);
			
			model.addObject("oid", oid);
			model.addObject("data", data);
			model.setViewName("popup:/change/viewECO");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	@RequestMapping("/viewECO_include")
	public ModelAndView viewECO_include(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			EChangeOrder2 eco = (EChangeOrder2)CommonUtil.getObject(oid);
			ECOData data = new ECOData(eco);
			
			model.addObject("oid", oid);
			model.addObject("data", data);
			model.setViewName("include:/change/include/viewECO_include2");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	@RequestMapping("/include_activityECA")
	public ModelAndView include_activityECA(@RequestParam Map<String, Object> reqMap) throws Exception {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		EChangeActivity eca = (EChangeActivity)CommonUtil.getObject(oid);
		ECAData data = new ECAData(eca);
		model.addObject("oid", oid);
		model.addObject("data", data);
		model.setViewName("include:/change/include/activityECA");
		
		return model;
	}
	
	@RequestMapping("/include_activityECR")
	public ModelAndView include_activityECR(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		model.addObject("oid", oid);
		
		model.setViewName("include:/change/include/activityECR");
		
		return model;
	}
	
	@RequestMapping("/include_activityECO")
	public ModelAndView include_activityECO(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		model.addObject("oid", oid);
		
		model.setViewName("include:/change/include/activityECO");
		
		return model;
	}
	@ResponseBody
	@RequestMapping("/getEcaDocOLD")
	public static Map<String, Object> getEcaDocOLD(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<>();
		
		try {
			List<ActivityDocDataOLD> list = ChangeHelper.manager.getEcaDocOLD(reqMap);
			map.put("list", list);
			map.put("result",true);
		} catch (Exception e) {
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/getEcaDocNEW")
	public static Map<String, Object> getEcaDocNEW(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<>();
		
		try {
			List<ActivityDocDataNEW> list = ChangeHelper.manager.getEcaDocNEW(reqMap);
			map.put("list", list);
			map.put("result",true);
		} catch (Exception e) {
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	@RequestMapping("/include_relatedCommon")
	public ModelAndView relatedCommon(@RequestParam Map<String, Object> reqMap) throws Exception {
		ModelAndView model = new ModelAndView();
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		EChangeActivity eca = (EChangeActivity) CommonUtil.getObject(oid);
		ECAData data = new ECAData(eca);
		String isEdit = StringUtil.checkNull((String) reqMap.get("isEdit"));
		model.addObject("isEdit", isEdit);
		model.addObject("oid", oid);
		model.addObject("data", data);
		model.setViewName("include:/change/include/relatedCommon");
		
		return model;
	}
	
	//변경결과 도면
	@RequestMapping("/include_relatedEpm")
	public ModelAndView relatedEpm(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String isEdit = StringUtil.checkNull((String) reqMap.get("isEdit"));
		model.addObject("isEdit", isEdit);
		model.addObject("oid", oid);
		
		model.setViewName("include:/change/include/relatedEpm");
		
		return model;
	}
	
	//변경결과 부품
	@RequestMapping("/include_relatedPart")
	public ModelAndView relatedPart(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String isEdit = StringUtil.checkNull((String) reqMap.get("isEdit"));
		model.addObject("isEdit", isEdit);
		model.addObject("oid", oid);
		
		model.setViewName("include:/change/include/relatedPart");
		
		return model;
	}
	
	@RequestMapping("/include_relatedDoc")
	public ModelAndView relatedDoc(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String isEdit = StringUtil.checkNull((String) reqMap.get("isEdit"));
		model.addObject("oid", oid);
		model.addObject("isEdit", isEdit);
		model.setViewName("include:/change/include/relatedDoc");
		
		return model;
	}
	
//	@RequestMapping("/include_detailECO")
//	public ModelAndView detailECO(@RequestParam Map<String, Object> reqMap) {
//		
//		ModelAndView model = new ModelAndView();
//		
//		try {
//			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
//			EChangeOrder2 eco = (EChangeOrder2)CommonUtil.getObject(oid);
//			ECOData data = new ECOData(eco);
//			String ecrOid = "";
//			String ecrTitle = "";
//			QueryResult qr = PersistenceHelper.manager.navigate(eco,"request",RequestOrderLink.class);
//			if(qr.hasMoreElements()){
//				EChangeRequest2 ecr = (EChangeRequest2)qr.nextElement();
//				ecrOid = ecr.getPersistInfo().getObjectIdentifier().toString();
//				ecrTitle = ecr.getRequestNumber()+" : "+ecr.getName();
//			}
//			List<EChangeStopStartHistoryData> historys = new ArrayList<EChangeStopStartHistoryData>();
//			historys = ChangeECOSearch.getStopStartHistory(eco);
//			model.addObject("ecrTitle", ecrTitle);
//			model.addObject("ecrOid", ecrOid);
//			model.addObject("data", data);
//			model.addObject("ht", historys);
//			model.setViewName("include:/change/include/viewECO_include");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return model;
//	}
	
	@RequestMapping("/include_detailECO")
	public ModelAndView detailECR(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			EChangeOrder2 eco = (EChangeOrder2)CommonUtil.getObject(oid);
			ECOData data = new ECOData(eco);
			//WTUser p = ecr.getWorker();
			//String worker = p.getFullName();
			
			model.addObject("oid", oid);
			model.addObject("data", data);
			//model.addObject("worker", worker);
			model.setViewName("include:/change/include/detailECO");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	@RequestMapping("/include_changeHistory")
	public ModelAndView changeHistory(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			model.addObject("oid", oid);
			model.setViewName("include:/change/include/changeHistory");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/getChangeHistory")
	public static Map<String, Object> getChangeHistory(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<>();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			EChangeOrder2 eco = (EChangeOrder2)CommonUtil.getObject(oid);
			List<EChangeStopStartHistoryData> list = ChangeECOSearch.getStopStartHistory(eco);
			map.put("list", list);
			map.put("result",true);
		} catch (Exception e) {
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/include_viewActivityState")
	public ModelAndView viewActivityState(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			HashMap<Integer,ArrayList<ECAData>> data = ChangeHelper.manager.getActivityStepList(oid);
			model.addObject("oid", oid);
			model.addObject("data", data);
			model.setViewName("include:/change/include/ViewActivityState_include");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	@RequestMapping("/include_activity")
	public ModelAndView activity(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
		model.addObject("oid", oid);
		model.setViewName("include:/change/include/activity");
		return model;
	}
	
	@RequestMapping("/include_activityList")
	public ModelAndView activityList(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			String isApproval = StringUtil.checkNull((String) reqMap.get("isApproval")); 
			Persistable per = CommonUtil.getObject(oid);
			if(per != null) {
				if(per instanceof EChangeOrder2){
					EChangeOrder2 eco = (EChangeOrder2) per;
					ECOData data = new ECOData(eco);
					model.addObject("data", data);
				}else if(per instanceof EChangeRequest2){
					EChangeRequest2 ecr = (EChangeRequest2) per;
					ECRData data = new ECRData(ecr);
					model.addObject("data", data);
				}
			}
			model.addObject("oid", oid);
			model.addObject("isApproval", isApproval);
			model.setViewName("include:/change/include/activityList");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	@RequestMapping("/include_relatedObject")
	public ModelAndView relatedObject(@RequestParam Map<String, Object> reqMap) throws Exception {
		ModelAndView model = new ModelAndView();
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		if(oid.contains("EChangeOrder")) {
			EChangeOrder2 eco = (EChangeOrder2) CommonUtil.getObject(oid);
			ECOData data = new ECOData(eco);
			String ecaOid = data.getEcaOid();
			model.addObject("ecaOid", ecaOid);
		}
		
		String type = StringUtil.checkNull((String) reqMap.get("type"));
		model.addObject("oid", oid);
		model.addObject("type", type);
		model.setViewName("include:/change/include/relatedObject");
		
		return model;
	}
	
	@RequestMapping("/updateECO")
	public ModelAndView updateECO(@RequestParam Map<String, Object> reqMap) throws Exception {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		EChangeOrder2 eco = (EChangeOrder2)CommonUtil.getObject(oid);
		ECOData data = new ECOData(eco);
		
		model.addObject("oid", oid);
		model.addObject("data", data);
		model.setViewName("popup:/change/updateECO");
		return model;
	}
	
	@RequestMapping("/viewActivityCommonLink")
	public ModelAndView viewActivityCommonLink(@RequestParam Map<String, Object> reqMap) throws Exception {
		ModelAndView model = new ModelAndView();
		model.setViewName("popup:/change/viewActivityCommonLink");
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/createECOAction")
	public Map<String, Object> createECOAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			ChangeECOHelper.service.createEco(reqMap);
			
			map.put("msg", "등록되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/change/searchECO"));
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/updateECOAction")
	public Map<String, Object> updateECOAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			ChangeECOHelper.service.updateEco(reqMap);
			
			map.put("msg", "수정되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", "close");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/deleteECOAction")
	public Map<String, Object> deleteECOAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			ChangeECOHelper.service.deleteEco(oid);
			
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
	 * 설계변경 활동 과 문서 연결 끊기
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/deleteActivityDocLink")
	public ModelAndView deleteActivityDocLink(HttpServletRequest request, HttpServletResponse response) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ECOController.deleteActivityDocLink");
		}
		try {

			JSONObject jsonResponse = new JSONObject();
			DocumentActivityOutput output = (DocumentActivityOutput)CommonUtil.getObject(request.getParameter("loid"));
			String activeLinkType = request.getParameter("activeLinkType");
			
			if(DocumentActivityOutput.OLD_LINK.equals(activeLinkType)){
				output.setDocumentOldNumber(null);
				output.setDocumentOldVersion(null);
			}else{
				output.setDocumentNewNumber(null);
				output.setDocumentNewVersion(null);
			}
			
			if((output.getDocumentNewNumber()==null || output.getDocumentNewNumber().length()==0) && 
					(output.getDocumentOldNumber()==null || output.getDocumentOldNumber().length()==0)){
				PersistenceHelper.manager.delete(output);
			}else{
				PersistenceHelper.manager.modify(output);
			}
			
			jsonResponse.put("delete", true);

			return new ModelAndView("jsonView", "jsonObject", jsonResponse);
		} catch (Exception e) {
			LOGGER.error("ERROR",e);
			throw new ControllerException(e);
		}
	}
	/**
	 * 설계변경 활동 과 문서 연결
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/createActivityDocLink")
	public ModelAndView createActivityDocLink(HttpServletRequest request, HttpServletResponse response) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ECOController.createActivityDocLink");
		}
		try {
			JSONObject jsonResponse = new JSONObject();
			String loids = request.getParameter("loid");
			Map hash = request.getParameterMap();
			
			StringTokenizer tokens = new StringTokenizer(loids,",");
			while(tokens.hasMoreTokens()){
				String loid = tokens.nextToken();
//				WTDocument doc = (WTDocument)CommonUtil.getObject(loid);
				E3PSDocument doc = (E3PSDocument)CommonUtil.getObject(loid);
				ChangeECOHelper.service.connectEca(doc,hash);
			}
			jsonResponse.put("create", true);

			return new ModelAndView("jsonView", "jsonObject", jsonResponse);
		} catch (Exception e) {
			LOGGER.error("ERROR",e);
			throw new ControllerException(e);
		}
	}
	
	/**
	 * 설계변경 활동 과 문서 연결 끊기
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/deleteActivityPartLink")
	public ModelAndView deleteActivityPartLink(HttpServletRequest request, HttpServletResponse response) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ECOController.deleteActivityPartLink");
		}
		try {

			JSONObject jsonResponse = new JSONObject();
			EChangeActivity eca = (EChangeActivity)CommonUtil.getObject(request.getParameter("oid"));
			WTPart part = (WTPart) CommonUtil.getObject(request.getParameter("loid"));
			WTPartMaster master = part.getMaster();
			if(eca.getOrder() instanceof EChangeOrder2){
				QuerySpec qs = new QuerySpec(EcoPartLink.class);
				qs.appendWhere(new SearchCondition(EcoPartLink.class,"roleBObjectRef.key.id","=",
						eca.getOrder().getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(EcoPartLink.class,"roleAObjectRef.key.id","=",
						master.getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
				QueryResult  aqr = PersistenceHelper.manager.find(qs);
				if(aqr.hasMoreElements()){
					EcoPartLink link = (EcoPartLink)aqr.nextElement();	
					PersistenceHelper.manager.delete(link);
				}
				jsonResponse.put("delete", true);
			}else if(eca.getOrder() instanceof EChangeRequest2){
				QuerySpec qs = new QuerySpec(EcrPartLink.class);
				qs.appendWhere(new SearchCondition(EcrPartLink.class,"roleBObjectRef.key.id","=",
						eca.getOrder().getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(EcrPartLink.class,"roleAObjectRef.key.id","=",
						master.getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
				QueryResult  aqr = PersistenceHelper.manager.find(qs);
				if(aqr.hasMoreElements()){
					EcrPartLink link = (EcrPartLink)aqr.nextElement();	
					PersistenceHelper.manager.delete(link);
				}
				jsonResponse.put("delete", true);
			}
			

			return new ModelAndView("jsonView", "jsonObject", jsonResponse);
		} catch (Exception e) {
			LOGGER.error("ERROR",e);
			throw new ControllerException(e);
		}
	}
	/**
	 * 설계변경 활동 과 문서 연결
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/createActivityPartLink")
	public ModelAndView createActivityPartLink(HttpServletRequest request, HttpServletResponse response) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ECOController.createActivityPartLink");
		}
		try {
			JSONObject jsonResponse = new JSONObject();
			String loids = request.getParameter("loid");
			Map hash = request.getParameterMap();
			
			StringTokenizer tokens = new StringTokenizer(loids,",");
			while(tokens.hasMoreTokens()){
				String loid = tokens.nextToken();
				WTPart doc = (WTPart)CommonUtil.getObject(loid);
				ChangeECOHelper.service.connectEcaPart(doc,hash);
			}
			jsonResponse.put("create", true);

			return new ModelAndView("jsonView", "jsonObject", jsonResponse);
		} catch (Exception e) {
			LOGGER.error("ERROR",e);
			throw new ControllerException(e);
		}
	}
	
	
	/**
	 * 설계변경 결과 도면 연결
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/createActivityRDrawingLink")
	public ModelAndView createActivityRDrawingLink(HttpServletRequest request, HttpServletResponse response) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ECOController.createActivityRDrawingLink");
		}
		try {
			JSONObject jsonResponse = new JSONObject();
			String activeOid = request.getParameter("activeOid");
			String loids = request.getParameter("loid");
			
			EChangeActivity active = (EChangeActivity)CommonUtil.getObject(activeOid);
			if(active.getOrder() instanceof EChangeOrder2){
				StringTokenizer tokens = new StringTokenizer(loids,",");
				while(tokens.hasMoreTokens()){
					String loid = tokens.nextToken();
					EPMDocument doc = (EPMDocument)CommonUtil.getObject(loid);
					EcoTargetResultLink link = EcoTargetResultLink.newEcoTargetResultLink((EChangeOrder2)active.getOrder(),doc);
					PersistenceHelper.manager.save(link);
					jsonResponse.put("msg", "링크 연결되었습니다.");
//					if("APPROVED".equals(doc.getState().toString())) {
//						EcoTargetResultLink link = EcoTargetResultLink.newEcoTargetResultLink((EChangeOrder2)active.getOrder(),doc);
//						PersistenceHelper.manager.save(link);
//						jsonResponse.put("msg", "연결되었습니다.");
//					}else {
//						LOGGER.debug("doc state -> "+doc.getState().toString());
//						jsonResponse.put("msg", "실패");
//					}
					
				}
				jsonResponse.put("create", true);
			}else if(active.getOrder() instanceof EChangeRequest2) {
				StringTokenizer tokens = new StringTokenizer(loids,",");
				while(tokens.hasMoreTokens()){
					String loid = tokens.nextToken();
					EPMDocument doc = (EPMDocument)CommonUtil.getObject(loid);
					EcrTargetLink link = EcrTargetLink.newEcrTargetLink((EChangeRequest2)active.getOrder(),doc);
					PersistenceHelper.manager.save(link);
					jsonResponse.put("msg", "링크 연결되었습니다.");
				}
				jsonResponse.put("create", true);
			}
			return new ModelAndView("jsonView", "jsonObject", jsonResponse);
		} catch (Exception e) {
			LOGGER.error("ERROR",e);
			throw new ControllerException(e);
		}
	}
	
	
	/**
	 * 설계변경 과 결과도면 연결 끊기
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/deleteActivityRDrawingLink")
	public ModelAndView deleteActivityRDrawingLink(HttpServletRequest request, HttpServletResponse response) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ECOController.deleteActivityRDrawingLink");
		}
		try {

			JSONObject jsonResponse = new JSONObject();
			EChangeActivity eca = (EChangeActivity)CommonUtil.getObject(request.getParameter("oid"));
			EPMDocument epm = (EPMDocument) CommonUtil.getObject(request.getParameter("loid"));
			if(eca.getOrder() instanceof EChangeOrder2){
				QuerySpec qs = new QuerySpec(EcoTargetResultLink.class);
				qs.appendWhere(new SearchCondition(EcoTargetResultLink.class,"roleAObjectRef.key.id","=",
						eca.getOrder().getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(EcoTargetResultLink.class,"roleBObjectRef.key.id","=",
						epm.getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
				QueryResult aqr = PersistenceHelper.manager.find(qs);
				if(aqr.hasMoreElements()){
					EcoTargetResultLink link = (EcoTargetResultLink)aqr.nextElement();	
					PersistenceHelper.manager.delete(link);
				}
				jsonResponse.put("delete", true);
			}else if(eca.getOrder() instanceof EChangeRequest2) {
				QuerySpec qs = new QuerySpec(EcrTargetLink.class);
				qs.appendWhere(new SearchCondition(EcrTargetLink.class,"roleAObjectRef.key.id","=",
						eca.getOrder().getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
				qs.appendAnd();
				qs.appendWhere(new SearchCondition(EcrTargetLink.class,"roleBObjectRef.key.id","=",
						epm.getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
				QueryResult aqr = PersistenceHelper.manager.find(qs);
				if(aqr.hasMoreElements()){
					EcrTargetLink link = (EcrTargetLink)aqr.nextElement();	
					PersistenceHelper.manager.delete(link);
				}
				jsonResponse.put("delete", true);
			}
			

			return new ModelAndView("jsonView", "jsonObject", jsonResponse);
		} catch (Exception e) {
			LOGGER.error("ERROR",e);
			throw new ControllerException(e);
		}
	}
	
	/**
	 * 설계변경 활동 과 문서 연결
	 * @param request
	 * @param response
	 * @return
	 */
//	@RequestMapping("/createActivityCommentAdd")
//	public ModelAndView createActivityCommentAdd(HttpServletRequest request, HttpServletResponse response) {
//		if (LOGGER.isDebugEnabled()) {
//			LOGGER.debug("ECOController.createActivityCommentAdd");
//		}
//		try {
//			JSONObject jsonResponse = new JSONObject();
//			String actOid = StringUtil.checkNull(request.getParameter("actOid"));
//			String comment = StringUtil.checkNull(request.getParameter("comments"));
//			String primary = StringUtil.checkNull((String) request.getParameter("PRIMARY"));
//			List<String> secondary = StringUtil.checkReplaceArray(request.getParameterValues("SECONDARY"));
//			List<String> delocIds		= StringUtil.checkReplaceArray(request.getParameterValues("delocIds"));
//			
//			EChangeActivity active = (EChangeActivity)CommonUtil.getObject(actOid);
//			LOGGER.debug("comment -> "+comment);
//			active.setComments(comment);
//			
//			PersistenceHelper.manager.modify(active);
//			
//			CommonContentHelper.service.attach((ContentHolder)active, primary, secondary,delocIds);
//			active = (EChangeActivity)PersistenceHelper.manager.refresh(active);
//			jsonResponse.put("create", true);
//
//			return new ModelAndView("jsonView", "jsonObject", jsonResponse);
//		} catch (Exception e) {
//			LOGGER.error("ERROR",e);
//			throw new ControllerException(e);
//		}
//	}
//	@ResponseBody
//	@RequestMapping("/createActivityCommentAdd")
//	public Map<String, Object> createActivityCommentAdd(@RequestBody Map<String, Object> reqMap) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		
//		try {
//			String actOid = StringUtil.checkNull((String) reqMap.get("actOid"));
//			String comment = StringUtil.checkNull((String) reqMap.get("comments"));
//			String primary = StringUtil.checkNull((String) reqMap.get("PRIMARY"));
//			List<String> secondary = StringUtil.checkReplaceArray(reqMap.get("SECONDARY"));
//			List<String> delocIds		= StringUtil.checkReplaceArray(reqMap.get("delocIds"));
//			
//			EChangeActivity active = (EChangeActivity)CommonUtil.getObject(actOid);
//			LOGGER.debug("actOid -> "+actOid);
//			LOGGER.debug("comment -> "+comment);
//			active.setComments(comment);
//			
//			PersistenceHelper.manager.modify(active);
//			
//			CommonContentHelper.service.attach((ContentHolder)active, primary, secondary,delocIds);
//			active = (EChangeActivity)PersistenceHelper.manager.refresh(active);
//			
//			map.put("msg", "저장 되었습니다.");
//			map.put("result", true);
////			map.put("redirectUrl", "close");
//		} catch (Exception e) {
//			e.printStackTrace();
//			map.put("result", false);
//			map.put("msg", "ERROR = " + e.getLocalizedMessage());
//		}
//		
//		return map;
//	}
	
	@ResponseBody
	@RequestMapping("/searchECOAction")
	public Map<String, Object> searchECOAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		SessionContext prev = SessionContext.newContext();
		try {
			SessionHelper.manager.setAdministrator();
			map = ChangeHelper.manager.getECOList(reqMap);
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		} finally {
			SessionContext.setContext(prev);
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/searchECRList")
	public Map<String, Object> searchECRList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			List<ECRData> list = ChangeHelper.manager.getECRListQuick(reqMap);
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
	 * @desc	: 관련 ECO include
	 * @author	: shkim
	 * @date	: 2020. 9. 16.
	 * @method	: relatedECO
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_relatedECO")
	public ModelAndView relatedECO(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String oid  = StringUtil.checkNull((String) reqMap.get("oid"));
		model.addObject("oid", oid);
		
		model.setViewName("include:/change/include/relatedECO");
		return model;
	}
	
	/**
	 * @desc	: 관련 ECO 가져오기
	 * @author	: shkim
	 * @date	: 2020. 9. 16.
	 * @method	: getRelatedECO
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getRelatedECO")
	public Map<String, Object> getRelatedECO(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<>();
		
		List<ECOData> list = new ArrayList<ECOData>();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			
			Persistable per = CommonUtil.getObject(oid);
			
			if(per instanceof EChangeRequest2) {
				EChangeRequest2 ecr = (EChangeRequest2) per;
				list = ChangeECRSearch.getRelatedEco(ecr);
			} else if(per instanceof WTPart) {
				WTPart part = (WTPart) per;
				list = ChangeECOHelper.manager.getRelatedECODataList(part);
			}
			
			map.put("list", list);
			map.put("result",true);
		}catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 관련 ECR include
	 * @author	: shkim
	 * @date	: 2020. 9. 16.
	 * @method	: relatedECO
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_relatedECR")
	public ModelAndView relatedECR(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String oid  = StringUtil.checkNull((String) reqMap.get("oid"));
		
		model.addObject("oid", oid);
		
		model.setViewName("include:/change/include/relatedECR");
		return model;
	}
	
	/**
	 * @desc	: 관련 ECR 가져오기
	 * @author	: shkim
	 * @date	: 2020. 9. 16.
	 * @method	: getRelatedECO
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getRelatedECR")
	public Map<String, Object> getRelatedECR(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<>();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			EChangeOrder2 eco = (EChangeOrder2)CommonUtil.getObject(oid);
			
			List<ECRData> list = ChangeECOSearch.getRelatedEcr(eco);
			
			map.put("list", list);
			map.put("result",true);
		}catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/getActivity")
	public Map<String, Object> getActivity(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<ECAData> list = ChangeECOHelper.manager.getActivity(reqMap);
			
			map.put("list", list);
			map.put("result",true);
		}catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/viewChangeActivity")
	public ModelAndView viewChangeActivity(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		String oid  = StringUtil.checkNull((String) reqMap.get("oid"));
		EChangeActivity eca = (EChangeActivity)CommonUtil.getObject(oid);
		String activeType = eca.getActiveType();
		model.addObject("oid", oid);
		model.addObject("activeType", activeType);
		model.setViewName("include:/change/viewChangeActivity");
		return model;
	}
	
	@RequestMapping("/viewChangeActivityPopup")
	public ModelAndView viewChangeActivityPopup(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		String oid  = StringUtil.checkNull((String) reqMap.get("oid"));
		EChangeActivity eca = (EChangeActivity)CommonUtil.getObject(oid);
		String activeType = eca.getActiveType();
		model.addObject("oid", oid);
		model.addObject("activeType", activeType);
		model.setViewName("popup:/change/viewChangeActivity");
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/getActivitySelf")
	public Map<String, Object> getActivitySelf(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<ECAData> list = new ArrayList<>();
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			EChangeActivity eca = (EChangeActivity) CommonUtil.getObject(oid);
			ECAData data = new ECAData(eca);
			list.add(data);
			
			map.put("list", list);
			map.put("result",true);
		}catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * ECO 생성화면에서 활동 추가
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addActivity")
	public ModelAndView addActivity(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		String oid  = StringUtil.checkNull((String) reqMap.get("oid"));
		model.addObject("oid", oid);
		model.setViewName("popup:/change/addActivity");
		return model;
	}
	
	@RequestMapping("/setECADefinition")
	public ModelAndView setECADefinition(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		model.setViewName("include:/change/include/setECADefinition");

		return model;
	}
	
	@ResponseBody
	@RequestMapping("/stopECOAction")
	public Map<String, Object> stopECOAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			ChangeECOHelper.service.stopECO(reqMap);
			
			map.put("msg", "중단 되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", "close");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/restartECOAction")
	public Map<String, Object> restartECOAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			ChangeECOHelper.service.restartECO(reqMap);
			
			map.put("msg", "재시작 되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", "close");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/stopECO")
	public ModelAndView stopECO(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		String oid  = StringUtil.checkNull((String) reqMap.get("oid"));
		String stop  = StringUtil.checkNull((String) reqMap.get("stop"));
		String s = "";
		if(stop.equals("true")){
			s = "중단";
		}else if(stop.equals("false")){
			s = "재시작";
		}
		model.addObject("oid", oid);
		model.addObject("stop", stop);
		model.addObject("s", s);
		model.setViewName("popup:/change/stopECO");
		return model;
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
			LOGGER.debug("ECOController.sendErpAction");
		}
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			EChangeOrder2 eco = (EChangeOrder2)CommonUtil.getObject(oid);
			//ERPInterface.send(eco);
			
			map.put("result", true);
			map.put("msg", "전송하였습니다.");
//			map.put("redirectUrl", "");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/getActivityDef")
	public Map<String, Object> getActivityDef(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<EChangeActivityDefinitionData> list = ChangeECOHelper.manager.getActivityDef(reqMap);
			
			map.put("list", list);
			map.put("result",true);
		}catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	
	// TODO
	/**************************************************************************************************/
	
	@RequestMapping("/include_ecaInfo")
	public ModelAndView include_ecaInfo(@RequestParam Map<String, Object> reqMap) throws Exception{
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		EChangeActivity eca = (EChangeActivity) CommonUtil.getObject(oid);
		ECAData data = new ECAData(eca);
		
		model.addObject("oid", oid);
		model.addObject("ecaData", data);
		model.setViewName("include:/change/include/ecaInfo");
		
		return model;
	}
	
	/**
	 * ECA 활동 화면 정의
	 * @methodName : include_ecaActiveCreate
	 * @author : tsuam
	 * @date : 2021.11.22
	 * @return : ModelAndView
	 * @description :
	 */
	@RequestMapping("/include_ecaActiveCreate")
	public ModelAndView include_ecaActiveCreate(@RequestParam Map<String, Object> reqMap) throws Exception{
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		EChangeActivity eca = (EChangeActivity) CommonUtil.getObject(oid);
		ECAData data = new ECAData(eca);
		
		model.addObject("oid", oid);
		model.addObject("ecaData", data);
		model.addObject("activeType", data.getActiveType());
		model.setViewName("include:/change/include/ecaActiveCreate");
		
		return model;
	}
	
	/**
	 * 도면/EBOM 변경
	 * @methodName : include_active_ReviseBom
	 * @author : tsuam
	 * @date : 2021.12.20
	 * @return : ModelAndView
	 * @description :
	 */
	@RequestMapping("/include_active_ReviseBom")
	public ModelAndView include_active_ReviseBom(@RequestParam Map<String, Object> reqMap) throws Exception{
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		EChangeActivity eca = (EChangeActivity) CommonUtil.getObject(oid);
		ECAData data = new ECAData(eca);
		EChangeOrder2 eco = (EChangeOrder2) CommonUtil.getObject(data.getOrder());
		ECOData ecoData = new ECOData(eco);
		
		
		model.addObject("oid", oid);
		model.addObject("eca", data);
		model.addObject("ecoData", ecoData);
		model.setViewName("include:/change/include/active_ReviseBom");
		
		return model;
	}
	
	/**
	 * ECO 관련 품목 - 도면/BOM 변경
	 * @methodName : include_reviseECOPart
	 * @author : tsuam
	 * @date : 2021.12.09
	 * @return : ModelAndView
	 * @description :
	 */
	@RequestMapping("/include_reviseECOPart")
	public ModelAndView include_reviseECOPart(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String rowCheck = StringUtil.checkReplaceStr((String) reqMap.get("rowCheck"), "false");
		String autoGridHeight = StringUtil.checkReplaceStr((String) reqMap.get("autoGridHeight"), "false");
		String gridHeight = StringUtil.checkReplaceStr((String) reqMap.get("gridHeight"), "200");

		model.addObject("oid", oid);
		model.addObject("rowCheck", rowCheck);
		model.addObject("autoGridHeight", autoGridHeight);
		model.addObject("gridHeight", gridHeight);

		model.setViewName("include:/change/include/reviseECOPart");

		return model;
	}
	
	/**
	 * ECO 관련 품목의 - 3D,2D,제어 도면,제어 문서
	 * @methodName : include_relatedECOEPMDoc
	 * @author : tsuam
	 * @date : 2021.12.09
	 * @return : ModelAndView
	 * @description :
	 */
	@RequestMapping("/include_relatedECOEPMDoc")
	public ModelAndView include_relatedECOEPMDoc(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		

		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String rowCheck = StringUtil.checkReplaceStr((String) reqMap.get("rowCheck"), "false");
		String autoGridHeight = StringUtil.checkReplaceStr((String) reqMap.get("autoGridHeight"), "false");
		String gridHeight = StringUtil.checkReplaceStr((String) reqMap.get("gridHeight"), "200");
		
		EChangeOrder2 eco = (EChangeOrder2)CommonUtil.getObject(oid);
		
		
		model.addObject("oid", oid);
		model.addObject("eoType", eco.getType());
		model.addObject("rowCheck", rowCheck);
		model.addObject("autoGridHeight", autoGridHeight);
		model.addObject("gridHeight", gridHeight);
		
		model.setViewName("include:/change/include/relatedECOEPMDoc");
		
		return model;
	}
	
	/**
	 * @methodName : getRelatedPart - ECO - 도면/BOM 변경
	 * @author : hckim
	 * @date : 2021.09.07
	 * @return : Map<String,Object>
	 * @description :  
	 */
	@ResponseBody
	@RequestMapping("/getECOActivePartList")
	public static Map<String, Object> getECOActivePartList(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<>();
		try {
			String oid = (String)reqMap.get("oid");
			String checkType = (String)reqMap.get("checkType");  //개정 유무 체크
			EChangeOrder2 eco = (EChangeOrder2)CommonUtil.getObject(oid);
		
			List<Map<String, Object>> list = ChangeECOHelper.manager.getECOActivePartList(eco,checkType);
			
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
	@RequestMapping("/getECOActiveEPMList")
	public static Map<String, Object> getECOActiveEPMList(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<>();
		try {
			String oid = (String)reqMap.get("oid");
			
			EChangeOrder2 eco = (EChangeOrder2)CommonUtil.getObject(oid);
			
			List<Map<String, Object>> list = ChangeECOHelper.manager.getECOActiveEPMList(eco);

			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}

		return map;
	}
	
	/**
	 * ECO - 도면/BOM 변경에서 일괄 개정
	 * @methodName : batchECOPartRevise
	 * @author : tsuam
	 * @date : 2021.12.23
	 * @return : Map<String,Object>
	 * @description :
	 */
	@ResponseBody
	@RequestMapping("/batchECOPartRevise")
	public static Map<String, Object> batchECOPartRevise(@RequestBody Map<String, Object> reqMap,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		System.out.println("======= controller batchECOPartRevise =======");
		try {
			//IP 저장
			String ip = CommonUtil.getClientIP((HttpServletRequest) request);
			reqMap.put("USERIP", ip);	
			String oid = (String)reqMap.get("oid");
			List<Map<String,Object>> checkItemList = (List<Map<String,Object>>)reqMap.get("checkItemList");
			System.out.println("batchECOPartRevise checkItemList =" + checkItemList);
			
			map = ChangeECOHelper.service.batchECOPartRevise(reqMap);
			
		
		}catch(Exception e) {
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * ECA 정보
	 * @methodName : include_detailECA
	 * @author : tsuam
	 * @date : 2021.11.23
	 * @return : ModelAndView
	 * @description :
	 */
	@RequestMapping("/include_detailECA")
	public ModelAndView include_detailECA(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			
			EChangeActivity eca = (EChangeActivity)CommonUtil.getObject(oid);

			ECAData data = new ECAData(eca);
			
			model.addObject("oid", oid);
			model.addObject("eca", data);
			model.setViewName("include:/change/include/detailECA");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	/**
	 * ECA 상세보기
	 * @methodName : viewECA
	 * @author : tsuam
	 * @date : 2021.11.23
	 * @return : ModelAndView
	 * @description :
	 */
	@RequestMapping("/viewECA")
	public ModelAndView viewECA(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			EChangeActivity eca = (EChangeActivity)CommonUtil.getObject(oid);

			ECAData data = new ECAData(eca);
			
			model.addObject("oid", oid);
			model.addObject("eca", data);
			
			model.setViewName("popup:/change/viewECA");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	/**
	 * ECA 활동별 상세 화면
	 * @methodName : include_ecaActiveView
	 * @author : tsuam
	 * @date : 2021.11.23
	 * @return : ModelAndView
	 * @description :
	 */
	@RequestMapping("/include_ecaActiveView")
	public ModelAndView include_ecaActiveView(@RequestParam Map<String, Object> reqMap) throws Exception{
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		EChangeActivity eca = (EChangeActivity)CommonUtil.getObject(oid);
		ECAData data = new ECAData(eca);
		
		model.addObject("oid", oid);
		model.addObject("activeType", data.getActiveType());
		model.setViewName("include:/change/include/ecaActiveView");
		
		return model;
	}
	
	/**
	 * 도면/EBOM 변경 View
	 * @methodName : view_ReviseBom
	 * @author : tsuam
	 * @date : 2022.01.17
	 * @return : ModelAndView
	 * @description :
	 */
	@RequestMapping("/include_view_ReviseBom")
	public ModelAndView view_ReviseBom(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			
			EChangeActivity eca = (EChangeActivity)CommonUtil.getObject(oid);
			
			ECAData data = new ECAData(eca);
			EChangeOrder2 eco = (EChangeOrder2) CommonUtil.getObject(data.getOrderOid());
			ECOData ecOData = new ECOData(eco);
			
			model.addObject("oid", oid);
			model.addObject("eca", data);
			model.addObject("ecOData", ecOData);
			model.setViewName("include:/change/include/view_ReviseBom");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	/**
	 * ECO 관련 품목
	 * @methodName : include_relatedECOPart
	 * @author : tsuam
	 * @date : 2021.12.09
	 * @return : ModelAndView
	 * @description :
	 */
	@RequestMapping("/include_relatedECOPart")
	public ModelAndView include_relatedECOPart(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String rowCheck = StringUtil.checkReplaceStr((String) reqMap.get("rowCheck"), "false");
		String autoGridHeight = StringUtil.checkReplaceStr((String) reqMap.get("autoGridHeight"), "false");
		String gridHeight = StringUtil.checkReplaceStr((String) reqMap.get("gridHeight"), "200");

		model.addObject("oid", oid);
		model.addObject("rowCheck", rowCheck);
		model.addObject("autoGridHeight", autoGridHeight);
		model.addObject("gridHeight", gridHeight);
		
		model.setViewName("include:/change/include/relatedECOPart");

		return model;
	}
	
	/**
	 * ECO 관련 도면 (3D,2D,관련 도면
	 * @methodName : include_relatedECOPart
	 * @author : tsuam
	 * @date : 2021.12.09
	 * @return : ModelAndView
	 * @description :
	 */
	@RequestMapping("/include_relatedECOEPM")
	public ModelAndView include_relatedECOEPM(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();

		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String rowCheck = StringUtil.checkReplaceStr((String) reqMap.get("rowCheck"), "false");
		String autoGridHeight = StringUtil.checkReplaceStr((String) reqMap.get("autoGridHeight"), "false");
		String gridHeight = StringUtil.checkReplaceStr((String) reqMap.get("gridHeight"), "200");

		model.addObject("oid", oid);
		model.addObject("rowCheck", rowCheck);
		model.addObject("autoGridHeight", autoGridHeight);
		model.addObject("gridHeight", gridHeight);

		model.setViewName("include:/change/include/relatedECOEPM");

		return model;
	}
	
	@ResponseBody
	@RequestMapping("/getECOStateList")
	public Map<String, Object> searchDocAttrAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<String> list = ChangeECOHelper.manager.getECOStateList();
			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
}
