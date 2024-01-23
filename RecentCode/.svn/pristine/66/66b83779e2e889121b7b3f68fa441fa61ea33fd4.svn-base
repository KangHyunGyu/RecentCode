package com.e3ps.part.bomLoader.service;

import java.util.List;
import java.util.Map;

import com.e3ps.part.bomLoader.bean.LoadBomData;

import wt.method.RemoteInterface;
import wt.part.WTPart;
import wt.part.WTPartUsageLink;
import wt.util.WTException;

@RemoteInterface
public interface BomLoaderService {

	WTPart createPartAction(LoadBomData data) throws Exception;

	WTPartUsageLink createWTPartUsageLink(WTPart pPart, WTPart cPart, String quantity) throws Exception;

	WTPart modifyPartAction(WTPart part, LoadBomData data) throws Exception;

	WTPartUsageLink modifyWTPartUsageLink(WTPartUsageLink link, String quantity, WTPart cPart) throws Exception;

	List<LoadBomData> loadBomAction(Map<String, Object> reqMap) throws Exception;

	List<LoadBomData> checkBomAction(Map<String, Object> reqMap) throws Exception;

	WTPart revisePartAction(WTPart part, LoadBomData data) throws Exception;

	void removeAllLink(WTPart parent) throws WTException;

	List<LoadBomData> checkPartAction(Map<String, Object> reqMap) throws Exception;

	List<LoadBomData> loadPartAction(Map<String, Object> reqMap) throws Exception;
}
