/* bcwti
 *
 * Copyright (c) 2008 Parametric Technology Corporation (PTC). All Rights
 * Reserved.
 *
 * This software is the confidential and proprietary information of PTC
 * and is subject to the terms of a software license agreement. You shall
 * not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement.
 *
 * ecwti
 */

package com.e3ps.project;

import wt.enterprise.Managed;
import wt.inf.container.WTContained;
import wt.util.WTException;

import com.ptc.windchill.annotations.metadata.ForeignKeyRole;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedForeignKey;
import com.ptc.windchill.annotations.metadata.MyRole;
import com.ptc.windchill.annotations.metadata.PropertyConstraints;
import com.ptc.windchill.annotations.metadata.Serialization;

/**
 * ???? ?? / ??
 * <p>
 * Use the <code>newProjectRegistApproval</code> static factory method(s),
 * not the <code>ProjectRegistApproval</code> constructor, to construct
 * instances of this class.  Instances must be constructed using the static
 * factory(s), in order to ensure proper initialization of the instance.
 * <p>
 *
 *
 * @version   1.0
 **/

@GenAsPersistable(superClass=Managed.class, interfaces={WTContained.class}, 
   serializable=Serialization.EXTERNALIZABLE_BASIC,
   foreignKeys={
   @GeneratedForeignKey(name="ProjectRegistLink", myRoleIsRoleA=false,
      foreignKeyRole=@ForeignKeyRole(name="project", type=com.e3ps.project.EProject.class,
         constraints=@PropertyConstraints(required=true)),
      myRole=@MyRole(name="regist"))
   })
public class ProjectRegistApproval extends _ProjectRegistApproval {


   static final long serialVersionUID = 1;




   /**
    * Default factory for the class.
    *
    * @return    ProjectRegistApproval
    * @exception wt.util.WTException
    **/
   public static ProjectRegistApproval newProjectRegistApproval()
            throws WTException {

      ProjectRegistApproval instance = new ProjectRegistApproval();
      instance.initialize();
      return instance;
   }

}
