package com.e3ps.calendar;

import com.e3ps.project.EProject;
import com.ptc.windchill.annotations.metadata.GenAsBinaryLink;
import com.ptc.windchill.annotations.metadata.GeneratedRole;

import wt.fc.ObjectToObjectLink;
import wt.util.WTException;

@GenAsBinaryLink(superClass=ObjectToObjectLink.class,
roleA=@GeneratedRole(name="dsc", type=DevelopmentStageCalendar.class),
roleB=@GeneratedRole(name="project", type=EProject.class)
)
public class CalendarProjectLink extends _CalendarProjectLink{
	static final long serialVersionUID = 1;

	public static CalendarProjectLink newCalendarProjectLink(DevelopmentStageCalendar dsc, EProject project) throws WTException {
		CalendarProjectLink instance = new CalendarProjectLink();
		instance.initialize(dsc, project);
		return instance;
	}
}
