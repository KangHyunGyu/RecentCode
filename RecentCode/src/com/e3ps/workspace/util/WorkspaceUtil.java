package com.e3ps.workspace.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.e3ps.common.log4j.Log4jPackages;

public class WorkspaceUtil {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.WORKSPACE.getName());
	
	/**
	 * @desc : notifyType 가져오기
	 * @author : tsjeong
	 * @date : 2019. 11. 18.
	 * @method : getNotifyTypeList
	 * @return : List<Map<String,String>>
	 * @throws Exception
	 */
	public static List<Map<String,String>> getNotifyTypeList() throws Exception{
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();

		Map<String,String> map = new HashMap<String,String>();
		
		map.put("key", "전체 공지");
		map.put("value", "전체 공지");
		list.add(map);
		
		map = new HashMap<String,String>();
		
		map.put("key", "작업공간 공지");
		map.put("value", "작업공간 공지");
		list.add(map);
		
		map = new HashMap<String,String>();
		
		map.put("key", "프로젝트 관리 공지");
		map.put("value", "프로젝트 관리 공지");
		list.add(map);
		
		map = new HashMap<String,String>();

		map.put("key", "설계변경 관리 공지");
		map.put("value", "설계변경 관리 공지");
		list.add(map);
		
		map = new HashMap<String,String>();

		map.put("key", "도면 관리 공지");
		map.put("value", "도면 관리 공지");
		list.add(map);

		map = new HashMap<String,String>();
		
		map.put("key", "부품/BOM 관리 공지");
		map.put("value", "부품/BOM 관리 공지");
		list.add(map);
		
		map = new HashMap<String,String>();
		
		map.put("key", "문서 관리 공지");
		map.put("value", "문서 관리 공지");
		map.put("selected", "selected");
		list.add(map);
		
		return list;
	}
}
