package net.gunivers.gunislave.util.trees;

public interface NodeFactory<N extends Node<N>>
{
	N produce(Node<N> node, String localName, NodeFactory<N> factory);
}
