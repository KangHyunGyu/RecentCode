package com.e3ps.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.e3ps.common.log4j.Log4jPackages;

public class TypeUtil {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
	/**
	 * 객체에 해당되는 String을 얻는다
	 * 
	 * @param obj 적용할 객체
	 * @param initValue 초기값
	 * @retrun String값
	 */
	public static String stringValue(Object obj, String initValue) {
		String value = null;
		if (obj != null) {
			if (obj.getClass().equals(String.class)) {
				value = (String) obj;
			} else {
				value = obj.toString();
			}
		}
		
		// trim 처리
		if (value != null) {
			value = value.trim();
			if (value.length() > 0) {
				return value;
			} else {
				return initValue;
			}
		} else {
			return initValue;
		}
	}

	/**
	 * 객체에 해당되는 String을 얻는다
	 * 
	 * @param obj 적용할 객체
	 * @retrun String값
	 */
	public static String stringValue(Object obj) {
		return stringValue(obj, "");
	}

	/**
	 * 객체에 해당되는 int를 얻는다
	 * 
	 * @param obj 적용할 객체
	 * @param initValue 초기값
	 * @retrun int값
	 */
	public static int intValue(Object obj, int initValue) {
		if (obj != null) {
			try {
				if (obj.getClass().equals(String.class)) {
					// #. modified by gryu (2014.05.28) : 1.0 과 같은 string이 int 로 되도록 변경
					//return Integer.parseInt((String) obj);
					return (int)floatValue(obj, initValue);
				} else if (obj.getClass().equals(Integer.class)) {
					return ((Integer) obj).intValue();
				} else {
					// #. modified by gryu (2014.05.28) : 1.0 과 같은 string이 int 로 되도록 변경
					//return Integer.parseInt(obj.toString());
					return (int)floatValue(obj, initValue);
				}
			} catch (NumberFormatException e) {
				// e.printStackTrace();
			}
		}
		return initValue;
	}

	/**
	 * 객체에 해당되는 int를 얻는다
	 * 
	 * @param obj 적용할 객체
	 * @retrun int값
	 */
	public static int intValue(Object obj) {
		return intValue(obj, 0);
	}

	/**
	 * 객체에 해당되는 long를 얻는다
	 * 
	 * @param obj 적용할 객체
	 * @param initValue 초기값
	 * @retrun long값
	 */
	public static long longValue(Object obj, long initValue) {
		if (obj != null) {
			try {
				if (obj.getClass().equals(String.class)) {
					// #. modified by gryu (2014.05.28) : 1.0 과 같은 string이 long 로 되도록 변경
					// return Long.parseLong((String) obj);
					return (long)doubleValue(obj, initValue);
				} else if (obj.getClass().equals(Long.class)) {
					return ((Long) obj).longValue();
				} else {
					// #. modified by gryu (2014.05.28) : 1.0 과 같은 string이 long 로 되도록 변경
					// return Long.parseLong(obj.toString());
					return (long)doubleValue(obj, initValue);
				}
			} catch (NumberFormatException e) {
				// e.printStackTrace();
			}
		}
		return initValue;
	}

	/**
	 * 객체에 해당되는 long를 얻는다
	 * 
	 * @param obj 적용할 객체
	 * @retrun long값
	 */
	public static long longValue(Object obj) {
		return longValue(obj, 0L);
	}

	/**
	 * 객체에 해당되는 float를 얻는다
	 * 
	 * @param obj 적용할 객체
	 * @param initValue 초기값
	 * @retrun float값
	 */
	public static float floatValue(Object obj, float initValue) {
		if (obj != null) {
			try {
				if (obj.getClass().equals(String.class)) {
					return Float.parseFloat((String) obj);
				} else if (obj.getClass().equals(Float.class)) {
					return ((Float) obj).floatValue();
				} else {
					return Float.parseFloat(obj.toString());
				}
			} catch (NumberFormatException e) {
				// e.printStackTrace();
			}
		}
		return initValue;
	}

	/**
	 * 객체에 해당되는 float를 얻는다
	 * 
	 * @param obj 적용할 객체
	 * @retrun float값
	 */
	public static float floatValue(Object obj) {
		return floatValue(obj, 0f);
	}

	/**
	 * 객체에 해당되는 double를 얻는다
	 * 
	 * @param obj 적용할 객체
	 * @param initValue 초기값
	 * @retrun double값
	 */
	public static double doubleValue(Object obj, double initValue) {
		if (obj != null) {
			try {
				if (obj.getClass().equals(String.class)) {
					return Double.parseDouble((String) obj);
				} else if (obj.getClass().equals(Double.class)) {
					return ((Double) obj).doubleValue();
				} else {
					return Double.parseDouble(obj.toString());
				}
			} catch (NumberFormatException e) {
				// e.printStackTrace();
			}
		}
		return initValue;
	}

	/**
	 * 객체에 해당되는 double를 얻는다
	 * 
	 * @param obj 적용할 객체
	 * @retrun double값
	 */
	public static double doubleValue(Object obj) {
		return doubleValue(obj, 0d);
	}

	/**
	 * 객체에 해당되는 date를 얻는다
	 * 
	 * @param obj 적용할 객체
	 * @param initValue 초기값
	 * @retrun date값
	 */
	public static Date dateValue(Object obj, String pattern, Date initValue) {
		if (obj != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(pattern);
				if (obj.getClass().equals(String.class)) {
					return sdf.parse((String) obj);
				} else {
					return sdf.parse(obj.toString());
				}
			} catch (ParseException e) {
				// e.printStackTrace();
			}
		}
		return initValue;
	}

	/**
	 * 객체에 해당되는 date를 얻는다
	 * 
	 * @param obj 적용할 객체
	 * @retrun date값
	 */
	public static Date dateValue(Object obj) {
		return dateValue(obj, "yyyy-MM-dd", Calendar.getInstance().getTime());
	}

	/**
	 * 객체에 해당되는 long를 얻는다
	 * 
	 * @param obj 적용할 객체
	 * @param initValue 초기값
	 * @retrun long값
	 */
	public static boolean booleanValue(Object obj, boolean initValue) {
		if (obj != null) {
			String value = obj.toString().trim();
			if (value.equalsIgnoreCase(Boolean.TRUE.toString())
					|| value.equalsIgnoreCase(Boolean.FALSE.toString())) {
				return Boolean.parseBoolean(value);
			}
		}
		return initValue;
	}

	/**
	 * 객체에 해당되는 long를 얻는다
	 * 
	 * @param obj 적용할 객체
	 * @retrun long값
	 */
	public static boolean booleanValue(Object obj) {
		return booleanValue(obj, false);
	}
}
