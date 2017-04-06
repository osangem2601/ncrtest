package com.ncr.treetest.controller;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ncr.treetest.vo.TreeNode;

@RestController
public class TreeNodeController {
	/**
	 * Retrieve a single node
	 * 
	 * @param nodeName name of the node to get
	 * @return node
	 * @throws Exception
	 */
	@RequestMapping(value="/treenode/{nodeName}", method=RequestMethod.GET)
	public TreeNode getNode(@PathVariable String nodeName) throws Exception
	{
		return TreeNode.findNode(nodeName);
		
	}
	
	/**
	 * Add a node to the tree at a specific location (for instance, add a new node to a leaf node’s children)
	 * 
	 * @param nodeName parent node of the child node being added. Parent can be anywhere in the tree
	 * @param childNode node to be added in the tree
	 * @return the newly added node
	 * @throws Exception
	 */
	@RequestMapping(value="/treenode/{nodeName}", method=RequestMethod.POST)
	public TreeNode addNode(@PathVariable  String nodeName, @RequestBody TreeNode childNode) throws Exception
	{
		System.out.print("Parent node name:" + nodeName);
		TreeNode parent = TreeNode.findNode(nodeName);
		if(parent != null)
		{
			if(childNode == null)
			{
				System.out.print("Child node is null");
				return null;
			}
			System.out.print("Child node name:" + childNode.getName());
			return parent.addChild(childNode);		
		}	
		else {
			System.out.println("Parent not found");
		}
			
		return null;		
	}
	
	/**
	 * Remove a node from the tree (also removes all of its children)
	 * 
	 * @param nodeName to delete from the tree
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/treenode/{nodeName}", method=RequestMethod.DELETE)
	public String deleteNode(@PathVariable  String nodeName) throws Exception
	{
		System.out.print("Node name:" + nodeName);
		boolean success = TreeNode.removeNode(nodeName);
		if(success)
		{ 
			return "Node removed sucessfully";
		}
			
		return "Failed to remove node";		
	}
	
	/**
	 *  Retrieve all descendants of a node (immediate children and nested children).
	 *  
	 * @param nodeName 
	 * @return all descendants of a node
	 * @throws Exception
	 */
	@RequestMapping(value="/treenode/{nodeName}/descendants", method=RequestMethod.GET)
	public ArrayList<ArrayList<TreeNode>> getDescendants(@PathVariable  String nodeName) throws Exception
	{
		System.out.print("Node name:" + nodeName);
		TreeNode node = TreeNode.findNode(nodeName);
		return TreeNode.findDescendants(node);
	}
	
	
	
	/**
	 * For an arbitrary node, retrieve all ancestors/parents of the node (the path from the root node to the specific node).
	 * 
	 * @param nodeName 
	 * @return path from ROOT to the given node
	 * @throws Exception
	 */
	@RequestMapping(value="/treenode/{nodeName}/calculatepath", method=RequestMethod.GET)
	public String getPath(@PathVariable  String nodeName) throws Exception
	{
		System.out.print("Node name:" + nodeName);
		TreeNode node = TreeNode.findNode(nodeName);
		if(node != null)
		{ 
			return node.calculatePath();
		}
			
		return null;		
	}
	
	/**
	 * Retrieve the immediate children of a node
	 * 
	 * @param nodeName
	 * @return collection of immediate children of a node
	 * @throws Exception
	 */
	@RequestMapping(value="/treenode/{nodeName}/children", method=RequestMethod.GET)
	public Collection<TreeNode> getChildren(@PathVariable  String nodeName) throws Exception
	{
		System.out.print("Node name:" + nodeName);
		TreeNode node = TreeNode.findNode(nodeName);
		if(node != null)
		{ 
			return node.findChildrenNodes();
		}
			
		return null;		
	}
	

}
