package com.e3ps.change;

import wt.fc.ObjectToObjectLink;
import wt.util.WTException;

import com.e3ps.project.EProject;
import com.ptc.windchill.annotations.metadata.GenAsBinaryLink;
import com.ptc.windchill.annotations.metadata.GeneratedRole;


@GenAsBinaryLink(superClass=ObjectToObjectLink.class,
   roleA=@GeneratedRole(name="ecr", type=EChangeRequest2.class),
   roleB=@GeneratedRole(name="project", type=EProject.class)
)
public class EcrProjectLink extends _EcrProjectLink {

   static final long serialVersionUID = 1;

   public static EcrProjectLink newEcrProjectLink( EChangeRequest2 ecr, EProject project )
            throws WTException {

	  EcrProjectLink instance = new EcrProjectLink();
      instance.initialize( ecr, project );
      return instance;
   }

}
