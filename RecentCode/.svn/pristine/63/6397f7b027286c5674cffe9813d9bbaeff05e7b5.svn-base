/**
 * 
 */
package com.e3ps.common.favorite;

import com.e3ps.common.impl.OwnPersistable;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.Serialization;

import wt.fc.InvalidAttributeException;
import wt.util.WTException;

@GenAsPersistable
(interfaces={OwnPersistable.class}, serializable=Serialization.EXTERNALIZABLE_BASIC,
	properties={
		@GeneratedProperty(name="url", type=String.class, javaDoc="URL"),
		@GeneratedProperty(name="name", type=String.class, javaDoc="메뉴명")
	}
)
public class Favorite extends _Favorite{
	static final long serialVersionUID = 1;
	
	public static Favorite newFavorite() throws WTException {
		
	Favorite instance = new Favorite();
		instance.initialize();
		return instance;
	}

	private void initialize() {
		// TODO Auto-generated method stub
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
