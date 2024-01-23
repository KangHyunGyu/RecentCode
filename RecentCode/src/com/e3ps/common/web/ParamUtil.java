/**
* 모듈명             	: com.e3ps.common.web
* 프로그램 명       	: ParamUtil
* 기능설명           	: request parameter 처리
* 프로그램 타입   	: Util
* 비고 / 특이사항	:

*/

package com.e3ps.common.web;

import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CharUtil;

/**
 */
public class ParamUtil {
    private static boolean isDecode = false;//ConfigImpl.getInstance ().getBoolean ( "http.parameter.isdecode" );

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
    /**
     * 
     * @param str
     * @param defaultStr
     * @return
     * @author yhjang1
     * @since 2014. 12. 15.
     */
    public static String getStrParameter(String str, String defaultStr) {
        if (str == null || str.equals ( "" )){
        	return defaultStr;
        }
        else {
            if (isDecode){
            	return CharUtil.E2K ( str.trim () );
            }
            else{
            	return str.trim ();
            }
        }
    }

    /**
     * 
     * @param str
     * @return
     * @author yhjang1
     * @since 2014. 12. 15.
     */
    public static String getStrParameter(String str) {
        if (str == null || str.equals ( "" )){
        	return "";
        }
        else {
            if (isDecode){
            	return CharUtil.E2K ( str.trim () );
            }
            else{
            	return str.trim ();
            }
        }
    }

    /**
     * 
     * @param str
     * @param defaultStr
     * @return
     * @author yhjang1
     * @since 2014. 12. 15.
     */
    public static String checkStrParameter(String str, String defaultStr) {
        if (str == null || str.equals ( "" )){
        	return defaultStr;
        }
        else{
        	return str.trim ();
        }
    }

    /**
     * 
     * @param str
     * @return
     * @author yhjang1
     * @since 2014. 12. 15.
     */
    public static String checkStrParameter(String str) {
        if (str == null || str.equals ( "" )){
        	return "";
        }
        else{
        	return str.trim ();
        }
    }

    /**
     * 
     * @param str
     * @return
     * @author yhjang1
     * @since 2014. 12. 15.
     */
    public static String[] getStrParameterValues(String[] str) {
        String[] defaultReturnValue = {};
        if (str == null){
        	return defaultReturnValue;
        }
        if (str.length == 0){
        	return defaultReturnValue;
        }

        if (isDecode) {
            String[] returnValue = new String[str.length];
            for (int i = 0; i < str.length; i++) {
                returnValue[i] = CharUtil.E2K ( str[i] );
            }
            return returnValue;
        } else {
            return str;
        }
    }

    /**
     * 
     * @param str
     * @param ifNulltoReplace
     * @return
     * @author yhjang1
     * @since 2014. 12. 15.
     */
    public static int getIntParameter(String str, int ifNulltoReplace) {
        try {
            if (str == null || str.equals ( "" )){
            	return ifNulltoReplace;
            }
            else{
            	return Integer.parseInt ( str.toString () );
            }
        } catch (NumberFormatException e) {
            return ifNulltoReplace;
        }
    }

    /**
     * 
     * @param str
     * @return
     * @author yhjang1
     * @since 2014. 12. 15.
     */
    public static int getIntParameter(String str) {
        try {
            if (str == null || str.equals ( "" )){
            	return 0;
            }
            else{
            	return Integer.parseInt ( str.toString () );
            }
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * @see     getParameter(HttpServletRequest request, String name, String defaultVal = "")
     */
    public static String getParameter(HttpServletRequest request, String name) {
        return getParameter(request, name, "");
    }
    

    /**
     * 
     * @param request
     * @param name
     * @param defaultVal
     * @return
     * @author yhjang1
     * @since 2014. 12. 15.
     */
    public static String getParameter(HttpServletRequest request, String name, String defaultVal) {
        String temp = request.getParameter(name);
        if(temp != null) {
            return temp.trim();
        } else {
            return defaultVal;
        }
    }
    
    
    /**
     * hash 를 이용해 파라미터 값을 가져옴
     * @param hash
     * @param key
     * @return
     */
    public static int getInt(Map<String,Object> hash,String key){
		String rr = get(hash,key);
		
		try{
			return Integer.parseInt(rr);
		}catch(NumberFormatException ex){
			LOGGER.error(ex.toString());
		}
		
		
		return 0;
	}
	
    /**
     * 
     * @param hash
     * @param key
     * @return
     * @author yhjang1
     * @since 2014. 12. 15.
     */
	public static double getDouble(Map<String,Object> hash,String key){
		String rr = get(hash,key);
		
		try{
			return Double.parseDouble(rr);
		}catch(NumberFormatException ex){
			LOGGER.error(ex.toString());
		}
		
		return 0;
	}
	
	/**
	 * 
	 * @param hash
	 * @param key
	 * @return
	 * @author yhjang1
	 * @since 2014. 12. 15.
	 */
	public static boolean getBoolean(Map<String,Object> hash,String key){
		String rr = get(hash,key);
		return "true".equals(rr);
	}
 
	/**
	 * 
	 * @param hash
	 * @param key
	 * @return
	 * @author yhjang1
	 * @since 2014. 12. 15.
	 */
    public static String get(Map<String,Object> hash, String key){
    	Object o = hash.get(key);
    	
    	if(o instanceof String[]){
    		String[] ss = (String[])hash.get(key);
    		if(ss==null){
    			return null;
    		}
    		return ss[0];
    	}
    	else if(o instanceof Vector){
    		Vector v = (Vector)o;
    		return (String)v.get(0);
    	}else if(o instanceof String){
    		return (String)o;
    	}
    	
    	return "";
    	
    }
    
    /**
     * 
     * @param hash
     * @param key
     * @return
     * @author yhjang1
     * @since 2014. 12. 15.
     */
    public static String[] getValues(Map<String,Object> hash, String key){
    	Object o = hash.get(key);
    	try{
	    	if(o instanceof String[]){
	    		return (String[])o;
	    	}
	    	else if(o instanceof Vector){
	    		Vector<String> v = (Vector<String>)o;
	    		String[] result = new String[v.size()];
	    		int count = 0;
	    		for(String s : v){
	    			result[count++] = s;
	    		}
	    		return result;
	    	}else if(o instanceof String){
	    		return new String[]{(String)o};
	    	}
	    	
    	}catch(ClassCastException e){
    		LOGGER.error("ERROR",e);
    	}
    	return new String[]{};
    }
}
