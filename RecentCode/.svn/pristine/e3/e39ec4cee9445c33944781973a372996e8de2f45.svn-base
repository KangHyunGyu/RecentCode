/**
* 모듈명             	: com.e3ps.common.content.uploader
* 프로그램 명       	: WBFile
* 기능설명           	: 파일 업로드시 사용
* 프로그램 타입   	: Util
* 비고 / 특이사항	:
*/

package com.e3ps.common.content.uploader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WBFile  implements Serializable
{
  private static final long serialVersionUID = -5836296113860572378L;
  private int size = 0;
  private byte[] contents;
  private DataHeader dataHeader;
  private File file = null;
  private DiskFileItem item;
  private String contentType;
  private String fieldName;
  private String fullPathName;
  private String name;
  
  protected static final Logger logger = LoggerFactory.getLogger(WBFile.class);

  /**
   * 
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public WBFile()
  {
  }

  /**
   * 
   * @param name
   * @param size
   * @param contents
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public WBFile(String name, int size, byte[] contents)
  {
    this.dataHeader = new DataHeader();
    setName(name);
    setSize(size);
    setContents(contents);
  }

  /**
   * 
   * @param name
   * @param size
   * @param file
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public WBFile(String name, int size, File file) {
    this.dataHeader = new DataHeader();
    setName(name);
    setSize(size);
    setFile(file);
  }
  
  /**
   * 
   * @param item
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public static WBFile newWBFile(DiskFileItem item) {
    WBFile wbfile = new WBFile();
    wbfile.setItem(item);
    wbfile.setContentType(item.getContentType());
    wbfile.setFieldName(item.getFieldName());
    wbfile.setFile(item.getStoreLocation());
    wbfile.setFullPathName(item.getName());
    wbfile.setName(FilenameUtils.getName(item.getName()));
    wbfile.setSize((int)item.getStoreLocation().length());
    return wbfile;
  }

  /**
   * 
   * @param dataHeader
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public WBFile(DataHeader dataHeader)
  {
    this.dataHeader = dataHeader;
  }

  /**
   * 
   * @param dataHeader
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public void setDataHeader(DataHeader dataHeader)
  {
    this.dataHeader = dataHeader;
  }

  /**
   * 
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public int getSize()
  {
    return this.size;
  }

  /**
   * 
   * @param size
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public void setSize(int size)
  {
    this.size = size;
  }

  /**
   * 
   * @param file
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public void setFile(File file)
  {
    File drmFile = file;

    this.file = drmFile;
  }

  /**
   * 
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public File getFile()
  {
    return this.file;
  }

  /**
   * 
   * @param contents
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public void setContents(byte[] contents)
  {
    this.contents = contents;
  }

  /**
   * 
   * @param index
   * @param content
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public void setContentByte(int index, byte content)
  {
    this.contents[index] = content;
  }

  /**
   * 
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public String getName()
  {
    if (this.dataHeader != null) {
      return this.dataHeader.getFileName();
    }
    return this.name;
  }

  /**
   * 
   * @param name
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public void setName(String name)
  {
    if (this.dataHeader != null){
    	this.dataHeader.setFileName(name);
    }
    else{
      this.name = name;
    }
  }

  /**
   * 
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public InputStream getInputStream()
  {
    if (this.contents != null) {
      return new ByteArrayInputStream(this.contents);
    }
    if (this.file != null) {
      try {
        return new FileInputStream(this.file);
      } catch (FileNotFoundException fnfe) {
        return null;
      }
    }
    return null;
  }

  /**
   * 
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public String getFullPathName()
  {
    if (this.dataHeader != null) {
      return this.dataHeader.getFullPathName();
    }
    return this.fullPathName;
  }

  /**
   * 
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public String getContentType()
  {
    if (this.dataHeader != null) {
      return this.dataHeader.getContentType();
    }
    return this.contentType;
  }

  /**
   * 
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public String getContentDisposition()
  {
    return this.dataHeader.getContentDisposition();
  }

  /**
   * 
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public String getMimeType()
  {
    return this.dataHeader.getMimeType();
  }

  /**
   * 
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public String getMimeSubType()
  {
    return this.dataHeader.getMimeSubType();
  }

  /**
   * 
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public String getFieldName()
  {
    if (this.dataHeader != null) {
      return this.dataHeader.getFieldName();
    }
    return this.fieldName;
  }

  /**
   * 
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public byte[] getContents()
  {
    return this.contents;
  }

  /**
   * 
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public DiskFileItem getItem() {
    return this.item;
  }

  /**
   * 
   * @param contentType
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  /**
   * 
   * @param fieldName
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  /**
   * 
   * @param item
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public void setItem(DiskFileItem item) {
    this.item = item;
  }

  /**
   * 
   * @param fullPathName
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public void setFullPathName(String fullPathName) {
    this.fullPathName = fullPathName;
  }

  /**
   * 
   * @param file
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  @SuppressWarnings("resource")
public static byte[] getBytesFromFile(File file)
  {
    long length = file.length();

    byte[] bytes = new byte[(int)length];
    try
    {
      InputStream is = new FileInputStream(file);

      int offset = 0;
      int numRead = 0;
      while ((offset < bytes.length) && 
        ((numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)) {
        offset += numRead;
      }

      if (offset < bytes.length) {
        throw new IOException("Could not completely read file " + file.getName());
      }

      is.close();
    } catch (IOException ioe) {
      logger.error("ERROR",ioe);
    }
    return bytes;
  }
}