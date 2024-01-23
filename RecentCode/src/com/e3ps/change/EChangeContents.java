package com.e3ps.change;

import com.e3ps.admin.AuthorityGroup;
import com.ptc.windchill.annotations.metadata.ForeignKeyRole;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedForeignKey;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.MyRole;
import com.ptc.windchill.annotations.metadata.PropertyConstraints;
import com.ptc.windchill.annotations.metadata.Serialization;

import wt.enterprise.Managed;
import wt.fc.InvalidAttributeException;
import wt.fc.Item;
import wt.util.WTException;

@GenAsPersistable(
		   serializable=Serialization.EXTERNALIZABLE_BASIC,
		   properties={
			   @GeneratedProperty(name="name", type=String.class),
			   @GeneratedProperty(name="contents", type=String.class),
			   @GeneratedProperty(name="sort", type=int.class)
		   },
		   foreignKeys={
			   @GeneratedForeignKey(name="echangeLink", myRoleIsRoleA=false,
			      foreignKeyRole=@ForeignKeyRole(name="echange", type=Managed.class,
			         constraints=@PropertyConstraints(required=true)),
			      myRole=@MyRole(name="link"))
		   })
public class EChangeContents extends _EChangeContents{
	static final long serialVersionUID = 1;
	
	public static EChangeContents newEChangeContents()throws WTException {
		EChangeContents instance = new EChangeContents();
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
