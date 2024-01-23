/**
 * @(#) LoggerWriter.java
 * Copyright (c) jerred. All rights reserverd
 * 
 *	@version 1.00
 *	@since jdk 1.4.02
 *	@createdate 2004. 3. 3.
 *	@author Cho Sung Ok, jerred@bcline.com
 *	@desc	
 */
 
package com.e3ps.common.jdf.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import wt.content.ApplicationData;
import wt.content.ContentHolder;
import wt.org.WTUser;

import com.e3ps.common.jdf.config.Config;
import com.e3ps.common.jdf.config.ConfigImpl;


/**
 *	<b>�� �ҽ��� <a href="http://www.javaservice.net/">JavaService Net</a>�� <br> Java Development Framework(JDF) ver1.0.0�� ���ʷ� ����������� �����ϴ�.</b>b>
 * <p>
 * <code>LoggerWriter</code> Ŭ������ �α������� �����ϰ� ������ �α׸� 
 * ����ϴ� ������ �ϴ� Ŭ�����̴�. �� Ŭ�������� ������ ��尡 �ִµ� �̸� �̿��ؼ�
 * ����ϰ��� �ϴ� ���¸� ������ �� �ִ�.
 * �α� ��� ���� Configuration File���� �����Ǵµ� File���� �α׿� ���õ� �κ��� ����
 *	<p><blockquote><pre>
 * ############################################################################
 * # Log Writer Configuration
 * ############################################################################
 * # NOTE: �̰��� �����ϰ� �ݿ��Ǳ⸦ ���ϸ� JVM�� ���� �⵿�ؾ� �Ѵ�.
 * # �α׸� ����� �ÿ� �ڵ� ��� ���θ� �����ϴ� �κ��̴�.
 * e3ps.log.autoflush = true
 *
 * # �α� ������ ���� ���丮�� ������ �ִ� �κ��̴�.
 * e3ps.log.dir = log
 * 
 * # Logging Trace Level ( FATAL, WARN, INFO, DEBUG )
 * # �������� Code�� Logger.xxx.println()�� ���� Log�� ����� �κп���
 * # �� flag�� true�� ��츸 �������� Log�� ���� �ȴ�.
 * e3ps.log.fatal.trace = true
 * e3ps.log.warn.trace = true
 * e3ps.log.info.trace = true
 * e3ps.log.debug.trace = true
 *	</pre></blockquote><p>
 * ���� �α������� �Ӽ�,�α������� ��ġ,�׸��� �α׸� ���� �ڵ带 �����ϴ� �κ��̴�.
 *
 *	@see		e3ps.log.DownloadLoggerWriter#FATAL
 *	@see		e3ps.log.DownloadLoggerWriter#INFO
 *	@see		e3ps.log.DownloadLoggerWriter#DEBUG
 *	@see		e3ps.log.DownloadLoggerWriter#DBWRAP
 */

public class DownloadLoggerWriter {	
	private static String checkday = null;
	private static boolean newLined = true;	
	private static PrintWriter writer = null;
	private static SimpleDateFormat form = new SimpleDateFormat ("yyyyMMdd HH:mm:ss", Locale.KOREA);
	private final static Object lock = new Object();
	
	/**	 
	* ������
	* @param mode <code>int</code> LoggerWriter ��ü�� ��� ���
	*/
	public DownloadLoggerWriter() {
		synchronized ( lock ) {
			checkDate();
		}
	}

	/**	 
	* ����� ������ ��¥�� üũ�Ѵ�.
	* ���� ����� ������ ��¥�� ���� ��¥�̸� ���ó�¥�� ���ο� �α������� ������ ��
	* �� ���Ͽ� �αױ���� �����.
	*/
	private static void checkDate() {
		Config conf = ConfigImpl.getInstance();
		SimpleDateFormat fileForm = null;
		String day = "";
		int logfiletype = conf.getInt("log.filetype");
		if ( logfiletype == 1 ) {			// �Ϻ�
			fileForm = new SimpleDateFormat ("yyyy��MM��dd��", Locale.KOREA);			
			day = "-"+fileForm.format(new Date());
		} else if ( logfiletype == 2 ) {	//�ֺ�
			Calendar cal = Calendar.getInstance(Locale.KOREA);
			fileForm = new SimpleDateFormat ("yyyy��MM��", Locale.KOREA);			
			day = "-"+fileForm.format(new Date())+cal.get(Calendar.WEEK_OF_MONTH)+"��";
		} else if ( logfiletype == 3 ) {	//����
			fileForm = new SimpleDateFormat ("yyyy��MM��", Locale.KOREA);			
			day = "-"+fileForm.format(new Date());
		} else if ( logfiletype == 4 ) {	//�⺰
			fileForm = new SimpleDateFormat ("yyyy��", Locale.KOREA);			
			day = "-"+fileForm.format(new Date());
		}						
		if ( day.equals(checkday) ) return;

		try {
			if ( writer != null ) {
				try { 
					writer.close();
				} catch(Exception e) {}
				writer = null;
			}
			checkday = day;
			String logname = "eSolution-Download" + checkday + ".log" ;
			
			String root = ConfigImpl.getRoot();
			String directory = conf.getString("log.dir");
			File rootDir = new File(root + File.separator + directory);
			//FileUtil.checkDir(rootDir); // ���丮 üũ		
			if (!rootDir.exists ()) rootDir.mkdir ();
			if (!rootDir.exists ()) rootDir.mkdirs ();
			File file = new File(rootDir,logname);
			String filename = file.getAbsolutePath();			
			FileWriter fw =  new FileWriter(filename, true);														// APPEND MODE
			writer = new PrintWriter(new BufferedWriter(fw),conf.getBoolean("log.autoflush")); // AUTO Flush
		} catch(Exception e){
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);
			writer.println("Can't open log file : " + e.getMessage());
			writer.println("Log will be printed into System.out");
		}		
	}

	/**	 
	* Garbage Collection �ɶ� �Ҹ���� Method
	*/
	public void finalize() {
		try {
			if ( writer != null ) writer.flush();
		} catch(Exception e){}
	}

	/**	 
	* ȣ��ɶ� ���ۿ� �����ִ� ��Ʈ���� �α����Ϸ� ����Ѵ�.
	*/
	public void flush() {
		writer.flush();
	}
	
	public void println(String type, WTUser user, ContentHolder holder , ApplicationData data) {
		synchronized ( lock ) {
			if ( newLined ) printTime();
			writer.print("[" + type + "] ");
			writer.print("[ UserName : " + user.getFullName() + "(" + user.getName() + ") ] ");
			writer.print("[ ContentHolder : " + holder.getPersistInfo().getObjectIdentifier().toString() + " ] ");
			writer.println("[ FileName : " + data.getFileName() + "(" + data.getFileSize() + " Bytes)] ");
			newLined = true;
		}
	}

	/**
	* LoggerWriter ��ü�� ���� �ð��� ���� ���� �α׿� ����Ѵ�.
	* ������´� ���(Error/Info/Debug/DBWrap) [HH:mm:ss] �̴�
	*/
	private void printTime() {
		checkDate();
		String serverty = "[D] ";
		writer.write(serverty + " [" + form.format(new Date())+"] ");
	}	
}