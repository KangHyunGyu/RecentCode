/**
* 모듈명             	: com.e3ps.common.util
* 프로그램 명       	: ControllerUtil
* 기능설명           	: 공통 유틸리티
* 프로그램 타입   	: util
* 비고 / 특이사항	:
*/

package com.e3ps.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.e3ps.common.content.FileRequest;
import com.e3ps.common.log4j.Log4jPackages;


public class ControllerUtil {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());

	/**
	 * action 수행 후 redirect를 하기 위해 사용한다.
	 * @param url 리다이렉트 URL
	 * @return ModelAndView
	 * @author yhjang1
	 * @since 2014. 12. 12.
	 */
	public static ModelAndView redirect(String url) {
		return redirect(url, "");
	}

	/**
	 * action 수행 후 redirect를 하기 위해 사용한다.
	 * @param url 리다이렉트 URL
	 * @param message 메시지
	 * @return ModelAndView
	 * @author yhjang1
	 * @since 2014. 12. 12.
	 */
	public static ModelAndView redirect(String url, String message) {
//		LOGGER.info("message="+message);
//		message = M.get(message); 
//		LOGGER.info("Convert message="+message);
		ModelAndView model = new ModelAndView("/jsp/project/redirect.jsp");
		model.addObject("url", url);
		if (message != null && message.length() > 0){
			model.addObject("message", replace(message));
		}
		return model;
	}
	
	/**
	 * action 수행 후 redirect를 하기 위해 사용한다.
	 * @param url 리다이렉트 URL
	 * @param paramMap 파라메터 맵
	 * @return ModelAndView
	 * @author yhjang1
	 * @since 2014. 12. 12.
	 */
	public static ModelAndView redirect(String url, Map<String, Object> paramMap) {
		return redirect(url, null, paramMap);
	}

	/**
	 * action 수행 후 redirect를 하기 위해 사용한다
	 * @param url
	 * @param message
	 * @param paramMap
	 * @return
	 */
	public static ModelAndView redirect(String url, String message, Map<String, Object> paramMap) {
//		message = M.get(message); //다국어 처리
		ModelAndView model = new ModelAndView("/jsp/project/redirect.jsp");
		model.addObject("url", url);
		if (message != null && message.length() > 0) {
			model.addObject("message", message);
		}
		if (paramMap != null) {
			model.addObject("paramMap", paramMap);
		}
		return model;
	}
	
	/**
	 * action 수행 후 redirect를 하기 위해 사용한다
	 * @param url
	 * @param request
	 * @param paramNames
	 * @return
	 */
	public static ModelAndView redirect(String url, HttpServletRequest request, List<String> paramNames) {
		return redirect(url, null, request, paramNames);
	}
	
	/**
	 * action 수행 후 redirect를 하기 위해 사용한다
	 * @param url
	 * @param message
	 * @param request
	 * @param paramNames
	 * @return
	 */
	public static ModelAndView redirect(String url, String message, HttpServletRequest request, List<String> paramNames) {
		ModelAndView model = new ModelAndView("/jsp/project/redirect.jsp");
		model.addObject("url", url);
		if (message != null && message.length() > 0) {
			model.addObject("message", message);
		}
		if (request != null && paramNames != null && paramNames.size() > 0) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			for (String paramName : paramNames) {
				String[] paramValues = request.getParameterMap().get(paramName);
				paramMap.put(paramName, paramValues);
			}
			model.addObject("paramMap", paramMap);
		}
		return model;
	}
	
	/**
	 * action 수행 후 redirect를 하기 위해 사용한다 (멀티파트용)
	 * @param url
	 * @param message
	 * @param request 파일 리퀘스트
	 * @param paramNames
	 * @return
	 */
	public static ModelAndView redirect(String url, String message, FileRequest request, List<String> paramNames) {
		ModelAndView model = new ModelAndView("/jsp/project/redirect.jsp");
		model.addObject("url", url);
		if (message != null && message.length() > 0) {
			model.addObject("message", message);
		}
		if (request != null && paramNames != null && paramNames.size() > 0) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			for (String paramName : paramNames) {
				String paramValue = request.getParameter(paramName);
				paramMap.put(paramName, paramValue);
			}
			model.addObject("paramMap", paramMap);
		}
		return model;
	}
	
	
	
	/**
	 * popup 창에서 action 수행 후 close를 하기 위해 사용한다.
	 * @param message
	 * @return ModelAndView
	 * @author yhjang1
	 * @since 2014. 12. 12.
	 */
	public static ModelAndView close(String message) {
//		message = M.get(message);	//다국어 처리
		ModelAndView model = new ModelAndView("/jsp/project/redirect.jsp");
		if (message != null && message.length() > 0) {
			model.addObject("message", replace(message));
		}
		model.addObject("close", true);
		return model;
	}

	/**
	 * message 변환 처리
	 * @param message
	 * @return String
	 * @author yhjang1
	 * @since 2014. 12. 12.
	 */
	private static String replace(String message) {
		message = message.replaceAll("\"","\\\\\"");
		message = message.replaceAll("\n","\\\\\\n");
		message = message.replaceAll("\r","\\\\\\r");
		return message;
	}
	
	public static ModelAndView reject(HttpServletRequest request){
		return ControllerUtil.redirect(request.getHeader("Referer"), "권한이 없습니다.");
	}
}
