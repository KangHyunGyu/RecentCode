package com.e3ps.approval.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.e3ps.approval.MultiApproval;
import com.e3ps.approval.bean.MultiApprovalData;
import com.e3ps.approval.service.MultiApprovalHelper;
import com.e3ps.approval.util.ApprovalUtil;
import com.e3ps.common.bean.RevisionData;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;




@Controller
@RequestMapping("/multi")
public class MultiApprovalController {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.APPROVAL.getName());
	
	/**
	 * 
	 * @desc	:
	 * @author	: tsuam
	 * @date	: 2019. 8. 7.
	 * @method	: viewMulti
	 * @return	: ModelAndView
	 * @param reqMap
	 * objectType : part,doc,epm 
	 * pageName	  : multiAppPart,multiAppDpc,multiAppEpm
	 * title	  : 부품,문서,도면
	 * @return
	 */
	@RequestMapping("/viewMulti")
	public ModelAndView viewMulti(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		LOGGER.debug(">>>>>>>>>> MultiApprovalController viewMulti");
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			String objectType = StringUtil.checkReplaceStr((String) reqMap.get("objectType"), "doc"); //part,epm
			
			Map<String,String> map = ApprovalUtil.getMultiTypeInfo(objectType);
			String pageName = map.get("pageName");//multiAppPart,multiAppEpm
			String title = map.get("title"); //폼목,도면
			MultiApproval multi  = (MultiApproval) CommonUtil.getObject(oid);
			
			MultiApprovalData multiData = new MultiApprovalData(multi);
			
			model.addObject("multi", multiData);
			model.addObject("objectType", objectType);
			model.addObject("pageName", pageName);
			model.addObject("title", title);
			
			model.setViewName("popup:/approval/viewMulti");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	@RequestMapping("/include_detailMulti")
	public ModelAndView detailMulti(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			
			MultiApproval multi  = (MultiApproval) CommonUtil.getObject(oid);
			
			MultiApprovalData multiData = new MultiApprovalData(multi);
			
			model.addObject("multi", multiData);
			
			model.setViewName("include:/approval/include/detailMulti");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	@RequestMapping("/include_relatedObject")
	public ModelAndView relatedObject(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			MultiApproval multi = (MultiApproval)CommonUtil.getObject(oid);
			MultiApprovalData multiData = new MultiApprovalData(multi);
			model.addObject("oid", oid);
			model.addObject("objectTypeName", multiData.getObjectTypeName());
			model.setViewName("include:/approval/include/relatedObject");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/getRelatedObject")
	public static Map<String, Object> getRelatedObject(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<>();
		
		
		try {
			List<RevisionData> list = MultiApprovalHelper.manager.getRelatedObject(reqMap);
			
			map.put("list", list);
			map.put("result",true);
		} catch (Exception e) {
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	

	@RequestMapping("/modifyMulti")
	public ModelAndView modifyMulti(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		LOGGER.debug(">>>>>>>>>> MultiApprovalController modifyMulti");
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			/*일괄 결재 타입에 따른 include pamrm 결정*/
			String objectType = StringUtil.checkReplaceStr((String) reqMap.get("objectType"), "doc"); //part,epm
			Map<String,String> map = ApprovalUtil.getMultiTypeInfo(objectType);
			String pageName = map.get("pageName");//multiAppPart,multiAppEpm
			String title = map.get("title"); //폼목,도면
			
			MultiApproval multi = (MultiApproval) CommonUtil.getObject(oid);
			
			MultiApprovalData multiData = new MultiApprovalData(multi);
			
			model.addObject("multi", multiData);
			model.addObject("objectType", objectType);
			model.addObject("pageName", pageName);
			model.addObject("title", title);
			model.setViewName("popup:/approval/modifyMultiApproval");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	/**
	 * 
	 * @desc	: 일괄 결재 수정
	 * @author	: tsuam
	 * @date	: 2019. 8. 8.
	 * @method	: modifyMultiAction
	 * @return	: Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/modifyMultiAction")
	public Map<String, Object> modifyMultiAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			MultiApproval multi = MultiApprovalHelper.service.modifyMultiAction(reqMap);//DocHelper.service.modifyDocAction(reqMap);
			
			String oid = CommonUtil.getOIDString(multi);
			
			map.put("msg", "수정되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/multi/viewMulti") + "?oid=" + oid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * 
	 * @desc	: 일괄 결재 삭제
	 * @author	: tsuam
	 * @date	: 2019. 8. 8.
	 * @method	: deleteMultiAction
	 * @return	: Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteMultiAction")
	public Map<String, Object> deleteMultiAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			MultiApprovalHelper.service.deleteMultiAction(reqMap);
			//DocHelper.service.deleteDocAction(reqMap);
			
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

}
