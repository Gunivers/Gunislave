package net.gunivers.gunislave.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

import net.gunivers.gunislave.util.CheckedFunction;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class Queries
{
	public Mono<Void> save()
	{
		return Mono.empty();
	}

	//-----------------------
	//DATABASE REACTIVE UTILS
	//-----------------------

	public static <C extends AutoCloseable> Mono<Void> close(C closeable)
	{
		return Mono.fromCallable(() -> { closeable.close(); return null; });
	}

	public static <T> Mono<T> queryBase(
			CheckedFunction<Connection, PreparedStatement, SQLException> query,
			Function<PreparedStatement, ? extends Mono<T>> resultProvider)
	{
		return Mono.usingWhen
		(
			Database.connection(),
			(Connection connection) -> Mono.usingWhen
			(
				Mono.fromCallable(() -> query.apply(connection)),
				resultProvider,
				Queries::close
			),
			Queries::close
		).subscribeOn(Schedulers.elastic());
	}

	public static <T> Mono<T> query(
			CheckedFunction<Connection, PreparedStatement, SQLException> query,
			CheckedFunction<PreparedStatement, T, SQLException> result)
	{
		return Queries.queryBase(query, statement -> Mono.fromCallable(() -> result.apply(statement)));
	}

	public static <T> Mono<T> query(
			CheckedFunction<Connection, PreparedStatement, SQLException> query,
			CheckedFunction<PreparedStatement, ResultSet, SQLException> result,
			CheckedFunction<ResultSet, T, SQLException> mapper)
	{
		return Queries.queryBase
		(
			query,
			statement -> Mono.usingWhen
			(
				Mono.fromCallable(() -> result.apply(statement)),
				(ResultSet resultSet) -> Mono.fromCallable(() -> mapper.apply(resultSet)),
				Queries::close
			)
		);
	}
}
