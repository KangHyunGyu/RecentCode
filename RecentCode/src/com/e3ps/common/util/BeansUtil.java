package com.e3ps.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import wt.session.SessionHelper;
import wt.util.WTException;

import com.e3ps.common.code.NumberCode;
import com.e3ps.common.log4j.Log4jPackages;


public class BeansUtil {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
	public static final String PREFIX_SETTER = "set";
	public static final String PREFIX_GETTER = "get";

	private static final String[] GETTING_METHOD_PREFIXES = { PREFIX_GETTER, "is", "to" };

	/**
	 * 원본객체의 지정된 속성값을 대상객체에 할당한다.<br>
	 * 동일한 형의 객체이어야 한다
	 * @param srcObject 원본객체
	 * @param destObject 대상객체
	 * @param 속성명들
	 */
	public static void assignAttributes(
			Object srcObject, Object destObject, String[] attributeNames) {
		if (srcObject != null && destObject != null) {
			// #. same type validation
			if (srcObject.getClass() != destObject.getClass()) {
				return;
			}
			try {
				for (int i = 0; i < attributeNames.length; i++) {
					String attributeName = attributeNames[i];

					Object value = getAttributeValue(srcObject, attributeName);
					setAttributeValue(destObject, attributeName, value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 원복객체의 속성명 끝이 일련번호로 된 속성값을 대상객체에 채운다<br>]
	 * 동일한 클래스형 이어야 한다.<br>
	 *
	 * [PseudoCode]<br>
	 * assignAttributes(obj1, obj2, "attr_", 1, 10, "00"); // attr_01, attr_02 ~ attr_10
	 *
	 * @param srcObject 원본객체
	 * @param destObject 대상객체
	 * @param baseAttributeName 반복되는 기본 속성명
	 * @param begin 뒤에붙을 일련번호의 시작
	 * @param end 뒤에붙을 일련번호의 끝
	 * @param numberFormatPattern 뒤에붙을 일련번호의 포멧 (DecimalFormat API 참조)
	 */
	public static void assignAttributes(Object srcObject, Object destObject,
			String baseAttributeName, int begin, int end, String numberFormatPattern) {
		if (srcObject != null && destObject != null) {
			// #. same type validation
			if (srcObject.getClass() != destObject.getClass()) {
				return;
			}
			try {
				DecimalFormat df = new DecimalFormat(numberFormatPattern);
				for (int i = begin; i <= end; i++) {
					String attributeName = baseAttributeName + df.format(i);

					Object value = getAttributeValue(srcObject, attributeName);
					setAttributeValue(destObject, attributeName, value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 원본객체의 MapKey와 동일한 속성명의 값을 대상객체의 MapValue 와 동일한 속성에 할당한다.<br>
	 * 다른 형태의 객체도 가능하다
	 * @param srcObject
	 * @param destObject
	 * @param attributeMap 속성지정맵 (key:원본객체 속성명, value:대상객체의 속성명
	 */
	public static void assignAttributes(
			Object srcObject, Object destObject, Map attributeMap) {
		if (srcObject != null && destObject != null) {
			try {
				Iterator srcAttributeNames = attributeMap.keySet().iterator();
				while (srcAttributeNames.hasNext()) {
					String srcAttributeName = (String)srcAttributeNames.next();
					String destAttributeName = (String)attributeMap.get(srcAttributeName);

					Object value = getAttributeValue(srcObject, srcAttributeName);
					setAttributeValue(destObject, destAttributeName, value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 대상 객체의 지정된 속성에 특정 값을 채운다
	 * @param object
	 * @param value
	 */
	public static void fillAttributes(
			Object object, Object value, String[] attributeNames) {
		if (object != null) {
			try {
				for (int i = 0; i < attributeNames.length; i++) {
					String attributeName = attributeNames[i];

					setAttributeValue(object, attributeName, value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 대상 객체의 속성명 끝이 일련번호로 된 속성에 특정 값을 채운다
	 * @param object 대상객체
	 * @param value 채울값
	 * @param baseAttributeName 반복되는 기본 속성명
	 * @param begin 뒤에붙을 일련번호의 시작
	 * @param end 뒤에붙을 일련번호의 끝
	 * @param numberFormatPattern 뒤에붙을 일련번호의 포멧 (DecimalFormat API 참조)
	 */
	public static void fillAttributes(Object object, Object value,
			String baseAttributeName, int begin, int end, String numberFormatPattern) {
		if (object != null) {
			try {
				DecimalFormat df = new DecimalFormat(numberFormatPattern);
				for (int i = begin; i <= end; i++) {
					String attributeName = baseAttributeName + df.format(i);

					setAttributeValue(object, attributeName, value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 엑세스 메쏘드명을 얻는다
	 */
	private static String getAccessorMethodName(String accessPrefix, String attributeName) {
		if (attributeName != null && attributeName.length() > 0) {
			return accessPrefix +
					attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
		} else {
			return accessPrefix;
		}

	}

	/**
	 * 메쏘드 존재 여부
	 * @param object
	 * @param accessPrefix
	 * @param attributeName
	 * @return
	 */
	public static boolean existsAccessorMethod(Object object, String accessPrefix, String attributeName) {
		return existsAccessorMethod(object, accessPrefix, attributeName, null);
	}

	/**
	 * 메쏘드 존재 여부
	 * @param object
	 * @param accessPrefix
	 * @param attributeName
	 * @return
	 */
	public static boolean existsAccessorMethod(
			Object object, String accessPrefix, String attributeName, Class<?> argType) {
		Method method = getAccessorMethod(object, accessPrefix, attributeName, argType);
		return (method != null);
	}

	/**
	 * 메쏘드 조회
	 * @param object
	 * @param accessPrefix
	 * @param attributeName
	 * @return
	 */
	public static Method getAccessorMethod(Object object, String accessPrefix, String attributeName) {
		return getAccessorMethod(object, accessPrefix, attributeName, null);
	}

	/**
	 * 메쏘드 조회
	 * @param object
	 * @param accessPrefix
	 * @param attributeName
	 * @return
	 */
	public static Method getAccessorMethod(
			Object object, String accessPrefix, String attributeName, Class<?> argType) {
		String methodName = getAccessorMethodName(accessPrefix, attributeName);
		try {
			if (argType != null) {
				try {
					return object.getClass().getMethod(methodName, argType);
				} catch (NoSuchMethodException e) {
					if (argType.equals(Boolean.class)) {
						return object.getClass().getMethod(methodName, boolean.class);
					} else if (argType.equals(Integer.class)) {
						return object.getClass().getMethod(methodName, int.class);
					} else if (argType.equals(Long.class)) {
						return object.getClass().getMethod(methodName, long.class);
					} else if (argType.equals(Float.class)) {
						return object.getClass().getMethod(methodName, float.class);
					} else if (argType.equals(Double.class)) {
						return object.getClass().getMethod(methodName, double.class);
					}
				}
			} else {
				if (accessPrefix.equalsIgnoreCase(PREFIX_SETTER)) {
					return findMethodByName(object, methodName);
				} else {
					return object.getClass().getMethod(methodName);
				}
			}
		} catch (NoSuchMethodException e) {
			// ignore
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 이름으로 메쏘드를 찾는다
	 * @param object
	 * @param methodName
	 * @return
	 */
	private static Method findMethodByName(Object object, String methodName) {
		if (object != null) {
			Method[] methods = object.getClass().getMethods();
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					return method;
				}
			}
		}
		return null;
	}

	/**
	 * 동적으로 속성값을 넣는다
	 *
	 * @param object
	 * @param attributeName
	 * @param argType
	 * @param value
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void setAttributeValue(Object object, String attributeName, Class<?> argType, Object value)
			throws SecurityException, NoSuchMethodException,
				IllegalArgumentException, IllegalAccessException, InvocationTargetException {

		if (object != null) {
			Method setMethod = getAccessorMethod(object, PREFIX_SETTER, attributeName, argType);
			if (setMethod != null) {
				setMethod.invoke(object, new Object[] { value });
			}
		}
	}

	/**
	 * 동적으로 속성값을 넣는다. Value 값이 null
	 * @param object
	 * @param attributeName
	 * @param value
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void setAttributeValue(Object object, String attributeName, Object value)
			throws SecurityException, NoSuchMethodException,
				IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if (object != null) {
			Class<?> argType = null;
			if (value != null) {
				argType = value.getClass();
			}
			Method setMethod = getAccessorMethod(object, PREFIX_SETTER, attributeName, argType);
			if (setMethod != null) {
				setMethod.invoke(object, new Object[] { value });
			}
		}
	}

	/**
	 * 동적으로 속성값을 얻는다
	 * @param object
	 * @param attributeName
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object getAttributeValue(Object object, String attributeName)
			throws SecurityException, NoSuchMethodException,
					IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Object attrValueObj = object;
		
		String[] attrNameTokens = attributeName.split("\\.");
		for (String attrNameToken : attrNameTokens) {
			if (attrValueObj != null) {
				if (attrNameToken != null && attrNameToken.trim().length() > 0) {
					Method getMethod = null;
					if (attrValueObj instanceof java.util.Map) {
						// #. Map object : Ex) get("key")
						getMethod = getAccessorMethod(attrValueObj, "get", null, Object.class);

						if (getMethod != null) {
							attrValueObj = getMethod.invoke(attrValueObj, attrNameToken);
						} else {
							attrValueObj = null;
						}
					} else {
						//NumberCode의 경우 EN을 가져온다. -wslee
//						if(attrValueObj instanceof NumberCode){
//								
//							if(CommonUtil.isUSLocale() && "NAME".equals(attrNameToken.toUpperCase())){
//								attrNameToken = "engName";
//							}
//						}
						// #. Object : Ex) getKey
						for (String methodPrefix : GETTING_METHOD_PREFIXES) {
							getMethod = getAccessorMethod(attrValueObj, methodPrefix, attrNameToken);
							if (getMethod != null) {
								break;
							} else {
								continue;
							}
						}
						if (getMethod != null) {
							attrValueObj = getMethod.invoke(attrValueObj);
						} else {
							attrValueObj = null;
						}
					}
				}
			}
		}
		return attrValueObj;
	}

	/**
	 * 지정한 속성을 비교해서 모두 같으면 동일한 객체임
	 * @param a
	 * @param b
	 * @param attrNames
	 * @return
	 */
	public static boolean isSameObject(Object a, Object b, List<String> attrNames) {
		if (!a.getClass().equals(b.getClass())) {
			return false;
		}

		for (String attrName : attrNames) {
			try {
				Object aValue = BeansUtil.getAttributeValue(a, attrName);
				Object bValue = BeansUtil.getAttributeValue(b, attrName);
				if (aValue != null) {
					if (!aValue.equals(bValue)) {
						return false;
					}
				} else {
					if (bValue != null) {
						return false;
					}
				}
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}
}


