package com.e3ps.common.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.e3ps.admin.util.AdminUtil;
import com.e3ps.common.jdf.config.ConfigImpl;

import wt.util.WTProperties;



public class AuthInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		String requestURI = request.getRequestURI();
		String[] uriArr = requestURI.split("/");
		String contextName = ConfigImpl.getInstance().getString("product.context.name");
		if(requestURI.contains(contextName) && uriArr.length > 3) {
			if(!isAjaxRequest(request) && !AdminUtil.hasMenuAuthority(requestURI)) {
				String hostName = request.getServerName();
				String url = "http://"+hostName +"/Windchill/jsp/admin/accessForbid.jsp";
				response.sendRedirect(url);
				return false;
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		
	}
	
	private boolean isAjaxRequest(HttpServletRequest request) {
	    String header = StringUtil.checkNull(request.getHeader("AJAX"));
	    
	    if("true".equals(header)) {
	    	return true;
	    }else {
	    	return false;
	    }
	    
	}

}
