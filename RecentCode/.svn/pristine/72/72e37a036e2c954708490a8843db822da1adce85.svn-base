package com.e3ps.groupware.workprocess;

import java.util.Enumeration;

import wt.fc.Persistable;
import wt.inf.container.WTContained;
import wt.inf.container.WTContainerHelper;
import wt.inf.container.WTContainerRef;
import wt.lifecycle.LifeCycleHelper;
import wt.lifecycle.LifeCycleManaged;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.org.WTPrincipal;
import wt.pom.Transaction;
import wt.session.SessionHelper;
import wt.util.WTException;
import wt.workflow.engine.WfEngineHelper;
import wt.workflow.engine.WfProcess;

import com.e3ps.groupware.service.WFItemHelper;

public class LifeCycleMgr implements RemoteAccess {

	public static LifeCycleManaged processReassign(
			LifeCycleManaged lifecyclemanaged, String state, String creatorId)
			throws WTException {
		if (!RemoteMethodServer.ServerFlag) {
			try {
				Class argTypes[] = { LifeCycleManaged.class, String.class,
						String.class, String.class };
				Object argValues[] = { lifecyclemanaged, state, creatorId };
				return (LifeCycleManaged) RemoteMethodServer.getDefault()
						.invoke("processReassign",
								"e3ps.groupware.workprocess.LifeCycleMgr", null,
								argTypes, argValues);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		Transaction trx = new Transaction();

		WTPrincipal oprincipal = null;
		try {
			oprincipal = SessionHelper.manager.getPrincipal();
			trx.start();

			if (creatorId != null && creatorId.length() > 0) {
				SessionHelper.manager.setPrincipal(creatorId);
				System.out.println("$$$$$$ Session change .... &&&&&&& ");
			}
			WTPrincipal nprincipal = SessionHelper.manager.getPrincipal();
			System.out.println("new Session : " + nprincipal.getName());

			if (lifecyclemanaged instanceof Persistable) {
				WFItemHelper.service
						.deleteWFItem((Persistable) lifecyclemanaged);
				System.out.println("WFItem delete ......");
			}

			WTContainerRef wtcontainerref = null;
			if (lifecyclemanaged instanceof WTContained)
				wtcontainerref = ((WTContained) lifecyclemanaged)
						.getContainerReference();
			else
				wtcontainerref = WTContainerHelper.service.getClassicRef();

			Enumeration queryresult = WfEngineHelper.service.getAssociatedProcesses(lifecyclemanaged, null);
			WfProcess wfprocess = null;
			while (queryresult.hasMoreElements()) {
				wfprocess = (WfProcess) queryresult.nextElement();
				WfEngineHelper.service.deleteProcess(wfprocess);
				System.out.println("WfProcess delete ......");
			}

			//State s = State.toState(state);
			lifecyclemanaged = (LifeCycleManaged) LifeCycleHelper.service
					.reassign(lifecyclemanaged, lifecyclemanaged.getLifeCycleTemplate());
			System.out.println(lifecyclemanaged.getPersistInfo()
					.getObjectIdentifier().getStringValue()
					+ " ���μ�����  ������մϴ�.");

			SessionHelper.manager.setPrincipal(oprincipal.getName());
			trx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(lifecyclemanaged.getPersistInfo()
					.getObjectIdentifier().getStringValue()
					+ " ���μ�����  ����� ����.");
			trx.rollback();

			SessionHelper.manager.setPrincipal(oprincipal.getName());
		}
		return lifecyclemanaged;
	}

}
