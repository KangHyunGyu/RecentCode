package com.e3ps.portal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.e3ps.approval.bean.ApprovalListData;
import com.e3ps.change.beans.ECAData;
import com.e3ps.common.jdf.config.Config;
import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.StringUtil;
import com.e3ps.portal.service.PortalHelper;
import com.e3ps.project.beans.IssueData;
import com.e3ps.project.beans.ProjectTaskData;
import com.e3ps.workspace.bean.NoticeData;

import wt.org.WTUser;
import wt.session.SessionHelper;
import wt.util.WTException;

@Controller
@RequestMapping("/portal")
public class PortalController {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PORTAL.getName());
	
	@RequestMapping("/main")
	public ModelAndView main(@RequestParam Map<String, Object> reqMap, HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		
		try {
			List<NoticeData> notice = PortalHelper.manager.getNoticeList();
			List<ApprovalListData> approval = PortalHelper.manager.getApprovalList();
			//List<RunTaskData> runTask = PortalHelper.manager.getTodoList();
			List<ProjectTaskData> task = PortalHelper.manager.getDelayTaskList();
			List<ECAData> eca = PortalHelper.manager.getECAList();
			List<IssueData> issue = PortalHelper.manager.getIssueList();
			
			System.out.println(approval.toString());
			
			model.addObject("notice", notice);
			model.addObject("approval", approval);
			model.addObject("task", task);
			model.addObject("eca", eca);
			model.addObject("issue", issue);
			model.setViewName("main:/portal/main");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping("/getUserInfo")
	public Map<String, Object> getUserInfo(@RequestBody Map<String, Object> reqMap) {

		Map<String, Object> map = new HashMap<String, Object>();

		try {

			WTUser user = (WTUser) SessionHelper.manager.getPrincipal();

			String userName = user.getFullName() + "(" + user.getName() + ")";
			map.put("userName", userName);
			map.put("result", true);
		} catch (WTException e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "오류가 발생했습니다.<br>관리자에게 문의하시기 바랍니다." + "<br> ERROR = " + e.getLocalizedMessage());
		}

		return map;
	}
	
	@ResponseBody
	@RequestMapping("/logout")
	public Map<String, Object> logout(@RequestBody Map<String, Object> reqMap, HttpServletRequest request) {

		Map<String, Object> map = new HashMap<String, Object>();

		try {

			request.getSession().invalidate();
			
			Config conf = ConfigImpl.getInstance();
			String serverName = conf.getString("HTTP.HOST.URL");
			
			map.put("msg", "로그아웃 되었습니다.");
			map.put("redirectUrl", serverName);
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("msg", "오류가 발생했습니다.<br>관리자에게 문의하시기 바랍니다." + "<br> ERROR = " + e.getLocalizedMessage());
		}

		return map;
	}
	
	@RequestMapping("/sitemap")
	public ModelAndView sitemap() {
		ModelAndView model = new ModelAndView();
		
		try {
			model.setViewName("main:/portal/sitemap");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	@RequestMapping("/include_menuContents")
	public ModelAndView include_menuContents(@RequestParam Map<String, Object> reqMap) throws Exception {
		ModelAndView model = new ModelAndView();
		
		String alias = StringUtil.checkNull((String)reqMap.get("alias"));
		model.addObject("alias", alias);
		model.setViewName("include:/portal/menuContents");
		return model;
	}
}
