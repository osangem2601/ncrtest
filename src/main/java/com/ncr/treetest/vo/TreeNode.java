package com.ncr.treetest.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TreeNode {

	private String name;
	private String description;
	private TreeNode parent = null;
	private Map<String, TreeNode> children = new HashMap<>();
	private static volatile Set<String> treeNodeSet = new TreeSet<String>();

	private static volatile TreeNode ROOT = createRootNode();
	public TreeNode()
	{
		
	}
	public TreeNode(String name, String description) {
		this.name = name;
		this.description = description;
	}

	private static TreeNode createRootNode() {
		TreeNode root = new TreeNode("root", "This is the root node");
		treeNodeSet.add("root");
		return root;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Collection<TreeNode> findChildrenNodes() {
		if (children != null) {
			return children.values();
		} else {
			return null;
		}
	}
	
	public Stream<TreeNode> flattened() {
		return Stream.concat(Stream.of(this), this.findChildrenNodes().stream().flatMap(TreeNode::flattened));
	}

	private static void validateNodeName(String nodeName) throws Exception{
		if (null == nodeName) {
			throw new Exception("Cannot find null node!");
		}
		if (!treeNodeSet.contains(nodeName)) {
			throw new Exception("Node does not exists");
		}
	}
	
	public TreeNode addChild(TreeNode childNode) throws Exception {

		if (treeNodeSet.contains(childNode.getName())) {
			throw new Exception("Child alreay exists somewhere in the tree");
		} else if (children.size() >= 15) {
			throw new Exception("Cannot add child node. Parent already reached max size 15");
		} else {
			childNode.parent = this;
			children.put(childNode.getName(), childNode);
			treeNodeSet.add(childNode.getName());
		}

		return childNode;
	}

	public boolean removeChild(String nodeName) throws Exception {

		validateNodeName(nodeName);
		treeNodeSet.remove(nodeName);
		TreeNode childNode = children.remove(nodeName);
		// remove the children recursively
		for (TreeNode child : childNode.findChildrenNodes()) {
			childNode.removeChild(child.getName());
		}
		childNode = null;// make it null otherwise it is just removed from the map.
	
		return true;
	}

	public static boolean removeNode(String nodeName) throws Exception {
		validateNodeName(nodeName);

		return TreeNode.findParent(nodeName).removeChild(nodeName);
	}

	public static TreeNode findParent(String nodeName) throws Exception {
		TreeNode treeNode = findNode(nodeName);
		return treeNode.parent;
	}

	public static TreeNode findNode(String nodeName) throws Exception {
		validateNodeName(nodeName);
		Optional<TreeNode> op = ROOT.flattened().filter(t -> t.getName().equals(nodeName)).findFirst();

		return op.get();
	}

	public static ArrayList<ArrayList<TreeNode>> findDescendants (TreeNode node) {
		ArrayList<ArrayList<TreeNode>> tempList = new ArrayList<ArrayList<TreeNode>>();
		TreeNode currentnode = node;

		if (currentnode.findChildrenNodes().size() > 0) {
			for (TreeNode child : currentnode.findChildrenNodes()) {

				tempList.addAll(findDescendants(child));

			}
			for (ArrayList<TreeNode> child : tempList) {
				child.add(0, currentnode);
			}

		} else {
			ArrayList<TreeNode> thisnode = new ArrayList<TreeNode>();
			thisnode.add(currentnode);
			tempList.add(thisnode);
		}

		return tempList;

	}
	
	public String calculatePath() {
		LinkedList<String> pathNodeNameList = new LinkedList<>();
		TreeNode node = this;
		pathNodeNameList.offerFirst(this.getName());
		while (node.parent != null) {
			node = node.parent;
			pathNodeNameList.offerFirst(node.getName());
		}

		String path = pathNodeNameList.stream().map(str -> str).collect(Collectors.joining("->"));
		return path;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		TreeNode treeNode = (TreeNode) obj;

		return this.name.equals(treeNode.name);

	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

}
