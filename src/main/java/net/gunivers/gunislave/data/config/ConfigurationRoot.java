package net.gunivers.gunislave.data.config;

import java.util.Map;

import net.gunivers.gunislave.util.trees.RootNode;

/**
 * {@linkplain RootNode} implementation for configuration trees.
 * @author AZ
 */
public final class ConfigurationRoot extends RootNode<ConfigurationNode>
{
	private static final long serialVersionUID = 1590375268848132111L;

	ConfigurationRoot(String name)
	{
		super(name, ConfigurationNode::new);
	}

	ConfigurationRoot(String name, Map<String, ConfigurationNode> children)
	{
		super(name, children, ConfigurationNode::new);
	}
}
