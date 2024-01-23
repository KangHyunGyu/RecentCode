package com.e3ps.common.workflow;

import wt.fc.QueryResult;
import wt.lifecycle.LifeCycleManaged;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.session.SessionHelper;
import wt.util.WTException;
import wt.workflow.engine.WfEngineHelper;
import wt.workflow.engine.WfProcess;

public class DeleteWorkflow
  implements RemoteAccess
{
  public static void delete(LifeCycleManaged _lcm)
  {
    if (!RemoteMethodServer.ServerFlag)
    {
      try
      {
        Class[] argTypes = { LifeCycleManaged.class };
        Object[] argValues = { _lcm };
        RemoteMethodServer.getDefault().invoke("delete", "com.e3ps.common.workflow.DeleteWorkflow", 
          null, argTypes, argValues);
        return;
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
    }

    try
    {
      SessionHelper.manager.setAdministrator();
      QueryResult qr = E3PSWorkflowHelper.manager.getWfProcess(_lcm);
      while (qr.hasMoreElements())
      {
        WfProcess wfProcess = (WfProcess)qr.nextElement();
        WfEngineHelper.service.deleteProcess(wfProcess);
      }
    }
    catch (WTException e)
    {
      e.printStackTrace();
    }
  }
}