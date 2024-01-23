package com.e3ps.common.drm;

import java.io.File;
import java.io.IOException;

import com.e3ps.common.log4j.Log4jPackages;

import wt.util.WTProperties;

public class E3PSDRMFileDeleteScheduler {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
    /**
     * @param args
     */
    public static void main(String[] args) {

//        wt.method.RemoteMethodServer remotemethodserver = wt.method.RemoteMethodServer.getDefault();
//       
//        remotemethodserver.setUserName(adminId);
//        remotemethodserver.setPassword(adminPw);
        LOGGER.info("E3PSDRMFileDeleteScheduler START============");
        E3PSDRMFileDeleteScheduler.manager.doDelete();
        LOGGER.info("E3PSDRMFileDeleteScheduler END   ============");
    }
    
    public static E3PSDRMFileDeleteScheduler manager = new E3PSDRMFileDeleteScheduler();
    
    public static void doDelete(){
    	try{
	        E3PSDRMHelper.drmInit();
	        String orgFolderStr = E3PSDRMHelper.orgFileTempFolder;
	        String destFolderStr = E3PSDRMHelper.destFileTempFolder;
	        String wtTempFolderStr = WTProperties.getServerProperties().getProperty("wt.temp");
	        LOGGER.info("orgFolderStr : " + orgFolderStr);
	        LOGGER.info("destFolderStr : " + destFolderStr);
	        LOGGER.info("wtTempFolderStr : " + wtTempFolderStr);
	        File orgFolder = new File(orgFolderStr);
	        File destFolder = new File(destFolderStr);
	        File wtTempFolder = new File(wtTempFolderStr);
	        if(orgFolder.isDirectory()){
	            File[] temp = orgFolder.listFiles();
	            for(int i=0; i<temp.length; i++){
	                System.out.print("[DELETE] " +temp[i].getAbsolutePath());
	                temp[i].delete();
	                LOGGER.info("   ..... done.");
	            }
	        }
	        if(destFolder.isDirectory()){
	            File[] temp = destFolder.listFiles();
	            for(int i=0; i<temp.length; i++){
	                System.out.print("[DELETE] " +temp[i].getAbsolutePath());
	                temp[i].delete();
	                LOGGER.info("   ..... done.");
	            }
	        }
	        if(wtTempFolder.isDirectory()){
	            File[] temp = wtTempFolder.listFiles();
	            for(int i=0; i<temp.length; i++){
	                System.out.print("[DELETE] " +temp[i].getAbsolutePath());
	                temp[i].delete();
	                LOGGER.info("   ..... done.");
	            }
	        }
    	}catch(IOException ioe){
    		ioe.printStackTrace();
    	}
    }

}
