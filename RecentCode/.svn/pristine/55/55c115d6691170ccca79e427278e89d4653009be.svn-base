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
			@GeneratedProperty(name="objCode", type=String.class),
			@GeneratedProperty(name="objType", type=String.class), // 탭별 객체 생성
		},
	   foreignKeys={
	   @GeneratedForeignKey(name="SGObjectMasterLink",
	      foreignKeyRole=@ForeignKeyRole(name="objMaster", type=SGObjectMaster.class, cascade=false,
	         constraints=@PropertyConstraints(required=false)),
	      myRole=@MyRole(name="obj", cascade=false)),
	   })
public class SGObject extends _SGObject{
	static final long serialVersionUID = 1;
	
	public static SGObject newSGObject() throws WTException {
		SGObject instance = new SGObject();
		instance.initialize();
		return instance;
	}

}
