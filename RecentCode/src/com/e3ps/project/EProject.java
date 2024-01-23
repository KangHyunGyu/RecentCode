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

import wt.content.ContentHolder;
import wt.enterprise.Managed;
import wt.iba.value.IBAHolder;
import wt.inf.container.WTContained;
import wt.util.WTException;

import com.ptc.windchill.annotations.metadata.ForeignKeyRole;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedForeignKey;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.MyRole;
import com.ptc.windchill.annotations.metadata.PropertyConstraints;
import com.ptc.windchill.annotations.metadata.Serialization;

/**
 * ????
 * <p>
 * Use the <code>newEProject</code> static factory method(s), not the <code>EProject</code>
 * constructor, to construct instances of this class.  Instances must be
 * constructed using the static factory(s), in order to ensure proper initialization
 * of the instance.
 * <p>
 *
 *
 * @version   1.0
 **/

@GenAsPersistable(superClass=Managed.class, interfaces={EProjectNode.class, ContentHolder.class, WTContained.class}, 
   serializable=Serialization.EXTERNALIZABLE_BASIC,
   properties={
	   @GeneratedProperty(name="wbsCode", type=String.class, javaDoc="WBS CODE"),
	   
	   @GeneratedProperty(name="groupCode", type=String.class, javaDoc="그룹"),
	   @GeneratedProperty(name="levelCode", type=String.class, javaDoc="레벨"),
	   @GeneratedProperty(name="materialCode", type=String.class, javaDoc="재질"),
	   
	   @GeneratedProperty(name="checkInComment", type=String.class, constraints=@PropertyConstraints(upperLimit=4000)),
	   @GeneratedProperty(name="projectType", type=String.class)
   },
   foreignKeys={
		   @GeneratedForeignKey(name="ProjectTemplateLink", myRoleIsRoleA=false,
				   foreignKeyRole=@ForeignKeyRole(name="template", type=com.e3ps.project.EProjectTemplate.class),
				   myRole=@MyRole(name="project"))
   })
public class EProject extends _EProject {


   static final long serialVersionUID = 1;




   /**
    * Default factory for the class.
    *
    * @return    EProject
    * @exception wt.util.WTException
    **/
   public static EProject newEProject()
            throws WTException {

      EProject instance = new EProject();
      instance.initialize();
      return instance;
   }

}
