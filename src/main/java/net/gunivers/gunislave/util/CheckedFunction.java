package net.gunivers.gunislave.util;

import java.util.function.Function;

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

	default <Z> CheckedFunction<Z, B, T> compose(CheckedFunction<Z, ? extends A, ? extends T> function) {
		return input -> this.apply(function.apply(input)); }

	default <Z> CheckedFunction<Z, B, T> compose(Function<Z, ? extends A> function) {
		return input -> this.apply(function.apply(input)); }

	default <C> CheckedFunction<A, C, T> andThen(CheckedFunction<? super B, C, ? extends T> function) {
		return input -> function.apply(this.apply(input)); }

	default <C> CheckedFunction<A, C, T> andThen(Function<? super B, C> function) {
		return input -> function.apply(this.apply(input)); }

	static <A, T extends Throwable> CheckedFunction<A, A, T> identity() { return a -> a; }
}
