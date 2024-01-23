package com.e3ps.project;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.e3ps.project.beans.TaskEditHelper;

@Controller
@RequestMapping("/project")
public class TaskEditController {
	protected static final Logger logger = LoggerFactory.getLogger(TaskEditController.class);
	
	@RequestMapping("/rightClickMenu")
	public ModelAndView rightClickMenu(HttpServletRequest request, HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("TaskEditController.rightClickMenu");
		}
		try {

			String xmlParsing = TaskEditHelper.service.rightClickMenu();
			
			JSONObject jsonResponse = new JSONObject();
			
			// #. 응답 json 생성
			jsonResponse.put("success", true);
			jsonResponse.put("xmlData", xmlParsing);
			
			return new ModelAndView("jsonView", "jsonObject", jsonResponse);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
	}
	
	@RequestMapping("/loadEditTaskGridXml")
	public ModelAndView loadEditTaskGridXml(HttpServletRequest request, HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("TaskEditController.loadEditTaskGridXml");
		}
		try {
//			
//			String httpVersion = request.getProtocol();
//			if(httpVersion.equals("HTTP/1.0")){
//				response.setDateHeader("Expires", 0);
//				response.setHeader("Pragma", "no-cache");
//			}else if(httpVersion.equals("HTTP/1.1")){
//				response.setHeader("Cache-Control", "no-cache");
//			}
//			
			String oid = request.getParameter("oid");
			String xmlParsing = TaskEditHelper.service.createEditTaskGridXml(oid);
			//String xmlParsing = TaskEditHelper.service.loadEditTaskGridXml(oid);
			
			JSONObject jsonResponse = new JSONObject();
			
			// #. 응답 json 생성
			jsonResponse.put("success", true);
			jsonResponse.put("xmlData", xmlParsing);
			
			return new ModelAndView("jsonView", "jsonObject", jsonResponse);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
	}
	
	@RequestMapping("/editPreTask")
	public ModelAndView editPreTask(HttpServletRequest request, HttpServletResponse response){
		
		if (logger.isDebugEnabled()) {
			logger.debug("TaskEditController.editPreTask");
		}
		try {
			Map map = request.getParameterMap();
			return new ModelAndView("popup:/project/EditPreTask", map);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException(e);
		}
	}
	
}
