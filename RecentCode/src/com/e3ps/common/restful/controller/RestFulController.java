package com.e3ps.common.restful.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3ps.common.restful.service.RestFulHelper;

@Controller
@RequestMapping("/restFul")
public class RestFulController {

	@ResponseBody
	@RequestMapping("/deptSyncAction")
	public Map<String, Object> deptSyncAction() throws Exception{
		Map<String, Object> map = new HashMap<>();
		
		try {
			map = RestFulHelper.service.deptSyncAction();
		}catch(Exception e) {
			map.put("result", false);
			e.printStackTrace();
		}
				
		return map;
	}
	
	
	@ResponseBody
	@RequestMapping("/userSyncAction")
	public Map<String, Object> userSyncAction() throws Exception{
		Map<String, Object> map = new HashMap<>();
		try {
			RestFulHelper.service.userSyncAction();
			map.put("result", true);
		}catch(Exception e) {
			map.put("result", false);
			e.printStackTrace();
		}
		return map;
	}
}
