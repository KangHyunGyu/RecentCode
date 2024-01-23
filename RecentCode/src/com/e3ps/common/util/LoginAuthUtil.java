package com.e3ps.common.util;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.e3ps.common.db.DBConnectionManager;
import com.e3ps.common.jdf.config.Config;
import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.log4j.Log4jPackages;
import com.infoengine.SAK.Task;
import com.infoengine.util.IEException;
import com.oreilly.servlet.Base64Encoder;

import wt.util.WTProperties;

/** 
 * AuthUtil ������� Utility Class
 * @author  Y. J. JEON
 * @version 1.0
 **/
public class LoginAuthUtil{
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
        
    private static boolean VERBOSE              = true;
    private static boolean DEFAULT_ENCODE_FLAG  = true;
    private static String HOSTNAME              = "";
    private static String WEBAPPNAME            = "";
    private static String CODEBASE              = "";
    private static WTProperties wtproperties    = null;
    private static Properties IEPROP            = new Properties();
    
    private static String JNDIADAPTER           = "";
    private static String LDAPHOST              = "";
    private static String LDAPPORT              = "";
    private static String LDAPURL               = "";
    private static String LDAPDN                = "";
    private static String LDAPPW                = "";
    private static String SEARCHBASE1            = "";
    private static String SEARCHBASE2            = "";
    
    private static boolean isInitialized        = false;
    private static DirContext ctx               = null;
   
    private static String host =  "";
    
    static{
    	Config conf = ConfigImpl.getInstance();
    	host =  conf.getString("HTTP.HOST.URL");
    }
            
    public static void initialize(){
        try{
            Hashtable userHash  = new Hashtable();          
            wtproperties    = WTProperties.getLocalProperties();
            HOSTNAME        = wtproperties.getProperty("java.rmi.server.hostname","");
            WEBAPPNAME      = wtproperties.getProperty("wt.webapp.name","");
            CODEBASE        = wtproperties.getProperty("wt.server.codebase","");

            // Windchill Properties
            JNDIADAPTER = wtproperties.getProperty("wt.federation.org.defaultAdapter","com.ptc.Ldap");
            
            // Customizing Properties
            //e3ps.common.jdf.config.Config myConfig = e3ps.common.jdf.config.ConfigImpl.getInstance();
            
            //DEFAULT_ENCODE_FLAG = (new Boolean(myConfig.getString("auth.isauth"))).booleanValue();
            LDAPPORT= "389";//myConfig.getString("ldap.port");

            // InfoEngine Properties
            IEPROP.load( new FileInputStream(wtproperties.getProperty("wt.federation.ie.propertyResource")) );
            String ldapSeeAlso  = IEPROP.getProperty("seeAlso");

            int startIdx    = ldapSeeAlso.indexOf("/")+2;
            int endIdx      = ldapSeeAlso.indexOf("@");

            String ieIdPw   = ldapSeeAlso.substring(startIdx, endIdx);

            LDAPDN          = ieIdPw.substring(0, ieIdPw.indexOf(":"));
			LOGGER.info("----->>>"+LDAPDN);
            LDAPPW          = "ldapadmin";//ieIdPw.substring(ieIdPw.indexOf(":")+1);
            ldapSeeAlso     = ldapSeeAlso.substring(endIdx+1);
            endIdx          = ldapSeeAlso.indexOf("/");
            LDAPHOST        = ldapSeeAlso.substring(0,endIdx);
            LDAPURL         = "ldap://"+LDAPHOST+":"+LDAPPORT;
//            SEARCHBASE      = "ou=people,"+ldapSeeAlso.substring(endIdx+1);
			SEARCHBASE1      = "ou=people,cn=AdministrativeLdap,cn=Windchill_10.1,o=ptc";
			SEARCHBASE2      = "ou=people,cn=EnterpriseLdap,cn=Windchill_10.1,o=ptc";

            // Setting the DirContext parameters
            userHash.put( Context.SECURITY_PRINCIPAL, LDAPDN );
            userHash.put( Context.SECURITY_CREDENTIALS, LDAPPW );
            userHash.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");                       
            userHash.put(Context.PROVIDER_URL, LDAPURL);
            //-- inform the LDAP provider that the value of 'loginAllowedTimeMap'           
            
            if( VERBOSE ){
                LOGGER.info("### AuthUtil initialize ###");
                LOGGER.info("==HOSTNAME : "+HOSTNAME);
                LOGGER.info("==WEBAPPNAME : "+WEBAPPNAME);
                LOGGER.info("==CODEBASE : "+CODEBASE);
                LOGGER.info("==SEARCHBASE1 : "+SEARCHBASE1);
                LOGGER.info("==SEARCHBASE2 : "+SEARCHBASE2);
                LOGGER.info("==LDAPDN : "+LDAPDN);
                LOGGER.info("==LDAPPW : "+LDAPPW);
                LOGGER.info("==JNDIADAPTER : "+JNDIADAPTER);
                LOGGER.info("==LDAPURL : "+LDAPURL);
                LOGGER.info("==DEFAULT_ENCODE_FLAG : "+DEFAULT_ENCODE_FLAG);
            }
            
            try{                    
                ctx             = new InitialDirContext(userHash);                  
                isInitialized   = true;
                
                if (VERBOSE) 
                    LOGGER.info("Jinyoung LdapService initialized...");
                
            }catch( NamingException ne ){
                if( ctx!=null )
                    ctx.close();                
                ne.printStackTrace();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }       
    }
    
    public static void main(String args[])throws Exception{
    	
    	getUserInfo("wcadmin");
    	LOGGER.info("wcadmin = " + getUserPassword("wcadmin"));
    	
    }
    
    
    private static Connection dbConnection() throws Exception{
    	
		DBConnectionManager conMgr = DBConnectionManager.getInstance();;
		Connection con = conMgr.getConnection("local");
		
		return con;
	}
    
    private static void disConnection(PreparedStatement pstmt,Connection con,ResultSet rs) throws SQLException{
		
		 if (pstmt != null) pstmt.close();
        if (con != null) con.close();
        if (rs != null) rs.close();
	}
    
    /** 
    * ����� ������ Ldap���� ���� �о� ���δ�.
    * @param    userId      (����ڻ��)     
    * @return   Attributes
    * @since    2005.02
    */
    public static Attributes getUserInfo(String userId){

        initialize();

        Attributes attrs    = null;
        String filter       = "uid="+userId+", "+SEARCHBASE1;

        try{
            attrs   = ctx.getAttributes(filter);

			if(attrs == null) {
				filter       = "uid="+userId+", "+SEARCHBASE2;
				attrs   = ctx.getAttributes(filter);
			}

            if( VERBOSE ){
                try{
                    LOGGER.info("### AuthUtil getUserInfo ###");
                    LOGGER.info("==uid : "+attrs.get("uid").get());
                    LOGGER.info("==sn : "+attrs.get("sn").get());
                    //LOGGER.info("==mail : "+attrs.get("mail").get());
                    LOGGER.info("==userPassword : "+attrs.get("userPassword").get());                
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                ctx.close();
            }catch(NamingException ne){}
        }
        return attrs;
    }
    
    
    /** 
    * ������� Windchill password�� ��ȯ�Ѵ�.(�ӽ�Password�� labeledUri�� ��ü��)
    * @param    userId      (����ڻ��) 
    * @param    encodeFlag  (��ȣȭ ��뿩��)
    * @return   String type�� ������� ���� Password ��(��ȣȭ���� ����)
    * @since    2005.02
    */
    public static String getUserPassword(String userId, boolean encodeFlag)
    throws Exception{       

        initialize();

        String password     = "";
        try{
            Attributes attrs    = getUserInfo(userId);
            if( attrs!=null ){
                String bulkString   = "http://";
                password    = attrs.get("userPassword")==null?"":attrs.get("userPassword").get().toString();                
                if(password.indexOf(bulkString)>-1){
                    password    = password.substring(bulkString.length());                  
                }
                if( encodeFlag )
                    password    = Base64.decodeToString(password);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return password;
    }
    
    
    /** 
    * ������� Windchill password�� ��ȯ�Ѵ�.
    * (Default�� ��ȣȭ ó���� �߰���).
    * @param    userId      (����ڻ��)     
    * @return   String type�� ������� ���� Password ��(��ȣȭ���� ����)
    * @since    2005.02
    * @see      getAuthorization(String userId, boolean encodeFlag)
    */
    public static String   getUserPassword(String userId){      
        String password     = "";
        try{
            password    = LoginAuthUtil.getUserPassword(userId, DEFAULT_ENCODE_FLAG);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return password;
    }
    
    
    /** 
    * ������� ID�� ��ȣ�� Ldap�� ���� ���Ͽ� ��ȿ������ �˻�   
    * @param    userId      (����ڻ��)     
    * @param    passwd      (�����Password)       
    * @return   String type�� ������� ���� Password ��(��ȣȭ���� ����)
    * @since    2005.02
    * @see      getAuthorization(String userId, boolean encodeFlag)
    */
//    public static boolean validatePassword(String userId, String passwd) throws Exception {
//        
//        initialize();
//        
//        boolean isValidUser = false;
//		String semUserObjectId = "uid="+userId+"," + SEARCHBASE1;
//        
//        try {           
//            LOGGER.info("validatePassword ... " + userId + "/" + passwd);
//            SearchControls ctls = new SearchControls();
//            ctls.setSearchScope( SearchControls.OBJECT_SCOPE );
//            ctls.setReturningAttributes( new String[0] );
//            
//        
//            String passStr  = "userPassword=" + (DEFAULT_ENCODE_FLAG ? passwd : passwd);
//            
//            if( VERBOSE ){
//                LOGGER.info("### semUserObjectId - " + semUserObjectId);
//                LOGGER.info("### validatePassword - "+passStr);
//                LOGGER.info("### validatePassword - SearchControls : "+ctls);
//                
//            }
//            NamingEnumeration sre = ctx.search( semUserObjectId, passStr, ctls );           
//
//			LOGGER.info("------->>>>>>>"+sre);
//			
//			if ( sre != null && sre.hasMoreElements()){
//                isValidUser = true;
//                SearchResult sResult    = (SearchResult)sre.nextElement();                  
//                Attributes attrs        = (Attributes)sResult.getAttributes();
//                ctx.modifyAttributes(semUserObjectId,2,attrs);
//            }          
//            
//            
//			LOGGER.info("------->>>>>>>"+isValidUser);
//        }catch(NamingException ne){
//
//			SearchControls ctls = new SearchControls();
//			ctls.setSearchScope( SearchControls.OBJECT_SCOPE );
//			ctls.setReturningAttributes( new String[0] );
//
//			String passStr  = "userPassword=" + (DEFAULT_ENCODE_FLAG ? passwd : passwd);
//			semUserObjectId = "uid="+userId+"," + SEARCHBASE2;
//
//			try{
//
//				NamingEnumeration sre = ctx.search( semUserObjectId, passStr, ctls );
//				LOGGER.info("-------###>>>>>>>"+sre);
//				
//				if ( sre != null && sre.hasMoreElements()){
//				isValidUser = true;
//				SearchResult sResult    = (SearchResult)sre.nextElement();                  
//				Attributes attrs        = (Attributes)sResult.getAttributes();
//				ctx.modifyAttributes(semUserObjectId,2,attrs);
//				}
//				
//				LOGGER.info("-------###>>>>>>>"+isValidUser);
//				
//	        }catch(NamingException ne2){
//				ne2.printStackTrace();
//				isValidUser = false;
//			}
//
//        }finally{
//            
//        }
//        
//        return isValidUser;
//    }
    
 public static boolean validatePassword(String userId, String passwd) throws Exception {
     boolean isAuth = false;
     String auth = userId + ":" + passwd;
     LOGGER.info("validatePassword..auth =" + auth);
	 try {
		 
			URL url = new URL(host + "/Windchill/jsp/loginOK.jsp");
			
			//LOGGER.info("validatePassword..1");
			String encoding = Base64Encoder.encode(auth);
						
			//LOGGER.info("validatePassword..2");
				
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", "Basic " + encoding);
			connection.setDoOutput(true);
			
						 
			//LOGGER.info("connection = " + connection);
			//LOGGER.info("connection.getResponseCode() = " + connection.getResponseCode());
			//LOGGER.info("connection.getInputStream() = " + connection.getInputStream());
			
			InputStream content = (InputStream) connection.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					content));
			String line = "kkkk";
			//LOGGER.info("validatePassword..3");
			if ((line = in.readLine()) != null) {
				LOGGER.info(line);
				
			}
			//LOGGER.info("validatePassword..4");
			 isAuth = "OK".equalsIgnoreCase(line);
			 
			 if(isAuth){
				 LOGGER.info("auth sucess..");
			 }else{
				 LOGGER.info("auth fail" + passwd);
			 }
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info(host + "  ,log in fail== " + auth);
		}
	 
	 	return isAuth;
    }
 	
 	/**
 	 * 
 	 * @desc	: 인증 영부 체크
 	 * @author	: tsuam
 	 * @date	: 2019. 9. 5.
 	 * @method	: isBasicAuth
 	 * @return	: boolean
 	 * @param request
 	 * @return
 	 */
	 public static boolean isBasicAuth(HttpServletResponse response){
	 	boolean isNoNAuth = true;
	 	try{
	 		
	 		Collection colist =response.getHeaderNames();
	 		isNoNAuth = colist.contains("Set-Cookie");
	 	
	 	}catch(Exception e){
	 		e.printStackTrace();
	 	}
	 	
	 	
	 	return isNoNAuth;
			
	 }
    
    public static void getAuthentication(String id, String pw){
        try{
            Hashtable userHash  = new Hashtable();

            // Setting the DirContext parameters
            userHash.put( Context.SECURITY_PRINCIPAL, LDAPDN );
            userHash.put( Context.SECURITY_CREDENTIALS, LDAPPW );
            userHash.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");                       
            userHash.put(Context.PROVIDER_URL, LDAPURL);
            
            LOGGER.info("### AuthUtil initialize ###");
            LOGGER.info("==LDAPDN : "+LDAPDN);
            LOGGER.info("==LDAPPW : "+LDAPPW);
            LOGGER.info("==LDAPURL : "+LDAPURL);
            
            try{                    
                ctx             = new InitialDirContext(userHash);                  
                isInitialized   = true;             
                LOGGER.info("############## LdapService initialized...");
            
            }catch( NamingException ne ){
                if( ctx!=null )
                    ctx.close();                
                ne.printStackTrace();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    
    

	/** 
    * ����� ������ �ش��ϴ� Ldap ������ �����Ѵ�.
    * @param    userId      (����ڻ��)     
    * @return   String
    * @since    2009.03
    */
    public static String getLdapInfo(String userId){

        initialize();

        Attributes attrs    = null;
		String SEARCHBASE = SEARCHBASE1;
        String filter       = "uid="+userId+", "+SEARCHBASE;

        try{
            attrs   = ctx.getAttributes(filter);

			if(attrs == null) {
				SEARCHBASE = SEARCHBASE2;
				filter       = "uid="+userId+", "+SEARCHBASE;
				attrs   = ctx.getAttributes(filter);
			}

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                ctx.close();
            }catch(NamingException ne){}
        }
        return SEARCHBASE;
    }

    public static Map<String, Boolean> checkPublicUser(String id){
    	
    	Map<String, Boolean> map = new HashMap<>();
    	
    	boolean idCheck = false;
    	boolean isPublic = false;
    	
    	if("wcadmin".equals(id) || "plmadmin".equals(id)) {
    		idCheck = true;
    		isPublic = true;
    	} else {
    		PreparedStatement pstmt = null;
        	Connection con = null;
        	boolean isdiscon = false;
        	ResultSet rs = null;
        	
    		try {
        		con = dbConnection();
    			isdiscon = true;
    			
    			StringBuffer sql = new StringBuffer();
    			sql.append("SELECT A0.ISPUBLIC FROM PEOPLE A0, WTUSER A1 ");
    			sql.append("WHERE A1.NAME = ? ");
    			sql.append("AND A1.IDA2A2 = A0.IDA3A4");
    			
    			pstmt = con.prepareStatement(sql.toString());
    			
    			pstmt.setString(1, id);
    		
    			rs= pstmt.executeQuery();
    			if(rs.next()){
    				idCheck = true;
    	    		isPublic = rs.getBoolean(1);
    			} else {
    				idCheck = false;
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		} finally{
                try{
                	if(isdiscon){
                		disConnection(pstmt, con, rs);
                	}else{
                		disConnection(pstmt, null, rs);
                	}
                }catch (SQLException e){
                	e.printStackTrace();
                }
            }
    	}
    	
    	map.put("idCheck", idCheck);
		map.put("isPublic", isPublic);
    	
    	return map;
    }
    
    public static boolean checkLoginAuth(HttpServletRequest request) {
		boolean result = false;
		try {
			String id = (String) request.getAttribute("j_username");
			String password = (String) request.getAttribute("j_password");
			String defaultAdapter = wt.federation.PrincipalManager.DirContext.getDefaultJNDIAdapter();
			WTProperties wtproperties = WTProperties.getServerProperties();
			String defaultDirectoryUser = wtproperties.getProperty("wt.federation.org.defaultDirectoryUser", wtproperties.getProperty("wt.admin.defaultAdministratorName"));
			
			String userDn = "uid=" + id + ",ou=people,cn=AdministrativeLdap,cn=Windchill_12.1,o=ptc";
			
			Task task = new Task("/wt/validateuser/ValidateUser.xml");
			task.addParam("instance", defaultAdapter);
	        task.addParam("user", userDn);
	        task.addParam("passwd", password);
	        
	        task.setUsername(defaultDirectoryUser);
	        
			result = true;
		} catch (IEException e) {
			result = false;
		} catch (IOException e) {
			result = false;
		}
		
		return result;
	}
    
}