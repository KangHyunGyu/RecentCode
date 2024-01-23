package com.e3ps.common.util;

import com.e3ps.common.log4j.Log4jPackages;

import jxl.Cell;

public class JxlUtil {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
	public static boolean isContent(Cell[] cell, int idx) {
		boolean isContent =  false;
		
		if(!"".equals(getContent(cell, idx))){
			isContent = true;
		}
		
		return isContent;
	}
	
	public static String getContent(Cell[] cell, int idx) {
		try {
			String val = cell[idx].getContents();
			if (val == null)
				return "";
			return val.trim();
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		return "";
	}
}
