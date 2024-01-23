/**
 * 
 */
package com.e3ps.common.favorite.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3ps.common.favorite.bean.FavoriteData;
import com.e3ps.common.favorite.service.FavoriteHelper;
import com.e3ps.common.log4j.Log4jPackages;


@Controller
@RequestMapping("/favorite")
public class FavoriteController {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
	/**
	 * @desc	: 즐겨찾기 추가
	 * @author	: mnyu
	 * @date	: 2019. 12. 16.
	 * @method	: createFavoriteAction
	 * @param	: reqMap
	 * @return	: Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/createFavoriteAction")
	public Map<String, Object> createFavoriteAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String oid = FavoriteHelper.service.createFavoriteAction(reqMap);
			
			map.put("oid", oid);
			map.put("result", true);
			map.put("msg", "즐겨찾기 등록이 완료되었습니다.");
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	/**
	 * @desc	: 즐겨찾기 여부
	 * @author	: mnyu
	 * @date	: 2019. 12. 16.
	 * @method	: isFavorite
	 * @param	: reqMap
	 * @return	: Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/isFavorite")
	public Map<String, Object> isFavorite(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			String oid = FavoriteHelper.manager.isFavorite(reqMap);
			
			map.put("oid", oid);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	/**
	 * @desc	: 즐겨찾기 삭제
	 * @author	: mnyu
	 * @date	: 2019. 12. 16.
	 * @method	: deleteFavoriteAction
	 * @param	: reqMap
	 * @return	: Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/deleteFavoriteAction")
	public Map<String, Object> deleteFavoriteAction(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			FavoriteHelper.service.deleteFavoriteAction(reqMap);
			
			map.put("result", true);
			map.put("msg", "즐겨찾기 삭제가 완료되었습니다.");
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
	/**
	 * @desc	: 즐겨찾기 목록
	 * @author	: mnyu
	 * @date	: 2019. 12. 17.
	 * @method	: getFavoriteList
	 * @param	: reqMap
	 * @return	: Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/getFavoriteList")
	public Map<String, Object> getFavoriteList() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			List<FavoriteData> list = FavoriteHelper.manager.getFavoriteList();
			
			map.put("list", list);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "ERROR = " + e.getLocalizedMessage());
		}
		
		return map;
	}
}
