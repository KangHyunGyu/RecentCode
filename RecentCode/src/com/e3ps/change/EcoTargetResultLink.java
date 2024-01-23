package com.e3ps.change;

import wt.fc.ObjectToObjectLink;
import wt.util.WTException;
import wt.vc.Versioned;

import com.ptc.windchill.annotations.metadata.GenAsBinaryLink;
import com.ptc.windchill.annotations.metadata.GeneratedRole;

@GenAsBinaryLink(superClass=ObjectToObjectLink.class,
   roleA=@GeneratedRole(name="eco", type=EChangeOrder2.class),
   roleB=@GeneratedRole(name="target", type=Versioned.class)
)
public class EcoTargetResultLink extends _EcoTargetResultLink {

   static final long serialVersionUID = 1;

   public static EcoTargetResultLink newEcoTargetResultLink(EChangeOrder2 eco, Versioned target  )
            throws WTException {

	  EcoTargetResultLink instance = new EcoTargetResultLink();
      instance.initialize( eco, target  );
      return instance;
   }

}
