package com.e3ps.project.beans;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.e3ps.project.EProject;
import com.e3ps.project.EProjectNode;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.ProjectRole;
import com.e3ps.project.RoleUserLink;
import com.e3ps.project.issue.IssueRequest;
import com.e3ps.project.issue.IssueSolution;
import com.e3ps.project.service.ProjectMemberHelper;

import wt.org.WTUser;

/**
 * 예외) 프로젝트 등록 결재 메일은 일반 결제 서비스에 포함 되어 있습니다.
 * @author yhjang
 *
 */
public class ProjectMailBroker{
	
	public static final ProjectMailBroker manager = new ProjectMailBroker();
	
	/**
	 * 프로젝트 시작 알림
	 * to : 프로젝트 맴버
	 */
	public void projectStart(EProject project){
		try{
			ArrayList ulist = ProjectMemberHelper.service.getUserList(project);
			
			Hashtable hash = new Hashtable();
			
			for(int i=0; i< ulist.size(); i++){
				Object[] o = (Object[])ulist.get(i);
				ProjectRole role = (ProjectRole)o[0];
				RoleUserLink link = (RoleUserLink)o[1];
				
				if(link!=null){
					WTUser user = (WTUser)link.getUser();
					String toUser = user.getName();
					
					PmsMailData data = (PmsMailData)hash.get(toUser);
					
					if(data==null){
						data = new PmsMailData();
						data.add(toUser);
						hash.put(toUser, data);
					}
					
					data.temp.add(role.getName());
				}
			}
				
			Enumeration en = hash.keys();
				
			while(en.hasMoreElements()){
				String key = (String)en.nextElement();
				PmsMailData data = (PmsMailData)hash.get(key);
				
				StringBuffer roles = new StringBuffer();
				for(int i=0; i< data.temp.size(); i++){
					String role = (String)data.temp.get(i);
					roles.append(role);
					if(data.temp.size()>i+1){
						roles.append(",");
					}
				}
				
				data.setTitle(project.getName() + " 프로젝트의 " + roles + "역할로 지정 되었습니다.");
				
				StringBuffer strContents = new StringBuffer();
				strContents.append(project.getName());
				strContents.append(" 프로젝트가 시작 되었습니다.<br>");
				strContents.append(data.getTitle());
				//strContents.append("<br><a href='/Windchill/extcore/kores/project/ViewProject.jsp?oid=");
				//strContents.append(project.getPersistInfo().getObjectIdentifier().toString());
				//strContents.append("'>프로젝트 상세정보 화면으로 이동</a>");
				data.setContents(strContents.toString());
				//data.sendMail();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 태스크 시작알림
	 * to:태스크 담당자
	 */
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
	
	
	/**
	 * 태스크 지연 알림
	 * to:태스크 담당자
	 */
	public void delayedTask(ArrayList list){
		try{
			for(int j=0; j< list.size(); j++){
				ETaskNode task = (ETaskNode)list.get(j);
				
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
				
				data.setTitle(task.getProject().getName() + " 프로젝트의 " + task.getName() + " 태스크가 지연 되었습니다.");
				StringBuffer strContents = new StringBuffer();
				strContents.append(data.getTitle());
				//strContents.append("<br><a href='/Windchill/extcore/kores/project/ViewProject.jsp?oid=");
				//strContents.append(task.getPersistInfo().getObjectIdentifier().toString());
				//strContents.append("'>태스크 상세정보 화면으로 이동</a>");
				data.setContents(strContents.toString());
				//data.sendMail();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 종료 3일전 알림
	 * to:태스크 담당자
	 */
	public void endDateNoti(ArrayList list){
		try{
			for(int j=0; j< list.size(); j++){
				ETaskNode task = (ETaskNode)list.get(j);
				
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
				
				data.setTitle(task.getProject().getName() + " 프로젝트의 " + task.getName() + " 태스크 종료까지 3일 남았습니다.");
				StringBuffer strContents = new StringBuffer();
				strContents.append(data.getTitle());
				//strContents.append("<br><a href='/Windchill/extcore/kores/project/ViewProject.jsp?oid=");
				//strContents.append(task.getPersistInfo().getObjectIdentifier().toString());
				//strContents.append("'>태스크 상세정보 화면으로 이동</a>");
				data.setContents(strContents.toString());
				//data.sendMail();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 이슈 등록 알림
	 * to:이슈 담당자
	 */
	public void createIssueMail(IssueRequest issue){
		try{
			PmsMailData data = new PmsMailData();
			WTUser user = issue.getManager();
			data.add(user.getName());
				
			data.setTitle(issue.getName() + " 이슈 담당자로 지정되었습니다.");
				StringBuffer strContents = new StringBuffer();
				strContents.append(data.getTitle());
				strContents.append("<br>");
				strContents.append("프로젝트 : " + issue.getTask().getProject().getName());
				strContents.append("<br>");
				strContents.append("태스크 : " + issue.getTask().getName());
				strContents.append("<br>");
				strContents.append("제기자 : " + issue.getCreator().getFullName());
				//strContents.append("<br><a href='/Windchill/extcore/kores/project/ViewProject.jsp?oid=");
				//strContents.append(issue.getTask().getPersistInfo().getObjectIdentifier().toString());
				//strContents.append("'>태스크 상세정보 화면으로 이동</a>");
				data.setContents(strContents.toString());
				
				data.sendMail();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 해결방안 등록 알림
	 * to:이슈 등록자
	 */
	public void createSolutionMail(IssueSolution solution){
		try{
			IssueRequest issue = solution.getRequest();
			
			PmsMailData data = new PmsMailData();
			WTUser user = issue.getCreator();
			data.add(user.getName());
				
			data.setTitle(issue.getName() + " 이슈 해결방인이 등록 되었습니다.");
				StringBuffer strContents = new StringBuffer();
				strContents.append(data.getTitle());
				strContents.append("<br>");
				strContents.append("프로젝트 : " + issue.getTask().getProject().getName());
				strContents.append("<br>");
				strContents.append("태스크 : " + issue.getTask().getName());
				strContents.append("<br>");
				strContents.append("담당자 : " + issue.getManager().getFullName());
				//strContents.append("<br><a href='/Windchill/extcore/kores/project/ViewProject.jsp?oid=");
				//strContents.append(issue.getTask().getPersistInfo().getObjectIdentifier().toString());
				//strContents.append("'>태스크 상세정보 화면으로 이동</a>");
				data.setContents(strContents.toString());
				data.sendMail();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 프로그램 등록 알림
	 * to:PM
	 */
/*	public void createProgramMail(EProgram program){
		try{
			QueryResult qq = PersistenceHelper.manager.navigate(program,"plan",ProgramPlanLink.class);
			while(qq.hasMoreElements()){
				EProjectPlan pp = (EProjectPlan)qq.nextElement();
				ProjectPlanData pdata = new ProjectPlanData(pp);
				
				
				if(pp.getSubPm()!=null){
					PmsMailData data = new PmsMailData();
					data.add(pp.getSubPm().getName());
					data.setTitle(pdata.getName() + " 프로젝트의 PM으로 지정 되었습니다.");
					
					StringBuffer strContents = new StringBuffer();
					strContents.append(data.getTitle());
					strContents.append("<br>");
					strContents.append("프로그램 : " + program.getName());
					strContents.append("<br>");
					strContents.append("프로젝트 : " + pdata.getName());
					strContents.append("<br>");
					strContents.append("기한일 : " + pp.getDeadLine()!=null?DateUtil.getDateString(pp.getDeadLine(),"d"):"");
					//strContents.append("<br><a href='/Windchill/extcore/kores/project/CreateProject.jsp?command=plan&planOid=");
					//strContents.append(pp.getPersistInfo().getObjectIdentifier().toString());
					//strContents.append("'>등록 화면으로 이동</a>");
					data.setContents(strContents.toString());
					data.sendMail();
				}
				
			}
		
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
*/	
	/**
	 * 프로젝트 완료 알림
	 * to:PM & 프로그램 관리자
	 * @param project
	 */
/*	public void completeProject(EProjectNode project){
		try{
			
			PmsMailData data = new PmsMailData();
			
			ProjectNodeData pdata = new ProjectNodeData(project);
			WTUser pm = pdata.getPM();
			
			data.add(pm.getName());
			
			QueryResult qq = PersistenceHelper.manager.navigate(project,"plan",ProjectPlanLink.class);
			
			if(qq.hasMoreElements()){
				EProjectPlan pp = (EProjectPlan)qq.nextElement();
				EProgram program = pp.getProgram();
				WTUser mpm = program.getPm();
				data.add(mpm.getName());
			}
			
			
			data.setTitle(project.getName() + " 프로젝트가 완료 되었습니다.");
			
			StringBuffer strContents = new StringBuffer();
			strContents.append(data.getTitle());
			strContents.append("<br>");
			strContents.append("프로젝트 : " + project.getName());
			strContents.append("<br>");
			strContents.append(data.getTitle());
			//strContents.append("<br><a href='/Windchill/extcore/kores/project/ViewProject.jsp?oid=");
			//strContents.append(project.getPersistInfo().getObjectIdentifier().toString());
			//strContents.append("'>상세정보 화면으로 이동</a>");
			data.setContents(strContents.toString());
			data.sendMail();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}*/
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
		//System.out.println("========= Mail 전송 ==========");
		//System.out.println("title : " + title);
		//System.out.println("contents : " + contents);
		//System.out.println("toUsers : " + toUsers);
		//System.out.println("=================================");
		
		String[] toNames = new String[toUsers.size()];
		for(int i=0; i< toUsers.size(); i++){
			toNames[i] = (String)toUsers.get(i);
		}
	}
}