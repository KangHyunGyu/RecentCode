<%@page import="java.nio.file.Files"%>
<%@page import="com.e3ps.common.drm.DRMService"%>
<%@page import="com.e3ps.common.message.util.MessageUtil"%>
<%@page import="com.e3ps.common.jdf.config.ConfigImpl"%>
<%@page import="com.e3ps.common.drm.E3PSDRMHelper"%>
<%@page import="org.json.JSONObject"%>
<%@page import="com.e3ps.common.util.StringUtil"%>
<%@page import="com.e3ps.common.content.util.CacheUploadUtil"%>
<%@page import="wt.util.WTProperties"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="wt.session.SessionServerHelper"%>
<%@page import="java.net.URL"%>
<%@page import="wt.org.WTUser"%>
<%@page import="wt.session.SessionHelper"%>
<%@page import="wt.content.ContentHolder"%>
<%@page import="wt.content.ApplicationData"%>
<%@page import="wt.content.ContentServerHelper"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.File"%>
<%@page import="wt.fv.uploadtocache.CachedContentDescriptor"%>
<%@page import="wt.fv.uploadtocache.UploadToCacheHelper"%>
<%@page import="wt.fv.uploadtocache.CacheDescriptor"%>
<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@ page language="java" contentType="text/plain; charset=UTF-8" pageEncoding="UTF-8"%>


<%
try{
	String savePath = WTProperties.getServerProperties().getProperty("wt.temp");
	savePath = savePath+ "\\"+"e3ps";
	
	File tempFolder = new File(savePath);
	if(!tempFolder.exists()){
	    tempFolder.mkdir();
	}
	System.out.println("################1 savePath : "+savePath);
	
	int sizeLimit = (1024*1024*1500);
	
	
	MultipartRequest multi = new MultipartRequest(request, savePath, sizeLimit, "UTF-8", new DefaultFileRenamePolicy());
	
	
	String roleType = multi.getParameter("roleType");
	String originFileName = multi.getOriginalFileName(roleType);
	String fileName = multi.getFilesystemName(roleType);
	
	String fileExtType = multi.getContentType(roleType);
	String userId = multi.getParameter("userId");
	String masterHost = multi.getParameter("masterHost");
	String formId = multi.getParameter("formId");
	String description = multi.getParameter("description");
	String moduleType = StringUtil.checkNull(multi.getParameter("moduleType"));
	
	
	System.out.println("################4 formId : "+formId);
	
	String m_fileFullPath = savePath + "/" + fileName;
	System.out.println("################  originFileName : " + originFileName +" , length = " +originFileName.length() );
	System.out.println("################5 fileName : "+fileName +" , length = " +fileName.length());
	
	    
	
	//CacheUploadUtil.getCacheDescriptor(1, true, userId, masterHost);
	CacheDescriptor cd = CacheUploadUtil.getCacheDescriptor(1, true, userId, masterHost);
	File file = new File(m_fileFullPath);
	
	//복호화
	boolean enableDRMCheck = ConfigImpl.getInstance().getBoolean("drm.enable", true);
	System.out.println("#### drm? boolean : " + enableDRMCheck);
	System.out.println("#### file.getAbsolutePath() : " + file.getAbsolutePath());
	System.out.println("#### file.getAbsolutePath() : " + file.getAbsolutePath());
	if(enableDRMCheck){
		file = DRMService.upload(file, fileName);
	}
	
	/*
	if(enableDRMCheck) {
		file = E3PSDRMHelper.manager.upload(file, fileName);	
	}
	*/
	
	//System.out.println("################ unix new file path ::  " + file.getPath());
	if(file == null) {
		throw new Exception(MessageUtil.getMessage("복호화 실패했습니다."));
	}
	
	InputStream[] streams = new InputStream[1];
	streams[0] = new FileInputStream(file);

	long[] fileSize = new long[1];
	fileSize[0] = file.length();

	String[] paths = new String[1];
	paths[0] = file.getPath();

	//System.out.println("fileSize[0] : " + fileSize[0] +"\n");
	//System.out.println("fileName : " + fileName +"\n");
	//System.out.println("paths[0] : " + paths[0] +"\n");

	ResourceBundle bundle =ResourceBundle.getBundle("wt");
	
	String reqHost = bundle.getString("wt.rmi.server.hostname");
   	boolean isMain = masterHost.indexOf(reqHost) > 0;

    CachedContentDescriptor descriptor =  CacheUploadUtil.doUploadToCache(cd, file, isMain);
    
    //originFileName = originFileName.replaceAll("'", "\\\\'");
    //fileName = fileName.replaceAll("'", "\\\\'");
    
    //file Double check : KMS
    boolean isDoubleCheck = false;
    String isDouble ="";
    
    
    JSONObject jsonReturn = new JSONObject();
	jsonReturn.put("name",originFileName);
	jsonReturn.put("type",fileExtType);
	jsonReturn.put("saveName",file.getName());
	jsonReturn.put("fileSize",fileSize[0]+"");
	jsonReturn.put("uploadedPath",file.getAbsolutePath());
	jsonReturn.put("thumbUrl","");
	jsonReturn.put("roleType",roleType);
	jsonReturn.put("formId",formId);
	jsonReturn.put("description",description);
	jsonReturn.put("cacheId",descriptor.getEncodedCCD());
	jsonReturn.put("isDouble",isDoubleCheck);
	out.println(jsonReturn.toJSONString());
	
}catch(Exception e){
    e.printStackTrace();
}
%>