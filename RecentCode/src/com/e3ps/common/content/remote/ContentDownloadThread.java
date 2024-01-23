package com.e3ps.common.content.remote;

import java.io.InputStream;
import java.util.Vector;
import wt.util.WTThread;

public class ContentDownloadThread extends WTThread
{
  private boolean ready;
  private boolean done;
  private InputStream inputStream;
  private String fileName;

  public ContentDownloadThread(Runnable paramRunnable)
  {
    super(paramRunnable);
    this.ready = false;
    this.done = false;
  }

  public boolean isReady()
  {
    return (this.ready) || (this.done);
  }

  public void setInputStream(InputStream paramInputStream)
  {
    this.inputStream = paramInputStream;
    this.ready = false;
  }

  public synchronized InputStream getInputStream()
  {
    this.ready = true;
    notifyAll();
    while (this.ready)
      try
      {
        wait();
      } catch (InterruptedException localInterruptedException) {
      }
    Vector localVector = new Vector();
    return this.inputStream;
  }

  public synchronized void done()
  {
    this.done = true;
    notifyAll();
  }
}