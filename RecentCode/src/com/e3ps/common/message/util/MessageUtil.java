package com.e3ps.common.message.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import com.e3ps.common.code.NumberCode;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.StringUtil;

import wt.session.SessionHelper;
import wt.util.WTException;

public class MessageUtil{
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
	static String MESSAGE_FILE_LOCATION;
	static Map<String,String> messageKey;
	
	static{
		try {
			MESSAGE_FILE_LOCATION = "D:\\ptc\\e3ps\\MessageResource.txt";
			
			messageKey = getMessageKOKey();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * <pre>
	 * @description Locale 문자열
	 * @author dhkim
	 * @date 2016. 6. 20. 오전 10:56:00
	 * @method getLocaleString
	 * @return String
	 * @throws WTException
	 * </pre>
	 */
	public static String getLocaleString() throws WTException {
	    return SessionHelper.manager.getLocale().toString();
	}
	
	
	/**
	 * <pre>
	 * @description Locale
	 * @author dhkim
	 * @date 2016. 6. 20. 오전 10:56:17
	 * @method getLocale
	 * @return Locale
	 * </pre>
	 */
	public static Locale getLocale() {
	    try {
			return Locale.KOREA;//SessionHelper.manager.getLocale();
		} catch (Exception e) {
			System.err.println("Locale을 받아올 수 없습니다.");
		}
	    return Locale.KOREA;
	}
	
	/**
	 * <pre>
	 * @description 한글 메시지 키 로드
	 * @author dhkim
	 * @date 2016. 6. 20. 오전 10:56:38
	 * @method getMessageKOKey
	 * @return Map<String,String>
	 * </pre>
	 */
	public static Map<String,String> getMessageKOKey(){
		
		Map<String,String> messageMap = new HashMap<String,String>();
		
		ResourceBundle bundle = ResourceBundle.getBundle("com.e3ps.common.message.MessageResources", Locale.KOREA);
		
		Enumeration<String> keyList = bundle.getKeys();
		
		while(keyList.hasMoreElements()){
			
			String key = keyList.nextElement();
			
			messageMap.put(bundle.getString(key), key);
		}
		
		return messageMap;
	}
	
	/**
	 * <pre>
	 * @description 메시지 가져오기
	 * @author dhkim
	 * @date 2016. 6. 20. 오전 11:10:34
	 * @method getMessage
	 * @param key
	 * @return String
	 * </pre>
	 */
	public static String getMessage(String key) {
		
		String display = key;
		
		if(messageKey.containsKey(key)){
			
			ResourceBundle bundle = ResourceBundle.getBundle("com.e3ps.common.message.MessageResources", getLocale());
			
			display = bundle.getString(messageKey.get(key));
			
		}else{
			
			setMessageResource(key);
//			display = "M.L"+display;

		}
		
		return display;
	}
	
	/**
	 * <pre>
	 * @description MessageResource에 없는 메시지 리스트 업
	 * @author dhkim
	 * @date 2016. 6. 20. 오전 10:57:23
	 * @method setMessageResource
	 * @param key
	 * </pre>
	 */
	public static void setMessageResource(String key){
		PrintWriter out = null;
		BufferedReader in = null;
		
		File outFile = new File(MESSAGE_FILE_LOCATION);
		
		try {
			
			if(!outFile.exists()){
				outFile.createNewFile();
			}
			
		} catch (IOException e) {
			
			try {
				String location = MESSAGE_FILE_LOCATION;//"C:\\Message\\MessageResource.txt";
				
				File folder = new File("D:\\ptc\\e3ps");
				
				if(!folder.exists()){
					folder.mkdirs();
				}
				
				outFile = new File(location);
				outFile.createNewFile();
			
			} catch (IOException e1) {
				return;
			}
		}
		
		try {
			
			in = new BufferedReader(new InputStreamReader(new FileInputStream(outFile), "EUC-KR"));
			String read = "";
			int Cnt = messageKey.size() + 1;
			
			while((read = in.readLine()) != null){
				
				if(read.indexOf("=") >= 1){
					
					String value = read.substring(read.indexOf("=") + 1,read.length());
					
					if(key.equals(value)){
						return;
					}
					Cnt++;
				}
			}

			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile, true), "EUC-KR")));
			
			//#E3PS.MSG.1 - 문서분류
			//E3PS.MSG.1 = 문서분류

			out.println("#E3PS.MSG." + Cnt + " - " + key);
			out.println("E3PS.MSG." + Cnt + "=" + key);
			out.println("");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(out!=null){
				out.close();
			}
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * NumberCode 다구겅 처리
	 * @methodName : getNCName
	 * @author : tsuam
	 * @date : 2021.11.10
	 * @return : String
	 * @description :
	 */
	public static String getNCName(NumberCode code){
		
		String display = code.getName();
		
		if(!isLangKor()){
			display = code.getEngName();
			
			if(!StringUtil.checkString(display)){
				display = code.getName();
				
			}
		}
		
		return display;
	}
	
	public static boolean isLangKor(){
		
		boolean isLangKor = true;
		
		if(!Locale.KOREA.toString().equals(getLocale().toString())){
			isLangKor = false;
		}
		
		return isLangKor;
	}
	
	public static void main(String[] args) throws Exception{
		
		
	}
}