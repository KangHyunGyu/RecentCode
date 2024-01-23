/**
* 모듈명             	: com.e3ps.common.content.fileuploader
* 프로그램 명       	: FormUploader
* 기능설명           	: 파일 업로드시 사용
* 프로그램 타입   	: Util
* 비고 / 특이사항	:
*/

package com.e3ps.common.content.fileuploader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;

import wt.util.WTProperties;

import com.e3ps.common.content.uploader.WBFile;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.WebUtil;

public class FormUploader
{
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
  public Vector files;
  public Hashtable formParameters;
  public static boolean writeToFile;
  public static int MaxMemorySize;
  public static File TempDirectory = null;
  

  /**
   * FormUploader
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public FormUploader()
  {
    this.formParameters = new Hashtable();
    MaxMemorySize = 20971520;

    WTProperties prop = null;
    try {
      prop = WTProperties.getServerProperties();
    }
    catch (IOException e)
    {
      LOGGER.error("ERROR",e);
    }
    if (TempDirectory == null){
      TempDirectory = new File(new String(prop.getProperty("wt.temp").getBytes())); 
    }
  }
  
  /**
   * 
   * @param req
   * @param res
   * @param context
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public FormUploader(HttpServletRequest req, HttpServletResponse res, ServletContext context) {
    this();
  }

  /**
   * 
   * @param req
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public static FormUploader newFormUploader(HttpServletRequest req) {
    FormUploader uploader = new FormUploader(req, null, null);
    return parseRequest(uploader, req);
  }
  
  /**
   * 
   * @param req
   * @param res
   * @param context
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public static FormUploader newFormUploader(HttpServletRequest req, HttpServletResponse res, ServletContext context) {
    FormUploader uploader = new FormUploader(req, res, context);
    return parseRequest(uploader, req);
  }

  /**
   * 
   * @param uploader
   * @param req
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public static FormUploader parseRequest(FormUploader uploader, HttpServletRequest req) {
    boolean isMultipart = ServletFileUpload.isMultipartContent(req);

    if (!isMultipart) {
      Hashtable hash = WebUtil.getHttpParams(req);
      uploader.setFormParameters(hash);
    }
    else {
      DiskFileItemFactory factory = new DiskFileItemFactory();

      factory.setRepository(TempDirectory);

      ServletFileUpload upload = new ServletFileUpload(factory);
      upload.setHeaderEncoding("UTF-8");
      try
      {
        List items = upload.parseRequest(req);

        Iterator iter = items.iterator();
        while (iter.hasNext()) {
          DiskFileItem item = (DiskFileItem)iter.next();

          if (item.isFormField()) {
            processFormField(item, uploader);
          }
          else if (item.getSize() != 0L){
            processUploadedFile(item, uploader);
          }
        }
      }
      catch (FileUploadException e) {
        LOGGER.error("ERROR",e);
      }
    }
    return uploader;
  }

  /**
   * 
   * @param item
   * @param uploader
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public static void processUploadedFile(DiskFileItem item, FormUploader uploader) {
    try {
      LOGGER.debug("[FileUploader][processUploadedFile]File Path === " + item.getStoreLocation().getPath() + " isExist = " + item.getStoreLocation().exists());
      LOGGER.debug("[FileUploader][processUploadedFile]pre File size === " + item.getStoreLocation().length());

      item.write(item.getStoreLocation());

      WBFile wbfile = new WBFile();

      wbfile.setContentType(item.getContentType());
      wbfile.setFieldName(item.getFieldName());

      wbfile.setFullPathName(item.getName());
      wbfile.setName(FilenameUtils.getName(item.getName()));

      File rfile = reWrite(item);

      wbfile.setSize((int)rfile.length());

      wbfile.setFile(rfile);

      Vector v = uploader.getFiles();
      if (v == null){
    	  v = new Vector();
      }
      v.addElement(wbfile);
      uploader.setFiles(v);
    } catch (Exception e) {
      LOGGER.error("ERROR",e);
    }
  }

  /**
   * 
   * @param item
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  private static File reWrite(DiskFileItem item)
  {
    BufferedOutputStream outs = null;
    byte[] b = new byte[4096];
    BufferedInputStream fin = null;
    File temFile = null;
    int read = 0;
    try {
      fin = new BufferedInputStream(new FileInputStream(item.getStoreLocation()));

      temFile = File.createTempFile("wbsItem", "temp", TempDirectory);
      outs = new BufferedOutputStream(new FileOutputStream(temFile));
      while ((read = fin.read(b)) != -1)
      {
        outs.write(b, 0, read);
      }
      outs.flush();
    } catch (IOException e) {
      LOGGER.error("ERROR",e);
    } finally {
      try {
        if (fin != null){
        	fin.close();
        }
        fin = null;
        LOGGER.debug("[FileUploader][reWrite][finally]Complete Memory Release : BufferedInputStream ");
      }
      catch (IOException localIOException1) {
        LOGGER.error("ERROR",localIOException1);
      }
      try {
        if (outs != null){
        	outs.close();
        }
        outs = null;
        LOGGER.debug("[FileUploader][reWrite][finally]Complete Memory Release : BufferedOutputStream ");
      } catch (IOException localIOException2) {
        LOGGER.error("ERROR",localIOException2);
      }
    }
    return temFile;
  }

  /**
   * 
   * @param item
   * @param uploader
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public static void processFormField(DiskFileItem item, FormUploader uploader)
  {
    if (item.isFormField()) {
      String name = item.getFieldName();
      String value = null;
      try {
        value = item.getString("UTF-8");
      } catch (UnsupportedEncodingException e) {
        LOGGER.error("ERROR",e);
      }
      Hashtable t = uploader.getFormParameters();
      Vector vec = getParameterVector(name, t);
      if (vec != null) {
        vec.add(value);
        t.put(name, vec);
      } else {
        t.put(name, value);
      }
      uploader.setFormParameters(t);
    }
  }

  /**
   * 
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public Vector getFiles() {
    return this.files;
  }
  
  /**
   * 
   * @param files
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public void setFiles(Vector files) {
    this.files = files;
  }
  
  /**
   * 
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public Hashtable getFormParameters() {
    return this.formParameters;
  }
  
  /**
   * 
   * @param formParameters
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public void setFormParameters(Hashtable formParameters) {
    this.formParameters = formParameters;
  }

  /**
   * 
   * @param name
   * @param table
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  private static Vector getParameterVector(String name, Hashtable table) {
    Object o = table.get(name);
    if (o == null){
    	return null;
    }
    if ((o instanceof Vector)){
    	return (Vector)o;
    }
    if ((o instanceof String)) {
      Vector v = new Vector();
      v.add(o);
      return v;
    }
    return null;
  }
}