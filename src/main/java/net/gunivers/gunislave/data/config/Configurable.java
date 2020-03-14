package net.gunivers.gunislave.data.config;

import java.io.Serializable;

/**
 * An interface which represents a configurable Object.
 * @author AZ
 * @see Configuration
 * @see ConfigurationNode
 */
public interface Configurable extends Serializable
{
	/**
	 * @return this Object's configuration nicely wrapped because of accessibility issues
	 */
	WrappedConfiguration getConfiguration();
}
