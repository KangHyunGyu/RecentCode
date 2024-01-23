package com.e3ps.groupware.service;

import java.util.Hashtable;
import java.util.Vector;

import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.pom.Transaction;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.StandardManager;
import wt.session.SessionHelper;
import wt.util.WTException;

import com.e3ps.common.content.service.CommonContentHelper;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.groupware.board.Board;
import com.e3ps.groupware.board.BoardComent;

public class StandardBoardService extends StandardManager implements BoardService {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.GROUPWARE.getName());
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default factory for the class.
	 * @return
	 * @throws WTException
	 */
	public static StandardBoardService newStandardBoardService() throws WTException {
		StandardBoardService instance = new StandardBoardService();
		instance.initialize();
		return instance;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.BoardService#commentSeach(java.lang.String)
	 */
	@Override
	public QueryResult commentSeach(String oid) {
		QueryResult qr = null;
		try {
			oid = oid.substring(oid.lastIndexOf(":") + 1);
			QuerySpec spec = new QuerySpec();
			int idx = spec.addClassList(BoardComent.class, true);
			spec.appendWhere(
					new SearchCondition(BoardComent.class, "boardReference.key.id", "=", Long.parseLong(oid)),
					new int[idx]);
			spec.appendOrderBy(new OrderBy(
					new ClassAttribute(BoardComent.class, "thePersistInfo.createStamp"), true),
					new int[] { idx });
			qr = PersistenceHelper.manager.find(spec);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return qr;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.BoardService#create(java.util.Hashtable, java.lang.String[])
	 */
	@Override
	public String create(Hashtable hash, String[] loc) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();

			Board b = Board.newBoard();
			b.setTitle((String) hash.get("title"));
			b.setContents((String) hash.get("contents"));
			b.setOwner(SessionHelper.manager.getPrincipalReference());
			b = (Board) PersistenceHelper.manager.save(b);
			if (loc != null) {
				for (int i = 0; i < loc.length; i++) {
					CommonContentHelper.service.attach(b, loc[i]);
				}
			}
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw e;
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return "등록 되었습니다.";
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.BoardService#delete(java.lang.String)
	 */
	@Override
	public String delete(String oid) {
		try {
			if (oid != null) {
				ReferenceFactory f = new ReferenceFactory();
				Board b = (Board) f.getReference(oid).getObject();
				PersistenceHelper.manager.delete(b);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "삭제 되었습니다.";
	}

	/* (non-Javadoc)
	 * @see com.e3ps.groupware.service.BoardService#modify(java.util.Hashtable, java.lang.String[], java.lang.String[])
	 */
	@Override
	public String modify(Hashtable hash, String[] loc, String[] deloc) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();
			String oid = (String) hash.get("oid");
			ReferenceFactory f = new ReferenceFactory();
			if (oid != null) {
				Board b = (Board) f.getReference(oid).getObject();
				b.setTitle((String) hash.get("title"));
				b.setContents((String) hash.get("contents"));
				b = (Board) PersistenceHelper.manager.modify(b);
				// 기존 첨부 파일이 삭제 된 여부를 판단 하여 삭제 한다.
				// null 인 경우는 전부 삭제 된 경우다..
				if (deloc != null) {
					ContentHolder holder = ContentHelper.service.getContents((ContentHolder) f.getReference(
							oid).getObject());
					Vector files = ContentHelper.getContentList(holder);
					if (files != null) {
						for (int i = 0; i < files.size(); i++) {
							// 이미 들어 있던 파일을 검색
							ApplicationData oldFile = (ApplicationData) files.get(i);
							boolean flag = true;
							// 삭제 된 파일을 찾아 삭제하여 준다.
							for (int j = 0; j < deloc.length; j++) {
								String oidNo = deloc[j];
								if (oidNo.equals(oldFile.getPersistInfo().getObjectIdentifier().toString())) {
									flag = false;
								}
							}
							if (flag) {
								CommonContentHelper.service.delete(b, oldFile);
							}
						}
					}
				} else {
					CommonContentHelper.service.delete(b);
				}

				if (loc != null) {
					for (int i = 0; i < loc.length; i++) {
						CommonContentHelper.service.attach(b, loc[i]);
					}
				}
			}
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw e;
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return "수정 되었습니다.";
	}

	
}
