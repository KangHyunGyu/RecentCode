package com.e3ps.epm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.e3ps.admin.service.AdminHelper;
import com.e3ps.approval.util.ApprovalUtil;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.service.CommonHelper;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.epm.bean.EpmData;
import com.e3ps.epm.bean.EpmPartStateData;
import com.e3ps.epm.bean.StructureData;
import com.e3ps.epm.service.EpmHelper;
import com.e3ps.org.bean.PeopleData;
import com.e3ps.part.comparator.bean.CADBomData;
import com.ptc.wvs.server.ui.UIHelper;
import com.ptc.wvs.server.util.FileHelper;
import com.ptc.wvs.server.util.PublishUtils;

import wt.content.ContentRoleType;
import wt.enterprise.Master;
import wt.enterprise.RevisionControlled;
import wt.epm.EPMDocument;
import wt.fc.Persistable;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.session.SessionHelper;

@Controller
@RequestMapping("/epm")
public class EpmController {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.EPM.getName());
	
	/**
	 * @desc	: 도면 검색
	 * @author	: sangylee
	 * @date	: 2019. 6. 21.
	 * @method	: searchEpm
	 * @param	: reqMap
	 * @return	: ModelAndView
	 */
	@RequestMapping("/searchEpm")
	public ModelAndView searchEpm(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		model.setViewName("default:/epm/searchEpm");
		
		return model;
	}
	
	/**
	 * @desc	: 도면 검색 Action
	 * @author	: sangylee
	 * @date	: 2019. 6. 21.
	 * @method	: searchEpmAction
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/searchEpmAction")
	public Map<String, Object> searchEpmAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			List<EpmData> list =  EpmHelper.manager.getEpmList(reqMap);
			
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
	 * @desc	: 도면 검색 스크롤 Action
	 * @author	: sangylee
	 * @date	: 2019. 8. 14.
	 * @method	: searchEpmScrollAction
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/searchEpmScrollAction")
	public Map<String, Object> searchEpmScrollAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			map =  EpmHelper.manager.getEpmScrollList(reqMap);
			
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 도면 등록
	 * @author	: sangylee
	 * @date	: 2019. 7. 24.
	 * @method	: createEpm
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/createEpm")
	public ModelAndView createEpm(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		model.setViewName("default:/epm/createEpm");
		
		return model;
	}
	
	/**
	 * @desc	: 도면 등록 Action
	 * @author	: sangylee
	 * @date	: 2019. 7. 24.
	 * @method	: createEpmAction
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/createEpmAction")
	public Map<String, Object> createEpmAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			EpmHelper.service.createEpmAction(reqMap);
			
			map.put("msg", "등록되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/epm/searchEpm"));
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc : 도면 일괄 등록
	 * @author : sangylee
	 * @date : 2019. 9. 17.
	 * @method : createMultiEpm
	 * @return : ModelAndView
	 * @param reqMap
	 * @return
	 */
	@RequestMapping("/createMultiEpm")
	public ModelAndView createMultiEpm(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		model.setViewName("default:/epm/createMultiEpm");
		
		return model;
	}

	/**
	 * @desc : 일괄 등록 Action
	 * @author : sangylee
	 * @date : 2019. 9. 17.
	 * @method : createMultiEpmAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/createMultiEpmAction")
	public Map<String, Object> createMultiEpmAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			Map<String, Object> returnMap = EpmHelper.service.createMultiEpmAction(reqMap);
			
			map.put("msg", returnMap.get("msg"));
			map.put("resultList", returnMap.get("list"));
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
		
	/**
	 * @desc	: 도면 상세보기
	 * @author	: sangylee
	 * @date	: 2019. 6. 21.
	 * @method	: viewEpm
	 * @param	: reqMap
	 * @return	: ModelAndView
	 */
	@RequestMapping("/viewEpm")
	public ModelAndView viewEpm(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			
			EPMDocument epm = (EPMDocument) CommonUtil.getObject(oid);
			
			EpmData epmData = new EpmData(epm);
			WTPart part = epmData.ownerPart();
			
			boolean isOwnerPart = false;
			if(part != null) {
				isOwnerPart = true;
			}
			boolean isAdmin = CommonUtil.isAdmin();
			
			//boolean isAuth = AdminHelper.manager.isAuth(epm);
			
			model.addObject("epm", epmData);
			model.addObject("isOwnerPart", isOwnerPart);
			model.addObject("isAdmin", isAdmin);
			model.setViewName("popup:/epm/viewEpm");
			
			//if(!isAuth) {
			//	model.setViewName("popup:/admin/noPermission");
			//}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	/**
	 * @desc	: 도면 상세 정보
	 * @author	: sangylee
	 * @date	: 2019. 8. 6.
	 * @method	: detailEpm
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_detailEpm")
	public ModelAndView detailEpm(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			
			EPMDocument epm = (EPMDocument) CommonUtil.getObject(oid);
			
			EpmData epmData = new EpmData(epm);
//			epmData.userOnGoingApproval();
			model.addObject("epm", epmData);
			
			model.setViewName("include:/epm/include/detailEpm");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	/**
	 * @desc	: 관련 객체
	 * @author	: sangylee
	 * @date	: 2019. 8. 6.
	 * @method	: relatedObject
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_relatedObject")
	public ModelAndView relatedObject(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		model.addObject("oid", oid);
		
		EPMDocument epm = (EPMDocument) CommonUtil.getObject(oid);
		
		boolean cadAssembly = false;
		if(epm.getDocType().toString().equals("CADASSEMBLY") && epm.getAuthoringApplication().toString().equals("PROE")) {
			cadAssembly = true;
		}
		
		boolean cadDrawing = false;
		if(epm.getDocType().toString().equals("CADDRAWING") && epm.getAuthoringApplication().toString().equals("PROE")) {
			cadDrawing = true;
		}
		model.addObject("cadAssembly", cadAssembly);
		model.addObject("cadDrawing", cadDrawing);
		model.setViewName("include:/epm/include/relatedObject");
		
		return model;
	}
	
	@RequestMapping("/modifyEpm")
	public ModelAndView modifyEpm(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			
			EPMDocument epm = (EPMDocument) CommonUtil.getObject(oid);
			
			EpmData epmData = new EpmData(epm);
			epmData.container();
			
			model.addObject("epm", epmData);
			
			model.setViewName("popup:/epm/modifyEpm");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	/**
	 * @desc	: 도면 수정 Action
	 * @author	: sangylee
	 * @date	: 2019. 6. 21.
	 * @method	: modifyEpmAction
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/modifyEpmAction")
	public Map<String, Object> modifyEpmAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			EPMDocument epm = EpmHelper.service.modifyEpmAction(reqMap);
			
			String oid = CommonUtil.getOIDString(epm);
			
			map.put("msg", "수정되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/epm/viewEpm") + "?oid=" + oid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc : 도면 개정(연결된 부품이 없는 경우만 가능)
	 * @author : sangylee
	 * @date : 2019. 10. 29.
	 * @method : reviseEpmAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/reviseEpmAction")
	public Map<String, Object> reviseEpmAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			EPMDocument epm = EpmHelper.service.reviseEpmAction(reqMap);
			
			String oid = CommonUtil.getOIDString(epm);
			
			map.put("msg", "개정되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/epm/viewEpm") + "?oid=" + oid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc : 도면 폐기(연결된 부품이 없는 경우만 가능)
	 * @author : sangylee
	 * @date : 2019. 10. 29.
	 * @method : withdrawEpmAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/withdrawEpmAction")
	public Map<String, Object> withdrawEpmAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			EPMDocument epm = EpmHelper.service.withdrawEpmAction(reqMap);
			
			String oid = CommonUtil.getOIDString(epm);
			
			map.put("msg", "폐기되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/epm/viewEpm") + "?oid=" + oid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 도면 삭제 Action
	 * @author	: sangylee
	 * @date	: 2019. 6. 21.
	 * @method	: deleteEpmAction
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/deleteEpmAction")
	public Map<String, Object> deleteEpmAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			EpmHelper.service.deleteEpmAction(reqMap);
			
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
	 * @desc	: 주도면 include
	 * @author	: sangylee
	 * @date	: 2019. 7. 25.
	 * @method	: mainEpm
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_mainEpm")
	public ModelAndView mainEpm(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		model.addObject("oid", oid);
		
		model.setViewName("include:/epm/include/mainEpm");
		
		return model;
	}
	
	/**
	 * @desc	: 주도면 가져오기
	 * @author	: sangylee
	 * @date	: 2019. 7. 25.
	 * @method	: getMainEpm
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getMainEpm")
	public static Map<String, Object> getMainEpm(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<>();
		
		try {
			List<EpmData> list = EpmHelper.manager.getMainEpm(reqMap);
			
			map.put("list", list);
			map.put("result",true);
		} catch (Exception e) {
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc : 구조 include
	 * @author : sangylee
	 * @date : 2020. 1. 17.
	 * @method : structure
	 * @return : ModelAndView
	 * @param reqMap
	 */
	@RequestMapping("/include_structure")
	public ModelAndView structure(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		model.addObject("oid", oid);
		
		model.setViewName("include:/epm/include/structure");
		
		return model;
	}
	
	/**
	 * @desc : 구조 가져오기
	 * @author : sangylee
	 * @date : 2020. 1. 17.
	 * @method : getStructure
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/getStructure")
	public static Map<String, Object> getStructure(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<>();
		
		try {
			List<StructureData> list = EpmHelper.manager.getStructure(reqMap);
			
			map.put("list", list);
			map.put("result",true);
		} catch (Exception e) {
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 참조 include
	 * @author	: sangylee
	 * @date	: 2019. 7. 25.
	 * @method	: reference
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_reference")
	public ModelAndView reference(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		model.addObject("oid", oid);
		
		model.setViewName("include:/epm/include/reference");
		
		return model;
	}
	
	/**
	 * @desc	: 참조 가져오기
	 * @author	: sangylee
	 * @date	: 2019. 7. 25.
	 * @method	: getReference
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getReference")
	public static Map<String, Object> getReference(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<>();
		
		try {
			List<EpmData> list = EpmHelper.manager.getReference(reqMap);
			
			map.put("list", list);
			map.put("result",true);
		} catch (Exception e) {
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 참조 항목 include
	 * @author	: sangylee
	 * @date	: 2019. 7. 25.
	 * @method	: referenceBy
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_referenceBy")
	public ModelAndView referenceBy(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		model.addObject("oid", oid);
		
		model.setViewName("include:/epm/include/referenceBy");
		
		return model;
	}
	
	/**
	 * @desc	: 참조 항목 가져오기
	 * @author	: sangylee
	 * @date	: 2019. 7. 25.
	 * @method	: getReferenceBy
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getReferenceBy")
	public static Map<String, Object> getReferenceBy(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<>();
		
		try {
			List<EpmData> list = EpmHelper.manager.getReferenceBy(reqMap);
			
			map.put("list", list);
			map.put("result",true);
		} catch (Exception e) {
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 관련 도면 include
	 * @author	: sangylee
	 * @date	: 2019. 7. 25.
	 * @method	: relatedEpm
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_relatedEpm")
	public ModelAndView relatedEpm(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		model.addObject("oid", oid);
		
		model.setViewName("include:/epm/include/relatedEpm");
		
		return model;
	}
	
	/**
	 * @desc	: 관련 도면 가져오기
	 * @author	: sangylee
	 * @date	: 2019. 7. 25.
	 * @method	: getRelatedEpm
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getRelatedEpm")
	public static Map<String, Object> getRelatedEpm(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<>();
		
		try {
			List<EpmData> list = EpmHelper.manager.getRelatedEpm(reqMap);
			
			map.put("list", list);
			map.put("result",true);
		} catch (Exception e) {
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	//변경결과
	@ResponseBody
	@RequestMapping("/getResultEpm")
	public static Map<String, Object> getResultEpm(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<>();
		
		try {
			List<EpmData> list = EpmHelper.manager.getResultEpm(reqMap);
			
			map.put("list", list);
			map.put("result",true);
		} catch (Exception e) {
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 썸네일 가져오기
	 * @author	: sangylee
	 * @date	: 2019. 7. 25.
	 * @method	: getThumbnail
	 * @param reqMap
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/getThumbnail")
	public ModelAndView getThumbnail(@RequestParam Map<String, String> reqMap) throws Exception {

		ModelAndView model = new ModelAndView();

		String epmOid = StringUtil.checkNull(reqMap.get("epmOid"));
		
		EPMDocument epm = (EPMDocument) CommonUtil.getObject(epmOid);
		
		String thumbnailUrl = null;
		if(epm != null) {
			thumbnailUrl = FileHelper.getViewContentURLForType(PublishUtils.findRepresentable((Persistable)epm), ContentRoleType.THUMBNAIL);
		}
		
		if(thumbnailUrl == null) {
	    	String[] sss = UIHelper.getDefaultVisualizationData(CommonUtil.getOIDString(epm), false, Locale.KOREA);
	    	
	    	if (sss[17].length() == 0) {
	    		thumbnailUrl = "/Windchill/wt/clients/images/wvs/productview_publish_288.png";
	    	} else {
	    		thumbnailUrl = "/Windchill/wt/clients/images/wvs/productview_openin_288.png";
	    	}
	    }

		RedirectView rv = new RedirectView(thumbnailUrl);
		model.setView(rv);

		return model;
	}
	
	/**
	 * @desc : 도면 일괄등록 리스트
	 * @author : sangylee
	 * @date : 2019. 9. 17.
	 * @method : multiEpmList
	 * @return : ModelAndView
	 * @param reqMap
	 * @return
	 */
	@RequestMapping("/include_multiEpmList")
	public ModelAndView multiEpmList(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		model.setViewName("include:/epm/include/multiEpmList");
		
		return model;
	}
	
	@RequestMapping("/include_epmAttributes")
	public ModelAndView epmAttributes(@RequestParam Map<String, Object> reqMap) throws Exception {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String module = StringUtil.checkNull((String) reqMap.get("module"));
		
		Map<String, Object> map = CommonHelper.manager.getAttributes(oid);
		
		model.addObject("oid", oid);
		model.addObject("module", module);
		model.addObject("attributes", map);
		
		model.setViewName("include:/epm/include/epmAttributes");
		
		return model;
	}
	
	/**
	 * @desc : CADBOM 생성 팝업
	 * @author : sangylee
	 * @date : 2020. 1. 2.
	 * @method : createCADBOMPopup
	 * @return : ModelAndView
	 * @param reqMap
	 * @throws Exception
	 */
	@RequestMapping("/createCADBOMPopup")
	public ModelAndView createCADBOMPopup(@RequestParam Map<String, Object> reqMap) throws Exception {
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			
			EPMDocument epm = (EPMDocument) CommonUtil.getObject(oid);
			
			EpmData epmData = new EpmData(epm);
			
			model.addObject("epm", epmData);
			model.setViewName("popup:/epm/createCADBOMPopup");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	/**
	 * @desc : CAD BOM 검증 Action
	 * @author : sangylee
	 * @date : 2020. 1. 2.
	 * @method : checkCADBOMAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("checkCADBOMAction")
	public Map<String, Object> checkCADBOMAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			List<CADBomData> list = EpmHelper.manager.checkCADBOMList(reqMap);
			
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
	 * @desc : CAD BOM 생성 Action
	 * @author : sangylee
	 * @date : 2020. 1. 2.
	 * @method : createCADBOMAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/createCADBOMAction")
	public Map<String, Object> createCADBOMAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			EpmHelper.service.createCADBOMAction(reqMap);
			
			map.put("result", true);
			map.put("msg", MessageUtil.getMessage("CAD BOM 생성을 완료하였습니다."));
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc : 일괄 결재 등록
	 * @author : sangylee
	 * @date : 2020. 1. 7.
	 * @method : createMultiApproval
	 * @return : ModelAndView
	 * @param reqMap
	 */
	@RequestMapping("/createMultiApproval")
	public ModelAndView createMultiApproval(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		/*일괄 결재 타입에 따른 include pamrm 결정*/
		//String objType = StringUtil.checkReplaceStr((String) reqMap.get("objType"), "doc"); //part,epm
		String objType = "epm"; //doc,part,epm
		Map<String,String> map = ApprovalUtil.getMultiTypeInfo(objType);
		String pageName = map.get("pageName");//multiAppDoc,multiAppPart,multiAppEpm
		String title = map.get("title"); //폼목,도면
		
		model.addObject("objType", objType);
		model.addObject("pageName", pageName);
		model.addObject("title", title);
		model.setViewName("default:/" + objType + "/createMultiApproval");
		
		return model;
	}
	
	/**
	 * @desc	: 부품 번호 변경
	 * @author	: tsuam
	 * @date	: 2020. 1. 10.
	 * @method	: chagePartNo
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/chagePartNo")
	public Map<String, Object> chagePartNo(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));

			EpmHelper.service.changePartNO(oid);
			
			map.put("result", true);
			map.put("msg", MessageUtil.getMessage("변경이 완료되었습니다."));
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/compareCADBOM")
	public ModelAndView compareCADBOM(@RequestParam Map<String, Object> reqMap) throws Exception {
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			
			EPMDocument epm = (EPMDocument) CommonUtil.getObject(oid);
			
			EpmData epmData = new EpmData(epm);
			
			model.addObject("epm", epmData);
			model.setViewName("popup:/epm/compareCADBOM");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	/**
	 * @desc : 도면 부품 상태,버전 확인 화면
	 * @author : sangylee
	 * @date : 2020. 2. 7.
	 * @method : searchEpmPartState
	 * @return : ModelAndView
	 * @param reqMap
	 */
	@RequestMapping("/searchEpmPartState")
	public ModelAndView searchEpmPartState(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		model.setViewName("default:/epm/searchEpmPartState");
		
		return model;
	}
	
	/**
	 * @desc : 도면 부품 상태,버전 확인 화면 검색 Action
	 * @author : sangylee
	 * @date : 2020. 2. 7.
	 * @method : searchEpmPartStateAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/searchEpmPartStateAction")
	public Map<String, Object> searchEpmPartStateAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			List<EpmPartStateData> list =  EpmHelper.manager.getEpmPartStateList(reqMap);
			
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
	 * @desc : 도면 부품 상태,버전 확인 화면 검색 스크롤 Action
	 * @author : sangylee
	 * @date : 2020. 2. 7.
	 * @method : searchEpmPartStateScrollAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/searchEpmPartStateScrollAction")
	public Map<String, Object> searchEpmPartStateScrollAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			map =  EpmHelper.manager.getEpmPartStateScrollList(reqMap);
			
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/rePublishAction")
	public Map<String, Object> rePublishAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			EPMDocument epm = (EPMDocument)CommonUtil.getObject(oid);
			EpmHelper.service.publish(epm);
			
			map.put("msg", "재변환 진행중입니다..");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/epm/viewEpm") + "?oid=" + oid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc : Folder 등록
	 * @author : hgkang
	 * @date : 2023. 1. 10.
	 * @method : createEpmFolder
	 * @return : ModelAndView
	 * @param reqMap
	 */
	@RequestMapping("/createEpmFolder")
	public ModelAndView createEpmFolder(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		model.setViewName("default:/epm/createEpmFolder");
		return model;
		
	}
	
	/**
	 * @desc	: 폴더 생성 Action
	 * @author	: hgkang
	 * @date	: 2023. 1. 10.
	 * @method	: createEpmFolderAction
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/createEpmFolderAction")
	public Map<String, Object> createEpmFolderAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			
			EpmHelper.service.createEpmFolderAction(reqMap);
			
			map.put("msg", "생성되었습니다.");
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
}
