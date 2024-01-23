package com.e3ps.change;

import wt.fc.ObjectToObjectLink;
import wt.util.WTException;

import com.e3ps.project.EProject;
import com.ptc.windchill.annotations.metadata.GenAsBinaryLink;
import com.ptc.windchill.annotations.metadata.GeneratedRole;


@GenAsBinaryLink(superClass=ObjectToObjectLink.class,
   roleA=@GeneratedRole(name="eco", type=EChangeOrder2.class),
   roleB=@GeneratedRole(name="project", type=EProject.class)
)
public class EcoProjectLink extends _EcoProjectLink {

   static final long serialVersionUID = 1;

   public static EcoProjectLink newEcoProjectLink( EChangeOrder2 eco, EProject project )
            throws WTException {

	   EcoProjectLink instance = new EcoProjectLink();
      instance.initialize( eco, project );
      return instance;
   }

}
