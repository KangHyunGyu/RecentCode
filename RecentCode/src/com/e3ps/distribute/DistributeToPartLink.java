package com.e3ps.distribute;

import com.ptc.windchill.annotations.metadata.GenAsBinaryLink;
import com.ptc.windchill.annotations.metadata.GeneratedRole;
import com.ptc.windchill.annotations.metadata.Serialization;
import com.ptc.windchill.annotations.metadata.TableProperties;

import wt.fc.ObjectToObjectLink;
import wt.part.WTPart;
import wt.util.WTException;


@GenAsBinaryLink(superClass=ObjectToObjectLink.class, serializable=Serialization.EXTERNALIZABLE_BASIC,
	roleA=@GeneratedRole(name="distribute", type=DistributeDocument.class),
	roleB=@GeneratedRole(name="part", type=WTPart.class),
	tableProperties=@TableProperties(tableName="DistributeToPartLink")
)
public class DistributeToPartLink extends _DistributeToPartLink {
	
	static final long serialVersionUID = 1;
	
	public static DistributeToPartLink newDistributeToPartLink(DistributeDocument distribute, WTPart part) throws WTException{
		DistributeToPartLink instance = new DistributeToPartLink();
		instance.initialize(distribute, part);
		return instance;
	}
}
