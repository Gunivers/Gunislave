package net.gunivers.gunislave.data;

import java.util.function.BiFunction;
import java.util.function.Function;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Result;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import io.r2dbc.spi.Statement;
import io.r2dbc.spi.ValidationDepth;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Queries
{
	// ╔═════════════════════════╗
	// ║ DATABASE REACTIVE UTILS ║
	// ╚═════════════════════════╝

	public static Mono<Void> close(AutoCloseable closeable)
	{
		return Mono.fromCallable(() -> { closeable.close(); return null; });
	}

	public static Mono<Result> query(Function<Connection, Statement> query)
	{
		return Database.connection().flatMap
		(
			connection -> Mono.from(query.apply(connection).execute()).flatMap
			(
				result ->
					Mono.from(connection.validate(ValidationDepth.LOCAL))
						.filter(validate -> validate)
						.flatMap(validate -> Mono.when(connection.close()))
						.thenReturn(result)
			)
		);
	}

	public static <T> Mono<T> query(Function<Connection, Statement> query, BiFunction<Row, RowMetadata, T> mapper)
	{
		return Queries.query(query).flatMap(r -> Mono.from(r.map(mapper)));
	}

	public static <T> Flux<T> queryMany(Function<Connection, Statement> query, BiFunction<Row, RowMetadata, T> mapper)
	{
		return Queries.query(query).flatMapMany(r -> r.map(mapper));
	}}
