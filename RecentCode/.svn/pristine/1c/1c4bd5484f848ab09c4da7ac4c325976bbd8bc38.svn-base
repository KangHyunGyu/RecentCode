package com.e3ps.groupware.service;

import java.util.Hashtable;

import com.e3ps.common.jdf.log.Logger;
import com.e3ps.common.log4j.Log4jPackages;

import wt.services.ServiceFactory;

public class WPFormHelper {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.GROUPWARE.getName());
	/** singleton service instance */
	public static final WPFormService service = ServiceFactory.getService(WPFormService.class);
	
	/**
	 * 결재 페이지에서 회람시 메일 발송
	 * @param hash
	 */
	public static void sendMail(Hashtable hash) {
		Logger.info.println("sendMail(Hashtable hash) 메일 발송 구현 바람");
	}
	
	/**
	 * 문자열중 지정한 문자열을 찾아서 새로운 문자열로 바꾸는 함수 origianl 대상 문자열 oldstr 찾을 문자열 newstr 바꿀 문자열 return 바뀐 결과
	 */
	public static String replace(String original, String oldstr, String newstr) {
		String convert = new String();
		int pos = 0;
		int begin = 0;
		pos = original.indexOf(oldstr);

		if (pos == -1)
			return original;

		while (pos != -1) {
			convert = convert + original.substring(begin, pos) + newstr;
			begin = pos + oldstr.length();
			pos = original.indexOf(oldstr, begin);
		}
		convert = convert + original.substring(begin);

		return convert;
	}

	/**
	 * 내용중 HTML 툭수기호인 문자를 HTML 특수기호 형식으로 변환합니다. htmlstr 바꿀 대상인 문자열 return 바뀐 결과 PHP의 htmlspecialchars와 유사한 결과를
	 * 반환합니다.
	 */
	public static String convertHtmlchars(String htmlstr) {
		String convert = new String();
		convert = replace(htmlstr, "<", "&lt;");
		convert = replace(convert, ">", "&gt;");
		convert = replace(convert, "\"", "&quot;");
		convert = replace(convert, "&nbsp;", "&amp;nbsp;");
		return convert;
	}
}
