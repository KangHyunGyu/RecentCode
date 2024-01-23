package com.e3ps.workspace.bean;

import java.util.ArrayList;
import java.util.List;

import com.e3ps.common.bean.PersistableData;
import com.e3ps.common.content.util.ContentUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.org.Department;
import com.e3ps.org.service.DepartmentHelper;
import com.e3ps.org.service.PeopleHelper;
import com.e3ps.workspace.notice.Notice;

import wt.content.ApplicationData;
import wt.content.ContentHolder;
import wt.org.WTUser;

public class NoticeData extends PersistableData{

	private String title;
	private String contents;
	private int cnt;
	private String creatorFullName;
	private String oid; // oid
	private String fileSize;
	private String fileIcon;
	private String deadline;	//게시 기한일자
	private String department;
	private String createDateFormat;
	private String notifyType;
	private String viewTitle;
	private int isAttache;

	public NoticeData(String oid) throws Exception {
		super(oid);
		
		Notice notice = (Notice) CommonUtil.getObject(oid);
		
		_NoticeData(notice);
	}
	
	public NoticeData(Notice notice) throws Exception {
		super(notice);
		
		_NoticeData(notice);
	}

	public void _NoticeData(Notice notice) {
		this.title = StringUtil.checkNull(notice.getTitle());
		this.oid = CommonUtil.getOIDString(notice);
		this.createDateFormat = DateUtil.getDateString(notice.getCreateTimestamp(),"d");

		this.contents = StringUtil.checkNull((String)notice.getContents());
		this.cnt =  notice.getCnt();
		this.creatorFullName = notice.getOwner().getFullName();
		this.deadline = notice.getDeadline();
		this.notifyType = notice.getNotifyType();
		
		String type = notice.getNotifyType();
		this.viewTitle = "[" + type + "] " + notice.getTitle();
		
		Department dp = null;
		WTUser user = PeopleHelper.manager.getWTUser(notice.getOwner().getName());
		try {
			List<ApplicationData> attacheList = new ArrayList<ApplicationData>();
			dp = (Department)DepartmentHelper.manager.getDepartment(user);
			attacheList = ContentUtil.getSecondaryFile((ContentHolder)notice);
			if (attacheList.size() != 0){
				this.isAttache = 1;
			}else{
				this.isAttache = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(dp != null){
			this.department = dp.getName();
		}else{
			this.department = "없음";
		}
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	public String getCreatorFullName() {
		return creatorFullName;
	}

	public void setCreatorFullName(String creatorFullName) {
		this.creatorFullName = creatorFullName;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
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

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getCreateDateFormat() {
		return createDateFormat;
	}

	public void setCreateDateFormat(String createDateFormat) {
		this.createDateFormat = createDateFormat;
	}

	public String getNotifyType() {
		return notifyType;
	}

	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}

	public String getViewTitle() {
		return viewTitle;
	}

	public void setViewTitle(String viewTitle) {
		this.viewTitle = viewTitle;
	}

	public int getIsAttache() {
		return isAttache;
	}

	public void setIsAttache(int isAttache) {
		this.isAttache = isAttache;
	}

}
