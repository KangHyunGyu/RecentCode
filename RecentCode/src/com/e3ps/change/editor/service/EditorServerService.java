package com.e3ps.change.editor.service;

import java.util.ArrayList;
import java.util.Hashtable;

import org.w3c.dom.Document;

import wt.method.RemoteInterface;
import wt.xml.XMLLob;

import com.e3ps.change.EChangeOrder2;
import com.e3ps.change.EOEul;
import com.e3ps.change.editor.EOActionTempAssyData;


@RemoteInterface
public interface EditorServerService {
	
	EOEul saveEul(EChangeOrder2 eo,String poid, XMLLob lob,ArrayList editParts,EOEul eul) throws Exception;
	
	String getSapString(Document document) throws Exception;
	
	Hashtable getSapData(Document document) throws Exception;
	
	EChangeOrder2 saveChangeData(EChangeOrder2 eco, Hashtable hash) throws Exception;
	
	EChangeOrder2 saveChangeData(EChangeOrder2 eco, EOActionTempAssyData adata) throws Exception;
	
}
