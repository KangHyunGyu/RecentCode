package com.e3ps.admin.util;

import java.util.AbstractMap;
import java.util.Map;

import com.e3ps.change.EChangeOrder2;
import com.e3ps.change.EChangeRequest2;
import com.e3ps.distribute.DistributeDocument;
import com.e3ps.project.EProject;

import wt.doc.WTDocument;
import wt.epm.EPMDocument;
import wt.part.WTPart;

public enum AuthKey {
	
	AUTHKEY(Map.ofEntries(
			new AbstractMap.SimpleEntry<String, String>("ALLDENY", "전부 거부"),
			new AbstractMap.SimpleEntry<String, String>("READ", "읽기")
			//new AbstractMap.SimpleEntry<String, String>("CRUD", "전부 허용")
		));
	
	private final Map<String,String> keyMap;
	
	private AuthKey(final Map<String,String> keyMap){
		this.keyMap = keyMap;
	}
	
	public Map<String,String> getKeyMap() {
		return this.keyMap;
	}
	
}
