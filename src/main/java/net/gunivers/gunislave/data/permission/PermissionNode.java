package net.gunivers.gunislave.data.permission;

import java.util.Optional;

import net.gunivers.gunislave.util.trees.Node;
import net.gunivers.gunislave.util.trees.NodeFactory;

/**
 * Core {@linkplain Node} implementation for permission trees.
 * @author AZ
 */
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
		Optional<CustomPermission> child = this.getPermission(localName);

		if (!child.isPresent())
			return new CustomPermission(this.getParent(), localName, level);

		return child.get();
	}

	public Optional<CustomPermission> getPermission(String localName)
	{
		return this.getChild(localName).filter(PermissionNode::isCustomPermission).map(PermissionNode::asCustomPermission);
	}

	/** @return wether this node is a permission. */
	public boolean isCustomPermission() { return false; }

	/** @return this node as a permission, or null if it isn't one. */
	public CustomPermission asCustomPermission() { return null; }
}
