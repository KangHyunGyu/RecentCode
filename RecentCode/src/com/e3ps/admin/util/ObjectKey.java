package com.e3ps.admin.util;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.e3ps.change.EChangeOrder2;
import com.e3ps.change.EChangeRequest2;
import com.e3ps.distribute.DistributeDocument;
import com.e3ps.distribute.DistributePartToEpmLink;
import com.e3ps.distribute.DistributeToPartLink;
import com.e3ps.doc.E3PSDocument;
import com.e3ps.project.EProject;
import com.e3ps.project.issue.IssueRequest;
import com.e3ps.project.issue.IssueSolution;

import wt.doc.WTDocument;
import wt.epm.EPMDocument;
import wt.part.WTPart;

public enum ObjectKey {
	
	OBJECTKEY( Arrays.asList(
			Map.ofEntries(new AbstractMap.SimpleEntry<String, Object>("code", "DASHBOARD"),
					  new AbstractMap.SimpleEntry<String, Object>("name", "대시보드"),	  
					  new AbstractMap.SimpleEntry<String, Object>("objectList", new ArrayList<String>())
					  ),
			
			Map.ofEntries(new AbstractMap.SimpleEntry<String, Object>("code", "DISTRIBUTE"),
					  new AbstractMap.SimpleEntry<String, Object>("name", "배포"),	  
					  new AbstractMap.SimpleEntry<String, Object>("objectList",new ArrayList<String>(List.of(
							  DistributeDocument.class.getName())))
					  ),
			
			Map.ofEntries(new AbstractMap.SimpleEntry<String, Object>("code", "DOC"),
					  new AbstractMap.SimpleEntry<String, Object>("name", "문서"),	  
					  new AbstractMap.SimpleEntry<String, Object>("objectList",new ArrayList<String>(List.of(
							  E3PSDocument.class.getName())))
					  ),
			
			Map.ofEntries(new AbstractMap.SimpleEntry<String, Object>("code", "PART"),
					  new AbstractMap.SimpleEntry<String, Object>("name", "부품"),	  
					  new AbstractMap.SimpleEntry<String, Object>("objectList",new ArrayList<String>(List.of(
							  WTPart.class.getName())))
					  ),
			
			Map.ofEntries(new AbstractMap.SimpleEntry<String, Object>("code", "EPM"),
					  new AbstractMap.SimpleEntry<String, Object>("name", "도면"),	  
					  new AbstractMap.SimpleEntry<String, Object>("objectList",new ArrayList<String>(List.of(
							  EPMDocument.class.getName())))
					  ),
			
			Map.ofEntries(new AbstractMap.SimpleEntry<String, Object>("code", "CHANGE"),
					  new AbstractMap.SimpleEntry<String, Object>("name", "설계변경"),	  
					  new AbstractMap.SimpleEntry<String, Object>("objectList",new ArrayList<String>(List.of(
							  EChangeOrder2.class.getName(),
							  EChangeRequest2.class.getName())))
					  ),
			
			Map.ofEntries(new AbstractMap.SimpleEntry<String, Object>("code", "ECO"),
					  new AbstractMap.SimpleEntry<String, Object>("name", "ECO"),	  
					  new AbstractMap.SimpleEntry<String, Object>("objectList",new ArrayList<String>(List.of(
							  EChangeOrder2.class.getName())))
					  ),
			
			Map.ofEntries(new AbstractMap.SimpleEntry<String, Object>("code", "ECR"),
					  new AbstractMap.SimpleEntry<String, Object>("name", "ECR"),	  
					  new AbstractMap.SimpleEntry<String, Object>("objectList",new ArrayList<String>(List.of(
							  EChangeRequest2.class.getName())))
					  ),
			
			Map.ofEntries(new AbstractMap.SimpleEntry<String, Object>("code", "PROJECT"),
					  new AbstractMap.SimpleEntry<String, Object>("name", "프로젝트"),	  
					  new AbstractMap.SimpleEntry<String, Object>("objectList",new ArrayList<String>(List.of(
							  EProject.class.getName(),
							  IssueRequest.class.getName(),
							  IssueSolution.class.getName())))
					  )
			)
	);
			
			
	
	private final List<Map<String,Object>> keyList;
	
	private ObjectKey(final List<Map<String,Object>> keyList){
		this.keyList = keyList;
	}
	
	public List<Map<String,Object>> getKeyList() {
		return this.keyList;
	}
	
}
