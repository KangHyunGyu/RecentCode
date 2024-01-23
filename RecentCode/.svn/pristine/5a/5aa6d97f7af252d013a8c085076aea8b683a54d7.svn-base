package com.e3ps.common.message;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.e3ps.common.log4j.ObjectLogger;
import com.e3ps.common.util.StringUtil;
import com.google.gson.JsonElement;

public class PLMMsgConfigJSON {

	static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PLMMsgConfigJSON.class);

	private static String PATH = "D:\\ptc\\Windchill_12.1\\Windchill\\wtCustom\\com\\e3ps\\common\\message\\";
	public static String PATH_KO = PATH + "esolution_message_ko.json";
	public static String PATH_EN = PATH + "esolution_message_en.json";

	private JSONObject KO;
	private JSONObject EN;

	public enum LANG {
		ko, ko_KR, en, en_US
	}

	private PLMMsgConfigJSON() {
		this.initialize();
	}

	/**
	 * 
	 * @Method Name : initialize
	 * @작성일 : 2020. 12. 28
	 * @작성자 : mjroh
	 * @Method 설명 : 생성자
	 */
	private void initialize() {
		BufferedReader br = null;
		InputStreamReader isr = null;
		FileInputStream fis = null;
		try {
			
			JSONParser parser = new JSONParser();
			
			fis = new FileInputStream(PATH_KO);
			isr = new InputStreamReader(fis, "UTF-8");
			br = new BufferedReader(isr);
			Object obj = parser.parse(br);
			KO = (JSONObject) obj;
			if (KO == null) {
				KO = new JSONObject();
			}
			
			fis = new FileInputStream(PATH_EN);
			isr = new InputStreamReader(fis, "UTF-8");
			br = new BufferedReader(isr);
			obj = parser.parse(br);
			EN = (JSONObject) obj;
			if (EN == null) {
				EN = new JSONObject();
			}
			
			System.out.println("PLMMsgConfigJSON :::: 다국어 Initialize !");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 클래스 로딩 시점에서 생성
	private static final PLMMsgConfigJSON instance = new PLMMsgConfigJSON();

	public static PLMMsgConfigJSON getInstance() {
		return PLMMsgConfigJSON.instance;
	}

	/**
	 * 
	 * @Method Name : getMessage
	 * @작성일 : 2020. 12. 22
	 * @작성자 : mjroh
	 * @Method 설명 : 메세지 가져오기
	 * @param key
	 * @param lang
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public synchronized String getMessage(String key, String lang) {
		String value = "";
		
		value = key;
		
		
		if (StringUtil.isNullEmpty(lang)) {
			log.error("lang is null");
			return key;
		}

		// trim
		key = key.trim();
		if (LANG.ko.toString().equals(lang) || LANG.ko_KR.toString().equals(lang)) {
			if(KO == null) {
				value = key;
			}else {
				value = (String) KO.get(key);
			}
		} else {
			value = (String) EN.get(key);
		}

		if (value == null) {
			// ko일때 없는지 확인
			value = (String) KO.get(key);
			if (value == null) {
				// 없으면 추가
				addMessage(key);

				value = (String) KO.get(key);
			}
		}
		// log.error("lang : " + lang + ", key : " + key + ", return : " + value);
		
		return value;
	}

	/**
	 * 
	 * @Method Name : addMessage
	 * @작성일 : 2020. 12. 22
	 * @작성자 : mjroh
	 * @Method 설명 : 메세지 저장 (utf-8)
	 * @param key
	 */
	@SuppressWarnings("unchecked")
	public synchronized void addMessage(String key) {
		Writer file = null;
		try {
			KO.put(key, key);
			ObjectLogger.debug(key,"메세지 추가");
			// encoding : UTF-8
			file = new FileWriterWithEncoding(PATH_KO, "UTF-8");
			Set<String> hashSet = KO.entrySet();
			
			
			String writeStr = KO.toJSONString();
			//writeStr = writeStr.replaceAll("\\{", "\\{\n");
			//writeStr = writeStr.replaceAll("\",", "\",\n");
			//writeStr = writeStr.replaceAll("\\}", "\n\\}");
			
			ObjectLogger.debug(writeStr,"Json Object");
			file.write(writeStr);
			file.flush();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void deleteAllMessage() {
		FileWriter file = null;

		try {
			KO = new JSONObject();

			file = new FileWriter(PATH_KO);
			file.write(KO.toJSONString());
			file.flush();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public JSONObject getKO() {
		return KO;
	}

	public void setKO(JSONObject kO) {
		KO = kO;
	}

	public JSONObject getEN() {
		return EN;
	}

	public void setEN(JSONObject eN) {
		EN = eN;
	}

	/**
	 * 
	 * @Method Name : getCopySRMMsgConfigJSON
	 * @작성일 : 2021. 1. 5
	 * @작성자 : mjroh
	 * @Method 설명 : 기존 인스턴스 대신 카피 인스턴스를 가져옴
	 * @return
	 */
	public PLMMsgConfigJSON newInstance() {
		PLMMsgConfigJSON obj = new PLMMsgConfigJSON();

		return obj;
	}
}
