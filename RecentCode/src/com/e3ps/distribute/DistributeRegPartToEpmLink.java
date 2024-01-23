package com.e3ps.distribute;

import com.ptc.windchill.annotations.metadata.ForeignKeyRole;
import com.ptc.windchill.annotations.metadata.GenAsBinaryLink;
import com.ptc.windchill.annotations.metadata.GeneratedForeignKey;
import com.ptc.windchill.annotations.metadata.GeneratedRole;
import com.ptc.windchill.annotations.metadata.MyRole;
import com.ptc.windchill.annotations.metadata.PropertyConstraints;
import com.ptc.windchill.annotations.metadata.Serialization;
import com.ptc.windchill.annotations.metadata.TableProperties;

import wt.fc.ObjectToObjectLink;
import wt.fc.Persistable;

@GenAsBinaryLink(superClass= ObjectToObjectLink.class, serializable=Serialization.EXTERNALIZABLE_BASIC,
foreignKeys={
		@GeneratedForeignKey(
			foreignKeyRole=@ForeignKeyRole(name="DistributeRegistration", type=DistributeRegistration.class, cascade=false, constraints=@PropertyConstraints(required=true)),
		    myRole=@MyRole(name="distributeReg", cascade=false)
		),
},
roleA=@GeneratedRole(name="DistributeRegPart", type=DistributeRegToPartLink.class, cascade = false),
roleB=@GeneratedRole(name="DistributeRegPartToEpm", type=Persistable.class, cascade = false),
tableProperties=@TableProperties(tableName="DistributeRegPartToEpmLink")
)

public class DistributeRegPartToEpmLink extends _DistributeRegPartToEpmLink{

	static final long serialVersionUID = 1;

	public static DistributeRegPartToEpmLink newDistributeRegPartToEpmLink(DistributeRegToPartLink DistributeRegPart, Persistable DistributeRegPartToEpm) throws wt.util.WTException{
		DistributeRegPartToEpmLink instance = new DistributeRegPartToEpmLink();
		instance.initialize(DistributeRegPart, DistributeRegPartToEpm);
		return instance;
	}
}
