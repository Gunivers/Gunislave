package net.gunivers.gunislave.data.permission;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PermissionNode implements Serializable
{
	private static final long serialVersionUID = 2065849983131455447L;

	private final Map<String, PermissionNode> children;
	private final Map<String, PermissionNode> childrenImmutable;

	private final PermissionNode parent;
	private final String localName;
	private final String fullName;

	PermissionNode(PermissionNode parent, String localName)
	{
		this(parent, parent.getFullName() + localName, localName);
		parent.children.put(localName, this);
	}

	PermissionNode(PermissionNode parent, String fullName, String localName)
	{
		this(parent, fullName, localName, new HashMap<>());
	}

	PermissionNode(PermissionNode parent, String fullName, String localName, Map<String, PermissionNode> children)
	{
		this.children = children;
		this.childrenImmutable = Collections.unmodifiableMap(children);

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
	public PermissionNode makePath(String path)
	{
		return this.navigate(path, this::getOrNewChild);
	}

	/**
	 * Navigate to the target node until it is reached or null is encountered.
	 * @param path the path to the target from this node.
	 * @return the target node, or null if it doesn't exist.
	 * @see #navigate(String, Function)
	 */
	public PermissionNode getNode(String path)
	{
		return this.navigate(path, this::getChild);
	}

	/**
	 * Navigate within the tree to the target from this node.
	 * @param path the path to the target from this node.
	 * @param navigator a function mapping a name to either a child or null.
	 * @return the target node, or null if it doesn't exist.
	 */
	public PermissionNode navigate(String path, Function<String, PermissionNode> navigator)
	{
		PermissionNode node = this;

		for (String child : path.split("\\."))
		{
			node = navigator.apply(child);

			if (node == null)
				return null;
		}

		return node;
	}

	public CustomPermission getOrNewPermission(String localName, int level)
	{
		CustomPermission child = this.getPermission(localName);

		if (child == null)
			return new CustomPermission(this.parent, localName, level);

		return child;
	}

	public PermissionNode getOrNewChild(String localName)
	{
		PermissionNode child = this.getChild(localName);

		if (child == null)
			return new PermissionNode(this, localName);

		return child;
	}

	public RootNode getRoot()
	{
		PermissionNode node = this;

		while (!node.isRoot())
			node = node.getParent();

		return node.asRoot();
	}

	public boolean hasChild(String localName) { return this.getChildren().containsKey(localName); }
	public PermissionNode getChild(String localName) { return this.children.get(localName); }
	public CustomPermission getPermission(String localName)
	{
		PermissionNode child = this.getChild(localName);

		if (child == null)
			return null;

		return this.getChild(localName).asCustomPermission();
	}

	/** @return wether this node is a permission */
	public boolean isCustomPermission() { return false; }
	/** @return this node as a permission, or null if and only if {@linkplain #isCustomPermission()} returns false*/
	public CustomPermission asCustomPermission() { return null; }

	public boolean isRoot() { return false; }
	public RootNode asRoot() { return null; }

	/** @return an immutable set of this node's children. */
	public Map<String, PermissionNode> getChildren() { return this.childrenImmutable; }
	/** @return this node's parent. */
	public PermissionNode getParent() { return this.parent; }
	/** @return the name of this node. */
	public String getLocalName() { return this.localName; }

	/**
	 * This name is unusable in a root node, use Permissions.CUSTOM.asPath(fullName) for conversion.
	 * @return the full path of this node, including the tree root.
	 * @see Permissions#CUSTOM
	 * @see CustomPermissionManager#asPath(String)
	 */
	public String getFullName() { return this.fullName; }
}
