package net.gunivers.gunislave.util.trees;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * This class represents a Node in a tree.
 * @author AZ
 *
 * @param <N> the core {@linkplain Node} implementation in a tree.
 */
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
			throw new IllegalArgumentException("A node's local name may not contain '.'");

		this.children = children;
		this.childrenImmutable = Collections.unmodifiableMap(children);

		this.factory = factory;
		this.parent = parent;
		this.localName = localName;
		this.fullName = fullName;
	}

	/**
	 * Navigates to the target node until it is reached. Each time null is encountered, creates a child to pursue navigation.
	 * <br>The returned {@linkplain Optional} being empty is equivalent to the provided path being empty.
	 *
	 * @param path the path to the target from this node.
	 * @return the target node.
	 * @see #navigate(String, Function)
	 */
	public Optional<N> makePath(String path)
	{
		return this.navigate(path, (node, name) -> Optional.of(node.getOrNewChild(name)));
	}

	/**
	 * Navigates to the target node until it is reached or null is encountered.
	 * <br>The returned {@linkplain Optional} is only full if the navigation completed successfully.
	 *
	 * @param path the path to the target from this node.
	 * @return the target node, or null if it doesn't exist.
	 * @see #navigate(String, Function)
	 */
	public Optional<N> getNode(String path)
	{
		return this.navigate(path, Node::getChild);
	}

	/**
	 * Navigates within the tree to the target from this node.
	 * If at any point the navigation completes empty, returns an empty optional (ie navigation failure)
	 *
	 * <p>
	 * <strong>Edge Case:</strong>
	 * <br> • If the path is empty, return an empty Optional
	 *
	 * @param path the path to the target from this node.
	 * @param navigator a function called in each iteration, getting to the next point toward the destination.
	 * @return an {@linkplain Optional} representing the target node.
	 * @see Node#getNode(String)
	 * @see Node#makePath(String)
	 */
	public Optional<N> navigate(String path, BiFunction<Node<N>, String, Optional<N>> navigator)
	{
		String[] parts = path.split("\\.");

		if (parts.length == 0) //Cannot initialize navigation
			return Optional.empty();

		//Handle first part of the navigation
		Optional<N> node = navigator.apply(this, parts[0]);

		for (int i = 1; i < parts.length; i++)
		{
			final int index = i; //Needed because of java's lambda expression requiring final outer variables
			node = node.flatMap(n -> navigator.apply(n, parts[index]));
		}

		return node;
	}

	/**
	 * Try to get a
	 * @param localName
	 * @return
	 * @see Node#getChild(String)
	 */
	public N getOrNewChild(String localName)
	{
		Optional<N> child = this.getChild(localName);

		if (!child.isPresent())
			return this.factory.produce(this, localName);

		return child.get();
	}

	/**
	 * Gets the root of the tree, by recursive calls to {@linkplain Node#getParent}.
	 * <br>If the actual root isn't an instance of {@linkplain RootNode} due to implementation details, this might fail and produce
	 * a {@linkplain NullPointerException}.
	 * @return the root
	 */
	public RootNode<N> getRoot()
	{
		Node<N> node = this;

		while (!node.isRoot())
			node = node.getParent();

		return node.asRoot();
	}

	/**
	 * Checks wether the provided local name is associated to a child of this node.
	 * @param localName the name of the child, inside this node.
	 * @return wether the child exists.
	 * @see Node#getLocalName()
	 */
	public boolean hasChild(String localName) { return this.getChildren().containsKey(localName); }

	/**
	 * Get a child of this node, associated to the provided local name.
	 * <br>If the child does not exists, return an empty {@linkplain Optional}. Otherwise it is only wrapped.
	 *
	 * @param localName the name of the child, inside this node.
	 * @return an {@linkplain Optional} representing this node's child.
	 * @see Node#getLocalName()
	 */
	public Optional<N> getChild(String localName) { return Optional.ofNullable(this.children.get(localName)); }

	/**
	 * Cast this node into N. Calling this on a {@linkplain RootNode} fails in most cases.
	 * <br>This operation is unchecked, and will throw a {@linkplain ClassCastException} upon failure.
	 * @return this, as a N
	 * @throws ClassCastException if this node isn't an instance of N ─ usually a root
	 */
	@SuppressWarnings("unchecked")
	public N cast() { return (N) this; }

	/** @return wether this node is a root node. */
	public boolean isRoot() { return false; }
	/** @return this node, or null if it isn't a root node. */
	public RootNode<N> asRoot() { return null; }

	/** @return this node's children, as an immutable map. */
	public Map<String, N> getChildren() { return this.childrenImmutable; }

	/**
	 * Beware the parent might not be an instance of N.
	 * For instance, {@linkplain RootNode} aren't instance of N although they extend {@linkplain Node}{@literal <N>}.
	 * <br>But, if the parent of this node's parent exists, this node's parent is guarranted to be an instance of N,
	 * because the tree propagates N downward.
	 * @return this node's parent.
	 */
	public Node<N> getParent() { return this.parent; }

	/**
	 * The local name of a node is its unique identifier <i>within its parent's children batch</i>.
	 * For instance, another node in the same tree may have the same local name, but wouldn't have the same parent.
	 * As such, the unique identifier of a node in a tree is its full name.
	 * <p>
	 * Use the local name for getting a child, and for {@linkplain #navigate(String, Function) navigation} within the tree.
	 * <br>Note : navigation requires you to stack a child's local name to its parent's with <code>.</code>
	 * @return the local name of this node.
	 * @see Node#getFullName()
	 */
	public String getLocalName() { return this.localName; }

	/**
	 * The full path of a node is its unique identifier <i>within the whole tree</i>.
	 * As a full name is the jointure of the parent node's full name and the node's local name by the character '.', it is guarranted to
	 * at least have one component, the last one being the node's local name.
	 * <p>
	 * In the simple straight-forward tree below, the full name of the node 'node' is 'tree.foo.node'.
	 * <code><blockquote>
	 * root ─ foo ─ node
	 * </blockquote></code>
	 * Lines '─' represent a parent ─ child link.
	 * <p>
	 * Note: A full name is computed upon instanciation and is then immutable, implying you cannot rename nodes and their local name is
	 * immutable as well.
	 *
	 * @return the full path of this node, including the tree root.
	 * @see Node#getLocalName()
	 */
	public String getFullName() { return this.fullName; }
}
