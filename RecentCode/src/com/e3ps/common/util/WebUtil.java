package com.e3ps.common.util;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;

import wt.httpgw.WTURLEncoder;
import wt.util.WTProperties;

import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.web.ParamHash;

/**
 * 
 * @author Choi Seunghwan, skyprda@e3ps.com
 * @version 1.00, 2007. 07. 23
 * @since 1.4
 */
public class WebUtil {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
	/**
	 * HTML INPUT 태그의 타입
	 * @author Administrator
	 *
	 */
	public enum HtmlInputType {
		hidden, text, checkbox, radio
	}
	
	private static boolean isDecode = ConfigImpl.getInstance().getBoolean("http.parameter.isdecode");

	public static URL host;

	public static URL getHost() {
		try {

			if (host == null) {
				WTProperties props = WTProperties.getLocalProperties();
				host = props.getServerCodebase();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return host;
	}

	/**
	 * Request로 받은 key, value를 Hashtabled으로 반환한다.
	 * 
	 * @param req
	 * @return
	 */

	public static ParamHash getHttpParamsFromAjax(HttpServletRequest req) {
		ParamHash returnHash = new ParamHash();
		Enumeration eu = req.getParameterNames();
		String key = null;
		String[] value = null;

		while (eu.hasMoreElements()) {
			key = (String) eu.nextElement();
			value = req.getParameterValues(key);
			if (value == null)
				returnHash.put(key, "");
			else if (value.length == 1) {
				returnHash.put(key, WTURLEncoder.decode(value[0]));
			} else {
				String[] returnValue = new String[value.length];
				for (int i = 0; i < value.length; i++) {
					returnValue[i] = WTURLEncoder.decode(value[i]);
				}
				returnHash.put(key, returnValue);
			}
		}

		return returnHash;

	}

	public static ParamHash getHttpParams(HttpServletRequest req) {
		ParamHash returnHash = new ParamHash();
		Enumeration eu = req.getParameterNames();
		String key = null;
		String[] value = null;
		while (eu.hasMoreElements()) {
			key = (String) eu.nextElement();

			value = req.getParameterValues(key);
			if (value == null)
				returnHash.put(key, "");
			else if (value.length == 1) {
				if (isDecode)
					returnHash.put(key, CharUtil.E2K(value[0]));
				else
					returnHash.put(key, value[0]);
			} else {
				if (isDecode) {
					String[] returnValue = new String[value.length];
					for (int i = 0; i < value.length; i++) {
						returnValue[i] = CharUtil.E2K(value[i]);
					}
					returnHash.put(key, returnValue);
				} else {
					returnHash.put(key, value);
				}
			}
		}
		return returnHash;
	}

	public static String getHtml(String ss) throws Exception {
		if (ss == null)
			return "";

		ss = ss.replaceAll("<", "&lt;");
		ss = ss.replace(">", "&gt;");

		ss = ss.replaceAll("\n", "<br>");
		return ss;
	}
	
	public static String getHtmls(String ss) throws Exception {
		if (ss == null)
			return "";

		ss = ss.replaceAll("\n", "<br>");
		return ss;
	}

	/**
	 * 파라미터의 이름을 변경한다.
	 * @param ht
	 * @param oldName
	 * @param newName
	 */
	public static void renameParameter(Hashtable ht, String oldName, String newName) {
		Object obj = ht.get(oldName);
		if (obj == null)
			return;

		ht.put(newName, obj);
		ht.remove(oldName);
	}

	/**
	 * paramName 값을 String[]로 변환한다.
	 * @param ht
	 * @param paramName
	 * @param newName
	 */
	public static void toStringArray(Hashtable ht, String paramName, String newName) {
		Object obj = ht.get(paramName);
		if (obj == null)
			return;

		if (obj != null) {
			if (obj instanceof String[]) {
				ht.put(paramName, obj);
			} else if (obj instanceof Vector) {
				Vector vt = (Vector) obj;
				String[] array = new String[vt.size()];
				for (int i = 0; i < vt.size(); i++) {
					array[i] = (String) vt.get(i);
				}

				ht.put(paramName, array);
			} else {
				ht.put(paramName, new String[] { (String) obj });
			}
		}

		if (newName != null) {
			renameParameter(ht, paramName, newName);
		}
	}

	public static void toUpperCase(Hashtable ht, String paramName) {
		Object obj = ht.get(paramName);
		try {
			if (obj == null)
				return;
			if (obj instanceof String) {
				String toUpper = (String) obj;
				ht.put(paramName, toUpper.toUpperCase());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void print(Hashtable ht) {
		java.util.Iterator it = ht.keySet().iterator();

		TreeMap tm = new TreeMap();

		while (it.hasNext()) {
			String key = (String) it.next();

			tm.put(key, ht.get(key));
		}

		LOGGER.info("############################################");
		int cnt = 0;
		it = tm.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			LOGGER.info(++cnt + ". " + key + " = " + ht.get(key));
		}
		LOGGER.info("############################################");
	}
	
	public static String getOptionsHtml(List texts, String initValue) {
		StringBuilder sb = new StringBuilder();
		if (texts != null) {
			for (Object text : texts) {
				if(text instanceof String[]){
					String[] textValue = (String[])text;
					appendOptionHtml(sb, textValue[0], textValue[1], initValue).append("\n");
				}else{
					appendOptionHtml(sb, (String)text, (String)text, initValue).append("\n");
				}
			}
		}
		return sb.toString();
	}
	public static String getOptionsHtml(HttpServletRequest request, String modelName, String initValue) {
		List texts = (List)request.getAttribute(modelName);
		return getOptionsHtml(texts, initValue);
	}
	
	/**
	 * Select 태그에 사용되는 Option태그들의 HTML 을 얻는다
	 * @param objects
	 * @param textIndex
	 * @param valueIndex
	 * @param initValue
	 * @return
	 */
	public static String getOptionsHtml(List<String[]> textValueArrays, int textIndex, int valueIndex, String initValue) {
		StringBuilder sb = new StringBuilder();
		
		if (textValueArrays != null && textIndex >= 0) {
			for (String[] textValueArray : textValueArrays) {
				if (textIndex >= 0 && textIndex < textValueArray.length) {
					String text = textValueArray[textIndex];
					if (text != null && text.trim().length() > 0) { // text 값이 유효하면 option 추가...
						String value = null;
						if (valueIndex >= 0 && valueIndex < textValueArray.length) {
							value = textValueArray[valueIndex];
						}
						appendOptionHtml(sb, text, value, initValue).append("\n");
					}
				}
			}
		}
		return sb.toString();
	}
	public static String getOptionsHtml(HttpServletRequest request, String modelName, int textIndex, int valueIndex, String initValue) {
		List<String[]> textValueArrays = (List<String[]>)request.getAttribute(modelName);
		return getOptionsHtml(textValueArrays, textIndex, valueIndex, initValue);
	}
	
	public static String createOptionsHTML(
			int startNo, int endNo, int interval, String numberFormat, String initValue) {
		StringBuilder sb = new StringBuilder();
		
		DecimalFormat df = null;
		if (numberFormat != null && numberFormat.length() > 0) {
			df = new DecimalFormat(numberFormat);
		}
		
		for (int i = startNo; i <= endNo; i += interval) {
			String opText = (df != null) ? df.format(i) : String.valueOf(i);
			String opValue = opText;
			
			appendOptionHtml(sb, opText, opValue, initValue);
		}
		return sb.toString();
	}
	/**
	 * Option HTML 를 추가한다
	 * @param sb
	 * @param text
	 * @param value
	 * @param initValue
	 */
	private static StringBuilder appendOptionHtml(StringBuilder sb, String text, String value, String initValue) {
		if (sb != null) {
			// #. 인자 초기화
			if (text != null) {
				text = text.trim();
			} else {
				text = "";
			}
			if (value != null) {
				value = value.trim();
			} else {
				value = text;
			}
			// #. 선택여부
			String selected = "";
			if (initValue != null) {
				initValue = initValue.trim();
				// #. 유효한 초기값일때만 비교
				if (initValue.length() > 0 && value.equals(initValue)) { 
					selected = " selected=\"selected\"";
				}
			}
			
			// #. html 추가
			sb.append("<OPTION value=\"").append(value).append("\"").append(selected).append(">")
				.append(text).append("</OPTION>");
		}
		return sb;
	}
	
	public static String getJSONStringValue(JSONObject jsonObject, String key) {
		return getJSONStringValue(jsonObject, key, "");
	}
	
	public static String getJSONStringValue(JSONObject jsonObject, String key, String defaultValue) {
		String value = defaultValue;
		try {
			// #. 인자 체크
			if (jsonObject == null || key == null) {
				return value;
			}
			key = key.trim();
			if (key.length() == 0) {
				return value;
			}
			
			int cutIndex = key.lastIndexOf("."); 
			if (cutIndex < 0) {
				// #. 값을 찾음
				if (jsonObject.has(key) && !jsonObject.isNull(key)) {
					value = jsonObject.getString(key);
				}
			} else {
				// #. 객체를 먼저 찾음
				String objectKeyTokens = key.substring(0, cutIndex);
				String attrKey = key.substring(cutIndex + 1);
				JSONObject parentJsonObject = findJSONObject(jsonObject, objectKeyTokens);
				if (parentJsonObject != null) {
					// #. 값을 찾음
					if (parentJsonObject.has(attrKey) && !parentJsonObject.isNull(attrKey)) {
						value = parentJsonObject.getString(attrKey);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	private static JSONObject findJSONObject(JSONObject jsonObject, String objectKeyTokens) {
		try {
			// #. arguments 체크
			if (jsonObject == null || objectKeyTokens == null) {
				return null;
			}
			objectKeyTokens = objectKeyTokens.trim();
			if (objectKeyTokens.length() == 0) {
				return null;
			}
			
			// #. 재귀적으로 json 객체 찾기
			int cutIndex = objectKeyTokens.indexOf(".");
			if (cutIndex < 0) {
				// #. child 가 없다면 리턴
				String key = objectKeyTokens;
				if (jsonObject.has(key) && !jsonObject.isNull(key)) {
					return jsonObject.getJSONObject(key);
				}
			} else {
				// #. child 가 있다면
				String key = objectKeyTokens.substring(0, cutIndex);
				if (jsonObject.has(key) && !jsonObject.isNull(key)) {
					JSONObject parentJsonObject = jsonObject.getJSONObject(key);
					String childKeyTokens = objectKeyTokens.substring(cutIndex + 1);
					JSONObject childJsonObject = findJSONObject(parentJsonObject, childKeyTokens);
					if (childJsonObject != null) {
						return childJsonObject;
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
}
