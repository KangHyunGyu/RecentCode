package com.e3ps.approval;

import java.sql.Timestamp;

import com.e3ps.common.impl.OwnPersistable;
import com.ptc.windchill.annotations.metadata.Cardinality;
import com.ptc.windchill.annotations.metadata.Changeable;
import com.ptc.windchill.annotations.metadata.ForeignKeyRole;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedForeignKey;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.MyRole;
import com.ptc.windchill.annotations.metadata.PropertyConstraints;
import com.ptc.windchill.annotations.metadata.Serialization;

import wt.fc.InvalidAttributeException;
import wt.fc.Persistable;
import wt.util.WTException;

@GenAsPersistable(interfaces = OwnPersistable.class,serializable=Serialization.EXTERNALIZABLE_BASIC,
	properties={
		@GeneratedProperty(name="activityName", type=String.class),
		@GeneratedProperty(name="wfcomment", type=Object.class,
		   		constraints=@PropertyConstraints(upperLimit=4000)),
		@GeneratedProperty(name="departmentName", type=String.class),
		@GeneratedProperty(name="objectVersion", type=String.class),
	},
	foreignKeys={
	   @GeneratedForeignKey(name="WFHistoryLink",myRoleIsRoleA=false,
	      foreignKeyRole=@ForeignKeyRole(name="wfObject", type=Persistable.class, cascade=false,
	         constraints=@PropertyConstraints(required=true)),
	      myRole=@MyRole(name="wfHistory", cascade=false)),
	   })
public class WFHistory extends _WFHistory{
	
	static final long serialVersionUID = 1;




	   /**
	    * Default factory for the class.
	    *
	    * @return    ApprovalLine
	    * @exception wt.util.WTException
	    **/
	   public static WFHistory newWFHistory()
	            throws WTException {

		   WFHistory instance = new WFHistory();
	      instance.initialize();
	      return instance;
	   }

	   /**
	    * Supports initialization, following construction of an instance.  Invoked
	    * by "new" factory having the same signature.
	    *
	    * @exception wt.util.WTException
	    **/
	   protected void initialize()
	            throws WTException {

	   }

	   /**
	    * Gets the value of the attribute: IDENTITY.
	    * Supplies the identity of the object for business purposes.  The identity
	    * is composed of name, number or possibly other attributes.  The identity
	    * does not include the type of the object.
	    *
	    *
	    * <BR><BR><B>Supported API: </B>false
	    *
	    * @deprecated Replaced by IdentityFactory.getDispayIdentifier(object)
	    * to return a localizable equivalent of getIdentity().  To return a
	    * localizable value which includes the object type, use IdentityFactory.getDisplayIdentity(object).
	    * Other alternatives are ((WTObject)obj).getDisplayIdentifier() and
	    * ((WTObject)obj).getDisplayIdentity().
	    *
	    * @return    String
	    **/
	   public String getIdentity() {

	      return null;
	   }

	   /**
	    * Gets the value of the attribute: TYPE.
	    * Identifies the type of the object for business purposes.  This is
	    * typically the class name of the object but may be derived from some
	    * other attribute of the object.
	    *
	    *
	    * <BR><BR><B>Supported API: </B>false
	    *
	    * @deprecated Replaced by IdentityFactory.getDispayType(object) to return
	    * a localizable equivalent of getType().  Another alternative is ((WTObject)obj).getDisplayType().
	    *
	    * @return    String
	    **/
	   public String getType() {

	      return null;
	   }

	   @Override
	   public void checkAttributes()
	            throws InvalidAttributeException {

	   }

}
