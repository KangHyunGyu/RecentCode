package com.e3ps.common.restful.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.util.DateUtil;

import wt.services.ServiceFactory;

public class RestFulHelper {
	public static final RestFulHelper manager = new RestFulHelper();
	public static final RestFulService service = ServiceFactory.getService(RestFulService.class);
	
	
	private static String host = ConfigImpl.getInstance().getString("HR.HOST.URL");		//url
	private static String p1 = ConfigImpl.getInstance().getString("HR.CUSTOMERKEY.P1");	//월덱스 회사 Customer Key
	private static String p2;
	private static String C_CD = ConfigImpl.getInstance().getString("HR.COMPANY.CODE");	//회사 HR 코드
	
	private static String hrInfoKey = ConfigImpl.getInstance().getString("HR.API.REQUESTSERVICEKEY.01");
	private static String codeInfoKey = ConfigImpl.getInstance().getString("HR.API.REQUESTSERVICEKEY.02");
	private static String deptInfoKey = ConfigImpl.getInstance().getString("HR.API.REQUESTSERVICEKEY.03");
	
	
	public static String getHrHost() {
		return host;
	}
	
	public static String getHrC_Cd() {
		return C_CD;
	}
	
	public static String getHrP1() {
		return p1;
	}
	
	public static String getHrP2() {
		return p2;
	}
	
	public static String getHrInfoKey() {
		return hrInfoKey;
	}
	
	public static String getCodeInfoKey() {
		return codeInfoKey;
	}
	
	public static String getDeptInfoKey() {
		return deptInfoKey;
	}
	
	public static String setHrP2(String v) {
		return p2 = v;
	}
	
	public static String getToken(){
		String token = "";
		try {
			URL obj = new URL(host+"/restful/getToken");
			HttpURLConnection con = (HttpURLConnection)obj.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
	        String inputLine;
	        StringBuffer response = new StringBuffer();
	        while((inputLine = in.readLine()) != null){
	            response.append(inputLine);
	        }
	        in.close();

	        String jsonStr = response.toString();
	        JSONParser parser = new JSONParser();
	        JSONObject jObj = (JSONObject) parser.parse(jsonStr);
	        token = (String) jObj.get("Token");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return token;
	}
	
	
	@SuppressWarnings("unchecked")
	public JSONObject getKeyObject() throws Exception {
		JSONObject top = new JSONObject();
    	
    	top.put("p1",URLEncoder.encode(getHrP1(),"UTF-8")); // Customer Key
        top.put("p2",URLEncoder.encode(getHrP2(),"UTF-8")); // Request Service Key
        top.put("p3",URLEncoder.encode(getToken(),"UTF-8")); // Access Token
    	
    	return top;
	}
	
	
	@SuppressWarnings("unchecked")
	public JSONArray getJsonData(String p2, String type, String codeIndex) throws Exception {
		setHrP2(p2);
		JSONObject top = getKeyObject();
		JSONObject param = getParam(type, codeIndex);
		
		top.put("PARAM", param);
		
		String jsonStr = "jsonData="+top.toJSONString();
        byte[] postDataBytes = jsonStr.getBytes("UTF-8");
        
        URL obj = new URL(getHrHost()+"/restful");//데이터 처리를 요청하는 url
        HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();
        
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuffer response = new StringBuffer();
        String inputLine;
        while((inputLine = in.readLine()) != null){
        	response.append(inputLine);
        }
        in.close();

        String json = response.toString();
        //System.out.println("요청응답데이터:"+json);
        
        Object objList = JSONValue.parse(json);
        JSONObject jsonObjList = (JSONObject)objList;
        JSONArray array = (JSONArray)jsonObjList.get("Data");
        
		return array;
	}
	
	
	
	public JSONArray getJsonData(String p2, String type) throws Exception{
		return getJsonData(p2, type, "");
	}
	
	
	@SuppressWarnings("unchecked")
	public JSONObject getParam(String type, String codeIndex) throws Exception{
		JSONObject param = new JSONObject();
		
		if("user".equals(type)) {
			param.put("SEARCH_COND", getHrC_Cd()+"^"+DateUtil.getToDay("yyyyMMdd"));
		}else if("duty".equals(type)) {
			param.put("C_CD", getHrC_Cd());
			param.put("IDX_CD", codeIndex);
		} else {
			
		}
		
		return param;
	}
	
}
