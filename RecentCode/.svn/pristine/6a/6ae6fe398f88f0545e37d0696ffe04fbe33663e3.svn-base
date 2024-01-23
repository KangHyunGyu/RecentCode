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
				foreignKeyRole=@ForeignKeyRole(name="DistributeDocument", type=DistributeDocument.class, cascade=false, constraints=@PropertyConstraints(required=true)),
			    myRole=@MyRole(name="distribute", cascade=false)
			),
	},
	roleA=@GeneratedRole(name="DistributePart", type=DistributeToPartLink.class, cascade = false),
	roleB=@GeneratedRole(name="DistributePartToEpm", type=Persistable.class, cascade = false),
	tableProperties=@TableProperties(tableName="DistributePartToEpmLink")
)
public class DistributePartToEpmLink extends _DistributePartToEpmLink {
	
	static final long serialVersionUID = 1;

	public static DistributePartToEpmLink newDistributePartToEpmLink(DistributeToPartLink DistributePart, Persistable DistributePartToEpm) throws wt.util.WTException{
		DistributePartToEpmLink instance = new DistributePartToEpmLink();
		instance.initialize(DistributePart, DistributePartToEpm);
		return instance;
	}
}
