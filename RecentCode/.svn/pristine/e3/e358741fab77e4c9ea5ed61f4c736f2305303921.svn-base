package com.e3ps.common.content.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;

import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.content.ContentItem;
import wt.content.ContentRoleType;
import wt.content.ContentServerHelper;
import wt.fc.QueryResult;
import wt.log4j.LogR;
import wt.services.ServiceFactory;

public class CommonContentHelper {
    public static final CommonContentService service = ServiceFactory.getService(CommonContentService.class);
    public static CommonContentHelper manager = new CommonContentHelper();

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());

    public List<Map<String, String>> getUploadFileList(Map<String, String> paramMap) throws Exception {

	String oid = paramMap.get("oid");
	String type = paramMap.get("type");
	String description = paramMap.get("description");
	String fileType = paramMap.get("fileType");

	boolean primary = false;
	boolean isAddRole = false;
	if (StringUtil.checkString(type) && "P".equals(type.toUpperCase())) {
	    primary = true;
	}
	if (StringUtil.checkString(type) && (type.equals("ECR") || type.equals("ECO"))) {
	    isAddRole = true;
	}

	Object obj = CommonUtil.getObject(oid);
	ContentHolder holder = ContentHelper.service.getContents((ContentHolder) obj);
	ContentRoleType roleType = primary ? ContentRoleType.PRIMARY : ContentRoleType.SECONDARY;
	if (isAddRole) {

	    roleType = ContentRoleType.toContentRoleType(type);
	}
	QueryResult qr = ContentHelper.service.getContentsByRole(holder, roleType);

	List<Map<String, String>> list = new ArrayList<Map<String, String>>();

	String holderOid = CommonUtil.getOIDString(holder);
	String appOid = null;
	while (qr.hasMoreElements()) {
	    ContentItem item = (ContentItem) qr.nextElement();

	    if (item != null) {

		ApplicationData data = (ApplicationData) item;

		appOid = CommonUtil.getOIDString(data);
		String url = "/Windchill/extcore/pmx/jsp/common/content/DownloadGW.jsp?holderOid=" + holderOid + "&appOid=" + appOid;

		Map<String, String> map = new HashMap<>();
		// String profileImageData = FileUtil.getImgBase64String(data);
		URL durl = ContentHelper.service.getDownloadURL(holder, data);
		map.put("name", data.getFileName());
		map.put("oid", data.getPersistInfo().getObjectIdentifier().toString());
		map.put("size", String.valueOf(data.getFileSize()));
		map.put("url", url);
		map.put("uploadedFromPath", durl.toString());
		map.put("holderOid", holderOid);
		map.put("appOid", appOid);

		if (StringUtil.checkString(description)) {
		    if (description.equals(item.getDescription())) {
			list.add(map);
		    }
		} else {
		    if (item.getDescription() == null) {
			list.add(map);
		    }
		}
	    }
	}

	return list;

    }

    public List<Map<String, String>> getAttatchFileList(String oid, String roletype) throws Exception {

	Object obj = CommonUtil.getObject(oid);
	ContentHolder holder = ContentHelper.service.getContents((ContentHolder) obj);
	ContentRoleType roleType = ContentRoleType.toContentRoleType(roletype);
	QueryResult qr = ContentHelper.service.getContentsByRole(holder, roleType);

	List<Map<String, String>> list = new ArrayList<Map<String, String>>();

	while (qr.hasMoreElements()) {
	    ContentItem item = (ContentItem) qr.nextElement();

	    if (item != null) {
		ApplicationData data = (ApplicationData) item;
		String url = "/Windchill/extcore/pmx/jsp/common/content/DownloadGW.jsp?holderOid=" + CommonUtil.getOIDString(holder) + "&appOid=" + CommonUtil.getOIDString(data);
		Map<String, String> map = new HashMap<>();
		URL durl = ContentHelper.service.getDownloadURL(holder, data);
		map.put("name", data.getFileName());
		map.put("oid", data.getPersistInfo().getObjectIdentifier().toString());
		map.put("size", String.valueOf(data.getFileSize()));
		map.put("url", url);
		list.add(map);
	    }
	}

	return list;
    }

    /**
     * @param holder
     * @return
     * @throws Exception
     * @메소드명 :
     * @작성자 : TaeSik, Eom
     * @작성일 : 2018. 03. 15
     * @설명 : Primary 첩부 파일
     * @수정이력 - 수정일,수정자,수정내용
     */
    public ApplicationData getPrimaryAttch(ContentHolder holder) throws Exception {
	ContentItem item = null;
	QueryResult result = ContentHelper.service.getContentsByRole(holder, ContentRoleType.PRIMARY);
	while (result.hasMoreElements()) {
	    item = (ContentItem) result.nextElement();
	}
	ApplicationData pAppData = null;
	if (item != null) {
	    pAppData = (ApplicationData) item;

	}

	return pAppData;
    }

    /**
     * @param oid
     * @return
     * @throws Exception
     * @메소드명 :
     * @작성자 : SangMin, Lee
     * @작성일 : 2018. 03. 19
     * @설명 :
     * @수정이력 - 수정일,수정자,수정내용
     */
    
    
    /**
     * 
     * @param holder
     * @return
     * @throws Exception
     * @메소드명 :
     * @작성자 : SeungJin, Lee
     * @작성일 : 2020. 10. 27
     * @설명 :
     * @수정이력 - 수정일,수정자,수정내용
     */
    public String getPrimaryContentData(ContentHolder holder) throws Exception {
		ApplicationData appData = (ApplicationData) CommonContentHelper.manager.getPrimaryAttch(holder);
		InputStream in = ContentServerHelper.service.findContentStream(appData);
		
		BufferedReader streamReader = new BufferedReader(new InputStreamReader(in));
	    StringBuilder responseStrBuilder = new StringBuilder();

	    String inputStr;
	    while ((inputStr = streamReader.readLine()) != null)
	        responseStrBuilder.append(inputStr);

	    return responseStrBuilder.toString();
	}

}
