package com.e3ps.doc.controller;

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

import com.e3ps.admin.service.AdminHelper;
import com.e3ps.approval.util.ApprovalUtil;
import com.e3ps.change.beans.EChangeContentsData;
import com.e3ps.change.service.ChangeHelper;
import com.e3ps.common.bean.FolderData;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.doc.E3PSDocument;
import com.e3ps.doc.ETCDocumentContentsData;
import com.e3ps.doc.PDRDocumentContentsData;
import com.e3ps.doc.bean.DocData;
import com.e3ps.doc.bean.DocValueDefinitionData;
import com.e3ps.doc.bean.E3PSDocumentData;
import com.e3ps.doc.service.DocHelper;
import com.e3ps.org.bean.PeopleData;

import wt.enterprise.Master;
import wt.enterprise.RevisionControlled;
import wt.fc.PersistenceHelper;
import wt.folder.Folder;
import wt.org.WTUser;
import wt.pom.Transaction;
import wt.session.SessionHelper;


@Controller
@RequestMapping("/doc")
public class DocController {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.DOC.getName());
	public static final String ROOTLOCATION = "/Default";
	/**
	 * @desc	: 문서 등록
	 * @author	: sangylee
	 * @date	: 2019. 5. 21.
	 * @method	: createDoc
	 * @param	: reqMap
	 * @return	: ModelAndView
	 */
	@RequestMapping("/createDoc")
	public ModelAndView createDoc(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		model.setViewName("default:/doc/createDoc");
		org.w3c.dom.Element a = null;
		
		return model;
	}
	
	/**
	 * @desc	: 문서 등록 Action
	 * @author	: sangylee
	 * @date	: 2019. 5. 17.
	 * @method	: createDocAction
	 * @param	: reqMap
	 * @return	: Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/createDocAction")
	public Map<String, Object> createDocAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			DocHelper.service.createDocAction(reqMap);
			
			map.put("result", true);
			map.put("msg", "등록이 완료되었습니다.");
			map.put("redirectUrl", CommonUtil.getURLString("/doc/searchDoc"));
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc : 문서등록 팝업 액션
	 * @author : sangylee
	 * @date : 2020. 1. 3.
	 * @method : createDocPopupAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/createDocPopupAction")
	public Map<String, Object> createDocPopupAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			E3PSDocument doc = DocHelper.service.createDocAction(reqMap);
			
			map.put("result", true);
			map.put("msg", "등록이 완료되었습니다.");
			map.put("docOid", CommonUtil.getOIDString(doc));
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/createDocPopupAction2")
	public Map<String, Object> createDocPopupAction2(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			E3PSDocument doc = DocHelper.service.createDocAction2(reqMap);
			map.put("result", true);
			map.put("msg", "등록이 완료되었습니다.");
			map.put("docOid", CommonUtil.getOIDString(doc));
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 문서 검색
	 * @author	: sangylee
	 * @date	: 2019. 5. 21.
	 * @method	: searchDoc
	 * @param	: reqMap
	 * @return	: ModelAndView
	 */
	@RequestMapping("/searchDoc")
	public ModelAndView searchDoc(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		model.setViewName("default:/doc/searchDoc");
		
		return model;
	}
	
	@RequestMapping("/searchDoc2")
	public ModelAndView searchDoc2(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		model.setViewName("default:/doc/searchDoc2");
		
		return model;
	}
	
	@RequestMapping("/searchDoc3")
	public ModelAndView searchDoc3(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		model.setViewName("default:/doc/searchDoc3");
		
		return model;
	}
	
	/**
	 * @desc	: 문서 검색 Action
	 * @author	: sangylee
	 * @date	: 2019. 5. 21.
	 * @method	: searchDocAction
	 * @param	: reqMap
	 * @return	: Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/searchDocAction")
	public Map<String, Object> searchDocAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			List<E3PSDocumentData> list = DocHelper.manager.getDocList(reqMap);
			
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
	 * @desc	: 문서 검색 스크롤 Action
	 * @author	: sangylee
	 * @date	: 2019. 8. 14.
	 * @method	: searchDocScrollAction
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/searchDocScrollAction")
	public Map<String, Object> searchDocScrollAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			map = DocHelper.manager.getDocScrollList(reqMap);
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/searchDocAction2")
	public Map<String, Object> searchDocAction2(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			List<DocData> list = DocHelper.manager.getDocList2(reqMap);
			
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
	@RequestMapping("/searchDocAction3")
	public Map<String, Object> searchDocAction3(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			map = DocHelper.manager.getDocList3(reqMap);
			
			map.put("result", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 문서 상세
	 * @author	: sangylee
	 * @date	: 2019. 5. 21.
	 * @method	: viewDoc
	 * @param	: reqMap
	 * @return	: ModelAndView
	 */
	@RequestMapping("/viewDoc")
	public ModelAndView viewDoc(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			E3PSDocument doc = (E3PSDocument) CommonUtil.getObject(oid);
			E3PSDocumentData docData = new E3PSDocumentData(doc);
			boolean isAdmin = CommonUtil.isAdmin();
			boolean docModify = doc.getLifeCycleState().toString().equals("INWORK");
			boolean docApp = doc.getLifeCycleState().toString().equals("APPROVED");
			//boolean isAuth = AdminHelper.manager.isAuth(doc);
			
			model.addObject("doc", docData);
			model.addObject("isAdmin", isAdmin);
			model.addObject("docModify", docModify);
			model.addObject("docApp", docApp);
			model.setViewName("popup:/doc/viewDoc");
			
			//if(!isAuth) {
			//	model.setVisewName("popup:/admin/noPermission");
			//}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	/**
	 * @desc	: 문서 상세 정보
	 * @author	: sangylee
	 * @date	: 2019. 8. 6.
	 * @method	: detailDoc
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_detailDoc")
	public ModelAndView detailDoc(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			E3PSDocument doc = (E3PSDocument) CommonUtil.getObject(oid);
			
			DocData docData = new DocData(doc, true);
			String docNumber = docData.getNumber();
			String docNum = docNumber.substring(0,4);
			
			model.addObject("docNum", docNum);
			model.addObject("doc", docData);
			model.setViewName("include:/doc/include/detailDoc");
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
		
		model.setViewName("include:/doc/include/relatedObject");
		
		return model;
	}
	
	/**
	 * @desc	: 문서 수정
	 * @author	: sangylee
	 * @date	: 2019. 5. 21.
	 * @method	: modifyDoc
	 * @param	: reqMap
	 * @return	: ModelAndView
	 */
	@RequestMapping("/modifyDoc")
	public ModelAndView modifyDoc(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			
			E3PSDocument doc = (E3PSDocument) CommonUtil.getObject(oid);
			
			E3PSDocumentData docData = new E3PSDocumentData(doc);
			DocData docData1 = new DocData(doc, true);
			
			model.addObject("doc", docData);
			model.addObject("docAttribute", docData1);
			
			model.setViewName("popup:/doc/modifyDoc");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	/**
	 * @desc	: 문서 수정 Action
	 * @author	: sangylee
	 * @date	: 2019. 5. 29.
	 * @method	: modifyDocAction
	 * @param	: reqMap
	 * @return	: Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/modifyDocAction")
	public Map<String, Object> modifyDocAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			E3PSDocument doc = DocHelper.service.modifyDocAction(reqMap);
			
			String oid = CommonUtil.getOIDString(doc);
			
			map.put("msg", "수정되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/doc/viewDoc") + "?oid=" + oid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 문서 삭제 Action
	 * @author	: sangylee
	 * @date	: 2019. 5. 29.
	 * @method	: modifyDocAction
	 * @param	: reqMap
	 * @return	: Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/deleteDocAction")
	public Map<String, Object> deleteDocAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			DocHelper.service.deleteDocAction(reqMap);
			
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
	 * @desc	: 관련 문서 include
	 * @author	: sangylee
	 * @date	: 2019. 8. 13.
	 * @method	: relatedDoc
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_relatedDoc")
	public ModelAndView relatedDoc(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		model.addObject("oid", oid);
		
		model.setViewName("include:/doc/include/relatedDoc");
		
		return model;
	}
	
	/**
	 * @desc	: 관련 문서 가져오기
	 * @author	: sangylee
	 * @date	: 2019. 8. 13.
	 * @method	: getRelatedDoc
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getRelatedDoc")
	public static Map<String, Object> getRelatedDoc(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<>();
		
		
		try {
			List<E3PSDocumentData> list = DocHelper.manager.getRelatedDoc(reqMap);
			
			map.put("list", list);
			map.put("result",true);
		} catch (Exception e) {
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	/**
	 * @desc	: 문서 속성 가져오기
	 * @author	: mnyu
	 * @date	: 2019. 9. 9.
	 * @method	: docAttribute
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_docAttribute")
	public ModelAndView docAttribute(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try { 
			List<DocValueDefinitionData> list = DocHelper.manager.getAttribute(reqMap);
			String mode = StringUtil.checkNull((String) reqMap.get("mode"));
			
			model.addObject("mode", mode);
			model.addObject("list", list);
			
			model.setViewName("include:/doc/include/docAttribute");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	/**
	 * @desc	: 문서 폐기
	 * @author	: mnyu
	 * @date	: 2019. 10. 21.
	 * @method	: withdrawnDocAction
	 * @param	: reqMap
	 * @return	: Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/withdrawnDocAction")
	public Map<String, Object> withdrawnDocAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			DocHelper.service.withdrawnDocAction(reqMap);
			
			map.put("msg", "폐기되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/doc/viewDoc") + "?oid=" + oid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	/**
	 * @desc	: 문서 개정
	 * @author	: mnyu
	 * @date	: 2019. 10. 29.
	 * @method	: reviseDocAction
	 * @param	: reqMap
	 * @return	: Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/reviseDocAction")
	public Map<String, Object> reviseDocAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			E3PSDocument doc = DocHelper.service.reviseDocAction(reqMap);
			String oid = CommonUtil.getOIDString(doc);
			map.put("msg", "개정되었습니다.");
			map.put("result", true);
			map.put("redirectUrl", CommonUtil.getURLString("/doc/viewDoc") + "?oid=" + oid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	
	/**
	 * @desc : 문서 등록 팝업
	 * @author : sangylee
	 * @date : 2020. 1. 3.
	 * @method : createDocPopup
	 * @return : ModelAndView
	 * @param reqMap
	 * @return
	 */
	@RequestMapping("/createDocPopup")
	public ModelAndView createDocPopup(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String outputOid = StringUtil.checkNull((String) reqMap.get("outputOid"));
		String foid = StringUtil.checkNull((String) reqMap.get("foid"));
		
		Folder folder = (Folder)CommonUtil.getObject(foid);
		String location = folder.getFolderPath();
		String locationDisplay = location.replace("/Default", "");
		
		model.addObject("outputOid", outputOid);
		model.addObject("location", location);
		model.addObject("locationDisplay", locationDisplay);
		model.addObject("foid", foid);
		
		model.setViewName("popup:/doc/createDocPopup");
		
		return model;
	}
	
	@RequestMapping("/createDocPopup2")
	public ModelAndView createDocPopup2(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		String activeOid = StringUtil.checkNull((String) reqMap.get("activeOid"));
		String activeLinkType = StringUtil.checkNull((String) reqMap.get("activeLinkType"));
		String activeLinkOid = StringUtil.checkNull((String) reqMap.get("activeLinkOid"));
		LOGGER.debug("activeOid ->"+activeOid);
		LOGGER.debug("activeLinkOid ->"+activeLinkOid);
		model.addObject("activeOid", activeOid);
		model.addObject("activeLinkType", activeLinkType);
		model.addObject("activeLinkOid", activeLinkOid);
		model.setViewName("popup:/doc/createDocPopup2");
		
		return model;
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
		String objType = "doc"; //doc,part,epm
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
	 * @desc	: 문서 Folder 수정 화면
	 * @author	: hgkang
	 * @date	: 2023. 02. 13.
	 * @method	: modifyDocFolder
	 * @param reqMap
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/modifyDocFolder")
	public ModelAndView modifyDocFolder(@RequestParam Map<String, Object> reqMap) throws Exception {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
		FolderData data = new FolderData(oid);
		model.addObject("data", data);
		model.setViewName("popup:/doc/modifyDocFolder");
		
		return model;
	}
	
	/**
	 * @desc	: 문서 Folder 수정 Action
	 * @author	: hgkang
	 * @date	: 2023. 2. 14.
	 * @method	: modifyDocFolderAction
	 * @param   : reqMap
	 * @return	: Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/modifyDocFolderAction")
	public Map<String, Object> modifyDepartmentAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			
			boolean result = DocHelper.service.modifyDocFolderAction(reqMap);
			
			if(result) {
				map.put("result", result);
				map.put("msg", "수정되었습니다.");
				map.put("redirectUrl", "closeAndReload");
			}else {
				map.put("result", result);
			}
		}catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 문서 Folder 등록 화면
	 * @author	: hgkang
	 * @date	: 2023. 02. 13.
	 * @method	: createDocFolder
	 * @param	: reqMap
	 * @return	: ModelAndView
	 * @throws Exception 
	 */
	@RequestMapping("/createDocFolder")
	public ModelAndView createDocFolder(@RequestParam Map<String, Object> reqMap) throws Exception {
		ModelAndView model = new ModelAndView();
		
		String parentOid = StringUtil.checkNull((String) reqMap.get("parentOid")); 
		FolderData data = new FolderData(parentOid);
		model.addObject("parent", data);
		model.setViewName("popup:/doc/createDocFolder");
		
		return model;
	}
	
	/**
	 * @desc	: 문서 Folder 등록 Action
	 * @author	: hgkang
	 * @date	: 2023. 2. 14.
	 * @method	: createDocFolderAction
	 * @param   : reqMap
	 * @return	: Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/createDocFolderAction")
	public Map<String, Object> createDocFolderAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			
			boolean result = DocHelper.service.createDocFolderAction(reqMap);
			
			if(result) {
				map.put("result", result);
				map.put("msg", "등록되었습니다.");
				map.put("redirectUrl", "closeAndReload");
			}else {
				map.put("result", result);
			}
		}catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc : 문서 계약개발검토서 정보
	 * @author : hgkang
	 * @date : 2023. 7. 26.
	 * @method : contractDevelopmentReview
	 * @param reqMap
	 * @return ModelAndView
	 */

	@RequestMapping("/include_contractDevelopmentReview")
	public ModelAndView contractDevelopmentReview(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		try {
			String title = StringUtil.checkNull((String) reqMap.get("title"));
			String docNumber = StringUtil.checkNull((String) reqMap.get("docNumber"));

			model.addObject("docNumber", docNumber);
			model.addObject("title", title);
			model.setViewName("include:/doc/include/contractDevelopmentReview");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	/**
	 * @desc : 문서 서비스 요청서 정보
	 * @author : hgkang
	 * @date : 2023. 7. 26.
	 * @method : serviceRequest
	 * @param reqMap
	 * @return ModelAndView
	 */

	@RequestMapping("/include_serviceRequest")
	public ModelAndView serviceRequest(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		try {
			String title = StringUtil.checkNull((String) reqMap.get("title"));
			String docNumber = StringUtil.checkNull((String) reqMap.get("docNumber"));

			model.addObject("docNumber", docNumber);
			model.addObject("title", title);
			model.setViewName("include:/doc/include/serviceRequest");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	/**
	 * @desc : 문서 측정 의뢰서 정보
	 * @author : hgkang
	 * @date : 2023. 7. 26.
	 * @method : measurementRequest
	 * @param reqMap
	 * @return ModelAndView
	 */

	@RequestMapping("/include_measurementRequest")
	public ModelAndView measurementRequest(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		try {
			String title = StringUtil.checkNull((String) reqMap.get("title"));
			String docNumber = StringUtil.checkNull((String) reqMap.get("docNumber"));

			model.addObject("docNumber", docNumber);
			model.addObject("title", title);
			model.setViewName("include:/doc/include/measurementRequest");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	/**
	 * @desc : 문서 분석 의뢰서 정보
	 * @author : hgkang
	 * @date : 2023. 7. 26.
	 * @method : analysisRequest
	 * @param reqMap
	 * @return ModelAndView
	 */

	@RequestMapping("/include_analysisRequest")
	public ModelAndView analysisRequest(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		try {
			String title = StringUtil.checkNull((String) reqMap.get("title"));
			String docNumber = StringUtil.checkNull((String) reqMap.get("docNumber"));

			model.addObject("docNumber", docNumber);
			model.addObject("title", title);
			model.setViewName("include:/doc/include/analysisRequest");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	/**
	 * @desc : 문서 공정 변경 요청서 정보
	 * @author : hgkang
	 * @date : 2023. 7. 26.
	 * @method : processChangeRequest
	 * @param reqMap
	 * @return ModelAndView
	 */

	@RequestMapping("/include_processChangeRequest")
	public ModelAndView processChangeRequest(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		try {
			String title = StringUtil.checkNull((String) reqMap.get("title"));
			String docNumber = StringUtil.checkNull((String) reqMap.get("docNumber"));

			model.addObject("docNumber", docNumber);
			model.addObject("title", title);
			model.setViewName("include:/doc/include/processChangeRequest");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	/**
	 * @desc : 문서 공정 개발 검토서 정보
	 * @author : hgkang
	 * @date : 2023. 7. 26.
	 * @method : processDevelopmentReview
	 * @param reqMap
	 * @return ModelAndView
	 */

	@RequestMapping("/include_processDevelopmentReview")
	public ModelAndView processDevelopmentReview(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		try {
			String title = StringUtil.checkNull((String) reqMap.get("title"));
			String docNumber = StringUtil.checkNull((String) reqMap.get("docNumber"));

			model.addObject("docNumber", docNumber);
			model.addObject("title", title);
			model.setViewName("include:/doc/include/processDevelopmentReview");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	/**
	 * @desc : 문서 기타 정보
	 * @author : hgkang
	 * @date : 2023. 7. 26.
	 * @method : docCreateEtc
	 * @param reqMap
	 * @return ModelAndView
	 */

	@RequestMapping("/include_docCreateEtc")
	public ModelAndView docCreateEtc(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		try {
			String title = StringUtil.checkNull((String) reqMap.get("title"));
//			String docNumber = StringUtil.checkNull((String) reqMap.get("docNumber"));
//
//			model.addObject("docNumber", docNumber);
			model.addObject("title", title);
			model.setViewName("include:/doc/include/docCreateEtc");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	/**
	 * @desc : 문서 계약개발검토서 정보
	 * @author : hgkang
	 * @date : 2023. 7. 26.
	 * @method : viewcContractDevelopmentReview
	 * @param reqMap
	 * @return ModelAndView
	 */

	@RequestMapping("/include_viewContractDevelopmentReview")
	public ModelAndView viewContractDevelopmentReview(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		try {
			String title = StringUtil.checkNull((String) reqMap.get("title"));
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			E3PSDocument doc = (E3PSDocument) CommonUtil.getObject(oid);
			
			DocData docData = new DocData(doc, true);
			
			model.addObject("doc", docData);
			model.addObject("title", title);
			model.setViewName("include:/doc/include/viewContractDevelopmentReview");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	/**
	 * @desc : 문서 서비스 요청서 정보
	 * @author : hgkang
	 * @date : 2023. 7. 26.
	 * @method : viewServiceRequest
	 * @param reqMap
	 * @return ModelAndView
	 */

	@RequestMapping("/include_viewServiceRequest")
	public ModelAndView viewServiceRequest(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		try {
			String title = StringUtil.checkNull((String) reqMap.get("title"));
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			E3PSDocument doc = (E3PSDocument) CommonUtil.getObject(oid);
			
			DocData docData = new DocData(doc, true);
			
			model.addObject("doc", docData);
			model.addObject("title", title);
			model.setViewName("include:/doc/include/viewServiceRequest");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	/**
	 * @desc : 문서 측정 의뢰서 정보
	 * @author : hgkang
	 * @date : 2023. 7. 26.
	 * @method : viewMeasurementRequest
	 * @param reqMap
	 * @return ModelAndView
	 */

	@RequestMapping("/include_viewMeasurementRequest")
	public ModelAndView viewMeasurementRequest(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		try {
			String title = StringUtil.checkNull((String) reqMap.get("title"));
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			E3PSDocument doc = (E3PSDocument) CommonUtil.getObject(oid);
			
			DocData docData = new DocData(doc, true);
			
			model.addObject("doc", docData);
			model.addObject("title", title);
			model.setViewName("include:/doc/include/viewMeasurementRequest");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	/**
	 * @desc : 문서 분석 의뢰서 정보
	 * @author : hgkang
	 * @date : 2023. 7. 26.
	 * @method : viewAnalysisRequest
	 * @param reqMap
	 * @return ModelAndView
	 */

	@RequestMapping("/include_viewAnalysisRequest")
	public ModelAndView viewAnalysisRequest(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		try {
			String title = StringUtil.checkNull((String) reqMap.get("title"));
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			E3PSDocument doc = (E3PSDocument) CommonUtil.getObject(oid);
			
			DocData docData = new DocData(doc, true);
			
			model.addObject("doc", docData);
			model.addObject("title", title);
			model.setViewName("include:/doc/include/viewAnalysisRequest");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	/**
	 * @desc : 문서 공정 변경 요청서 정보
	 * @author : hgkang
	 * @date : 2023. 7. 26.
	 * @method : viewProcessChangeRequest
	 * @param reqMap
	 * @return ModelAndView
	 */

	@RequestMapping("/include_viewProcessChangeRequest")
	public ModelAndView viewProcessChangeRequest(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		try {
			String title = StringUtil.checkNull((String) reqMap.get("title"));
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			E3PSDocument doc = (E3PSDocument) CommonUtil.getObject(oid);
			
			DocData docData = new DocData(doc, true);
			
			model.addObject("doc", docData);
			model.addObject("title", title);
			model.setViewName("include:/doc/include/viewProcessChangeRequest");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	/**
	 * @desc : 문서 공정 개발 검토서 정보
	 * @author : hgkang
	 * @date : 2023. 7. 26.
	 * @method : viewProcessDevelopmentReview
	 * @param reqMap
	 * @return ModelAndView
	 */

	@RequestMapping("/include_viewProcessDevelopmentReview")
	public ModelAndView viewProcessDevelopmentReview(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		try {
			String title = StringUtil.checkNull((String) reqMap.get("title"));
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			E3PSDocument doc = (E3PSDocument) CommonUtil.getObject(oid);
			
			DocData docData = new DocData(doc, true);
			
			model.addObject("doc", docData);
			model.addObject("title", title);
			model.setViewName("include:/doc/include/viewProcessDevelopmentReview");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	/**
	 * @desc : 문서 기타 정보
	 * @author : hgkang
	 * @date : 2023. 7. 26.
	 * @method : viewDocEtc
	 * @param reqMap
	 * @return ModelAndView
	 */

	@RequestMapping("/include_viewDocEtc")
	public ModelAndView viewDocEtc(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		try {
			String title = StringUtil.checkNull((String) reqMap.get("title"));
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			E3PSDocument doc = (E3PSDocument) CommonUtil.getObject(oid);
			
			DocData docData = new DocData(doc, true);
			
			model.addObject("doc", docData);
			model.addObject("title", title);
			model.setViewName("include:/doc/include/viewDocEtc");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	
	/**
	  * @desc : 문서검색 Popup 
	  * @author : 
	  * @date : 2022. 11. 29.
	  * @method : searchDocPopup
	  * @param reqMap
	  * @return ModelAndView
	 */
	@RequestMapping("/searchDocPopup")
	public ModelAndView searchDocPopup(@RequestParam Map<String, Object> reqMap) {
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
		model.setViewName("popup:/doc/searchDocPopup");

		return model;
	}
	
	@ResponseBody
	@RequestMapping("/getDocumentTypeList")
	public Map<String, Object> getDocumentTypeList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			
			Map<String, String> returnMap = DocHelper.manager.getDocumetTypes();
			
			map.put("list", returnMap);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
	
	/**
	 * @desc : 문서 계약 개발 검토서 정보
	 * @author : hgkang
	 * @date : 2023. 7. 26.
	 * @method : modifyContractDevelopmentReview
	 * @param reqMap
	 * @return ModelAndView
	 */

	@RequestMapping("/include_modifyContractDevelopmentReview")
	public ModelAndView modifyContractDevelopmentReview(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		try {
			String title = StringUtil.checkNull((String) reqMap.get("title"));
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			E3PSDocument doc = (E3PSDocument) CommonUtil.getObject(oid);
			
			DocData docData = new DocData(doc, true);
			
			model.addObject("doc", docData);
			model.addObject("title", title);
			model.setViewName("include:/doc/include/modifyContractDevelopmentReview");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	/**
	 * @desc : 문서 서비스 요청서 정보
	 * @author : hgkang
	 * @date : 2023. 7. 26.
	 * @method : modifyServiceRequest
	 * @param reqMap
	 * @return ModelAndView
	 */

	@RequestMapping("/include_modifyServiceRequest")
	public ModelAndView modifyServiceRequest(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		try {
			String title = StringUtil.checkNull((String) reqMap.get("title"));
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			E3PSDocument doc = (E3PSDocument) CommonUtil.getObject(oid);
			
			DocData docData = new DocData(doc, true);
			
			model.addObject("doc", docData);
			model.addObject("title", title);
			model.setViewName("include:/doc/include/modifyServiceRequest");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	/**
	 * @desc : 문서 측정 의뢰서 정보
	 * @author : hgkang
	 * @date : 2023. 7. 26.
	 * @method : modifyMeasurementRequest
	 * @param reqMap
	 * @return ModelAndView
	 */

	@RequestMapping("/include_modifyMeasurementRequest")
	public ModelAndView modifyMeasurementRequest(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		try {
			String title = StringUtil.checkNull((String) reqMap.get("title"));
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			E3PSDocument doc = (E3PSDocument) CommonUtil.getObject(oid);
			
			DocData docData = new DocData(doc, true);
			
			model.addObject("doc", docData);
			model.addObject("title", title);
			model.setViewName("include:/doc/include/modifyMeasurementRequest");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	/**
	 * @desc : 문서 분석 의뢰서 정보
	 * @author : hgkang
	 * @date : 2023. 7. 26.
	 * @method : modifyAnalysisRequest
	 * @param reqMap
	 * @return ModelAndView
	 */

	@RequestMapping("/include_modifyAnalysisRequest")
	public ModelAndView modifyAnalysisRequest(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		try {
			String title = StringUtil.checkNull((String) reqMap.get("title"));
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			E3PSDocument doc = (E3PSDocument) CommonUtil.getObject(oid);
			
			DocData docData = new DocData(doc, true);
			
			model.addObject("doc", docData);
			model.addObject("title", title);
			model.setViewName("include:/doc/include/modifyAnalysisRequest");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	/**
	 * @desc : 공정 변경 요청서 정보
	 * @author : hgkang
	 * @date : 2023. 7. 26.
	 * @method : modifyProcessChangeRequest
	 * @param reqMap
	 * @return ModelAndView
	 */

	@RequestMapping("/include_modifyProcessChangeRequest")
	public ModelAndView modifyProcessChangeRequest(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		try {
			String title = StringUtil.checkNull((String) reqMap.get("title"));
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			E3PSDocument doc = (E3PSDocument) CommonUtil.getObject(oid);
			
			DocData docData = new DocData(doc, true);
			
			model.addObject("doc", docData);
			model.addObject("title", title);
			model.setViewName("include:/doc/include/modifyProcessChangeRequest");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	/**
	 * @desc : 공정 개발 검토서 정보
	 * @author : hgkang
	 * @date : 2023. 7. 26.
	 * @method : modifyProcessDevelopmentReview
	 * @param reqMap
	 * @return ModelAndView
	 */

	@RequestMapping("/include_modifyProcessDevelopmentReview")
	public ModelAndView modifyProcessDevelopmentReview(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		try {
			String title = StringUtil.checkNull((String) reqMap.get("title"));
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			E3PSDocument doc = (E3PSDocument) CommonUtil.getObject(oid);
			
			DocData docData = new DocData(doc, true);
			
			model.addObject("doc", docData);
			model.addObject("title", title);
			model.setViewName("include:/doc/include/modifyProcessDevelopmentReview");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	/**
	 * @desc : 문서 기타 수정 정보
	 * @author : hgkang
	 * @date : 2023. 7. 26.
	 * @method : modifyDocEtc
	 * @param reqMap
	 * @return ModelAndView
	 */

	@RequestMapping("/include_modifyDocEtc")
	public ModelAndView modifyDocEtc(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		try {
			String title = StringUtil.checkNull((String) reqMap.get("title"));
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			E3PSDocument doc = (E3PSDocument) CommonUtil.getObject(oid);
			
			DocData docData = new DocData(doc, true);
			
			model.addObject("doc", docData);
			model.addObject("title", title);
			model.setViewName("include:/doc/include/modifyDocEtc");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	@RequestMapping("/include_etcContent")
	public ModelAndView etcContent(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			String type = StringUtil.checkNull((String) reqMap.get("type"));
			
			model.addObject("oid", oid);
			model.addObject("type", type);
			model.setViewName("include:/doc/include/etcContent");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/getETCDocumentContents")
	public Map<String, Object> getETCDocumentContents(@RequestBody Map<String, Object> reqMap) {
		List<ETCDocumentContentsData> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			
			list = DocHelper.manager.getETCDocumentContents(oid);
			
			map.put("result", true);
			map.put("list", list);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/include_pdrContent")
	public ModelAndView pdrContent(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			model.addObject("oid", oid);
			model.setViewName("include:/doc/include/pdrContent");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	@RequestMapping("/include_viewPdrContent")
	public ModelAndView viewPdrContent(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			
			model.addObject("oid", oid);
			model.setViewName("include:/doc/include/viewPdrContent");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/getPDRDocumentContents")
	public Map<String, Object> getPDRDocumentContents(@RequestBody Map<String, Object> reqMap) {
		List<PDRDocumentContentsData> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			
			list = DocHelper.manager.getPDRDocumentContents(oid);
			
			map.put("result", true);
			map.put("list", list);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/include_modifyPdrContent")
	public ModelAndView modifyPdrContent(@RequestParam Map<String, Object> reqMap) {
		
		ModelAndView model = new ModelAndView();
		
		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid")); 
			model.addObject("oid", oid);
			model.setViewName("include:/doc/include/modifyPdrContent");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
}
