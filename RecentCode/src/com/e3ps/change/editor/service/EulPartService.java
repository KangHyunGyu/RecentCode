package com.e3ps.change.editor.service;

import java.rmi.RemoteException;
import java.util.Vector;

import wt.access.NotAuthorizedException;
import wt.fc.QueryResult;
import wt.iba.definition.IBADefinitionException;
import wt.lifecycle.State;
import wt.method.RemoteInterface;
import wt.part.WTPart;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;
import wt.vc.views.View;

@RemoteInterface
public interface EulPartService {

	
	WTPart getPart(String partNumber, String version) throws WTException, RemoteException, WTPropertyVetoException;

	WTPart getPart(WTPart part, View view) throws RemoteException, WTPropertyVetoException,
	WTException;
	
	WTPart getPart(String number, View view) throws WTException, RemoteException,
	WTPropertyVetoException;
	
	WTPart getPart(String number) throws WTException, RemoteException, WTPropertyVetoException;
	
	WTPart getPlantPart(String number, String plant) throws WTException, RemoteException,
	WTPropertyVetoException;
	
	QueryResult getViews(WTPart part) throws Exception;
	
	Vector ancestorPart(WTPart part, View view) throws WTException;
	
	Vector ancestorPart(WTPart part) throws WTException;
	
	Vector ancestorPart(WTPart part, View view, State state) throws WTException;
	
	Vector descentLastPart(WTPart part, View view) throws WTException;
	
	Vector descentLastPart(WTPart part) throws WTException;
	
	Vector descentLastPart(WTPart part, View view, State state) throws WTException;
	
	void addLastVersionCondition(QuerySpec query, int idx) throws IBADefinitionException,
	NotAuthorizedException, RemoteException, WTException, QueryException, WTPropertyVetoException;
	
	void addSapCondition(QuerySpec query, int idx) throws IBADefinitionException,
	NotAuthorizedException, RemoteException, WTException, QueryException, WTPropertyVetoException;
	
	QuerySpec getQuerySpec(String number, String name, String model,
			boolean isLastest, boolean isMine, boolean isInWork, String views);
	
}
