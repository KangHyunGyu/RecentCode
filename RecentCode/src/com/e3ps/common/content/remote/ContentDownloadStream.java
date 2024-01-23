package com.e3ps.common.content.remote;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import wt.content.ApplicationData;
import wt.content.ContentHolder;
import wt.content.ContentServerHelper;
import wt.content.HolderToContent;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.session.SessionContext;
import wt.session.SessionHelper;
import wt.util.WTException;
import wt.util.WTIOException;

import com.e3ps.common.util.CommonUtil;




public class ContentDownloadStream implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7201426360269959935L;
	/**
	 * 
	 */
	private static final int BUFSIZ = 8192;
	private transient Object applicationData;
	private transient boolean download;
    private transient FilterInputStream stream;
	
	public ContentDownloadStream(Object applicationData)
	{
	    download = false;
	    this.applicationData = applicationData;
	}
	
	public InputStream getInputStream(){
		return stream;
	}
	
	private void readObject(ObjectInputStream objectinputstream)
	    throws IOException, ClassNotFoundException
	{
	    boolean flag = objectinputstream.readBoolean();
	    if(flag)
	    {
	        ContentDownloadThread contentdownloadthread = (ContentDownloadThread)Thread.currentThread();
	        synchronized(contentdownloadthread)
	        {  
	        	
	            while(!contentdownloadthread.isReady()) {
	                try
	                {   
	                	
	                    contentdownloadthread.wait();
	                }
	                catch(InterruptedException interruptedexception) { }
	            }
	            
	            FilterInputStream filterinputstream = new FilterInputStream(objectinputstream) {
	                public void close()
	                {
	                }
	            };
	            
	            contentdownloadthread.setInputStream(filterinputstream);
	            contentdownloadthread.notifyAll();
	            
	            while(!contentdownloadthread.isReady()) { 
	                try
	                {
	                    contentdownloadthread.wait();
	                }
	                catch(InterruptedException interruptedexception1) { }
	            }
	        }
	    }
	    else
	    {
	        applicationData = (Object)objectinputstream.readObject();
	        download = true;
	    }
	    
	}
	
	private void writeObject(ObjectOutputStream objectoutputstream)
	    throws IOException
	{
	    objectoutputstream.writeBoolean(download);
	    if(download)
	    {   
	    	
	        InputStream inputstream = null;
	        try
	        {
	            inputstream = findContentStream(applicationData);
	            
	            byte abyte0[] = new byte[BUFSIZ];
	            int i;
	            while((i = inputstream.read(abyte0, 0, BUFSIZ)) > 0) {
	                objectoutputstream.write(abyte0, 0, i);
	            }
	        }
	        catch(WTException wtexception)
	        {
	            throw new WTIOException((String)null, wtexception);
	        }
	        finally
	        {
	            if(inputstream != null)
	                inputstream.close();
	        }
	    } else
	    {
	        objectoutputstream.writeObject(applicationData);
	    }
	}
	
	private InputStream findContentStream(Object applicationdata)throws WTException
	{   
		SessionContext sessioncontext = SessionContext.newContext();
      
        try
        {
            SessionHelper.manager.setAdministrator();
            if(applicationdata instanceof ApplicationData){
            	return ContentServerHelper.service.findContentStream((ApplicationData)applicationdata);
            }else if(applicationdata instanceof RequestData){
            	RequestData data = (RequestData)applicationdata;
            	String holderOid = data.holderOid;
        		String applicationOid = data.applicationOid;
        		
        		ContentHolder contentHolder = (ContentHolder)CommonUtil.getObject(holderOid);
        		
        		ApplicationData ad = (ApplicationData)CommonUtil.getObject(applicationOid);
        		
        		String fileName = ad.getFileName();
        		
        		
        		
        		
        		try {
					ad.setHolderLink(getHolderToContent(contentHolder, ad));
				
        		} catch (Exception e) {
					e.printStackTrace();
					throw new WTException(e);
				}
				return ContentServerHelper.service.findContentStream(ad);
            }
            else{
                try {
					return	new FileInputStream(new File((String)applicationdata));
				} catch (FileNotFoundException e) {
					
					e.printStackTrace();
					throw new WTException(e);
				}
            	
            }
            
		    
        }finally{
        	
        	SessionContext.setContext(sessioncontext);
        }
	   
	}
	
	private HolderToContent getHolderToContent(ContentHolder contentHolder, ApplicationData ad)throws Exception{
		
		QuerySpec spec = new QuerySpec(ApplicationData.class, wt.content.HolderToContent.class);
		
		spec.appendWhere(new SearchCondition(ApplicationData.class, "thePersistInfo.theObjectIdentifier.id", "=",  ad.getPersistInfo().getObjectIdentifier().getId()));
		
		
		QueryResult queryresult = PersistenceHelper.manager.navigate(contentHolder, "theContentItem" , spec, false);//(pp, "theContentItem", wt.content.HolderToContent.class, false);
		
		
		HolderToContent holdertocontent = (HolderToContent)queryresult.nextElement();
		
		return holdertocontent;
	}

}

/* $Log: ContentDownloadStream.java,v $
/* Revision 1.3  2017/11/16 08:36:07  jtpark
/* 2017.11.16 PJT ÇùÀÇ½Ã ÃÖÁ¾ÀûÀ¸·Î Ã¤¹ø·ê ÆäÀÌÁö ±×³É °íÁ¤À¸·Î ÇùÀÇ·Î ÀÎÇÑ º¹±¸
/*
/* Revision 1.1  2017/10/25 00:54:38  jtpark
/* *** empty log message ***
/*
/* Revision 1.1  2016/01/12 04:37:06  wslee
/* ÇÁ·ÎÁ§Æ®
/*
/* Revision 1.1  2015/12/08 04:16:22  wslee
/* *** empty log message ***
/*
/* Revision 1.4  2015/11/04 08:54:32  administrator
/* *** empty log message ***
/*
/* Revision 1.3  2015/10/13 01:32:43  administrator
/* *** empty log message ***
/*
/* Revision 1.2  2009/10/27 06:57:42  administrator
/*
/* Committed on the Free edition of March Hare Software CVSNT Server.
/* Upgrade to CVS Suite for more features and support:
/* http://march-hare.com/cvsnt/
/*
/* Revision 1.1  2009/08/11 04:16:22  administrator
/* Init Source
/* Committed on the Free edition of March Hare Software CVSNT Server.
/* Upgrade to CVS Suite for more features and support:
/* http://march-hare.com/cvsnt/
/*
/* Revision 1.1  2009/02/25 01:26:02  smkim
/* ï¿½ï¿½ï¿½ï¿½ ï¿½Û¼ï¿½
/* Committed on the Free edition of March Hare Software CVSNT Server.
/* Upgrade to CVS Suite for more features and support:
/* http://march-hare.com/cvsnt/
/*
/* Revision 1.3  2008/09/29 02:35:09  sjhan
/* *** empty log message ***
/*
/* Revision 1.2  2008/06/02 08:32:00  administrator
/* ï¿½ï¿½ï¿½ï¿½
/*
/* Revision 1.1  2008/03/12 04:51:01  hyun
/* content service ï¿½ï¿½ï¿½ï¿½
/*
/* Revision 1.2  2008/02/19 10:04:42  hyun
/* ï¿½ï¿½Â¾ï¿½Ã¼ ï¿½ï¿½ï¿½ï¿½ß¼ï¿½
/*
/* Revision 1.1  2007/12/12 06:44:20  plmadmin
/* *** empty log message ***
/*
/* Revision 1.1  2006/01/25 05:39:41  khkim
/* *** empty log message ***
/* */
