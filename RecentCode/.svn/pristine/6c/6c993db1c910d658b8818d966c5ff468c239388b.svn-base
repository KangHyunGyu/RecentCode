package com.e3ps.project.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wt.doc.WTDocument;
import wt.enterprise.RevisionControlled;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.pom.Transaction;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.StandardManager;
import wt.util.WTException;

import com.e3ps.admin.service.AdminHelper;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.web.ParamUtil;
import com.e3ps.doc.E3PSDocument;
import com.e3ps.doc.bean.DocCodeTypeData;
import com.e3ps.project.EOutput;
import com.e3ps.project.EProject;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.OutputType;
import com.e3ps.project.OutputTypeStep;
import com.e3ps.project.key.ProjectKey.MESSAGEKEY;

public class StandardOutputService extends StandardManager implements
		wt.method.RemoteAccess, java.io.Serializable, OutputService {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PROJECT.getName());
	private static final long serialVersionUID = 1L;

	public static StandardOutputService newStandardOutputService()
			throws WTException {
		StandardOutputService instance = new StandardOutputService();
		instance.initialize();
		return instance;
	}

	/**
	 * 산출물 정의등록
	 * 
	 * @param hash
	 * @return
	 * @throws Exception
	 */
	@Override
	public String saveOutput(Map<String, Object> hash) throws Exception {

		Transaction trx = new Transaction();
		String msg = MESSAGEKEY.CREATE;
		try {
			trx.start();
			String oid = ParamUtil.get(hash, "oid");
			String name = ParamUtil.get(hash, "name");
			String description = ParamUtil.get(hash, "description");
			String location = ParamUtil.get(hash, "location");
			boolean attachFile = ParamUtil.getBoolean(hash, "attachFile");
			String outputStep = ParamUtil.get(hash, "outputStep");
			String outputChildStep = ParamUtil.get(hash, "outputChildStep");
			String outputType = StringUtil.checkNull(ParamUtil.get(hash, "outputType"));

			ETaskNode node = (ETaskNode) CommonUtil.getObject(oid);

			if ("".equals(outputType))
				outputType = "GENERAL";

			EOutput output = EOutput.newEOutput();
			output.setName(name);
			output.setDescription(description);
			output.setLocation(location);
			output.setTask(node);
			output.setAttachFile(attachFile);
			output.setDocType(outputType);

			if (outputChildStep != null && outputChildStep.length() > 0) {
				OutputTypeStep ots = (OutputTypeStep) CommonUtil.getObject(outputChildStep);
				output.setStep(ots);
			} else {
				if (outputStep != null && outputStep.length() > 0) {
					OutputTypeStep ots = (OutputTypeStep) CommonUtil.getObject(outputStep);
					output.setStep(ots);
				} else {
					output.setStep(null);
				}
			}

			output = (EOutput) PersistenceHelper.manager.save(output);

			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getLocalizedMessage() + MESSAGEKEY.CREATE_ERROR;
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return msg;
	}

	/**
	 * 산출물 정의 수정
	 * 
	 * @param hash
	 * @return
	 * @throws Exception
	 */

	@Override
	public String updateOutput(Map<String, Object> hash) throws Exception {

		Transaction trx = new Transaction();
		String msg = MESSAGEKEY.UPDATE;
		try {
			trx.start();

			String oid = ParamUtil.get(hash, "oid");
			String name = ParamUtil.get(hash, "name");
			String description = ParamUtil.get(hash, "description");
			String location = ParamUtil.get(hash, "location");
			boolean attachFile = ParamUtil.getBoolean(hash, "attachFile");
			String outputStep = ParamUtil.get(hash, "outputStep");
			String outputChildStep = ParamUtil.get(hash, "outputChildStep");
			String outputType = StringUtil.checkNull(ParamUtil.get(hash,
					"outputType"));

			EOutput output = (EOutput) CommonUtil.getObject(oid);

			if ("".equals(outputType))
				outputType = "GENERAL";

			output.setName(name);
			output.setDescription(description);
			output.setLocation(location);
			output.setAttachFile(attachFile);
			output.setDocType(outputType);

			if (outputChildStep != null && outputChildStep.length() > 0) {
				OutputTypeStep ots = (OutputTypeStep) CommonUtil.getObject(outputChildStep);
				output.setStep(ots);
			} else {
				if (outputStep != null && outputStep.length() > 0) {
					OutputTypeStep ots = (OutputTypeStep) CommonUtil.getObject(outputStep);
					output.setStep(ots);
				} else {
					output.setStep(null);
				}
			}

			output = (EOutput) PersistenceHelper.manager.modify(output);

			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getLocalizedMessage() + MESSAGEKEY.UPDATE_ERROR;
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return msg;
	}

	/**
	 * 산출물 정의 삭제
	 * 
	 * @param hash
	 * @return
	 * @throws Exception
	 */

	@Override
	public String deleteOutput(Map<String, Object> hash) throws Exception {

		Transaction trx = new Transaction();
		String msg = MESSAGEKEY.DELETE;
		try {
			trx.start();

			String oid = ParamUtil.get(hash, "oid");

			EOutput output = (EOutput) CommonUtil.getObject(oid);

			PersistenceHelper.manager.delete(output);
			
			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getLocalizedMessage() +  MESSAGEKEY.DELETE_ERROR;
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return msg;
	}

	@Override
	public String linkOutputDocument(Map<String, Object> hash) throws Exception {

		Transaction trx = new Transaction();
		String msg = MESSAGEKEY.LINK;
		try {
			trx.start();

			String outputOid = ParamUtil.get(hash, "outputOid");
			String documentOid = ParamUtil.get(hash, "documentOid");
			
			ReferenceFactory rf = new ReferenceFactory(); 

			EOutput output = (EOutput) rf.getReference(outputOid).getObject();
			EProject project = (EProject) output.getTask().getProject();
			LOGGER.info("outputOid :: "+outputOid);
			LOGGER.info("documentOid :: "+documentOid);
			if( documentOid.indexOf("E3PSDocument") > 0) {
				E3PSDocument document = (E3PSDocument) rf.getReference(documentOid)
						.getObject();

				output.setDocument(document);
				output.setBranchIdentifier(document.getBranchIdentifier());
				output.setDocClassName(document.getClass().getName());
				
				//권한
				RevisionControlled per = (RevisionControlled) document;
				AdminHelper.service.setAuthToObject(per, project);
			}else if(documentOid.indexOf("WTDocument") > 0) {
				WTDocument document = (WTDocument) rf.getReference(documentOid)
						.getObject();

				output.setDocument(document);
				output.setBranchIdentifier(document.getBranchIdentifier());
				output.setDocClassName(document.getClass().getName());
				
				//권한
				RevisionControlled per = (RevisionControlled) document;
				AdminHelper.service.setAuthToObject(per, project);
			}
			LOGGER.info("output.getDocument() :: "+output.getDocument());
			PersistenceHelper.manager.modify(output);

			trx.commit();
			trx = null;

		} catch (Exception e) {
			msg = e.getLocalizedMessage() + MESSAGEKEY.LINK_ERROR;
			e.printStackTrace();
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return msg;
	}

	/**
	 * 산출물 링크 삭제
	 * 
	 * @param hash
	 * @throws Exception
	 */

	@Override
	public String deleteOutputDocumentLink(Map<String, Object> hash) throws Exception {

		Transaction trx = new Transaction();
		String msg = "산출물 링크가 " + MESSAGEKEY.DELETE;
		try {
			trx.start();

			String outputOid = ParamUtil.get(hash, "outputOid");

			ReferenceFactory rf = new ReferenceFactory();

			EOutput output = (EOutput) rf.getReference(outputOid).getObject();
			output.setDocument(null);
			output.setBranchIdentifier(0);
			output.setDocClassName(null);
			PersistenceHelper.manager.modify(output);

			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getLocalizedMessage() + "산출물 링크 "+MESSAGEKEY.DELETE_ERROR;
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return msg;
	}

	@Override
	public ArrayList<OutputType> getOutputType() throws Exception {
		ArrayList<OutputType> list = new ArrayList<OutputType>();
		try {
			OutputType[] output = OutputType.getOutputTypeSet();
			for (int i = 0; i < output.length; i++) {
				list.add(output[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new WTException(e);
		}
		return list;
	}

	/**
	 * output의 step목록 가져오기
	 * 
	 * @param key
	 * @return
	 */

	@Override
	public QueryResult getCodeList(String key) {
		return getCodeList(key, null);
	}

	@Override
	public QueryResult getCodeList(String key, OutputTypeStep parent) {
		try {
			QuerySpec query = new QuerySpec(OutputTypeStep.class);
			query.appendWhere(new SearchCondition(OutputTypeStep.class,
					"outputType", "=", OutputType.toOutputType(key)),
					new int[] { 0 });

			query.appendAnd();
			if (parent == null) {
				query.appendWhere(new SearchCondition(OutputTypeStep.class,
						"parentReference.key.classname", true), new int[] { 0 });
			} else {
				query.appendWhere(new SearchCondition(OutputTypeStep.class,
						"parentReference.key.id", "=", parent.getPersistInfo()
								.getObjectIdentifier().getId()),
						new int[] { 0 });
			}

			query.appendOrderBy(new OrderBy(new ClassAttribute(
					OutputTypeStep.class, OutputTypeStep.CODE), false),
					new int[] { 0 });

			return PersistenceHelper.manager.find(query);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new QueryResult();
		}
	}

}