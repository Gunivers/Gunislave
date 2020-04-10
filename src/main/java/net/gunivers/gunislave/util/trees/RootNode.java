package net.gunivers.gunislave.util.trees;

import java.util.Map;

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

	@Override public boolean isRoot() { return true; }
	@Override public RootNode<N> asRoot() { return this; }
}
