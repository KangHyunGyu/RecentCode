
package com.e3ps.groupware.faq;

import wt.content.ContentHolder;
import wt.fc.Item;
import wt.inf.container.WTContained;
import wt.util.WTException;

import com.e3ps.common.util.OwnPersistable;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.PropertyConstraints;
import com.ptc.windchill.annotations.metadata.Serialization;


@GenAsPersistable(superClass=Item.class,interfaces={OwnPersistable.class, WTContained.class, ContentHolder.class}, 
   serializable=Serialization.EXTERNALIZABLE_BASIC,
   properties={
   @GeneratedProperty(name="title", type=String.class), //제목
   @GeneratedProperty(name="mType", type=String.class), //구분
   @GeneratedProperty(name="seq", type=int.class), //순번
   @GeneratedProperty(name="solution", type=String.class, // 답변
      constraints=@PropertyConstraints(upperLimit=4000))
   })
public class Faq extends _Faq {


   static final long serialVersionUID = 1;

   public static Faq newFaq()
            throws WTException {

	   Faq instance = new Faq();
      instance.initialize();
      return instance;
   }
}
