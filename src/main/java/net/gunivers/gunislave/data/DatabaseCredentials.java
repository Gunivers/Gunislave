package net.gunivers.gunislave.data;

import java.util.function.Predicate;

public record DatabaseCredentials(String user, String host, int port, String password, String database)
{
	public DatabaseCredentials(String user, String host, String port, String password, String database)
	{
		this
		(
			user,
			host,
			Integer.parseInt
			(
				DatabaseCredentials.check
				(
					"db-port",
					DatabaseCredentials.check("db-port", port),
					p -> p.chars().anyMatch(c -> c < '0' || c > '9')
				)
			),
			password,
			database
		);
	}

	public DatabaseCredentials
	{
		DatabaseCredentials.check("db-user", user);
		DatabaseCredentials.check("db-host", user);
		DatabaseCredentials.check("db-port", port);
		DatabaseCredentials.check("db-pass", password);
		DatabaseCredentials.check("db-name", database);
	}

	private static int check(String name, int value)
	{
		return DatabaseCredentials.check(name, value, v -> v < 0 || v > 65_535);
	}

	private static String check(String name, String value)
	{
		return DatabaseCredentials.check(name, value, v -> v == null || v.isBlank());
	}

	private static <T> T check(String name, T value, Predicate<T> error)
	{
		if (error.test(value))
			throw new IllegalArgumentException("Empty or invalid credential provided for argument '%s'".formatted(name));

		return value;
	}
}
