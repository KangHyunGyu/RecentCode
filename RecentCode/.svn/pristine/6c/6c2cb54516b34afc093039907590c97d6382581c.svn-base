/**
 * @(#) FileUtil.java Copyright (c) jerred. All rights reserverd
 * @version 1.00
 * @since jdk 1.4.02
 * @createdate 2004. 3. 22.
 * @author Cho Sung Ok, jerred@bcline.com
 * @desc
 */

package com.e3ps.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.e3ps.common.jdf.config.Config;
import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.log4j.Log4jPackages;

import wt.content.ApplicationData;
import wt.content.ContentServerHelper;
import wt.content.Streamed;
import wt.fc.PersistenceHelper;
import wt.util.WTException;


/**
 * 
 */
public class FileUtil implements wt.method.RemoteAccess, java.io.Serializable {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
	static final boolean SERVER = wt.method.RemoteMethodServer.ServerFlag;
	
	public static FileUtil manager = new FileUtil();
	
	
	public static void checkDir(String dir) {
		File createDir = new File ( dir );
		if (!createDir.exists ()) createDir.mkdir ();
		if (!createDir.exists ()) createDir.mkdirs ();
	}

	public static void checkDir(File dir) {
		if (!dir.exists ()) dir.mkdir ();
		if (!dir.exists ()) dir.mkdirs ();
	}

	public static String getFileSizeStr(long filesize) {
		DecimalFormat df = new DecimalFormat ( ".#" );
		String fSize = "";
		if ( ( filesize > 1024 ) && ( filesize < 1024 * 1024 )) {
			fSize = df.format ( (float) filesize / 1024 ).toString () + " KB";
		} else if (filesize >= 1024 * 1024) {
			fSize = df.format ( (float) filesize / ( 1024 * 1024 ) ).toString ()	+ " MB";
		} else if (filesize < 1024 && filesize > 1) {
			fSize = "1 KB";
		} else {
			fSize = "0 Bytes";
		}
		return fSize;
	}
	
   public static File copyFiles(ApplicationData appData, String moveTo, String fileName) throws Exception {
	   
		int fileSize    = 0;
		int result      = 0;
		BufferedOutputStream bo = null;
		File toFile = null;
		try {
			LOGGER.info("1.appData : "+appData);
			LOGGER.info("2.moveTo : "+moveTo);
			LOGGER.info("3.fileName : "+fileName);
			LOGGER.info("4.appData Size : "+appData.getFileSize());
			
			InputStream inStream = ContentServerHelper.service.findContentStream((ApplicationData)appData);
			
			//FileInputStream  inStream = (FileInputStream)FileUtil.findContentStream(appData);
			
			LOGGER.info("5inStream : "+inStream);
			
			BufferedInputStream bi = new BufferedInputStream(inStream);
			
			File toFolder = new File(moveTo);
	
			if(!toFolder.exists())
				toFolder.mkdirs();
	
			FileOutputStream outStream = new FileOutputStream(toFolder + "\\" + fileName);
			bo = new BufferedOutputStream(outStream);
			fileSize = bi.available();
			
			while( (result = bi.read())  != -1  ){
				bo.write(result);
			}		
			
			String toFileUrl = toFolder.getPath().concat(File.separator).concat(fileName);
			toFile = new File(toFileUrl);
			
			LOGGER.info("5.toFile.getAbsolutePath : "+toFile.getAbsolutePath());
		} catch(Exception e) {
			e.printStackTrace();
			throw new WTException(e);
		} finally {
			if(bo != null)
				bo.close();
		}
		
		return toFile;
	}  

	   
	   
	public static InputStream findContentStream( ApplicationData appData ) throws WTException {
		
		Streamed sd = null;
		
		try {
			sd = ( Streamed )PersistenceHelper.manager.refresh( appData.getStreamData( ).getObjectId( ) );
			LOGGER.info("findContentStream : "+sd);
        } catch (wt.fc.ObjectNoLongerExistsException e){
        	appData = (ApplicationData)PersistenceHelper.manager.refresh( appData );
            sd = ( Streamed )PersistenceHelper.manager.refresh( appData.getStreamData( ).getObjectId( ) );
			LOGGER.info("catch findContentStream : "+sd);
        }
        
        InputStream is = sd.retrieveStream();
        
        return is;
        
    }
	
	/**
	 * 
	 * @desc	: 임시 폴더 생성
	 * @author	: tsuam
	 * @date	: 2019. 9. 9.
	 * @method	: createTempDirectory
	 * @return	: File
	 * @return
	 * @throws IOException
	 */
	public static File createTempDirectory()
		    throws IOException
	{
	    final File temp;
	    
	    String tempLocation = WCUtil.getWTTemp().concat("\\").concat("e3ps").concat("\\");
	    tempLocation = tempLocation.concat(Long.toString(System.nanoTime()));
	    LOGGER.info("tempLocation =" + tempLocation);
	    temp = new File(tempLocation);
	    
	    if(temp.exists()){
	    	temp.delete();
	    }else{
	    	temp.mkdirs();
	    }
	    
	    //temp = File.createTempFile(tempLocation, Long.toString(System.nanoTime()));
	    /*
	    if(!(temp.delete()))
	    {
	        throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
	    }

	    if(!(temp.mkdir()))
	    {
	        throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
	    }
	    */
	    return (temp);
	}
	
	/**
	 * 
	 * @desc	: 파일 생성 시간
	 * @author	: tsuam
	 * @date	: 2019. 10. 15.
	 * @method	: getFileCreatStamp
	 * @return	: void
	 */
	public static void getFileCreatStamp(){
		

	    File file = new File( "D:\\ptc\\Windchill_10.2\\Windchill\\temp\\e3ps\\cpc\\1571045090732\\양식3.pptx" );

		BasicFileAttributes attrs;
		try {
		    attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
		    FileTime time = attrs.creationTime();
		    
		    String pattern = "yyyy-MM-dd HH:mm:ss";
		    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			
		    String formatted = simpleDateFormat.format( new Date( time.toMillis() ) );

		    LOGGER.info( "파일 생성 날짜 및 시간은 다음과 같습니다.: " + formatted );
		} catch (IOException e) {
		    e.printStackTrace();
		}
	
	}
	public static File getTempDirectory() {
		Config conf = ConfigImpl.getInstance();
		String tempfilepath = conf.getString("project.xml.path");
		FileUtil.checkDir(tempfilepath);
		return new File(new String(tempfilepath.getBytes()));
	}
	
	  public static void main(String[] args) throws Exception {
		  getFileCreatStamp();
	  }
	   
	   
}
