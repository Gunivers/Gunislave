package net.gunivers.gunislave.data.permission;

import java.util.Map;

public final class RootNode extends PermissionNode
{
	private static final long serialVersionUID = -1262134563083325167L;

	RootNode(String name)
	{
		super(null, name, name);
	}

	RootNode(String name, Map<String, PermissionNode> children)
	{
		super(null, name, name, children);
	}

	@Override public boolean isRoot() { return true; }
	@Override public RootNode asRoot() { return this; }
}
