/**
 * @(#) Message.java
 * Copyright (c) jerred. All rights reserverd
 * 
 *	@version 1.00
 *	@since jdk 1.4.02
 *	@createdate 2004. 3. 3.
 *	@author Cho Sung Ok, jerred@bcline.com
 *	@desc	
 */

package com.e3ps.common.jdf.message;

import java.io.Serializable;
/**
 *	<b>�� �ҽ��� <a href="http://www.javaservice.net/">JavaService Net</a>�� <br> Java Development Framework(JDF) ver1.0.0�� ���ʷ� ����������� �����ϴ�.</b>
 * <p>
 * <code>Message</code> �������̽��� e3ps.message package��
 * �ֿ� ����� Method�� ������ �ִ�.
 */

public interface Message extends Serializable{
	/**	 
	 * Ư�� code�� �ش��ϴ� Message�� �����Ѵ�.
	 * @param 	code 	<code>java.lang.String</code>
	 * @return 				<code>java.lang.String</code>
	 */
	public String getMessage(String code);
	
	/**	 
	 * Ư�� code�� �ش��ϴ� Message�� �����Ѵ�.
	 * @param 	code 	<code>java.lang.String</code>
	 * @return 				<code>java.lang.String</code>
	 */
	public boolean contains(String code);	
}