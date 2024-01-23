/*
 * @(#) ReflectUtil.java  Create on 2004. 11. 23.
 * Copyright (c) e3ps. All rights reserverd
 */
package com.e3ps.common.obj;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.e3ps.common.log4j.Log4jPackages;

/**
 * 
 * @author Choi Seunghwan, skyprda@e3ps.com
 * @version 1.00, 2004. 11. 23.
 * @since 1.4
 */
public class ReflectUtil
{
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
    public static void callSetMethod(Object obj, String methodName, Object[] args)
    {
        callSetMethod(obj, methodName, args, new Class[]{String.class});
    }
    
    public static void callSetMethod(Object obj, String methodName, Object[] args, Class[] classes)
    {
        try
        {
            Method method = obj.getClass().getMethod("set"+methodName, classes);
            method.invoke(obj, args);
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * @param obj
     * @param methodName
     * @return
     */
    public static String callGetMethod(Object obj, String methodName)
    {
        return (String)callGetMethod(obj,methodName,true);
    }
    
    public static Object callGetMethod(Object obj, String methodName, boolean flag)
    {
        Object result=null;
        try
        {
            Method method = obj.getClass().getMethod("get"+methodName);
            result = method.invoke(obj);
        }
        catch (Exception e)
        {
            LOGGER.info("Exception = " + e.getLocalizedMessage());
        }
        return result;
    }
    
    
    
}
