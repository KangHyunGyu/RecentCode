package com.e3ps.common.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.e3ps.common.log4j.Log4jPackages;

public class TreeManager<T> {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
	private static Pattern jsonAttrConfigPattern = null;
	private static Pattern jsonValueConfigPattern = null;

	private static final String TYPE_STRING = "string";
	private static final String TYPE_BOOLEAN = "boolean";
	private static final String TYPE_INT = "int";
	private static final String TYPE_INTEGER = "integer";
	private static final String TYPE_LONG = "long";
	private static final String TYPE_DOUBLE = "double";

	static {
		jsonAttrConfigPattern = Pattern.compile("([^:]+)" + "(:(string|boolean|int|integer|long|double))?",
				Pattern.CASE_INSENSITIVE);
		jsonValueConfigPattern = Pattern.compile("^\\$\\{(.*)\\}$", Pattern.CASE_INSENSITIVE);
	}
	
	public JSONObject toJsonObject(TreeNode<T> node,
			String jonsChildrenFieldName, Map<String, String> jsonTreeNodeFieldMap) {

		JSONObject jsonObject = null;
		try {
			jsonObject = createAllJsonTree(node, jonsChildrenFieldName, jsonTreeNodeFieldMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	public List<TreeNode<T>> asList(TreeNode<T> treeNode) {
		return traverseAllChildren(0, treeNode);
	}
	
	public TreeNode<T> loadTreeNodes(T rootNodeObj, TreeLoader<T> treeLoader) 
			throws Exception {
		TreeNode<T> rootNode = new TreeNode<T>(null, rootNodeObj, 0);
		fillChildNodesByLoader(rootNode, treeLoader, -1);
		return rootNode;
	}
	
	public TreeNode<T> loadTreeNodes(T rootNodeObj, TreeLoader<T> treeLoader, int maxDepth) 
			throws Exception {
		TreeNode<T> rootNode = new TreeNode<T>(null, rootNodeObj, 0);
		fillChildNodesByLoader(rootNode, treeLoader, maxDepth);
		return rootNode;
	}
	
	public List<TreeNode<T>> loadTreeNodesByLevel(List<T> nodeObjects, String levelAttrName) 
			throws Exception {
		return loadTreeNodesByLevel(nodeObjects, levelAttrName, 0);
	}
	
	public List<TreeNode<T>> loadTreeNodesByLevel(
				List<T> nodeObjects, String levelAttrName, int rootLevel) 
			throws Exception {
		List<TreeNode<T>> rootNodes = new ArrayList<TreeNode<T>>();
		
		if (nodeObjects != null && nodeObjects.size() > 0) {
			Stack<TreeNode<T>> stack = new Stack<TreeNode<T>>();
			
			for (int i = 0; i < nodeObjects.size(); i++) {
				T nodeObject = nodeObjects.get(i);

				int peekLevel = getPeekedNodeObjectLevel(stack, levelAttrName, rootLevel);
				int level = getNodeObjectLevel(nodeObject, levelAttrName, rootLevel);
				if (level > peekLevel && (level - peekLevel) != 1) {
					throw new Exception("Can't skip level " 
							+ peekLevel + " to " + level + " # list index = " + i);
				}
				
				while (peekLevel >= level) {
					TreeNode<T> popedNode = popNode(stack);
					TreeNode<T> peekedNode = peekNode(stack);
					if (peekedNode != null) {
						peekedNode.addChild(popedNode);
					} else {
						rootNodes.add(popedNode);
					}
					peekLevel = getPeekedNodeObjectLevel(stack, levelAttrName, rootLevel);
				}
				
				boolean leaf = isLeafNode(nodeObjects, i, level, levelAttrName, rootLevel);
				TreeNode<T> peekedNode = peekNode(stack);
				TreeNode<T> node = new TreeNode<T>(peekedNode, nodeObject);
				if (leaf && peekedNode != null) {
					peekedNode.addChild(node); // addChild
				} else {
					stack.push(node);
				}
			} // End of for
			
			while (!stack.isEmpty()) {
				TreeNode<T> popedNode = popNode(stack);
				TreeNode<T> peekedNode = peekNode(stack);
				if (peekedNode != null) {
					peekedNode.addChild(popedNode);
				} else {
					rootNodes.add(popedNode);
				}
			}
		}
		return rootNodes;
	}
	
	public List<TreeNode<T>> getAllParentsAsList(TreeNode<T> baseNode, boolean includeSelf) {
		List<TreeNode<T>> parents = new ArrayList<TreeNode<T>>();
		TreeNode<T> node = baseNode;
		while (node.getParent() != null) {
			TreeNode<T> parentNode = node.getParent();
			parents.add(0, parentNode);
			node = parentNode;
		}
		if (includeSelf) {
			parents.add(baseNode);
		}

		return parents;
	}

	public TreeNode<T> findNode(TreeNode<T> treeNode, T object) {
		Object nodeObject = treeNode.getObject();
		if (object.equals(nodeObject)) {
			return treeNode;
		} else {
			if (treeNode.hasChildren()) {
				List<TreeNode<T>> children = treeNode.getChildren();
				for (TreeNode<T> child : children) {
					TreeNode<T> findedNode = findNode(child, object);
					if (findedNode != null) {
						return findedNode;
					}
				}
			}
		}
		return null;
	}
	
	public List<TreeNode<T>> findNodesByValue(TreeNode<T> treeNode, String attrName, Object value) {

		List<TreeNode<T>> findedNodes = new ArrayList<TreeNode<T>>();
		try {
			Object nodeObjectValue = BeansUtil.getAttributeValue(treeNode, attrName);
			if (value.equals(nodeObjectValue)) {
				findedNodes.add(treeNode);
			} else {
				if (treeNode.hasChildren()) {
					List<TreeNode<T>> children = treeNode.getChildren();
					for (TreeNode<T> child : children) {
						findedNodes.addAll(findNodesByValue(child, attrName, value));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return findedNodes;
	}
	
	private List<TreeNode<T>> traverseAllChildren(int depth, TreeNode<T> node) {
		List<TreeNode<T>> allNodes = new ArrayList<TreeNode<T>>();

		node.setDepth(depth);

		allNodes.add(node);

		if (node.hasChildren()) {
			List<TreeNode<T>> children = node.getChildren();
			int childrenDepth = depth + 1;
			for (TreeNode<T> child : children) {
				allNodes.addAll(traverseAllChildren(childrenDepth, child));
			}
		}
		return allNodes;
	}

	private JSONObject createAllJsonTree(TreeNode<T> node, String jonsChildrenFieldName,
			Map<String, String> jsonTreeNodeFieldMap) throws JSONException {

		JSONObject jsonObject = createJsonTreeNode(node, jsonTreeNodeFieldMap);
		if (node.hasChildren()) {
			List<TreeNode<T>> children = node.getChildren();
			JSONArray jsonArray = new JSONArray();
			for (TreeNode<T> child : children) {
				jsonArray.put(createAllJsonTree(child, jonsChildrenFieldName, jsonTreeNodeFieldMap));
			}
			jsonObject.put(jonsChildrenFieldName, jsonArray);
		}
		return jsonObject;
	}

	private JSONObject createJsonTreeNode(TreeNode<T> node, Map<String, String> jsonTreeNodeFieldMap)
			throws JSONException {
		try {
			JSONObject jsonObject = new JSONObject();
			Iterator<String> keys = jsonTreeNodeFieldMap.keySet().iterator();
			while (keys.hasNext()) {
				String jsonAttrConfig = keys.next();
				String jsonValueConfig = jsonTreeNodeFieldMap.get(jsonAttrConfig);

				Matcher jsonAttrMatcher = jsonAttrConfigPattern.matcher(jsonAttrConfig);
				if (jsonAttrMatcher.matches()) {
					String jsonAttrNameTokenStr = jsonAttrMatcher.group(1);
					String jsonAttrType = (jsonAttrMatcher.group(3) != null) ? jsonAttrMatcher.group(3) : "string";

					String[] jsonAttrNameTokens = jsonAttrNameTokenStr.split("\\.");
					JSONObject jsonNodeObject = findTargetJsonObject(jsonObject, jsonAttrNameTokens);
					String jsonAttrName = jsonAttrNameTokens[jsonAttrNameTokens.length - 1];

					Object objectValue = null;
					Matcher jsonValueMatcher = jsonValueConfigPattern.matcher(jsonValueConfig);
					if (jsonValueMatcher.matches()) {
						String objectAttrName = jsonValueMatcher.group(1);
						objectValue = BeansUtil.getAttributeValue(node, objectAttrName);
					} else {
						objectValue = jsonValueConfig;
					}
					putValueToJsonObject(jsonNodeObject, jsonAttrName, jsonAttrType, objectValue);
				} else {
					throw new JSONException("Check the jsonTreeNodeFieldMap # jsonAttrName.jsonAttrName:type");
				}

			} // End of while

			return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			throw new JSONException(e.getMessage());
		}
	}
	
	private JSONObject findTargetJsonObject(JSONObject jsonObject, String[] jsonAttrNameTokens) throws JSONException {
		JSONObject jsonNodeObject = jsonObject;
		for (int i = 0; i < jsonAttrNameTokens.length - 1; i++) {
			String attrNameToken = jsonAttrNameTokens[i];
			if (jsonNodeObject.has(attrNameToken)) {
				jsonNodeObject = (JSONObject) jsonNodeObject.get(attrNameToken);
			} else {
				JSONObject nodeObject = new JSONObject();
				jsonNodeObject.put(attrNameToken, nodeObject);
				jsonNodeObject = nodeObject;
			}
		}
		return jsonNodeObject;
	}
	
	private void putValueToJsonObject(
				JSONObject jsonObject, String jsonAttrName, String jsonAttrType, Object objectValue) 
			throws JSONException {

		if (objectValue != null) {
			if (jsonAttrType.equalsIgnoreCase(TYPE_STRING)) {
				jsonObject.put(jsonAttrName, TypeUtil.stringValue(objectValue));
			} else if (jsonAttrType.equalsIgnoreCase(TYPE_BOOLEAN)) {
				jsonObject.put(jsonAttrName, TypeUtil.booleanValue(objectValue));
			} else if (jsonAttrType.equalsIgnoreCase(TYPE_INT) || jsonAttrType.equalsIgnoreCase(TYPE_INTEGER)) {
				jsonObject.put(jsonAttrName, TypeUtil.intValue(objectValue));
			} else if (jsonAttrType.equalsIgnoreCase(TYPE_LONG)) {
				jsonObject.put(jsonAttrName, TypeUtil.longValue(objectValue));
			} else if (jsonAttrType.equalsIgnoreCase(TYPE_DOUBLE)) {
				jsonObject.put(jsonAttrName, TypeUtil.doubleValue(objectValue));
			} else {
				jsonObject.put(jsonAttrName, objectValue);
			}
		} else {
			jsonObject.put(jsonAttrName, JSONObject.NULL);
		}
	}
	
	private boolean isLeafNode(
				List<T> nodeObjects, int index, int level, String levelAttrName, int rootLevel) 
			throws Exception {
		boolean isLeaf = true;
		if (index < nodeObjects.size() - 1) {
			T nextNodeObject = nodeObjects.get(index + 1);
			int nextLevel = getNodeObjectLevel(nextNodeObject, levelAttrName, rootLevel);
			if (nextLevel > level) {
				isLeaf = false;
			}
		}
		return isLeaf;
	}
	
	private int getNodeObjectLevel(T nodeObject, String levelAttrName, int rootLevel) 
			throws Exception {
		int level = getLevelValue(nodeObject, levelAttrName, rootLevel);
		if (level >= rootLevel) {
			return level;
		} else {
			throw new Exception("Level value must greter than rootLevel. # " 
					+ "levelAttrName=" + levelAttrName + ", " 
					+ "rootLevel=" + rootLevel + ", level=" + level);
		}
	}
	
	private int getLevelValue(T nodeObject, String levelAttrName, int rootLevel) throws Exception {
		int level = rootLevel - 1;
		if (nodeObject != null) {
			Object levelAttrValue = BeansUtil.getAttributeValue(nodeObject, levelAttrName);
			if (levelAttrValue != null) {
				level = TypeUtil.intValue(levelAttrValue);
			}
		}
		return level;
	}
	
	private int getPeekedNodeObjectLevel(
				Stack<TreeNode<T>> stack, String levelAttrName, int rootLevel) 
			throws Exception {
		int initLevel = rootLevel - 1;
		if (!stack.isEmpty()) {
			int peekLevel = initLevel;
			T peekNodeObject = stack.peek().getObject();
			if (peekNodeObject != null) {
				Object levelAttrValue = BeansUtil.getAttributeValue(peekNodeObject, levelAttrName);
				if (levelAttrValue != null) {
					peekLevel = TypeUtil.intValue(levelAttrValue);
				}
			}
			if (peekLevel >= rootLevel) {
				return peekLevel;
			} else {
				throw new Exception("Can't found level value in '" + levelAttrName + "' attribute");
			}
		} else {
			return initLevel;
		}
	}
	
	private TreeNode<T> peekNode(Stack<TreeNode<T>> stack) {
		if (!stack.isEmpty()) {
			return stack.peek();
		}
		return null;
	}
	
	private TreeNode<T> popNode(Stack<TreeNode<T>> stack) {
		if (!stack.isEmpty()) {
			return stack.pop();
		}
		return null;
	}
	
	private TreeNode<T> fillChildNodesByLoader(TreeNode<T> node, TreeLoader<T> treeNodeLoader, int maxDepth) {
		T nodeObj = node.getObject();
		if (maxDepth < 0 || node.getDepth() < maxDepth) {
			List<T> childNodeObjs = treeNodeLoader.getChildNodeObjects(nodeObj);
			if (childNodeObjs != null) {
				for (T childNodeObj : childNodeObjs) {
					TreeNode<T> childNode = new TreeNode<T>(node, childNodeObj);
					node.addChild(fillChildNodesByLoader(childNode, treeNodeLoader, maxDepth));
				}
			}
		}
		return node;
	}
}
