//package com.e3ps.stagegate.controller;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.ws.rs.Consumes;
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.QueryParam;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
//import com.e3ps.common.util.CommonUtil;
//import com.e3ps.doc.E3PSDocument;
//import com.e3ps.doc.bean.E3PSDocumentData;
//import com.e3ps.project.EProject;
//import com.e3ps.project.beans.ProjectData;
//import com.e3ps.project.beans.ProjectRoleData;
//import com.e3ps.project.service.ProjectMemberHelper;
//import com.e3ps.stagegate.StageGate;
//import com.e3ps.stagegate.bean.SGObjectValueData;
//import com.e3ps.stagegate.service.SgHelper;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiResponse;
//import io.swagger.annotations.ApiResponses;
//import wt.fc.PersistenceHelper;
//import wt.fc.QueryResult;
//import wt.query.QuerySpec;
//
//@Path("/wncRest")
//@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//@Api(value = "/wncRest", description = "This resource provides endpoints to work with and navigate objects that are linked together to "
//		+ "form tree structure. For example: Parts, Documents, and CAD documents.", tags = { "Structure Navigation" })
//public class StageGateRest {
//	@GET
//	@Path("/docList")
//	@ApiOperation(position = 0, response = Map.class, value = "Get an object's descendants resource to any number of levels.", notes = "The descendants are the children objects that the resource object requires. So then, an object's "
//			+ "descendants, when navigated recursively, form a tree structure. Each descendant has an array of "
//			+ "paths and each path has a treeId and parentId as a way to define the tree structure.")
//	@ApiResponses({
//			@ApiResponse(code = 400, message = "If one of the URL or query parameters is not in the correct format."),
//			@ApiResponse(code = 404, message = "If the specified objects does not exist."),
//			@ApiResponse(code = 500, message = "If an unexpected error occurs.") })
//	public Response docList( @QueryParam("number") String number, @QueryParam("name") String name)throws Exception {
//		Map<String, String> map = new HashMap<String, String>();
//		ArrayList<Map<String,String>> list = new ArrayList<Map<String, String>>();
//		
//		try {
//			map.put("number", number);
//			map.put("name", name);
//			list = test(map);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return Response.status(Response.Status.OK).entity(list).build();
//	}
//	
//	public static ArrayList<Map<String,String>> test(Map<String, String> reqMap) {
//		ArrayList<Map<String,String>> list = new ArrayList<Map<String, String>>();
//		
//		try {
//			
//			QuerySpec query = new QuerySpec();
//			QueryResult result = PersistenceHelper.manager.find(query);
//			while(result.hasMoreElements()){
//				Map<String, String> map = new HashMap<>();
//				Object[] obj = (Object[]) result.nextElement();
//				E3PSDocument doc = (E3PSDocument) obj[0];
//				
//				E3PSDocumentData data = new E3PSDocumentData(doc);
//				 
//				map.put("name", data.getName());
//				map.put("number", data.getNumber());
//				map.put("oid", data.getOid());
//				list.add(map);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return list;
//	}
//	
//	@GET
//	@Path("/summary")
//	@ApiOperation(position = 0, response = Map.class, value = "Get an object's descendants resource to any number of levels.", notes = "The descendants are the children objects that the resource object requires. So then, an object's "
//			+ "descendants, when navigated recursively, form a tree structure. Each descendant has an array of "
//			+ "paths and each path has a treeId and parentId as a way to define the tree structure.")
//	@ApiResponses({
//			@ApiResponse(code = 400, message = "If one of the URL or query parameters is not in the correct format."),
//			@ApiResponse(code = 404, message = "If the specified objects does not exist."),
//			@ApiResponse(code = 500, message = "If an unexpected error occurs.") })
//	public Response summary( @QueryParam("oid") String oid)throws Exception {
//		Map<String, Object> sgMap = new HashMap<String, Object>();
//		Map<String, Object> projectMap = new HashMap<String, Object>();
//		Map<String, Object> memberMap = new HashMap<String, Object>();
//		ArrayList<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
//		
//		try {
//			EProject project = (EProject) CommonUtil.getObject(oid);
//			String code = project.getCode();
//			StageGate sg = SgHelper.manager.getStageGate(code);
//			
//			ProjectData pData = new ProjectData(project);
//			List<ProjectRoleData> members = ProjectMemberHelper.manager.getRoleUserList(project);
//			List<SGObjectValueData> valueList = SgHelper.manager.getValueList(sg, "SUMMARY");
//			
//			projectMap.put("pData", pData);
//			sgMap.put("sData", valueList);
//			memberMap.put("members", members);
//			 
//			list.add(projectMap);
//			list.add(sgMap);
//			list.add(memberMap);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return Response.status(Response.Status.OK).entity(list).build();
//		
//	}
//	
//}