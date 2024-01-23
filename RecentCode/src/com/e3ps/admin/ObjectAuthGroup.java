package com.e3ps.admin;

import com.ptc.windchill.annotations.metadata.ForeignKeyRole;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedForeignKey;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.MyRole;
import com.ptc.windchill.annotations.metadata.PropertyConstraints;
import com.ptc.windchill.annotations.metadata.Serialization;

import wt.fc.InvalidAttributeException;
import wt.org.WTGroup;
import wt.util.WTException;

@GenAsPersistable(serializable=Serialization.EXTERNALIZABLE_BASIC,
properties={
		   @GeneratedProperty(name="authObjectType", type=AuthorityObjectType.class),
		   @GeneratedProperty(name="authGroupType", type=AuthorityGroupType.class)
},
foreignKeys={
		   @GeneratedForeignKey(name="authObjectGroup", myRoleIsRoleA=false,
		      foreignKeyRole=@ForeignKeyRole(name="objectGroup", type=WTGroup.class,
		         constraints=@PropertyConstraints(required=false)),
		      myRole=@MyRole(name="objectType"))
})
public class ObjectAuthGroup extends _ObjectAuthGroup{
	static final long serialVersionUID = 1;
	
	public static ObjectAuthGroup newObjectAuthGroup()throws WTException {
		ObjectAuthGroup instance = new ObjectAuthGroup();
	    instance.initialize();
	    return instance;
   }

	private void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkAttributes() throws InvalidAttributeException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getIdentity() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
