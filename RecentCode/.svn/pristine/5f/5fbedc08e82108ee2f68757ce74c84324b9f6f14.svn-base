package com.e3ps.admin;

import com.ptc.windchill.annotations.metadata.Cardinality;
import com.ptc.windchill.annotations.metadata.ForeignKeyRole;
import com.ptc.windchill.annotations.metadata.GenAsBinaryLink;
import com.ptc.windchill.annotations.metadata.GeneratedForeignKey;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.GeneratedRole;
import com.ptc.windchill.annotations.metadata.MyRole;
import com.ptc.windchill.annotations.metadata.PropertyConstraints;
import com.ptc.windchill.annotations.metadata.Serialization;

import wt.admin.AdministrativeDomain;
import wt.fc.ObjectToObjectLink;
import wt.folder.Folder;
import wt.org.WTGroup;
import wt.util.WTException;

@GenAsBinaryLink(superClass=ObjectToObjectLink.class, serializable=Serialization.EXTERNALIZABLE_BASIC,
	properties={
			   @GeneratedProperty(name="authFolderGroupType", type=AuthorityGroupType.class)
	},
	roleA=@GeneratedRole(name="authFolder", type=Folder.class, cardinality=Cardinality.ONE_TO_MANY),
	roleB=@GeneratedRole(name="authFoldergroup", type=WTGroup.class),
	foreignKeys={
		@GeneratedForeignKey(name="authFolderDomainLink", myRoleIsRoleA=false, foreignKeyRole=@ForeignKeyRole(name="domain", type=AdministrativeDomain.class, constraints=@PropertyConstraints(required=false)), myRole=@MyRole(name="domainAuth"))
	}
)
public class FolderAuthGroup extends _FolderAuthGroup {
	static final long serialVersionUID = 1;
	
	public static FolderAuthGroup newFolderAuthGroup( Folder folder, WTGroup group )
            throws WTException {

		FolderAuthGroup instance = new FolderAuthGroup();
	    instance.initialize( folder, group );
	    return instance;
   }
}
