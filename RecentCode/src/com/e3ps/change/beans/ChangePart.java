package com.e3ps.change.beans;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Hashtable;

import wt.fc.PersistenceHelper;
import wt.fc.ReferenceFactory;
import wt.folder.Folder;
import wt.folder.FolderEntry;
import wt.folder.FolderHelper;
import wt.inf.container.WTContainerRef;
import wt.lifecycle.LifeCycleHelper;
import wt.lifecycle.LifeCycleManaged;
import wt.lifecycle.LifeCycleTemplate;
import wt.part.WTPart;
import wt.pdmlink.PDMLinkProduct;
import wt.pom.Transaction;
import wt.util.WTException;
import wt.vc.VersionControlHelper;
import wt.vc.Versioned;

import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;

public class ChangePart implements wt.method.RemoteAccess {
	
	static final boolean SERVER = wt.method.RemoteMethodServer.ServerFlag;
	
	public static ChangePart manager = new ChangePart();
	
	public  HashMap revisePart(String[] oid){
		
		try{
			for( int i = 0 ; i <oid.length ; i++){
				
				reviseUpdate(oid[i]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public WTPart reviseUpdate(final String oid) throws Exception
    {
        if ( !SERVER ) {
            Class argTypes[] = new Class[] { String.class };
            Object args[] = new Object[] { oid };
            try {
                return (WTPart) wt.method.RemoteMethodServer.getDefault().invoke("reviseUpdate", null, this, argTypes, args);
            }
            catch ( RemoteException e ) {
                e.printStackTrace();
                throw new WTException(e);
            }
            catch ( InvocationTargetException e ) {
                e.printStackTrace();
                throw new WTException(e);
            }
            catch ( Exception e ) {
                e.printStackTrace();
                throw e;
            }
        }

        Hashtable rtnVal = null;
        Transaction trx = new Transaction();

        //String oid = (String) hash.get("oid");

        try {
            trx.start();

            if ( StringUtil.checkString(oid) ) {
                ReferenceFactory rf = new ReferenceFactory();
                Versioned vs = (Versioned) rf.getReference(oid).getObject();

                //개정전 셋팅정보 get(lifecycle, folder, view)
                String lifecycle = ((LifeCycleManaged) vs).getLifeCycleName();
                Folder folder = FolderHelper.service.getFolder((FolderEntry) vs);

                WTPart part = (WTPart) VersionControlHelper.service.newVersion(vs);
                PDMLinkProduct e3psProduct = WCUtil.getPDMLinkProduct();
                WTContainerRef wtContainerRef = WTContainerRef.newWTContainerRef(e3psProduct);
                part.setContainer(e3psProduct);

                //폴더 셋팅
                FolderHelper.assignLocation((FolderEntry) part, folder);

                //라이프사이클 셋팅(Default 고정임)
                LifeCycleTemplate tmpLifeCycle = LifeCycleHelper.service.getLifeCycleTemplate(lifecycle, wtContainerRef);
                part = (WTPart) LifeCycleHelper.setLifeCycle(part, tmpLifeCycle);

                //저장
                part = (WTPart) PersistenceHelper.manager.save(part);

                //hash.put("oid", CommonUtil.getOIDString(part));

                //rtnVal = modify(hash, true);
            }

            trx.commit();
            trx = null;
        }
        catch ( Exception e ) {
            throw e;
        }
        finally {
            if ( trx != null ) {
                trx.rollback();
            }
        }
        return null;//rtnVal;
    }
	
}
