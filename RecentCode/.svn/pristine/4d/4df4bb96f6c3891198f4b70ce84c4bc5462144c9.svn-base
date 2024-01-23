package com.e3ps.project;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.servlet.view.AbstractView;

public class JsonView extends AbstractView {
	
	public JsonView() {
		
	}
	
	@Override
	protected void renderMergedOutputModel(
				Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		Object jsonObject = model.get("jsonObject");
		
		String json = null;
		if (jsonObject != null) {
			if (jsonObject instanceof JSONObject) {
				json = ((JSONObject)jsonObject).toString();
			} else if (jsonObject instanceof JSONArray) {
				json = ((JSONArray)jsonObject).toString();
			}
		}
		
		this.setContentType("application/json; charset=utf-8");
		response.setContentType(this.getContentType());
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "utf-8"));
			writer.print(json);
			writer.flush();
		} finally {
			if (writer != null) writer.close();
		}
	}
}
