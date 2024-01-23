package com.e3ps.admin;

import com.ptc.windchill.annotations.metadata.Changeable;
import com.ptc.windchill.annotations.metadata.ForeignKeyRole;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedForeignKey;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.MyRole;
import com.ptc.windchill.annotations.metadata.PropertyConstraints;
import com.ptc.windchill.annotations.metadata.Serialization;

import wt.admin.AdministrativeDomain;
import wt.fc.InvalidAttributeException;
import wt.org.WTGroup;
import wt.util.WTException;

@GenAsPersistable(
		   serializable=Serialization.EXTERNALIZABLE_BASIC,
		   properties={
		   @GeneratedProperty(name="name", type=String.class),
		   @GeneratedProperty(name="auth", type=String.class),
		   @GeneratedProperty(name="authObjectType", type=String.class),
		   @GeneratedProperty(name="domainPath", type=String.class),
		   @GeneratedProperty(name="description", type=String.class),
		   @GeneratedProperty(name="codeType", type=AuthorityGroupType.class,
		      constraints=@PropertyConstraints(changeable=Changeable.VIA_OTHER_MEANS, required=true))
		   },
		   foreignKeys={
		   @GeneratedForeignKey(name="groupLink", myRoleIsRoleA=false,
		      foreignKeyRole=@ForeignKeyRole(name="group", type=WTGroup.class,
		         constraints=@PropertyConstraints(required=true)),
		      myRole=@MyRole(name="link")),
		   @GeneratedForeignKey(name="domainLink", myRoleIsRoleA=false,
		      foreignKeyRole=@ForeignKeyRole(name="domain", type=AdministrativeDomain.class,
		         constraints=@PropertyConstraints(required=true)),
		      myRole=@MyRole(name="domainAuth"))
		   })
public class AuthorityGroup extends _AuthorityGroup{
	static final long serialVersionUID = 1;
	
	public static AuthorityGroup newAuthorityGroup()throws WTException {
	  AuthorityGroup instance = new AuthorityGroup();
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
