package com.e3ps.change;

import wt.fc.ObjectToObjectLink;
import wt.util.WTException;
import wt.vc.Versioned;

import com.ptc.windchill.annotations.metadata.GenAsBinaryLink;
import com.ptc.windchill.annotations.metadata.GeneratedRole;

@GenAsBinaryLink(superClass=ObjectToObjectLink.class,
   roleA=@GeneratedRole(name="ecr", type=EChangeOrder2.class),
   roleB=@GeneratedRole(name="target", type=Versioned.class)
)
public class EcoTargetLink extends _EcoTargetLink {

   static final long serialVersionUID = 1;

   public static EcoTargetLink newEcoTargetLink(EChangeOrder2 ecr, Versioned target  )
            throws WTException {

	   EcoTargetLink instance = new EcoTargetLink();
      instance.initialize( ecr, target  );
      return instance;
   }

}
