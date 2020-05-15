package net.gunivers.gunislave.util.trees;

/**
 * A NodeFactory is used in trees made of {@linkplain Node} for instanciation purposes, letting the tree implementation decide on a
 * node creation behavior.
 *
 * In most cases, the same factory is propagated throughout the tree, but this isn't mandatory and some nodes might produce their children
 * with a different factory from their parent.
 *
 * @author AZ
 *
 * @param <N> the core {@linkplain Node} implementation in a tree.
 */
public interface NodeFactory<N extends Node<N>>
{
	/**
	 * This method is called whenever the parent node creates a child, instanciation being delegated to this factory.
	 * <br>A {@linkplain NodeFactory} may produce null children, but such behavior is deprecated and would results in incompletion with
	 * methods such as {@linkplain Node#makePath(String)}, or null return in {@linkplain Node#getOrNewChild(String)}}
	 *
	 * @param parent the parent requesting a child.
	 * @param localName the local name of the child
	 * @return the generated child node.
	 * @see Node#getLocalName()
	 */
	N produce(Node<N> parent, String localName);
}
