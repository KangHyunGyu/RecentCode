package com.e3ps.project.service;

import java.util.ArrayList;

import wt.org.WTUser;
import wt.services.StandardManager;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.project.EProjectNode;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.ProjectRole;
import com.e3ps.project.RoleUserLink;

public class StandardProjectMailBrokerSerivce extends StandardManager implements ProjectMailBrokerSerivce {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PROJECT.getName());
	
	public static StandardProjectMailBrokerSerivce newStandardProjectMailBrokerSerivce() throws Exception {
		final StandardProjectMailBrokerSerivce instance = new StandardProjectMailBrokerSerivce();
		instance.initialize();
		return instance;
	}
	
	/**
	 * 태스크 시작알림
	 * to:태스크 담당자
	 */
	@Override
	public void taskStart(ETaskNode task){
		try{
			EProjectNode project = task.getProject();
			ArrayList ulist = ProjectMemberHelper.service.getOwner(task);
			
			PmsMailData data = new PmsMailData();
			
			for(int i=0; i< ulist.size(); i++){
				Object[] o = (Object[])ulist.get(i);
				ProjectRole role = (ProjectRole)o[0];
				RoleUserLink link = (RoleUserLink)o[1];
				
				if(link!=null){
					WTUser user = (WTUser)link.getUser();
					
					data.add(user.getName());
				}
			}
					
			data.setTitle(project.getName() + " 프로젝트의 " + task.getName() + " 태스크가 시작 되었습니다.");
			StringBuffer strContents = new StringBuffer();
			strContents.append(data.getTitle());
			//strContents.append("<br><a href='/Windchill/extcore/kores/project/ViewProject.jsp?oid=");
			//strContents.append(task.getPersistInfo().getObjectIdentifier().toString());
			//strContents.append("'>태스크 상세정보 화면으로 이동</a>");
			data.setContents(strContents.toString());
			//data.sendMail();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}

class PmsMailData{
	
	String title;
	String contents;
	ArrayList toUsers = new ArrayList();
	
	String fromUser = "PLM System";
	String module = "Project";
	
	public ArrayList temp = new ArrayList();
	
	public void setTitle(String title){
		this.title= title;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setContents(String contents){
		this.contents= contents;
	}
	
	public void add(String toUser){
		toUsers.add(toUser);
	}
	
	
	public void sendMail(){
		//LOGGER.info("========= Mail 전송 ==========");
		//LOGGER.info("title : " + title);
		//LOGGER.info("contents : " + contents);
		//LOGGER.info("toUsers : " + toUsers);
		//LOGGER.info("=================================");
		
		String[] toNames = new String[toUsers.size()];
		for(int i=0; i< toUsers.size(); i++){
			toNames[i] = (String)toUsers.get(i);
		}
	}
}