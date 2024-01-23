package com.e3ps.doc.util;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.util.StringUtil;

public class DocUtil {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.DOC.getName());
	
	/**
	 * @desc	: keepYear 한국어
	 * @author	: mnyu
	 * @date	: 2019. 11. 21.
	 * @method	: keepYearKR
	 * @return	: String
	 * @param keepYear
	 * @return
	 */
	public static String keepYearKR(String keepYear) {
		if(!StringUtil.checkString(keepYear)) return "";
		
		if("infinite".equals(keepYear)){
			return MessageUtil.getMessage("영구보존");
		}else if(keepYear.contains("year")){
			return MessageUtil.getMessage(keepYear.replace("year", "년"));
		}
		return "";
	}

}
