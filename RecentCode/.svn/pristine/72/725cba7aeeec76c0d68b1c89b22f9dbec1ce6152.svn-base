/*
 * @(#) GenerateFormNumber.java  Create on 2006. 1. 11.
 * Copyright (c) e3ps. All rights reserverd
 */
package com.e3ps.groupware.workprocess.beans;

import java.util.HashMap;
import java.util.Hashtable;

import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.ManageSequence;

public class GenerateFormNumber
{
    public String getNumber(HashMap hashmap)
    {
        return null;
    }
    public String getNumber(Hashtable param)
    {
        String number = "";
        String type = (String)param.get("type");
        number = type+"-"+DateUtil.getToDay().substring(2,4)+"-"+ManageSequence.getSeqNo(type,"0000");
        return number;
    }
    public String getNumber()
    {
        return null;
    }
}
