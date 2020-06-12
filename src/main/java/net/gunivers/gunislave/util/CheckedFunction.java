package net.gunivers.gunislave.util;

/**
 * An alternative to {@linkplain java.util.function.Function} with support for checked exceptions.
 * @author A~Z
 *
 * @param <A> the input type
 * @param <B> the output type
 * @param <T> the checked exception
 */
@FunctionalInterface
public interface CheckedFunction<A, B, T extends Throwable>
{
	B apply(A input) throws T;
}
