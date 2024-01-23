package com.e3ps.common.util;

import java.rmi.RemoteException;

import com.e3ps.common.log4j.Log4jPackages;

import wt.access.NotAuthorizedException;
import wt.enterprise.Master;
import wt.enterprise.RevisionControlled;
import wt.iba.definition.IBADefinitionException;
import wt.introspection.WTIntrospectionException;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SQLFunction;
import wt.query.SearchCondition;
import wt.query.SubSelectExpression;
import wt.util.WTAttributeNameIfc;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;
import wt.vc.ControlBranch;
import wt.vc.VersionControlHelper;
import wt.vc.wip.CheckoutInfo;
import wt.vc.wip.WorkInProgressState;

public class SearchUtil {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
	public static void addLastVersionCondition(QuerySpec _query, Class _target, int _idx) throws IBADefinitionException,
			NotAuthorizedException, RemoteException, WTException, QueryException, WTPropertyVetoException {
		addLastVersionCondition3(_query, _target, _idx);
		/*
		 * AttributeDefDefaultView aview =
		 * IBADefinitionHelper.service.getAttributeDefDefaultViewByPath(
		 * "LatestVersionFlag"); if (aview != null) { if (_query.getConditionCount() >
		 * 0) _query.appendAnd();
		 * 
		 * int idx = _query.appendClassList(StringValue.class, false); SearchCondition
		 * sc = new SearchCondition(new ClassAttribute(StringValue.class,
		 * "theIBAHolderReference.key.id"), "=", new ClassAttribute( _target,
		 * "thePersistInfo.theObjectIdentifier.id")); sc.setFromIndicies(new int[] {
		 * idx, _idx }, 0); sc.setOuterJoin(0); _query.appendWhere(sc, new int[] { idx,
		 * _idx }); _query.appendAnd(); sc = new SearchCondition(StringValue.class,
		 * "definitionReference.hierarchyID", "=", aview.getHierarchyID());
		 * _query.appendWhere(sc, new int[] { idx }); _query.appendAnd(); sc = new
		 * SearchCondition(StringValue.class, "value2", "=", "true");
		 * _query.appendWhere(sc, new int[] { idx }); }
		 */
	}
	
	/**
	 * 
	 * @desc	: RevisionController 최신 버전 
	 * @author	: tsuam
	 * @date	: 2019. 7. 19.
	 * @method	: addLastVersionCondition2
	 * @return	: void
	 * @param qs
	 * @param targetClass
	 * @param idx
	 * @throws WTException
	 */
	public static void addLastVersionCondition2(QuerySpec qs, Class targetClass, int idx) throws WTException {
		try {
			int branchIdx = qs.appendClassList(ControlBranch.class, false);
			int childBranchIdx = qs.appendClassList(ControlBranch.class, false);

			// #. 媛앹껜 - Parent ControlBranch 媛�Join
			if (qs.getConditionCount() > 0) qs.appendAnd();
			qs.appendWhere(new SearchCondition(targetClass, RevisionControlled.BRANCH_IDENTIFIER, ControlBranch.class, WTAttributeNameIfc.ID_NAME), new int[] { idx, branchIdx });

			// #. ControlBranch ��遺�え - �먯떇 outer join
			if (qs.getConditionCount() > 0) qs.appendAnd();
			SearchCondition outerJoinSc = new SearchCondition(ControlBranch.class, WTAttributeNameIfc.ID_NAME, ControlBranch.class, "predecessorReference.key.id");
			outerJoinSc.setOuterJoin(SearchCondition.RIGHT_OUTER_JOIN);
			qs.appendWhere(outerJoinSc, new int[] { branchIdx, childBranchIdx });

			// #. �먯떇 ControllBranch 媛�null �대㈃ 理쒖떊 Revision
			ClassAttribute childBranchIdNameCa = new ClassAttribute(ControlBranch.class, WTAttributeNameIfc.ID_NAME);
			// qs.appendSelect(childBranchIdNameCa, new int[] {childBranchIdx}, false);

			if (qs.getConditionCount() > 0) qs.appendAnd();
			qs.appendWhere(new SearchCondition(childBranchIdNameCa, SearchCondition.IS_NULL), new int[] { childBranchIdx });
		} catch (WTPropertyVetoException e) {
			throw new WTException(e);
		}
	}
	
	/**
	 * 
	 * @desc	: RevisionController 최신 버전 
	 * @author	: shjeong
	 * @date	: 2024. 01. 02.
	 * @method	: addLastVersionCondition3
	 * @return	: void
	 * @param qs
	 * @param targetClass
	 * @param idx
	 * @throws WTException
	 * @throws WTPropertyVetoException 
	 */
	public static void addLastVersionCondition3(QuerySpec qs, Class targetClass, int idx) throws WTException, WTPropertyVetoException {
		
		qs.setAdvancedQueryEnabled(true);	
		
		SearchCondition sc = null;
		
		QuerySpec subQuery = new QuerySpec();
		int subIdx = subQuery.addClassList(targetClass, false);
		subQuery.getFromClause().setAliasPrefix("Rev");
		SQLFunction func = SQLFunction.newSQLFunction(SQLFunction.MAXIMUM, new ClassAttribute(targetClass, WTAttributeNameIfc.VERSION_ID_NAME));
		subQuery.appendSelect(func, false);
			
		ClassAttribute masterColumn1 =  new ClassAttribute(targetClass, "masterReference.key.id", subQuery.getFromClause().getAliasAt(subIdx));
		ClassAttribute masterColumn2 =  new ClassAttribute(targetClass, "masterReference.key.id", qs.getFromClause().getAliasAt(idx));
			
		sc = new SearchCondition(masterColumn1, SearchCondition.EQUAL, masterColumn2);
		subQuery.appendWhere(sc, new int[] {subIdx, idx});
		subQuery.appendAnd();
		subQuery.appendOpenParen();
		sc = new SearchCondition(targetClass, "checkoutInfo."+CheckoutInfo.STATE, SearchCondition.EQUAL, WorkInProgressState.CHECKED_IN);
		subQuery.appendWhere(sc, new int[] {subIdx});
		subQuery.appendOr();
		sc = new SearchCondition(targetClass, "checkoutInfo."+CheckoutInfo.STATE, SearchCondition.EQUAL, WorkInProgressState.CHECKED_OUT);
		subQuery.appendWhere(sc, new int[] {subIdx});
		subQuery.appendCloseParen();
		
		sc = new SearchCondition(new ClassAttribute(targetClass, WTAttributeNameIfc.VERSION_ID_NAME), SearchCondition.EQUAL, new SubSelectExpression(subQuery));
		if (qs.getConditionCount() > 0) qs.appendAnd();
		qs.appendWhere(sc, new int[] {idx});
			
	}
	
	public static void setOrderBy(QuerySpec spec, Class sortingClass, int tableNo, String field, String aliasName, boolean sortingFlag)
            throws WTPropertyVetoException, QueryException, WTIntrospectionException
    {
        ClassAttribute classattribute = new ClassAttribute(sortingClass, field);
        classattribute.setColumnAlias(aliasName + String.valueOf(0));
        int[] fieldNoArr = { tableNo };
        spec.appendSelect(classattribute, fieldNoArr, false);
        OrderBy orderby = new OrderBy(classattribute, sortingFlag, null);
        spec.appendOrderBy(orderby, tableNo);
       
        //spec.appendOrderBy(new OrderBy(new ClassAttribute(sortingClass,field), sortingFlag), new int[] { tableNo });

    }
}
