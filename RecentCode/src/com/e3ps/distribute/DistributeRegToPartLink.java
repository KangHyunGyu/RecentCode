package com.e3ps.distribute;

import com.ptc.windchill.annotations.metadata.GenAsBinaryLink;
import com.ptc.windchill.annotations.metadata.GeneratedRole;
import com.ptc.windchill.annotations.metadata.Serialization;
import com.ptc.windchill.annotations.metadata.TableProperties;

import wt.fc.ObjectToObjectLink;
import wt.part.WTPart;
import wt.util.WTException;


@GenAsBinaryLink(superClass=ObjectToObjectLink.class, serializable=Serialization.EXTERNALIZABLE_BASIC,
roleA=@GeneratedRole(name="distributeReg", type=DistributeRegistration.class),
roleB=@GeneratedRole(name="part", type=WTPart.class),
tableProperties=@TableProperties(tableName="DistributeRegToPartLink")
)

public class DistributeRegToPartLink extends _DistributeRegToPartLink {

static final long serialVersionUID = 1;
	
	public static DistributeRegToPartLink newDistributeRegToPartLink(DistributeRegistration distributeReg, WTPart part) throws WTException{
		DistributeRegToPartLink instance = new DistributeRegToPartLink();
		instance.initialize(distributeReg, part);
		return instance;
	}
}
