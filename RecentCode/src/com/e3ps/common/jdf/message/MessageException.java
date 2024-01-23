/**
 * @(#) MessageException.java
 * Copyright (c) jerred. All rights reserverd
 * 
 *	@version 1.00
 *	@since jdk 1.4.02
 *	@createdate 2004. 3. 3.
 *	@author Cho Sung Ok, jerred@bcline.com
 *	@desc	
 */

package com.e3ps.common.jdf.message;

/**
 *	<b>�� �ҽ��� <a href="http://www.javaservice.net/">JavaService Net</a>�� <br> Java Development Framework(JDF) ver1.0.0�� ���ʷ� ����������� �����ϴ�.</b>
 * <p>
 * Message Definition File�� ã���� ���ų� Load�� �Ҽ� ������ �߻��ϴ� Exception
 */

public class MessageException extends RuntimeException {
	/**
	 * Default ������
	 */
	public MessageException() {
		super();
	}

	/**
	 * @param str <code>java.lang.String</cdoe> Exception �޼���
	 */
	public MessageException(String str) {
		super(str);
	}	
}