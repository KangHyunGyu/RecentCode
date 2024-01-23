package com.e3ps.common.drm;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Vector;

import com.e3ps.common.jdf.config.Config;
import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.org.bean.PeopleData;
import com.fasoo.fcwpkg.packager.WorkPackager;

import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.content.ContentServerHelper;
import wt.content.FormatContentHolder;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.org.WTUser;
import wt.session.SessionHelper;
import wt.util.WTException;
import wt.util.WTProperties;

public class E3PSDRMHelper implements java.io.Serializable, RemoteAccess {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	private static final long serialVersionUID = -7277643209320587027L;
	
	public static E3PSDRMHelper manager = new E3PSDRMHelper();

	static final boolean SERVER = RemoteMethodServer.ServerFlag;
	static final boolean VERBOSE = true; //ConfigImpl.getInstance().getBoolean("develop.verbose", false);

	static final String FSDHOMEDIR = "D:\\ptc\\fasoo\\fsdinit";
	//static final String NXHOMEDIR = "D:\\ptc\\fasoo\\fsdinit_NX";	//이 경로에 관련된 라이센스 없음

	static final String SERVERNAME = ConfigImpl.getInstance().getString("drm.server.name");
	//static final String SERVERNAME = "plmdev.iljincomposite.com";
	static final String SERVERID = ConfigImpl.getInstance().getString("drm.server.id");
	//static final String SERVERID = "0100000000001302";
	//static final String SERVERID_16 = ConfigImpl.getInstance().getString("drm.server.id_16");
	//static final String SERVERID_NX = ConfigImpl.getInstance().getString("drm.server.id_nx");
	
	//static final String NXSECURITYLEVEL = ConfigImpl.getInstance().getString("drm.nxsecuritylevel");

	static String PLMSERVER = null;
	static String CODEBASE = null;
	static String TEMP = null;
	public static String orgFileTempFolder = null;
	public static String destFileTempFolder = null;
	
	public static Config conf = ConfigImpl.getInstance();

	static {
		drmInit();
	}
	
	public static void drmInit() {
		
		if(VERBOSE) LOGGER.info("====== DRMHelper initialize....... ======");
		
		try {
			PLMSERVER = WTProperties.getLocalProperties().getProperty("wt.home");
			CODEBASE = WTProperties.getLocalProperties().getProperty("wt.server.codebase");
			TEMP = WTProperties.getServerProperties().getProperty("wt.temp");
			orgFileTempFolder = TEMP + "\\e3ps\\drm\\orgFileTemp";
			destFileTempFolder = TEMP + "\\e3ps\\drm\\destFileTemp";

			File orgfolder = new File(orgFileTempFolder);
			File destfolder = new File(destFileTempFolder);
			
			if (!orgfolder.isDirectory()) {
				orgfolder.mkdirs();
				if(VERBOSE) LOGGER.info("orgFileTempFolder created : " + orgFileTempFolder);
			}
			
			if (!destfolder.isDirectory()) {
				destfolder.mkdirs();
				if(VERBOSE) LOGGER.info("destFileTempFolder created : " + destFileTempFolder);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(VERBOSE) LOGGER.info("====== drm Init done ======");
	}
	
	public static String FileTypeStr(int i) {
		String ret = null;
		switch(i) {
	    	case 20 : ret = "파일을 찾을 수 없습니다."; break;
	    	case 21 : ret = "파일 사이즈가 0 입니다.";  break;
	    	case 22 : ret = "파일을 읽을 수 없습니다."; break;
	    	case 29 : ret = "암호화 파일이 아닙니다.";  break;
	    	case 26 : ret = "FSD 파일입니다.";       	break;
	    	case 105: ret = "Wrapsody 파일입니다.";  	break;
	    	case 106: ret = "NX 파일입니다.";			break;	    	
	    	case 101: ret = "MarkAny 파일입니다.";   	break;
	    	case 104: ret = "INCAPS 파일입니다.";    	break;
	    	case 103: ret = "FSN 파일입니다.";       	break;
		}
		return ret;	
	}

	public String[] download(String holderoid, String appoid) {
		
		String[] rtn = new String[3];
		
		ContentHolder holder = (ContentHolder) CommonUtil.getObject(holderoid);
		ApplicationData appData = (ApplicationData) CommonUtil.getObject(appoid);
			
		if(VERBOSE) LOGGER.info("======= DRM Download Start ======");
		
		if (!SERVER) {
			try {
				RemoteMethodServer remotemethodserver = RemoteMethodServer.getDefault();
				Class[] argTypes = { ContentHolder.class, ApplicationData.class };
				Object[] argValues = { holder, appData };
				Object obj = remotemethodserver.invoke("download", "e3ps.common.drm.E3PSDRMHelper", null, argTypes, argValues);
				rtn = (String[]) obj;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return rtn;
	}

	
	public String[] download(ContentHolder holder, ApplicationData appData) {
		
		if(VERBOSE) LOGGER.info(">>>>> E3PSDRMHelper.download START ************");
		
		String[] rtn = new String[3];
		
		drmInit();
		
		// download 할 컨텐츠를 가져와 drm 원본파일 폴더에 저장.
		File orgFile = null;
		String appoid = CommonUtil.getOIDString(appData);
		PeopleData pData = null;
		
		try {
			
			WTUser cUser = (WTUser) SessionHelper.manager.getPrincipal();
			pData = new PeopleData(cUser);
			
			Vector vec = new Vector();
			if (holder instanceof FormatContentHolder) {
				FormatContentHolder fch = (FormatContentHolder) ContentHelper.service.getContents((ContentHolder) holder);
				vec = ContentHelper.getApplicationData(fch);
				vec.add(ContentHelper.getPrimary(fch));
			} else if (holder instanceof ContentHolder) {
				ContentHolder ch = ContentHelper.service.getContents((ContentHolder) holder);
				vec = ContentHelper.getApplicationData(ch);
			} else {
				if(VERBOSE) LOGGER.info("WARNING :: OBJECT IS NOT CONTENTHOLDER!!!!!!");
			}
			vec.addElement(ContentHelper.service.getThumbnail((ContentHolder) holder));
			for (int i = 0; i < vec.size(); i++) {
				ApplicationData ad = (ApplicationData) vec.elementAt(i);
				if (appoid.equals(CommonUtil.getOIDString(ad)))
					appData = ad;
			}
			
			if(VERBOSE) LOGGER.info("appData : " + appData);
			
			byte abyte0[] = new byte[8192];
			InputStream in = ContentServerHelper.service.findContentStream(appData);

			String suffix = "";

			int suffixIndex = appData.getFileName().lastIndexOf(".");
			if (suffixIndex > 0) {
				suffix = appData.getFileName().substring(suffixIndex);
			}

			orgFile = File.createTempFile("test", suffix, new File(orgFileTempFolder));

			FileOutputStream fout = new FileOutputStream(orgFile);
			int i = 0;
			while ((i = in.read(abyte0, 0, 8192)) > 0) {
				fout.write(abyte0, 0, i);
			}
			
			fout.flush();
			fout.close();
			
			
		} catch (WTException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// drm 소스 시작부분
		boolean iret = false; 
		int retVal = 0;
		
		WorkPackager objWorkPackager = new WorkPackager();
		//objWorkPackager.setCharset("eucKR");

		//복호화 된문서가 암호화된 문서를 덮어쓰지 않음
		objWorkPackager.setOverWriteFlag(false);
		objWorkPackager.SetLogInfo(40, "./logs/");

		retVal = objWorkPackager.GetFileType(orgFile.getAbsolutePath());

		LOGGER.info("파일형태는 " + FileTypeStr(retVal) + "["+retVal+"]"+" 입니다.");

		//일반 파일의 경우
		if (retVal == 29) {

			//파일 확장자 체크( IsSupportFile() ) 로직
			//파일 확장자 체크( IsSupportFile() ) 로직
			iret = objWorkPackager.IsSupportFile(FSDHOMEDIR,
							SERVERID,
							orgFile.getAbsolutePath());
			LOGGER.info("strFsdinitPath  : "+ FSDHOMEDIR );
			LOGGER.info("strCPID  : "+ SERVERID );
			LOGGER.info("strOrgFilePath  : "+ orgFile.getAbsolutePath() );
			LOGGER.info("지원 확장자 체크  : "+ iret );
			
			//지원 확장자의 경우 암호화 진행
			if (iret) {
			//파일 암호화
				iret = objWorkPackager.DoPackagingFsn2( 
												  FSDHOMEDIR,		//fsdinit 폴더 FullPath 설정
												  SERVERID,			//고객사 Key(default) 
												  orgFile.getAbsolutePath(),		//암호화 대상 문서 FullPath + FileName
												  destFileTempFolder + "\\" + appData.getFileName(),		//암호화 된 문서 FullPath + FileName
												  appData.getFileName(),					//파일 명
												  pData.getId(),						//작성자 ID
												  pData.getName(),						//작성자 명
												  "wcadmin", "Administrator", "plm", "iljincomposite",				//시스템 ID
												  "wcadmin", "Administrator", "plm", "iljincomposite",				//ACL ID (권한 확인을 위한 ID로 SystemID와 동일 적용)
												  "7ca0571112c64686a1b39e3be6bf173e"
												  );
				
				LOGGER.info("암호화 결과값 : " + iret);
				LOGGER.info("암호화 문서 : " + objWorkPackager.getContainerFilePathName());
				LOGGER.info("오류코드 : " + objWorkPackager.getLastErrorNum());
				LOGGER.info("오류값 : " + objWorkPackager.getLastErrorStr());
				
				if(!iret) {
					rtn[0] = String.valueOf(objWorkPackager.getLastErrorNum());
					rtn[1] = "파일 암호화 중 에러가 발생하였습니다. [error code : "
							+ objWorkPackager.getLastErrorNum() + " : "
							+ objWorkPackager.getLastErrorStr()
							+ "]\\n관리자에게 문의 하십시오.";
					rtn[2] = appData.getFileName();
				}else {
					rtn[0] = String.valueOf(objWorkPackager.getLastErrorNum());
					rtn[1] = destFileTempFolder + "\\"
							+ objWorkPackager.getContainerFileName();
					rtn[2] = appData.getFileName();
				}
			}
			else {
				LOGGER.info("지원 확장자가 아닌경우 암호화 불가능 합니다.["+ retVal +"]");
				rtn[0] = "0";
				rtn[1] = orgFile.getAbsolutePath();
				rtn[2] = appData.getFileName();
			}
		}
		else {
			LOGGER.info("일반 파일이 아닌경우 암호화 불가능 합니다.["+ retVal + "]");
			rtn[0] = "0";
			rtn[1] = orgFile.getAbsolutePath();
			rtn[2] = appData.getFileName();
		}
		// drm 소스 끝
		if(VERBOSE) LOGGER.info(rtn[0] + " file = " + rtn[1]);

		return rtn;
	}

	public File upload(String filePath) {
		File drmFile = null;
		File uploadFile = new File(filePath);
		if (uploadFile.exists()) {
			if (!SERVER) {
				try {
					RemoteMethodServer remotemethodserver = RemoteMethodServer.getDefault();
					Class[] argTypes = { File.class };
					Object[] argValues = { uploadFile };
					drmFile = (File) remotemethodserver.invoke("upload", "e3ps.common.drm.E3PSDRMHelper", null, argTypes, argValues);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return drmFile;
	}

	public File upload(File file, String fileName) {
		
		File drmFile = null;
		
		try {
			if(VERBOSE) LOGGER.info(">>>>>> DRM Decrypt **********");
			
			drmInit();

			String strSrcFile = file.getAbsolutePath();
			String strDesFile =  destFileTempFolder + "\\" + fileName;
			
			if(VERBOSE) LOGGER.info("strSrcFile : " + strSrcFile);
			if(VERBOSE) LOGGER.info("strDesFile : " + strDesFile);

			// drm 시작 부분
			boolean bret = false;
			boolean iret = false; 
			int retVal = 0;
			
			WorkPackager objWorkPackager = new WorkPackager();
			//objWorkPackager.setCharset("eucKR");
			//objWorkPackager.SetLogInfo(40, "./logs/");

			//복호화 된문서가 암호화된 문서를 덮어쓰지 않음
			objWorkPackager.setOverWriteFlag(false);

			retVal = objWorkPackager.GetFileType(strSrcFile);

			LOGGER.info("파일형태는 " + FileTypeStr(retVal) + "["+retVal+"]"+" 입니다.");
	       
			//대상 문서가 FSN로 암호화 되었을 때만 복호화 실행
			if (retVal == 103) {
				
				Hashtable htable = objWorkPackager.GetFileHeader(strSrcFile);
				String SOURCE_SERVERID     = htable.get("CPID").toString();
				String SOURCE_SECURITYCODE = htable.get("misc2-info").toString();

				LOGGER.info("SERVERID : " + SOURCE_SERVERID);
				LOGGER.info("SECURITYCODE : " + SOURCE_SECURITYCODE);

				//파일 확장자 체크( IsSupportFile() ) 로직
				iret = objWorkPackager.IsSupportFile(FSDHOMEDIR,
								SERVERID,
								strSrcFile);
				
				LOGGER.info("지원 확장자 체크  : "+ iret );
				
				//지원 확장자의 경우 복호화 진행
				if (iret) {
					// 암호화 된 파일 복호화
					bret = objWorkPackager.DoExtract(
											FSDHOMEDIR,					//fsdinit 폴더 FullPath 설정
											SERVERID,				//고객사 Key(default) 
											strSrcFile,			//복호화 대상 문서 FullPath + FileName
											strDesFile		//복호화 된 문서 FullPath + FileName
											);
					
					LOGGER.info("복호화 결과값 : " + iret);
					LOGGER.info("복호화 문서 : " + objWorkPackager.getContainerFilePathName());
					//LOGGER.info("문서등급 : " + objWorkPackager.getmisc2-info());
					LOGGER.info("오류코드 : " + objWorkPackager.getLastErrorNum());
					LOGGER.info("오류값 : " + objWorkPackager.getLastErrorStr());
					if (!bret) {
						if(VERBOSE) LOGGER.info("encryption fail = " + objWorkPackager.getLastErrorNum() + "  " + objWorkPackager.getLastErrorStr());
						drmFile = null;
					} else {
						if(VERBOSE) LOGGER.info("encryption completed");
						drmFile = new File(strDesFile);
					}
				}
				else {
					LOGGER.info("지원 확장자가 아닌경우 복호화 불가능 합니다.["+ iret +"]");
					return file;
				}
			}
			else {
				LOGGER.info("FSN 파일이 아닌경우 복호화 불가능 합니다.["+ retVal + "]");
				return file;
			}
				// drm 끝
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return drmFile;
	}

	/**
	 * @desc DRM 활성 여부
	 * @author shkim
	 * @date 2020.10.21
	 * @method enableDRMCheck
	 * @return boolean
	 */
	public boolean enableDRMCheck() {
		return conf.getBoolean("drm.enable", false);
	}
	
	/**
	 * @desc 다운로드할 파일의 암호화 여부 (암호화 : true)
	 * @author shkim
	 * @date 2020.10.21
	 * @method IsPackageFile
	 * @param workPackager
	 * @param file
	 * @return boolean
	 */
	public boolean isPackageFile(WorkPackager workPackager, File file) {
		return workPackager.IsPackageFile(file.getAbsolutePath());
	}
}
