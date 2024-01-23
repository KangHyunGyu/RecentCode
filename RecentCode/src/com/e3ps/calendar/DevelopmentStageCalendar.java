package com.e3ps.calendar;

import java.sql.Timestamp;

import com.e3ps.common.util.OwnPersistable;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.Serialization;

import wt.content.ContentHolder;
import wt.fc.Item;
import wt.inf.container.WTContained;
import wt.util.WTException;

@GenAsPersistable(
		superClass=Item.class,
		interfaces={OwnPersistable.class, WTContained.class, ContentHolder.class}, 
		serializable=Serialization.EXTERNALIZABLE_BASIC,
		properties={
			@GeneratedProperty(name="name", type=String.class), 			// 이름
			@GeneratedProperty(name="remark", type=String.class),   		// 비고
			@GeneratedProperty(name="startDate", type=Timestamp.class),		// 시작일
			@GeneratedProperty(name="endDate", type=Timestamp.class),		// 종료일
		})
public class DevelopmentStageCalendar extends _DevelopmentStageCalendar{
	static final long serialVersionUID = 1;

	public static DevelopmentStageCalendar newDevelopmentStageCalendar() throws WTException {
		DevelopmentStageCalendar instance = new DevelopmentStageCalendar();
		instance.initialize();
		return instance;
	}
}
