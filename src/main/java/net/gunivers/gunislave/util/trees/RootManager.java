package net.gunivers.gunislave.util.trees;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import net.gunivers.gunislave.data.permission.PermissionNode;

public class RootManager<N extends Node<N>, R extends RootNode<N>>
{
	private final Function<String, R> rootProducer;
	private final Map<String, R> roots;

	public RootManager(Function<String, R> rootProducer)
	{
		this.rootProducer = rootProducer;
		this.roots = new HashMap<>();
	}

	public R getRoot(String name) { return this.roots.get(name); }
	public R getOrNewRoot(String name)
	{
		R node = this.getRoot(name);

		if (node == null)
			this.roots.put(name, node = this.rootProducer.apply(name));

		return node;
	}

	/**
	 * Convert the full name of a {@linkplain PermissionNode} to a path for usage in a {@linkplain R}.
	 * Effectively strips the full name of its first node target.
	 * <code><blockquote>
	 *     "root.path" -> "path"
	 * <br>"my.awesome.tree" -> "awesome.tree"
	 * <br>"root" -> "root"
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
	public String toPath(String fullName, N origin)
	{
		if (fullName.startsWith(origin.getFullName()))
			return fullName.substring(origin.getFullName().length());

		return fullName;
	}
}
