package com.e3ps.part.editor.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.ObjectUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.part.bean.PartData;
import com.e3ps.part.editor.bean.BomEditorTreeData;
import com.e3ps.part.editor.service.BomEditorHelper;
import com.e3ps.part.service.PartHelper;
import com.e3ps.part.util.PartUtil;

import wt.part.WTPart;

@Controller
@RequestMapping("/bomEditor")
public class BomEditorController {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PART.getName());
	
	/**
	 * @desc	: Bom root 리스트
	 * @author	: sangylee
	 * @date	: 2019. 8. 6.
	 * @method	: getBomRoot
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getBomRoot")
	public static Map<String, Object> getBomRoot(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<>();
		
		try {
			List<BomEditorTreeData> list = BomEditorHelper.manager.getBomRoot(reqMap);
			
			map.put("list", list);
			map.put("result",true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: Bom children 리스트 가져오기
	 * @author	: sangylee
	 * @date	: 2019. 8. 6.
	 * @method	: getBomChildren
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getBomChildren")
	public static Map<String, Object> getBomChildren(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<>();
		
		try {
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			WTPart part = (WTPart)CommonUtil.getObject(oid);
			boolean isOwnerCheckOut = PartUtil.isOwnerCheckOut(part);
			boolean isCheckOut = PartUtil.isCheckOut(part);
			boolean isModify = false;
			List<BomEditorTreeData> list = BomEditorHelper.manager.getBomChildren(reqMap);
			if(isCheckOut){
				isModify = isOwnerCheckOut;
			}else{
				isModify = true;
			}
			map.put("isModify", isModify);
			map.put("list", list);
			map.put("result",true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: BOM 에디터
	 * @author	: sangylee
	 * @date	: 2019. 8. 9.
	 * @method	: editBom
	 * @param reqMap
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/editBom")
	public ModelAndView editBom(@RequestParam Map<String, Object> reqMap) throws Exception {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		if(oid.length() > 0) {
			WTPart part = (WTPart) CommonUtil.getObject(oid);
			
			String vrOid = CommonUtil.getVROID(part);
			
			part = (WTPart)CommonUtil.getObject(vrOid);
			
			PartData data = new PartData(part);
			
			model.addObject("part", data);
			model.addObject("isPart", true);
		} else {
			model.addObject("isPart", false);
		}
		
		model.setViewName("popup:/part/editor/editBom");
		
		return model;
	}
	

	/**
	 * @desc : BOM 에디터 변경사항 저장
	 * @author : sangylee
	 * @date : 2019. 9. 17.
	 * @method : saveChildrenBomAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveChildrenBomAction")
	public Map<String, Object> saveChildrenBomAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		
		try {
			WTPart pPart = BomEditorHelper.service.saveChildrenBomAction(reqMap);
			
			map.put("parentOid", CommonUtil.getOIDString(pPart));
			map.put("result", true);
			map.put("msg", "저장이 완료되었습니다.");
		} catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
	
	/**
	 * @desc : 추가된 부품 저장
	 * @author : sangylee
	 * @date : 2019. 10. 30.
	 * @method : saveAddedPartAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveAddedPartAction")
	public Map<String, Object> saveAddedPartAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		
		try {
			WTPart pPart = BomEditorHelper.service.saveAddedPartAction(reqMap);
			
			PartData data = new PartData(pPart);
			
			map.put("part", data);
			map.put("result", true);
		} catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
	
	/**
	 * @desc : bom part 삭제
	 * @author : sangylee
	 * @date : 2019. 10. 30.
	 * @method : deletBomPartAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteBomPartAction")
	public Map<String, Object> deletBomPartAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		
		try {
			WTPart pPart = BomEditorHelper.service.deleteBomPartAction(reqMap);
			
			PartData data = new PartData(pPart);
			
			map.put("part", data);
			map.put("result", true);
		} catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
	
	/**
	 * @desc : bom part 수정
	 * @author : sangylee
	 * @date : 2019. 10. 30.
	 * @method : modifyBomPartAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/modifyBomPartAction")
	public Map<String, Object> modifyBomPartAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		
		try {
			WTPart pPart = BomEditorHelper.service.modifyBomPartAction(reqMap);
			
			PartData data = new PartData(pPart);
			
			map.put("part", data);
			map.put("result", true);
		} catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
	
	/**
	 * @desc : bom 수정, 삭제, 추가 시 parent 업데이트
	 * @author : sangylee
	 * @date : 2019. 10. 31.
	 * @method : updateBomTreeParent
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/getUpdatedBomData")
	public Map<String, Object> getUpdatedBomData(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		
		try {
			List<BomEditorTreeData> list = BomEditorHelper.manager.getUpdatedBomData(reqMap);
			
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
	 * @desc : 체크아웃
	 * @author : sangylee
	 * @date : 2019. 11. 5.
	 * @method : checkout
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/checkout")
	public Map<String, Object> checkout(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		
		List<PartData> checkOutList = new ArrayList<PartData>();
		
		try {
			List<Map<String, Object>> items = (List<Map<String, Object>>) reqMap.get("items");
			
			// 중복처리
			List<String> oidList = new ArrayList<>();
			for(Map<String, Object> item : items) {
				String oid = StringUtil.checkNull((String) item.get("oid"));
				
				if(oidList.contains(oid)) {
					continue;
				}
				oidList.add(oid);
				
				WTPart part = (WTPart) CommonUtil.getObject(oid);
				
				part = (WTPart) ObjectUtil.checkout(part);
				
				PartData data = new PartData(part);
				
				checkOutList.add(data);
			}
			
			map.put("checkOutList", checkOutList);
			map.put("result", true);
		} catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
	
	/**
	 * @desc : 체크 인
	 * @author : sangylee
	 * @date : 2019. 11. 5.
	 * @method : checkin
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/checkin")
	public Map<String, Object> checkin(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		
		List<PartData> checkInList = new ArrayList<PartData>();
		
		try {
			
			List<Map<String, Object>> items = (List<Map<String, Object>>) reqMap.get("items");
			
			// 중복처리
			List<String> oidList = new ArrayList<>();
			for(Map<String, Object> item : items) {
				String oid = StringUtil.checkNull((String) item.get("oid"));
				
				if(oidList.contains(oid)) {
					continue;
				}
				oidList.add(oid);
				
				WTPart part = (WTPart) CommonUtil.getObject(oid);
				
				part = (WTPart) ObjectUtil.checkin(part);
				
				PartData data = new PartData(part);
				
				checkInList.add(data);
			}
			
			map.put("checkInList", checkInList);
			map.put("result", true);
		} catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
	
	/**
	 * @desc : 체크아웃 명령 취소
	 * @author : sangylee
	 * @date : 2019. 11. 5.
	 * @method : undoCheckout
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/undoCheckout")
	public Map<String, Object> undoCheckout(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		
		List<PartData> undoList = new ArrayList<PartData>();
		
		try {
			
			List<Map<String, Object>> items = (List<Map<String, Object>>) reqMap.get("items");
			
			// 중복처리
			List<String> oidList = new ArrayList<>();
			for(Map<String, Object> item : items) {
				String oid = StringUtil.checkNull((String) item.get("oid"));
				
				if(oidList.contains(oid)) {
					continue;
				}
				oidList.add(oid);
				
				WTPart part = (WTPart) CommonUtil.getObject(oid);
				
				part = (WTPart) ObjectUtil.undoCheckout(part);
				
				PartData data = new PartData(part);
				
				undoList.add(data);
			}
			
			map.put("undoList", undoList);
			map.put("result", true);
		} catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/revise")
	public Map<String, Object> revise(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		
		List<PartData> reviseList = new ArrayList<>();
		
		try {
			List<Map<String, Object>> items = (List<Map<String, Object>>) reqMap.get("items");
			
			for(Map<String, Object> item : items) {
				WTPart newPart = PartHelper.service.revisePartAction(item);
				
				PartData data = new PartData(newPart);
				
				reviseList.add(data);
			}
			
			map.put("reviseList", reviseList);
			map.put("result", true);
		} catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
	
	/**
	 * @desc : BOM 부품 검색 팝업
	 * @author : sangylee
	 * @date : 2019. 11. 11.
	 * @method : searchBomPartPopup
	 * @return : ModelAndView
	 * @param reqMap
	 * @throws Exception
	 */
	@RequestMapping("/searchBomPartPopup")
	public ModelAndView searchBomPartPopup(@RequestParam Map<String, Object> reqMap) throws Exception {
		ModelAndView model = new ModelAndView();
		
		String type = StringUtil.checkReplaceStr((String) reqMap.get("type"), "multi");
		String parentOid = StringUtil.checkNull((String) reqMap.get("parentOid"));
		
		model.addObject("type", type);
		model.addObject("parentOid", parentOid);
		model.setViewName("popup:/part/editor/searchBomPartPopup");
		
		return model;
	}
	
	/**
	 * @desc : BOM 부품 검색 팝업 Action
	 * @author : sangylee
	 * @date : 2019. 11. 11.
	 * @method : searchBomPartScrollAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/searchBomPartScrollAction")
	public Map<String, Object> searchBomPartScrollAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			map = BomEditorHelper.manager.getBomPartScrollList(reqMap);
			
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc : 부품 일괄 추가
	 * @author : sangylee
	 * @date : 2019. 11. 11.
	 * @method : saveAddedPartListAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/saveAddedPartListAction")
	public Map<String, Object> saveAddedPartListAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		
		try {
			List<WTPart> list = BomEditorHelper.service.saveAddedPartListAction(reqMap);
			
			map.put("result", true);
		} catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
	
	/**
	 * @desc : Refresh 데이터
	 * @author : sangylee
	 * @date : 2019. 11. 12.
	 * @method : getRefreshBomData
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/getRefreshBomData")
	public Map<String, Object> getRefreshBomData(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		
		try {
			List<BomEditorTreeData> list = BomEditorHelper.manager.getRefreshBomData(reqMap);
			
			map.put("list", list);
			map.put("result", true);
		} catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/multiCheckOut")
	public Map<String, Object> multiCheckOut(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		
		try {
			List<Map<String, Object>> checkOutItemList = (List<Map<String, Object>>) reqMap.get("checkOutItemList");
			
			for(Map<String, Object> checkOutItem : checkOutItemList) {
				String oid = StringUtil.checkNull((String) checkOutItem.get("oid"));
				
				WTPart part = (WTPart) CommonUtil.getObject(oid);
				
				part = (WTPart) ObjectUtil.checkout(part);
			}
			
			map.put("result", true);
		} catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
}
