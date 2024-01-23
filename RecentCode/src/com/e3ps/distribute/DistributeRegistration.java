package com.e3ps.distribute;

import com.ptc.windchill.annotations.metadata.ForeignKeyRole;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedForeignKey;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.MyRole;
import com.ptc.windchill.annotations.metadata.PropertyConstraints;
import com.ptc.windchill.annotations.metadata.Serialization;

import wt.content.ContentHolder;
import wt.enterprise.Managed;
import wt.inf.container.WTContained;
import wt.util.WTException;


@GenAsPersistable(superClass= Managed.class, serializable=Serialization.EXTERNALIZABLE_BASIC, interfaces = {WTContained.class, ContentHolder.class},
	properties={
			@GeneratedProperty(name="descriptionDRF", type=String.class, constraints=@PropertyConstraints(upperLimit=4000), javaDoc="추가 입력 사항"),
			@GeneratedProperty(name="distributeTarget", type=String.class, constraints=@PropertyConstraints(upperLimit=200),javaDoc="배포 대상"),
			@GeneratedProperty(name="distributeCompany", type=String.class, constraints=@PropertyConstraints(upperLimit=400),javaDoc="배포 업체")
	},
	 foreignKeys={
			   @GeneratedForeignKey(name="distributeLink", myRoleIsRoleA=false,
			      foreignKeyRole=@ForeignKeyRole(name="distribute", type=DistributeDocument.class,
			         constraints=@PropertyConstraints(required=true)),
			      myRole=@MyRole(name="link"))
			   }
)
public class DistributeRegistration extends _DistributeRegistration{

	static final long serialVersionUID = 1;

	public static DistributeRegistration newDistributeRegistration() throws WTException{
		DistributeRegistration instance = new DistributeRegistration();
		instance.initialize();
		return instance;
	}

}
