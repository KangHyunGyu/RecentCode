package com.e3ps.common.controller;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.e3ps.approval.bean.ApprovalLineData;
import com.e3ps.approval.service.ApprovalHelper;
import com.e3ps.approval.util.ApprovalUtil;
import com.e3ps.common.bean.FolderData;
import com.e3ps.common.bean.RevisionData;
import com.e3ps.common.code.NumberCode;
import com.e3ps.common.code.NumberCodeType;
import com.e3ps.common.code.bean.NumberCodeData;
import com.e3ps.common.code.service.CodeHelper;
import com.e3ps.common.code.util.NumberCodeUtil;
import com.e3ps.common.history.bean.DownloadHistoryData;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.service.CommonHelper;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.FolderUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.common.util.WFItemUtil;
import com.e3ps.epm.util.EpmUtil;
import com.e3ps.org.bean.DepartmentData;
import com.e3ps.org.bean.PeopleData;
import com.e3ps.org.service.DepartmentHelper;
import com.e3ps.org.service.PeopleHelper;
import com.e3ps.workspace.notice.Notice;

import wt.content.ContentHelper;
import wt.fc.Persistable;
import wt.folder.Folder;
import wt.util.WTException;

/**
 * @author plmadmin
 *
 */
@Controller
@RequestMapping("/common")
public class CommonController {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
	/**
	 * @desc	: 폴더트리 뷰
	 * @author	: sangylee
	 * @date	: 2019. 7. 10.
	 * @method	: folderTree
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_folderTree")
	public ModelAndView folderTree(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String container = StringUtil.checkNull((String) reqMap.get("container"));
		String renderTo = StringUtil.checkNull((String) reqMap.get("renderTo"));
		String formId = StringUtil.checkNull((String) reqMap.get("formId"));
		String locationId = StringUtil.checkReplaceStr((String) reqMap.get("locationId"), "location");
		String rootLocation = StringUtil.checkNull((String) reqMap.get("rootLocation"));
		String location = StringUtil.checkNull((String) reqMap.get("location"));
		String autoGridHeight = StringUtil.checkReplaceStr((String) reqMap.get("autoGridHeight"), "false");
		String gridHeight = StringUtil.checkReplaceStr((String) reqMap.get("gridHeight"), "300");
		String gridWidth = StringUtil.checkReplaceStr((String) reqMap.get("gridWidth"), "230");

		model.addObject("container", container);
		model.addObject("renderTo", renderTo);
		model.addObject("formId", formId);
		model.addObject("locationId", locationId);
		model.addObject("rootLocation", rootLocation);
		model.addObject("location", location);
		model.addObject("autoGridHeight", autoGridHeight);
		model.addObject("gridHeight", gridHeight);
		model.addObject("gridWidth", gridWidth);

		model.setViewName("include:/common/include/folderTree");
		
		return model;
	}
	
	/**
	 * @desc	: 폴더 트리 리스트 액션
	 * @author	: sangylee
	 * @date	: 2019. 7. 10.
	 * @method	: getFolderTree
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getFolderTree")
	public static Map<String, Object> getFolderTree(@RequestBody Map<String, Object> reqMap) {
		
		return CommonHelper.manager.getFolderTree(reqMap);
		/*
		Map<String, Object> map = new HashMap<>();
		
		String container = StringUtil.checkNull((String) reqMap.get("container"));
		String rootLocation = StringUtil.checkNull((String) reqMap.get("rootLocation")); 
		
		try {
			if(rootLocation.length() > 0) {
				PDMLinkProduct pdmProduct = null;
				Folder folder = null;
				if(container.length() > 0) {
					pdmProduct = WCUtil.getPDMLinkProduct(container);
				} else {
					pdmProduct = WCUtil.getPDMLinkProduct();
				}
				
				if(pdmProduct == null){
					WCUtil.getLibrary(container);
				}else{
					WTContainerRef wtContainerRef = WTContainerRef.newWTContainerRef(pdmProduct);
					
					folder = FolderHelper.service.getFolder(rootLocation, wtContainerRef);
				}
				
				
				
				
				
				List<FolderData> list = new ArrayList<>();
				if(folder != null) {
					
					if(folder instanceof Cabinet){
						
						List<Folder> subFolderList =FolderUtil.getCabinetSubFolder(folder);
						
						for(Folder suFolder : subFolderList){
							FolderUtil.getFolderTree(suFolder, list);
						}
						
					}else{
						
						FolderUtil.getFolderTree(folder, list);
					}
					
					
					
					map.put("list", list);
					map.put("result", true);
				} else {
					map.put("result", false);
					map.put("msg", "폴더를 생성해야 합니다. 관리자에게 문의하세요.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
		*/
	}
	
	/**
	 * @desc	: 특정 폴더 트리 자식 리스트 가져오기
	 * @author	: mnyu
	 * @date	: 2020. 2. 7.
	 * @method	: getFolderChildrenList
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getFolderChildrenList")
	public static Map<String, Object> getFolderChildrenList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			String folderOid = StringUtil.checkNull((String) reqMap.get("folderOid"));
			Folder folder = FolderUtil.getFolderOid(folderOid);
			List<FolderData> list = FolderUtil.geSubFolderList(folder);
			
			map.put("list", list);
			map.put("result",true);
		} catch (Exception e) {
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 특정 폴더 트리 자식 리스트 가져오기
	 * @author	: tsjeong
	 * @date	: 2020. 8. 25.
	 * @method	: getFolderChildrenList2
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getFolderChildrenList2")
	public static Map<String, Object> getFolderChildrenList2(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			String folderOid = StringUtil.checkNull((String) reqMap.get("folderOid"));
			Folder folder = FolderUtil.getFolderOid(folderOid);
			List<FolderData> list = FolderUtil.getSubFolderList(folder);
			
			map.put("list", list);
			map.put("result",true);
		} catch (Exception e) {
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: Library 폴더 트리 자식 리스트 가져오기
	 * @author	: tsjeong
	 * @date	: 2020. 8. 26.
	 * @method	: getLibraryFolderChildrenList
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getLibraryFolderChildrenList")
	public static Map<String, Object> getLibraryFolderChildrenList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			Folder folder = null;
			if(reqMap.get("folderOid") != null) {
				String folderOid = StringUtil.checkNull((String) reqMap.get("folderOid"));
				folder = FolderUtil.getFolderOid(folderOid);
			}
			
			List<FolderData> list = FolderUtil.getLibrarySubFolderList(folder);
			
			map.put("list", list);
			map.put("result",true);
		} catch (Exception e) {
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 폴더 생성
	 * @author	: mnyu
	 * @date	: 2020. 2. 7.
	 * @method	: createFolder
	 * @param	: reqMap
	 * @return	: ModelAndView
	 */
	@RequestMapping("/createFolderPopup")
	public ModelAndView createFolder(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		try {
			String renderTo = StringUtil.checkNull((String) reqMap.get("renderTo")); 
			String fOid = StringUtil.checkNull((String) reqMap.get("fOid")); 
			String rootLocation = StringUtil.checkNull((String) reqMap.get("rootLocation")); 
			String container = StringUtil.checkNull((String) reqMap.get("container")); 
			
			model.addObject("renderTo", renderTo);
			model.addObject("fOid", fOid);
			model.addObject("rootLocation", rootLocation);
			model.addObject("container", container);
			model.setViewName("popup:/common/createFolderPopup");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	/**
	 * @desc	: 폴더 저장
	 * @author	: mnyu
	 * @date	: 2020. 2. 10.
	 * @method	: saveFolderAction
	 * @param	: reqMap
	 * @return	: Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/saveFolderAction")
	public Map<String, Object> saveFolderAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			CommonHelper.service.saveFolderAction(reqMap);
			
			map.put("msg", "저장되었습니다.");
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 버전 이력 뷰
	 * @author	: sangylee
	 * @date	: 2019. 7. 10.
	 * @method	: versionHistory
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_versionHistory")
	public ModelAndView versionHistory(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		model.addObject("oid", oid);
		
		model.setViewName("include:/common/include/versionHistory");
		
		return model;
	}
	
	/**
	 * @desc	: 버전 이력 조회 액션
	 * @author	: sangylee
	 * @date	: 2019. 7. 10.
	 * @method	: getVersionHistory
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getVersionHistory")
	public static Map<String, Object> getVersionHistory(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<>();
		
		try {
			
			List<RevisionData> list = CommonHelper.manager.getVersionHistory(reqMap);
			
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
	 * @desc	: 다운로드 이력 뷰
	 * @author	: sangylee
	 * @date	: 2019. 7. 10.
	 * @method	: downloadHistory
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_downloadHistory")
	public ModelAndView downloadHistory(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		model.addObject("oid", oid);
		
		model.setViewName("include:/common/include/downloadHistory");
		
		return model;
	}
	
	/**
	 * @desc	: 다운로드 이력 조회 액션
	 * @author	: sangylee
	 * @date	: 2019. 7. 10.
	 * @method	: getDownloadHistory
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getDownloadHistory")
	public static Map<String, Object> getDownloadHistory(@RequestBody Map<String, Object> reqMap) {
		
		Map<String, Object> map = new HashMap<>();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		try {
			List<DownloadHistoryData> list = new ArrayList<DownloadHistoryData>();
			list = CommonHelper.manager.getDownloadHistory(oid);
			
			
			map.put("list", list);
			map.put("result", true);
			
		} catch (Exception e) {
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc	: 결재 이력 조회 뷰
	 * @author	: sangylee
	 * @date	: 2019. 7. 10.
	 * @method	: approveHistory
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_approveHistory")
	public ModelAndView approveHistory(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		model.addObject("oid", oid);
		
		model.setViewName("include:/common/include/approveHistory");
		
		return model;
	}
	
	/**
	 * @desc	: 결재 객체로 결재 이력 조회 액션
	 * @author	: sangylee
	 * @date	: 2019. 7. 10.
	 * @method	: getApproveHistory
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getApproveHistory")
	public static Map<String, Object> getApproveHistory(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		try {
			List<ApprovalLineData> list = new ArrayList<ApprovalLineData>();
			list = ApprovalHelper.manager.getApproveHistory(oid);
			
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
	 * @desc	: 유저 검색 액션
	 * @author	: sangylee
	 * @date	: 2019. 7. 10.
	 * @method	: searchUserAction
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/searchUserAction")
	public Map<String,Object> searchUserAction(@RequestBody Map<String, Object> reqMap) {
		Map<String,Object> map = new HashMap<>();
		try {
			List<PeopleData> list = PeopleHelper.manager.getLicenseAllUserList(reqMap);
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
	 * @desc : 부서 검색 액션
	 * @author : sangylee
	 * @date : 2019. 9. 27.
	 * @method : searchDepartmentAction
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/searchDepartmentAction")
	public Map<String,Object> searchDepartmentAction(@RequestBody Map<String, Object> reqMap) {
		Map<String,Object> map = new HashMap<>();
		try {
			List<DepartmentData> list = DepartmentHelper.manager.getSearchDepartmentListAction(reqMap);
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
	 * @desc	: AUIGrid 엑셀 다운로드
	 * @author	: sangylee
	 * @date	: 2019. 7. 10.
	 * @method	: xlsxExport
	 * @param reqMap
	 * @return void
	 */
	@RequestMapping("/xlsxExport")
	public void xlsxExport(HttpServletRequest req, HttpServletResponse res) {
		try {

			// AUIGrid Export 시 로컬에 다운로드 가능하도록 작성된 서버사이드 예제입니다.
			// AUIGrid 가 xlsx, csv, xml 등의 형식을 작성하여 base64 로 인코딩하여 data 파라메터로 post 요청을 합니다.
			// 해당 서버 예제(본 JSP) 에서는 base64 로 인코딩 된 데이터를 디코드하여 다운로드 가능하도록 붙임으로 마무리합니다.
			// 참고로 org.apache.commons.codec.binary.Base64 클래스 사용을 위해는 commons-codec-1.4.jar
			// 파일이 필요합니다.
			String data = req.getParameter("data"); // 파라메터 data
			String extension = req.getParameter("extension"); // 파라메터 확장자

			// ("data ---" + data);
			// AUIGrid.exportAsXlsx() 사용시 exportProps 로 파일명을 지정해 줬다면 다음과 같이 지정된 파일명을 얻을 수
			// 있습니다.
			// String req_fileName = request.getParameter("filename"); // 파라메터 파일명

			byte[] dataByte = Base64.decodeBase64(data.getBytes()); // 데이터 base64 디코딩
			
			String fileName = "export." + extension;

			String encoding = "UTF-8";
			fileName = ContentHelper.encode(fileName, encoding);
			
			res.setContentType("application/octet-stream");
			res.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\";");
			
			OutputStream os = res.getOutputStream();

			os.write(dataByte);

			os.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @desc	: 라이프 사이클 리스트
	 * @author	: sangylee
	 * @date	: 2019. 7. 10.
	 * @method	: getLifecycleList
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getLifecycleList")
	public Map<String, Object> getLifecycleList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		String lifecycle = StringUtil.checkNull((String) reqMap.get("lifecycle"));

		try {
			if(lifecycle.length() == 0) {
				lifecycle = "LC_Default";
			}
			List<Map<String, String>> list = WFItemUtil.getLifeCycleStateList(lifecycle);
			
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
	 * @desc	: 캐드 구분 리스트 조회
	 * @author	: sangylee
	 * @date	: 2019. 7. 10.
	 * @method	: getCadDivisionList
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getCadDivisionList")
	public Map<String, Object> getCadDivisionList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			List<Map<String, String>> list = EpmUtil.getCadDivisionList();
			
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
	 * @desc	: 캐드 타입 리스트 조회
	 * @author	: sangylee
	 * @date	: 2019. 7. 10.
	 * @method	: getCadTypeList
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getCadTypeList")
	public Map<String, Object> getCadTypeList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			List<Map<String, String>> list = EpmUtil.getCadTypeList();
			
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
	 * @desc : 캐드 타입 리스트(등록 용)
	 * @author : sangylee
	 * @date : 2019. 9. 9.
	 * @method : getCadTypeListForCreate
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/getCadTypeListForCreate")
	public Map<String, Object> getCadTypeListForCreate(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			List<Map<String, String>> list = EpmUtil.getCadTypeListForCreate();
			
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
	 * @desc	: 로그아웃
	 * @author	: sangylee
	 * @date	: 2019. 7. 10.
	 * @method	: logout
	 * @param req
	 * @param res
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/logout")
	public Map<String, Object> logout(HttpServletRequest req, HttpServletResponse res) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		req.getSession().invalidate();
		
		map.put("result", true);
		map.put("redirectUrl", CommonUtil.getURLString("/portal/main"));
		
		return map;
	}
	
	/**
	 * 
	 * @desc	: 결재 타입 리스트
	 * @author	: tsuam
	 * @date	: 2019. 7. 16.
	 * @method	: getApprovalRoleTypeList
	 * @return	: Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getApprovalRoleTypeList")
	public Map<String, Object> getApprovalRoleTypeList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			List<Map<String, String>> list = ApprovalUtil.getApprovalRoleType();
			
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
	@RequestMapping("/getCodeTypeList")
	public Map<String, Object> getCodeTypeList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			List<Map<String, String>> list = NumberCodeUtil.getCodeTypeList();
			
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
	 * @desc	: 객체 추가 include
	 * @author	: sangylee
	 * @date	: 2019. 7. 18.
	 * @method	: addObject
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/include_addObject")
	public ModelAndView addObject(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String type = StringUtil.checkReplaceStr((String) reqMap.get("type"), "multi");
		String title = StringUtil.checkNull((String) reqMap.get("title"));
		String objType = StringUtil.checkNull((String) reqMap.get("objType"));
		String pageName = StringUtil.checkNull((String) reqMap.get("pageName"));
		String autoGridHeight = StringUtil.checkReplaceStr((String) reqMap.get("autoGridHeight"), "false");
		String gridHeight = StringUtil.checkReplaceStr((String) reqMap.get("gridHeight"), "55");
		String toggle = StringUtil.checkReplaceStr((String) reqMap.get("toggle"), "true");
		
		String moduleType = StringUtil.checkNull((String) reqMap.get("moduleType"));
		
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
		model.addObject("toggle", toggle);
		
		model.setViewName("include:/common/include/addObject");
		
		return model;
	}
	
	/**
	 * @desc	: 객체 리스트 가져오기
	 * @author	: sangylee
	 * @date	: 2019. 7. 19.
	 * @method	: getObjectList
	 * @param reqMap
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/getObjectList")
	public Map<String,Object> getObjectList(@RequestBody Map<String, Object> reqMap) {
		Map<String,Object> map = new HashMap<>();
		try {
			List<RevisionData> list = CommonHelper.manager.getObjectList(reqMap);
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
	 * @desc	: 객체 추가 검색
	 * @author	: sangylee
	 * @date	: 2019. 7. 18.
	 * @method	: searchObjectAction
	 * @param reqMap
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/searchObjectAction")
	public Map<String,Object> searchObjectAction(@RequestBody Map<String, Object> reqMap) {
		Map<String,Object> map = new HashMap<>();
		try {
			List<RevisionData> list = CommonHelper.manager.searchObjectAction(reqMap);
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
	 * @desc	: 객체 검색 팝업
	 * @author	: sangylee
	 * @date	: 2019. 7. 19.
	 * @method	: searchObjectPopup
	 * @param reqMap
	 * @return ModelAndView
	 */
	@RequestMapping("/searchObjectPopup")
	public ModelAndView searchObjectPopup(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String type = StringUtil.checkReplaceStr((String) reqMap.get("type"), "multi");
		String objType = StringUtil.checkNull((String) reqMap.get("objType"));
		String pageName = StringUtil.checkNull((String) reqMap.get("pageName"));
		String pageType = StringUtil.checkNull((String) reqMap.get("pageType"));
		String moduleType = StringUtil.checkNull((String) reqMap.get("moduleType"));
		String rowId = StringUtil.checkNull((String) reqMap.get("rowId"));
		
//		String aoid = StringUtil.checkNull((String) reqMap.get("aoid"));
//		String activeLinkType = StringUtil.checkNull((String) reqMap.get("activeLinkType"));
//		String activeLinkOid = StringUtil.checkNull((String) reqMap.get("activeLinkOid"));
		
		model.addObject("type", type);
		model.addObject("pageName", pageName);
		model.addObject("pageType", pageType);
		model.addObject("moduleType", moduleType);
		model.addObject("rowId", rowId);
//		model.addObject("activeLinkType", activeLinkType);
//		model.addObject("activeLinkOid", activeLinkOid);
		
		if("doc".equals(objType)) {
			model.setViewName("popup:/doc/searchDocPopup");
		} else if("part".equals(objType)) {
			model.setViewName("popup:/part/searchPartPopup");
		} else if("epm".equals(objType)) {
			model.setViewName("popup:/epm/searchEpmPopup");
		} else if("project".equals(objType)) {
			model.setViewName("popup:/change/include2/searchProjectPopup");
		} else if("ecr".equals(objType)) {
			model.setViewName("popup:/change/include2/searchEcrPopup");
		}
	
		
		return model;
	}
	
	@RequestMapping("/include_attributes")
	public ModelAndView attributes(@RequestParam Map<String, Object> reqMap) throws Exception {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String module = StringUtil.checkNull((String) reqMap.get("module"));
		
		Map<String, Object> map = CommonHelper.manager.getAttributes(oid);
		
		model.addObject("oid", oid);
		model.addObject("module", module);
		model.addObject("attributes", map);
		
		model.setViewName("include:/common/include/attributes");
		
		return model;
	}
	
	/**
	 * 
	 * @desc	: 결재 Master 상태 리스트
	 * @author	: tsuam
	 * @date	: 2019. 7. 30.
	 * @method	: getApprovalStateList
	 * @return	: Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getApprovalStateList")
	public Map<String, Object> getApprovalStateList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			List<Map<String, String>> list = ApprovalUtil.getMasterState();
			
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
	 * 
	 * @desc	: 결재 객체 타입 정의
	 * @author	: tsuam
	 * @date	: 2019. 7. 30.
	 * @method	: getApprovalObjectTypeList
	 * @return	: Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getApprovalObjectTypeList")
	public Map<String, Object> getApprovalObjectTypeList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			List<Map<String, String>> list = ApprovalUtil.getObjectTypeList();
			
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
	@RequestMapping("/getMultiApprovalObjectTypeList")
	public Map<String, Object> getMultiApprovalObjectTypeList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			List<Map<String, String>> list = ApprovalUtil.getMultiObjectTypeList();
			
			map.put("list", list);
			map.put("result", true);
		} catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/include_smartEditor2")
	public ModelAndView smartEditor2(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		
		if(oid.length() > 0) {
			Persistable per = CommonUtil.getObject(oid);
			
			if(per instanceof Notice) {
				String contents = StringUtil.checkNull((String) ((Notice) per).getContents());
				
				model.addObject("contents", contents);
			}
		}
		
		model.addObject("oid", oid);
		
		model.setViewName("include:/component/smartEditor2/SmartEditor2");
		
		return model;
	}
	
	/**
	 * @desc	: 관련 Object 검색 액션
	 * @author	: mnyu
	 * @date	: 2019. 9. 5.
	 * @method	: searchRelatedObject
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/searchRelatedObject")
	public Map<String,Object> searchRelatedObject(@RequestBody Map<String, Object> reqMap) {
		Map<String,Object> map = new HashMap<>();
		try {
			List<RevisionData> list = CommonHelper.manager.getSearchRelatedObject(reqMap);
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
	 * @desc : 넘버 코드 리스트
	 * @author : sangylee
	 * @date : 2019. 9. 4.
	 * @method : getNumberCodeList
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/getNumberCodeList")
	public Map<String, Object> getNumberCodeList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			List<Map<String, String>> list = NumberCodeUtil.getCodeList(reqMap);
			
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
	@RequestMapping("/getNumberCode")
	public Map<String, Object> getNumberCode(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			String code = StringUtil.checkNull((String) reqMap.get("code"));
			String codeType = StringUtil.checkNull((String) reqMap.get("codeType"));
			
			NumberCode numberCode = CodeHelper.manager.getNumberCode(codeType, code);
			
			NumberCodeData data = null;
			if(numberCode != null) {
				data = new NumberCodeData(numberCode);
			}
			
			map.put("code", data);
			map.put("result", true);
		} catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	/**
	 * @desc : NumberCode 자동완성
	 * @author : sangylee
	 * @date : 2019. 9. 9.
	 * @method : getNumberCodeListAutoComplete
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/getNumberCodeListAutoComplete")
	public Map<String, Object> getNumberCodeListAutoComplete(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			List<NumberCodeData> list = CodeHelper.manager.getNumberCodeListAutoComplete(reqMap);
			
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
	 * @desc : NumberCode 검색 팝업
	 * @author : sangylee
	 * @date : 2019. 9. 9.
	 * @method : openCodePopup
	 * @return : ModelAndView
	 * @param reqMap
	 */
	@RequestMapping("/openCodePopup")
	public ModelAndView openCodePopup(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String type = StringUtil.checkReplaceStr((String) reqMap.get("type"), "single");
		String codeType = StringUtil.checkNull((String) reqMap.get("codeType"));
		String code = StringUtil.checkNull((String) reqMap.get("code"));
		String id = StringUtil.checkNull((String) reqMap.get("id"));
		String roleCode = StringUtil.checkNull((String) reqMap.get("roleCode"));
		
		NumberCodeType nCodeType = NumberCodeType.toNumberCodeType(codeType);
		
		model.addObject("type", type);
		model.addObject("id", id);
		model.addObject("codeType", codeType);
		model.addObject("codeTypeName", nCodeType.getDisplay());
		model.addObject("code", code);
		model.addObject("isTree", nCodeType.getAbbreviatedDisplay());
		model.addObject("roleCode", roleCode);
		
		model.setViewName("popup:/common/searchCodePopup");
		
		return model;
	}
	
	/**
	 * @desc : 넘버코드 추가 include
	 * @author : sangylee
	 * @date : 2019. 9. 26.
	 * @method : addNumberCode
	 * @return : ModelAndView
	 * @param reqMap
	 * @return
	 */
	@RequestMapping("/include_addNumberCode")
	public ModelAndView addNumberCode(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String linkTableName = StringUtil.checkNull((String) reqMap.get("linkTableName"));
		String type = StringUtil.checkReplaceStr((String) reqMap.get("type"), "multi");
		String endLevel = StringUtil.checkNull((String) reqMap.get("endLevel"));
		String title = StringUtil.checkNull((String) reqMap.get("title"));
		String codeType = StringUtil.checkNull((String) reqMap.get("codeType"));
		String autoGridHeight = StringUtil.checkReplaceStr((String) reqMap.get("autoGridHeight"), "false");
		String gridHeight = StringUtil.checkReplaceStr((String) reqMap.get("gridHeight"), "55");
		
		if("single".equals(type)) {
			autoGridHeight = "true";
			gridHeight = "55";
		}
		
		model.addObject("type", type);
		model.addObject("oid", oid);
		model.addObject("linkTableName", linkTableName);
		model.addObject("endLevel", endLevel);
		model.addObject("title", title);
		model.addObject("codeType", codeType);
		model.addObject("autoGridHeight", autoGridHeight);
		model.addObject("gridHeight", gridHeight);
		
		model.setViewName("include:/common/include/addNumberCode");
		
		return model;
	}
	
	/**
	 * @desc : 넘버코드 리스트 뷰
	 * @author : sangylee
	 * @date : 2019. 9. 30.
	 * @method : numberCodeList
	 * @return : ModelAndView
	 * @param reqMap
	 * @return
	 */
	@RequestMapping("/include_numberCodeList")
	public ModelAndView numberCodeList(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String linkTableName = StringUtil.checkNull((String) reqMap.get("linkTableName"));
		String autoGridHeight = StringUtil.checkReplaceStr((String) reqMap.get("autoGridHeight"), "false");
		String gridHeight = StringUtil.checkReplaceStr((String) reqMap.get("gridHeight"), "55");
		
		model.addObject("oid", oid);
		model.addObject("linkTableName", linkTableName);
		model.addObject("autoGridHeight", autoGridHeight);
		model.addObject("gridHeight", gridHeight);
		
		model.setViewName("include:/common/include/numberCodeList");
		
		return model;
	}
	
	/**
	 * @desc : link table로 연결된 code list 가져오기
	 * @author : sangylee
	 * @date : 2019. 9. 30.
	 * @method : getRelatedCodeList
	 * @return : Map<String,Object>
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getRelatedCodeList")
	public Map<String, Object> getRelatedCodeList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			String linkTableName = StringUtil.checkNull((String) reqMap.get("linkTableName"));
			
			List<NumberCodeData> list = new ArrayList<>();
			
			Persistable per = null;
			if(oid.length() > 0) {
				per = CommonUtil.getObject(oid);
			}
			
			map.put("list", list);
			map.put("result", true);
		} catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/searchDepartmentPopup")
	public ModelAndView searchDepartmentPopup(@RequestParam Map<String, Object> reqMap) throws WTException {
		ModelAndView model = new ModelAndView();
		
		String pageName = StringUtil.checkNull((String) reqMap.get("pageName"));
		String id = StringUtil.checkNull((String) reqMap.get("id"));
		
		String rootLocation = DepartmentHelper.manager.getDepartmentRootLocation();
		
		model.addObject("rootLocation", rootLocation);
		model.addObject("pageName", pageName);
		model.addObject("id", id);
		
		model.setViewName("popup:/common/searchDepartmentPopup");
		
		return model;
	}
	
	@RequestMapping("/searchUserPopup")
	public ModelAndView searchUserPopup(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String id = StringUtil.checkNull((String) reqMap.get("id"));
		String type = StringUtil.checkReplaceStr((String) reqMap.get("type"), "single");
		String except = StringUtil.checkNull((String) reqMap.get("except"));
		
		model.addObject("id", id);
		model.addObject("type", type);
		model.addObject("except", except);
		
		model.setViewName("popup:/common/searchUserPopup");
		
		return model;
	}
	
	/**
	 * @desc	: product, library 가져오기
	 * @author	: mnyu
	 * @date	: 2019. 12. 23.
	 * @method	: getProductLibraryList
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getProductLibraryList")
	public Map<String, Object> getProductLibraryList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			List<Map<String, String>> list = WCUtil.getProductLibraryList();
			map.put("list", list);
			map.put("result", true);
		} catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/include_addObject2")
	public ModelAndView addObject2(@RequestParam Map<String, Object> reqMap) {
		ModelAndView model = new ModelAndView();
		
		String oid = StringUtil.checkNull((String) reqMap.get("oid"));
		String type = StringUtil.checkReplaceStr((String) reqMap.get("type"), "multi");
		String title = StringUtil.checkNull((String) reqMap.get("title"));
		String objType = StringUtil.checkNull((String) reqMap.get("objType"));
		String pageName = StringUtil.checkNull((String) reqMap.get("pageName"));
		String autoGridHeight = StringUtil.checkReplaceStr((String) reqMap.get("autoGridHeight"), "false");
		String gridHeight = StringUtil.checkReplaceStr((String) reqMap.get("gridHeight"), "55");
		String toggle = StringUtil.checkReplaceStr((String) reqMap.get("toggle"), "true");
		
		String moduleType = StringUtil.checkNull((String) reqMap.get("moduleType"));
		
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
		model.addObject("toggle", toggle);
		
		model.setViewName("include:/change/include2/addObject");
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/getSequenceNo")
	public Map<String, Object> getSequenceNo(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		
		try {
			String sequence = CommonHelper.manager.getSequenceNo(reqMap);
			
			map.put("sequence", sequence);
			map.put("result", true);
		}catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
	
	@RequestMapping("/viewFolderTree")
	public ModelAndView viewFolderTree(@RequestParam Map<String, Object> reqMap) {

		ModelAndView model = new ModelAndView();

		String container = StringUtil.checkNull((String) reqMap.get("container"));
		String renderTo = StringUtil.checkNull((String) reqMap.get("renderTo"));
		String formId = StringUtil.checkNull((String) reqMap.get("formId"));
		String locationId = StringUtil.checkReplaceStr((String) reqMap.get("locationId"), "location");
		String rootLocation = StringUtil.checkNull((String) reqMap.get("rootLocation"));
		String location = StringUtil.checkNull((String) reqMap.get("location"));
		String gridWidth = StringUtil.checkReplaceStr((String) reqMap.get("gridWidth"), "230");
		
		model.addObject("container", container);
		model.addObject("renderTo", renderTo);
		model.addObject("formId", formId);
		model.addObject("locationId", locationId);
		model.addObject("rootLocation", rootLocation);
		model.addObject("location", location);
		model.addObject("gridWidth", gridWidth);
		
		model.setViewName("popup:/common/viewFolderTree");
		
		return model;
	}
	
	/**
	 * @desc	: 2 Level 넘버코드 리스트 가져오기
	 * @author	: shkim
	 * @date	: 2020. 9. 17.
	 * @method	: getNumberCodeChildList
	 * @param   : reqMap
	 * @return  : Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getNumberCodeChildList")
	public Map<String, Object> getNumberCodeChildList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			List<Map<String, String>> list = NumberCodeUtil.getCodeChildList(reqMap);
			
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
	 * @desc	: 프로젝트 명 (넘버코드 : PROJECTCODE) 리스트 가져오기
	 * @author	: shkim
	 * @date	: 2020. 9. 17.
	 * @method	: getProjectCodeList
	 * @param   : reqMap
	 * @return  : Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/getProjectCodeList")
	public Map<String, Object> getProjectCodeList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			List<Map<String, String>> list = NumberCodeUtil.getProjectCodeList();
			
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
	 * @desc : 고객사 검색 액션
	 * @author : shjeong
	 * @date : 2027. 7. 28.
	 * @method : searchCustomerListAction
	 * @param reqMap
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/searchCustomerListAction")
	public Map<String, Object> searchCustomerListAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<NumberCodeData> list = CommonHelper.manager.getSearchCustomerListAction(reqMap);
			
			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/getCreateNumberPropCodeList")
	public Map<String, Object> getCreateNumberPropCodeList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			Map<String,Object> list = NumberCodeUtil.getCreateNumberPropCodeList(reqMap);

			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}

		return map;
	}
	
	
	
	
	/**
	 * @desc : 넘버 코드 리스트
	 * @author : sangylee
	 * @date : 2019. 9. 4.
	 * @method : getNumberCodeList
	 * @return : Map<String,Object>
	 * @param reqMap
	 */
	@ResponseBody
	@RequestMapping("/getProjectRoleCodeList")
	public Map<String, Object> getProjectRoleCodeList(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<>();

		try {
			List<Map<String, String>> list = NumberCodeUtil.getProjectRoleCodeList(reqMap);
			
			map.put("list", list);
			map.put("result", true);
		} catch(Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", e.getStackTrace()[0].getMethodName() + " ERROR : " + e.getLocalizedMessage());
		}
		
		return map;
	}
	
}