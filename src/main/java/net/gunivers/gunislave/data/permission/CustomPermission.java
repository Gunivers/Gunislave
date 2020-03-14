package net.gunivers.gunislave.data.permission;

public class CustomPermission extends PermissionNode implements Permissions
{
	private static final long serialVersionUID = 2065849983131455447L;

	private final int level;

	CustomPermission(PermissionNode parent, String localName, int level)
	{
		super(parent, localName);
		this.level = level;
	}

	/** @return the level of this permission. */
	@Override public int getLevel() { return this.level; }

	@Override public boolean isCustomPermission() { return true; }
	@Override public CustomPermission asCustomPermission() { return this; }
}
