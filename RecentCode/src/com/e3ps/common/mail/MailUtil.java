/**
 * @(#) MailUtil.java
 * Copyright (c) e3ps. All rights reserverd
 * 
 *	@version 1.00
 *	@since jdk 1.4.02
 *	@createdate 2005. 3. 3..
 *	@author Cho Sung Ok, jerred@e3ps.com
 *	@desc	
 */

package com.e3ps.common.mail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import wt.content.ApplicationData;
import wt.content.ContentHolder;
import wt.content.StreamData;
import wt.content.Streamed;
import wt.fc.LobLocator;
import wt.fc.ObjectReference;
import wt.fc.PersistenceHelper;
import com.e3ps.common.jdf.config.ConfigEx;
import com.e3ps.common.jdf.config.ConfigExImpl;
import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.StringUtil;


public class MailUtil {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
	public static final MailUtil manager = new MailUtil();
	static final boolean VERBOSE = ConfigImpl.getInstance().getBoolean("develop.verbose", false); 
	static final boolean enableMail = ConfigImpl.getInstance().getBoolean("e3ps.mail.enable", true);
	protected MailUtil(){}


	public boolean sendMail(Hashtable<String, Object> hash) {
		
		ConfigExImpl conf = ConfigEx.getInstance("eSolution");
		
		if(enableMail){
			HashMap to = (HashMap)hash.get("TO");
			String subject = (String)hash.get("SUBJECT");
			String content = (String)hash.get("CONTENT");
			Vector attache = (Vector)hash.get("ATTACHE");

			try {
				
				String mailTo = conf.getString("email.admin.mailTo");
				String mailToName = conf.getString("email.admin.name");
				
				SendMail mail = new SendMail() ;

				mail.setFromMailAddress(mailTo, mailToName);
				if (to != null && to.size() > 0) {
					Object[] objArr = to.keySet().toArray();
					String emails = "";
					String toname = "";
					for ( int i = 0 ; i < objArr.length ; i++ ) {
						emails = (String)objArr[i];
						toname = (String)to.get(emails);

						if(emails==null || emails.indexOf("@") < 0 )
							continue;

						mail.setToMailAddress(emails, toname);
					}

				} else {
					throw new MailException("받는 사람 설정오류");
				}

				mail.setSubject(subject);

				String message = " Text 메일 메시지 내용 " ; 
				String htmlMessage = "<html><font color='red'> HTML 메일 메시지 내용</font></html>" ;
				// String[] fileNames = { "c:/attachFile1.zip","c:/attachFile2.txt"   } ;
				String[] fileNames = { };
				if(attache != null){
					fileNames = new String[attache.size()];
					for(int i = 0 ; i < attache.size() ; i++){
						fileNames[i] = (String) attache.get(i);
								
					}
				}

				if( content != null ) { 
					mail.setHtmlAndFile(content,fileNames);
				} else {
					mail.setHtmlAndFile(htmlMessage,fileNames);
				}
				
				
				//mail.setHtml(htmlMessage);
				//mail.setText(message);  

				/**
				 * @Todo 개인 서버에서 주석처리함.
				 */
				mail.send();  // 메일 전송 

				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * @desc	: 주성 메일
	 * @author	: tsuam
	 * @date	: 2019. 7. 31.
	 * @method	: sendApprovalMail
	 * @return	: void
	 */
	public static boolean sendWorldexMail(Hashtable<String, Object> hash) {
		
		ConfigExImpl conf = ConfigEx.getInstance("eSolution");
		
		if(enableMail){

			try {
				
				String mailTo = conf.getString("email.admin.mailTo");
				String mailToName = conf.getString("email.admin.name");
				String subject = (String)hash.get("subject");
				String content = (String)hash.get("content");
				String toName = StringUtil.checkNull((String)hash.get("toName"));
				String toMail = StringUtil.checkNull((String)hash.get("toMail"));
				Vector attache = (Vector)hash.get("attache");
				
				String htmlMessage = "<html><font color='red'> HTML 메일 메시지 내용</font></html>" ;
				String[] fileNames = { };
				if(attache != null){
					fileNames = new String[attache.size()];
					for(int i = 0 ; i < attache.size() ; i++){
						fileNames[i] = (String) attache.get(i);
								
					}
				}
				
				SendMail mail = new SendMail() ;
				
				if(toName.length() ==0 ) {
					throw new MailException("받는 사람 설정오류");
				}
				
				mail.setSubject(subject);
				mail.setFromMailAddress(mailTo, mailToName);
				mail.setToMailAddress(toMail, toName);
				if( content != null ) { 
					mail.setHtmlAndFile(content,fileNames);
				} else {
					mail.setHtmlAndFile(htmlMessage,fileNames);
				}

				/**
				 * @Todo 개인 서버에서 주석처리함.
				 */
				mail.send();  // 메일 전송 

				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}else{
			return false;
		}
		
	}

	private File getFile(ContentHolder contentholder,ApplicationData applicationdata)
	throws Exception {
		//ContentHolder contentholder = applicationdata.getHolderLink().getContentHolder();
		Streamed streamed = (Streamed)PersistenceHelper.manager.refresh(applicationdata.getStreamData().getObjectId());
		LobLocator loblocator = null;
		if(streamed instanceof StreamData)
		{
			applicationdata = (ApplicationData)PersistenceHelper.manager.refresh(applicationdata);
			streamed = (Streamed)PersistenceHelper.manager.refresh(applicationdata.getStreamData().getObjectId());
			try
			{
				loblocator.setObjectIdentifier(((ObjectReference)streamed).getObjectId());
				((StreamData)streamed).setLobLoc(loblocator);
			}
			catch(Exception exception) { }
		}

		String tempDir = System.getProperty("java.io.tmpdir");
		InputStream in = streamed.retrieveStream();
		File attachfile = new File(tempDir+File.separator+applicationdata.getFileName());		// 파일 저장 위치 
		FileOutputStream fileOut = new FileOutputStream(attachfile);
		byte[] buffer = new byte[1024];	
		int c;
		while ( ( c = in.read(buffer) ) != -1 ) fileOut.write(buffer,0,c);
		fileOut.close();

		return attachfile;
	}
	
	
	public static String getHtmlTemplate(String mailType, String url, String subject, String author, String title, String startDate, String creator) throws Exception{
		return getHtmlTemplate( mailType, url, subject, author, title, startDate, creator, false);
	}

	public static String getHtmlTemplate(String mailType, String url, String subject, String author, String title, String startDate, String creator, boolean work) throws Exception{
		
		/**
		 * type : ApprovalMail
		<@url>
		<@subject>
		<@author>
		<@title>
		<@startDate>
		<@creator>
		 */
		
		MailHtmlContentTemplate template = MailHtmlContentTemplate.getInstance();
		
		
		StringBuffer content = new StringBuffer();
		
		String approve = "결재가";
		String approve2 = "결재";
		
		if(work){
			approve = "작업이";
			approve2 = "작업";
		}
		
		if( mailType.equals("pressingApproval") ) {
			content.append(approve+" 지연되고 있습니다.");
			content.append(" <BR> "+approve2+" 요청에 대한 빠른 처리 바랍니다.");
		} else if( mailType.equals("requestApproval")) {
			content.append(approve+" 요청되었습니다.");
			content.append(" <BR> "+approve2+" 요청에 대한 처리 바랍니다.");
		} else if( mailType.equals("create")) {
			content.append(approve+" 등록되었습니다.");
			content.append(" <BR> "+approve2+"에 대한 확인 바랍니다.");
		} else {
			content.append(approve+" 완료 되었습니다.");
			content.append(" <BR> "+approve2+" 완료에 대한 확인 바랍니다.");
		}
		
		Hashtable hash = new Hashtable();
		
		hash.put("SUBJECT", subject);
		hash.put("TO", author);
		hash.put("CONTENT", content.toString());
		hash.put("TITLE", title);
		hash.put("STARTDATE", startDate);
		hash.put("CREATOR", creator);
		if(url!=null){
			url = URLEncoder.encode(url);
			hash.put("URL", url);
		}
		
		return template.htmlContent(hash,"ApprovalMail.html");
	}
}
