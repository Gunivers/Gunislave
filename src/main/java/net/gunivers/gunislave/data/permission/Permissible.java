package net.gunivers.gunislave.data.permission;

import java.util.Collection;
import java.util.HashMap;

public interface Permissible
{
	HashMap<?, ?> MAP = new HashMap<Object, Object>()
	{
		private static final long serialVersionUID = 6304756077958269146L;

	};

	void setPermissions(Collection<Permissions> perms);
	Collection<Permissions> getPermissions();
	void recalculatePermissions();
}
