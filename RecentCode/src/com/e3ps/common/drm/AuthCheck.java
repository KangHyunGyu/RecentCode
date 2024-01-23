package com.e3ps.common.drm;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.doc.E3PSDocument;

import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.org.OrganizationServicesHelper;
import wt.org.WTUser;
import wt.session.SessionContext;
import wt.session.SessionHelper;
import wt.util.WTException;

public class AuthCheck implements RemoteAccess{
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	public static DrmAuth getAuth(String userId, String contentHolderId)throws Exception{
		
		 if (!RemoteMethodServer.ServerFlag)
	        {
	            try
	            {
	                RemoteMethodServer rms = RemoteMethodServer.getDefault();
	                Class[] argTypes = { String.class, String.class };
	                Object[] argValues = {userId,  contentHolderId};
	                Object obj = rms.invoke("getAuth", AuthCheck.class.getName(), null, argTypes, argValues);
	                
	                return (DrmAuth)obj;
	                
	            }
	            catch (Exception ex)
	            {
	                ex.printStackTrace();
	                throw new WTException(ex);
	            }
	        }

	        SessionContext sessioncontext = SessionContext.newContext();
	        
	        int accessType = 0;
	        try
	        {
	            SessionHelper.manager.setAdministrator();
	            
	            String startDate = "0";
	            String endDate = "0";
	            
	            Object obj = CommonUtil.getObject(contentHolderId);
	            boolean isAccess = false;
	            
	            
	            if(obj instanceof E3PSDocument) {

	            	E3PSDocument document = (E3PSDocument)obj;
	            	  
	            	WTUser wtuser = OrganizationServicesHelper.manager.getUser(userId);
	            		            	
	            }else{
	            	accessType = 8;
	            }
	            DrmAuth drmauth = new DrmAuth(accessType, "", startDate, endDate);
	            return drmauth;
	        }finally
		    {
		        SessionContext.setContext(sessioncontext);
		    }
		
	}
}
