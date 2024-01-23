/**
 * @(#) MessageImpl.java
 * Copyright (c) jerred. All rights reserverd
 * 
 *	@version 1.00
 *	@since jdk 1.4.02
 *	@createdate 2004. 3. 3.
 *	@author Cho Sung Ok, jerred@bcline.com
 *	@desc	
 */

package com.e3ps.common.jdf.message;
 
import java.util.Properties;

import com.e3ps.common.util.CharUtil;

/**
*<b>�� �ҽ��� <a href="http://www.javaservice.net/">JavaService Net</a>�� <br> Java Development Framework(JDF) ver1.0.0�� ���ʷ� ����������� �����ϴ�.</b>
*/
public class MessageImpl implements Message {
 	private static Properties INSTANCE = null;
	
	protected MessageImpl(Properties prop) {
		INSTANCE = prop;
	}
	
	/**
	* Ư�� code�� �ش��ϴ� Message�� �����Ѵ�.
	*/
	public String getMessage(String code) {	
		String value = INSTANCE.getProperty(code);
		if ( value == null ) throw new MessageException(this.getClass().getName() + ":getMessage(code) -  value of key(" +code+") is null" );
		else value = CharUtil.E2K(value);
		return value;
	}
	
	/**	 
	 * 
	 */
	public boolean contains(String code) {	
		return INSTANCE.contains(code);
	}
}