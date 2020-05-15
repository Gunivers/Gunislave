package net.gunivers.gunislave.data.permission;

import net.gunivers.gunislave.util.trees.Node;

/**
 * Represents a permission custom-tailored by the bot.
 * @author AZ
 */
public class CustomPermission extends PermissionNode implements Permissions
{
	private static final long serialVersionUID = 2065849983131455447L;

	private final int level;

	CustomPermission(Node<PermissionNode> parent, String localName, int level)
	{
		super(parent, localName);
		this.level = level;
	}

	@Override public void grant(Permissible holder) { holder.grant(this); }
	@Override public void revoke(Permissible holder) { holder.revoke(this); }

	/** @return the level of this permission. */
	@Override public int getLevel() { return this.level; }

	/** @return true */
	@Override public boolean isCustomPermission() { return true; }
	/** @return this */
	@Override public CustomPermission asCustomPermission() { return this; }
}
