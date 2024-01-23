package com.e3ps.common.util;

import com.e3ps.common.log4j.Log4jPackages;

import wt.access.WTAclEntry;
import wt.enterprise.Master;
import wt.enterprise.RevisionControlled;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.WTObject;
import wt.folder.Folder;
import wt.folder.FolderEntry;
import wt.folder.FolderHelper;
import wt.lifecycle.LifeCycleHelper;
import wt.lifecycle.LifeCycleManaged;
import wt.lifecycle.State;
import wt.part.WTPart;
import wt.pom.Transaction;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.series.HarvardSeries;
import wt.series.MultilevelSeries;
import wt.series.Series;
import wt.series.SeriesIncrementInvalidException;
import wt.team.Team;
import wt.team.TeamException;
import wt.team.TeamHelper;
import wt.team.TeamManaged;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;
import wt.util.WTRuntimeException;
import wt.vc.Iterated;
import wt.vc.IterationIdentifier;
import wt.vc.Mastered;
import wt.vc.VersionControlException;
import wt.vc.VersionControlHelper;
import wt.vc.VersionIdentifier;
import wt.vc.Versioned;
import wt.vc.views.ViewManageable;
import wt.vc.wip.CheckoutLink;
import wt.vc.wip.WorkInProgressHelper;
import wt.vc.wip.Workable;

/**
 * @author JYK
 *
 */
public class ObjectUtil {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());

   /**
    * <pre>
    * @desc  :
    * @author : JYK
    * @date   : 2015. 2. 12.오후 2:37:16
    * </pre>
    * @method : checkin
    * @param workable
    * @return
    * @throws WTException
    */
   public static Workable checkin(Workable workable) throws WTException {
      Workable returnObj = null;
      try {
         returnObj = WorkInProgressHelper.service.checkin(workable, "");
      } catch (WTPropertyVetoException e) {
         throw new WTException(e);
      }
      return returnObj;
   }
   
   public static Workable checkin(Workable workable,String notice) throws Exception{
       return WorkInProgressHelper.service.checkin(workable, notice);
   }

   /**
    * <pre>
    * @desc  :
    * @author : JYK
    * @date   : 2015. 2. 12.오후 2:38:13
    * </pre>
    * @method : checkout
    * @param workable
    * @return
    * @throws WTException
    */
   public static Workable checkout(Workable workable) throws WTException {
      CheckoutLink checkOutLink = null;
      Workable workingCopy = null;
      try {

         if (!WorkInProgressHelper.isWorkingCopy(workable)) {
            if (!WorkInProgressHelper.isCheckedOut(workable)) {
               Folder folder = WorkInProgressHelper.service.getCheckoutFolder();
               checkOutLink = WorkInProgressHelper.service.checkout(workable, folder, "");
               workingCopy = checkOutLink.getWorkingCopy();
            } else if (WorkInProgressHelper.isCheckedOut(workable)) {
               workingCopy = WorkInProgressHelper.service.workingCopyOf(workable);
            }
         } else {
            workingCopy = workable;
         }

      } catch (WTPropertyVetoException e) {
         throw new WTException(e);
      }

      return workingCopy;
   }
   
   public static Workable getWorkingObject(Workable workable) throws WTException {
	     
	   CheckoutLink checkOutLink = null;
	   Workable workingCopy = null;
	   try {
		   
		   if(WorkInProgressHelper.isWorkingCopy(workable)){
			   workingCopy = workable;
		   }else{
			   if (WorkInProgressHelper.isCheckedOut(workable)) {
				   workingCopy = WorkInProgressHelper.service.workingCopyOf(workable);
			   } else{
				   workingCopy = workable;
			   }
		   }
		  
	   } catch (Exception e) {
		   throw new WTException(e);
	   }

	   return workingCopy;
  }

   /**
    * <pre>
    * @desc  :
    * @author : JYK
    * @date   : 2015. 2. 12.오후 2:38:49
    * </pre>
    * @method : undoCheckout
    * @param work
    * @return
    * @throws WTException
    */
   public static Workable undoCheckout(Workable work) throws WTException {
      try {
         work = WorkInProgressHelper.service.undoCheckout(work);

      } catch (WTPropertyVetoException e) {
         throw new WTException(e);
      }
      return work;
   }

   /**
    * <pre>
    * @desc  :
    * @author : JYK
    * @date   : 2015. 2. 12.오후 2:40:15
    * </pre>
    * @method : deletePersistable
    * @param persist
    * @throws WTException
    */
   public static void deletePersistable(Persistable persist) throws WTException {
      PersistenceHelper.manager.delete(persist);
   }

   /**
    * <pre>
    * @desc  :
    * @author : JYK
    * @date   : 2015. 2. 12.오후 2:40:30
    * </pre>
    * @method : getLatestObject
    * @param master
    * @return
    * @throws WTException
    */
   public static RevisionControlled getLatestObject(Master master) throws WTException {
      return getLatestObject(master, null);
   }

   /**
    * <pre>
    * @desc  :
    * @author : JYK
    * @date   : 2015. 2. 12.오후 2:40:36
    * </pre>
    * @method : getLatestObject
    * @param master
    * @param viewName
    * @return
    * @throws WTException
    */
   public static RevisionControlled getLatestObject(Master master, String viewName) throws WTException {
      RevisionControlled rc = null;
      QueryResult qr = VersionControlHelper.service.allVersionsOf(master);

      while (qr.hasMoreElements()) {
         RevisionControlled obj = (RevisionControlled) qr.nextElement();

         if ((viewName != null) && (!viewName.equals(((ViewManageable) obj).getViewName()))) {
            continue;
         }
         if ((rc != null) && (!obj.getVersionIdentifier().getSeries().greaterThan(rc.getVersionIdentifier().getSeries()))) {
            continue;
         }
         rc = obj;
      }

      if (rc != null) {
         return (RevisionControlled) VersionControlHelper.getLatestIteration(rc, false);
      }
      return rc;
   }

   /**
    * <pre>
    * @desc  :
    * @author : JYK
    * @date   : 2015. 2. 12.오후 2:41:06
    * </pre>
    * @method : getLatestVersion
    * @param object
    * @return
    * @throws WTException
    */
   public static RevisionControlled getLatestVersion(RevisionControlled object) throws WTException {
      return getLatestObject((Master) object.getMaster(), null);
   }

   /**
    * <pre>
    * @desc  :
    * @author : JYK
    * @date   : 2015. 2. 12.오후 2:41:11
    * </pre>
    * @method : getNextVersion
    * @param rc
    * @return
    */
   /*
   public static RevisionControlled getNextVersion(RevisionControlled rc) {
      try {
         int iRev = Integer.parseInt(rc.getVersionIdentifier().getValue());
         String sNextRev = String.valueOf(iRev + 1);
         rc = getVersionObject((Master) rc.getMaster(), sNextRev);
      } catch (WTException e) {
         LOGGER.error(e);
         rc = null;
      }

      return rc;
   }*/
   
   /**
    * 
    * @desc	:  
    * @author	: tsuam
    * @date	: 2019. 11. 8.
    * @method	: getNextVersion
    * @return	: RevisionControlled
    * @param rc
    * @return
    */
   public static RevisionControlled getNextVersion(RevisionControlled rc)
   {
       byte[] b = rc.getVersionIdentifier().getValue().getBytes();
       b[b.length-1] += 1;
       try
       {
           rc = getVersionObject((Master) rc.getMaster(), new String(b));
       }
       catch (WTException e)
       {
           e.printStackTrace();
           rc = null;
       }
       return rc;
   }
   
   /**
    * 
    * @desc	:
    * @author	: tsuam
    * @date	: 2019. 11. 8.
    * @method	: getPreviousVersion
    * @return	: RevisionControlled
    * @param rc
    * @return
    */
   public static RevisionControlled getPreviousVersion(RevisionControlled rc)
   {
       byte[] b = rc.getVersionIdentifier().getValue().getBytes();
       b[b.length-1] -= 1;
       try
       {
           rc = getVersionObject((Master) rc.getMaster(), new String(b));
       }
       catch (WTException e)
       {
           e.printStackTrace();
           rc = null;
       }
       return rc;
   }

   /**
    * <pre>
    * @desc  :
    * @author : JYK
    * @date   : 2015. 2. 12.오후 2:42:48
    * </pre>
    * @method : getPreviousVersion
    * @param rc
    * @return
    */
   /*
   public static RevisionControlled getPreviousVersion(RevisionControlled rc) {
      byte[] b = rc.getVersionIdentifier().getValue().getBytes();
      int tmp16_15 = (b.length - 1);
      byte[] tmp16_11 = b;
      tmp16_11[tmp16_15] = (byte) (tmp16_11[tmp16_15] - 1);
      try {
         rc = getVersionObject((Master) rc.getMaster(), new String(b));
      } catch (WTException e) {
         LOGGER.error(e);
         rc = null;
      }
      return rc;
   }
	*/
   /**
    * <pre>
    * @desc  :
    * @author : JYK
    * @date   : 2015. 2. 12.오후 2:43:10
    * </pre>
    * @method : isLatestVersion
    * @param obj
    * @return
    * @throws WTException
    */
   public static boolean isLatestVersion(RevisionControlled obj) throws WTException {
      String viewName = null;
      if ((obj instanceof ViewManageable)) {
         viewName = ((ViewManageable) obj).getViewName();
      }
      RevisionControlled lastObject = getLatestObject((Master) obj.getMaster(), viewName);
      return !lastObject.getVersionIdentifier().getSeries().greaterThan(obj.getVersionIdentifier().getSeries());
   }

   /**
    * <pre>
    * @desc  :
    * @author : JYK
    * @date   : 2015. 2. 12.오후 2:43:12
    * </pre>
    * @method : getAllIterations
    * @param iteration
    * @return
    * @throws WTException
    */
   public static QueryResult getAllIterations(Iterated iteration) throws WTException {
      QueryResult result = VersionControlHelper.service.allIterationsFrom(iteration);
      return result;
   }

   /**
    * <pre>
    * @desc  :
    * @author : JYK
    * @date   : 2015. 2. 12.오후 2:43:15
    * </pre>
    * @method : delete
    * @param persistable
    */
   public static void delete(Persistable persistable) {
      Transaction trx = new Transaction();
      if ((persistable instanceof LifeCycleManaged)) ;
      try {
         trx.start();
         deleteAclObject((WTObject) persistable);
         PersistenceHelper.manager.delete(persistable);
         trx.commit();
         trx = null;
      } catch (WTException e) {
         throw new WTRuntimeException(e);
      } finally {
         if (trx != null) trx.rollback();
      }
   }

   /**
    * <pre>
    * @desc  :
    * @author : JYK
    * @date   : 2015. 2. 12.오후 2:43:17
    * </pre>
    * @method : deleteAclObject
    * @param obj
    */
   @SuppressWarnings("deprecation")
   public static void deleteAclObject(WTObject obj) {
      Transaction trx = new Transaction();
      try {
         trx.start();
         QuerySpec query = new QuerySpec(WTAclEntry.class);
         if ((obj instanceof TeamManaged)) {
            Team team = TeamHelper.service.getTeam((TeamManaged) obj);
            query.appendWhere(new SearchCondition(WTAclEntry.class, "aclReference.key.id", "=", CommonUtil.getOIDLongValue(team)), new int[] {0});

            query.appendOr();
         }
         query.appendWhere(new SearchCondition(WTAclEntry.class, "aclReference.key.id", "=", CommonUtil.getOIDLongValue(obj)), new int[] {0});

         QueryResult qr = PersistenceHelper.manager.find(query);

         while (qr.hasMoreElements()) {
            WTAclEntry element = (WTAclEntry) qr.nextElement();
            PersistenceHelper.manager.delete(element);
         }
         trx.commit();
         trx = null;
      } catch (QueryException e) {
         throw new WTRuntimeException(e);
      } catch (TeamException e) {
         throw new WTRuntimeException(e);
      } catch (WTException e) {
         throw new WTRuntimeException(e);
      } finally {
         if (trx != null) trx.rollback();
      }
   }

   /**
    * <pre>
    * @desc  :
    * @author : JYK
    * @date   : 2015. 2. 12.오후 2:45:18
    * </pre>
    * @method : getVersionObject
    * @param master
    * @param version
    * @return
    * @throws WTException
    */
   public static RevisionControlled getVersionObject(Master master, String version) throws WTException {
      RevisionControlled rc = null;
      QueryResult qr = VersionControlHelper.service.allVersionsOf(master);

      while (qr.hasMoreElements()) {
         RevisionControlled obj = (RevisionControlled) qr.nextElement();
         if (obj.getVersionIdentifier().getSeries().getValue().equals(version)) {
            rc = obj;
         }
      }
      if (rc != null) {
         return (RevisionControlled) VersionControlHelper.getLatestIteration(rc, false);
      }
      return rc;
   }

   /**
    * <pre>
    * @desc  :
    * @author : JYK
    * @date   : 2015. 2. 12.오후 2:45:14
    * </pre>
    * @method : getNewestIteration
    * @param targetClass
    * @param master
    * @return
    */
   @SuppressWarnings("deprecation")
   public static WTObject getNewestIteration(Class targetClass, Mastered master) {
      WTObject obj = null;
      try {
         QuerySpec query = new QuerySpec(targetClass);
         query.appendWhere(VersionControlHelper.getSearchCondition(targetClass, master), new int[] {0});
         query.appendAnd();
         query.appendWhere(VersionControlHelper.getSearchCondition(targetClass, true), new int[] {0});

         QueryResult result = PersistenceHelper.manager.find(query);
         while (result.hasMoreElements()) {
            obj = (WTObject) result.nextElement();
         }
      } catch (QueryException e) {
         throw new WTRuntimeException(e);
      } catch (VersionControlException e) {
         throw new WTRuntimeException(e);
      } catch (WTException e) {
         throw new WTRuntimeException(e);
      }
      return obj;
   }

   /**
    * <pre>
    * @desc  :
    * @author : JYK
    * @date   : 2015. 2. 12.오후 2:44:58
    * </pre>
    * @method : getVersion
    * @param obj
    * @return
    * @throws WTException
    */
   public static String getVersion(RevisionControlled obj) throws WTException {
      return getMajorVersion(obj) + "." + VersionControlHelper.getIterationIdentifier(obj).getSeries().getValue();
   }

   /**
    * <pre>
    * @desc  :
    * @author : JYK
    * @date   : 2015. 2. 12.오후 2:44:56
    * </pre>
    * @method : getMajorVersion
    * @param obj
    * @return
    * @throws WTException
    */
   public static String getMajorVersion(RevisionControlled obj) throws WTException {
      return VersionControlHelper.getVersionIdentifier(obj).getSeries().getValue();
   }

   /**
    * <pre>
    * @desc  :
    * @author : JYK
    * @date   : 2015. 2. 12.오후 2:44:49
    * </pre>
    * @method : setVersion
    * @param versioned
    * @param ver
    * @throws SeriesIncrementInvalidException
    * @throws VersionControlException
    * @throws WTPropertyVetoException
    * @throws WTException
    */
   @SuppressWarnings("deprecation")
   public static void setVersion(Versioned versioned, String ver) throws SeriesIncrementInvalidException, VersionControlException, WTPropertyVetoException, WTException {
      HarvardSeries multilevelseries = HarvardSeries.newHarvardSeries("IntegerSpec");
      multilevelseries.setValue(ver);

      VersionIdentifier versionidentifier = VersionIdentifier.newVersionIdentifier(multilevelseries);
      VersionControlHelper.setVersionIdentifier(versioned, versionidentifier, false);

      Series series = Series.newSeries("wt.vc.IterationIdentifier", "1");
      IterationIdentifier iterationidentifier = IterationIdentifier.newIterationIdentifier(series);
      VersionControlHelper.setIterationIdentifier(versioned, iterationidentifier);
   }

   /**
    * <pre>
    * @desc  :
    * @author : JYK
    * @date   : 2015. 2. 12.오후 2:44:47
    * </pre>
    * @method : revise
    * @param obj
    * @param ver
    * @param state
    * @return
    * @throws WTException
    */
   public static Versioned revise(Versioned obj, String ver, String state) throws WTException {
      Transaction trx = new Transaction();

      try {
         trx.start();
         String lifecycle = ((LifeCycleManaged) obj).getLifeCycleName();
         Folder folder = FolderHelper.service.getFolder((FolderEntry) obj);
         MultilevelSeries multilevelseries = MultilevelSeries.newMultilevelSeries("wt.vc.VersionIdentifier", ver);
         VersionIdentifier vi = VersionIdentifier.newVersionIdentifier(multilevelseries);

         obj = VersionControlHelper.service.newVersion(obj, vi, VersionControlHelper.firstIterationId(obj));
         FolderHelper.assignLocation((FolderEntry) obj, folder);
         LifeCycleHelper.setLifeCycle((LifeCycleManaged) obj, LifeCycleHelper.service.getLifeCycleTemplate(lifecycle, WCUtil.getWTContainerRef()));
         obj = (Versioned) PersistenceHelper.manager.save((Persistable) obj);

         if (state != null) {
            LifeCycleHelper.service.setLifeCycleState((LifeCycleManaged) obj, State.toState(state), false);
         }
         trx.commit();
         trx = null;
      } catch (WTPropertyVetoException e) {
         throw new WTException(e);
      } finally {
         if (trx != null) {
            trx.rollback();
            trx = null;
         }
      }
      return obj;
   }

   /**
    * <pre>
    * @desc  :
    * @author : JYK
    * @date   : 2015. 2. 12.오후 2:43:55
    * </pre>
    * @method : revise
    * @param obj
    * @return
    */
   public static Versioned revise(Versioned obj) {
      String lifecycle = ((LifeCycleManaged) obj).getLifeCycleName();
      Folder folder = null;
      try {
         folder = FolderHelper.service.getFolder((FolderEntry) obj);
      } catch (WTException e) {
         throw new WTRuntimeException(e);
      }
      return revise(obj, lifecycle, folder);
   }

   /**
    * <pre>
    * @desc  :
    * @author : JYK
    * @date   : 2015. 2. 12.오후 2:43:53
    * </pre>
    * @method : revise
    * @param obj
    * @param lifecycle
    * @param folder
    * @return
    */
   public static Versioned revise(Versioned obj, String lifecycle, Folder folder) {
      return revise(obj, lifecycle, folder, null);
   }

   /**
    * <pre>
    * @desc  :
    * @author : JYK
    * @date   : 2015. 2. 12.오후 2:43:50
    * </pre>
    * @method : revise
    * @param obj
    * @param lifecycle
    * @param folder
    * @param _note
    * @return
    */
   public static Versioned revise(Versioned obj, String lifecycle, Folder folder, String _note) {
      Transaction trx = new Transaction();
      try {
         trx.start();
         obj = VersionControlHelper.service.newVersion(obj);
         if (_note != null) {
            VersionControlHelper.setNote(obj, _note);
         }
         FolderHelper.assignLocation((FolderEntry) obj, folder);
         LifeCycleHelper.setLifeCycle((LifeCycleManaged) obj, LifeCycleHelper.service.getLifeCycleTemplate(lifecycle, WCUtil.getWTContainerRef()));
         Versioned vs = (Versioned) PersistenceHelper.manager.save(obj);

         trx.commit();
         trx = null;
         return vs;
      } catch (WTException e) {
         throw new WTRuntimeException(e);
      } catch (WTPropertyVetoException e) {
         throw new WTRuntimeException(e);
      } finally {
         if (trx != null) {
            trx.rollback();
            trx = null;
         }
      }
   }

   /**
    * <pre>
    * @desc  :
    * @author : JYK
    * @date   : 2015. 2. 12.오후 2:45:44
    * </pre>
    * @method : revise
    * @param obj
    * @param lifecycle
    * @return
    */
   public static Versioned revise(Versioned obj, String lifecycle) {
      if (lifecycle == null) {
         lifecycle = ((LifeCycleManaged) obj).getLifeCycleName();
      }
      Folder folder = null;
      try {
         folder = FolderHelper.service.getFolder((FolderEntry) obj);
      } catch (WTException e) {
         throw new WTRuntimeException(e);
      }

      return revise(obj, lifecycle, folder);
   }

   /**
    * <pre>
    * @desc  :
    * @author : JYK
    * @date   : 2015. 2. 12.오후 2:46:56
    * </pre>
    * @method : revise
    * @param vObj
    * @param ver
    * @param sLifecycle
    * @param state
    * @return
    * @throws WTException
    */
   public static Versioned revise(Versioned vObj, String ver, String sLifecycle, String state) throws WTException {
      try {
         if (sLifecycle == null) {
            sLifecycle = ((LifeCycleManaged) vObj).getLifeCycleName();
         }
         Folder folder = FolderHelper.service.getFolder((FolderEntry) vObj);
         MultilevelSeries multilevelseries = MultilevelSeries.newMultilevelSeries("wt.vc.VersionIdentifier", ver);
         VersionIdentifier vi = VersionIdentifier.newVersionIdentifier(multilevelseries);
         vObj = VersionControlHelper.service.newVersion(vObj, vi, VersionControlHelper.firstIterationId(vObj));
         FolderHelper.assignLocation((FolderEntry) vObj, folder);

         LifeCycleHelper.setLifeCycle((LifeCycleManaged) vObj, LifeCycleHelper.service.getLifeCycleTemplate(sLifecycle, WCUtil.getWTContainerRef()));
         vObj = (Versioned) PersistenceHelper.manager.save(vObj);
         if (state != null) {
            LifeCycleHelper.service.setLifeCycleState((LifeCycleManaged) vObj, State.toState(state), false);
         }
         return vObj;

      } catch (WTPropertyVetoException e) {
         throw new WTException(e);
      }
   }
   
   public static boolean isCheckOut(Workable workable) throws Exception{
   	
   	boolean isCheckout = false;
       
       boolean isWorkCopy = WorkInProgressHelper.isWorkingCopy(workable);
       
       if(isWorkCopy) {
       	return true;
       }else {
       	isCheckout = WorkInProgressHelper.isCheckedOut(workable);
       }
       
       return isCheckout;
   }
   
   public static WTPart getNextPartVersion(WTPart part)
   {
       byte[] b = part.getVersionIdentifier().getValue().getBytes();
       b[b.length-1] += 1;
       try
       {
       	part = getPartVersionObject(part, new String(b));
       }
       catch (WTException e)
       {
           e.printStackTrace();
           part = null;
       }
       return part;
   }
   
   public static WTPart getPartVersionObject(WTPart part, String version) throws WTException
   {
       WTPart nextPart= null;
       Master master = part.getMaster();
       String viewName = part.getViewName();
       
       QueryResult qr = VersionControlHelper.service.allVersionsOf(master);
       while (qr.hasMoreElements())
       {
       	
       	/**
       	 *  버젼 체계 변경으로 인한 0버전 다음인 1이 들어왔을 시에 A로 강제 전환
       	 */
       	
       	if("1".equals(version)) {
       		version = "A";
       	}
       	
       	WTPart obj = ((WTPart) qr.nextElement());
       	
       	if(obj.getViewName().equals(viewName)) {
       		if (obj.getVersionIdentifier().getSeries().getValue().equals(version)) {
       			nextPart = obj;
       		}
               	
       	}
           
       }
       
       if (nextPart != null) {
       	return (WTPart) VersionControlHelper.getLatestIteration(nextPart, false);
       }
           

       return nextPart;
   }
   
   public static Versioned reviseNote(Versioned obj,String note){
   	
   	String lifecycle = ((LifeCycleManaged) obj).getLifeCycleName();
       Folder folder = null;
       try
       {
           folder = FolderHelper.service.getFolder((FolderEntry) obj);
       }
       catch (WTException e)
       {
           e.printStackTrace();
       }
       return revise(obj, lifecycle, folder,note);
   	
   }
}
