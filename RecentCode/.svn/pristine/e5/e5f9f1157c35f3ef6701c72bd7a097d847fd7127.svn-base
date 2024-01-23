/**
 * @클래스명 : ContentController.java
 * @작성자 : TaeSik, Eom
 * @작성일 : 2017. 12. 28
 * @설명 :
 * @수정이력 - 수정일,수정자,수정내용
 */
package com.e3ps.common.content.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.e3ps.common.drm.DRMService;
import com.e3ps.common.drm.E3PSDRMHelper;
import com.e3ps.common.history.service.HistoryHelper;
import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.FTPUtil;
import com.e3ps.common.util.StringUtil;
import com.ptc.wvs.server.util.PublishUtils;

import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.content.ContentItem;
import wt.content.ContentRoleType;
import wt.content.ContentServerHelper;
import wt.content.FormatContentHolder;
import wt.epm.EPMDocument;
import wt.fc.QueryResult;
import wt.fv.uploadtocache.UploadToCacheHelper;
import wt.org.WTUser;
import wt.representation.Representation;
import wt.session.SessionHelper;
import wt.util.WTException;
import wt.util.WTProperties;
import wt.util.WTPropertyVetoException;

/**
 * @클래스명 : ContentController.java
 * @작성자 : TaeSik, Eom
 * @작성일 : 2017. 12. 28
 * @설명 :
 * @수정이력 - 수정일,수정자,수정내용
 */

@Controller("ContentController")
@RequestMapping("/content")
public class ContentController {
	public static String orgFileTempFolder = ConfigImpl.getInstance().getString("drm.api.orgFileFolder");
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());

	/**
	 * 파일 업로드 컴포넌트 로드 (CREATE, UPDATE)
	 * 
	 * @param reqMap
	 * @return
	 * @throws WTException
	 * @메소드명 :
	 * @작성자 : TaeSik, Eom
	 * @작성일 : 2017. 12. 28
	 * @설명 :
	 * @수정이력 - 수정일,수정자,수정내용
	 */
	@RequestMapping("/include_fileAttach")
	public ModelAndView include_fileAttach(@RequestParam Map<String, String> reqMap) throws WTException {
		// LOGGER.debug("==================== fileAttach ==========================");
		
		String btnId = StringUtil.checkReplaceStr(reqMap.get("btnId"), "none");
		String location = StringUtil.checkReplaceStr(SessionHelper.getLocale().toString().toUpperCase(), "KO").substring(0, 2);
		String type = StringUtil.checkReplaceStr(reqMap.get("type"), "SECONDARY").toUpperCase();
		
		String oid = StringUtil.checkNull(reqMap.get("oid"));
		String description = StringUtil.checkNull(reqMap.get("description"));
		String formId = StringUtil.checkNull(reqMap.get("formId"));
		String savePath = null;
		String fileType = StringUtil.checkNull(reqMap.get("fileType"));
		String dropHeight = StringUtil.checkReplaceStr(reqMap.get("dropHeight"), "150"); // 파일 업로드 높이
		String fileCount = StringUtil.checkReplaceStr(reqMap.get("fileCount"), "0"); // 파일 업로드 높이

		String moduleType = StringUtil.checkNull(reqMap.get("moduleType"));
		try {
			savePath = WTProperties.getServerProperties().getProperty("wt.temp");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String m_fileFullPath = null;

		String url = UploadToCacheHelper.service.getUploadToCacheURL();
		URL hostUrl;
		try {
			hostUrl = new URL(url);
			url = "http://" + hostUrl.getHost();

		} catch (MalformedURLException e) {
			throw new WTException(e);
		}

		ContentRoleType roleType = ContentRoleType.toContentRoleType(type);

		ContentHolder holder = null;
		QueryResult result = new QueryResult();

		if (oid != null && !oid.isEmpty()) {
			holder = (ContentHolder) CommonUtil.getPersistable(oid);
			result = wt.content.ContentHelper.service.getContentsByRole(holder, roleType);
		}

		StringBuffer uploadedList = new StringBuffer();

//		if (ContentRoleType.SECONDARY == roleType) {
		uploadedList.append("[");
//		}

		List<ContentItem> items = new ArrayList<>();
		while (result.hasMoreElements()) {
			ContentItem item = (ContentItem) result.nextElement();

			if (StringUtil.checkString(description)) {
				if (description.equals(item.getDescription())) {
					items.add(item);
				}
			} else {
				if (item.getDescription() == null || "".equals(item.getDescription())) {
					items.add(item);
				}
			}
		}
		int cnt = 0;
		for (ContentItem item : items) {
			if (cnt != 0) {
				uploadedList.append(",");
			}
			ApplicationData appData = (ApplicationData) item;

			String delocId = item.getPersistInfo().getObjectIdentifier().toString();

			URL durl = ContentHelper.service.getDownloadURL(holder, appData);
			String fileName = appData.getFileName();
			fileName = fileName.replaceAll("'", "\\\\'");
			m_fileFullPath = savePath + "\\" + fileName;

			long fileSize = appData.getFileSize();
			String fileExtType = fileName.split(".").length > 1 ? fileName.split(".")[1] : "";
			uploadedList.append("{");
			uploadedList.append("id : '" + type + description + cnt + "', ");
			uploadedList.append("name : '" + fileName + "', ");
			uploadedList.append("type : '" + fileExtType + "', ");
			uploadedList.append("saveName : '" + fileName + "', ");
			uploadedList.append("fileSize : '" + fileSize + "', ");
			uploadedList.append(
					"uploadedPath : 'servlet/AttachmentsDownloadDirectionServlet?oid=" + CommonUtil.getOIDString(holder)
							+ "&cioids=" + CommonUtil.getOIDString(appData) + "&role=" + type + "', ");
			uploadedList.append("thumbUrl : '" + durl.toString() + "', ");
			// replaceAll ( "(? <! ^) (\\\\ | /) {2,}", Matcher.quoteReplacement
			// (File.separator))
			uploadedList.append("roleType : '" + type + "', ");
			uploadedList.append("formId : '" + formId + "', ");
			uploadedList.append(description + "delocId : '" + delocId + "', ");
			uploadedList.append("description : '" + description + "', ");
			uploadedList.append("cacheId : '' ");
			uploadedList.append("}");
			cnt++;
		}

//		if (ContentRoleType.SECONDARY == roleType) {
		uploadedList.append("]");
//		}

		WTUser user = (WTUser) SessionHelper.manager.getPrincipal();
		String userName = user.getName();
		String componentName = type + "_" + description;
		ModelAndView model = new ModelAndView();
		model.setViewName("include:/common/include/content/fileAttach");
		model.addObject("userName", userName);
		model.addObject("location", location);
		model.addObject("type", type);
		model.addObject("uploadedList", uploadedList.toString());
		model.addObject("description", description);
		model.addObject("formId", formId);
		model.addObject("componentName", componentName);
		model.addObject("m_fileFullPath", m_fileFullPath);
		model.addObject("url", url);
		model.addObject("btnId", btnId);
		model.addObject("fileType", fileType);
		model.addObject("dropHeight", dropHeight);
		model.addObject("fileCount", fileCount);
		model.addObject("moduleType", moduleType);
		return model;
	}

	/**
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @메소드명 :
	 * @작성자 : TaeSik, Eom
	 * @작성일 : 2018. 01. 22
	 * @설명 : 파일 View
	 * @수정이력 - 수정일,수정자,수정내용
	 */
	@RequestMapping("/include_fileView")
	public ModelAndView fileViewAttach(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String oid = request.getParameter("oid");
		String type = StringUtil.checkReplaceStr(request.getParameter("type"), "SECONDARY").toUpperCase();
		String description = request.getParameter("description");
		String fileType = request.getParameter("fileType");

		boolean primary = false;
		boolean isAddRole = false;
		if ("PRIMARY".equals(type.toUpperCase())) {
			primary = true;
		}

		Object obj = CommonUtil.getPersistable(oid);
		ContentHolder holder = ContentHelper.service.getContents((ContentHolder) obj);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		if ("ADDITIONAL_PV".equals(type)) {

			if (obj instanceof EPMDocument) {
				EPMDocument epm = (EPMDocument) obj;
				Representation representation = PublishUtils.getRepresentation(epm);
				representation = (Representation)ContentHelper.service.getContents(representation);

				@SuppressWarnings("rawtypes")
				Vector contentList = ContentHelper.getContentList(representation);

				String[] pvCondition = fileType.split(" ");

				for (Object content : contentList) {

					if (content instanceof ApplicationData) {
						ApplicationData data = (ApplicationData) content;

						String pvExt = CommonUtil.getExtension(data.getFileName()).toUpperCase();
						if (ContentRoleType.ADDITIONAL_FILES.toString().equals(data.getRole().toString()) && Arrays.stream(pvCondition).anyMatch(item -> pvExt.equals(item.toUpperCase()))) {
							
							Map<String, Object> map = new HashMap<String, Object>();
							
							String fileName = data.getFileName();
							fileName = fileName.startsWith(pvExt.toLowerCase()+"_") ? fileName.substring(fileName.indexOf(pvExt.toLowerCase()+"_")+pvExt.length()+1) : fileName;
							
							map.put("name", fileName);
							map.put("oid", data.getPersistInfo().getObjectIdentifier().toString());
							map.put("size", String.valueOf(data.getFileSize()));
							// http://plmdev.dnc-corp.com/Windchill/worldex/content/publishDownload?appOid=wt.content.ApplicationData:678070
							map.put("url", CommonUtil.getURLString("/content/publishDownload") + "?appOid="
									+ CommonUtil.getOIDString(data));
							map.put("uploadedFromPath", "");
							list.add(map);
							
						}
					}
				}
			}

		} else {

			ContentRoleType roleType = primary ? ContentRoleType.PRIMARY : ContentRoleType.SECONDARY;
			if (isAddRole) {
				roleType = ContentRoleType.toContentRoleType(type);
			}
			QueryResult qr = ContentHelper.service.getContentsByRole(holder, roleType);

			while (qr.hasMoreElements()) {
				ContentItem item = (ContentItem) qr.nextElement();

				if (item != null) {

					ApplicationData data = (ApplicationData) item;
					URL viewUrl = ContentHelper.getDownloadURL(holder, data);// data.getViewContentURL(holder);
					// String url =
					// "/Windchill/extcore/pmx/jsp/common/content/DownloadGW.jsp?holderOid="
					// + CommonUtil.getOIDString(holder) + "&appOid=" +
					// CommonUtil.getOIDString(data);

					String url = CommonUtil.getURLString("/content/fileDownload") + "?holderOid="
							+ CommonUtil.getOIDString(holder) + "&appOid=" + CommonUtil.getOIDString(data);

					Map<String, Object> map = new HashMap<String, Object>();

					String fileName = data.getFileName();
					if (fileName.indexOf("{$CAD_NAME}") > -1) {
						if (holder instanceof EPMDocument) {
							fileName = fileName.replace("{$CAD_NAME}", ((EPMDocument) holder).getCADName());
						}
					}

					map.put("name", fileName);
					map.put("oid", data.getPersistInfo().getObjectIdentifier().toString());
					map.put("size", String.valueOf(data.getFileSize()));
					map.put("url", url);
					map.put("uploadedFromPath", viewUrl.toString());

					if (StringUtil.checkString(description)) {
						if (description.equals(item.getDescription())) {
							list.add(map);
						}
					} else {
						if (item.getDescription() == null ||  "".equals(item.getDescription())) {
							list.add(map);
						}
					}
				}
			}

		}

		ModelAndView model = new ModelAndView();
		model.addObject("list", list);
		model.addObject("fileType", fileType);
		model.setViewName("include:/common/include/content/fileView");
		return model;
	}

	@RequestMapping("/fileDownload")
	public ModelAndView fileDownload(@RequestParam Map<String, String> reqMap, HttpServletRequest req, HttpServletResponse res) throws Exception {

		ModelAndView model = new ModelAndView();

		String holderOid = StringUtil.checkNull(reqMap.get("holderOid"));
		String appOid = StringUtil.checkNull(reqMap.get("appOid"));
		String fileName = StringUtil.checkNull(reqMap.get("fileName"));
		String originalFileName = StringUtil.checkNull(reqMap.get("originalFileName"));
		String type = StringUtil.checkNull(reqMap.get("type"));
		String ip = req.getRemoteAddr();
		
		FileOutputStream output = null;
		try {

			String downloadURL = "";

			if (appOid.length() > 0) {

				ContentHolder holder = (ContentHolder) CommonUtil.getObject(holderOid);
				ApplicationData appData = (ApplicationData) CommonUtil.getObject(appOid);

				if ("{$CAD_NAME}".equals(appData.getFileName())) {

					EPMDocument epm = (EPMDocument) holder;

					try {

						appData.setFileName(epm.getCADName());

					} catch (WTPropertyVetoException e) {
						e.printStackTrace();
						throw new WTException(e.getLocalizedMessage());
					}
				}
				WTUser user = (WTUser) SessionHelper.getPrincipal();
				
				boolean enableDRMCheck = ConfigImpl.getInstance().getBoolean("drm.enable", false);
				if (enableDRMCheck) {

					File f = DRMService.download(holder, appData, req, res);
					String tmpUrl = ConfigImpl.getInstance().getString("drm.api.tmpUrl");
					
					String fff = f.getName();
//					byte[] bytes = fff.getBytes("euc-kr");
//					fff = new String(bytes);
					
					String encodedParam = URLEncoder.encode(fff, "UTF-8");
					encodedParam = encodedParam.replaceAll("\\+", "%20");
					RedirectView rv = new RedirectView(tmpUrl + encodedParam);
					model.setView(rv);	
				} else {
					// URL dURL = ContentHelper.getDownloadURL(holder, appData);
					URL dURL = ContentHelper.service.getDownloadURL(holder, appData);

					downloadURL = dURL.toString();

					int index = downloadURL.indexOf("/Windchill");
					downloadURL = downloadURL.substring(index);
					
					RedirectView rv = new RedirectView(downloadURL);
					model.setView(rv);
				}

				// 다운로드 이력 생성
				Map<String, String> map = new HashMap<String, String>();

				map.put("fname", appData.getFileName());
				map.put("dOid", holderOid);
				map.put("userId", user.getName());
				map.put("ip", ip);

				HistoryHelper.service.createDownloadHistory(map);
			} else {
				model.addObject("type", type);
				model.addObject("fileName", fileName);
				model.addObject("originalFileName", originalFileName);

				downloadURL = CommonUtil.getURLString("/content/serverFileDownload");

				RedirectView rv = new RedirectView(downloadURL);
				model.setView(rv);
			}
		} catch (WTException e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				// output.close();
			}
		}

		return model;
	}

	/**
	 * <pre>
	 * &#64;description 파일 다운로드 처리
	 * &#64;author dhkim
	 * &#64;date 2016. 5. 24. 오후 2:25:10
	 * &#64;method serverFileDownload
	 * &#64;param reqMap
	 * &#64;param response
	 * </pre>
	 */
	@RequestMapping("/serverFileDownload")
	public void serverFileDownload(@RequestParam Map<String, String> reqMap, HttpServletResponse response) {

		String fileName = StringUtil.checkNull(reqMap.get("fileName"));
		String originalFileName = StringUtil.checkNull(reqMap.get("originalFileName"));
		String type = StringUtil.checkNull(reqMap.get("type"));
		
		System.out.println("--------serverFileDown-----------");
		System.out.println("originalFileName ::: " + originalFileName);
		System.out.println("type ::: " + type);

		try {
			String filePath = CommonUtil.getURLString("/content/fildDownload/" + fileName);

			if (originalFileName.trim().isEmpty()) {
				originalFileName = fileName;
			}

			// MIME Type 을 application/octet-stream 타입으로 변경
			// 무조건 팝업(다운로드창)이 뜨게 된다.
			response.setContentType("application/octet-stream");

			originalFileName = new String(originalFileName.getBytes("UTF-8"), "ISO-8859-1");

			response.setHeader("Content-Disposition", "attachment;filename=\"" + originalFileName + "\";");

			ServletOutputStream os = response.getOutputStream();

			FileInputStream fis = new FileInputStream(filePath);
			
			int n = 0;
			byte[] b = new byte[512];

			while ((n = fis.read(b)) != -1) {
				os.write(b, 0, n);
			}

			fis.close();
			os.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/cpcFileDownload")
	public void cpcFileDownload(@RequestParam Map<String, String> reqMap, HttpServletRequest request,
			HttpServletResponse response) {

//		String instance_id = StringUtil.checkNull(reqMap.get("instance_id"));
//		String fileName = StringUtil.checkNull(reqMap.get("fileName"));
//
//		try {
//
//			String location = CPCHelper.manager.getAttachFileLocation(null, instance_id);
//			if (location.length() > 0) {
//				location = location.replace("D:\\CPC_FILE", "").replace("/" + fileName, "");
//			}
//
//			File file = FTPUtil.manager.dowonloadFile(location, fileName);
//			String filePath = file.getPath();
//
//			String browser = request.getHeader("User-Agent");
//			// 파일 인코딩
//			if (browser.contains("MSIE") || browser.contains("Trident") || browser.contains("Chrome")) {
//				fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
//			} else {
//				fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
//			}
//
//			// MIME Type 을 application/octet-stream 타입으로 변경
//			// 무조건 팝업(다운로드창)이 뜨게 된다.
//			response.setContentType("application/octet-stream");
//
//			response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\";");
//
//			ServletOutputStream os = response.getOutputStream();
//
//			FileInputStream fis = new FileInputStream(filePath);
//			int n = 0;
//			byte[] b = new byte[512];
//
//			while ((n = fis.read(b)) != -1) {
//				os.write(b, 0, n);
//			}
//
//			fis.close();
//			os.close();
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	@RequestMapping("/publishDownload")
	public void publishDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			LOGGER.debug("publishDown tempDir = ");

			String appOid = (String) request.getParameter("appOid");

			ApplicationData adata = (ApplicationData) CommonUtil.getObject(appOid);

			// MIME Type 을 application/octet-stream 타입으로 변경
			// 무조건 팝업(다운로드창)이 뜨게 된다.
			response.setContentType("application/octet-stream");

			String fileName = adata.getFileName();
			String pvExt = CommonUtil.getExtension(fileName).toLowerCase();
			fileName = fileName.startsWith(pvExt+"_") ? fileName.substring(fileName.indexOf(pvExt+"_")+pvExt.length()+1) : fileName;
			
			response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\";");

			ServletOutputStream os = response.getOutputStream();

			byte[] buffer = new byte[1024];
			InputStream is = null;

			is = ContentServerHelper.service.findLocalContentStream(adata);

			int j = 0;
			while ((j = is.read(buffer, 0, 1024)) > 0) {

				os.write(buffer, 0, j);
			}

			os.close();
			is.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
