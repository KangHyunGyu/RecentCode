/* bcwti
 *
 * Copyright (c) 2008 Parametric Technology Corporation (PTC). All Rights
 * Reserved.
 *
 * This software is the confidential and proprietary information of PTC
 * and is subject to the terms of a software license agreement. You shall
 * not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement.
 *
 * ecwti
 */

package com.e3ps.common.content;

import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.lang.ClassNotFoundException;
import java.lang.String;
import wt.util.WTPropertyVetoException;


/**
 *
 * @version   1.0
 **/

public class UploadFile implements Serializable, Externalizable {


   private static final String RESOURCE = "ext.pmx.common.content.contentResource";
   private static final String CLASSNAME = UploadFile.class.getName();
   private String fileName;
   private String serverFileName;
   private String contentRoleType;
   private String contentType;
   private String extension;
   private long size = 0;
   private File file;
   static final long serialVersionUID = 1;
   public static final long EXTERNALIZATION_VERSION_UID = -6112675044716211716L;



   /**
    * Writes the non-transient fields of this class to an external source.
    *
    * <BR><BR><B>Supported API: </B>false
    *
    * @param     output
    * @exception java.io.IOException
    **/
   public void writeExternal( ObjectOutput output )
            throws IOException {

      output.writeLong( EXTERNALIZATION_VERSION_UID );

      output.writeObject( contentRoleType );
      output.writeObject( contentType );
      output.writeObject( extension );
      output.writeObject( file );
      output.writeObject( fileName );
      output.writeObject( serverFileName );
      output.writeLong( size );
   }

   /**
    * Reads the non-transient fields of this class from an external source.
    *
    * <BR><BR><B>Supported API: </B>false
    *
    * @param     input
    * @exception java.io.IOException
    * @exception java.lang.ClassNotFoundException
    **/
   public void readExternal( ObjectInput input )
            throws IOException, ClassNotFoundException {

      long readSerialVersionUID = input.readLong();                // consume UID

      if ( readSerialVersionUID == EXTERNALIZATION_VERSION_UID ) {  // if current version UID
         contentRoleType = (String)input.readObject();
         contentType = (String)input.readObject();
         extension = (String)input.readObject();
         file = (File)input.readObject();
         fileName = (String)input.readObject();
         serverFileName = (String)input.readObject();
         size = input.readLong();
      }
      else
         throw new java.io.InvalidClassException( CLASSNAME, "Local class not compatible:"
                           + " stream classdesc externalizationVersionUID=" + readSerialVersionUID 
                           + " local class externalizationVersionUID=" + EXTERNALIZATION_VERSION_UID );
   }

   /**
    * Gets the value of the attribute: fileName.
    *
    * @return    String
    **/
   public String getFileName() {

      return fileName;
   }

   /**
    * Sets the value of the attribute: fileName.
    *
    * @param     a_FileName
    * @exception wt.util.WTPropertyVetoException
    **/
   public void setFileName( String a_FileName )
            throws WTPropertyVetoException {

      fileNameValidate( a_FileName );   // throws exception if not valid
      fileName = a_FileName;
   }

   /**
    * @param     a_FileName
    * @exception wt.util.WTPropertyVetoException
    **/
   private void fileNameValidate( String a_FileName )
            throws WTPropertyVetoException {
      if ( a_FileName != null && a_FileName.length() > 200 ) {   // upper limit check
         Object[] args = { new wt.introspection.PropertyDisplayName( CLASSNAME, "fileName" ), "200" };
         throw new WTPropertyVetoException( "wt.introspection.introspectionResource", wt.introspection.introspectionResource.UPPER_LIMIT, args,
               new java.beans.PropertyChangeEvent( this, "fileName", fileName, a_FileName ) );
      }
   }

   /**
    * Gets the value of the attribute: serverFileName.
    *
    * @return    String
    **/
   public String getServerFileName() {

      return serverFileName;
   }

   /**
    * Sets the value of the attribute: serverFileName.
    *
    * @param     a_ServerFileName
    * @exception wt.util.WTPropertyVetoException
    **/
   public void setServerFileName( String a_ServerFileName )
            throws WTPropertyVetoException {

      serverFileNameValidate( a_ServerFileName );   // throws exception if not valid
      serverFileName = a_ServerFileName;
   }

   /**
    * @param     a_ServerFileName
    * @exception wt.util.WTPropertyVetoException
    **/
   private void serverFileNameValidate( String a_ServerFileName )
            throws WTPropertyVetoException {
      if ( a_ServerFileName != null && a_ServerFileName.length() > 200 ) {   // upper limit check
         Object[] args = { new wt.introspection.PropertyDisplayName( CLASSNAME, "serverFileName" ), "200" };
         throw new WTPropertyVetoException( "wt.introspection.introspectionResource", wt.introspection.introspectionResource.UPPER_LIMIT, args,
               new java.beans.PropertyChangeEvent( this, "serverFileName", serverFileName, a_ServerFileName ) );
      }
   }

   /**
    * Gets the value of the attribute: contentRoleType.
    *
    * @return    String
    **/
   public String getContentRoleType() {

      return contentRoleType;
   }

   /**
    * Sets the value of the attribute: contentRoleType.
    *
    * @param     a_ContentRoleType
    * @exception wt.util.WTPropertyVetoException
    **/
   public void setContentRoleType( String a_ContentRoleType )
            throws WTPropertyVetoException {

      contentRoleTypeValidate( a_ContentRoleType );   // throws exception if not valid
      contentRoleType = a_ContentRoleType;
   }

   /**
    * @param     a_ContentRoleType
    * @exception wt.util.WTPropertyVetoException
    **/
   private void contentRoleTypeValidate( String a_ContentRoleType )
            throws WTPropertyVetoException {
      if ( a_ContentRoleType != null && a_ContentRoleType.length() > 200 ) {   // upper limit check
         Object[] args = { new wt.introspection.PropertyDisplayName( CLASSNAME, "contentRoleType" ), "200" };
         throw new WTPropertyVetoException( "wt.introspection.introspectionResource", wt.introspection.introspectionResource.UPPER_LIMIT, args,
               new java.beans.PropertyChangeEvent( this, "contentRoleType", contentRoleType, a_ContentRoleType ) );
      }
   }

   /**
    * Gets the value of the attribute: contentType.
    *
    * @return    String
    **/
   public String getContentType() {

      return contentType;
   }

   /**
    * Sets the value of the attribute: contentType.
    *
    * @param     a_ContentType
    * @exception wt.util.WTPropertyVetoException
    **/
   public void setContentType( String a_ContentType )
            throws WTPropertyVetoException {

      contentTypeValidate( a_ContentType );   // throws exception if not valid
      contentType = a_ContentType;
   }

   /**
    * @param     a_ContentType
    * @exception wt.util.WTPropertyVetoException
    **/
   private void contentTypeValidate( String a_ContentType )
            throws WTPropertyVetoException {
      if ( a_ContentType != null && a_ContentType.length() > 200 ) {   // upper limit check
         Object[] args = { new wt.introspection.PropertyDisplayName( CLASSNAME, "contentType" ), "200" };
         throw new WTPropertyVetoException( "wt.introspection.introspectionResource", wt.introspection.introspectionResource.UPPER_LIMIT, args,
               new java.beans.PropertyChangeEvent( this, "contentType", contentType, a_ContentType ) );
      }
   }

   /**
    * Gets the value of the attribute: extension.
    *
    * @return    String
    **/
   public String getExtension() {

      return extension;
   }

   /**
    * Sets the value of the attribute: extension.
    *
    * @param     a_Extension
    * @exception wt.util.WTPropertyVetoException
    **/
   public void setExtension( String a_Extension )
            throws WTPropertyVetoException {

      extensionValidate( a_Extension );   // throws exception if not valid
      extension = a_Extension;
   }

   /**
    * @param     a_Extension
    * @exception wt.util.WTPropertyVetoException
    **/
   private void extensionValidate( String a_Extension )
            throws WTPropertyVetoException {
      if ( a_Extension != null && a_Extension.length() > 200 ) {   // upper limit check
         Object[] args = { new wt.introspection.PropertyDisplayName( CLASSNAME, "extension" ), "200" };
         throw new WTPropertyVetoException( "wt.introspection.introspectionResource", wt.introspection.introspectionResource.UPPER_LIMIT, args,
               new java.beans.PropertyChangeEvent( this, "extension", extension, a_Extension ) );
      }
   }

   /**
    * Gets the value of the attribute: size.
    *
    * @return    long
    **/
   public long getSize() {

      return size;
   }

   /**
    * Sets the value of the attribute: size.
    *
    * @param     a_Size
    * @exception wt.util.WTPropertyVetoException
    **/
   public void setSize( long a_Size )
            throws WTPropertyVetoException {

      size = a_Size;
   }

   /**
    * Gets the value of the attribute: file.
    *
    * @return    File
    **/
   public File getFile() {

      return file;
   }

   /**
    * Sets the value of the attribute: file.
    *
    * @param     a_File
    * @exception wt.util.WTPropertyVetoException
    **/
   public void setFile( File a_File )
            throws WTPropertyVetoException {

      file = a_File;
   }

   /**
    *
    * <BR><BR><B>Supported API: </B>true
    *
    * @param     servierFileName
    * @param     contentRoleName
    * @param     fileName
    * @return    UploadFile
    **/
   public static UploadFile newUploadFile( String servierFileName, String contentRoleName, String fileName ) {

      return null;
   }

}
