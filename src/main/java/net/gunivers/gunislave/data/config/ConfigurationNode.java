package net.gunivers.gunislave.data.config;

import java.io.Serializable;
import java.util.Optional;

import net.gunivers.gunislave.util.trees.Node;
import net.gunivers.gunislave.util.trees.NodeFactory;

import fr.az.util.parsing.ParsingException;
import fr.az.util.parsing.SimpleParser;

/**
 * This class is a node within a {@linkplain ConfigurationTree}.
 * It is represented by three values:
 * <ul>
 *  <li>parent: this node's parent, or null if this node is the root</li>
 *  <li>name: this node's String identifier, used to retrieve it from the children's map</li>
 *  <li>children: this node's children</li>
 * </ul>
 * @author AZ
 * @see ConfigurationRoot
 */
public class ConfigurationNode extends Node<ConfigurationNode> implements Serializable
{
	private static final long serialVersionUID = 1680337493303538189L;

	protected boolean visible = true;

	/**
	 * Construct a node with the provided parent and name
	 * @param parent this node's parent
	 * @param name this node's String identifier
	 */
	ConfigurationNode(Node<ConfigurationNode> parent, String name)
	{
		this(parent, name, ConfigurationNode::new);
	}

	/**
	 * Construct a node with the provided parent, name and factory.
	 * Mostly used to propagate the factory throughout the tree.
	 * @param parent this node's parent
	 * @param name this node's String identifier
	 */
	ConfigurationNode(Node<ConfigurationNode> parent, String name, NodeFactory<ConfigurationNode> factory)
	{
		super(parent, name, factory);
	}

	/**
	 * Returns a {@linkplain Configuration} as this node's child, or null if a child with this name already exists.
	 * To create a simple configuration node instead, please refer to {@linkplain ConfigurationNode#createChild(String)}
	 * @param name the child's string identifier
	 * @param parser the child's parser
	 * @param type the child's displayed type
	 * @param defaultValue the child's default value
	 * @return the child
	 */
	public <T> Configuration<T> getOrNewConfiguration(String localName, SimpleParser<T, ? extends ParsingException> parser, String type, T value)
	{
		Optional<Configuration<T>> child = this.getConfiguration(localName);

		if (!child.isPresent())
			return new Configuration<>(this.getParent(), localName, parser, type, value);

		return child.get();
	}

	/**
	 * @return this node's child as a configuration, or null if it doesn't exist or is a simple node
	 */
	public <T> Optional<Configuration<T>> getConfiguration(String localName)
	{
		return this.getChild(localName).filter(ConfigurationNode::isConfiguration).map(ConfigurationNode::asConfiguration);
	}

	/**
	 * Return this node as a configuration, or null
	 * @return <code>null</code> by default
	 * @see Configuration#asConfiguration()
	 */
	public <T> Configuration<T> asConfiguration() { return null; }

	/**
	 * Return wether this node is a Configuration
	 * @return <code>false</code> by default
	 * @see Configuration#isConfiguration()
	 */
	public boolean isConfiguration() { return false; }

	/** @return wether this node is visible for the user */
	public boolean isVisible() { return this.visible && this.getParent().cast().isVisible(); }

	/**
	 * Set this node's visibility for the user. While invisible, this node's children are unable to be seen as well
	 * @param visible this node's visibility
	 */
	public void setVisible(boolean visible) { this.visible = visible; }
}
