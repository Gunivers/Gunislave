package net.gunivers.gunislave.data.permission;

import discord4j.core.object.util.Permission;

public final class DiscordPermission implements Permissions
{
	private final Permission handle;

	/**
	 * Return the only DiscordPermission associated with the provided discord built-in permission.
	 * @param permission discord's permission
	 * @return the overlay of the provided permission.
	 */
	public static final DiscordPermission of(Permission permission)
	{
		return DISCORD.get(permission);
	}

	public static final DiscordPermission of(String name)
	{
		return DiscordPermission.of(Permission.valueOf(name));
	}

	DiscordPermission(Permission handle)
	{
		this.handle = handle;
	}

	public Permission getHandle() { return this.handle; }

	@Override
	public int getLevel()
	{
		switch (this.handle)
		{
			case MANAGE_CHANNELS: return 2;
			case MANAGE_GUILD: return 3;
			case ADMINISTRATOR: return 4;
			default: return 0;
		}
	}

	@Override public boolean isDiscordPermission() { return true; }
	@Override public DiscordPermission asDiscordPermission() { return this; }
}
