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