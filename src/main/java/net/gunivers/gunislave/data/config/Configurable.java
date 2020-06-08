package net.gunivers.gunislave.data.config;

import java.io.Serializable;

/**
 * An interface which represents a object holding configuration.
 * @author AZ
 * @see Configuration
 * @see ConfigurationNode
 */
public interface Configurable extends Serializable
{
	/**
	 * @return this Configurable's configuration as a {@linkplain WrappedConfiguration}.
	 */
	WrappedConfiguration getConfiguration();
}
