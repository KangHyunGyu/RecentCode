/**
* 모듈명             	: com.e3ps.common.content.uploader
* 프로그램 명       	: DataHeader
* 기능설명           	: 파일 업로드시 사용
* 프로그램 타입   	: Util
* 비고 / 특이사항	:
*/

package com.e3ps.common.content.uploader;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataHeader  implements Serializable
{

	private static final long serialVersionUID = 3584560294036618285L;
	private boolean file = false;
	private String fieldName = null;
	private String fullPathName = null;
	private String fileName = null;
	private String fileExtension = null;
	private String contentType = null;
	private String contentDisposition = null;
	private String mimeType = null;
	private String mimeSubType = null;
  
  protected static final Logger logger = LoggerFactory.getLogger(DataHeader.class);

  /**
   * 
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public DataHeader()
  {
  }

  /**
   * 
   * @param headerString
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public DataHeader(String headerString)
  {
    this.file = (headerString.indexOf("filename") > 0);

    int startPosition = headerString.indexOf("name=\"") + "name=\"".length();
    int endPosition = headerString.indexOf("\"", startPosition);
    if ((startPosition > 0) && (endPosition > 0)) {
      this.fieldName = headerString.substring(startPosition, endPosition);
    }

    if (isFile())
    {
      startPosition = headerString.indexOf("filename=\"") + "filename=\"".length();
      endPosition = headerString.indexOf("\"", startPosition);
      if ((startPosition > 0) && (endPosition > 0)) {
        this.fullPathName = headerString.substring(startPosition, endPosition);
      }

      startPosition = headerString.indexOf("Content-Type: ") + "Content-Type: ".length();
      if (startPosition > 0) {
        this.contentType = headerString.substring(startPosition);
      }

      startPosition = headerString.indexOf("Content-Disposition: ") + "Content-Disposition: ".length();
      endPosition = headerString.indexOf(";", startPosition);
      this.contentDisposition = headerString.substring(startPosition, endPosition);

      if ((startPosition = this.fullPathName.lastIndexOf('/')) > 0){
    	  this.fileName = this.fullPathName.substring(startPosition + 1);
      }
      else if ((startPosition = this.fullPathName.lastIndexOf('\\')) > 0){
    	  this.fileName = this.fullPathName.substring(startPosition + 1); 
      }
      else {
        this.fileName = this.fullPathName;
      }

      if ((startPosition = this.fileName.lastIndexOf('.')) > 0){
        this.fileExtension = this.fileName.substring(startPosition + 1);
      }
      else {
        this.fileExtension = "";
      }

      if ((startPosition = this.contentType.indexOf("/")) > 0) {
        this.mimeType = this.contentType.substring(0, startPosition);
        this.mimeSubType = 
          this.contentType.substring(startPosition + 1);
      } else {
        this.mimeType = this.contentType;
        this.mimeSubType = this.contentType;
      }
    }
  }

  /**
   * 
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public boolean isFile()
  {
    return this.file;
  }

  /**
   * 
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public String getFieldName()
  {
    return this.fieldName;
  }

  /**
   * 
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public String getFullPathName()
  {
    return this.fullPathName;
  }

  /**
   * 
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public String getFileName()
  {
    return this.fileName;
  }

  /**
   * 
   * @param name
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public void setFileName(String name) {
    this.fileName = name;
  }

  /**
   * 
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public String getFileExtension()
  {
    return this.fileExtension;
  }

  /**
   * 
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public String getContentType()
  {
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
    return this.contentDisposition;
  }

  /**
   * 
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public String getMimeType()
  {
    return this.mimeType;
  }

  /**
   * 
   * @return
   * @author yhjang1
   * @since 2014. 12. 15.
   */
  public String getMimeSubType()
  {
    return this.mimeSubType;
  }
}