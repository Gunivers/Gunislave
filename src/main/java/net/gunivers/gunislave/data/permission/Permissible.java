package net.gunivers.gunislave.data.permission;

/**
 * An interface for entities holding permissions.
 * Implements the visitor pattern.
 * @author AZ
 */
public interface Permissible
{
	/**
	 * Grant a {@linkplain DiscordPermission} to this {@linkplain Permissible}
	 * @param permission the permission to grant
	 */
	void grant(DiscordPermission permission);

	/**
	 * Grant a {@linkplain CustomPermission} to this {@linkplain Permissible}
	 * @param permission the permission to grant
	 */
	void grant(CustomPermission permission);

	/**
	 * Revoke a {@linkplain DiscordPermission} from this {@linkplain Permissible}
	 * @param permission the permission to revoke
	 */
	void revoke(DiscordPermission permission);

	/**
	 * Revoke a {@linkplain CustomPermission} from this {@linkplain Permissible}
	 * @param permission the permission to revoke
	 */
	void revoke(CustomPermission permission);

	/**
	 * Grant multiple permissions from this {@linkplain Permissible}
	 * @param permissions an array of {@linkplain Permissions}
	 */
	default void grant(Permissions... permissions) { for (Permissions permission : permissions) permission.grant(this); }

	/**
	 * Grant multiple permissions from this {@linkplain Permissible}
	 * @param permissions an {@linkplain Iterable} of {@linkplain Permissions}
	 */
	default void grant(Iterable<Permissions> permissions) { for (Permissions permission : permissions) permission.grant(this); }

	/**
	 * Revoke multiple permissions from this {@linkplain Permissible}
	 * @param permissions an array of {@linkplain Permissions}
	 */
	default void revoke(Permissions... permissions) { for (Permissions permission : permissions) permission.revoke(this); }

	/**
	 * Revoke multiple permissions from this {@linkplain Permissible}
	 * @param permissions an {@linkplain Iterable} of {@linkplain Permissions}
	 */
	default void revoke(Iterable<Permissions> permissions) { for (Permissions permission : permissions) permission.revoke(this); }
}
