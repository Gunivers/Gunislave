package net.gunivers.gunislave.util.trees;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Node<N extends Node<N>> implements Serializable
{
	private static final long serialVersionUID = 2065849983131455447L;

	final Map<String, N> children;
	private final Map<String, N> childrenImmutable;

	private final NodeFactory<N> factory;
	private final Node<N> parent;
	private final String localName;
	private final String fullName;

	protected Node(Node<N> parent, String localName, NodeFactory<N> factory)
	{
		this(parent, parent.getFullName() + localName, localName, factory);
		parent.children.put(localName, this.cast());
	}

	Node(Node<N> parent, String fullName, String localName, NodeFactory<N> factory)
	{
		this(parent, fullName, localName, new HashMap<>(), factory);
	}

	Node(Node<N> parent, String fullName, String localName, Map<String, N> children, NodeFactory<N> factory)
	{
		if (localName.contains("."))
			throw new IllegalArgumentException("A node name may not contain '.'");

		this.children = children;
		this.childrenImmutable = Collections.unmodifiableMap(children);

		this.factory = factory;
		this.parent = parent;
		this.localName = localName;
		this.fullName = fullName;
	}

	/**
	 * Navigate to the target node until it is reached. Each time null is encountered, creates a child to pursue navigation.
	 * @param path the path to the target from this node.
	 * @return the target node.
	 * @see #navigate(String, Function)
	 */
	public N makePath(String path)
	{
		return this.navigate(path, this::getOrNewChild);
	}

	/**
	 * Navigate to the target node until it is reached or null is encountered.
	 * @param path the path to the target from this node.
	 * @return the target node, or null if it doesn't exist.
	 * @see #navigate(String, Function)
	 */
	public N getNode(String path)
	{
		return this.navigate(path, this::getChild);
	}

	/**
	 * Navigate within the tree to the target from this node.
	 * <p>
	 * <strong>Special Case:</strong>
	 * <br> • If the path is empty, return null
	 *
	 * @param path the path to the target from this node.
	 * @param navigator a function mapping a name to either a child or null.
	 * @return the target node, or null if it doesn't exist.
	 */
	public N navigate(String path, Function<String, N> navigator)
	{
		String[] parts = path.split("\\.");
		N node = navigator.apply(parts[0]);

		for (int i = 1; i < parts.length; i++)
		{
			if (node == null)
				return null;

			node = navigator.apply(parts[i]);
		}

		return node;
	}

	public N getOrNewChild(String localName)
	{
		N child = this.getChild(localName);

		if (child == null)
			return this.factory.produce(this, localName, this.factory);

		return child;
	}

	public RootNode<N> getRoot()
	{
		Node<N> node = this;

		while (!node.isRoot())
			node = node.getParent();

		return node.asRoot();
	}

	public boolean hasChild(String localName) { return this.getChildren().containsKey(localName); }
	public N getChild(String localName) { return this.children.get(localName); }

	/**
	 * Cast this node into N.
	 * Use it only if you know what you are doing, because some nodes such as root nodes may not extend N.
	 * @return this, as a N
	 * @throws ClassCastException if this node isn't an instance of N
	 */
	@SuppressWarnings("unchecked")
	public N cast() { return (N) this; }

	/** @return wether this node is a root node */
	public boolean isRoot() { return false; }
	public RootNode<N> asRoot() { return null; }

	/** @return an immutable set of this node's children. */
	public Map<String, N> getChildren() { return this.childrenImmutable; }
	/** @return this node's parent. */
	public Node<N> getParent() { return this.parent; }
	/** @return the name of this node. */
	public String getLocalName() { return this.localName; }

	/**
	 * @return the full path of this node, including the tree root.
	 */
	public String getFullName() { return this.fullName; }
}
