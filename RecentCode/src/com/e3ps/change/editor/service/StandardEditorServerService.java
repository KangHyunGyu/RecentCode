package com.e3ps.change.editor.service;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import wt.fc.PersistenceHelper;
import wt.part.WTPart;
import wt.pom.Transaction;
import wt.services.StandardManager;
import wt.session.SessionHelper;
import wt.util.WTException;
import wt.xml.XMLLob;

import com.e3ps.change.EChangeOrder2;
import com.e3ps.change.EOEul;
import com.e3ps.change.editor.EOActionTempAssyData;
import com.e3ps.change.editor.EOActionTempItemData;
import com.e3ps.common.log4j.Log4jPackages;

public class StandardEditorServerService extends StandardManager implements EditorServerService {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.CHANGE.getName());
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Default factory for the class.
	 * @return
	 * @throws WTException
	 */
	public static StandardEditorServerService newStandardEditorServerService() throws WTException {
		StandardEditorServerService instance = new StandardEditorServerService();
		instance.initialize();
		return instance;
	}
	
	@Override
	public EOEul saveEul(EChangeOrder2 eo, String poid, XMLLob lob,
			ArrayList editParts, EOEul eul) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();

			if (eul != null && PersistenceHelper.isPersistent(eul)) {
				eul = (EOEul) PersistenceHelper.manager.refresh(eul);
				eul.setTopAssyOid(poid);
				eul.setXml(lob);
				LOGGER.info("eul save");
				eul = (EOEul) PersistenceHelper.manager.save(eul);
				EditorServerHelper.removeAllEditPartsLink(eul);
			} else {
				eul = EOEul.newEOEul();
				eul.setTopAssyOid(poid);
				eul.setXml(lob);
				eul.setEco(eo);
				eul.setOwner(SessionHelper.manager.getPrincipalReference());
				eul = (EOEul) wt.fc.PersistenceHelper.manager.save(eul);
			}

			EditorServerHelper.addEditPartsLink(eul, editParts);

			trx.commit();
			trx = null;

			return eul;

		} catch (Exception e) {
			throw e;
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
	}

	@Override
	public String getSapString(Document document) throws Exception {
		Hashtable hash = getSapData(document);

		Enumeration en = hash.keys();

		StringBuffer result = new StringBuffer();

		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();

			EOActionTempAssyData eta = (EOActionTempAssyData) hash.get(key);

			result.append(eta);
			result.append("--------------------------------------------------------------------------------------\n");
		}

		return result.toString();
	}

	@Override
	public Hashtable getSapData(Document document) throws Exception {
		Element root = (Element) document.getFirstChild();
		NodeList list = root.getElementsByTagName("EOStructure");

		ArrayList assyList = new ArrayList();
		Hashtable hash = new Hashtable();

		for (int i = 0; i < list.getLength(); i++) {
			Element node = (Element) list.item(i);
			NodeList newElement = node.getElementsByTagName("NEW");
			NodeList oldElement = node.getElementsByTagName("OLD");
			NodeList assyElement = node.getElementsByTagName("NextAssy");

			EOActionTempAssyData adata = null;

			if (assyElement != null && assyElement.getLength() > 0) {
				Element ele = (Element) assyElement.item(0);
				NodeList nodelist4 = ele.getChildNodes();

				String assyPart = nodelist4.item(0).getNodeValue().trim();
				String nextAssyVersion = ele.getAttribute("NextAssyVersion");

				String stdQuantity = ele.getAttribute("StdQuantity");
				String orgStdQuantity = ele.getAttribute("OrgStdQuantity");

				String key = assyPart + ":" + nextAssyVersion;

				if (assyList.contains(key)) {
					adata = (EOActionTempAssyData) hash.get(key);
				} else {
					adata = new EOActionTempAssyData(assyPart, nextAssyVersion,
							orgStdQuantity, stdQuantity);
					assyList.add(key);
				}

				hash.put(key, adata);
			}

			EOActionTempItemData data = null;

			if (newElement != null && newElement.getLength() > 0) {
				Element ele = (Element) newElement.item(0);
				NodeList nodelist1 = ele.getChildNodes();

				data = new EOActionTempItemData();
				data.newPart = nodelist1.item(0).getNodeValue().trim();
				data.newQuantity = ele.getAttribute("Quantity");
				data.newVersion = ele.getAttribute("Version");
				data.newUnit = ele.getAttribute("Unit");
				data.newItemSeq = ele.getAttribute("ItemSeq");

				adata.itemList.add(data);

			}
			if (oldElement != null && oldElement.getLength() > 0) {
				Element ele = (Element) oldElement.item(0);
				NodeList nodelist2 = ele.getChildNodes();
				String oldPart = nodelist2.item(0).getNodeValue().trim();
				String oldVersion = ele.getAttribute("Version");
				String oldItemSeq = ele.getAttribute("ItemSeq");
				if (data == null) {
					data = new EOActionTempItemData();
					data.editType = "D";
					adata.itemList.add(data);
				} else {
					data.editType = "C";
				}
				data.oldPart = oldPart;
				data.oldVersion = oldVersion;
				data.oldQuantity = ele.getAttribute("Quantity");
				data.oldUnit = ele.getAttribute("Unit");
				data.oldItemSeq = oldItemSeq;
			}
		}

		return hash;
	}

	@Override
	public EChangeOrder2 saveChangeData(EChangeOrder2 eco, Hashtable hash)
			throws Exception {
		Enumeration en = hash.keys();

		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			EOActionTempAssyData adata = (EOActionTempAssyData) hash.get(key);
			eco = saveChangeData(eco, adata);
		}
		return eco;
	}

	@Override
	public EChangeOrder2 saveChangeData(EChangeOrder2 eco,
			EOActionTempAssyData adata) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();

			WTPart assy = EulPartHelper.service.getPart(adata.assyPart);

			for (int i = 0; i < adata.itemList.size(); i++) {
				EOActionTempItemData data = (EOActionTempItemData) adata.itemList
						.get(i);

				if ("I".equals(data.editType)) {
					WTPart item = EulPartHelper.service.getPart(data.newPart);

				} else if ("D".equals(data.editType)) {
					WTPart item = EulPartHelper.service.getPart(data.oldPart);

				} else if ("C".equals(data.editType)) {
					WTPart newItem = EulPartHelper.service.getPart(data.newPart);
					WTPart oldItem = EulPartHelper.service.getPart(data.oldPart);

				}
			}

			trx.commit();
			trx = null;

			return eco;

		} catch (Exception e) {
			throw e;
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
	}

}
