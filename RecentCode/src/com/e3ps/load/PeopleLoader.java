package com.e3ps.load;

import java.io.File;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.org.Department;
import com.e3ps.org.People;
import com.e3ps.org.service.PeopleHelper;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.method.RemoteMethodServer;
import wt.org.WTUser;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.util.WTException;

public class PeopleLoader {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.LOAD.getName());
	
	public static void main(String[] args) throws Exception {

		String sWtHome = wt.util.WTProperties.getLocalProperties().getProperty("wt.home", "");
		String sFilePath = sWtHome + "\\loadFiles\\e3ps\\People.xls";

		if (args != null && args.length > 0 && args[0].trim().length() > 0) {
			sFilePath = args[0];
		}
		
		setUser(args[1], args[2]);
		
		new PeopleLoader().loadPeople(sFilePath);
	}
	
	public static void setUser(final String id, final String pw) {
		RemoteMethodServer.getDefault().setUserName(id);
		RemoteMethodServer.getDefault().setPassword(pw);
	}

	public void loadPeople(String sFilePath) throws Exception {

		File newfile = new File(sFilePath);

		Workbook wb = Workbook.getWorkbook(newfile);

		Sheet[] sheets = wb.getSheets();
		for (int i = 0; i < sheets.length; i++) {
			int rows = sheets[i].getRows();
			for (int j = 1; j < rows; j++) {
				if (checkLine(sheets[i].getRow(j), 0)) {
					Cell[] cell = sheets[i].getRow(j);

					String name = getContent(cell, 0).trim();
					String fullName = getContent(cell, 1).trim();
					String dutyCode = getContent(cell, 2).trim();
					String dutyName = getContent(cell, 3).trim();
					String departmentCode = getContent(cell, 4).trim();

					WTUser user = getWTUser(name);

					if (user == null) {
						System.out.println(j + " . WTUser :" + name + " is not Exist");
						break;
					}

					People people = PeopleHelper.manager.getPeople(user);

					if (people == null) {
						people = People.newPeople();
						people.setName(fullName);
						people.setUser(user);
					}

					people.setDutyCode(dutyCode);
					people.setDuty(dutyName);

					if (departmentCode != null) {
						people.setDepartment(getDept(departmentCode.trim()));
					}

					PersistenceHelper.manager.save(people);
				}
			}
		} // for

	}

	public WTUser getWTUser(String name) {
		try {
			QuerySpec spec = new QuerySpec();
			int userPos = spec.addClassList(WTUser.class, true);
			spec.appendWhere(new SearchCondition(WTUser.class, WTUser.NAME, "=", name), new int[] { userPos });
			QueryResult qr = PersistenceHelper.manager.find(spec);
			if (qr.hasMoreElements()) {
				Object[] obj = (Object[]) qr.nextElement();
				return (WTUser) obj[0];
			}
		} catch (QueryException e) {
			e.printStackTrace();
		} catch (WTException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Department getDept(String code) throws Exception {
		QuerySpec qs = new QuerySpec(Department.class);
		qs.appendWhere(new SearchCondition(Department.class, Department.CODE, "=", code), new int[] { 0 });
		QueryResult qr = PersistenceHelper.manager.find(qs);
		if (qr.size() == 1) {
			return (Department) qr.nextElement();
		}
		return null;
	}

	public Department getDeptByName(String name) throws Exception {
		QuerySpec qs = new QuerySpec(Department.class);
		qs.appendWhere(new SearchCondition(Department.class, "name", "=", name), new int[] { 0 });
		QueryResult qr = PersistenceHelper.manager.find(qs);
		if (qr.size() == 1) {
			return (Department) qr.nextElement();
		}
		return null;
	}

	public boolean checkLine(Cell[] cell, int line) {
		String value = null;
		try {
			value = cell[line].getContents().trim();
		} catch (ArrayIndexOutOfBoundsException e) {
			e.getMessage();
			return false;
		}
		if (value == null || value.length() == 0)
			return false;
		return true;
	}

	public static String getContent(Cell[] cell, int idx) {
		try {
			String val = cell[idx].getContents();
			if (val == null)
				return "";
			return val.trim();
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		return "";
	}
}
