package net.gunivers.gunislave.util.trees;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * A helper class for managing trees.
 * @author AZ
 *
 * @param <N> the core {@linkplain Node} implementation for the trees.
 * @param <R> the {@linkplain RootNode} implementation for the trees.
 */
public class RootManager<N extends Node<N>, R extends RootNode<N>>
{
	private final Function<String, R> rootProducer;
	private final Map<String, R> roots;

	/**
	 * Constructor.
	 * @param rootProducer a {@linkplain Function} used to control root production in {@linkplain RootManager#getOrNewRoot(String)}.
	 */
	public RootManager(Function<String, R> rootProducer)
	{
		this.rootProducer = rootProducer;
		this.roots = new HashMap<>();
	}

	/**
	 * Retrieves the root associated to the provided name and wrap it in an {@linkplain Optional}.
	 * If the mapping doesn't exist, return an empty {@linkplain Optional} instead.
	 * @param name the local name of the root.
	 * @return the target root
	 */
	public Optional<R> getRoot(String name) { return Optional.ofNullable(this.roots.get(name)); }

	/**
	 * Retrieves the root associated to the provided name, and creates a new one if the mapping doesn't exist.
	 * @param name the local name of the root.
	 * @return the target root, or a fresh created one.
	 */
	public R getOrNewRoot(String name)
	{
		Optional<R> root = this.getRoot(name);

		if (!root.isPresent())
		{
			R newRoot = this.rootProducer.apply(name);
			this.roots.put(name, newRoot);
			return newRoot;
		}

		return root.get();
	}

	/**
	 * Uses {@linkplain Node}'s navigation at root level, enabling usage of full names.
	 * @param fullName the target node's full name
	 * @param navigator the navigator as in {@linkplain Node#navigate(String, Function)}
	 * @return the navigation result
	 * @see Node#navigate(String, Function)
	 * @see Node#getFullName()
	 */
	public Optional<N> navigate(String fullName, Function<String, N> navigator)
	{
		String path = this.toPath(fullName);

		if (path != "")
			return this.getRoot(fullName.substring(0, fullName.indexOf('.'))).flatMap(root -> root.navigate(path, navigator));

		return Optional.empty();
	}

	/**
	 * Convert the full name of a {@linkplain Node} to a path usable in a {@linkplain RootNode} navigation.
	 * Effectively strips the full name of its first node target.
	 * <code><blockquote>
	 *     "root.path" -> "path"
	 * <br>"my.awesome.tree" -> "awesome.tree"
	 * <br>"root" -> ""
	 * </blockquote></code>
	 *
	 * @param fullName a node's full name
	 * @return the full name to convert
	 * @see Node#getFullName()
	 * @see Node#navigate(String, Function)
	 */
	public String toPath(String fullName)
	{
		int index = fullName.indexOf('.');
		return index > -1 ? fullName.substring(index) : "";
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
