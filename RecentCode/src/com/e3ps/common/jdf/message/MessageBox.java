/**
 *	@(#) MessageBox.java
 *	Copyright (c) jerred. All rights reserverd
 * 
 *	@version 1.00
 *	@since jdk 1.4.02
 *	@createdate 2004. 3. 3.
 *	@author Cho Sung Ok, jerred@bcline.com
 *	@desc	
 */
 
package com.e3ps.common.jdf.message;

import java.io.File;
import java.io.FileInputStream;
import java.util.Hashtable;
import java.util.Properties;

import com.e3ps.common.jdf.config.Config;
import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.jdf.log.Logger;
;
/**
 * 
 */
public class MessageBox {
	private static Hashtable INSTANCE = new Hashtable();
	
	/**
	* 	Ư�� Ű�� �ش��ϴ� Configuration File�� �ʱ�ȭ �����ִ� Method
	*/
	public synchronized static MessageImpl getInstance(String msgCode) {
		if ( INSTANCE == null ) INSTANCE = new Hashtable();
		if ( !INSTANCE.contains(msgCode) ) initialize(msgCode);
		
		return (MessageImpl)INSTANCE.get(msgCode);
	}  	
	
	/**
	 *	Message Definition File�� ã���� ���ų� load�� �� ������ �߻��Ѵ�.
	 */
	private synchronized static void initialize(String msgCode) throws MessageException {		
		try {
			Config conf = ConfigImpl.getInstance();
				
			String message_file = conf.getString(msgCode);				
			File file = new File(message_file);
			if ( ! file.canRead() ) throw new MessageException("e3ps.jdf.message.MessageBox:initialize(msgCode) - Can't open message file : " + message_file );
			FileInputStream fin = new FileInputStream(file);
			Properties props = new Properties();
			props.load(fin);	
			fin.close();
				
			MessageImpl instance = new MessageImpl(props);				
			INSTANCE.put(msgCode,instance);
		} catch (Exception e) {
			Logger.err.println("e3ps.jdf.message.MessageBox:initialize(msgCode) - Can't initialize msgBox : " + msgCode + "  error : " + e.getMessage());
			throw new MessageException("e3ps.jdf.message.MessageBox:initialize(msgCode) - Can't initialize msgBox : " + msgCode + "  error : " + e.getMessage());
		} 
	}
}
