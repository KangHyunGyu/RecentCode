package com.e3ps.change;

import wt.fc.ObjectToObjectLink;
import wt.util.WTException;
import wt.vc.Versioned;

import com.ptc.windchill.annotations.metadata.GenAsBinaryLink;
import com.ptc.windchill.annotations.metadata.GeneratedRole;

@GenAsBinaryLink(superClass=ObjectToObjectLink.class,
   roleA=@GeneratedRole(name="ecr", type=EChangeRequest2.class),
   roleB=@GeneratedRole(name="target", type=Versioned.class)
)
public class EcrTargetLink extends _EcrTargetLink {

   static final long serialVersionUID = 1;

   public static EcrTargetLink newEcrTargetLink(EChangeRequest2 ecr, Versioned target  )
            throws WTException {

	  EcrTargetLink instance = new EcrTargetLink();
      instance.initialize( ecr, target  );
      return instance;
   }

}
