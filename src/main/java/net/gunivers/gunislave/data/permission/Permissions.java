package net.gunivers.gunislave.data.permission;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import discord4j.core.object.util.Permission;

public interface Permissions
{
	/**
	 * Immutable map of all discord permissions.
	 */
	Map<Permission, DiscordPermission> DISCORD = Collections.unmodifiableMap(new HashMap<Permission, DiscordPermission>()
	{
		private static final long serialVersionUID = 6278003296155861195L;

		{
			for (Permission permission : Permission.values())
				this.put(permission, new DiscordPermission(permission));
		}
	});

	CustomPermissionManager CUSTOM = new CustomPermissionManager();

	int getLevel();

	default boolean isDiscordPermission() { return false; }
	default boolean isCustomPermission() { return false; }

	default DiscordPermission asDiscordPermission() { return null; }
	default CustomPermission asCustomPermission() { return null; }
}

final class CustomPermissionManager
{
	final static Map<String, RootNode> ROOTS = new HashMap<>();

	public static RootNode getRoot(String name) { return ROOTS.get(name); }
	public static RootNode getOrNewRoot(String name)
	{
		RootNode node = CustomPermissionManager.getRoot(name);

		if (node == null)
			ROOTS.put(name, node = new RootNode(name));

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
	 * @throws IndexOutOfBoundsException if fullName doesn't contains any dots.
	 */
	public static String toPath(String fullName) { return fullName.substring(fullName.indexOf('.')); }

	/**
	 * If the full name starts with the origin's fullName, it is stripped from the latter.
	 * @param fullName a node's full name
	 * @param origin the origin of the path
	 * @return the path from the origin
	 */
	public static String toPath(String fullName, PermissionNode origin)
	{
		if (fullName.startsWith(origin.getFullName()))
			return fullName.substring(origin.getFullName().length());

		return fullName;
	}
}