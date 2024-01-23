package com.e3ps.stagegate;

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
			@GeneratedProperty(name="division", type=String.class,			// 구분(값)
					constraints=@PropertyConstraints(upperLimit=4000)),   	
			@GeneratedProperty(name="parentCode", type=String.class), 		// 부모코드
			@GeneratedProperty(name="seq", type=int.class),					// 시퀀스
			@GeneratedProperty(name="isSubTitle", type=boolean.class),		// 부제목
		},
	   foreignKeys={
	   @GeneratedForeignKey(name="SGObjectValueLink", myRoleIsRoleA=false,
			      foreignKeyRole=@ForeignKeyRole(name="obj", type=SGObject.class,
			         constraints=@PropertyConstraints(required=true)),
			      myRole=@MyRole(name="objValue"))
	   })
public class SGObjectValue extends _SGObjectValue{
	static final long serialVersionUID = 1;
	
	public static SGObjectValue newSGObjectValue() throws WTException {
		SGObjectValue instance = new SGObjectValue();
		instance.initialize();
		return instance;
	}

}
