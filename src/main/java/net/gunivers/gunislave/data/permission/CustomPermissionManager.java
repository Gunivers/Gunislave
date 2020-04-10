package net.gunivers.gunislave.data.permission;

import java.util.HashMap;
import java.util.Map;

public final class CustomPermissionManager
{
	final Map<String, RootNode> roots = new HashMap<>();

	public RootNode getRoot(String name) { return this.roots.get(name); }
	public RootNode getOrNewRoot(String name)
	{
		RootNode node = this.getRoot(name);

		if (node == null)
			this.roots.put(name, node = new RootNode(name));

		return node;
	}

	/**
	 * Convert the full name of a {@linkplain PermissionNode} to a path for usage in a {@linkplain RootNode}.
	 * Effectively strips the full name of its first node target.
	 * <code><blockquote>
	 * "root.path" -> "path"
	 * <br>"root" -> Error, it doesn't contains any dots.
	 * </blockquote></code>
	 *
	 * @param fullName a node's full name
	 * @return the path
	 * @see PermissionNode#getFullName()
	 */
	public String toPath(String fullName)
	{
		int index = fullName.indexOf('.');
		return index > -1 ? fullName.substring(index) : fullName;
	}

	/**
	 * If the full name starts with the origin's fullName, it is stripped from the latter.
	 * @param fullName a node's full name
	 * @param origin the origin of the path
	 * @return the path from the origin
	 */
	public String toPath(String fullName, PermissionNode origin)
	{
		if (fullName.startsWith(origin.getFullName()))
			return fullName.substring(origin.getFullName().length());

		return fullName;
	}

	CustomPermissionManager() {}
}