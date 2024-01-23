/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.e3ps.common.content.remote;

import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Vector;

import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.util.WTException;

public class ContentDownload implements RemoteAccess, Serializable, Runnable {
   private static final long serialVersionUID = 3478805821606945578L;
   private transient Vector workers;
   private transient ContentDownloadThread downloadThread;
   private transient Throwable downloadException;
   private transient RemoteMethodServer remoteMethodServer;
   private transient URL url;
   private transient Vector reVector;

   public ContentDownload() {
   }

   public ContentDownload(URL paramURL) {
      this.url = paramURL;
   }

   public void addContentStream(Object paramObject) {
      addWorker(new ContentDownloadStream(paramObject));
   }

   protected void addWorker(Object paramObject) {
      if (this.workers == null) {
         this.workers = new Vector();
      }
      this.workers.add(paramObject);
   }

   public void execute() {
      this.downloadThread = new ContentDownloadThread(this);
      this.downloadThread.start();
   }

   public void run() {
      try {
         Class[] arrayOfClass = {Vector.class};

         Object[] arrayOfObject = {this.workers};

         if (this.url != null) {
            RemoteMethodServer localRemoteMethodServer =
                  RemoteMethodServer.getInstance(this.url);
            Object localObject1 =
                  localRemoteMethodServer.invoke("execute", null, this,
                        arrayOfClass, arrayOfObject);
         } else {
            RemoteMethodServer.getDefault().invoke("execute", null, this,
                  arrayOfClass, arrayOfObject);
         }
      } catch (InvocationTargetException localInvocationTargetException) {
         localInvocationTargetException.printStackTrace();
         this.downloadException =
               localInvocationTargetException.getTargetException();
      } catch (Throwable localThrowable) {
         localThrowable.printStackTrace();
         this.downloadException = localThrowable;
      } finally {
      }
   }

   public Vector execute(Vector paramVector) throws WTException {
      return paramVector;
   }

   public InputStream getInputStream() {
      return this.downloadThread.getInputStream();
   }

   public void done() {
      if (this.downloadThread != null) this.downloadThread.done();
   }

   public void checkStatus() throws WTException {
      if (this.downloadException == null) return;
      if (this.downloadException instanceof WTException) {
         throw ((WTException) this.downloadException);
      }
      throw new WTException(this.downloadException);
   }
}
