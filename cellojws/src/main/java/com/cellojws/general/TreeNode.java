package com.cellojws.general;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TreeNode
{

	private Collection<TreeNode> below = new ArrayList<TreeNode>();
	
	private Collection<TreeNode> above = new ArrayList<TreeNode>();
	
	private NodeContainer data;
	
	public TreeNode(NodeContainer data)
	{
		setData(data);
		data.setNode(this);
	}
	
	public void addNodeBelow(final TreeNode node)
	{
		if( !below.contains(node) )
		{
			below.add(node);
			node.addNodeAbove(this);
		}
		
	}
	
	public void addNodeAbove(final TreeNode treeNode)
	{
		if( !above.contains(treeNode) )
		{
			above.add(treeNode);
			treeNode.addNodeBelow(this);
		}
	}
	
	public Collection<TreeNode> getAllNodesBelow(final Tree tree, final boolean includeRoot)
	{
		// Use a set since we only want unique nodes
		final Set<TreeNode> all = new HashSet<TreeNode>();
		for( final TreeNode node : below )
		{
			if( node != tree.getRoot() && !includeRoot )
			{
				all.addAll(node.getAllNodesBelow(tree, includeRoot));
				all.add(node);
			}
		}		
		
		return all;
	}

	public NodeContainer getData()
	{
		return data;
	}

	public void setData(NodeContainer data)
	{
		this.data = data;
	}

	@Override
	public String toString()
	{
		return data != null ? data.toString() : "Null data";
	}
	
}
