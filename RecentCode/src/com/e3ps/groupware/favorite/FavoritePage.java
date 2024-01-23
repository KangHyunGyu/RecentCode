package com.e3ps.groupware.favorite;

import wt.fc.InvalidAttributeException;
import wt.util.WTException;

import com.e3ps.common.util.OwnPersistable;
import com.e3ps.groupware.board.Board;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.Serialization;


@GenAsPersistable(interfaces={OwnPersistable.class}, 
   serializable=Serialization.EXTERNALIZABLE_BASIC,
   properties={
   @GeneratedProperty(name="name", type=String.class),
   @GeneratedProperty(name="url", type=String.class)
   })
public class FavoritePage extends  _FavoritePage{

   static final long serialVersionUID = 1;
   


   /**
    * Default factory for the class.
    *
    * @return    FavoritePage
    * @exception wt.util.WTException
    **/
   public static FavoritePage newFavoritePage()
            throws WTException {

	   FavoritePage instance = new FavoritePage();
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
