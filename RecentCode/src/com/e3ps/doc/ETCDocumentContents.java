package com.e3ps.doc;

import com.ptc.windchill.annotations.metadata.ForeignKeyRole;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedForeignKey;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.MyRole;
import com.ptc.windchill.annotations.metadata.PropertyConstraints;
import com.ptc.windchill.annotations.metadata.Serialization;

import wt.fc.InvalidAttributeException;
import wt.util.WTException;

@GenAsPersistable(
		   serializable=Serialization.EXTERNALIZABLE_BASIC,
		   properties={
			   @GeneratedProperty(name="name", type=String.class),
			   @GeneratedProperty(name="contents", type=String.class),
			   @GeneratedProperty(name="sort", type=int.class),
		   },
		   foreignKeys={
			   @GeneratedForeignKey(name="docEtcLink", myRoleIsRoleA=false,
			      foreignKeyRole=@ForeignKeyRole(name="docEtc", type=E3PSDocument.class,
			         constraints=@PropertyConstraints(required=true)),
			      myRole=@MyRole(name="link"))
		   })
public class ETCDocumentContents extends _ETCDocumentContents{
	
static final long serialVersionUID = 1;
	
	public static ETCDocumentContents newETCDocumentContents()throws WTException {
		ETCDocumentContents instance = new ETCDocumentContents();
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
