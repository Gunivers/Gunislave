package net.gunivers.gunislave.util.trees;

import java.util.Map;

/**
 * A helper class which represents the root of a tree.
 * It uses specific package-private {@linkplain Node} constructors to bypass full name computation and other operation on the parent.
 * @author AZ
 *
 * @param <N> the core {@linkplain Node} implementation in a tree.
 */
public class RootNode<N extends Node<N>> extends Node<N>
{
	private static final long serialVersionUID = -1262134563083325167L;

	protected RootNode(String name, NodeFactory<N> factory)
	{
		super(null, name, name, factory);
	}

	protected RootNode(String name, Map<String, N> children, NodeFactory<N> factory)
	{
		super(null, name, name, children, factory);
	}

	/**
	 * Always returns true, because a {@linkplain RootNode} is always a root.
	 * @return true
	 */
	@Override public boolean isRoot() { return true; }

	/** @return this node */
	@Override public RootNode<N> asRoot() { return this; }
}
