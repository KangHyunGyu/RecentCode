package com.e3ps.project.beans;

import com.e3ps.common.content.util.ContentUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.project.ProjectStopHistory;

import wt.content.ContentHolder;

public class ProjectStopHistoryData {
	
	private String historyComment; 
	private String gubun; 
	private String createDate; 

	private String fileName; 
	private String primaryURL; 
	private String fileSize; 
	private String fileIcon; 
	
	public ProjectStopHistoryData(ProjectStopHistory history) throws Exception {
		
		this.historyComment = history.getHistoryComment();
		this.gubun = "STOP".equals(history.getGubun())?"중단":"재시작";
		
		this.createDate = DateUtil.getDateString(history.getPersistInfo().getCreateStamp(),"a");
		
		this.fileName = ContentUtil.getFileName((ContentHolder)history);
		this.primaryURL = ContentUtil.getPrimaryUrl((ContentHolder)history);
		this.fileSize = ContentUtil.getFileSize((ContentHolder)history);
		this.fileIcon = ContentUtil.getFileIconStr((ContentHolder)history);
	}

	public String getHistoryComment() {
		return historyComment;
	}

	public void setHistoryComment(String historyComment) {
		this.historyComment = historyComment;
	}

	public String getGubun() {
		return gubun;
	}

	public void setGubun(String gubun) {
		this.gubun = gubun;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPrimaryURL() {
		return primaryURL;
	}

	public void setPrimaryURL(String primaryURL) {
		this.primaryURL = primaryURL;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileIcon() {
		return fileIcon;
	}

	public void setFileIcon(String fileIcon) {
		this.fileIcon = fileIcon;
	}
}
