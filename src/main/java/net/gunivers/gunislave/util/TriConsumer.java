package net.gunivers.gunislave.util;

import java.util.Objects;

/**
 * A {@linkplain java.util.function.Consumer Consumer}, but accepting three values instead of one.
 * @param <T>
 * @param <U>
 * @param <V>
 */
@FunctionalInterface
public interface TriConsumer<T, U, V>
{
	void accept(T t, U u, V v);

	default TriConsumer<T, U, V> andThen(TriConsumer<T, U, V> then)
	{
		Objects.requireNonNull(then, "Cannot compose with null TriConsumer");
		return (t, u, v) -> { this.accept(t, u, v); then.accept(t, u, v); };
	}
}
