package com.e3ps.common.content.service;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.e3ps.common.content.uploader.WBFile;
import com.e3ps.common.content.util.ContentUtil;
//import com.e3ps.common.content.util.CacheUploadUtil;
import com.e3ps.common.jdf.config.Config;
import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.content.ContentItem;
import wt.content.ContentRoleType;
import wt.content.ContentServerHelper;
import wt.content.FormatContentHolder;
import wt.content.StreamData;
import wt.content.Streamed;
import wt.content.URLData;
import wt.fc.LobLocator;
import wt.fc.ObjectReference;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.fv.uploadtocache.CacheDescriptor;
import wt.fv.uploadtocache.CachedContentDescriptor;
import wt.org.WTUser;
import wt.pom.Transaction;
import wt.services.StandardManager;
import wt.session.SessionHelper;
import wt.util.EncodingConverter;
import wt.util.WTException;
import wt.util.WTProperties;
import wt.util.WTPropertyVetoException;

@SuppressWarnings("serial")
public class StandardCommonContentService extends StandardManager implements CommonContentService {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
	public static boolean isAutoDel = false;
	 public static StandardCommonContentService newStandardCommonContentService() throws Exception {
			final StandardCommonContentService instance = new StandardCommonContentService();
			instance.initialize();
			return instance;
		    }

	 @Override
	    public ContentHolder attachPrimary(ContentHolder holder, String loc) throws Exception {
		return attach(holder, loc, null, ContentRoleType.PRIMARY);
	    }

	    @Override
	    public ContentHolder attach(ContentHolder holder, String loc) throws Exception {
		return attach(holder, loc, null, ContentRoleType.SECONDARY);
	    }

	    @Override
	    public ContentHolder attach(ContentHolder holder, String loc, String desc) throws Exception {
		return attach(holder, loc, desc, ContentRoleType.SECONDARY);
	    }

	    // public ContentHolder attach(ContentHolder holder, String loc, String desc, ContentRoleType contentRoleType)
	    // throws Exception {
	    //
	    // String cacheId = loc.split("/")[0];
	    // String fileName = loc.split("/")[1];
	    //
	    // CachedContentDescriptor cacheDs = new CachedContentDescriptor(cacheId);
	    //
	    // holder = CommonContentHelper.service.attach(holder, cacheDs, fileName, desc, contentRoleType);
	    //
	    // return holder;
	    // }

	    @Override
	    public ContentHolder attach(ContentHolder holder, String loc, String desc, ContentRoleType contentRoleType)
		    throws Exception {
		Transaction trx = new Transaction();
		try {
		    trx.start();

		    File file = new File(loc);
		    String downloadFile = "";
		    String newfile = "";
		    ApplicationData applicationdata = ApplicationData.newApplicationData(holder);
		    applicationdata.setFileName(file.getName());
		    applicationdata.setUploadedFromPath(file.getPath());
		    applicationdata.setRole(contentRoleType);
		    applicationdata.setCreatedBy(SessionHelper.manager.getPrincipalReference());
		   

		    if (desc != null) {
			applicationdata.setDescription(desc);
		    }

		    ContentServerHelper.service.updateContent(holder, applicationdata, file.getPath());

		    if (holder instanceof FormatContentHolder) {
			holder = ContentServerHelper.service.updateHolderFormat((FormatContentHolder) holder);
		    }
		    // EpmUtil.deleteFile(file.getPath());
		    trx.commit();
		    trx = null;
		} catch (Exception e) {
		    throw e;
		} finally {
		    if (trx != null) {
			trx.rollback();
		    }
		}
		return holder;
	    }

	    @Override
	    public ContentHolder attach(ContentHolder holder, ApplicationData data, boolean isPrimary) throws Exception {
		if (isPrimary) return attach(holder, data, ContentRoleType.PRIMARY);
		else
		    return attach(holder, data, ContentRoleType.SECONDARY);
	    }

	    @Override
	    public ContentHolder attach(ContentHolder holder, ApplicationData data, ContentRoleType contentRoleType)
		    throws Exception {

		Transaction transaction = new Transaction();
		try {
		    transaction.start();

		    LobLocator loblocator = null;
		    data = (ApplicationData) PersistenceHelper.manager.refresh(data);
		    Streamed streamed = (Streamed) PersistenceHelper.manager.refresh(data.getStreamData().getObjectId());
		    try {
			loblocator.setObjectIdentifier(((ObjectReference) streamed).getObjectId());
			((StreamData) streamed).setLobLoc(loblocator);
		    } catch (Exception exception) {
		    }
		    InputStream is = streamed.retrieveStream();

		    ApplicationData saveData = ApplicationData.newApplicationData(holder);
		    saveData.setIntendedForHttpOp(true);
		    saveData.setFileName(data.getFileName());
		    saveData.setFileSize(data.getFileSize());
		    saveData.setCreatedBy(data.getCreatedBy());
		    saveData.setDescription(data.getDescription() == null ? "" : data.getDescription());
		    saveData.setRole(contentRoleType);

		    ContentServerHelper.service.updateContent(holder, saveData, is);

		    transaction.commit();
		} catch (Exception e) {
		    transaction.rollback();
		    e.printStackTrace();
		} finally {
		    transaction = null;
		}
		return holder;
		// ##end attach%40B6D04600B0.body
	    }

	    @Override
	    public ContentHolder delete(ContentHolder holder, ContentItem deleteItem) throws Exception {

		Transaction trx = new Transaction();
		try {
		    trx.start();

		    if (holder == null || deleteItem == null) return holder;
		    holder = ContentHelper.service.getContents(holder);
		    ContentServerHelper.service.deleteContent(holder, deleteItem);

		    trx.commit();
		    trx = null;
		} catch (Exception e) {
		    throw e;
		} finally {
		    if (trx != null) {
			trx.rollback();
		    }
		}
		return holder;
	    }

	    @Override
	    public ContentHolder delete(ContentHolder holder) throws Exception {

		Transaction trx = new Transaction();
		try {

		    trx.start();

		    holder = ContentHelper.service.getContents(holder);
		    Vector files = ContentHelper.getApplicationData(holder);
		    if (files != null) {
			for (int i = 0; i < files.size(); i++) {
			    holder = delete(holder, (ApplicationData) files.get(i));
			}
		    }
		    trx.commit();
		    trx = null;
		} catch (Exception e) {
		    throw e;
		} finally {
		    if (trx != null) {
			trx.rollback();
		    }
		}
		return holder;
	    }

	    /**
	     * <pre>
	     * &#64;description 업로드 컴포넌트로 파일 추가
	     * &#64;author dhkim
	     * &#64;date 2016. 3. 10. 오후 4:05:06
	     * &#64;method attach
	     * &#64;param holder
	     * &#64;param casheDes
	     * &#64;param fileName
	     * &#64;param desc
	     * &#64;param contentRoleType
	     * &#64;return
	     * &#64;throws Exception
	     * </pre>
	     */
	    @Override
	    public ContentHolder attach(ContentHolder holder, CachedContentDescriptor casheDes, String fileName, String desc,
		    ContentRoleType contentRoleType) throws Exception {

		Transaction trx = new Transaction();
		try {
		    trx.start();
		    ApplicationData applicationdata = ApplicationData.newApplicationData(holder);
		    String uploadPath = casheDes.getContentIdentity();
		    applicationdata.setRole(contentRoleType);
		    applicationdata.setFileName(fileName);
		    applicationdata.setContentIdentity(uploadPath);
//		    applicationdata.setUploadedFromPath(uploadPath);
		    //2022-07-25 uploadPath logic 수정
			String uploadedFromPath = ContentUtil.getUploadPath(casheDes.getEncodedCCD());
		    applicationdata.setUploadedFromPath(uploadedFromPath);

		    applicationdata.setCreatedBy(SessionHelper.manager.getPrincipalReference());

		    if (desc != null) {
			applicationdata.setDescription(desc);
		    }

		    ContentServerHelper.service.updateContent(holder, applicationdata, casheDes);
		    // ContentServerHelper.service.updateContent(holder, applicationdata, uploadPath);

		    if (holder instanceof FormatContentHolder) {
			holder = ContentServerHelper.service.updateHolderFormat((FormatContentHolder) holder);
		    }

		    trx.commit();
		    trx = null;
		} catch (PropertyVetoException e) {
		    throw new WTException(e);
		} finally {
		    if (trx != null) {
			trx.rollback();
			trx = null;
		    }
		}
		return holder;
	    }

	    @Override
	    public ContentHolder attach(ContentHolder holder, String primary, List<String> secondary) throws Exception {
		return attach(holder, primary, secondary, null);
	    }

	    @Override
	    public ContentHolder attach(ContentHolder holder, String primary, List<String> secondary, List<String> orgSecondary)
		    throws Exception {
		return attach(holder, primary, secondary, orgSecondary, true);
	    }

	    @Override
	    public ContentHolder attach(ContentHolder holder, String primary, List<String> secondary, List<String> delSecondary,
		    boolean isWorkingCopy) throws Exception {
		return attach(holder, primary, secondary, delSecondary, null, isWorkingCopy);
	    }

	    @Override
	    public ContentHolder attach(ContentHolder holder, String primary, List<String> secondary, List<String> delSecondary,
		    String description, boolean isWorkingCopy) throws Exception {
	    	/*
	    	 LOGGER.info(">>>>>>>>>> primary = " + primary);
	    	 LOGGER.info(">>>>>>>>>> secondary = " + secondary);
	    	 LOGGER.info(">>>>>>>>>> delSecondary = " + delSecondary);
	    	 LOGGER.info("isWorkingCopy="+(isWorkingCopy));
	    	 */
		// 20161109 PJT EDIT START
			boolean isParse = true;
			try {
			    if (StringUtil.checkString(primary)) {
				String tmp = primary.split("/")[0];
				EncodingConverter localEncodingConverter = new EncodingConverter();
				String str = localEncodingConverter.decode(tmp);
				String[] arrayOfString = str.split(":");
				Long.parseLong(arrayOfString[0]);
			    }
			} catch (NumberFormatException e) {
			    isParse = false;
			}
			
			if (StringUtil.checkString(primary) && isParse) {
			    // 20161109 PJT EDIT END &&isParse ADD
			    ContentItem item = null;
			    QueryResult result = ContentHelper.service.getContentsByRole(holder, ContentRoleType.PRIMARY);
			    if (result.hasMoreElements()) {
				item = (ContentItem) result.nextElement();
				CommonContentHelper.service.delete(holder, item);
			    }
	
			    String cacheId = primary.split("/")[0];
			    String fileName = primary.split("/")[1];
			    //LOGGER.info(">>>>>>>>>> Primary cacheId = " + cacheId);
			    //LOGGER.info(">>>>>>>>>> Primary fileName = " + fileName);
			    CachedContentDescriptor cacheDs = new CachedContentDescriptor(cacheId);
			    //LOGGER.info(">>>>>>>>>>>>>>>> cacheDs =" + cacheDs);
			    CommonContentHelper.service.attach(holder, cacheDs, fileName, description, ContentRoleType.PRIMARY);
	
			}
			
			
			if (isWorkingCopy) {
	
			    CommonContentHelper.service.delete(holder);
	
			    if (delSecondary != null) {
					for (String appOid : delSecondary) {
					    ApplicationData app = (ApplicationData) CommonUtil.getPersistable(appOid);
					    
					    ContentItem contentitem = (ContentItem)app;
					    ContentHelper.service.copyContentItem(holder, contentitem);
					}
	
			    }
			} else {
				
				
				//LOGGER.info("----------------------- 대상 파일  delSecondary >>>>>>>>>>>>>>>>> " + delSecondary);
			    ContentHolder holder2 = ContentHelper.service.getContents(holder);
			    Vector ofiles = ContentHelper.getApplicationData(holder2);
			    if (ofiles != null) {
				for (int i = 0; i < ofiles.size(); i++) {
				    ApplicationData oad = (ApplicationData) ofiles.get(i);
				   // LOGGER.info("----------------------- 대상 파일 >>>>>>>>>>>>>>>>> " + oad.getFileName());
				    boolean flag = false;
				    // LOGGER.info("null!=delSecondary =?"
				    // +(null!=delSecondary)+"\tdelSecondary.length="+delSecondary.length);
				    if (delSecondary.size() > 0) {
						for (int j = 0; j < delSecondary.size(); j++) {
						    String noid = delSecondary.get(j);
						    //LOGGER.info("----------------------- 대상 파일  delSecondary noid >>>>>>>>>>>>>>>>> " + noid);
						    if (noid.equals(oad.getPersistInfo().getObjectIdentifier().toString())) {
							//LOGGER.info("대상 파일 삭제 >>> " + oad.getFileName()+"X");
							flag = true;
							break;
						    }
						}
				    }
				    
				    // LOGGER.info("-----------------------    대상 파일 flag >>>>>>>>>>>>>>>>>  " + flag);
				     
				     if (!flag) {
				    	 /*
						 LOGGER.info( "-----------------------    대상 파일 oad.getRole().toString().equals() >>>>>>>>>>>>>>>>>  "
						  + (oad.getRole().toString().equals("SECONDARY")));
						  LOGGER.info("-----------------------    description == null && oad.getDescription() == null >>>>>>>>>>>>>>>>>  "
						  + (description == null && oad.getDescription() == null));
						 LOGGER.info("-----------------------    description != null && description.equals(oad.getDescription())l >>>>>>>>>>>>>>>>>  "
						 + (description != null && description.equals(oad.getDescription())));
						 LOGGER.info("-----------------------    description !oad.getRole().toString()="
						  + oad.getRole().toString());
					*/
						if (oad.getRole().toString().equals("SECONDARY")) {
						    if (description == null && oad.getDescription() == null) {
							//LOGGER.info("#####################  삭제 목록 >>>>>  " + oad.getFileName()); 
							holder = CommonContentHelper.service.delete(holder, oad);
						    } else if (description != null && description.equals(oad.getDescription())) {
							holder = CommonContentHelper.service.delete(holder, oad);
							//LOGGER.info("#####################  삭제 목록 >>>>>  " + oad.getFileName()); 
						    }
						}
	
				    }
				    
				}
			    }
			}
			//LOGGER.info("----------------------- 대상 파일 secondary >>>>>>>>>>>>>>>>> " + (secondary.size()));
			//LOGGER.info("----------------------- 대상 파일 secondary >>>>>>>>>>>>>>>>> " + secondary);
			if (secondary.size() > 0) {
			    for (int i = 0; i < secondary.size(); i++) {
				String cacheId = secondary.get(i).split("/")[0];
				String fileName = secondary.get(i).split("/")[1];
				
				// LOGGER.info(">>>>>>>>>> Secondary cacheId = " + cacheId);
				 //LOGGER.info(">>>>>>>>>> Secondary fileName = " + fileName);
				
				CachedContentDescriptor cacheDs = new CachedContentDescriptor(cacheId);
				CommonContentHelper.service.attach(holder, cacheDs, fileName, description, ContentRoleType.SECONDARY);
			    }
			}
	
			return holder;
	    }

	    @Override
	    public ApplicationData attachADDRole(ContentHolder holder, String roleType, String cachFile, boolean isWorkingCopy)
		    throws Exception {
		ApplicationData app = null;
		ContentItem item = null;
		ContentRoleType contentroleType = ContentRoleType.toContentRoleType(roleType);

		if (StringUtil.checkString(roleType) && StringUtil.checkString(cachFile)) {

		    QueryResult result = ContentHelper.service.getContentsByRole(holder, contentroleType);
		    String roleContent = cachFile;
		    String cacheId = roleContent.split("/")[0];
		    String fileName = roleContent.split("/")[1];
		    EncodingConverter localEncodingConverter = new EncodingConverter();
		    String str = localEncodingConverter.decode(cacheId);
		    String[] arrayOfString = str.split(":");
		    boolean isParse = true;
		    try {
			long streamId = Long.parseLong(arrayOfString[0]);
			long fileSize = Long.parseLong(arrayOfString[1]);
			long folderId = Long.parseLong(arrayOfString[2]);
		    } catch (NumberFormatException nfe) {
			isParse = false;
		    }
		    /* LOGGER.info("isParse=" + isParse); */
		    if (result.hasMoreElements()) {
			item = (ContentItem) result.nextElement();
			ApplicationData appData = (ApplicationData) item;
			String apFileName = appData.getFileName();
			/* LOGGER.info("fileName=" + fileName + "\tapFileName=" + apFileName); */
			if (!"ECO".equals(fileName)) CommonContentHelper.service.delete(holder, item);
		    }
		    CachedContentDescriptor cacheDs = null;
		    if (isParse) {
			cacheDs = new CachedContentDescriptor(cacheId);
			app = CommonContentHelper.service.attachROHS(holder, cacheDs, fileName, null,
				ContentRoleType.toContentRoleType(roleType));
		    }
		} else {
		    QueryResult result = ContentHelper.service.getContentsByRole(holder, contentroleType);

		    if (result.hasMoreElements()) {
			item = (ContentItem) result.nextElement();
			CommonContentHelper.service.delete(holder, item);
		    }
		}

		return app;
	    }

	    @Override
	    public ApplicationData attachROHS(ContentHolder holder, CachedContentDescriptor casheDes, String fileName,
		    String desc, ContentRoleType contentRoleType) throws Exception {
		ApplicationData applicationdata = null;
		Transaction trx = new Transaction();
		try {
		    trx.start();
		    applicationdata = ApplicationData.newApplicationData(holder);
		    String uploadPath = casheDes.getContentIdentity();
		    applicationdata.setRole(contentRoleType);
		    applicationdata.setFileName(fileName);
		    applicationdata.setContentIdentity(uploadPath);
//		    applicationdata.setUploadedFromPath(uploadPath);
		    //2022-07-25 uploadPath logic 수정
			String uploadedFromPath = ContentUtil.getUploadPath(casheDes.getEncodedCCD());
		    applicationdata.setUploadedFromPath(uploadedFromPath);

		    applicationdata.setCreatedBy(SessionHelper.manager.getPrincipalReference());

		    if (desc != null) {
			applicationdata.setDescription(desc);
		    }

		    ContentServerHelper.service.updateContent(holder, applicationdata, casheDes);

		    if (holder instanceof FormatContentHolder) {
			holder = ContentServerHelper.service.updateHolderFormat((FormatContentHolder) holder);
		    }

		    trx.commit();
		    trx = null;
		} catch (PropertyVetoException e) {
		    throw new WTException(e);
		} finally {
		    if (trx != null) {
			trx.rollback();
			trx = null;
		    }
		}
		return applicationdata;
	    }

	    @Override
	    public String copyApplicationData(String appOid) throws Exception {
		ApplicationData app = (ApplicationData) CommonUtil.getPersistable(appOid);

		fileDown(appOid);

		WTUser user = (WTUser) SessionHelper.manager.getPrincipal();
		String userName = user.getName();

		Config conf = ConfigImpl.getInstance();
		String masterHost = conf.getString("HTTP.HOST.URL") + "/Windchill/";

		CacheDescriptor cd = null;//CacheUploadUtil.getCacheDescriptor(1, true, userName, masterHost);

		ResourceBundle bundle = ResourceBundle.getBundle("wt");

		String reqHost = bundle.getString("wt.rmi.server.hostname");
		boolean isMain = masterHost.indexOf(reqHost) > 0;

		String savePath = WTProperties.getServerProperties().getProperty("wt.temp");

		String m_fileFullPath = savePath + "/" + app.getFileName();

		File file = new File(m_fileFullPath);

		CachedContentDescriptor descriptor = null;//CacheUploadUtil.doUploadToCache(cd, file, isMain);

		CachedContentDescriptor cacheDs = new CachedContentDescriptor(descriptor.getEncodedCCD());

		String cId = descriptor.getEncodedCCD();
		String name = app.getFileName();

		return cId + "/" + name;

		// attach(holder, cacheDs, app.getFileName(), null, app.getRole());
	    }

	    @Override
	    public void fileDown(String appOid) throws Exception {
		FileOutputStream fos = null;
		InputStream is = null;
		try {
		    String savePath = WTProperties.getServerProperties().getProperty("wt.temp");
		    ApplicationData appData = (ApplicationData) CommonUtil.getPersistable(appOid);

		    byte[] buffer = new byte[1024];

		    File tempfile = new File(savePath + File.separator + appData.getFileName());
		    fos = new FileOutputStream(tempfile);

		    is = ContentServerHelper.service.findLocalContentStream(appData);

		    int j = 0;
		    while ((j = is.read(buffer, 0, 1024)) > 0) {

			fos.write(buffer, 0, j);
		    }

		    fos.flush();
		    fos.close();
		    is.close();
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
		    if (fos != null) {
			fos.close();
		    }

		    if (is != null) {
			is.close();
		    }
		}

	    }

	    /**
	     * Holder 의 첨부 파일 리스트
	     * 
	     * @param holder
	     * @param contentRoleType
	     * @param list
	     * @return
	     */
	    @Override
	    public List<ApplicationData> getAttachFileList(ContentHolder holder, String contentRoleType,
		    List<ApplicationData> list) throws Exception {
		QueryResult contentFiles = null;

		if (ContentRoleType.PRIMARY.toString().equals(contentRoleType)) {
		    contentFiles = ContentHelper.service.getContentsByRole(holder, ContentRoleType.PRIMARY);
		} else if (ContentRoleType.SECONDARY.toString().equals(contentRoleType)) {
		    contentFiles = ContentHelper.service.getContentsByRole(holder, ContentRoleType.SECONDARY);
		}
		// LOGGER.info("contentFiles =" + contentFiles);
		while (contentFiles != null && contentFiles.hasMoreElements()) {
		    ContentItem item = (ContentItem) contentFiles.nextElement();

		    if (item instanceof ApplicationData) {

			ApplicationData ap = (ApplicationData) item;
			// LOGGER.info("ApplicationData =" + ap.getFileName());
			list.add(ap);
		    }
		}

		return list;
	    }

	    @Override
	    public ApplicationData attachADDRole(ContentHolder holder, String roleType, String cachFile, String delFileName,
		    boolean b) throws Exception {
		ApplicationData app = null;
		ContentItem item = null;
		ContentRoleType contentroleType = ContentRoleType.toContentRoleType(roleType);

		if (StringUtil.checkString(roleType) && StringUtil.checkString(cachFile)) {

		    QueryResult result = ContentHelper.service.getContentsByRole(holder, contentroleType);
		    String roleContent = cachFile;
		    String cacheId = roleContent.split("/")[0];
		    String fileName = roleContent.split("/")[1];
		    EncodingConverter localEncodingConverter = new EncodingConverter();
		    String str = localEncodingConverter.decode(cacheId);
		    String[] arrayOfString = str.split(":");
		    boolean isParse = true;
		    try {
			long streamId = Long.parseLong(arrayOfString[0]);
			long fileSize = Long.parseLong(arrayOfString[1]);
			long folderId = Long.parseLong(arrayOfString[2]);
		    } catch (NumberFormatException nfe) {
			isParse = false;
		    }
		    //LOGGER.info("isParse=" + isParse);
		    if (result.hasMoreElements()) {
			item = (ContentItem) result.nextElement();
			ApplicationData appData = (ApplicationData) item;
			String apFileName = appData.getFileName();
			//LOGGER.info("delFileName=" + delFileName + "\tapFileName=" + apFileName);
			if (apFileName.indexOf(delFileName) > -1) CommonContentHelper.service.delete(holder, item);
		    }
		    CachedContentDescriptor cacheDs = null;
		    if (isParse) {
			cacheDs = new CachedContentDescriptor(cacheId);
			app = CommonContentHelper.service.attachROHS(holder, cacheDs, fileName, null,
				ContentRoleType.toContentRoleType(roleType));
		    }
		} else {
		    QueryResult result = ContentHelper.service.getContentsByRole(holder, contentroleType);

		    if (result.hasMoreElements()) {
			item = (ContentItem) result.nextElement();
			CommonContentHelper.service.delete(holder, item);
		    }
		}

		return app;
	    }
	    
	    @Override
		public ContentHolder attach(ContentHolder holder, WBFile file, String desc, boolean isPrimary)
				throws WTException {

			Transaction transaction = new Transaction();
			try {
				transaction.start();
				wt.org.WTPrincipalReference principalReference = SessionHelper.manager.getPrincipalReference();
				String fileName = file.getName();
				java.io.InputStream is = new FileInputStream(file.getFile());
				ApplicationData applicationData = ApplicationData.newApplicationData(holder);
				applicationData.setFileName(fileName);
				applicationData.setFileSize(file.getSize());
				if (isPrimary) {
					applicationData.setRole(ContentRoleType.PRIMARY);
				} else {
					applicationData.setRole(ContentRoleType.SECONDARY);
				}
				applicationData.setDescription(desc != null ? desc : "");
				applicationData.setCreatedBy(principalReference);
				ContentServerHelper.service.updateContent(holder, applicationData, is);
				transaction.commit();
				transaction = null;
			} catch (FileNotFoundException e) {
				throw new WTException(e.getLocalizedMessage());
			} catch (WTPropertyVetoException e) {
				throw new WTException(e.getLocalizedMessage());
			} catch (PropertyVetoException e) {
				throw new WTException(e.getLocalizedMessage());
			} catch (IOException e) {
				throw new WTException(e.getLocalizedMessage());
			} finally {
				if (transaction != null) {
					transaction.rollback();
				}
			}
			return holder;
		}


		

		@Override
		public ContentHolder attach(ContentHolder holder, ApplicationData data) throws WTException {
			Transaction transaction = new Transaction();
			try {
				transaction.start();
				LobLocator loblocator = null;
				data = (ApplicationData) PersistenceHelper.manager.refresh(data);
				Streamed streamed = (Streamed) PersistenceHelper.manager.refresh(data.getStreamData()
						.getObjectId());

				loblocator.setObjectIdentifier(((ObjectReference) streamed).getObjectId());
				((StreamData) streamed).setLobLoc(loblocator);

				java.io.InputStream is = streamed.retrieveStream();
				ApplicationData saveData = ApplicationData.newApplicationData(holder);
				saveData.setIntendedForHttpOp(true);
				saveData.setFileName(data.getFileName());
				saveData.setFileSize(data.getFileSize());
				saveData.setCreatedBy(data.getCreatedBy());
				saveData.setDescription(data.getDescription() != null ? data.getDescription() : "");
				saveData.setRole(ContentRoleType.SECONDARY);
				ContentServerHelper.service.updateContent(holder, saveData, is);
				transaction.commit();
				transaction = null;
			} catch (WTPropertyVetoException e) {
				throw new WTException(e.getLocalizedMessage());
			} catch (FileNotFoundException e) {
				throw new WTException(e.getLocalizedMessage());
			} catch (PropertyVetoException e) {
				throw new WTException(e.getLocalizedMessage());
			} catch (IOException e) {
				throw new WTException(e.getLocalizedMessage());
			} finally {
				if (transaction != null) {
					transaction.rollback();
				}
			}
			return holder;
		}

		
		@Override
		public ContentHolder attach(ContentHolder holder, File file, String desc, ContentRoleType contentRoleType)
				throws WTException {
			Transaction transaction = new Transaction();
			FileInputStream in = null;
			try {
				transaction.start();
				String fileName = file.getName();
				ApplicationData applicationdata = ApplicationData.newApplicationData(holder);
				applicationdata.setFileName(fileName);
				applicationdata.setRole(contentRoleType);
				applicationdata.setCreatedBy(SessionHelper.manager.getPrincipalReference());
				applicationdata.setDescription(desc);
				in = new FileInputStream(file);
				if (holder instanceof FormatContentHolder) {
					ContentServerHelper.service.updateContent((FormatContentHolder) holder, applicationdata, in);
				} else {
					ContentServerHelper.service.updateContent(holder, applicationdata, in);
				}
				if (holder instanceof FormatContentHolder) {
					holder = ContentServerHelper.service.updateHolderFormat((FormatContentHolder) holder);
				}
				transaction.commit();
				transaction = null;
			} catch (WTPropertyVetoException e) {
				throw new WTException(e.getLocalizedMessage());
			} catch (FileNotFoundException e) {
				throw new WTException(e.getLocalizedMessage());
			} catch (PropertyVetoException e) {
				throw new WTException(e.getLocalizedMessage());
			} catch (IOException e) {
				throw new WTException(e.getLocalizedMessage());
			} finally {
				if (transaction != null) {
					transaction.rollback();
				}
				if (file != null && file.exists() && isAutoDel) {
					file.delete();
				}
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
					}
				}
			}
			return holder;
		}

		@Override
		public ContentHolder attach(ContentHolder holder, WBFile file, String desc,
				ContentRoleType contentRoleType) throws WTException {
			try {
				attach(holder, file, desc, "PRIMARY".equals(contentRoleType.toString()));
			} catch (WTException e) {
				throw new WTException(e.getLocalizedMessage());
			}
			return holder;
		}

		@Override
		public ContentHolder attachURL(ContentHolder holder, String url, String desc,
				ContentRoleType contentRoleType) throws WTException {
			Transaction transaction = new Transaction();
			try {
				transaction.start();
				URLData urldata = URLData.newURLData(holder);
				urldata.setUrlLocation(url);
				urldata.setRole(contentRoleType);
				urldata.setCreatedBy(SessionHelper.manager.getPrincipalReference());
				if (holder instanceof FormatContentHolder) {
					ContentServerHelper.service.updateContent((FormatContentHolder) holder, urldata);
				
				} else {
					
					ContentServerHelper.service.updateContent(holder, urldata);
				}
				if (holder instanceof FormatContentHolder) {
					holder = ContentServerHelper.service.updateHolderFormat((FormatContentHolder) holder);
				}
				transaction.commit();
				transaction = null;
			} catch (WTPropertyVetoException e) {
				throw new WTException(e.getLocalizedMessage());
			} catch (PropertyVetoException e) {
				throw new WTException(e.getLocalizedMessage());
			} finally {
				if (transaction != null) {
					transaction.rollback();
				}
			}
			return holder;
		}

		
		@Override
		public ContentHolder update(ContentHolder holder, Vector<WBFile> files, Vector<String> oldfiles)
				throws Exception {

			Transaction trx = new Transaction();
			try {
				trx.start();

				ReferenceFactory rf = new ReferenceFactory();

				LOGGER.info("### content service update ###");
				StandardCommonContentService contentService = new StandardCommonContentService();

				ConcurrentHashMap<String, Object> hash = new ConcurrentHashMap<>();
				QueryResult qr = ContentHelper.service.getContentsByRole(holder, ContentRoleType.SECONDARY);
				LOGGER.info("### qr size = " + qr.size());
				while (qr.hasMoreElements()) {
					ContentItem item = (ContentItem) qr.nextElement();

					if (item instanceof URLData) {
						URLData url = (URLData) item;
						hash.put(url.getUrlLocation(), item);
					} else if (item instanceof ApplicationData) {
						ApplicationData file = (ApplicationData) item;
						hash.put(file.getFileName(), item);
					}
				}

				for (int i = 0; i < oldfiles.size(); i++) {
					ApplicationData app = (ApplicationData) rf.getReference(oldfiles.get(i)).getObject();
					if (hash.containsKey(app.getFileName())) {
						hash.remove(app.getFileName());
						continue;
					}
				}
				
				if (files != null) {
					for (WBFile file : files) {
						LOGGER.info("file = " + file.getFieldName());
						if ("primary".equalsIgnoreCase(file.getFieldName())) {
							ContentItem item = null;
							QueryResult result = ContentHelper.service.getContentsByRole(holder, ContentRoleType.PRIMARY);
							while (result.hasMoreElements()) {
								item = (ContentItem) result.nextElement();
							}
							contentService.delete(holder, item);
							contentService.attach(holder, file, "", true);
						} else {
							LOGGER.info("file = " + file.getName());
							if (hash.containsKey(file.getName())) {
								hash.remove(file.getName());
								continue;
							} else {
								contentService.attach(holder, file, "", false);
							}
						}
					}
				}

				Enumeration<String> e = hash.keys();
				while (e.hasMoreElements()) {
					Object k = e.nextElement();
					ContentItem item = (ContentItem) hash.get(k);
					LOGGER.info("del file = " + ((ApplicationData) item).getFileName());
					contentService.delete(holder, item);
				}


				trx.commit();
				trx = null;
			} catch (WTException e) {
				throw new WTException(e.getLocalizedMessage());
			} finally {
				if (trx != null) {
					trx.rollback();
				}
			}
			return holder;

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.e3ps.common.content.service.CommonContentService#delete(wt.content.ContentHolder,
		 * java.lang.String)
		 */
		@Override
		public ContentHolder delete(ContentHolder holder, String pdf) throws Exception {
			Transaction trx = new Transaction();
			try {

				trx.start();

				holder = ContentHelper.service.getContents(holder);
				Vector files = ContentHelper.getApplicationData(holder);
				if (files != null) {
					for (int i = 0; i < files.size(); i++) {
						ApplicationData app = (ApplicationData) files.get(i);
						if (pdf.equals(app.getDescription()))
							holder = delete(holder, (ApplicationData) files.get(i));
					}
				}
				trx.commit();
				trx = null;
			} catch (Exception e) {
				throw e;
			} finally {
				if (trx != null) {
					trx.rollback();
				}
			}
			return holder;
		}
		
		@Override
		public ContentHolder delete(ContentHolder holder, ContentRoleType paramContentRoleType) throws Exception{
			Transaction trx = new Transaction();
			try {
				trx.start();

				QueryResult qr  = ContentHelper.service.getContentsByRole(holder, paramContentRoleType);;
				
				while( qr.hasMoreElements()){
					ContentItem item = (ContentItem)qr.nextElement();
					delete(holder, item);
				}
				
				trx.commit();
				trx = null;
			} catch (Exception e) {
				throw e;
			} finally {
				if (trx != null) {
					trx.rollback();
				}
			}
			return holder;
			
		}

		@Override
		public ContentHolder attach(ContentHolder paramContentHolder, WBFile paramWBFile, String paramString)
				throws WTException {
			Transaction transaction = new Transaction();
			try {
				transaction.start();
				wt.org.WTPrincipalReference principalReference = SessionHelper.manager.getPrincipalReference();
				java.io.InputStream is = new FileInputStream(paramWBFile.getFile());
				ApplicationData ad = ApplicationData.newApplicationData(paramContentHolder);
				ad.setFileName(paramWBFile.getName());
				ad.setFileSize(paramWBFile.getSize());
				if ((paramContentHolder instanceof FormatContentHolder) && paramWBFile.getFieldName().equals("PRIMARY")) {
					ad.setRole(ContentRoleType.PRIMARY);
				} else {
					ad.setRole(ContentRoleType.SECONDARY);
				}
				ad.setDescription(paramString != null ? paramString : "");
				ad.setCreatedBy(principalReference);
				ContentServerHelper.service.updateContent(paramContentHolder, ad, is);
				transaction.commit();
				transaction = null;
			} catch (FileNotFoundException e) {
				throw new WTException(e.getLocalizedMessage());
			} catch (WTPropertyVetoException e) {
				throw new WTException(e.getLocalizedMessage());
			} catch (PropertyVetoException e) {
				throw new WTException(e.getLocalizedMessage());
			} catch (IOException e) {
				throw new WTException(e.getLocalizedMessage());
			} finally {
				if (transaction != null) {
					transaction.rollback();
				}
			}
			return paramContentHolder;
		}
		
		@Override
		public void isDataExcludedDelete (ContentHolder var1, ContentRoleType var2, List<String> var3) throws Exception {
			
			QueryResult qr = ContentHelper.service.getContentsByRole(var1, var2);
			while(qr.hasMoreElements()) {
				boolean exist = false;
				ContentItem conItem = (ContentItem)qr.nextElement();
				ApplicationData appData  = (ApplicationData)conItem;
				for (String persistId : var3) {
					if(persistId.equalsIgnoreCase(CommonUtil.getOIDString(appData))) {
						exist =true;
						break;
					}
				}
				if(!exist) {
					delete(var1, appData);
				}
			}
			
		}


}
