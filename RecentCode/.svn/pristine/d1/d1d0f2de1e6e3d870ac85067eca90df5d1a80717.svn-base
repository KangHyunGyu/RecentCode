package com.e3ps.common.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private TreeNode<T> parent;
	private List<TreeNode<T>> children;
	private T object;
	private int depth;

	public TreeNode() {
		// do nothing
	}

	public TreeNode(TreeNode<T> parent, T object) {
		this(parent, object, null);
	}

	public TreeNode(TreeNode<T> parent, T object, int depth) {
		this(parent, object, depth, null);
	}

	public TreeNode(T object) {
		this(null, object, null);
	}

	public TreeNode(TreeNode<T> parent, T object, List<TreeNode<T>> children) {
		this(parent, object, -1, children);
	}

	public TreeNode(TreeNode<T> parent, T object, int depth, List<TreeNode<T>> children) {
		this.parent = parent;
		this.object = object;
		if (depth >= 0) {
			this.depth = depth;
		} else {
			if (parent != null) {
				this.depth = parent.getDepth() + 1;
			} else {
				this.depth = 0;
			}
		}
		setChildren(children);
	}

	public int getChildrenCount() {
		int childrenCount = 0;
		if (children != null) {
			childrenCount = children.size();
		}
		return childrenCount;
	}

	public void addChild(TreeNode<T> child) {
		// #. 추가되는 child 의 parent 변경
		child.setParent(this);

		if (children == null) {
			children = new ArrayList<TreeNode<T>>();
		}
		children.add(child);
	}

	public boolean hasChildren() {
		return (getChildrenCount() > 0);
	}

	public boolean isLeaf() {
		return !hasChildren();
	}

	

	/**
	 * @return the parent
	 */
	public TreeNode<T> getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(TreeNode<T> parent) {
		this.parent = parent;
	}

	/**
	 * @return the children
	 */
	public List<TreeNode<T>> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<TreeNode<T>> children) {
		// #. 추가되는 children 의 parent 변경
		if (children != null) {
			for (TreeNode<T> child : children) {
				child.setParent(this);
			}
		}
		this.children = children;
	}

	/**
	 * @return the object
	 */
	public T getObject() {
		return object;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(T object) {
		this.object = object;
	}

	/**
	 * @return the depth
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * @param depth the depth to set
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}
}
