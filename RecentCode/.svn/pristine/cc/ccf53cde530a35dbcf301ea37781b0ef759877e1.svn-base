package com.e3ps.calendar;

import java.sql.Timestamp;

import com.e3ps.common.util.OwnPersistable;
import com.ptc.windchill.annotations.metadata.ForeignKeyRole;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedForeignKey;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.MyRole;
import com.ptc.windchill.annotations.metadata.PropertyConstraints;
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
			@GeneratedProperty(name="seq", type=int.class),					// 시퀀스
		},
	   foreignKeys={
	   @GeneratedForeignKey(name="DevelopmentStageCalendarLink", myRoleIsRoleA=false,
			      foreignKeyRole=@ForeignKeyRole(name="dsc", type=DevelopmentStageCalendar.class,
			         constraints=@PropertyConstraints(required=true)),
			      myRole=@MyRole(name="ds"))
	   })
public class DevelopmentStage extends _DevelopmentStage{
	static final long serialVersionUID = 1;
	
	public static DevelopmentStage newDevelopmentStage() throws WTException {
		DevelopmentStage instance = new DevelopmentStage();
		instance.initialize();
		return instance;
	}

}
