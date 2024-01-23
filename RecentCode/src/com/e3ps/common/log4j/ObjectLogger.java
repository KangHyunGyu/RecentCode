package com.e3ps.common.log4j;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;

import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.org.WTUser;
import wt.session.SessionHelper;
import wt.util.WTException;

/**
 * @file : ObjectLogger.java
 * @author : hckim
 * @date : 2021.09.23
 * @description : 객체 로깅 클래스
 */
public class ObjectLogger {

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(ObjectLogger.class);
	private static String loggingMsg;

	private ObjectLogger() {
	}

	/**
	 * @methodName : systemOut
	 * @author : hckim
	 * @date : 2021.11.12
	 * @return : void
	 * @description : System.out 출력 메소드. 콘솔 창 확인용으로만 쓰십시오. 실 운영 사용 X
	 */
	public static void systemOut(Object obj) {
		console(obj, "");
		
	}
	private static void console(Object obj, String comment) {
		makeMessage(obj, comment);
		System.out.println(loggingMsg);
	}
	
	public static void debug(Object obj) {
		debugLogging(obj, "");
	}

	public static void debug(Object obj, String comment) {
		debugLogging(obj, comment);
	}

	public static void info(Object obj) {
		infoLogging(obj, "");
	}

	public static void info(Object obj, String comment) {
		infoLogging(obj, comment);
	}

	public static void error(Object obj) {
		errorLogging(obj, "");
	}

	public static void error(Object obj, String comment) {
		errorLogging(obj, comment);
	}

	/**
	 * @methodName : debugLogging
	 * @author : hckim
	 * @date : 2021.11.10
	 * @return : void
	 * @description :
	 */
	private static void debugLogging(Object object, String comment) {

		if (logger.isDebugEnabled()) {
			makeMessage(object, comment);
			logger.debug(loggingMsg);
			append("ㅇㅇㅇㅇㅇㅇㅇㅇㅇDEBUG ENDㅇㅇㅇㅇㅇㅇㅇㅇㅇ");
		}
	}

	/**
	 * @methodName : infoLogging
	 * @author : hckim
	 * @date : 2021.11.10
	 * @return : void
	 * @description :
	 */
	private static void infoLogging(Object object, String comment) {
		if (logger.isInfoEnabled()) {
			makeMessage(object, comment);
			logger.info(loggingMsg);
			append("ㅇㅇㅇㅇㅇㅇㅇㅇㅇINFO ENDㅇㅇㅇㅇㅇㅇㅇㅇㅇ");
		}
	}

	/**
	 * @methodName : errorLogging
	 * @author : hckim
	 * @date : 2021.11.10
	 * @return : void
	 * @description :
	 */
	private static void errorLogging(Object object, String comment) {
		makeMessage(object, comment);
		logger.error(loggingMsg);
		append("ㅇㅇㅇㅇㅇㅇㅇㅇㅇERROR ENDㅇㅇㅇㅇㅇㅇㅇㅇㅇ");
	}

	private static void makeMessage(Object object, String comment) {

		try {

			loggingMsg = "";

			append("ㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁ START");

			StackTraceElement element = Thread.currentThread().getStackTrace()[4];
			append(getRequesterInfo());
			append("[클래스 : " + element.getClassName() + "]");
			append("[메소드 : " + element.getMethodName() + ":" + element.getLineNumber() + "]");

			if (comment.trim().length() > 0) {
				append("[메세지 : " + comment + "]");
			}

			if (object != null) {
				objLogging(object);
			}

		} catch (Exception e) {
			logger.debug("로깅 에러 : " + e.getLocalizedMessage() + " : " + object);
			e.printStackTrace();
		}

	}
	
	private static String append(Object print) {

		loggingMsg += print.toString() + "\n";

		return loggingMsg;
	}
	
	
	@SuppressWarnings("rawtypes")
	private static void objLogging(Object object) throws Exception {
		
		if(object == null) {
			append("<NULL>");
			
		}else {
			
			if (object instanceof List<?>) {
				List<?> list = (List<?>) object;
				append("사이즈 : " + list.size() + " ("+list.getClass()+")");
				for (Object obj : list) {
					objLogging(obj);
				}

			} else if (object instanceof Map<?, ?>) {
				Map<?, ?> map = (Map<?, ?>) object;

				append("사이즈 : " + map.size() + " ("+map.getClass()+")");
				for (Entry<?, ?> entry : map.entrySet()) {

					append("KEY : " + entry.getKey());
					objLogging(entry.getValue());

				}

			} else if (object instanceof Set<?>) {

				Set<?> set = (Set<?>)object;
				Iterator<?> iter = set.iterator();
				append("사이즈 : " + set.size() + " ("+set.getClass()+")");
				while (iter.hasNext()) {
					objLogging(iter.next());
				}

			} else if (object instanceof Object[]) {
				
				//append(Arrays.toString((Object[]) object));
				
				Object[] obj = (Object[])object;
				append("사이즈 : " + obj.length + " ("+obj.getClass()+")");
				for(int i = 0; i < obj.length; i++) {
					objLogging(obj[i]);
				}

			} else if (object instanceof QueryResult) {

				QueryResult qr = (QueryResult) object;
				
				append("사이즈 : " + qr.size()+ " ("+qr.getClass()+")");
//				while (qr.hasMoreElements()) {
//					objLogging(qr.nextElement());
//					//if(qr.nextElement() instanceof Object[]) {
//					//	Object[] obj = (Object[]) qr.nextElement();
//					//	if (obj != null) {
//					//		objLogging(obj);
//					//	}
//					//}
//				}
				
			} 
//			else if (object instanceof FileRequest) {
//
//				FileRequest req = (FileRequest) object;
//
//				Hashtable has = req.getParameters();
//
//				Iterator it = has.keySet().iterator();
//				while (it.hasNext()) {
//					Object key = (Object) it.next();
//
//					objLogging("req " + key + " = " + has.get(key));
//				}
//
//			} 
			else if (object instanceof Persistable) {
				append(PersistenceHelper.getObjectIdentifier((Persistable) object).getStringValue());
			} else {
				append(object.toString());
			}
			
		}
	}
	

//	@SuppressWarnings("rawtypes")
//	private static void objLogging(Object object) throws Exception {
//
//		if (object instanceof List<?>) {
//			List<?> list = (List<?>) object;
//			append("사이즈 : " + list.size());
//			for (Object obj : list) {
//				append(obj);
//			}
//
//		} else if (object instanceof Map<?, ?>) {
//			Map<?, ?> map = (Map<?, ?>) object;
//
//			append("사이즈 : " + map.size());
//			for (Entry<?, ?> entry : map.entrySet()) {
//
//				append("KEY : " + entry.getKey());
//				append("VALUE : " + entry.getValue());
//
//			}
//
//		} else if (object instanceof Set<?>) {
//
//			Iterator<?> iter = ((Set<?>) object).iterator();
//			while (iter.hasNext()) {
//				append(iter.next());
//			}
//
//		} else if (object instanceof Object[]) {
//			append(Arrays.toString((Object[]) object));
//
//		} else if (object instanceof Object[][]) {
//			append(Arrays.toString((Object[][]) object));
//
//		} else if (object instanceof QueryResult) {
//
//			QueryResult qr = (QueryResult) object;
//			append("사이즈 : " + qr.size());
//
//			while (qr.hasMoreElements()) {
//				Object[] obj = (Object[]) qr.nextElement();
//				if (obj != null) {
//					append(Arrays.toString(obj));
//				}
//			}
//		} else if (object instanceof FileRequest) {
//
//			FileRequest req = (FileRequest) object;
//
//			Hashtable has = req.getParameters();
//
//			Iterator it = has.keySet().iterator();
//			while (it.hasNext()) {
//				Object key = (Object) it.next();
//
//				append("req " + key + " = " + has.get(key));
//			}
//
//		} else {
//			append(object);
//		}
//	}
//
//	private static String append(Object print) {
//
//		loggingMsg += print(print) + "\n";
//
//		return loggingMsg;
//	}
//
//	private static String print(Object obj) {
//
//		String returnVal = "";
//
//		try {
//
//			if (obj instanceof Persistable) {
//				returnVal = PersistenceHelper.getObjectIdentifier((Persistable) obj).getStringValue();
//
//			} else if (obj instanceof QuerySpec) {
//
//				QuerySpec spec = (QuerySpec) obj;
//				returnVal = spec.toString();
//
//			} else {
//				returnVal = String.valueOf(obj);
//			}
//
//		} catch (NullPointerException e) {
//			returnVal = "<NULL>" + e.getLocalizedMessage() + "\n";
//			StackTraceElement[] stack = e.getStackTrace();
//
//			for (StackTraceElement s : stack) {
//				returnVal += s.toString();
//			}
//
//		} catch (ClassCastException e) {
//			returnVal = "<Undefined Logging Class>" + e.getLocalizedMessage() + "\n";
//			StackTraceElement[] stack = e.getStackTrace();
//
//			for (StackTraceElement s : stack) {
//				returnVal += s.toString();
//			}
//		}
//
//		return returnVal;
//	}


	private static String getRequesterInfo() {
		
//		String[] IP_HEADER_CANDIDATES = { "X-FORWARDED-FOR", "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
//				"HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",
//				"HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR" };

		String userInfoString = "";
		
		try {
			
			String loginUser = "";
			try {
				WTUser user = (wt.org.WTUser) SessionHelper.manager.getPrincipal();
				loginUser = user.getFullName();
			} catch (WTException e) {
				loginUser = "알 수 없음";
			}
			userInfoString += "[유저 : " + loginUser + "]";
//
//			String ip = "";
//			if (RequestContextHolder.getRequestAttributes() == null) {
//				ip = "알 수 없음";
//			} else {
//				HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
//						.getRequest();
//
//				for (String header : IP_HEADER_CANDIDATES) {
//					ip = request.getHeader(header);
//					
//					if (ip != null && ip.length() > 0 && !"unknown".equalsIgnoreCase(ip)) {
//						ip = ip.split(",")[0];
//						break;
//					}
//					
//				}
//				if(ip == null) {
//					ip = request.getRemoteAddr();
//				}
//			}
//			userInfoString += "[IP : " + ip + "]";
//
//			if (ip != null && !"알 수 없음".equals(ip)) {
//				String deviceName = "";
//				try {
//					InetAddress inet = InetAddress.getByName(ip);
//					deviceName = inet.getHostName();
//				} catch (UnknownHostException e) {
//					deviceName = "알 수 없음";
//				}
//				userInfoString += "[장비명 : " + deviceName + "]";
//			}
			
		}catch(Exception e) {
			userInfoString += "[Request 정보 에러 : "+e.getLocalizedMessage()+"]";
			e.printStackTrace();
		}
		return userInfoString;
	}

}
