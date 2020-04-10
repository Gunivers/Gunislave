package net.gunivers.gunislave.data.permission;

import net.gunivers.gunislave.util.trees.Node;
import net.gunivers.gunislave.util.trees.NodeFactory;

public class PermissionNode extends Node<PermissionNode>
{
	private static final long serialVersionUID = 2065849983131455447L;

	PermissionNode(Node<PermissionNode> parent, String localName)
	{
		this(parent, localName, PermissionNode::new);
	}

	PermissionNode(Node<PermissionNode> parent, String localName, NodeFactory<PermissionNode> factory)
	{
		super(parent, localName, factory);
	}

	public CustomPermission getOrNewPermission(String localName, int level)
	{
		CustomPermission child = this.getPermission(localName);

		if (child == null)
			return new CustomPermission(this.getParent(), localName, level);

		return child;
	}

	public CustomPermission getPermission(String localName)
	{
		PermissionNode child = this.getChild(localName);

		if (child == null)
			return null;

		return child.asCustomPermission();
	}

	/** @return wether this node is a permission */
	public boolean isCustomPermission() { return false; }
	/** @return this node as a permission, or null if and only if {@linkplain #isCustomPermission()} returns false*/
	public CustomPermission asCustomPermission() { return null; }
}
