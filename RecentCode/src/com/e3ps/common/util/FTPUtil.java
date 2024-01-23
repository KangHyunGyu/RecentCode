package com.e3ps.common.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.e3ps.common.db.DBConnectionManager;
import com.e3ps.common.jdf.config.ConfigEx;
import com.e3ps.common.jdf.config.ConfigExImpl;
import com.e3ps.common.log4j.Log4jPackages;

import wt.content.ApplicationData;
import wt.content.ContentServerHelper;

public class FTPUtil implements java.io.Serializable{
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
	private static final long serialVersionUID = 1L;
	public static final String CPC_FILE_PATH = "D:\\DISTRIBUTE_FILE";
	
	public static FTPUtil manager = new FTPUtil();
	
	//static FTPClient ftp = new FTPClient();
	static String cpcHost;
	static String ftpID;
	static String ftpPWD;
	static String cpcDownFolder;
	
	static{
		
		ConfigExImpl conf = ConfigEx.getInstance("eSolution");
		
		cpcHost = conf.getString("CPC.HOST.URL");
		ftpID = conf.getString("CPC.FTP.ID");
		ftpPWD = conf.getString("CPC.FTP.PWD");
		
		cpcDownFolder = WCUtil.getWTTemp()+"\\e3ps\\cpc";
	}
	 
	/**
	 * 
	 * @desc	: CPC DB Connection
	 * @author	: mnyu
	 * @date	: 2019. 11. 8.
	 * @method	: dbConnection
	 * @return	: Connection
	 * @return
	 * @throws Exception
	 */
	private Connection dbConnection() throws Exception{
		return DBConnectionManager.dbConnection("cpc");
	}
	
	/**
	 * 
	 * @desc	: CPC DB DisConnection
	 * @author	: mnyu
	 * @date	: 2019. 11. 8.
	 * @method	: disConnection
	 * @return	: void
	 * @param pstmt
	 * @param con
	 * @throws SQLException
	 */
	private void disConnection(PreparedStatement pstmt,Connection con,ResultSet rs) throws SQLException{
		DBConnectionManager.disConnection(pstmt,con,rs);
	}
	
	/**
	 * 
	 * @desc	: Ftp 접속
	 * @author	: tsuam
	 * @date	: 2019. 9. 17.
	 * @method	: ftpConnection
	 * @return	: FTPClient
	 * @return
	 * @throws Exception
	 */
	public FTPClient connection() throws Exception{
		FTPClient client = new FTPClient();
		client.setControlEncoding("euc-kr"); //인코딩//UTF-8 euc-kr
		client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
       
		client.connect(cpcHost);//호스트 연결
        int reply = client.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
        	client.disconnect();
            throw new Exception("Exception in connecting to FTP Server");
        }
        client.login(ftpID, ftpPWD);//로그인
        client.setFileType(FTP.BINARY_FILE_TYPE);
        client.enterLocalPassiveMode();
		
		
		return client;
		
	}
	
	/**
	 * 
	 * @desc	: FTP 연결 종료
	 * @author	: tsuam
	 * @date	: 2019. 9. 17.
	 * @method	: disconnect
	 * @return	: void
	 */
	public void disconnect(FTPClient client){
        if (client.isConnected()) {
            try {
            	client.logout();
            	client.disconnect();
            } catch (IOException f) {
                f.printStackTrace();
            }
        }
	}
	
	/**
	 * 
	 * @desc	: 파일 업로드
	 * @author	: tsuam
	 * @date	: 2019. 9. 18.
	 * @method	: uploadFTP
	 * @return	: void
	 * @param file : ftp 접속 client
	 * @param file : 전송 대상 파일
	 * @param romotePath : FTP 저장 위치
	 */
	public void uploadFTP(FTPClient client,File file,String romotePath) throws Exception{
		
		InputStream input = new FileInputStream(file);
		
		String sendFilePath = romotePath + File.separator + file.getName();
		
		client.storeFile(sendFilePath, input);
		
		input.close();
	}
	
	/**
	 * 
	 * @desc	:
	 * @author	: tsuam
	 * @date	: 2019. 10. 17.
	 * @method	: dowonloadFile 
	 * @return	: void
	 * @param filePath CPC FTP 파일 경로
	 * @param fileName CPC 파일명
	 */
	public File dowonloadFile(String filePath,String fileName){
		File cpcFile = null;
		FTPClient client = null;
    	try{
			 client = FTPUtil.manager.connection();
			 
			 cpcFile = FTPUtil.manager.downloadFTP(client, filePath, fileName);
			
			 FTPUtil.manager.disconnect(client);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if (client != null && client.isConnected()) {
				  try {
					   client.disconnect();
				  } catch (Exception e) {
					  e.printStackTrace();
				  }
		    }
		}
    	
    	return cpcFile;
    }
	
	/**
	 * 
	 * @desc	: CPC FTP 에서 파일 PLM 임시 폴더로 파일 다운
	 * @author	: tsuam
	 * @date	: 2019. 10. 14.
	 * @method	: downloadFTP
	 * @return	: File
	 * @param client
	 * @param filePath
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public File downloadFTP(FTPClient client,String filePath,String fileName) throws Exception{
		
		BufferedOutputStream bos = null;
		File cpcFile = null;
		try{
			
			long time =System.currentTimeMillis();
			String tempFolder = String.valueOf(time);
			String fileDwonPath = cpcDownFolder +"\\"+tempFolder;
			cpcFile = new File(fileDwonPath,fileName);
			File fpath = new File(fileDwonPath);
			fpath.mkdirs();
			
			client.setFileType(FTP.BINARY_FILE_TYPE);
			client.changeWorkingDirectory(filePath);
			
			bos = new BufferedOutputStream(new FileOutputStream(cpcFile));
			boolean isSucess = client.retrieveFile(fileName, bos);
			if(!isSucess) {
				throw new Exception("파일 다운에 실패 했습니다. ");
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("FTP Exception : " + e);

			
		}finally{
			if (bos != null) {
				try {
					bos.close();
				}catch (Exception e) {
					e.printStackTrace();
				}    
			}
				
		    if (client != null && client.isConnected()) {
			   try {
				   client.disconnect();
			  } catch (Exception e) {
				  e.printStackTrace();
			  }
		    }
			
		}
		
		return cpcFile;
		
	}
	
	/**
	 * 
	 * @desc	: 파일 업로드
	 * @author	: tsuam
	 * @date	: 2019. 9. 19.
	 * @method	: uploadFTP
	 * @return	: void
	 * @param client
	 * @param app
	 * @param romotePath
	 * @throws Exception
	 */
	public void uploadFTP(FTPClient client,ApplicationData app,String remotePath) throws Exception{
		
		//DistributeHelper.service.uploadFTP(client, app, remotePath);
		
		InputStream input = ContentServerHelper.service.findContentStream((ApplicationData)app);
		String sendFilePath = remotePath + File.separator + app.getFileName();
		
		makeDirectorys(client,remotePath);
		client.storeFile(sendFilePath, input);
		
		input.close();
		
	}
	
	/**
	 * 
	 * @desc	: CPC 에  대상 폴더 생성 /PLM/배포번호/부품번호,/PLM/배포번호/문서번호/Primary,/PLM/배포번호/문서번호/Secondary
	 * @author	: tsuam
	 * @date	: 2019. 9. 19.
	 * @method	: makeDirectorys
	 * @return	: void
	 * @param client
	 * @param remotePath
	 * @throws Exception
	 */
	public static void makeDirectorys(FTPClient client,String remotePath) throws Exception{
		
		
		 String[] remotePathList = remotePath.split("/");
		 String tempDir = "";
		 for(int i = 0 ; i < remotePathList.length ; i++){
			 
			 tempDir = tempDir+"/"+remotePathList[i];
			
			 client.makeDirectory(tempDir);
		 }
		
	}
	 

	/**
	 * @desc	: CPC 파일 객체 저장
	 * @author	: mnyu
	 * @date	: 2019. 10. 8.
	 * @method	: saveFTP
	 * @return	: void
	 * @param data
	 */
//	public void saveFTP(Connection con ,DistributeFileData data) throws Exception {
//		PreparedStatement pstmt = null;
//		boolean isdicon = false;
//		try {
//			if(con == null){
//				con = dbConnection();
//				isdicon = true;
//			}
//			String sql = "insert into ATTACHFILE(MAIN_OBJ_NO, SUB_OBJ_NO, EPM_NO, FILE_NAME, LOCATION, SIZE) "
//					+ "values(?,?,?,?,?,?)";
//			int idx = 1;
//			pstmt = con.prepareStatement(sql);
//			pstmt.setString(idx++, data.getMainNo());
//			pstmt.setString(idx++, data.getSubNo());
//			pstmt.setString(idx++, data.getEpmNo());
//			pstmt.setString(idx++, data.getApp().getFileName());
//			pstmt.setString(idx++, CPC_FILE_PATH + data.getFileLocation() + File.separator + data.getApp().getFileName());
//			pstmt.setLong(idx++, data.getApp().getFileSize());
//			pstmt.executeUpdate(); 
//			LOGGER.info("##### ATTACHFILE - 파일 저장 완료 ####");
//		} catch (IOException e) {
//			e.printStackTrace();
//			throw new Exception(e.getMessage());
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new Exception(e.getMessage());
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new Exception(e.getMessage());
//		} finally{
//            try{
//            	if(isdicon){
//            		disConnection(pstmt, con,null);
//            	}else{
//            		disConnection(pstmt, null,null);
//            	}
//            }catch (SQLException e){
//            	e.printStackTrace();
//            	throw new Exception(e.getMessage());
//            }
//        }
//	}
	/**
	 * @desc	: FTP 파일 삭제
	 * @author	: mnyu
	 * @date	: 2020. 1. 21.
	 * @method	: deleteFTP
	 * @return	: void
	 * @param filePath
	 */
	public void deleteFileFTP(FTPClient client, String filePath) throws Exception {
		try{
			// filePath = "/PLM/IP-2001-0002/CS-1912-0014/images.png";
			boolean isSucess = client.deleteFile(filePath);
			
			if(!isSucess) {
				throw new Exception("파일 삭제 실패");
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("FTP Exception : " + e);
		}
	}
	
	/**
	 * 
	 * @desc	: FTP 폴더 삭제
	 * @author	: tsuam
	 * @date	: 2020. 1. 30.
	 * @method	: deleteFolerFTP
	 * @return	: void
	 * @param client
	 * @param folderPath
	 * @throws Exception
	 */
	public void deleteFolerFTP(FTPClient client, String folderPath) throws Exception {
		try{
			// filePath = "/PLM/IP-2001-0002/CS-1912-0014";
			
			boolean isSucess = client.removeDirectory(folderPath);
			
			if(!isSucess) {
				throw new Exception("폴더 삭제 실패");
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("FTP Exception : " + e);
		}
	}
	
	/**
	 * 
	 * @desc	: 폴더 및 파일 삭제
	 * @author	: tsuam
	 * @date	: 2020. 1. 30.
	 * @method	: deleteALLFTP
	 * @return	: void
	 * @param client
	 * @param folderPath
	 * @throws Exception
	 */
	public void deleteALLFTP(FTPClient client, String folderPath) throws Exception {
		
		boolean isClose = false;
		try{
			// folderPath = "/PLM/IP-2001-0002/CS-1912-0014/";
			if(client == null ){
				client = FTPUtil.manager.connection();
				isClose = true;
			}
			
			FTPFile[] fileList = client.listFiles(folderPath);
			
			LOGGER.info(">>>>>>>>>> FTP list : " +fileList.length);
			for(FTPFile ftpFile : fileList){
				if(ftpFile.isDirectory()){
					String subfolderPath = folderPath +"/"+ ftpFile.getName();
					FTPFile[] subfileList = client.listFiles(subfolderPath);
					for(FTPFile subftpFile : subfileList){
						String filePath = subfolderPath +"/"+ subftpFile.getName();
						FTPUtil.manager.deleteFileFTP(client, filePath);
					}
					
					if(subfileList.length > 0){
						FTPUtil.manager.deleteFolerFTP(client, subfolderPath);
					}
					
				}else{
					String filePath = folderPath +"/"+ ftpFile.getName();
					FTPUtil.manager.deleteFileFTP(client, filePath);
				}
				
			}
			
			if(fileList.length > 0){
				FTPUtil.manager.deleteFolerFTP(client, folderPath);
			}
			
			
			
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("FTP Exception : " + e);
		}finally{
			if(isClose){
				if (client != null && client.isConnected()) {
				   try {
					   client.disconnect();
				  } catch (Exception e) {
					  e.printStackTrace();
				  }
			    }
			}
		    
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		LOGGER.info("START");
		String folderPath = "/PLM/LE-2001-0005/P93531083";
		try{
			FTPClient client = FTPUtil.manager.connection();
			
			//FTPFile[] list = client.listDirectories(folderPath);
			FTPFile[] fileList = client.listFiles(folderPath);
			LOGGER.info(">>>>>>>>>> FTP list : " +fileList.length);
			for(FTPFile ftpFile : fileList){
				String filePath = folderPath +"/"+ ftpFile.getName();
				FTPUtil.manager.deleteFileFTP(client, filePath);
			}
			client.removeDirectory(folderPath);
			//FTPUtil.manager.deleteFTP(client, folderPath);
			
			//FTPUtil.manager.deleteFTP(client, filePath);
			
			FTPUtil.manager.disconnect(client);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		LOGGER.info("END");
		
	 }
	
}
