package com.e3ps.common.message.util;

import java.util.Locale;

import com.e3ps.common.message.PLMMsgConfigJSON;
import com.e3ps.common.message.PLMMsgConfigJSON.LANG;
import com.e3ps.common.util.StringUtil;

import wt.session.SessionHelper;
import wt.util.WTException;

/**
 * @file	: MultiLangUtil.java
 * @author	: hckim
 * @date	: 2022.02.10
 * @description : PLM 다국어
 */
public class MultiLangUtil {
	
	/**
	 * 
	  * @Method Name : getMessage
	  * @작성일 : 2020. 12. 28
	  * @작성자 : hckim
	  * @Method 설명 : 다국어
	  * @param key
	  * @param lang (ko, ko_KR, en, en_US)
	  * @return
	 */
	public static String getMessage(String key, String lang) {
		PLMMsgConfigJSON msg = PLMMsgConfigJSON.getInstance();
		
		return msg.getMessage(key, lang);
	}
	
	/**
	 * 
	  * @Method Name : getSRMMessage
	  * @작성일 : 2020. 12. 28
	  * @작성자 : mjroh
	  * @Method 설명 : 다국어 (기본으로 한글 출력)
	  * @param key
	  * @param lang (ko)
	  * @return
	 */
	public static String getMessage(String key) {
		String message = "";
		if(StringUtil.isNullEmpty(key)){
			return message;
		}
		
		PLMMsgConfigJSON msg = PLMMsgConfigJSON.getInstance();
		
		message = msg.getMessage(key, getLang());
		
		if(StringUtil.isNullEmpty(message)) {
			message = msg.getMessage(key, LANG.ko.toString());
		}
		
		return message;
	}
	
	public static boolean isKor() {
		boolean isKor = false;
		String lang = getLang();
		
		if(LANG.ko.toString().equals(lang) || LANG.ko_KR.toString().equals(lang)) {
			isKor = true;
		}
		
		return isKor;
	}
	
	private static String getLang() {
		
		Locale locale = null;
		try {
			locale = SessionHelper.manager.getLocale();
		}catch(WTException wte) {
			locale = Locale.KOREA;
		}
		
		String lang = "";
		if(locale != null) {
			if(!StringUtil.isNullEmpty(locale.getLanguage())) {
				lang = locale.getLanguage();
			}
		}
		return lang;
	}
	
	public static boolean isKor(String lang) {
		boolean isKor = false;
		
		if(LANG.ko.toString().equals(lang) || LANG.ko_KR.toString().equals(lang)) {
			isKor = true;
		}
		
		return isKor;
	}

}
