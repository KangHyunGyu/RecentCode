package com.e3ps.admin;

import com.ptc.windchill.annotations.metadata.ForeignKeyRole;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.MyRole;
import com.ptc.windchill.annotations.metadata.PropertyConstraints;
import com.ptc.windchill.annotations.metadata.Serialization;

import wt.fc.InvalidAttributeException;
import wt.org.WTGroup;
import wt.util.WTException;

import com.ptc.windchill.annotations.metadata.GeneratedForeignKey;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;

@GenAsPersistable(
		   serializable=Serialization.EXTERNALIZABLE_BASIC,
		   properties={
			   @GeneratedProperty(name="code", type=String.class),
			   @GeneratedProperty(name="name", type=String.class),
			   @GeneratedProperty(name="name_en", type=String.class),
			   @GeneratedProperty(name="href", type=String.class),
			   @GeneratedProperty(name="imgsrc", type=String.class),
			   @GeneratedProperty(name="disabled", type=boolean.class, initialValue="false"),
			   @GeneratedProperty(name="sort", type=int.class),
			   @GeneratedProperty(name="menuLevel", type=int.class),
			   @GeneratedProperty(name="alias", type=String.class)
		   },
		   foreignKeys={
				   @GeneratedForeignKey(name="myParentLink", myRoleIsRoleA=false,
				      foreignKeyRole=@ForeignKeyRole(name="parent", type=com.e3ps.admin.EsolutionMenu.class,
				         constraints=@PropertyConstraints(required=false)),
				      myRole=@MyRole(name="child")),
				   @GeneratedForeignKey(name="authGroupLink", myRoleIsRoleA=false,
				      foreignKeyRole=@ForeignKeyRole(name="group", type=WTGroup.class,
				         constraints=@PropertyConstraints(required=false)),
				      myRole=@MyRole(name="menu"))
		   })
public class EsolutionMenu extends _EsolutionMenu {

	static final long serialVersionUID = 1;
	
	public static EsolutionMenu newEsolutionMenu()throws WTException {
		EsolutionMenu instance = new EsolutionMenu();
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
