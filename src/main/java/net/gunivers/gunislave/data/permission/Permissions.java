package net.gunivers.gunislave.data.permission;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.gunivers.gunislave.util.trees.RootManager;

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

	RootManager<PermissionNode, PermissionRoot> CUSTOM = new RootManager<>(PermissionRoot::new);

	/**
	 * Part of the visitor pattern; the implementation should only call the corresponding PermissionHolder.grant(Permissions) method
	 * @param holder a {@linkplain Permissible}
	 */
	void grant(Permissible holder);

	/**
	 * Part of the visitor pattern; the implementation should only call the corresponding PermissionHolder.revoke(Permissions) method
	 * @param holder a {@linkplain Permissible}
	 */
	void revoke(Permissible holder);

	int getLevel();

	default boolean isDiscordPermission() { return false; }
	default boolean isCustomPermission() { return false; }

	default DiscordPermission asDiscordPermission() { return null; }
	default CustomPermission asCustomPermission() { return null; }
}