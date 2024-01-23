package com.e3ps.part.controller;

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

import com.e3ps.approval.util.ApprovalUtil;
import com.e3ps.change.EChangeOrder2;
import com.e3ps.change.EChangeRequest2;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.service.CommonHelper;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.erp.util.ERPInterface;
import com.e3ps.part.bean.PartData;
import com.e3ps.part.service.PartHelper;
import com.e3ps.part.util.PartUtil;

import wt.fc.Persistable;
import wt.part.WTPart;

@Controller
@RequestMapping("/part")
public class PartContorller {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PART.getName());
	
	/**
	 * @desc	: 부품 검색
	 * @author	: sangylee
	 * @date	: 2019. 6. 21.
	 * @method	: searchPart
	 * @param	: reqMap
	 * @return	: ModelAndView
	 */
	@RequestMapping("/searchPart")
	public ModelAndView searchPart(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		model.setViewName("default:/part/searchPart");
		
		return model;
	}
	
	/**
	 * @desc	: 부품 검색 Action
	 * @author	: sangylee
	 * @date	: 2019. 6. 21.
	 * @method	: searchPartAction
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/searchPartAction")
	public Map<String, Object> searchPartAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			List<PartData> list = PartHelper.manager.getPartList(reqMap);
			
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
	 * @desc	: 부품 검색 스크롤 Action
	 * @author	: sangylee
	 * @date	: 2019. 8. 14.
	 * @method	: searchPartScrollAction
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/searchPartScrollAction")
	public Map<String, Object> searchPartScrollAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			map = PartHelper.manager.getPartScrollList(reqMap);
			
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 부품 등록
	 * @author	: sangylee
	 * @date	: 2019. 7. 24.
	 * @method	: createPart
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/createPart")
	public ModelAndView createPart(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		model.setViewName("default:/part/createPart");
		
		return model;
	}
	
	/**
	 * @desc : repair 부품 등록
	 * @author : sangylee
	 * @date : 2019. 9. 10.
	 * @method : createRepairPart
	 * @return : ModelAndView
	 * @param reqMap
	 */
	@RequestMapping("/createRepairPart")
	public ModelAndView createRepairPart(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		model.setViewName("default:/part/createRepairPart");
		
		return model;
	}
	
	/**
	 * @desc	: 부품 등록 Action
	 * @author	: sangylee
	 * @date	: 2019. 7. 24.
	 * @method	: createPartAction
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/createPartAction")
	public Map<String, Object> createPartAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String number = StringUtil.checkNull((String) reqMap.get("number"));
			WTPart part = PartHelper.manager.getPart(number);
			if(part != null) {
				map.put("msg", "이미 존재하는 부품입니다.\n채번 버튼을 클릭하여 주십시오.");
			} else {
				PartHelper.service.createPartAction(reqMap);
				map.put("msg", "등록되었습니다.");
			}
			
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/part/searchPart"));
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 부품 상세보기
	 * @author	: sangylee
	 * @date	: 2019. 6. 21.
	 * @method	: viewPart
	 * @param	: reqMap
	 * @return	: ModelAndView
	 */
	@RequestMapping("/viewPart")
	public ModelAndView viewPart(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			
			WTPart part = (WTPart) CommonUtil.getObject(oid);
			
			PartData partData = new PartData(part);
			
			boolean isAdmin = CommonUtil.isAdmin();
			
			//boolean isAuth = AdminHelper.manager.isAuth(part);
			
			model.addObject("part", partData);
			model.addObject("isAdmin", isAdmin);
			model.setViewName("popup:/part/viewPart");
			
			//if(!isAuth) {
			//	model.setViewName("popup:/admin/noPermission");
			//}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	/**
	 * @desc	: 부품 상세 정보
	 * @author	: sangylee
	 * @date	: 2019. 8. 6.
	 * @method	: detailPart
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_detailPart")
	public ModelAndView detailPart(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			
			WTPart part = (WTPart) CommonUtil.getObject(oid);
			
			PartData partData = new PartData(part);
//			partData.userOnGoingApproval();
			partData.bomEndItemHash();
			
			model.addObject("part", partData);
			
			model.setViewName("include:/part/include/detailPart");
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
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			WTPart part = (WTPart) CommonUtil.getObject(oid);
			
			PartData partData = new PartData(part);
			
			String epmOid = partData.epmOid();
			
			model.addObject("oid", oid);
			model.addObject("epmOid", epmOid);
			
			model.setViewName("include:/part/include/relatedObject");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	
	/**
	 * @desc	: 부품 수정
	 * @author	: sangylee
	 * @date	: 2019. 8. 9.
	 * @method	: relatedObject
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/modifyPart")
	public ModelAndView modifyPart(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			
			WTPart part = (WTPart) CommonUtil.getObject(oid);
			
			PartData partData = new PartData(part);
			partData.loadAttributes();
			
			model.addObject("part", partData);
			
			model.setViewName("popup:/part/modifyPart");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	/**
	 * @desc	: 부품 수정 Action
	 * @author	: sangylee
	 * @date	: 2019. 6. 21.
	 * @method	: modifyPartAction
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/modifyPartAction")
	public Map<String, Object> modifyPartAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			WTPart Part = PartHelper.service.modifyPartAction(reqMap);
			
			String oid = CommonUtil.getOIDString(Part);
			
			map.put("msg", "수정되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/part/viewPart") + "?oid=" + oid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc : 부품 개정
	 * @author : sangylee
	 * @date : 2020. 1. 15.
	 * @method : revisePart
	 * @return : ModelAndView
	 * @param reqMap
	 */
	@RequestMapping("/revisePart")
	public ModelAndView revisePart(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			
			WTPart part = (WTPart) CommonUtil.getObject(oid);
			
			PartData partData = new PartData(part);
			partData.loadAttributes();
			
			model.addObject("part", partData);
			
			model.setViewName("popup:/part/revisePart");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	/**
	 * @desc : 부품 개정 Action
	 * @author : sangylee
	 * @date : 2019. 10. 29.
	 * @method : updatePartAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/revisePartAction")
	public Map<String, Object> revisePartAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			WTPart Part = PartHelper.service.revisePartAction(reqMap);
			
			String oid = CommonUtil.getOIDString(Part);
			
			map.put("msg", "개정되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/part/viewPart") + "?oid=" + oid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc : 부품 속성 수정
	 * @author : sangylee
	 * @date : 2020. 1. 15.
	 * @method : modifyPartAttribute
	 * @return : ModelAndView
	 * @param reqMap
	 */
	@RequestMapping("/modifyPartAttribute")
	public ModelAndView modifyPartAttribute(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			
			WTPart part = (WTPart) CommonUtil.getObject(oid);
			
			PartData partData = new PartData(part);
			partData.loadAttributes();
			
			model.addObject("part", partData);
			
			model.setViewName("popup:/part/modifyPartAttribute");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	/**
	 * @desc : 부품 속성 수정 Action
	 * @author : sangylee
	 * @date : 2020. 1. 15.
	 * @method : modifyPartAttributeAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/modifyPartAttributeAction")
	public Map<String, Object> modifyPartAttributeAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			WTPart Part = PartHelper.service.modifyPartAttributeAction(reqMap);
			
			String oid = CommonUtil.getOIDString(Part);
			
			map.put("msg", "수정되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/part/viewPart") + "?oid=" + oid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc : 부품 폐기 Action
	 * @author : sangylee
	 * @date : 2019. 10. 29.
	 * @method : withdrawnPartAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/withdrawPartAction")
	public Map<String, Object> withdrawPartAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			WTPart Part = PartHelper.service.withdrawPartAction(reqMap);
			
			String oid = CommonUtil.getOIDString(Part);
			
			map.put("msg", "폐기되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/part/viewPart") + "?oid=" + oid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 부품 삭제 Action
	 * @author	: sangylee
	 * @date	: 2019. 6. 21.
	 * @method	: deletePartAction
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/deletePartAction")
	public Map<String, Object> deletePartAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			PartHelper.service.deletePartAction(reqMap);
			
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
	 * @desc	: 관련 부품 include
	 * @author	: sangylee
	 * @date	: 2019. 8. 14.
	 * @method	: relatedPart
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_relatedPart")
	public ModelAndView relatedPart(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String rowCheck = StringUtil.checkReplaceStr((String) reqMap.get("rowCheck"), "false");
		
		Persistable per = CommonUtil.getObject(oid);
		model.addObject("isEO", "");
		if(per instanceof EChangeOrder2) {
			model.addObject("isEO", "isEO");
		}else if(per instanceof EChangeRequest2) {
			model.addObject("isEO", "isEO");
		}
		model.addObject("oid", oid);
		model.addObject("rowCheck", rowCheck);
		
		model.setViewName("include:/part/include/relatedPart");
		
		return model;
	}
	
	/**
	 * @desc	: 관련 부품 가져오기
	 * @author	: sangylee
	 * @date	: 2019. 8. 14.
	 * @method	: getRelatedPart
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getRelatedPart")
	public static Map<String, Object> getRelatedPart(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<>();
		
		try {
			List<PartData> list = PartHelper.manager.getRelatedPart(reqMap);
			
			map.put("list", list);
			map.put("result",true);
		} catch (Exception e) {
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc : 부품 단위 리스트
	 * @author : sangylee
	 * @date : 2019. 9. 4.
	 * @method : getUnitList
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/getUnitList")
	public Map<String, Object> getUnitList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			List<Map<String, String>> list = PartUtil.getUnitList();
			
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
	 * @desc : 부품 재질 리스트
	 * @author : sangylee
	 * @date : 2019. 9. 4.
	 * @method : getMaterialList
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/getMaterialList")
	public Map<String, Object> getMaterialList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			List<Map<String, String>> list = PartUtil.getMaterialList();
			
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
	 * @desc : 부품 Material Type 리스트
	 * @author : sangylee
	 * @date : 2019. 9. 4.
	 * @method : getMaterialTypeList
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/getMaterialTypeList")
	public Map<String, Object> getMaterialTypeList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			List<Map<String, String>> list = PartUtil.getMaterialTypeList();
			
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
	 * @desc : 부품 등록 화면 부품 검색
	 * @author : sangylee
	 * @date : 2019. 9. 6.
	 * @method : searchPartList
	 * @return : ModelAndView
	 * @param reqMap
	 */
	@RequestMapping("/include_searchPartList")
	public ModelAndView searchPartList(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String autoGridHeight = StringUtil.checkReplaceStr((String) reqMap.get("autoGridHeight"), "false");
		String gridHeight = StringUtil.checkReplaceStr((String) reqMap.get("gridHeight"), "200");
		
		model.addObject("autoGridHeight", autoGridHeight);
		model.addObject("gridHeight", gridHeight);
		
		model.setViewName("include:/part/include/searchPartList");
		
		return model;
	}
	
	/**
	 * @desc : Repair 부품 등록 화면 부품 검색
	 * @author : sangylee
	 * @date : 2019. 9. 10.
	 * @method : searchPartListForRepair
	 * @return : ModelAndView
	 * @param reqMap
	 */
	@RequestMapping("/include_searchPartListForRepair")
	public ModelAndView searchPartListForRepair(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String autoGridHeight = StringUtil.checkReplaceStr((String) reqMap.get("autoGridHeight"), "false");
		String gridHeight = StringUtil.checkReplaceStr((String) reqMap.get("gridHeight"), "200");
		
		model.addObject("autoGridHeight", autoGridHeight);
		model.addObject("gridHeight", gridHeight);
		
		model.setViewName("include:/part/include/searchPartListForRepair");
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/getPartInfo")
	public Map<String, Object> getPartInfo(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		try {
			
			WTPart part = (WTPart) CommonUtil.getObject(oid);
			
			PartData pData = new PartData(part);
			pData.loadAttributes();
			
			map.put("part", pData);
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/include_partAttributes")
	public ModelAndView partAttributes(@RequestParam Map<String, Object> reqMap) throws Exception {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String module = StringUtil.checkNull((String) reqMap.get("module"));
		
		Map<String, Object> map = CommonHelper.manager.getAttributesDisplay(oid);
		
		model.addObject("oid", oid);
		model.addObject("module", module);
		model.addObject("attributes", map);
		
		model.setViewName("include:/part/include/partAttributes");
		
		return model;
	}
	
	@RequestMapping("/include_usedPart")
	public ModelAndView usedPart(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		model.addObject("oid", oid);
		model.setViewName("include:/part/include/usedPart");
		
		return model;
	}
	
	
	/**
	 * @desc : BOM 로드 화면
	 * @author : sangylee
	 * @date : 2019. 12. 10.
	 * @method : loadBOM
	 * @return : ModelAndView
	 * @param reqMap
	 */
	@RequestMapping("/loadBom")
	public ModelAndView loadBom(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		model.setViewName("default:/part/loadBom");
		
		return model;
	}
	
	/**
	 * @desc : BOM 로드 리스트
	 * @author : sangylee
	 * @date : 2019. 12. 11.
	 * @method : loadBOM
	 * @return : ModelAndView
	 * @param reqMap
	 */
	@RequestMapping("/include_loadBomList")
	public ModelAndView loadBomList(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		String autoGridHeight = StringUtil.checkReplaceStr((String) reqMap.get("autoGridHeight"), "false");
		String gridHeight = StringUtil.checkReplaceStr((String) reqMap.get("gridHeight"), "200");
		
		model.addObject("autoGridHeight", autoGridHeight);
		model.addObject("gridHeight", gridHeight);
		
		model.setViewName("include:/part/include/loadBomList");
		
		return model;
	}
	
	/**
	 * @desc : 부품 채번에서 부품 검색
	 * @author : sangylee
	 * @date : 2020. 1. 3.
	 * @method : searchPartListAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/searchPartListAction")
	public Map<String, Object> searchPartListAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			List<PartData> list = PartHelper.manager.getCreateSearchPartList(reqMap);
			
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
		String objType = "part"; //doc,part,epm
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
	 * @desc : ERP 전송
	 * @author : sangylee
	 * @date : 2020. 2. 4.
	 * @method : sendERPPartInfo
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/sendERPPartInfo")
	public Map<String, Object> sendERPPartInfo(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			WTPart part = (WTPart) CommonUtil.getObject(oid);
			
			map.put("msg", "전송되었습니다.");
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/createPartAction2")
	public Map<String, Object> createPartAction2(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String number = StringUtil.checkNull((String) reqMap.get("number"));
			WTPart part = PartHelper.manager.getPart(number);
			if(part != null) {
				map.put("msg", "이미 존재하는 부품입니다.");
			} else {
				PartHelper.service.createPartAction(reqMap);

				map.put("msg", "등록되었습니다.");
			}
			
			map.put("result", true);
			//map.put("redirectUrl", CommonUtil.getURLString("/part/searchPart"));
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
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
		
		/*if (logger.isDebugEnabled()) {
			logger.debug("ProjectController.sendErpAction");
		}*/
		try {
			
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			WTPart part = (WTPart)CommonUtil.getObject(oid);
			
			if("APPROVED".equals(part.getState().toString())) {
				//ERPInterface.send(part);
				
				map.put("result", true);
				map.put("msg", "전송하였습니다.");
			}else{
				map.put("result", false);
				map.put("msg", "승인되지 않은 부품은 ERP 전송할 수 없습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * 
	  * @desc :  품목 멀티 등록
	  * @author : shjeong
	  * @date : 2023. 07. 27.
	  * @method : createPartMulti
	  * @param reqMap
	  * @return ModelAndView
	 */
	@RequestMapping("/createPartMulti")
	public ModelAndView createPartMulti(@RequestParam Map<String, Object> reqMap) {

		ModelAndView model = new ModelAndView();

		model.setViewName("default:/part/createPartMulti");

		return model;
	}
	
	@ResponseBody
	@RequestMapping("/createPartMultiAction")
	public Map<String, Object> createPartMultiAction(@RequestBody Map<String, Object> reqMap, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
	
		List<Map<String,Object>> itemList = (List<Map<String,Object>>)reqMap.get("itemList");
		try {
			itemList = PartHelper.service.createPartMultiAction(reqMap);
			map.put("itemList",itemList);
			map.put("result", true);
			map.put("msg", "등록이 완료되었습니다.");
			map.put("redirectUrl", CommonUtil.getURLString("/part/searchPart"));

		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		} finally {
			System.out.println("createPartMultiAction END333");
		}
		
		return map;
	}
	
	@RequestMapping("/addRevisionPopup")
	public ModelAndView editBom(@RequestParam Map<String, Object> reqMap) throws Exception {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		if(oid.length() > 0) {
			WTPart part = (WTPart) CommonUtil.getObject(oid);
			
			String vrOid = CommonUtil.getVROID(part);
			
			part = (WTPart)CommonUtil.getObject(vrOid);
			
			List<String> alphabetList = PartHelper.service.checkPartRevision(part);
			
			PartData data = new PartData(part);
			
			model.addObject("list", alphabetList);
			model.addObject("part", data);
		}
		
		model.setViewName("popup:/part/addRevisionPopup");
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/addRevisionAction")
	public Map<String, Object> addRevisionAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			WTPart part = PartHelper.service.addRevision(reqMap);
			String oid = CommonUtil.getOIDString(part);

			map.put("msg", "생성되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/part/viewPart") + "?oid="+oid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}

		return map;
	}

}
