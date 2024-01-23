package com.e3ps.common.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.e3ps.common.log4j.Log4jPackages;

public class SessionCheckFilter implements Filter{

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
	@SuppressWarnings("unused")
	private FilterConfig filterConfig;

	public void init(FilterConfig paramFilterConfig) throws ServletException {
		this.filterConfig = paramFilterConfig;
	}

	public void destroy() {
	}

	@SuppressWarnings("deprecation")
	public void doFilter(ServletRequest paramServletRequest,
			ServletResponse paramServletResponse, FilterChain paramFilterChain)
			throws IOException, ServletException {

		LOGGER.info("############ SessionCheckFilter START#####################");
		String requestURI = ((HttpServletRequest) paramServletRequest).getRequestURI();

		LOGGER.info("doFilter requestURI == "  + requestURI);
		
	
		HttpServletRequest request = (HttpServletRequest) paramServletRequest ;

		HttpSession session = request.getSession();
		
		
		
		String userId = (String)session.getAttribute("loginUserId");
		
		LOGGER.info("1doFilter userId == "  + userId);
		/*
		if(userId == null || userId.length() == 0){
			LOGGER.info("1.Session Non Exist");
			
			if(requestURI.indexOf("/Windchill/login/index.jsp") > -1 
					|| requestURI.indexOf("/Windchill/login/logincheck.jsp") > -1
					|| requestURI.indexOf("/Windchill/jsp/loginOK.jsp") > -1){
				
			}else{
				
					HttpServletResponse reponse = (HttpServletResponse)paramServletResponse;
					String hostName = request.getServerName();
					String url = "http://"+hostName;
					
					LOGGER.info("url =" +url);
					reponse.sendRedirect(url);
				
			}
			
			
		}else{
			LOGGER.info("2.Session Exist");
			if(requestURI.indexOf("/Windchill/login/index.jsp") > -1){
				
				
				HttpServletResponse reponse = (HttpServletResponse)paramServletResponse;
				String hostName = request.getServerName();
				String url = "http://"+hostName+CommonUtil.getURLString("/portal/main");
				
				//LOGGER.info("url =" +url);
				reponse.sendRedirect(url);
			}
			
			
			//paramFilterChain.doFilter((ServletRequest) paramServletRequest, paramServletResponse);
		}
		
	*/
		
		paramFilterChain.doFilter((ServletRequest) paramServletRequest, paramServletResponse);
		
		LOGGER.info("############ SessionCheckFilter END#####################");
		
	}

	
}
