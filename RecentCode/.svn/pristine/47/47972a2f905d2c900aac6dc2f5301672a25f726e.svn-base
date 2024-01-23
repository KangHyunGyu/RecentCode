package com.e3ps.common.content.service;

import java.io.File;
import java.util.List;
import java.util.Vector;

import com.e3ps.common.content.uploader.WBFile;

import wt.content.ApplicationData;
import wt.content.ContentHolder;
import wt.content.ContentItem;
import wt.content.ContentRoleType;
import wt.fv.uploadtocache.CachedContentDescriptor;
import wt.util.WTException;

public interface CommonContentService {

    ContentHolder attachPrimary(ContentHolder holder, String loc) throws Exception;

    ContentHolder attach(ContentHolder holder, String loc) throws Exception;

    ContentHolder attach(ContentHolder holder, String loc, String desc) throws Exception;

    ContentHolder attach(ContentHolder holder, String loc, String desc, ContentRoleType contentRoleType)
	    throws Exception;

    ContentHolder delete(ContentHolder holder, ContentItem deleteItem) throws Exception;

    ContentHolder delete(ContentHolder holder) throws Exception;

    ContentHolder attach(ContentHolder holder, ApplicationData data, boolean isPrimary) throws Exception;

    ContentHolder attach(ContentHolder holder, ApplicationData data, ContentRoleType contentRoleType) throws Exception;

    ContentHolder attach(ContentHolder holder, CachedContentDescriptor casheDes, String fileName, String desc,
	    ContentRoleType contentRoleType) throws Exception;

    ContentHolder attach(ContentHolder holder, String primary, List<String> secondary) throws Exception;

    ContentHolder attach(ContentHolder holder, String primary, List<String> secondary, List<String> delSecondary)
	    throws Exception;

    ContentHolder attach(ContentHolder holder, String primary, List<String> secondary, List<String> delSecondary,
	    boolean isWorkingCopy) throws Exception;

    ApplicationData attachADDRole(ContentHolder holder, String roleType, String cachFile, boolean isWorkingCopy)
	    throws Exception;

    ApplicationData attachROHS(ContentHolder holder, CachedContentDescriptor casheDes, String fileName, String desc,
	    ContentRoleType contentRoleType) throws Exception;

    String copyApplicationData(String appOid) throws Exception;

    ContentHolder attach(ContentHolder holder, String primary, List<String> secondary, List<String> delSecondary,
	    String description, boolean isWorkingCopy) throws Exception;

    void fileDown(String appOid) throws Exception;

    List<ApplicationData> getAttachFileList(ContentHolder holder, String contentRoleType, List<ApplicationData> list)
	    throws Exception;

    ApplicationData attachADDRole(ContentHolder holder, String roleType, String cachFile, String delFileName, boolean b)
	    throws Exception;

	ContentHolder attach(ContentHolder paramContentHolder, WBFile paramWBFile, String paramString)
			throws WTException;

	ContentHolder attach(ContentHolder paramContentHolder, WBFile file, String paramString, boolean paramBoolean) throws WTException;

	ContentHolder attach(ContentHolder paramContentHolder, ApplicationData paramApplicationData)
			throws WTException;

	
	ContentHolder attach(ContentHolder paramContentHolder, File paramFile, String paramString,
			ContentRoleType paramContentRoleType) throws WTException;

	ContentHolder attach(ContentHolder paramContentHolder, WBFile paramWBFile, String paramString,
			ContentRoleType paramContentRoleType) throws WTException;

	ContentHolder attachURL(ContentHolder paramContentHolder, String paramString1,
			String paramString2, ContentRoleType paramContentRoleType) throws WTException;


	ContentHolder delete(ContentHolder holder, String pdf) throws Exception;
	
	ContentHolder update(ContentHolder holder, Vector<WBFile> files, Vector<String> oldfiles)
			throws Exception;
	
	ContentHolder delete(ContentHolder holder, ContentRoleType paramContentRoleType) throws Exception;

	void isDataExcludedDelete(ContentHolder var1, ContentRoleType var2, List<String> var3) throws Exception;
}
