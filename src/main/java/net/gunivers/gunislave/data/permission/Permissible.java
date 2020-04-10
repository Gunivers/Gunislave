package net.gunivers.gunislave.data.permission;

import java.util.Collection;

public interface Permissible
{
	void setPermissions(Collection<Permissions> perms);
	Collection<Permissions> getPermissions();
	void recalculatePermissions();
}
