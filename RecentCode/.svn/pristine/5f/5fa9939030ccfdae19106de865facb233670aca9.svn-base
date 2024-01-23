package com.e3ps.dashboard.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.e3ps.dashboard.bean.ProjectDashboardData;
import com.e3ps.project.EProject;
import com.e3ps.project.beans.ProjectQuery;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.query.QuerySpec;
import wt.services.ServiceFactory;

public class ProjectDashboardHelper {
	public static ProjectDashboardService service = ServiceFactory.getService(ProjectDashboardService.class);
	public static final ProjectDashboardHelper manager = new ProjectDashboardHelper();
	
	
	public List<ProjectDashboardData> searchProject(Map<String, Object> reqMap) throws Exception {
		List<ProjectDashboardData> result = new ArrayList<ProjectDashboardData>();
		
		QuerySpec qs = ProjectQuery.manager.getProjectListQuery(reqMap);
		
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		while(qr.hasMoreElements()) {
			Object o[] = (Object[]) qr.nextElement();
			EProject project = (EProject) o[0];
			
			result.add(new ProjectDashboardData(project));
		}
		return result;
	}
	
	
	
	
}
