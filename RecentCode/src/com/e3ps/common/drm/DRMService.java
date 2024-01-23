package com.e3ps.common.drm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;

import com.duruan.shadowcube.SecureStreamCreator;
import com.duruan.shadowcube.SecureStreamReader;
import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.util.CommonUtil;

import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.content.ContentServerHelper;
import wt.content.FormatContentHolder;

public class DRMService {
	
	private static String id = ConfigImpl.getInstance().getString("drm.api.snnum");
	private static String pa = ConfigImpl.getInstance().getString("drm.api.pass");
	private static String fUrl = ConfigImpl.getInstance().getString("drm.api.initfolder");
	
	private static String encryfolder = ConfigImpl.getInstance().getString("drm.api.encryfolder");  //암
	private static String decryfolder = ConfigImpl.getInstance().getString("drm.api.decryfolder");	//복
	
	public static String orgFileTempFolder = ConfigImpl.getInstance().getString("drm.api.orgFileFolder");
	
	/**
	 * 인증
	 */
	public static void drmInit() {
		SecureStreamCreator.Init(id, pa, fUrl);
	}
	
	
	/**
	 * 복호화
	 * @param file
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static File upload(File file, String fileName) throws IOException{
		File rFile = null;
		SecureStreamReader ssr = null;
		
		try {
			drmInit();
			
			ssr = new SecureStreamCreator().CreateDecryptReader(file.getAbsolutePath().toString());
			if(ssr != null) {
				if(ssr.IsSecureSource()) {
					ssr.ReadAllToFile(decryfolder+file.getName(), null);
					rFile = new File(decryfolder+file.getName());
					System.out.println("upload rFile ssr if : "+ rFile);
				}else {
					rFile = file;
					System.out.println("upload rFile ssr else : "+ rFile);
				}
			}else {
				rFile = file;
				System.out.println("upload rFile else : "+ rFile);
			}
		
		}catch(Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}finally {
			if(ssr!=null) {
				ssr.close();
			}
		}
		
		
		return rFile;
	}
	
	
	/**
	 * 암호화
	 * @param file
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static File download(File file, String fileName) throws IOException {
		File rFile = null;
		SecureStreamReader ssr = null;
		
		try {
			drmInit();
			
			ssr = new SecureStreamCreator().CreateEncryptReader(file.getAbsolutePath(), 0, 0);
			
			System.out.println("File Download ssr value : " + ssr);
			
			if(ssr != null) {
				ssr.ReadAllToFile(encryfolder+fileName, null);
				rFile = new File(encryfolder+fileName);
			}else {
				rFile = file;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(ssr!=null) {
				ssr.close();
			}
		}
		return rFile;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static File download(ContentHolder holder, ApplicationData appData) {
		return download(holder, appData, null, null);
	}
	
	public static File download(ContentHolder holder, ApplicationData appData, HttpServletRequest req, HttpServletResponse res) {
		
		File rFile = null;
		try {
			
			String appoid = CommonUtil.getOIDString(appData);
			
			@SuppressWarnings("rawtypes")
			Vector vec = new Vector();
			if (holder instanceof FormatContentHolder) {
				FormatContentHolder fch = (FormatContentHolder) ContentHelper.service.getContents((ContentHolder) holder);
				vec = ContentHelper.getApplicationData(fch);
				vec.add(ContentHelper.getPrimary(fch));
			} else if (holder instanceof ContentHolder) {
				ContentHolder ch = ContentHelper.service.getContents((ContentHolder) holder);
				vec = ContentHelper.getApplicationData(ch);
			} else {
				//if(VERBOSE) LOGGER.info("WARNING :: OBJECT IS NOT CONTENTHOLDER!!!!!!");
			}
			vec.addElement(ContentHelper.service.getThumbnail((ContentHolder) holder));
			for (int i = 0; i < vec.size(); i++) {
				ApplicationData ad = (ApplicationData) vec.elementAt(i);
				if (appoid.equals(CommonUtil.getOIDString(ad)))
					appData = ad;
			}
			
			byte abyte0[] = new byte[8192];
			InputStream in = ContentServerHelper.service.findContentStream(appData);

			String suffix = "";

			int suffixIndex = appData.getFileName().lastIndexOf(".");
			if (suffixIndex > 0) {
				suffix = appData.getFileName().substring(suffixIndex);
			}
			
			File orgFile = File.createTempFile("test", suffix, new File(orgFileTempFolder));
			
			FileOutputStream fout = new FileOutputStream(orgFile);
			int i = 0;
			while ((i = in.read(abyte0, 0, 8192)) > 0) {
				fout.write(abyte0, 0, i);
			}
			
			fout.flush();
			fout.close();
			
			String fileName = appData.getFileName();
			
			rFile = download(orgFile, fileName);
			
			System.out.println("File download :  rFile : " + rFile);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return rFile;
	}

}
