package com.e3ps.stagegate.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import com.e3ps.change.beans.ECRData;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.project.EProject;
import com.e3ps.project.beans.ProjectData;
import com.e3ps.project.beans.ProjectRoleData;
import com.e3ps.project.service.ProjectHelper;
import com.e3ps.project.service.ProjectMemberHelper;
import com.e3ps.stagegate.SGObject;
import com.e3ps.stagegate.SGObjectMaster;
import com.e3ps.stagegate.SGObjectValue;
import com.e3ps.stagegate.StageGate;
import com.e3ps.stagegate.bean.SGChartData;
import com.e3ps.stagegate.bean.SGObjectMasterData;
import com.e3ps.stagegate.bean.SGObjectValueData;
import com.e3ps.stagegate.bean.StageGateData;
import com.e3ps.stagegate.service.SgHelper;

@Controller
@RequestMapping("/gate")
public class StageGateController {
	
	@ResponseBody
	@RequestMapping("/createGateAction")
	public Map<String, Object> createGateAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			EProject project = (EProject) CommonUtil.getObject(oid);
			String code = project.getCode();
			boolean sg = SgHelper.manager.checkStageGateList(code);
			if(sg) {
				map.put("result", false);
				map.put("msg", "스테이지 게이트 리뷰가 이미 존재합니다.");
				return map;
			}
			SgHelper.service.createGateAction(reqMap);
			
			map.put("msg", "등록되었습니다.");
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
	@RequestMapping("/deleteGateAction")
	public Map<String, Object> deleteGateAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			SGObjectMaster objMaster = (SGObjectMaster) CommonUtil.getObject(oid);
			StageGate sg = objMaster.getStageGate();
			SgHelper.service.deleteGateAction(sg);
			map.put("msg", "삭제되었습니다.");
			map.put("result", true);
			//map.put("redirectUrl", CommonUtil.getURLString("/project/viewMain") + "?oid=" + oid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/viewStageGate")
	public ModelAndView viewStageGate(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			EProject project = (EProject) CommonUtil.getObject(oid);
			String code = project.getCode();
			//StageGate sg = SgHelper.manager.getStageGate(code);
			SGObjectMaster objMaster = SgHelper.manager.getSGMaster(code);
			
			StageGateData data = new StageGateData(objMaster);
			ProjectData pData = new ProjectData(project);
			boolean isAuth = ProjectHelper.manager.isAuth(project);
			List<SGObjectValueData> list = SgHelper.manager.getValueList(objMaster, "LIGHT");
			
			String viewOnly = "false";
			
			model.addObject("pData", pData);
			model.addObject("data", data);
			model.addObject("lightList", list);
			model.addObject("viewOnly", viewOnly);
			model.addObject("isAuth", isAuth);
			model.setViewName("empty:/stagegate/viewStageGate");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	@RequestMapping("/include_tab")
	public ModelAndView include_tab(@RequestParam Map<String, Object> reqMap) {
		// SUMMARY TIMING QUALITY CSTOP RISK DS REVIEWS PRODUCTDEV ECR RECORD
		ModelAndView model = new ModelAndView();
		List<SGObjectValueData> list = null;
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			String objType = StringUtil.checkNull((String) reqMap.get("objType")); 
			String viewOnly = StringUtil.checkNull((String) reqMap.get("viewOnly")); 
			
			SGObjectMaster objMaster = (SGObjectMaster) CommonUtil.getObject(oid);
			StageGateData data = new StageGateData(objMaster);
			
			
			String viewName = "include:/stagegate/include/"+objType.toLowerCase();
			
			if("SUMMARY".equals(objType)) {
				SGObject obj = SgHelper.manager.getObjBySg(objMaster, objType);
				String objOid = CommonUtil.getOIDString(obj);
				URL imgUrl = SgHelper.manager.getImgURL(obj);
				EProject project = data.getProject();
				ProjectData pData = new ProjectData(project);
				List<ProjectRoleData> members = ProjectMemberHelper.manager.getRoleUserList(project);
				list = SgHelper.manager.getValueList(objMaster, objType);
				for(SGObjectValueData sData : list) {
					model.addObject("sData", sData);
				}
				model.addObject("oid", oid);
				model.addObject("objOid", objOid);
				model.addObject("pData", pData);
				model.addObject("members", members);
				model.addObject("imgUrl", imgUrl);
				model.addObject("viewOnly", viewOnly);
				
			}else if("TIMING".equals(objType)) {
				EProject project = data.getProject();
				String pOid = CommonUtil.getOIDString(project);
				ProjectData pData = new ProjectData(project);
				model.addObject("isSG", true);
				model.addObject("project", pData);
				model.addObject("oid", pOid);
				viewName = "empty:/stagegate/include/"+objType.toLowerCase();
			}else if("QUALITY".equals(objType)) {
				list = SgHelper.manager.getChildValueList(objMaster, objType);
				List<SGObjectValueData> parentList = SgHelper.manager.getParentValueList(objMaster, objType);
				model.addObject("childList", list);
				model.addObject("parentList", parentList);
				model.addObject("viewOnly", viewOnly);
			}else if("CSTOP".equals(objType)) {
				SGObject obj = SgHelper.manager.getObjBySg(objMaster, objType);
				String objOid = CommonUtil.getOIDString(obj);
				model.addObject("objOid", objOid);
				model.addObject("oid", oid);
				model.addObject("objType", objType);
				model.addObject("viewOnly", viewOnly);
			}else if("RISK".equals(objType)) {
				model.addObject("oid", oid);
				model.addObject("objType", objType);
				model.addObject("viewOnly", viewOnly);
			}else if("DS".equals(objType)) {
				model.addObject("oid", oid);
				model.addObject("objType", objType);
				model.addObject("viewOnly", viewOnly);
			}else if("REVIEWS".equals(objType)) {
				list = SgHelper.manager.getChildValueList(objMaster, objType);
				List<SGObjectValueData> parentList = SgHelper.manager.getParentValueList(objMaster, objType);
				
				model.addObject("childList", list);
				model.addObject("parentList", parentList);
				
				int numOfGateReview = 6;
				List<String> manager[] = new List[numOfGateReview];
				
				for(int i =0; i< numOfGateReview ; i++) {
					manager[i] = new ArrayList<String>();
				}
				
				String inputs[][] = {
						{"PM","PM","경영관리","영업","PM","품질"},
						{"영업","PM","설계","PM","제조","생기","개발"},
						{"영업","설계","설계","설계","제조","생기","설계","생기","생기","제조","개발","개발"},
						{"설계","생기","생기","생기","생기","생기","생기","구매","생기","구매","구매","품질","품질"},
						{"PM","영업","영업","품질","제조","생기","품질","품질","생기","생기","구매","제조","품질"},
						{"PM","PM","설계","제조","생기","품질"},
				};
				
				int idx = 0;
				
				for(String[] strs : inputs) {
					for(String str : strs ) {
						manager[idx].add(str);
					}
					idx++;
				}
				for(int i =0; i < numOfGateReview; i++) {
					model.addObject("manager"+i,manager[i]);
				}
				
				model.addObject("oid", oid);
				model.addObject("viewOnly", viewOnly);
			}else if("PRODUCTDEV".equals(objType)) {
				SGObject obj = SgHelper.manager.getObjBySg(objMaster, objType);
				String devOid = CommonUtil.getOIDString(obj);
				URL imgUrl = SgHelper.manager.getImgURL(obj);
				list = SgHelper.manager.getValueList(objMaster, objType);
				
				model.addObject("oid", devOid);
				model.addObject("imgUrl", imgUrl);
				model.addObject("sgCodeList", list);
				model.addObject("viewOnly", viewOnly);
			}else if("ECR".equals(objType)) {
				StageGate sg = objMaster.getStageGate();
				EProject project = sg.getProject();
				List<ECRData> ecrList = SgHelper.manager.getECRList(project);
				model.addObject("list", ecrList);
			}else if("RECORD".equals(objType)) {
				StageGate sg = objMaster.getStageGate();
				List<SGObjectMasterData> masterList = SgHelper.manager.getMasterList(sg);
				model.addObject("list", masterList);
			}
			
			model.setViewName(viewName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}

	@ResponseBody
	@RequestMapping("/modifyObjectValue")
	public Map<String, Object> modifyObjectValue(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
//			reqMap.entrySet().forEach((entry) -> System.out.println(
//			        "key: " + entry.getKey() + ", value: " + entry.getValue()));
			
			SgHelper.service.modifyObjectValue(reqMap);
			
			map.put("msg", "저장되었습니다.");
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/uploadFile")
	public Map<String, Object> uploadFile(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> fileMap = new LinkedHashMap<String, Object>();
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			fileMap = (Map<String, Object>) reqMap.get("file");
			fileMap.put("oid", oid);
//			SgHelper.service.uploadFile(fileMap);
			SgHelper.service.uploadPrimary(fileMap);
			
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping("/modifyQuality")
	public Map<String, Object> modifyQuality(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			ArrayList list = (ArrayList) reqMap.get("list");
			
			for(int i=0; list.size() > i;i++) {
				Map<String, Object> serviceMap = new HashMap<String, Object>();
				
				ArrayList rowList = (ArrayList) list.get(i);
				String[] row = (String[]) rowList.toArray(new String[rowList.size()]);
				
				serviceMap.put("type", "quality");
				serviceMap.put("oid", row[0]);
				serviceMap.put("value0", row[1]);
				serviceMap.put("value1", row[2]);
				serviceMap.put("value2", row[3]);
				serviceMap.put("value3", row[4]);
				serviceMap.put("value4", row[5]);
				serviceMap.put("value5", row[6]);
				serviceMap.put("value6", row[7]);
				serviceMap.put("value7", row[8]);
				serviceMap.put("value8", row[9]);
				serviceMap.put("value9", row[10]);
				
				SgHelper.service.modifyObjectValue(serviceMap);
			}
			
			map.put("msg", "저장되었습니다.");
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/getParentValueList")
	public Map<String, Object> getParentValueList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			String objType = StringUtil.checkNull((String) reqMap.get("objType")); 
			SGObjectMaster objMaster = (SGObjectMaster) CommonUtil.getObject(oid);
			
			List<SGObjectValueData> list = SgHelper.manager.getParentValueList(objMaster, objType);
			
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
	@RequestMapping("/getChildValueList")
	public Map<String, Object> getChildValueList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			String objType = StringUtil.checkNull((String) reqMap.get("objType")); 
			SGObjectMaster objMaster = (SGObjectMaster) CommonUtil.getObject(oid);
			
			List<SGObjectValueData> list = SgHelper.manager.getChildValueList(objMaster, objType);
			
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
	@RequestMapping("/getValueList")
	public Map<String, Object> getValueList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			String objType = StringUtil.checkNull((String) reqMap.get("objType")); 
			SGObjectMaster objMaster = (SGObjectMaster) CommonUtil.getObject(oid);
			
			List<SGObjectValueData> list = SgHelper.manager.getValueList(objMaster, objType);
			
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
	@RequestMapping("/modifyObjectValueList")
	public Map<String, Object> modifyObjectValueList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			List<Map<String, Object>> list =  (List<Map<String, Object>>) reqMap.get("list");
			
			for(Map<String, Object> valueMap : list) {
//				valueMap.entrySet().forEach((entry) -> System.out.println(
//				        "key: " + entry.getKey() + ", value: " + entry.getValue()));
				
				SgHelper.service.modifyObjectValue(valueMap);
			}
			
			map.put("msg", "저장되었습니다.");
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/upsertObjectValueList")
	public Map<String, Object> createRiskValue(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			String objType = StringUtil.checkNull((String) reqMap.get("objType"));
			SGObjectMaster objMaster = (SGObjectMaster) CommonUtil.getObject(oid);
			SGObject obj = SgHelper.manager.getObjByType(objMaster, objType);
			List<Map<String, Object>> list =  (List<Map<String, Object>>) reqMap.get("list");
			
			for(Map<String, Object> valueMap : list) {
				if(valueMap == null) {
					continue;
				}
//				valueMap.entrySet().forEach((entry) -> System.out.println(
//						"key: " + entry.getKey() + ", value: " + entry.getValue()));
				
				SgHelper.service.upsertObjectValue(obj, valueMap);
			}
			
			map.put("msg", "저장되었습니다.");
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping("/modifyLight")
	public Map<String, Object> modifyLight(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			ArrayList list = (ArrayList) reqMap.get("list");
			
			Map<String, Object> serviceMap = new HashMap<String, Object>();
			
			ArrayList rowList = (ArrayList) list.get(0);
			String[] row = (String[]) rowList.toArray(new String[rowList.size()]);
			serviceMap.put("oid", row[0]);
			serviceMap.put("value1", row[1]);
			serviceMap.put("value2", row[2]);
			SgHelper.service.modifyLight(serviceMap);
			
			serviceMap.put("oid", row[3]);
			serviceMap.put("value1", row[4]);
			serviceMap.put("value2", row[5]);
			SgHelper.service.modifyLight(serviceMap);
			
			serviceMap.put("oid", row[6]);
			serviceMap.put("value1", row[7]);
			serviceMap.put("value2", row[8]);
			SgHelper.service.modifyLight(serviceMap);
			
			serviceMap.put("oid", row[9]);
			serviceMap.put("value1", row[10]);
			serviceMap.put("value2", row[11]);
			SgHelper.service.modifyLight(serviceMap);
			
			serviceMap.put("oid", row[12]);
			serviceMap.put("value1", row[13]);
			serviceMap.put("value2", row[14]);
			SgHelper.service.modifyLight(serviceMap);
			
			serviceMap.put("oid", row[15]);
			serviceMap.put("value1", row[16]);
			serviceMap.put("value2", row[17]);
			SgHelper.service.modifyLight(serviceMap);
			
//			map.put("msg", "저장되었습니다.");
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/getChartData")
	public Map<String, Object> getChartData(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			SGObjectMaster objMaster = (SGObjectMaster) CommonUtil.getObject(oid);
			
			List<SGChartData> list = SgHelper.manager.getChartData(objMaster);
			
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
	@RequestMapping("/revisionGate")
	public Map<String, Object> revisionGate(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		SGObjectMaster objMaster = null;
		StageGate sg = null;
		EProject project = null;
		
		try {
			String masterOid = StringUtil.checkNull((String) reqMap.get("oid"));
			String remark = StringUtil.checkNull((String) reqMap.get("remark"));
			SGObjectMaster master = (SGObjectMaster) CommonUtil.getObject(masterOid);
			
			boolean isOverlap = SgHelper.manager.isOverLap(master, remark);
			if(!isOverlap) {
				objMaster = SgHelper.service.revisionGate(reqMap);
				if(objMaster != null) {
					sg = objMaster.getStageGate();
					project = sg.getProject();
					//String oid = CommonUtil.getOIDString(project);
					//map.put("redirectUrl", CommonUtil.getURLString("/gate/viewStageGate") + "?oid=" + oid);
					//map.put("msg", "이력이 저장되었습니다.");
					map.put("redirectUrl", "Reload");
					map.put("gen", true);
					
				}
			}
			
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/viewRecord")
	public ModelAndView viewRecord(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			SGObjectMaster objMaster = (SGObjectMaster) CommonUtil.getObject(oid);
			
			StageGateData data = new StageGateData(objMaster);
			EProject project = data.getProject();
			ProjectData pData = new ProjectData(project);
			List<SGObjectValueData> list = SgHelper.manager.getValueList(objMaster, "LIGHT");
			
			String viewOnly = "true";
			
			model.addObject("pData", pData);
			model.addObject("data", data);
			model.addObject("lightList", list);
			model.addObject("viewOnly", viewOnly);
			model.setViewName("empty:/stagegate/viewStageGate");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	@RequestMapping("/viewObject")
	public ModelAndView viewObject(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			SGObjectValue objValue = (SGObjectValue) CommonUtil.getObject(oid);
			
			SGObjectValueData data = new SGObjectValueData(objValue);
			model.addObject("data", data);
			model.setViewName("popup:/stagegate/viewObject");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	@RequestMapping("/exportExcelRisk")
	public void exportExcelRisk(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			SgHelper.service.exportExcelRisk(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping("/exportExcelCStop")
	public void exportExcelCStop(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			SgHelper.service.exportExcelCStop(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping("/importExcelCStop")
	public ModelAndView importExcelCStop(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String cOid = StringUtil.checkNull((String) reqMap.get("cOid")); 
			String cObjType = StringUtil.checkNull((String) reqMap.get("cObjType")); 
			model.addObject("cOid", cOid);
			model.addObject("cObjType", cObjType);
			model.setViewName("empty:/stagegate/importExcelCStop");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/importExcelCStopAction")
	public Map<String, Object> importExcelCStopAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			SgHelper.service.importExcelCStop(reqMap);
			
			map.put("result", true);
			map.put("msg", "등록이 완료되었습니다.");
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/filePopup")
	public ModelAndView filePopup(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			model.addObject("oid", oid);
			model.setViewName("empty:/stagegate/filePopup");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/fileUpload")
	public Map<String, Object> fileUpload(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			SgHelper.service.fileUpload(reqMap);
			
			map.put("result", true);
			map.put("msg", "등록이 완료되었습니다.");
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
}
