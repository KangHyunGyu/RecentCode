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
			   @GeneratedProperty(name="code", type=String.class),
			   @GeneratedProperty(name="description", type=String.class),
			   @GeneratedProperty(name="name", type=String.class),
			   @GeneratedProperty(name="disabled", type=boolean.class),
			   @GeneratedProperty(name="engName", type=String.class),
			   @GeneratedProperty(name="path", type=String.class),
			   @GeneratedProperty(name="sort", type=int.class),
		   },
		   foreignKeys={
		   @GeneratedForeignKey(name="FolderToCodeLink", myRoleIsRoleA=false,
		      foreignKeyRole=@ForeignKeyRole(name="folder", type=wt.folder.Folder.class,
		         constraints=@PropertyConstraints(required=true)),
		      myRole=@MyRole(name="docCode"))
		   }
)		  
public class DocCodeType extends _DocCodeType{
	
	 static final long serialVersionUID = 1;
	
	 public static DocCodeType newDocCodeType()
	            throws WTException {

		 DocCodeType instance = new DocCodeType();
	     instance.initialize();
	     return instance;
	 }

	 protected void initialize()
	           throws WTException {

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
