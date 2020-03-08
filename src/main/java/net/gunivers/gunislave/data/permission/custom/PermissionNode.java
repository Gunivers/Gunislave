package net.gunivers.gunislave.data.permission.custom;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

	public PermissionNode makePath(String path)
	{
		PermissionNode node = this;

		for (String child : path.split("\\."))
			node = node.getOrNewChild(child);

		return node;
	}

	public CustomPermission getOrNewPermission(String localName, int level)
	{
		PermissionNode child = this.getChildren().get(localName);

		if (child == null)
			return new CustomPermission(this.parent, localName, level);

		return child.asCustomPermission();
	}

	public PermissionNode getOrNewChild(String localName)
	{
		PermissionNode child = this.getChildren().get(localName);

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
	/** @return the path of this configuration in its tree. */
	public String getFullName() { return this.fullName; }
}
