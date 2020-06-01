package net.gunivers.gunislave.data.permission;

import java.util.Map;

import net.gunivers.gunislave.util.trees.RootNode;

/**
 * {@linkplain RootNode} implementation for permission trees.
 * @author AZ
 */
public final class PermissionRoot extends RootNode<PermissionNode>
{
	private static final long serialVersionUID = -1262134563083325167L;

	PermissionRoot(String name)
	{
		super(name, PermissionNode::new);
	}

	PermissionRoot(String name, Map<String, PermissionNode> children)
	{
		super(name, children, PermissionNode::new);
	}
}
